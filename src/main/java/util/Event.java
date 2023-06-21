package util;

/**Class that represents an enumeration of events, used in {@link distributed.messages.Message}*/
public enum Event {
    SET_INDEX(""),
    SET_NICKNAME(""),

    ASK_NUM_PLAYERS(""),
    CHOOSE_NETWORK_PROTOCOL(""),
    CHOOSE_VIEW(""),
    START(""),
    START_YOUR_TURN(""),
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
    TURN_COLUMN(""),
    TURN_POSITION(""),
    TURN_BOOKSHELF(""),
    END_OF_TURN(""),
    CHECK_MY_TURN(""),
    CHECK_REFILL(""),
    TURN_PICKED_TILES(""),
    UPDATE_BOOKSHELF(""),

   //Errors
    NOT_AVAILABLE("\u001B[35mNickname already taken...\u001B[0m"),
    OK("\u001B[35mAccepted\u001B[0m"),
    EMPTY_NICKNAME("\u001B[35mNickname is empty...\u001B[0m"),
    INVALID_VALUE("\u001B[35mInvalid value...\u001B[0m"),
    OUT_OF_BOUNDS("\u001B[35mThis number is either too big or negative\u001B[0m"),
    BLOCKED_NOTHING("\u001B[35mYou are trying to pick up a tile that doesn't exist...\u001B[0m"),
    NOT_ON_BORDER("\u001B[35mThis tile cannot be picked up right now...\u001B[0m"),
    NOT_ADJACENT("\u001B[35mYou can only pick up tiles that are adjacent one to the other...\u001B[0m"),
    NOT_YOUR_TURN(""),
    REFILL("\u001B[35mThe board had to be refilled and is now ready for the next turn...\u001B[0m"),
    BOARD_NOT_EMPTY(""),
    REPETITION("\u001B[35mInvalid input, you put the same coordinate more than once\u001B[0m"),
    WAIT("\u001B[35mWaiting for all the players...\u001B[0m"),
    GAME_OVER("\u001B[35mGame over\u001B[0m"),
    LAST_TURN("\u001B[35mThis is the last turn!\u001B[0m"),
    GUI_VIEW(""),
    TUI_VIEW(""),
    DISCONNECTION("\u001B[35mGame over due to a Disconnection\u001B[0m"),

    EMPTY(""),


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
    SET_TILE_ORDER(""),
    COLUMN_CHOSEN(""),
    ORDER_CHOSEN("");

    private String msg;

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
     *     Method that returns an error message.
     * </p>
     * @return <code>String</code> that represents an error message*/
    public String getMsg(){
        return msg;
    }
}
