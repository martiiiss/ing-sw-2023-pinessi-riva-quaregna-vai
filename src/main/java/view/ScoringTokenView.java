package view;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class ScoringTokenView extends JPanel {

    private int valueDisplayed;
    private int referenceCGC;

    private JLabel displayedImage;

    public ScoringTokenView (int valueDisplayed, int referenceCGC) {
        this.valueDisplayed = valueDisplayed;
        this.referenceCGC = referenceCGC;
    }

    public int getReferenceCGC() {
        return this.referenceCGC;
    }

    public int getValueDisplayed() {
        return valueDisplayed;
    }

    public JLabel getDisplayedImage(){
        return this.displayedImage;
    }

    public void setDisplayedImage() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("resources/ScoringTokenImages/" + this.valueDisplayed + ".jpg");
        Image img = ImageIO.read(is);
        Image scaledImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        this.displayedImage = new JLabel(new ImageIcon(scaledImg));
    }

    public void setReferenceCGC(int referenceCGC) {
        this.referenceCGC = referenceCGC;
    }

    public void setValueDisplayed(int valueDisplayed) {
        this.valueDisplayed = valueDisplayed;
    }
}
