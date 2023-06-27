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
        setIcon(imageReader.readIcon("resources/CommonGoalCardImages/back.jpg", 75, 75));
        setVisible(true);
    }

    /**
     * This method sets the number of the Common Goal Card.*/
    public void setCGCView(int id){
        ImageReader imageReader = new ImageReader();
        setIcon(imageReader.readIcon("resources/CommonGoalCardImages/"+id+".jpg", 75,75));
    }
}
