package view.GUI;

import util.ImageReader;
import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;

/**Class that represents the frame of the Personal Goal Card in the Graphic User Interface*/
public class PGCView extends JPanel implements Serializable {
    @Serial
    private static final long serialVersionUID = 4758892564965292619L;
    private final JLabel displayedImage;

    /**
     * Constructor of the Class. This sets the back image for a Personal Goal Card.*/
    public PGCView(){
        ImageReader imageReader = new ImageReader();
        this.displayedImage = new JLabel(imageReader.readIcon("resources/PersonalGoalCardImages/back.jpg", 250, 300));
    }

    /**
     * Method used to get the JLabel relative to the Personal Goal Card.
     * @return a {@link JLabel}*/
    public JLabel getDisplayedImage(){
        return this.displayedImage;
    }

    /**
     * Method used to display a specific Personal Goal Card.
     * @param pgcId is the id of one of the 12 Personal Goal Cards available*/
    public void setDisplayedImage(int pgcId){
        ImageReader imageReader = new ImageReader();
        this.displayedImage.setIcon(imageReader.readIcon("resources/PersonalGoalCardImages/" +pgcId+".png", 250, 300));
    }
}
