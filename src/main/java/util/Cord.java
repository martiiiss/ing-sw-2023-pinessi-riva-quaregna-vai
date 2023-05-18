package util;

import java.io.Serializable;

public class Cord implements Serializable {
    private int x;
    private int y;

    public void setCords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getRowCord() {
        return this.x;
    }

    public int getColCord() {
        return this.y;
    }
}
