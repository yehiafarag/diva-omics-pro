/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.HashSet;

/**
 *
 * @author Yehia Farag
 */
public class CustomNode implements IsSerializable, Serializable {

    private String name;
    private long index;
    private HashSet<String> childernList;
    private int geneIndex;
    private double value;
    private int[] selectedNodes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public HashSet<String> getChildernList() {
        return childernList;
    }

    public void setChildernList(HashSet<String> childernList) {
        this.childernList = childernList;
    }

    public int getGeneIndex() {
        return geneIndex;
    }

    public void setGeneIndex(int geneIndex) {
        this.geneIndex = geneIndex;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(int[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

}
