package controller;

import model.*;
import org.jetbrains.annotations.NotNull;
import util.Cord;
import util.Event;
import view.GUI.GUIView;

import java.util.ArrayList;
import java.util.Collections;
import static util.Event.*;

/**Class that represents the Controller*/
public class Controller  {
    private Game game;
    private Bag bag;
    private Board board;
    private int chosenColumn;
    private final Object lock = new Object();
    private int upScore = -1;
    private int numberOfChosenTiles;
    private ArrayList<Tile> playerHand;
    private ArrayList<Cord> playerCords = new ArrayList<>();
    private boolean hasGameStarted;
    private boolean gameOver = false;
    /**
     * <p>
     *     Constructor of the Class.<br>
     *     This creates the Game by calling the method {@code createGame()}.
     * </p>*/
    public Controller(){
        createGame();
    }
    /**
     * <p>
     *     Method that instantiates the Game.
     * </p>*/
    public void createGame() {
        this.game = new Game();
    }

    /**Method that returns the instance of {@code Bag}.
     * @return an instance of {@code Bag}*/
    public Bag getBag() {return bag;}
    /**
     * <p>
     *     Method that returns the current instance of Game.
     * </p>
     * @return the instance of {@code Game} relative of a specific match*/
    public Game getInstanceOfGame(){
        return this.game;
    }

    /**Method that returns the instance of {@code Board}.
     * @return an instance of {@code Board}*/
    public Board getBoard(){ return this.board;}
    /**
     *     Method that given a {@code String} as a parameter
     *     creates a new {@code Player} and sets its nickname.<br>
     *     This also adds the player inside a list of players
     *     that represents the players in a match.<br>
     *     If the client chose its nickname correctly this method will
     *     return an <b>{@code Event.OK}</b>; otherwise, if a client inserted an
     *     empty string this will return an <b>{@code Event.EMPTY_NICKNAME}</b>, if a client
     *     inserted a nickname that has already been chosen
     *     this will return an <b>{@code Event.NOT_AVAILABLE}</b>.
     * @param nickname a {@code String} that represents the nickname given in input by the player
     * @return an {@code Event}, the type of event depends on the input*/
    public Event chooseNickname(@NotNull String nickname) {
        //control on emptiness
        if(nickname.isEmpty()) {return Event.EMPTY_NICKNAME;}
        //control on nickname duplicates
        for(int i = 0; i<game.getPlayers().size();i++) {
            if(game.getPlayers().get(i).getNickname().equals(nickname)){
                return Event.NOT_AVAILABLE;}}
        Player newPlayer = new Player();
        newPlayer.setNickname(nickname);
        game.addPlayer(newPlayer);
        return Event.OK;
    }
    /**
     *
     *     Method used to choose the type of user interface.<br>
     *     Based on the int in input the client can choose the type of interface that it wants:
     *     <b>1</b> for a Textual User Interface, <b>2</b> for a Graphical User Interface. <br>
     *     This method returns an <b>{@code Event.TUI_VIEW}</b> if the client chose a Textual User Interface,
     *     an <b>{@code Event.GUI_VIEW}</b>, otherwise if the input si not valid this method returns an <b>{@code Event.INVALID_VALUE}</b>.
     *
     * @param chosenInterface an int in between 1 and 2
     * @return an {@code Event}, based on the input*/
    public Event chooseUserInterface(int chosenInterface) {
        switch (chosenInterface) {
            case 1 -> {return Event.TUI_VIEW;}
            case 2 -> {return Event.GUI_VIEW;}
            default -> {return Event.INVALID_VALUE;}
        }
    }
    /**
     *
     *     Method that initializes the Game for a match. <br>
     *     This method instantiates the {@code Bag} and the{@code Board},
     *     it also assigns an empty {@code Bookshelf}, a {@code PersonalGoalCard},to every player in the match. <br>
     *     Also, it sets the {@code CommonGoalCards}, the number of cells based on the number of players in the match,
     *     it fills the {@code Board}.<br>
     *     Finally, it sets a player as the first player, sets the firs player as player in turn and starts the game.
     *
     * <p>
     *     <b>NOTE:</b> This method gets called after all the clients needed to start the game are connected.
     * </p>*/
    public void initializeGame() {
        game.setGameStarted();
        boolean goodSetUp;
        this.bag = new Bag();
        this.board = new Board(game.getNumOfPlayers());
        for(int i=0;i<game.getNumOfPlayers();i++){game.getPlayers().get(i).setMyBookshelf(new Bookshelf());}
        game.assignPersonalGoalCard(game.getNumOfPlayers());
        game.setCommonGoalCards();
        ArrayList<Tile> tiles = bag.getBagTiles(board.getNumOfCells());
        do {
            goodSetUp = board.setUpBoard(tiles);
        }while (!goodSetUp);
        game.getPlayers().get(0).setAsFirstPlayer();
        game.setPlayerInTurn(game.getPlayers().get(0));
    }

