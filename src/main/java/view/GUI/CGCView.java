package view.GUI;

import util.ImageReader;

import javax.swing.*;

public class CGCView extends JLabel {
    public CGCView(){
        ImageReader imageReader = new ImageReader();
        setIcon(imageReader.readIcon("resources/CommonGoalCardImages/back.jpg", 100,100));
        setVisible(true);
    }

    public void setCGCView(int id){
        ImageReader imageReader = new ImageReader();
        setIcon(imageReader.readIcon("resources/CommonGoalCardImages/"+id+".jpg", 100,100));
    }
}
