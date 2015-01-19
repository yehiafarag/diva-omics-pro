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
public class DivaMain implements EntryPoint, ChangeHandler {

    private final SelectionManager SelectionManager = new SelectionManager();
    private final String SERVER_ERROR = "An error occurred while attempting to contact the server";
    private VerticalPanel profilePlotLayout, PCAPlotLayout;
    private RankTablesComponent rankTables;
    private final TreeMap datasetsNames = new TreeMap();
    private LeftPanelView leftPanelView;
    private final DivaServiceAsync DivaClientService = GWT.create(DivaService.class);
    private ListBox selectDatasetList;
    private UpdatedPCAPlot pcaPlotComponent;
    private DatasetInformation datasetInfo;
    private Label datasetTitle;
//    private int modulesPanelHeight;
//    private int clusteringPanelWidth;

    @Override
    public void onModuleLoad() {
        SelectionManager.busyTask(true, true);
        SelectionManager.setMainAppController(this);
        datasetTitle = new Label();
        RootPanel.get("dataset_main_title").add(datasetTitle);
        this.initApplication();
    }

    /**
     * This method is responsible for initializing the main DIVA layout
     *
     */
    private void initApplication() {
        selectDatasetList = new ListBox();
        selectDatasetList.setWidth("300px");
        selectDatasetList.addItem("Select Dataset");
        getDatasetsList();//get available dataset names
        RootPanel.get("dropdown_select").add(selectDatasetList);
        selectDatasetList.addChangeHandler(this);
        initMiddleBodyLayout();
    }

    /**
     * on select dataset
     *
        selectDatasetList.addChangeHandler(this);
        initMiddleBodyLayout();
    }

     * @param event user select dataset
     *
     */
    @Override
    public void onChange(ChangeEvent event) {
        if (selectDatasetList.getSelectedIndex() > 0) {
            try {
                int datasetId = (Integer) datasetsNames.get(selectDatasetList.getItemText(selectDatasetList.getSelectedIndex()));
                SelectionManager.resetSelection();
                datasetTitle.setText(selectDatasetList.getItemText(selectDatasetList.getSelectedIndex()));
                loadDataset(datasetId);
                selectDatasetList.setItemSelected(0, true);

            } catch (Exception e) {
                Window.alert("exp " + e.getMessage());
            }
        }
    }

    public void changeDSSelection(int datasetId) {
        SelectionManager.resetSelection();
        datasetsNames.clear();
        selectDatasetList.clear();
        initApplication();
        selectDatasetList.setSelectedIndex(datasetId);
        selectDatasetList.setItemSelected(datasetId, true);
        selectDatasetList.setValue(datasetId, selectDatasetList.getItemText(datasetId));

    }
    private final int userTabId = Random.nextInt(1000000001);

