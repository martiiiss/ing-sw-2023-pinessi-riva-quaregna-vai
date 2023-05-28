package view;

import model.Board;
import model.Game;

import javax.swing.*;
import java.awt.*;

public class GUIView { //class that contains all the GUI elements
    public GUIView (Game game, Board board) {
        JFrame GUI = new JFrame();
        ImageReader imageReader = new ImageReader();
        GUI.setContentPane( new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(imageReader.readImage("resources/parquet.jpg", 2000,2000), 0, 0, null);
            };

        });
        JInternalFrame CGCArea = new JInternalFrame();
        GUI.setLayout(new GridBagLayout());
        BoardView boardView = new BoardView(board);
        GUI.add(boardView.getBoardDisplayed());
        JLabel jLabel;
        CGCArea.setLayout(new GridLayout(2,2));
        CGCArea.setTitle("CommonGoalCards");


        for(int i=0; i<2; i++) {
            jLabel = new JLabel(imageReader.readIcon("resources/CommonGoalCardImages/" + game.getCommonGoalCard().get(i).getIdCGC() + ".jpg", 100,100));
            CGCArea.add(jLabel);
            ScoringTokenView scv = new ScoringTokenView(game.getCommonGoalCard().get(i).getTokenStack().get(game.getNumOfPlayers()-1).getValue());;
            CGCArea.add(scv.getDisplayedImage());
        }
        CGCArea.setVisible(true);
        CGCArea.setMinimumSize(new Dimension(250,250));
        GUI.add(CGCArea);
        HandView hand = new HandView();
        GUI.add(hand);
        BookshelfView bookshelfView = new BookshelfView();
        GUI.add(bookshelfView.getBookshelfDisplayed());
        PGCView pgc = new PGCView(1);
        GUI.add(pgc.getDisplayedImage());
        GUI.pack();
        GUI.setVisible(true);

    }
}
