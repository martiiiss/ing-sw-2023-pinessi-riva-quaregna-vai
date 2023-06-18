package Model;

import model.Bookshelf;
import model.Tile;
import model.Type;
import org.junit.jupiter.api.Test;
import static model.Type.CAT;
import static model.Type.NOTHING;
import static org.junit.jupiter.api.Assertions.*;

public class BookshelfTest {

    Bookshelf bks = new Bookshelf();


    @Test
    void placeTileTest() {
        int columns, j;
        j = 0;
        Tile cat = new Tile(Type.CAT, 1);
        columns = (int) (Math.random() * 5);
        while (bks.getBookshelf()[j + 1][columns].getType() == Type.NOTHING) {
            j++;
            if (j == 5)
                break;
        }
        bks.placeTile(columns, cat);
        assertEquals(cat.getType(), bks.getBookshelf()[j][columns].getType());
    }

    @Test
    void GetNumOfFreeSlotsTest(){

        //Here I create the bookshelf used to test the method
        for(int i=0;i<6;i++){
                bks.getBookshelf()[i][4] = new Tile(CAT,1);
                bks.getBookshelf()[i][3] = new Tile(CAT, 2);
                bks.getBookshelf()[i][2] = new Tile(CAT, 2);
                bks.getBookshelf()[i][1] = new Tile(CAT, 2);
                bks.getBookshelf()[i][0] = new Tile(CAT, 2);
            }
        assertEquals(0, bks.getNumOfFreeSlots()); //testing a full bookshelf
        bks.getBookshelf()[5][0] = new Tile(NOTHING,0);//The available slot
        assertEquals(1, bks.getNumOfFreeSlots()); //testing a bookshelf with one slot available

    }

    @Test
    void GetStatusTest(){
        //This is a full Bookshelf
        for(int i=0;i<6;i++){
            bks.getBookshelf()[i][4] = new Tile(CAT,1);
            bks.getBookshelf()[i][3] = new Tile(CAT, 2);
            bks.getBookshelf()[i][2] = new Tile(CAT, 2);
            bks.getBookshelf()[i][1] = new Tile(CAT, 2);
            bks.getBookshelf()[i][0] = new Tile(CAT, 2);
        }
       assertTrue(bks.getStatus());

        bks.getBookshelf()[5][0] = new Tile(NOTHING,0);//I set to nothing one of the slots
        assertFalse(bks.getStatus());
    }




}
