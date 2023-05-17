package model;

import java.io.Serializable;

import static model.Type.BLOCKED;
import static model.Type.NOTHING;


    /*4 angles equal*/
    public class CGC3 implements CGCStrategy, Serializable {
        private static final long serialVersionUID = 6808001289351890649L;
        public boolean compareRule(Bookshelf bks, int id) {
            Tile[][] bookshelf = bks.getBookshelf();
            return bookshelf[5][0] == bookshelf[5][4] && bookshelf[0][0] == bookshelf[0][4] && bookshelf[0][0] == bookshelf[5][0] && !(bookshelf[0][0].getType() == BLOCKED || bookshelf[0][0].getType() == NOTHING);
        }
    }

