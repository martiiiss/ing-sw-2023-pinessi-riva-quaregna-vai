package view.GUI;

import distributed.messages.Message;
import model.*;
import org.jetbrains.annotations.NotNull;
import util.*;
import util.Event;
import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**Class that contains all the Graphic User Interface elements*/
public class GUIView implements Observer, Serializable {
    @Serial
    private static final long serialVersionUID = 3573316570282498841L;
    private transient BoardView boardView;
    private transient ScoringTokenView[] scv;
    private transient BookshelfView bookshelfView;

    private transient HandView hand;

    private transient PGCView pgc;

    private transient CGCView[] cgc;

    private transient JLabel errorLog;
    private transient  JTextArea playerList;

    private int tilesToPick = 0;

    private transient Image image;

    private boolean bksChanged = false;

    private final JButton bksButton;

    private boolean bksReturned = false;
    private ArrayList<Player> listPlayers = new ArrayList<>();

    /**
     * Constructor of the class. This creates all the "blocks" of the interface.*/
    public GUIView () {
        //All the page
        GridBagConstraints constraints = new GridBagConstraints();
        JFrame GUI = new JFrame();
        GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        //Board
        GUI.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.gridheight = 2;
        this.boardView = new BoardView();
        GUI.add(this.boardView.getBoardDisplayed(), constraints);

        //Bookshelf
        bookshelfView = new BookshelfView();
        constraints.gridy = 0;
        constraints.gridx = 2;
        constraints.gridwidth = 2;
        constraints.gridheight = 2;
        GUI.add(bookshelfView.getBookshelfDisplayed(), constraints);

        //Tiles in hand
        hand = new HandView();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        GUI.add(hand.getImageDisplayed(), constraints);


        bksButton = new JButton();

        playerList = new JTextArea();
        JInternalFrame playerListFrame = new JInternalFrame();
        playerListFrame.setVisible(true);
        playerListFrame.setTitle("Players List");
        playerList.setFont(new Font("Arial", Font.BOLD, 14));
        playerList.setForeground(Color.black);
        playerListFrame.setPreferredSize(new Dimension(300,150));
        playerList.setPreferredSize(new Dimension(250, 100));
        playerList.setMinimumSize(new Dimension(250,70));
        playerListFrame.add(playerList);
        playerListFrame.setVisible(true);
        playerList.setEditable(false);
        constraints.gridx = 1;
        GUI.add(playerListFrame, constraints);


        //CGC
        JInternalFrame CGCArea = new JInternalFrame();
        CGCArea.setLayout(new GridLayout(2, 2));
        CGCArea.setTitle("CommonGoalCards");
        cgc = new CGCView[2];
        CGCArea.setVisible(true);
        CGCArea.setMinimumSize(new Dimension(175, 175));
        constraints.gridy = 1;
        constraints.gridx = 4;
        //constraints.insets = new Insets(5, 5,5,5);
        constraints.gridwidth = 1;
        constraints.gridheight = 2;
        GUI.add(CGCArea, constraints);

        //Scoring token
        scv = new ScoringTokenView[2];
        for (int i = 0; i < 2; i++) {
            scv[i] = new ScoringTokenView();
            CGCArea.add(scv[i].getDisplayedImage());
            cgc[i] = new CGCView();
            CGCArea.add(cgc[i]);
        }

        //PersonalGoalCard
        pgc = new PGCView();
        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        GUI.add(pgc.getDisplayedImage(), constraints);

        //Error log
        errorLog = new JLabel();
        JInternalFrame errorLogFrame = new JInternalFrame();
        errorLogFrame.setVisible(true);
        errorLogFrame.setTitle("Message Log");
        errorLog.setFont(new Font("Arial", Font.BOLD, 18));
        errorLog.setForeground(Color.RED);
        errorLogFrame.setPreferredSize(new Dimension(300,100));
        errorLog.setPreferredSize(new Dimension(250, 70));
        errorLog.setMinimumSize(new Dimension(250,70));
        constraints.gridx = 2;
        constraints.gridy = 2;
        errorLogFrame.add(errorLog);
        GUI.add(errorLogFrame, constraints);


        GUI.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        GUI.setBounds(0,0, screenSize.width, screenSize.height);
        GUI.setResizable(true);


    }

    /**
     * Method that changes the scoring token displayed in the romanNumber position.
     * @param sc is a {@link ScoringToken}*/
    public void changeScoringToken(@NotNull ScoringToken sc) {
        scv[sc.getRomanNumber()].popSCV(sc.getValue());
        hand.setSC(sc.getValue(), sc.getRomanNumber());
    }

