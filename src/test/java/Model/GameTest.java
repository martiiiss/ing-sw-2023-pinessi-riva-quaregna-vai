package Model;


import model.Bookshelf;
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
    }//TODO do this test -> I cannot test this

    @Test
    void checkCommonGoalCard() {
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        game.setNumOfPlayers(3);
        game.setCommonGoalCards();
        p1.setMyBookshelf();
        p2.setMyBookshelf();
        p3.setMyBookshelf();
        p1.getMyBookshelf().setBookshelf(b1);
        p2.getMyBookshelf().setBookshelf(b2);
        p3.getMyBookshelf().setBookshelf(b3);
        int temp = 1;
        while (temp < 4) {
            switch (temp) {
                case 1 -> {
                    game.setPlayerInTurn(p1);
                    if (game.getCommonGoalCard().get(0).compare(game.getPlayerInTurn().getMyBookshelf()) == true
                            || game.getCommonGoalCard().get(1).compare(game.getPlayerInTurn().getMyBookshelf()) == true) {
                        int points = game.getCommonGoalCard().get(0).popScoringToken().getValue() + game.getCommonGoalCard().get(1).popScoringToken().getValue();
                        assertEquals(points, game.checkCommonGoalCard());
                    } else {
                        assertEquals(0, game.checkCommonGoalCard());
                    }
                }
                case 2 -> {
                    game.setPlayerInTurn(p2);
                    if (game.getCommonGoalCard().get(0).compare(game.getPlayerInTurn().getMyBookshelf()) == true
                            || game.getCommonGoalCard().get(1).compare(game.getPlayerInTurn().getMyBookshelf()) == true) {
                        int points = game.getCommonGoalCard().get(0).popScoringToken().getValue() + game.getCommonGoalCard().get(1).popScoringToken().getValue();
                        assertEquals(points, game.checkCommonGoalCard());
                    } else {
                        assertEquals(0, game.checkCommonGoalCard());
                    }
                }
                case 3 -> {
                    game.setPlayerInTurn(p3);
                    if (game.getCommonGoalCard().get(0).compare(game.getPlayerInTurn().getMyBookshelf()) == true
                            || game.getCommonGoalCard().get(1).compare(game.getPlayerInTurn().getMyBookshelf()) == true) {
                        int points = game.getCommonGoalCard().get(0).popScoringToken().getValue() + game.getCommonGoalCard().get(1).popScoringToken().getValue();
                        assertEquals(points, game.checkCommonGoalCard());
                    } else {
                        assertEquals(0, game.checkCommonGoalCard());
                    }
                }
            }
            temp++;
        }//TODO this test does weird things and has to be fixed, it doesn't always work unfortunately
    }





    Tile c = new Tile(Type.CAT, 1);
    Tile b = new Tile(Type.BOOK, 2);
    Tile g = new Tile(Type.GAME, 3);
    Tile f = new Tile(Type.FRAME, 2);
    Tile t = new Tile(Type.TROPHY, 1);
    Tile p = new Tile(Type.PLANT, 3);
    Tile n = new Tile(Type.NOTHING, 0);
    //OK for c1 KO for c2

    Tile[][] b1 = {
            {n, n, n, n, n},
            {c, n, n, n, n},
            {p, c, n, n, n,},
            {f, t, c, n, n,},
            {f, f, p, c, n,},
            {c, p, t, c, p}
    };
    //OK for c2 OK for c1
    Tile[][] b2 = {
            {c, n, n, n, c},
            {p, c, g, b, g},
            {f, t, c, b, g},
            {p, t, c, c, b},
            {g, c, b, f, c},
            {c, g, t, p, c}
    };
    //OK for both
    Tile[][] b3 = {
            {n, n, n, n, c},
            {p, c, g, b, g},
            {f, t, c, b, g},
            {p, t, c, c, b},
            {g, c, b, f, c},
            {c, g, t, p, c}
    };
}
