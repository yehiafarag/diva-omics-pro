/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.IButton;

/**
 *
 * @author Yehia Farag
 */
public class SaveAsPanel extends PopupPanel {

    private final  HTML link;
    public SaveAsPanel(String fileType) {
        this.setAnimationEnabled(true);
        this.ensureDebugId("cwBasicPopup-imagePopup");
        this.setModal(false);

        VerticalPanel mainBodyLayout = new VerticalPanel();
        mainBodyLayout.setWidth("300px");
        mainBodyLayout.setHeight("150px");
       
          HorizontalPanel topLayout = new HorizontalPanel();
        topLayout.setWidth("300px");
        topLayout.setHeight("20px");
        mainBodyLayout.add(topLayout);
        Label title = new Label("Save "+fileType);
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth(200 + "px");

        Label closeBtn = new Label();

        closeBtn.addStyleName("close");
        closeBtn.setHeight("16px");
        closeBtn.setWidth("16px");

        
        topLayout.add(closeBtn);
        
        HorizontalPanel btnsLayout = new HorizontalPanel();
        btnsLayout.setWidth("300px");
        btnsLayout.setHeight("130px");
        mainBodyLayout.add(btnsLayout);
        mainBodyLayout.setCellVerticalAlignment(btnsLayout, VerticalPanel.ALIGN_MIDDLE);
        mainBodyLayout.setCellHorizontalAlignment(btnsLayout, VerticalPanel.ALIGN_CENTER);
       
        
       

        link = new HTML();
        link.setStyleName("buttonRounded");
        link.setWidth("120px");
        btnsLayout.add(link);
        btnsLayout.setCellVerticalAlignment(link, HorizontalPanel.ALIGN_MIDDLE);
        btnsLayout.setCellHorizontalAlignment(link, HorizontalPanel.ALIGN_CENTER);
        
         IButton cancelExportBtn = new IButton("Cancel");
        btnsLayout.add(cancelExportBtn);
         btnsLayout.setCellVerticalAlignment(cancelExportBtn, HorizontalPanel.ALIGN_MIDDLE);
        btnsLayout.setCellHorizontalAlignment(cancelExportBtn, HorizontalPanel.ALIGN_CENTER);
        cancelExportBtn.setWidth("120px");
        cancelExportBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                hide();
            }
        });
        ClickHandler hideHandler = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        }; 
        link.addClickHandler(hideHandler);
        closeBtn.addClickHandler(hideHandler);

        this.add(mainBodyLayout);
        mainBodyLayout.setStyleName("progress");

    }
    public void setResourceUrl(String url){
         link.setHTML("<a href=\" "+url+"\"style=\"color: black;" +"\" download > Save </a>");
        
    }

}
