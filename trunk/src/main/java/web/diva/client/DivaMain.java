package web.diva.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.client.omicstables.view.LeftPanelView;
import web.diva.client.profileplot.view.ProfilePlotComponent;
import web.diva.client.pca.view.PCAPlot;
import web.diva.client.somclust.view.SomClustView;
import web.diva.shared.model.core.model.dataset.DatasetInformation;
import web.diva.client.pca.view.PCABtn;
import web.diva.client.rank.view.RankBtn;
import web.diva.client.rank.view.RankTablesComponent;
import web.diva.client.view.core.ButtonsBarMenuComponent;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.somclust.view.SomClusterBtn;
import web.diva.client.view.core.BusyTaskIndicator;
import web.diva.shared.beans.PCAImageResult;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClusteringResult;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 *
 * The App-Controller layer in DIVA Provides the client entry point Initializes
 * different components (the Selection-Manager as single instance variable,
 * GWT-Client-Service, HTML components, and all the GUI panels) Manages
 * different functions (updates the dataset information, omics analysis
 * functions invocations, export data, save data-sets, create groups etc.).
 *
 * @author Yehia Farag
 *
 */
public class DivaMain implements EntryPoint,ChangeHandler {

    private  final SelectionManager selectionManager = SelectionManager.getInstance();
    private static final String SERVER_ERROR = "An error occurred while attempting to contact the server";
    private final Label errorLabel = new Label();
    private boolean busyProcess = false;
    private final Label rowLab = new Label();
    private final Label colLab = new Label();
    private final Label rowGroup = new Label();
    private final Label colGroup = new Label();
    private final int leftPanelWidth = RootPanel.get("templatemo_left").getOffsetWidth()-20;
    private final int rightPanelWidth = RootPanel.get("SomClusteringResults").getOffsetWidth()-10;
    private SomClusterBtn somClustBtn;
    private PCABtn pcaBtn;
    private RankBtn rankBtn;
    private Label profilePlotBtn;
    private VerticalPanel profilePlotLayout,PCAPlotLayout,RankLayout;
    private  RankTablesComponent rankTables;
    private final BusyTaskIndicator busyIndicator = new BusyTaskIndicator();
   
   //init analysis and functions buttons
    private final ButtonsBarMenuComponent btnsMenueComponent = new ButtonsBarMenuComponent();;
    private final TreeMap datasetsNames = new TreeMap();
    private LeftPanelView leftPanelView;
    
    private final GreetingServiceAsync GWTClientService = GWT.create(GreetingService.class);
    private final ModularizedListener[] compponents = new ModularizedListener[2];

    private  ListBox selectDatasetList;
    private PCAPlot pcaPlotComponent;


//    private int datasetId;
     
//    private SaveDatasetDialog saveDs;
    

//    private SomClustView hierarchicalClustering;
//    private RankTablesComponent rankTables;
//    private InitImgs initImgs;
//    private HeaderLayout header;
//    private SomClustPanel somClustPanel;
//    private PCAPanel pcaPanel;
//    private DatasetPanel dsPanel;
//    private ActivateGroupPanel activateGroupPanel;
//    private ActivateGroupPanel exportGroupPanel;
//    private RankPanel rankPanel;
//    private int pcaI = 0;
//    private int pcaII = 1;
    
//    private HandlerRegistration lineChartHandlerRegistration;
//    private HandlerRegistration pcaHandlerRegistration;
//    private HandlerRegistration rankHandlerRegistration;
//    private HandlerRegistration somclustHandlerRegistration, actvGroupPanel, createGroupPanel, savePanelRegis;
//
//    private HandlerRegistration somclustBtnHandlerRegistration;
//    private HandlerRegistration pcaBtnHandlerRegistration;
//    private HandlerRegistration rankBtnHandlerRegistration, createGroupBtnHandlerRegistration, expBtnClickHandlerRegistration, saveBtnClickHandlerRegistration, activateGroupBtnClickHandlerRegistration;

    @Override
    public void onModuleLoad() {
        busyTask(true);
        this.initApplication();
    }

    
    
