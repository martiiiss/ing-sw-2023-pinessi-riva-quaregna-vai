package model;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class PersonalGoalCard {
    private boolean isCompleted;
    private int position[][];
    private Type tileType[];

    public PersonalGoalCard (int numberOfPGC){
        this.tileType = new Type[6];
        this.position = new int[6][2];
        for(int i=0;i<6;i++){
            this.tileType[i]= Type.NOTHING;
            for(int j=0;j<2;j++){
                this.position[i][j] = 0;
            }
        }
        readerJSON(numberOfPGC);
    }
    int i = 0;
    int j = 0;
    public void readerJSON (int id){
        try {
            JsonReader reader = new JsonReader(new FileReader("src/main/java/model/PersonalGoalCardDescription/"+id+".json"));

            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String parameter = reader.nextName();
                    if (parameter.equals("x")) {
                        position[i][j] = reader.nextInt();
                        j++;
                    } else if (parameter.equals("y")) {
                        position[i][j] = reader.nextInt();
                        j--;
                    } else if (parameter.equals("type")) {
                        switch (reader.nextString()){
                            case "CAT"    -> tileType[i] = Type.CAT;
                            case "BOOK"   -> tileType[i] = Type.BOOK;
                            case "GAME"   -> tileType[i] = Type.GAME;
                            case "FRAME"  -> tileType[i] = Type.FRAME;
                            case "TROPHY" -> tileType[i] = Type.TROPHY;
                            case "PLANT"  -> tileType[i] = Type.PLANT;
                            default -> throw new IllegalStateException("Unexpected value!");
                        }
                        i++;
                    }else{reader.skipValue();}
                }
                reader.endObject();
            }
            reader.endArray();
            reader.close();
        } catch (FileNotFoundException e) {e.printStackTrace();}
          catch (IOException e) {e.printStackTrace();}
    }
    public int[][] getPosition(){return this.position;}
    public Type[] getTileType(){return this.tileType;}

}
