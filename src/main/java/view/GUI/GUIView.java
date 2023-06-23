package view.GUI;

import distributed.messages.Message;
import model.*;
import util.*;
import util.Event;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.Objects;

public class GUIView implements Observer { //class that contains all the GUI elements
    private BoardView boardView;
    private ScoringTokenView[] scv;
    private BookshelfView bookshelfView;

    private HandView hand;

    private PGCView pgc;

    private CGCView[] cgc;

    private int tilesToPick = 0;

    public GUIView () {
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
        GUI.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        GUI.setBounds(0,0, screenSize.width, screenSize.height);
        GUI.setExtendedState(JFrame.MAXIMIZED_BOTH);

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
    public int askTiles(){ //invoked by client, asks the GUI to choose tiles and returns an arraylist of them
        JFrame frame = new JFrame();
        for(int i=1 ; i<4; i++){
            JButton button = new JButton();
            frame.add(button);
            button.setText(""+i);
            button.setVisible(true);
            button.addActionListener(e ->{
                tilesToPick= (Integer.parseInt(button.getText()));
                frame.dispose();
                System.out.println("selezionato: " +  tilesToPick);
                synchronized (this){
                    this.notify();
                }
            });
        }
        frame.setVisible(true);
        frame.setLayout(new GridLayout());
        frame.setSize(400,200);
        frame.setTitle("How many tiles you want to pick?");
        synchronized (this){
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        boardView.setTilesPicked(tilesToPick);
        return this.tilesToPick;
    }

    public ArrayList <Cord> getTilesClient(){
        boardView.getListTilesPicked().removeAll(boardView.getListTilesPicked());
        System.out.println("setta a true can pick");
        boardView.setCanPick(true);
        boardView.getBoardDisplayed().setTitle("Click the tiles to pick them");
        synchronized (boardView){
            try {
                boardView.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("get tiles picked " + boardView.getTilesPicked() );
        boardView.setCanPick(false);
        boardView.getBoardDisplayed().setTitle("Board");
        return boardView.getListTilesPicked();
    }

    public void pickTiles(ArrayList<Cord> cords, ArrayList<Tile> tiles){ //pick the tiles from the board and put them in the hand
        int i=0;
        while(i<tiles.size()) {
            boardView.pickTile(cords.get(i).getRowCord(), cords.get(i).getColCord());
            hand.setTilesInHand(tiles.get(i));
            i++;
        }
    }
    public int chooseColumn(){ //choose the column where i want to put the tile and return it
        bookshelfView.getBookshelfDisplayed().setTitle("Choose the column to insert tiles by clicking one of its buttons");
        bookshelfView.setColumnChosen(-1);
        synchronized (bookshelfView){
            try{
                bookshelfView.wait();
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
        bookshelfView.getBookshelfDisplayed().setTitle("Bookshelf");
        return bookshelfView.getColumnChosen();
    }
    public int chooseTile(){ //choose the tile to insert and return its position
        hand.setTileToInsert(0);
        hand.getImageDisplayed().setTitle("Choose the first tile to insert by clicking on it");
        synchronized (hand){
            try {
                hand.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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
            case INVALID_VALUE -> boardView.getBoardDisplayed().setTitle("Retry, invalid value... ");
            case REPETITION, BLOCKED_NOTHING, NOT_ON_BORDER, NOT_ADJACENT -> boardView.getBoardDisplayed().setTitle(e.getMsg());

            //TODO: aggiungere i vari errori!
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

    @Override
    public void update(Observable o, Message message) {
        switch(message.getMessageEvent()){
            case REMOVE_TILE_BOARD, SET_UP_BOARD -> {
                updateBoard((Board)o);
                System.out.println("aggiorna view ");
            }
        }
    }

    @Override
    public void onUpdate(Message message) throws IOException {

    }
}