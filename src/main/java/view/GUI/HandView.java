package view.GUI;

import distributed.messages.Message;
import model.Tile;
import util.*;
import util.Event;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

import static java.awt.GridBagConstraints.*;


public class HandView implements Serializable { //class to represent the "hand", the tiles picked from the board and not placed in the bookshelf yet and the ScoringTokens taken by the player
    private static final long serialVersionUID = 4758892564965792611L;
    private JButton [] hand;
    private JInternalFrame imageDisplayed;

    private int tileToInsert = -1;

    private int [] order;
    public HandView(){
        order = new int[3];
        for (int i=0; i<3; i++){
            order[0]= -1;
        }
        imageDisplayed = new JInternalFrame();
        hand = new JButton[6];
        imageDisplayed.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = FIRST_LINE_START;

        for(int i=0; i<6;i++) {
            hand[i] = new JButton();
            hand[i].setPreferredSize(new Dimension(50, 50));
            hand[i].setMinimumSize(new Dimension(50,50));
            hand[i].setBorderPainted(false);
            hand[i].setOpaque(false);
            hand[i].setContentAreaFilled(false);
            if(i<3){
                constraints.gridx = i;
                constraints.gridy = 0;
                imageDisplayed.add(hand[i], constraints);
                hand[i].putClientProperty("index", i);
                hand[i].addActionListener(e -> {
                    JButton button = (JButton) e.getSource();
                    int index = (int) button.getClientProperty("index");
                    if(hand[index].getIcon()!=null && tileToInsert==0){
                        synchronized (this) {
                            this.tileToInsert = index;
                            removeTileInHand(index);
                            this.notify();
                        }
                    }

                });
            }
            else{
                constraints.gridx = i-3;
                constraints.gridy = 1;
                imageDisplayed.add(hand[i], constraints);
            }
        }

        imageDisplayed.setVisible(true);
        imageDisplayed.setMinimumSize(new Dimension(250, 200));
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
                synchronized (this){
                    this.notify();
                }
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
