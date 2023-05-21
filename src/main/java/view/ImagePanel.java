package view;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.io.IOException;

public class ImagePanel extends JInternalFrame{
    private Image img;

    /*public ImagePanel (Image img){
        this.img= img;
        Dimension size = new Dimension(50, 50);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }*/
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
