package controller;

import model.*;
import util.Cord;
import util.Event;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;


import static util.Event.*;

public class Controller  {
    private Game game;
    private Bag bag;
    private Board board;
    private int chosenColumn;
    private int numberOfChosenTiles;
    private int protocol = 0;
    private ArrayList<Player> finalRank;
    private ArrayList<Tile> playerHand;
    //private Event nextEvent;

    public Controller() throws IOException {
        createGame();
    }

    //Before the game actually starts
    public void createGame() {
        //this.nextEventPlayer = new ArrayList();
        this.game = new Game(); /*I create the Game object */
//         chooseNumOfPlayer();
    }


    public Game getInstanceOfGame(){
        return this.game;
    }

    //this method needs to be fixed -> multithreading TODO
    public Event chooseNickname(String nickname) {
        if(nickname.isEmpty()) { //control for the first player
            return Event.EMPTY_NICKNAME;
        }
        //control for the others
        for(int i = 0; i<game.getPlayers().size();i++) {
            if(game.getPlayers().get(i).getNickname().equals(nickname)){
                return Event.NOT_AVAILABLE;
            }
        }
        Player newPlayer = new Player();
        newPlayer.setNickname(nickname);
        game.addPlayer(newPlayer);
        return Event.OK;
    }

    public Event chooseUserInterface(int chosenInterface) {
        switch (chosenInterface) {
            case 1 -> {
                return TUI_VIEW;
            }
            case 2 -> {
                return GUI_VIEW;
            }
            default -> {
                return Event.INVALID_VALUE;
            }
        }
    }

    /**this method initializes the Game after all the players are connected*/
    public void initializeGame() throws RemoteException {
        this.bag = new Bag();
        this.board = new Board(game.getNumOfPlayers());
       // while(!countPlayers()){/*we make the server wait*/}
        for(int i=0;i<game.getNumOfPlayers();i++){
            game.getPlayers().get(i).setMyBookshelf(new Bookshelf());//the method of player creates a new Bookshelf
        }
        game.assignPersonalGoalCard(game.getNumOfPlayers());
        game.setCommonGoalCards();
        board.setNumOfCells(game.getNumOfPlayers());
        ArrayList<Tile> tiles = bag.getBagTiles(board.getNumOfCells());
        board.setUpBoard(tiles);//filled the board
        game.getPlayers().get(0).setAsFirstPlayer();
        game.setPlayerInTurn(game.getPlayers().get(0));
        game.setGameStarted();
    }

    //button START GAME somewhere

    public void checkBoardToBeFilled(){
        if(board.checkBoardStatus()){ //true if board need to be filled
            for(int r=0;r<9;r++){
                for(int c=0;c<9;c++){
                    if(board.getBoard()[r][c].getType()!=Type.NOTHING && board.getBoard()[r][c].getType()!=Type.BLOCKED){
                        bag.addTile(board.removeTile(r,c)); //I add the tiles that are on the board to the bag
                    }
                }
            }
            //I check if inside the bag I have enough Tiles
            if(bag.getTilesContained().size()>= board.getNumOfCells()){
                //I have enough tiles -> fill board
                board.setUpBoard(bag.getBagTiles(board.getNumOfCells()));
            } else {
                //I don't have enough tiles, I add to the board only the tiles I have inside the bag
                board.setUpBoard(bag.getBagTiles(bag.getTilesContained().size()));
            }
        }
    }


