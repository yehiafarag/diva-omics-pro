/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.unused;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import java.util.List;
import web.diva.shared.beans.DivaGroup;

/**
 *
 * @author Yehia Farag
 */
public class RankBtn extends Label{
    
    private final RankPanel rankPanel;
    public  RankBtn(List<DivaGroup> colGroupsList){
        super(" Rank Product ");
        rankPanel = new RankPanel();
        this.addStyleName("clickable");
        this.setHeight("20px");
        this.setWidth(" Rank Product ".length() * 6 + "px");
        this.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                rankPanel.center();
                rankPanel.show();
            }
        });

    }
    public void hidePanel(){
    rankPanel.hide();
    }
      public void showPanel(){
          
                rankPanel.center();
    rankPanel.show();
    }
      
      public void setClickListener(com.smartgwt.client.widgets.events.ClickHandler handler){
      rankPanel.getOkBtn().addClickHandler(handler);
      }
      
      public String getSeed(){
          return rankPanel.getSeed();
      }
       public String getPerm(){
          return rankPanel.getPermutation();
      }
        public String getLog2(){
          return rankPanel.getRadioGroupItem();
      }
        public List<String>  getSelectColGroups(){
            return rankPanel.getSelectColGroups();
        }
        
        public void setErrorlablVisible(boolean visible){
            rankPanel.getErrorlabl().setVisible(visible);
        }
        public void rankPanelvalidate(){
        rankPanel.getForm2().validate();
        }
    
    
}
