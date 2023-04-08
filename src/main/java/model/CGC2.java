package model;


public class CGC2 implements CGMStrategy {
    /*DIAGONAL: 5 tiles of the same type*/
    public boolean compareRule(Bookshelf bks, int id){
        Tile[][] bookshelf = bks.getBookshelf();

        for(int r=0; r<MAX_ROW; r++){
            for(int c=0; c+5<=MAX_COLUMN; c++){
                    if(r+4<=MAX_ROW){ //diag dec
                        if(checkDiagonal(r, c, bookshelf,+1))
                            return true;
                    } else if(r>=4){ //diag cresc
                        if(checkDiagonal(r, c, bookshelf, -1))
                            return true;
                    }
            }
        }
        return false;
    }

    public boolean checkDiagonal(int firstRow, int firstColumn, Tile[][] bookshelf, int direction){
        for(int i=0; i<5 ; i++){
            if(bookshelf[firstRow+i*direction][firstColumn+i].getType()==Type.NOTHING ||
                bookshelf[firstRow][firstColumn].getType()!=bookshelf[firstRow+i*direction][firstColumn+i].getType()){
                return false; //diagonal of different tiles (or "nothing")
            }
        }
        return true;
    }
}

