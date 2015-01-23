/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.unused;

//import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.user.client.ui.Image;
//import com.google.gwt.user.client.ui.Label;
//import com.smartgwt.client.types.Alignment;
//import com.smartgwt.client.widgets.layout.HLayout;
//import com.smartgwt.client.widgets.layout.VLayout;
//import web.diva.client.DivaServiceAsync;
//import web.diva.client.selectionmanager.ModularizedListener;
//import web.diva.client.selectionmanager.Selection;
//import web.diva.client.selectionmanager.SelectionManager;
//import web.diva.shared.beans.SomClustTreeSelectionUpdate;
//import web.diva.shared.beans.SomClusteringResult;
//
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.ui.VerticalPanel;
//import java.util.Arrays;
//import web.diva.client.somclust.view.HeatmapImg;
//import web.diva.client.somclust.view.TreeImg;

/**
 *
 * @author Yehia Farag clustering component wrapper viewer
 */
public class SomClustView {//extends ModularizedListener {

//    @Override
//    public String toString() {
//        return "SomClustView"; //To change body of generated methods, choose Tools | Templates.
//    }
//
//    private SelectionManager selectionManager;
//    private final String defaultSideTreeImgURL;
//    private String defaultTopTreeImgURL;
//    private boolean update = true;
//    private boolean clustColumn=true;
//
//    private final DivaServiceAsync greetingService;
//
//    private final TreeImg sideTreeImg;
//    private TreeImg upperTreeImg;
//    private final HeatmapImg heatMapImg;
//    private final Image scaleImg;
//    private final HLayout clusterLayout = new HLayout();
//    private final VerticalPanel clusteringPanel = new VerticalPanel();
//    private final VLayout leftSideLayout = new VLayout();
//    private final VLayout rightSideLayout = new VLayout();

//    public SomClustView(SomClusteringResult somClusteringResults, SelectionManager selectionManager, DivaServiceAsync greetingService, int width, int height, boolean clustColumn) {
//        this.greetingService = greetingService;
//        clusteringPanel.setHeight(height + "px");
//        clusteringPanel.setWidth(width + "px");
//        clusteringPanel.setBorderWidth(1);
////        clusteringPanel.setAlwaysShowScrollBars(false);
//        clusteringPanel.setStyleName("clusterLayout");
//        defaultSideTreeImgURL = somClusteringResults.getSideTreeImgUrl();
//
//        sideTreeImg = new TreeImg(somClusteringResults.getSideTreeImgUrl(), somClusteringResults.getRowNode(), 2,null);//"data:sideTreeImg/png;base64,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");
//        heatMapImg = new HeatmapImg(somClusteringResults.getHeatMapImgUrl(), somClusteringResults.getRowNames(), somClusteringResults.getColNames(), somClusteringResults.getValues(),null,1);
//        scaleImg = new Image(somClusteringResults.getScaleImgUrl());
//        rightSideLayout.setAlign(Alignment.CENTER);
////        clustColumn = false;
//        this.clustColumn= clustColumn;
//        if (clustColumn) {
//            defaultTopTreeImgURL = somClusteringResults.getUpperTreeImgUrl();
//            upperTreeImg = new TreeImg(somClusteringResults.getUpperTreeImgUrl(), somClusteringResults.getColNode(), 1,null);
//            upperTreeImg.setHeight("70px");
//            Label spacer = new Label("");
//            spacer.setHeight(upperTreeImg.getHeight() + "px");
//            spacer.setWidth(sideTreeImg.getWidth() + "px");
//            leftSideLayout.addMember(spacer);
////            upperTreeImg.addClickHandler(new ClickHandler() {
////                @Override
////                public void onClick(ClickEvent event) {
////                    int y = (int) (event.getY());
////                    int x = ((int) (event.getX()));
////                    if (upperTreeImg.isSelectedNode()) {
////                        updateUpperTreeSelection(x, y);
////                    }
////                }
////            });
//            rightSideLayout.setWidth(upperTreeImg.getWidth());
//
//            rightSideLayout.addMember(upperTreeImg);
//        }
//
////        spacer.addStyleName("magnifying");
//        Label info1 = new Label("Distance metrics : " + somClusteringResults.getDistanceMeasure());
//        info1.setWidth(leftSideLayout.getWidth() + "px");
//        info1.setHeight("20px");
//        info1.setStyleName("info");
//        Label info2 = new Label("Linkage : " + somClusteringResults.getLinkage());
//        info2.setWidth(leftSideLayout.getWidth() + "px");
//        info2.setHeight("20px");
//        info2.setStyleName("info");
//
//        //init top layout
//        leftSideLayout.setWidth(sideTreeImg.getWidth() + "px");
//        leftSideLayout.setHeight(425);
//
//        leftSideLayout.addMember(sideTreeImg);
//        leftSideLayout.addMember(info1);
//        leftSideLayout.addMember(info2);
//
//        clusterLayout.addMember(leftSideLayout);
//
////        spacer.addClickHandler(new ClickHandler() {
////
////            @Override
////            public void onClick(ClickEvent event) {
////                final Window winModal = new Window();
////                winModal.setStyleName("testo");
////                winModal.setWidth("50%");
////                winModal.setHeight("70%");
////                winModal.setTitle("Hierarchical Clustering Panel");
////                winModal.setShowMinimizeButton(false);
////                winModal.setShowCloseButton(true);
////                winModal.setOverflow(Overflow.SCROLL);
////                winModal.setIsModal(true);
////                winModal.setShowModalMask(true);
////                winModal.centerInPage();
////                winModal.addCloseClickHandler(new CloseClickHandler() {
////                    @Override
////                    public void onCloseClick(CloseClickEvent event) {
////                        winModal.close();
////                        clusteringPanel.add(clusterLayout);
////
////                    }
////                });
////                winModal.show();
////            }
////        });
//        //init middle layout   
//        rightSideLayout.addMember(heatMapImg);
//        rightSideLayout.addMember(scaleImg);
//        clusterLayout.addMember(rightSideLayout);
//        clusteringPanel.add(clusterLayout);
////        sideTreeImg.addClickHandler(new ClickHandler() {
////            @Override
////            public void onClick(ClickEvent event) {
////                int y = (int) (event.getY());
////                int x = ((int) (event.getX()));
////                if (sideTreeImg.isSelectedNode()) {
////                    updateSideTreeSelection(x, y);
////                }
////
////            }
////
////        });
//
//        this.selectionManager = selectionManager;
//        this.selectionManager.addSelectionChangeListener(SomClustView.this);
//        somClusteringResults = null;
//    }

//    public VerticalPanel componentView() {
//        return clusteringPanel;
//    }
//
//    @Override
//    public void selectionChanged(Selection.TYPE type) {
//        if (type == Selection.TYPE.OF_ROWS) {
//            Selection sel = selectionManager.getSelectedRows();
//            if (sel != null) {
//                int[] selectedRows = sel.getMembers();
//                if (selectedRows != null && update) {
//                    clearRowSelection();
//                }
//            }
//        } else {
//            Selection sel = selectionManager.getSelectedColumns();
//            if (sel != null) {
//                int[] selectedColumns = sel.getMembers();
//                if (selectedColumns != null && update) {
//                    clearColSelection();
//                }
//            }
//
//        }
//    }
//
//    public void clearRowSelection() {
////        sideTreeImg.setUrl(defaultSideTreeImgURL);
//        
//    }
//    public void clearColSelection(){
////    if(clustColumn)
////            upperTreeImg.setUrl(defaultTopTreeImgURL);    
//    }
//
//    @Override
//    public void remove() {
//        selectionManager = null;
//    }
//
//    private void updateSideTreeSelection(int x, int y) {
//         selectionManager.Busy_Task(true,false);
//        greetingService.updateSideTree(x, y, 200, (500 - 25.0), new AsyncCallback<SomClustTreeSelectionUpdate>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                 selectionManager.Busy_Task(false,false);
//            }
//
//            @Override
//            public void onSuccess(SomClustTreeSelectionUpdate result) {
////                sideTreeImg.setUrl(result.getTreeImgUrl());
//                if (result.getSelectedIndices() != null) {
//                    update = false;
//                    Selection selection = new Selection(Selection.TYPE.OF_ROWS, result.getSelectedIndices());
//                    selectionManager.setSelectedRows(selection);
//                    update = true;
//                }
//                selectionManager.Busy_Task(false,false);
//            }
//        });
//    }
//
//    private void updateUpperTreeSelection(int x, int y) {
//        selectionManager.Busy_Task(true,false);
//        greetingService.updateUpperTree(x, y, 70, (500 - 25.0), new AsyncCallback<SomClustTreeSelectionUpdate>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                selectionManager.Busy_Task(false,false);
//            }
//
//            @Override
//            public void onSuccess(SomClustTreeSelectionUpdate result) {
////                upperTreeImg.setUrl(result.getTreeImgUrl());//          
//                if (result.getSelectedIndices() != null) {
//                    update = false;
//                    Selection selection = new Selection(Selection.TYPE.OF_COLUMNS, result.getSelectedIndices());
//                    selectionManager.setSelectedColumns(selection);
//                    update = true;
//                }
//                selectionManager.Busy_Task(false,false);
//            }
//        });
//    }

}
