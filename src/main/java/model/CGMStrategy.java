package model;

import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;

import static model.Type.BLOCKED;
import static model.Type.NOTHING;

public interface CGMStrategy {
    boolean compareRule(Bookshelf bks, int id); //takes in the bookshelf of the player to confront it with the CGC
}



