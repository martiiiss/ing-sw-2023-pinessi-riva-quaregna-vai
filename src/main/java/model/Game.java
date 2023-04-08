package model;

import java.util.ArrayList;
import java.util.Random;
import model.CommonGoalCard;

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
        commonGoalCards = new ArrayList<CommonGoalCard>();
        players = new ArrayList<Player>();
        numberOfPlayers = 0;
    }

    public void setCommonGoalCards(){ //choose 2 commonGoalCard
        int n1 = (new Random()).nextInt(12); //random number (1-12)
        int n2 = (new Random()).nextInt(12);
        if (n1 == 0){n1++;}
        if (n2 == 0){n2++;}
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

    /**Using the number of players I create different random number in between 1 and 12
     *I control that every number is different and
     *I assign the corresponding card to every player
     */
    public void assignPersonalGoalCard(int nOfPlayers){

        switch(nOfPlayers){
            case 2: {
                int temp1 = (new Random()).nextInt(12)+1;
                int temp2 = (new Random()).nextInt(12)+1;
                while (temp1 == temp2){temp2 = (new Random()).nextInt(12)+1;}
                PersonalGoalCard pgc1 = new PersonalGoalCard(temp1);
                players.get(1).setPersonalGoalCard(pgc1);
                PersonalGoalCard pgc2 = new PersonalGoalCard(temp2);
                players.get(2).setPersonalGoalCard(pgc2);
                /**The text three lines are used in tests*/
                System.out.println("first two chosen temp:");
                System.out.println(temp1);
                System.out.println(temp2);
            }

            case 3: {
                int temp1 = (new Random()).nextInt(12)+1;
                int temp2 = (new Random()).nextInt(12)+1;
                int temp3 = (new Random()).nextInt(12)+1;
                while(temp1==temp2){
                    temp2 = (new Random()).nextInt(12)+1;
                }
                while(temp1 == temp3 || temp2 == temp3) {
                    temp3 = (new Random()).nextInt(12)+1;
                }
                PersonalGoalCard pgc1 = new PersonalGoalCard(temp1);
                players.get(0).setPersonalGoalCard(pgc1);
                PersonalGoalCard pgc2 = new PersonalGoalCard(temp2);
                players.get(1).setPersonalGoalCard(pgc2);
                PersonalGoalCard pgc3 = new PersonalGoalCard(temp3);
                players.get(2).setPersonalGoalCard(pgc3);
                /**The text four lines are used in tests*/
                System.out.println("Three chosen temp:");
                System.out.println(temp1);
                System.out.println(temp2);
                System.out.println(temp3);
            }
            case 4: {
                int temp1 = (new Random()).nextInt(12) +1;
                int temp2 = (new Random()).nextInt(12)+1;
                int temp3 = (new Random()).nextInt(12)+1;
                int temp4 = (new Random()).nextInt(12)+1;
                while(temp1==temp2){
                    temp2 = (new Random()).nextInt(12)+1;
                }
                while(temp1 == temp3 || temp2 == temp3) {
                    temp3 = (new Random()).nextInt(12)+1;
                }
                while (temp1 == temp4 || temp2 == temp4 || temp3 == temp4) {
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
                /**The text five lines are used in tests*/
                System.out.println("four chosen temp:");
                System.out.println(temp1);
                System.out.println(temp2);
                System.out.println(temp3);
                System.out.println(temp4);
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

        //ArrayList<CommonGoalCard> c = commonGoalCards;
        if(st1 == false) {
            if (commonGoalCards.get(0).compare(playerInTurn.getMyBookshelf())==true){
                playerInTurn.setScoringToken1();
                ScoringToken firstScoringTokenTile = commonGoalCards.get(0).popScoringToken();
                scoreST += firstScoringTokenTile.getValue();
            }
        }
        if (st2 == false){
            if(commonGoalCards.get(1).compare(playerInTurn.getMyBookshelf())==true){
                playerInTurn.setScoringToken2();
                ScoringToken secondScoringTokenTile = commonGoalCards.get(1).popScoringToken();
                scoreST += secondScoringTokenTile.getValue();
            }
        }
        return scoreST;
    }
}

