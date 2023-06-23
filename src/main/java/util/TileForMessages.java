package util;

import model.Tile;

import java.io.Serializable;

public class TileForMessages implements Serializable {
    private static final long serialVersionUID = -2387426531023024704L;
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
