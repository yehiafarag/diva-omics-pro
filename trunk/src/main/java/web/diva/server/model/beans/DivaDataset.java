/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model.beans;

import java.io.Serializable;
import java.util.Map;
import no.uib.jexpress_modularized.core.dataset.Dataset;

/**
 *
 * @author Yehia Farag
 */
public class DivaDataset extends Dataset implements Serializable {

    private Map<Integer, String> geneIndexNameMap;
    private Map<String, Integer> geneNameIndexMap;
//    private String[] geneColorArr;
    private String[] geneNamesArr;
//    private Map<Integer, Number[]> membersMap;
    private Number[][] lineChartPointArr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    private int id;

//    public Map<Integer, Number[]> getMembersMap() {
//        return membersMap;
//    }

//    public void setMembersMap(Map<Integer, Number[]> membersMap) {
//        this.membersMap = membersMap;
//    }

    public DivaDataset(double[][] data, String[] names, String[] colnames) {
        super(data, names, colnames);
    }

    public Map<Integer, String> getGeneIndexNameMap() {
        return geneIndexNameMap;
    }

    public void setGeneIndexNameMap(Map<Integer, String> geneIndexNameMap) {
        this.geneIndexNameMap = geneIndexNameMap;
    }

    public Map<String, Integer> getGeneNameIndexMap() {
        return geneNameIndexMap;
    }

    public void setGeneNameIndexMap(Map<String, Integer> geneNameIndexMap) {
        this.geneNameIndexMap = geneNameIndexMap;
    }

//    public String[] getGeneColorArr() {
//        return geneColorArr;
//    }
//
//    public void setGeneColorArr(String[] geneColorArr) {
//        this.geneColorArr = geneColorArr;
//    }

    public String[] getGeneNamesArr() {
        return geneNamesArr;
    }

    public void setGeneNamesArr(String[] geneNamesArr) {
        this.geneNamesArr = geneNamesArr;
    }

    public Number[][] getLineChartPointArr() {
        return lineChartPointArr;
    }

    public void setLineChartPointArr(Number[][] lineChartPointArr) {
        this.lineChartPointArr = lineChartPointArr;
    }

}
