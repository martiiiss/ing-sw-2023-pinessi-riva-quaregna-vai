package util;

import model.Tile;
import java.io.Serial;
import java.io.Serializable;

/**Class that represents the instance of Tile passed to update the Bookshelf*/
public class TileForMessages implements Serializable {
    @Serial
    private static final long serialVersionUID = -2387426531023024704L;
    private final Object object;
    private final int row;
    private final int column;
    private final Tile tile;

    /**
     * Constructor of the Class.<br>
     * This sets the given parameters.
     * @param object an {@code Object} that represents the parameter that has to be passed
     * @param row an int that represents the Bookshelf row
     * @param column  an int that represents the Bookshelf column
     * @param tile the tile that has to be passed*/
    public TileForMessages(Object object, int row, int column, Tile tile){
        this.object = object;
        this.row = row;
        this.column = column;
        this.tile = tile;
    }

    /**
     * Method used to get the object.
     * @return an {@code Object}*/
    public Object getObject() {
        return object;
    }

    /**
     * Method used to get the column.
     * @return an int that represents the column*/
    public int getColumn() {
        return column;
    }

    /**
     * Method used to get a tile.
     * @return a {@link Tile}*/
    public Tile getTile() {
        return tile;
    }
}
