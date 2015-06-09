/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author Yehia Farag
 */
public class JFreeImgGenerator {

    private BufferedImage bimage ;
    public synchronized String saveToFile(final JFreeChart chart,final double width,final double height, ChartRenderingInfo chartRenderingInfo) {
        byte imageData[] = null;
        try {
            if (chartRenderingInfo != null) {
                imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
              
            } else {
                System.out.println("start 3 "); 
                
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        bimage = chart.createBufferedImage((int) width, (int) height);
//                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                });
              t.start();
              while(t.isAlive()){
                  try{
              Thread.sleep(100);
                  }catch(InterruptedException e){e.printStackTrace();}
              }
                
                System.out.println("end 3");
                 imageData = ChartUtilities.encodeAsPNG(bimage);
               System.out.println("end 4");
                
                
                
            }
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;
            return base64;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return "";

    }

    public Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }
    
    

}
