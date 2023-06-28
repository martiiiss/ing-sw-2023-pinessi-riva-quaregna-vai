package org.example;

import model.*;
import util.Event;
import view.GUI.GUIView;
import util.Cord;

import java.io.IOException;
import java.util.ArrayList;

public class AppGUITry {
    static GUIView view;
    static Board board;
//AA
    public static void main(String[] args) throws IOException {
        Bag bag = new Bag();
        board = new Board(3);
        board.initializeBoard(3);
        Game game = new Game();
        game.setNumOfPlayers(4);
        game.setCommonGoalCards();
        ArrayList<Tile> tiles = bag.getBagTiles(board.getNumOfCells());
        board.setUpBoard(tiles);//filled the board
        view = new GUIView();
        view.updateBoard(board);
        view.showError(Event.COLUMN_NOT_VALID,null);
        view.askTiles();
        ArrayList <Cord> array;
        array = view.getTilesClient();
        System.out.println(array.get(0).getRowCord());
        ArrayList<Tile> a = new ArrayList<>();
        Tile b = new Tile(Type.BOOK, 1);
        a.add(b);
        view.pickTiles(array, a);
        int i= view.chooseColumn();
        System.out.println(i);
        System.out.println(i);
        i = view.chooseTile();
        System.out.println(i);
        view.addTile(b);

    }
}
