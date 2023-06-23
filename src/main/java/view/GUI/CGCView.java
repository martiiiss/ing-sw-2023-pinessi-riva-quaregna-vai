package view.GUI;

import util.ImageReader;

import javax.swing.*;
import java.io.Serializable;

public class CGCView extends JLabel implements Serializable {
    private static final long serialVersionUID = 4758892564965722613L;
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
