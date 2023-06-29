package util;

/**Class that represents an enumeration of events, used in {@link distributed.messages.Message}*/
public enum Event {
    SET_INDEX(""),
    SET_NICKNAME(""),

    ASK_NUM_PLAYERS(""),
    CHOOSE_NETWORK_PROTOCOL(""),
    CHOOSE_VIEW(""),
    START(""),
    START_YOUR_TURN("It's your turn!"),
    ALL_CONNECTED(""),
    END(""),
    GAME_BOARD(""),
    GAME_BOOKSHELF(""),
    GAME_PLAYERS(""),
    GAME_CGC(""),
    GAME_PGC(""),
    GAME_PIT(""),
    GAME_STARTED(""),
    TURN_AMOUNT(""),
    TURN_TILE_IN_HAND(""),
    GET_TILES_HAND(""),
    TURN_COLUMN(""),
    TURN_POSITION(""),
    TURN_BOOKSHELF(""),
    END_OF_TURN(""),
    CHECK_MY_TURN(""),
    CHECK_REFILL(""),
    TURN_PICKED_TILES(""),
    UPDATE_BOOKSHELF(""),
    CHECK_ENDGAME(""),
    UPDATE_SCORINGTOKEN(""),
    UPDATED_GAME_BOARD(""),
    SET_ALL_CONNECTED(""),
    UPDATE_SCORINGTOKEN_1(""),
    UPDATE_SCORINGTOKEN_2(""),
    DISCONNECTED(""),

   //Errors
    NOT_AVAILABLE("Nickname already taken..."),
    OK("Accepted"),
    EMPTY_NICKNAME("35mNickname is empty..."),
    INVALID_VALUE("Invalid value..."),
    OUT_OF_BOUNDS("This number is either too big or negative"),
    BLOCKED_NOTHING("You are trying to pick up a tile that doesn't exist..."),
    NOT_ON_BORDER("This tile cannot be picked up right now..."),
    NOT_ADJACENT("You can only pick up tiles that are adjacent one to the other..."),
    NOT_YOUR_TURN("It's not your turn..."),
    REFILL("The board had to be refilled and is now ready for the next turn..."),
    BOARD_NOT_EMPTY(""),
    REPETITION("Invalid input, you put the same coordinate more than once"),
    WAIT("Waiting for all the players..."),
    GAME_OVER("Game over"),
    LAST_TURN("This is the last turn!"),
    GUI_VIEW(""),
    TUI_VIEW(""),
    SET_CLIENT_INDEX(""),


    //per notify
    SET_UP_BOARD(""),
    SET_FIRST_PLAYER(""),
    SET_PLAYER_BOOKSHELF(""),
    UPDATED_SCORE(""),
    SET_PGC(""),
    SET_SCORING_TOKEN_1(""),
    SET_SCORING_TOKEN_2(""),
    SET_TILES_IN_HAND(""),
    SET_TILE_BOOKSHELF(""),
    SET_NUM_PLAYERS(""),
    SET_PLAYER_IN_TURN(""),
    SET_COMMONGC(""),
    SET_WINNER(""),
    SET_FINISHER(""),
    REMOVE_TILE_BOARD(""),

    //per GUI
    ASK_CAN_PICK(""),
    OK_TO_PICK(""),
    TILES_NOT_VALID(""),
    TILE_PICKED(""),
    COLUMN_NOT_VALID(""),
    START_THREAD(""),
    SET_TILE_ORDER(""),
    COLUMN_CHOSEN(""),
    ORDER_CHOSEN(""),
    ASK_MODEL(""),
    ADD_OBSERVER(""),
    GET_WINNER("");

    private final String msg;

    /**
     * <p>
     *     Constructor of the Class.<br>
     *     This sets an error message based on the given parameter.
     * </p>
     * @param errMsg a <code>String</code> that represents an error message*/
    Event(String errMsg) {
        this.msg = errMsg;
    }

    /**
     * <p>
     * Method that returns an error message.
     * </p>
     *
     * @return <code>String</code> that represents an error message
     */
    public String getMsg(){
        return msg;
    }
    public String getTUIMsg() {return "\u001B[35m"+msg+"\u001B[0m";}
}
