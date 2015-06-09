/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.somclust.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
//import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import web.diva.client.DivaServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.client.view.core.InfoIcon;
import web.diva.client.view.core.SaveAsPanel;
import web.diva.shared.beans.InteractiveColumnsResults;
import web.diva.shared.beans.SomClustTreeSelectionResult;
import web.diva.shared.beans.SomClusteringResult;

/**
 *
 * @author Yehia Farag
 */
public class SomClustComponent extends ModularizedListener {

    @Override
    public String toString() {
        return "SomClust"; //To change body of generated methods, choose Tools | Templates.
    }

    private SelectionManager Selection_Manager;
    private String defaultTopTreeImgURL;
    private boolean update = true;
    private boolean clustColumn = true;

    private final DivaServiceAsync GWTClientService;

    private final SplitSideTreeImg sideTreeImg;
    private TopTreeImg upperTreeImg;
    private final SplitHeatmapImg heatMapImg;
    private final SplitInteractiveClusterColumnSelectionImg interactiveColImage;
    private final Image scaleImg;
    private final VerticalPanel clusterLayout = new VerticalPanel();
    private final HorizontalPanel topClusterLayout = new HorizontalPanel();
    private final ScrollPanel mainClusterPanelLayout = new ScrollPanel();
    private final HorizontalPanel middleClusterLayout = new HorizontalPanel();
    private final HorizontalPanel bottomClusterLayout = new HorizontalPanel();
    private final HorizontalPanel tooltipPanel = new HorizontalPanel();

    private final VLayout mainThumbClusteringLayout;
    private final HTML tooltip = new HTML();

    ///maxmize layout variables
    private final PopupPanel clusteringPopup;
    private final HorizontalPanel tooltipViewPortLayout;
    private final HTML maxmizeTooltip = new HTML();

    private MaxmizedSplitSideTreeImg maxSideTreeImg;
    private MaxmizedTopTreeImg maxUpperTreeImg;
    private final SplitHeatmapImg maxHeatMapImg;
    private final SplitInteractiveClusterColumnSelectionImg maxInteractiveColImage;
    private final Image maxScaleImg;
    private VerticalPanel maxClusterLayout = new VerticalPanel();
    private final HorizontalPanel maxTopClusterLayout = new HorizontalPanel();
    private final ScrollPanel framMaxMainClusterPanelLayout = new ScrollPanel();
    private final HorizontalPanel maxMiddleClusterLayout = new HorizontalPanel();
    private final HorizontalPanel maxBottomClusterLayout = new HorizontalPanel();

    private HandlerRegistration uperTreeReg, maxUpperTreeReg;
    private final HandlerRegistration sideTreeReg, maxSideTree1Reg, minSettingBtnReg, clusteringProcessBtnReg, maxmizeBtnReg, settingBtnReg, saveBtnReg, minmizeBtnReg;
    private final VLayout mainClusteringPopupBodyLayout;
    private SomClusteringResult somClusteringResults;

    public SomClustComponent(SomClusteringResult somClusteringResults, final SelectionManager Selection_Manager, DivaServiceAsync DivaClientService, boolean clusterColumn, int width, int height) {
        this.clustColumn = clusterColumn;
        this.GWTClientService = DivaClientService;
        this.somClusteringResults = somClusteringResults;
        tooltip.setStyleName("clustertooltip");
        mainThumbClusteringLayout = new VLayout();
        mainThumbClusteringLayout.setStyleName("somclustering");
        mainThumbClusteringLayout.setOverflow(Overflow.HIDDEN);
        mainThumbClusteringLayout.setHeight(height + "px");
        mainThumbClusteringLayout.setWidth(width + "px");
        mainThumbClusteringLayout.setMargin(0);
        mainThumbClusteringLayout.setMembersMargin(0);

        HorizontalPanel topLayout = new HorizontalPanel();
        mainThumbClusteringLayout.addMember(topLayout);
        topLayout.setWidth("100%");
        topLayout.setHeight("20px");
        topLayout.setStyleName("whiteLayout");
        Label title = new Label("Hierarchical Clustering");
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth("50%");
        title.setHeight("18px");

        topLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);

        HorizontalPanel btnsLayout = new HorizontalPanel();
        btnsLayout.setSpacing(2);
        btnsLayout.setWidth("52px");
        btnsLayout.setHeight("18px");
        btnsLayout.getElement().setAttribute("style", " margin-right: 15px;");
        topLayout.add(btnsLayout);
        topLayout.setCellHorizontalAlignment(btnsLayout, HorizontalPanel.ALIGN_RIGHT);
        topLayout.setCellVerticalAlignment(btnsLayout, HorizontalPanel.ALIGN_TOP);

        Label minSettingBtn = new Label();
        minSettingBtn.addStyleName("settings");
        minSettingBtn.setHeight("16px");
        minSettingBtn.setWidth("16px");
        btnsLayout.add(minSettingBtn);
        btnsLayout.setCellHorizontalAlignment(minSettingBtn, HorizontalPanel.ALIGN_RIGHT);
        Label maxmizeBtn = new Label();
        maxmizeBtn.addStyleName("maxmize");
        maxmizeBtn.setHeight("16px");
        maxmizeBtn.setWidth("16px");

        InfoIcon icon = new InfoIcon("Hierarchical Clustering", initInfoLayout(400, 620), 400, 620);
        btnsLayout.add(icon);
        btnsLayout.setCellHorizontalAlignment(icon, HorizontalPanel.ALIGN_RIGHT);

        btnsLayout.add(maxmizeBtn);
        btnsLayout.setCellHorizontalAlignment(maxmizeBtn, HorizontalPanel.ALIGN_RIGHT);

        mainThumbClusteringLayout.addMember(mainClusterPanelLayout);

        //add tooltip panel after clustering panel
        mainThumbClusteringLayout.addMember(tooltipPanel);
        tooltipPanel.setWidth("100%");
        tooltipPanel.setHeight("25px");
        tooltipPanel.setStyleName("whiteLayout");
        tooltipPanel.add(tooltip);
        tooltipPanel.setCellHorizontalAlignment(tooltip, HorizontalPanel.ALIGN_CENTER);
        tooltipPanel.setCellVerticalAlignment(tooltip, HorizontalPanel.ALIGN_BOTTOM);
        tooltip.setWidth("350px");
        tooltip.setHeight("24px");

        sideTreeImg = new SplitSideTreeImg(somClusteringResults.getSideTreeImg(), somClusteringResults.getRowNode(), 2, tooltip, somClusteringResults.getSquareL(), somClusteringResults.getSideTreeWidth(), somClusteringResults.getSideTreeHeight());

        heatMapImg = new SplitHeatmapImg(somClusteringResults.getHeatMapImg(), somClusteringResults.getRowNames(), somClusteringResults.getColNames(), somClusteringResults.getValues(), tooltip, 1, somClusteringResults.getSquareL(), somClusteringResults.getSquareW(), somClusteringResults.getHeatmapWidth(), somClusteringResults.getReIndexer(), Selection_Manager);
        interactiveColImage = new SplitInteractiveClusterColumnSelectionImg(somClusteringResults.getInteractiveColumnImgUrl().getInteractiveColumn(), somClusteringResults.getRowNames(), tooltip, 1, somClusteringResults.getSquareL(), somClusteringResults.getInteractiveColumnWidth(), somClusteringResults.getReIndexer(), Selection_Manager);

        scaleImg = new Image(somClusteringResults.getScaleImgUrl());

        mainClusterPanelLayout.setWidth("100%");
        mainClusterPanelLayout.setHeight((height - 50) + "px");
        mainClusterPanelLayout.setStyleName("frame");

