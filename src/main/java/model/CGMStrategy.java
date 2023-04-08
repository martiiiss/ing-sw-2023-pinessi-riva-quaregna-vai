package model;
public interface CGMStrategy {
    final int MAX_ROW = 6;
    final int MAX_COLUMN = 5;

    boolean compareRule(Bookshelf bks, int id); //takes in the bookshelf of the player to confront it with the CGC
}





