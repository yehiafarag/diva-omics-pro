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
    private final Image image1,image2,image3,image4;
    
    private final SelectionManager selectionManager;
    public SplitInteractiveClusterColumnSelectionImg(SplitedImg interactiveImg, String[] rowNames,HTML toolTip,int type,int squareL,int width,int[] reindexes,SelectionManager selectionManager) {
       this.selectionManager =selectionManager;
        this.squareL = squareL;
        this.reindexer = reindexes;
        
        this.setWidth(width+"px");
        this.addDomHandler(SplitInteractiveClusterColumnSelectionImg.this,MouseMoveEvent.getType());
        this.addDomHandler(SplitInteractiveClusterColumnSelectionImg.this,MouseOutEvent.getType());
        this.addDomHandler(SplitInteractiveClusterColumnSelectionImg.this,ClickEvent.getType());
        image1 = new Image(interactiveImg.getImg1Url());
        image1.setWidth(width + "px");
        image1.setHeight(interactiveImg.getHeightFirst() + "px");
        
         image2 = new Image(interactiveImg.getImg2Url());
        image2.setWidth(width + "px");
        image2.setHeight(interactiveImg.getHeightFirst() + "px");
        
         image3 = new Image(interactiveImg.getImg3Url());
        image3.setWidth(width + "px");
        image3.setHeight(interactiveImg.getHeightFirst() + "px");
         
        
        image4 = new Image(interactiveImg.getImg4Url());
        image4.setWidth(width + "px");
        image4.setHeight(interactiveImg.getHeightLast()+ "px");
         
        
                this.setSpacing(0);
        this.add(image1);             
         this.add(image2);
          this.add(image3);   
           this.add(image4);   
           
        
        this.setCellHorizontalAlignment(image1, VerticalPanel.ALIGN_CENTER); 
        this.setCellHorizontalAlignment(image2, VerticalPanel.ALIGN_CENTER);
        this.setCellVerticalAlignment(image2, VerticalPanel.ALIGN_TOP);
         this.setCellHorizontalAlignment(image3, VerticalPanel.ALIGN_CENTER);
        this.setCellVerticalAlignment(image3, VerticalPanel.ALIGN_TOP);
         this.setCellHorizontalAlignment(image4, VerticalPanel.ALIGN_CENTER);
        this.setCellVerticalAlignment(image4, VerticalPanel.ALIGN_TOP);
        
        
        
       
        this.rowNames = rowNames;
        
        this.toolTip = toolTip;
        toolTip.setVisible(false);
        this.type= type;
    }
    public void updateTooltips(String[] rowNames){
        this.rowNames = rowNames;
    }
    
    public void setUrl(SplitedImg sideTreeeImg){
    image1.setUrl(sideTreeeImg.getImg1Url());
    image2.setUrl(sideTreeeImg.getImg2Url());
    image3.setUrl(sideTreeeImg.getImg3Url());
    image4.setUrl(sideTreeeImg.getImg4Url());
   
    
    }
    
    }