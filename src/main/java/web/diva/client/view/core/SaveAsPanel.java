/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 * @author Yehia Farag
 */
public class SaveAsPanel extends PopupPanel {

    private final  HTML downloadLink,openLink;
    HandlerRegistration reg1,reg2;
    public SaveAsPanel(String fileType, final String url) {
        this.setAnimationEnabled(true);
        this.ensureDebugId("cwBasicPopup-imagePopup");
        this.setModal(false);
        
        VerticalPanel framLayout = new VerticalPanel();
        framLayout.setWidth("300px");
        framLayout.setHeight("100px");

        VerticalPanel mainBodyLayout = new VerticalPanel();
        mainBodyLayout.setWidth("298px");
        mainBodyLayout.setHeight("128px");
        
        mainBodyLayout.setStyleName("modalPanelBody");
       
          HorizontalPanel topLayout = new HorizontalPanel();          
        topLayout.setWidth("300px");
        topLayout.setHeight("20px");
        Label title = new Label("Save "+fileType);
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth(150 + "px");

        Label closeBtn = new Label();

        closeBtn.addStyleName("close");
        closeBtn.setHeight("16px");
        closeBtn.setWidth("16px");

        
        topLayout.add(closeBtn);
        
        topLayout.setCellVerticalAlignment(closeBtn, HorizontalPanel.ALIGN_TOP);
        topLayout.setCellHorizontalAlignment(closeBtn, HorizontalPanel.ALIGN_RIGHT);
        
        topLayout.setCellVerticalAlignment(closeBtn, HorizontalPanel.ALIGN_TOP);
        topLayout.setCellHorizontalAlignment(closeBtn, HorizontalPanel.ALIGN_RIGHT);
        
        final HorizontalPanel btnsLayout = new HorizontalPanel();
        btnsLayout.setWidth("298px");
        btnsLayout.setHeight("30px");
        mainBodyLayout.add(btnsLayout);
        mainBodyLayout.setCellVerticalAlignment(btnsLayout, VerticalPanel.ALIGN_MIDDLE);
        mainBodyLayout.setCellHorizontalAlignment(btnsLayout, VerticalPanel.ALIGN_CENTER);

//        downloadLink = new HTML("<a href='"+url+"'style='color: black;'target='_blank' download> Save As</a>");
        downloadLink = new HTML(new SafeHtml() {

            @Override
            public String asString() {
                return "<a href='" + url + "'style='color: black;'target='_blank' download> Save As</a>";
            }
        });
        downloadLink.setStyleName("buttonRounded");
        downloadLink.setWidth("80px");
        btnsLayout.add(downloadLink);
        btnsLayout.setCellVerticalAlignment(downloadLink, HorizontalPanel.ALIGN_MIDDLE);
        btnsLayout.setCellHorizontalAlignment(downloadLink, HorizontalPanel.ALIGN_CENTER);

        openLink = new HTML(new SafeHtml() {

            @Override
            public String asString() {
                return ("<a href='" + url + "'style='color: black;'target='_blank' > Open</a>");
            }
        });
        openLink.setStyleName("buttonRounded");
        openLink.setWidth("80px");
        btnsLayout.add(openLink);
        btnsLayout.setCellVerticalAlignment(openLink, HorizontalPanel.ALIGN_MIDDLE);
        btnsLayout.setCellHorizontalAlignment(openLink, HorizontalPanel.ALIGN_CENTER);

        ClickHandler hideHandler = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                reg1.removeHandler();
                reg2.removeHandler();
                clear();                
                hide(true);

            }
        }; 
       reg1 =downloadLink.addClickHandler(hideHandler);
       reg2= openLink.addClickHandler(hideHandler);
        closeBtn.addClickHandler(hideHandler);

        framLayout.add(topLayout);
        framLayout.add(mainBodyLayout);
           this.setWidget(framLayout);
        framLayout.setStyleName("modalPanelLayout");

    }
    public void setResourceUrl(String url){
         downloadLink.setHTML("<a href='"+url+"'style='color: black;' download> Save As</a>");
         openLink.setHTML("<a href='"+url+"'style='color: black;'target='_blank' > Open</a>");
       
         
        
    }

}
