package util;

import model.Tile;

public class TileForMessages {
    private Object object;
    private int row;
    private int column;

    private Tile tile;

    public TileForMessages(Object object, int row, int column, Tile tile){
        this.object = object;
        this.row = row;
        this.column = column;
        this.tile = tile;
    }


    public Object getObject() {
        return object;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Tile getTile() {
        return tile;
    }
}
