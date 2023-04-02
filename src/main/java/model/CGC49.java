package model;

import java.util.ArrayList;

import static model.Type.BLOCKED;
import static model.Type.NOTHING;

/*with 4, 4 complete rows of 1,2 or 3 different types, with 9, 3 complete columns of 1,2 or 3 different types*/
public class CGC49 implements CGMStrategy {
    public boolean compareRule(Bookshelf bks, int id){
        Tile[][] bookshelf = bks.getBookshelf();
        int i, j, found, maxJ, maxI, num, maxType = 3, count = 0;
        if(id==9) {
            maxJ = 5;
            maxI = 6;
            num = 3;

        }
        else {
            maxJ = 6;
            maxI = 5;
            num = 4;
        }
        for (j = 0; j < maxJ; j++) {
            found=1;
            ArrayList<Type> countType = new ArrayList<Type>();
            for (i = 0; i < maxI && found != 0; i++) {
                switch(id) {
                    case 9:
                        if (bookshelf[i][j].getType() == NOTHING || bookshelf[i][j].getType() == BLOCKED)
                            found = 0;
                        break;
                    case 4:
                        if (bookshelf[j][i].getType() == NOTHING || bookshelf[j][i].getType() == BLOCKED)
                            found = 0;
                        break;
                }
                    if (id == 9 && !countType.contains(bookshelf[i][j].getType()))
                        countType.add(bookshelf[i][j].getType());
                    if (id == 4 && !countType.contains(bookshelf[j][i].getType()))
                        countType.add(bookshelf[j][i].getType());

            }
            if (found != 0)
                if(countType.size()<=3)
                    count = count+1;
        }
        if(count>=num)
            return true;
        else
            return false;
    }
}