    /**
     * This method is responsible for initializing the main DIVA layout
     *
     */
    private void initApplication() {
        //init dataset information panel        
        initializePrimLayout();
        //init dropdown dataset select list
        selectDatasetList = new ListBox();
        selectDatasetList.setWidth("300px");
        selectDatasetList.addItem("Select Dataset");
        getDatasetsList();//get available dataset names
        RootPanel.get("headerLayout").add(selectDatasetList);;
//        initImgs = new InitImgs();
        selectDatasetList.addChangeHandler(this);
    }

    
    /**
     * on select dataset 
     * @param event user select dataset
     *
     */
    @Override
    public void onChange(ChangeEvent event) {
        if (selectDatasetList.getSelectedIndex() > 0) {
            try {
              int  datasetId = (Integer) datasetsNames.get(selectDatasetList.getItemText(selectDatasetList.getSelectedIndex()));
              selectionManager.resetSelection();
                loadDataset(datasetId);
            } catch (Exception e) {
                Window.alert("exp " + e.getMessage());
            }
//            ClickHandler somClustHandler = new ClickHandler() {
//                @Override
//                public void onClick(ClickEvent event) {
//                    if (somClustPanel == null) {
//                        somClustPanel = new SomClustPanel();
//                        ClickHandler somClustClickhandler = new ClickHandler() {
//                            @Override
//                            public void onClick(ClickEvent event) {
//                                processSomClustering(datasetId, somClustPanel.getX(), somClustPanel.getY());
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
//                }
//            };
//
//            if (somclustBtnHandlerRegistration != null) {
//                somclustBtnHandlerRegistration.removeHandler();
//            }
//            somclustBtnHandlerRegistration = btnsMenueComponent.getSomClustBtn().addClickHandler(somClustHandler);
//
//            ClickHandler lineChartBtnHandler = new ClickHandler() {
//                @Override
//                public void onClick(ClickEvent event) {
//                    viewLineChart(datasetId);
//                }
//            };
//            if (lineChartHandlerRegistration != null) {
//                lineChartHandlerRegistration.removeHandler();
//            }
//
//            lineChartHandlerRegistration = btnsMenueComponent.getLineChartBtn().addClickHandler(lineChartBtnHandler);
//
//            ClickHandler PCAChartBtnHandler = new ClickHandler() {
//                @Override
//                public void onClick(ClickEvent event) {
//                    initPcaPanel();
//
//                }
//            };
//            if (pcaBtnHandlerRegistration != null) {
//                pcaBtnHandlerRegistration.removeHandler();
//            }
//            pcaBtnHandlerRegistration = btnsMenueComponent.getPcaBtn().addClickHandler(PCAChartBtnHandler);
//
//            ClickHandler rankBtnHandler = new ClickHandler() {
//                @Override
//                public void onClick(ClickEvent event) {
//                    initGroupsPanel(2);
//                }
//            };
//            if (rankBtnHandlerRegistration != null) {
//                rankBtnHandlerRegistration.removeHandler();
//            }
//            rankBtnHandlerRegistration = btnsMenueComponent.getRankBtn().addClickHandler(rankBtnHandler);
//
//            ClickHandler createGroupBtnHandler = new ClickHandler() {
//                @Override
//                public void onClick(ClickEvent event) {
//                    Selection rowSel = selectionManager.getSelectedRows(datasetId);
//                    Selection colSel = selectionManager.getSelectedColumns(datasetId);
//                    int[] selectedRows = null;
//                    int[] selectedCol = null;
//
//                    if (rowSel != null) {
//                        selectedRows = rowSel.getMembers();
//                    }
//                    if (colSel != null) {
//                        selectedCol = colSel.getMembers();
//                    }
//                    initDsPanel(selectedRows, selectedCol);
//                }
//            };
//
//            if (createGroupBtnHandlerRegistration != null) {
//                createGroupBtnHandlerRegistration.removeHandler();
//            }
//            createGroupBtnHandlerRegistration = btnsMenueComponent.getCreateGroupBtn().addClickHandler(createGroupBtnHandler);
//
//            ClickHandler activBtnHandler = new ClickHandler() {
//                @Override
//                public void onClick(ClickEvent event) {
//                    initGroupsPanel(1);
//                }
//            };
//            if (activateGroupBtnClickHandlerRegistration != null) {
//                activateGroupBtnClickHandlerRegistration.removeHandler();
//            }
//            activateGroupBtnClickHandlerRegistration = btnsMenueComponent.getActGroupBtn().addClickHandler(activBtnHandler);
//            ClickHandler expBtnClickHandler = new ClickHandler() {
//                @Override
//                public void onClick(ClickEvent event) {
//                    initExportPanel();
//                }
//            };
//
//            if (expBtnClickHandlerRegistration != null) {
//                expBtnClickHandlerRegistration.removeHandler();
//            }
//            expBtnClickHandlerRegistration = btnsMenueComponent.getExportGroupBtn().addClickHandler(expBtnClickHandler);

//            ClickHandler saveBtnClickHandler = new ClickHandler() {
//                @Override
//                public void onClick(ClickEvent event) {
//                    if (saveDs == null) {
//                        saveDs = new SaveDatasetDialog();
//                        ClickHandler savBtnHandler = new ClickHandler() {
//                            @Override
//                            public void onClick(ClickEvent event) {
//                                String newName = saveDs.getName();
//                                if (newName == null || newName.equals("")) {
//                                    saveDs.getErrorlabl().setVisible(true);
//                                    saveDs.getForm().validate();
//                                } else {
//                                    saveDs.getErrorlabl().setVisible(false);
//                                    saveDataset(newName);
//                                    saveDs.hide();
//                                }
//                            }
//                        };
//                        if (savePanelRegis != null) {
//                            savePanelRegis.removeHandler();
//                        }
//                        savePanelRegis = saveDs.getOkBtn().addClickHandler(savBtnHandler);
//                    }
//                    saveDs.show();
//
//                }
//            };
//            if (saveBtnClickHandlerRegistration != null) {
//                saveBtnClickHandlerRegistration.removeHandler();
//            }
//            saveBtnClickHandlerRegistration = btnsMenueComponent.getSaveBtn().addClickHandler(saveBtnClickHandler);
        }
    }


