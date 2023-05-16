package view;

import model.Bookshelf;

import javax.swing.*;
import java.awt.*;

public class BookshelfView extends JInternalFrame {

    private JInternalFrame bookshelfDisplayed;
    public BookshelfView(){
        bookshelfDisplayed = new JInternalFrame("Bookshelf");
        bookshelfDisplayed.setLayout(new GridLayout(6, 5));
        bookshelfDisplayed.setMinimumSize(new Dimension(300, 250));
        bookshelfDisplayed.setVisible(true);
    }

    public JInternalFrame getBookshelfDisplayed() {
        return bookshelfDisplayed;
    }

    public void setBookshelfDisplayed(JInternalFrame bookshelfDisplayed) {
        this.bookshelfDisplayed = bookshelfDisplayed;
    }

}
