package model;

import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;

import static model.Type.BLOCKED;
import static model.Type.NOTHING;

public interface CGMStrategy {
    final int MAX_ROW = 6;
    final int MAX_COLUMN = 5;

    boolean compareRule(Bookshelf bks, int id); //takes in the bookshelf of the player to confront it with the CGC
}