    /**
     * This method is responsible for initializing dataset drop-down list
     */
    private void getDatasetsList() {
        GWTClientService.getAvailableDatasets(new AsyncCallback<TreeMap<Integer, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                busyTask(false);
            }

            @Override
            public void onSuccess(TreeMap results) {
                errorLabel.setText("");
                for (Object o : results.keySet()) {
                    int key = (Integer) o;
                    selectDatasetList.addItem((String) results.get(key));
                    datasetsNames.put(results.get(key), key);
                    busyTask(false);
                }
            }

        });

    }
    private boolean init= true;

    /**
     * This method is responsible for loading dataset upon user selection
     *
     * @param datasetId - datasetId
     *
     */
    private void loadDataset(int datasetId) {
        busyTask(true);
        GWTClientService.setMainDataset(datasetId,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                    }

                    @Override
                    public void onSuccess(DatasetInformation datasetInfos) {
                        rowLab.setText("Rows : " + datasetInfos.getRowsNumb());
                        colLab.setText("Columns : " + datasetInfos.getColNumb());
                        rowGroup.setText("Row Groups : " + (datasetInfos.getRowGroupsNumb()));
                        colGroup.setText("Column Groups : " + (datasetInfos.getColGroupsNumb()));
                        activateAnalysisFunctions(datasetInfos.getPcaColNames(),datasetInfos.getColGroupsNamesMap());
                        updateLeftPanel(datasetInfos);                        
//                        Window.alert(""+(selectionManager == null)+"  "+(datasetInfos==null));                        
                        resetLayout();
                        init=true;
                        processProfilePlot(); 
                        
                        datasetInfos = null;
                        if(!init)
                            busyTask(false);
                    }
                });
    }

    private void updateLeftPanel(DatasetInformation datasetInfos) {

        leftPanelView = new LeftPanelView(selectionManager, datasetInfos, leftPanelWidth);
        RootPanel.get("omicsTable").clear();
        RootPanel.get("omicsTable").add(leftPanelView);

    }

    private void initializePrimLayout() {
        RootPanel.get("row").add(rowLab);
        RootPanel.get("col").add(colLab);
        RootPanel.get("rowGroups").add(rowGroup);
        RootPanel.get("colGroups").add(colGroup);
        RootPanel.get("Hierarchical_Clustering_btn").add(btnsMenueComponent.getSomClustBtn());
        profilePlotBtn = btnsMenueComponent.getProfilePlotBtn();
        RootPanel.get("Profile_Plot_btn").add(profilePlotBtn);
        RootPanel.get("PCA_btn").add(btnsMenueComponent.getPcaBtn());
        RootPanel.get("Rank_Product_btn").add(btnsMenueComponent.getRankBtn());

        VLayout midPanelLayout = new VLayout();
        
        //pca and profile plot layout
        HLayout topMedLayout = new HLayout();
        topMedLayout.setWidth("502px");
        topMedLayout.setHeight("212px");
        profilePlotLayout = new VerticalPanel();
        profilePlotLayout.setWidth("250px");
        profilePlotLayout.setHeight("212px");
//        profilePlotLayout.setBorderWidth(1);
        topMedLayout.addMember(profilePlotLayout);
        PCAPlotLayout = new VerticalPanel();
        PCAPlotLayout.setWidth("250px");
        PCAPlotLayout.setHeight("212px");
//        PCAPlotLayout.setBorderWidth(1);
        topMedLayout.addMember(PCAPlotLayout);
        topMedLayout.setMembersMargin(1);
        midPanelLayout.addMember(topMedLayout);
        
        RankLayout = new VerticalPanel();
//        RankLayout.setBorderWidth(1);
        RankLayout.setWidth("500px");
        RankLayout.setHeight("213px");
        midPanelLayout.addMember(RankLayout);
        RootPanel.get("templatemo_mid").add(midPanelLayout);
    }

    private void activateAnalysisFunctions(String[] colsNames,LinkedHashMap<String,String> colGroupsNamesMap) {
        
        initializeSomClusteringBtn();
        initializeProfilePlotBtn();
        initializePCABtn(colsNames);
        initializeRankBtn(colGroupsNamesMap);
        RootPanel.get("Hierarchical_Clustering_btn").clear();
        RootPanel.get("Hierarchical_Clustering_btn").add(somClustBtn);
        RootPanel.get("PCA_btn").clear();
        RootPanel.get("PCA_btn").add(pcaBtn);
        RootPanel.get("Rank_Product_btn").clear();
        RootPanel.get("Rank_Product_btn").add(rankBtn);
        
    }
    
    private void initializeSomClusteringBtn() {
        somClustBtn = new SomClusterBtn();
        ClickHandler somClustHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!busyProcess) {
                    processSomClustering(somClustBtn.getLinkage(), somClustBtn.getDistanceMeasure());
                    somClustBtn.hidePanel();
                }
            }
        };
        somClustBtn.setClickListener(somClustHandler);

    }
    
    private void initializeProfilePlotBtn() {
        com.google.gwt.event.dom.client.ClickHandler profilePlotHandler = new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
                if (!busyProcess) {
                    processProfilePlot();
                }
            }
        };
        profilePlotBtn.addClickHandler(profilePlotHandler);

    }

    
    private void initializePCABtn(String[] colsNames){
    pcaBtn = new PCABtn(colsNames);
    ClickHandler pcaHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
//                if (!busyProcess) {
                    int pcaI = pcaBtn.getPcaPanel().getPcaI();
                    int pcaII = pcaBtn.getPcaPanel().getPcaII();
                    viewPCAChart(pcaI, pcaII);
                    pcaBtn.getPcaPanel().hide();
//                }
            }
        };
        pcaBtn.setClickListener(pcaHandler);
    }
    
     private void initializeRankBtn(LinkedHashMap<String, String> colGroupsNamesMap){
    rankBtn = new RankBtn(colGroupsNamesMap);
    ClickHandler rankHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                rankBtn.setErrorlablVisible(false);
                                String[] groups = rankBtn.getSelectColGroups();
                                String seed = rankBtn.getSeed();
                                String perm = rankBtn.getPerm();
                                String log2 = rankBtn.getLog2();
                                if (groups == null || groups.length == 0 || groups.length > 2 || seed == null || seed.equals("") || perm == null || perm.equals("")) {
                                    rankBtn.setErrorlablVisible(true);
                                    rankBtn.rankPanelvalidate();
                                } else {
                                    viewRankTables(perm, seed, groups, log2);
                                    rankBtn.hidePanel();
                                }
                    rankBtn.hidePanel();
            }
        };
        rankBtn.setClickListener(rankHandler);
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
    private void processSomClustering(int linkage, int distanceMeasure) {
        busyTask(true);
        GWTClientService.computeSomClustering(linkage, distanceMeasure,
                new AsyncCallback<SomClusteringResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("datasetInformation").setVisible(false);
//                        init=false;
                        busyTask(false);
                    }

                    @Override
                    public void onSuccess(SomClusteringResult result) {
                        RootPanel.get("datasetInformation").setVisible(true);
                        errorLabel.setText("");
                        SomClustView hierarchicalClustering = new SomClustView(result, selectionManager,GWTClientService,rightPanelWidth);
                        RootPanel.get("SomClusteringResults").clear();
                        RootPanel.get("SomClusteringResults").add(hierarchicalClustering.componentView());
//                        generateHeatMap(hierarchicalClustering.getIndexer(), hierarchicalClustering.getColIndexer());
                        busyTask(false);
                        init=false;
                    }
                });

    }

    /**
     * This method is responsible for generating Heat-Map images after drawing
     * the side and top trees
     *
     * @param indexers - new rows indexes based on side tree
     * @param colIndexer - new columns indexes based on top tree
     *
     */
