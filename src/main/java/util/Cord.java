package util;

import java.io.Serializable;

/**Class that represents a couple of coordinates; first element is the <b>x</b> coordinate, second element is the <b>y</b> coordinate. */
public class Cord implements Serializable {
    private int x;
    private int y;

    /**
     * <p>
     *     Constructor of the Class. <br>
     *     This initializes the two coordinates given the two parameters.
     * </p>
     * @param x an int that represents the x coordinate
     * @param y an int that represents the y coordinate*/
    public void setCords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * <p>
     *     Method that returns the value of the <b>x</b> coordinate.
     * </p>
     * @return an int, the value of the x*/
    public int getRowCord() {
        return this.x;
    }

    /**
     * <p>
     *     Method that returns the value of the <b>y</b> coordinate.
     * </p>
     * @return an int, the value of the y*/
    public int getColCord() {
        return this.y;
    }
}