    /**
     * Method used to get the hand view.
     * @return {@link HandView}*/
    public HandView getHandView() {
        return this.hand;
    }

    /**
     * Method used to get the BookshelfView.
     * @return {@link BookshelfView}*/
    public BookshelfView getBookshelfView() {
        return this.bookshelfView;
    }

    /**
     * Method used to get the BoardView.
     * @return {@link BoardView}*/
    public BoardView getBoardView() {
        return this.boardView;
    }

    /**
     * Method used to get the ScoringTokenView.
     * @return {@link ScoringTokenView}*/
    public ScoringTokenView getScv(int i) {
        return scv[i-1];
    }

    /**
     * Method used to get the PGCView.
     * @return {@link PGCView}*/
    public PGCView getPGC() {
        return pgc;
    }

    /**
     * Method used to get the CGCView.
     * @return {@link CGCView}*/
    public CGCView getCGC(int i) {
        return cgc[i-1];
    }

    /**
     * Method used to set up the board or update it after someone else pick tiles.
     * @param board is the {@code Board} that has been modified*/
    public void updateBoard(Board board){
        if(boardView!=null)
            boardView.updateBoard(board);
    }

    /**
     * Method used to set up the cgc and the scoring token.
     * @param cgc is the {@code CommonGoalCard} that has been modified*/
    public void setupCGC(@NotNull CommonGoalCard cgc){
        System.out.println("cgc " + cgc.getRomanNumber());
        getCGC(cgc.getRomanNumber()).setCGCView(cgc.getIdCGC());
        getScv(cgc.getRomanNumber()).setDisplayedImage(cgc.getTokenStack().lastElement().getValue());
    }

    /**
     * Method used to set up the personal goal card.
     * @param pgcID is an int that represents the id of a {@code PersonalGalCard}*/
    public void setupPGC(int pgcID){
        this.pgc.setDisplayedImage(pgcID);
    }

