package web.diva.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.client.omicstables.view.LeftPanelView;
import web.diva.client.profileplot.view.ProfilePlotComponent;
import web.diva.client.somclust.view.SomClustView;
import web.diva.shared.model.core.model.dataset.DatasetInformation;
import web.diva.client.pca.view.UpdatedPCAPlot;
import web.diva.client.rank.view.RankTablesComponent;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.somclust.view.UpdatedSomClustView;
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

    private  final SelectionManager selectionManager = new SelectionManager();
    private static final String SERVER_ERROR = "An error occurred while attempting to contact the server";
    private final Label errorLabel = new Label();
    
//    private final int leftPanelWidth = RootPanel.get("templatemo_left").getOffsetWidth();
    private final int rightPanelWidth = RootPanel.get("SomClusteringResults").getOffsetWidth()-10;

    private VerticalPanel profilePlotLayout,PCAPlotLayout;
//    private VLayout rankLayout;
    private  RankTablesComponent rankTables;
   
   //init analysis and functions buttons
//    private final ButtonsBarMenuComponent btnsMenueComponent = new ButtonsBarMenuComponent();;
    private final TreeMap datasetsNames = new TreeMap();
    private LeftPanelView leftPanelView;
    
    private final GreetingServiceAsync GWTClientService = GWT.create(GreetingService.class);
//    private final ModularizedListener[] compponents = new ModularizedListener[2];

    private  ListBox selectDatasetList;
    private UpdatedPCAPlot pcaPlotComponent;
    
    private DatasetInformation datasetInfo;


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
private Label datasetTitle;
    @Override
    public void onModuleLoad() {
        selectionManager.busyTask(true,true);
        selectionManager.setMainAppController(this);
        datasetTitle = new Label();
        RootPanel.get("dataset_main_title").add(datasetTitle);
        this.initApplication();
    }

    
    
    /**
     * This method is responsible for initializing the main DIVA layout
     *
     */
    private void initApplication() {
        //init dataset information panel        
        
//        initHeaderLayout();
        //init dropdown dataset select list
        selectDatasetList = new ListBox();
        selectDatasetList.setWidth("300px");
        selectDatasetList.addItem("Select Dataset");
         
        getDatasetsList();//get available dataset names
        RootPanel.get("headerLayout").add(selectDatasetList);;
//        initImgs = new InitImgs();
        selectDatasetList.addChangeHandler(this);
        initMiddleBodyLayout();
         
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
                datasetTitle.setText(selectDatasetList.getItemText(selectDatasetList.getSelectedIndex()));
                loadDataset(datasetId);
                selectDatasetList.setItemSelected(0, true);
                
                
            } catch (Exception e) {
                Window.alert("exp " + e.getMessage());
            }
        }
    }
    
    public void changeDSSelection(int datasetId){
        selectionManager.resetSelection();
        datasetsNames.clear();
        selectDatasetList.clear();
      initApplication();
//        selectDatasetList.addItem("Select Dataset");
//        getDatasetsList();//get available dataset names
//        while(selectDatasetList.getItemCount() < datasetId)
//        {
//        }
        selectDatasetList.setSelectedIndex(datasetId);
        selectDatasetList.setItemSelected(datasetId, true);
        selectDatasetList.setValue(datasetId, selectDatasetList.getItemText(datasetId));
//        Window.alert("loading ds  "+datasetId);
//        loadDataset(datasetId);
        
    
    }
    private final int userTabId = Random.nextInt(1000000001);


    /**
     * This method is responsible for initializing dataset drop-down list
     */
    private void getDatasetsList() {
        GWTClientService.getAvailableDatasets(userTabId,new AsyncCallback<TreeMap<Integer, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                selectionManager.busyTask(false,true);
            }

            @Override
            public void onSuccess(TreeMap results) {
                errorLabel.setText("");
                for (Object o : results.keySet()) {
                    int key = (Integer) o;
                    selectDatasetList.addItem((String) results.get(key));
                    datasetsNames.put(results.get(key), key);
                    selectionManager.busyTask(false,true);
                }
            }

        });

    }
    private boolean init = true;

    /**
     * This method is responsible for loading dataset upon user selection
     *
     * @param datasetId - datasetId
     *
     */
    private void loadDataset(int datasetId) {
        selectionManager.busyTask(true,true);
        GWTClientService.setMainDataset(datasetId,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        Window.alert(SERVER_ERROR);
                    }

                    @Override
                    public void onSuccess(DatasetInformation datasetInfos) {                        
                        init = true;
                        reload = false;
                        loadingAnalysis(datasetInfos);
                    }
                });
    }
    private boolean reload = false;

    private void loadingAnalysis(DatasetInformation datasetInfos) {
        datasetInfo = datasetInfos;
        updateLeftPanel(datasetInfos);
//        initMiddleBodyLayout();
//        boolean wait = true;
        if (reload) {
            Selection s = selectionManager.getSelectedRows();
            selectionManager.setSelectedRows(s);          
            return;
        }
//        wait = resetMidPanelHeights(RootPanel.get("omicsTable").getOffsetHeight() - 4);

//        if (wait) {
            processProfilePlot();
            reload = true;
//        }

        if (!init) {
            selectionManager.busyTask(false,true);
        }

    }

    private void updateLeftPanel(DatasetInformation datasetInfos) {

        this.datasetInfo = datasetInfos;
        if (leftPanelView != null) {
            leftPanelView.deparent();
        }
        leftPanelView = new LeftPanelView(selectionManager, GWTClientService, datasetInfos);
        leftPanelView.setStyleName("whiteLayout");
        RootPanel.get("omicsTable").clear(true);
        RootPanel.get("omicsTable").add(leftPanelView);

    }

