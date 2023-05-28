package view;

import javax.swing.*;
import java.awt.*;


public class ImagePanel extends JInternalFrame{ //class to create a JInternalFrame and set its background image
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
