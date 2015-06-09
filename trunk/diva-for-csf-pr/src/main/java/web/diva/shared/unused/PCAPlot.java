/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.shared.unused;
//
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.event.dom.client.MouseDownEvent;
//import com.google.gwt.event.dom.client.MouseDownHandler;
//import com.google.gwt.event.dom.client.MouseMoveEvent;
//import com.google.gwt.event.dom.client.MouseMoveHandler;
//import com.google.gwt.event.dom.client.MouseOutEvent;
//import com.google.gwt.event.dom.client.MouseOutHandler;
//import com.google.gwt.event.dom.client.MouseUpEvent;
//import com.google.gwt.event.dom.client.MouseUpHandler;
//import com.google.gwt.event.shared.HandlerRegistration;
//import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.user.client.ui.CheckBox;
//import com.google.gwt.user.client.ui.HTML;
//import com.google.gwt.user.client.ui.HasHorizontalAlignment;
//import com.google.gwt.user.client.ui.HasVerticalAlignment;
//import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.Image;
//import com.google.gwt.user.client.ui.PopupPanel;
//import com.google.gwt.user.client.ui.RootPanel;
//import com.google.gwt.user.client.ui.VerticalPanel;
//import com.smartgwt.client.widgets.IButton;
//import web.diva.client.DivaServiceAsync;
//import web.diva.client.selectionmanager.ModularizedListener;
//import web.diva.client.selectionmanager.Selection;
//import web.diva.client.selectionmanager.SelectionManager;
//import web.diva.shared.beans.PCAImageResult;
//import web.diva.shared.beans.UpdatedTooltip;

/**
 *
 * @author Yehia Farag PCA plot container for pca-plot images
 */
