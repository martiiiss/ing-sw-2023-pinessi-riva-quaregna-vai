package view;

import model.Board;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;




public class BoardView extends JInternalFrame{

    private JInternalFrame boardDisplayed;
    public BoardView (Board board) throws IOException {
        Image img, scaledImg;
        boardDisplayed = new JInternalFrame("Board");
        boardDisplayed.setLayout(new GridLayout(9,9));
        JButton button;
        InputStream is;
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
                    boardDisplayed.add(button);
                    button.setIcon(im);

            }
        boardDisplayed.setVisible(true);
        boardDisplayed.setMinimumSize(new Dimension(450,450));
    }

    public JInternalFrame getBoardDisplayed() {
        return boardDisplayed;
    }

    public void setBoardDisplayed(JInternalFrame boardDisplayed) {
        this.boardDisplayed = boardDisplayed;
    }
}