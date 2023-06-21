package model;

import util.Cord;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import static model.Type.NOTHING;

/**This class represents the control of the fifth Common Goal Card*/
public class CGC5 implements CGCStrategy, Serializable {
    @Serial
    private static final long serialVersionUID = 2808176289351890649L;
    public boolean compareRule(Bookshelf bks, int id) {
        Tile[][] bookshelf = bks.getBookshelf();
        Cord cords = new Cord();
        ArrayList<Cord> listOfCords = new ArrayList<>();
        boolean diff;
        boolean found;
        int score = 0;
        Tile nothing = new Tile(Type.NOTHING, 0);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                found = false;
                diff = true;
                for (int counter = 0; counter < listOfCords.size() && !(listOfCords.isEmpty()) && bookshelf[i][j].getType() != NOTHING; counter++) {
                    if (listOfCords.get(counter).getRowCord() == i && listOfCords.get(counter).getColCord() == j && bookshelf[i][j].getType() == NOTHING) { //ALERT!
                        diff = false;
                        break;
                    }
                }
                cords.setCords(i, j);

                if (diff)
                    listOfCords = checkAdj(bookshelf, listOfCords, cords);
                for (int counter = 0; counter < listOfCords.size() && !(listOfCords.isEmpty()); counter++) {
                    int x = listOfCords.get(counter).getRowCord();
                    int y = listOfCords.get(counter).getColCord();
                    bookshelf[x][y] = nothing;
                    found = true;
                }
                if(listOfCords.size()>=4 && found)
                    score++;
                listOfCords.clear();
            }
        }
        return score == 4;
    }

    /**
     * <p>
     *     Method used to check the adjacencies in a bookshelf.
     * </p>
     * @param bookshelf represents the bookshelf on which will be performed the control
     * @param cord is a cord, used to perform the control
     * @param listOfCords is an <code>ArrayList</code> of Cord
     * @return an <code>ArrayList</code> of Cord
     *
     * @see util.Cord*/
    private ArrayList<Cord> checkAdj(Tile[][] bookshelf, ArrayList<Cord> listOfCords, Cord cord) {
        int i = cord.getRowCord();
        int j = cord.getColCord();
        boolean newCord;
        Cord nextTo;

        for (int counter = 0; counter < listOfCords.size() || listOfCords.size() == 0; counter++) {
            newCord = true;
            if (!(listOfCords.isEmpty())) {
                i = listOfCords.get(counter).getRowCord();
                j = listOfCords.get(counter).getColCord();
            }
            nextTo = new Cord();
            nextTo.setCords(i - 1, j);
            if (i > 0)
                if (bookshelf[i][j].getType() == bookshelf[i - 1][j].getType() && bookshelf[i][j].getType() != NOTHING) {
                    if (!(listOfCords.contains(cord))) {
                        cord.setCords(i, j);
                        listOfCords.add(cord);
                    }
                    for (Cord listOfCord : listOfCords)
                        if (listOfCord.getRowCord() == nextTo.getRowCord() && listOfCord.getColCord() == nextTo.getColCord()) {
                            newCord = false;
                            break;
                        }
                    if (newCord)
                        listOfCords.add(nextTo);
                }
            newCord = true;
            nextTo = new Cord();
            nextTo.setCords(i, j + 1);
            if (j < 4)
                if (bookshelf[i][j].getType() == bookshelf[i][j + 1].getType() && bookshelf[i][j].getType() != NOTHING) {
                    if (!(listOfCords.contains(cord))) {
                        cord.setCords(i, j);
                        listOfCords.add(cord);
                    }
                    for (Cord listOfCord : listOfCords)
                        if (listOfCord.getRowCord() == nextTo.getRowCord() && listOfCord.getColCord() == nextTo.getColCord()) {
                            newCord = false;
                            break;
                        }
                    if (newCord)
                        listOfCords.add(nextTo);
                }
            newCord = true;
            nextTo = new Cord();
            nextTo.setCords(i + 1, j);
            if (i < 5)
                if (bookshelf[i][j].getType() == bookshelf[i + 1][j].getType() && bookshelf[i][j].getType() != NOTHING) {
                    if (!(listOfCords.contains(cord))) {
                        cord.setCords(i, j);
                        listOfCords.add(cord);
                    }
                    for (Cord listOfCord : listOfCords)
                        if (listOfCord.getRowCord() == nextTo.getRowCord() && listOfCord.getColCord() == nextTo.getColCord()) {
                            newCord = false;
                            break;
                        }
                    if (newCord)
                        listOfCords.add(nextTo);
                }
            newCord = true;
            nextTo = new Cord();
            nextTo.setCords(i, j - 1);
            if (j > 0)
                if (bookshelf[i][j].getType() == bookshelf[i][j - 1].getType() && bookshelf[i][j].getType() != NOTHING) {
                    if (!(listOfCords.contains(cord))) {
                        cord.setCords(i, j);
                        listOfCords.add(cord);
                    }
                    for (Cord listOfCord : listOfCords)
                        if (listOfCord.getRowCord() == nextTo.getRowCord() && listOfCord.getColCord() == nextTo.getColCord()) {
                            newCord = false;
                            break;
                        }
                    if (newCord)
                        listOfCords.add(nextTo);
                }
            if (listOfCords.size() == 0)
                break;
        }
        return listOfCords;
    }
}