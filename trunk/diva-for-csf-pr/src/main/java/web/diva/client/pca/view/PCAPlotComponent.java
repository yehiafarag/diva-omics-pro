package web.diva.client.pca.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.ArrowStyle;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.KnobType;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.widgets.IconButton;
import com.smartgwt.client.widgets.drawing.DrawImage;
import com.smartgwt.client.widgets.drawing.DrawPane;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import web.diva.client.DivaServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.client.view.core.InfoIcon;
import web.diva.client.view.core.SaveAsPanel;
import web.diva.shared.beans.PCAImageResult;
import web.diva.shared.beans.UpdatedTooltip;

/**
 * Principal Components Analysis component
 *
 * @author Yehia Farag
 */
public class PCAPlotComponent extends ModularizedListener {

    private SelectionManager Selection_Manager;
    private boolean zoom = false;
    private boolean selectAll = false;
    private DivaServiceAsync GWTClientService;
    private IconButton zoomoutBtn;
    private int[] selectedRows;
    private UpdatedTooltip tooltipInformationData;
    private VLayout mainThumbPCALayout;
    private final HTML tooltipLabel = new HTML();
    private HorizontalPanel tooltipViewPortLayout;
    private final DrawImage thumbChartImg = new DrawImage();
    private final PopupPanel pcaPopup;
    private final DrawImage mainPCAImage = new DrawImage();
    private final int newWidth;

    private final String datasetInfo;
    private DrawPane pcaMaxImageDrawPan = null, pcaThumbImgDrawPan = null;
    private final HandlerRegistration minLabelReg, showAllReg, zoomInReg, zoomoutReg, settingBtnReg, saveBtnReg, maxmizeBtnReg;

    private HandlerRegistration imagereg;
    private final int pcaPlotPanelWidth;
    private final int pcaPlotPanelHeight;
    private PCAImageResult results;
    private final HorizontalPanel topLayout;

    private int maxStartUX, maxStartUY, maxEndUX, maxEndUY;
    private int minStartUX, minStartUY, minEndUX, minEndUY;

    private int calMaxImgWidth;
    private float minScaler;
    private float maxScaler;

    /**
     *
     * @param Selection_Manager main central manager
     * @param datasetInfo dataset information object
     * @param results main PCAImageResult with all pca information, tool-tips
     * and data
     * @param divaService diva GWTClientService
     * @param colNumber column numbers
     * @param midPanelWidth the width of the mid panel
     *
     *
     */
    public PCAPlotComponent(final PCAImageResult results, final SelectionManager Selection_Manager, DivaServiceAsync divaService, final int colNumber, String datasetInfo, int midPanelWidth) {

        this.GWTClientService = divaService;
        this.classtype = 2;
        this.components.add(PCAPlotComponent.this);
        this.Selection_Manager = Selection_Manager;
        this.Selection_Manager.addSelectionChangeListener(PCAPlotComponent.this);
        this.tooltipInformationData = results.getTooltipInformatinData();
        this.datasetInfo = datasetInfo;
        this.results = results;

        newWidth = (midPanelWidth / 2);
        pcaPlotPanelWidth = newWidth;
        pcaPlotPanelHeight = newWidth + 22;

        mainThumbPCALayout = new VLayout();
        mainThumbPCALayout.setStyleName("pca");
        mainThumbPCALayout.setHeight(pcaPlotPanelHeight + "px");
        mainThumbPCALayout.setWidth(pcaPlotPanelWidth + "px");

        topLayout = new HorizontalPanel();
        mainThumbPCALayout.addMember(topLayout);
        topLayout.setWidth(pcaPlotPanelWidth + "px");
        topLayout.setHeight("18px");
        topLayout.setStyleName("whiteLayout");
        Label title = new Label("PCA Plot");
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth("50%");
        topLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);
        
        
        
        
        
         HorizontalPanel btnsPanel = new HorizontalPanel();
        btnsPanel.setWidth("34px");
        btnsPanel.setHeight("20px");
        btnsPanel.add(new InfoIcon("PCA Plot", initInfoLayout(600,600),600,600));
        
