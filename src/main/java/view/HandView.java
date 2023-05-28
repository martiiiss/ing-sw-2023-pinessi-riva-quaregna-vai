package view;

import model.Tile;

import javax.swing.*;
import java.awt.*;


public class HandView extends JInternalFrame { //class to represent the "hand", the tiles picked from the board and not placed in the bookshelf yet
    private JButton [] tilesInHand;
    public HandView(){
        tilesInHand = new JButton[3];
        setLayout(new GridLayout(1,3));
        for(int i=0; i<3;i++) {
            tilesInHand[i] = new JButton();
            add(tilesInHand[i]);
            tilesInHand[i].setPreferredSize(new Dimension(50, 50));
        }
    setVisible(true);
    setMinimumSize(new Dimension(250, 150));
    setTitle("Hand");
    }

    public void setTilesInHand(Tile [] tilesInHand) { //set the "hand" given the tiles in hand in the model
        ImageReader imageReader = new ImageReader();
        for(int i=0; i<tilesInHand.length; i++){
            this.tilesInHand[i].setIcon(imageReader.readIcon("resources/TileImages/" + tilesInHand[i].getType() + "/" + tilesInHand[i].getNumType() + ".png", 50, 50));
        }
    }

    public void removeTileInHand(int i){ //set the given position in the hand to null since it's been picked
        tilesInHand[i].setIcon(null);
    }
}
