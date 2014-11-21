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
public class UpdatedTooltip implements IsSerializable{
    private double[][] points;
    private String[]  rowIds;
    private int plotLeft;
    private int plotRight;
    private int plotTop;
    private int plotBottom;
    private double minX;
    private double maxY;
    private int plotWidth;
    private int plotHeight;
    private int plotHeightArea;
    private int plotWidthArea;
    private double xUnitPix;
    private double yUnitPix;
    private int yAxisFactor;
     private int xAxisFactor;

    public double[][] getPoints() {
        return points;
    }

    public void setPoints(double[][] points) {
        this.points = points;
    }

    public String[] getRowIds() {
        return rowIds;
    }

    public void setRowIds(String[] rowIds) {
        this.rowIds = rowIds;
    }

    

    public int getPlotRight() {
        return plotRight;
    }

    public void setPlotRight(int plotRight) {
        this.plotRight = plotRight;
    }

    public int getPlotTop() {
        return plotTop;
    }

    public void setPlotTop(int plotTop) {
        this.plotTop = plotTop;
    }

    public int getPlotBottom() {
        return plotBottom;
    }

    public void setPlotBottom(int plotBottom) {
        this.plotBottom = plotBottom;
    }

    

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    

    public int getPlotWidth() {
        return plotWidth;
    }

    public void setPlotWidth(int plotWidth) {
        this.plotWidth = plotWidth;
    }

    public int getPlotHeight() {
        return plotHeight;
    }

    public void setPlotHeight(int plotHeight) {
        this.plotHeight = plotHeight;
    }

    public int getPlotHeightArea() {
        return plotHeightArea;
    }

    public void setPlotHeightArea(int plotHeightArea) {
        this.plotHeightArea = plotHeightArea;
    }

    public int getPlotWidthArea() {
        return plotWidthArea;
    }

    public void setPlotWidthArea(int plotWidthArea) {
        this.plotWidthArea = plotWidthArea;
    }

    public int getPlotLeft() {
        return plotLeft;
    }

    public void setPlotLeft(int plotLeft) {
        this.plotLeft = plotLeft;
    }

    public double getxUnitPix() {
        return xUnitPix;
    }

    public void setxUnitPix(double xUnitPix) {
        this.xUnitPix = xUnitPix;
    }

    public double getyUnitPix() {
        return yUnitPix;
    }

    public void setyUnitPix(double yUnitPix) {
        this.yUnitPix = yUnitPix;
    }

    public int getyAxisFactor() {
        return yAxisFactor;
    }

    public void setyAxisFactor(int yAxisFactor) {
        this.yAxisFactor = yAxisFactor;
    }

    public int getxAxisFactor() {
        return xAxisFactor;
    }

    public void setxAxisFactor(int xAxisFactor) {
        this.xAxisFactor = xAxisFactor;
    }
    
}
