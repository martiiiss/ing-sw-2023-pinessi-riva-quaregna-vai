package view.GUI;

import model.Board;
import model.Type;
import util.*;
import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**Class that represents the frame of the board in the Graphic User Interface*/
public class BoardView implements Serializable {
    @Serial
    private static final long serialVersionUID = 4758892564965792652L;
    private int tilesPicked = -1;
    private boolean canPick;
    private final JInternalFrame boardDisplayed;
    private final JButton [][] boardTiles;
    private final ArrayList<Cord> listTilesPicked = new ArrayList<>();

    /**
     * Constructor of the Class.
     * This creates and initializes the board section of the GUI */
    public BoardView (){
        ImageReader imageReader = new ImageReader();
        boardDisplayed = new ImagePanel("Board", imageReader.readImage("resources/livingroomResized.png", 467, 467), 9,9, 2, 2);
        boardTiles= new JButton[9][9];
        for(int row=0; row<9; row++)
            for(int column=0; column<9; column++) {
                    boardTiles[row][column] = new JButton();
                    boardTiles[row][column].setPreferredSize(new Dimension(50, 50));
                    boardDisplayed.add(boardTiles[row][column]);
                    boardTiles[row][column].setOpaque(false);
                    boardTiles[row][column].setContentAreaFilled(false);
                    boardTiles[row][column].setBorderPainted(false);
                    boardTiles[row][column].putClientProperty("row", row);
                    boardTiles[row][column].putClientProperty("column", column);
                    boardTiles[row][column].addActionListener(e -> {
                        if(canPick && tilesPicked >0){
                            Cord tile = new Cord();
                            JButton button = (JButton) e.getSource();
                            tile.setCords((int)button.getClientProperty("row"),(int) button.getClientProperty("column"));
                            listTilesPicked.add(tile);
                            tilesPicked--;
                            if(tilesPicked ==0)
                                synchronized (this){
                                    notify();
                                }
                        }
                    });

                }
        boardDisplayed.setVisible(true);
        Dimension d= new Dimension(500,530);
        boardDisplayed.setMinimumSize(d);
    }

    /**
     * Method used to get the JInternalFrame relative to the Board.
     * @return a {@link JInternalFrame}*/
    public JInternalFrame getBoardDisplayed() {
        return boardDisplayed;
    }

    /**
     * Method used to set the tile from the board in the given position to nothing.
     * @param column an int that represents a column
     * @param row an int that represents a row*/
    public void pickTile(int row, int column) {
            this.boardTiles[row][column].setIcon(null);
            this.boardTiles[row][column].setOpaque(false);
            this.boardTiles[row][column].setContentAreaFilled(false);
            this.boardTiles[row][column].setBorderPainted(false);
    }

    /**
     * Method that checks all the board and update the GUI.
     * @param board the {@code Board} that needs to be updated*/
    public void updateBoard(Board board){
        ImageReader imageReader = new ImageReader();
        for(int row=0; row<9; row++)
            for(int column=0; column<9; column++) {
                if(board.getSelectedType(row, column)!= Type.BLOCKED){
                    if(board.getSelectedType(row, column) != Type.NOTHING){
                        boardTiles[row][column].setIcon(imageReader.readIcon("resources/TileImages/" + board.getSelectedType(row, column) + "/" + board.getSelectedNumType(row, column) + ".png", 50, 50));
                        this.boardTiles[row][column].setOpaque(true);
                        this.boardTiles[row][column].setContentAreaFilled(true);
                        this.boardTiles[row][column].setBorderPainted(true);
                    }
                    else{
                        this.boardTiles[row][column].setIcon(null);
                        this.boardTiles[row][column].setOpaque(false);
                        this.boardTiles[row][column].setContentAreaFilled(false);
                        this.boardTiles[row][column].setBorderPainted(false);
                    }
                }
            }
    }

    /**
     * Method used to set a tile to one that can be picked.
     * @param canPick is a boolean, <b>true</b> if a tile can be picked, <b>false</b> otherwise*/
    public void setCanPick(boolean canPick) {
        this.canPick = canPick;
    }

    /**
     * Method used to return the number of tiles picked.
     * @return an int, the number of tiles that can be picked*/
    public int getTilesPicked() {
        return tilesPicked;
    }

    /**
     *Method used to set the number of tiles picked.
     *@param i an int that represents the number of tiles picked  */
    public void setTilesPicked(int i){
        this.tilesPicked = i;
    }

    /**
     * Method that returns the list of picked tiles.
     * @return an {@code ArrayList} of {@code Tiles} that represents the picked tiles*/
    public ArrayList<Cord> getListTilesPicked() {
        return listTilesPicked;
    }

}
