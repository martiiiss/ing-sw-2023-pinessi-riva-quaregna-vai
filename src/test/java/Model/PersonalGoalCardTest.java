package Model;

import model.Tile;
import org.junit.jupiter.api.Test;
import model.PersonalGoalCard;
import model.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class PersonalGoalCardTest {
    @Test
    void readerJSONTest() {
        PersonalGoalCard personalGoalCard1 = new PersonalGoalCard(1);
        PersonalGoalCard personalGoalCard2 = new PersonalGoalCard(2);
        PersonalGoalCard personalGoalCard3 = new PersonalGoalCard(3);
        PersonalGoalCard personalGoalCard4 = new PersonalGoalCard(4);
        PersonalGoalCard personalGoalCard5 = new PersonalGoalCard(5);
        PersonalGoalCard personalGoalCard6 = new PersonalGoalCard(6);
        PersonalGoalCard personalGoalCard7 = new PersonalGoalCard(7);
        PersonalGoalCard personalGoalCard8 = new PersonalGoalCard(8);
        PersonalGoalCard personalGoalCard9 = new PersonalGoalCard(9);
        PersonalGoalCard personalGoalCard10 = new PersonalGoalCard(10);

        Tile cat = new Tile(Type.CAT, 1);
        Tile book = new Tile(Type.BOOK, 2);
        Tile game = new Tile(Type.GAME, 3);
        Tile frame = new Tile(Type.FRAME, 2);
        Tile trophy = new Tile(Type.TROPHY, 1);
        Tile plant = new Tile(Type.PLANT, 3);
        Tile nothing = new Tile(Type.NOTHING, 0);

        Tile[][] pgc1 = personalGoalCard1.getPGC();
        Tile[][] pgc2 = personalGoalCard2.getPGC();
        Tile[][] pgc3 = personalGoalCard3.getPGC();
        Tile[][] pgc4 = personalGoalCard4.getPGC();
        Tile[][] pgc5 = personalGoalCard5.getPGC();
        Tile[][] pgc6 = personalGoalCard6.getPGC();
        Tile[][] pgc7 = personalGoalCard7.getPGC();
        Tile[][] pgc8 = personalGoalCard8.getPGC();
        Tile[][] pgc9 = personalGoalCard9.getPGC();
        Tile[][] pgc10 = personalGoalCard10.getPGC();

        Tile[][] bookshelf1 = {
                {plant, nothing, frame, nothing, nothing},
                {nothing, nothing, nothing, nothing, cat},
                {nothing, nothing, nothing, book, nothing},
                {nothing, game, nothing, nothing, nothing},
                {nothing, nothing, nothing, nothing, nothing},
                {nothing, nothing, trophy, nothing, nothing}};
        Tile[][] bookshelf2 = {
                {nothing, nothing, nothing, nothing, nothing},
                {nothing, plant, nothing, nothing, nothing},
                {cat, nothing, game, nothing, nothing},
                {nothing, nothing, nothing, nothing, book},
                {nothing, nothing, nothing, trophy, nothing},
                {nothing, nothing, nothing, nothing, frame}};
        Tile[][] bookshelf3 = {
                {nothing, nothing, nothing, nothing, nothing},
                {frame, nothing, nothing, game, nothing},
                {nothing, nothing, plant, nothing, nothing},
                {nothing, cat, nothing, nothing, nothing},
                {nothing, nothing, nothing, nothing, trophy},
                {book, nothing, nothing, nothing, nothing}};
        Tile[][] bookshelf4 = {
                {nothing, nothing, nothing, nothing, game},
                {nothing, nothing, nothing, nothing, nothing},
                {trophy, nothing, frame, nothing, nothing},
                {nothing, nothing, nothing, plant, nothing},
                {nothing, book, cat, nothing, nothing},
                {nothing, nothing, nothing, nothing, nothing}};
        Tile[][] bookshelf5 = {
                {nothing, nothing, nothing, nothing, nothing},
                {nothing, trophy, nothing, nothing, nothing},
                {nothing, nothing, nothing, nothing, nothing},
                {nothing, frame, book, nothing, nothing},
                {nothing, nothing, nothing, plant, nothing},
                {game, nothing, nothing, cat, nothing}};
        Tile[][] bookshelf6 = {
                {nothing, nothing, trophy, nothing, cat},
                {nothing, nothing, nothing, nothing, nothing},
                {nothing, nothing, nothing, book, nothing},
                {nothing, nothing, nothing, nothing, nothing},
                {nothing, game, nothing, frame, nothing},
                {plant, nothing, nothing, nothing, nothing}};
        Tile[][] bookshelf7 = {
                {cat, nothing, nothing, nothing, nothing},
                {nothing, nothing, nothing, frame, nothing},
                {nothing, plant, nothing, nothing, nothing},
                {trophy, nothing, nothing, nothing, nothing},
                {nothing, nothing, nothing, nothing, game},
                {nothing, nothing, book, nothing, nothing}};
        Tile[][] bookshelf8 = {
                {nothing, nothing, nothing, nothing, frame},
                {nothing, cat, nothing, nothing, nothing},
                {nothing, nothing, trophy, nothing, nothing},
                {plant, nothing, nothing, nothing, nothing},
                {nothing, nothing, nothing, book, nothing},
                {nothing, nothing, nothing, game, nothing}};
        Tile[][] bookshelf9 = {
                {nothing, nothing, game, nothing, nothing},
                {nothing, nothing, nothing, nothing, nothing},
                {nothing, nothing, cat, nothing, nothing},
                {nothing, nothing, nothing, nothing, book},
                {nothing, trophy, nothing, nothing, plant},
                {frame, nothing, nothing, nothing, nothing}};
        Tile[][] bookshelf10 = {
                {nothing, nothing, nothing, nothing, trophy},
                {nothing, game, nothing, nothing, nothing},
                {book, nothing, nothing, nothing, nothing},
                {nothing, nothing, nothing, cat, nothing},
                {nothing, frame, nothing, nothing, nothing},
                {nothing, nothing, nothing, plant, nothing}};
        Tile[][] bookshelfNot = {
                {plant, nothing, nothing, frame, nothing},
                {nothing, nothing, nothing, nothing, cat},
                {nothing, nothing, nothing, book, nothing},
                {nothing, plant, nothing, nothing, nothing},
                {nothing, nothing, nothing, nothing, nothing},
                {nothing, nothing, trophy, nothing, nothing}};

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(bookshelf1[i][j].getType(), pgc1[i][j].getType());
                assertEquals(bookshelf2[i][j].getType(), pgc2[i][j].getType());
                assertEquals(bookshelf3[i][j].getType(), pgc3[i][j].getType());
                assertEquals(bookshelf4[i][j].getType(), pgc4[i][j].getType());
                assertEquals(bookshelf5[i][j].getType(), pgc5[i][j].getType());
                assertEquals(bookshelf6[i][j].getType(), pgc6[i][j].getType());
                assertEquals(bookshelf7[i][j].getType(), pgc7[i][j].getType());
                assertEquals(bookshelf8[i][j].getType(), pgc8[i][j].getType());
                assertEquals(bookshelf9[i][j].getType(), pgc9[i][j].getType());
                assertEquals(bookshelf10[i][j].getType(), pgc10[i][j].getType());
                assertNotSame(bookshelfNot[i][j],pgc1[i][j].getType());
            }
        }
    }
}