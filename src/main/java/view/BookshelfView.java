package view;

import model.Bookshelf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class BookshelfView extends JInternalFrame {

    private JInternalFrame bookshelfDisplayed;
    public BookshelfView() throws IOException {
        Image img,scaledImg;
        JButton button;
        InputStream is;
        is = this.getClass().getClassLoader().getResourceAsStream("resources/bookshelfResized.png");
        img = ImageIO.read(is);
        scaledImg = img.getScaledInstance(290, 327, Image.SCALE_SMOOTH);
        bookshelfDisplayed = new ImagePanel("Bookshelf", scaledImg, 6,5, 10,5);
        for(int i=0; i<30; i++){
        is = this.getClass().getClassLoader().getResourceAsStream("resources/TileImages/NOTHING.png");
        img = ImageIO.read(is);
        scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon im = new ImageIcon(scaledImg);
        button = new JButton();
        button.setPreferredSize(new Dimension(50,50));
        bookshelfDisplayed.add(button);
        button.setIcon(im);
        }

        bookshelfDisplayed.setMinimumSize(new Dimension(800, 450));
        bookshelfDisplayed.setVisible(true);
    }

    public JInternalFrame getBookshelfDisplayed() {
        return bookshelfDisplayed;
    }

    public void setBookshelfDisplayed(JInternalFrame bookshelfDisplayed) {
        this.bookshelfDisplayed = bookshelfDisplayed;
    }

}
