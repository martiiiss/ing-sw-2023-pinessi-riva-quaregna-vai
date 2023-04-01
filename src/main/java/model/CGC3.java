package model;

import static model.Type.BLOCKED;
import static model.Type.NOTHING;


    /*4 angles equal*/
    public class CGC3 implements CGMStrategy {
        public boolean compareRule(Bookshelf bks, int id) {
            Tile[][] bookshelf = bks.getBookshelf();
            return bookshelf[5][0] == bookshelf[5][4] && bookshelf[0][0] == bookshelf[0][4] && bookshelf[0][0] == bookshelf[5][0] && !(bookshelf[0][0].getType() == BLOCKED || bookshelf[0][0].getType() == NOTHING);
        }
    }

