/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author Yehia Farag
 * initialize main layout images
 */
public class InitImgs {

    public Image getGtImg() {
        return gtImg;
    }
    private final Image gtImg, rtImg, pcaImg, lCImg, hcImg;

    public InitImgs() {
        gtImg = new Image("images/gt.png");
        gtImg.setHeight("580px");
        gtImg.setWidth("" + RootPanel.get("geneTable").getOffsetWidth() + "px");

        lCImg = new Image("images/clear1.png");
        lCImg.setHeight("300px");
        lCImg.setWidth("" + RootPanel.get("LineChartResults").getOffsetWidth() + "px");

        pcaImg = new Image("images/clear2.png");
        pcaImg.setHeight("300px");
        pcaImg.setWidth("" + RootPanel.get("LineChartResults").getOffsetWidth() + "px");

        rtImg = new Image("images/rt.png");
        rtImg.setHeight("270px");
        rtImg.setWidth("" + RootPanel.get("RankTablesResults").getOffsetWidth() + "px");

        hcImg = new Image("images/hc.png");
        hcImg.setHeight("575px");
        hcImg.setWidth("" + (RootPanel.get("SomClusteringResults").getOffsetWidth() - 26) + "px");

    }

    public Image getHcImg() {
        return hcImg;
    }

    public Image getRtImg() {
        return rtImg;
    }

    public Image getPcaImg() {
        return pcaImg;
    }

    public Image getlCImg() {
        return lCImg;
    }

}
