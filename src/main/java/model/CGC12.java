package model;

import static model.Type.NOTHING;

// The diagonal, both sides
class CGC12 implements CGMStrategy {
    public boolean compareRule(Bookshelf bks, int id) {

        Tile[][] bookshelf = bks.getBookshelf();
        int i, j;
        boolean result = false;
        int flag = 0; //the tot of NOTHING tiles has to be 15

        //this controls through the matrix from top to bottom and from left to right
        for (i = 6; i > 1; i--) {
            for (j = 0; j < 5; j++) {
                if (bookshelf[i][j].getType() == NOTHING) {
                    flag++;
                }
            }
        }
        if (flag < 15) { //the if block is there because we want to control the matrix in both ways separately and then put them in OR
            flag = 0;
            //this controls through the matrix from top to bottom and from right to left
            for (i = 6; i > 1; i--) {
                for (j = 5; j > 0; j--) {
                    if (bookshelf[i][j].getType() == NOTHING) {
                        flag++;
                    }
                }
            }
        }


        //this sets the result looking at the times the rest of the code
        // found a NOTHING tile -> in order for this to work it has to find 15 NOTHING tiles in one control or the other

        if (flag == 15) {
            result = true;
        } else {
            result = false;
        }
        return result;
        //try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}

    }
}