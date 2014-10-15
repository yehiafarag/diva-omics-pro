/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.shared.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class RankResult implements IsSerializable, Serializable {

    private String[] posTableData[];
    private String[] negTableData[];
    private String[] posTableHeader;
    private String[] negTableHeader;

    private int[] posRankToIndex;
    private int[] posIndexToRank;

    private int[] negRankToIndex;
    private int[] negIndexToRank;
    private int datasetId;

    public String[][] getPosTableData() {
        return posTableData;
    }

    public void setPosTableData(String[][] posTableData) {
        this.posTableData = posTableData;
    }

    public String[][] getNegTableData() {
        return negTableData;
    }

    public void setNegTableData(String[][] negTableData) {
        this.negTableData = negTableData;
    }

    public String[] getPosTableHeader() {
        return posTableHeader;
    }

    public void setPosTableHeader(String[] posTableHeader) {
        this.posTableHeader = posTableHeader;
    }

    public String[] getNegTableHeader() {
        return negTableHeader;
    }

    public void setNegTableHeader(String[] negTableHeader) {
        this.negTableHeader = negTableHeader;
    }

    public int getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }

    public int[] getPosRankToIndex() {
        return posRankToIndex;
    }

    public void setPosRankToIndex(int[] posRankToIndex) {
        this.posRankToIndex = posRankToIndex;
    }

    public int[] getPosIndexToRank() {
        return posIndexToRank;
    }

    public void setPosIndexToRank(int[] posIndexToRank) {
        this.posIndexToRank = posIndexToRank;
    }

    public int[] getNegRankToIndex() {
        return negRankToIndex;
    }

    public void setNegRankToIndex(int[] negRankToIndex) {
        this.negRankToIndex = negRankToIndex;
    }

    public int[] getNegIndexToRank() {
        return negIndexToRank;
    }

    public void setNegIndexToRank(int[] negIndexToRank) {
        this.negIndexToRank = negIndexToRank;
    }

}