    /**
     *     Method that, if the {@code Board} needs to be filled,
     *     gets the needed tiles from the {@code Bag} after eventually emptying the {@code Board}. <br>
     *     If the {@code Bag} contains enough tiles the {@code Board} gets entirely filled,
     *     otherwise the {@code Bag} gets emptied and the {@code Board} gets partially filled.
     */
    public void checkBoardToBeFilled(){
        if(board.checkBoardStatus()){
            for(int r=0;r<9;r++){
                for(int c=0;c<9;c++){
                    if(board.getBoard()[r][c].getType()!=Type.NOTHING && board.getBoard()[r][c].getType()!=Type.BLOCKED){
                        bag.addTile(board.removeTile(r,c));}
                }
            }
            if(bag.getTilesContained().size()>= board.getNumOfCells()){
                board.setUpBoard(bag.getBagTiles(board.getNumOfCells()));
            } else {
                board.setUpBoard(bag.getBagTiles(bag.getTilesContained().size()));
            }
        }
    }

    /**
     *     Method that gets the number of tiles that a player wants to pick.<br>
     *     This method, given an int as a parameter, controls if there are enough
     *     tiles on the board and returns an event accordingly. <br>
     *     If everything worked, this method sets the input parameter to the attribute {@code numberOfChosenTiles}. <br>
     *     This method can return the following type of events: <br>
     *     - <b>{@code Event.OUT_OF_BOUNDS}</b> if <code>numberOfChosenTiles<1 || numberOfChosenTiles>3</code> <br>
     *     - <b>{@code Event.INVALID_VALUE}</b> if there are not enough free slots in the player's bookshelf
     *     or if the board doesn't contain enough tiles <br>
     *     - <b>{@code Event.OK}</b> if everything worked.
     * @param numberOfChosenTiles an int in between 1 and 3
     * @return an {@code Event} accordingly to the input*/
    private Event numOfChosenTiles(int numberOfChosenTiles) {
        int freeSlots = game.getPlayerInTurn().getMyBookshelf().getNumOfFreeSlots();
        if (numberOfChosenTiles<1 || numberOfChosenTiles>3)
            return Event.OUT_OF_BOUNDS;
        if(freeSlots < numberOfChosenTiles )
            return Event.INVALID_VALUE;

        int temp, cont;
        if(numberOfChosenTiles!=1) {
            for (int i = 0; i < board.BOARD_ROW; i++) {
                for (int j = 0; j < board.BOARD_COLUMN; j++) {
                    Cord cord = new Cord();
                    cord.setCords(i, j);
                    cont = 0;
                    if(isTileFreeTile(cord)) {
                        while (cont < 2 && board.getBoard()[i][j].getType() != Type.BLOCKED && board.getBoard()[i][j].getType() != Type.NOTHING) {
                            int e;
                            temp = 0;
                            for (e = 1; e <= numberOfChosenTiles; e++) {
                                if(i - e * (cont - 1)< board.BOARD_ROW && j + e * (cont)< board.BOARD_COLUMN) {
                                    cord.setCords(i - e * (cont - 1), j + e * (cont));
                                    if (board.getBoard()[i - e * (cont - 1)][j + e * (cont)].getType() != Type.BLOCKED && board.getBoard()[i - e * (cont - 1)][j + e * (cont)].getType() != Type.NOTHING && isTileFreeTile(cord)) {
                                        temp = e + 1;
                                    } else {
                                        e = numberOfChosenTiles + 1;
                                    }
                                }
                            }
                            if (temp == numberOfChosenTiles) {
                                this.numberOfChosenTiles = numberOfChosenTiles;
                                return Event.OK;
                            }
                            cont++;
                        }
                    }
                }
            }
            return Event.INVALID_VALUE;
        } else {
            this.numberOfChosenTiles = numberOfChosenTiles;
        }
        return Event.OK;
    }


