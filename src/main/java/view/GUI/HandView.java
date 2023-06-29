package view.GUI;

import model.Tile;
import util.*;
import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import static java.awt.GridBagConstraints.*;

/**Class that represents the frame of the tiles in hand in the Graphic User Interface.*/
public class HandView implements Serializable {
    @Serial
    private static final long serialVersionUID = 4758892564965792611L;
    private final JButton [] hand;
    private final JInternalFrame imageDisplayed;
    private int tileToInsert = -1;

    /**Constructor of the Class.*/
    public HandView(){
        int[] order = new int[3];
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
            } else {
                constraints.gridx = i-3;
                constraints.gridy = 1;
                imageDisplayed.add(hand[i], constraints);
            }
        }
        imageDisplayed.setVisible(true);
        imageDisplayed.setMinimumSize(new Dimension(250, 200));
        imageDisplayed.setTitle("Hand");
    }

    /**
     * Method used to set the tiles in hand given the tiles in hand in the model.
     * @param tile is a {@code Tile} that has to be set graphically as a tile in hand*/
    public void setTilesInHand(Tile tile) {
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

    /**
     * Method that sets the given position in the hand to null when a tile it's picked
     * @param i is the index of the tile to remove*/
    public void removeTileInHand(int i){
        hand[i].setIcon(null);
        hand[i].setBorderPainted(false);
        hand[i].setOpaque(false);
        hand[i].setContentAreaFilled(false);
    }

    /**
     * Method used to get the JInternalFrame relative to the tiles in hand.
     * @return a {@link JInternalFrame}*/
    public JInternalFrame getImageDisplayed(){
        return this.imageDisplayed;
    }


    /**
     * Method used to get an int that represents the index of tile to insert
     * @return an int, the index of the tile to insert*/
    public int getTileToInsert() {
        return this.tileToInsert;
    }

    /**
     * Method used to set an int that represents the index of tile to insert
     * @param tileToInsert  an int, the index of the tile to insert*/
    public void setTileToInsert(int tileToInsert) {
        this.tileToInsert = tileToInsert;
    }

}
