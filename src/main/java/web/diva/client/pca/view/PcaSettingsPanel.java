/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.pca.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;

/**
 *
 * @author Yehia Farag
 */
public class PcaSettingsPanel extends PopupPanel {

    private final IButton okBtn;

    public IButton getOkBtn() {
        return okBtn;
    }
    private final ListBox pcaI;
    private final ListBox pcaII;

    public PcaSettingsPanel(String[] pcaLabelData) {

        this.setAnimationEnabled(true);
        this.ensureDebugId("cwBasicPopup-imagePopup");
        this.setModal(true);

        VerticalPanel framLayout = new VerticalPanel();
       framLayout.setWidth("400px");
        framLayout.setHeight("150px");

        VerticalPanel mainBodyLayout = new VerticalPanel();

        mainBodyLayout.setWidth("398px");
        mainBodyLayout.setHeight("150px");
          mainBodyLayout.setStyleName("modalPanelBody");
          mainBodyLayout.setSpacing(3);

        HorizontalPanel topLayout = new HorizontalPanel();
        topLayout.setWidth(398+"px");
        topLayout.setHeight(20+"px");

        Label title = new Label("Principal Component Analysis Settings");
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth(250 + "px");
        topLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);
        topLayout.setCellVerticalAlignment(title, HorizontalPanel.ALIGN_TOP);

        Label closeBtn = new Label();

        closeBtn.addStyleName("close");
        closeBtn.setHeight("16px");
        closeBtn.setWidth("16px");

        closeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                hide(true);
            }
        });

        topLayout.add(closeBtn);        
        topLayout.setCellHorizontalAlignment(closeBtn, HorizontalPanel.ALIGN_RIGHT);
        topLayout.setCellVerticalAlignment(closeBtn, HorizontalPanel.ALIGN_TOP);

        pcaI = new ListBox();
        pcaI.setWidth("250px");
        pcaI.setHeight("25px");
        pcaI.setTitle("X AXES");
      
        HorizontalPanel hp1 = new HorizontalPanel();
        hp1.setWidth("398px");
        hp1.setHeight("30px");
        Label l1 = new Label("X AXES ");
        l1.setHeight("25px");
        l1.setWidth("100px");

        hp1.add(l1);
        hp1.add(pcaI);
          hp1.setCellHorizontalAlignment(l1, HorizontalPanel.ALIGN_CENTER);
        hp1.setCellVerticalAlignment(l1, HorizontalPanel.ALIGN_BOTTOM);
          hp1.setCellHorizontalAlignment(pcaI, HorizontalPanel.ALIGN_LEFT);
        hp1.setCellVerticalAlignment(pcaI, HorizontalPanel.ALIGN_BOTTOM);
//        hp1.setMargin(5);

        mainBodyLayout.add(hp1);
//        hp1.setAlign(Alignment.LEFT);

        pcaII = new ListBox();
        pcaII.setWidth("250px");
        pcaII.setHeight("25px");
        pcaII.setTitle("Y AXES");

        HorizontalPanel hp2 = new HorizontalPanel();
        hp2.setWidth("398px");
        hp2.setHeight("30px");
        Label l2 = new Label("Y AXES ");
        l2.setHeight("25px");
        l2.setWidth("100px");

        hp2.add(l2);
        hp2.add(pcaII);
   hp2.setCellHorizontalAlignment(l2, HorizontalPanel.ALIGN_CENTER);
        hp2.setCellVerticalAlignment(l2, HorizontalPanel.ALIGN_BOTTOM);
          hp2.setCellHorizontalAlignment(pcaII, HorizontalPanel.ALIGN_LEFT);
        hp2.setCellVerticalAlignment(pcaII, HorizontalPanel.ALIGN_BOTTOM);
        mainBodyLayout.add(hp2);
        
        
        for (String str: pcaLabelData) {
            pcaI.addItem(str);
            pcaII.addItem(str);
        }
        pcaI.setSelectedIndex(0);
        pcaII.setSelectedIndex(1);

        okBtn = new IButton("Start Process");
        okBtn.setWidth("200px");
        okBtn.setAlign(Alignment.CENTER);
        okBtn.setShowRollOver(true);
        okBtn.setShowDown(true);
        okBtn.setTitleStyle("stretchTitle");
       
         HorizontalPanel bottomLayout = new HorizontalPanel();
        bottomLayout.setWidth(400+"px");
        bottomLayout.setHeight(30+"px");
        
        bottomLayout.add(okBtn);

        bottomLayout.setCellHorizontalAlignment(okBtn, VerticalPanel.ALIGN_CENTER);
            bottomLayout.setCellVerticalAlignment(okBtn, VerticalPanel.ALIGN_MIDDLE);
        mainBodyLayout.add(bottomLayout);



        framLayout.add(topLayout);
        framLayout.add(mainBodyLayout);
           this.setWidget(framLayout);
           framLayout.setStyleName("modalPanelLayout");

    }

    public int getPcaI() {
        return pcaI.getSelectedIndex();
    }

    public int getPcaII() {
        return pcaII.getSelectedIndex();
    }

    public void setClickListener(com.smartgwt.client.widgets.events.ClickHandler handler) {
        getOkBtn().addClickHandler(handler);
    }

}
