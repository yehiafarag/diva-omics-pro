package web.diva.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.TreeMap;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.client.omicstables.view.LeftPanelView;
import web.diva.client.profileplot.view.ProfilePlotComponent;
import web.diva.shared.model.core.model.dataset.DatasetInformation;
import web.diva.client.pca.view.PCAPlotComponent;
import web.diva.client.rank.view.RankTablesComponent;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.somclust.view.SomClustComponent;
import web.diva.client.view.core.ImageScaler;
import web.diva.shared.beans.LineChartResults;
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
public class DivaMain implements EntryPoint, ChangeHandler {

    private  SelectionManager selectionManager;
    private final String SERVER_ERROR = "An error occurred while attempting to contact the server";
//    private VLayout profilePlotLayout, PCAPlotLayout;
    private RankTablesComponent rankTables;
    private final TreeMap datasetsNames = new TreeMap();
    private LeftPanelView leftPanelView;
    private final DivaServiceAsync DivaClientService = GWT.create(DivaService.class);
    private ListBox selectDatasetList,tempSelectDatasetList;
    
    private ListBox selectSubDatasetList;
    private PCAPlotComponent pcaPlotComponent;
    private DatasetInformation datasetInfo;
    private Label datasetTitle;
    private final HorizontalPanel welcomePage = new HorizontalPanel();
    private boolean oldIE = false;
    
    private int masterWidth;
    private int masterHeight;

    @Override
    public void onModuleLoad() {
        if(Window.Navigator.getUserAgent().contains("MSIE"))
            oldIE= true;
        masterWidth= Page.getScreenWidth();
        masterHeight=Page.getScreenHeight();
        calcAllDim();
        selectionManager = new SelectionManager();
        SelectionManager.Busy_Task(true, true);
        selectionManager.setMainAppController(this);
        datasetTitle = new Label();   

        RootPanel.get("dataset_main_title").add(datasetTitle);
        this.initApplication();
    }
    private int leftPanelWidth,leftPanelHeight,medPanelWidth,rightPanelWidth;
    private void calcAllDim(){
        
   leftPanelWidth = Window.getClientWidth()*20/100;
   leftPanelHeight = Window.getClientHeight()-100;
   
   medPanelWidth = Window.getClientWidth()/2;   
   if(Double.valueOf(medPanelWidth)%2.0 >0.0)
       medPanelWidth = medPanelWidth-1;
   rightPanelWidth = Window.getClientWidth()-(10+leftPanelWidth+2+medPanelWidth+2+10);
   
   
    
    }
//    private final ImageScaler scaler = new ImageScaler();
    private void resizeApp(){
    calcAllDim();
    leftPanelView.resize(leftPanelWidth, leftPanelHeight);
    midPanelLayoutCanv.setWidth(medPanelWidth+"px");
    midPanelLayoutCanv.setHeight((leftPanelHeight-2)+"px");   
    profilePlotComponent.resize(medPanelWidth);
    
        int newWidth = (medPanelWidth / 2) ;
        int newHeight = newWidth + 22;
        topMidLayout.setHeight(newHeight + "px");
        topMidLayout.setWidth(medPanelWidth + "px");
        rankLayoutCanv.setHeight((leftPanelHeight - newHeight - 4) + "px");
        rankLayoutCanv.setWidth(medPanelWidth + "px");
        

    }

   

