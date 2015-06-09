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
public class SomClustTreeSelectionResult implements IsSerializable{
    
    private SplitedImg treeImg;
    private int[] selectedIndices;
    private String treeImg1Url;

    public String getTreeImg1Url() {
        return treeImg1Url;
    }

    public void setTreeImg1Url(String treeImg1Url) {
        this.treeImg1Url = treeImg1Url;
    }

    

   

    public int[] getSelectedIndices() {
        return selectedIndices;
    }

    public void setSelectedIndices(int[] selectedIndices) {
        this.selectedIndices = selectedIndices;
    }

    public SplitedImg getTreeImg() {
        return treeImg;
    }

    public void setTreeImg(SplitedImg treeImg) {
        this.treeImg = treeImg;
    }

  

   
    
    
   
    
}
