package view.GUI;

import distributed.messages.Message;
import model.Tile;
import model.Type;
import util.Event;
import util.ImagePanel;
import util.ImageReader;
import util.Observable;


import javax.swing.*;
import java.awt.*;
import java.io.Serializable;


public class BookshelfView implements Serializable {
    private static final long serialVersionUID = 4758892563965792612L;
    private JInternalFrame bookshelfDisplayed;
    private JButton [][] bookshelfTiles;

    private int columnChosen = -1;

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
                bookshelfTiles[i][j].putClientProperty("column", j);
                bookshelfTiles[i][j].addActionListener(e -> {
                    JButton button = (JButton) e.getSource();
                    if(columnChosen==-1)
                        synchronized (this){
                            columnChosen = (int) button.getClientProperty("column");
                            this.notify();
                        }
                });
            }

        bookshelfDisplayed.setMinimumSize(new Dimension(800, 450));
        bookshelfDisplayed.setMaximumSize(new Dimension(800, 450));
        bookshelfDisplayed.setVisible(true);
    }

    public JInternalFrame getBookshelfDisplayed() {
        return bookshelfDisplayed;
    }

    public void insertTile(int column, int row, Tile tile){ //modify the GUI bookshelf inserting the given tile to the first free position in the given column
        ImageReader imageReader = new ImageReader();
        this.bookshelfTiles[row][column].setIcon(imageReader.readIcon("resources/TileImages/" + tile.getType() + "/" + tile.getNumType() + ".png", 50, 50))
    }
    public void updateBookshelf(Tile[][] tiles){
        ImageReader imageReader = new ImageReader();
        for (int i=0; i<=6; i++){
            for (int j=0; j<=5; j++) {
                if (tiles[i][j].getType() != Type.NOTHING)
                    this.bookshelfTiles[i][j].setIcon(imageReader.readIcon("resources/TileImages/" + tiles[i][j].getType() + "/" + tiles[i][j].getNumType() + ".png", 50, 50));
            }
        }
    }

    public int getColumnChosen() {
        return columnChosen;
    }

    public void setColumnChosen(int columnChosen) {
        this.columnChosen = columnChosen;
    }
}
