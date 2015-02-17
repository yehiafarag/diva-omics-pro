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
    
    private String treeImgUrl;
    private int[] selectedIndices;

  

    public String getTreeImgUrl() {
        return treeImgUrl;
    }

    public void setTreeImgUrl(String treeImgUrl) {
        this.treeImgUrl = treeImgUrl;
    }

    public int[] getSelectedIndices() {
        return selectedIndices;
    }

    public void setSelectedIndices(int[] selectedIndices) {
        this.selectedIndices = selectedIndices;
    }

    
    
   
    
}
