package org.example;

import model.Bag;
import model.Board;
import model.Tile;
import view.BoardView;

import java.io.IOException;
import java.util.ArrayList;

public class AppGUITry {
    static BoardView view;
    static Board board;

    public static void main(String[] args) throws IOException {
        Bag bag = new Bag();
        board = new Board(2);
        board.setNumOfCells(2);
        ArrayList<Tile> tiles = bag.getBagTiles(board.getNumOfCells());
        board.setUpBoard(tiles);//filled the board
        view = new BoardView(board);

    }
}
