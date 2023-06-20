package Model;

import model.Type;
import model.PersonalGoalCard;
import model.Bookshelf;
import model.Tile;
import model.Player;

import org.junit.jupiter.api.Test;
import view.UserView;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    Player p = new Player();
    @Test
    void setAsFirstPlayer() {
    }

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
        p.setMyBookshelf(new Bookshelf());
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
    //the next 12 matrices are used to test if the 12 PGC do work properly
    Tile[][] bookshelf1 = { {plant, nothing, frame, nothing, nothing},
                            {nothing, nothing, nothing, nothing, cat},
                            {nothing, nothing, nothing, book, nothing},
                            {nothing, game, nothing, nothing, nothing},
                            {nothing,nothing,nothing,nothing,nothing},
                            {nothing, nothing, trophy, nothing, nothing}};
    /*
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

     */


    //The next 6 matrices are used to test if I get the correct amount of points while the PGC isn't completed
    Tile[][] bookshelfpt0 = { {nothing, nothing, nothing, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing}};

    Tile[][] bookshelfpt1 = { {plant,   nothing, nothing, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing}};

    Tile[][] bookshelfpt2 = { {plant, nothing, frame, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing,nothing,nothing,nothing,nothing},
                              {nothing, nothing, nothing, nothing, nothing}};

    Tile[][] bookshelfpt4 = { {plant, nothing, frame, nothing, nothing},
                              {nothing, nothing, nothing, nothing, cat},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing,nothing,nothing,nothing,nothing},
                              {nothing, nothing, nothing, nothing, nothing}};

    Tile[][] bookshelfpt6 = { {plant, nothing, frame, nothing, nothing},
                              {nothing, nothing, nothing, nothing, cat},
                              {nothing, nothing, nothing, book, nothing},
                              {nothing, nothing, nothing, nothing, nothing},
                              {nothing,nothing,nothing,nothing,nothing},
                              {nothing, nothing, nothing, nothing, nothing}};

    Tile[][] bookshelfpt9 = { {plant, nothing, frame, nothing, nothing},
                              {nothing, nothing, nothing, nothing, cat},
                              {nothing, nothing, nothing, book, nothing},
                              {nothing, game, nothing, nothing, nothing},
                              {nothing,nothing,nothing,nothing,nothing},
                              {nothing, nothing, nothing, nothing, nothing}};
    private final Player player = new Player();
    private final Player p2 = new Player();
    Bookshelf bks = null;
    @Test
    void checkCompletePGC() {
        //here I test if I actually can get 12 points if the PGC is completed
        player.setMyBookshelf(new Bookshelf());
        bks = player.getMyBookshelf();
        bks.setBookshelf(bookshelf1);
        UserView u = new UserView();
        u.showTUIBookshelf(bks);
        //6 tiles in correct position
        PersonalGoalCard pgcPlayer = new PersonalGoalCard(1);
        player.setPersonalGoalCard(pgcPlayer);
        assertEquals(12, player.checkCompletePGC());

        //here I test the progression in points
        PersonalGoalCard pgc = new PersonalGoalCard(1);
        p2.setPersonalGoalCard(pgc);
        p2.setMyBookshelf(new Bookshelf());
        bks = p2.getMyBookshelf();
        //0 tiles in correct position
        bks.setBookshelf(bookshelfpt0);
        assertEquals(0, p2.checkCompletePGC());
        //1 tiles in correct position
        bks.setBookshelf(bookshelfpt1);
        assertEquals(1, p2.checkCompletePGC());
        //2 tiles in correct position
        bks.setBookshelf(bookshelfpt2);
        assertEquals(1, p2.checkCompletePGC());
        //3 tiles in correct position
        bks.setBookshelf(bookshelfpt4);
        assertEquals(2, p2.checkCompletePGC());
        //4 tiles in correct position
        bks.setBookshelf(bookshelfpt6);
        assertEquals(2, p2.checkCompletePGC());
        //5 tiles in correct position
        bks.setBookshelf(bookshelfpt9);
        assertEquals(3, p2.checkCompletePGC());
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
        player.setMyBookshelf(new Bookshelf());
        Bookshelf b = player.getMyBookshelf();
        b.setBookshelf(bookshelfAdj);
        assertEquals(player.checkAdjacentBookshelf(),5);
        //assertEquals(player.checkAdjacentBookshelf(),0);
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
    }

    @Test
    void checkBookshelf() {
        Player p1 = new Player();
        Player p2 = new Player();
        p1.setMyBookshelf(new Bookshelf());
        p2.setMyBookshelf(new Bookshelf());
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
