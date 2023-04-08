package Model;

import model.Tile;
import model.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {
    Tile tile = new Tile(Type.NOTHING,0);
    @Test
    void getType() {
        tile.setType(Type.PLANT);
        assertEquals(Type.PLANT,tile.getType());
    }

    @Test
    void setType() {
        tile.setType(Type.PLANT);
        assertEquals(Type.PLANT,tile.getType());
    }

    @Test
    void setNumType() {
        tile.setNumType(3);
        assertEquals(3,tile.getNumType());
        Tile t = new Tile(Type.CAT, 2);
        assertThrows(IllegalArgumentException.class, ()->t.setNumType(-1));
        assertThrows(IllegalArgumentException.class, ()->t.setNumType(15));
    }

    @Test
    void getNumType(){
        tile.setNumType(3);
        assertEquals(3,tile.getNumType());
    }//Same test as the method before
}