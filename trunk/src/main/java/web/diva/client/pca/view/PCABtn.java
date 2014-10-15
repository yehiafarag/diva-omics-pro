/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.pca.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

/**
 *
 * @author Yehia Farag
 */
public class PCABtn extends Label {
    private final PCAPanel pcaPanel;
    public PCABtn(String[] colsNames){
    super("PCA");
    pcaPanel = new PCAPanel(colsNames);
        this.addStyleName("clickable");
        this.setHeight("20px");
        this.setWidth("PCA".length() * 6 + "px");
        this.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getPcaPanel().show();
            }
        });
    
    }

    public PCAPanel getPcaPanel() {
        return pcaPanel;
    }
     public void setClickListener(com.smartgwt.client.widgets.events.ClickHandler handler){
      pcaPanel.getOkBtn().addClickHandler(handler);
      }
    
}
