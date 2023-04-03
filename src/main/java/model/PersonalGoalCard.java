package model;
import jdk.jshell.spi.ExecutionControl;
/*
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.lang.reflect.Type;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.*;
 */




public class PersonalGoalCard {

    private Type[][] seed; //tiles position
    private boolean isCompleted;

    public PersonalGoalCard (int id){
    }

    //Do we really need the seed?
    public Type[][] getSeed() {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
        return seed;
    }

}
