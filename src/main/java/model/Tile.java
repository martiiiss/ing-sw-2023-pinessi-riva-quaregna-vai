package model;

import java.io.Serializable;

public class Tile implements Serializable {
    private static final long serialVersionUID = 8393623658151365163L;
    private Type type;
    private int numType;

    public Tile (Type t, int numType)  {
        this.type =t;
        this.setNumType(numType);
    }

    public Type getType() {
        return this.type;
    }
    public void setType(Type t) {this.type = t;}

    public void setNumType(int n) {
        if(n<0||n>3) throw new IllegalArgumentException("Unexpected value: "+n);
        this.numType=n;
    }

    public int getNumType(){
        return this.numType;
    }

}
