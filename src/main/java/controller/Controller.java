package controller;

import model.*;
import org.example.App;
import util.Cord;
import util.Observable;
import util.Observer;
import view.UserInterface;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Controller implements Observer {
    private Game game;
    private Bag bag;
    private Board board;
    private int chosenColumn;
    private int numberOfChosenTiles;
    private int protocol = 0;
    UserInterface UI = new UserInterface();

    private ArrayList<Player> finalRank;
    //Before the game actually starts
    public void createGame() throws IOException {
        this.game = new Game(); /*I create the Game object */
//         chooseNumOfPlayer();
        this.bag = new Bag();
        this.board = new Board(game.getNumOfPlayers());
        game.setGameStarted();
    }

    public Game getInstanceOfGame(){
        return this.game;
    }

    public Bag getInstanceOfBag(){
        return this.bag;
    }

    public Board getInstanceOfBoard(){
        return this.board;
    }

    public UserInterface getInstanceOfUI(){
        return this.UI;
    }


    //this method needs to be error checked
    public boolean chooseNumOfPlayer(int num) throws IOException {
        if(num<2 || num>4){
            return false;
        }
        game.setNumOfPlayers(num);
        return true;
    }

    //this method needs to be fixed -> multithreading TODO
    public void chooseNickname() throws IOException {
        String nickname = UI.askPlayerNickname();
        //control for the first player
        while(nickname.isEmpty())
        {
            nickname= UI.askPlayerNickname();
        }
        //control for the others
        for(int i = 0; i<game.getPlayers().size();i++) {
            while(game.getPlayers().get(i).getNickname().equals(nickname) || nickname.isEmpty()){
                System.out.println("Invalid nickname! Same as another user or empty.");
                nickname = UI.askPlayerNickname();
                i=0;
            }
        }
        Player newPlayer = new Player();
        newPlayer.setNickname(nickname);
        game.addPlayer(newPlayer);
    }

    public void chooseProtocol() throws IOException {
        int chosenProtocol = UI.webProtocol();
        boolean flag = false;
        while (!flag) {
            switch (chosenProtocol) {
                case 1 -> {
                    flag = true;
                    System.out.println("You have chosen the Socket protocol!");
                    protocol = chosenProtocol;
                }
                case 2 -> {
                    flag = true;
                    System.out.println("You have chosen the JavaRMI protocol!");
                    protocol = chosenProtocol;
                }
                default -> {
                    System.out.println("Sorry, try again...");
                    chosenProtocol = UI.webProtocol();
                }
            }
        }
    }//IDK how we will use this, but in this way we know witch one in between the two protocols is chosen by the player
    //TODO the cases are useful, we need to implement the choice


    public int getProtocol(){return this.protocol;}

    public void chooseUserInterface() throws IOException {
        int chosenInterface= UI.userInterface();
        boolean flag = false;
        while(!flag) {
            switch (chosenInterface) {
                case 1 -> {
                    flag = true;
                    System.out.println("You have chosen the TUI!");
                }
                case 2 -> {
                    flag = true;
                    System.out.println("You have chosen the GUI!");
                }
                default -> {
                    System.out.println("Sorry, try again...");
                    chosenInterface= UI.userInterface();
                }
            }
        }
        //We will find a way to use this one
    }//TODO the cases are useful, we need to implement the choice

    public void userChoices() throws IOException {
        chooseProtocol();
        chooseUserInterface();
        chooseNickname();
    }

    public boolean countPlayers(){
        if(game.getPlayers().size() == game.getNumOfPlayers()){
            System.out.println("Number of players reached!");
            return true;
        } else {return false;}
    }

    /**this method initializes the Game after all the players are connected*/
    public void initializeGame(){
        while(!countPlayers()){/*we make the server wait*/}
        for(int i=0;i<game.getNumOfPlayers();i++){
            game.getPlayers().get(i).setMyBookshelf();//the method of player creates a new Bookshelf
        }
        game.assignPersonalGoalCard(game.getNumOfPlayers());
        game.setCommonGoalCards();
        board.setUpBoard(bag.getBagTiles(board.getNumOfCells()));//filled the board
        game.getPlayers().get(0).setAsFirstPlayer();
        game.setPlayerInTurn(game.getPlayers().get(0));

    }//TODO We consider the first player in the ArrayList as the first player -> implementation with server

    //button START GAME somewhere

    //TODO create the GameFlow
    public void gameFlow() throws IOException {
        do {
            UI.showTUIBookshelf(game.getPlayerInTurn().getMyBookshelf());
            UI.showTUIBoard(board);
            checkBoardToBeFilled();
            numOfChosenTiles();
            chooseTiles();
            chooseColumn();
            for (int i = 0; i < numberOfChosenTiles; i++) {
                chooseTilesDisposition();
            }
            calculateScore();//I calculate the score every time a Player puts some tiles into its Bookshelf
        } while (!checkIfGameEnd());//this method controls if the Bookshelf is full and determines how to proceed
    }
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
    private void numOfChosenTiles() throws IOException {
        int freeSlots = game.getPlayerInTurn().getMyBookshelf().getNumOfFreeSlots();
        this.numberOfChosenTiles = UI.askNumberOfChosenTiles();
        while(this.numberOfChosenTiles<1 || this.numberOfChosenTiles>4 || freeSlots < this.numberOfChosenTiles ){
            System.out.println("This number is wrong, retry!");
            this.numberOfChosenTiles = UI.askNumberOfChosenTiles();
        }

    }

    //this method will work together with the view, maybe showing the player which tiles can be chosen
    //first number chosen is the column, the second is the row
    //FIXME: Si possono pescare tiles dappertutto, non viene controllato se è disponibile
    //FIXME: Va avanti all'infinito se la scelta è diversa da 1
    private ArrayList<Cord> cords = new ArrayList<>();
    public void chooseTiles() throws IOException {
        ArrayList<Tile> tiles = new ArrayList();
        boolean accepted = true;
        int i = 0;
        cords.removeAll(cords);
        while (cords.size()<this.numberOfChosenTiles) {
            Cord cord = new Cord();
            do {
                String in = UI.askTilePosition();
                try {
                    String[] splittedStr = in.split(",");
                    cord.setCords(Integer.parseInt(splittedStr[0]), Integer.parseInt(splittedStr[1]));
                } catch (NumberFormatException formatException) {
                    System.err.println("Invalid format...");
                } catch (ArrayIndexOutOfBoundsException boundsException) {
                    System.err.println("Invalid format or non existent coordinate...");
                }
            } while (cord.getRowCord() == 0 && cord.getColCord() == 0);
            accepted = true;
            if (board.getSelectedType(cord.getRowCord(), cord.getColCord()) == Type.NOTHING || board.getSelectedType(cord.getRowCord(), cord.getColCord()) == Type.BLOCKED) {
                System.err.println("Invalid tile....");
                accepted = false;
            }
            if (accepted && !isTileFreeTile(cord)) {
                System.err.println("This tile is blocked...");
                accepted = false;
            }
            if (!this.cords.isEmpty())
                for (Cord value : this.cords)
                    if (value.getRowCord() != cord.getRowCord() && value.getColCord() != cord.getColCord()) {
                        accepted = false;
                        System.err.println("This tile is not adjacent to the previous...");
                        break;
                    }
            if(accepted)
                cords.add(cord);
        }
        for (i=0; i<this.cords.size();i++)
            tiles.add(board.removeTile(this.cords.get(i).getRowCord(), this.cords.get(i).getColCord()));
        game.getPlayerInTurn().setTilesInHand(tiles);
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

    public void chooseColumn() throws IOException {
        Tile[][] playerBookshelf = game.getPlayerInTurn().getMyBookshelf().getBookshelf();
        this.chosenColumn = UI.askColumn();
        boolean flag = false;
        while(!flag){
            if(this.chosenColumn<0 || this.chosenColumn >4) {
                System.err.println("That column doesn't exist! Try again:");
            }else if(playerBookshelf[this.numberOfChosenTiles-1][this.chosenColumn].getType() != Type.NOTHING){
                System.err.println("That column hasn't enough space! Try again:");
            }else{
                flag = true; //The number is correct -> I don't ask again the player
            }
            if(!flag){
                this.chosenColumn = UI.askColumn();
            }
        }
    }

    //after chooseColumn has been invoked
    public void chooseTilesDisposition() throws IOException {
        int index = UI.askTileToInsert(game.getPlayerInTurn().getTilesInHand());
        while(index <0 || index >= game.getPlayerInTurn().getTilesInHand().size()){
            System.err.println("That index doesn't exist! Try again:");
            index = UI.askTileToInsert(game.getPlayerInTurn().getTilesInHand());
        }
        game.getPlayerInTurn().getMyBookshelf().placeTile(this.chosenColumn,game.getPlayerInTurn().getTilesInHand().get(index));
        game.getPlayerInTurn().getTilesInHand().remove(index);
    }


    public void calculateScore(){
        int cgc = game.checkCommonGoalCard();
        int pgc = game.getPlayerInTurn().checkCompletePGC();
        int adjacencies = game.getPlayerInTurn().checkAdjacentBookshelf();
        game.getPlayerInTurn().updateScore(cgc+pgc+adjacencies);
    }

    public boolean checkIfGameEnd() throws IOException {
        int index=0;
        if (game.getPlayers().indexOf(game.getPlayerInTurn()) != game.getNumOfPlayers() - 1) { //calculate index
            index = game.getPlayers().indexOf(game.getPlayerInTurn()) + 1;//index of the next player
        }

        if(game.getPlayerInTurn().getMyBookshelf().getStatus()){//if Bookshelf is full
            if(game.getIsLastTurn()) {//is last turn
                if (game.getPlayers().get(index).getIsFirstPlayer()) {//if the player next to the current one is THE FIRST PLAYER
                    endOfGame(); /*CALL THE END OF GAME*/
                    return true;
                } else {
                    goToNext(); //set next Player in turn
                    return false;
                }
            } else{
                game.setFinisher(game.getPlayerInTurn());//I set the player that finished first and set isLastTurn -> I use a method from method.Game
                game.getPlayerInTurn().updateScore(1);//I add an extra point to the first player to finish
                if (game.getPlayers().get(index).getIsFirstPlayer()) {//if the player next to the current one is THE FIRST PLAYER
                    endOfGame();/*CALL THE END OF GAME*/
                    return true;
                } else {
                    goToNext(); //set next Player in turn
                    return false;
                }
            }
        } else {//Bookshelf NOT full
            if(game.getIsLastTurn()){
                if(game.getPlayers().get(index).getIsFirstPlayer()){//if the player next to the current one is THE FIRST PLAYER
                    endOfGame();/*CALL END OF GAME*/
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

    public void goToNext(){ //set player in turn
        int i = game.getPlayers().indexOf(game.getPlayerInTurn())+1;
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

    @Override
    public void update(Observable o,Object obj) {
    }

    public void clearChoice() throws IOException {
        this.cords.clear();
        chooseTiles();
    }
}
