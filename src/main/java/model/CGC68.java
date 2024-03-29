package model;

import java.io.Serial;
import java.io.Serializable;
import static model.Type.NOTHING;

/**This class represents the control of the sixth and eight Common Goal Card*/
/*two full columns equal with id 6, full row equal with id 8*/
public class CGC68 implements CGCStrategy, Serializable {
    @Serial
    private static final long serialVersionUID = 6828176289351890009L;
    public boolean compareRule(Bookshelf bks, int id) {
        Tile[][] bookshelf = bks.getBookshelf();
        int i1, i2, j, count, found, maxJ, maxI;
        count=0;
        if(id==6) {
            maxJ = 5;
            maxI = 6;
        }
        else {
            maxJ = 6;
            maxI = 5;
        }
        for (j = 0; j < maxJ; j++) {
            found=1;
            for (i1 = 0; i1 < maxI && found != 0; i1++) {
                for (i2 = i1 + 1; i2 < maxI && found!=0; i2++) {
                    switch (id) {
                        case 6 -> {
                            if (bookshelf[i1][j].getType() == bookshelf[i2][j].getType() || bookshelf[i1][j].getType() == NOTHING || bookshelf[i2][j].getType() == NOTHING)
                                found = 0;
                        }
                        case 8 -> {
                            if (bookshelf[j][i1].getType() == bookshelf[j][i2].getType() || bookshelf[j][i1].getType() == NOTHING || bookshelf[j][i2].getType() == NOTHING)
                                found = 0;
                        }
                    }
                }
            }
            if (found==1)
                count= count+1;
        }
        return count >= 2;
    }
}