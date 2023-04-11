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
        commonGoalCards = new ArrayList<>();
        players = new ArrayList<>();
        numberOfPlayers = 0;
    }

    public void setCommonGoalCards(){ //choose 2 commonGoalCard
        int n1 = (new Random()).nextInt(12)+1; //random number (1-12)
        int n2 = (new Random()).nextInt(12)+1;
        System.out.println(n1+" "+n2);
        while(n1==n2){
            n2 = (new Random()).nextInt(12)+1;
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

    /**Using the number of players I create different random number in between 1 and 12
     *I control that every number is different and
     *I assign the corresponding card to every player
     */
    public void assignPersonalGoalCard(int nOfPlayers){
        int id1,id2,id3,id4;
        PersonalGoalCard pgc1,pgc2,pgc3,pgc4;

        switch(nOfPlayers){
            case 2: {
                 id1 = (new Random()).nextInt(12)+1;
                 id2 = (new Random()).nextInt(12)+1;
                while (id1 == id2){id2 = (new Random()).nextInt(12)+1;}
                pgc1 = new PersonalGoalCard(id1);
                players.get(0).setPersonalGoalCard(pgc1);
                pgc2 = new PersonalGoalCard(id2);
                players.get(1).setPersonalGoalCard(pgc2);
                /**The text three lines are used in tests*/
                System.out.println("first two chosen id:");
                System.out.println(id1);
                System.out.println(id2);
            }

            case 3: {
                 id1 = (new Random()).nextInt(12)+1;
                 id2 = (new Random()).nextInt(12)+1;
                 id3 = (new Random()).nextInt(12)+1;
                while(id1==id2){
                    id2 = (new Random()).nextInt(12)+1;
                }
                while(id1 == id3 || id2 == id3) {
                    id3 = (new Random()).nextInt(12)+1;
                }
                pgc1 = new PersonalGoalCard(id1);
                players.get(0).setPersonalGoalCard(pgc1);
                pgc2 = new PersonalGoalCard(id2);
                players.get(1).setPersonalGoalCard(pgc2);
                pgc3 = new PersonalGoalCard(id3);
                players.get(2).setPersonalGoalCard(pgc3);
                /**The text four lines are used in tests*/
                System.out.println("Three chosen id:");
                System.out.println(id1);
                System.out.println(id2);
                System.out.println(id3);
            }
            case 4: {
                 id1 = (new Random()).nextInt(12) +1;
                 id2 = (new Random()).nextInt(12)+1;
                 id3 = (new Random()).nextInt(12)+1;
                 id4 = (new Random()).nextInt(12)+1;
                while(id1==id2){
                    id2 = (new Random()).nextInt(12)+1;
                }
                while(id1 == id3 || id2 == id3) {
                    id3 = (new Random()).nextInt(12)+1;
                }
                while (id1 == id4 || id2 == id4 || id3 == id4) {
                    id4 = (new Random()).nextInt(12)+1;
                }
                pgc1 = new PersonalGoalCard(id1);
                players.get(0).setPersonalGoalCard(pgc1);
                pgc2 = new PersonalGoalCard(id2);
                players.get(1).setPersonalGoalCard(pgc2);
                pgc3 = new PersonalGoalCard(id3);
                players.get(2).setPersonalGoalCard(pgc3);
                pgc4 = new PersonalGoalCard(id4);
                players.get(3).setPersonalGoalCard(pgc4);
                /**The text five lines are used in tests*/
                System.out.println("four chosen id:");
                System.out.println(id1);
                System.out.println(id2);
                System.out.println(id3);
                System.out.println(id4);
            }
        }
    }

    // it adds the player into the array
    public void addPlayer(Player p){players.add(p);}

    /** If st1 or st2 are FALSE, I verify if the player has completed one or both CGC
     * -> if the player has completed the CGC I set the ScoringToken flag to TRUE and return scoreST, the score given by the CGC
     */
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
}

