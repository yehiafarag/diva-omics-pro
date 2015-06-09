package web.diva.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
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
import web.diva.client.view.core.ServerConnError;
import web.diva.shared.beans.ProfilePlotResults;
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

    private SelectionManager Selection_Manager;
    
    private RankTablesComponent rankTables;
    private final TreeMap datasetsNames = new TreeMap();
    private LeftPanelView leftPanelView;
    private final DivaServiceAsync DivaClientService = GWT.create(DivaService.class);
    private ListBox selectDatasetList, tempSelectDatasetList;

    private ListBox selectSubDatasetList;
    private PCAPlotComponent pcaPlotComponent;
    private DatasetInformation datasetInfo;
    private Label datasetTitle;
    private final HorizontalPanel welcomePage = new HorizontalPanel();
    private boolean oldIE = false;

    private int masterWidth;
    private int masterHeight;

    private int leftPanelWidth, leftPanelHeight, middlePanelWidth, rightPanelWidth;
    private boolean init = true;
    private boolean reload = false;

    private VLayout rankLayoutCanv;
    private VerticalPanel midPanelLayoutCanv;
    private HorizontalPanel topMidLayout;
    private SomClustComponent hierarchicalClustering;
    private ProfilePlotComponent profilePlotComponent;
    private RankResult rankResults;

    @Override
    public void onModuleLoad() {
        if (Window.Navigator.getUserAgent().contains("MSIE")) {
            oldIE = true;
        }

        masterWidth = Page.getScreenWidth();
        masterHeight = Page.getScreenHeight();
        initLayoutDimensions();
        RootPanel.get().setHeight(700 + "px");
        Selection_Manager = new SelectionManager();
        SelectionManager.Busy_Task(true, true);
        Selection_Manager.setMainAppController(this);
        datasetTitle = new Label();
        RootPanel.get("dataset_main_title").add(datasetTitle);
        this.initMainComponentsLayout();
    }

    /**
     * This method is responsible for initializing the the main layout
     * dimensions left, middle and right panels the panel size depends on the
     * browser width and height
     *
     */
    @SuppressWarnings("UnnecessaryBoxing")
    private void initLayoutDimensions() {
        leftPanelWidth = Page.getScreenWidth() * 20 / 100;
        leftPanelHeight = Page.getScreenHeight() - 225;
        middlePanelWidth = Page.getScreenWidth() / 2;
        if (Double.valueOf(middlePanelWidth) % 2.0 > 0.0) {
            middlePanelWidth = middlePanelWidth - 1;
        }
        rightPanelWidth = Page.getScreenWidth() - (leftPanelWidth + 2 + middlePanelWidth + 2);
    }

    /**
     * This method is responsible for initializing the main DIVA layout
     * components initialize dataset drop down list
     *
     */
    private void initMainComponentsLayout() {
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
                            if (value) {
                                Window.Location.reload();
                            }
                        }
                    };
                    SC.confirm("You have changed the screen size the application need to reload the page press OK  to proceed or close and back to the old screen", ok);
                }
            }
        });

        initHomePage();
        welcomePage.setStyleName("welcomepagelayout");
        RootPanel.get("welcomediva").add(welcomePage);
        if (!oldIE) {
            initMiddleBodyLayout();
        }
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
                RootPanel.get().setHeight((Page.getScreenHeight() - 145) + "px");
                RootPanel.get().setWidth((Page.getScreenWidth()) + "px");

                RootPanel.get("welcomediva").clear(true);
                selectSubDatasetList.clear();
                selectSubDatasetList.addItem("Select Sub-Dataset");
                selectDatasetList.setVisible(true);
                selectSubDatasetList.setVisible(false);
                int datasetId = (Integer) datasetsNames.get(tempSelectDatasetList.getItemText(tempSelectDatasetList.getSelectedIndex()));
                Selection_Manager.resetSelection();
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
                Selection_Manager.resetSelection();
                datasetTitle.setText(selectDatasetList.getItemText(selectDatasetList.getSelectedIndex()));
                loadDataset(datasetId);
                updateSubDsSelectionList(selectDatasetList.getItemText(selectDatasetList.getSelectedIndex()));
                selectDatasetList.setItemSelected(0, true);

            } catch (Exception e) {
                Window.alert("exp " + e.getMessage());
            }
        } else if (selectSubDatasetList.getSelectedIndex() > 0) {
            int datasetId = (Integer) datasetsNames.get(selectSubDatasetList.getItemText(selectSubDatasetList.getSelectedIndex()));
            Selection_Manager.resetSelection();
            datasetTitle.setText(selectSubDatasetList.getItemText(selectSubDatasetList.getSelectedIndex()));
            loadDataset(datasetId);
            updateSubDsSelectionList(selectSubDatasetList.getItemText(selectSubDatasetList.getSelectedIndex()));
            selectDatasetList.setItemSelected(0, true);

        }

    }

    /**
     * This method is responsible for initializing the sub-dataset drop down
     * select list
     *
     */
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

    /**
     * This method is responsible for initializing dataset drop-down list
     *
     * @param newName - new dataset name - empty string if no new name applied
     *
     */
    private void getDatasetsList(final String newName) {
        DivaClientService.getAvailableDatasets( new AsyncCallback<TreeMap<Integer, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("DiVAFiles folder is not available please contact the system administrator");
                SelectionManager.Busy_Task(false, true);
            }

            @Override
            public void onSuccess(TreeMap results) {
                for (Object o : results.keySet()) {
                    int key = (Integer) o;
                    String str = formatTitle((String) results.get(key));
                    selectDatasetList.addItem(str);
                    datasetsNames.put(str, key);
                    selectDatasetList.setItemSelected(0, true);
                    tempSelectDatasetList.addItem(str);
                    tempSelectDatasetList.setItemSelected(0, true);
                    SelectionManager.Busy_Task(false, true);
                }
                updateSubDsSelectionList(newName);
            }

        });

    }

    /**
     * This method is responsible for loading dataset upon user selection
     *
     * @param datasetId - datasetId
     *
     */
    private void loadDataset(int datasetId) {
        SelectionManager.Busy_Task(true, true);
        DivaClientService.registerMainDataset(datasetId,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Selection_Manager.connError();
                    }

                    @Override
                    public void onSuccess(DatasetInformation datasetInfos) {
                        init = true;
                        reload = false;
                        loadingAnalysis(datasetInfos);
                    }
                });
    }

    /**
     * This method is responsible for invoking different analysis on selection
     * dataset
     *
     * @param datasetInfos dataset information
     *
     */
    private void loadingAnalysis(DatasetInformation datasetInfos) {
        datasetInfo = datasetInfos;
        updateLeftPanel(datasetInfos);
        if (reload) {

            Selection s = Selection_Manager.getSelectedRows();
            Selection_Manager.setSelectedRows(s);
            return;
        }
        processProfilePlot();
        reload = true;

        if (!init) {
            SelectionManager.Busy_Task(false, true);
        }

    }

    /**
     * This method is responsible for initializing the the home page layout
     *
     */
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

        HTML infoLabel = new HTML("<p align=\"justify\" style=\"margin-left:0px;color:#585858;\"><font size=\"2\">Start using DiVA by selecting a dataset</font></p>");
        infoLabel.setWidth("400px");

        HorizontalPanel selectionLayout = new HorizontalPanel();

        if (oldIE) {
            leftSideLayout.add(ieErrorLabel);
        } else {
            leftSideLayout.add(infoLabel);
            leftSideLayout.add(selectionLayout);
            selectionLayout.setWidth("400px");
            selectionLayout.add(tempSelectDatasetList);
        }

        tempSelectDatasetList.getElement().setAttribute("style", "border: 1px solid gray;height: 24px;font-weight: bold;width: 400px;border-radius: 5px;");

        leftSideLayout.setCellVerticalAlignment(tempSelectDatasetList, VerticalPanel.ALIGN_MIDDLE);
        leftSideLayout.setCellHorizontalAlignment(tempSelectDatasetList, VerticalPanel.ALIGN_LEFT);
        
           HTML info2Label = new HTML("<p align=\"justify\" style=\"margin-top:20px;margin-left:0px;color:#585858;line-height: 200%;\"><font size=\"2\">Getting started tutorial available <a target=\"_blank\" href='" +"tutorial/diva_tutorial.pdf" + "'>here</a>. </font><br/><font size=\"2\">More information available <a target=\"_blank\" href='" + "http://diva-omics-demo.googlecode.com/" + "'>here</a>. </font></p>");
        leftSideLayout.add(info2Label);

