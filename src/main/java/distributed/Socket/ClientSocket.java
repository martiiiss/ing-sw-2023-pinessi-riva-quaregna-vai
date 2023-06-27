package distributed.Socket;

import distributed.messages.Message;
import distributed.messages.SocketMessage;
import model.*;
import util.Cord;
import util.Event;
import view.GUI.GUIView;
import view.UserView;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.UnmarshalException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.Event.*;
import static util.Event.GAME_PIT;

public class ClientSocket {
    private String address;
    private int port;
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ExecutorService executorService;
    private Object serverObj;
    private int myIndex;
    private int myMatch;
    private UserView uView;
    private int viewChosen;
    private String nickname;
    private boolean hasGameStarted = false;
    private Board board;
    private ArrayList<Player> listOfPlayers;
    private ArrayList<CommonGoalCard> commonGoalCard;
    private PersonalGoalCard myPersonalGoalCard;
    private int indexOfPIT;
    private Player playerInTurn;
    private Thread threadWaitTurn;
    private Object lock;
    private boolean disabledInput;
    private boolean isFirstTurn = true;
    private int numberOfChosenTiles;
    private ArrayList<Cord> cords = new ArrayList<>();
    private int column;
    private GUIView gui;
    private ArrayList<Cord> tilesCords = new ArrayList<>();
    private ArrayList<Tile> tilesInHand;
    private boolean isYourTurn;
    private boolean active = false;
    private int choice=0;
    private int count=0;

