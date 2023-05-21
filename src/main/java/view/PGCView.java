package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class PGCView extends JPanel {

    private int PGCId;
    private JLabel displayedImage;


    public PGCView(int PGCId){
        this.PGCId = PGCId;

    }
    public void setDisplayedImage() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("resources/PersonalGoalCardImages/" + this.PGCId + ".png");
        Image img = ImageIO.read(is);
        Image scaledImg = img.getScaledInstance(300, 400, Image.SCALE_SMOOTH);
        this.displayedImage = new JLabel(new ImageIcon(scaledImg));
    }
    public JLabel getDisplayedImage(){
        return this.displayedImage;
    }
}
