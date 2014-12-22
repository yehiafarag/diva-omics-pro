/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.pca.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
//import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.KnobType;
import com.smartgwt.client.widgets.IconButton;
import com.smartgwt.client.widgets.drawing.DrawImage;
import com.smartgwt.client.widgets.drawing.DrawPane;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import web.diva.client.GreetingServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.PCAImageResult;
import web.diva.shared.beans.UpdatedTooltip;

/**
 *
 * @author Yehia Farag
 */
public class UpdatedPCAPlot extends ModularizedListener {

    private SelectionManager selectionManager;
    private boolean zoom = false;
    private boolean selectAll = false;
    private GreetingServiceAsync GWTClientService;
    private IconButton zoomoutBtn;
    private boolean enable = true;
    private int[] selectedRows;
//    private MouseMoveHandler mouseMoveHandler;
//    private MouseOverHandler mouseOverHandler;
//    private MouseOutHandler mouseOutHandler;
//    private MouseUpHandler mouseUpHandler;
//    private com.google.gwt.event.dom.client.MouseDownHandler mouseDownHandler;
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
    private UpdatedTooltip tooltipInformationData;
    private VLayout mainThumbPCALayout;
    private final HTML tooltipLabel = new HTML();
    private HorizontalPanel tooltipViewPortLayout;
    private final Image thumbChart; //maxmizePlotImage,
    private final PopupPanel pcaPopup;
    private final DrawImage mainPCAImage = new DrawImage();

    @Override
    public String toString() {
        return "PCAPlot";
    }

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows();
            if (sel != null && !zoom && !selectAll) {
                selectedRows = sel.getMembers();
                if (selectedRows != null && selectedRows.length != 0) {
                    updateSelection(selectedRows);
                }
            }
        }
    }
//    private final VerticalPanel maxmizePlotImgLayout;
    private final String datasetInfo;
    private DrawPane pcaImageDrawPan = null;

    public UpdatedPCAPlot(final PCAImageResult results, SelectionManager selectionManager, GreetingServiceAsync greetingService, final int colNumber, String datasetInfo) {

        this.GWTClientService = greetingService;
        this.classtype = 4;
        this.components.add(UpdatedPCAPlot.this);
        this.selectionManager = selectionManager;
        this.selectionManager.addSelectionChangeListener(UpdatedPCAPlot.this);
        this.tooltipInformationData = results.getTooltipInformatinData();
        this.datasetInfo = datasetInfo;

        mainThumbPCALayout = new VLayout();
//        mainThumbPCALayout.setBorder("1px solid #E6E6E6");
        mainThumbPCALayout.setStyleName("pca");
        mainThumbPCALayout.setHeight("46%");
        mainThumbPCALayout.setWidth("25%");
        HorizontalPanel topLayout = new HorizontalPanel();
        mainThumbPCALayout.addMember(topLayout);
        topLayout.setWidth("100%");
        topLayout.setHeight("18px");
        topLayout.setStyleName("whiteLayout");
        Label title = new Label("PCA Plot");
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth("90%");
        topLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);
        Label maxmizeBtn = new Label();
        maxmizeBtn.addStyleName("maxmize");
        maxmizeBtn.setHeight("16px");
        maxmizeBtn.setWidth("16px");
        topLayout.add(maxmizeBtn);
        thumbChart = new Image();
        thumbChart.ensureDebugId("cwBasicPopup-thumb");
        thumbChart.addStyleName("cw-BasicPopup-thumb");
        mainThumbPCALayout.addMember(thumbChart);
        thumbChart.setHeight("100%");
        thumbChart.setWidth("100%");

        /* the end of thumb layout*/
        pcaPopup = new PopupPanel(false, true);
        pcaPopup.setAnimationEnabled(true);
        pcaPopup.ensureDebugId("cwBasicPopup-imagePopup");

        final VLayout mainPcaPopupBodyLayout = new VLayout();
        mainPcaPopupBodyLayout.setWidth(900 + "px");
        mainPcaPopupBodyLayout.setHeight(770+"px");
