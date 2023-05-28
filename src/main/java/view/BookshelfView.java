package view;

import model.Tile;


import javax.swing.*;
import java.awt.*;


public class BookshelfView extends JInternalFrame {

    private JInternalFrame bookshelfDisplayed;
    private JButton [][] bookshelfTiles;

    public BookshelfView(){ //creates the bookshelf GUI section
        ImageReader imageReader = new ImageReader();
        bookshelfTiles = new JButton[6][5];
        bookshelfDisplayed = new ImagePanel("Bookshelf", imageReader.readImage("resources/bookshelfResized.png", 290, 327), 6,5, 10,5);
        for(int i=0; i<6; i++)
            for(int j=0; j<5; j++){
                bookshelfTiles[i][j] = new JButton();
                bookshelfTiles[i][j].setPreferredSize(new Dimension(50,50));
                bookshelfDisplayed.add(bookshelfTiles[i][j]);
                bookshelfTiles[i][j].setIcon(imageReader.readIcon("resources/TileImages/NOTHING.png", 50, 50));
        }

        bookshelfDisplayed.setMinimumSize(new Dimension(800, 450));
        bookshelfDisplayed.setMaximumSize(new Dimension(800, 450));
        bookshelfDisplayed.setVisible(true);
    }

    public JInternalFrame getBookshelfDisplayed() {
        return bookshelfDisplayed;
    }

    public void insertTile(int column, Tile tile){ //modify the GUI bookshelf inserting the given tile to the first free position in the given column
        ImageReader imageReader = new ImageReader();
        for (int i = 5; i >= 0; i++) {
            if (bookshelfTiles[i][column].getIcon() == null) {
                this.bookshelfTiles[i][column].setIcon(imageReader.readIcon("resources/TileImages/" + tile.getType() + "/" + tile.getNumType() + ".png", 50, 50));
                i = -1;
            }
        }
    }

}