    /**
     *     Method that given a list of coordinates picks up tiles from the board (only if the player chose correct tiles).<br>
     *     This method returns a specific {@code Event} based on the input:<br>
     *     - if the player tries to pick up a tile that is not on the board it returns an {@code Event.OUT_OF_BOUNDS}; <br>
     *     - if the player tries to pick up the same tile it returns an {@code Event.REPETITION}; <br>
     *     - if the player tries to pick up a NOTHING or BLOCKED tile it returns an {@code Event.BLOCKED_NOTHING}; <br>
     *     - if the player tries to pick up tiles that are not on the border it returns an {@code Event.NOT_ON_BORDER}; <br>
     *     - if the player tries to pick up tiles that are not adjacent it returns an {@code Event.NOT_ADJACENT}; <br>
     *     - if the player picks up correct tiles it returns an {@code Event.OK}.
     * @param cords is an {@code ArrayList} of {@link Cord} that represents a list of coordinates of the chosen tiles
     * @return an {@code Event} based on the input*/

    public Event chooseTiles(@NotNull ArrayList<Cord> cords) {
        for(Cord cord : cords) {
            if (cord.getRowCord() > 8 || cord.getRowCord() < 0 || cord.getColCord() > 8 || cord.getColCord() < 0)
                return Event.OUT_OF_BOUNDS;
        }
        for(int i=0; i<cords.size();i++) {
            for (int j = i + 1; j < cords.size(); j++) {
                if (cords.get(i).getRowCord() == cords.get(j).getRowCord() && cords.get(i).getColCord() == cords.get(j).getColCord())
                    return Event.REPETITION;
            }
        }
        for (Cord cord : cords){
            if (board.getSelectedType(cord.getRowCord(), cord.getColCord()) == Type.NOTHING || board.getSelectedType(cord.getRowCord(), cord.getColCord()) == Type.BLOCKED) {
                playerCords.clear();
                return Event.BLOCKED_NOTHING;
            }
            if (!isTileFreeTile(cord)) {
                playerCords.clear();
                return Event.NOT_ON_BORDER;
            }
            if(cords.size()!=1)
                if(!checkAdj(cords))
                    return Event.NOT_ADJACENT;
            playerCords.add(cord);
        }
        return Event.OK;
    }

    /**
     *     Method that checks that all the chosen tiles are adjacent.<br>
     *     This method returns <b>true</b> if all the chosen tiles
     *     are on the same row or column and are also adjacent to one another, <b>false</b> otherwise.
     * @param cords is an {@code ArrayList} of {@link Cord} that represents a list of coordinates of the chosen tiles
     * @return a boolean, based on the conditions listed above*/
    private boolean checkAdj(@NotNull ArrayList<Cord> cords) {
        boolean sameRow = true;
        boolean sameCol = true;
        Cord currCord = cords.get(1);
        Cord prevCord = cords.get(0);
        if (currCord.getRowCord() != prevCord.getRowCord())
            sameRow = false;
        if(currCord.getColCord() != prevCord.getColCord())
            sameCol = false;
        ArrayList<Integer> col = new ArrayList<>();
        ArrayList<Integer> row = new ArrayList<>();
        if(cords.size()==3) {
            Cord lastCord = cords.get(2);
            if (currCord.getRowCord() != lastCord.getRowCord() || prevCord.getRowCord() != lastCord.getRowCord())
                sameRow = false;
            if(currCord.getColCord() != lastCord.getColCord() || prevCord.getColCord() != lastCord.getColCord())
                sameCol = false;
        }
        if (!sameCol && !sameRow)
            return false;
        if(sameRow) {
            for (Cord cord : cords)
                col.add(cord.getColCord());
            Collections.sort(col);
            for(int i = 0; i<col.size()-1;i++) {
                if (col.get(i + 1) != col.get(i) + 1)
                    return false;
            }
        }
        if(sameCol) {
            for (Cord cord : cords)
                row.add(cord.getRowCord());
            Collections.sort(row);
            for(int i = 0; i<row.size()-1;i++)
                if(row.get(i + 1) != row.get(i) +1)
                    return false;
        }
        return true;
    }


