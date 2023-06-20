package view.GUI;

import distributed.messages.Message;
import model.Board;
import model.Game;
import model.Player;
import model.Tile;
import util.*;
import util.Event;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GUIView extends Observable implements Observer { //class that contains all the GUI elements
    private String nickname;
    private int playerNumber;
    private BoardView boardView;
    private ScoringTokenView[] scv;
    private BookshelfView bookshelfView;

    private HandView hand;

    private PGCView pgc;

    private CGCView[] cgc;

    private boolean isMyTurn;

    private int tilesMoved = 0;

    private ArrayList <Cord> listTilesPicked;

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
        boardView = new BoardView();
        boardView.addObserver(this);
        GUI.add(boardView.getBoardDisplayed());
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
        hand.addObserver(this);
        bookshelfView = new BookshelfView();
        bookshelfView.addObserver(this);
        GUI.add(bookshelfView.getBookshelfDisplayed());
        pgc = new PGCView();
        GUI.add(pgc.getDisplayedImage());
        GUI.pack();
        GUI.setVisible(true);

    }

    public void changeSC(int valueDisplayed, int romanNumber) {
        scv[romanNumber].popSCV(valueDisplayed);
        hand.setSC(valueDisplayed, romanNumber);
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
        return scv[i];
    }

    public PGCView getPGC() {
        return pgc;
    }

    public CGCView getCGC(int i) {
        return cgc[i];
    }

    public boolean getIsMyTurn() {
        return this.isMyTurn;
    }

    public void setIsMyTurn(boolean value) {
        this.isMyTurn = value;
    }

    @Override
    public void update(Observable observable, Message message) {
        switch (message.getMessageEvent()) {
            case REMOVE_TILE_BOARD ->
                    boardView.pickTile(((TileForMessages) message.getObj()).getRow(), ((TileForMessages) message.getObj()).getColumn());
            case UPDATE_BOOKSHELF -> {
                hand.removeTileInHand(tilesMoved);
                bookshelfView.insertTile(((TileForMessages) message.getObj()).getColumn(), ((TileForMessages) message.getObj()).getTile());
                tilesMoved++;
            }
            //case SET_SCORING_TOKEN_1 -> guiView.changeSC(message.getObj()controller.getInstanceOfGame().getCommonGoalCard().get(0).getTokenStack().get((this.controller.getInstanceOfGame().getNumOfPlayers())-1).getValue(), 1); // these two will add the scoringToken I took to my hand
            //case SET_SCORING_TOKEN_2 -> guiView.changeSC(this.controller.getInstanceOfGame().getCommonGoalCard().get(0).getTokenStack().get((this.controller.getInstanceOfGame().getNumOfPlayers())-1).getValue(), 2);
            case SET_UP_BOARD -> boardView.updateBoard(((Board) message.getObj()));
            case SET_PGC -> pgc.setDisplayedImage(((Game) message.getObj()).getPlayers().get(playerNumber).getPersonalGoalCard().getNumber());
            case SET_COMMONGC -> {
                getCGC(0).setCGCView(((Game) message.getObj()).getCommonGoalCard().get(0).getIdCGC());
                getCGC(1).setCGCView(((Game) message.getObj()).getCommonGoalCard().get(1).getIdCGC());
                getScv(0).setDisplayedImage(((Game) message.getObj()).getCommonGoalCard().get(0).getTokenStack().get(((Game) message.getObj()).getNumOfPlayers() - 1).getValue());
                getScv(1).setDisplayedImage(((Game) message.getObj()).getCommonGoalCard().get(1).getTokenStack().get(((Game) message.getObj()).getNumOfPlayers() - 1).getValue());
            }
            case SET_PLAYER_IN_TURN, SET_FIRST_PLAYER -> {
                if (((Game) message.getObj()).getPlayers().get(playerNumber).getIsFirstPlayer()) {
                    setIsMyTurn(true);
                    boardView.setCanPick(true);
                }
            }
            case SET_SCORING_TOKEN_1 -> {
                if (Objects.equals(((Player) message.getObj()).getNickname(), nickname))
                    hand.setSC(getScv(0).getValueDisplayed(), 0);
            }
            case SET_SCORING_TOKEN_2 -> {
                if (Objects.equals(((Player) message.getObj()).getNickname(), nickname))
                    hand.setSC(getScv(1).getValueDisplayed(), 1);
            }
            case ASK_CAN_PICK -> this.listTilesPicked.add((Cord) message.getObj());
            case OK_TO_PICK -> {
                if (boardView.isCanPick()) {
                    boardView.pickTile(((TileForMessages) message.getObj()).getRow(), ((TileForMessages) message.getObj()).getColumn());
                    hand.setTilesInHand(((TileForMessages) message.getObj()).getTile());
                    notifyObservers(new Message(message.getObj(), Event.TILE_PICKED));
                }
            }

            case SET_TILE_ORDER -> {
                if (!boardView.isCanPick() && !hand.searchOrder((int) message.getObj())) {
                    hand.insertFirstVoid((int) message.getObj());
                }
            }

        }
    }

    //TODO set tilesMoved to 0 once turn ends

    @Override
    public void onUpdate(Message message) throws IOException {

    }
    //aggiungo metodo per setup
    public ArrayList <Cord> askTiles(){ //invoked by client, asks the GUI to choose tiles and returns an arraylist of them
        //crea pop up per chiedere quante tiles prendere con 3 pulsanti
        //setTilesPicked(int valore pop up)
        boardView.setCanPick(true);
        while (boardView.getTilesPicked()!=0){
        }
        boardView.setCanPick(false);
        return this.listTilesPicked;
    }
    public void pickTiles(ArrayList<Cord> cords, ArrayList<Tile> tiles){ //pick the tiles from the board and put them in the hand
        int i=0;
        while(cords.get(i)!=null){
            boardView.pickTile(cords.get(i).getRowCord(), cords.get(i).getColCord());
            hand.setTilesInHand(tiles.get(i));
        }
    }
    public int chooseColumn(){ //choose the column where i want to put the tile and return it
        bookshelfView.setColumnChosen(0);
        while(bookshelfView.getColumnChosen()==0){

        }
        return bookshelfView.getColumnChosen();
    }
    public int chooseTile(){ //choose the tile to insert and return its position
        hand.setTileToInsert(0);
        while(hand.getTileToInsert()==0){

        }
        return hand.getTileToInsert();
    }
    public void addTile(Tile tile){ //add the tile to the bookshelf
        bookshelfView.insertTile(bookshelfView.getColumnChosen(), tile);
    }
}