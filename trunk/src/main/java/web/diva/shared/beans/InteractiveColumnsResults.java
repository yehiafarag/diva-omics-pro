/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.shared.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author y-mok_000
 */
public class InteractiveColumnsResults implements IsSerializable{
    
    private String navgUrl;
    private String interactiveColumn;

    public String getNavgUrl() {
        return navgUrl;
    }

    public void setNavgUrl(String navgUrl) {
        this.navgUrl = navgUrl;
    }

    public String getInteractiveColumn() {
        return interactiveColumn;
    }

    public void setInteractiveColumn(String interactiveColumn) {
        this.interactiveColumn = interactiveColumn;
    }
    
}
