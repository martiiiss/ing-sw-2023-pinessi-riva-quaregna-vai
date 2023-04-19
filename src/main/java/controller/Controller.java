package controller;

import jdk.jshell.spi.ExecutionControl;
import model.*;
import util.Cord;
import view.UserInterface;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    //private UserInterface UI;
    private Game game;
    private Bag bag;
    private Board board;
    //Before the game actually starts
    public void createGame() throws IOException {
         this.game = new Game(); /*I create the Game object */
         chooseNumOfPlayer();
         this.bag = new Bag();
         this.board = new Board(game.getNumOfPlayers());
    }

    UserInterface UI = new view.UserInterface();
    //this method needs to be error checked
    public void chooseNumOfPlayer() throws IOException {
        int numberOfPlayers = UI.askNumOfPlayers();
        while(numberOfPlayers<2 || numberOfPlayers>4){
            System.out.println("This number is wrong, retry!");
            numberOfPlayers = UI.askNumOfPlayers();
        }
        game.setNumOfPlayers(numberOfPlayers);
    }



    //this method needs to be fixed -> multithreading TODO
    public void chooseNickname() throws IOException {
        String nickname = UI.askPlayerNickname();
        //control for the first player
        while(nickname.isEmpty())
        {
            System.out.println("Your name is empty!");
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
                }
                case 2 -> {
                    flag = true;
                    System.out.println("You have chosen the JavaRMI protocol!");
                }
                default -> {
                    System.out.println("Sorry, try again...");
                    chosenProtocol = UI.webProtocol();
                }
            }
        }
    }//IDK how we will use this, but in this way we know witch one in between the two protocols is chosen by the player
    //TODO the cases are useful, we need to implement the choice

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
        chooseNickname();
        chooseProtocol();
        chooseUserInterface();
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
        checkBoardToBeFilled();
        chooseTiles();
        chooseColumn();
        chooseTilesDisposition();
        calculateScore();//I calculate the score every time a Player puts some tiles into its Bookshelf
        checkIfGameEnd();//this method controls if the Bookshelf is full and determines how to proceed
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


    private int numOfChosenTiles() throws IOException {
        int numberOfChosenTiles = UI.askNumberOfChosenTiles();
        while(numberOfChosenTiles<1 || numberOfChosenTiles>4){
            System.out.println("This number is wrong, retry!");
            numberOfChosenTiles = UI.askNumberOfChosenTiles();
        }
        return numberOfChosenTiles;
    }


    //this method will work together with the view, maybe showing the player which tiles can be chosen
    public void chooseTiles() throws IOException {
        ArrayList<Tile> tiles = new ArrayList();
        ArrayList<Cord> cords;
        int numberOfChosenTiles = numOfChosenTiles();//I save the number of chosen tiles//TODO check if this number is correct with the bookshelf
        int freeSlots = game.getPlayerInTurn().getMyBookshelf().getNumOfFreeSlots();
        if (freeSlots < numberOfChosenTiles){ /*FIXME:Throws the Exc??*/}
        cords = UI.askTilePosition(numberOfChosenTiles);
        for (int i=0; i<numberOfChosenTiles; i++)
            tiles.add(board.removeTile(cords.get(i).getRowCord(),cords.get(i).getColCord()));
        game.getPlayerInTurn().setTilesInHand(tiles);





        //4.	Il playerInTurn pesca le sue tiles (da 1 a 3)
        //a.	Controllo che prenda un numero corretto di tiles
        //i.	Se prende un numero corretto di tiles -> vai avanti
        //ii.	Se prova a prendere un numero =0 oppure >3 tiles -> lancio un errore e vai all'inizio del metodo
        //b.	Controllo che prenda delle tiles disponibili
        //i.	Se prende delle tiles corrette -> vai avanti
        //ii.	Se prova a prendere tiles non disponibili -> lancio un errore e vai all'inizio del metodo
        //c.	Controllo che le tiles che ha preso ci stiano nella bookshelf
        //i.	Se ci stanno -> vai avanti
        //ii.	Se non dovessero starci -> lancio un errore e vai all'inizio del metodo
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }//TODO implement this method

    public void chooseColumn(){
        //5.	Il playerInTurn sceglie la colonna in cui inserire le tiles
        //a.	Se ci stanno vai avanti
        //b.	Se non ci stanno in quella colonna -> lancio un errore e vai a inizio metodo
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }//TODO implement this method

    //after chooseColumn has been invoked
    public void chooseTilesDisposition(){
        //	Il playerInTurn sceglie la disposizione delle tile
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }//TODO implement this method

    public void calculateScore(){
        int cgc = game.checkCommonGoalCard();
        int pgc = game.getPlayerInTurn().checkCompletePGC();
        int adjacencies = game.getPlayerInTurn().checkAdjacentBookshelf();
        game.getPlayerInTurn().updateScore(cgc+pgc+adjacencies);
    }

    public void checkIfGameEnd() throws IOException {
        int index=0;
        if (game.getPlayers().indexOf(game.getPlayerInTurn()) != game.getNumOfPlayers() - 1) { //calculate index
            index = game.getPlayers().indexOf(game.getPlayerInTurn()) + 1;//index of the next player
        }

        if(game.getPlayerInTurn().getMyBookshelf().getStatus()){//if Bookshelf is full
            if(game.getIsLastTurn()) {//is last turn
                if (game.getPlayers().get(index).getIsFirstPlayer()) {//if the player next to the current one is THE FIRST PLAYER
                    endOfGame();/*CALL THE END OF GAME*/
                } else {
                    goToNext(); //set next Player in turn
                }
            } else{
                game.setFinisher(game.getPlayerInTurn());//I set the player that finished first and set isLastTurn -> I use a method from method.Game
                game.getPlayerInTurn().updateScore(1);//I add an extra point to the first player to finish
                if (game.getPlayers().get(index).getIsFirstPlayer()) {//if the player next to the current one is THE FIRST PLAYER
                    endOfGame();/*CALL THE END OF GAME*/
                } else {
                    goToNext(); //set next Player in turn
                }
            }
        } else {//Bookshelf NOT full
            if(game.getIsLastTurn()){
                if(game.getPlayers().get(index).getIsFirstPlayer()){//if the player next to the current one is THE FIRST PLAYER
                    endOfGame();/*CALL END OF GAME*/
                } else{
                    goToNext(); //set next Player in turn
                }
            } else{ //not last turn
                goToNext();
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
        //1.	In base al punteggio dei Player viene stilata una classifica
        //a.	In caso di parità chiedere al tutor/su slack
        //2.	Viene terminata la partita e compare un tasto PlayAgain che ci riporta al punto di partenza.
        playAgain();
    }//TODO implement this method -> ask how to choose the winner if two players have the same score
}