    private Event numOfChosenTiles(int numberOfChosenTiles) {
        int freeSlots = game.getPlayerInTurn().getMyBookshelf().getNumOfFreeSlots();
        if (numberOfChosenTiles<1 || numberOfChosenTiles>3)
            return Event.OUT_OF_BOUNDS;
        if(freeSlots < numberOfChosenTiles )
            return Event.INVALID_VALUE;

        int temp=0, cont=0;
        if(numberOfChosenTiles!=1) {
            for (int i = 0; i < board.BOARD_ROW; i++) {
                for (int j = 0; j < board.BOARD_COLUMN; j++) {
                    Cord cord = new Cord();
                    cord.setCords(i, j);
                    cont = 0;
                    if(isTileFreeTile(cord)) {
                        while (cont < 2 && board.getBoard()[i][j].getType() != Type.BLOCKED && board.getBoard()[i][j].getType() != Type.NOTHING) {
                            int e;
                            temp = 0;
                            for (e = 1; e <= numberOfChosenTiles; e++) {
                                if(i - e * (cont - 1)< board.BOARD_ROW && j + e * (cont)< board.BOARD_COLUMN) {
                                    cord.setCords(i - e * (cont - 1), j + e * (cont));
                                    if (board.getBoard()[i - e * (cont - 1)][j + e * (cont)].getType() != Type.BLOCKED && board.getBoard()[i - e * (cont - 1)][j + e * (cont)].getType() != Type.NOTHING && isTileFreeTile(cord)) {
                                        temp = e + 1;
                                    } else {
                                        e = numberOfChosenTiles + 1;
                                    }
                                }
                            }
                            if (temp == numberOfChosenTiles) {
                                this.numberOfChosenTiles = numberOfChosenTiles;
                                return Event.OK;
                            }
                            cont++;
                        }
                    }
                }
            }
            return Event.INVALID_VALUE;
        } else {
            this.numberOfChosenTiles = numberOfChosenTiles;
        }
        return Event.OK;
    }

    //this method will work together with the view, maybe showing the player which tiles can be chosen
    //first number chosen is the column, the second is the row
    //FIXME: Si possono pescare tiles dappertutto, non viene controllato se è disponibile
    //FIXME: Va avanti all'infinito se la scelta è diversa da 1
    private ArrayList<Cord> playerCords = new ArrayList<>();
    public Event chooseTiles(ArrayList<Cord> cords) {
        for(Cord cord : cords)
            if(cord.getRowCord()>8 || cord.getRowCord()<0 || cord.getColCord()>8 || cord.getColCord()<0)
                return Event.OUT_OF_BOUNDS;
        for(int i=0; i<cords.size();i++)
            for (int j=i+1; j<cords.size();j++)
                if(cords.get(i).getRowCord() == cords.get(j).getRowCord() && cords.get(i).getColCord()==cords.get(j).getColCord())
                    return Event.REPETITION;

        for (Cord cord : cords){
            if (board.getSelectedType(cord.getRowCord(), cord.getColCord()) == Type.NOTHING || board.getSelectedType(cord.getRowCord(), cord.getColCord()) == Type.BLOCKED) {
                playerCords.clear();
                return Event.BLOCKED_NOTHING;
            }
            if (!isTileFreeTile(cord)) {
                playerCords.clear();
                return Event.NOT_ON_BORDER;
            }
            if(cords.size()!=1)
                if(!checkAdj(cords))
                    return Event.NOT_ADJACENT;
            playerCords.add(cord);
        }
        if(cords.size()==0 || cords == null){
            return EMPTY;
        }
        return Event.OK;
    }
    //FIXME se qualcuno ha voglia si può notevolmente ottimizzare
    private boolean checkAdj(ArrayList<Cord> cords) {
        boolean sameRow = true;
        boolean sameCol = true;
        Cord currCord = cords.get(1);
        Cord prevCord = cords.get(0);
        if (currCord.getRowCord() != prevCord.getRowCord())
            sameRow = false;
        if(currCord.getColCord()!=prevCord.getColCord())
            sameCol = false;
        ArrayList<Integer> col = new ArrayList<>();
        ArrayList<Integer> row = new ArrayList<>();
        if(cords.size()==3) {
            Cord lastCord = cords.get(2);
            if (currCord.getRowCord() != lastCord.getRowCord() && prevCord.getRowCord() != lastCord.getRowCord())
                sameRow = false;
            if(currCord.getColCord() != lastCord.getColCord() && prevCord.getColCord() != lastCord.getColCord())
                sameCol = false;
        }
        if(sameRow) {
            for (Cord cord : cords)
                col.add(cord.getColCord());
            Collections.sort(col);
            for(int i = 0; i<col.size()-1;i++) {
                System.out.println(col.get(i + 1)+" "+(col.get(i)+1));
                if (col.get(i + 1) != col.get(i) + 1)
                    return false;
            }
        }
        if(sameCol) {
            for (Cord cord : cords)
                row.add(cord.getRowCord());
            Collections.sort(row);
            for(int i = 0; i<row.size()-1;i++)
                if(row.get(i + 1) != row.get(i) +1)
                    return false;
        }
        return true;
    }

