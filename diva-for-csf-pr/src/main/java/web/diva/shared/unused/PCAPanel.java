/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.shared.unused;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author Yehia Farag
 * pup up window for pca user input parameters 
 */
public class PCAPanel extends Window {

    private final IButton okBtn;

    public IButton getOkBtn() {
        return okBtn;
    }
    private final ListBox pcaI;
    private final ListBox pcaII;

    public PCAPanel(String[] colsNames) {
        this.setWidth("400px");
        this.setHeight("150px");
        this.setTitle("Principal component analysis");
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.centerInPage();
        this.addCloseClickHandler(new CloseClickHandler() {
            @Override
            public void onCloseClick(CloseClickEvent event) {
                hide();
            }
        });

        VLayout vp = new VLayout();
        vp.setWidth100();
        vp.setHeight100();
        this.addItem(vp);

        HLayout hp1 = new HLayout();
        hp1.setWidth100();
        hp1.setHeight("20px");
        Label l1 = new Label("X AXES ");
        l1.setHeight("20px");
        l1.setWidth("100px");
        pcaI = new ListBox();
        pcaI.setWidth("200px");
        pcaI.setHeight("20px");
        pcaI.setTitle("X AXES");

        hp1.addMember(l1);
        hp1.addMember(pcaI);
        hp1.setMargin(5);

        vp.addMember(hp1);
        hp1.setAlign(Alignment.LEFT);

        HLayout hp2 = new HLayout();
        hp2.setWidth100();
        hp2.setHeight("20px");
        Label l2 = new Label("Y AXES ");
        l2.setHeight("20px");
        l2.setWidth("100px");

        pcaII = new ListBox();
        pcaII.setWidth("200px");
        pcaII.setHeight("20px");
        pcaII.setTitle("Y AXES");

        hp2.addMember(l2);
        hp2.addMember(pcaII);
        hp2.setMargin(5);

        vp.addMember(hp2);
        hp2.setAlign(Alignment.LEFT);

        for (String str : colsNames) {
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

        HLayout btnLayout = new HLayout();
        btnLayout.setWidth100();
        btnLayout.setHeight("20px");
        btnLayout.addMember(okBtn);
        btnLayout.setAlign(Alignment.CENTER);

        vp.addMember(btnLayout);
        vp.setTop(20);
        vp.setMembersMargin(15);
        colsNames = null;

    }

    public int getPcaI() {
        return pcaI.getSelectedIndex();
    }

    public int getPcaII() {
        return pcaII.getSelectedIndex();
    }
     public void setClickListener(com.smartgwt.client.widgets.events.ClickHandler handler){
      getOkBtn().addClickHandler(handler);
      }

}
