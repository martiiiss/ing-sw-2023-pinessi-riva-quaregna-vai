package controller;

import model.*;
import org.junit.jupiter.api.Test;
import util.Cord;
import util.Event;
import view.GUI.GUIView;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static util.Event.*;

class ControllerTest {
private Controller controller = new Controller();
private void creationOfGame(){
    Player p1 = new Player();
    Player p2 = new Player();
    controller.createGame();
    controller.getInstanceOfGame().setNumOfPlayers(2);
    controller.getInstanceOfGame().addPlayer(p1);
    controller.getInstanceOfGame().addPlayer(p2);
    controller.initializeGame();
}
    @Test
    void chooseNickname() {
        Event empty = controller.chooseNickname("");
        Event ok = controller.chooseNickname("pippo");
        Event notAvailable = controller.chooseNickname("pippo");
        assertEquals(empty, EMPTY_NICKNAME);
        assertEquals(ok, OK);
        assertEquals(notAvailable, NOT_AVAILABLE);
    }

    @Test
    void chooseUserInterface() {
        Event tui = controller.chooseUserInterface(1);
        Event gui = controller.chooseUserInterface(2);
        Event invalid = controller.chooseUserInterface(10);
        assertEquals(tui, TUI_VIEW);
        assertEquals(gui, GUI_VIEW);
        assertEquals(invalid, INVALID_VALUE);
    }

    @Test
    void checkBoardToBeFilled() {
        creationOfGame();
        Bag bag = controller.getBag();
        Board board = controller.getBoard();
        for (int i = 0; i< board.BOARD_ROW; i++){
            for (int j = 0; j < board.BOARD_COLUMN; j++){
                if(board.getBoard()[i][j].getType()!=Type.BLOCKED) {
                    board.getBoard()[i][j].setType(Type.NOTHING);
                }
            }
        }
        controller.checkBoardToBeFilled();

        for (int i = 0; i< board.BOARD_ROW; i++){
            for (int j = 0; j < board.BOARD_COLUMN; j++){
                if(board.getBoard()[i][j].getType()!=Type.BLOCKED) {
                    board.getBoard()[i][j].setType(Type.NOTHING);
                }
            }
        }
        board.getBoard()[1][3].setType(Type.CAT);
        controller.checkBoardToBeFilled();

        for (int i = 0; i< board.BOARD_ROW; i++){
            for (int j = 0; j < board.BOARD_COLUMN; j++){
                if(board.getBoard()[i][j].getType()!=Type.BLOCKED) {
                    board.getBoard()[i][j].setType(Type.NOTHING);
                }
            }
        }
        board.getBoard()[1][3].setType(Type.CAT);
        bag.getBagTiles(30);

        //controller.checkBoardToBeFilled();
    }

    @Test
    void chooseTiles() {
        creationOfGame();
        Cord temp = new Cord();
        Cord temp1 = new Cord();
        ArrayList<Cord> cords = new ArrayList<>();
        //OUT OF BOUNDS
        temp.setCords(9,10);
        cords.add(temp);
        assertEquals(controller.chooseTiles(cords), OUT_OF_BOUNDS);

        cords.clear();

        //REPETITION
        temp.setCords(1,3);
        temp1.setCords(1,3);
        cords.add(temp);
        cords.add(temp1);
        assertEquals(controller.chooseTiles(cords),REPETITION);

        cords.clear();

        //BLOCKED OR NOTHING
        temp.setCords(0,0);
        cords.add(temp);
        assertEquals(controller.chooseTiles(cords),BLOCKED_NOTHING);

        cords.clear();

        //NOT ON BORDER
        temp.setCords(4,3);
        cords.add(temp);
        assertEquals(controller.chooseTiles(cords),NOT_ON_BORDER);

        cords.clear();

        //NOT ADJACENT
        temp.setCords(1,4);
        temp1.setCords(2,5);
        cords.add(temp);
        cords.add(temp1);
        assertEquals(controller.chooseTiles(cords),NOT_ADJACENT);

        cords.clear();
        //OK
        temp.setCords(1,3);
        cords.add(temp);
        assertEquals(controller.chooseTiles(cords),OK);
    }


