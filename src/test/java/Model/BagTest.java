package Model;

import model.Bag;
import model.Tile;
import model.Type;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    private Bag bag = new Bag();

    @Test
    void getBagTilesTest() {
        assertEquals(6, bag.getBagTiles(6).size());
        assertEquals(126, bag.getTilesContained().size());

        bag = new Bag();

        ArrayList<Tile> tiles = bag.getBagTiles(10); //take 10 random Tiles
        assertNotNull(tiles);

        for(int i=0; i<10; i++){
            assertNotNull(tiles.get(i));
            assertEquals(-1, bag.getTilesContained().indexOf(tiles.get(i))); //tile is not contained in bag
        }

    }

    @Test
    void addTileTest() {
        Tile cat = new Tile(Type.CAT, 1);
        bag.addTile(cat); //cat is insert in position 132
        assertEquals( cat, bag.getTilesContained().get(132));
    }
}