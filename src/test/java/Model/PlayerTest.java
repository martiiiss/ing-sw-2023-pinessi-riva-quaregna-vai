package Model;

import model.Type;
import model.PersonalGoalCard;
import model.Bookshelf;
import model.Tile;
import model.Player;

import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    Player p = new Player();
    @Test
    void setAsFirstPlayer() {
    }//TODO do this test -> there isn't a get method to return the boolean, this method cannot be tested

    //The next two methods are tested in the same way
    @Test
    void setNickname() {
        p.setNickname("Foo");
        assertEquals("Foo",p.getNickname());
        assertThrows(IllegalArgumentException.class, ()->p.setNickname(""));
    }
    @Test
    void getNickname() {
        p.setNickname("Foo");
        assertEquals("Foo",p.getNickname());
    }

    @Test
    void setMyBookshelf() {
        p.setMyBookshelf();
        assertNotNull(p.getMyBookshelf());
    }
    @Test
    void getMyBookshelf() {
    } //I don't think is really useful to test this, it has already been tested

    //the two methods are tested together
    @Test
    void getScore() {
    }
    @Test
    void updateScore() {
        p.updateScore(15);
        assertEquals(15,p.getScore());
    }

    @Test
    void setPersonalGoalCard() {
        PersonalGoalCard pgc1 = new PersonalGoalCard(5);
        p.setPersonalGoalCard(pgc1);
        assertEquals(pgc1, p.getPersonalGoalCard());
    }
    @Test
    void getPersonalGoalCard() {
        PersonalGoalCard pgc1 = new PersonalGoalCard(5);
        p.setPersonalGoalCard(pgc1);
        assertEquals(pgc1, p.getPersonalGoalCard());
    }//same test as the method before

    @Test
    void setCompletePGC() {
        p.setCompletePGC();
        assertTrue(p.getCompletePGC());
    }
    @Test
    void getCompletePGC() {
        p.setCompletePGC();
        assertTrue(p.getCompletePGC());
    }//tested in the same way as the method before

    @Test
    void getScoringToken1() {
        p.setScoringToken1();
        assertTrue(p.getScoringToken1());
    }

    @Test
    void setScoringToken1() {
        p.setScoringToken1();
        assertTrue(p.getScoringToken1());
    }

    @Test
    void getScoringToken2() {
        p.setScoringToken2();
        assertTrue(p.getScoringToken2());
    }

    @Test
    void setScoringToken2() {
        p.setScoringToken2();
        assertTrue(p.getScoringToken2());
    }

    @Test
    void getTilesInHand() {
        Tile t1 = new Tile(Type.CAT,3);
        Tile t2 = new Tile(Type.PLANT,2);
        Tile t3 = new Tile(Type.FRAME,1);
        ArrayList<Tile> chosenOnes = new ArrayList<>();
        chosenOnes.add(t1);
        chosenOnes.add(t2);
        chosenOnes.add(t3);
        p.setTilesInHand(chosenOnes);
        assertNotNull(chosenOnes);
        assertEquals(t1, chosenOnes.get(0));
        assertNotEquals(t1, chosenOnes.get(2));
        assertEquals(t3, chosenOnes.get(2));
    }
    @Test
    void setTilesInHand() {
    }//Tested with the method before this one

    Tile cat = new Tile(Type.CAT,1);
    Tile book = new Tile(Type.BOOK,2);
    Tile game = new Tile(Type.GAME,3);
    Tile frame = new Tile(Type.FRAME,2);
    Tile trophy = new Tile(Type.TROPHY,1);
    Tile plant = new Tile(Type.PLANT,3);
    Tile nothing = new Tile(Type.NOTHING,0);

    Tile[][] bookshelf1 = { {plant, nothing, frame, nothing, nothing},
                            {nothing, nothing, nothing, nothing, cat},
                            {nothing, nothing, nothing, book, nothing},
                            {nothing, game, nothing, nothing, nothing},
                            {nothing,nothing,nothing,nothing,nothing},
                            {nothing, nothing, trophy, nothing, nothing}};
    Tile[][] bookshelf2 = { {nothing, nothing, nothing, nothing, nothing},
                            {nothing, plant, nothing, nothing, nothing},
                            {cat, nothing, game, nothing, nothing},
                            {nothing, nothing, nothing, nothing, book},
                            {nothing,nothing,nothing,trophy,nothing},
                            {nothing, nothing, nothing, nothing, frame}};
    Tile[][] bookshelf3 = { {nothing, nothing, nothing, nothing, nothing},
                            {frame, nothing, nothing, game, nothing},
                            {nothing, nothing, plant, nothing, nothing},
                            {nothing, cat, nothing, nothing, trophy},
                            {nothing,nothing,nothing,nothing,nothing},
                            {book, nothing, nothing, nothing, nothing}};

    Tile[][] bookshelf4 = { {nothing, nothing, nothing, nothing, game},
                            {nothing, nothing, nothing, nothing, nothing},
                            {trophy, nothing, frame, nothing, nothing},
                            {nothing, nothing, nothing, plant, nothing},
                            {nothing,book,cat,nothing,nothing},
                            {nothing, nothing, nothing, nothing, nothing}};
    Tile[][] bookshelf5 = { {nothing, nothing, nothing, nothing, nothing},
                            {nothing, trophy, nothing, nothing, nothing},
                            {nothing, nothing, nothing, nothing, nothing},
                            {nothing, frame, book, nothing, nothing},
                            {nothing,nothing,nothing,nothing,plant},
                            {game, nothing, nothing, cat, nothing}};
    Tile[][] bookshelf6 = { {nothing, nothing, trophy, nothing, cat},
                            {nothing, nothing, nothing, nothing, nothing},
                            {nothing, nothing, nothing, book, nothing},
                            {nothing, nothing, nothing, nothing, nothing},
                            {nothing,game,nothing,frame,nothing},
                            {plant, nothing, nothing, nothing, nothing}};
    Tile[][] bookshelf7 = { {cat, nothing, nothing, nothing, nothing},
                            {nothing, nothing, nothing, frame, nothing},
                            {nothing, plant, nothing, nothing, nothing},
                            {trophy, nothing, nothing, nothing, nothing},
                            {nothing,nothing,nothing,nothing,game},
                            {nothing, nothing, book, nothing, nothing}};
    Tile[][] bookshelf8 = { {nothing, nothing, nothing, nothing, frame},
                            {nothing, cat, nothing, nothing, nothing},
                            {nothing, nothing, trophy, nothing, nothing},
                            {plant, nothing, nothing, nothing, nothing},
                            {nothing,nothing,nothing,book,nothing},
                            {nothing, nothing, nothing, game, nothing}};
    Tile[][] bookshelf9 = { {nothing, nothing, game, nothing, nothing},
                            {nothing, nothing, nothing, nothing, nothing},
                            {nothing, nothing, cat, nothing, nothing},
                            {nothing, nothing, nothing, nothing, book},
                            {nothing,trophy,nothing,nothing,plant},
                            {frame, nothing, nothing, nothing, nothing}};
    Tile[][] bookshelf10 = {{nothing, nothing, nothing, nothing, trophy},
                            {nothing, game, nothing, nothing, nothing},
                            {book, nothing, nothing, nothing, nothing},
                            {nothing, nothing, nothing, cat, nothing},
                            {nothing,frame,nothing,nothing,nothing},
                            {nothing, nothing, nothing, plant, nothing}};
    Tile[][] bookshelf11 = {{nothing, nothing, plant, nothing, nothing},
                            {nothing, book, nothing, nothing, nothing},
                            {game, nothing, nothing, nothing, nothing},
                            {nothing, nothing, frame, nothing, nothing},
                            {nothing,nothing,nothing,nothing,cat},
                            {nothing, nothing, nothing, trophy, nothing}};
    Tile[][] bookshelf12 = {{nothing, nothing, book, nothing, nothing},
                            {nothing, plant, nothing, nothing, nothing},
                            {nothing, nothing, frame, nothing, nothing},
                            {nothing, nothing, nothing, trophy, nothing},
                            {nothing,nothing,nothing,nothing,game},
                            {cat, nothing, nothing, nothing, nothing}};
    private final Player player = new Player();
    Bookshelf bks = null;
    @Test
    void checkCompletePGC() {
        PersonalGoalCard pgc1 = new PersonalGoalCard(1);
        PersonalGoalCard pgc2 = new PersonalGoalCard(2);
        PersonalGoalCard pgc3 = new PersonalGoalCard(3);
        PersonalGoalCard pgc4 = new PersonalGoalCard(4);
        PersonalGoalCard pgc5 = new PersonalGoalCard(5);
        PersonalGoalCard pgc6 = new PersonalGoalCard(6);
        PersonalGoalCard pgc7 = new PersonalGoalCard(7);
        PersonalGoalCard pgc8 = new PersonalGoalCard(8);
        PersonalGoalCard pgc9 = new PersonalGoalCard(9);
        PersonalGoalCard pgc10 = new PersonalGoalCard(10);
        PersonalGoalCard pgc11 = new PersonalGoalCard(11);
        PersonalGoalCard pgc12 = new PersonalGoalCard(12);
        int id=1;
        while(id<13){
            player.setMyBookshelf();
            bks = player.getMyBookshelf();
            assignName(id);
            PersonalGoalCard pgc = new PersonalGoalCard(id);
            player.setPersonalGoalCard(pgc);
            assertEquals(player.checkCompletePGC(),12);
            id++;
        }
    }//TODO check different cases of this test
    //method only used in test
    private void assignName(int id) {
        switch (id){
            case 1 -> bks.setBookshelf(bookshelf1);
            case 2 -> bks.setBookshelf(bookshelf2);
            case 3 -> bks.setBookshelf(bookshelf3);
            case 4 -> bks.setBookshelf(bookshelf4);
            case 5 -> bks.setBookshelf(bookshelf5);
            case 6 -> bks.setBookshelf(bookshelf6);
            case 7 -> bks.setBookshelf(bookshelf7);
            case 8 -> bks.setBookshelf(bookshelf8);
            case 9 -> bks.setBookshelf(bookshelf9);
            case 10 -> bks.setBookshelf(bookshelf10);
            case 11 -> bks.setBookshelf(bookshelf11);
            case 12 -> bks.setBookshelf(bookshelf12);
        }
    }

    Tile[][] bookshelfAdj = {
            {cat, cat, frame, nothing, nothing},
            {cat, nothing, nothing, nothing, cat},
            {nothing, nothing, nothing, book, nothing},
            {nothing, game, game, nothing, nothing},
            {nothing,trophy,nothing,nothing,nothing},
            {trophy, trophy, trophy, nothing, nothing}};
    @Test
    void checkAdjacentBookshelf() {
        player.setMyBookshelf();
        Bookshelf b = player.getMyBookshelf();
        b.setBookshelf(bookshelfAdj);
        assertEquals(player.checkAdjacentBookshelf(),5);
        assertNotEquals(player.checkAdjacentBookshelf(),5);
        assertEquals(player.checkAdjacentBookshelf(),0);
        Tile[][] bookshelfAdj = {
                {cat, cat, frame, nothing, nothing},
                {cat, nothing, nothing, nothing, cat},
                {nothing, nothing, nothing, book, nothing},
                {nothing, game, game, nothing, nothing},
                {nothing,trophy,trophy,nothing,nothing},
                {trophy, trophy, trophy, nothing, nothing}};
        b = player.getMyBookshelf();
        b.setBookshelf(bookshelfAdj);
        assertEquals(player.checkAdjacentBookshelf(),7);
        Tile[][] bookshelfAdj2 = {
                {cat, cat, frame, nothing, cat},
                {cat, nothing, nothing, nothing, cat},
                {nothing, nothing, book, book, cat},
                {nothing, game, game, nothing, nothing},
                {nothing,trophy,trophy,nothing,nothing},
                {trophy, trophy, trophy, nothing, nothing}};
        b = player.getMyBookshelf();
        b.setBookshelf(bookshelfAdj2);
        assertEquals(player.checkAdjacentBookshelf(),9);
    }//TODO do this test

    @Test
    void checkBookshelf() {
        Player p1 = new Player();
        Player p2 = new Player();
        p1.setMyBookshelf();
        p2.setMyBookshelf();
        p1.getMyBookshelf().setBookshelf(full);
        assertTrue(p1.checkBookshelf());
        p2.getMyBookshelf().setBookshelf(notFull);
        assertFalse(p2.checkBookshelf());
    }
    Tile[][] full = {
            {cat, plant, frame, plant, game},
            {cat, trophy, cat, game, plant},
            {plant, cat, trophy, plant, cat,},
            {frame, trophy, cat, frame, cat,},
            {frame, frame, plant, cat, frame,},
            {cat, plant, trophy, cat, plant}
    };

    Tile[][] notFull = {
            {cat, nothing, frame, nothing, game},
            {cat, nothing, cat, game, plant},
            {plant, cat, trophy, plant, cat,},
            {frame, trophy, cat, frame, cat,},
            {frame, frame, plant, cat, frame,},
            {cat, plant, trophy, cat, plant}
    };

}
