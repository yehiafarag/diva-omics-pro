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
public class InteractiveClusterSelectionImg extends Image implements MouseMoveHandler, MouseOutHandler {

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        int y = 0;
        if (type == 1) {
            y = event.getY() / 2;
        }else{
         y = event.getX() / 2;
        }
        toolTip.setHTML("<textarea cols=\"50\" rows=\"1\">"+ y +"</textarea>");
        toolTip.setVisible(true);
        if (rowNames[y] != null ) {
            toolTip.setHTML("<textarea cols=\"50\" rows=\"1\">"+ rowNames[y] +"</textarea>");
            toolTip.setVisible(true);
        } else {
//            toolTip.setVisible(false);
        }

    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        toolTip.setVisible(false);
    }
    
    private  String[] rowNames;
    private final HTML toolTip;
    private final int type;
   
    
    public InteractiveClusterSelectionImg(String url, String[] rowNames,HTML toolTip,int type) {
        super(url);
        this.addMouseMoveHandler(InteractiveClusterSelectionImg.this);
        this.addMouseOutHandler(InteractiveClusterSelectionImg.this);
       
        this.rowNames = rowNames;
        
        this.toolTip = toolTip;
        toolTip.setVisible(false);
        this.type= type;
    }
    public void updateTooltips(String[] rowNames){
        this.rowNames = rowNames;
    }
    
    }