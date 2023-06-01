package view.GUI;

import model.Tile;
import util.ImageReader;

import javax.swing.*;
import java.awt.*;


public class HandView extends JInternalFrame { //class to represent the "hand", the tiles picked from the board and not placed in the bookshelf yet and the ScoringTokens taken by the player
    private JButton [] tilesInHand;
    public HandView(){
        tilesInHand = new JButton[5];
        setLayout(new GridLayout(2,3));
        for(int i=0; i<5;i++) {
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

    public  void setSC(int valueDisplayed, int romanNumber){
        ImageReader imageReader = new ImageReader();
        this.tilesInHand[romanNumber].setIcon(imageReader.readIcon("resources/ScoringTokenImages/" + valueDisplayed + ".jpg", 50, 50));
    }

    public void removeTileInHand(int i){ //set the given position in the hand to null since it's been picked
        tilesInHand[i].setIcon(null);
    }
}