          Label maxmizeBtn = new Label();
        maxmizeBtn.addStyleName("maxmize");
        maxmizeBtn.setHeight("16px");
        maxmizeBtn.setWidth("16px");
        btnsPanel.add(maxmizeBtn);
        maxmizeBtn.setHorizontalAlignment(Label.ALIGN_RIGHT);
        btnsPanel.setCellHorizontalAlignment(maxmizeBtn, HorizontalPanel.ALIGN_RIGHT);
        
        topLayout.add(btnsPanel);
        topLayout.setCellHorizontalAlignment(btnsPanel, HorizontalPanel.ALIGN_RIGHT);
        
        calcMinImgResize();
        if (pcaThumbImgDrawPan == null) {
            pcaThumbImgDrawPan = createThumbImgDrawPane();
        }
        mainThumbPCALayout.addMember(pcaThumbImgDrawPan);
        /* the end of thumb layout*/
        pcaPopup = new PopupPanel(false, true);
        pcaPopup.setAnimationEnabled(true);
        pcaPopup.ensureDebugId("cwBasicPopup-imagePopup");

        calcMaxImgResize();

        final VLayout mainPcaPopupBodyLayout = new VLayout();
        mainPcaPopupBodyLayout.setWidth((calMaxImgWidth + 2) + "px");
        mainPcaPopupBodyLayout.setHeight((calMaxImgWidth + 30 + 80) + "px");

        HorizontalPanel maxTopLayout = new HorizontalPanel();
        mainPcaPopupBodyLayout.addMember(maxTopLayout);
        maxTopLayout.setWidth((calMaxImgWidth + 2) + "px");
        maxTopLayout.setHeight("18px");
        maxTopLayout.setStyleName("whiteLayout");
        maxTopLayout.setSpacing(3);

        Label maxTitle = new Label("PCA Plot");
        maxTitle.setStyleName("labelheader");
        maxTopLayout.add(maxTitle);

        maxTitle.setWidth(((calMaxImgWidth + 2) - 318) + "px");
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

        
        InfoIcon icon = new InfoIcon("PCA Plot",initInfoLayout(600,600),600,600);
        
          maxTopLayout.add(icon);
        maxTopLayout.setCellHorizontalAlignment(icon, HorizontalPanel.ALIGN_RIGHT);
        
        
        
        Label minmizeBtn = new Label();
        minmizeBtn.addStyleName("minmize");
        minmizeBtn.setHeight("16px");
        minmizeBtn.setWidth("16px");
        maxTopLayout.add(minmizeBtn);
        maxTopLayout.setCellHorizontalAlignment(minmizeBtn, HorizontalPanel.ALIGN_RIGHT);

