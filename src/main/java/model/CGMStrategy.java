package model;
public interface CGMStrategy {
    int MAX_ROW = 6;
    int MAX_COLUMN = 5;

    boolean compareRule(Bookshelf bks, int id); //takes in the bookshelf of the player to confront it with the CGC
}





