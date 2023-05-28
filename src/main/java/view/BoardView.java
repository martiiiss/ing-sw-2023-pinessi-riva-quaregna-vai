package view;

import model.Board;
import model.Type;


import javax.swing.*;

import java.awt.*;





public class BoardView extends JInternalFrame{

    private JInternalFrame boardDisplayed;
    private JButton [][] boardTiles;
    public BoardView (Board board){ //creates and initializes the board section of the GUI
        ImageReader imageReader = new ImageReader();
        boardDisplayed = new ImagePanel("Board", imageReader.readImage("resources/livingroomResized.png", 467, 467), 9,9, 2, 2);

        boardTiles= new JButton[9][9];
        for(int row=0; row<9; row++)
            for(int column=0; column<9; column++) {
                if(board.getSelectedType(row, column)!= model.Type.BLOCKED) {
                    boardTiles[row][column] = new JButton();
                    boardTiles[row][column].setPreferredSize(new Dimension(50, 50));
                    boardDisplayed.add(boardTiles[row][column]);
                    boardTiles[row][column].setIcon(imageReader.readIcon("resources/TileImages/" + board.getSelectedType(row, column) + "/" + board.getSelectedNumType(row, column) + ".png", 50, 50));
                }
                else {
                    boardTiles[row][column] = new JButton();
                    boardTiles[row][column].setPreferredSize(new Dimension(50, 50));
                    boardDisplayed.add(boardTiles[row][column]);
                    boardTiles[row][column].setOpaque(false);
                    boardTiles[row][column].setContentAreaFilled(false);
                    boardTiles[row][column].setBorderPainted(false);

                }

            }
        boardDisplayed.setVisible(true);
        Dimension d= new Dimension(500,530);
        boardDisplayed.setMinimumSize(d);

    }

    public JInternalFrame getBoardDisplayed() {
        return boardDisplayed;
    }

    public void pickTile(int row, int column) { // set the tile from the board in the given position to nothing
        ImageReader imageReader = new ImageReader();
        this.boardTiles[row][column].setIcon(imageReader.readIcon("resources/TileImages/NOTHING.png", 50, 50));
    }

    public void updateBoard(Board board){ //method that checks all the board and update the GUI
        ImageReader imageReader = new ImageReader();
        for(int row=0; row<9; row++)
            for(int column=0; column<9; column++) {
                if(board.getSelectedType(row, column)!= Type.BLOCKED){
                    if(board.getSelectedType(row, column) == Type.NOTHING)
                        boardTiles[row][column].setIcon(imageReader.readIcon("resources/TileImages/NOTHING.png", 50, 50));
                    else
                        boardTiles[row][column].setIcon(imageReader.readIcon("resources/TileImages/" + board.getSelectedType(row, column) + "/" + board.getSelectedNumType(row, column) + ".png", 50, 50));
                }
            }
    }

}