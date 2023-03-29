package model;

import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;

import static model.Type.BLOCKED;
import static model.Type.NOTHING;

public interface CGMStrategy {
    boolean compareRule(Bookshelf bks, int id); //takes in the bookshelf of the player to confront it with the CGC
}


class CommonGoalCard1 implements CGMStrategy {
    public boolean compareRule(Bookshelf bks, int id){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
        return false;
    }
}

class CommonGoalCard2 implements CGMStrategy {
    public boolean compareRule(Bookshelf bks, int id){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
        return false;
    }
}

/*4 angles equal*/
class CommonGoalCard3 implements CGMStrategy {
    public boolean compareRule(Bookshelf bks, int id) {
        Tile[][] bookshelf = bks.getBookshelf();
        return bookshelf[6][0] == bookshelf[6][5] && bookshelf[0][0] == bookshelf[0][5] && bookshelf[0][0] == bookshelf[6][0] && !(bookshelf[0][0].getType() == BLOCKED || bookshelf[0][0].getType() == NOTHING);
    }
}

/*with 4, 4 complete rows of 1,2 or 3 different types, with 9, 3 complete columns of 1,2 or 3 different types*/
class CommonGoalCard49 implements CGMStrategy {
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
                if (bookshelf[i][j].getType() == NOTHING || bookshelf[i][j].getType() == BLOCKED && id==9)
                    found=0;
                if (bookshelf[j][i].getType() == NOTHING || bookshelf[j][i].getType() == BLOCKED && id==4)
                    found=0;
                if(id == 9 && !countType.contains(bookshelf[i][j].getType()))
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

class CommonGoalCard5 implements CGMStrategy {
    public boolean compareRule(Bookshelf bks, int id){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
        return false;
    }
}


/*two full columns equal with id 6, full row equal with id 8*/
class CommonGoalCard68 implements CGMStrategy {
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
                    if (bookshelf[i1][j] == bookshelf[i2][j] || bookshelf[i1][j].getType() == NOTHING || bookshelf[i2][j].getType() == NOTHING && id==6)
                        found=0;
                    if (bookshelf[j][i1] == bookshelf[j][i2] || bookshelf[j][i1].getType() == NOTHING || bookshelf[j][i2].getType() == NOTHING && id==8)
                        found=0;
                }
            }
            if (found==1)
                count= count+1;
        }
        if (count>=2)
            return true;
        else
            return false;
    }
}

class CommonGoalCard7 implements CGMStrategy {
    public boolean compareRule(Bookshelf bks, int id){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
        return false;
    }
}

/* x pattern, same type */
class CommonGoalCard10 implements CGMStrategy {
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

/*same type 8 tiles*/
class CommonGoalCard11 implements CGMStrategy {
    public boolean compareRule(Bookshelf bks, int id){
        Tile[][] bookshelf = bks.getBookshelf();
        int i, j;
        boolean result = false;
        ArrayList<Integer> count = new ArrayList<Integer>();
        for(i=0; i<6; i++)
            count.add(i, 0);
        for(i=0; i<6; i++)
            for(j=0; j<5; j++){
                switch (bookshelf[i][j].getType()){
                    case CAT -> {
                        count.set(0, count.get(0) + 1);
                    }

                    case BOOK -> {
                        count.set(1, count.get(1) + 1);
                    }
                    case GAME -> {
                        count.set(2, count.get(2) + 1);
                    }
                    case FRAME -> {
                        count.set(3, count.get(3) + 1);
                    }
                    case TROPHY -> {
                        count.set(4, count.get(4) + 1);
                    }
                    case PLANT -> {
                        count.set(5, count.get(5) + 1);
                    }
                    case NOTHING, BLOCKED -> {
                    }
                }
            }
        for (i=0; i<6; i++)
            if (count.get(i)>=8)
                result= true;
        return result;

    }
}

// The diagonal, both sides
class CommonGoalCard12 implements CGMStrategy {
    public boolean compareRule(Bookshelf bks, int id){

        Tile[][] bookshelf = bks.getBookshelf();
        int i, j;
        boolean result = false;

        //this controls through the matrix from top to bottom and from left to right
        for (i=6; i>1; i--){
            for (j=0;j<5;j++){
                if (bookshelf[i][j].getType() == NOTHING){
                    result = true;
                }
            }
        } if (result == false) {
            //this controls through the matrix from top to bottom and from right to left
            for (i=6; i>1; i--){
                for (j=5;j>0;j--){
                    if (bookshelf[i][j].getType() == NOTHING){
                        result = true;
                    }
                }
            }
        }
            return result;
        //try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}

    }
}
