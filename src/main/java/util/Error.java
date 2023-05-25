package util;

public enum Error {
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
    WAIT("\u001B[35mWaiting for all the players...\u001B[0m");

    private String msg;
    Error(String errMsg) {
        this.msg = errMsg;
    }
    public String getMsg(){
        return msg;
    }
}