    @Test
    void chooseColumn() {
    creationOfGame();
    Tile cat = new Tile(Type.CAT,3);
    Tile nothing = new Tile(Type.NOTHING,0);
    controller.setNumberOfChosenTiles(2);
    //full bookshelf
    Tile [][] bks1 = new Tile[][]  {{cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat}};
    controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().setBookshelf(bks1);
    assertEquals(controller.chooseColumn(-1), INVALID_VALUE);
    assertEquals(controller.chooseColumn(0),OUT_OF_BOUNDS);
    //regular bookshelf
    Tile [][] bks2 = new Tile[][]  {{nothing, nothing, nothing,nothing, cat},
                                    {nothing, nothing, nothing, cat, cat},
                                    {nothing, nothing, cat, cat, cat},
                                    {nothing, cat, cat, cat, cat},
                                    {nothing, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat}};
    controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().setBookshelf(bks2);
    assertEquals(controller.chooseColumn(0),OK);
    }

    @Test
    void chooseTilesDisposition() {
    creationOfGame();
    Tile tile1 = new Tile(Type.TROPHY,3);
    Tile tile2 = new Tile(Type.PLANT, 1);
    ArrayList<Tile> tiles = new ArrayList<>();
    tiles.add(tile1);
    tiles.add(tile2);
    controller.setPlayerHand(tiles);
    Tile cat = new Tile(Type.CAT,3);
    Tile nothing = new Tile(Type.NOTHING,0);

    Tile [][] bks2 = new Tile[][]  {{nothing, nothing, nothing,nothing, cat},
                                    {nothing, nothing, nothing, cat, cat},
                                    {nothing, nothing, cat, cat, cat},
                                    {nothing, cat, cat, cat, cat},
                                    {nothing, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat}};
    controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().setBookshelf(bks2);
    assertEquals(controller.chooseTilesDisposition(-1), INVALID_VALUE);
    assertEquals(controller.chooseTilesDisposition(0),OK);
    }

    @Test
    void calculateScore() {
    creationOfGame();
    controller.getInstanceOfGame().getPlayerInTurn().updateScore(8+2+6);
    controller.calculateScore();
    }

    @Test
    void checkIfGameEnd() {
    Player p1 = new Player();
    Player p2 = new Player();
    controller.createGame();
    controller.getInstanceOfGame().setNumOfPlayers(2);
    controller.getInstanceOfGame().addPlayer(p1);
    controller.getInstanceOfGame().addPlayer(p2);
    controller.initializeGame();
    Tile cat = new Tile(Type.CAT,3);
    Tile nothing = new Tile(Type.NOTHING,0);
    Tile [][] bks1 = new Tile[][]  {{cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat}};

    Tile [][] bks2 = new Tile[][]  {{nothing, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat},
                                    {cat, cat, cat, cat, cat}};

    //A player didn't fill the bookshelf
    controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().setBookshelf(bks2);
    assertEquals(controller.checkIfGameEnd(),OK);

    //The last player is the first one to finish
    controller.getInstanceOfGame().setPlayerInTurn(p2);
    controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().setBookshelf(bks1);
    assertEquals(controller.checkIfGameEnd(),GAME_OVER);

    controller.getInstanceOfGame().setLastTurn(false);

    //The first player is the first one to finish
    controller.getInstanceOfGame().setPlayerInTurn(p1);
    controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().setBookshelf(bks1);
    assertEquals(controller.checkIfGameEnd(),LAST_TURN);

    controller.getInstanceOfGame().setLastTurn(true);

    //The first player is the first one to finish
    controller.getInstanceOfGame().setPlayerInTurn(p1);
    controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().setBookshelf(bks1);
    assertEquals(controller.checkIfGameEnd(),LAST_TURN);

    //The last player is the first one to finish
    controller.getInstanceOfGame().setPlayerInTurn(p2);
    controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().setBookshelf(bks1);
    assertEquals(controller.checkIfGameEnd(),GAME_OVER);
    }

