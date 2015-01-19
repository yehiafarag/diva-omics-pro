/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.somclust.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author y-mok_000
 */
public class UpdatedSomClustPanel extends PopupPanel {

    private final IButton processBtn;
    private final VLayout mainBodyLayout;

    private final ListBox linkage;
    private final ListBox distanceMeasure;
    
    private final CheckBox clusterColumns ;

    public UpdatedSomClustPanel() {
        this.setAnimationEnabled(true);
        this.ensureDebugId("cwBasicPopup-imagePopup");
        this.setModal(false);
        mainBodyLayout = new VLayout();
        mainBodyLayout.setWidth(300);
        mainBodyLayout.setHeight(150);

        HorizontalPanel topLayout = new HorizontalPanel();
        topLayout.setWidth("300px");
        topLayout.setHeight("20px");
        topLayout.setSpacing(3);
        mainBodyLayout.addMember(topLayout);
        Label title = new Label("Hierarchical Clustering Settings");
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth(200 + "px");
        title.setHeight("18px");
        topLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);
        topLayout.setCellVerticalAlignment(title, HorizontalPanel.ALIGN_TOP);

        Label closeBtn = new Label();

        closeBtn.addStyleName("close");
        closeBtn.setHeight("16px");
        closeBtn.setWidth("16px");

        closeBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });

        topLayout.add(closeBtn);
        topLayout.setCellHorizontalAlignment(closeBtn, HorizontalPanel.ALIGN_RIGHT);
        topLayout.setCellVerticalAlignment(closeBtn, HorizontalPanel.ALIGN_TOP);

        mainBodyLayout.setMembersMargin(2);
        //main body

        HorizontalPanel linkageLayout = new HorizontalPanel();
        mainBodyLayout.addMember(linkageLayout);
        linkageLayout.setWidth("300px");
        linkageLayout.setHeight("20px");

        Label linkageTitle = new Label("LINKAGE ");
        linkageTitle.setWidth("100px");
        linkageTitle.setHeight("20px");
        linkageTitle.setStyleName("secheadertitle");
        linkageLayout.add(linkageTitle);
        linkageLayout.setCellHorizontalAlignment(linkageTitle, HorizontalPanel.ALIGN_LEFT);

        linkage = new ListBox();
        linkage.setWidth("150px");
        linkage.setHeight("20px");
        linkage.setTitle("LINKAGE");

        linkage.addItem("SINGLE");
        linkage.addItem("WPGMA");
        linkage.addItem("UPGMA");
        linkage.addItem("COMPLETE");
        linkage.setSelectedIndex(1);
        linkageLayout.add(linkage);
        linkageLayout.setCellHorizontalAlignment(linkage, HorizontalPanel.ALIGN_LEFT);

        HorizontalPanel distanceMeasureLayout = new HorizontalPanel();
        mainBodyLayout.addMember(distanceMeasureLayout);
        distanceMeasureLayout.setWidth("300px");
        distanceMeasureLayout.setHeight("20px");

        Label distanceMeasureTitle = new Label("Distance Measure");
        distanceMeasureTitle.setWidth("100px");
        distanceMeasureTitle.setHeight("20px");
        distanceMeasureTitle.setStyleName("secheadertitle");
        distanceMeasureLayout.add(distanceMeasureTitle);
        distanceMeasureLayout.setCellHorizontalAlignment(distanceMeasureTitle, HorizontalPanel.ALIGN_LEFT);
        distanceMeasure = new ListBox();
        distanceMeasure.setTitle("DISTANCE MEASURE");
        distanceMeasure.setWidth("150px");
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
        distanceMeasureLayout.add(distanceMeasure);
        distanceMeasureLayout.setCellHorizontalAlignment(distanceMeasureTitle, HorizontalPanel.ALIGN_LEFT);

        clusterColumns = new CheckBox("Cluster Columns");
        clusterColumns.setValue(true);
        clusterColumns.setHeight("20px");
        clusterColumns.setStyleName("secheadertitle");
        mainBodyLayout.addMember(clusterColumns);

        //ok button
        HLayout hlo = new HLayout();
        mainBodyLayout.addMember(hlo);
        hlo.setWidth("300px");
        hlo.setHeight("30px");
        processBtn = new IButton("Run Clustering");
        hlo.addMember(processBtn);

        hlo.setMargin(5);
        hlo.setMembersMargin(1);
        hlo.setAlign(Alignment.CENTER);

        this.setWidget(mainBodyLayout);
        mainBodyLayout.setStyleName("modalLayout");
        this.show();
        this.hide();

    }
    
     public int getLinkageValue() {
        return linkage.getSelectedIndex();
    }

    public int getDistanceMeasureValue() {
        return distanceMeasure.getSelectedIndex();
    }
 public IButton getProcessBtn() {
        return processBtn;
    }
 
 public boolean isClusterColumns(){
     return clusterColumns.getValue();
 
 }

}
