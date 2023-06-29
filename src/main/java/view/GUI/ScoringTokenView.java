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

    /**Constructor of the Class. This sets the background of a scoring token*/
    public ScoringTokenView (){
        ImageReader imageReader = new ImageReader();
        this.displayedImage = new JLabel(imageReader.readIcon("resources/ScoringTokenImages/back.jpg", 120, 120));
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
        ImageReader imageReader = new ImageReader();
        if(valueDisplayed==0)
            displayedImage.setIcon(imageReader.readIcon("resources/ScoringTokenImages/back.jpg", 120, 120));
        else
            displayedImage.setIcon(imageReader.readIcon("resources/ScoringTokenImages/" + valueDisplayed + ".jpg", 120, 120));
    }
}