    /**
     *     Method that checks if a tile is available.<br>
     *     This method returns <b>true</b> if a tile has two or more free sides, <b>false</b> otherwise.
     * @param cord is a {@link Cord} that represents the two coordinates of a specific tile
     * @return a boolean, based on the conditions listed above */
    private boolean isTileFreeTile(@NotNull Cord cord) {
        int x = cord.getRowCord();
        int y = cord.getColCord();
        if(x==0 || x== board.BOARD_ROW-1 || y==0 || y== board.BOARD_COLUMN-1){
            if(x==0 && y== 0 &&
                    (board.getSelectedType(x + 1, y) == Type.NOTHING
                            || board.getSelectedType(x + 1, y) == Type.BLOCKED
                            || board.getSelectedType(x, y+1)==Type.BLOCKED
                            || board.getSelectedType(x, y+1)==Type.NOTHING)){
                return true;
            } else if(x==0 && y==board.BOARD_COLUMN-1 &&
                    (board.getSelectedType(x +1, y) == Type.NOTHING
                            || board.getSelectedType(x + 1, y) == Type.BLOCKED
                            || board.getSelectedType(x, y-1)==Type.BLOCKED
                            || board.getSelectedType(x, y-1)==Type.NOTHING)) {
                return true;
            } else if(x==board.BOARD_ROW-1 && y==0 &&
                    (board.getSelectedType(x -1, y) == Type.NOTHING
                            || board.getSelectedType(x - 1, y) == Type.BLOCKED
                            || board.getSelectedType(x, y+1)==Type.BLOCKED
                            || board.getSelectedType(x, y+1)==Type.NOTHING)) {
                return true;
            } else if(x==board.BOARD_ROW-1 && y==board.BOARD_COLUMN-1 &&
                    (board.getSelectedType(x - 1, y) == Type.NOTHING
                            || board.getSelectedType(x - 1, y) == Type.BLOCKED
                            || board.getSelectedType(x, y-1)==Type.BLOCKED
                            || board.getSelectedType(x, y-1)==Type.NOTHING)) {
                return true;
            } else if(x==0 &&
                    (board.getSelectedType(x + 1, y) == Type.NOTHING
                            || board.getSelectedType(x, y + 1) == Type.NOTHING
                            || board.getSelectedType(x, y - 1) == Type.NOTHING
                            || board.getSelectedType(x + 1, y) == Type.BLOCKED
                            || board.getSelectedType(x, y + 1) == Type.BLOCKED
                            || board.getSelectedType(x, y - 1) == Type.BLOCKED)){
                return true;
            } else if(x==board.BOARD_ROW-1 &&
                    (board.getSelectedType(x - 1, y) == Type.NOTHING
                            || board.getSelectedType(x, y + 1) == Type.NOTHING
                            || board.getSelectedType(x, y - 1) == Type.NOTHING
                            || board.getSelectedType(x - 1, y) == Type.BLOCKED
                            || board.getSelectedType(x, y + 1) == Type.BLOCKED
                            || board.getSelectedType(x, y - 1) == Type.BLOCKED)){
                return true;
            } else if(y==0 &&
                    (board.getSelectedType(x - 1, y) == Type.NOTHING
                            || board.getSelectedType(x, y + 1) == Type.NOTHING
                            || board.getSelectedType(x + 1, y) == Type.NOTHING
                            || board.getSelectedType(x - 1, y) == Type.BLOCKED
                            || board.getSelectedType(x, y + 1) == Type.BLOCKED
                            || board.getSelectedType(x + 1, y ) == Type.BLOCKED)){
                return true;
            }  else return y == board.BOARD_COLUMN - 1 &&
                    (board.getSelectedType(x - 1, y) == Type.NOTHING
                            || board.getSelectedType(x, y - 1) == Type.NOTHING
                            || board.getSelectedType(x + 1, y) == Type.NOTHING
                            || board.getSelectedType(x - 1, y) == Type.BLOCKED
                            || board.getSelectedType(x, y - 1) == Type.BLOCKED
                            || board.getSelectedType(x + 1, y) == Type.BLOCKED);
        } else {
            return board.getSelectedType(x + 1, y) == Type.NOTHING
                    || board.getSelectedType(x, y + 1) == Type.NOTHING
                    || board.getSelectedType(x - 1, y) == Type.NOTHING
                    || board.getSelectedType(x, y - 1) == Type.NOTHING
                    || board.getSelectedType(x + 1, y) == Type.BLOCKED
                    || board.getSelectedType(x, y + 1) == Type.BLOCKED
                    || board.getSelectedType(x - 1, y) == Type.BLOCKED
                    || board.getSelectedType(x, y - 1) == Type.BLOCKED;
        }
    }