        saveBtnReg = saveBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SelectionManager.Busy_Task(true, false);
                GWTClientService.exportImgAsPdf("PCA_Plot", "high", new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Selection_Manager.connError();
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
        updatedMmaxmizePlotImgLayout.setHeight((calMaxImgWidth + 2));
        updatedMmaxmizePlotImgLayout.setWidth((calMaxImgWidth + 2));

        mainPcaPopupBodyLayout.addMember(updatedMmaxmizePlotImgLayout);

        tooltipViewPortLayout = new HorizontalPanel();
        tooltipViewPortLayout.setWidth((calMaxImgWidth + 2) + "px");
        tooltipViewPortLayout.setHeight("80px");
        mainPcaPopupBodyLayout.addMember(tooltipViewPortLayout);
        tooltipViewPortLayout.add(tooltipLabel);
        tooltipLabel.setStyleName("tooltip");
        tooltipLabel.setWidth((calMaxImgWidth - 44 - 300) + "px");
        VerticalPanel variationPanel = new VerticalPanel();
        variationPanel.setWidth("300px");
        variationPanel.setHeight("70px");
        variationPanel.setStyleName("fullborder");
        tooltipViewPortLayout.add(variationPanel);
        tooltipViewPortLayout.setCellVerticalAlignment(variationPanel, HorizontalPanel.ALIGN_TOP);
        tooltipViewPortLayout.setCellHorizontalAlignment(variationPanel, HorizontalPanel.ALIGN_RIGHT);

        Label l1 = new Label(results.getPcax());
        Label l2 = new Label(results.getPcay());
        Label l3 = new Label(results.getTotalVarianc());
        variationPanel.add(l1);
        variationPanel.add(l2);
        variationPanel.add(l3);

        maxmizeBtnReg = maxmizeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                pcaPopup.center();
                pcaPopup.show();
                if (pcaMaxImageDrawPan == null) {
                    pcaMaxImageDrawPan = createMaxImgDrawPane();
                    updatedMmaxmizePlotImgLayout.addMember(pcaMaxImageDrawPan);
                }
            }
        });
        pcaPopup.setWidget(mainPcaPopupBodyLayout);
        mainPcaPopupBodyLayout.setStyleName("modalLayout");
        updateWithSelection();

    }

    /**
     * This method is responsible for updating PCA selection updated row/column
     * groups to share with other users
     *
     */
    private void updateWithSelection() {
        Selection sel = Selection_Manager.getSelectedRows();
        if (sel != null) {
            selectedRows = sel.getMembers();
            this.updateSelection(selectedRows);
        } else {
            this.updateSelection(null);
        }

    }

    /**
     * This method is responsible for updating thePCA chart by zooming in
     * selection rectangle
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     *
     */
    private void zoomIn(int startX, int startY, int endX, int endY) {
        GWTClientService.pcaZoomIn(startX, startY, endX, endY, new AsyncCallback<PCAImageResult>() {

            @Override
            public void onFailure(Throwable caught) {
                Selection_Manager.connError();
            }

            @Override
            public void onSuccess(PCAImageResult result) {
                results = result;
                mainPCAImage.setSrc(result.getImgString());
                thumbChartImg.setSrc(result.getImgString());
                tooltipInformationData = result.getTooltipInformatinData();

                if (zoom) {
                    zoomoutBtn.enable();
                }
            }
        });

    }

    /**
     * This method is responsible for updating thePCA chart by reset the plot to
     * default
     */
    private void zoomOut() {
        GWTClientService.pcaZoomReset(new AsyncCallback<PCAImageResult>() {

            @Override
            public void onFailure(Throwable caught) {
                Selection_Manager.connError();
            }

            @Override
            public void onSuccess(PCAImageResult result) {
                results = result;
                mainPCAImage.setSrc(result.getImgString());
                thumbChartImg.setSrc(result.getImgString());
                tooltipInformationData = result.getTooltipInformatinData();
                zoom = false;
            }
        });

    }

    /**
     * This method is responsible for updating thePCA chart by showing all
     * selected and unselected members
     *
     * @param showAll show all members or selected only
     *
     */
    private void showAll(boolean showAll) {
        Selection sel = Selection_Manager.getSelectedRows();
        if (sel != null) {
            selectedRows = sel.getMembers();
        }
        GWTClientService.pcaShowAll(showAll, selectedRows, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Selection_Manager.connError();
            }

            @Override
            public void onSuccess(String result) {
                mainPCAImage.setSrc(result);
                thumbChartImg.setSrc(result);

            }
        });

    }

    /**
     * This method is responsible for converting PCA user selection coordinates
     * into indexes
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     *
     */
    private void getSelection(int startX, int startY, int endX, int endY) {
        GWTClientService.getPCASelection(startX, startY, endX, endY, new AsyncCallback<int[]>() {
            @Override
            public void onFailure(Throwable caught) {
                Selection_Manager.connError();
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

    /**
     * This method is responsible for updating the selection manager on user
     * selection
     *
     * @param selectedIndices selected data indexes
     *
     */
    private void updateSelectedList(int[] selectedIndices) {
        if (selectedIndices != null) {// && selectedIndices.length > 0) {
            SelectionManager.Busy_Task(true, false);
            Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
            Selection_Manager.setSelectedRows(selection);
        }
    }

    /**
     *
     * @return PCA main body layout
     *
     */
    public VLayout getPCAComponent() {
        return mainThumbPCALayout;
    }

    /**
     * This method is responsible for updating chart with the user selected
     * indexes
     *
     * @param selectedIndices selected data indexes
     *
     */
    private void updateSelection(int[] selectedIndices) {

        GWTClientService.updatePCASelection(selectedIndices, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Selection_Manager.connError();
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

    /**
     * This method is responsible for updating the tool-tip appear on user
     * moving over specific points
     *
     * @param lable selected data indexes
     *
     */
    private void updateToolTip(String lable) {
        tooltipLabel.setHTML("<textarea cols=\"30\" rows=\"4\">" + datasetInfo + " : " + lable + "</textarea>");

    }

    /**
     * This method is responsible for cleaning on removing the component from
     * the container
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
        Selection_Manager.removeSelectionChangeListener(this);
        Selection_Manager = null;
        mainThumbPCALayout = null;
        tooltipViewPortLayout = null;
        GWTClientService = null;
        zoomoutBtn = null;
        selectedRows = null;
    }

    private PcaSettingsPanel pcsSettingPanel;

    /**
     * This method is responsible for initializing pca sitting panel
     *
     * @param pcaLabelData pca names lables
     */
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

    /**
     * This method is responsible for invoking the PCA analysis and
     * visualization method on the server side
     *
     * @param comI principal component 1 index
     * @param comII principal component 2 index
     *
     */
    private void updatePcaChart(int pcaI, int pcaII) {

        SelectionManager.Busy_Task(true, false);
        GWTClientService.computePCA(pcaI, pcaII,
                new AsyncCallback<PCAImageResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Selection_Manager.connError();
                        SelectionManager.Busy_Task(false, false);
                    }

                    @Override
                    public void onSuccess(PCAImageResult result) {
                        results = result;
                        SelectionManager.Busy_Task(false, false);
                        tooltipInformationData = result.getTooltipInformatinData();
                        updateWithSelection();

                    }
                });
    }

    /**
     * This method is responsible for initializing pca thumb image drawing panel
     */
    private DrawPane createThumbImgDrawPane() {
        final DrawPane drawPane = new DrawPane();
        drawPane.setCursor(Cursor.ARROW);
        drawPane.setWidth((newWidth - 4));
        drawPane.setHeight((newWidth - 4));

        drawPane.setAutoDraw(false);

        imagereg = drawPane.addDrawHandler(new DrawHandler() {
            @Override
            public void onDraw(DrawEvent event) {

                thumbChartImg.setLineWidth(0);
                thumbChartImg.setTop(0);
                thumbChartImg.setLeft(0);
                thumbChartImg.setWidth(newWidth - 4);
                thumbChartImg.setHeight(newWidth - 4);
                thumbChartImg.setKeepInParentRect(true);
                thumbChartImg.setDrawPane(drawPane);
                thumbChartImg.setStartArrow(ArrowStyle.NULL);

                thumbChartImg.draw();

                final DrawImage selectionRectangel = new DrawImage();
                selectionRectangel.hide();
                selectionRectangel.setLineWidth(1);
                selectionRectangel.setWidth(0);
                selectionRectangel.setHeight(0);
                selectionRectangel.setKeepInParentRect(true);
                selectionRectangel.setKnobs(KnobType.RESIZE);
                selectionRectangel.setUseMatrixFilter(true);
                selectionRectangel.hideKnobs(KnobType.RESIZE);
                selectionRectangel.setDrawPane(drawPane);

                drawPane.draw();

                drawPane.addMouseMoveHandler(new com.smartgwt.client.widgets.events.MouseMoveHandler() {

                    @Override
                    public void onMouseMove(com.smartgwt.client.widgets.events.MouseMoveEvent event) {
                        if (event.isLeftButtonDown()) {

                            selectionRectangel.setRect(minStartUX, minStartUY, (event.getX() - drawPane.getAbsoluteLeft() - minStartUX), (event.getY() - drawPane.getAbsoluteTop() - minStartUY));
                            selectionRectangel.showKnobs(KnobType.RESIZE);
                        } else {
                            selectionRectangel.hideKnobs(KnobType.RESIZE);
                        }
                    }

                });

                drawPane.addMouseDownHandler(new com.smartgwt.client.widgets.events.MouseDownHandler() {

                    @Override
                    public void onMouseDown(com.smartgwt.client.widgets.events.MouseDownEvent event) {
                        selectionRectangel.show();
                        minStartUX = event.getX() - drawPane.getAbsoluteLeft();
                        minStartUY = event.getY() - drawPane.getAbsoluteTop();

                        drawPane.setCursor(Cursor.CROSSHAIR);

                    }
                });
                drawPane.addMouseUpHandler(new com.smartgwt.client.widgets.events.MouseUpHandler() {

                    @Override
                    @SuppressWarnings("UnnecessaryBoxing")
                    public void onMouseUp(com.smartgwt.client.widgets.events.MouseUpEvent event) {

                        minEndUX = event.getX() - drawPane.getAbsoluteLeft();
                        minEndUY = event.getY() - drawPane.getAbsoluteTop();
                        selectionRectangel.hideKnobs(KnobType.RESIZE);
                        drawPane.setCursor(Cursor.ARROW);
                        minStartUX = Math.round(Float.valueOf(minStartUX) * minScaler);
                        minStartUY = Math.round(Float.valueOf(minStartUY) * minScaler);
                        minEndUX = Math.round(Float.valueOf(minEndUX) * minScaler);
                        minEndUY = Math.round(Float.valueOf(minEndUY) * minScaler);

                        getSelection(minStartUX, minStartUY, minEndUX, minEndUY);

                    }
                });

            }
        });

        return drawPane;
    }

    /**
     * This method is responsible for calculates the max image size( image width
     * and height)
     */
    @SuppressWarnings("UnnecessaryBoxing")
    private void calcMaxImgResize() {
        calMaxImgWidth = Page.getScreenHeight() - (300);
        maxScaler = Float.valueOf(results.getImgWidth()) / Float.valueOf(calMaxImgWidth);

    }

    /**
     * This method is responsible for calculates the thumb image size( image
     * width and height)
     */
    @SuppressWarnings("UnnecessaryBoxing")
    private void calcMinImgResize() {
        minScaler = Float.valueOf(results.getImgWidth()) / Float.valueOf(newWidth - 2.0f);

    }

    /**
     * This method is responsible for initializing pca max image drawing panel
     */
    private DrawPane createMaxImgDrawPane() {
        final DrawPane drawPane = new DrawPane();
        drawPane.setCursor(Cursor.ARROW);
        drawPane.setWidth((calMaxImgWidth));
        drawPane.setHeight((calMaxImgWidth));

        drawPane.setAutoDraw(false);

        drawPane.addDrawHandler(new DrawHandler() {
            @Override
            public void onDraw(DrawEvent event) {

                mainPCAImage.setLineWidth(1);
                mainPCAImage.setTop(0);
                mainPCAImage.setLeft(0);
                mainPCAImage.setWidth(calMaxImgWidth);
                mainPCAImage.setHeight(calMaxImgWidth);
                mainPCAImage.setKeepInParentRect(true);
                mainPCAImage.setDrawPane(drawPane);
                mainPCAImage.setStartArrow(ArrowStyle.NULL);
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
                    @SuppressWarnings("UnnecessaryBoxing")
                    public void onMouseMove(com.smartgwt.client.widgets.events.MouseMoveEvent event) {
                        if (event.isLeftButtonDown()) {
                            selectionRectangel.setRect(maxStartUX, maxStartUY, (event.getX() - drawPane.getAbsoluteLeft() - maxStartUX), (event.getY() - drawPane.getAbsoluteTop() - maxStartUY));
                            selectionRectangel.showKnobs(KnobType.RESIZE);

                        } else {
                            selectionRectangel.hideKnobs(KnobType.RESIZE);
                            try {

                                int pointY = event.getY() - drawPane.getAbsoluteTop();
                                int pointX = event.getX() - drawPane.getAbsoluteLeft();
                                pointY = Math.round(Float.valueOf(pointY) * maxScaler);
                                pointX = Math.round(Float.valueOf(pointX) * maxScaler);
//                                updateToolTip("x "+pointX+"   y "+pointY);
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
                        maxStartUX = event.getX() - drawPane.getAbsoluteLeft();
                        maxStartUY = event.getY() - drawPane.getAbsoluteTop();

                        drawPane.setCursor(Cursor.CROSSHAIR);

                    }
                });
                drawPane.addMouseUpHandler(new com.smartgwt.client.widgets.events.MouseUpHandler() {

                    @Override
                    @SuppressWarnings("UnnecessaryBoxing")
                    public void onMouseUp(com.smartgwt.client.widgets.events.MouseUpEvent event) {

                        maxEndUX = event.getX() - drawPane.getAbsoluteLeft();
                        maxEndUY = event.getY() - drawPane.getAbsoluteTop();
                        selectionRectangel.hideKnobs(KnobType.RESIZE);
                        drawPane.setCursor(Cursor.ARROW);
                        maxStartUX = Math.round(Float.valueOf(maxStartUX) * maxScaler);
                        maxStartUY = Math.round(Float.valueOf(maxStartUY) * maxScaler);
                        maxEndUX = Math.round(Float.valueOf(maxEndUX) * maxScaler);
                        maxEndUY = Math.round(Float.valueOf(maxEndUY) * maxScaler);

                        if (zoom) {
                            zoomIn(maxStartUX, maxStartUY, maxEndUX, maxEndUY);
                        } else {
                            getSelection(maxStartUX, maxStartUY, maxEndUX, maxEndUY);
                        }
                    }
                });

            }
        });

        return drawPane;
    }

    @Override
    public String toString() {
        return "PCAPlot";
    }

    /**
     * This method is the listener implementation for the central manager the
     * method responsible for the component notification there is selection
     * event
     *
     * @param type Selection.TYPE row or column
     *
     */
    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = Selection_Manager.getSelectedRows();
            if (sel != null && !zoom && !selectAll) {
                selectedRows = sel.getMembers();
                if (selectedRows != null) {//&& selectedRows.length != 0) {
                    updateSelection(selectedRows);
                }
            }
        }
    }

    /**
     * This method to get the current pca results
     *
     * @return PCAImageResult
     *
     */
    public PCAImageResult getResults() {
        return results;
    }

    private VerticalPanel initInfoLayout(int h, int w) {
        VerticalPanel infopanel = new VerticalPanel();
        infopanel.setWidth(w + "px");
        infopanel.setHeight(h + "px");
        infopanel.setStyleName("whiteLayout");

        HTML information = new HTML("<p style='margin-left:30px;font-size:14px;line-height: 150%;'> The PCA plot supports direct mouse selections in both the minimized and maximized state using mouse click and drag.<br/><center> <img src='images/pca-selection.png' alt='' style='width:auto;height:100px'/></center></p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>It is recommended to use the PCA plot in the maximized state <img src='images/maxmize.png' alt='' style='width:auto;height:16px'/> to get better visualization and to get access to all PCA plot features.</p>."
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>In the maximized state one can export the PCA plot as PDF by clicking the save icon <img src='images/icon_save.gif' alt='' style='width:auto;height:16px'/>.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>The principal components used can be changed by clicking the setting icon <img src='images/setting.gif' alt='' style='width:auto;height:16px'/>.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>The PCA plot supports zoom and select. Activate zooming mode by selecting the zoom checkbox and select the chosen area in the chart.<center><img src='images/zoompca.png' alt='' style='width:auto;height:100px'/></center></p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>After zooming to the desired level, reactivate the selection mode by unchecking the zoom checkbox.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>Reset the chart by clicking the zoom out icon <img src='images/zoomout.png' alt='' style='width:auto;height:16px'/>.</p>"
                + "<p align=\"right\" style='margin-left:30px;font-size:14px;line-height: 150%;float: right;'><i>Full tutorial available <a target=\"_blank\" href='" + "tutorial/diva_tutorial.pdf" + "'>here</a>.</i></p>");

        infopanel.add(information);

        return infopanel;

    }
}