    private boolean isTileFreeTile(Cord cord) {
        int x = cord.getRowCord();
        int y = cord.getColCord();
        if(x==0 || x== board.BOARD_ROW-1 || y==0 || y== board.BOARD_COLUMN-1){
            if(x==0 && y== 0 && (board.getSelectedType(x + 1, y) == Type.NOTHING  || board.getSelectedType(x + 1, y) == Type.BLOCKED || board.getSelectedType(x, y+1)==Type.BLOCKED || board.getSelectedType(x, y+1)==Type.NOTHING)){
                return true;
            } else if(x==0 && y==board.BOARD_COLUMN-1 && (board.getSelectedType(x +1, y) == Type.NOTHING  || board.getSelectedType(x + 1, y) == Type.BLOCKED || board.getSelectedType(x, y-1)==Type.BLOCKED || board.getSelectedType(x, y-1)==Type.NOTHING)) {
                return true;
            } else if(x==board.BOARD_ROW-1 && y==0 && (board.getSelectedType(x -1, y) == Type.NOTHING  || board.getSelectedType(x - 1, y) == Type.BLOCKED || board.getSelectedType(x, y+1)==Type.BLOCKED || board.getSelectedType(x, y+1)==Type.NOTHING)) {
                return true;
            } else if(x==board.BOARD_ROW-1 && y==board.BOARD_COLUMN-1 && (board.getSelectedType(x - 1, y) == Type.NOTHING  || board.getSelectedType(x - 1, y) == Type.BLOCKED || board.getSelectedType(x, y-1)==Type.BLOCKED || board.getSelectedType(x, y-1)==Type.NOTHING)) {
                return true;
            } else if(x==0 && (board.getSelectedType(x + 1, y) == Type.NOTHING || board.getSelectedType(x, y + 1) == Type.NOTHING || board.getSelectedType(x, y - 1) == Type.NOTHING
                    || board.getSelectedType(x + 1, y) == Type.BLOCKED || board.getSelectedType(x, y + 1) == Type.BLOCKED || board.getSelectedType(x, y - 1) == Type.BLOCKED)){
                return true;
            } else if(x==board.BOARD_ROW-1 && (board.getSelectedType(x - 1, y) == Type.NOTHING || board.getSelectedType(x, y + 1) == Type.NOTHING || board.getSelectedType(x, y - 1) == Type.NOTHING
                    || board.getSelectedType(x - 1, y) == Type.BLOCKED || board.getSelectedType(x, y + 1) == Type.BLOCKED || board.getSelectedType(x, y - 1) == Type.BLOCKED)){
                return true;
            } else if(y==0 && (board.getSelectedType(x - 1, y) == Type.NOTHING || board.getSelectedType(x, y + 1) == Type.NOTHING || board.getSelectedType(x+1, y) == Type.NOTHING
                    || board.getSelectedType(x - 1, y) == Type.BLOCKED || board.getSelectedType(x, y + 1) == Type.BLOCKED || board.getSelectedType(x+1, y ) == Type.BLOCKED)){
                return true;
            }  else if(y==board.BOARD_COLUMN-1 && (board.getSelectedType(x - 1, y) == Type.NOTHING || board.getSelectedType(x, y -1) == Type.NOTHING || board.getSelectedType(x+1, y) == Type.NOTHING
                    || board.getSelectedType(x - 1, y) == Type.BLOCKED || board.getSelectedType(x, y - 1) == Type.BLOCKED || board.getSelectedType(x+1, y ) == Type.BLOCKED)){
                return true;
            }
        } else{
            if(board.getSelectedType(x + 1, y) == Type.NOTHING || board.getSelectedType(x, y + 1) == Type.NOTHING || board.getSelectedType(x - 1, y) == Type.NOTHING || board.getSelectedType(x, y - 1) == Type.NOTHING
                    || board.getSelectedType(x + 1, y) == Type.BLOCKED || board.getSelectedType(x, y + 1) == Type.BLOCKED || board.getSelectedType(x - 1, y) == Type.BLOCKED || board.getSelectedType(x, y - 1) == Type.BLOCKED)
                return true;
        }
        return false;
    }


