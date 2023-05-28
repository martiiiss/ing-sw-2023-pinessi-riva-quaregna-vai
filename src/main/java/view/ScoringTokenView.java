package view;

import javax.swing.*;


public class ScoringTokenView extends JPanel { //manages the ScoringToken section of the GUI

    private JLabel displayedImage;

    public ScoringTokenView (int valueDisplayed) {
        setDisplayedImage(valueDisplayed);
    }


    public JLabel getDisplayedImage(){
        return this.displayedImage;
    }

    public void setDisplayedImage(int valueDisplayed) {
        ImageReader imageReader = new ImageReader();
        this.displayedImage = new JLabel(imageReader.readIcon("resources/ScoringTokenImages/" + valueDisplayed + ".jpg", 100, 100));
    }


    public void popSCV(int valueDisplayed){ //changes the
        ImageReader imageReader = new ImageReader();
        setDisplayedImage(valueDisplayed);
    }
}