//    private void initHeaderLayout() {
////     RootPanel.get("row").add(rowLab);
////        RootPanel.get("col").add(colLab);
////        RootPanel.get("rowGroups").add(rowGroup);
////        RootPanel.get("colGroups").add(colGroup);
////        RootPanel.get("Hierarchical_Clustering_btn").add(btnsMenueComponent.getSomClustBtn());
////        profilePlotBtn = btnsMenueComponent.getProfilePlotBtn();
////        RootPanel.get("Profile_Plot_btn").add(profilePlotBtn);
////        RootPanel.get("PCA_btn").add(btnsMenueComponent.getPcaBtn());
////        RootPanel.get("Rank_Product_btn").add(btnsMenueComponent.getRankBtn());
//    
//    }
    
    private VerticalPanel rankLayout;
    
    private void initMiddleBodyLayout() {   
        VerticalPanel midPanelLayout = new VerticalPanel();
        midPanelLayout.setSpacing(1);
        RootPanel.get("templatemo_mid").clear();
        RootPanel.get("templatemo_mid").add(midPanelLayout);
//        midPanelLayout.setWidth((RootPanel.get("templatemo_mid").getOffsetWidth()-4)+"px");
        midPanelLayout.setStyleName("midpanelborder");
        
//        midPanelLayout.setHeight(RootPanel.get("templatemo_mid").getOffsetHeight()+"px");
        
        
        //pca and profile plot layout
        HorizontalPanel topMedLayout = new HorizontalPanel();
        rankLayout = new VerticalPanel();
        midPanelLayout.add(topMedLayout);
        midPanelLayout.add(rankLayout);
//        rankLayout.setStyleName("lightborder ");
//        
//        int width = midPanelLayout.getOffsetWidth();
        
//        topMedLayout.setWidth(width+"px");
//        topMedLayout.setHeight((midPanelLayout.getOffsetHeight()* 40 / 100)+"px");
//        topMedLayout.setStyleName("layouttest1");
        
//        rankLayout.setWidth((width)+"px");
        
//        rankLayout.setStyleName("layouttest2");
        
        
//        topMedLayout.setStyleName("layouttest1");
        
        profilePlotLayout = new VerticalPanel();
        
        PCAPlotLayout = new VerticalPanel();
        topMedLayout.add(profilePlotLayout);
        topMedLayout.add(PCAPlotLayout);
        topMedLayout.setCellHorizontalAlignment(profilePlotLayout, HorizontalPanel.ALIGN_LEFT);
        topMedLayout.setCellHorizontalAlignment(PCAPlotLayout, HorizontalPanel.ALIGN_RIGHT);
        
//        width  = midPanelLayout.getOffsetWidth()/2;
//        int height =midPanelLayout.getOffsetHeight()*40/100;
//        profilePlotLayout.setBorderWidth(1);
//        profilePlotLayout.setPixelSize(width,height);//Width(width+"px");
//        profilePlotLayout.setHeight(height+"px");
//        profilePlotLayout.setStyleName("layouttest3");
        
        
//        PCAPlotLayout.setBorderWidth(1);
//        topMedLayout.addMember(PCAPlotLayout);
//         PCAPlotLayout.setBorder("#000000 1px solid");
//        PCAPlotLayout.setPixelSize(width,height);//.setWidth(width+"px");
//        pcaWidth = width-2;
//        PCAPlotLayout.setHeight(height+"px");
//        PCAPlotLayout.setStyleName("layouttest4");

        
        
        
//        Window.alert("first heghi is "+rankLayout.getOffsetHeight()+"  first width "+ rankLayout.getOffsetWidth());
       
    }

