package util;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;


public class ImagePanel extends JInternalFrame implements Serializable { //class to create a JInternalFrame and set its background image
    @Serial
    private static final long serialVersionUID = -2387426453695936563L;
    private Image img;


    public ImagePanel(String title, Image img, int rows, int columns, int hgap, int vgap){
        setTitle(title);

        this.setContentPane( new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(img, 0, 0, null);
            };

        });
        setLayout(new GridLayout(rows,columns, hgap, vgap));

        setVisible(true);
    }

}
