package model;

import java.util.Stack;

public class CommonGoalCard {
    private Stack<ScoringToken> tokenStack;
    private int romanNumber;
    private CGMStrategy strategy; // reference to CGMStrategy (an interface in control yet to be build)
    private int idCGC;


    //costruttore o un metodo initializeCGC?
    public CommonGoalCard(int id, int numPlayers, int romanNumber){
        this.idCGC = id;
        this.romanNumber = romanNumber;
        setCGCStrategy(id);

        //create scoringToken & push
        if(numPlayers >= 4){
            ScoringToken st2 = new ScoringToken(2, romanNumber);
            pushScoringToken(st2);
        }
        if(numPlayers>=2){
            ScoringToken st4 = new ScoringToken(4, romanNumber);
            pushScoringToken(st4);
        }
        if(numPlayers>=3){
            ScoringToken st6 = new ScoringToken(6, romanNumber);
            pushScoringToken(st6);
        }
        if(numPlayers >= 2){
            ScoringToken st8 = new ScoringToken(8, romanNumber);
            pushScoringToken(st8);
        }
    }

    public ScoringToken popScoringToken(){ //ScoringToken extraction (player scored)
        return this.tokenStack.pop();//TO DO: we return the value of the popped Token
    }

    public void pushScoringToken(ScoringToken scToken){ //add ScoringToken (at the game start)
        this.tokenStack.push(scToken);
    }

    public int getRomanNumber(){
       return this.romanNumber;
    }

    public void setCGCStrategy(int id){
        // in base al id implementa una strategy precisa (NB sono raggruppate alcune!)
        // this.strategy = ; //set strategy --> the commonGoalCard choice
    }

    // compare bookshelf and commonGoalCard
    public boolean compare(Bookshelf bookshelf){
        return this.strategy.compareRule(bookshelf, idCGC);
    }
}
