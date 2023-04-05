package Model;

import org.junit.jupiter.api.Test;
import model.PersonalGoalCard;
import model.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonalGoalCardTest {

    final PersonalGoalCard pgc = new PersonalGoalCard(5);
    @Test
    void readerJSONTest() {
        int card5[][] = {{1,1},{3,1},{3,2},{4,4},{5,0},{5,3}}; //position
        for(int r=0; r<6; r++){
            for(int c=0; c<2; c++){
                assertEquals(card5[r][c], pgc.getPosition()[r][c]);
            }
        }
        Type[] tileType5 = {Type.TROPHY, Type.FRAME, Type.BOOK, Type.PLANT, Type.GAME, Type.CAT};
        for(int i=0; i<6; i++){
            assertEquals(tileType5[i], pgc.getTileType()[i]);
        }
    }
}