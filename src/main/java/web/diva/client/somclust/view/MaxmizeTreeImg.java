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
import web.diva.shared.beans.ClientClusterNode;

/**
 *
 * @author Yehia Farag
 */
public class MaxmizeTreeImg extends Image implements MouseMoveHandler, MouseOutHandler {

    private  int squareL ;
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
        x = this.getHeight()-((int)((double) x/scale));

//            int modX = this.getHeight()- ((event.getClientX() - this.getAbsoluteLeft())/2);
//            int modY = event.getClientY() - (this.getAbsoluteTop()/2);
//            
//            
//            
//               
//        switch (type) {
//            case 1:
//
//                node = getTooltipAt(y, x, mainNode);
//                break;
//            case 2:
//                node = getTooltipAt(x, y, mainNode);
//                break;
//
//        }
//            toolTip.setHTML("<textarea cols=\"50\" rows=\"1\">" + " x " + x + " y " + y + "   x " + modX + " relative y " + modY+" node is null "+(node==null)+" layout eidth "+this.getHeight()+"</textarea>");
//            toolTip.setVisible(true);
            
            
            
            
            
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

    public MaxmizeTreeImg(String url, ClientClusterNode node, int type,HTML toolTip,int squareL) {
        super(url);
        this.squareL= squareL;
        this.toolTip = toolTip ;
        toolTip.setVisible(false);
        this.addMouseMoveHandler(MaxmizeTreeImg.this);
        this.addMouseOutHandler(MaxmizeTreeImg.this);
        this.mainNode = node;
        this.type = type;
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

   

}
