/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.somclust.view;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 *
 * @author Yehia Farag
 */
public class HeatmapImg extends Image implements MouseMoveHandler, MouseOutHandler {

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        int x = 0;
        int y = 0;
        if (type == 1) {
            x = event.getX() / 12;
            y = event.getY() / 2;
        }else{
         y = event.getX() / 2;
            x = event.getY() / 12;
        }
        
        if (rowNames[y] != null && colNames[x] != null) {
            toolTip.setHTML("<textarea cols=\"50\" rows=\"1\">"+ colNames[x] + " - " + rowNames[y] + " - Value:" + values[y][x]+"</textarea>");
            toolTip.setVisible(true);
        } else {
            toolTip.setVisible(false);
        }

    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        toolTip.setVisible(false);
    }
    
    private  String[] colNames;
    private  String[] rowNames;
    private  double[][] values;
    private final HTML toolTip;
    private final int type;
   
    
    public HeatmapImg(String url, String[] rowNames, String[] colNames,double[][] values,HTML toolTip,int type) {
        super(url);
        this.addMouseMoveHandler(HeatmapImg.this);
        this.addMouseOutHandler(HeatmapImg.this);
        this.colNames = colNames;
        this.rowNames = rowNames;
        this.values = values;
        this.toolTip = toolTip;
        toolTip.setVisible(false);
        this.type= type;
    }
    public void updateTooltips(String[] rowNames, String[] colNames,double[][] values){
     this.colNames = colNames;
        this.rowNames = rowNames;
        this.values = values;
    
    }
}