        mainClusterPanelLayout.setAlwaysShowScrollBars(false);
        if (clustColumn) {
            topClusterLayout.setHeight(70 + "px");
            topClusterLayout.setWidth("20%");
            upperTreeImg = new TopTreeImg(somClusteringResults.getUpperTreeImgUrl(), somClusteringResults.getColNode(), 1, tooltip, somClusteringResults.getSquareL());
            spacer.setSize((somClusteringResults.getSideTreeWidth() + "px"), (70 + "px"));
            spacer.setStyleName("borderless");

            spacer2.setSize((somClusteringResults.getInteractiveColumnWidth() + "px"), (70 + "px"));
            spacer2.setStyleName("borderless");

            topClusterLayout.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
            topClusterLayout.add(spacer);
            topClusterLayout.setCellHorizontalAlignment(spacer, HorizontalPanel.ALIGN_LEFT);
            topClusterLayout.setCellWidth(spacer, spacer.getWidth() + "px");

            topClusterLayout.add(upperTreeImg);
            topClusterLayout.setCellHorizontalAlignment(upperTreeImg, HorizontalPanel.ALIGN_LEFT);
            topClusterLayout.setCellWidth(upperTreeImg, somClusteringResults.getTopTreeWidth() + "px");
            topClusterLayout.add(spacer2);
            topClusterLayout.setCellHorizontalAlignment(spacer2, HorizontalPanel.ALIGN_LEFT);
            topClusterLayout.setCellWidth(spacer2, somClusteringResults.getInteractiveColumnWidth() + "px");
        } else {
            topClusterLayout.setHeight(0 + "px");
        }

        mainClusterPanelLayout.add(clusterLayout);
        clusterLayout.setWidth("100%");
        clusterLayout.setHeight("99%");
        clusterLayout.setSpacing(0);
        clusterLayout.getElement().setAttribute("style", "overflow:hidden;");

        clusterLayout.add(topClusterLayout);
        clusterLayout.setCellHorizontalAlignment(topClusterLayout, VerticalPanel.ALIGN_CENTER);
        clusterLayout.setCellVerticalAlignment(topClusterLayout, VerticalPanel.ALIGN_MIDDLE);

        clusterLayout.add(middleClusterLayout);
        clusterLayout.setCellHorizontalAlignment(middleClusterLayout, VerticalPanel.ALIGN_CENTER);
        clusterLayout.setCellVerticalAlignment(middleClusterLayout, VerticalPanel.ALIGN_MIDDLE);

        clusterLayout.add(bottomClusterLayout);
        clusterLayout.setCellHorizontalAlignment(bottomClusterLayout, VerticalPanel.ALIGN_CENTER);
        clusterLayout.setCellVerticalAlignment(bottomClusterLayout, VerticalPanel.ALIGN_MIDDLE);

        initThumLayout(somClusteringResults);
        clustInfoLabel.setWidth(somClusteringResults.getSideTreeWidth() + "px");
        clustInfoLabel.setHeight("20px");
        clustInfoLabel.setStyleName("info");
        bottomClusterLayout.add(clustInfoLabel);
        bottomClusterLayout.add(scaleImg);
        Image spacer3 = new Image("images/w.png");
        spacer3.setWidth(somClusteringResults.getInteractiveColumnWidth() + "px");
        spacer3.setHeight("20px");
        bottomClusterLayout.add(spacer3);

        if (clustColumn && upperTreeImg != null) {
            uperTreeReg = upperTreeImg.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    int y = (int) (event.getY());
                    int x = ((int) (event.getX()));
                    if (upperTreeImg.isSelectedNode()) {
                        updateUpperTreeSelection(x, y);
                    } else {
                        Selection selection = new Selection(Selection.TYPE.OF_COLUMNS, new int[]{});
                        SomClustComponent.this.Selection_Manager.setSelectedColumns(selection);

                    }
                }
            });
        }

//       sideTreeReg=  sideTreeImg1.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                int y = (int) (event.getY());
//                int x = ((int) (event.getX()));
//                if (sideTreeImg1.isSelectedNode()) {
//                    updateSideTreeSelection(x, y);
//                }else{
//                     Selection selection = new Selection(Selection.TYPE.OF_ROWS, new int[]{});
//                    SomClustComponent.this.Selection_Manager.setSelectedRows(selection);
//                    
//                    }
//
//            }
//
//        });
        sideTreeReg = sideTreeImg.addDomHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                int y = (int) (event.getY());
                int x = ((int) (event.getX()));
                if (sideTreeImg.isSelectedNode()) {
                    updateSideTreeSelection(x, y);
                } else {
                    Selection selection = new Selection(Selection.TYPE.OF_ROWS, new int[]{});
                    SomClustComponent.this.Selection_Manager.setSelectedRows(selection);

                }
            }
        }, ClickEvent.getType());

        this.Selection_Manager = Selection_Manager;
        this.Selection_Manager.addSelectionChangeListener(SomClustComponent.this);
        classtype = 4;

        final UpdatedSomClustPanel clusteringSettingPanel = new UpdatedSomClustPanel();
        ///done with normal mode start setteing and maxmize mode
        ClickHandler settingClickHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (clusteringPopup.isShowing()) {
                    maxClusterLayout.getElement().setAttribute("style", "overflow-y: auto;  -moz-transform: rotate(90deg) scale(" + 1 + ");  -o-transform:rotate(90deg) scale(" + 1 + ");-ms-transform: rotate(90deg) scale(" + 1 + "); -webkit-transform:rotate(90deg) scale(" + 1 + ");  transform: rotate(90deg) scale(" + 1 + "); position: absolute;  top:" + top + "px;   left:" + 100 + "px; ");
                    maxSideTreeImg.setScale(1);
                    maxHeatMapImg.setScale(1);
                    maxInteractiveColImage.setScale(1);

                    zoomSlider.setValue(1.0);
                    nvigatorSlider.setValue(0.0);
                    nvigatorSlider.disable();
                    if (clustColumn) {
                        maxUpperTreeImg.setScale(1);
                    }
                    clusteringPopup.hide(true);
                }
                clusteringSettingPanel.center();
                clusteringSettingPanel.show();

            }
        };
        minSettingBtnReg = minSettingBtn.addClickHandler(settingClickHandler);

        clusteringProcessBtnReg = clusteringSettingPanel.getProcessBtn().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                clusteringSettingPanel.hide();
                clustColumn = clusteringSettingPanel.isClusterColumns();
                runSomClustering(clusteringSettingPanel.getLinkageValue(), clusteringSettingPanel.getDistanceMeasureValue(), clustColumn);

            }
        });
        maxmizeBtnReg = maxmizeBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {

                clusteringPopup.center();
                clusteringPopup.show();
                maxClusterLayout.getElement().setAttribute("style", "overflow-y: auto; -moz-transform: rotate(90deg) scale(" + 1 + ");  -o-transform:rotate(90deg) scale(" + 1 + "); -ms-transform: rotate(90deg) scale(" + 1 + "); -webkit-transform:rotate(90deg) scale(" + 1 + ");  transform: rotate(90deg) scale(" + 1 + "); position: absolute;  top:" + top + "px;   left:" + 100 + "px; ");
                maxSideTreeImg.setScale(1);
                maxHeatMapImg.setScale(1);
                maxInteractiveColImage.setScale(1);
                zoomSlider.setValue(1.0);
                if (clustColumn) {
                    maxUpperTreeImg.setScale(1);
                }
            }
        });

        /* the end of thumb layout*/
        /* the start of maxmize layout*/
        clusteringPopup = new PopupPanel(false, true);
        clusteringPopup.setAnimationEnabled(true);
        clusteringPopup.ensureDebugId("cwBasicPopup-imagePopup");

        mainClusteringPopupBodyLayout = new VLayout();
        mainClusteringPopupBodyLayout.setWidth(97 + "%");
        mainClusteringPopupBodyLayout.setHeight(80 + "%");

        HorizontalPanel maxTopLayout = new HorizontalPanel();

        mainClusteringPopupBodyLayout.addMember(maxTopLayout);
        maxTopLayout.setWidth("100%");
        maxTopLayout.setHeight("18px");
        maxTopLayout.setStyleName("whiteLayout");
        maxTopLayout.setSpacing(3);

        Label maxTitle = new Label("Hierarchical Clustering");
        maxTitle.setStyleName("labelheader");
        maxTopLayout.add(maxTitle);

        maxTitle.setWidth(300 + "px%");
        maxTopLayout.setCellHorizontalAlignment(maxTitle, HorizontalPanel.ALIGN_LEFT);

        HorizontalPanel maxTopBtnLayout = new HorizontalPanel();
        maxTopBtnLayout.setSpacing(1);
        maxTopLayout.add(maxTopBtnLayout);
        maxTopLayout.setCellHorizontalAlignment(maxTopBtnLayout, HorizontalPanel.ALIGN_RIGHT);
        maxTopBtnLayout.setWidth("85px");
        Label settingsBtn = new Label();
        settingsBtn.addStyleName("settings");
        settingsBtn.setHeight("16px");
        settingsBtn.setWidth("16px");
        maxTopBtnLayout.add(settingsBtn);
        maxTopBtnLayout.setCellHorizontalAlignment(settingsBtn, HorizontalPanel.ALIGN_RIGHT);
        settingBtnReg = settingsBtn.addClickHandler(settingClickHandler);

        Label saveBtn = new Label();
        saveBtn.addStyleName("save");
        saveBtn.setHeight("16px");
        saveBtn.setWidth("16px");
        maxTopBtnLayout.add(saveBtn);
        maxTopBtnLayout.setCellHorizontalAlignment(saveBtn, HorizontalPanel.ALIGN_RIGHT);

        InfoIcon maxIcon = new InfoIcon("Hierarchical Clustering", initInfoLayout(400, 620), 400, 620);
        maxTopBtnLayout.add(maxIcon);
        maxTopBtnLayout.setCellHorizontalAlignment(maxIcon, HorizontalPanel.ALIGN_RIGHT);

        Label minmizeBtn = new Label();
        minmizeBtn.addStyleName("minmize");
        minmizeBtn.setHeight("16px");
        minmizeBtn.setWidth("16px");
        maxTopBtnLayout.add(minmizeBtn);
        maxTopBtnLayout.setCellHorizontalAlignment(minmizeBtn, HorizontalPanel.ALIGN_RIGHT);

        ClickHandler saveHandler = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SelectionManager.Busy_Task(true, false);
