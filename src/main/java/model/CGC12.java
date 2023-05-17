package model;

import java.io.Serializable;

import static model.Type.NOTHING;

// The diagonal, both sides
public class CGC12 implements CGCStrategy, Serializable {
    private static final long serialVersionUID = 6002176189551890649L;
    public boolean compareRule(Bookshelf bks, int id) {
        Tile[][] bookshelf = bks.getBookshelf();
        boolean flag = false;

        //check first whole diagonal
        if ((bookshelf[0][0].getType() == NOTHING && bookshelf[1][0].getType() != NOTHING) &&
                (bookshelf[1][1].getType() == NOTHING && bookshelf[2][1].getType() != NOTHING) &&
                (bookshelf[2][2].getType() == NOTHING && bookshelf[3][2].getType() != NOTHING) &&
                (bookshelf[3][3].getType() == NOTHING && bookshelf[4][3].getType() != NOTHING) &&
                (bookshelf[4][4].getType() == NOTHING && bookshelf[5][4].getType() != NOTHING)
            ) {flag = true;}
        //check first whole and partial diagonal
        if ((bookshelf[0][0].getType() != NOTHING && bookshelf[0][1].getType() == NOTHING) &&
                (bookshelf[1][1].getType() != NOTHING && bookshelf[1][2].getType() == NOTHING) &&
                (bookshelf[2][2].getType() != NOTHING && bookshelf[2][3].getType() == NOTHING) &&
                (bookshelf[3][3].getType() != NOTHING && bookshelf[3][4].getType() == NOTHING) &&
                (bookshelf[4][4].getType() != NOTHING)
            ) {flag = true;}
        //check second whole diagonal
        if ((bookshelf[4][0].getType() == NOTHING && bookshelf[5][0].getType() != NOTHING) &&
           (bookshelf[3][1].getType() == NOTHING && bookshelf[4][1].getType() != NOTHING) &&
           (bookshelf[2][2].getType() == NOTHING && bookshelf[3][2].getType() != NOTHING) &&
           (bookshelf[1][3].getType() == NOTHING && bookshelf[2][3].getType() != NOTHING) &&
           (bookshelf[0][4].getType() == NOTHING && bookshelf[1][4].getType() != NOTHING)
            ) {flag = true;}
        //check second whole and partial diagonal
        if ((bookshelf[4][0].getType() != NOTHING && bookshelf[3][0].getType() == NOTHING) &&
            (bookshelf[3][1].getType() != NOTHING && bookshelf[2][1].getType() == NOTHING) &&
            (bookshelf[2][2].getType() != NOTHING && bookshelf[1][2].getType() == NOTHING) &&
            (bookshelf[1][3].getType() != NOTHING && bookshelf[0][3].getType() == NOTHING) &&
            (bookshelf[0][4].getType() != NOTHING)
        ) {flag = true;}
        return flag;
    }
}