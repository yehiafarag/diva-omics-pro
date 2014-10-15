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
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author Yehia Farag
 */
public class HeatmapImg extends Image implements MouseMoveHandler, MouseOutHandler {

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        int x = event.getX() / 12;
        int y = event.getY() / 2;
        if (rowNames[y] != null && colNames[x] != null) {
            toolTip.setHTML("<p style='font-weight: bold; color:white;font-size: 12px;background: #819FF7; border-style:double;'>" + colNames[x] + "<br/>" + rowNames[y] + "<br/> Value:" + values[y][x]+"</p>");
            toolTip.setVisible(true);
        } else {
            toolTip.setVisible(false);
        }

    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        toolTip.setVisible(false);
    }
    
    private final String[] colNames;
    private final String[] rowNames;
    private final double[][] values;
    private final HTML toolTip;
   
    
     @SuppressWarnings("LeakingThisInConstructor")
    public HeatmapImg(String url, String[] rowNames, String[] colNames,double[][] values) {
        super(url);
        this.addMouseMoveHandler(this);
        this.addMouseOutHandler(this);
        this.colNames = colNames;
        this.rowNames = rowNames;
        this.values = values;
        toolTip = new HTML();
        toolTip.setVisible(false);
        RootPanel.get("tooltip").add(toolTip);
    }
}