//    private void generateHeatMap(ArrayList<String> indexers, ArrayList<String> colIndexer) {
//        GWTClientService.computeHeatmap(datasetId, indexers, colIndexer,
//                new AsyncCallback<HeatMapImageResult>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        errorLabel.setText(SERVER_ERROR);
//                        RootPanel.get("datasetInformation").setVisible(false);
//                        busyTask(false);
//                    }
//
//                    @Override
//                    public void onSuccess(HeatMapImageResult result) {
//                        RootPanel.get("datasetInformation").setVisible(true);
//                        errorLabel.setText("");
//                        hierarchicalClustering.setImge(result);
//                        busyTask(false);
//                    }
//                });
//    }

    private ProfilePlotComponent profilePlotComponent;

    /**
     * This method is responsible for invoking line chart method
     *
     * @param datasetId - datasetId
     *
     */
    private void processProfilePlot() {
        busyTask(true);
        GWTClientService.computeProfilePlot(212,250,
                new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
//                        init=false;
                        busyTask(false);
                    }

                    @Override
                    public void onSuccess(String result) {
                        errorLabel.setText("");
                        if (profilePlotComponent != null) {
                            profilePlotComponent.remove();
                        }
                        profilePlotComponent = new ProfilePlotComponent(result, selectionManager, GWTClientService);
                        compponents[0] = profilePlotComponent;
                        profilePlotLayout.clear();
                        profilePlotLayout.add(profilePlotComponent.getLayout());
                        profilePlotLayout.setBorderWidth(1);
                        if(!init)
                            busyTask(false);
                        if (init) {
                            viewPCAChart(0, 1);
                        }
                    }
                });
    }

    /**
     * This method is responsible for invoking PCA method
     *
     * @param datasetId - datasetId
     * @param pcaI - first selected principal component index
     * @param pcaII - second selected principal component index
     *
     */
    private void viewPCAChart(int pcaI, int pcaII) {

        busyTask(true);
        GWTClientService.computePCA(pcaI, pcaII,
                new AsyncCallback<PCAImageResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
//                        errorLabel.setText(SERVER_ERROR);
//                        RootPanel.get("datasetInformation").setVisible(false);
//                        init=false;
                        busyTask(false);
                    }

                    @Override
                    public void onSuccess(PCAImageResult result) {
//                        errorLabel.setText("");
//                        RootPanel.get("datasetInformation").setVisible(true);
                        if (pcaPlotComponent != null) {
                            pcaPlotComponent.remove();
                        }
                        pcaPlotComponent = new PCAPlot(result, selectionManager, GWTClientService);
                        
                        compponents[1] = (pcaPlotComponent);
                        PCAPlotLayout.clear();
                        PCAPlotLayout.add(pcaPlotComponent.getPCAPlot());
                        PCAPlotLayout.setBorderWidth(1);
                        if(!init)
                            busyTask(false);
                        if (init) {
                            viewRankTables("400", "288848379", new String[]{"true,ALL",}, "Log 2");
                        }
                    }
                });
    }

    /**
     * This method is responsible for invoking Ranking method
     *
     * @param datasetId - datasetId
     * @param perm
     * @param seed
     * @param colGropNames selected ranking columns indexes
     * @param log2
     */
    private void viewRankTables( String perm, String seed, String[] colGropNames, String log2) {
        busyTask(true);
        GWTClientService.computeRank(perm, seed, colGropNames, log2,
                new AsyncCallback<RankResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
//                        init=false;
                        busyTask(false);
                    }

                    @Override
                    public void onSuccess(RankResult result) {
                       
                        RootPanel.get("datasetInformation").setVisible(true);
                        errorLabel.setText("");
                        RankLayout.clear();
                        rankTables = new RankTablesComponent(GWTClientService, selectionManager, result);
                        RankLayout.add(rankTables.getMainRankLayout());
                        RankLayout.setBorderWidth(1);
                        if(!init)
                            busyTask(false);
                        if (init) {
                            processSomClustering(2, 1);
                        }
                    }
                });

    }

    /**
     * This method is responsible for invoking activate rows/columns groups
     * method
     *
     * @param datasetId - datasetId
     * @param rowGroups row groups indexes to activate
     * @param colGroups column groups indexes to activate
     * @param colGropNames selected ranking columns indexes
     */
