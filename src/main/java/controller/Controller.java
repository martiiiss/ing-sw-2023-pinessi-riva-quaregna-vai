package controller;

import distributed.Client;
import distributed.Server;
import model.*;
import org.example.App;
import util.Cord;
import util.Error;
import util.Event;
import util.Observable;
import util.Observer;
import view.UserInterface;
import view.UserView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;

import static util.Event.*;

public class Controller implements Observer {
    private Game game;
    private Bag bag;
    private Board board;
    private int chosenColumn;
    private int numberOfChosenTiles;
    private int protocol = 0;
    private UserInterface UI = new UserInterface();
    private ArrayList<Player> finalRank;
    private ArrayList<Tile> playerHand;
    //private Event nextEvent;

    public Controller() throws IOException {
        createGame();
    }

    //Before the game actually starts
    public void createGame() throws IOException {
        //this.nextEventPlayer = new ArrayList();
        this.game = new Game(); /*I create the Game object */
//         chooseNumOfPlayer();
    }


    public Game getInstanceOfGame(){
        return this.game;
    }

    //this method needs to be fixed -> multithreading TODO
    public Error chooseNickname(String nickname) throws IOException {
        if(nickname.isEmpty()) { //control for the first player
            return Error.EMPTY_NICKNAME;
        }
        //control for the others
        for(int i = 0; i<game.getPlayers().size();i++) {
            if(game.getPlayers().get(i).getNickname().equals(nickname)){
                return Error.NOT_AVAILABLE;
            }
        }
        Player newPlayer = new Player();
        newPlayer.setNickname(nickname);
        game.addPlayer(newPlayer);
        return Error.OK;
    }

    public Error chooseUserInterface(int chosenInterface) throws IOException {
        switch (chosenInterface) {
            case 1, 2 -> {
                System.out.println("user interface scelta");
                return Error.OK;
            }
            default -> {
                return Error.NOT_AVAILABLE;
            }
        }
    }

    /**this method initializes the Game after all the players are connected*/
    public void initializeGame(){
        this.bag = new Bag();
        this.board = new Board(game.getNumOfPlayers());
       // while(!countPlayers()){/*we make the server wait*/}
        for(int i=0;i<game.getNumOfPlayers();i++){
            game.getPlayers().get(i).setMyBookshelf();//the method of player creates a new Bookshelf
        }
        game.assignPersonalGoalCard(game.getNumOfPlayers());
        game.setCommonGoalCards();
        board.setNumOfCells(game.getNumOfPlayers());
        ArrayList<Tile> tiles = bag.getBagTiles(board.getNumOfCells());
        board.setUpBoard(tiles);//filled the board
        game.getPlayers().get(0).setAsFirstPlayer();
        game.setPlayerInTurn(game.getPlayers().get(0));
        game.setGameStarted();
    }//TODO We consider the first player in the ArrayList as the first player -> implementation with server

    //button START GAME somewhere

    //TODO create the GameFlow
   /* public void gameFlow() throws IOException {
        do {
            UI.showTUIBookshelf(game.getPlayerInTurn().getMyBookshelf());
            UI.showTUIBoard(board);
            checkBoardToBeFilled();
            for (int i = 0; i < numberOfChosenTiles; i++) {
            }
            calculateScore();//I calculate the score every time a Player puts some tiles into its Bookshelf
        } while (!checkIfGameEnd());//this method controls if the Bookshelf is full and determines how to proceed
    }*/
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


    //Cosa succede se il giocatore prova a prendere 3 tiles quando sulla plancia l'unico
    //gruppo disponibile è di dimensione 2?
    private Error numOfChosenTiles(int numberOfChosenTiles) throws IOException {
        int freeSlots = game.getPlayerInTurn().getMyBookshelf().getNumOfFreeSlots();
        if (numberOfChosenTiles<1 || numberOfChosenTiles>=3)
            return Error.OUT_OF_BOUNDS;
        if(freeSlots < numberOfChosenTiles )
            return Error.INVALID_VALUE;
        this.numberOfChosenTiles = numberOfChosenTiles;
        return Error.OK;
    }

    //this method will work together with the view, maybe showing the player which tiles can be chosen
    //first number chosen is the column, the second is the row
    //FIXME: Si possono pescare tiles dappertutto, non viene controllato se è disponibile
    //FIXME: Va avanti all'infinito se la scelta è diversa da 1
    private ArrayList<Cord> playerCords = new ArrayList<>();
    public Error chooseTiles(ArrayList<Cord> cords) throws IOException {
        for (Cord cord : cords){
            if (board.getSelectedType(cord.getRowCord(), cord.getColCord()) == Type.NOTHING || board.getSelectedType(cord.getRowCord(), cord.getColCord()) == Type.BLOCKED)
                return Error.BLOCKED_NOTHING;
            if (!isTileFreeTile(cord))
                return Error.NOT_ON_BORDER;
            for (Cord value : cords)
                if (value.getRowCord() != cord.getRowCord() && value.getColCord() != cord.getColCord()) {
                    System.err.println("This tile is not adjacent to the previous...");
                    return Error.NOT_ADJACENT;
                }
        }
        playerCords = cords;
        return Error.OK;
    }

