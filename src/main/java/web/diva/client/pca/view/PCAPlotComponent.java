/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.pca.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.KnobType;
import com.smartgwt.client.widgets.IconButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.drawing.DrawImage;
import com.smartgwt.client.widgets.drawing.DrawPane;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import web.diva.client.DivaServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.client.view.core.ImageScaler;
import web.diva.client.view.core.SaveAsPanel;
import web.diva.shared.beans.PCAImageResult;
import web.diva.shared.beans.UpdatedTooltip;

/**
 *
 * @author Yehia Farag
 */
public class PCAPlotComponent extends ModularizedListener {

    private SelectionManager selectionManager;
    private boolean zoom = false;
    private boolean selectAll = false;
    private DivaServiceAsync GWTClientService;
    private IconButton zoomoutBtn;
    private boolean enable = true;
    private int[] selectedRows;
    private UpdatedTooltip tooltipInformationData;
    private VLayout mainThumbPCALayout;
    private final HTML tooltipLabel = new HTML();
    private HorizontalPanel tooltipViewPortLayout;
    private final Img thumbChartImg; //maxmizePlotImage,
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
                if (selectedRows != null ){//&& selectedRows.length != 0) {
                    updateSelection(selectedRows);
                }
            }
        }
    }
    private final String datasetInfo;
    private DrawPane pcaImageDrawPan = null;
    private final HandlerRegistration minLabelReg, imagereg, showAllReg, zoomInReg, zoomoutReg, settingBtnReg, saveBtnReg, maxmizeBtnReg;
    private int pcaPlotPanelWidth;
    private int pcaPlotPanelHeight;
    private final PCAImageResult results;

    public PCAPlotComponent(final PCAImageResult results, SelectionManager selectionManager, DivaServiceAsync greetingService, final int colNumber, String datasetInfo) {

        this.GWTClientService = greetingService;
        this.classtype = 2;
        this.components.add(PCAPlotComponent.this);
        this.selectionManager = selectionManager;
        this.selectionManager.addSelectionChangeListener(PCAPlotComponent.this);
        this.tooltipInformationData = results.getTooltipInformatinData();
        this.datasetInfo = datasetInfo;
        this.results=results;
        
        scaler = new ImageScaler();
        int newWidth = (RootPanel.get("diva_mid_panel").getOffsetWidth() / 2) - 20;
        int newHeight = scaler.reScale(newWidth, results.getImgHeight(),results.getImgWidth());
        pcaPlotPanelWidth = newWidth+10;
        pcaPlotPanelHeight= newHeight+20;

        mainThumbPCALayout = new VLayout();
        mainThumbPCALayout.setStyleName("pca");
         mainThumbPCALayout.setHeight(pcaPlotPanelHeight+"px");
        mainThumbPCALayout.setWidth(pcaPlotPanelWidth+"px");
        
        
        topLayout = new HorizontalPanel();
        mainThumbPCALayout.addMember(topLayout);
        topLayout.setWidth(pcaPlotPanelWidth+"px");
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
        thumbChartImg = new Img(results.getImgString());
        thumbChartImg.setMaxHeight(700);
        thumbChartImg.setMaxWidth(900);
        thumbChartImg.setTitle("To Activate the PCA Selection Use Maximized Mode");
        thumbChartImg.ensureDebugId("cwBasicPopup-thumb");
        thumbChartImg.addStyleName("clickableImg");
        thumbImageLayout = new VLayout();

//        thumbImageLayout.setStyleName("imagesborder");
        int width = (RootPanel.get("diva_mid_panel").getOffsetWidth() / 2) - 20;

        mainThumbPCALayout.addMember(thumbImageLayout);
        thumbImageLayout.setHeight(newHeight + "px");
        thumbImageLayout.setWidth(pcaPlotPanelWidth + "px");
        thumbImageLayout.addMember(thumbChartImg);


        thumbChartImg.setWidth(width + "px");
        thumbChartImg.setHeight(newHeight + "px");
//        thumbImageLayout.setCellHorizontalAlignment(thumbChartImg, VerticalPanel.ALIGN_CENTER);
//        thumbImageLayout.setCellVerticalAlignment(thumbChartImg, VerticalPanel.ALIGN_MIDDLE);


        
//        mainThumbPCALayout.addMember(thumbChartImg);
        
        
//        thumbChartImg.setHeight("100%");
//        thumbChartImg.setWidth("100%");

        /* the end of thumb layout*/
        pcaPopup = new PopupPanel(false, true);
        pcaPopup.setAnimationEnabled(true);
        pcaPopup.ensureDebugId("cwBasicPopup-imagePopup");

        final VLayout mainPcaPopupBodyLayout = new VLayout();
        mainPcaPopupBodyLayout.setWidth(900 + "px");
        mainPcaPopupBodyLayout.setHeight(770 + "px");

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
        showAllReg = showallBtn.addClickHandler(new ClickHandler() {
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

        zoomInReg = zoomInBtn.addClickHandler(new ClickHandler() {
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

        zoomoutReg = zoomOut.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                zoomInBtn.setValue(false);
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
        settingBtnReg = settingsBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (pcsSettingPanel == null) {
                    initPcaSettingPanel(results.getPcaLabelData());
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

        saveBtnReg = saveBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                Window.open(thumbChartImg.getUrl(), "Download Image", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,toolbar=true, width=" + Window.getClientWidth() + ",height=" + Window.getClientHeight());
                SelectionManager.Busy_Task(true, false);
//                Window.open(profilePlotMaxImage.getUrl(), "downlodwindow", "status=0,toolbar=0,menubar=0,location=0");
                GWTClientService.exportImgAsPdf("PCA_Plot","high", new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        SelectionManager.Busy_Task(false, false);
                    }

                    @Override
                    public void onSuccess(String result) {
                        SaveAsPanel sa = new SaveAsPanel("PCA Image", result);
                        sa.center();
                        sa.show();
                        SelectionManager.Busy_Task(false, false);
                    }
                });

            }
        });
        ClickHandler minmizeClickHandler = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                zoom = false;
                zoomInBtn.setValue(false);
                pcaPopup.hide();
            }
        };

        minLabelReg = minmizeBtn.addClickHandler(minmizeClickHandler);

        final VLayout updatedMmaxmizePlotImgLayout = new VLayout();
        updatedMmaxmizePlotImgLayout.setHeight(710);
        updatedMmaxmizePlotImgLayout.setWidth(900);

        mainPcaPopupBodyLayout.addMember(updatedMmaxmizePlotImgLayout);

        tooltipViewPortLayout = new HorizontalPanel();
        tooltipViewPortLayout.setWidth(900 + "px");
        tooltipViewPortLayout.setHeight("80px");
        mainPcaPopupBodyLayout.addMember(tooltipViewPortLayout);
        tooltipViewPortLayout.add(tooltipLabel);
        tooltipLabel.setStyleName("tooltip");
        com.smartgwt.client.widgets.events.ClickHandler maxmizeClickHandler = new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                pcaPopup.center();
                pcaPopup.show();
                if (pcaImageDrawPan == null) {
                    pcaImageDrawPan = createDrawPane();
                    updatedMmaxmizePlotImgLayout.addMember(pcaImageDrawPan);
                }
            }
        };
        
        VerticalPanel variationPanel = new VerticalPanel();
        variationPanel.setWidth("300px");
        variationPanel.setHeight("70px");
        variationPanel.setStyleName("fullborder");
        tooltipViewPortLayout.add(variationPanel);
        tooltipViewPortLayout.setCellVerticalAlignment(variationPanel, HorizontalPanel.ALIGN_TOP);
        tooltipViewPortLayout.setCellHorizontalAlignment(variationPanel, HorizontalPanel.ALIGN_RIGHT);
        
        Label l1= new Label(results.getPcax());
         Label l2= new Label(results.getPcay());
          Label l3= new Label(results.getTotalVarianc());
          variationPanel.add(l1);
          variationPanel.add(l2);
          variationPanel.add(l3);
        

        imagereg = thumbChartImg.addClickHandler(maxmizeClickHandler);

        maxmizeBtnReg = maxmizeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                  pcaPopup.center();
                pcaPopup.show();
                if (pcaImageDrawPan == null) {
                    pcaImageDrawPan = createDrawPane();
                    updatedMmaxmizePlotImgLayout.addMember(pcaImageDrawPan);
                }
            }
        });
        pcaPopup.setWidget(mainPcaPopupBodyLayout);
        mainPcaPopupBodyLayout.setStyleName("modalLayout");
        updateWithSelection();
        
        
         
          
