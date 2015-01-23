/*used
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.unused;

import com.google.gwt.user.client.ui.Label;

/**
 *
 * @author Yehia Farag main function buttons layout
 */
public class ButtonsBarMenuComponent {

    private final Label somClustBtn, profilePlotBtn, pcaBtn, rankBtn, createGroupBtn, actGroupBtn, exportGroupBtn, saveBtn;
//    private final Label somClustLabel, profilePlotLabel, pcaLabel, rankLabel, createGroupLabel, actGroupLabel, exportGroupLabel, saveLabel;

    public Label getSaveBtn() {
        return saveBtn;
    }

    public Label getSomClustBtn() {
        return somClustBtn;
    }

    public Label getProfilePlotBtn() {
        return profilePlotBtn;
    }

    public Label getPcaBtn() {
        return pcaBtn;
    }

    public Label getRankBtn() {
        return rankBtn;
    }

    public Label getCreateGroupBtn() {
        return createGroupBtn;
    }

    public Label getActGroupBtn() {
        return actGroupBtn;
    }

    public Label getExportGroupBtn() {
        return exportGroupBtn;
    }

    public ButtonsBarMenuComponent() {

        somClustBtn = generateLabel("Hierarchical Clustering");

        profilePlotBtn = generateLabel("Profile Plot");

        pcaBtn = generateLabel("PCA");

        rankBtn = generateLabel(" Rank Product ");

        createGroupBtn = generateLabel("Create Groups/Datasets");

        actGroupBtn = generateLabel("Create Groups/Datasets");

        exportGroupBtn = generateLabel("Export");

        saveBtn = generateLabel("Save");
        
        
////        
////        somClustLabel = generateLabel("Hierarchical Clustering");
////
////        profilePlotLabel= generateLabel("Profile Plot");
////
////        pcaLabel = generateLabel("PCA");
////
////        rankLabel = generateLabel("Rank Product");
////
////        createGroupLabel = generateLabel("Create Groups/Datasets");
////
////        actGroupLabel = generateLabel("Create Groups/Datasets");
////
////        exportGroupLabel = generateLabel("Export");
////
////        saveLabel = generateLabel("Save");

    }

    private Label generateLabel(String btnName) {
        final Label btn = new Label(btnName);        
        btn.addStyleName("clickable");
        btn.setHeight("20px");
        btn.setWidth((btnName.length() * 6) + "px");
        return btn;
    }
    
//
//    public Label getSomClustLabel() {
//        return somClustLabel;
//    }
//
//    public Label getProfilePlotLabel() {
//        return profilePlotLabel;
//    }
//
//    public Label getPcaLabel() {
//        return pcaLabel;
//    }
//
//    public Label getRankLabel() {
//        return rankLabel;
//    }
//
//    public Label getCreateGroupLabel() {
//        return createGroupLabel;
//    }
//
//    public Label getActGroupLabel() {
//        return actGroupLabel;
//    }
//
//    public Label getExportGroupLabel() {
//        return exportGroupLabel;
//    }
//
//    public Label getSaveLabel() {
//        return saveLabel;
//    }
    
//    private void initFunctionsListeners(final String str,final Label btn){
//         com.google.gwt.event.dom.client.ClickHandler handler = new com.google.gwt.event.dom.client.ClickHandler() {
//
//            @Override
//            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
//              if(str.equalsIgnoreCase("Hierarchical Clustering")){
//                 
//                       final SomClustPanel somClustPanel = new SomClustPanel();
//                        ClickHandler somClustClickhandler = new ClickHandler() {
//                            @Override
//                            public void onClick(ClickEvent event) {
//                                runSomClustering(datasetId, somClustPanel.getX(), somClustPanel.getY());
//                                somClustPanel.hide();
//
//                            }
//                        };
//                        if (somclustHandlerRegistration != null) {
//                            somclustHandlerRegistration.removeHandler();
//                        }
//                        somclustHandlerRegistration = somClustPanel.getOkBtn().addClickHandler(somClustClickhandler);
//                    }
//                    somClustPanel.show();
//              
//
//            }
//        };
//         btn.addClickHandler(handler);
//    
//    }
    


}