    /**
     * This method is responsible for initializing the main DIVA layout
     *
     */
    private void initApplication() {
        VerticalPanel selectDatasetLayout = new VerticalPanel();
        selectDatasetLayout.setWidth("300px");
        selectDatasetLayout.setHeight("40px");
        selectDatasetList = new ListBox();
        selectDatasetList.getElement().setAttribute("style", "border: 1px solid gray;height: 24px;font-weight: bold;width: 300px;border-radius: 5px;");
        selectDatasetList.setWidth("300px");
        selectDatasetList.addItem("Select Dataset");
        selectDatasetLayout.add(selectDatasetList);
        selectDatasetList.setVisible(false);
        
        selectSubDatasetList = new ListBox();
        selectSubDatasetList.getElement().setAttribute("style", "border: 1px solid gray;height: 24px;font-weight: bold;width: 300px;border-radius: 5px;");
        selectSubDatasetList.setWidth("300px");
        selectSubDatasetList.addItem("Select Sub-Dataset");
        selectSubDatasetList.setVisible(false);
        selectDatasetLayout.add(selectSubDatasetList);
        
        tempSelectDatasetList = new ListBox();
        tempSelectDatasetList.addItem("Select Dataset");

        tempSelectDatasetList.setWidth("300px");
        tempSelectDatasetList.setHeight("24px");

        getDatasetsList("");//get available dataset names
        RootPanel.get("dropdown_select").add(selectDatasetLayout);
        selectDatasetList.addChangeHandler(this);
        selectSubDatasetList.addChangeHandler(this);
        
        tempSelectDatasetList.addChangeHandler(this);
        tempSelectDatasetList.addChangeHandler(this);
        Window.addResizeHandler(new ResizeHandler() {

            @Override
            public void onResize(ResizeEvent event) {
                if (masterWidth != Page.getScreenWidth() || masterHeight != Page.getScreenHeight()) {
                    BooleanCallback ok = new BooleanCallback() {

                        @Override
                        public void execute(Boolean value) {
                            Window.Location.reload();
                        }
                    };
                    SC.warn("You have changed the Screen size the application need. To reload the page press OK  or press cancel and back to the old screen", ok);

                }

                redrawTimer.schedule(500);

            }
        });
        redrawTimer = new Timer() {

            @Override
            public void run() {
//                resizeApp();
////             
            }
        };
        initHomePage();
        welcomePage.setStyleName("welcomepagelayout");
        RootPanel.get("welcomediva").add(welcomePage);
        if(!oldIE)
            initMiddleBodyLayout();
    }

    /**
     * on select dataset
     *
     * @param event user select dataset
     *
     */
    @Override
    public void onChange(ChangeEvent event) {
        if (tempSelectDatasetList.getSelectedIndex() > 0) {
            try {

                RootPanel.get("welcomediva").clear(true);
                selectSubDatasetList.clear();
                selectSubDatasetList.addItem("Select Sub-Dataset");
                selectDatasetList.setVisible(true);
                selectSubDatasetList.setVisible(false);
                int datasetId = (Integer) datasetsNames.get(tempSelectDatasetList.getItemText(tempSelectDatasetList.getSelectedIndex()));
                selectionManager.resetSelection();
                datasetTitle.setText(tempSelectDatasetList.getItemText(tempSelectDatasetList.getSelectedIndex()));
                loadDataset(datasetId);
                updateSubDsSelectionList(tempSelectDatasetList.getItemText(tempSelectDatasetList.getSelectedIndex()));
                tempSelectDatasetList.setItemSelected(0, true);

            } catch (Exception e) {
                Window.alert("exp " + e.getMessage());
            }
        } else if (selectDatasetList.getSelectedIndex() > 0) {
            try {
                selectSubDatasetList.clear();
                selectSubDatasetList.addItem("Select Sub-Dataset");
                selectSubDatasetList.setVisible(false);
                int datasetId = (Integer) datasetsNames.get(selectDatasetList.getItemText(selectDatasetList.getSelectedIndex()));
                selectionManager.resetSelection();
                datasetTitle.setText(selectDatasetList.getItemText(selectDatasetList.getSelectedIndex()));
                loadDataset(datasetId);
                updateSubDsSelectionList(selectDatasetList.getItemText(selectDatasetList.getSelectedIndex()));
                selectDatasetList.setItemSelected(0, true);

            } catch (Exception e) {
                Window.alert("exp " + e.getMessage());
            }
        } else if (selectSubDatasetList.getSelectedIndex() > 0) {
            int datasetId = (Integer) datasetsNames.get(selectSubDatasetList.getItemText(selectSubDatasetList.getSelectedIndex()));
            selectionManager.resetSelection();
            datasetTitle.setText(selectSubDatasetList.getItemText(selectSubDatasetList.getSelectedIndex()));
            loadDataset(datasetId);
            updateSubDsSelectionList(selectSubDatasetList.getItemText(selectSubDatasetList.getSelectedIndex()));
            selectDatasetList.setItemSelected(0, true);

        }
    }

