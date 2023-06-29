package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import distributed.messages.Message;
import util.Event;
import util.Observable;
import java.util.Random;

/**Class that represents the Game*/
public class Game extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = -5788544275616187567L;
    private Player winner;
    private int numberOfPlayers;
    private Player playerInTurn;
    private Player finisher;
    private boolean isLastTurn;
    private final ArrayList<Player> players;
    private final ArrayList<CommonGoalCard> commonGoalCards;
    private boolean gameStarted;
    /**
     * <p>
     *     Constructor of the Class.<br>
     *     This initializes the <code>commonGoalCards</code>, the <code>players</code>,
     *     the <code>numberOfPlayers</code> and also two status of the game:
     *     <code>isLastTurn</code> and <code>gameStarted</code>to <b>false</b>
     * </p>*/
    public Game() {
        this.commonGoalCards = new ArrayList<>();
        this.players = new ArrayList<>();
        this.numberOfPlayers = 0;
        this.isLastTurn = false;
        this.gameStarted = false;
    }

    /**
     * <p>
     *     Method that returns a boolean on the status of the game.
     * </p>
     * @return <b>true</b> if the game has started, <b>false</b> otherwise*/
    public boolean getGameStarted(){return this.gameStarted;}

    /**
     * <p>
     *     Method that sets the status <code>gameStarted</code> to <b>true</b>
     *     and notifies the Observers with a {@link Message}.
     * </p>
     **/
    public void setGameStarted(){
        this.gameStarted=true;
        notifyObservers(new Message(this, Event.GAME_STARTED));
    }

    /**
     * <p>
     *     Method that returns an <code>ArrayList</code> of player.
     * </p>
     * @return <code>players</code>, an ArrayList
     */
    public ArrayList<Player> getPlayers(){return this.players;}

    /**
     * <p>
     *     Method that randomly chooses two common goal cards
     *     and constructs them using the {@link model.CommonGoalCard} constructor.
     *     It then adds the created cards into <code>commonGoalCards</code>,
     *     finally it notifies the Observers with a {@link Message}.
     * </p>*/
    public void setCommonGoalCards(){
        int n1 = (new Random()).nextInt(12)+1; //random number (1-12)
        int n2 = (new Random()).nextInt(12)+1;
        while(n1==n2){
            n2 = (new Random()).nextInt(12)+1;
        }

        CommonGoalCard c1 = new CommonGoalCard(n1, numberOfPlayers, 1);
        CommonGoalCard c2 = new CommonGoalCard(n2, numberOfPlayers, 2);

        commonGoalCards.add(c1);
        commonGoalCards.add(c2);
        notifyObservers(new Message(this, Event.SET_COMMONGC));
    }

    /**
     * <p>
     *     Method that returns the two common goal cards.
     * </p>
     * @return <code>commonGoalCards</code>, an ArrayList of <code>CommonGoalCard</code>*/
    public ArrayList<CommonGoalCard> getCommonGoalCard(){
        return this.commonGoalCards;
    }

    /**
     * <p>
     *     Method that given an integer sets the number of players of a game
     *     and notifies the Observers with a {@link Message}.
     * </p>
     * @param nop is an int in between 2 and 4 that represents the number of players*/
    public void setNumOfPlayers(int nop){
        this.numberOfPlayers = nop;
        notifyObservers(new Message(this, Event.SET_NUM_PLAYERS));
    }

    /**
     * <p>
     *     Method that returns the number of players of a game.
     * </p>
     * @return an int that represents <code>numberOfPlayers</code>*/
    public int getNumOfPlayers(){return this.numberOfPlayers;}

    /**
     * <p>
     *     Method that sets a player, given as a parameter, as "in turn".
     *     This also notifies the Observers with a {@link Message}
     * </p>
     * @param pit a player that has to be set as currently "in turn"*/
    public void setPlayerInTurn(Player pit){
        this.playerInTurn = pit;
        notifyObservers(new Message(this, Event.SET_PLAYER_IN_TURN));
    }

    /**
     * <p>
     *     Method that returns the player currently "in turn".
     * </p>
     * @return the <code>Player</code> in turn*/
    public Player getPlayerInTurn(){return this.playerInTurn;}

    /**
     * <p>
     *     Method that sets a player as the winner.
     *     This also notifies the Observers with a {@link Message}
     * </p>
     * @param win the <code>Player</code> that has to be set as winner*/
    public void setWinner(Player win){
        this.winner = win;
        notifyObservers(new Message(this, Event.SET_WINNER));
    }

    /**
     * <p>
     *     Method that returns the winner.
     * </p>
     * @return a <code>Player</code>, the winner*/
    public Player getWinner(){return this.winner;}

    /**
     * <p>
     *     Method that given <code>Player</code> as a parameter sets it as the first to finish.
     *     This also notifies the Observers with a {@link Message}
     * </p>
     * @param finished the <code>Player</code> that has to be set as the first to finish*/
    public void setFinisher(Player finished){
        this.finisher = finished; //set finisher
        this.isLastTurn = true; // it updates the value of isLastTurn to TRUE
        notifyObservers(new Message(this, Event.SET_FINISHER));
    }

    /**
     * <p>
     *     Method that returns the first player to finish.
     * </p>
     * @return a <code>Player</code>, the first to finish*/
    public Player getFinisher(){return this.finisher;}

    /**
     * <p>
     *     Method that returns a boolean on the status of the game.
     * </p>
     * @return <b>true</b> if is the last turn, <b>false</b> otherwise*/
    public boolean getIsLastTurn(){return this.isLastTurn;}


    /**
     * <p>
     *     Method that randomly generates and assigns a different personal goal card for each player.<br>
     *     This also notifies the Observers with a {@link Message}.
     * </p>
     * @param nOfPlayers an int in between 2 and 4 that represents the number of players*/
    public void assignPersonalGoalCard(int nOfPlayers){
        int id1,id2,id3,id4;
        PersonalGoalCard pgc1,pgc2,pgc3,pgc4;
        //The basic case is having only two players
        id1 = (new Random()).nextInt(12) + 1;
        id2 = (new Random()).nextInt(12) + 1;
        while (id1 == id2) {
            id2 = (new Random()).nextInt(12) + 1;
        }
        pgc1 = new PersonalGoalCard(id1);
        players.get(0).setPersonalGoalCard(pgc1);
        pgc2 = new PersonalGoalCard(id2);
        players.get(1).setPersonalGoalCard(pgc2);

        switch (nOfPlayers) {
            case 3 -> {
                id3 = (new Random()).nextInt(12) + 1;
                while (id1 == id3 || id2 == id3) {
                    id3 = (new Random()).nextInt(12) + 1;
                }
                pgc3 = new PersonalGoalCard(id3);
                players.get(2).setPersonalGoalCard(pgc3);
            }
            case 4 -> {
                id3 = (new Random()).nextInt(12) + 1;
                id4 = (new Random()).nextInt(12) + 1;
                while (id1 == id3 || id2 == id3) {
                    id3 = (new Random()).nextInt(12) + 1;
                }
                while (id1 == id4 || id2 == id4 || id3 == id4) {
                    id4 = (new Random()).nextInt(12) + 1;
                }
                pgc3 = new PersonalGoalCard(id3);
                players.get(2).setPersonalGoalCard(pgc3);
                pgc4 = new PersonalGoalCard(id4);
                players.get(3).setPersonalGoalCard(pgc4);
            }
        }
        notifyObservers(new Message(this, Event.SET_PGC));
    }

    /**
     * <p>
     *     Method that adds a <code>Player</code>, given as a parameter, into the ArrayList that contains all the player in game.
     * </p>
     * @param p a <code>Player</code> that has to be added*/
    public void addPlayer(Player p){players.add(p);}

    /**
     * <p>
     *     Method that returns the total score of a common goal card for a player.
     * </p>
     * @return an int that represents the total score of the two common goal cards*/
    public int checkCommonGoalCard(){
        int scoreST = 0;
        boolean st1 = playerInTurn.getScoringToken1();
        boolean st2 = playerInTurn.getScoringToken2();

        if(!st1) {
            if (commonGoalCards.get(0).compare(playerInTurn.getMyBookshelf())){
                playerInTurn.setScoringToken1();
                ScoringToken firstScoringTokenTile = commonGoalCards.get(0).popScoringToken();
                scoreST += firstScoringTokenTile.getValue();
            }
        }
        if (!st2){
            if(commonGoalCards.get(1).compare(playerInTurn.getMyBookshelf())){
                playerInTurn.setScoringToken2();
                ScoringToken secondScoringTokenTile = commonGoalCards.get(1).popScoringToken();
                scoreST += secondScoringTokenTile.getValue();
            }
        }
        return scoreST;
    }

    /**
     * Method used to set the last turn.
     * @param lastTurn is a boolean, <b>true</b> if is the last turn <b>false</b> otherwise*/
    public void setLastTurn(boolean lastTurn) {
        isLastTurn = lastTurn;
    }
}