//                Window.open(profilePlotMaxImage.getUrl(), "downlodwindow", "status=0,toolbar=0,menubar=0,location=0");
                String quality = "normal";
                GWTClientService.exportClusteringAsPdf(quality, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Selection_Manager.connError();
                        SelectionManager.Busy_Task(false, false);
                    }

                    @Override
                    public void onSuccess(String result) {
                        SaveAsPanel sa = new SaveAsPanel("Hierarchical Clustering", result);
                        sa.center();
                        sa.show();
                        SelectionManager.Busy_Task(false, false);
                    }
                });
            }
        };
        saveBtnReg = saveBtn.addClickHandler(saveHandler);
        minmizeBtnReg = minmizeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                maxClusterLayout.getElement().setAttribute("style", "overflow-y: auto; -moz-transform: rotate(90deg) scale(" + 1 + ");  -o-transform:rotate(90deg) scale(" + 1 + "); -ms-transform: rotate(90deg) scale(" + 1 + "); -webkit-transform:rotate(90deg) scale(" + 1 + ");  transform: rotate(90deg) scale(" + 1 + "); position: absolute;  top:" + top + "px;   left:" + 100 + "px; ");
                maxSideTreeImg.setScale(1);
                maxHeatMapImg.setScale(1);
                maxInteractiveColImage.setScale(1);
                zoomSlider.setValue(1.0);
                nvigatorSlider.setValue(0.0);
                nvigatorSlider.disable();
                if (clustColumn) {
                    maxUpperTreeImg.setScale(1);
                }
                clusteringPopup.hide(true);

            }

        });

        mainClusteringPopupBodyLayout.addMember(framMaxMainClusterPanelLayout);

        tooltipViewPortLayout = new HorizontalPanel();
        mainClusteringPopupBodyLayout.addMember(tooltipViewPortLayout);
        tooltipViewPortLayout.setWidth(100 + "%");
        tooltipViewPortLayout.setHeight("50px");
        tooltipViewPortLayout.setStyleName("whiteLayout");
        tooltipViewPortLayout.getElement().setAttribute("style", "overflow:auto");

        VerticalPanel tooltipLayout = new VerticalPanel();
        tooltipLayout.setStyleName("whiteLayout");
        tooltipViewPortLayout.add(tooltipLayout);
        tooltipLayout.setWidth("270px");
        tooltipLayout.setHeight("50px");

        tooltipLayout.add(maxmizeTooltip);
        maxmizeTooltip.setWidth("270px");
        maxmizeTooltip.setStyleName("clustertooltip");
        tooltipViewPortLayout.add(maxBottomClusterLayout);
        tooltipViewPortLayout.setCellHorizontalAlignment(maxBottomClusterLayout, HorizontalPanel.ALIGN_LEFT);

        framMaxMainClusterPanelLayout.setWidth("100%");//(RootPanel.get("diva_content_area").getOffsetHeight()-100)+"px");//((sideTreeImg1.getWidth()+heatMapImg.getWidth()+10)+"px");
        framMaxMainClusterPanelLayout.setHeight("100%");//((heatMapImg.getHeight()+70+50)+"px");        
        framMaxMainClusterPanelLayout.setAlwaysShowScrollBars(false);

        maxSideTreeImg = new MaxmizedSplitSideTreeImg(somClusteringResults.getSideTreeImg(), somClusteringResults.getRowNode(), 1, maxmizeTooltip, somClusteringResults.getSquareL(), somClusteringResults.getSideTreeWidth());//"data:sideTreeImg1/png;base64,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");

        maxHeatMapImg = new SplitHeatmapImg(somClusteringResults.getHeatMapImg(), somClusteringResults.getRowNames(), somClusteringResults.getColNames(), somClusteringResults.getValues(), maxmizeTooltip, 2, somClusteringResults.getSquareL(), somClusteringResults.getSquareW(), somClusteringResults.getHeatmapWidth(), somClusteringResults.getReIndexer(), Selection_Manager);

        maxInteractiveColImage = new SplitInteractiveClusterColumnSelectionImg(somClusteringResults.getInteractiveColumnImgUrl().getInteractiveColumn(), somClusteringResults.getRowNames(), maxmizeTooltip, 2, somClusteringResults.getSquareL(), somClusteringResults.getInteractiveColumnWidth(), somClusteringResults.getReIndexer(), Selection_Manager);

        maxScaleImg = new Image(somClusteringResults.getScaleImgUrl());

        final int maxClusterheight = somClusteringResults.getSideTreeWidth() + somClusteringResults.getHeatmapWidth() + somClusteringResults.getInteractiveColumnWidth();

        framMaxMainClusterPanelLayout.setStylePrimaryName("scrolx");
        framMaxMainClusterPanelLayout.getElement().setAttribute("style", "overflow-y: auto; position: relative; zoom: 1; width: 100%; height:100%;");

//        framMaxMainClusterPanelLayout.add(maxClusterLayout);
        maxClusterLayout.setStyleName("rotate");
        int toptreewidth = 0;
        if (clustColumn) {
            toptreewidth = 70;
        }
        top = (toptreewidth + somClusteringResults.getSideTreeHeight() - (somClusteringResults.getSideTreeWidth() + somClusteringResults.getHeatmapWidth() + somClusteringResults.getInteractiveColumnWidth()) + somClusteringResults.getSideTreeWidth()) * -1;

        maxClusterLayout.setWidth("10%");
        maxClusterLayout.setHeight("2%");
        final VerticalPanel vp = new VerticalPanel();
        framMaxMainClusterPanelLayout.setWidget(maxClusterLayout.asWidget());
        vp.setWidth(maxClusterheight + "px");
        vp.getElement().setAttribute("style", "overflow-y: hidden;");
