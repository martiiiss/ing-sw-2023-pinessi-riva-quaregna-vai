package model;

import java.io.Serial;
import java.io.Serializable;

/**This class represents the Tile*/
public class Tile implements Serializable {
    @Serial
    private static final long serialVersionUID = 8393623658151365163L;
    private Type type;
    private int numType;

    /**
     * <p>
     *     Constructor of the Class.<br>
     *     It initializes the Tile using two given parameters. It also calls the function <code>setNumType</code>.
     * </p>
     * @param t a <code>Type</code> of tile
     * @param numType an int in between 1 and 3 (for regular type) and 0 for <i>nothing</i> or <i>blocked</i> type*/
    public Tile (Type t, int numType)  {
        this.type =t;
        this.setNumType(numType);
    }

    /**
     * <p>
     *     Method that return a <code>Type</code> of a specific tile.
     * </p>
     * @return a type of tile*/
    public Type getType() {
        return this.type;
    }

    /**
     * <p>
     *     Method that sets the <code>type</code> of a specific tile.
     * </p>
     * @param t a <code>Type</code> of tile*/
    public void setType(Type t) {this.type = t;}

    /**
     * <p>
     *     Method that sets the <code>numType</code> of a specific tile.<br>
     *     <b>NOTE THAT:</b> this method can be invoked only to set regular tiles,
     *     <i>nothing</i> and <i>blocked</i> tiles aren't candidates to use this method.
     * </p>
     * @param n an int that represents the numType of a tile
     * @throws IllegalArgumentException when <i>n<0</i> or <i>n>3</i> */
    public void setNumType(int n) {
        if(n<0||n>3) throw new IllegalArgumentException("Unexpected value: "+n);
        this.numType=n;
    }

    /**
     * <p>
     *     Method that returns the <code>numType</code> of a specific tile.
     * </p>
     * @return an int that represents <code>numType</code>*/
    public int getNumType(){
        return this.numType;
    }

}
