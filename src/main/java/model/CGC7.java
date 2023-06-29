package model;

import java.io.Serial;
import java.io.Serializable;

/**This class represents the control of the seventh Common Goal Card*/
public class CGC7 implements CGCStrategy, Serializable {
    @Serial
    private static final long serialVersionUID = 998176289351890649L;

    public boolean compareRule(Bookshelf bks, int id) {
        int count = 0;

        // creating a support matrix so we can simplify corner cases
        Tile[][] matrix = bks.getBookshelf();
        Tile[][] matrixSupport = new Tile[8][7];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 7; c++) {
                if (r > 0 && r < 7 && c > 0 && c < 6) {
                    matrixSupport[r][c] = new Tile(matrix[r - 1][c - 1].getType(), 1);
                } else {
                    matrixSupport[r][c] = new Tile(Type.NOTHING, 1);
                }
            }
        }

        // check for 2x2 with no adiacenses
        for (int i = 1; i < 6; i++) {
            for (int j = 1; j < 5; j++) {


                if (!matrixSupport[i][j].getType().equals(Type.NOTHING)
                        //first check 2x2
                        && matrixSupport[i][j].getType().equals(matrixSupport[i][j + 1].getType())
                        && matrixSupport[i][j].getType().equals(matrixSupport[i + 1][j].getType())
                        && matrixSupport[i][j].getType().equals(matrixSupport[i + 1][j + 1].getType())) {
                    //check for no more than 2x2 (no same color adiacenses)

                    //general case
                    if (         //bottom layer
                            !matrixSupport[i][j].getType().equals(matrixSupport[i - 1][j].getType())
                                    && !matrixSupport[i][j].getType().equals(matrixSupport[i - 1][j + 1].getType())
                                    //i-layer
                                    && !matrixSupport[i][j].getType().equals(matrixSupport[i][j + 2].getType())
                                    && !matrixSupport[i][j].getType().equals(matrixSupport[i][j - 1].getType())
                                    //i+1 layer
                                    && !matrixSupport[i][j].getType().equals(matrixSupport[i + 1][j + 2].getType())
                                    && !matrixSupport[i][j].getType().equals(matrixSupport[i + 1][j - 1].getType())
                                    //i+2 layer
                                    && !matrixSupport[i][j].getType().equals(matrixSupport[i + 2][j].getType())
                                    && !matrixSupport[i][j].getType().equals(matrixSupport[i + 2][j + 1].getType())) {

                        count = count + 1;
                    }
                }
            }
        }
        return count > 1;
    }
}