    /**
     * This method is responsible for initializing dataset drop-down list
     */
    private void getDatasetsList() {
        DivaClientService.getAvailableDatasets(userTabId, new AsyncCallback<TreeMap<Integer, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(SERVER_ERROR);
                SelectionManager.busyTask(false, true);
            }

            @Override
            public void onSuccess(TreeMap results) {
                for (Object o : results.keySet()) {
                    int key = (Integer) o;
                    selectDatasetList.addItem((String) results.get(key));
                    datasetsNames.put(results.get(key), key);
                    SelectionManager.busyTask(false, true);
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
        SelectionManager.busyTask(true, true);
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
            Selection s = SelectionManager.getSelectedRows();
            SelectionManager.setSelectedRows(s);
            return;
        }
        processProfilePlot();
        reload = true;

        if (!init) {
            SelectionManager.busyTask(false, true);
        }

    }

    private void updateLeftPanel(DatasetInformation datasetInfos) {

        this.datasetInfo = datasetInfos;
        if (leftPanelView != null) {
            leftPanelView.deparent();
        }
        leftPanelView = new LeftPanelView(SelectionManager, DivaClientService, datasetInfos);
        

    
        
        leftPanelView.setStyleName("whitelayout");
        RootPanel.get("leftpanel").clear(true);
        RootPanel.get("leftpanel").add(leftPanelView);
        }


    private VerticalPanel rankLayout;

    private void initMiddleBodyLayout() {
        VerticalPanel midPanelLayout = new VerticalPanel();
        midPanelLayout.setSpacing(1);
        RootPanel.get("diva_mid_panel").clear();
        RootPanel.get("diva_mid_panel").add(midPanelLayout);
        midPanelLayout.setStyleName("diva_mid_panel_border");

        //pca and profile plot layout
        HorizontalPanel topMedLayout = new HorizontalPanel();
        rankLayout = new VerticalPanel();
        midPanelLayout.add(topMedLayout);
        midPanelLayout.add(rankLayout);

        profilePlotLayout = new VerticalPanel();

        PCAPlotLayout = new VerticalPanel();
        topMedLayout.add(profilePlotLayout);
        topMedLayout.add(PCAPlotLayout);
        topMedLayout.setCellHorizontalAlignment(profilePlotLayout, HorizontalPanel.ALIGN_LEFT);
        topMedLayout.setCellHorizontalAlignment(PCAPlotLayout, HorizontalPanel.ALIGN_RIGHT);

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
        SelectionManager.busyTask(true, true);
        DivaClientService.computeSomClustering(linkage, distanceMeasure, clusterColumns,
                new AsyncCallback<SomClusteringResult>() {
                    @Override
                    public void onFailure(Throwable caught) {

                        Window.alert(SERVER_ERROR);
                        SelectionManager.busyTask(false, true);
                    }

                    @Override
                    public void onSuccess(SomClusteringResult result) {
                        UpdatedSomClustView hierarchicalClustering = new UpdatedSomClustView(result, SelectionManager, DivaClientService,true);

                        RootPanel.get("SomClusteringResults").clear();
                        
                        RootPanel.get("SomClusteringResults").add(hierarchicalClustering.getSomclusteringLayout());
                        SelectionManager.busyTask(false, true);
                        init = false;
                    }
                });

    }

    public void updateClusteringPanel(SomClusteringResult result,boolean clusterColumn) {
        SelectionManager.busyTask(true, true);
        UpdatedSomClustView hierarchicalClustering = new UpdatedSomClustView(result, SelectionManager, DivaClientService, clusterColumn);

        RootPanel.get("SomClusteringResults").clear();

        RootPanel.get("SomClusteringResults").add(hierarchicalClustering.getSomclusteringLayout());
        SelectionManager.busyTask(false, true);

    }

    private ProfilePlotComponent profilePlotComponent;

    /**
     * This method is responsible for invoking line chart method
     *
     * @param datasetId - datasetId
     *
     */
    private void processProfilePlot() {
        SelectionManager.busyTask(true, true);
        DivaClientService.computeProfilePlot(212, 250,
                new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(SERVER_ERROR);
//                        init=false;
                        SelectionManager.busyTask(false, true);
                    }

                    @Override
                    public void onSuccess(String result) {
                        if (profilePlotComponent != null) {
                            profilePlotComponent.remove();
                        }

                        profilePlotComponent = new ProfilePlotComponent(result, SelectionManager, DivaClientService);

                        profilePlotLayout.clear();
                        profilePlotLayout.add(profilePlotComponent.getLayout().asWidget());
                        profilePlotComponent.getLayout().setMargin(2);
                        if (!init) {
                            SelectionManager.busyTask(false, true);
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

        SelectionManager.busyTask(true, true);
        DivaClientService.computePCA(pcaI, pcaII,
                new AsyncCallback<PCAImageResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(SERVER_ERROR);
                        SelectionManager.busyTask(false, true);
                    }

                    @Override
                    public void onSuccess(PCAImageResult result) {
                        if (pcaPlotComponent != null) {
                            pcaPlotComponent.remove();
                        }
                        pcaPlotComponent = new UpdatedPCAPlot(result, SelectionManager, DivaClientService, datasetInfo.getColNumb(), datasetInfo.getDatasetInfo());

                        PCAPlotLayout.clear();
                        PCAPlotLayout.add(pcaPlotComponent.getPCAComponent());
                        pcaPlotComponent.getPCAComponent().setMargin(2);
                        if (!init) {
                            SelectionManager.busyTask(false, true);
                        }
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
    private void viewRankTables(String perm, String seed, List<String> colGropNames, String log2) {
        SelectionManager.busyTask(true, true);
        DivaClientService.computeRank(perm, seed, colGropNames, log2,
                new AsyncCallback<RankResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(SERVER_ERROR);
                        SelectionManager.busyTask(false, true);
                    }

                    @Override
                    public void onSuccess(RankResult result) {

                        rankLayout.clear();
                        rankTables = new RankTablesComponent(DivaClientService, SelectionManager, result, datasetInfo.getColGroupsList());
                        rankLayout.add(rankTables.getMainRankLayout());
                        if (!init) {
                            SelectionManager.busyTask(false, true);
                        }
                        if (init) {
                            runSomClustering(2, 1, true);
                        }
                    }
                });

    }

    private int layoutHeight;

    public void updateApp(DatasetInformation datasetInfos) {

        loadingAnalysis(datasetInfos);

    }
}
