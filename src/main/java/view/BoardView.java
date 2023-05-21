package view;

import model.Board;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;




public class BoardView extends JInternalFrame{

    private JInternalFrame boardDisplayed;
    public BoardView (Board board) throws IOException {
        Image img, scaledImg;
        InputStream is;
        is = this.getClass().getClassLoader().getResourceAsStream("resources/livingroomResized.png");
        img = ImageIO.read(is);
        scaledImg = img.getScaledInstance(467,467, Image.SCALE_SMOOTH);
        boardDisplayed = new ImagePanel("Board", scaledImg, 9,9, 2, 2);
        /*JInternalFrame boardDisplayedTrue = new JInternalFrame();
        boardDisplayedTrue = new JInternalFrame();
        boardDisplayedTrue.setTitle("Board");
        boardDisplayedTrue.pack();
        boardDisplayedTrue.setLayout(new GridLayout(9,9));
        boardDisplayed.add(boardDisplayedTrue);*/


        /*ImagePanel panel = new ImagePanel(img);
        boardDisplayed.getContentPane().add(panel);
        boardDisplayed.pack();
        boardDisplayed.setVisible(true);*/
        JButton button;
        Dimension dButton = new Dimension(50,50);
        for(int row=0; row<9; row++)
            for(int column=0; column<9; column++) {
                if(board.getSelectedType(row, column)!= model.Type.BLOCKED) {
                    is = this.getClass().getClassLoader().getResourceAsStream("resources/TileImages/" + board.getSelectedType(row, column) + "/" + board.getSelectedNumType(row, column) + ".png");
                    /*is = this.getClass().getClassLoader().getResourceAsStream("resources/TileImages/" + board.getSelectedType(row, column) + ".png");*/
                    img = ImageIO.read(is);
                    scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    ImageIcon im = new ImageIcon(scaledImg);
                    button = new JButton();
                    button.setPreferredSize(new Dimension(50, 50));
                    boardDisplayed.add(button);
                    button.setIcon(im);
                }
                else {
                    button = new JButton();
                    button.setPreferredSize(new Dimension(50, 50));
                    boardDisplayed.add(button);
                    button.setOpaque(false);
                    button.setContentAreaFilled(false);
                    button.setBorderPainted(false);

                }

            }
        boardDisplayed.setVisible(true);
        Dimension d= new Dimension(500,530);
        boardDisplayed.setMinimumSize(d);
        /*boardDisplayedTrue.setMinimumSize(new Dimension(450,450));
        boardDisplayedTrue.setVisible(true);*/
    }

    public JInternalFrame getBoardDisplayed() {
        return boardDisplayed;
    }

    public void setBoardDisplayed(JInternalFrame boardDisplayed) {
        this.boardDisplayed = boardDisplayed;
    }
    public void paintComponent(Graphics g,Image img){
                super.paintComponent(g);
                g.drawImage(img, 0, 0, null);
            }
}