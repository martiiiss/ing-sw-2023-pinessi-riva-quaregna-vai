package model;

public class ScoringToken {
    private final int romanNumber;
    private final int value;

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

