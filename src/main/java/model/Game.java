package model;


import java.io.Serializable;
import java.util.ArrayList;

import distributed.messages.Message;
import util.Event;
import util.Observable;
import java.util.Random;

import static util.Event.ASK_NUM_PLAYERS;


public class Game extends Observable implements Serializable {
    private static final long serialVersionUID = -5788544275616187567L; //random number
    private Player winner;
    private int numberOfPlayers;
    private Player playerInTurn;
    private Player finisher;
    private boolean isLastTurn;
    private ArrayList<Player> players;

    private ArrayList<CommonGoalCard> commonGoalCards;
    private boolean gameStarted;

    private ArrayList<Event> nextEventPlayer;




    public Game() { // added the constructor
        this.commonGoalCards = new ArrayList<>();
        this.players = new ArrayList<>();
        this.numberOfPlayers = 0;
        this.isLastTurn = false;
        this.gameStarted = false;
        this.nextEventPlayer = new ArrayList<>();
    }

    public boolean getGameStarted(){return this.gameStarted;}
    public void setGameStarted(){
        this.gameStarted=true;
        notifyObservers(new Message(this, Event.GAME_STARTED));
    }

    public ArrayList<Player> getPlayers(){return this.players;}

    public void setCommonGoalCards(){ //choose 2 commonGoalCard
        int n1 = (new Random()).nextInt(12)+1; //random number (1-12)
        int n2 = (new Random()).nextInt(12)+1;
        while(n1==n2){
            n2 = (new Random()).nextInt(12)+1;
        }

        CommonGoalCard c1 = new CommonGoalCard(n1, numberOfPlayers, 1);
        CommonGoalCard c2 = new CommonGoalCard(n2, numberOfPlayers, 2);

        commonGoalCards.add(c1); //add c1, c2 to arrayList
        commonGoalCards.add(c2);
        //FIXME: notifyObserver?
        notifyObservers(new Message(this, Event.SET_COMMONGC));
    }

    public ArrayList<CommonGoalCard> getCommonGoalCard(){
        return this.commonGoalCards;
    }

    public void setNumOfPlayers(int nop){ //nop is chosen by the player who creates the game
        this.numberOfPlayers = nop;
        notifyObservers(new Message(this, Event.SET_NUM_PLAYERS));
    }

    public int getNumOfPlayers(){return this.numberOfPlayers;}

    public void setPlayerInTurn(Player pit){
        this.playerInTurn = pit;
        notifyObservers(new Message(this, Event.SET_PLAYER_IN_TURN));
    }

    public Player getPlayerInTurn(){return this.playerInTurn;}

    public void setWinner(Player win){
        this.winner = win;
        notifyObservers(new Message(this, Event.SET_WINNER));
    }

    public Player getWinner(){return this.winner;}


    public void setFinisher(Player finished){
        this.finisher = finished; //set finisher
        isLastTurn = true; // it updates the value of isLastTurn to TRUE
        notifyObservers(new Message(this, Event.SET_FINISHER));
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
                break;
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
                break;
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
                break;
            }
        }

        notifyObservers(new Message(this, Event.SET_PGC));
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



    public ArrayList<Event> getNextEventPlayer(){
        return this.nextEventPlayer;
    }

    public void setNextEventPlayer(Event event, int n, int numOfClientConnected){
        if(this.nextEventPlayer.size()>n) {
            this.nextEventPlayer.set(n, event);
        } else{
            if(this.nextEventPlayer==null || this.nextEventPlayer.size()!=numOfClientConnected){
                for(int i=0; i<numOfClientConnected-this.nextEventPlayer.size(); i++ ){
                    if(this.nextEventPlayer!=null && this.nextEventPlayer.size()>1){
                        this.nextEventPlayer.add(this.nextEventPlayer.size()-1+i, ASK_NUM_PLAYERS);
                    }
                    if(nextEventPlayer==null){
                        this.nextEventPlayer=new ArrayList<>();
                        this.nextEventPlayer.add(0, ASK_NUM_PLAYERS);
                    }
                }
            }
            else {
                this.nextEventPlayer.add(n, event);
            }
        }
    }
}

