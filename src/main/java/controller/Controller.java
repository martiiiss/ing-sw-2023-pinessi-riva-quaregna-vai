package controller;

import jdk.jshell.spi.ExecutionControl;
import model.*;

public class Controller {

    //Before the game actually starts
    public void createGame(){
        Game game = new Game(); /**I create the Game object -> MAYBE WRONG */
        //TODO is this ok?
        //try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }

    //this method needs to be error checked
    public void chooseNumOfPlayer(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }


    //this method needs to be error checked
    public void chooseNickname(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }

    //During the game

    //this method will work together with the view, maybe showing the player which tiles can be chosen
    public void chooseTiles(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }

    //with this method the tiles chosen will be put into tilesInHand, it is needed a button in view
    public void confirmYourChoice(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }

    //this method needs to be error checked
    public void chooseColumn(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }

    //after chooseColumn has been invoked
    public void chooseTileToInsert(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }


    //huge method. --> maybe is better to split this into different methods :)
    // It changes the player in turn,
    // controls if the board needs to be refilled,
    // verifies the commonGoalCard and eventually gives the player a token,
    // updates the player score,
    // finally (if it's needed) signal the last turn and gives the finisher an extra point
    public void goToNext(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }

    //asks the player if he wants to play another time
    public void playAgain(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}

    }
}