    /**
     * Method that asks the GUI to choose a number of tiles.
     * @return an int that represents the number of chosen tiles*/
    public int askTiles(){
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

    /**Method that asks the GUI to choose tiles and returns an arraylist of them.
     * @return an {@code ArrayList} of {@code Cord}*/
    public ArrayList <Cord> getTilesClient(){
        boardView.getListTilesPicked().clear();
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

    /**
     * Method used to pick tiles from the board and put the in the hand.
     * @param tiles is an {@code ArrayList} of {@code Tile}
     * @param cords is an {@code ArrayList} of {@code Cord}*/
    public void pickTiles(ArrayList<Cord> cords, @NotNull ArrayList<Tile> tiles){
        int i=0;
        while(i<tiles.size()) {
            boardView.pickTile(cords.get(i).getRowCord(), cords.get(i).getColCord());
            hand.setTilesInHand(tiles.get(i));
            i++;
        }
    }

    /**Method used to choose the column where the player wants to put the tile.
     * @return an int that represents the column*/
    public int chooseColumn(){
        synchronized (bksButton){
            bookshelfView.setCanChange(false);
            bksReturned = true;
            bksButton.notify();
        }
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

    /**
     * Method used to choose the tile to insert and return its position.
     * @return an int that represents the index of a tile*/
    public int chooseTile(){
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

    /**
     * Method used to add the tile to the bookshelf.
     * @param tile a {@link Tile} tat has to be put into the bookshelf*/
    public void addTile(Tile tile){
        bookshelfView.insertTile(bookshelfView.getColumnChosen(), tile);
    }

    /**
     * Method used to show the errors and messages.
     * @param e an {@link Event}
     * @param o an {@code Object}*/
    public void showError(Event e, Object o){
        errorLog.setForeground(Color.RED);
        if(e==Event.OK)
            errorLog.setForeground(Color.GREEN);
        if(e==Event.START_YOUR_TURN || e==Event.NOT_YOUR_TURN)
            errorLog.setForeground(Color.BLACK);
        if(e!=Event.END)
            errorLog.setText("<html>" + e.getMsg() + "</p><html>");
        else {
            errorLog.setForeground(Color.black);
            Player winner = (Player) o;
            errorLog.setText("Game over, The winner is "+winner.getNickname()+" with "+winner.getScore()+" points");
        }
       /* switch (e){
            case TILES_NOT_VALID-> errorLog.setText("<html><p>Tiles not valid, select again</p><html>");
            case COLUMN_NOT_VALID -> errorLog.setText("<html><p>You selected a column with not enough space,try again</p><html>");
            case INVALID_VALUE -> errorLog.setText("<html><p>Retry, invalid value... </p><html>");
            case REPETITION, BLOCKED_NOTHING, NOT_ON_BORDER, NOT_ADJACENT -> errorLog.setText("<html>"+e.getMsg()+"</p><html>");

            //TODO: aggiungere i vari errori!
        }*/
    }

    /**
     * Method used to show the list of players for a match with their score.
     * @param list is an {@code ArrayList} of {@code Player}*/
    public void loadPlayers(ArrayList<Player> list) {
        if (listPlayers.isEmpty())
            listPlayers = list;
        StringBuilder textToPrint = new StringBuilder();
        for (Player p : list) {
            textToPrint.append(p.getNickname()).append(" SCORE: ").append(p.getScore()).append("\n");
        }
        playerList.setText(textToPrint.toString());
    }

    /**
     * Method used to put the scoring token in the player's hand
     * @param sc is a {@code ScoringToken}*/
    public void scoringTokenTakenByMe(@NotNull ScoringToken sc){
        hand.setSC(sc.getValue(), sc.getRomanNumber());
        changeScoringToken(sc);
    }

    /**
     * Method used to assign the player the token for the first player to finish*/
    public void firstToFinish(){
        hand.setEndgame();
    }
    /*public void results(String nickname, int myPoints){ //show the winner and my points
        JFrame frame = new JFrame();
        JLabel label = new JLabel(""+nickname+" won, you got "+myPoints+ " points");
        frame.setVisible(true);
        frame.add(label);
        frame.setTitle("Game ended");
        frame.setLayout(new GridLayout());
        frame.setSize(400,200);
    }*/

    /**
     * Method that detects that a player has finished to insert tiles into its bookshelf*/
    public void endInsertion(){
        synchronized (bksButton) {
            hand.getImageDisplayed().setTitle("Hand");
            bookshelfView.setCanChange(true);
        }
    }

    /**
     * Method used to show the bookshelves of the different players.
     * @param players is an {@code ArrayList} of {@code Player}
     * @param nextPlayerIndex is an int that represents the index of the next player*/
    public boolean nextBookshelf(ArrayList <Player> players, int nextPlayerIndex) {
        synchronized (bksButton){
            while (!bksChanged && !bksReturned) {
                try {
                    bksButton.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if(bksChanged) {
                bookshelfView.changeBookshelf(players.get(nextPlayerIndex).getMyBookshelf());
                bookshelfView.getBookshelfDisplayed().setTitle("" + players.get(nextPlayerIndex).getNickname() + "'s Bookshelf");
            }
            if(bksReturned) {
                bksReturned = false;
                return true;
            }
            bksChanged = false;
            return false;

        }
    }

    //TODO: DELETE THIS METHOD
    public void returnToMyBookshelf(@NotNull ArrayList <Player> players, int nextPlayerIndex){
        bookshelfView.changeBookshelf(players.get(nextPlayerIndex).getMyBookshelf());
        bookshelfView.getBookshelfDisplayed().setTitle("" + players.get(nextPlayerIndex).getNickname() + "'s Bookshelf");
    }

    /**
     * Method used to update a specific object.
     * @param o is an {@code Object}
     * @param message is a {@link Message}*/
    @Override
    public void update(Observable o, Message message) {
        switch(message.getMessageEvent()){
            case SET_UP_BOARD, REMOVE_TILE_BOARD -> {
                updateBoard((Board)o);
                System.out.println("aggiorna view ");
            }
            case UPDATE_SCORINGTOKEN -> {
                CommonGoalCard cgc = (CommonGoalCard) message.getObj();
                scv[cgc.getRomanNumber()-1].setDisplayedImage(cgc.getTokenStack().lastElement().getValue());
            }
            case UPDATED_SCORE -> {
                ArrayList<Player> listRec = (ArrayList<Player>) message.getObj();
                loadPlayers(listRec);
            }
            case END -> {
                showError(Event.END,message.getObj()); //Object is the Winner
            }
            case UPDATE_SCORINGTOKEN_1 -> {
                CommonGoalCard cgc = (CommonGoalCard) message.getObj();
                scv[0].setDisplayedImage(cgc.getTokenStack().lastElement().getValue());
            }
            case UPDATE_SCORINGTOKEN_2 -> {
                CommonGoalCard cgc = (CommonGoalCard) message.getObj();
                scv[1].setDisplayedImage(cgc.getTokenStack().lastElement().getValue());
            }
        }
    }

    /**
     * Method used to perform an action after receiving a specific message.
     * @param message is a {@link Message}*/
    @Override
    public void onUpdate(Message message){

    }
}