//    private void activateGroups(final int datasetId, String[] rowGroups, String[] colGroups) {
//        boolean test = false;
//        if (rowGroups != null && rowGroups.length > 0) {
//            for (String rowGroup1 : rowGroups) {
//                if (rowGroup1.startsWith("false,")) {
//                    test = true;
//                    break;
//                }
//            }
//        }
//        if (!test && colGroups != null && colGroups.length > 0) {
//            for (String colGroup1 : colGroups) {
//                if (colGroup1.startsWith("false,")) {
//                    test = true;
//                    break;
//                }
//            }
//        }
//        if (test) {
//            busyTask(true);
//            GWTClientService.activateGroups(datasetId, rowGroups, colGroups,
//                    new AsyncCallback<DatasetInformation>() {
//                        @Override
//                        public void onFailure(Throwable caught) {
//                            errorLabel.setText(SERVER_ERROR);
//                            RootPanel.get("datasetInformation").setVisible(false);
//                            busyTask(false);
//                        }
//
//                        @Override
//                        public void onSuccess(DatasetInformation result) {
//                            RootPanel.get("datasetInformation").setVisible(true);
//                            errorLabel.setText("");
//                            leftPanelView = new LeftPanelView(selectionManager, result);
//                            RootPanel.get("geneTable").clear();
//                            RootPanel.get("geneTable").add(leftPanelView);
//                            updateRowGroups();
//                            busyTask(false);
//                            result = null;
//
//                        }
//                    });
//        }
//
//    }

    /**
     * This method is responsible for invoking export data method
     *
     * @param datasetId - datasetId
     * @param rowGroup row group to export as text file
     */
//    private void exportData(final int datasetId, String rowGroup) {
//        busyTask(true);
//        GWTClientService.exportData(datasetId, rowGroup,
//                new AsyncCallback<String>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        errorLabel.setText(SERVER_ERROR);
//                        RootPanel.get("datasetInformation").setVisible(false);
//                        busyTask(false);
//                    }
//
//                    @Override
//                    public void onSuccess(String result) {
//                        RootPanel.get("datasetInformation").setVisible(true);
//                        errorLabel.setText("");
//                        Window.open(result, "downlodwindow", "status=0,toolbar=0,menubar=0,location=0");
//                        busyTask(false);
//                        result = null;
//
//                    }
//                });
//
//    }

    /**
     * This method is responsible for invoking create row group method
     *
     * @param datasetId - datasetId
     * @param name - row group name
     * @param color - selected hashed color
     * @param type - group type (row)
     * @param selectedRows - selected rows indexes
     */
//    private void createRowGroup(final int datasetId, String name, String color, String type, int[] selectedRows) {
//        busyTask(true);
//        GWTClientService.createRowGroup(datasetId, name, color, type, selectedRows,
//                new AsyncCallback<Boolean>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        errorLabel.setText(SERVER_ERROR);
//                        RootPanel.get("datasetInformation").setVisible(false);
//                        busyTask(false);
//                        dsPanel.getOkBtn().enable();
//                    }
//
//                    @Override
//                    public void onSuccess(Boolean result) {
//                        errorLabel.setText("");
//                        RootPanel.get("datasetInformation").setVisible(true);
//                        updateApp(datasetId);
//                        dsPanel.getOkBtn().enable();
//                    }
//                });
//
//    }

    /**
     * This method is responsible for invoking create colum group method
     *
     * @param datasetId - datasetId
     * @param name - column group name
     * @param color - selected hashed color
     * @param selection - selected column indexes
     * @param type - group type (column)
     */