//         Window.addResizeHandler(new ResizeHandler() {
//
//            @Override
//            public void onResize(ResizeEvent event) {
//               
//                if (redrawTimer == null) {
//                    redrawTimer = new Timer() {
//
//                        @Override
//                        public void run() {
//                            int newWidth = (RootPanel.get("diva_mid_panel").getOffsetWidth() / 2) - 20;
//                            int newHeight = scaler.reScale(newWidth, results.getImgHeight(),results.getImgWidth());
//                            pcaPlotPanelWidth = newWidth + 10;
//                            pcaPlotPanelHeight = newHeight + 20;
//                            mainThumbPCALayout.setWidth(pcaPlotPanelWidth+"px" );
//                            mainThumbPCALayout.setHeight(pcaPlotPanelHeight+"px");
//                             topLayout.setWidth(pcaPlotPanelWidth+"px");
//                            thumbImageLayout.setHeight(newHeight + "px");
//                            thumbImageLayout.setWidth(pcaPlotPanelWidth + "px");
//                            thumbChartImg.setHeight(newHeight + "px");
//                            thumbChartImg.setWidth(newWidth + "px");
//                            thumbImageLayout.setCellHorizontalAlignment(thumbChartImg, VerticalPanel.ALIGN_CENTER);
//                            thumbImageLayout.setCellVerticalAlignment(thumbChartImg, VerticalPanel.ALIGN_MIDDLE);
////                            mainThumbPCALayout.redraw();
//                        }
//                    };
//                    
//                }
//                redrawTimer.schedule(200);
//
//            }
//        });

    }
    
     private   final VLayout thumbImageLayout;
   private    final HorizontalPanel topLayout;
   private final ImageScaler scaler;
    public void resize(int newWidth,int newHeight){
                            pcaPlotPanelWidth = newWidth;
                            pcaPlotPanelHeight = newHeight;
                            mainThumbPCALayout.setWidth(pcaPlotPanelWidth+"px" );
                            mainThumbPCALayout.setHeight(pcaPlotPanelHeight+"px");
                             topLayout.setWidth(pcaPlotPanelWidth+"px");
                            thumbImageLayout.setHeight(newHeight + "px");
                            thumbImageLayout.setWidth(pcaPlotPanelWidth + "px");
                            thumbChartImg.setHeight((newHeight-20) + "px");
                            thumbChartImg.setWidth(newWidth + "px");
//                            thumbImageLayout.setCellHorizontalAlignment(thumbChartImg, VerticalPanel.ALIGN_CENTER);
//                            thumbImageLayout.setCellVerticalAlignment(thumbChartImg, VerticalPanel.ALIGN_MIDDLE);
    
    }
