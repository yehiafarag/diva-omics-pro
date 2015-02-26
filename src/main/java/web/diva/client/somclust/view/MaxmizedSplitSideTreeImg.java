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
 * @author Yehia Farag
 */
public class MaxmizedSplitSideTreeImg extends VerticalPanel implements MouseMoveHandler, MouseOutHandler {

    private  final int squareL ;
    private ClientClusterNode mainNode;
    private final int type;
    private final HTML toolTip;
    private boolean selectedNode;
    private double scale =1;
    private int xcor;
    private int ycor;

    @Override
    public final void onMouseMove(MouseMoveEvent event) {
        int y = (int) (event.getY());
        int x = ((int) (event.getX()));
        ClientClusterNode node = null;
        y = (int)((double)y/scale);
        x = this.getOffsetHeight()-((int)((double) x/scale));
            
        switch (type) {
            case 1:
                node = getTooltipAt(y, x, mainNode);
                break;
            case 2:
                node = getTooltipAt(x, y, mainNode);
                break;

        }
        toolTip.setVisible(true);
        if (node != null) {
            xcor = x;
            ycor = y;
            selectedNode = true;
            this.setStyleName("clusterTreeOverNode");
            toolTip.setHTML("<textarea cols=\"50\" rows=\"1\">" + "Merged at " + node.getValue() + " - Nodes: " + node.getMembers() + "</textarea>");
            toolTip.setVisible(true);
        } else {
            xcor = -1000;
            ycor = -10000;
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
 private final Image topImage;
    private final Image bottomImage;
    private final String defaultTopUrl;
    private final String defaultBottomUrl;
    public MaxmizedSplitSideTreeImg(SplitedImg sideTreeeImg, ClientClusterNode node, int type,HTML toolTip,int squareL,int width) {
      
        this.squareL= squareL;
        this.toolTip = toolTip ;
        toolTip.setVisible(false);
          this.setWidth(width+"px");
        this.addDomHandler(MaxmizedSplitSideTreeImg.this,MouseMoveEvent.getType());
        this.addDomHandler(MaxmizedSplitSideTreeImg.this,MouseOutEvent.getType());        
        
        
        
        topImage = new Image(sideTreeeImg.getImg1Url());
        topImage.setWidth(width + "px");
        topImage.setHeight(sideTreeeImg.getHeight1() + "px");
        bottomImage = new Image(sideTreeeImg.getImg2Url());
        bottomImage.setWidth(width + "px");
        bottomImage.setHeight(sideTreeeImg.getHeight2() + "px");        
        this.mainNode = node;
        this.type = type;
        
         this.setSpacing(0);
        this.add(topImage);             
        this.add(bottomImage);
        
        this.setCellHorizontalAlignment(topImage, VerticalPanel.ALIGN_CENTER); 
        this.setCellHorizontalAlignment(bottomImage, VerticalPanel.ALIGN_CENTER);
        this.setCellVerticalAlignment(bottomImage, VerticalPanel.ALIGN_TOP);
        this.defaultTopUrl = sideTreeeImg.getImg1Url();
        this.defaultBottomUrl= sideTreeeImg.getImg2Url();
         this.setStyleName("clusterTreeOver");
      
        
         
    }
    
    public void updateTooltips( ClientClusterNode node){
    this.mainNode = node;
    }

    public boolean isSelectedNode() {
        return selectedNode;
    }
    public void setScale(double scale){
        this.scale = scale;
//        squareL = (int)((double)squareL*scale);
    }

    public int getXcor() {
        return xcor;
    }

    public int getYcor() {
        return ycor;
    }
     public void setUrl(String img1,String img2){
    topImage.setUrl(img1);
    bottomImage.setUrl(img2);
    
    }
    public void clearSelection(){
      topImage.setUrl(defaultTopUrl);
    bottomImage.setUrl(defaultBottomUrl);
    
    
    }

   

}
