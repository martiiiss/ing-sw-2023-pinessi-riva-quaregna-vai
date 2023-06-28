package view.GUI;

import util.ImageReader;
import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;

/**Class that represents the frame of the Common Goal Card in the Graphic User Interface*/
public class CGCView extends JLabel implements Serializable {
    @Serial
    private static final long serialVersionUID = 4758892564965722613L;

    /**Constructor of the Class. This sets the image of the two Common Goal Cards.*/
    public CGCView(){
        ImageReader imageReader = new ImageReader();
        setIcon(imageReader.readIcon("resources/CommonGoalCardImages/back.jpg", 120, 120));
        setVisible(true);
    }

    /**
     * This method sets the number of the Common Goal Card.
     * @param id an int that represents the id of a common goal card*/
    public void setCGCView(int id){
        ImageReader imageReader = new ImageReader();
        setIcon(imageReader.readIcon("resources/CommonGoalCardImages/"+id+".jpg", 120,120));
    }
}
