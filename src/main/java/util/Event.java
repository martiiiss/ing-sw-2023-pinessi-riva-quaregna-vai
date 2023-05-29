package util;

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



    //per notify
    SET_UP_BOARD(""),
    SET_TILE_BOOKSHELF(""),
    SET_NUM_PLAYERS(""),
    SET_PLAYER_IN_TURN(""),
    SET_COMMONGC(""),
    SET_PGC(""),
    SET_WINNER(""),
    SET_FINISHER(""),
    REMOVE_TILE_BOARD("");

    private String msg;
    Event(String errMsg) {
        this.msg = errMsg;
    }
    public String getMsg(){
        return msg;
    }
}
