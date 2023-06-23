package view.GUI;

import util.ImageReader;

import javax.swing.*;


public class ScoringTokenView extends JPanel { //manages the ScoringToken section of the GUI
    private static final long serialVersionUID = 4758892564965420652L;
    private JLabel displayedImage;

    private int valueDisplayed;

    public ScoringTokenView (){
        ImageReader imageReader = new ImageReader();
        this.displayedImage = new JLabel(imageReader.readIcon("resources/ScoringTokenImages/back.jpg", 100, 100));
        setVisible(true);
    }

    public JLabel getDisplayedImage(){
        return this.displayedImage;
    }

    public void setDisplayedImage(int valueDisplayed) {
        this.valueDisplayed = valueDisplayed;
        ImageReader imageReader = new ImageReader();
        displayedImage.setIcon(imageReader.readIcon("resources/ScoringTokenImages/" + valueDisplayed + ".jpg", 100, 100));
    }

    public int getValueDisplayed(){
        return this.valueDisplayed;
    }


    public void popSCV(int valueDisplayed){ //changes the scoring token
        ImageReader imageReader = new ImageReader();
        setDisplayedImage(valueDisplayed);
    }
}