    private boolean isTileFreeTile(Cord cord) {
        int x = cord.getRowCord();
        int y = cord.getColCord();
        boolean valid = false;
        if(board.getSelectedType(x + 1, y) == Type.NOTHING || board.getSelectedType(x, y + 1) == Type.NOTHING || board.getSelectedType(x - 1, y) == Type.NOTHING || board.getSelectedType(x, y - 1) == Type.NOTHING)
            valid=true;
        if(board.getSelectedType(x + 1, y) == Type.BLOCKED || board.getSelectedType(x, y + 1) == Type.BLOCKED || board.getSelectedType(x - 1, y) == Type.BLOCKED || board.getSelectedType(x, y - 1) == Type.BLOCKED)
            valid=true;
        return valid;
    }

    public Error chooseColumn(int chosenColumn) throws IOException {
        Tile[][] playerBookshelf = game.getPlayerInTurn().getMyBookshelf().getBookshelf();
        if(chosenColumn<0 || chosenColumn >4)
            return Error.INVALID_VALUE;
        if(playerBookshelf[this.numberOfChosenTiles-1][chosenColumn].getType() != Type.NOTHING)
            return Error.OUT_OF_BOUNDS;
        this.chosenColumn = chosenColumn;
        return Error.OK;
    }

    //after chooseColumn has been invoked
    public Error chooseTilesDisposition(int index) throws IOException {
        if(index <0 || index >= playerHand.size())
            return Error.INVALID_VALUE;
        game.getPlayerInTurn().getMyBookshelf().placeTile(this.chosenColumn,playerHand.get(index));
        playerHand.get(index).setType(Type.NOTHING);
        return Error.OK;
    }


    public void calculateScore(){
        int cgc = game.checkCommonGoalCard();
        int pgc = game.getPlayerInTurn().checkCompletePGC();
        int adjacencies = game.getPlayerInTurn().checkAdjacentBookshelf();
        game.getPlayerInTurn().updateScore(cgc+pgc+adjacencies);
    }

   /* public boolean checkIfGameEnd() throws IOException {
        int index=0;
        if (game.getPlayers().indexOf(game.getPlayerInTurn()) != game.getNumOfPlayers() - 1) { //calculate index
            index = game.getPlayers().indexOf(game.getPlayerInTurn()) + 1;//index of the next player
        }

        if(game.getPlayerInTurn().getMyBookshelf().getStatus()){//if Bookshelf is full
            if(game.getIsLastTurn()) {//is last turn
                if (game.getPlayers().get(index).getIsFirstPlayer()) {//if the player next to the current one is THE FIRST PLAYER
                    endOfGame(); CALL THE END OF GAME
                    return true;
                } else {
                    goToNext(); //set next Player in turn
                    return false;
                }
            } else{
                game.setFinisher(game.getPlayerInTurn());//I set the player that finished first and set isLastTurn -> I use a method from method.Game
                game.getPlayerInTurn().updateScore(1);//I add an extra point to the first player to finish
                if (game.getPlayers().get(index).getIsFirstPlayer()) {//if the player next to the current one is THE FIRST PLAYER
                    endOfGame();
                    return true;
                } else {
                    goToNext(); //set next Player in turn
                    return false;
                }
            }
        } else {//Bookshelf NOT full
            if(game.getIsLastTurn()){
                if(game.getPlayers().get(index).getIsFirstPlayer()){//if the player next to the current one is THE FIRST PLAYER
                    endOfGame();CALL END OF GAME
                    return true;
                } else{
                    goToNext(); //set next Player in turn
                    return false;
                }
            } else{ //not last turn
                goToNext();
                return false;
            }
        }
    }//TODO optimize this method
*/

    public void goToNext(Player playerInTurn){ //set player in turn
        int i = game.getPlayers().indexOf(playerInTurn)+1;
        if(i<game.getNumOfPlayers()){
            game.setPlayerInTurn(game.getPlayers().get(i));
        } else {
            game.setPlayerInTurn(game.getPlayers().get(0));
        }
    }//TODO optimize this

    //asks the player if he wants to play another time
    public void playAgain() throws IOException {
        int startAgain = UI.askPlayAgain();
        boolean flag = false;
        while(!flag) {
            switch (startAgain) {
                case 1 -> {
                    flag = true;
                    System.out.println("Ok, starting a new game...");}
                case 0 -> {
                    flag = true;
                    System.out.println("Ok, bye bye!");}
                default -> {
                    System.out.println("Sorry, try again...");
                    startAgain = UI.userInterface();
                }
            }
        }
    }//TODO the cases are useful, we need to implement the choice