//       vp.add(maxClusterLayout);

        maxClusterLayout.add(maxTopClusterLayout);

        maxClusterLayout.setCellHorizontalAlignment(maxTopClusterLayout, VerticalPanel.ALIGN_RIGHT);
        maxClusterLayout.setCellVerticalAlignment(maxTopClusterLayout, VerticalPanel.ALIGN_MIDDLE);

        maxClusterLayout.add(maxMiddleClusterLayout);
        maxClusterLayout.setCellHorizontalAlignment(maxMiddleClusterLayout, VerticalPanel.ALIGN_RIGHT);
        maxClusterLayout.setCellVerticalAlignment(maxMiddleClusterLayout, VerticalPanel.ALIGN_MIDDLE);

        //top clustering layout include spacer 1 and rotated side tree
        if (clustColumn) {
            maxTopClusterLayout.setHeight(70 + "px");
            maxTopClusterLayout.setWidth("10%");//(sideTreeImg1.getWidth()+upperTreeImg.getWidth()+interactiveColImage.getWidth())+"px");
            maxUpperTreeImg = new MaxmizedTopTreeImg(somClusteringResults.getUpperTreeImgUrl(), somClusteringResults.getColNode(), 2, maxmizeTooltip, somClusteringResults.getSquareL());
            maxSpacer.setSize((somClusteringResults.getSideTreeWidth() + "px"), (70 + "px"));
            maxSpacer.setStyleName("borderless");

            maxSpacer2.setSize((somClusteringResults.getInteractiveColumnWidth() + "px"), (70 + "px"));
            maxSpacer2.setStyleName("borderless");

            maxTopClusterLayout.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);

            maxTopClusterLayout.add(maxSpacer);
            maxTopClusterLayout.setCellHorizontalAlignment(maxSpacer, HorizontalPanel.ALIGN_LEFT);
            maxTopClusterLayout.setCellWidth(maxSpacer, somClusteringResults.getSideTreeWidth() + "px");

            maxTopClusterLayout.add(maxUpperTreeImg);
            maxTopClusterLayout.setCellHorizontalAlignment(maxUpperTreeImg, HorizontalPanel.ALIGN_LEFT);
            maxTopClusterLayout.setCellWidth(maxUpperTreeImg, somClusteringResults.getTopTreeWidth() + "px");
            maxTopClusterLayout.add(maxSpacer2);
            maxTopClusterLayout.setCellHorizontalAlignment(maxSpacer2, HorizontalPanel.ALIGN_LEFT);
            maxTopClusterLayout.setCellWidth(maxSpacer2, somClusteringResults.getInteractiveColumnWidth() + "px");
        } else {
            maxTopClusterLayout.setHeight(0 + "px");
        }

        initMaxmizeLayout(somClusteringResults);
        maxClustInfoLabel.setWidth(280 + "px");
        maxClustInfoLabel.setHeight("64px");
        maxClustInfoLabel.setStyleName("maxInfo");//       
        maxBottomClusterLayout.add(maxClustInfoLabel);

//            maxBottomClusterLayout.setCellWidth(maxClustInfoLabel, maxClustInfoLabel.getWidth() + "px");
        maxBottomClusterLayout.add(maxScaleImg);

        nvigatorSlider = new Slider();
        nvigatorSlider.setMinValue(0.0);
        nvigatorSlider.setMaxValue(100.0);
        nvigatorSlider.setShowRange(false);
        nvigatorSlider.setShowValue(false);
        nvigatorSlider.setNumValues(100);
        nvigatorSlider.setValue(0.0);
        nvigatorSlider.setWidth(300);
        nvigatorSlider.setLeft(25);
        nvigatorSlider.setRoundValues(false);
        nvigatorSlider.setRoundPrecision(2);
        nvigatorSlider.setShowTitle(false);
        nvigatorSlider.setVertical(false);
        nvigatorSlider.disable();

        maxBottomClusterLayout.add(nvigatorSlider);
        nvigatorSlider.draw();

        nvigatorSlider.setBackgroundImage(somClusteringResults.getInteractiveColumnImgUrl().getNavgUrl());
        nvigatorSlider.addValueChangedHandler(new ValueChangedHandler() {

            @Override
            public void onValueChanged(ValueChangedEvent event) {
                if (event.isLeftButtonDown()) {
                    double maxScroll = (double) framMaxMainClusterPanelLayout.getMaximumHorizontalScrollPosition();
                    int sp = (int) ((event.getValue() * maxScroll) / 100.0);
                    framMaxMainClusterPanelLayout.setHorizontalScrollPosition(sp);
//                    navControl = false;
                }

            }
        });

        //zoom slider
        zoomSlider = new Slider();
        zoomSlider.setMinValue(1.0);//0.1);  
        zoomSlider.setMaxValue(5.0); //3.0 
        zoomSlider.setShowRange(true);

        zoomSlider.setShowValue(false);
        zoomSlider.setNumValues(9);//60  
        zoomSlider.setWidth(200);
        zoomSlider.setLeft(25);
        zoomSlider.setValue(1.0);
        zoomSlider.setRoundValues(false);
        zoomSlider.setRoundPrecision(2);
        zoomSlider.setTitle("zoom");
        zoomSlider.setVertical(false);
        zoomSlider.setMaxValueLabel("+");
        zoomSlider.setMinValueLabel("-");

//        zoomSlider.setBackgroundImage(interactiveColImage.getUrl());
        zoomSlider.addValueChangedHandler(new ValueChangedHandler() {

            @Override
            public void onValueChanged(ValueChangedEvent event) {
                if (nvigatorSlider.isDisabled()) {
                    nvigatorSlider.enable();
                }
                double sp = (double) framMaxMainClusterPanelLayout.getHorizontalScrollPosition();
                double maxScroll = (double) framMaxMainClusterPanelLayout.getMaximumHorizontalScrollPosition();
                double vp = (sp / maxScroll);
                vp = vp * 100.0;
                nvigatorSlider.setValue(vp);
                nvigatorSlider.draw();
//                maxSideTreeImg.getElement().setAttribute("style","zoom:"+event.getValue()+";");
//                maxUpperTreeImg.getElement().setAttribute("style","zoom:"+event.getValue()+";");
//                maxHeatMapImg.getElement().setAttribute("style","zoom:"+event.getValue()+";");
//                maxClusterLayout.getElement().setAttribute("style","overflow-y: hidden; zoom:"+event.getValue()+"; -moz-transform:"+event.getValue()+";");
//                  vp.getElement().setAttribute("style","overflow-y: hidden; zoom:"+event.getValue()+";");
//                vp.getElement().setAttribute("style","	-webkit-transform: scale("+event.getValue()+","+ event.getValue()+");-moz-transform: scale("+event.getValue()+","+ event.getValue()+");-ms-transform: scale("+event.getValue()+","+ event.getValue()+");-o-transform: scale("+event.getValue()+","+ event.getValue()+"),transform: scale("+event.getValue()+"," +event.getValue()+")");

                maxClusterLayout.getElement().setAttribute("style", "overflow-y: auto;-moz-transform: rotate(90deg) scale(" + event.getValue() + ");  -o-transform:rotate(90deg) scale(" + event.getValue() + "); -ms-transform: rotate(90deg) scale(" + event.getValue() + "); -webkit-transform:rotate(90deg) scale(" + event.getValue() + ");  transform: rotate(90deg) scale(" + event.getValue() + "); position: absolute;  top:" + top + "px;   left:" + 100 + "px; ");
                maxSideTreeImg.setScale(event.getValue());
                maxHeatMapImg.setScale(event.getValue());
                maxInteractiveColImage.setScale(event.getValue());
                if (clustColumn) {
                    maxUpperTreeImg.setScale(event.getValue());
                }
//            maxSideTreeImg.onZoom();
//            maxSideTreeImg.setHeight((sideTreeImg1.getHeight()*event.getValue())+"px");

            }
        });
