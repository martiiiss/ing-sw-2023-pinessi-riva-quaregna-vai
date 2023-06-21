package view.GUI;

import distributed.messages.Message;
import model.Tile;
import util.*;
import util.Event;

import javax.swing.*;
import java.awt.*;


public class HandView { //class to represent the "hand", the tiles picked from the board and not placed in the bookshelf yet and the ScoringTokens taken by the player
    private JButton [] hand;
    private JInternalFrame imageDisplayed;

    private int tileToInsert = 0;

    private int [] order;
    public HandView(){
        order = new int[3];
        for (int i=0; i<3; i++){
            order[0]= -1;
        }
        imageDisplayed = new JInternalFrame();
        hand = new JButton[6];
        imageDisplayed.setLayout(new GridLayout(2,3));
        for(int i=0; i<6;i++) {
            hand[i] = new JButton();
            imageDisplayed.add(hand[i]);
            hand[i].setPreferredSize(new Dimension(50, 50));
            hand[i].setBorderPainted(false);
            hand[i].setOpaque(false);
            hand[i].setContentAreaFilled(false);
            if(i<3){
                int finalI = i;
                hand[i].addActionListener(e -> {
                    if(hand[finalI].getIcon()!=null && tileToInsert==0){
                        removeTileInHand(finalI);
                        this.tileToInsert=finalI;
                    }

                });
            }
        }
    imageDisplayed.setVisible(true);
    imageDisplayed.setMinimumSize(new Dimension(250, 150));
    imageDisplayed.setTitle("Hand");
    }

    public void setTilesInHand(Tile tile) { //set the "hand" given the tiles in hand in the model
        ImageReader imageReader = new ImageReader();
        for (JButton jButton : hand) {
            if (jButton.getIcon() == null) {
                jButton.setOpaque(true);
                jButton.setBorderPainted(true);
                jButton.setContentAreaFilled(true);
                jButton.setIcon(imageReader.readIcon("resources/TileImages/" + tile.getType() + "/" + tile.getNumType() + ".png", 50, 50));
                break;
            }
        }
    }

    public  void setSC(int valueDisplayed, int romanNumber){
        ImageReader imageReader = new ImageReader();
        this.hand[romanNumber+3].setIcon(imageReader.readIcon("resources/ScoringTokenImages/" + valueDisplayed + ".jpg", 50, 50));
    }

    public void removeTileInHand(int i){ //set the given position in the hand to null since it's been picked
        hand[i].setIcon(null);
        hand[i].setBorderPainted(false);
        hand[i].setOpaque(false);
        hand[i].setContentAreaFilled(false);
    }

    public JInternalFrame getImageDisplayed(){
        return this.imageDisplayed;
    }

    public int getOrder(int pos) {
        return order[pos];
    }

    public int[] getOrder(){
        return order;
    }

    public void insertFirstVoid(int value) {
        for(int i=0; i<3; i++)
            if(order[i]== -1) {
                order[i] = value;
                break;
            }
    }

    public boolean searchOrder(int value){
        for(int i=0; i<3; i++){
            if(order[i] == value)
                return true;
        }
        return false;
    }


    public void setEndgame(){
        ImageReader imageReader = new ImageReader();
        this.hand[5].setIcon(imageReader.readIcon("resources/ScoringTokenImages/endgame.jpg", 50, 50));
    }

    public int getTileToInsert() {
        return this.tileToInsert;
    }

    public void setTileToInsert(int tileToInsert) {
        this.tileToInsert = tileToInsert;
    }
}
