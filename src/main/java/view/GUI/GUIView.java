package view.GUI;

import distributed.messages.Message;
import model.*;
import util.*;
import util.Event;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class GUIView implements Observer, Serializable { //class that contains all the GUI elements
    @Serial
    private static final long serialVersionUID = 3573316570282498841L;
    private transient BoardView boardView;
    private transient ScoringTokenView[] scv;
    private transient BookshelfView bookshelfView;

    private transient HandView hand;

    private transient PGCView pgc;

    private transient CGCView[] cgc;

    private transient JLabel errorLog;

    private int tilesToPick = 0;

    private transient Image image;

    public GUIView () {
        GridBagConstraints constraints = new GridBagConstraints();
        JFrame GUI = new JFrame();
        ImageReader imageReader = new ImageReader();
        image = imageReader.readImage("resources/parquet.jpg", 2000, 2000);
        JPanel imagePanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        GUI.setContentPane(imagePanel);
        JInternalFrame CGCArea = new JInternalFrame();
        GUI.setLayout(new GridBagLayout());
        constraints.gridx= 0;
        constraints.gridy =0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        this.boardView = new BoardView();
        GUI.add(this.boardView.getBoardDisplayed(), constraints);
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
        constraints.gridy = 1;
        constraints.insets = new Insets(10, 10,10,10);
        constraints.gridwidth = 1;
        GUI.add(CGCArea, constraints);
        hand = new HandView();
        constraints.gridx = 1;
        GUI.add(hand.getImageDisplayed(), constraints);
        bookshelfView = new BookshelfView();
        constraints.gridy = 0;
        constraints.gridx = 2;
        GUI.add(bookshelfView.getBookshelfDisplayed(), constraints);
        pgc = new PGCView();
        constraints.gridx = 3;
        GUI.add(pgc.getDisplayedImage(), constraints);
        errorLog = new JLabel();
        JInternalFrame errorLogFrame = new JInternalFrame();
        errorLogFrame.setVisible(true);
        errorLogFrame.setTitle("Error Log");
        errorLogFrame.setPreferredSize(new Dimension(300,100));
        errorLog.setPreferredSize(new Dimension(250, 70));
        errorLog.setMinimumSize(new Dimension(250,70));
        constraints.gridx = 2;
        constraints.gridy = 1;
        errorLogFrame.add(errorLog);
        GUI.add(errorLogFrame, constraints);
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
        System.out.println("AAAAAAAAAAAAA");
        if(boardView!=null)
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
            case TILES_NOT_VALID-> errorLog.setText("<html><p>Tiles not valid, select again</p><html>");
            case COLUMN_NOT_VALID -> errorLog.setText("<html><p>You selected a column with not enough space,try again</p><html>");
            case INVALID_VALUE -> errorLog.setText("<html><p>Retry, invalid value... </p><html>");
            case REPETITION, BLOCKED_NOTHING, NOT_ON_BORDER, NOT_ADJACENT -> errorLog.setText("<html>"+e.getMsg()+"</p><html>");

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
        System.out.println("update in gui della board ");
        switch(message.getMessageEvent()){
            case SET_UP_BOARD, REMOVE_TILE_BOARD -> {
                updateBoard((Board) message.getObj());
                System.out.println("aggiorna view ");
            }
        }
    }

    @Override
    public void onUpdate(Message message) throws IOException {

    }
}