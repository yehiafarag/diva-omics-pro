/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package web.diva.shared.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Yehia Farag
 */
public class SomClusteringResult implements IsSerializable{
    
    private String sideTreeImgUrl;
    private String upperTreeImgUrl;
    private String heatMapImgUrl;
    private InteractiveColumnsResults interactiveRowImgUrl;
    
    
    
    private String scaleImgUrl;
    private String linkage;
    private String distanceMeasure;
    private ClientClusterNode colNode;
    private ClientClusterNode rowNode;
    
    private double[][] values;
    
    private  String[] rowNames;
    private  String[] colNames;
    
    private int topTreeWidth;
    private int sideTreeWidth;
    private int topTreeHeight;
    private int sideTreeHeight;
    
    private int squareL;
     private int squareW;

    public String getSideTreeImgUrl() {
        return sideTreeImgUrl;
    }

    public void setSideTreeImgUrl(String sideTreeImgUrl) {
        this.sideTreeImgUrl = sideTreeImgUrl;
    }

    public String getUpperTreeImgUrl() {
        return upperTreeImgUrl;
    }

    public void setUpperTreeImgUrl(String upperTreeImgUrl) {
        this.upperTreeImgUrl = upperTreeImgUrl;
    }

    public String getHeatMapImgUrl() {
        return heatMapImgUrl;
    }

    public void setHeatMapImgUrl(String heatMapImgUrl) {
        this.heatMapImgUrl = heatMapImgUrl;
    }

    public String getScaleImgUrl() {
        return scaleImgUrl;
    }

    public void setScaleImgUrl(String scaleImgUrl) {
        this.scaleImgUrl = scaleImgUrl;
    }

    public String getLinkage() {
        return linkage;
    }

    public void setLinkage(String linkage) {
        this.linkage = linkage;
    }

    public String getDistanceMeasure() {
        return distanceMeasure;
    }

    public void setDistanceMeasure(String distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    public ClientClusterNode getColNode() {
        return colNode;
    }

    public void setColNode(ClientClusterNode colNode) {
        this.colNode = colNode;
    }

    public ClientClusterNode getRowNode() {
        return rowNode;
    }

    public void setRowNode(ClientClusterNode rowNode) {
        this.rowNode = rowNode;
    }

    public String[] getRowNames() {
        return rowNames;
    }

    public void setRowNames(String[] rowNames) {
        this.rowNames = rowNames;
    }

    public String[] getColNames() {
        return colNames;
    }

    public void setColNames(String[] colNames) {
        this.colNames = colNames;
    }

    public double[][] getValues() {
        return values;
    }

    public void setValues(double[][] values) {
        this.values = values;
    }

    public int getTopTreeWidth() {
        return topTreeWidth;
    }

    public void setTopTreeWidth(int topTreeWidth) {
        this.topTreeWidth = topTreeWidth;
    }

    public int getSideTreeWidth() {
        return sideTreeWidth;
    }

    public void setSideTreeWidth(int sideTreeWidth) {
        this.sideTreeWidth = sideTreeWidth;
    }

    public int getTopTreeHeight() {
        return topTreeHeight;
    }

    public void setTopTreeHeight(int topTreeHeight) {
        this.topTreeHeight = topTreeHeight;
    }

    public int getSideTreeHeight() {
        return sideTreeHeight;
    }

    public void setSideTreeHeight(int sideTreeHeight) {
        this.sideTreeHeight = sideTreeHeight;
    }

    public InteractiveColumnsResults getInteractiveColumnImgUrl() {
        return interactiveRowImgUrl;
    }

    public void setInteractiveRowImgUrl(InteractiveColumnsResults interactiveRowImgUrl) {
        this.interactiveRowImgUrl = interactiveRowImgUrl;
    }

    public int getSquareL() {
        return squareL;
    }

    public void setSquareL(int squareL) {
        this.squareL = squareL;
    }

    public int getSquareW() {
        return squareW;
    }

    public void setSquareW(int squareW) {
        this.squareW = squareW;
    }


 
    
}
