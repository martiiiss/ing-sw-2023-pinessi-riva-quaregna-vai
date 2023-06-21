package org.example;

import model.*;
import view.GUI.GUIView;
import util.Cord;

import java.io.IOException;
import java.util.ArrayList;

public class AppGUITry {
    static GUIView view;
    static Board board;

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
        view.askTiles();
        ArrayList <Cord> array;
        array = view.getTilesClient();
        System.out.println(array.get(0).getRowCord());
        ArrayList<Tile> a = new ArrayList<>();
        Tile b = new Tile(Type.BOOK, 1);
        a.add(b);
        view.pickTiles(array, a);

    }
}