//        mainPcaPopupBodyLayout.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
//        mainPcaPopupBodyLayout.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);

        HorizontalPanel maxTopLayout = new HorizontalPanel();
        mainPcaPopupBodyLayout.addMember(maxTopLayout);
        maxTopLayout.setWidth(900 + "px");
        maxTopLayout.setHeight("18px");
        maxTopLayout.setStyleName("whiteLayout");
        maxTopLayout.setSpacing(3);

        Label maxTitle = new Label("PCA Plot");
        maxTitle.setStyleName("labelheader");
        maxTopLayout.add(maxTitle);

        maxTitle.setWidth((900 - 300) + "px");
        maxTopLayout.setCellHorizontalAlignment(maxTitle, HorizontalPanel.ALIGN_LEFT);

        CheckBox showallBtn = new CheckBox("Show All");
        showallBtn.setValue(selectAll);
        showallBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                selectAll = ((CheckBox) event.getSource()).getValue();
                showAll(selectAll);
            }
        });

        maxTopLayout.add(showallBtn);
        maxTopLayout.setCellHorizontalAlignment(showallBtn, HorizontalPanel.ALIGN_RIGHT);
        final CheckBox zoomInBtn = new CheckBox("Zoom In");
        zoomInBtn.setValue(false);
        zoomInBtn.setWidth("90px");

        zoomInBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                zoom = ((CheckBox) event.getSource()).getValue();

            }
        });

        maxTopLayout.add(zoomInBtn);
        maxTopLayout.setCellHorizontalAlignment(zoomInBtn, HorizontalPanel.ALIGN_LEFT);
        maxTopLayout.setCellVerticalAlignment(zoomInBtn, VerticalPanel.ALIGN_TOP);

        Label zoomOut = new Label();
        zoomOut.addStyleName("zoomout");
        zoomOut.setHeight("16px");
        zoomOut.setWidth("16px");
        maxTopLayout.add(zoomOut);
        maxTopLayout.setCellHorizontalAlignment(zoomOut, HorizontalPanel.ALIGN_RIGHT);

        zoomOut.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                zoomInBtn.setValue(false);
//                zoomoutBtn.disable();
                zoomOut();
            }
        }
        );

        Label settingsBtn = new Label();
        settingsBtn.addStyleName("settings");
        settingsBtn.setHeight("16px");
        settingsBtn.setWidth("16px");
        maxTopLayout.add(settingsBtn);
        maxTopLayout.setCellHorizontalAlignment(settingsBtn, HorizontalPanel.ALIGN_RIGHT);
        settingsBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (pcsSettingPanel == null) {
                    initPcaSettingPanel(colNumber);
                }

                pcsSettingPanel.center();
                pcsSettingPanel.show();

            }
        });

        Label saveBtn = new Label();
        saveBtn.addStyleName("save");
        saveBtn.setHeight("16px");
        saveBtn.setWidth("16px");
        maxTopLayout.add(saveBtn);
        maxTopLayout.setCellHorizontalAlignment(saveBtn, HorizontalPanel.ALIGN_RIGHT);

        Label minmizeBtn = new Label();
        minmizeBtn.addStyleName("minmize");
        minmizeBtn.setHeight("16px");
        minmizeBtn.setWidth("16px");
        maxTopLayout.add(minmizeBtn);
        maxTopLayout.setCellHorizontalAlignment(minmizeBtn, HorizontalPanel.ALIGN_RIGHT);

        saveBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Window.open(thumbChart.getUrl(), "Download Image", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,toolbar=true, width=" + Window.getClientWidth() + ",height=" + Window.getClientHeight());

            }
        });
        minmizeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                zoom = false;
                pcaPopup.hide();
            }
        });

        //old v
//        maxmizePlotImgLayout = new VerticalPanel();
//        this.maxmizePlotImage = new Image();
////
//        maxmizePlotImgLayout.add(maxmizePlotImage);
//        mainPcaPopupBodyLayout.add(maxmizePlotImgLayout);

        //end of old
        //new v
//        VLayout updatedMmaxmizePlotImgLayout = new VLayout();
//        updatedMmaxmizePlotImgLayout.setHeight(700);
//        updatedMmaxmizePlotImgLayout.setWidth(900);
//        updatedMmaxmizePlotImgLayout.setBorder("1px solid #E6E6E6");
//
//        updatedMmaxmizePlotImgLayout.addMember(this.createDrawPane(""));
//        mainPcaPopupBodyLayout.addMember(updatedMmaxmizePlotImgLayout);
        //end of new
        final VLayout updatedMmaxmizePlotImgLayout = new VLayout();
        updatedMmaxmizePlotImgLayout.setHeight(700);
        updatedMmaxmizePlotImgLayout.setWidth(900);
//                updatedMmaxmizePlotImgLayout.setBorder("1px solid #E6E6E6");
        mainPcaPopupBodyLayout.addMember(updatedMmaxmizePlotImgLayout);

        tooltipViewPortLayout = new HorizontalPanel();
        tooltipViewPortLayout.setWidth(900 + "px");
        tooltipViewPortLayout.setHeight("50px");
        mainPcaPopupBodyLayout.addMember(tooltipViewPortLayout);
        tooltipViewPortLayout.add(tooltipLabel);
        tooltipLabel.setStyleName("tooltip");
        maxmizeBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
                pcaPopup.center();
                pcaPopup.show();
                if (pcaImageDrawPan == null) {
                    pcaImageDrawPan = createDrawPane();
                    updatedMmaxmizePlotImgLayout.addMember(pcaImageDrawPan);
                }
            }
        });
