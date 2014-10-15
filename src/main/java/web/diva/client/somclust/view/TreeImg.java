/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.somclust.view;

import com.google.gwt.user.client.ui.Image;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import web.diva.shared.beans.ClientClusterNode;

/**
 *
 * @author Yehia Farag
 */
public class TreeImg extends Image implements MouseMoveHandler, MouseOutHandler {

    private final int squareL = 2;
    private final ClientClusterNode mainNode;
    private final int type;
    private final HTML toolTip;
    private boolean selectedNode;

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
            toolTip.setHTML("<p style='font-weight: bold; color:white;font-size: 15px;background: #819FF7; border-style:double;'>"+ "Merged at " +node.getValue()+"<br/>" + "Nodes: " + node.getMembers()+"</p>");
            toolTip.setVisible(true);
        }
        else{
            selectedNode = false;
            toolTip.setVisible(false);
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

    @SuppressWarnings("LeakingThisInConstructor")
    public TreeImg(String url, ClientClusterNode node, int type) {
        super(url);
        toolTip = new HTML();
        toolTip.setVisible(false);
        RootPanel.get("tooltip").add(toolTip);
        this.addMouseMoveHandler(this);
        this.addMouseOutHandler(this);
        this.mainNode = node;
        this.type = type;
         
    }

    public boolean isSelectedNode() {
        return selectedNode;
    }

   

}
