package model;

import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

/**Class that represents the Personal Goal Card*/
public class PersonalGoalCard implements Serializable {
    @Serial
    private static final long serialVersionUID = -5786944275606187567L;
    private Tile[][] seedOfCard;
    private int number;

    //numOfPGC is the "name of the card that has to be generated" this number is extracted in game by Rand(). It has to be
    //passed as a param since it needs to be different from all the previous and next generated numOfCards (Every player must
    //have a different card)

    /**
     * <p>
     *     Constructor of the Class.<br>
     *     This initializes the PersonalGoalCard given an int in between 0 and 12, it also creates a matrix <code>mockTile</code>
     *     used to save the PersonalGoalCard read from the corresponding json files.
     *     This also calls the method <code>readerJSON</code>.
     * </p>
     * @param numberOfPGC an int that represents a specific PersonalGoalCard
     * @throws IllegalArgumentException when the {@code numberOfPGC} doesn't respect the rule {@code 0 <= numberOfPGC < 12}
     * */

    public PersonalGoalCard (int numberOfPGC){
        this.number = numberOfPGC;
        if(numberOfPGC<=0 || numberOfPGC>12) throw new IllegalArgumentException("Cannot access the card "+numberOfPGC+", it doesn't exist!");
        this.seedOfCard = new Tile[6][5];
        Tile mockTile = new Tile(Type.NOTHING,0);
        for(int i=0;i<6;i++)
            for(int j=0;j<5;j++)
                this.seedOfCard[i][j] = mockTile;
        readerJSON(numberOfPGC);
    }

    /**
     * <p>
     *     Method that given an int instantiates a <code>JsonReader</code> used to read from
     *     json files. After reading it saves the PersonalGoalCard inside <code>seedOfCard</code>.
     * </p>
     * @param id is an int that represents the number of the personalGoalCard that has to be read from the json files
     * @throws IllegalStateException when it's read from the json an invalid type of tile
     */
    private void readerJSON (int id) {
        int i = 0, j = 0;
        Tile readTile;
        try {
            JsonReader reader = new JsonReader(new FileReader("src/main/java/model/PersonalGoalCardDescription/" + id + ".json"));
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String row = reader.nextName();
                    if (row.equals("x")) {
                        i = reader.nextInt();
                    }
                    String col = reader.nextName();
                    if (col.equals("y")) {
                        j = reader.nextInt();
                    }
                    String type = reader.nextName();
                    if (type.equals("type")) {
                        switch (reader.nextString()) {
                            case "CAT" -> readTile = new Tile(Type.CAT,0);
                            case "BOOK" -> readTile = new Tile(Type.BOOK,0);
                            case "GAME" -> readTile = new Tile(Type.GAME,0);
                            case "FRAME" -> readTile = new Tile(Type.FRAME,0);
                            case "TROPHY" -> readTile = new Tile(Type.TROPHY,0);
                            case "PLANT" -> readTile = new Tile(Type.PLANT,0);
                            default -> throw new IllegalStateException("Unexpected value:" + reader.nextString());
                        }
                        seedOfCard[i][j]=readTile;
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            }
            reader.endArray();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     *     Method used to get a specific <code>seedOfCard</code>.
     * </p>
     * @return a matrix of Tile that represents the seedOfCard*/
    public Tile[][] getPGC() {
        return this.seedOfCard;
    }

    /**
     * <p>
     *     Method used to get the number that identifies a specific personaGoalCard.
     * </p>
     * @return an integer that represents a specific personaGoalCard */
    public int getNumber() {
        return number;
    }
}
