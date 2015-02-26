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
public class SomClustTreeSelectionUpdate implements IsSerializable{
    
    private String treeImg1Url;
     private String treeImg2Url;
    private int[] selectedIndices;

    public String getTreeImg1Url() {
        return treeImg1Url;
    }

    public void setTreeImg1Url(String treeImg1Url) {
        this.treeImg1Url = treeImg1Url;
    }

    public String getTreeImg2Url() {
        return treeImg2Url;
    }

    public void setTreeImg2Url(String treeImg2Url) {
        this.treeImg2Url = treeImg2Url;
    }

    public int[] getSelectedIndices() {
        return selectedIndices;
    }

    public void setSelectedIndices(int[] selectedIndices) {
        this.selectedIndices = selectedIndices;
    }

  

   
    
    
   
    
}