    public void endOfGame() throws IOException {
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
        playAgain();
    }//TODO ask how to choose the winner if two players have the same score


    public Board getBoard(){ return this.board;}

    @Override
    public void update(Observable o,Object obj) {

    }

    public void clearChoice() throws IOException {
    }

    public ArrayList<Tile> getTilesFromBoard() {
        ArrayList<Tile> removedTiles = new ArrayList<>();
        for(Cord cord : playerCords) {
            removedTiles.add(board.removeTile(cord.getRowCord(),cord.getColCord()));
        }
        System.out.println("TEST DELLE TILES NELL HAND: "+game.getPlayerInTurn().getTilesInHand());
        this.playerHand = removedTiles;
        return removedTiles;
        //TODO: UPDATE OBSERVERS!
    }


    public Event getNextEvent(int num, int numOfClientConnected) {
        if (this.game.getNextEventPlayer().size() == 0) {
            return ASK_NUM_PLAYERS;
        }
        if(this.game.getPlayerInTurn()==null && this.game.getPlayers().size()!=0){
            this.game.setPlayerInTurn(this.game.getPlayers().get(0));
        }

        if(this.game.getNextEventPlayer()==null || this.game.getNextEventPlayer().size()!=numOfClientConnected){
            for(int i=0; i<numOfClientConnected-this.game.getNextEventPlayer().size(); i++ ){
                this.game.getNextEventPlayer().add(this.game.getNextEventPlayer().size()-1+i, ASK_NUM_PLAYERS);
            }
        }
        if(this.game.getNextEventPlayer().size()>num && this.game.getPlayers().size()>num) {
            if (this.game.getNextEventPlayer().get(num) == WAIT && this.game.getPlayers().get(num).equals(this.game.getPlayerInTurn()) && this.game.getPlayers().size() == this.game.getNumOfPlayers()) {
                System.out.println("num è in WAIT: " + num);

                if (num == this.game.getNumOfPlayers() - 1) { //sono l'ultimo giocatore
                    this.game.setPlayerInTurn(this.game.getPlayers().get(0));
                    this.game.setNextEventPlayer(START, 0, numOfClientConnected); //il primo giocatore inizia il gioco
                    System.out.println("parte primo player");
                    initializeGame();

                } else if (num < this.game.getNumOfPlayers() - 1) {
                    this.game.setPlayerInTurn(this.game.getPlayers().get(num + 1));
                    System.out.println("parte" + (num + 1) + " player");
                    this.game.setNextEventPlayer(START, num + 1, numOfClientConnected);
                }
            }
        }
        return this.game.getNextEventPlayer().get(num);
    }


    public Error updateController(Object obj, Event event) throws IOException {
        Error error = Error.OK;
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
                if((int) obj == game.getPlayers().size()) {
                    System.out.println("The game is starting");
                    initializeGame();
                    error = Error.OK;
                }
                else
                    error = Error.WAIT;
            }
            case GAME_STARTED -> {
                if(game.getGameStarted())
                    error = Error.OK;
                else
                    error = Error.WAIT;
            }
            case TURN_AMOUNT -> {
                error = numOfChosenTiles((int) obj);
            }
            case TURN_PICKED_TILES -> {
               error = chooseTiles((ArrayList<Cord>) obj);
            }
            case TURN_COLUMN -> {
                error = chooseColumn((int) obj);
            }
            case TURN_POSITION -> {
                error = chooseTilesDisposition((int) obj);
            }
            case END_OF_TURN -> {
                goToNext(game.getPlayers().get((int)obj));
                System.out.println("PIT index"+game.getPlayers().indexOf(game.getPlayerInTurn()));
                return Error.OK;
            }
            case CHECK_MY_TURN -> {
                if((int) obj == game.getPlayers().indexOf(game.getPlayerInTurn()))
                    return Error.OK;
                else
                    return Error.NOT_YOUR_TURN;
            }
        }
        return error;
    }

    public Object getControllerModel(Event event, int playerIndex) {
        Object obj = null;
        switch (event) {
            case GAME_BOARD -> obj = this.board;
            case GAME_PLAYERS -> obj = game.getPlayers();
            case GAME_CGC -> obj = game.getCommonGoalCard();
            case GAME_PGC -> obj = game.getPlayers().get(playerIndex).getPersonalGoalCard();
            case GAME_PIT -> obj = game.getPlayers().indexOf(game.getPlayerInTurn());
            case TURN_TILE_IN_HAND -> obj = getTilesFromBoard();
            case TURN_POSITION -> obj = this.playerHand;
            case TURN_BOOKSHELF -> obj = this.game.getPlayers().get(playerIndex);

        }
        return obj;
    }
}

