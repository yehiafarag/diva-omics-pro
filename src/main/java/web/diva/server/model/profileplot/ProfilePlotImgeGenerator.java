/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model.profileplot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartUtilities;
import web.diva.server.model.beans.DivaDataset;

/**
 *
 * @author Yehia Farag
 */
public class ProfilePlotImgeGenerator extends ProfilePlot {

    ChartUtilities cu = new ChartUtilities() {
};
    private boolean aalias = false;
    private boolean[] members;
    private  int width;
    private  int height;

    public ProfilePlotImgeGenerator(final DivaDataset dataset, boolean[] members) {
        this.members = members;
        updateDataset(dataset);
        

    }
    
    public final void updateDataset(DivaDataset dataset){
    
    int cnt = 0;
        if (members == null) {
            cnt = dataset.getDataLength();
        } else {
            for (int i = 0; i < members.length; i++) {
                if (members[i]) {
                    cnt++;
                }
            }
        }
        int autoalias = 1000;
        if (cnt < autoalias) {
            aalias = true;
        }
        setData(dataset, members);
        if (aalias) {
            this.setAntialias(aalias);
        }
        int over = 0;
        for(String str:dataset.getColumnIds()){
            if(str.length() > over)
                over = str.length();
        }
      getXaxis().minimumSize=900;
   
        width = getXaxis().predictLength() + getYaxis().predictWidth() + getXaxis().endLength() + (over*4);
        height = 700;
        super.setDsize(new Dimension(width, height));
        setSize(new Dimension(getDsize().width, getDsize().height));
        setDraw(getDataSelection(new int[]{}));
         setForeground(new Color(0, 51, 153));
        forceFullRepaint();       
    
    }
    

    @Override
    public String toImage(){
        BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(Color.WHITE);
       
        super.forceFullRepaint();
        super.paint(graphics);
        byte[] imageData = null;
        try{
        imageData = BufferedImageToByteArray(image);
        }catch(Exception e){e.printStackTrace();}
        String base64 = Base64.encodeBase64String(imageData);
        base64 = "data:image/png;base64," + base64;
        return base64;

    }
    
    public byte[] BufferedImageToByteArray(BufferedImage orImage){
  try{
    System.out.println("working ");
  ByteArrayOutputStream baos=new ByteArrayOutputStream();
  ImageIO.write(orImage, "png", baos );
     
  byte[] imageBytes=baos.toByteArray();
  return imageBytes;
  //do something with the byte array
  
  }catch(IOException ie){}
  return null;
 }
}
