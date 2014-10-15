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
public class HeatMapImageResult implements IsSerializable {

    private String imgString;
    private int colNum;
    private int rowNum;
    private int[] geneReindex;
    private int[] colReindex;
     private String maxColour;
    private String minColour;
    private double minValue;
    private double maxValue;

    public int[] getColReindex() {
        return colReindex;
    }
    private String[] geneName;
    private String[] colNames;

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
   

    public String getImgString() {
        return imgString;
    }

    public void setImgString(String imgString) {
        this.imgString = imgString;
    }

    public int getColNum() {
        return colNum;
    }

    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

    public String getMaxColour() {
        return maxColour;
    }

    public void setMaxColour(String maxColour) {
        this.maxColour = maxColour;
    }

    public String getMinColour() {
        return minColour;
    }

    public void setMinColour(String minColour) {
        this.minColour = minColour;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public int[] getGeneReindex() {
        return geneReindex;
    }

    public void setGeneReindex(int[] geneReindex) {
        this.geneReindex = geneReindex;
    }

    public String[] getGeneName() {
        return geneName;
    }

    public void setGeneName(String[] geneName) {
        this.geneName = geneName;
    }

    public String[] getColNames() {
        return colNames;
    }

    public void setColNames(String[] colNames) {
        this.colNames = colNames;
    }

    public void setColReindex(int[] colReindex) {
        this.colReindex = colReindex;
    }

}
