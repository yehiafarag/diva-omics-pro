/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.somclust.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.layout.VLayout;
import web.diva.client.DivaServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.SomClustTreeSelectionUpdate;
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

    private SelectionManager selectionManager;
    private String defaultSideTreeImgURL;
    private String defaultTopTreeImgURL;
    private boolean update = true;
    private boolean clustColumn = true;

    private final DivaServiceAsync DivaClientService;

    private final TreeImg sideTreeImg;
    private TreeImg upperTreeImg;
    private final HeatmapImg heatMapImg;
    private final Image interactiveColImage;
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
    private final PopupPanel clusteringPopup ;
    private final  HorizontalPanel tooltipViewPortLayout;
    private final  HTML maxmizeTooltip = new HTML();
    
       private  MaxmizeTreeImg maxSideTreeImg;
    private MaxmizeTreeImg maxUpperTreeImg;
    private final  HeatmapImg maxHeatMapImg;
    private final  Image maxInteractiveColImage;
    private final  Image maxScaleImg;
    private  VerticalPanel maxClusterLayout = new VerticalPanel();
    private final  HorizontalPanel maxTopClusterLayout = new HorizontalPanel();
    private final  ScrollPanel maxMainClusterPanelLayout = new ScrollPanel();
    private final  HorizontalPanel maxMiddleClusterLayout = new HorizontalPanel();
    private final  HorizontalPanel maxBottomClusterLayout = new HorizontalPanel();
    
    
    private final VLayout mainClusteringPopupBodyLayout ;
    public SomClustComponent(SomClusteringResult somClusteringResults, SelectionManager selectionManager, DivaServiceAsync DivaClientService,boolean clusterColumn) {
        this.clustColumn = clusterColumn;
        this.DivaClientService = DivaClientService;
       
        tooltip.setStyleName("clustertooltip");
        
        mainThumbClusteringLayout = new VLayout();
        mainThumbClusteringLayout.setStyleName("somclustering");
        mainThumbClusteringLayout.setHeight("89%");
        mainThumbClusteringLayout.setWidth("29%");
        mainThumbClusteringLayout.setMargin(2);
        mainThumbClusteringLayout.setMembersMargin(0);
        

        HorizontalPanel topLayout = new HorizontalPanel();
        mainThumbClusteringLayout.addMember(topLayout);
        topLayout.setWidth("100%");
        topLayout.setHeight("18px");
        topLayout.setStyleName("whiteLayout");
        Label title = new Label("Hierarchical Clustering");
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth("70%");
        title.setHeight("18px");

        topLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);

        HorizontalPanel btnsLayout = new HorizontalPanel();
        btnsLayout.setWidth("34px");
        btnsLayout.setHeight("18px");
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
        tooltipPanel.setCellVerticalAlignment(tooltip, HorizontalPanel.ALIGN_TOP);
        tooltip.setWidth("400px");
        tooltip.setHeight("24px");
        sideTreeImg = new TreeImg(somClusteringResults.getSideTreeImgUrl(), somClusteringResults.getRowNode(), 2, tooltip);//"data:sideTreeImg/png;base64,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");
        heatMapImg = new HeatmapImg(somClusteringResults.getHeatMapImgUrl(), somClusteringResults.getRowNames(), somClusteringResults.getColNames(), somClusteringResults.getValues(), tooltip,1);
        interactiveColImage = new Image(somClusteringResults.getInteractiveColumnImgUrl());
        scaleImg = new Image(somClusteringResults.getScaleImgUrl());
        
        mainClusterPanelLayout.setWidth("100%");
        mainClusterPanelLayout.setHeight("95%");
        mainClusterPanelLayout.setStyleName("whiteLayout");
        mainClusterPanelLayout.setAlwaysShowScrollBars(false);
        
       

        mainClusterPanelLayout.add(clusterLayout);
        clusterLayout.setWidth("100%");
        clusterLayout.setHeight("100%");
        clusterLayout.setSpacing(0);

        clusterLayout.add(topClusterLayout);
        clusterLayout.setCellHorizontalAlignment(topClusterLayout, VerticalPanel.ALIGN_CENTER);
        clusterLayout.setCellVerticalAlignment(topClusterLayout, VerticalPanel.ALIGN_MIDDLE);

        clusterLayout.add(middleClusterLayout);
        clusterLayout.setCellHorizontalAlignment(middleClusterLayout, VerticalPanel.ALIGN_CENTER);
        clusterLayout.setCellVerticalAlignment(middleClusterLayout, VerticalPanel.ALIGN_MIDDLE);

        clusterLayout.add(bottomClusterLayout);
        clusterLayout.setCellHorizontalAlignment(bottomClusterLayout, VerticalPanel.ALIGN_CENTER);
        clusterLayout.setCellVerticalAlignment(bottomClusterLayout, VerticalPanel.ALIGN_MIDDLE);
       
        
        if (clusterColumn) {
            topClusterLayout.setHeight(70 + "px");
            topClusterLayout.setWidth("20%");
            upperTreeImg = new TreeImg(somClusteringResults.getUpperTreeImgUrl(), somClusteringResults.getColNode(), 1, tooltip);
            spacer.setSize((200 + "px"), (70 + "px"));
            spacer.setStyleName("borderless");

            spacer2.setSize((20 + "px"), (70 + "px"));
            spacer2.setStyleName("borderless");

            topClusterLayout.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
            topClusterLayout.add(spacer);
            topClusterLayout.setCellHorizontalAlignment(spacer, HorizontalPanel.ALIGN_LEFT);
            topClusterLayout.setCellWidth(spacer, 200 + "px");

            topClusterLayout.add(upperTreeImg);
            topClusterLayout.setCellHorizontalAlignment(upperTreeImg, HorizontalPanel.ALIGN_LEFT);
            topClusterLayout.setCellWidth(upperTreeImg, upperTreeImg.getWidth() + "px");
            topClusterLayout.add(spacer2);
            topClusterLayout.setCellHorizontalAlignment(spacer2, HorizontalPanel.ALIGN_LEFT);
            topClusterLayout.setCellWidth(spacer2, "20px");
        } else{
            topClusterLayout.setHeight(0 + "px");
        }


       
        initThumLayout(somClusteringResults);
        clustInfoLabel.setWidth(210 + "px");
        clustInfoLabel.setHeight("20px");
        clustInfoLabel.setStyleName("info");       
        bottomClusterLayout.add(clustInfoLabel);
        bottomClusterLayout.add(scaleImg);

        if (upperTreeImg != null) {
            upperTreeImg.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    int y = (int) (event.getY());
                    int x = ((int) (event.getX()));
                    if (upperTreeImg.isSelectedNode()) {
                        updateUpperTreeSelection(x, y);
                    }
                }
            });
        }

        sideTreeImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int y = (int) (event.getY());
                int x = ((int) (event.getX()));
                if (sideTreeImg.isSelectedNode()) {
                    updateSideTreeSelection(x, y);
                }

            }

        });
        this.selectionManager = selectionManager;
        this.selectionManager.addSelectionChangeListener(SomClustComponent.this);
        classtype= 4;
        
        final UpdatedSomClustPanel clusteringSettingPanel = new UpdatedSomClustPanel();        
        ///done with normal mode start setteing and maxmize mode
        ClickHandler settingClickHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (clusteringPopup.isShowing()) {
                    clusteringPopup.hide(true);
                }
                clusteringSettingPanel.center();
                clusteringSettingPanel.show();

            }
        };
        minSettingBtn.addClickHandler(settingClickHandler);
        
        clusteringSettingPanel.getProcessBtn().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                clusteringSettingPanel.hide();
                clustColumn = clusteringSettingPanel.isClusterColumns();
                runSomClustering(clusteringSettingPanel.getLinkageValue(), clusteringSettingPanel.getDistanceMeasureValue(), clustColumn);

            }
        });
        maxmizeBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {

                clusteringPopup.center();
                clusteringPopup.show();
                maxClusterLayout.getElement().setAttribute("style", "position: relative;"+("top:"+(-maxClusterLayout.getOffsetHeight())+"px ;"));

            }
        });


        
         /* the end of thumb layout*/
        /* the start of maxmize layout*/
        clusteringPopup = new PopupPanel(false, true);
        clusteringPopup.setAnimationEnabled(true);
        clusteringPopup.ensureDebugId("cwBasicPopup-imagePopup");
        
        mainClusteringPopupBodyLayout = new VLayout();
        mainClusteringPopupBodyLayout.setWidth(80 + "%");
        mainClusteringPopupBodyLayout.setHeight(70+"%");
      
        
        HorizontalPanel maxTopLayout = new HorizontalPanel();
        mainClusteringPopupBodyLayout.addMember(maxTopLayout);
        maxTopLayout.setWidth(100 + "%");
        maxTopLayout.setHeight("18px");
        maxTopLayout.setStyleName("whiteLayout");
        maxTopLayout.setSpacing(3);
         
        Label maxTitle = new Label("Hierarchical Clustering");
        maxTitle.setStyleName("labelheader");
        maxTopLayout.add(maxTitle);

        maxTitle.setWidth(80+ "%");
        maxTopLayout.setCellHorizontalAlignment(maxTitle, HorizontalPanel.ALIGN_LEFT);
        
         HorizontalPanel maxTopBtnLayout = new HorizontalPanel();
         maxTopLayout.add(maxTopBtnLayout);
        maxTopLayout.setCellHorizontalAlignment(maxTopBtnLayout, HorizontalPanel.ALIGN_RIGHT);
         maxTopBtnLayout.setWidth("50px");
         Label settingsBtn = new Label();
        settingsBtn.addStyleName("settings");
        settingsBtn.setHeight("16px");
        settingsBtn.setWidth("16px");
        maxTopBtnLayout.add(settingsBtn);
        maxTopBtnLayout.setCellHorizontalAlignment(settingsBtn, HorizontalPanel.ALIGN_RIGHT);
        settingsBtn.addClickHandler(settingClickHandler);
        
        
        Label saveBtn = new Label();
        saveBtn.addStyleName("save");
        saveBtn.setHeight("16px");
        saveBtn.setWidth("16px");
        maxTopBtnLayout.add(saveBtn);
        maxTopBtnLayout.setCellHorizontalAlignment(saveBtn, HorizontalPanel.ALIGN_RIGHT);

        Label minmizeBtn = new Label();
        minmizeBtn.addStyleName("minmize");
        minmizeBtn.setHeight("16px");
        minmizeBtn.setWidth("16px");
        maxTopBtnLayout.add(minmizeBtn);
        maxTopBtnLayout.setCellHorizontalAlignment(minmizeBtn, HorizontalPanel.ALIGN_RIGHT);

        saveBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                Window.open(thumbChart.getUrl(), "Download Image", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,toolbar=true, width=" + Window.getClientWidth() + ",height=" + Window.getClientHeight());

            }
        });
        minmizeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                
                clusteringPopup.hide();
            }
        });
        

        mainClusteringPopupBodyLayout.addMember(maxMainClusterPanelLayout);
       
       
        
       
        
        tooltipViewPortLayout = new HorizontalPanel();          
        mainClusteringPopupBodyLayout.addMember(tooltipViewPortLayout);
        tooltipViewPortLayout.setWidth(maxTopLayout.getOffsetWidth()+"px");
        tooltipViewPortLayout.setHeight("50px");
        tooltipViewPortLayout.setStyleName("whiteLayout");
        
        VerticalPanel tooltipLayout = new VerticalPanel();tooltipLayout.setStyleName("whiteLayout");   
           tooltipViewPortLayout.add(tooltipLayout);
           tooltipLayout.setWidth("500px");
           tooltipLayout.setHeight("50px");
           
        
        tooltipLayout.add(maxmizeTooltip);
        maxmizeTooltip.setWidth("500px");
        maxmizeTooltip.setStyleName("clustertooltip");      
          tooltipViewPortLayout.add(maxBottomClusterLayout);
          tooltipViewPortLayout.setCellHorizontalAlignment(maxBottomClusterLayout, HorizontalPanel.ALIGN_LEFT);
            
        
       
        maxMainClusterPanelLayout.setWidth("100%");//(RootPanel.get("diva_content_area").getOffsetHeight()-100)+"px");//((sideTreeImg.getWidth()+heatMapImg.getWidth()+10)+"px");
        maxMainClusterPanelLayout.setHeight("100%");//((heatMapImg.getHeight()+70+50)+"px");        
        maxMainClusterPanelLayout.setAlwaysShowScrollBars(false);
        
        maxSideTreeImg = new MaxmizeTreeImg(somClusteringResults.getSideTreeImgUrl(), somClusteringResults.getRowNode(), 1,maxmizeTooltip);//"data:sideTreeImg/png;base64,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");

        maxHeatMapImg = new HeatmapImg(somClusteringResults.getHeatMapImgUrl(), somClusteringResults.getRowNames(), somClusteringResults.getColNames(), somClusteringResults.getValues(), maxmizeTooltip, 2);
        maxInteractiveColImage = new Image("images/w.png");
        maxScaleImg = new Image(somClusteringResults.getScaleImgUrl());
        int rotatSpacerFactor = 0;
        maxMainClusterPanelLayout.setStylePrimaryName("scrolx");
        maxMainClusterPanelLayout.getElement().setAttribute("style","overflow-y: hidden; position: relative; zoom: 1; width: 100%; height: 100%;");
        
        maxMainClusterPanelLayout.add(maxClusterLayout);
        maxClusterLayout.setStyleName("rotate");

        maxClusterLayout.setWidth("5%");
        maxClusterLayout.setHeight("1%");
       
   

        maxClusterLayout.add(maxTopClusterLayout);

        maxClusterLayout.setCellHorizontalAlignment(maxTopClusterLayout, VerticalPanel.ALIGN_RIGHT);
        maxClusterLayout.setCellVerticalAlignment(maxTopClusterLayout, VerticalPanel.ALIGN_MIDDLE);

        maxClusterLayout.add(maxMiddleClusterLayout);
        maxClusterLayout.setCellHorizontalAlignment(maxMiddleClusterLayout, VerticalPanel.ALIGN_RIGHT);
        maxClusterLayout.setCellVerticalAlignment(maxMiddleClusterLayout, VerticalPanel.ALIGN_MIDDLE);
      

        //top clustering layout include spacer 1 and rotated side tree
        if (clusterColumn) {
            rotatSpacerFactor = (sideTreeImg.getHeight() + 70) / 2;
            maxTopClusterLayout.setHeight(70 + "px");
            maxTopClusterLayout.setWidth("5%");
            maxUpperTreeImg = new MaxmizeTreeImg(somClusteringResults.getUpperTreeImgUrl(), somClusteringResults.getColNode(), 2, maxmizeTooltip);
            maxSpacer.setSize((200 + "px"), (70 + "px"));
            maxSpacer.setStyleName("borderless");

            maxSpacer2.setSize((20 + "px"), (70 + "px"));
            maxSpacer2.setStyleName("borderless");

            maxTopClusterLayout.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
            
            maxTopClusterLayout.add(maxSpacer);
            maxTopClusterLayout.setCellHorizontalAlignment(maxSpacer, HorizontalPanel.ALIGN_LEFT);
            maxTopClusterLayout.setCellWidth(maxSpacer, 200 + "px");

            maxTopClusterLayout.add(maxUpperTreeImg);
            maxTopClusterLayout.setCellHorizontalAlignment(maxUpperTreeImg, HorizontalPanel.ALIGN_LEFT);
            maxTopClusterLayout.setCellWidth(maxUpperTreeImg, maxUpperTreeImg.getWidth() + "px");
            maxTopClusterLayout.add(maxSpacer2);
            maxTopClusterLayout.setCellHorizontalAlignment(maxSpacer2, HorizontalPanel.ALIGN_LEFT);
            maxTopClusterLayout.setCellWidth(maxSpacer2, "20px");
        } else {
            maxTopClusterLayout.setHeight(0 + "px");
            rotatSpacerFactor = sideTreeImg.getHeight() / 2;
        }

       
        initMaxmizeLayout(somClusteringResults,rotatSpacerFactor);
        maxClustInfoLabel.setWidth(500 + "px");
        maxClustInfoLabel.setHeight("20px");
        maxClustInfoLabel.setStyleName("maxInfo");
