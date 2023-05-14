package view;

import model.Board;
import model.Type;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
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
                InputStream is;
                if(board.getSelectedType(row, column)!= model.Type.BLOCKED)
                    is = this.getClass().getClassLoader().getResourceAsStream("resources/TileImages/"+ board.getSelectedType(row, column) +"/"+ board.getSelectedNumType(row, column) +".png");
                else
                    is = this.getClass().getClassLoader().getResourceAsStream("resources/TileImages/"+ board.getSelectedType(row, column) +".png");
                Image img = ImageIO.read(is);
                    Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    ImageIcon im = new ImageIcon(scaledImg);
                    button = new JButton();
                    button.setPreferredSize(new Dimension(50,50));
                    frame.add(button);
                    button.setIcon(im);
            }
        frame.pack();
        frame.setVisible(true);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }


}