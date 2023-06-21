package model;


import java.io.Serial;
import java.io.Serializable;
import static model.Bookshelf.SHELF_ROW;
import static model.Bookshelf.SHELF_COLUMN;

/**This class represents the control of the second Common Goal Card*/
public class CGC2 implements CGCStrategy, Serializable {
    @Serial
    private static final long serialVersionUID = 6808176289351890600L;

    public boolean compareRule(Bookshelf bks, int id){
        Tile[][] bookshelf = bks.getBookshelf();

        for(int r=0; r<SHELF_ROW; r++){
            for(int c=0; c+5<=SHELF_COLUMN; c++){
                    if(r+4<=SHELF_ROW){ //ascending diagonal
                        if(checkDiagonal(r, c, bookshelf,+1))
                            return true;
                    } else if(r>=4){ //descending diagonal
                        if(checkDiagonal(r, c, bookshelf, -1))
                            return true;
                    }
            }
        }
        return false;
    }

    /**
     * <p>
     *     Method that checks if a diagonal of the bookshelf is composed by all tiles of the same type.
     * </p>
     * @param bookshelf represents the bookshelf on which will be performed the control
     * @param direction is an int (+1 for descending diagonal, -1 for ascending diagonal)
     * @param firstColumn is an int that indicates the column on which the diagonal starts
     * @param firstRow is an int that indicates the row on which the diagonal starts
     * @return a boolean, <b>true</b> if a diagonal is all composed by the same type of tile, <b>false</b> otherwise*/
    public boolean checkDiagonal(int firstRow, int firstColumn, Tile[][] bookshelf, int direction){
        for(int i=0; i<5 ; i++){
            if(bookshelf[firstRow+i*direction][firstColumn+i].getType()==Type.NOTHING ||
                bookshelf[firstRow][firstColumn].getType()!=bookshelf[firstRow+i*direction][firstColumn+i].getType()){
                return false;
            }
        }
        return true;
    }
}

