package model;

/**Class that represents the 6 Type of tiles: <i>CAT, BOOK, GAME, FRAME, TROPHY</i> and <i>PLANT</i>. <br>
 *Apart from the regular types of tile there are also two extra type of tiles <i>NOTHING</i>
 *and <i>BLOCKED</i> that are used respectively to: <br>
 *<b>NOTHING:</b> used to indicate that there are no tiles in this position; <br>
 *<b>BLOCKED:</b> used to indicate that this position of the board cannot be filled. */
public enum Type {
    CAT,
    BOOK,
    GAME,
    FRAME,
    TROPHY,
    PLANT,
    NOTHING,
    BLOCKED
}
