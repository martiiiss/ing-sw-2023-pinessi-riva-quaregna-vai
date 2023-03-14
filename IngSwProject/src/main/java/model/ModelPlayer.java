package model;

import jdk.jshell.spi.ExecutionControl;


import java.util.ArrayList;

public class ModelPlayer {
    private String nickname;
    private ModelPlayer nextPlayer;
    private boolean isWinner;
    private boolean isFirstPlayer;
    private int score;
    private ModelBookshelf myBookshelf;
    private boolean isFirstToFinish;
    private ArrayList<ModelTile> tilesInHand; //This attribute saves the tiles selected by the player in chooseNTiles so that the bookshelf can be filled with fillBookshelf

    /*This method will be launched when the game starts once game chooses the first player. it will update isFirstPlayer*/
    public void setAsFirstPlayer(ModelPlayer firstPlayer) {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }
    /*Based on the amount of tiles that the player wants to take from the board, one of the following methods gets "launched"
    * inside these method they will invoke getTile() from BoardCell which return the tile stored in b1,b2,b3... */
    public void chooseOneTile(ModelBoardCell b1) {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }
    public void chooseTwoTiles(ModelBoardCell b1, ModelBoardCell b2) {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }
    public void chooseThreeTiles(ModelBoardCell b1,ModelBoardCell b2,ModelBoardCell b3) {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }
    /*Sets the player's nickname this method will be invoked by the view and will pass the arg "nickname"*/
    public void setNickname(String nickname) {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }
    /*Calculates the player score */
    public int countPlayerScore () {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
        return 0;
    }
    /*It compares the bookshelf of the player with the seed of the PGC, if the bookshelf has tiles in the "correct spot",
    * the method return the amount of points that will have to be added to the player's score*/
    public int checkPersonalGoalCard(ModelPersonalGoalCard pgc) {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
        return 0;
    }
    /* just like checkPersonalGoalCard it compares the seed of the card with the current state of the player's bookshelf
     * and returns the points that will be added to the score */
    public int checkCommonGoalCard(ModelCommonGoalCard cgc) {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
        return 0;
    }
    /* this method scans the entire bookshelf of the player and looks for tiles of the same Type (Cat/Book/Game...) next to
    * each other. if found it will return the amount of points that needs to be added to the score*/
    public int checkAdjacentShelf() {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
        return 0;
    }
    /*when game launches lastTurn() this method gets invoked every time a player finishes his turn. when this method will return
     *true,Game will end the game since everyone will have played their last move */
    public boolean isNextPlayerFirst() {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
        return false;
    }
    /*If the player's bookshelf is complete (there are no more available shelves), it invokes setFistToFinish in Game /**RICORDA CHE QUESTO METODO DOVRA' CONTROLLARE SE QUESTO PLAYER E' EFFETTIVAMENTE IL PRIMO A TERMINARE ATTRAVERSO L'ATTRIBUTO firstPlayeerToFinish*/
    public boolean checkIfFinished() {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
        return false;
    }
    /*this method invokes fillBookshelf and passes all the ars required it will also ask the player (Through a method of the view)
    * what column they wish to fill*/
    public void makeMove() {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }
}