    public ClientSocket(String address, int port) throws IOException {
        this.port = port;
        this.address = address;
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address, port), 1000);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.myIndex = -1;
        this.myMatch = -1;
    }


    public void lobby(UserView userView) throws IOException, ClassNotFoundException {
        uView = userView;
        this.lock = new Object();

        Thread clientThread = new Thread(()-> {
          executorService.execute(()->{
              while(!executorService.isShutdown()){
                  try{
                      System.out.println("attendo messaggio...");
                      SocketMessage message = receivedMessageC();
                      System.out.println("ho ricevuto " + message.getMessageEvent());
                      update(message);
                  } catch (IOException | ClassNotFoundException | InterruptedException e) {
                      throw new RuntimeException(e);
                  }
                  //notifyObservers(message);
              }
              System.out.println("esco");
          });
      });

      clientThread.start();
    }

    public void update(SocketMessage message) throws IOException, ClassNotFoundException, InterruptedException {
        switch (message.getMessageEvent()){
            case SET_CLIENT_INDEX ->{
                this.myIndex = message.getClientIndex();
                this.myMatch = message.getMatchIndex();
                update(new SocketMessage(myIndex, myMatch, null, CHOOSE_VIEW));
            }
            case CHOOSE_VIEW -> {
                if(message.getObj()!=null){
                    System.out.println(((Event)message.getObj()).getMsg());
                }
                this.viewChosen = uView.userInterface();
                sendMessageC(new SocketMessage(myIndex, myMatch, this.viewChosen, CHOOSE_VIEW));
            }
            case SET_NICKNAME -> {
                if(message.getObj()!=null){
                    System.out.println(((Event)message.getObj()).getMsg());
                }
                this.nickname = uView.askPlayerNickname();
                sendMessageC(new SocketMessage(myIndex, myMatch, this.nickname, SET_NICKNAME));
            }
            case ALL_CONNECTED -> {
                System.out.println("Starta il thread");
                //startThread();
                getModel();
                //wait(500);
                update(new SocketMessage(myIndex, myMatch, null, START));
            }
            case START, CHECK_MY_TURN -> {
                sendMessageC(new SocketMessage(myIndex, myMatch, CHECK_MY_TURN, GAME_PIT ));
            }
            case START_YOUR_TURN -> {
                active = true;
                if(viewChosen==1){
                    if(!isFirstTurn)
                        getModel();
                    isFirstTurn = false;
                    playerInTurn = listOfPlayers.get(myIndex);
                    activePlayerMenu();
                    System.out.println("Here's your Bookshelf:");
                    uView.showTUIBookshelf(listOfPlayers.get(myIndex).getMyBookshelf());
                    uView.showTUIBoard(this.board);

                    activeAskNumOfTiles();

                } if(viewChosen==2){
                    System.out.println("MIO TURNO ");
                    gui.showError(START_YOUR_TURN);
                    flowGui();
                    System.out.println("next");
                }
            }
            case NOT_YOUR_TURN -> {
                active = false;
                if(viewChosen==1){
                    passivePlay();
                } else if(viewChosen==2){

                }
            }
            case GAME_BOARD -> {
                this.board = (Board) message.getObj();
                System.out.println("act " + active);
                if(!active) {
                    System.out.println("Here's the game board...");
                    uView.showTUIBoard(board);
                }
                passivePlay();
            }
            case GAME_PLAYERS -> {
                this.listOfPlayers = (ArrayList<Player>) message.getObj();
                System.out.println("choice " +choice + "active"+ active);
                if(!active) {
                    if(choice==4){
                        for (Player player : listOfPlayers) {
                            System.out.println("\u001B[36m" + player.getNickname() + "\u001B[0m's bookshelf:");
                            uView.showTUIBookshelf(player.getMyBookshelf());
                        }
                        passivePlay();
                    } else if(choice==6){
                        this.listOfPlayers = (ArrayList<Player>) message.getObj();
                        uView.showPlayers(listOfPlayers);
                        passivePlay();
                    }
                } else if(active){
                    this.listOfPlayers = (ArrayList<Player>) message.getObj();
                    uView.showPlayers(listOfPlayers);
                }
            }
            case TURN_AMOUNT -> {
                if(message.getObj()!=null) {
                    if (message.getObj() != OK) {
                        System.out.println(((Event) message.getObj()).getMsg());
                        activeAskNumOfTiles();
                    } else {
                        System.out.println("ACTIVE ASK TILE");
                        activeAskTiles();
                    }
                }
            }
            case TURN_PICKED_TILES -> {
                if(message.getObj()!= OK) {
                    System.out.println(((Event) message.getObj()).getMsg());
                    activeAskTiles();
                } else {
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, TURN_TILE_IN_HAND));
                }
            }
            case TURN_TILE_IN_HAND -> {
                tilesInHand = (ArrayList<Tile>) message.getObj();
                System.out.println(tilesInHand);
                uView.printTilesInHand(tilesInHand);
                uView.showTUIBookshelf(listOfPlayers.get(myIndex).getMyBookshelf());
                activeAskColumn((ArrayList<Tile>) message.getObj());

            }
            case TURN_COLUMN -> {
                if (message.getObj() == OK) {
                    activePlaceTile(tilesInHand);
                }else {
                    System.out.println(((Event) message.getObj()).getMsg());
                    activeAskColumn(tilesInHand);
                }
            }
            case TURN_POSITION -> {
                System.out.println("ogg" + message.getObj());
                if(message.getObj()!=OK){
                    System.out.println(((Event)message.getObj()).getMsg());
                    activePlaceTile(tilesInHand);
                } else if(count<numberOfChosenTiles){
                    count++;
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, UPDATE_BOOKSHELF));
                }
            } case UPDATE_BOOKSHELF -> {
                System.out.println("count" + count);
                if(count<=numberOfChosenTiles){
                    System.out.println("aggiorno la bookshelf ");
                    Bookshelf bookshelf = (Bookshelf) message.getObj();
                    playerInTurn.setMyBookshelf(bookshelf);
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GET_TILES_HAND));
                }
            }
            case GET_TILES_HAND -> {
                uView.printTilesInHand((ArrayList<Tile>) message.getObj());
                uView.showTUIBookshelf(playerInTurn.getMyBookshelf());

                if(count<numberOfChosenTiles) {
                    activePlaceTile((ArrayList<Tile>) message.getObj());
                }else {
                    count = 0;
                    sendMessageC(new SocketMessage(myIndex, myMatch, null, CHECK_REFILL));
                }

            }
            case CHECK_REFILL -> {
                if (message.getObj() == REFILL)
                    System.out.println("\u001B[35mThe board had to be refilled and is now ready for the next turn...\u001B[0m");
                sendMessageC(new SocketMessage(myIndex, myMatch, null, END_OF_TURN));

            }
            case END_OF_TURN -> {
                System.out.println(((Event)(message.getObj())).getMsg());
                getModel();
                sendMessageC(new SocketMessage(myIndex, myMatch, myIndex, CHECK_MY_TURN));
            }
        }
    }

    public void sendMessageC(SocketMessage mess) throws IOException {
        try{
            //invio messaggio:
            outputStream.writeObject(mess);
            outputStream.flush();
            outputStream.reset();
        }catch (IOException e){
            //TODO disconnessione
            //notifyObserver con messaggio di errore
        }
    }

    public SocketMessage receivedMessageC() throws IOException, ClassNotFoundException {
        SocketMessage message = (SocketMessage) inputStream.readObject();
        return message;
    }

    public void getModel() throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println("model");
        disabledInput = false;
        if(!hasGameStarted) {
            System.out.println("game non iniziato");
            sendMessageC(new SocketMessage(this.myIndex, this.myMatch, null, GAME_STARTED));
            while (receivedMessageC().getObj()!=Event.OK) {
                sendMessageC(new SocketMessage(this.myIndex, this.myMatch, null, GAME_STARTED));
            }
            hasGameStarted = true;
        }
        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_BOARD));
        this.board = (Board) receivedMessageC().getObj();
        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PLAYERS));
        this.listOfPlayers = (ArrayList<Player>) receivedMessageC().getObj();
        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_CGC));
        this.commonGoalCard = (ArrayList<CommonGoalCard>) receivedMessageC().getObj();
        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PGC));
        this.myPersonalGoalCard = (PersonalGoalCard) receivedMessageC().getObj();
        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PIT));
        this.indexOfPIT = (int) receivedMessageC().getObj();
        this.playerInTurn = listOfPlayers.get(indexOfPIT);

        System.out.println("ho il model ora devo startare la partita!");

        if(viewChosen==2) {
            if (this.isFirstTurn) {
                gui = new GUIView();
                sendMessageC(new SocketMessage(myIndex, myMatch, gui, ADD_OBSERVER));
                this.isFirstTurn = false;

                gui.updateBoard(this.board);
                sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_CGC));
                this.commonGoalCard = (ArrayList<CommonGoalCard>) receivedMessageC().getObj();
                gui.setupCGC(commonGoalCard.get(0));
                gui.setupCGC(commonGoalCard.get(1));
                sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PGC));
                this.myPersonalGoalCard = (PersonalGoalCard) receivedMessageC().getObj();
                gui.setupPGC(this.myPersonalGoalCard.getNumber());
                System.out.println("fine setup gui");
            }
        }
