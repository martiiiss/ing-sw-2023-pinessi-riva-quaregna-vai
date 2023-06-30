package model;

import distributed.messages.Message;
import org.jetbrains.annotations.NotNull;
import util.Cord;
import util.Event;
import util.Observable;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import static model.Type.NOTHING;

/**Class that represents the Player*/
public class Player extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = 627657924675627426L;
    private String nickname;
    private PersonalGoalCard myGoalCard;
    private boolean isFirstPlayer;
    private int score;
    private Bookshelf myBookshelf;
    private int scorePGC = 0;
    private int scoreAdj = 0;
    private boolean scoringToken1;
    private boolean scoringToken2;

    /**
     * <p>
     *     Constructor of the Class.<br>
     *     This sets the two flags <code>scoringToken</code> to <b>false</b>,
     *     initializes the score to 0 and sets the flag <code>isFirstPlayer</code> to <b>false</b>.
     * </p>*/
    public Player () {
        scoringToken1 = false;
        scoringToken2 = false;
        score = 0;
        isFirstPlayer = false;
    }

    /**
     * <p>
     *     Method that sets a <code>Player</code> as first player.
     *     This also notifies the Observers with a {@link Message}.
     * </p>*/
    public void setAsFirstPlayer() {
        this.isFirstPlayer = true;
        notifyObservers(new Message(this, Event.SET_FIRST_PLAYER));
    }

    /**
     * <p>
     *     Method that given a <code>String</code> parameter sets is as the player nickname,
     *     after checking that the parameter is not <b>null</b>.
     *     This also notifies the Observers with a {@link Message}
     * </p>
     * @param nickname a String
     * @throws IllegalArgumentException if <code>nickname</code> is empty*/
    public void setNickname(@NotNull String nickname){
        if(nickname.isEmpty()){
            throw new IllegalArgumentException();
        }
        else {
                this.nickname = nickname;
                notifyObservers(new Message(this, Event.SET_NICKNAME));
        }
    }

    /**
     * <p>
     *     Method that returns the nickname of the <code>Player</code>.
     * </p>
     * @return a <code>String</code> that represents the nickname*/
    public String getNickname(){return this.nickname;}

    /*Assign to each player their bookshelf*/
    public void setMyBookshelf(Bookshelf bookshelf) {
        this.myBookshelf = bookshelf;
        notifyObservers(new Message(this, Event.SET_PLAYER_BOOKSHELF));

    }

    /**
     * <p>
     *     Method that returns the player's bookshelf.
     * </p>
     * @return a <code>Bookshelf</code>, belonging to the player */
    public Bookshelf getMyBookshelf() {
        return this.myBookshelf;
    }

    /**
     * <p>
     *     Method that returns the player's score.
     * </p>
     * @return an int that represents the player's score*/
    public int getScore(){
        return this.score;
    }

    /**
     * <p>
     *     Method that given an int, updates the player's score.
     *     This also notifies the Observers with a {@link Message}.
     * </p>
     * @param score an int that represents one of the three possible partial scores */
    public void updateScore (int score) {
        this.score += score;
        notifyObservers(new Message(this, Event.UPDATED_SCORE));
    }


    /**
     * <p>
     *     Method that sets the given <code>PersonalGoalCard</code> parameter as the player's personal goal card.
     *     This also notifies the Observers with a {@link Message}
     * </p>
     * @param pgc a <code>PersonalGoalCard</code>*/
    public void setPersonalGoalCard(PersonalGoalCard pgc) {this.myGoalCard = pgc;
        notifyObservers(new Message(this, Event.SET_PGC));

    }

    /**
     * <p>
     *     Method that returns the player's personal goal card.
     * </p>
     * @return <code>PersonalGoalCard</code>*/
    public PersonalGoalCard getPersonalGoalCard() {
        return this.myGoalCard;
    }

    /**
     * <p>
     *     Method that returns the status of the first scoring token.
     * </p>
     * @return <b>true</b> if the player has already picked the scoring token of the first common goal card,
     * <b>false</b> otherwise*/
    public boolean getScoringToken1(){return this.scoringToken1;}


    /**
     * <p>
     *     Method that sets to true the flag <code>scoringToken1</code>. This
     *     also notifies the Observers with a {@link Message}.<br>
     *     <b>NOTE:</b> Once the player completes a CommonGoalCard,
     *     they won't be able to collect other points from that CGC,
     *     this method is invoked when a players completes a common goal .
     * </p>*/
    public void setScoringToken1(){
        this.scoringToken1 = true;
        notifyObservers(new Message(this, Event.SET_SCORING_TOKEN_1));

    }

    /**
     * <p>
     *     Method that returns the status of the second scoring token.
     * </p>
     * @return <b>true</b> if the player has already picked the scoring token of the second common goal card,
     * <b>false</b> otherwise*/
    public boolean getScoringToken2(){ return this.scoringToken2; }

    /**
     * <p>
     *     Method that sets to true the flag <code>scoringToken2</code>. This
     *     also notifies the Observers with a {@link Message}.<br>
     *     <b>NOTE:</b> Once the player completes a CommonGoalCard,
     *     they won't be able to collect other points from that CGC,
     *     this method is invoked when a players completes a common goal .
     * </p>*/
    public void setScoringToken2(){
        this.scoringToken2 = true;
        notifyObservers(new Message(this, Event.SET_SCORING_TOKEN_2));
    }
    /**
     * <p>
     *     Method that returns the reached score of the personal goal card.
     *     Based on how many tiles are rightly placed, an int that represents
     *     the personal goal card score, gets returned.
     * </p>
     * @throws IllegalStateException when a number of tiles completed is different from an int in between 0 and 6
     * @return an int that represents the "delta" of the score that has to be added to the general score*/
    public int checkCompletePGC(){
        int PGCScore;
        int numberOfTilesCompleted = 0;
        int r,c;
        for(r=0;r<6;r++){
            for (c=0;c<5;c++){
                if(myGoalCard.getPGC()[r][c].getType() == myBookshelf.getBookshelf()[r][c].getType()
                        && myGoalCard.getPGC()[r][c].getType() != NOTHING){
                    numberOfTilesCompleted++;
                }
            }
        }

        switch (numberOfTilesCompleted) {
            case 0 -> PGCScore = 0;
            case 1 -> PGCScore = 1;
            case 2 -> PGCScore = 2;
            case 3 -> PGCScore = 4;
            case 4 -> PGCScore = 6;
            case 5 -> PGCScore = 9;
            case 6 -> PGCScore = 12;
            default -> throw new IllegalStateException("Unexpected value:"+ numberOfTilesCompleted);
        }
        if(PGCScore<=this.scorePGC) {return 0;}
        int deltaScore = PGCScore-this.scorePGC;
        this.scorePGC=PGCScore;
        return deltaScore;
    }

    /**
     * <p>
     *     Method that returns a partial score regarding the adjacencies
     *     of tiles of the same tipe in the player's bookshelf.
     * </p>
     * @return an int that represents the adjacencies score that has to be added to the general score*/
    public int checkAdjacentBookshelf(){
        score-=scoreAdj;
        scoreAdj=0;
        Tile[][] originalBookshelf = this.myBookshelf.getBookshelf();
        Tile[][] bookshelf = new Tile[6][5];
        for (int x=0; x<6; x++) {
            for (int y = 0; y <5; y++) {
                Type originalType = originalBookshelf[x][y].getType();
                Tile copiedTile = new Tile(originalType,0);
                bookshelf[x][y] = copiedTile;
            }
        }

        boolean newCord;
        Cord cord = new Cord();
        Cord nextTo;
        ArrayList<Cord> listOfCords = new ArrayList<>();
        Tile nothing = new Tile(NOTHING,0);
        int i,j;

        for (int x=0; x<6; x++) {
            for (int y = 0; y <5; y++) {
                for (int counter = 0; counter < listOfCords.size() || listOfCords.size() == 0; counter++) {
                    newCord = true;
                    if (listOfCords.isEmpty()) {
                        i = x;
                        j = y;
                    } else {
                        i = listOfCords.get(counter).getRowCord();
                        j = listOfCords.get(counter).getColCord();
                    }
                    nextTo = new Cord();
                    nextTo.setCords(i - 1, j);
                    if (i > 0)
                        if (bookshelf[i][j].getType() == bookshelf[i - 1][j].getType() && bookshelf[i][j].getType() != NOTHING) {
                            if (!(listOfCords.contains(cord))) {
                                cord.setCords(i, j);
                                listOfCords.add(cord);
                            }
                            for (Cord listOfCord : listOfCords)
                                if (listOfCord.getRowCord() == nextTo.getRowCord() && listOfCord.getColCord() == nextTo.getColCord()) {
                                    newCord = false;
                                    break;
                                }
                            if (newCord && !listOfCords.contains(nextTo))
                                listOfCords.add(nextTo);
                        }
                    newCord = true;
                    nextTo = new Cord();
                    nextTo.setCords(i, j + 1);
                    if (j < 4)
                        if (bookshelf[i][j].getType() == bookshelf[i][j + 1].getType() && bookshelf[i][j].getType() != NOTHING) {
                            if (!(listOfCords.contains(cord))) {
                                cord.setCords(i, j);
                                listOfCords.add(cord);
                            }
                            for (Cord listOfCord : listOfCords)
                                if (listOfCord.getRowCord() == nextTo.getRowCord() && listOfCord.getColCord() == nextTo.getColCord()) {
                                    newCord = false;
                                    break;
                                }
                            if (newCord && !listOfCords.contains(nextTo))
                                listOfCords.add(nextTo);
                        }
                    newCord = true;
                    nextTo = new Cord();
                    nextTo.setCords(i + 1, j);
                    if (i < 5)
                        if (bookshelf[i][j].getType() == bookshelf[i + 1][j].getType() && bookshelf[i][j].getType() != NOTHING) {
                            if (!(listOfCords.contains(cord))) {
                                cord.setCords(i, j);
                                listOfCords.add(cord);
                            }
                            for (Cord listOfCord : listOfCords)
                                if (listOfCord.getRowCord() == nextTo.getRowCord() && listOfCord.getColCord() == nextTo.getColCord()) {
                                    newCord = false;
                                    break;
                                }
                            if (newCord && !listOfCords.contains(nextTo))
                                listOfCords.add(nextTo);
                        }
                    newCord = true;
                    nextTo = new Cord();
                    nextTo.setCords(i, j - 1);
                    if (j > 0)
                        if (bookshelf[i][j].getType() == bookshelf[i][j - 1].getType() && bookshelf[i][j].getType() != NOTHING) {
                            if (!(listOfCords.contains(cord))) {
                                cord.setCords(i, j);
                                listOfCords.add(cord);
                            }
                            for (Cord listOfCord : listOfCords)
                                if (listOfCord.getRowCord() == nextTo.getRowCord() && listOfCord.getColCord() == nextTo.getColCord()) {
                                    newCord = false;
                                    break;
                                }
                            if (newCord && !listOfCords.contains(nextTo))
                                listOfCords.add(nextTo);
                        }
                    if (listOfCords.size() == 0)
                        break;
                }
            }
            for(Cord cord1 : listOfCords) {
                System.out.print(cord1.getRowCord() + " " + cord1.getColCord()+": ");
                System.out.println(bookshelf[cord1.getRowCord()][cord1.getColCord()].getType());
                bookshelf[cord1.getRowCord()][cord1.getColCord()]=nothing;
            }
            if (listOfCords.size() == 3)
                scoreAdj += 2;
            if (listOfCords.size() == 4)
                scoreAdj += 3;
            if (listOfCords.size() == 5)
                scoreAdj += 5;
            if (listOfCords.size() >= 6)
                scoreAdj += 8;
            listOfCords.clear();
        }
        return scoreAdj;
    }


    /**
     * <p>
     *     Method that returns the status of a bookshelf.
     * </p>
     * @return <b>true</b> if a bookshelf has one or more tiles of a regular type, <b>false</b> otherwise.*/
    public boolean checkBookshelf(){
        for(int i=0; i<5; i++) {
            if (myBookshelf.getBookshelf()[0][i].getType() == Type.NOTHING) {
                return false;
            }
        }
        myBookshelf.setAsFull();
        return true;
    }
}

