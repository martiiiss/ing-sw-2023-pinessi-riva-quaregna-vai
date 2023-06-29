package view.GUI;


import model.Bookshelf;
import model.Tile;
import model.Type;
import util.ImagePanel;
import util.ImageReader;
import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

/**Class that represents the frame of the bookshelf in the Graphic User Interface*/
public class BookshelfView implements Serializable {
    @Serial
    private static final long serialVersionUID = 4758892563965792612L;
    private final JInternalFrame bookshelfDisplayed;
    private final JButton [][] bookshelfTiles;
    private boolean canChange = true;
    private int columnChosen = -1;

    /**Constructor of the Class. This creates the bookshelf GUI section. */
    public BookshelfView(){
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
                bookshelfTiles[i][j].putClientProperty("status", 0);
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

    /**
     * Method used to get the JInternalFrame relative to the Bookshelf.
     * @return a {@link JInternalFrame}*/
    public JInternalFrame getBookshelfDisplayed() {
        return bookshelfDisplayed;
    }

    /**
     * Method used to modify the GUI bookshelf by inserting
     * the given tile to the first free position in the given column
     * @param column an int that represents a column
     * @param tile a {@code Tile} that has to be inserted*/
    public void insertTile(int column, Tile tile){
        ImageReader imageReader = new ImageReader();
        for(int i=5; i>=0; i--){
            if((int)bookshelfTiles[i][column].getClientProperty("status")==0){
                this.bookshelfTiles[i][column].setIcon(imageReader.readIcon("resources/TileImages/" + tile.getType() + "/" + tile.getNumType() + ".png", 50, 50));
                this.bookshelfTiles[i][column].putClientProperty("status", 1);
                break;
            }
        }
    }

    /**
     * Method used to update the bookshelf.
     * @param bks is the {@code Bookshelf} that has to be updated*/
    public void changeBookshelf(Bookshelf bks){
        ImageReader imageReader = new ImageReader();
        for (int i = 0; i <= 6; i++){
            for (int j = 0; j<= 5; j++){
                if (bks.getBookshelf()[i][j].getType() !=Type.NOTHING){
                    this.bookshelfTiles[i][j].setIcon(imageReader.readIcon("resources/TileImages/" + bks.getBookshelf()[i][j].getType() + "/" + bks.getBookshelf()[i][j].getNumType() + ".png", 50, 50));
                    this.bookshelfTiles[i][j].putClientProperty("status", 1);
                 }
                else{
                    bookshelfTiles[i][j].setIcon(imageReader.readIcon("resources/TileImages/NOTHING.png", 50, 50));
                    this.bookshelfTiles[i][j].putClientProperty("status", 0);
                }
            }
        }
    }

    /**
     * Method used to get the chosen column.
     * @return an int that represents a column*/
    public int getColumnChosen() {
        return columnChosen;
    }

    /**
     * Method used to gst the chosen column.
     * @param columnChosen  an int that represents a column*/
    public void setColumnChosen(int columnChosen) {
        this.columnChosen = columnChosen;
    }

    /**
     * Method used to return a boolean on a column.
     * @return <b>true</b> if a column can be changed, <b>false</b> otherwise*/
    public boolean isCanChange() {
        return canChange;
    }

    /**
     * Method used to set a boolean on a column.
     * @param canChange is <b>true</b> if a column can be changed, <b>false</b> otherwise*/
    public void setCanChange(boolean canChange) {
        this.canChange = canChange;
    }
}