    /**
     *     Method that given an int permits the player to choose the column in which it wants to put its tiles.<br>
     *     This method returns a specific {@code Event} based on the input:<br>
     *     - if {@code chosenColumn<0 || chosenColumn >4} it returns an {@code Event.INVALID_VALUE};<br>
     *     - if the player tries to put its tiles in a column that doesn't have enough slots it returns an {@code Event.OUT_OF_BOUNDS};<br>
     *     - if the player made a correct choice it returns an {@code Event.OK}.
     *
     * @param chosenColumn an int in between 0 and 4 that represents the number of a column
     * @return an {@code Event} based on the input*/
    public Event chooseColumn(int chosenColumn) {
        Tile[][] playerBookshelf = game.getPlayerInTurn().getMyBookshelf().getBookshelf();
        if(chosenColumn<0 || chosenColumn >4)
            return Event.INVALID_VALUE;
        if(playerBookshelf[this.numberOfChosenTiles-1][chosenColumn].getType() != Type.NOTHING){
            return Event.OUT_OF_BOUNDS;
        }
        this.chosenColumn = chosenColumn;
        return Event.OK;
    }


    /**
     * <p>
     *     Method that given an int lets the player chose
     *     the disposition of the tiles as it puts them into the bookshelf.<br>
     *     This method returns a specific {@code Event} based on the input:<br>
     *     - if the int is less that zero or is more that the size of the tiles in hand of the player
     *     it returns an {@code Event.INVALID_VALUE};<br>
     *     - if the player chooses a correct int it returns an {@code Event.OK}
     * </p>
     * @param index an int that represents the index of one of the tiles from the tiles in hand
     * @return an {@code Event} based on the input */

    public Event chooseTilesDisposition(int index) {
        if(index <0 || index >= playerHand.size())
            return Event.INVALID_VALUE;
        Player pit = game.getPlayerInTurn();
        pit.getMyBookshelf().placeTile(this.chosenColumn,playerHand.get(index));
        playerHand.add(index+1,new Tile(Type.NOTHING,0));
        playerHand.remove(index);
        return Event.OK;
    }


    /**Method that calculates the total score of a player.
     **/
    public void calculateScore(){
        int cgc = game.checkCommonGoalCard();
        int pgc = game.getPlayerInTurn().checkCompletePGC();
        int adjacency = game.getPlayerInTurn().checkAdjacentBookshelf();
        game.getPlayerInTurn().updateScore(cgc+pgc+adjacency);
    }


