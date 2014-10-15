/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.unused;

import web.diva.client.unused.SideTreeGraph;
import web.diva.client.unused.HeatMapGraph;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.ArrayList;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.HeatMapImageResult;
import web.diva.shared.beans.SomClusteringResult;
//import web.diva.shared.unused.SomClusteringResult;

/**
 *
 * @author Yehia Farag
 */
public class SomClustComp extends HLayout {

    private SideTreeGraph sideTree;
    private TopTreeGraph topTree;
    private ArrayList<String> indexer;
    private ArrayList<String> colIndexer;
    private final int width;
    private VerticalPanel vp;

    public double getCurrentWidth() {
        return width;
    }

    public double getCurrentHeight() {
        return height;
    }

    private final double height = 550;
    private double sideTreeHeight;

    public ArrayList<String> getIndexer() {
        return indexer;
    }

    public SomClustComp(SomClusteringResult results, SelectionManager selectionManager) {
        this.setShowDragPlaceHolder(false);
        this.setBorder("1px solid black");
        width = (RootPanel.get("SomClusteringResults").getOffsetWidth() - 50);
        this.setWidth(width);
        Image img = new Image(results.getSideTreeImgUrl());
        VerticalPanel v = new VerticalPanel();
        v.setWidth(width+"px");
        this.addMember(v);
        v.add(img);
//        double topTreeHeight = (double) results.getColsNames().length * 2.0;
//
//        sideTreeHeight = ((double) results.getGeneNames().length * 1.5);
//        if (sideTreeHeight > 4000.0) {
//            sideTreeHeight = 4000.0;
//        }
//        sideTree = new SideTreeGraph(results, "left", selectionManager, sideTreeHeight, (width), (topTreeHeight + 15.0));
//        this.addMember(sideTree.asWidget());
//        this.indexer = sideTree.getIndexers();
//        vp = new VerticalPanel();
//        vp.setHeight((sideTreeHeight + topTreeHeight + 30.0 + 10.0) + "px");
//        this.addMember(vp);
//
//        topTree = new TopTreeGraph(results, "top", selectionManager, topTreeHeight, (width));
//        vp.add(topTree.asWidget());
//        vp.setSpacing(0);
//        this.colIndexer = topTree.getIndexers();

    }

    public void setImage(final HeatMapImageResult imageResult) {
        HeatMapGraph hmg = new HeatMapGraph(imageResult, (width), sideTreeHeight);
        vp.add(hmg);
        colIndexer = null;
        indexer = null;
        sideTree.clearIndexer();
        topTree.clearIndexer();

    }

    public ArrayList<String> getColIndexer() {
        return colIndexer;
    }

    public void clearSelection() {
//        sideTree.clearSelection();
//        topTree.clearSelection();
    }

    public void remove() {
        sideTree = null;
        topTree = null;
        indexer = null;
        colIndexer = null;
        vp = null;

    }
}
