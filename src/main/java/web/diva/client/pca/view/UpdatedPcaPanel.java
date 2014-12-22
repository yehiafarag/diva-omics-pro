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
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
//import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author Yehia Farag
 */
public class UpdatedPcaPanel extends PopupPanel {

    private final IButton okBtn;

    public IButton getOkBtn() {
        return okBtn;
    }
    private final ListBox pcaI;
    private final ListBox pcaII;

    public UpdatedPcaPanel(int colNumber) {

        this.setAnimationEnabled(true);
        this.ensureDebugId("cwBasicPopup-imagePopup");
        this.setModal(true);

       

        VLayout mainBodyLayout = new VLayout();

        mainBodyLayout.setWidth("400px");
        mainBodyLayout.setHeight("150px");

        HorizontalPanel topLayout = new HorizontalPanel();
        topLayout.setWidth(400+"px");
        topLayout.setHeight(20+"px");
        topLayout.setSpacing(3);
        mainBodyLayout.addMember(topLayout);

        Label title = new Label("Principal Component Analysis Settings");
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth(350 + "px");
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
        pcaI.setWidth("200px");
        pcaI.setHeight("25px");
        pcaI.setTitle("X AXES");
        HLayout hp1 = new HLayout();
        hp1.setWidth(400);
        hp1.setHeight("30px");
        Label l1 = new Label("X AXES ");
        l1.setHeight("25px");
        l1.setWidth("100px");

        hp1.addMember(l1);
        hp1.addMember(pcaI);
        hp1.setMargin(5);

        mainBodyLayout.addMember(hp1);
        hp1.setAlign(Alignment.LEFT);

        pcaII = new ListBox();
        pcaII.setWidth("200px");
        pcaII.setHeight("25px");
        pcaII.setTitle("Y AXES");

        HLayout hp2 = new HLayout();
        hp2.setWidth(400);
        hp2.setHeight("30px");
        Label l2 = new Label("Y AXES ");
        l2.setHeight("25px");
        l2.setWidth("100px");

        hp2.addMember(l2);
        hp2.addMember(pcaII);
        hp2.setMargin(5);

        mainBodyLayout.addMember(hp2);
        hp2.setAlign(Alignment.LEFT);
        for (int x = 1; x <= colNumber; x++) {
            pcaI.addItem("Principal Component nr " + x);
            pcaII.addItem("Principal Component nr " + x);
        }
//        pcaI.setSelectedIndex(0);
//        pcaII.setSelectedIndex(1);

        okBtn = new IButton("Start Process");
        okBtn.setWidth("200px");
        okBtn.setAlign(Alignment.CENTER);
        okBtn.setShowRollOver(true);
        okBtn.setShowDown(true);
        okBtn.setTitleStyle("stretchTitle");
       
         HLayout bottomLayout = new HLayout();
        bottomLayout.setWidth(400+"px");
        bottomLayout.setHeight(30+"px");
        bottomLayout.setMargin(5);
        bottomLayout.setAlign(Alignment.LEFT);
        
         Label l3 = new Label(" ");
        l3.setHeight("25px");
        l3.setWidth("100px");

        bottomLayout.addMember(l3);
        bottomLayout.addMember(okBtn);
//        bottomLayout.setCellHorizontalAlignment(okBtn, HorizontalPanel.ALIGN_CENTER);
        mainBodyLayout.addMember(bottomLayout);


        this.setWidget(mainBodyLayout);
        mainBodyLayout.setStyleName("modalLayout");

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
