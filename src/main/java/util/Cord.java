package util;

public class Cord {
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
