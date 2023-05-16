package view;

import model.Board;
import model.CommonGoalCard;
import model.Game;
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



public class BoardView extends JSplitPane{

    private final JLabel tilePickedLabel = new JLabel("Tile picked!");
    public BoardView (Board board, Game game) throws IOException {
        Image img, scaledImg;
        JLabel jLabel;
        JFrame frame = new JFrame();
        JInternalFrame jInternalFrame = new JInternalFrame("Board");
        jInternalFrame.setLayout(new GridLayout(9,9));
        frame.setLayout(new FlowLayout());
        JButton button;
        InputStream is;
        /*is = this.getClass().getClassLoader().getResourceAsStream("resources/livingroom.png");
        img = ImageIO.read(is);
        scaledImg = img.getScaledInstance(450,450, Image.SCALE_SMOOTH);
        jLabel = new JLabel(new ImageIcon(scaledImg));
        jInternalFrame.setContentPane(jLabel);*/
        for(int row=0; row<9; row++)
            for(int column=0; column<9; column++) {
                if(board.getSelectedType(row, column)!= model.Type.BLOCKED)
                    is = this.getClass().getClassLoader().getResourceAsStream("resources/TileImages/"+ board.getSelectedType(row, column) +"/"+ board.getSelectedNumType(row, column) +".png");
                else
                    is = this.getClass().getClassLoader().getResourceAsStream("resources/TileImages/"+ board.getSelectedType(row, column) +".png");
                img = ImageIO.read(is);
                    scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    ImageIcon im = new ImageIcon(scaledImg);
                    button = new JButton();
                    button.setPreferredSize(new Dimension(50,50));
                    jInternalFrame.add(button);
                    button.setIcon(im);

            }
        jInternalFrame.setVisible(true);
        jInternalFrame.setMinimumSize(new Dimension(450,450));
        frame.add(jInternalFrame);
        for(int i=0; i<2; i++) {
            is = this.getClass().getClassLoader().getResourceAsStream("resources/CommonGoalCardImages/" + game.getCommonGoalCard().get(i).getIdCGC() + ".jpg");
            img = ImageIO.read(is);
            scaledImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            jLabel = new JLabel(new ImageIcon(scaledImg));
            frame.add(jLabel);
            is = this.getClass().getClassLoader().getResourceAsStream("resources/ScoringTokenImages/" + game.getCommonGoalCard().get(i).getTokenStack().get(game.getNumOfPlayers()-1).getValue() + ".jpg");
            img = ImageIO.read(is);
            scaledImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            jLabel = new JLabel(new ImageIcon(scaledImg));
            frame.add(jLabel);
        }
        frame.pack();
        frame.setVisible(true);


    }


}