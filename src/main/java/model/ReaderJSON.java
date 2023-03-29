package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ReaderJSON {
    public void boh() {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("1.json");) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);

            String name = (String) jsonObject.get("first tile");
            System.out.println(name);

            int x = (Integer) jsonObject.get("x");
            System.out.println(x);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}




/** I'm still trying to understand how this actually works.
 *  Online I found plenty of different ways to write the code used to read JSON files
 *  but each one of them is different in some way, also I still have to think if having 12 different JSON
 *  is actually a great idea or if it's better to have a single big file divided into 12 pieces.
 *
 *  Also, the code is incomplete and I have to try to compile it to see if it has any sense before completing it**/