/*
        if(viewChosen==2){
            if (myIndex == indexOfPIT) {
                System.out.println("MIO TURNO ");
                gui.showError(START_YOUR_TURN);
                flowGui();
                System.out.println("next");
               // getModel();
            } else {
                System.out.println("non è il mio turno");
                gui.showError(NOT_YOUR_TURN);
                synchronized (lock) {
                    this.lock.notifyAll();
                }

                if(!threadWaitTurn.isAlive()){
                    threadWaitTurn.start();
                }
                Event status = Event.WAIT;
                do {
                    try {
                        sendMessageC(new SocketMessage(myIndex, myMatch, myIndex, CHECK_MY_TURN));
                        status = (Event) receivedMessageC().getObj();
                        sendMessageC(new SocketMessage(myIndex, myMatch, board, SET_UP_BOARD));
                        Event e = (Event) receivedMessageC().getObj();
                        if(e==SET_UP_BOARD) {
                            sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_BOARD));
                            this.board = (Board) receivedMessageC().getObj();
                            gui.update(board, new Message(board, SET_UP_BOARD));
                        }
                    } catch (SocketException | UnmarshalException ex) {}
                } while (status != Event.OK);

                System.out.println("new");
                getModel();
            }
        }
        */

    }

    private void activePlayerMenu() throws IOException, ClassNotFoundException {
        System.out.println("Would you like to do any of these actions before making your move?");
        do{
            choice = uView.askAction();
            switch (choice) {
                case 1 -> {
                    System.out.println("Here's the game board...");
                    uView.showTUIBoard(board);
                }
                case 2 -> {
                    System.out.println("Here are the CommonGoalCards...");
                    uView.showCGC(commonGoalCard);
                }
                case 3 -> {
                    System.out.println("Here's your PersonalGoalCard (Shhh don't tell anyone!)");
                    uView.showPGC(listOfPlayers.get(myIndex).getPersonalGoalCard());
                }
                case 4 -> {
                    System.out.println("Here's everyone's Bookshelf");
                    for (Player player : listOfPlayers) {
                        System.out.println("\u001B[36m" + player.getNickname() + "\u001B[0m's bookshelf:");
                        uView.showTUIBookshelf(player.getMyBookshelf());
                    }
                }
                case 5 -> uView.chatOptions(listOfPlayers.get(myIndex));
                case 7 -> uView.showPlayers(listOfPlayers);
            }
            if (choice != 6)
                System.out.println("What else would you like to do?");
        } while (choice != 6);
    }

    private void activeAskNumOfTiles() throws IOException, ClassNotFoundException {
        do{
            this.numberOfChosenTiles = uView.askNumberOfChosenTiles();
            if(this.numberOfChosenTiles!=-1){
                sendMessageC(new SocketMessage(myIndex, myMatch, numberOfChosenTiles, TURN_AMOUNT));
            } else{
                System.out.println(Event.OUT_OF_BOUNDS.getMsg());
            }
        } while(this.numberOfChosenTiles==-1);
    }

    private void activeAskTiles() throws IOException, ClassNotFoundException {
        chooseTiles();
        sendMessageC(new SocketMessage(myIndex, myMatch, cords, TURN_PICKED_TILES));
    }

    public void chooseTiles() throws IOException {
        cords.removeAll(cords);
        while (cords.size() < this.numberOfChosenTiles) {
            Cord cord = new Cord();
            do {
                String in = uView.askTilePosition();
                try {
                    String[] splittedStr = in.split(",");
                    cord.setCords(Integer.parseInt(splittedStr[0]), Integer.parseInt(splittedStr[1]));
                } catch (NumberFormatException formatException) {
                    System.err.println("Invalid format...");
                } catch (ArrayIndexOutOfBoundsException boundsException) {
                    System.err.println("Invalid format or non existent coordinate...");
                }
            } while (cord.getRowCord() == 0 && cord.getColCord() == 0);
            cords.add(cord);
        }
    }

    private void activeAskColumn(ArrayList<Tile> tilesInHand) throws IOException, ClassNotFoundException {
        column = uView.askColumn();
        sendMessageC(new SocketMessage(myIndex, myMatch, column, TURN_COLUMN));
    }

    private void activePlaceTile(ArrayList<Tile> tilesInHand) throws IOException, ClassNotFoundException {
        int pos = uView.askTileToInsert(tilesInHand);
        sendMessageC(new SocketMessage(myIndex, myMatch, pos, TURN_POSITION));
    }

    private void passivePlay() throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("It's not your turn, here are some actions you can do!");
        active=false;
        do {
            passivePlayerMenu();
            System.out.println("choice " + choice);
        } while (!active && choice!=1 && choice!=4 && choice!=6);
        System.out.println("ciao");
        //getModel();
    }
    private void passivePlayerMenu() throws IOException, ClassNotFoundException {
        uView.askPassiveAction();
        choice = uView.waitInput();
        System.out.println("choice " + choice);
        if (!disabledInput) {
            switch (choice) {
                case 1 -> {
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_BOARD));
                }
                case 2 -> {
                    System.out.println("Here are the CommonGoalCards...");
                    uView.showCGC(commonGoalCard);
                }
                case 3 -> {
                    System.out.println("Here's your PersonalGoalCard (Shhh don't tell anyone!)");
                    uView.showPGC(listOfPlayers.get(myIndex).getPersonalGoalCard());
                }
                case 4 -> {
                    System.out.println("Here's everyone's Bookshelf");
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PLAYERS));
                }
                case 5 -> {
                    uView.chatOptions(listOfPlayers.get(myIndex));
                }
                case 6 -> {
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PLAYERS));
                }
            }
        }
    }

    public void flowGui() throws IOException, InterruptedException, ClassNotFoundException {
        int tilesToPick;
        SocketMessage message;
        do {
            tilesToPick = gui.askTiles(); //ask number of tiles
            sendMessageC(new SocketMessage(myIndex, myMatch, tilesToPick, TURN_AMOUNT));
            message = receivedMessageC();
            gui.showError((Event) message.getObj());
        } while (message.getObj() != Event.OK);


        Event errorReceived;
        do {
            gui.getBoardView().setTilesPicked(tilesToPick);
            tilesCords = gui.getTilesClient();
            sendMessageC(new SocketMessage(myIndex, myMatch, tilesCords, TURN_PICKED_TILES));
            errorReceived = (Event) receivedMessageC().getObj();
            System.out.println(errorReceived.getTUIMsg());
            gui.showError(errorReceived);
        } while (errorReceived != Event.OK);

        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, TURN_TILE_IN_HAND));
        tilesInHand = (ArrayList<Tile>) receivedMessageC().getObj();

        gui.pickTiles(tilesCords, tilesInHand); //adds the tile to tiles in hand

        do {
            int column = gui.chooseColumn();
            sendMessageC(new SocketMessage(myIndex, myMatch, column, TURN_COLUMN));
            errorReceived = (Event) receivedMessageC().getObj();
            System.out.println("errore colonna: " + errorReceived);
            gui.showError(errorReceived);
        }while(errorReceived!=Event.OK);

        for(int i=0; i<tilesToPick; i++){
            int pos = gui.chooseTile();
            gui.addTile(tilesInHand.get(pos));
            sendMessageC(new SocketMessage(myIndex, myMatch, pos, TURN_POSITION));
            errorReceived = (Event) receivedMessageC().getObj();
        }
        gui.endInsertion();
        gui.getHandView().setTileToInsert(-1);

        //FIXME sistemare i seguenti (fine)
        sendMessageC(new SocketMessage(myIndex, myMatch, null, CHECK_REFILL));
        errorReceived = (Event) receivedMessageC().getObj();
        if (errorReceived == Event.REFILL) {
            sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_BOARD));
            this.board = (Board) receivedMessageC().getObj();
            gui.update(board,new Message(board,SET_UP_BOARD));
            gui.showError(errorReceived);
        }
        sendMessageC(new SocketMessage(myIndex, myMatch, null, END_OF_TURN));
        errorReceived = (Event) receivedMessageC().getObj();
        if(errorReceived != OK)
            gui.showError(errorReceived);
        if (errorReceived == GAME_OVER) {
            gui.results(listOfPlayers.get(myIndex).getNickname(),listOfPlayers.get(myIndex).getScore());
            wait(10000); //FIXME: Lancia la IllegalMonitorStateException. Da capire come gestire il fine partita (si chiude da solo dopo un tot?)
            System.exit(10);
        }
    }

    public int getMyIndex(){
        return myIndex;
    }

    public int getMyMatch(){
        return myMatch;
    }


    public void closeConnection(){
        //TODO implement this
    }

    /**
     *
     */
    public void disconnected() {

    }



    public void disconnect(){
        //TODO implement this
    };


    public void setMatchIndex(int i){
        this.myMatch = i;
    }

    public void setMyIndex(int i){
        this.myIndex = i;
    }
}
