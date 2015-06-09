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
import com.google.gwt.user.client.ui.VerticalPanel;

import web.diva.shared.beans.ClientClusterNode;
import web.diva.shared.beans.SplitedImg;

/**
 *
 * @author y-mok_000
 */
public class SplitSideTreeImg extends VerticalPanel implements MouseMoveHandler, MouseOutHandler{
    
    private final int squareL ;
    private ClientClusterNode mainNode;
    private final int type;
    private final HTML toolTip;
    private boolean selectedNode;
//    private int xcor,ycor;

    
    @Override
    public final void onMouseMove(MouseMoveEvent event) {
        int y = (int) (event.getY());
        int x = ((int) (event.getX()));
        ClientClusterNode node = null;
        switch (type) {
            case 1:
                node = getTooltipAt(y, x, mainNode);
                break;
            case 2:
                node = getTooltipAt(x, y, mainNode);
                break;

        }
        if (node != null) {
            selectedNode = true;
            toolTip.setHTML("<textarea cols=\"50\" rows=\"1\">" + "Merged at " + node.getValue() + " - Nodes: " + node.getMembers() + "</textarea>");
            toolTip.setVisible(true);
            this.setStyleName("clusterTreeOverNode");
        } else {
            selectedNode = false;
            toolTip.setVisible(false);
            this.setStyleName("clusterTreeOver");
        }
    }

   
    @Override
    public final void onMouseOut(MouseOutEvent event) {
        toolTip.setVisible(false);
        selectedNode = false;
    }

    public ClientClusterNode getTooltipAt(int xcor, int ycor, ClientClusterNode trunk) {
        ClientClusterNode ret = null;
        if (trunk != null) {
            if (trunk.getX() > xcor - squareL && trunk.getX() < xcor + squareL
                    && trunk.getY() > ycor - squareL && trunk.getY() < ycor + squareL) {
                ret = trunk;
            } else {
                ret = getTooltipAt(xcor, ycor, trunk.getRight());
            }
            if (ret == null) {
                ret = getTooltipAt(xcor, ycor, trunk.getLeft());
            }
        }
        return ret;
    }
   private final Image image1,image2,image3,image4;
//    private final Image bottomImage;
    private final SplitedImg defaultSideTreeeImg;
    
    public SplitSideTreeImg(SplitedImg sideTreeeImg, ClientClusterNode node, int type,HTML toolTip,int squareL,int width,int height) {
        this.squareL = squareL;
        this.toolTip = toolTip ;
        toolTip.setVisible(false);
        this.setWidth(width+"px");
        this.addDomHandler(SplitSideTreeImg.this,MouseMoveEvent.getType());
        this.addDomHandler(SplitSideTreeImg.this,MouseOutEvent.getType());        
        
        
      image1 = new Image(sideTreeeImg.getImg1Url());
        image1.setWidth(width + "px");
        image1.setHeight(sideTreeeImg.getHeightFirst() + "px");
        
         image2 = new Image(sideTreeeImg.getImg2Url());
        image2.setWidth(width + "px");
        image2.setHeight(sideTreeeImg.getHeightFirst() + "px");
        
         image3 = new Image(sideTreeeImg.getImg3Url());
        image3.setWidth(width + "px");
        image3.setHeight(sideTreeeImg.getHeightFirst() + "px");
         
        
        image4 = new Image(sideTreeeImg.getImg4Url());
        image4.setWidth(width + "px");
        image4.setHeight(sideTreeeImg.getHeightLast()+ "px");
         
      
               
        this.mainNode = node;
        this.type = type;
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
        
        
        
        
        this.defaultSideTreeeImg = sideTreeeImg;
       
         
    }
    
    public void updateTooltips( ClientClusterNode node){
    this.mainNode = node;
    }

    public boolean isSelectedNode() {
        return selectedNode;
    }
    
    public void setUrl(SplitedImg sideTreeeImg){
    image1.setUrl(sideTreeeImg.getImg1Url());
    image2.setUrl(sideTreeeImg.getImg2Url());
    image3.setUrl(sideTreeeImg.getImg3Url());
    image4.setUrl(sideTreeeImg.getImg4Url());
  
    
    }
    public void clearSelection(){
      image1.setUrl(defaultSideTreeeImg.getImg1Url());
    image2.setUrl(defaultSideTreeeImg.getImg2Url());
    image3.setUrl(defaultSideTreeeImg.getImg3Url());
    image4.setUrl(defaultSideTreeeImg.getImg4Url());
    
    
    
    }

   

}
