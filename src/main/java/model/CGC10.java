package model;

import java.io.Serial;
import java.io.Serializable;

import static model.Type.BLOCKED;
import static model.Type.NOTHING;

/**This class represents the control of the tenth Common Goal Card*/
public class CGC10 implements CGCStrategy, Serializable {
    @Serial
    private static final long serialVersionUID = 2228176289351890649L;
    public boolean compareRule(Bookshelf bks, int id) {
        Tile[][] bookshelf = bks.getBookshelf();
        int i, j;
        boolean result = false;
        for (i = 0; i < 4; i++)
            for (j = 0; j < 3; j++)
                if (bookshelf[i][j] == bookshelf[i + 2][j] && bookshelf[i + 2][j + 2] == bookshelf[i][j + 2] && bookshelf[i][j] == bookshelf[i + 1][j + 1] && bookshelf[i][j] == bookshelf[i][j + 2] && !(bookshelf[i][j].getType() == BLOCKED || bookshelf[i][j].getType() == NOTHING))
                    result = true;
        return result;
    }
}
