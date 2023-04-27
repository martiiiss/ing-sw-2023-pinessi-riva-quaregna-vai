package model;

import java.util.ArrayList;

/*same type 8 tiles*/
public class CGC11 implements CGCStrategy {
    public boolean compareRule(Bookshelf bks, int id){
        Tile[][] bookshelf = bks.getBookshelf();
        int i, j;
        boolean result = false;
        ArrayList<Integer> count = new ArrayList<>();
        for(i=0; i<6; i++)
            count.add(i, 0);
        for(i=0; i<6; i++)
            for(j=0; j<5; j++){
                switch (bookshelf[i][j].getType()){
                    case CAT -> count.set(0, count.get(0) + 1);

                    case BOOK -> count.set(1, count.get(1) + 1);
                    case GAME -> count.set(2, count.get(2) + 1);
                    case FRAME -> count.set(3, count.get(3) + 1);
                    case TROPHY -> count.set(4, count.get(4) + 1);
                    case PLANT -> count.set(5, count.get(5) + 1);
                    case NOTHING, BLOCKED -> {}
                }
            }
        for (i=0; i<6; i++)
            if (count.get(i)>=8)
                result= true;
        return result;

    }
}
