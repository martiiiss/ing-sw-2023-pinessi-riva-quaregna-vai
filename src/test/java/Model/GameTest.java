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
                    int points = 0;
                    if (game.getCommonGoalCard().get(0).compare(game.getPlayerInTurn().getMyBookshelf())) {
                        points += game.getCommonGoalCard().get(0).popScoringToken().getValue();
                    }
                    if (game.getCommonGoalCard().get(1).compare(game.getPlayerInTurn().getMyBookshelf())){
                        points += game.getCommonGoalCard().get(1).popScoringToken().getValue();
                    }
                    assertEquals(points, game.checkCommonGoalCard());
                }
                case 2 -> {
                    game.setPlayerInTurn(p2);
                    int points = 0;
                    if (game.getCommonGoalCard().get(0).compare(game.getPlayerInTurn().getMyBookshelf())) {
                        points += game.getCommonGoalCard().get(0).popScoringToken().getValue();
                    }
                    if (game.getCommonGoalCard().get(1).compare(game.getPlayerInTurn().getMyBookshelf())){
                        points += game.getCommonGoalCard().get(1).popScoringToken().getValue();
                    }
                    assertEquals(points, game.checkCommonGoalCard());
                }
                case 3 -> {
                    int points = 0;
                    if (game.getCommonGoalCard().get(0).compare(game.getPlayerInTurn().getMyBookshelf())) {
                        points += game.getCommonGoalCard().get(0).popScoringToken().getValue();
                    }
                    if (game.getCommonGoalCard().get(1).compare(game.getPlayerInTurn().getMyBookshelf())){
                        points += game.getCommonGoalCard().get(1).popScoringToken().getValue();
                    }
                    assertEquals(points, game.checkCommonGoalCard());
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
            {c,c,b,t,f},
            {c,t,b,f,f},
            {g,p,p,t,g},
            {g,g,c,c,t},
            {t,p,p,c,b},
            {t,p,c,b,b}
    };
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
            {p,c,g,b,g},
            {f,t,c,b,g},
            {p,t,c,c,b},
            {g,c,b,f,c},
            {c,g,t,p,c}
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
            {c,c,n,b,b},
            {c,c,n,b,b},
            {n,n,n,n,n},
            {n,n,n,n,n},
            {n,n,n,c,c},
            {n,n,n,c,c}
    };
    Tile[][] b8 = {
            {c,t,n,n,c},
            {p,c,g,c,c},
            {f,t,g,b,c},
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
            {n,n,n,n,n},
            {c,n,n,n,n},
            {p,c,n,n,n,},
            {f,t,c,n,n,},
            {f,f,p,c,n,},
            {c,p,t,c,p}
    };
}
