package view.GUI;

import distributed.messages.Message;
import model.*;
import util.*;
import util.Event;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GUIView{ //class that contains all the GUI elements
    private BoardView boardView;
    private ScoringTokenView[] scv;
    private BookshelfView bookshelfView;

    private HandView hand;

    private PGCView pgc;

    private CGCView[] cgc;

    private int tilesMoved = 0;

    public GUIView() {
        JFrame GUI = new JFrame();
        ImageReader imageReader = new ImageReader();
        GUI.setContentPane(new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imageReader.readImage("resources/parquet.jpg", 2000, 2000), 0, 0, null);
            }

            ;

        });
        JInternalFrame CGCArea = new JInternalFrame();
        GUI.setLayout(new GridBagLayout());
        this.boardView = new BoardView();
        GUI.add(this.boardView.getBoardDisplayed());
        cgc = new CGCView[2];
        CGCArea.setLayout(new GridLayout(2, 2));
        CGCArea.setTitle("CommonGoalCards");

        scv = new ScoringTokenView[2];
        for (int i = 0; i < 2; i++) {
            cgc[i] = new CGCView();
            CGCArea.add(cgc[i]);
            scv[i] = new ScoringTokenView();
            CGCArea.add(scv[i].getDisplayedImage());
        }
        CGCArea.setVisible(true);
        CGCArea.setMinimumSize(new Dimension(250, 250));
        GUI.add(CGCArea);
        hand = new HandView();
        GUI.add(hand.getImageDisplayed());
        bookshelfView = new BookshelfView();
        GUI.add(bookshelfView.getBookshelfDisplayed());
        pgc = new PGCView();
        GUI.add(pgc.getDisplayedImage());
        GUI.pack();
        GUI.setVisible(true);

    }

    public void changeScoringToken(ScoringToken sc) { //changes the scoring token displayed in the romanNumber position
        scv[sc.getRomanNumber()].popSCV(sc.getValue());
        hand.setSC(sc.getValue(), sc.getRomanNumber());
    }

    public HandView getHandView() {
        return this.hand;
    }

    public BookshelfView getBookshelfView() {
        return this.bookshelfView;
    }

    public BoardView getBoardView() {
        return this.boardView;
    }

    public ScoringTokenView getScv(int i) {
        return scv[i-1];
    }

    public PGCView getPGC() {
        return pgc;
    }

    public CGCView getCGC(int i) {
        return cgc[i-1];
    }

    public void updateBoard(Board board){ //set up the board or update it after someone else pick tiles
        boardView.updateBoard(board);
    }
    public void setupCGC(CommonGoalCard cgc){ //set up the cgc and the scoring token
        System.out.println("cgc " + cgc.getRomanNumber());
        getCGC(cgc.getRomanNumber()).setCGCView(cgc.getIdCGC());
        getScv(cgc.getRomanNumber()).setDisplayedImage(cgc.getTokenStack().firstElement().getValue());
    }
    public void setupPGC(int pgcID){  //set up the pgc
        this.pgc.setDisplayedImage(pgcID);
    }
    public void askTiles(){ //invoked by client, asks the GUI to choose tiles and returns an arraylist of them
        JFrame frame = new JFrame();
        for(int i=1 ; i<4; i++){
            JButton button = new JButton();
            frame.add(button);
            button.setText(""+i);
            button.setVisible(true);
            button.addActionListener(e ->{
                boardView.setTilesPicked(Integer.parseInt(button.getText()));
                frame.dispose();
                System.out.println("selezionato: " +  boardView.getTilesPicked());
            });
        }
        frame.setVisible(true);
        frame.setLayout(new GridLayout());
        frame.setSize(400,200);
        frame.setTitle("How many tiles you want to pick?");
    }

    public ArrayList <Cord> getTilesClient() throws InterruptedException {
        System.out.println("prima: " + boardView.getTilesPicked());
        while(boardView.getTilesPicked()<=0){
            waitUpdate();
        }
        System.out.println("setta a true can pick");
        boardView.setCanPick(true);

        boardView.getBoardDisplayed().setTitle("Click the tiles to pick them");
        while (boardView.getTilesPicked()!=0){
            waitUpdate();
        }
        System.out.println("get tiles picked " + boardView.getTilesPicked() );
        boardView.setCanPick(false);
        boardView.getBoardDisplayed().setTitle("Board");
        return boardView.getListTilesPicked();
    }

    public void waitUpdate() throws InterruptedException {
        System.out.print("");
    }
    public void pickTiles(ArrayList<Cord> cords, ArrayList<Tile> tiles){ //pick the tiles from the board and put them in the hand
        int i=0;
        while(cords.get(i)!=null){
            boardView.pickTile(cords.get(i).getRowCord(), cords.get(i).getColCord());
            hand.setTilesInHand(tiles.get(i));
            i++;
        }
    }
    public int chooseColumn() throws InterruptedException { //choose the column where i want to put the tile and return it
        bookshelfView.getBookshelfDisplayed().setTitle("Choose the column to insert tiles by clicking one of its buttons");
        bookshelfView.setColumnChosen(0);
        while(bookshelfView.getColumnChosen()==0){
            waitUpdate();
        }
        bookshelfView.getBookshelfDisplayed().setTitle("Bookshelf");
        return bookshelfView.getColumnChosen();
    }
    public int chooseTile() throws InterruptedException { //choose the tile to insert and return its position
        hand.setTileToInsert(0);
        hand.getImageDisplayed().setTitle("Choose the first tile to insert by clicking on it");
        while(hand.getTileToInsert()==0){
            waitUpdate();
        }
        return hand.getTileToInsert();
    }
    public void addTile(Tile tile){ //add the tile to the bookshelf
        bookshelfView.insertTile(bookshelfView.getColumnChosen(), tile);
    }
    public void showError(Event e){
        switch (e){
            case TILES_NOT_VALID-> boardView.getBoardDisplayed().setTitle("Tiles not valid, select again");
            case COLUMN_NOT_VALID -> bookshelfView.getBookshelfDisplayed().setTitle("You selected a column with not enough space,try again");
        }
    }
    public void scoringTokenTakenByMe(ScoringToken sc){ //put the scoring token in my hand
        hand.setSC(sc.getValue(), sc.getRomanNumber());
        changeScoringToken(sc);
    }
    public void firstToFinish(){ //assign me the token for the first player to finish
        hand.setEndgame();
    }
    public void results(String nickname, int myPoints){ //show the winner and my points
        JFrame frame = new JFrame();
        JLabel label = new JLabel(""+nickname+" won, you got "+myPoints+ " points");
        frame.setVisible(true);
        frame.add(label);
        frame.setTitle("Game ended");
        frame.setLayout(new GridLayout());
        frame.setSize(400,200);
    }
    public void endInsertion(){
        hand.getImageDisplayed().setTitle("Hand");
    }
}