//            zoomSlider.setShowTitle(false);
//        zoomSlider.setShowValue(true);
        maxBottomClusterLayout.add(zoomSlider);
        zoomSlider.draw();

        maxBottomClusterLayout.setCellHorizontalAlignment(nvigatorSlider, HorizontalPanel.ALIGN_LEFT);
        maxBottomClusterLayout.setCellVerticalAlignment(nvigatorSlider, HorizontalPanel.ALIGN_TOP);
        maxBottomClusterLayout.setCellHorizontalAlignment(zoomSlider, HorizontalPanel.ALIGN_LEFT);
        maxBottomClusterLayout.setCellVerticalAlignment(zoomSlider, HorizontalPanel.ALIGN_TOP);
        maxBottomClusterLayout.setCellHorizontalAlignment(maxClustInfoLabel, HorizontalPanel.ALIGN_LEFT);
        maxBottomClusterLayout.setCellVerticalAlignment(maxClustInfoLabel, HorizontalPanel.ALIGN_MIDDLE);
        maxBottomClusterLayout.setCellHorizontalAlignment(maxScaleImg, HorizontalPanel.ALIGN_LEFT);
        maxBottomClusterLayout.setCellVerticalAlignment(maxScaleImg, HorizontalPanel.ALIGN_MIDDLE);

//            maxBottomClusterLayout.setCellWidth(maxUpperTreeImg, maxUpperTreeImg.getWidth() + "px");
        if (clustColumn && maxUpperTreeImg != null) {
            maxUpperTreeReg = maxUpperTreeImg.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (maxUpperTreeImg.isSelectedNode()) {
                        int x = maxUpperTreeImg.getXcor();
                        int y = maxUpperTreeImg.getYcor();

                        updateUpperTreeSelection(y, x);
                    } else {
                        Selection selection = new Selection(Selection.TYPE.OF_COLUMNS, new int[]{});
                        SomClustComponent.this.Selection_Manager.setSelectedColumns(selection);

                    }
                }
            });
        }

        ClickHandler sideTreeClickHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (maxSideTreeImg.isSelectedNode()) {
                    int x = maxSideTreeImg.getXcor();
                    int y = maxSideTreeImg.getYcor();
                    updateSideTreeSelection(y, x);
                } else {
                    Selection selection = new Selection(Selection.TYPE.OF_ROWS, new int[]{});
                    SomClustComponent.this.Selection_Manager.setSelectedRows(selection);

                }

            }

        };

        maxSideTree1Reg = maxSideTreeImg.addDomHandler(sideTreeClickHandler, ClickEvent.getType());
        somClusteringResults = null;
        clusteringPopup.setWidget(mainClusteringPopupBodyLayout);
        mainClusteringPopupBodyLayout.setStyleName("modalLayout");

        if (framMaxMainClusterPanelLayout.getMaximumHorizontalScrollPosition() <= 0) {
            nvigatorSlider.disable();

        } else {
            nvigatorSlider.enable();
            nvigatorSlider.setValue(0.0);
        }

        if (framMaxMainClusterPanelLayout.getMaximumVerticalScrollPosition() > 0) {
            nvigatorSlider.enable();

        }
        framMaxMainClusterPanelLayout.addScrollHandler(new ScrollHandler() {
//            boolean resize = true;
            @Override
            public void onScroll(ScrollEvent event) {

                if (nvigatorSlider.isDisabled()) {
                    nvigatorSlider.enable();
                }
                double sp = (double) framMaxMainClusterPanelLayout.getHorizontalScrollPosition();
                double maxScroll = (double) framMaxMainClusterPanelLayout.getMaximumHorizontalScrollPosition();
                double vp = (sp / maxScroll);
                vp = vp * 100.0;
                nvigatorSlider.setValue(vp);
                nvigatorSlider.draw();
//                maxmizeTooltip.setVisible(true);
//                 if (resize) {
//                     resize=false;
//                maxClusterLayout.getElement().setAttribute("style", "overflow-y: auto; -ms-transform: rotate(90deg) scale(" + 1.1 + "); -webkit-transform:rotate(90deg) scale(" + 1.1 + ");  transform: rotate(90deg) scale(" + 1.1 + "); position: absolute;  top:" + top + "px;   left:" + 100 + "px; ");
//                maxSideTreeImg.setScale(1.1);
//                if (clustColumn) {
//                    maxUpperTreeImg.setScale(1.1);
//                }
//            }

            }
        });

    }
//    private int maxVerticalScrol =-1000000000;
    final Slider nvigatorSlider, zoomSlider;
    private final Image spacer = new Image("images/w.png"), spacer2 = new Image("images/w.png"), maxSpacer = new Image("images/w.png"), maxSpacer2 = new Image("images/w.png");//,rotatSpacer = new Image("images/w.png"),rotatSpacer2 = new Image("images/w.png");
    private final Label clustInfoLabel = new Label(), maxClustInfoLabel = new Label();
