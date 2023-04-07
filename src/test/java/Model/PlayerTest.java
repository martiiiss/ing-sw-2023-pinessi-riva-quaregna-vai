package Model;
import model.*;
import model.Type;
import model.PersonalGoalCard;
import model.Bookshelf;
import model.Tile;
import model.Player;
import model.Game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    @Test
    void setAsFirstPlayer() {
    }//TODO do this test

    @Test
    void setNickname() {
    }//TODO do this test

    @Test
    void getNickname() {
    }//TODO do this test

    @Test
    void setMyBookshelf() {
    }//TODO do this test -> already tested

    @Test
    void getMyBookshelf() {
    }//TODO do this test -> already tested

    @Test
    void getScore() {
    }//TODO do this test

    @Test
    void updateScore() {
    }//TODO do this test

    @Test
    void setPersonalGoalCard() {
    }//TODO do this test -> already tested

    @Test
    void getPersonalGoalCard() {
    }//TODO do this test

    @Test
    void setCompletePGC() {
    }//TODO do this test -> already tested

    @Test
    void getCompletePGC() {
    }//TODO do this test

    @Test
    void getScoringToken1() {
    }//TODO do this test

    @Test
    void setScoringToken1() {
    }//TODO do this test

    @Test
    void getScoringToken2() {
    }

    @Test
    void setScoringToken2() {
    }//TODO do this test

    @Test
    void getTilesInHand() {
    }//TODO do this test

    @Test
    void setTilesInHand() {
    }//TODO do this test

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
    };

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
//z
        Bookshelf b = player.getMyBookshelf();
        b.setBookshelf(bookshelfAdj);
        assertEquals(player.checkAdjacentBookshelf(),5);
    }//TODO do this test

    @Test
    void checkBookshelf() {
    }//TODO do this test
}
