package view;

import model.Board;
import model.Type;
import util.images.ImageLocation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;



public class BoardView extends JFrame {
    private Board board;

    private final JLabel tilePickedLabel = new JLabel("Tile picked!");
    public BoardView (Board board) throws IOException {
        JFrame frame = new JFrame("Board");
        frame.setLayout(new GridLayout(9,9));
        this.board = board;
        JButton button;
        for(int row=0; row<9; row++)
            for(int column=0; column<9; column++) {
                if(board.getSelectedType(row, column)!= model.Type.BLOCKED) {
                    /*ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    InputStream is = this.getClass().getClassLoader().getResourceAsStream("BOOK.1.png");*/
                    ImageIcon img = new ImageIcon(ImageLocation.class.getResource("BOOK.1.png"));
                    frame.add(button = new JButton());
                    button.setIcon(img);
                }
                else frame.add(new JButton(""+board.getSelectedType(row,column)));
            }
        frame.pack();
        frame.setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }


}