//        HTML info3Label = new HTML("<p align=\"justify\" style=\"margin-top:5px;margin-left:0px;color:#585858;\"><font size=\"2\">More information available <a target=\"_blank\" href='" + "http://diva-omics-demo.googlecode.com/" + "'>here</a>. </font></p>");
//        leftSideLayout.add(info3Label);

        Image screenImg = new Image("images/divascreen1.png");
        screenImg.getElement().setAttribute("style", "width:640px;");

        welcomePage.add(screenImg);
        welcomePage.setCellVerticalAlignment(screenImg, VerticalPanel.ALIGN_MIDDLE);
        welcomePage.setCellHorizontalAlignment(screenImg, VerticalPanel.ALIGN_CENTER);
        tempSelectDatasetList.setFocus(true);

    }

    /**
     * This method is responsible for initializing the left panel (omics
     * information tables)
     *
     */
    private void updateLeftPanel(DatasetInformation datasetInfos) {

        this.datasetInfo = datasetInfos;
        if (leftPanelView != null) {
            leftPanelView.deparent();
        }
        leftPanelView = new LeftPanelView(Selection_Manager, DivaClientService, datasetInfos, leftPanelWidth, leftPanelHeight);
        leftPanelView.setStyleName("whitelayout");
        RootPanel.get("leftpanel").clear(true);
        RootPanel.get("leftpanel").add(leftPanelView);
        midPanelLayoutCanv.setStyleName("diva_mid_panel_border");
    }

    /**
     * This method is responsible for initializing the the middle layout panels
     *
     */
    private void initMiddleBodyLayout() {
        midPanelLayoutCanv = new VerticalPanel();
        midPanelLayoutCanv.setWidth(middlePanelWidth + "px");
        midPanelLayoutCanv.setHeight((leftPanelHeight - 2) + "px");
        RootPanel.get("diva_mid_panel").clear();
        RootPanel.get("diva_mid_panel").add(midPanelLayoutCanv);
        //pca and profile plot layout
        topMidLayout = new HorizontalPanel();
        int newWidth = (middlePanelWidth / 2);
        int newHeight = newWidth + 22;
        topMidLayout.setHeight(newHeight + "px");
        topMidLayout.setWidth(middlePanelWidth + "px");
        topMidLayout.setStyleName("whitelayout");

        //rank table layout
        rankLayoutCanv = new VLayout();
        rankLayoutCanv.setHeight((leftPanelHeight - (newHeight + 2)) + "px");
        rankLayoutCanv.setWidth(middlePanelWidth + "px");
        midPanelLayoutCanv.add(topMidLayout);
        midPanelLayoutCanv.add(rankLayoutCanv);
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
        DivaClientService.computeSomClustering(linkage, distanceMeasure, clusterColumns,
                new AsyncCallback<SomClusteringResult>() {
                    @Override
                    public void onFailure(Throwable caught) {

                         Selection_Manager.connError();
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(SomClusteringResult result) {
                        hierarchicalClustering = new SomClustComponent(result, Selection_Manager, DivaClientService, true, rightPanelWidth, leftPanelHeight);
                        RootPanel.get("SomClusteringResults").clear();
                        RootPanel.get("SomClusteringResults").add(hierarchicalClustering.getSomclusteringLayout());
                        Timer t = new Timer() {
                            @Override
                            public void run() {
                                hierarchicalClustering.selectRootNode();
                            }
                        };
//                         Schedule the timer to run once in 0.5 seconds.
//                        if (result.getRowNames().length < 5000) {
////                            t.schedule(500);
//                            SelectionManager.Busy_Task(false, true);
//                        } else {
                            SelectionManager.Busy_Task(false, true);
//                        }
                        init = false;
                    }
                });

    }

    /**
     * This method is responsible for updating clustering panel the selection
     * manager invoke this method on updating clustering input parameters by
     * users
     *
     * @param result - SomClusteringResult
     * @param clusterColumn - cluster columns or not
     *
     */
    public void updateClusteringPanel(SomClusteringResult result, boolean clusterColumn) {
        SelectionManager.Busy_Task(true, true);
        hierarchicalClustering = new SomClustComponent(result, Selection_Manager, DivaClientService, clusterColumn, rightPanelWidth, leftPanelHeight - 2);
        RootPanel.get("SomClusteringResults").clear();
        RootPanel.get("SomClusteringResults").add(hierarchicalClustering.getSomclusteringLayout());
        SelectionManager.Busy_Task(false, true);

    }

    /**
     * This method is responsible for invoking Profile Plot visualization
     *
     * @param datasetId - datasetId
     *
     */
    private void processProfilePlot() {
        DivaClientService.computeProfilePlot(900, 900,
                new AsyncCallback<ProfilePlotResults>() {
                    @Override
                    public void onFailure(Throwable caught) {
                         Selection_Manager.connError();
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(ProfilePlotResults result) {
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

                        profilePlotComponent = new ProfilePlotComponent(result.getUrl(), Selection_Manager, DivaClientService, result.getImgHeight(), result.getImgWidth(), middlePanelWidth);
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
        DivaClientService.computePCA(pcaI, pcaII,
                new AsyncCallback<PCAImageResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                         Selection_Manager.connError();
                    }

                    @Override
                    public void onSuccess(PCAImageResult result) {
                        pcaPlotComponent = new PCAPlotComponent(result, Selection_Manager, DivaClientService, datasetInfo.getColNumb(), datasetInfo.getDatasetInfo(), middlePanelWidth);
                        topMidLayout.add(pcaPlotComponent.getPCAComponent());
                        topMidLayout.setCellHorizontalAlignment(pcaPlotComponent.getPCAComponent(), HorizontalPanel.ALIGN_RIGHT);
                        if (!init) {
                            SelectionManager.Busy_Task(false, true);
                        }
                        if (init) {
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
        DivaClientService.getDefaultRank(new AsyncCallback<RankResult>() {
            @Override
            public void onFailure(Throwable caught) {
                 Selection_Manager.connError();
                SelectionManager.Busy_Task(false, true);
            }

            @Override
            public void onSuccess(RankResult result) {
                rankResults = result;
                if (rankTables != null) {
                    rankLayoutCanv.removeMember(rankTables.getMainRankLayout());
                    rankTables.remove();
                }
                rankTables = new RankTablesComponent(DivaClientService, Selection_Manager, rankResults, datasetInfo.getColGroupsList());
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

    /**
     * This method is responsible for re-initializing the application layout on
     * creating groups the selection manager is responsible for calling this
     * method on creating new groups
     *
     * @param datasetInfos updated dataset information
     *
     */
    public void updateApp(DatasetInformation datasetInfos) {
        loadingAnalysis(datasetInfos);
    }

    /**
     * This method is responsible for re-initializing the drop down list and
     * dataset title on new dataset selection or creating sub-dataset or saving
     * current dataset
     *
     * @param newName dataset new name
     *
     */
    public void updateDatasetDetails(String newName) {
        datasetTitle.setText(newName);
        updateDropDownList(newName);
    }

    /**
     * This method is responsible for re-initializing the drop down list
     *
     * @param newName dataset new name (sub-dataset or updated dataset name)
     *
     */
    public void updateDropDownList(String newName) {
        selectDatasetList.clear();
        selectDatasetList.addItem("Select Dataset");
        selectSubDatasetList.clear();

        selectSubDatasetList.addItem("Select Sub-Dataset");
        selectSubDatasetList.setVisible(false);
        getDatasetsList(newName);//get available dataset names
        SelectionManager.Busy_Task(false, true);
    }

    /**
     * This method is responsible for re-format the title
     *
     * @param dataset title
     * @return new formated title
     */
    private String formatTitle(String source) {
        if (source.equalsIgnoreCase("Robles Ms Et Al. Plos Genet. (2014)")) {
            return "Robles MS et al. PloS Genet.(2014)";
        }

        StringBuilder res = new StringBuilder();
        source = source.toLowerCase();
        String[] strArr = source.split(" ");
        for (String str : strArr) {
            char[] stringArray = str.trim().toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            str = new String(stringArray);
            res.append(str).append(" ");
        }
        return res.toString().trim();
    }
}
