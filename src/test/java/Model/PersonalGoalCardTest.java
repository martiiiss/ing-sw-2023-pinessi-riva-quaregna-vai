package Model;

import org.junit.jupiter.api.Test;
import model.PersonalGoalCard;
import model.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonalGoalCardTest {

    final PersonalGoalCard pgc1 = new PersonalGoalCard(1);
    final PersonalGoalCard pgc2 = new PersonalGoalCard(2);
    final PersonalGoalCard pgc3 = new PersonalGoalCard(3);
    final PersonalGoalCard pgc4 = new PersonalGoalCard(4);
    final PersonalGoalCard pgc5 = new PersonalGoalCard(5);
    final PersonalGoalCard pgc6 = new PersonalGoalCard(6);
    final PersonalGoalCard pgc7 = new PersonalGoalCard(7);
    final PersonalGoalCard pgc8 = new PersonalGoalCard(8);
    final PersonalGoalCard pgc9 = new PersonalGoalCard(9);
    final PersonalGoalCard pgc10 = new PersonalGoalCard(10);
    final PersonalGoalCard pgc11 = new PersonalGoalCard(11);
    final PersonalGoalCard pgc12 = new PersonalGoalCard(12);

    @Test
    void readerJSONTest() {
        int[][] card1 = {{0,0},{0,2},{1,4},{2,3},{3,1},{5,2}}; //position
        Type[] tileType1 = {Type.PLANT, Type.FRAME, Type.CAT, Type.BOOK, Type.GAME, Type.TROPHY};

        int[][] card2 = {{1,1},{2,0},{2,2},{3,4},{4,3},{5,4}}; //position
        Type[] tileType2 = {Type.PLANT, Type.CAT, Type.GAME, Type.BOOK, Type.TROPHY, Type.FRAME};

        int[][] card3 = {{1,0},{1, 3},{2,2},{3,1},{3,4},{5,0}}; //position
        Type[] tileType3 = {Type.FRAME, Type.GAME, Type.PLANT, Type.CAT, Type.TROPHY, Type.BOOK};

        int[][] card4 = {{0,4},{2,0},{2,2},{3,3},{4,1},{4,2}}; //position
        Type[] tileType4 = {Type.GAME, Type.TROPHY, Type.FRAME, Type.PLANT, Type.BOOK, Type.CAT};

        int[][] card5 = {{1,1},{3,1},{3,2},{4,4},{5,0},{5,3}}; //position
        Type[] tileType5 = {Type.TROPHY, Type.FRAME, Type.BOOK, Type.PLANT, Type.GAME, Type.CAT};

        int[][] card6 = {{0,2},{0,4},{2,3},{4,1},{4,3},{5,0}}; //position
        Type[] tileType6 = {Type.TROPHY, Type.CAT, Type.BOOK, Type.GAME, Type.FRAME, Type.PLANT};

        int[][] card7 = {{0,0},{1,3},{2,1},{3,0},{4,4},{5,2}}; //position
        Type[] tileType7 = {Type.CAT, Type.FRAME, Type.PLANT, Type.TROPHY, Type.GAME, Type.BOOK};

        int[][] card8 = {{0,4},{1,1},{2,2},{3,0},{4,3},{5,3}}; //position
        Type[] tileType8 = {Type.FRAME, Type.CAT, Type.TROPHY, Type.PLANT, Type.BOOK, Type.GAME};

        int[][] card9 = {{0,2},{2,2},{3,4},{4,1},{4,4},{5,0}}; //position
        Type[] tileType9 = {Type.GAME, Type.CAT, Type.BOOK, Type.TROPHY, Type.PLANT, Type.FRAME};

        int[][] card10 = {{0,4},{1,1},{2,0},{3,3},{4,1},{5,3}}; //position
        Type[] tileType10 = {Type.TROPHY, Type.GAME, Type.BOOK, Type.CAT, Type.FRAME, Type.PLANT};

        int[][] card11 = {{0,2},{1,1},{2,0},{3,2},{4,4},{5,3}}; //position
        Type[] tileType11 = {Type.PLANT, Type.BOOK, Type.GAME, Type.FRAME, Type.CAT, Type.TROPHY};

        int[][] card12 = {{0,2},{1,1},{2,2},{3,3},{4,4},{5,0}}; //position
        Type[] tileType12 = {Type.BOOK, Type.PLANT, Type.FRAME, Type.TROPHY, Type.GAME, Type.CAT};



        for(int r=0; r<6; r++){
            for(int c=0; c<2; c++){
                assertEquals(card1[r][c], pgc1.getPosition()[r][c]);
                assertEquals(card2[r][c], pgc2.getPosition()[r][c]);
                assertEquals(card3[r][c], pgc3.getPosition()[r][c]);
                assertEquals(card4[r][c], pgc4.getPosition()[r][c]);
                assertEquals(card5[r][c], pgc5.getPosition()[r][c]);
                assertEquals(card6[r][c], pgc6.getPosition()[r][c]);
                assertEquals(card7[r][c], pgc7.getPosition()[r][c]);
                assertEquals(card8[r][c], pgc8.getPosition()[r][c]);
                assertEquals(card9[r][c], pgc9.getPosition()[r][c]);
                assertEquals(card10[r][c], pgc10.getPosition()[r][c]);
                assertEquals(card11[r][c], pgc11.getPosition()[r][c]);
                assertEquals(card12[r][c], pgc12.getPosition()[r][c]);
            }
        }
        for(int i=0; i<6; i++){
            assertEquals(tileType1[i], pgc1.getTileType()[i]);
            assertEquals(tileType2[i], pgc2.getTileType()[i]);
            assertEquals(tileType3[i], pgc3.getTileType()[i]);
            assertEquals(tileType4[i], pgc4.getTileType()[i]);
            assertEquals(tileType5[i], pgc5.getTileType()[i]);
            assertEquals(tileType6[i], pgc6.getTileType()[i]);
            assertEquals(tileType7[i], pgc7.getTileType()[i]);
            assertEquals(tileType8[i], pgc8.getTileType()[i]);
            assertEquals(tileType9[i], pgc9.getTileType()[i]);
            assertEquals(tileType10[i], pgc10.getTileType()[i]);
            assertEquals(tileType11[i], pgc11.getTileType()[i]);
            assertEquals(tileType12[i], pgc12.getTileType()[i]);
        }
    }
}