//        initChartImageListiners();
        pcaPopup.setWidget(mainPcaPopupBodyLayout);
        mainPcaPopupBodyLayout.setStyleName("modalLayout");
        updateWithSelection();
    }

    private void updateWithSelection() {
        Selection sel = selectionManager.getSelectedRows();
        if (sel != null) {
            selectedRows = sel.getMembers();
            this.updateSelection(selectedRows);
        } else {
            this.updateSelection(null);
        }

    }

    private void zoomIn(int startX, int startY, int endX, int endY) {
        GWTClientService.pcaZoomIn(startX, startY, endX, endY, new AsyncCallback<PCAImageResult>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("ERROR IN SERVER CONNECTION");
            }

            @Override
            public void onSuccess(PCAImageResult result) {
//                maxmizePlotImage.setUrl(result.getImgString());
                mainPCAImage.setSrc(result.getImgString());
                thumbChart.setUrl(result.getImgString());
                tooltipInformationData = result.getTooltipInformatinData();

                if (zoom) {
                    zoomoutBtn.enable();
                }
            }
        });

    }

    private void zoomOut() {
        GWTClientService.pcaZoomReset(new AsyncCallback<PCAImageResult>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("ERROR IN SERVER CONNECTION");
            }

            @Override
            public void onSuccess(PCAImageResult result) {
//                maxmizePlotImage.setUrl(result.getImgString());
                mainPCAImage.setSrc(result.getImgString());
                thumbChart.setUrl(result.getImgString());
                tooltipInformationData = result.getTooltipInformatinData();
                zoom = false;
            }
        });

    }

    private void showAll(boolean showAll) {
        Selection sel = selectionManager.getSelectedRows();
        if (sel != null) {
            selectedRows = sel.getMembers();
        }
        GWTClientService.pcaShowAll(showAll, selectedRows, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("ERROR IN SERVER CONNECTION");
            }

            @Override
            public void onSuccess(String result) {
//                maxmizePlotImage.setUrl(result);

                mainPCAImage.setSrc(result);
                thumbChart.setUrl(result);

            }
        });

    }

    private void getSelection(int startX, int startY, int endX, int endY) {
        GWTClientService.getPCASelection(startX, startY, endX, endY, new AsyncCallback<int[]>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("ERROR IN SERVER CONNECTION");
            }

            @Override
            public void onSuccess(int[] result) {
                if (result != null && !zoom) {
                    updateSelectedList(result);
                } else if (result != null && zoom) {
                    updateSelection(result);

                }
            }
        });
    }

    private void updateSelectedList(int[] selIndex) {
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selIndex);
        selectionManager.setSelectedRows(selection);
    }

    public VLayout getPCAComponent() {
        return mainThumbPCALayout;
    }

    private void updateSelection(int[] selection) {
        if (enable) {
            GWTClientService.updatePCASelection(selection, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("ERROR IN SERVER CONNECTION");
                }

                @Override
                public void onSuccess(String result) {
//                    maxmizePlotImage.setUrl(result);    
                    mainPCAImage.setSrc(result);
                    thumbChart.setUrl(result);

                    if (zoom) {
                        zoomoutBtn.enable();
                    }
                }
            });

        }
    }
//    private int selectionHeight, selectionWidth;

    /**
     * This method is responsible for initializing image information
     */