//    private boolean navControl=false;

    private void initThumLayout(SomClusteringResult somClusteringResults) {
        if (clustColumn) {
            upperTreeImg.setUrl(somClusteringResults.getUpperTreeImgUrl());
            defaultTopTreeImgURL = somClusteringResults.getUpperTreeImgUrl();
            upperTreeImg.setVisible(clustColumn);
            spacer.setVisible(clustColumn);
            spacer2.setVisible(clustColumn);
        }

//        sideTreeImg1.setUrl(somClusteringResults.getSideTreeImgUrl());//"data:sideTreeImg1/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAAMPCAIAAAAfPw1YAAAUM0lEQVR42u3d2XYjqRIFUP//T/u+3nZpSCACAtgsP3S7pJy0fQhQDj+/mpbQfkpshHZeKwLLn7jEAkt78RH++QFLC8IksbQwWP/+t8TSBju7v7D++0uwtPZ8evcbiaW1JdOrKur346cGlvaMy0tY73/A0nphfV4MWNpTWC0/YGlDySSxtCmwJJYWCcs8lva9chrhKLG0t5MIA0GlxtKCYP37Loml9cB62W/+80uwwGqH9fnFEkt7BOvzSQ3vFgzWpUO/518t//nNu7NlzLyLpbZ8+gDrwzrBAqvt7b4r1FJgPVwnWGANlWgSC6yoNJJY2gNYOT9g3Q0rb51ggRW/OokFVqwn3xWCFcfo1RfSYIEVtMz//gYssOJgGRWCFdD3GRWCFb+Qb+c4gAVWL6yP6QUWWMMLeZVeYB3h5utPyAIfvl5i3RJIHbC6FyKxwEqBpcYCKx6WUSFYPbBaijmwwIobRUossDoHgGossJ7C6lugM0jBSoElscAKgNU+WQoWWM9gvfvfN+bAAqsL1ocL8CUWWJ2wHvwTWGA9G/Q1jhDBAitiFRILrBhY5rHAioHlnHew4mF9fqPEuhdW1MmlT2YcJNYtsJLe+zG9wAIrYgPMvIMVD+sVMrDAytkosMAKjyuJBVbQqNA572CFwZJYYI2+18w7WFmwJBZYk2CpscBqrsH7vvmRWGBlrwIssEJXIbHAilmFq3TAyirFJBZY09YOFlgpYQZWeTThtxids+FgbZ9Gy4+exAIrS5LEAit+q5zzDlYWLPNYYMUMIL69GCyw2pfvjn5gTYVlVAjWKCznY4E1YxskFliRpboaC6zUUt2oEKwgWN/KLLDA6oUlscBKGQOqscCKHwNKLLDSYamxwIqE9bJblFhghS3N+VhgjVflD5cD1t2w0vIPLLCiYUkssLI6VokF1tDSXhZnEgusAFhv3ggWWI1L+zaDJbHAGn6E/fsXgwVWKCyJdQus1ps+NN0qwvlYV8OKjbpnjsECK2GTJNa0HexrEgusnB2sCetBBQYWWNF9t8QCK3GXwToBVt+NJAMPu/OxDoTVvYTYGktigRU/KpRYYDX0oQPnLoN1MaxMqWCBFVRjuT8WWHn7KbHASt5lsM6HteIHrNNhrdplsMDKKLPAAiu6q5VYYMXAklhg5UWUGgus6IiSWGDl9X1qLLBS+j6JBVZo3/fuTWCBFQlLYoH1qNf7t+9zlQ5YMavzLB2w1sNSY90LK+M2Ic5uAGvOBWFggRW0OokFVmcv2XL1DlhgpZRuYJWHlfEzYZfBum4HJRZYdWGpsezgEKzn/anEAivgxZ4JbQdHBwRdCwHLDqYUZGDZwZT5DrDsYM4uO+52MHA3/+8W9Y67HYzC5P5YdjBK0n8wSSw7GLUvP66EPnUHf6a3R2sH6wBYBfcFLLAiVyexwMrqeSUWWJnswAIrZavAAktigVX06KmxwMoq4SUWWPEr/fG8QrCiUurLHARYYHWs6Mc572DNhCWxwGouz9+eIWPmHayRxT5ZkcQCKwWWGgusHlidJ22BBVbUYsE6E1b4maLNJT9Yp8Kauc1GhWAFbPOnWQaJBdYIrO+/AQus57H0Lo+MCsGatDtg7bGDrcO3VbAk1n6wSiWW7wrBmr2nEguszF0GK6lHiB0kSay7YEms3zenLIMFVuRxkFhgRXb6aiyw4vdUYoGVBUuNBVb6nkossBI/ILDAelStN9f1YIH19e2tax9NrKQb8YJVGdbTc7MqJA1Ye8F6+U9/qIEF1iis16e9gwXWCKy3bwELrCewvhbEkVUzWJfA6lgyWGD1ry5rnA8WWBILrLDVPbrAEKzLYY10ZS+7RYkF1ujb/7UosY6CNVI9R/Wk5rFOg5WxbX2wjArByq39JRZY/bASz1cB63JYEgusAFgP73IjscBqhvXuf4Ov0gHrclhZ9yAF62BYTRV58D1IwToVVusrjQrBmrQXYIEV+UqJBVbPK59fyQcWWDk3PALrHlgzrxYG6y5Y4dvmu0Kwss6QkVhgxZ/T9/ISe4kFVgCsl5bAAqvn1Kt3y5FYYLXBej7klFhgRcL6m21ggRUIS40F1ttffp2WSr9HI1iBr4z9pAZhBYwcwaoDq05ijeypxAIrBpazG8Dqh/Wwm5ZYYIVVVPFP/wILrLd5BtapsJ4+WDDnDC2wzoQ1spCRdUkssGLW5Zx3sLJgSSywYmA5gxSsLFjv/ltigRUG6/VMKVhgPZyqaHucDlhgZSwHLLDCliOxwIpcjhoLrLDP68vt/CQWWEmLBat5QBT1WJGTYDm7IXJLwDIqBGvScZZYYGX1hhLrRlh5TWLdC2vmcQMLrJyBIVhgGRWCVRuTxJoPa3C6tSws52OthDW+tMqwJBZYKZMUaiyw0jdMYoEVtmGuKwSrs+/reS9YYH3t3XoWAhZYTbCCT1UDC6ymLQELrMcDvQePHJdYYH3q17rZSSyw3i62b35BYoE1BOvhi8ECa/QgGBWCFQ/LqBCsdFgS60ZYg/dRbp0WBesiWBN2QWKBlXvkwQIrIa4kFliBRZvE2hJW1MXKgYuVWNvDKrj8L38JYIEVO38hscDKXB1YYKWkF1j3wJpwGyOJdR2s2WrBAkuNdTisoxpYRWAd1sACKyd0wQIr0JPEAiuyEJRYYMUfH8/SASsLllEhWKMdn1EhWPFHo+GkMbDAGoHlKp3bYcV8tfz5kq+zE2vaVxHbwcp4y0X3bhi8QACsVli3jArBmgnrolEhWIGwnN0AVq2tBQssiQXWGAI1FljxWz4HlsQCK3IHr3iWDlgdL+i7L/ddT7EHqw/WyFuuuK4QrDxYj74llFhgdcB6ukCw7oEVcvespwsE6xJYIW+/+gxSsFJhmXmfActF7RJrvxAqmFgNf1dggfUbdOKyxAIrZY0SC6yYNd71LB2wFm6DxAIreXvAAitlSgUssDIWC9Y5sBbOu0qsY2HNDMJL7/MO1oQglFhgrSm8wAIrp54DC6wUfGCBlZJkYIEV2CdKLLBiSiuJBVb8gf1wuMACq2HQ1zA8BAushweh7YIAsMB6DsvZDWB1dnbjKiTW1bDyDqDEAivgILx9xBxYYA3VUu+msorAKnJN8AGwJtTWjyr9OrAk1rTdjz3apUeFYG0Kq/qoEKytYUkssFKK2rrnvINVYfclViKsA1oJ32BdNUc1L7TAAisq1CUWWPHLl1hgjdaaaiyw4hfy9KJ7sM6DlTq0NI+lzTuGEkubdAzB0nL6U7C0FJpgacc+gAWsA2YoJJb26IhJLLACKIQfRrD0aL+BsCQWWGGwSl9XCNZesL4/KBossHoeR/jtcfZggdWztO+PswcLrIdLa5IDFlgNsL6uS2KBFQzLqBCsLFgSC6zOoeLDf5VYYAWPEyUWWPGw1FhgZS1fYoGVu3ywwBoq552PBdbcjQELrJRgAwus8I5SYoEVWXVJLLDi12jmHaz4lPqteX+s3zm3tL+grU2pcon162LA2sew6avoYxMLrFUpJbHAGoK1zcw7WPXr1OYlgAXW10V13DkSLLBGYUkssPphNTyeSWKBNb6ov9/klPquEKytYdX9rhCstccwpPiu+F0hWMthjS/KqBCsFFhGhWAlwpJYYEXCKnpHP7C2g/Wu2JdYYI3C+rIEsC6B1TN7/vgtviu8Glbgwd/jjn5g7QjL+VhgxcP6vjFggRV7tZXEOhBW92Oeo1YqseRZ2xjwiVeJBVb6YsECK6f/BQuslNWBpbQ/9pnQYB3oGCwtJQXB0kYMSSwt/gP6dI4DWNoILDPvWvoHJLG0nskIo0It/vhLLG0qLDWWNgrL2Q1ac/0UeIWPxJJGAcffzDtYU49/IVjawlMNAu4CUjOxtOWJFcsXLLBy+hywwEpZKVimG04+g1Q7TTNYWkqwgWXKI+Nxh2AJYIkFVj4siQVW7dDySYBlVAiWxALr7iMAVvDumLOQWFmwJJbEAkuNBZZRoRpLA0tiSSywNirOHMTwXuwiPR8e8wTWnwN3VWJF/c1ILLD6t7btxWCBlRJvYB0Gq0hFeA6sIqV3BVixL759HivqIvHbYCUhAetqWIMLlFhbwioykdY5AQFWZVjFPxSJBdb0jwMssFKG2GCdB6vEtAtY4bBqXvMefjAl1l2JNXNAKrHAmo4PLLCMCsEqH1QSawtY+14nAVZdWFuWVhILLDUWWBuGFlhXwZr3DBWwboOVtBCJBVZW7EmsdJ2X3HNbYim9Z+8gWGDl9Ixg3QZrTvcK1l2w5mEFCyw1Flj7dKZg3QzL2Q1gVTloEusQWLvOl4JVH1blhUsssLLiUGKBNZEmWEl7dMBlq2qsorD2SixPpgArC4rEAiv+jcH3mAQLrL6Ek1jL6ozlpc/Kggys4om16QaABVZOgoJ1MCz3bgBrmyaxwFr6cYCVB8szocESMxILrC3GAWDpvzKcgKVJLLDUWGDdHl1gaU16JJYWfyQf3ctPYl1dXPc9N9XdZmTMtKVJLLDmLQ0ssHI6VrDAStkksM6ovsttP1gHbPy8m2w/lg0WWAFLllhgNfe8aiyw4l//5YniRoVgdcPqnKMH6wxY5a7SBmvtNMGS+YIJqwOrxEoPgCWxwJrxRB2wboQ1AytYYKmxam1M7PBq43AyKgQrqvaSWNvA2ujCVIm1DazzjiFYYOUEJ1hgGRVWhKVJrO3XKLFOhrX745PUWHVhHdwVSqwSsCSWxAJLjaUrNCq8GZYmsUxfSSyJJbHA0n7dHwusvBogPrGOnNw7ElbeEa54+xuJtdHxaXsaOVhgpWQeWDvC2uBia7A2hbV2syUWWM1vCck/sMDqeaXEAqunLFNj7Q2r5qRdTKcJ1lpYBXc25qk7YIGVQhAssFKOElhgpaQXWDMr7rC7qJc/tw+szg/1mJkLiQXWVh8HWGCllAdggRVSxkkssOK3TWKVg3XsFVxgLYS19fF0dgNYWWl6UWIddkXQxvLOgyWxSnwcYIGVMp4A67ChXBESYB01+bRk7yQWWFl5LLHAmmURLLCMCsHap+sEC6zBSktinQlr7aSDxDoZVsFlggVWTgqCteSDPP4bcbCWwVq4PU7/ACsLlsQCKyV+0tcI1u6wktY+yAasZTVNfVjP/0liValplsMauvfVhdcVgrVkNyUWWJOqQLDAihxsSiywko8SWHP+lIPz4JJLMq6Ctd2slcQ6GZZnQoMlsSSWxJJYEktigQXWcCqDdQks93kHa8ttkFh7f6i71NNg7QerQmIZFYLV/EbPhAYrqw+VWGAV3Vqw9hvq77G1YEkso0Kwdni2qsQCK6pnlFgnwKp2poLEOgHWLhsPFlg5GQkWWEaFG8NyBilYmsQCq2yCgnVnRyaxSsA6L7GMCsFa3xVKLLDWxJvEAisr3iQWWFPwgQWWUSFYdQt8ibUS1j1zXWDNhiWxwLoalhoLrJIhBxZYgX2lxAIroMCSWGDFb+fn14A1e45gl4He6NLAuiexmm44Y1QI1hpYFyVW0h5VgzWhIx5f2tXnRu4Lq8LBuSWxwCrVEYMFlsSaPjUQfpTWbs/Mw3ImrPopeMD23JhYYK3fSLDAciU0WFkOJBZY6yNQYoGVFYESC6xFEMECy6gQrH26S7DugTVz5AhWIViVr0GVWHvD2nr5EuteWO6aDJZR4QWwLvk7l1hTYc3c+OVEJBZY8YuKvNoCLLD6ck5igbWiGgOr2sYfMg4A68i2/tsCsMAaDFGJdReslQ+EllhaUvKBpeUkH1iaUSFY+wQbWFpIzyixtPhaXmJpowW7GkuLn2L4/E8SSyB1zhU8fxdYAqn5BRJLGnV+SxOwJWBdlUYPE8s572DFw4rZErAuhOW6wtth7fuBgrV9As3v5h65BwuskciUWGDFb4DEughWkSocrKNgzd8AibXlEG+L24RIrP3ip/5fvsQCa+7OggVWSi8JFljdtaDEWlZ6D/4Zr4IlsabCWpJYS5oa63BYW3TZEgusrJi8JbFqdhCnnigmsSTWxA8CrJmwLmpgyRuJtf08lsQCS5NYYBUcfYOl9QGSWFr8kOXFndYkltbUqT1/18mJZV4g9nD1Pw8MLLAyDiZYYPVeOXjVqBCsIocLrNtr86znBYN1eSCNHK4PRsECa+g50BILrABYz98LFlj9Qz2jwnsL81UPMAfr8D3NGvSZxwJrzbaBBVZKeoEFVp8eiQVW/PKvOx/rAFixJXLSJRHOxzp5+7P3tG1+QWKBNWH5YIGV042CBVbKXoAFVkqqgbU1rLIX04Nl+yXWhh/MtYUvWOU2RmKBVQ6WGgusLFgSC6wTYKmxwJobYGCBFdshSiywIksriQVW/H69OO8PrIKwqp1V0HYNtMQ6b2Om3ePeOe9gxefTr6t0wJqTTxILrEd5E1D2gXUnrPTYAwuskG2WWIfDCngAfcRMB1i/GYe4+J9uxlZJrKmLugeWGgus32Wn/4F1NqwZnwJYYKUEIVhgZXSAYIEVtpESC6z82QewwEo5JwKsk2BNmxww834drNh39Z8BARZYTTexfbgisMDqhGXmHayhysyoEKz41bUuUGKBFbbA11euggXW+PyFxFoAq8LdquYfcLAK5c2mG2xUCNbELQELrJTQAguskKpRYoE1OuaQWGDFL+fpNbFHwrr2YpPAx6WMHl6JdWdiZd++AazTYK29yl5inQmrzq6BdS8sV0KDteGnABZYKeEHFljjvafEAmuo5JJYYMXvadPRAAusBlhGhWA1azAqBCt+F/ov85JYYHW/zDOhz4eVNAcef7IDWNvBmr8LnqUDVhYs3xWCVWIXwDof1poG1ipYZ39kYK2EdfJAFSywxvNVYoHV3zVLrFqPlYutllJhRQ4awNprO1dBNyo8HNYu2wOW7czJQrBsZ8regXVIQlRrYGkSC6x9QtTR0TI8+OPWJJa2T/sfzLKROygb1PEAAAAASUVORK5CYII=");
//        heatMapImg .setUrl(somClusteringResults.getHeatMapImg());
        heatMapImg.updateTooltips(somClusteringResults.getRowNames(), somClusteringResults.getColNames(), somClusteringResults.getValues());
        interactiveColImage.updateTooltips(somClusteringResults.getRowNames());
        middleClusterLayout.setHeight(somClusteringResults.getSideTreeHeight() + "px");
        middleClusterLayout.add(sideTreeImg);
        middleClusterLayout.setCellWidth(sideTreeImg, somClusteringResults.getSideTreeWidth() + "px");
        VerticalPanel heatmapLayout = new VerticalPanel();
        heatmapLayout.add(heatMapImg);
        middleClusterLayout.add(heatmapLayout);
        middleClusterLayout.setCellWidth(heatmapLayout, somClusteringResults.getHeatmapWidth() + "px");

        middleClusterLayout.add(interactiveColImage);
        middleClusterLayout.setCellWidth(interactiveColImage, somClusteringResults.getInteractiveColumnWidth() + "px");

        clustInfoLabel.setText("Distance metrics: " + somClusteringResults.getDistanceMeasure() + " - " + "Linkage: " + somClusteringResults.getLinkage());

    }

    private void initMaxmizeLayout(SomClusteringResult somClusteringResults) {
        if (clustColumn) {
            maxUpperTreeImg.setUrl(somClusteringResults.getUpperTreeImgUrl());
            maxUpperTreeImg.setVisible(clustColumn);
            maxSpacer.setVisible(clustColumn);
            maxSpacer2.setVisible(clustColumn);
        }

        maxSideTreeImg.setUrl(somClusteringResults.getSideTreeImg());
        maxHeatMapImg.updateTooltips(somClusteringResults.getRowNames(), somClusteringResults.getColNames(), somClusteringResults.getValues());

        maxInteractiveColImage.updateTooltips(somClusteringResults.getRowNames());
        maxScaleImg.setUrl(somClusteringResults.getScaleImgUrl());

//        maxMiddleClusterLayout.setWidth("20%");
//        maxInteractiveColImage.setSize("20px", sideTreeImg1.getHeight() + "px");
        maxMiddleClusterLayout.setHeight(somClusteringResults.getSideTreeHeight() + "px");
        maxMiddleClusterLayout.add(maxSideTreeImg);
        maxMiddleClusterLayout.setCellWidth(maxSideTreeImg, somClusteringResults.getSideTreeWidth() + "px");
        VerticalPanel maxHeatmapLayout = new VerticalPanel();

        maxHeatmapLayout.add(maxHeatMapImg);
        maxMiddleClusterLayout.add(maxHeatmapLayout);
        maxMiddleClusterLayout.setCellWidth(maxHeatmapLayout, somClusteringResults.getHeatmapWidth() + "px");

        maxMiddleClusterLayout.add(maxInteractiveColImage);
        maxMiddleClusterLayout.setCellWidth(maxInteractiveColImage, somClusteringResults.getInteractiveColumnWidth() + "px");

        rootX = somClusteringResults.getRowNode().getX();
        rootY = somClusteringResults.getRowNode().getY();

        maxClustInfoLabel.setText("Distance metrics: " + somClusteringResults.getDistanceMeasure() + " - " + "Linkage: " + somClusteringResults.getLinkage());

    }
    private int rootX;
    private int rootY;

    public void selectRootNode() {
        updateSideTreeSelection(rootX, rootY);

    }

    public VLayout getSomclusteringLayout() {
        return mainThumbClusteringLayout;
    }

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = Selection_Manager.getSelectedRows();
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                if (selectedRows != null && update) {
                    clearRowSelection();

                }
                update = true;
                updateInteractiveColumnImg(selectedRows);

            }
        } else {
            Selection sel = Selection_Manager.getSelectedColumns();
            if (sel != null) {
                int[] selectedColumns = sel.getMembers();
//                 nvigatorSlider.setBackgroundImage(upperTreeImg.getUrl());
                if (selectedColumns != null && update) {
                    clearColSelection();
                }
                update = true;
                SelectionManager.Busy_Task(false, true);
            }

        }
    }

    private void updateInteractiveColumnImg(int[] selection) {
//         Selection_Manager.Busy_Task(true, true);
        GWTClientService.updateSomClustInteractiveColumn(selection,
                new AsyncCallback<InteractiveColumnsResults>() {
                    @Override
                    public void onFailure(Throwable caught) {

                        Selection_Manager.connError();
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(InteractiveColumnsResults result) {
                        interactiveColImage.setUrl(result.getInteractiveColumn());
                        maxInteractiveColImage.setUrl(result.getInteractiveColumn());

                        nvigatorSlider.setBackgroundImage(result.getNavgUrl());
                        SelectionManager.Busy_Task(false, true);
                    }
                });

    }

    /**
     * This method is responsible for invoking clustering method
     *
     * @param datasetId - datasetId
     * @param linkage - selected clustering linkage type
     * @param distanceMeasure - the selected clustering distance measurement for
     * clustering
     *
     */
    private void runSomClustering(int linkage, int distanceMeasure, final boolean clusterColumns) {
        SelectionManager.Busy_Task(true, true);
        GWTClientService.computeSomClustering(linkage, distanceMeasure, clusterColumns,
                new AsyncCallback<SomClusteringResult>() {
                    @Override
                    public void onFailure(Throwable caught) {

                        Selection_Manager.connError();
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(SomClusteringResult result) {
                        somClusteringResults = result;
                        Selection_Manager.updateClusteringPanel(result, clusterColumns);
//                        Selection_Manager.Busy_Task(false, true);
                        selectionChanged(Selection.TYPE.OF_ROWS);
                    }
                });

    }

    public SomClusteringResult getSomClusteringResults() {
        return somClusteringResults;
    }

    public void clearRowSelection() {
        sideTreeImg.clearSelection();
        maxSideTreeImg.clearSelection();

    }

    public void clearColSelection() {
        if (clustColumn) {
            upperTreeImg.setUrl(defaultTopTreeImgURL);
            maxUpperTreeImg.setUrl(defaultTopTreeImgURL);
        }
    }

    @Override
    public void remove() {
        if (clusteringProcessBtnReg != null) {
            clusteringProcessBtnReg.removeHandler();
        }
        if (uperTreeReg != null) {
            uperTreeReg.removeHandler();
        }
        if (sideTreeReg != null) {
            sideTreeReg.removeHandler();
        }
        if (minSettingBtnReg != null) {
            minSettingBtnReg.removeHandler();
        }
        if (maxmizeBtnReg != null) {
            maxmizeBtnReg.removeHandler();
        }
        if (settingBtnReg != null) {
            settingBtnReg.removeHandler();
        }
        if (saveBtnReg != null) {
            saveBtnReg.removeHandler();
        }
        if (minmizeBtnReg != null) {
            minmizeBtnReg.removeHandler();
        }

        if (maxUpperTreeReg != null) {
            maxUpperTreeReg.removeHandler();
        }
        if (maxSideTree1Reg != null) {
            maxSideTree1Reg.removeHandler();
        }
        Selection_Manager.removeSelectionChangeListener(this);
        Selection_Manager = null;
    }

    private void updateSideTreeSelection(int x, int y) {
        SelectionManager.Busy_Task(true, false);
        GWTClientService.updateSideTree(x, y, 350, (350 - 25.0), new AsyncCallback<SomClustTreeSelectionResult>() {
            @Override
            public void onFailure(Throwable caught) {
                Selection_Manager.connError();
                SelectionManager.Busy_Task(false, false);
            }

            @Override
            public void onSuccess(SomClustTreeSelectionResult result) {
                sideTreeImg.setUrl(result.getTreeImg());
                maxSideTreeImg.setUrl(result.getTreeImg());
                if (result.getSelectedIndices() != null) {
                    update = false;
                    Selection selection = new Selection(Selection.TYPE.OF_ROWS, result.getSelectedIndices());
                    Selection_Manager.setSelectedRows(selection);

                }
//                Selection_Manager.Busy_Task(false, false);
            }
        });
    }
    private int top;

    private void updateUpperTreeSelection(int x, int y) {
        SelectionManager.Busy_Task(true, false);
        GWTClientService.updateUpperTree(x, y, 70, (350 - 25.0), new AsyncCallback<SomClustTreeSelectionResult>() {
            @Override
            public void onFailure(Throwable caught) {
                Selection_Manager.connError();
                SelectionManager.Busy_Task(false, false);
            }

            @Override
            public void onSuccess(SomClustTreeSelectionResult result) {
                upperTreeImg.setUrl(result.getTreeImg1Url());// 
                maxUpperTreeImg.setUrl(result.getTreeImg1Url());//
                if (result.getSelectedIndices() != null) {
                    update = false;
                    Selection selection = new Selection(Selection.TYPE.OF_COLUMNS, result.getSelectedIndices());
                    Selection_Manager.setSelectedColumns(selection);

                }
//                Selection_Manager.Busy_Task(false, false);
            }
        });
    }

    private VerticalPanel initInfoLayout(int h, int w) {
        VerticalPanel infopanel = new VerticalPanel();
        infopanel.setWidth(w + "px");
        infopanel.setHeight(h + "px");
        infopanel.setStyleName("whiteLayout");

        HTML information = new HTML("<p style='margin-left:30px;font-size:14px;line-height: 150%;'>The Hierarchical Clustering component supports direct mouse selections for the side and top trees and for the heat map in both the minimized and maximized state. <br/><center> <img src='images/hcselect.png' alt='' style='width:auto;height:100px'/></center></p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>Change the clustering parameters by clicking the setting icon <img src='images/setting.gif' alt='' style='width:auto;height:16px'/>.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>It is recommended to use the Hierarchical Clustering component in the maximized state <img src='images/maxmize.png' alt='' style='width:auto;height:16px'/> to get better visualization and to get access to all Hierarchical Clustering features.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>The Hierarchical Clustering plot supports zoom and select. Zoom to the desired level using the zoom scroll bar<img src='images/hczoom.png' alt='' style='width:auto;height:20px'/> and then select data.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>In the maximized state one can export the complete plot as PDF by clicking the save icon <img src='images/icon_save.gif' alt='' style='width:auto;height:16px'/>.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;float: right;'><i>Full tutorial available <a target=\"_blank\" href='" + "tutorial/diva_tutorial.pdf" + "'>here</a>.</i></p>");

        infopanel.add(information);

        return infopanel;

    }

}