//    private void activateAnalysisFunctions(String[] colsNames) {
//        
////        initializeSomClusteringBtn();
////        initializeProfilePlotBtn();
////        initializePCABtn(colsNames);
//////        initializeRankBtn(datasetInfo.getColGroupsList());
////        RootPanel.get("Hierarchical_Clustering_btn").clear();
////        RootPanel.get("Hierarchical_Clustering_btn").add(somClustBtn);
////        RootPanel.get("PCA_btn").clear();
////        RootPanel.get("PCA_btn").add(pcaBtn);
////        RootPanel.get("Rank_Product_btn").clear();
////        RootPanel.get("Rank_Product_btn").add(rankBtn);
////        
//    }
    
//    private void initializeSomClusteringBtn() {
//        somClustBtn = new SomClusterBtn();
//        ClickHandler somClustHandler = new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                
//                    processSomClustering(somClustBtn.getLinkage(), somClustBtn.getDistanceMeasure(),true);
//                    somClustBtn.hidePanel();
//              
//            }
//        };
//        somClustBtn.setClickListener(somClustHandler);
//
//    }
//    
//    private void initializeProfilePlotBtn() {
//        com.google.gwt.event.dom.client.ClickHandler profilePlotHandler = new com.google.gwt.event.dom.client.ClickHandler() {
//            @Override
//            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
//               
//                    processProfilePlot();
//               
//            }
//        };
//        profilePlotBtn.addClickHandler(profilePlotHandler);
//
//    }
//
//    
//    private void initializePCABtn(String[] colsNames){
//    pcaBtn = new PCABtn(colsNames);
//    ClickHandler pcaHandler = new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
////                if (!busyProcess) {
//                    int pcaI = pcaBtn.getPcaPanel().getPcaI();
//                    int pcaII = pcaBtn.getPcaPanel().getPcaII();
//                    viewPCAChart(pcaI, pcaII);
//                    pcaBtn.getPcaPanel().hide();
////                }
//            }
//        };
//        pcaBtn.setClickListener(pcaHandler);
//    }
    
