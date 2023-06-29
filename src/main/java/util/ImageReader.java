package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;

/**Class used to read an image or an icon from the folder resources and return it by invoking the correct method*/
public class ImageReader implements Serializable {
    @Serial
    private static final long serialVersionUID = -7539948344374909126L;

    /**
     * Method used to read icons.<br>
     * If an error occurs during the reading of the image a {@link IOException} gets caught.
     * @param location is a {@code String} that represents the location of the file
     * @param x an int that represents the x scaling of an image
     * @param y an int that represents the y scaling of an image
     * @return an {@link ImageIcon} if everything worked, {@code null} otherwise */
    public ImageIcon readIcon(String location, int x, int y){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location);
        Image img;
        try {
            img = ImageIO.read(is);
            Image scaledImg = img.getScaledInstance(x, y, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (IOException e) {
            //System.err.println("It was impossible to read the image...");
        }
        return null;
     }

     /**
      * Method used to read images.<br>
      If an error occurs during the reading of the image a {@link IOException} gets caught.
      * @param location is a {@code String} that represents the location of the file
      * @param x an int that represents the x scaling of an image
      * @param y an int that represents the y scaling of an image
      * @return a scaled {@link Image}
      * @throws RuntimeException when a {@link IOException} gets caught*/
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