//    private void initChartImageListiners() {
////if (mouseMoveHandler == null) {
////            mouseMoveHandler = new MouseMoveHandler() {
////                @Override
////                public void onMouseMove(MouseMoveEvent event) {
//////                    boolean clicked = isClicked();
////                    int pointY = event.getY();
////                    int pointX = event.getX();
//////                    int plotWidthArea = tooltipInformationData.getPlotWidthArea();
//////                    int plotHeightArea = tooltipInformationData.getPlotHeightArea();
//////                    if ((event.getX() < (tooltipInformationData.getPlotLeft() - tooltipInformationData.getyAxisFactor()) || event.getX() > (tooltipInformationData.getPlotLeft() + plotWidthArea) || event.getY() < tooltipInformationData.getPlotTop() || event.getY() > (tooltipInformationData.getPlotTop() + plotHeightArea))) {
//////                        setClicked(false);
//////                    } else
////                    if (event.isLeftButtonDown()) {
//////                        if (absStartX < event.getClientX()) {
//////                            selectionWidth = event.getClientX() - absStartX;
//////                        } else {
//////                            selectionWidth = absStartX - event.getClientX();
//////                            absStartX = event.getClientX();
//////                        }
//////                        if (absStartY < event.getClientY()) {
//////                            selectionHeight = event.getClientY() - absStartY;
//////                        } else {
//////                            selectionHeight = absStartY - event.getClientY();
//////                            absStartY = event.getClientY();
//////                        }
//////                        rectanglePath.setWidth(selectionWidth + "px");
//////                        rectanglePath.setHeight(selectionHeight + "px");
//////                        rectanglePath.setPopupPosition(absStartX, absStartY);
////
////                    } else {
////                        
////                        try {
////
////                            pointX = pointX - 1 - tooltipInformationData.getPlotLeft() + tooltipInformationData.getyAxisFactor();
////                            pointY -= tooltipInformationData.getPlotTop() - 1;
////                            String tooltipStr = "";
////                            double modPointX = (pointX * tooltipInformationData.getxUnitPix()) + tooltipInformationData.getMinX();//xstart units from min         
////                            double modPointY = tooltipInformationData.getMaxY() - (pointY * tooltipInformationData.getyUnitPix());
////                            double modDotXSize = 2 * tooltipInformationData.getxUnitPix();
////                            double modDotYSize = 2 * tooltipInformationData.getyUnitPix();
////                            for (int x = 0; x < tooltipInformationData.getPoints()[0].length; x++) {
////                                double tempPointX = tooltipInformationData.getPoints()[0][x];
////                                double tempPointY = tooltipInformationData.getPoints()[1][x];
////                                if ((tempPointX == modPointX || (tempPointX <= (modPointX + modDotXSize) && tempPointX >= modPointX - modDotXSize)) && (tempPointY == modPointY || (tempPointY <= (modPointY + modDotYSize) && tempPointY >= (modPointY - modDotYSize)))) {
////                                    tooltipStr = tooltipStr + tooltipInformationData.getRowIds()[x];
////                                    tooltipStr += ",";
////                                }
////
////                            }
////                            if (!tooltipStr.equals("")) {
////                                tooltipStr = tooltipStr.substring(0, (tooltipStr.length() - 2));
////                                updateToolTip(tooltipStr);
////                            } else {
////                                tooltipLabel.setText("");
////                            }
////                        } catch (Exception e) {
////                            Window.alert(e.getMessage());
////                        }
////
////                    }
////                }
////            };
////
////        }
//// if (mouseOutHandler == null) {
////            mouseOutHandler = new MouseOutHandler() {
////                @Override
////                public void onMouseOut(MouseOutEvent event) {
//////                    tooltip.setVisible(false);
//////                    rect.setVisible(false);
//////                    rectanglePath.hide(false);
////                    event.cancel();
//////                   
////                }
////            };
////        }
////        if (moveRegHandler != null) {
////            moveRegHandler.removeHandler();
////        }
////        if (outRegHandler != null) {
////            outRegHandler.removeHandler();
////        }
////
////        moveRegHandler = pcaImageDrawPan.addMouseMoveHandler(mouseMoveHandler);
////        outRegHandler = this.pcaImageDrawPan.addMouseOutHandler(mouseOutHandler);
////        if (mouseUpHandler == null) {
////            mouseUpHandler = new MouseUpHandler() {
////
////                @Override
////                public void onMouseUp(MouseUpEvent event) {
//////                    clicked = false;
////                    endX = event.getX();
////                    endY = event.getY();
//////                    rect.setVisible(false);
//////                    rectanglePath.hide(false);
////
////                    //update 
////                    if (zoom) {
////                        zoomIn(startX, startY, endX, endY);
////                    } else {
////                        getSelection(startX, startY, endX, endY);
////                    }
////                }
////            };
////        }
////        if (upRegHandler != null) {
////            upRegHandler.removeHandler();
////        }
////        upRegHandler = pcaImageDrawPan.addMouseUpHandler(mouseUpHandler);
////
////        if (mouseDownHandler == null) {
////            mouseDownHandler = new com.google.gwt.event.dom.client.MouseDownHandler() {
////                @Override
////                public void onMouseDown(com.google.gwt.event.dom.client.MouseDownEvent event) {
//////                    clicked = true;
////                    startX = event.getX();
////                    startY = event.getY();
////                    absStartX = event.getClientX();
////                    absStartY = event.getClientY();
//////                    rect.setHTML("<p style='opacity: 0.6;z-index:90000000; position: absolute;height:" + (2) + "px; width:" + (2) + "px; left:" + absStartX + "px;top:" + absStartY + "px;font-weight: bold; color:white;font-size: 10px;background: #CECEF6; border-style:solid;'>" + "</p>");
//////                    rect.setVisible(true);
//////                    rectanglePath.setVisible(true);
////                    event.preventDefault();
////
////                }
////            };
////        }
////        if (downRegHandler != null) {
////            downRegHandler.removeHandler();
////        }
////        downRegHandler = maxmizePlotImage.addMouseDownHandler(mouseDownHandler);
//        //start of old listeners
////        if (mouseMoveHandler == null) {
////            mouseMoveHandler = new com.google.gwt.event.dom.client.MouseMoveHandler() {
////                @Override
////                public void onMouseMove(com.google.gwt.event.dom.client.MouseMoveEvent event) {
////                    boolean clicked = isClicked();
////                    int pointY = event.getY();
////                    int pointX = event.getX();
////                    int plotWidthArea = tooltipInformationData.getPlotWidthArea();
////                    int plotHeightArea = tooltipInformationData.getPlotHeightArea();
////                    if ((event.getX() < (tooltipInformationData.getPlotLeft() - tooltipInformationData.getyAxisFactor()) || event.getX() > (tooltipInformationData.getPlotLeft() + plotWidthArea) || event.getY() < tooltipInformationData.getPlotTop() || event.getY() > (tooltipInformationData.getPlotTop() + plotHeightArea))) {
////                        setClicked(false);
////                    } else if (clicked) {
////                        if (absStartX < event.getClientX()) {
////                            selectionWidth = event.getClientX() - absStartX;
////                        } else {
////                            selectionWidth = absStartX - event.getClientX();
////                            absStartX = event.getClientX();
////                        }
////                        if (absStartY < event.getClientY()) {
////                            selectionHeight = event.getClientY() - absStartY;
////                        } else {
////                            selectionHeight = absStartY - event.getClientY();
////                            absStartY = event.getClientY();
////                        }
//////                        rectanglePath.setWidth(selectionWidth + "px");
//////                        rectanglePath.setHeight(selectionHeight + "px");
//////                        rectanglePath.setPopupPosition(absStartX, absStartY);
////
////                    } else {
////                        
////                        try {
////
////                            pointX = pointX - 1 - tooltipInformationData.getPlotLeft() + tooltipInformationData.getyAxisFactor();
////                            pointY -= tooltipInformationData.getPlotTop() - 1;
////                            String tooltipStr = "";
////                            double modPointX = (pointX * tooltipInformationData.getxUnitPix()) + tooltipInformationData.getMinX();//xstart units from min         
////                            double modPointY = tooltipInformationData.getMaxY() - (pointY * tooltipInformationData.getyUnitPix());
////                            double modDotXSize = 2 * tooltipInformationData.getxUnitPix();
////                            double modDotYSize = 2 * tooltipInformationData.getyUnitPix();
////                            for (int x = 0; x < tooltipInformationData.getPoints()[0].length; x++) {
////                                double tempPointX = tooltipInformationData.getPoints()[0][x];
////                                double tempPointY = tooltipInformationData.getPoints()[1][x];
////                                if ((tempPointX == modPointX || (tempPointX <= (modPointX + modDotXSize) && tempPointX >= modPointX - modDotXSize)) && (tempPointY == modPointY || (tempPointY <= (modPointY + modDotYSize) && tempPointY >= (modPointY - modDotYSize)))) {
////                                    tooltipStr = tooltipStr + tooltipInformationData.getRowIds()[x];
////                                    tooltipStr += ",";
////                                }
////
////                            }
////                            if (!tooltipStr.equals("")) {
////                                tooltipStr = tooltipStr.substring(0, (tooltipStr.length() - 2));
////                                updateToolTip(tooltipStr);
////                            } else {
////                                tooltipLabel.setText("");
////                            }
////                        } catch (Exception e) {
////                            Window.alert(e.getMessage());
////                        }
////
////                    }
////                }
////            };
////
////        }
//
//
//// if (mouseOutHandler == null) {
////            mouseOutHandler = new MouseOutHandler() {
////                @Override
////                public void onMouseOut(com.google.gwt.event.dom.client.MouseOutEvent event) {
//////                    tooltip.setVisible(false);
//////                    rect.setVisible(false);
//////                    rectanglePath.hide(false);
////                    event.preventDefault();
//////                   
////                }
////            };
////        }
////        if (moveRegHandler != null) {
////            moveRegHandler.removeHandler();
////        }
////        if (outRegHandler != null) {
////            outRegHandler.removeHandler();
////        }
////
////        moveRegHandler = maxmizePlotImage.addMouseMoveHandler(mouseMoveHandler);
//////        maxmizePlotImage.addMouseOverHandler(mouseOverHandler);
////        outRegHandler = this.maxmizePlotImage.addMouseOutHandler(mouseOutHandler);
////        if (mouseUpHandler == null) {
////            mouseUpHandler = new com.google.gwt.event.dom.client.MouseUpHandler() {
////
////                @Override
////                public void onMouseUp(MouseUpEvent event) {
////                    clicked = false;
////                    endX = event.getX();
////                    endY = event.getY();
//////                    rect.setVisible(false);
//////                    rectanglePath.hide(false);
////
////                    //update 
////                    if (zoom) {
////                        zoomIn(startX, startY, endX, endY);
////                    } else {
////                        getSelection(startX, startY, endX, endY);
////                    }
////                }
////            };
////        }
////        if (upRegHandler != null) {
////            upRegHandler.removeHandler();
////        }
////        upRegHandler = maxmizePlotImage.addMouseUpHandler(mouseUpHandler);
////
////        if (mouseDownHandler == null) {
////            mouseDownHandler = new com.google.gwt.event.dom.client.MouseDownHandler() {
////                @Override
////                public void onMouseDown(com.google.gwt.event.dom.client.MouseDownEvent event) {
////                    clicked = true;
////                    startX = event.getX();
////                    startY = event.getY();
////                    absStartX = event.getClientX();
////                    absStartY = event.getClientY();
//////                    rect.setHTML("<p style='opacity: 0.6;z-index:90000000; position: absolute;height:" + (2) + "px; width:" + (2) + "px; left:" + absStartX + "px;top:" + absStartY + "px;font-weight: bold; color:white;font-size: 10px;background: #CECEF6; border-style:solid;'>" + "</p>");
//////                    rect.setVisible(true);
//////                    rectanglePath.setVisible(true);
////                    event.preventDefault();
////
////                }
////            };
////        }
////        if (downRegHandler != null) {
////            downRegHandler.removeHandler();
////        }
////        downRegHandler = maxmizePlotImage.addMouseDownHandler(mouseDownHandler);
//        
//        //end of old listeners
////        if (mouseOverHandler == null) {
////
////            mouseOverHandler = new com.google.gwt.event.dom.client.MouseOverHandler() {
////
////                @Override
////                public void onMouseOver(MouseOverEvent event) {
////                    int pointY = event.getY();
////                    int pointX = event.getX();
////                    int plotWidthArea = tooltipInformationData.getPlotWidthArea();
////                    int plotHeightArea = tooltipInformationData.getPlotHeightArea();
////                    if ((event.getX() < (tooltipInformationData.getPlotLeft() - tooltipInformationData.getyAxisFactor()) || event.getX() > (tooltipInformationData.getPlotLeft() + plotWidthArea) || event.getY() < tooltipInformationData.getPlotTop() || event.getY() > (tooltipInformationData.getPlotTop() + plotHeightArea))) {
////                        clicked = false;
////                    } else if (clicked) {
////                        //show rectangle
//////                        int width = 0;
//////                        int height = 0;
////                        if (absStartX < event.getClientX()) {
////                            selectionWidth = event.getClientX() - absStartX;
////                        } else {
////                            selectionWidth = absStartX - event.getClientX();
////                            absStartX = event.getClientX();
////                        }
////                        if (absStartY < event.getClientY()) {
////                            selectionHeight = event.getClientY() - absStartY;
////                        } else {
////                            selectionHeight = absStartY - event.getClientY();
////                            absStartY = event.getClientY();
////                        }
//////                        rect.setHTML("<p style='opacity: 0.6;z-index:90000000;position: absolute;height:" + (selectionHeight) + "px; width:" + (selectionWidth) + "px; left:" + absStartX + "px;top:" + absStartY + "px;font-weight: bold; color:white;font-size: 10px;background: #CECEF6; border-style:solid;'>" + "</p>");
//////                        rect.setVisible(clicked);
////                        rectanglePath.setVisible(true);
////
////                    } else {
////                        //tooltip initilization
////                        try {
//////                            if (rect.isVisible()) {
//////                                setRectVisible(false);
//////                            }
////
////                            pointX = pointX - 1 - tooltipInformationData.getPlotLeft() + tooltipInformationData.getyAxisFactor();
////                            pointY -= tooltipInformationData.getPlotTop() - 1;
////                            String tooltipStr = "";
////                            double modPointX = (pointX * tooltipInformationData.getxUnitPix()) + tooltipInformationData.getMinX();//xstart units from min         
////                            double modPointY = tooltipInformationData.getMaxY() - (pointY * tooltipInformationData.getyUnitPix());
////                            double modDotXSize = 2 * tooltipInformationData.getxUnitPix();
////                            double modDotYSize = 2 * tooltipInformationData.getyUnitPix();
////                            for (int x = 0; x < tooltipInformationData.getPoints()[0].length; x++) {
////                                double tempPointX = tooltipInformationData.getPoints()[0][x];
////                                double tempPointY = tooltipInformationData.getPoints()[1][x];
////                                if ((tempPointX == modPointX || (tempPointX <= (modPointX + modDotXSize) && tempPointX >= modPointX - modDotXSize)) && (tempPointY == modPointY || (tempPointY <= (modPointY + modDotYSize) && tempPointY >= (modPointY - modDotYSize)))) {
////                                    tooltipStr = tooltipStr + tooltipInformationData.getRowIds()[x];
////                                    tooltipStr += ",";
////                                }
////
////                            }
////                            if (!tooltipStr.equals("")) {
////                                tooltipStr = tooltipStr.substring(0, (tooltipStr.length() - 1));
////                                updateToolTip(tooltipStr, event.getClientX(), event.getClientY());
////                            } else {
////                                tooltip.hide(true);
////                            }
////                        } catch (Exception e) {
////                            Window.alert(e.getMessage());
////                        }
////
////                    }
////
////                }
////            };
////
////        }
//
////        if (mouseOutHandler == null) {
////            mouseOutHandler = new MouseOutHandler() {
////                @Override
////                public void onMouseOut(com.google.gwt.event.dom.client.MouseOutEvent event) {
//////                    tooltip.setVisible(false);
//////                    rect.setVisible(false);
//////                    rectanglePath.hide(false);
////                    event.preventDefault();
//////                   
////                }
////            };
////        }
////        if (moveRegHandler != null) {
////            moveRegHandler.removeHandler();
////        }
////        if (outRegHandler != null) {
////            outRegHandler.removeHandler();
////        }
////
////        moveRegHandler = maxmizePlotImage.addMouseMoveHandler(mouseMoveHandler);
//////        maxmizePlotImage.addMouseOverHandler(mouseOverHandler);
////        outRegHandler = this.maxmizePlotImage.addMouseOutHandler(mouseOutHandler);
////        if (mouseUpHandler == null) {
////            mouseUpHandler = new com.google.gwt.event.dom.client.MouseUpHandler() {
////
////                @Override
////                public void onMouseUp(MouseUpEvent event) {
////                    clicked = false;
////                    endX = event.getX();
////                    endY = event.getY();
//////                    rect.setVisible(false);
//////                    rectanglePath.hide(false);
////
////                    //update 
////                    if (zoom) {
////                        zoomIn(startX, startY, endX, endY);
////                    } else {
////                        getSelection(startX, startY, endX, endY);
////                    }
////                }
////            };
////        }
////        if (upRegHandler != null) {
////            upRegHandler.removeHandler();
////        }
////        upRegHandler = maxmizePlotImage.addMouseUpHandler(mouseUpHandler);
////
////        if (mouseDownHandler == null) {
////            mouseDownHandler = new com.google.gwt.event.dom.client.MouseDownHandler() {
////                @Override
////                public void onMouseDown(com.google.gwt.event.dom.client.MouseDownEvent event) {
////                    clicked = true;
////                    startX = event.getX();
////                    startY = event.getY();
////                    absStartX = event.getClientX();
////                    absStartY = event.getClientY();
//////                    rect.setHTML("<p style='opacity: 0.6;z-index:90000000; position: absolute;height:" + (2) + "px; width:" + (2) + "px; left:" + absStartX + "px;top:" + absStartY + "px;font-weight: bold; color:white;font-size: 10px;background: #CECEF6; border-style:solid;'>" + "</p>");
//////                    rect.setVisible(true);
//////                    rectanglePath.setVisible(true);
////                    event.preventDefault();
////
////                }
////            };
////        }
////        if (downRegHandler != null) {
////            downRegHandler.removeHandler();
////        }
////        downRegHandler = maxmizePlotImage.addMouseDownHandler(mouseDownHandler);
//
//    }
    private void updateToolTip(String lable) {
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

        tooltipLabel.setHTML("<textarea cols=\"30\" rows=\"4\">" + datasetInfo + " : " + lable + "</textarea>");
//        tooltip.setAnimationEnabled(false);
//        tooltip.setWidget(tooltipMessage);
//        tooltip.setWidth((nString.length() * 10) + "px");
//        tooltip.setStyleName("notification");
//        tooltipMessage.setText(nString);
//        tooltip.setPopupPosition(x - 10, y - 10);
//        tooltip.show();
//        tooltip.setAutoHideEnabled(true);

    }

    public void enable(boolean enabel) {
        this.enable = enabel;

    }

    /**
     * This method is responsible for removing all references on removing
     * components
     */
    @Override
    public void remove() {
        selectionManager = null;
//        mouseMoveHandler = null;
//        mouseOutHandler = null;
//        mouseUpHandler = null;
//        mouseDownHandler = null;
//        moveRegHandler = null;
//        outRegHandler = null;
//        upRegHandler = null;
//        downRegHandler = null;
        mainThumbPCALayout = null;
//        thumbImgLayout = null;
        tooltipViewPortLayout = null;
//        toolTip = null;
        GWTClientService = null;
        zoomoutBtn = null;
        selectedRows = null;
    }