//    private void createColGroup(final int datasetId, String name, String color, String[] selection, String type) {
//        busyTask(true);
//        GWTClientService.createColGroup(datasetId, name, color, type, selection,
//                new AsyncCallback<Boolean>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        errorLabel.setText(SERVER_ERROR);
//                        RootPanel.get("datasetInformation").setVisible(false);
//                        busyTask(false);
//                    }
//
//                    @Override
//                    public void onSuccess(Boolean result) {
//                        errorLabel.setText("");
//                        RootPanel.get("datasetInformation").setVisible(true);
//                        updateApp(datasetId);
//                    }
//                });
//
//    }

    /**
     * This method is responsible for invoking save dataset method
     *
     * @param name - new dataset name
     */
//    private void saveDataset(String name) {
//        busyTask(true);
//        GWTClientService.saveDataset(datasetId, name,
//                new AsyncCallback<Integer>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        RootPanel.get("datasetInformation").setVisible(false);
//                        errorLabel.setText(SERVER_ERROR);
//                        busyTask(false);
//                    }
//
//                    @Override
//                    public void onSuccess(Integer datasetId) {
//                        RootPanel.get("datasetInformation").setVisible(true);
//                        Window.Location.reload();
//                    }
//                });
//
//    }

    /**
     * This method is responsible for invoking create sub-dataset method
     *
     * @param name - new sub-dataset name
     * @param selectedRows - selected rows indexes
     */
//    private void createSubDataset(String name, int[] selectedRows) {
//        busyTask(true);
//        GWTClientService.createSubDataset(name, selectedRows,
//                new AsyncCallback<Integer>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        RootPanel.get("datasetInformation").setVisible(false);
//                        errorLabel.setText(SERVER_ERROR);
//                        busyTask(false);
//                    }
//
//                    @Override
//                    public void onSuccess(Integer datasetId) {
//                        RootPanel.get("datasetInformation").setVisible(true);
//                        Window.Location.reload();
//                    }
//                });
//
//    }

    /**
     * This method is responsible for update dataset information
     *
     * @param datasetId
     */
//    private void updateApp(int datasetId) {
//        busyTask(true);
//        GWTClientService.updateDatasetInfo(datasetId,
//                new AsyncCallback<DatasetInformation>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        RootPanel.get("datasetInformation").setVisible(false);
//                        errorLabel.setText(SERVER_ERROR);
//                        busyTask(false);
//                    }
//
//                    @Override
//                    public void onSuccess(DatasetInformation datasetInfos) {
//                        RootPanel.get("datasetInformation").setVisible(true);
//                        errorLabel.setText("");
//                        rowLab.setText("Rows : " + datasetInfos.getRowsNumb());
//                        colLab.setText("Columns : " + datasetInfos.getColNumb());
//                        rowGroup.setText("Row Groups : " + (datasetInfos.getRowGroupsNumb()));
//                        colGroup.setText("Column Groups : " + (datasetInfos.getColGroupsNumb()));
//                        leftPanelView = new LeftPanelView(selectionManager, datasetInfos);
//                        RootPanel.get("geneTable").clear();
//                        RootPanel.get("geneTable").add(leftPanelView);
//                        busyTask(false);
//                        datasetInfos = null;
//                        updateRowGroups();
//                    }
//                });
//    }

    /**
     * This method is responsible for initializing group/sub-dataset panel
     *
     * @param panelType - group/sub-dataset or activate group panels
     */
//    private void initGroupsPanel(final int panelType) {
//        RootPanel.get("loaderImage").setVisible(true);
//        GWTClientService.getGroupsPanelData(datasetId, new AsyncCallback<LinkedHashMap<String, String>[]>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                errorLabel.setText(SERVER_ERROR);
//                RootPanel.get("datasetInformation").setVisible(false);
//                RootPanel.get("loaderImage").setVisible(false);
//            }
//
//            @Override
//            public void onSuccess(LinkedHashMap[] results) {
//                errorLabel.setText("");
//                if (panelType == 1)//activateGroupPanel
//                {
//                    if (activateGroupPanel == null) {
//                        activateGroupPanel = new ActivateGroupPanel(results[0], results[1]);
//
//                        ClickHandler actPanelHandler = new ClickHandler() {
//                            @Override
//                            public void onClick(ClickEvent event) {
//                                try {
//                                    errorLabel.setVisible(false);
//                                    String[] rowValues = activateGroupPanel.getSelectRowGroups();
//                                    String[] colValues = activateGroupPanel.getSelectColGroups();
//                                    if (rowValues != null && rowValues.length > 2) {
//                                        activateGroupPanel.getErrorlabl().setVisible(true);
//
//                                    } else if (colValues != null && colValues.length > 2) {
//                                        activateGroupPanel.getErrorlabl().setVisible(true);
//
//                                    } else {
//                                        activateGroups(datasetId, rowValues, colValues);
//                                        activateGroupPanel.hide();
//                                    }
//                                } catch (Exception exp) {
//                                }
//
//                            }
//                        };
//                        if (actvGroupPanel != null) {
//                            actvGroupPanel.removeHandler();
//                        }
//
//                        actvGroupPanel = activateGroupPanel.getOkBtn().addClickHandler(actPanelHandler);
//                    } else {
//                        activateGroupPanel.updataChartData(results[0], results[1]);
//                    }
//                    activateGroupPanel.show();
//                } else if (panelType == 2) {
//
//                    if (rankPanel == null) {
//                        rankPanel = new RankPanel(results[1]);
//                        ClickHandler rankPanelBtnHandler = new ClickHandler() {
//                            @Override
//                            public void onClick(ClickEvent event) {
//                                rankPanel.getErrorlabl().setVisible(false);
//                                String[] groups = rankPanel.getSelectColGroups();
//                                String seed = rankPanel.getSeed();
//                                String perm = rankPanel.getPermutation();
//                                String log2 = rankPanel.getRadioGroupItem();
//                                if (groups == null || groups.length == 0 || groups.length > 2 || seed == null || seed.equals("") || perm == null || perm.equals("")) {
//                                    rankPanel.getErrorlabl().setVisible(true);
//                                    rankPanel.getForm2().validate();
//                                } else {
//                                    viewRankTables(datasetId, perm, seed, groups, log2);
//                                    rankPanel.hide();
//                                }
//                            }
//                        };
//                        if (rankHandlerRegistration != null) {
//                            rankHandlerRegistration.removeHandler();
//                        }
//                        rankHandlerRegistration = rankPanel.getOkBtn().addClickHandler(rankPanelBtnHandler);
//                    } else {
//                        rankPanel.updateData(results[1]);
//                    }
//                    rankPanel.show();
//
//                }
//                results = null;
//                RootPanel.get("loaderImage").setVisible(false);
//            }
//
//        });
//
//    }

    /**
     * This method is responsible for initializing export panel
     *
     * @param panelType - group/sub-dataset or activate group panels
     */
