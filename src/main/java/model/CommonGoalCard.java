package model;

import util.Observable;

import java.util.Stack;

public class CommonGoalCard extends Observable {
    private Stack<ScoringToken> tokenStack;
    private int romanNumber;
    private CGCStrategy strategy; // reference to CGMStrategy
    private int idCGC;

    public CommonGoalCard(int id, int numPlayers, int romanNumber){
        this.idCGC = id;
        this.romanNumber = romanNumber;
        this.tokenStack = new Stack<>();
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
        return this.tokenStack.pop();// we return the value of the popped Token
    }

    //This method is called only at the beginning
    public void pushScoringToken(ScoringToken scToken){ //add ScoringToken (at the game start)
        this.tokenStack.push(scToken);
    }

    public Stack<ScoringToken> getTokenStack(){
        return this.tokenStack;
    }

    public int getRomanNumber(){
       return this.romanNumber;
    }

    public void setCGCStrategy(int id) throws IllegalStateException{
        switch (id) {
            case 1 -> this.strategy = new CGC1();
            case 2 -> this.strategy = new CGC2();
            case 3 -> this.strategy = new CGC3();
            case 5 -> this.strategy = new CGC5();
            case 7 -> this.strategy = new CGC7();
            case 10 -> this.strategy = new CGC10();
            case 11 -> this.strategy = new CGC11();
            case 12 -> this.strategy = new CGC12();
            case 4, 9 -> this.strategy = new CGC49();
            case 6, 8 -> this.strategy = new CGC68();
            default -> throw new IllegalStateException("Unexpected value: " + id);

        }
        //based on the id this method implements a precise strategy (!!! Some of them are in groups)
        //set strategy --> the commonGoalCard choice
    }

    public CGCStrategy getStrategy(){ return this.strategy; }

    // compare bookshelf and commonGoalCard
    public boolean compare(Bookshelf bookshelf)  {
        Tile [][] copy = new Tile[6][];

        for(int i = 0; i < 6; i++)
        {
            Tile[] aMatrix = bookshelf.getBookshelf()[i];
            //int   aLength = 5;
            copy[i] = new Tile[5];
            System.arraycopy(aMatrix, 0, copy[i], 0, 5);
        }
        Bookshelf mock = new Bookshelf();
        mock.setBookshelf(copy);
        return this.strategy.compareRule(mock, idCGC);
    }
}
