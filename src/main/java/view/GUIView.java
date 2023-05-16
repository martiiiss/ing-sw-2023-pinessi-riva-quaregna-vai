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
        InputStream is;
        Image img, scaledImg;
        GUI.setLayout(new FlowLayout());
        BoardView boardView = new BoardView(board);
        GUI.add(boardView.getBoardDisplayed());
        JLabel jLabel;


        for(int i=0; i<2; i++) {
            is = this.getClass().getClassLoader().getResourceAsStream("resources/CommonGoalCardImages/" + game.getCommonGoalCard().get(i).getIdCGC() + ".jpg");
            img = ImageIO.read(is);
            scaledImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            jLabel = new JLabel(new ImageIcon(scaledImg));
            GUI.add(jLabel);
            ScoringTokenView scv = new ScoringTokenView(game.getCommonGoalCard().get(i).getTokenStack().get(game.getNumOfPlayers()-1).getValue(), i);
            scv.setDisplayedImage();
            GUI.add(scv.getDisplayedImage());
        }
        GUI.pack();
        GUI.setVisible(true);

    }
}
