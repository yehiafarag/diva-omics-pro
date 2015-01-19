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

    private final int squareL = 2;
    private ClientClusterNode mainNode;
    private final int type;
    private final HTML toolTip;
    private boolean selectedNode;

    @Override
    public final void onMouseMove(MouseMoveEvent event) {
        int y = (int) (event.getY());
        int x = ((int) (event.getX()));
        ClientClusterNode node = null;
        x = this.getHeight()-x;
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
          this.setStyleName("clusterTreeOverNode");
            toolTip.setHTML("<textarea cols=\"50\" rows=\"1\">" + "Merged at " + node.getValue() + " - Nodes: " + node.getMembers() + "</textarea>");
            toolTip.setVisible(true);
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

    public MaxmizeTreeImg(String url, ClientClusterNode node, int type,HTML toolTip) {
        super(url);
        this.toolTip = toolTip ;
        toolTip.setVisible(false);
        this.addMouseMoveHandler(MaxmizeTreeImg.this);
        this.addMouseOutHandler(MaxmizeTreeImg.this);
        this.mainNode = node;
        this.type = type;
        
         
    }
    
    public void updateTooltips( ClientClusterNode node){
    this.mainNode = node;
    }

    public boolean isSelectedNode() {
        return selectedNode;
    }

   

}
