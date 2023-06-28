package model;

import java.io.Serial;
import java.io.Serializable;
import static model.Type.BLOCKED;
import static model.Type.NOTHING;

/**This class represents the control of the third Common Goal Card*/
    public class CGC3 implements CGCStrategy, Serializable {
        @Serial
        private static final long serialVersionUID = 6808001289351890649L;
        public boolean compareRule(Bookshelf bks, int id) {
            Tile[][] bookshelf = bks.getBookshelf();
            return bookshelf[5][0].getType() == bookshelf[5][4].getType() && bookshelf[0][0].getType() == bookshelf[0][4].getType() && bookshelf[0][0].getType() == bookshelf[5][0].getType() && !(bookshelf[0][0].getType() == BLOCKED || bookshelf[0][0].getType() == NOTHING);
        }
    }

