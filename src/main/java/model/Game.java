package model;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Player winner;
    private int numberOfPlayers;
    private Player playerInTurn; // we don't know if we'll have to delete this
    private Player finisher;
    private boolean isLastTurn;

    private int numOfPlayers;

    private PersonalGoalCard personalGoalCard;

    private ArrayList<Player> players;

    private ArrayList<CommonGoalCard> commonGoalCards;


    public Game() { // added the constructor
    }

    public void setCommonGoalCards(){ //choose 2 commonGoalCard
        int n1 = (new Random()).nextInt(12)+1; //random number (1-12)
        int n2 = (new Random()).nextInt(12)+1;
        if(n1==n2){
            n2++;
        }

        CommonGoalCard c1 = new CommonGoalCard(n1, numberOfPlayers, 1);
        CommonGoalCard c2 = new CommonGoalCard(n2, numberOfPlayers, 2);

        commonGoalCards.add(c1); //add c1, c2 to arrayList
        commonGoalCards.add(c2);
    }

    public ArrayList<CommonGoalCard> getCommonGoalCard(){
        return this.commonGoalCards;
    }

    public void setNumOfPlayers(int nop){ //nop is chosen by the player who creates the game
        this.numberOfPlayers = nop;
    }

    public int getNumOfPlayers(){return this.numberOfPlayers;}

    public void setPlayerInTurn(Player pit){this.playerInTurn = pit;}

    public Player getPlayerInTurn(){return this.playerInTurn;}

    public void setWinner(Player win){this.winner = win;}

    public Player getWinner(){return this.winner;}


    public void setFinisher(Player finished){
        this.finisher = finished; //set finisher
        isLastTurn = true; // it updates the value of isLastTurn to TRUE
    }

    public Player getFinisher(){return this.finisher;}

    public boolean getIsLastTurn(){return this.isLastTurn;}

    /*Using the number of players I create different random number in between 1 and 12
     *I control that every number is different
     *I assign the corresponding card to every player
     */
    public void assignPersonalGoalCard(int nOfPlayers){
        if (nOfPlayers==2){
            int temp1 = (new Random()).nextInt(12)+1;
            int temp2 = (new Random()).nextInt(12)+1;
            while (temp1 == temp2){temp2 = (new Random()).nextInt(12)+1;}
                PersonalGoalCard pgc1 = new PersonalGoalCard(temp1);
                players.get(0).setPersonalGoalCard(pgc1);
                PersonalGoalCard pgc2 = new PersonalGoalCard(temp2);
                players.get(1).setPersonalGoalCard(pgc2);

        } else if (nOfPlayers==3) {
            int temp1 = (new Random()).nextInt(12)+1;
            int temp2 = (new Random()).nextInt(12)+1;
            int temp3 = (new Random()).nextInt(12)+1;
            while (temp1 == temp2 && temp2 == temp3 && temp1 == temp3) {
                temp2 = (new Random()).nextInt(12)+1;
                temp3 = (new Random()).nextInt(12)+1;
            }
            PersonalGoalCard pgc1 = new PersonalGoalCard(temp1);
            players.get(0).setPersonalGoalCard(pgc1);
            PersonalGoalCard pgc2 = new PersonalGoalCard(temp2);
            players.get(1).setPersonalGoalCard(pgc2);
            PersonalGoalCard pgc3 = new PersonalGoalCard(temp3);
            players.get(2).setPersonalGoalCard(pgc3);
        } else if (nOfPlayers==4){
            int temp1 = (new Random()).nextInt(12)+1;
            int temp2 = (new Random()).nextInt(12)+1;
            int temp3 = (new Random()).nextInt(12)+1;
            int temp4 = (new Random()).nextInt(12)+1;

            while (temp1 == temp2 && temp1 == temp3 && temp1 == temp4
                && temp2 == temp3 && temp2 == temp4 && temp3 == temp4) {
                temp2 = (new Random()).nextInt(12)+1;
                temp3 = (new Random()).nextInt(12)+1;
                temp4 = (new Random()).nextInt(12)+1;
            }
            PersonalGoalCard pgc1 = new PersonalGoalCard(temp1);
            players.get(0).setPersonalGoalCard(pgc1);
            PersonalGoalCard pgc2 = new PersonalGoalCard(temp2);
            players.get(1).setPersonalGoalCard(pgc2);
            PersonalGoalCard pgc3 = new PersonalGoalCard(temp3);
            players.get(2).setPersonalGoalCard(pgc3);
            PersonalGoalCard pgc4 = new PersonalGoalCard(temp4);
            players.get(3).setPersonalGoalCard(pgc4);
        }
    } //TODO control if this method is ok

    // it adds the player into the array
    public void addPlayer(Player p){players.add(p);}

    /* If st1 or st2 are FALSE, I verify if the player has completed one or both CGC
     * -> if the player has completed the CGC i set the ScoringToken flag to TRUE and return scoreST, the score given by the CGC
     */
    public int checkCommonGoalCard(){
        int scoreST = 0;
        boolean st1 = playerInTurn.getScoringToken1();
        boolean st2 = playerInTurn.getScoringToken2();

        ArrayList<CommonGoalCard> c = this.getCommonGoalCard(); // -> maybe this is ok
        if(st1 == false) {
            if (c.get(0).compare(playerInTurn.getMyBookshelf())==true){
                playerInTurn.setScoringToken1(true);
                ScoringToken firstScoringTokenTile = c.get(0).popScoringToken();
                scoreST += firstScoringTokenTile.getValue();
            }
        }
        if (st2 == false){
            if(c.get(1).compare(playerInTurn.getMyBookshelf())==true){
                playerInTurn.setScoringToken2(true);
                ScoringToken secondScoringTokenTile = c.get(1).popScoringToken();
                scoreST += secondScoringTokenTile.getValue();
            }
        }
        return scoreST;
    }
}
