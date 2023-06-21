package view.GUI;

import distributed.messages.Message;
import model.Board;
import model.Type;
import util.*;
import util.Event;


import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;


public class BoardView {
    private int tilesPicked = -1;
    private boolean canPick;
    private JInternalFrame boardDisplayed;
    private JButton [][] boardTiles;
    private ArrayList<Cord> listTilesPicked = new ArrayList<>();
    public BoardView (){ //creates and initializes the board section of the GUI
        ImageReader imageReader = new ImageReader();
        boardDisplayed = new ImagePanel("Board", imageReader.readImage("resources/livingroomResized.png", 467, 467), 9,9, 2, 2);
        boardTiles= new JButton[9][9];
        this.listTilesPicked = new ArrayList<>();

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
                            System.out.println("tile aggiunta " + tile);
                        }
                    });

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
            this.boardTiles[row][column].setOpaque(false);
            this.boardTiles[row][column].setContentAreaFilled(false);
            this.boardTiles[row][column].setBorderPainted(false);
            //if(tilesPicked==3)
               // setCanPick(false);
    }
    public void updateBoard(Board board){ //method that checks all the board and update the GUI
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
                        this.boardTiles[row][column].setOpaque(false);
                        this.boardTiles[row][column].setContentAreaFilled(false);
                        this.boardTiles[row][column].setBorderPainted(false);
                    }
                }
            }
    }

    public boolean isCanPick() {
        return canPick;
    }

    public void setCanPick(boolean canPick) {
        this.canPick = canPick;

    }

    public int getTilesPicked() {
        return tilesPicked;
    }

    public void decrementTilesPicked(){
        this.tilesPicked++;
    }
    public void setTilesPicked(int i){
        this.tilesPicked = i;
    }

    public ArrayList<Cord> getListTilesPicked() {
        return listTilesPicked;
    }
}
