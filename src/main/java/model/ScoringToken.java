package model;

import jdk.jshell.spi.ExecutionControl;

/* how do we manage the scoring token creation?*/

public class ScoringToken {
    private int romanNumber;
    private int value;

    public ScoringToken(int val, int rn){  //constructor
        this.value = val;
        this.romanNumber = rn;
    }
    /*returns the value of scoring token*/
    public int getValue(){
        return this.value;
    }

    /*returns the roman number of the scoring token*/
    public int getRomanNumber(){
        return this.romanNumber;
    }


}