    public Event chooseColumn(int chosenColumn) {
        System.out.println("NumOfChosenTiles: "+this.numberOfChosenTiles);
        Tile[][] playerBookshelf = game.getPlayerInTurn().getMyBookshelf().getBookshelf();
        if(chosenColumn<0 || chosenColumn >4)
            return Event.INVALID_VALUE;
        if(playerBookshelf[this.numberOfChosenTiles-1][chosenColumn].getType() != Type.NOTHING)
            return Event.OUT_OF_BOUNDS;
        this.chosenColumn = chosenColumn;
        return Event.OK;
    }

    //after chooseColumn has been invoked
    public Event chooseTilesDisposition(int index) {
        if(index <0 || index >= playerHand.size())
            return Event.INVALID_VALUE;
        Player pit = game.getPlayerInTurn();
        System.out.println(game.getPlayerInTurn().getNickname());
        pit.getMyBookshelf().placeTile(this.chosenColumn,playerHand.get(index));
        playerHand.add(index+1,new Tile(Type.NOTHING,0));
        playerHand.remove(index);
        return Event.OK;
    }

    public void calculateScore(){
        int cgc = game.checkCommonGoalCard();
        int pgc = game.getPlayerInTurn().checkCompletePGC();
        int adjacency = game.getPlayerInTurn().checkAdjacentBookshelf();
        game.getPlayerInTurn().updateScore(cgc+pgc+adjacency);
        System.out.println("SCORE PIT: "+game.getPlayerInTurn().getScore());
    }

    public Event checkIfGameEnd() {
        int index=0;
        if (game.getPlayers().indexOf(game.getPlayerInTurn()) != game.getNumOfPlayers() - 1) { //calculate index
            index = game.getPlayers().indexOf(game.getPlayerInTurn()) + 1;//index of the next player
        }
        if(!game.getIsLastTurn()){
            System.out.println("Hellooo");
            if(game.getPlayerInTurn().getMyBookshelf().getStatus()) {
                game.getPlayerInTurn().updateScore(1);
                System.out.println("Punteggio di quello che ha finito (PIT) incrementato: "+game.getPlayerInTurn().getScore());
                game.setFinisher(game.getPlayerInTurn());
                System.out.println("PIT SCORE: "+game.getPlayerInTurn().getScore());
                if(index==0) //If the PIT is also the first one to finish the game is over instantly
                {
                    System.out.println("PIT ha riempito la shelf ed è quello con index 0. La partita finisce subito");
                    //endOfGame();
                    return GAME_OVER;
                }
                else {
                    goToNext(game.getPlayerInTurn());
                    return LAST_TURN;
                }
            }
            else {
                System.out.println("PIT non ha riempito la Bookshelf");
                goToNext(game.getPlayerInTurn());
                return OK;
            }
        }
        else {
            System.out.println("Siamo già in lastTurn");
            if(index==0) {
                System.out.println("Il pit è il primo, partita finisce");
                //endOfGame();
                return GAME_OVER;
            }
            else {
                System.out.println("il PIT non è il primo quindi non termina");
                goToNext(game.getPlayerInTurn());
                return LAST_TURN;
            }
        }
    }


    public void goToNext(Player playerInTurn){ //set player in turn
        int i = game.getPlayers().indexOf(playerInTurn)+1;
        if(i<game.getNumOfPlayers()){
            game.setPlayerInTurn(game.getPlayers().get(i));
        } else {
            game.setPlayerInTurn(game.getPlayers().get(0));
        }
        System.out.println("NickPIT: "+game.getPlayerInTurn().getNickname());
    }//TODO optimize this


