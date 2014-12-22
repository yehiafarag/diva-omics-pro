/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartUtilities;

/**
 *
 * @author Yehia Farag
 */
public class GroupColorUtil implements Serializable{

    public GroupColorUtil() {
    }

    public synchronized String getImageColor(String hashColor) {
        try {
            Color color = hex2Rgb(hashColor);
            BufferedImage image = new BufferedImage(8, 9, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setBackground(color);
            graphics.setColor(color);
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

            byte[] imageData = null;
            try {
                imageData = ChartUtilities.encodeAsPNG(image);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;

            return base64;

//            
//            File f = new File(path, colName + ".png");
//            if (!f.exists()) {
//                f.createNewFile();
//            }
//
//            ImageIO.write(image, "PNG", f);
//            // Reading a Image file from file system
//            FileInputStream imageInFile = new FileInputStream(f);
//            byte imageData[] = new byte[(int) f.length()];
//            imageInFile.read(imageData);
//            String base64 = Base64.encodeBase64String(imageData);
//            base64 = "data:image/png;base64," + base64;
//
//            return base64;
        } catch (Exception ioexp) {
            System.err.println(ioexp.getMessage());
        }
        return null;
    }

    /**
     *
     * @param colorStr e.g. "#FFFFFF"
     * @return
     */
    private Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

}