    /**
     * <p>
     *     Method that checks the status of match.<br>
     *     This method returns a specific {@code Event} based on the match status:<br>
     *     - if the game is ended it returns an {@code Event.GAME_OVER};<br>
     *     - if it's the last turn it returns an {@code Event.LAST_TURN};<br>
     *     - if the game isn't in any of the two previous conditions it returns an {@code Event.OK}.
     * </p>
     * @return an {@code Event} based on the match status */
    public Event checkIfGameEnd() {
        int index=0;
        if (game.getPlayers().indexOf(game.getPlayerInTurn()) != game.getNumOfPlayers() - 1) {
            index = game.getPlayers().indexOf(game.getPlayerInTurn()) + 1;
        }
        if(!game.getIsLastTurn()){
            if(game.getPlayerInTurn().getMyBookshelf().getStatus()) {
                game.getPlayerInTurn().updateScore(1);
                game.setFinisher(game.getPlayerInTurn());
                if(index==0) //If the PIT is also the first one to finish the game is over instantly
                {
                    gameOver = true;
                    return Event.GAME_OVER;
                }
                else {
                    goToNext(game.getPlayerInTurn());
                    return Event.LAST_TURN;
                }
            }
            else {
                goToNext(game.getPlayerInTurn());
                return Event.OK;
            }
        }
        else {
            if(index==0) {
                gameOver = true;
                return GAME_OVER;
            }
            else {
                goToNext(game.getPlayerInTurn());
                return LAST_TURN;
            }
        }
    }


    /**
     * Method that given a {@code Player} as input sets it as the player in turn.<br>
     * @param playerInTurn a player that has to be set as the player in turn*/
    public void goToNext(Player playerInTurn){
        int i = game.getPlayers().indexOf(playerInTurn)+1;
        if(i<game.getNumOfPlayers()){
            game.setPlayerInTurn(game.getPlayers().get(i));
        } else {
            game.setPlayerInTurn(game.getPlayers().get(0));
        }
    }

    /**
     * Method that removes the tiles from the board.<br>
     * @return an {@code ArrayList} of {@code Tile}
     * that represents the list of tiles removed from the board*/
    public ArrayList<Tile> getTilesFromBoard() {
        ArrayList<Tile> removedTiles = new ArrayList<>();
        for(Cord cord : playerCords) {
            removedTiles.add(board.removeTile(cord.getRowCord(),cord.getColCord()));
        }
        this.playerHand = removedTiles;
        playerCords.clear();
        return removedTiles;
    }

    /**
     * Method that updates the controller based on the two received parameters.<br>
     * @param event is a specific {@code Event} needed to do a specific update
     * @param obj is the object that has to be set as an update
     * @return a specific type of error, for brevity the list of possible errors won't be described*/
    //REM
    public Event updateController(Object obj, @NotNull Event event) {
        Event error = Event.OK;
        switch (event) {
            case ASK_NUM_PLAYERS -> {
                System.out.println("Num of players set: "+(int)obj);
                game.setNumOfPlayers((int)obj);
            }
            case SET_NICKNAME -> {
                System.out.println("Nickname Received: "+ obj);
                 error = chooseNickname((String) obj);
            }
            case CHOOSE_VIEW -> error = chooseUserInterface((int) obj);
            case ALL_CONNECTED -> {
                synchronized (lock) {
                    if (!hasGameStarted && game.getNumOfPlayers() == game.getPlayers().size()) {
                        hasGameStarted = true;
                        System.out.println("\u001B[32mThe game is starting\u001B[0m");
                        initializeGame();
                        return error;
                    } else if (hasGameStarted && game.getNumOfPlayers() == game.getPlayers().size()) {
                        return error;
                    } else
                        error = Event.WAIT;
                }
            }
            case GAME_STARTED -> {
                if(game.getGameStarted())
                    error = Event.OK;
                else
                    error = Event.WAIT;
            }
            case TURN_AMOUNT -> error = numOfChosenTiles((int) obj);

            case TURN_PICKED_TILES -> error = chooseTiles((ArrayList<Cord>) obj);

            case TURN_COLUMN -> error = chooseColumn((int) obj);

            case TURN_POSITION -> error = chooseTilesDisposition((int) obj);

            case END_OF_TURN -> {
                calculateScore();
                upScore= game.getNumOfPlayers();
                error = checkIfGameEnd();
                return error;
            }
            case CHECK_MY_TURN -> {
                if((int) obj == game.getPlayers().indexOf(game.getPlayerInTurn()))
                    return Event.OK;
                else
                    return Event.NOT_YOUR_TURN;
            }
            case SET_UP_BOARD ->{
                if(board.hasChanged()){
                    board.clearChanged();
                    return SET_UP_BOARD;
                }
            }
            case CHECK_REFILL -> {
                if(board.checkBoardStatus()) {
                    checkBoardToBeFilled();
                    return Event.REFILL;
                }
                else
                    return Event.BOARD_NOT_EMPTY;
            }
            case CHECK_ENDGAME -> {
                    return checkIfGameEnd();
            }
            case ADD_OBSERVER -> {
                addGui((GUIView) obj);
                return Event.OK;
            }
            case UPDATE_SCORINGTOKEN -> {
                if (game.getCommonGoalCard().get(0).hasChanged()) {
                    game.getCommonGoalCard().get(0).clearChanged();
                    return UPDATE_SCORINGTOKEN_1;
                }
                if(game.getCommonGoalCard().get(1).hasChanged()) {
                    game.getCommonGoalCard().get(1).clearChanged();
                    return UPDATE_SCORINGTOKEN_2;
                }
            }
            case UPDATED_SCORE -> {
                if(upScore>=0){
                    upScore--;
                    return UPDATED_SCORE;
                }
            }
            case END -> {
                if (gameOver)
                    return END;
            }
        }
        return error;
    }

