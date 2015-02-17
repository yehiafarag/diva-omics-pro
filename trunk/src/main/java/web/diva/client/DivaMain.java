package web.diva.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.layout.HLayout;
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
import web.diva.client.view.core.HeaderLayout;
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
    private VLayout profilePlotLayout, PCAPlotLayout;
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

    @Override
    public void onModuleLoad() {
        selectionManager = new SelectionManager();
        SelectionManager.Busy_Task(true, true);
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
        VerticalPanel selectDatasetLayout = new VerticalPanel();
        selectDatasetLayout.setWidth("300px");
        selectDatasetLayout.setHeight("40px");
        selectDatasetList = new ListBox();
        selectDatasetList.getElement().setAttribute("style", "border: 1px solid gray;font-weight: bold;border-radius: 5px;");
        selectDatasetList.setWidth("300px");
        selectDatasetList.addItem("Select Dataset");
        selectDatasetLayout.add(selectDatasetList);
        selectDatasetList.setVisible(false);
        
        selectSubDatasetList = new ListBox();
        selectSubDatasetList.getElement().setAttribute("style", "border: 1px solid gray;font-weight: bold;border-radius: 5px;");
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
        
        initHomePage();
        welcomePage.setStyleName("welcomepagelayout");
        RootPanel.get("welcomediva").add(welcomePage);
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
                Window.alert(SERVER_ERROR);
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
        welcomeLabel.setWidth("300px");
        VerticalPanel leftSideLayout = new VerticalPanel();
        welcomePage.add(leftSideLayout);
        welcomePage.setCellVerticalAlignment(leftSideLayout, VerticalPanel.ALIGN_MIDDLE);
        welcomePage.setCellHorizontalAlignment(leftSideLayout, VerticalPanel.ALIGN_RIGHT);
        leftSideLayout.add(welcomeLabel);
        leftSideLayout.setSpacing(10);

        HTML infoLabel = new HTML("<p align=\"justify\" style=\"margin-left:0px;color:#585858;\"><font size=\"2\">start using DiVA by selecting dataset.</font></p>");
        infoLabel.setWidth("300px");
        leftSideLayout.add(infoLabel);

        HorizontalPanel selectionLayout = new HorizontalPanel();
        leftSideLayout.add(selectionLayout);
        selectionLayout.setWidth("100%");
        selectionLayout.add(tempSelectDatasetList);

        tempSelectDatasetList.getElement().setAttribute("style", "border: 1px solid gray;height: 24px;font-weight: bold;border-radius: 5px;");

        leftSideLayout.setCellVerticalAlignment(tempSelectDatasetList, VerticalPanel.ALIGN_MIDDLE);
        leftSideLayout.setCellHorizontalAlignment(tempSelectDatasetList, VerticalPanel.ALIGN_LEFT);

        HTML info2Label = new HTML("<p align=\"justify\" style=\"margin-top:10px;margin-left:0px;color:#585858;\"><font size=\"2\">source of code and setup details and other supplementary information available at <a target=\"_blank\" href='" + "http://diva-omics-pro.googlecode.com/" + "'>Diva omics page</a>. </font></p>");
        leftSideLayout.add(info2Label);

        Image screenImg = new Image("images/divascreen.jpg");
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
        leftPanelView = new LeftPanelView(selectionManager, DivaClientService, datasetInfos);
        leftPanelView.setStyleName("whitelayout");
        RootPanel.get("leftpanel").clear(true);
        RootPanel.get("leftpanel").add(leftPanelView);

        midPanelLayout.setBorder("1px solid #E6E6E6");
        midPanelLayout.setStyleName("diva_mid_panel_border");
    }

    private VLayout rankLayout;
    private VLayout midPanelLayout;

    private void initMiddleBodyLayout() {
        midPanelLayout = new VLayout();
        midPanelLayout.setMargin(2);
        midPanelLayout.setWidth("50%");
        midPanelLayout.setHeight("89%");
        RootPanel.get("diva_mid_panel").clear();
        RootPanel.get("diva_mid_panel").add(midPanelLayout);

        //pca and profile plot layout
        HLayout topMedLayout = new HLayout();
        topMedLayout.setWidth("100%");
        topMedLayout.setHeight("46%");
        topMedLayout.setStyleName("whitelayout");
        rankLayout = new VLayout();
        rankLayout.setWidth("100%");
        rankLayout.setHeight("42%");
        rankLayout.setStyleName("whitelayout");

        midPanelLayout.addMember(topMedLayout);
        midPanelLayout.addMember(rankLayout);

        profilePlotLayout = new VLayout();
        PCAPlotLayout = new VLayout();
        topMedLayout.addMember(profilePlotLayout);
        topMedLayout.addMember(PCAPlotLayout);

        profilePlotLayout.setWidth("100%");
        profilePlotLayout.setHeight("100%");
        profilePlotLayout.setStyleName("whitelayout");

        PCAPlotLayout.setWidth("100%");
        PCAPlotLayout.setHeight("100%");
        PCAPlotLayout.setStyleName("whitelayout");

//        topMedLayout.setCellHorizontalAlignment(profilePlotLayout, HorizontalPanel.ALIGN_LEFT);
//        topMedLayout.setCellHorizontalAlignment(PCAPlotLayout, HorizontalPanel.ALIGN_LEFT);
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
                        SomClustComponent hierarchicalClustering = new SomClustComponent(result, selectionManager, DivaClientService,true);
                        RootPanel.get("SomClusteringResults").clear();                        
                        RootPanel.get("SomClusteringResults").add(hierarchicalClustering.getSomclusteringLayout());
                        SelectionManager.Busy_Task(false, true);
                        init = false;
                    }
                });

    }

    public void updateClusteringPanel(SomClusteringResult result,boolean clusterColumn) {
        SelectionManager.Busy_Task(true, true);
        SomClustComponent hierarchicalClustering = new SomClustComponent(result, selectionManager, DivaClientService, clusterColumn);

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
        DivaClientService.computeProfilePlot(212, 250,
                new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(SERVER_ERROR);
//                        init=false;
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(String result) {
                        if (profilePlotComponent != null) {                           
                            profilePlotLayout.removeMember(profilePlotComponent.getLayout()); 
                            profilePlotComponent.remove();
                        }

                        profilePlotComponent = new ProfilePlotComponent(result, selectionManager, DivaClientService);
//                        profilePlotLayout.clear();                    
                        
                        profilePlotLayout.addMember(profilePlotComponent.getLayout());
//                        profilePlotComponent.getLayout().setMargin(0);
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
                        if (pcaPlotComponent != null) {
                             PCAPlotLayout.removeMember(pcaPlotComponent.getPCAComponent());
                            pcaPlotComponent.remove();
                        }
                        pcaPlotComponent = new PCAPlotComponent(result, selectionManager, DivaClientService, datasetInfo.getColNumb(), datasetInfo.getDatasetInfo());
                        PCAPlotLayout.addMember(pcaPlotComponent.getPCAComponent());
                        pcaPlotComponent.getPCAComponent().setMargin(0);
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
        DivaClientService.getDefaultRank(
                new AsyncCallback<RankResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(SERVER_ERROR);
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(RankResult result) {

                        if(rankTables != null){
                        rankLayout.removeMember(rankTables.getMainRankLayout());
                        rankTables.remove();
                        }
//                        rankLayout.clear();
                        rankTables = new RankTablesComponent(DivaClientService, selectionManager, result, datasetInfo.getColGroupsList());
                        rankLayout.addMember(rankTables.getMainRankLayout());
                        if (!init) {
                            SelectionManager.Busy_Task(false, true);
                        }
                        if (init) {
//                             SelectionManager.Busy_Task(false, true);
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
