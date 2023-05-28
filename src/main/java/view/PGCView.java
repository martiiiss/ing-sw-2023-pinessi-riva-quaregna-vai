package view;

import javax.swing.*;


public class PGCView extends JPanel { //manages the PersonalGoalCard section of the GUI

    private JLabel displayedImage;

    public PGCView(int PGCId){
        ImageReader imageReader = new ImageReader();
        this.displayedImage = new JLabel(imageReader.readIcon("resources/PersonalGoalCardImages/" + PGCId + ".png", 200, 300));
    }
    public JLabel getDisplayedImage(){
        return this.displayedImage;
    }
}
