package view.GUI;

import util.ImageReader;
import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;

/**Class that represents the frame of the Scoring Token in the Graphic User Interface*/
public class ScoringTokenView extends JPanel implements Serializable { //manages the ScoringToken section of the GUI
    @Serial
    private static final long serialVersionUID = -8795948759486879869L;
    private final JLabel displayedImage;
    private int valueDisplayed;

    /**Constructor of the Class. This sets the background of a scoring token*/
    public ScoringTokenView (){
        ImageReader imageReader = new ImageReader();
        this.displayedImage = new JLabel(imageReader.readIcon("resources/ScoringTokenImages/back.jpg", 75, 75));
        setVisible(true);
    }

    /**
     * Method used to get the JLabel relative to the Scoring Token.
     * @return a {@link JLabel}*/
    public JLabel getDisplayedImage(){
        return this.displayedImage;
    }

    /**
     * Method used to display the value of a scoring token.
     * @param valueDisplayed an int that represents the value of a scoring token*/
    public void setDisplayedImage(int valueDisplayed) {
        this.valueDisplayed = valueDisplayed;
        ImageReader imageReader = new ImageReader();
        displayedImage.setIcon(imageReader.readIcon("resources/ScoringTokenImages/" + valueDisplayed + ".jpg", 75, 75));
    }

    /**
     * Method used to change the scoring token after one has been popped
     * @param valueDisplayed an int that represents the value of a scoring token*/
    public void popSCV(int valueDisplayed){
        setDisplayedImage(valueDisplayed);
    }
}