    /**
     * <p>
     *     Method that given two parameters returns a specific object.<br>
     *     This method is used to make the player make choices during the match.
     * </p>
     * @param event is a specific {@code Event} needed to get a specific object
     * @param playerIndex is an object used to perform specific actions
     * @return an {@code Object} used to let the player chose some actions during the match
     */
    public Object getControllerModel(@NotNull Event event, Object playerIndex) {
        Object obj = null;
        switch (event) {
            case SET_INDEX -> obj = getPlayerFromNick((String) playerIndex);
            case GAME_BOARD -> obj = getBoard();
            case GAME_PLAYERS -> obj = game.getPlayers();
            case GAME_CGC -> obj = game.getCommonGoalCard();
            case GAME_PGC -> obj = game.getPlayers().get((int)playerIndex).getPersonalGoalCard();
            case GAME_PIT -> obj = game.getPlayers().indexOf(game.getPlayerInTurn());
            case TURN_TILE_IN_HAND -> obj = getTilesFromBoard();
            case TURN_POSITION, GET_TILES_HAND -> obj = this.playerHand;
            case UPDATE_BOOKSHELF-> obj =  game.getPlayerInTurn().getMyBookshelf();
            case GET_WINNER -> obj = game.getWinner();
        }
        return obj;
    }


    /**
     * <p>
     *     Method that given a {@code String}
     *     that represents the nickname of a player returns it's position in the list of a match.
     * </p>
     * @return the index of a player, if the player isn't in a match this returns -1*/
    private int getPlayerFromNick(String nickname) {
        for(Player p : game.getPlayers()) {
            if (p.getNickname().equals(nickname)) {
                return game.getPlayers().indexOf(p);
            }
        }
        return -1;
    }

    /**
     * Method used to get the index of the player in turn.
     * @return an in that represents the index of the player in turn
     */
    public int getPITIndex() {
        return game.getPlayers().indexOf(game.getPlayerInTurn());
    }

    /**
     * Method used to add a {@code GUIView}.
     * @param gui is a {@link GUIView} */
    public void addGui(GUIView gui){
        board.addObserver(gui, game.getNumOfPlayers());
        game.getCommonGoalCard().get(0).addObserver(gui, game.getNumOfPlayers());
        game.getCommonGoalCard().get(1).addObserver(gui, game.getNumOfPlayers());
    }

    /**
     * Method used to set the number of chosen tiles.
     * @param numberOfChosenTiles is an int that represents the number of chosen tiles*/
    public void setNumberOfChosenTiles(int numberOfChosenTiles) {
        this.numberOfChosenTiles = numberOfChosenTiles;
    }

    /**
     * Method used to set the tiles in the hand of a player.
     * @param playerHand is an {@code ArrayList} of {@code Tile}*/
    public void setPlayerHand(ArrayList<Tile> playerHand) {
        this.playerHand = playerHand;
    }

    /**
     * Method used to set the coordinates chosen by a player.
     * @param playerCords is an {@code ArrayList} of {@code Cord}*/
    public void setPlayerCords(ArrayList<Cord> playerCords) {
        this.playerCords = playerCords;
    }

}