//    private Timer redrawTimer ; 
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
                mainPCAImage.setSrc(result.getImgString());
                thumbChartImg.setSrc(result.getImgString());
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
                mainPCAImage.setSrc(result.getImgString());
                thumbChartImg.setSrc(result.getImgString());
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
                mainPCAImage.setSrc(result);
                thumbChartImg.setSrc(result);

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
        if (selIndex != null){// && selIndex.length > 0) {
            SelectionManager.Busy_Task(true, false);
            Selection selection = new Selection(Selection.TYPE.OF_ROWS, selIndex);
            selectionManager.setSelectedRows(selection);
        }
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
                    mainPCAImage.setSrc(result);
                    thumbChartImg.setSrc(result);

                    if (zoom) {
                        zoomoutBtn.enable();
                    }
                }
            });

        }
    }

    private void updateToolTip(String lable) {
        tooltipLabel.setHTML("<textarea cols=\"30\" rows=\"4\">" + datasetInfo + " : " + lable + "</textarea>");

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
        minLabelReg.removeHandler();
        imagereg.removeHandler();
        showAllReg.removeHandler();
        zoomInReg.removeHandler();
        zoomoutReg.removeHandler();
        settingBtnReg.removeHandler();
        saveBtnReg.removeHandler();
        maxmizeBtnReg.removeHandler();
        selectionManager.removeSelectionChangeListener(this);
        selectionManager = null;
        mainThumbPCALayout = null;
        tooltipViewPortLayout = null;
        GWTClientService = null;
        zoomoutBtn = null;
        selectedRows = null;
    }

    private PcaSettingsPanel pcsSettingPanel;

    private void initPcaSettingPanel(String[] pcaLabelData) {

        pcsSettingPanel = new PcaSettingsPanel(pcaLabelData);
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

        SelectionManager.Busy_Task(true, false);
        GWTClientService.computePCA(pcaI, pcaII,
                new AsyncCallback<PCAImageResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        SelectionManager.Busy_Task(false, false);
                    }

                    @Override
                    public void onSuccess(PCAImageResult result) {
                        SelectionManager.Busy_Task(false, false);
                        tooltipInformationData = result.getTooltipInformatinData();
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
        drawPane.setAutoDraw(false);

        drawPane.addDrawHandler(new DrawHandler() {
            @Override
            public void onDraw(DrawEvent event) {

                mainPCAImage.setLineWidth(1);
                mainPCAImage.setTop(0);
                mainPCAImage.setLeft(0);
                mainPCAImage.setWidth(900);
                mainPCAImage.setHeight(700);
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
                        }
                    }

                });

                drawPane.addMouseDownHandler(new com.smartgwt.client.widgets.events.MouseDownHandler() {

                    @Override
                    public void onMouseDown(com.smartgwt.client.widgets.events.MouseDownEvent event) {
                        startUX = event.getX() - drawPane.getAbsoluteLeft();
                        startUY = event.getY() - drawPane.getAbsoluteTop();
                        drawPane.setCursor(Cursor.CROSSHAIR);

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
                    }
                });

            }
        });

        return drawPane;
    }

}