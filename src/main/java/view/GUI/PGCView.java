package view.GUI;

import util.ImageReader;

import javax.swing.*;


public class PGCView extends JPanel { //manages the PersonalGoalCard section of the GUI

    private JLabel displayedImage;

    public PGCView(){
        ImageReader imageReader = new ImageReader();
        this.displayedImage = new JLabel(imageReader.readIcon("resources/PersonalGoalCardImages/back.jpg", 200, 300));
    }
    public JLabel getDisplayedImage(){
        return this.displayedImage;
    }

    public void setDisplayedImage(int pgcId){
        ImageReader imageReader = new ImageReader();
        this.displayedImage.setIcon(imageReader.readIcon("resources/PersonalGoalCardImages/" +pgcId+".png", 200, 300));
    }
}
