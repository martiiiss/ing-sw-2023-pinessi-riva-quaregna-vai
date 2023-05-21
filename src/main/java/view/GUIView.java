package view;

import model.Board;
import model.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class GUIView {
    public GUIView (Game game, Board board) throws IOException {
        JFrame GUI = new JFrame();
        JInternalFrame CGCArea = new JInternalFrame();
        InputStream is;
        Image img, scaledImg;
        GUI.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx=100;
        BoardView boardView = new BoardView(board);
        GUI.add(boardView.getBoardDisplayed());
        JLabel jLabel;
        CGCArea.setLayout(new GridLayout(2,2));
        CGCArea.setTitle("CommonGoalCards");


        for(int i=0; i<2; i++) {
            is = this.getClass().getClassLoader().getResourceAsStream("resources/CommonGoalCardImages/" + game.getCommonGoalCard().get(i).getIdCGC() + ".jpg");
            img = ImageIO.read(is);
            scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            jLabel = new JLabel(new ImageIcon(scaledImg));
            CGCArea.add(jLabel);
            ScoringTokenView scv = new ScoringTokenView(game.getCommonGoalCard().get(i).getTokenStack().get(game.getNumOfPlayers()-1).getValue(), i);
            scv.setDisplayedImage();
            CGCArea.add(scv.getDisplayedImage());
        }
        CGCArea.setVisible(true);
        CGCArea.setMinimumSize(new Dimension(250,250));
        GUI.add(CGCArea);
        BookshelfView bookshelfView = new BookshelfView();
        GUI.add(bookshelfView.getBookshelfDisplayed(), constraints);
        PGCView pgc = new PGCView(1);
        pgc.setDisplayedImage();
        GUI.add(pgc.getDisplayedImage());
        GUI.pack();
        GUI.setVisible(true);

    }
}
