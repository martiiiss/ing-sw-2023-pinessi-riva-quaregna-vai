package view.GUI;

import util.ImageReader;

import javax.swing.*;
import java.awt.*;

public class GUIView { //class that contains all the GUI elements

    private BoardView boardView;
    private ScoringTokenView [] scv;
    private BookshelfView bookshelfView;

    private HandView hand;

    private PGCView pgc;

    private CGCView [] cgc;

    private boolean isMyTurn;

    public GUIView () {
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
        boardView = new BoardView();
        GUI.add(boardView.getBoardDisplayed());
        cgc = new CGCView[2];
        CGCArea.setLayout(new GridLayout(2,2));
        CGCArea.setTitle("CommonGoalCards");

        scv= new ScoringTokenView[2];
        for(int i=0; i<2; i++) {
            cgc[i] = new CGCView();
            CGCArea.add(cgc[i]);
            scv[i] = new ScoringTokenView();
            CGCArea.add(scv[i].getDisplayedImage());
        }
        CGCArea.setVisible(true);
        CGCArea.setMinimumSize(new Dimension(250,250));
        GUI.add(CGCArea);
        hand = new HandView();
        GUI.add(hand);
        bookshelfView = new BookshelfView();
        GUI.add(bookshelfView.getBookshelfDisplayed());
        pgc = new PGCView();
        GUI.add(pgc.getDisplayedImage());
        GUI.pack();
        GUI.setVisible(true);

    }

    public void changeSC(int valueDisplayed, int romanNumber){
        scv[romanNumber].popSCV(valueDisplayed);
        hand.setSC(valueDisplayed, romanNumber);
    }

    public HandView getHandView(){
        return this.hand;
    }
    public BookshelfView getBookshelfView(){
        return this.bookshelfView;
    }
    public BoardView getBoardView(){
        return this.boardView;
    }

    public ScoringTokenView getScv(int i) {
        return scv[i];
    }

    public PGCView getPGC(){
        return pgc;
    }

    public CGCView getCGC(int i) {
        return cgc[i];
    }

    public boolean getIsMyTurn(){
        return this.isMyTurn;
    }

    public void setIsMyTurn(boolean value){
        this.isMyTurn = value;
    }
}
