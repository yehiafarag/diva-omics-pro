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
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.SplitedImg;

/**
 *
 * @author Yehia Farag
 */
public class SplitInteractiveClusterColumnSelectionImg extends VerticalPanel implements MouseMoveHandler, MouseOutHandler,ClickHandler {

     private final int squareL;
     private double scale =1;
    @Override
    public void onMouseMove(MouseMoveEvent event) {
//         y = (int)((double)y/scale);
        int x = ((int) (event.getX()));
        x = this.getOffsetHeight()-((int)((double) x/scale));
        int y = 0;
        if (type == 1) {
            y = event.getY() / squareL;
        }else{
         y = x / squareL;
        }
        toolTip.setHTML("<textarea cols=\"50\" rows=\"1\">"+ y +"</textarea>");
        toolTip.setVisible(true);
        if (rowNames[y] != null ) {
            this.setStyleName("clusterTreeOverNode");
            
            toolTip.setHTML("<textarea cols=\"50\" rows=\"1\">"+ rowNames[y] +"</textarea>");
            toolTip.setVisible(true);
        } else {
            this.setStyleName("clusterTreeOver");
            
//            toolTip.setVisible(false);
        }

    }

    @Override
    public void onClick(ClickEvent event) {
        int x = ((int) (event.getX()));
        x = this.getOffsetHeight()-((int)((double) x/scale));
        int y = 0;
        if (type == 1) {
            y = event.getY() / squareL;
        }else{
         y = x / squareL;
        }
        if (rowNames[y] != null ) {
            selectionManager.setSelectedRows(new Selection(Selection.TYPE.OF_ROWS,new int[]{reindexer[y]}));
        } else{        
            selectionManager.setSelectedRows(new Selection(Selection.TYPE.OF_ROWS,new int[]{}));
        }
    }
    

    public void setScale(double scale) {
        this.scale = scale;
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        toolTip.setVisible(false);
    }
    
    private  String[] rowNames;
    private  final int[] reindexer;
    private final HTML toolTip;
    private final int type;
     private final Image topImage;
    private final Image bottomImage;
    
    private final SelectionManager selectionManager;
    public SplitInteractiveClusterColumnSelectionImg(SplitedImg interactiveImg, String[] rowNames,HTML toolTip,int type,int squareL,int width,int[] reindexes,SelectionManager selectionManager) {
       this.selectionManager =selectionManager;
        this.squareL = squareL;
        this.reindexer = reindexes;
        
        this.setWidth(width+"px");
        this.addDomHandler(SplitInteractiveClusterColumnSelectionImg.this,MouseMoveEvent.getType());
        this.addDomHandler(SplitInteractiveClusterColumnSelectionImg.this,MouseOutEvent.getType());
        this.addDomHandler(SplitInteractiveClusterColumnSelectionImg.this,ClickEvent.getType());
        topImage = new Image(interactiveImg.getImg1Url());
        topImage.setWidth(width + "px");
        topImage.setHeight(interactiveImg.getHeight1() + "px");
        bottomImage = new Image(interactiveImg.getImg2Url());
        bottomImage.setWidth(width + "px");
        bottomImage.setHeight(interactiveImg.getHeight2() + "px");        
        this.setSpacing(0);
        this.add(topImage);             
        this.add(bottomImage);
        
        this.setCellHorizontalAlignment(topImage, VerticalPanel.ALIGN_CENTER); 
        this.setCellHorizontalAlignment(bottomImage, VerticalPanel.ALIGN_CENTER);
        this.setCellVerticalAlignment(bottomImage, VerticalPanel.ALIGN_TOP);
        
        
       
        this.rowNames = rowNames;
        
        this.toolTip = toolTip;
        toolTip.setVisible(false);
        this.type= type;
    }
    public void updateTooltips(String[] rowNames){
        this.rowNames = rowNames;
    }
    
    public void setUrl(String url1,String url2){
    topImage.setUrl(url1);
    bottomImage.setUrl(url2);
    }
    
    }