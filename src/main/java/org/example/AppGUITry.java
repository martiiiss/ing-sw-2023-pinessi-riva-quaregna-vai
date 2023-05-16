package org.example;

import model.Bag;
import model.Board;
import model.Game;
import model.Tile;
import view.BoardView;

import java.io.IOException;
import java.util.ArrayList;

public class AppGUITry {
    static BoardView view;
    static Board board;

    public static void main(String[] args) throws IOException {
        Bag bag = new Bag();
        board = new Board(4);
        Game game = new Game();
        game.setNumOfPlayers(4);
        board.setNumOfCells(4);
        game.setCommonGoalCards();
        ArrayList<Tile> tiles = bag.getBagTiles(board.getNumOfCells());
        board.setUpBoard(tiles);//filled the board
        view = new BoardView(board, game);

    }
}
