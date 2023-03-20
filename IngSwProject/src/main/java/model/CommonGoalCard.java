package model;



import jdk.jshell.spi.ExecutionControl;

import java.util.Stack;

public class CommonGoalCard {
    private Stack<ScoringToken> tokenStack;
    private int romanNumber;
    private CGMStrategy strategy; // CGMStrategy is an interface in control yet to be build


    public ScoringToken popScoringToken(){ //ScoringToken extraction (player scored)
        return tokenStack.pop();//TO DO: we return the value of the popped Token
    }

    public void pushScoringToken(ScoringToken scToken){ //add ScoringToken (at the game start)
        tokenStack.push(scToken);
    }

    public void setRomanNumber(int romNum){
        this.romanNumber= romNum;
    }

    public int getRomanNumber(){
       return romanNumber;
    }

    public void setCGCStrategy(CGCStrategy cgc){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }
}
