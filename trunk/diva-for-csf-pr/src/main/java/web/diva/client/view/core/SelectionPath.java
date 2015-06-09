/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 *
 * @author y-mok_000
 */
public class SelectionPath {
         private static final PopupPanel pathPanel = new PopupPanel(true,true);

    private SelectionPath() {
    }
    
    public static void showPath(int startX,int startY,int endX,int endY) {
        pathPanel.setAnimationEnabled(false);
        pathPanel.setPopupPosition(startX, startY);
        pathPanel.setWidth((startX-endX)+"px");
        pathPanel.setHeight((startY-endY)+"px");
        pathPanel.setStyleName("path");
//        pathPanel.setHeight("60px");
        pathPanel.show();
       

    }
    public static void hidePath() {
        pathPanel.hide();
       

    }
}
