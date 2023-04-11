package controller;

import jdk.jshell.spi.ExecutionControl;
import model.*;
import view.UserInterface;

public class Controller {
    private UserInterface UI;
    private Game game;
    private Bag bag;
    private Board board;
    //Before the game actually starts
    public void createGame(){
         this.game = new Game(); /**I create the Game object */
         chooseNumOfPlayer();
         this.bag = new Bag();
         this.board = new Board(game.getNumOfPlayers());
         this.UI = new view.UserInterface();
    }
    //this method needs to be error checked
    public void chooseNumOfPlayer(){
        int numOfPlayers = UI.askNumOfPlayers();
        if(numOfPlayers<2 || numOfPlayers>4){
            System.out.println("This number is wrong, retry!");
            numOfPlayers=UI.askNumOfPlayers();
        }
        game.setNumOfPlayers(numOfPlayers);
    }


    //this method needs to be fixed -> multithreading TODO
    public void chooseNickname(){
        String nickname = UI.askPlayerNickname();
        //control for the first player
        while(nickname.isEmpty())
        {
            System.out.println("Your name is empty!");
            nickname= UI.askPlayerNickname();
        }
        //control for the others
        for(int i = 0; i<game.getPlayers().size();i++) {
            while(game.getPlayers().get(i).equals(nickname) || nickname.isEmpty()){
                System.out.println("Invalid nickname! Same as another user or empty.");
                nickname = UI.askPlayerNickname();
                i=0;
            }
        }
        Player newPlayer = new Player();
        newPlayer.setNickname(nickname);
        game.addPlayer(newPlayer);
    }

    public void chooseProtocol(){ //maybe change the String into an int or char
        String chosenProtocol = UI.webProtocol();
        if(chosenProtocol.equals("socket")||chosenProtocol.equals("rmi")){
            System.out.println("Ok!");
        } else {
            System.out.println("Sorry, try again...");
            chosenProtocol = UI.webProtocol();
        }
    }//IDK how we will use this, but in this way we know witch one in between the two protocols is chosen by the player

    public void chooseUserInterface(){//maybe change the String into an int or char
        String chosenInterface = UI.userInterface();
        if(chosenInterface.equals("tui") || chosenInterface.equals("gui")){
            System.out.println("Ok!");
        } else {
            System.out.println("Sorry, try again...");
            chosenInterface = UI.userInterface();
        }
        //We will find a way to use this one
    }
    public void userChoices(){
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
        //game.getPlayers().get((new Random()).nextInt(game.getNumOfPlayers)).setAsFirstPlayer();
    }//TODO check if we consider the first player in the ArrayList as the first player

    //button START GAME somewhere

    //TODO create the GameFlow
    public void gameFlow(){
        checkBoardToBeFilled();
        chooseTiles();
        chooseColumn();
        chooseTilesDisposition();
        calculateScore();//I calculate the score every time a Player puts some tiles into its Bookshelf
        checkIfGameEnd();//this method controls if the Bookshelf is full and determines how to proceed
    }
    public void checkBoardToBeFilled(){
        if(board.checkBoardStatus()){
            //Qui implementeremo il codice per riempire la board
            // sia che sia vuota sia che ci siano tessere sulla Board
            //Indipentemente da fatto che siano presenti tiles sulla board
            //devo provare a raccoglierle
            //controllare se la bag contiene abbastanza tiles
        }
    }//TODO implement this function



    //this method will work together with the view, maybe showing the player which tiles can be chosen
    public void chooseTiles(){
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

    //with this method the tiles chosen will be put into tilesInHand, it is needed a button in view
    public void confirmYourChoice(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }//TODO decide if this is needed


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

    public void checkIfGameEnd(){
        if(game.getPlayerInTurn().getMyBookshelf().getStatus()){//if Bookshelf is full
            if(game.getIsLastTurn()){//is last turn
                int index = game.getPlayers().indexOf(game.getPlayerInTurn())+1;//index of the next player
                if(game.getPlayers().get(index).getIsFirstPlayer()){//if the player next to the current one is THE FIRST PLAYER
                    endOfGame();/*CALL THE END OF GAME*/
                }
                goToNext(); //set next Player in turn
            }
            game.setFinisher(game.getPlayerInTurn());//I set the player that finished first and set isLastTurn -> I use a method from method.Game
            game.getPlayerInTurn().updateScore(1);//I add an extra point to the first player to finish

            int index = game.getPlayers().indexOf(game.getPlayerInTurn())+1;//index of the next player
            if(game.getPlayers().get(index).getIsFirstPlayer()) {//if the player next to the current one is THE FIRST PLAYER
                endOfGame();/*CALL THE END OF GAME*/
            }
            goToNext();//set next Player in turn

        } else {//Bookshelf NOT full
            if(game.getIsLastTurn()){
                int index = game.getPlayers().indexOf(game.getPlayerInTurn())+1;//index of the next player
                if(game.getPlayers().get(index).getIsFirstPlayer()){//if the player next to the current one is THE FIRST PLAYER
                    endOfGame();/*CALL END OF GAME*/
                }
                goToNext(); //set next Player in turn
            }
        }
    }//TODO implement this method -> it needs to be split into two different method (MAYBE NOT)
     // Control if how it's implemented now it's OK


     public void goToNext(){
        int i = game.getPlayers().indexOf(game.getPlayerInTurn())+1;
        if(i<game.getNumOfPlayers()){
            game.setPlayerInTurn(game.getPlayers().get(i));
        } else {
            game.setPlayerInTurn(game.getPlayers().get(0));
        }
    }//TODO optimize this

    //asks the player if he wants to play another time
    public void playAgain(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }//TODO implement this method

    public void endOfGame(){
        //1.	In base al punteggio dei Player viene stilata una classifica
        //a.	In caso di parità chiedere al tutor/su slack
        //2.	Viene terminata la partita e compare un tasto PlayAgain che ci riporta al punto di partenza.
        playAgain();
    }//TODO implement this method
}
