package model;

public class Tile {
    private Type type;
    private int NumType;

    private static final int MAX_TILES_ONE_TYPE = 22;

    public Tile (Type t){
        this.type =t;
    }

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