    public void endOfGame() {
        ArrayList<Integer> rankNumber = new ArrayList<>();
        for(int i=0; i<game.getPlayers().size(); i++){
            rankNumber.add(game.getPlayers().get(i).getScore());
        }
        Collections.sort(rankNumber);

        this.finalRank = new ArrayList<>(game.getPlayers());

        for(int i=game.getPlayers().size()-1; i>=0; i--) {
            for(int j=0; j<game.getPlayers().size(); j++){
                if(rankNumber.get(j)!=-1 && game.getPlayers().get(i).getScore() == rankNumber.get(j)){
                    this.finalRank.set(j, game.getPlayers().get(i));
                    rankNumber.set(j, -1);
                    j=5;
                }
            }
        }
        System.out.println("GAME OVER!!!!!!!!!!!");
        System.exit(0);
    }


    public Board getBoard(){ return this.board;}

    public ArrayList<Tile> getTilesFromBoard() {
        ArrayList<Tile> removedTiles = new ArrayList<>();
        for(Cord cord : playerCords) {
            removedTiles.add(board.removeTile(cord.getRowCord(),cord.getColCord()));
        }
        this.playerHand = removedTiles;
        playerCords.clear();
        System.out.println("TEST DELLE TILES NELL HAND: "+removedTiles);
        return removedTiles;
        //TODO: UPDATE OBSERVERS!
    }


    public Event updateController(Object obj, Event event) throws RemoteException {
        Event error = Event.OK;
        switch (event) {
            case ASK_NUM_PLAYERS -> {
                game.setNumOfPlayers((int)obj);
            }
            case SET_NICKNAME -> {
                 error = chooseNickname((String) obj);
            }
            case CHOOSE_VIEW -> {
                error = chooseUserInterface((int) obj);
            }
            case ALL_CONNECTED -> {
                if(game.getNumOfPlayers() == game.getPlayers().size()) {
                    System.out.println("The game is starting");
                    initializeGame();
                    error = Event.OK;
                }
                else
                    error = Event.WAIT;
            }
            case GAME_STARTED -> {
                if(game.getGameStarted())
                    error = Event.OK;
                else
                    error = Event.WAIT;
            }
            case TURN_AMOUNT -> {
                System.out.println("Turn ammount: "+(int) obj);
                error = numOfChosenTiles((int) obj);
            }
            case TURN_PICKED_TILES -> {
                error = chooseTiles((ArrayList<Cord>) obj);
            }
            case TURN_COLUMN -> {
                System.out.println("Colonna: "+(int) obj);
                error = chooseColumn((int) obj);
            }
            case TURN_POSITION -> {
                error = chooseTilesDisposition((int) obj);
            }
            case END_OF_TURN -> {
                calculateScore();
                error = checkIfGameEnd();
                return error;
            }
            case CHECK_MY_TURN -> {
                if((int) obj == game.getPlayers().indexOf(game.getPlayerInTurn()))
                    return Event.OK;
                else
                    return Event.NOT_YOUR_TURN;
            }
            case CHECK_REFILL -> {
                if(board.checkBoardStatus()) {
                    checkBoardToBeFilled();
                    return Event.REFILL;
                }
                else
                    return Event.BOARD_NOT_EMPTY;
            }
        }
        return error;
    }

    public Object getControllerModel(Event event, Object playerIndex) {
        Object obj = null;
        switch (event) {
            case SET_INDEX -> obj = getPlayerFromNick((String) playerIndex);
            case GAME_BOARD -> obj = getBoard();
            case GAME_PLAYERS -> obj = game.getPlayers();
            case GAME_CGC -> obj = game.getCommonGoalCard();
            case GAME_PGC -> obj = game.getPlayers().get((int)playerIndex).getPersonalGoalCard();
            case GAME_PIT -> obj = game.getPlayers().indexOf(game.getPlayerInTurn());
            case TURN_TILE_IN_HAND -> obj = getTilesFromBoard();
            case TURN_POSITION -> obj = this.playerHand;
            case UPDATE_BOOKSHELF-> {
                obj =  game.getPlayerInTurn().getMyBookshelf();
                //calculateScore();
            }
            //case TURN_BOOKSHELF -> obj = this.game.getPlayers().get(playerIndex);
        }
        return obj;
    }

    private int getPlayerFromNick(String nickname) {
        for(Player p : game.getPlayers()) {
            if (p.getNickname().equals(nickname)) {
                return game.getPlayers().indexOf(p);
            }
        }
        return -1;
    }
}

