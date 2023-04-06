package Model;

import model.Bag;
import model.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    private Bag bag = new Bag();

    @Test
    void getBagTilesTest() {
        assertEquals(6, bag.getBagTiles(6).size());
        ArrayList<Tile> tiles = bag.getBagTiles(8);
        assertNotNull(tiles);
    }

    @Test
    void addTile() {
    } //TODO do this test
}