public class PCAPlot {//extends ModularizedListener {

//    private SelectionManager selectionManager;
//    private boolean zoom = false;
//    private boolean selectAll = false;
//    private DivaServiceAsync greetingService;
//    private IButton resetPlotBtn;
//    private boolean enable = true;
////    private int[] currentDataSet;
//    private int[] selectedRows;
//    private final int height = 10;
//    private final int width = 10;
//    private MouseMoveHandler mouseMoveHandler;
//    private MouseOutHandler mouseOutHandler;
//    private MouseUpHandler mouseUpHandler;
//    private MouseDownHandler mouseDownHandler;
//    private HandlerRegistration moveRegHandler;
//    private HandlerRegistration outRegHandler;
//    private HandlerRegistration upRegHandler;
//    private HandlerRegistration downRegHandler;
//    private int startX;
//    private int endX;
//    private int startY;
//    private int endY;
//    private boolean clicked = false;
//    private int absStartX, absStartY;
//    private UpdatedTooltip tooltipInformationData;
//    private VerticalPanel mainThumbPCALayout;
//    private VerticalPanel thumbImgLayout;
//    private HorizontalPanel buttonsLayout;
//    private final Image chart, thumbChart;
//    private HTML toolTip = new HTML();
//    private final HTML rect;
//    private final PopupPanel imagePopup;

//    @Override
//    public String toString() {
//        return "PCAPlot";
//    }
//
//    @Override
//    public void selectionChanged(Selection.TYPE type) {
//        if (type == Selection.TYPE.OF_ROWS) {
//            Selection sel = selectionManager.getSelectedRows();
//            if (sel != null && !zoom && !selectAll) {
//                selectedRows = sel.getMembers();
//                if (selectedRows != null && selectedRows.length != 0) {
//                    updateSelection(selectedRows);
//                }
//            }
//        }
//    }
//
//    public PCAPlot(final PCAImageResult results, SelectionManager selectionManager, DivaServiceAsync greetingService,int height) {
//
//        this.greetingService = greetingService;
//        this.classtype = 4;
//        this.components.add(PCAPlot.this);
//        this.selectionManager = selectionManager;
//        this.selectionManager.addSelectionChangeListener(PCAPlot.this);
//        mainThumbPCALayout = new VerticalPanel();
////        mainThumbPCALayout.setHeight("100%");//(height + "px");
////        mainThumbPCALayout.setWidth("50%");//width + "px");
//       
//        //tooltip  init
//        RootPanel.get("tooltip").add(toolTip);
//        
//        
//        thumbImgLayout = new VerticalPanel();        
//        mainThumbPCALayout.add(thumbImgLayout);
//        thumbImgLayout.setWidth("100%");
//        thumbImgLayout.setHeight(height+"px");
//
//        thumbChart = new Image();
//        thumbChart.setHeight(height+"px");
//        thumbChart.setWidth("100%");
//        thumbChart.setStyleName("magnifying");
//        thumbChart.ensureDebugId("cwBasicPopup-thumb");
//        thumbChart.addStyleName("cw-BasicPopup-thumb");
//        thumbChart.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
//            @Override
//            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
//                imagePopup.center();
//                imagePopup.show();
//            }
//        });
//        thumbImgLayout.add(thumbChart);
//        /*          the end of thumb layout*/
//
//        VerticalPanel chartLayout = new VerticalPanel();
//        VerticalPanel chartImgLayout = new VerticalPanel();
//        this.chart = new Image();
//
//        initChartImageListiners();
//
//        imagePopup = new PopupPanel(false, true);
//        imagePopup.setAnimationEnabled(true);
//        imagePopup.ensureDebugId("cwBasicPopup-imagePopup");
//        imagePopup.setWidget(chartLayout);
//
//        chartImgLayout.add(chart);
//        chartImgLayout.setBorderWidth(1);
//        chartLayout.add(chartImgLayout);
//
//        buttonsLayout = new HorizontalPanel();
//        buttonsLayout.setWidth("350px");
//        buttonsLayout.setHeight("30px");
//
//        final CheckBox cb = new CheckBox("Zoom");
//        resetPlotBtn = new IButton("Zoom Out");
//        resetPlotBtn.setTooltip("Reset the Plot");
//        cb.setValue(false);
//        cb.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                zoom = ((CheckBox) event.getSource()).getValue();
//                   
//            }
//        });
//        buttonsLayout.add(cb);
//        CheckBox cb2 = new CheckBox("Show All");
//        cb2.setValue(selectAll);    // Hook up a handler to find out when it's clicked.
//        cb2.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                selectAll = ((CheckBox) event.getSource()).getValue();
//                showAll(selectAll);
//            }
//        });
//
//        buttonsLayout.add(cb2);
//        resetPlotBtn.disable();
//        resetPlotBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
//            @Override
//            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
//                 cb.setValue(false);
//                 resetPlotBtn.disable();
//                 zoomOut();
////                if (!zoom) {
////                    resetPlotBtn.disable();
////                    updateWithSelection();
////                }
//            }
//        }
//        );
//        buttonsLayout.add(resetPlotBtn);
//        IButton closeBtn = new IButton("Minimize");
//        closeBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
//
//            @Override
//            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
//                 cb.setValue(false);
//                 zoom= false;
//                imagePopup.hide();
//            }
//        });
//        buttonsLayout.add(closeBtn);
//        buttonsLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//        buttonsLayout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//
////        
//        chartLayout.add(buttonsLayout);
//        chartLayout.setSpacing(5);
//
//        chartLayout.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
//        chartLayout.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
//
//        //rectangle draw
//        /**
//         * ***
//         */
//        rect = new HTML();
//        rect.setVisible(false);
//        RootPanel.get().add(rect);
//         
//        
//        
//
//        /**
//         * *****************
//         */
//        tooltipInformationData = results.getTooltipInformatinData();
//
//        /**
//         * *********************
//         */
//        mainThumbPCALayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        mainThumbPCALayout.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
//        updateWithSelection();
//
//    }
//
//    private void updateWithSelection() {
//        Selection sel = selectionManager.getSelectedRows();
//        if (sel != null) {
//            selectedRows = sel.getMembers();
//            this.updateSelection(selectedRows);
//        } else {
//            this.updateSelection(null);
//        }
//
//    }
//
//    private void zoomIn(int startX, int startY, int endX, int endY) {
//        greetingService.pcaZoomIn(startX, startY, endX, endY, new AsyncCallback<PCAImageResult>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void onSuccess(PCAImageResult result) {
//                chart.setUrl(result.getImgString());
//                thumbChart.setUrl(result.getImgString());
//                tooltipInformationData = result.getTooltipInformatinData();
//             
//                if (zoom) {
//                    resetPlotBtn.enable();
//                }
//            }
//        });
//
//    }
//
//    private void zoomOut() {
//        greetingService.pcaZoomReset(new AsyncCallback<PCAImageResult>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void onSuccess(PCAImageResult result) {
//                chart.setUrl(result.getImgString());
//                thumbChart.setUrl(result.getImgString());             
//                tooltipInformationData = result.getTooltipInformatinData();
//                zoom = false;
//            }
//        });
//
//    }
//
//    private void showAll(boolean showAll) {
//        Selection sel = selectionManager.getSelectedRows();
//        if (sel != null) {
//            selectedRows = sel.getMembers();
//        }
//        greetingService.pcaShowAll(showAll, selectedRows, new AsyncCallback<String>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void onSuccess(String result) {
//                chart.setUrl(result);
//                thumbChart.setUrl(result);
//
//            }
//        });
//
//    }
//
//    /**
//     * This method is responsible for mapping the image selection coordinates to
//     * omics data indexes
//     */
//    private void getSelection(int startX, int startY, int endX, int endY) {
//        greetingService.getPCASelection(startX, startY, endX, endY, new AsyncCallback<int[]>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                RootPanel.get("loaderImage").setVisible(false);
//            }
//
//            @Override
//            public void onSuccess(int[] result) {
//                if (result != null && !zoom) {
//                    updateSelectedList(result);
//                } else if (result != null && zoom) {
//                    updateSelection(result);
//
//                }
//            }
//        });
//    }
//
//    private void updateSelectedList(int[] selIndex) {
//        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selIndex);
//        selectionManager.setSelectedRows(selection);
//    }
//
//    public VerticalPanel getPCAPlot() {
//        return mainThumbPCALayout;
//    }
//
//    private void updateSelection(int[] selection) {
//        if (enable) {
////            if (zoom) {
//////                currentDataSet = selection;
////            }
//            greetingService.updatePCASelection(selection, new AsyncCallback<String>() {
//                @Override
//                public void onFailure(Throwable caught) {
//                    RootPanel.get("loaderImage").setVisible(false);
//                }
//
//                @Override
//                public void onSuccess(String result) {
//
//                    chart.setUrl(result);
//                    thumbChart.setUrl(result);
////                    tooltips = result.getXyName();
//                    if (zoom) {
//                        resetPlotBtn.enable();
//                    }
////                    RootPanel.get("loaderImage").setVisible(false);
//                }
//            });
//
//        }
//    }
//    private int selectionHeight, selectionWidth;//,startSelectionX,startSelectionY;
//
//    /**
//     * This method is responsible for initializing image information
//     */
//    private void initChartImageListiners() {
//
//        if (mouseMoveHandler == null) {
//
//            mouseMoveHandler = new MouseMoveHandler() {
//                @Override
//                public void onMouseMove(MouseMoveEvent event) {
//                    int pointY = event.getY();
//                    int pointX = event.getX();
//                    int plotWidthArea = tooltipInformationData.getPlotWidthArea();
//                    int plotHeightArea = tooltipInformationData.getPlotHeightArea();
//                    if ((event.getX() < (tooltipInformationData.getPlotLeft() - tooltipInformationData.getyAxisFactor()) || event.getX() > (tooltipInformationData.getPlotLeft() + plotWidthArea) || event.getY() < tooltipInformationData.getPlotTop() || event.getY() > (tooltipInformationData.getPlotTop() + plotHeightArea))) {
//                        clicked = false;
//                         toolTip.setText("");
//                                toolTip.setVisible(false);
//                    }
//
//                    else if (clicked) {
//                        //show rectangle
////                        int width = 0;
////                        int height = 0;
//                        if (absStartX < event.getClientX()) {
//                            selectionWidth = event.getClientX() - absStartX;
//                        } else {
//                            selectionWidth = absStartX - event.getClientX();
//                            absStartX = event.getClientX();
//                        }
//                        if (absStartY < event.getClientY()) {
//                            selectionHeight = event.getClientY() - absStartY;
//                        } else {
//                            selectionHeight = absStartY - event.getClientY();
//                            absStartY = event.getClientY();
//                        }
//                        rect.setHTML("<p style='opacity: 0.6;z-index:2000000001;position: absolute;height:" + (selectionHeight) + "px; width:" + (selectionWidth) + "px; left:" + absStartX + "px;top:" + absStartY + "px;font-weight: bold; color:white;font-size: 10px;background: #CECEF6; border-style:solid;'>" + "</p>");
//                        rect.setVisible(clicked);
//                    } else {
//                        //tooltip initilization
//                        try {
//                           
////                            if ((event.getX() < (tooltipInformationData.getPlotLeft() - tooltipInformationData.getyAxisFactor()) || event.getX() > (tooltipInformationData.getPlotLeft() + plotWidthArea) || event.getY() < tooltipInformationData.getPlotTop() || event.getY() > (tooltipInformationData.getPlotTop() + plotHeightArea))) {
////                                toolTip.setText("");
////                                toolTip.setVisible(false);
////                            } else {
//                                pointX = pointX - 1 - tooltipInformationData.getPlotLeft() + tooltipInformationData.getyAxisFactor();
//                                pointY -= tooltipInformationData.getPlotTop() - 1;
//                                String tooltipStr = "";
//                                double modPointX = (pointX * tooltipInformationData.getxUnitPix()) + tooltipInformationData.getMinX();//xstart units from min         
//                                double modPointY = tooltipInformationData.getMaxY() - (pointY * tooltipInformationData.getyUnitPix());
//                                double modDotXSize = 2 * tooltipInformationData.getxUnitPix();
//                                double modDotYSize = 2 * tooltipInformationData.getyUnitPix();
//                                for (int x = 0; x < tooltipInformationData.getPoints()[0].length; x++) {
//                                    double tempPointX = tooltipInformationData.getPoints()[0][x];
//                                    double tempPointY = tooltipInformationData.getPoints()[1][x];
//                                    if ((tempPointX == modPointX || (tempPointX <= (modPointX + modDotXSize) && tempPointX >= modPointX - modDotXSize)) && (tempPointY == modPointY || (tempPointY <= (modPointY + modDotYSize) && tempPointY >= (modPointY - modDotYSize)))) {
//                                        tooltipStr = tooltipStr + tooltipInformationData.getRowIds()[x];
//                                        tooltipStr += ",";
//                                    }
//
//                                }
//                                if (!tooltipStr.equals("")) {
//                                    tooltipStr = tooltipStr.substring(0, (tooltipStr.length() - 1));
//                                    updateToolTip(tooltipStr);
//                                } else {
////                                     updateToolTip(" x  "+modPointX+"  y  "+modPointY);
//                                    toolTip.setText("");
//                                    toolTip.setVisible(false);
//                                }
////                            }
//                        } catch (Exception e) {
//                            Window.alert(e.getMessage());
//                        }
//
//////                        String key = pointX + "," + pointY;
//////                        String key1 = (pointX + 1) + "," + pointY;
//////                        String key2 = (pointX - 1) + "," + pointY;
//////                        String key3 = pointX + "," + (pointY + 1);
//////                        String key4 = pointX + "," + (pointY - 1);
//////                        if (tooltips.containsKey(key)) {
//////                            updateToolTip(tooltips.get(key));
//////
//////                        } else if (tooltips.containsKey(key1)) {
//////                            updateToolTip(tooltips.get(key1));
//////                        } else if (tooltips.containsKey(key2)) {
//////                            updateToolTip(tooltips.get(key2));
//////                        } else if (tooltips.containsKey(key3)) {
//////                            updateToolTip(tooltips.get(key3));
//////                        } else if (tooltips.containsKey(key4)) {
//////                            updateToolTip(tooltips.get(key4));
//////                        } else {
//////                            toolTip.setText("");
//////                            toolTip.setVisible(false);
//////                        }
//                    }
//                }
//            };
//
//        }
//        if (mouseOutHandler == null) {
//            mouseOutHandler = new MouseOutHandler() {
//                @Override
//                public void onMouseOut(MouseOutEvent event) {
//                    toolTip.setText("");
//                    toolTip.setVisible(false);
//                    rect.setVisible(false);
//                    
////                     imagePopup.hide();
//                }
//            };
//        }
//        if (moveRegHandler != null) {
//            moveRegHandler.removeHandler();
//        }
//        if (outRegHandler != null) {
//            outRegHandler.removeHandler();
//        }
//
//        moveRegHandler = chart.addMouseMoveHandler(mouseMoveHandler);
//        outRegHandler = this.chart.addMouseOutHandler(mouseOutHandler);
//        if (mouseUpHandler == null) {
//            mouseUpHandler = new MouseUpHandler() {
//
//                @Override
//                public void onMouseUp(MouseUpEvent event) {
//                    endX = event.getX();
//                    endY = event.getY();
//                    rect.setVisible(false);
//                    clicked = false;
//                    //update 
//                    if (zoom) {
//                        zoomIn(startX, startY, endX, endY);
//                    } else {
//                        getSelection(startX, startY, endX, endY);
//                    }
//                }
//            };
//        }
//        if (upRegHandler != null) {
//            upRegHandler.removeHandler();
//        }
//        upRegHandler = chart.addMouseUpHandler(mouseUpHandler);
//
//        if (mouseDownHandler == null) {
//            mouseDownHandler = new MouseDownHandler() {
//                @Override
//                public void onMouseDown(MouseDownEvent event) {
//                    clicked = true;
//                    startX = event.getX();
//                    startY = event.getY();
//                    absStartX = event.getClientX();
//                    absStartY = event.getClientY();
//                    rect.setHTML("<p style='opacity: 0.6;z-index:9000000; position: absolute;height:" + (2) + "px; width:" + (2) + "px; left:" + absStartX + "px;top:" + absStartY + "px;font-weight: bold; color:white;font-size: 10px;background: #CECEF6; border-style:solid;'>" + "</p>");
//                    rect.setVisible(true);
//                    event.preventDefault();
//
//                }
//            };
//        }
//        if (downRegHandler != null) {
//            downRegHandler.removeHandler();
//        }
//        downRegHandler = chart.addMouseDownHandler(mouseDownHandler);
//
//    }
//
//    private void updateToolTip(String lable) {
//        String nString = "";
//        if (lable.length() >= 25) {
//            String row = "";
//            for (String str : lable.split(",")) {
//                nString = nString + str + ",";
//                row = row + str + ",";
//                if (row.length() >= 25) {
//                    nString = nString + "<br/>";
//                    row = "";
//                }
//
//            }
//
//        } else {
//            nString = lable;
//        }
//
//        toolTip.setHTML("<p style='height:60px;font-weight: bold; color:white;font-size: 10px;background: #819FF7; border-style:double;'>" + nString + "</p>");
//        toolTip.setVisible(true);
//    }
//
//    public void enable(boolean enabel) {
//        this.enable = enabel;
//
//    }
//
//    /**
//     * This method is responsible for removing all references on removing
//     * components
//     */
//    @Override
//    public void remove() {
//        selectionManager = null;
//        mouseMoveHandler = null;
//        mouseOutHandler = null;
//        mouseUpHandler = null;
//        mouseDownHandler = null;
//        moveRegHandler = null;
//        outRegHandler = null;
//        upRegHandler = null;
//        downRegHandler = null;
//        mainThumbPCALayout = null;
//        thumbImgLayout = null;
//        buttonsLayout = null;
////        selectionIndexMap = null;
////        chart = null;
//        toolTip = null;
//
//        greetingService = null;
//        resetPlotBtn = null;
////        currentDataSet = null;
//        selectedRows = null;
//    }
// 
}