//       
        maxBottomClusterLayout.add(maxClustInfoLabel);
          maxBottomClusterLayout.setCellHorizontalAlignment(maxClustInfoLabel, HorizontalPanel.ALIGN_LEFT);
//            maxBottomClusterLayout.setCellWidth(maxClustInfoLabel, maxClustInfoLabel.getWidth() + "px");

        maxBottomClusterLayout.add(maxScaleImg);
          maxBottomClusterLayout.setCellHorizontalAlignment(maxScaleImg, HorizontalPanel.ALIGN_LEFT);
//            maxBottomClusterLayout.setCellWidth(maxUpperTreeImg, maxUpperTreeImg.getWidth() + "px");
            
      

        if (maxUpperTreeImg != null) {
            maxUpperTreeImg.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    int y = (int) (event.getY());
                    int x = ((int) (event.getX()));
                    
                    if (maxUpperTreeImg.isSelectedNode()) {
                        x = upperTreeImg.getHeight()-x;
                        updateUpperTreeSelection(y, x);
                    }
                }
            });
        }

        maxSideTreeImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int y = (int) (event.getY());
                int x = ((int) (event.getX()));
                if (maxSideTreeImg.isSelectedNode()) {
                    x = sideTreeImg.getHeight()-x;
                    updateSideTreeSelection(y, x);
                }

            }

        });        
         somClusteringResults = null;        
        clusteringPopup.setWidget(mainClusteringPopupBodyLayout);
        mainClusteringPopupBodyLayout.setStyleName("modalLayout");
        
    }
    private final  Image spacer = new Image("images/w.png"),spacer2 = new Image("images/w.png"),maxSpacer = new Image("images/w.png"),maxSpacer2 = new Image("images/w.png");//,rotatSpacer = new Image("images/w.png"),rotatSpacer2 = new Image("images/w.png");
    private final Label clustInfoLabel = new Label(),maxClustInfoLabel = new Label();
    
    private void initThumLayout(SomClusteringResult somClusteringResults) {
        this.defaultSideTreeImgURL = somClusteringResults.getSideTreeImgUrl(); 
        if (clustColumn) {               
            upperTreeImg.setUrl(somClusteringResults.getUpperTreeImgUrl());
            defaultTopTreeImgURL = somClusteringResults.getUpperTreeImgUrl(); 
            upperTreeImg.setVisible(clustColumn);
            spacer.setVisible(clustColumn);
            spacer2.setVisible(clustColumn);
        }        

        sideTreeImg.setUrl(somClusteringResults.getSideTreeImgUrl());//"data:sideTreeImg/png;base64,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");
        heatMapImg .setUrl(somClusteringResults.getHeatMapImgUrl());
        heatMapImg.updateTooltips(somClusteringResults.getRowNames(), somClusteringResults.getColNames(), somClusteringResults.getValues());
        interactiveColImage.setUrl(somClusteringResults.getInteractiveColumnImgUrl());
         scaleImg.setUrl(somClusteringResults.getScaleImgUrl());
    
            middleClusterLayout.setWidth("20%");
        interactiveColImage.setSize("20px", sideTreeImg.getHeight() + "px");
        middleClusterLayout.setHeight(sideTreeImg.getHeight() + "px");
        middleClusterLayout.add(sideTreeImg);
        middleClusterLayout.setCellWidth(sideTreeImg, sideTreeImg.getWidth() + "px");
        middleClusterLayout.add(heatMapImg);
        middleClusterLayout.setCellWidth(heatMapImg, heatMapImg.getWidth() + "px");
        middleClusterLayout.add(interactiveColImage);
        middleClusterLayout.setCellWidth(interactiveColImage, "20px");

       clustInfoLabel.setText("Distance metrics: " + somClusteringResults.getDistanceMeasure() + " - " + "Linkage: " + somClusteringResults.getLinkage());
       
    }
    
    private void initMaxmizeLayout(SomClusteringResult somClusteringResults, int rotateSpacerFactor) {
        if (clustColumn) {     
            maxUpperTreeImg.setUrl(somClusteringResults.getUpperTreeImgUrl());
            maxUpperTreeImg.setVisible(clustColumn);
            maxSpacer.setVisible(clustColumn);
            maxSpacer2.setVisible(clustColumn);
        }

        maxSideTreeImg.setUrl(somClusteringResults.getSideTreeImgUrl()); 
        maxHeatMapImg.setUrl(somClusteringResults.getHeatMapImgUrl());
        maxHeatMapImg.updateTooltips(somClusteringResults.getRowNames(), somClusteringResults.getColNames(), somClusteringResults.getValues());
        maxInteractiveColImage.setUrl(somClusteringResults.getInteractiveColumnImgUrl());
        maxScaleImg.setUrl(somClusteringResults.getScaleImgUrl());

        maxMiddleClusterLayout.setWidth("20%");
        maxInteractiveColImage.setSize("20px", sideTreeImg.getHeight() + "px");
        maxMiddleClusterLayout.setHeight(maxSideTreeImg.getHeight() + "px");
        maxMiddleClusterLayout.add(maxSideTreeImg);
        maxMiddleClusterLayout.setCellWidth(maxSideTreeImg, maxSideTreeImg.getWidth() + "px");
        maxMiddleClusterLayout.add(maxHeatMapImg);
        maxMiddleClusterLayout.setCellWidth(maxHeatMapImg, maxHeatMapImg.getWidth() + "px");
        maxMiddleClusterLayout.add(maxInteractiveColImage);
        maxMiddleClusterLayout.setCellWidth(maxInteractiveColImage, "20px");

        maxClustInfoLabel.setText("Distance metrics: " + somClusteringResults.getDistanceMeasure() + " - " + "Linkage: " + somClusteringResults.getLinkage());

    }


    public VLayout getSomclusteringLayout() {
        return mainThumbClusteringLayout;
    }

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows();
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                if (selectedRows != null && update) {
                    clearRowSelection();                    
                    
                }
                updateInteractiveColumnImg(selectedRows);
            }
        } else {
            Selection sel = selectionManager.getSelectedColumns();
            if (sel != null) {
                int[] selectedColumns = sel.getMembers();
                if (selectedColumns != null && update) {
                    clearColSelection();
                }
            }

        }
    }
    
    
    private void updateInteractiveColumnImg(int[] selection){
         selectionManager.Busy_Task(true, true);
        DivaClientService.updateSomClustInteractiveColumn(selection,
                new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {

                         Window.alert("ERROR IN SERVER CONNECTION");
                        selectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(String result) {
                        interactiveColImage.setUrl(result);
                        maxInteractiveColImage.setUrl(result);
                        selectionManager.Busy_Task(false, true);
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
        selectionManager.Busy_Task(true, true);
        DivaClientService.computeSomClustering(linkage, distanceMeasure, clusterColumns,
                new AsyncCallback<SomClusteringResult>() {
                    @Override
                    public void onFailure(Throwable caught) {

                         Window.alert("ERROR IN SERVER CONNECTION");
                        selectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(SomClusteringResult result) {
                        selectionManager.updateClusteringPanel(result,clusterColumns);
                        selectionManager.Busy_Task(false, true);
                    }
                });

    }
    

    public void clearRowSelection() {
        sideTreeImg.setUrl(defaultSideTreeImgURL);
        maxSideTreeImg.setUrl(defaultSideTreeImgURL);

    }

    public void clearColSelection() {
        if (clustColumn) {
            upperTreeImg.setUrl(defaultTopTreeImgURL);
            maxUpperTreeImg.setUrl(defaultTopTreeImgURL);
        }
    }

    @Override
    public void remove() {
        selectionManager = null;
    }

    private void updateSideTreeSelection(int x, int y) {
        selectionManager.Busy_Task(true, false);
        DivaClientService.updateSideTree(x, y, 200, (500 - 25.0), new AsyncCallback<SomClustTreeSelectionUpdate>() {
            @Override
            public void onFailure(Throwable caught) {
                 Window.alert("ERROR IN SERVER CONNECTION");
                selectionManager.Busy_Task(false, false);
            }

            @Override
            public void onSuccess(SomClustTreeSelectionUpdate result) {
                sideTreeImg.setUrl(result.getTreeImgUrl());
                maxSideTreeImg.setUrl(result.getTreeImgUrl());
                if (result.getSelectedIndices() != null) {
                    update = false;
                    Selection selection = new Selection(Selection.TYPE.OF_ROWS, result.getSelectedIndices());
                    selectionManager.setSelectedRows(selection);
                    update = true;
                }
                selectionManager.Busy_Task(false, false);
            }
        });
    }

    private void updateUpperTreeSelection(int x, int y) {
        selectionManager.Busy_Task(true, false);
        DivaClientService.updateUpperTree(x, y, 70, (500 - 25.0), new AsyncCallback<SomClustTreeSelectionUpdate>() {
            @Override
            public void onFailure(Throwable caught) {
                 Window.alert("ERROR IN SERVER CONNECTION");
                selectionManager.Busy_Task(false, false);
            }

            @Override
            public void onSuccess(SomClustTreeSelectionUpdate result) {
                upperTreeImg.setUrl(result.getTreeImgUrl());// 
                  maxUpperTreeImg.setUrl(result.getTreeImgUrl());//
                if (result.getSelectedIndices() != null) {
                    update = false;
                    Selection selection = new Selection(Selection.TYPE.OF_COLUMNS, result.getSelectedIndices());
                    selectionManager.setSelectedColumns(selection);
                    update = true;
                }
                selectionManager.Busy_Task(false, false);
            }
        });
    }

}
