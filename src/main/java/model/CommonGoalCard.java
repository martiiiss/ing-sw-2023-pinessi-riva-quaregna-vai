package model;

import util.Observable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Stack;
/**Class that represents the Common Goal Card*/
public class CommonGoalCard extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = 6808176289351890649L;
    private Stack<ScoringToken> tokenStack;
    private int romanNumber;
    private CGCStrategy strategy; // reference to CGMStrategy
    private int idCGC;

    /**
     * <p>
     *     Constructor of the class. <br>
     *     It initializes the Common Goal Card by setting the Strategy Pattern based on the id as well as
     *     creating the stack of scoringToken based on the number of players.
     *
     * </p>
     * @param id is an integer that represents the id of the card, needed for the strategy
     * @param numPlayers is an integer that represents the number of players
     * @param romanNumber is an integer that identifies which of the two Common Goal Card stack it's been addressed
     * */
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

    /**
     * <p>
     *     This method does a <code>pop</code> on the stack of ScoringToken.
     * </p>
     * @return a ScoringToken */
    public ScoringToken popScoringToken(){
        return this.tokenStack.pop();
    }

    /**
     * <p>
     *     Method used in the initialization of the CommonGoalCard. It adds a ScoringToken to the stack.
     * </p>
     * @param scToken the ScoringToken that needs to be added o the stack*/
    public void pushScoringToken(ScoringToken scToken){
        this.tokenStack.push(scToken);
    }

    /**
     * <p>
     *     Method that return the stack.
     * </p>
     * @return a <code>Stack</code> of ScoringToken*/
    public Stack<ScoringToken> getTokenStack(){
        return this.tokenStack;
    }

    /**
     * <p>
     *     Method that return the romenNumber of a ScoringToken.
     * </p>
     * @return an int that represents the romanNumber of a ScoringToken*/
    public int getRomanNumber(){
       return this.romanNumber;
    }
    /**
     * <p>
     *     Method that sets the CGCStrategy based on the id.
     * </p>
     * @param id is an integer that represents the id of the card, needed for the Strategy Pattern
     * @throws IllegalStateException if the id doesn't match with any of the Strategy Pattern id
     */
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
    }

    /**
     * <p>
     *     Method that returns a specific CGCStrategy.
     * </p>
     * @return a CGCStrategy
     * @see model.CGCStrategy*/
    public CGCStrategy getStrategy(){ return this.strategy; }

    /**
     * <p>
     *     Method used to create a clone of the bookshelf in order to make controls on it without modifying the player bookshelf.
     * </p>
     * @param bookshelf is the bookshelf that has to be cloned
     * @return boolean <b>true</b> if the <code>compareRule</code> method of the strategy returns <i>true</i>, <b>false</b> otherwise*/
    // compare bookshelf and commonGoalCard
    public boolean compare(Bookshelf bookshelf)  {
        Tile [][] copy = new Tile[6][];

        for(int i = 0; i < 6; i++)
        {
            Tile[] aMatrix = bookshelf.getBookshelf()[i];
            copy[i] = new Tile[5];
            System.arraycopy(aMatrix, 0, copy[i], 0, 5);
        }
        Bookshelf mock = new Bookshelf();
        mock.setBookshelf(copy);
        return this.strategy.compareRule(mock, idCGC);
    }

    /**
     * <p>
     *     Method that returns the id of a CGC.
     * </p>
     * @return an int that represents the id of a specific CGC*/
    public int getIdCGC() {return idCGC;}
}