//     private void initializeRankBtn(List<ColumnGroup>colGroupsList){
//    rankBtn = new RankBtn(colGroupsList);
//    ClickHandler rankHandler = new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                rankBtn.setErrorlablVisible(false);
//                                List<String> groups = rankBtn.getSelectColGroups();
//                                String seed = rankBtn.getSeed();
//                                String perm = rankBtn.getPerm();
//                                String log2 = rankBtn.getLog2();
//                                if (groups == null || groups.isEmpty() || groups.size() > 2 || seed == null || seed.equals("") || perm == null || perm.equals("")) {
//                                    rankBtn.setErrorlablVisible(true);
//                                    rankBtn.rankPanelvalidate();
//                                } else {
//                                    viewRankTables(perm, seed, groups, log2);
//                                    rankBtn.hidePanel();
//                                }
//                    rankBtn.hidePanel();
//            }
//        };
//        rankBtn.setClickListener(rankHandler);
//    }
    /**
     * This method is responsible for invoking clustering method
     *
     * @param datasetId - datasetId
     * @param linkage - selected clustering linkage type
     * @param distanceMeasure - the selected clustering distance measurement for
     * clustering
     *
     */
    private void processSomClustering(int linkage, int distanceMeasure,final boolean clusterColumns) {
        selectionManager.busyTask(true,true);
        GWTClientService.computeSomClustering(linkage, distanceMeasure,clusterColumns,
                new AsyncCallback<SomClusteringResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
//                        RootPanel.get("datasetInformation").setVisible(false);
//                        init=false;
                        selectionManager.busyTask(false,true);
                    }

                    @Override
                    public void onSuccess(SomClusteringResult result) {
//                        RootPanel.get("datasetInformation").setVisible(true);
                        errorLabel.setText("");
                        UpdatedSomClustView hierarchicalClustering = new UpdatedSomClustView(result, selectionManager,GWTClientService,rightPanelWidth,layoutHeight,clusterColumns);
                       
                        RootPanel.get("SomClusteringResults").clear();
                        RootPanel.get("SomClusteringResults").add(hierarchicalClustering.getSomclusteringLayout());
//                        generateHeatMap(hierarchicalClustering.getIndexer(), hierarchicalClustering.getColIndexer());
                        selectionManager.busyTask(false,true);
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
        selectionManager.busyTask(true,true);
        GWTClientService.computeProfilePlot(212,250,
                new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                         Window.alert(SERVER_ERROR);
//                        init=false;
                        selectionManager.busyTask(false,true);
                    }

                    @Override
                    public void onSuccess(String result) {
                        errorLabel.setText("");
                        if (profilePlotComponent != null) {
                            profilePlotComponent.remove();
                        }
                        
                        profilePlotComponent = new ProfilePlotComponent(result, selectionManager, GWTClientService);
//                        compponents[0] = profilePlotComponent;
                        
                        profilePlotLayout.clear();
                        profilePlotLayout.add(profilePlotComponent.getLayout().asWidget());
                        profilePlotComponent.getLayout().setMargin(2);
//                        profilePlotLayout.setBorderWidth(1);
        

//                        profilePlotComponent.getLayout().asWidget().setHeight(profilePlotLayout.getHeight()+"px");
//                        profilePlotComponent.getLayout().asWidget().setWidth(profilePlotLayout.getWidth()+"px");
                        
                       
                        if (!init) {
                            selectionManager.busyTask(false,true);
                        }
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

        selectionManager.busyTask(true,true);
        GWTClientService.computePCA(pcaI, pcaII,
                new AsyncCallback<PCAImageResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
//                        errorLabel.setText(SERVER_ERROR);
//                        RootPanel.get("datasetInformation").setVisible(false);
//                        init=false;
                        selectionManager.busyTask(false,true);
                    }

                    @Override
                    public void onSuccess(PCAImageResult result) {
//                        errorLabel.setText("");
//                        RootPanel.get("datasetInformation").setVisible(true);
                        if (pcaPlotComponent != null) {
                            pcaPlotComponent.remove();
                        }
                        pcaPlotComponent = new UpdatedPCAPlot(result, selectionManager, GWTClientService,datasetInfo.getColNumb(),datasetInfo.getDatasetInfo());
                        
                        PCAPlotLayout.clear();
                        PCAPlotLayout.add(pcaPlotComponent.getPCAComponent());
                        pcaPlotComponent.getPCAComponent().setMargin(2);
//                        pcaPlotComponent.getPCAComponent().setHeight("" + 60 + "%");
//                        pcaPlotComponent.getPCAComponent().setWidth("" + 100 + "%");
                        if(!init)
                            selectionManager.busyTask(false,true);
                        if (init) {
                            viewRankTables("400", "288848379", Arrays.asList(new String[]{"All",}), "Log 2");
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
    private void viewRankTables( String perm, String seed, List<String> colGropNames, String log2) {
        selectionManager.busyTask(true,true);
        GWTClientService.computeRank(perm, seed, colGropNames, log2,
                new AsyncCallback<RankResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(SERVER_ERROR);
//                        init=false;
                        selectionManager.busyTask(false,true);
                    }

                    @Override
                    public void onSuccess(RankResult result) {
                       
                        errorLabel.setText("");
                        rankLayout.clear();
                        rankTables = new RankTablesComponent(GWTClientService, selectionManager, result, datasetInfo.getColGroupsList());
                        rankLayout.add(rankTables.getMainRankLayout());
//                        rankTables.getMainRankLayout().setWidth(rankLayout.getOffsetWidth()+"px");
//                        rankTables.getMainRankLayout().setHeight(rankLayoutHeight+"px");
//                       rankLayout.setBorderWidth(1);
                        
                        if(!init)
                            selectionManager.busyTask(false,true);
                        if (init) {
                            processSomClustering(2, 1,true);
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
//    private void resetLayout() {
////        profilePlotLayout.clear();
////        PCAPlotLayout.clear();
////        RootPanel.get("SomClusteringResults").clear();
////        rankLayout.clear();
////        RootPanel.get("LineChartResults").clear();
////        RootPanel.get("LineChartResults").add(initImgs.getlCImg());
////        RootPanel.get("PCAChartResults").clear();
////        RootPanel.get("PCAChartResults").add(initImgs.getPcaImg());
////        
////        RootPanel.get("SomClusteringResults").add(initImgs.getHcImg());
////        hierarchicalClustering = null;
////        compponents[0] = null;
////        compponents[1] = null;
////        RootPanel.get("RankTablesResults").clear();
////        RootPanel.get("RankTablesResults").add(initImgs.getRtImg());
//    }

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
//    private void busyTask(boolean busy) {
//        
//        busyProcess = busy;
//        busyIndicator.busyTask(busy);
        
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
//////                hierarchicalClustering.getSomclusteringLayout().disable();
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
//                hierarchicalClustering.getSomclusteringLayout().enable();
//            }

////        }
//        RootPanel.get("loaderImage").setVisible(busy);

//    }
//    pcaWidth
    private int layoutHeight;
      private boolean resetMidPanelHeights(int leftPanelHeight) {
//          layoutHeight= leftPanelHeight;
//        profilePlotLayout.setHeight((leftPanelHeight * 40 / 100) + "px");
//        topLayoutHeight = leftPanelHeight *40/100;
//        PCAPlotLayout.setHeight((leftPanelHeight * 40 / 100) + "px");
//        rankLayout.setHeight((leftPanelHeight - (leftPanelHeight * 40 / 100)-5) + "px");
//        rankLayoutHeight = leftPanelHeight - topLayoutHeight-5;
        return true;

    }
      
      public void updateApp(DatasetInformation datasetInfos){
   
          loadingAnalysis(datasetInfos);
      
      }
}
