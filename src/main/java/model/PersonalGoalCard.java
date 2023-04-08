package model;
import com.google.gson.stream.JsonReader;


import java.io.FileReader;
import java.io.IOException;



public class PersonalGoalCard {
    private boolean isCompleted;
    private Tile[][] seedOfCard;

    //numOfPGC is the "name of the card that has to be generated" this number is extracted in game by Rand(). It has to be
    //passed as a param since it needs to be different from all the previous and next generated numOfCards (Every player must
    //have a different card)
    public PersonalGoalCard (int numberOfPGC){
        if(numberOfPGC<=0 || numberOfPGC>12) throw new IllegalArgumentException("Cannot access the card "+numberOfPGC+", it doesn't exist!");
        this.seedOfCard = new Tile[6][5];
        Tile mockTile = new Tile(Type.NOTHING,0);
        for(int i=0;i<6;i++)
            for(int j=0;j<5;j++)
                this.seedOfCard[i][j] = mockTile;
        readerJSON(numberOfPGC);
    }
    private void readerJSON (int id) { //To be set as private once the tests are done
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
                        System.out.print(i+" ");
                    }
                    String col = reader.nextName();
                    if (col.equals("y")) {
                        j = reader.nextInt();
                        System.out.print(j+" ");
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
                        System.out.println(seedOfCard[i][j].getType());
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
        for(i=0;i<6;i++) {
            System.out.println();
            for(j=0;j<5;j++)
                System.out.print(this.seedOfCard[i][j].getType()+" ");
        }
    }

    public Tile[][] getPGC() {
        return this.seedOfCard;
    }

}