//    private void initExportPanel() {
//        RootPanel.get("loaderImage").setVisible(true);
//        GWTClientService.getGroupsPanelData(datasetId, new AsyncCallback<LinkedHashMap<String, String>[]>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                errorLabel.setText(SERVER_ERROR);
//                RootPanel.get("datasetInformation").setVisible(false);
//                RootPanel.get("loaderImage").setVisible(false);
//            }
//
//            @Override
//            public void onSuccess(LinkedHashMap[] results) {
//                errorLabel.setText("");
//                if (exportGroupPanel == null) {
//                    exportGroupPanel = new ActivateGroupPanel(results[0], null);
//                    exportGroupPanel.getOkBtn().addClickHandler(new ClickHandler() {
//                        @Override
//                        public void onClick(ClickEvent event) {
//                            try {
//                                errorLabel.setVisible(false);
//                                String[] rowValues = exportGroupPanel.getSelectRowGroups();
//                                if (rowValues != null && rowValues.length != 1) {
//                                    exportGroupPanel.getErrorlabl().setVisible(true);
//
//                                } else {
//                                    exportData(datasetId, rowValues[0]);
//                                    exportGroupPanel.hide();
//                                }
//                            } catch (Exception exp) {
//                                Window.alert(exp.getLocalizedMessage());
//                            }
//
//                        }
//                    });
//                } else {
//                    exportGroupPanel.updataChartData(results[0], null);
//                }
//                exportGroupPanel.show();
//
//                results = null;
//                RootPanel.get("loaderImage").setVisible(false);
//
//            }
//        });
//    }

    /**
     * This method is responsible for initializing PCA panel method
     */
//    private void initPcaPanel() {
//        RootPanel.get("loaderImage").setVisible(true);
//        GWTClientService.getPcaColNames(datasetId, new AsyncCallback<String[]>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                errorLabel.setText(SERVER_ERROR);
//                RootPanel.get("datasetInformation").setVisible(false);
//                RootPanel.get("loaderImage").setVisible(false);
//            }
//
//            @Override
//            public void onSuccess(String[] results) {
//                errorLabel.setText("");
//                if (pcaPanel == null) {
//                    pcaPanel = new PCAPanel(results);
//                    ClickHandler pcaPanelHandler = new ClickHandler() {
//                        @Override
//                        public void onClick(ClickEvent event) {
//                            try {
//                                int pcaI = pcaPanel.getPcaI();
//                                int pcaII = pcaPanel.getPcaII();
//                                viewPCAChart(datasetId, pcaI, pcaII);
//                                pcaPanel.hide();
//
//                            } catch (Exception exp) {
//                            }
//
//                        }
//                    };
//                    if (pcaHandlerRegistration != null) {
//
//                        pcaHandlerRegistration.removeHandler();
//                    }
//                    pcaHandlerRegistration = pcaPanel.getOkBtn().addClickHandler(pcaPanelHandler);
//
//                }
//                pcaPanel.show();
//
//                results = null;
//                RootPanel.get("loaderImage").setVisible(false);
//            }
//
//        });
//
//    }

    /**
     * This method is responsible for initializing save dataset panel
     *
     * @param selectedRows - selected rows indexes
     * @param selectedCol - selected columns indexes
     */
