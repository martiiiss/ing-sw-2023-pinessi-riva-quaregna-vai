package model;

/**Class that represents the Common Goal Card Strategy Pattern*/
public interface CGCStrategy {
    /**
     * <p>Method that gets override in the implementations of this interface.<br>
     * This method is used to check the condition of a specific common goal card.
     * </p>
     * @param bks the bookshelf on which will be performed the control
     * @param id is an integer that represents the id of the card, needed for the strategy pattern
     * @return a boolean, <b>true</b> if the rule is fulfilled, <b>false</b> otherwise  */
    boolean compareRule(Bookshelf bks, int id);
}