    @Test
    void goToNext() {
    Player p1 = new Player();
    Player p2 = new Player();
    controller.createGame();
    controller.getInstanceOfGame().setNumOfPlayers(2);
    controller.getInstanceOfGame().addPlayer(p1);
    controller.getInstanceOfGame().addPlayer(p2);
    controller.initializeGame();

    controller.goToNext(p1);
    controller.goToNext(p2);
    }


    @Test
    void getTilesFromBoard() {
    creationOfGame();
    Cord c = new Cord();
    ArrayList<Cord> cords = new ArrayList<>();
    c.setCords(1,3);
    cords.add(c);
    controller.setPlayerCords(cords);
    Type test = controller.getBoard().getSelectedType(1,3);
    assertEquals(controller.getTilesFromBoard().get(0).getType(), test);
    }

    @Test
    void updateController() {
    Player p1 = new Player();
    Player p2 = new Player();
    Cord c = new Cord();
    ArrayList<Cord> cords = new ArrayList<>();
    c.setCords(1,3);
    cords.add(c);
    controller.createGame();
    controller.getInstanceOfGame().setNumOfPlayers(2);
    controller.getInstanceOfGame().addPlayer(p1);

    assertEquals(controller.updateController(null,ALL_CONNECTED),WAIT);
    controller.getInstanceOfGame().addPlayer(p2);
    assertEquals(controller.updateController(2,ASK_NUM_PLAYERS),OK);
    assertEquals(controller.updateController("", SET_NICKNAME),EMPTY_NICKNAME);
    assertEquals(controller.updateController(1, CHOOSE_VIEW), TUI_VIEW);
    assertEquals(controller.updateController(null, ALL_CONNECTED),OK);
    assertEquals(controller.updateController(2, TURN_AMOUNT),OK);
    assertEquals(controller.updateController(cords,TURN_PICKED_TILES), OK);
    assertEquals(controller.updateController(2,TURN_COLUMN),OK);
    assertEquals(controller.updateController(-1,TURN_POSITION),INVALID_VALUE);
    assertEquals(controller.updateController(null,END_OF_TURN),OK);
    assertEquals(controller.updateController(0,CHECK_MY_TURN),NOT_YOUR_TURN);
    assertEquals(controller.updateController(1,CHECK_MY_TURN),OK);
    assertEquals(controller.updateController(null,CHECK_REFILL),BOARD_NOT_EMPTY);
    assertEquals(controller.updateController(null, CHECK_ENDGAME),OK);
    }

    @Test
    void getControllerModel() {
        Player p1 = new Player();
        Player p2 = new Player();
        Cord c = new Cord();
        ArrayList<Cord> cords = new ArrayList<>();
        c.setCords(1,3);
        cords.add(c);
        controller.createGame();
        controller.getInstanceOfGame().setNumOfPlayers(2);
        controller.getInstanceOfGame().addPlayer(p1);
        controller.getInstanceOfGame().addPlayer(p2);
        controller.getInstanceOfGame().getPlayers().get(0).setNickname("pippo");

        assertEquals(controller.getControllerModel(SET_INDEX,"pippo"),0);
        assertEquals(controller.getControllerModel(GAME_BOARD,null),controller.getBoard());
        assertEquals(controller.getControllerModel(GAME_PLAYERS,null),controller.getInstanceOfGame().getPlayers());
        assertEquals(controller.getControllerModel(GAME_CGC,null),controller.getInstanceOfGame().getCommonGoalCard());
        //assertEquals(controller.getControllerModel(GAME_PGC,null),controller.getInstanceOfGame().getPlayers().get(0).getPersonalGoalCard());
        assertEquals(controller.getControllerModel(GAME_PIT,null),controller.getInstanceOfGame().getPlayers().indexOf(controller.getInstanceOfGame().getPlayerInTurn()));

    }

}