//    private void initDsPanel(final int[] selectedRows, final int[] selectedCol) {
//
//        RootPanel.get("loaderImage").setVisible(true);
//        GWTClientService.getColNamesMaps(datasetId, new AsyncCallback<LinkedHashMap<String, String>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                errorLabel.setText(SERVER_ERROR);
//                RootPanel.get("datasetInformation").setVisible(false);
//                RootPanel.get("loaderImage").setVisible(false);
//            }
//
//            @Override
//            public void onSuccess(LinkedHashMap results) {
//                errorLabel.setText("");
//                if (dsPanel == null) {
//                    dsPanel = new DatasetPanel(results, selectedRows, selectedCol);
//                    ClickHandler dsPanelHandler = new ClickHandler() {
//                        @Override
//                        public void onClick(ClickEvent event) {
//                            dsPanel.getOkBtn().disable();
//                            dsPanel.getErrorlabl().setVisible(false);
//                            String processType = dsPanel.getProcessType();
//                            if (processType.equalsIgnoreCase("Groups")) {
//                                String name = dsPanel.getName().getValueAsString();
//                                String color = dsPanel.getColor();
//                                String type = dsPanel.getRadioGroupItemValue();
//                                if (name == null || name.equals("")) {
//                                    dsPanel.getErrorlabl().setVisible(true);
//                                    dsPanel.getForm().validate();
//                                } else if (type.equalsIgnoreCase("COLUMN GROUPS")) {
//                                    String[] selCol = dsPanel.getSelectColsValue();
//                                    if (selCol == null || selCol.length == 0) {
//                                        dsPanel.getErrorlabl().setVisible(true);
//                                        dsPanel.getForm().validate();
//                                    } else {
//                                        dsPanel.getErrorlabl().setVisible(false);
//                                        createColGroup(datasetId, name, color, selCol, type);
//                                        dsPanel.hide();
//                                    }
//                                } else {
//                                    dsPanel.getErrorlabl().setVisible(false);
//
//                                    createRowGroup(datasetId, name, color, type, dsPanel.getRowSelection());
//                                    dsPanel.hide();
//
//                                }
//
//                            } else //save sub-dataset
//                            {
//                                String name = dsPanel.getDatasetName().getValueAsString();
//                                if (name == null || name.equals("")) {
//                                    dsPanel.getErrorlabl().setVisible(true);
//                                    dsPanel.getForm2().validate();
//                                } else {
//                                    createSubDataset(name, selectedRows);
//                                    dsPanel.hide();
//
//                                }
//
//                            }
//
//                        }
//                    };
//                    if (createGroupPanel != null) {
//                        createGroupPanel.removeHandler();
//                    }
//                    createGroupPanel = dsPanel.getOkBtn().addClickHandler(dsPanelHandler);
//
//                } else {
//                    dsPanel.updateDataValues(selectedRows, selectedCol);
//                }
//                dsPanel.getColorLable().setHTML("<p style='height:10px;width:10px;font-weight: bold; color:white;font-size: 10px;background:#ffffff; border-style:double;'></p>");
//
//                dsPanel.show();
//                results = null;
//                RootPanel.get("loaderImage").setVisible(false);
//            }
//
//        });
//
//    }

    /**
     * This method is responsible for reset layout in case of changing datasets
     * only
     */
    private void resetLayout() {
        profilePlotLayout.clear();
        PCAPlotLayout.clear();
        RootPanel.get("SomClusteringResults").clear();
        RankLayout.clear();
//        RootPanel.get("LineChartResults").clear();
//        RootPanel.get("LineChartResults").add(initImgs.getlCImg());
//        RootPanel.get("PCAChartResults").clear();
//        RootPanel.get("PCAChartResults").add(initImgs.getPcaImg());
//        
//        RootPanel.get("SomClusteringResults").add(initImgs.getHcImg());
//        hierarchicalClustering = null;
        compponents[0] = null;
        compponents[1] = null;
//        RootPanel.get("RankTablesResults").clear();
//        RootPanel.get("RankTablesResults").add(initImgs.getRtImg());
    }

    /**
     * This method is responsible for update line chart, and PCA chart when
     * creating or activating row groups
     */
//    private void updateRowGroups() {
//        for (ModularizedListener o : compponents) {
//            if (o == null) {
//                continue;
//            }
//            if (o instanceof ProfilePlotComponent) {
//                viewLineChart(datasetId);
//
//            } else {
//                viewPCAChart(datasetId, pcaI, pcaII);
//            }
//        }
//
//    }

    /**
     * This method is responsible for deactivating all visualizations and
     * showing the progress image when performing heavy tasks
     */
    private void busyTask(boolean busy) {
        
        busyProcess = busy;
        busyIndicator.busyTask(busy);
        
//        if (busy) {
//            if (btnsMenueComponent != null) {
////                btnsMenueComponent.deactivatMenue();
//            }
//
//            selectDatasetList.setEnabled(false);
//
//            if (leftPanelView != null) {
//                leftPanelView.disable();
//            }
//
//////            if (rankTables != null) {
//////                rankTables.disable();
//////            }
//////
//////            if (pcaPlotComponent != null) {
//////                pcaPlotComponent.enable(false);
//////            }
//////
//////            if (hierarchicalClustering != null) {
//////                hierarchicalClustering.componentView().disable();
//////            }
//
//        } else {
//            if (btnsMenueComponent != null) {
//                btnsMenueComponent.activatMenue();
//            }
//
//             selectDatasetList.setEnabled(true);
//
//            if (leftPanelView != null) {
//                leftPanelView.enable();
//            }
//
//            if (rankTables != null) {
//                rankTables.enable();
//            }
//            if (pcaPlotComponent != null) {
//                pcaPlotComponent.enable(true);
//            }
//            if (hierarchicalClustering != null) {
//                hierarchicalClustering.componentView().enable();
//            }

//        }
        RootPanel.get("loaderImage").setVisible(busy);

    }
    
  
}
