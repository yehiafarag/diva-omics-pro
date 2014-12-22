/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.rank.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import java.util.List;
import web.diva.client.GreetingServiceAsync;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.DivaGroup;

/**
 *
 * @author Yehia Farag
 */
public class UpdatedRankBtn extends Label{
    
    private final RankPanel rankPanel;
     private final GreetingServiceAsync GWTClientService;
 private final  SelectionManager selectionManager;
    public  UpdatedRankBtn(SelectionManager selectionManager,GreetingServiceAsync GWTClientService){
        this.selectionManager = selectionManager;
        this.GWTClientService=GWTClientService;
        
        rankPanel = new RankPanel();
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
        public List<String> getSelectColGroups(){
            return rankPanel.getSelectColGroups();
        }
        
        public void setErrorlablVisible(boolean visible){
            rankPanel.getErrorlabl().setVisible(visible);
        }
        public void rankPanelvalidate(){
        rankPanel.getForm2().validate();
        }

    public RankPanel getRankPanel() {
        return rankPanel;
    }
      private void updateAndViewRankPanel() {
        selectionManager.busyTask(true,true);
        GWTClientService.getColGroups(new AsyncCallback<List<DivaGroup>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("An error occurred while attempting to contact the server");
//                        init=false;
                selectionManager.busyTask(false,true);
            }

            @Override
            public void onSuccess(List<DivaGroup> result) {

                getRankPanel().updateData(result);
                 getRankPanel().show();
                getRankPanel().center();
                 selectionManager.busyTask(false,true);

            }
        });

    }
    
}