//    private final PopupPanel tooltip = new PopupPanel(true, true);
    private final Label tooltipMessage = new Label();

//    public boolean isClicked() {
//        return clicked;
//    }
//
//    public void setClicked(boolean clicked) {
//        this.clicked = clicked;
//    }
//    private void setRectVisible(boolean visible) {
////        rect.setVisible(visible);
////        rectanglePath.setVisible(visible);
//    }
    private UpdatedPcaPanel pcsSettingPanel;

    private void initPcaSettingPanel(int colNumber) {

        pcsSettingPanel = new UpdatedPcaPanel(colNumber);
        pcsSettingPanel.getOkBtn().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {

                int pcaI = pcsSettingPanel.getPcaI();
                int pcaII = pcsSettingPanel.getPcaII();
                updatePcaChart(pcaI, pcaII);
                pcsSettingPanel.hide();
            }
        });

    }

    private void updatePcaChart(int pcaI, int pcaII) {

        selectionManager.busyTask(true,false);
        GWTClientService.computePCA(pcaI, pcaII,
                new AsyncCallback<PCAImageResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        selectionManager.busyTask(false,false);
                    }

                    @Override
                    public void onSuccess(PCAImageResult result) {
                        selectionManager.busyTask(false,false);
                        updateWithSelection();

                    }
                });
    }

    private int startUX, startUY, endUX, endUY;

    private DrawPane createDrawPane() {
        final DrawPane drawPane = new DrawPane();
        drawPane.setCursor(Cursor.ARROW);
        drawPane.setWidth(900);
        drawPane.setHeight(700);
//        drawPane.setBorder("2px solid black");
        drawPane.setAutoDraw(false);

        drawPane.addDrawHandler(new DrawHandler() {
            @Override
            public void onDraw(DrawEvent event) {

                mainPCAImage.setLineWidth(1);
                mainPCAImage.setTop(0);
                mainPCAImage.setLeft(0);
                mainPCAImage.setWidth(900);
                mainPCAImage.setHeight(700);
//                mainPCAImage.setKnobs(KnobType.RESIZE);
//                mainPCAImage.hideKnobs(KnobType.RESIZE);
                mainPCAImage.setKeepInParentRect(true);
                mainPCAImage.setDrawPane(drawPane);
                mainPCAImage.draw();

                final DrawImage selectionRectangel = new DrawImage();
                selectionRectangel.setLineWidth(1);
                selectionRectangel.setWidth(0);
                selectionRectangel.setHeight(0);
                selectionRectangel.setKeepInParentRect(true);
                selectionRectangel.setKnobs(KnobType.RESIZE);
                selectionRectangel.setUseMatrixFilter(true);
                selectionRectangel.showKnobs(KnobType.RESIZE);
                selectionRectangel.setDrawPane(drawPane);
//                selectionRectangel.erase();

                drawPane.draw();

                drawPane.addMouseMoveHandler(new com.smartgwt.client.widgets.events.MouseMoveHandler() {

                    @Override
                    public void onMouseMove(com.smartgwt.client.widgets.events.MouseMoveEvent event) {
                        if (event.isLeftButtonDown()) {
                            selectionRectangel.setRect(startUX, startUY, (event.getX() - drawPane.getAbsoluteLeft() - startUX), (event.getY() - drawPane.getAbsoluteTop() - startUY));
                            selectionRectangel.showKnobs(KnobType.RESIZE);

                        } else {
                            selectionRectangel.hideKnobs(KnobType.RESIZE);
                            try {

                                int pointY = event.getY() - drawPane.getAbsoluteTop();
                                int pointX = event.getX() - drawPane.getAbsoluteLeft();
                                pointX = pointX - 1 - tooltipInformationData.getPlotLeft() + tooltipInformationData.getyAxisFactor();
                                pointY -= tooltipInformationData.getPlotTop() - 1;
                                String tooltipStr = "";
                                double modPointX = (pointX * tooltipInformationData.getxUnitPix()) + tooltipInformationData.getMinX();//xstart units from min         
                                double modPointY = tooltipInformationData.getMaxY() - (pointY * tooltipInformationData.getyUnitPix());
                                double modDotXSize = 2 * tooltipInformationData.getxUnitPix();
                                double modDotYSize = 2 * tooltipInformationData.getyUnitPix();
                                for (int x = 0; x < tooltipInformationData.getPoints()[0].length; x++) {
                                    double tempPointX = tooltipInformationData.getPoints()[0][x];
                                    double tempPointY = tooltipInformationData.getPoints()[1][x];
                                    if ((tempPointX == modPointX || (tempPointX <= (modPointX + modDotXSize) && tempPointX >= modPointX - modDotXSize)) && (tempPointY == modPointY || (tempPointY <= (modPointY + modDotYSize) && tempPointY >= (modPointY - modDotYSize)))) {
                                        tooltipStr = tooltipStr + tooltipInformationData.getRowIds()[x];
                                        tooltipStr += ",";
                                    }

                                }
                                if (!tooltipStr.equals("")) {
                                    tooltipStr = tooltipStr.substring(0, (tooltipStr.length() - 2));
                                    updateToolTip(tooltipStr);
                                } else {
                                    tooltipLabel.setText("");
                                }
                            } catch (Exception e) {
                                Window.alert(e.getMessage());
                            }
//                            selectionRectangel.erase();
                        }
                    }

                });

                drawPane.addMouseDownHandler(new com.smartgwt.client.widgets.events.MouseDownHandler() {

                    @Override
                    public void onMouseDown(com.smartgwt.client.widgets.events.MouseDownEvent event) {
//                         SC.say("down");

                        startUX = event.getX() - drawPane.getAbsoluteLeft();
                        startUY = event.getY() - drawPane.getAbsoluteTop();

                        drawPane.setCursor(Cursor.CROSSHAIR);
//                        mainPCAImage.erase();
//                        selectionRectangel.draw();
//                        drawPane.draw();

                    }
                });
                drawPane.addMouseUpHandler(new com.smartgwt.client.widgets.events.MouseUpHandler() {

                    @Override
                    public void onMouseUp(com.smartgwt.client.widgets.events.MouseUpEvent event) {

                        endUX = event.getX() - drawPane.getAbsoluteLeft();
                        endUY = event.getY() - drawPane.getAbsoluteTop();
                        selectionRectangel.hideKnobs(KnobType.RESIZE);

                        drawPane.setCursor(Cursor.ARROW);

                        if (zoom) {
                            zoomIn(startUX, startUY, endUX, endUY);
                        } else {
                            getSelection(startUX, startUY, endUX, endUY);
                        }
//                        selectionRectangel.erase();
//                        drawPane.draw();
                    }
                });

            }
        });

        return drawPane;
    }

}
