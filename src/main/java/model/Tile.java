package model;

public class Tile {
    private Type type;
    private int NumType;

    private static final int MaxTiles = 22;

    public Type getType() {
        return this.type;
    }
    public void setType(Type t) {
        this.type = t;
    }

    public void setNumType(int n){
        this.NumType=n;
    }
    public int getNumType(){
        return this.NumType;
    }

}
