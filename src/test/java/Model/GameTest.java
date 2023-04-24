package Model;

import model.Type;
import model.Tile;
import model.Player;
import model.CommonGoalCard;
import model.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    Game game = new Game();

    @Test
    void setCommonGoalCards() {
        game.setNumOfPlayers(2);
        game.setCommonGoalCards();
        ArrayList<CommonGoalCard> cgc = game.getCommonGoalCard();
        assertNotEquals(cgc.get(0), cgc.get(1));
    }

    @Test
    void getCommonGoalCard() {
        game.setCommonGoalCards();
        assertNotNull(game.getCommonGoalCard());
    }

    // Same test for these two methods
    @Test
    void setNumOfPlayers() {
        game.setNumOfPlayers(4);
        assertEquals(4, game.getNumOfPlayers());
    }

    @Test
    void getNumOfPlayers() {
        game.setNumOfPlayers(4);
        assertEquals(4, game.getNumOfPlayers());
    }

    Player player = new Player();

    // Same test for these two methods
    @Test
    void setPlayerInTurn() {
        game.setPlayerInTurn(player);
        assertEquals(player, game.getPlayerInTurn());
    }

    @Test
    void getPlayerInTurn() {
        game.setPlayerInTurn(player);
        assertEquals(player, game.getPlayerInTurn());
    }

    // Same test for these two methods
    @Test
    void setWinner() {
        game.setWinner(player);
        assertEquals(player, game.getWinner());
    }

    @Test
    void getWinner() {
        game.setWinner(player);
        assertEquals(player, game.getWinner());
    }

    // Same test for these two methods
    @Test
    void setFinisher() {
        game.setFinisher(player);
        assertEquals(player, game.getFinisher());
    }

    @Test
    void getFinisher() {
        game.setFinisher(player);
        assertEquals(player, game.getFinisher());
    }

    @Test
    void getIsLastTurn() {
        if (game.getIsLastTurn()) {
            assertTrue(game.getIsLastTurn());
        } else {
            assertFalse(game.getIsLastTurn());
        }
    }

    @Test
    void assignPersonalGoalCard() {
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        Player p4 = new Player();
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.addPlayer(p4);
        int id = 2;
        while (id < 5) {
            game.assignPersonalGoalCard(id);
            switch (id) {
                case 2: {
                    assertNotEquals(p1.getPersonalGoalCard(), p2.getPersonalGoalCard());
                    id++;
                }
                case 3: {
                    assertNotEquals(p1.getPersonalGoalCard(), p2.getPersonalGoalCard());
                    assertNotEquals(p1.getPersonalGoalCard(), p3.getPersonalGoalCard());
                    assertNotEquals(p2.getPersonalGoalCard(), p3.getPersonalGoalCard());
                    id++;
                }
                case 4: {
                    assertNotEquals(p1.getPersonalGoalCard(), p2.getPersonalGoalCard());
                    assertNotEquals(p1.getPersonalGoalCard(), p3.getPersonalGoalCard());
                    assertNotEquals(p1.getPersonalGoalCard(), p4.getPersonalGoalCard());
                    assertNotEquals(p2.getPersonalGoalCard(), p3.getPersonalGoalCard());
                    assertNotEquals(p2.getPersonalGoalCard(), p4.getPersonalGoalCard());
                    assertNotEquals(p3.getPersonalGoalCard(), p4.getPersonalGoalCard());
                    id++;
                }
            }
        }
    }

    @Test
    void addPlayer() {
    }


    @Test
    void checkCommonGoalCard() {
        //tests with initialize test -> only one player, the first one to take a scoring token,
        //using this function we can test every combination of CGC with every "winning" bookshelf,
        //there are 12 bookshelves written after this test, we can use those
        assertEquals(8,initializeTest(1,12,b12));
        assertEquals(0,initializeTest(12,7,b9));
        assertEquals(16,initializeTest(1,3,b1));

        //test with 3 players -> I try to get from the same CGC the scoring token
        //here I simulate a full turn with 3 players
        Player p2 = new Player();
        Player p3 = new Player();
        Player p4 = new Player();
        p2.setMyBookshelf();
        p3.setMyBookshelf();
        p4.setMyBookshelf();
        p2.getMyBookshelf().setBookshelf(b12);
        p3.getMyBookshelf().setBookshelf(b12);
        p4.getMyBookshelf().setBookshelf(b12);
        game.setNumOfPlayers(3);
        game.getCommonGoalCard().clear(); //this is only so that I can decide which CGC to test
        game.getCommonGoalCard().add(new CommonGoalCard(12,3,1));
        game.getCommonGoalCard().add(new CommonGoalCard(3,3,2));
        //the first player gets 8 points -> it is the one that starts the turn
        game.setPlayerInTurn(p2);
        assertEquals(8,game.checkCommonGoalCard());
        //the second player gets 6 points -> the second player in turn
        game.setPlayerInTurn(p3);
        assertEquals(6,game.checkCommonGoalCard());
        //the third and last player gets 4 points -> the last one in turn
        game.setPlayerInTurn(p4);
        assertEquals(4,game.checkCommonGoalCard());
    }

    //this function generalizes the test for a single player, it's useful to see if every CGC works
    private int initializeTest(int idCGC1, int idCGC2, Tile[][] playerBookshelf){
        Player p1 = new Player();
        game.setPlayerInTurn(p1);
        game.setNumOfPlayers(4);
        game.getCommonGoalCard().clear();//this is only so that I can decide which CGC to test
        p1.setMyBookshelf();
        game.getCommonGoalCard().add(new CommonGoalCard(idCGC1,4,1));
        game.getCommonGoalCard().add(new CommonGoalCard(idCGC2,4,2));
        p1.getMyBookshelf().setBookshelf(playerBookshelf);
        return game.checkCommonGoalCard();
    }


    Tile c = new Tile(Type.CAT, 1);
    Tile b = new Tile(Type.BOOK, 2);
    Tile g = new Tile(Type.GAME, 3);
    Tile f = new Tile(Type.FRAME, 2);
    Tile t = new Tile(Type.TROPHY, 1);
    Tile p = new Tile(Type.PLANT, 3);
    Tile n = new Tile(Type.NOTHING, 0);

    //the number of the bookshelf is linked to the CGC, for example: b1 is true for CGC1;
    //be careful, some of these matrices are true for more than one CGC
    Tile[][] b1 = {
            {f,f,n,f,f},
            {f,t,n,t,f},
            {p,g,t,t,f},
            {p,f,p,p,f},
            {p,f,p,p,c},
            {f,p,c,p,f}
    };//CGC1 & CGC3
    Tile[][] b2 = {
            {c,b,t,t,f},
            {g,c,b,f,f},
            {c,p,c,t,g},
            {g,g,b,c,t},
            {t,p,p,c,c},
            {t,p,c,b,b}
    };
    Tile[][] b3 = {
            {c,n,n,n,c},
            {c,c,g,b,g},
            {f,t,c,b,g},
            {p,t,c,c,b},
            {g,c,b,p,c},
            {c,g,g,p,c}
    };
    Tile[][] b4 = {
            {c,n,n,n,c},
            {p,c,g,c,g},
            {f,t,c,b,g},
            {c,c,c,c,c},
            {g,c,b,c,c},
            {c,p,c,p,c}
    };
    Tile[][] b5 = {
            {c,c,n,f,f},
            {c,t,t,t,f},
            {p,g,t,t,f},
            {p,f,p,p,f},
            {p,b,p,p,f},
            {p,p,c,p,c}
    };
    Tile[][] b6 = {
            {c,b,n,n,c},
            {p,c,g,c,g},
            {f,t,c,b,p},
            {t,g,c,c,t},
            {g,f,b,c,b},
            {b,p,c,p,f}
    };
    Tile[][] b7 = {
            {c,c,n,b,c},
            {c,c,n,b,b},
            {n,n,n,n,n},
            {n,n,n,n,n},
            {n,n,n,c,c},
            {c,n,n,c,c}
    };
    Tile[][] b8 = {
            {c,c,n,n,c},
            {c,c,g,c,c},
            {f,t,g,c,c},
            {c,c,c,c,c},
            {p,t,b,g,c},
            {c,t,g,p,f}
    };
    Tile[][] b9 = {
            {c,t,n,n,c},
            {p,c,g,c,c},
            {f,t,c,b,c},
            {c,c,c,c,c},
            {p,c,b,c,c},
            {c,t,c,p,c}
    };
    Tile[][] b10 = {
            {c,b,c,t,f},
            {g,c,b,f,f},
            {c,p,c,t,g},
            {g,g,b,t,t},
            {t,p,p,c,b},
            {t,p,c,b,b}
    };
    Tile[][] b11 = {
            {c,n,n,n,c},
            {p,c,g,b,g},
            {f,t,c,b,g},
            {p,t,c,c,c},
            {g,c,b,c,c},
            {c,g,c,p,c}
    };
    Tile[][] b12 = {
            {c,n,n,n,n},
            {c,c,n,n,n},
            {c,c,f,n,n,},
            {f,f,c,p,n,},
            {f,f,p,c,c,},
            {c,p,p,c,c}
    };
}
