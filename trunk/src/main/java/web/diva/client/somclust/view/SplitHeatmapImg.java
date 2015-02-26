/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.somclust.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.util.SC;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.SplitedImg;

/**
 *
 * @author Yehia Farag
 */
public class SplitHeatmapImg extends VerticalPanel implements MouseMoveHandler, MouseOutHandler,ClickHandler {

    private final int squareL;
    private final int squareW;
    private double scale =1;

    public void setScale(double scale) {
        this.scale = scale;
    }
    @Override
    public void onMouseMove(MouseMoveEvent event) {
        int y = (int) (event.getY());
        int x = ((int) (event.getX()));
         y = (int)((double)y/scale);         
        x = this.getOffsetHeight()-((int)((double) x/scale));
        int corX = 0;
        int corY = 0;
        if (type == 1) {
            corX = event.getX() / squareW;
            corY = event.getY() / squareL;
        }else{
         corY =x / squareL;
         corX = y / squareW;
        }
        
        if (rowNames[corY] != null && colNames[corX] != null) {
            this.setStyleName("clusterTreeOverNode");
            
            toolTip.setHTML("<textarea cols=\"50\" rows=\"1\">"+ colNames[corX] + " - " + rowNames[corY] + " - Value:" + values[corY][corX]+"</textarea>");
            toolTip.setVisible(true);
        } else {
            toolTip.setVisible(false);
            this.setStyleName("clusterTreeOver");
            
        }

    }

    @Override
    public void onClick(ClickEvent event) {
       int y = (int) (event.getY());
        int x = ((int) (event.getX()));
         y = (int)((double)y/scale);         
        x = this.getOffsetHeight()-((int)((double) x/scale));
        int corX = 0;
        int corY = 0;
        if (type == 1) {
            corX = event.getX() / squareW;
            corY = event.getY() / squareL;
        }else{
         corY =x / squareL;
         corX = y / squareW;
        }
        
        if (rowNames[corY] != null && colNames[corX] != null) {
            selectionManager.setSelectedRows(new Selection(Selection.TYPE.OF_ROWS,new int[]{reindexer[corY]}));
        } else{        
            selectionManager.setSelectedRows(new Selection(Selection.TYPE.OF_ROWS,new int[]{}));
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

    private final Image topImage;
    private final Image bottomImage;
    private final int[] reindexer;
    private final SelectionManager selectionManager;

    public SplitHeatmapImg(SplitedImg simage, String[] rowNames, String[] colNames, double[][] values, HTML toolTip, int type, int squareL, int squareW, int width, int[] reindexes,SelectionManager selectionManager) {
        this.reindexer = reindexes;
        this.selectionManager = selectionManager;
//        super(url);

        topImage = new Image(simage.getImg1Url());
        topImage.setWidth(width + "px");
        topImage.setHeight(simage.getHeight1() + "px");
        bottomImage = new Image(simage.getImg2Url());
        bottomImage.setWidth(width + "px");
        bottomImage.setHeight(simage.getHeight2() + "px");
        this.squareL = squareL;
        this.squareW = squareW;
        this.addDomHandler(SplitHeatmapImg.this, MouseMoveEvent.getType());
        this.addDomHandler(SplitHeatmapImg.this, MouseOutEvent.getType());

        this.addDomHandler(SplitHeatmapImg.this, ClickEvent.getType());
        this.colNames = colNames;
        this.rowNames = rowNames;
        this.values = values;
        this.toolTip = toolTip;
        toolTip.setVisible(false);
        this.type = type;

        this.add(topImage);             
        this.add(bottomImage);
        this.setCellHorizontalAlignment(topImage, VerticalPanel.ALIGN_CENTER); 
        this.setCellHorizontalAlignment(bottomImage, VerticalPanel.ALIGN_CENTER);
        this.setCellVerticalAlignment(bottomImage, VerticalPanel.ALIGN_TOP);
        
        
        
    }
    public void updateTooltips(String[] rowNames, String[] colNames,double[][] values){
     this.colNames = colNames;
        this.rowNames = rowNames;
        this.values = values;
    
    }
}
