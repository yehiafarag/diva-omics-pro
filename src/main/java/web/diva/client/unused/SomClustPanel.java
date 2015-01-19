/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.unused;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author Yehia Farag
 * side-tree, top-tree, and heat-map container component
 */
public class SomClustPanel extends Window {

   

    private final ListBox linkage;
    private final ListBox distanceMeasure;
    private final IButton okBtn;

    public int getLinkageValue() {
        return linkage.getSelectedIndex();
    }

    public int getDistanceMeasureValue() {
        return distanceMeasure.getSelectedIndex();
    }

    public SomClustPanel() {
        this.setWidth("400px");
        this.setTitle("Hierarchical Clustering");
        this.setShowMinimizeButton(false);
        this.setIsModal(false);
        this.centerInPage();
        this.addCloseClickHandler(new CloseClickHandler() {
            @Override
            public void onCloseClick(CloseClickEvent event) {
                hide();
            }
        });
        this.setHeight("150px");
        VLayout vp = new VLayout();
        vp.setWidth100();
        vp.setHeight100();
        this.addItem(vp);
        HLayout hp1 = new HLayout();
        hp1.setWidth100();
        hp1.setHeight("20px");
        Label l1 = new Label("LINKAGE ");
        l1.setWidth("150px");
        l1.setHeight("20px");
        linkage = new ListBox();
        linkage.setWidth("200px");
        linkage.setHeight("20px");
        linkage.setTitle("LINKAGE");

        linkage.addItem("SINGLE");
        linkage.addItem("WPGMA");
        linkage.addItem("UPGMA");
        linkage.addItem("COMPLETE");
        linkage.setSelectedIndex(1);

        hp1.addMember(l1);
        hp1.addMember(linkage);

        vp.addMember(hp1);
        hp1.setMargin(5);
        hp1.setAlign(Alignment.LEFT);

        HLayout hp2 = new HLayout();
        hp2.setWidth100();
        hp2.setHeight("20px");
        Label l2 = new Label("DISTANCE MEASURE ");
        l2.setHeight("20px");
        l2.setWidth("150px");

        distanceMeasure = new ListBox();
        distanceMeasure.setTitle("DISTANCE MEASURE");
        distanceMeasure.setWidth("200px");
        distanceMeasure.setHeight("20px");
        distanceMeasure.addItem("Squared Euclidean");
        distanceMeasure.addItem("Euclidean");
        distanceMeasure.addItem("Bray Curtis");
        distanceMeasure.addItem("Manhattan");
        distanceMeasure.addItem("Cosine Correlation");
        distanceMeasure.addItem("Pearson Correlation");
        distanceMeasure.addItem("Uncentered Pearson Correlation");
        distanceMeasure.addItem("Euclidean (Nullweighted)");
        distanceMeasure.addItem("Camberra");
        distanceMeasure.addItem("Chebychev");
        distanceMeasure.addItem("Spearman Rank Correlation");
        distanceMeasure.setSelectedIndex(0);

        hp2.addMember(l2);
        hp2.addMember(distanceMeasure);

        vp.addMember(hp2);
        hp2.setAlign(Alignment.LEFT);
        hp2.setMargin(5);

        okBtn = new IButton("Start Clustering");
        okBtn.setWidth("200px");
        okBtn.setHeight("20px");
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

    }

//    public ListBox getLinkage() {
//        return linkage;
//    }
//
//    public ListBox getDistanceMeasure() {
//        return distanceMeasure;
//    }

    public IButton getOkBtn() {
        return okBtn;
    }

}
