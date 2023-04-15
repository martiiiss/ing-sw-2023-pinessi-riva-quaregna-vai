package Model;

import model.Bag;
import model.Tile;
import model.Type;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    private Bag bag = new Bag();

    /*
    @Test
    void controlBag(){
        for(int i=0; i<6; i++){
            for(int j=0; j<22; j++){
                switch (i) {
                    case 0 -> {
                        assertEquals(Type.CAT, bag.getTilesContained().get(j).getType());
                        System.out.println((j + 22 * i) + " tipo " + bag.getTilesContained().get(j + 22 * i).getType() + bag.getTilesContained().get(j + 22 * i).getNumType());
                    }
                    case 1 -> {
                        assertEquals(Type.BOOK, bag.getTilesContained().get(j + 22 * i).getType());
                        System.out.println((j + 22 * i) + " tipo " + bag.getTilesContained().get(j + 22 * i).getType() + bag.getTilesContained().get(j + 22 * i).getNumType());
                    }
                }
            }
        }
    }
*/
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