    private void updateSubDsSelectionList(String datasetName) {
        for (int x = 0; x < selectDatasetList.getItemCount(); x++) {
            String dsName = selectDatasetList.getItemText(x);
            if (dsName.contains("( SUB DS - " + datasetName + " )")) {
                selectSubDatasetList.addItem(dsName);
            }
        }
        if (selectSubDatasetList.getItemCount() > 1) {
            selectSubDatasetList.setVisible(true);
        }
    }

    private final int userTabId = Random.nextInt(1000000001);

    /**
     * This method is responsible for initializing dataset drop-down list
     */
    private void getDatasetsList(final String newName) {
        DivaClientService.getAvailableDatasets(userTabId, new AsyncCallback<TreeMap<Integer, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("DiVAFiles folder is not available please contact the system administrator");
                SelectionManager.Busy_Task(false, true);
            }

            @Override
            public void onSuccess(TreeMap results) {
                for (Object o : results.keySet()) {
                    int key = (Integer) o;
                    selectDatasetList.addItem((String) results.get(key));
                    datasetsNames.put(results.get(key), key);                    
                    selectDatasetList.setItemSelected(0, true);
                    tempSelectDatasetList.addItem((String) results.get(key));
                     tempSelectDatasetList.setItemSelected(0, true);
                    SelectionManager.Busy_Task(false, true);
                }
                updateSubDsSelectionList(newName);
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
        SelectionManager.Busy_Task(true, true);
        DivaClientService.setMainDataset(datasetId,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
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
        if (reload) {
            Selection s = selectionManager.getSelectedRows();
            selectionManager.setSelectedRows(s);
            return;
        }
        processProfilePlot();
        reload = true;

        if (!init) {
            SelectionManager.Busy_Task(false, true);
        }

    }
    private void initHomePage() {
        welcomePage.setSpacing(30);
        HTML welcomeLabel = new HTML("<h1 style='color:black;font-family:verdana; font-weight:bold;text-decoration:none;font-style:normal;'>Welcome to DiVA <font size='3'>omics </h1>");
        welcomeLabel.setWidth("400px");
        VerticalPanel leftSideLayout = new VerticalPanel();
        welcomePage.add(leftSideLayout);
        welcomePage.setCellVerticalAlignment(leftSideLayout, VerticalPanel.ALIGN_MIDDLE);
        welcomePage.setCellHorizontalAlignment(leftSideLayout, VerticalPanel.ALIGN_RIGHT);
        leftSideLayout.add(welcomeLabel);
        leftSideLayout.setSpacing(10);

        HTML ieErrorLabel = new HTML("<p align=\"left\" style=\"margin-left:0px;color:red;\"><font size=\"3\">It looks like you are using an old version of Internet Explorer. Unfortunatly Internet Explorer 6,7,8,9 and 10 are not supported in the curent version of DiVA. Please try other browsers.</font></p>");
        ieErrorLabel.setWidth("400px");
      
        HTML infoLabel = new HTML("<p align=\"justify\" style=\"margin-left:0px;color:#585858;\"><font size=\"2\">Start using DiVA by selecting dataset</font></p>");
        infoLabel.setWidth("400px");
        

        HorizontalPanel selectionLayout = new HorizontalPanel();
        
        
        
        if(oldIE)
              leftSideLayout.add(ieErrorLabel);
        else{
            leftSideLayout.add(infoLabel);
            leftSideLayout.add(selectionLayout);
        selectionLayout.setWidth("400px");
         selectionLayout.add(tempSelectDatasetList);
        }
           

        tempSelectDatasetList.getElement().setAttribute("style", "border: 1px solid gray;height: 24px;font-weight: bold;width: 400px;border-radius: 5px;");

        leftSideLayout.setCellVerticalAlignment(tempSelectDatasetList, VerticalPanel.ALIGN_MIDDLE);
        leftSideLayout.setCellHorizontalAlignment(tempSelectDatasetList, VerticalPanel.ALIGN_LEFT);

        HTML info2Label = new HTML("<p align=\"justify\" style=\"margin-top:10px;margin-left:0px;color:#585858;\"><font size=\"2\">More information available at <a target=\"_blank\" href='" + "http://diva-omics-demo.googlecode.com/" + "'>DiVA omics page</a>. </font></p>");
        leftSideLayout.add(info2Label);

        Image screenImg = new Image("images/divascreen1.png");
        welcomePage.add(screenImg);
        welcomePage.setCellVerticalAlignment(screenImg, VerticalPanel.ALIGN_MIDDLE);
        welcomePage.setCellHorizontalAlignment(screenImg, VerticalPanel.ALIGN_CENTER);
        tempSelectDatasetList.setFocus(true);

    }

    private void updateLeftPanel(DatasetInformation datasetInfos) {

        this.datasetInfo = datasetInfos;
        if (leftPanelView != null) {
            leftPanelView.deparent();
        }
        leftPanelView = new LeftPanelView(selectionManager, DivaClientService, datasetInfos,leftPanelWidth,leftPanelHeight);
        leftPanelView.setStyleName("whitelayout");
        RootPanel.get("leftpanel").clear(true);
        RootPanel.get("leftpanel").add(leftPanelView);

        
//        midPanelLayoutCanv.setBorder("1px solid #F6F5F5");
        midPanelLayoutCanv.setStyleName("diva_mid_panel_border");
    }

    private VLayout rankLayoutCanv;
    private VerticalPanel midPanelLayoutCanv;
    private HorizontalPanel topMidLayout;

    private void initMiddleBodyLayout() {
        midPanelLayoutCanv = new VerticalPanel();
        midPanelLayoutCanv.setWidth(medPanelWidth+"px");
        midPanelLayoutCanv.setHeight((leftPanelHeight-2)+"px");
        RootPanel.get("diva_mid_panel").clear();
        RootPanel.get("diva_mid_panel").add(midPanelLayoutCanv);
        //pca and profile plot layout
        topMidLayout = new HorizontalPanel();
        int newWidth = (medPanelWidth / 2);
        int newHeight = newWidth+ 22;
        topMidLayout.setHeight(newHeight + "px");
        topMidLayout.setWidth(medPanelWidth + "px");
        topMidLayout.setStyleName("whitelayout");

        //rank table layout
        rankLayoutCanv = new VLayout();
        rankLayoutCanv.setHeight((leftPanelHeight - (newHeight + 2)) + "px");
        rankLayoutCanv.setWidth(medPanelWidth + "px");        
        midPanelLayoutCanv.add(topMidLayout);
        midPanelLayoutCanv.add(rankLayoutCanv);
    }
    private Timer redrawTimer;

   
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
//        selectionManager.Busy_Task(true, true);
        DivaClientService.computeSomClustering(linkage, distanceMeasure, clusterColumns,
                new AsyncCallback<SomClusteringResult>() {
                    @Override
                    public void onFailure(Throwable caught) {

                        Window.alert(SERVER_ERROR);
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(SomClusteringResult result) {
                        final SomClustComponent hierarchicalClustering = new SomClustComponent(result, selectionManager, DivaClientService, true,rightPanelWidth,leftPanelHeight-2);
                        RootPanel.get("SomClusteringResults").clear();
                        RootPanel.get("SomClusteringResults").add(hierarchicalClustering.getSomclusteringLayout());
                        Timer t = new Timer() {
                            @Override
                            public void run() {
                                hierarchicalClustering.selectRootNode();
                            }
                        };
                        // Schedule the timer to run once in 0.5 seconds.
                        if (result.getRowNames().length < 5000) {
//                            t.schedule(500);
                             SelectionManager.Busy_Task(false, true);
                        } else {
                            SelectionManager.Busy_Task(false, true);
                        }
                        init = false;
                    }
                });

    }

    public void updateClusteringPanel(SomClusteringResult result,boolean clusterColumn) {
        SelectionManager.Busy_Task(true, true);
        SomClustComponent hierarchicalClustering = new SomClustComponent(result, selectionManager, DivaClientService, clusterColumn,rightPanelWidth,leftPanelHeight-2);

        RootPanel.get("SomClusteringResults").clear();

        RootPanel.get("SomClusteringResults").add(hierarchicalClustering.getSomclusteringLayout());
        SelectionManager.Busy_Task(false, true);

    }

    private ProfilePlotComponent profilePlotComponent;

    /**
     * This method is responsible for invoking line chart method
     *
     * @param datasetId - datasetId
     *
     */
    private void processProfilePlot() {
        DivaClientService.computeProfilePlot(900,900,
                new AsyncCallback<LineChartResults>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(SERVER_ERROR);
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(LineChartResults result) {
                        if (profilePlotComponent != null) {  
                            topMidLayout.remove(profilePlotComponent.getLayout());
                            profilePlotComponent.getLayout().removeFromParent();
                            profilePlotComponent.remove();
                        }
                        if (pcaPlotComponent != null) {  
                            topMidLayout.remove(pcaPlotComponent.getPCAComponent());
                            pcaPlotComponent.getPCAComponent().removeFromParent();
                            pcaPlotComponent.remove();
                        }

                        profilePlotComponent = new ProfilePlotComponent(result.getUrl(), selectionManager, DivaClientService,result.getImgHeight(),result.getImgWidth(),medPanelWidth);                       
                        topMidLayout.add(profilePlotComponent.getLayout());
                        if (!init) {
                            SelectionManager.Busy_Task(false, true);
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

//        selectionManager.Busy_Task(true, true);
        DivaClientService.computePCA(pcaI, pcaII,
                new AsyncCallback<PCAImageResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(SERVER_ERROR);
//                        selectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(PCAImageResult result) {
//                        if (pcaPlotComponent != null) {
//                             PCAPlotLayout.removeMember(pcaPlotComponent.getPCAComponent());
//                            pcaPlotComponent.remove();
//                        }
                        pcaPlotComponent = new PCAPlotComponent(result, selectionManager, DivaClientService, datasetInfo.getColNumb(), datasetInfo.getDatasetInfo(),medPanelWidth);
                        topMidLayout.add(pcaPlotComponent.getPCAComponent());
                        topMidLayout.setCellHorizontalAlignment(pcaPlotComponent.getPCAComponent(),HorizontalPanel.ALIGN_RIGHT);
//                        pcaPlotComponent.getPCAComponent().setMargin(0);
                        if (!init) {
                            SelectionManager.Busy_Task(false, true);
                        }
                        if (init) {
                          

//                            SelectionManager.Busy_Task(false, true);
                            viewRankTables();
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
    private void viewRankTables() {
//        selectionManager.Busy_Task(true, true);
        DivaClientService.getDefaultRank(new AsyncCallback<RankResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(SERVER_ERROR);
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(RankResult result) {

                        if(rankTables != null){
                        rankLayoutCanv.removeMember(rankTables.getMainRankLayout());
                        rankTables.remove();
                        }
//                        rankLayoutCanv.clear();
                        rankTables = new RankTablesComponent(DivaClientService, selectionManager, result, datasetInfo.getColGroupsList());
                        rankLayoutCanv.addMember(rankTables.getMainRankLayout());
                        if (!init) {
                            SelectionManager.Busy_Task(false, true);
                        }
                        if (init) { 
                            runSomClustering(2, 1, true);
                        }
                    }
                });

    }


    public void updateApp(DatasetInformation datasetInfos) {

        loadingAnalysis(datasetInfos);

    }

    public void updateDatasetDetails(String newName) {
        datasetTitle.setText(newName);
        updateDropDownList(newName);

    }

    public void updateDropDownList(String newName) {
        selectDatasetList.clear();
        selectDatasetList.addItem("Select Dataset");
        selectSubDatasetList.clear();

        selectSubDatasetList.addItem("Select Sub-Dataset");
        selectSubDatasetList.setVisible(false);
        getDatasetsList(newName);//get available dataset names
        SelectionManager.Busy_Task(false, true);
    }
}
