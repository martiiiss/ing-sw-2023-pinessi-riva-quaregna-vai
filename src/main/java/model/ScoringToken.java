package model;

import java.io.Serial;
import java.io.Serializable;
/**Class that represents the Scoring Token*/
public class ScoringToken implements Serializable {
    @Serial
    private static final long serialVersionUID = 4785265365456310765L;
    private final int romanNumber;
    private final int value;

    /**
     * <p>
     *     Constructor of the Class.<br>
     *     Given two int it initializes a ScoringToken.
     * </p>
     * @param rn an int that represents the <code>romanNumber</code> that identifies a ScoringToken
     * @param val an int that represents the <code>value</code> given to a ScoringToken*/
    public ScoringToken(int val, int rn){  //constructor
        this.value = val;
        this.romanNumber = rn;
    }

    /**
     * <p>
     *     Method that return the <code>value</code> of a specific ScoringToken
     * </p>
     * @return an int that represents the <code>value</code>*/
    public int getValue(){
        return this.value;
    }

    /**
     * <p>
     *     Method that return the <code>romanNumber</code> of a specific ScoringToken
     * </p>
     * @return an int that represents the <code>romanNumber</code>*/
    public int getRomanNumber(){
        return this.romanNumber;
    }
}

