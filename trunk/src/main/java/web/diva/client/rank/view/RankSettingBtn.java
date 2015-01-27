/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.rank.view;

import web.diva.client.unused.RankPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import java.util.List;
import web.diva.client.DivaServiceAsync;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.DivaGroup;

/**
 *
 * @author Yehia Farag
 */
public class RankSettingBtn extends Label{
    
    private final RankPanel rankPanel;
     private final DivaServiceAsync GWTClientService;
    public  RankSettingBtn(DivaServiceAsync GWTClientService,RankPanel rankPanel){
        this.GWTClientService=GWTClientService;
        
        this.rankPanel = rankPanel;
        this.addStyleName("settings");
        this.setHeight("16px");
        this.setWidth("16px");
        
        this.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                updateAndViewRankPanel();               
            }
        });

    }
    public void hidePanel(){
    rankPanel.getPopupPanel().hide();
    }
      public void showPanel(){
          rankPanel.getPopupPanel().center();
    rankPanel.getPopupPanel().show();
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
        public List<String> getSelectColGroups(){
            return rankPanel.getSelectColGroups();
        }
        
        public void setErrorlablVisible(boolean visible){
            rankPanel.getErrorlabl().setVisible(visible);
        }
        public void rankPanelvalidate(){
        rankPanel.getForm2().validate();
        }
         public boolean isDefaultRank() {
        return rankPanel.isDefaultRank();
    }

    public RankPanel getRankPanel() {
        return rankPanel;
    }
      private void updateAndViewRankPanel() {
        SelectionManager.Busy_Task(true,true);
        GWTClientService.getColGroups(new AsyncCallback<List<DivaGroup>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("An error occurred while attempting to contact the server");
//                        init=false;
                SelectionManager.Busy_Task(false,true);
            }

            @Override
            public void onSuccess(List<DivaGroup> result) {

                getRankPanel().updateData(result);
                 getRankPanel().getPopupPanel().show();
                getRankPanel().getPopupPanel().center();
                SelectionManager.Busy_Task(false,true);

            }
        });

    }
    
}
