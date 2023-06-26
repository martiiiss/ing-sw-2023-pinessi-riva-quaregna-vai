package util;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

/**Class used to create a JInternalFrame and set its background image*/
public class ImagePanel extends JInternalFrame implements Serializable {
    @Serial
    private static final long serialVersionUID = -2387426453695936563L;
    /**
     *Constructor oƒ the class that overrides the method {@code paintComponent(Graphics g)}.
     * @param title a {@code String} used as the title of the JInternalFrame
     * @param img an {@code Image} used to set the background of the JInternalFrame
     * @param rows an int that represents the number of rows of a JInternalFrame
     * @param columns an int that represents the number of columns of a JInternalFrame
     * @param hgap an int that represents the horizontal distance between components
     * @param vgap an int that represents the vertical distance between components*/
    public ImagePanel(String title, Image img, int rows, int columns, int hgap, int vgap){
        setTitle(title);
        this.setContentPane( new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(img, 0, 0, null);
            }
        });
        setLayout(new GridLayout(rows,columns, hgap, vgap));
        setVisible(true);
    }
}
