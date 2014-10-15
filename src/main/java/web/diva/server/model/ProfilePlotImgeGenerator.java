/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import no.uib.jexpress_modularized.linechart.visualization.LineChart;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartUtilities;
import web.diva.server.model.beans.DivaDataset;

/**
 *
 * @author Yehia Farag
 */
public class ProfilePlotImgeGenerator extends LineChart {

    private boolean aalias = false;
    private boolean[] members;
    private final int width;
    private final int height;

    public ProfilePlotImgeGenerator(DivaDataset dataset, boolean[] members) {
        this.members = members;

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
        int autoalias = 500;
        if (cnt < autoalias) {
            aalias = true;
        }
        setData(dataset, members);
        if (aalias) {
            this.antialias = aalias;
        }
        int over = 0;
        for(String str:dataset.getColumnIds()){
            if(str.length() > over)
                over = str.length();
        }
        width = xaxis.predictLength() + yaxis.predictWidth() + xaxis.endLength() + (over*3);
        height = 400;
        setSize(width, 400);
        setDraw(getDataSelection(new int[]{}));
        forceFullRepaint();       

    }

    public String toImage(){
        BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(Color.WHITE);
        this.paint(graphics);
        byte[] imageData = null;
        try{
        imageData = ChartUtilities.encodeAsPNG(image);
        }catch(Exception e){e.printStackTrace();}
        String base64 = Base64.encodeBase64String(imageData);
        base64 = "data:image/png;base64," + base64;
        return base64;

    }
}
