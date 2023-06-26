package view.GUI;

import util.ImageReader;

import javax.swing.*;
import java.io.Serializable;


public class PGCView extends JPanel implements Serializable { //manages the PersonalGoalCard section of the GUI
    private static final long serialVersionUID = 4758892564965292619L;
    private JLabel displayedImage;

    public PGCView(){
        ImageReader imageReader = new ImageReader();
        this.displayedImage = new JLabel(imageReader.readIcon("resources/PersonalGoalCardImages/back.jpg", 250, 300));
    }
    public JLabel getDisplayedImage(){
        return this.displayedImage;
    }

    public void setDisplayedImage(int pgcId){
        ImageReader imageReader = new ImageReader();
        this.displayedImage.setIcon(imageReader.readIcon("resources/PersonalGoalCardImages/" +pgcId+".png", 250, 300));
    }
}
