package model;

public class Tile {
    private Type type;
    private int numType;

    public Tile (Type t, int numType){
        this.type =t;
        this.setNumType(numType);
    }

    public Type getType() {
        return this.type;
    }
    public void setType(Type t) {
        this.type = t;
    }

    public void setNumType(int n){
        this.numType=n;
    }

    public int getNumType(){
        return this.numType;
    }

}
