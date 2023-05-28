package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class ImageReader {

    //class to read an image or an icon from the folder resources and return it by invoking the correct method

    public ImageIcon readIcon(String location, int x, int y){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location);
        Image img;
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image scaledImg = img.getScaledInstance(x, y, Image.SCALE_SMOOTH);
         ImageIcon im = new ImageIcon(scaledImg);
        return im;
     }
    public Image readImage(String location, int x, int y){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location);
        Image img;
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img.getScaledInstance(x, y, Image.SCALE_SMOOTH);
    }
}
