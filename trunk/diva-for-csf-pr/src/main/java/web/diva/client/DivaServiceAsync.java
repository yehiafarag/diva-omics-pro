package web.diva.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import java.util.TreeMap;
import web.diva.shared.beans.DivaGroup;
import web.diva.shared.beans.InteractiveColumnsResults;
import web.diva.shared.beans.ProfilePlotResults;
import web.diva.shared.beans.PCAImageResult;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClustTreeSelectionResult;
import web.diva.shared.beans.SomClusteringResult;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 * The async counterpart of <code>GreetingService</code>.
 *
 * @author Yehia Farag
 *
 */
public interface DivaServiceAsync {

    /**
     * This method is responsible for getting the available dataset ids from
     * server
     *
     * @param datasetResults AsyncCallback list of the available datasets
     *
     */
    public void getAvailableDatasets(AsyncCallback<TreeMap<Integer, String>> datasetResults);

    /**
     * This method is responsible for setting the selected dataset as main
     * dataset on the server side
     *
     * @param datasetId
     * @param asyncCallback AsyncCallbackdataset information that have main
     * information for the dataset and omics tables information
     *
     */
    public void registerMainDataset(int datasetId, AsyncCallback<DatasetInformation> asyncCallback);

    /**
     * This method is responsible for invoking the profile plot visualization
     * method on the server side
     *
     * @param w width of the profile plot panel
     * @param h heights of the profile plot panel
     * @param asyncCallback ProfilePlotResults that have image url in addition
     * to any other information
     *
     */
    public void computeProfilePlot(double w, double h, AsyncCallback<ProfilePlotResults> asyncCallback);

    /**
     * This method is responsible for invoking the clustering analysis and
     * visualization method on the server side
     *
     * @param linkage clustering linkage
     * @param distanceMeasure selected distance
     * @param clusterColumns clustering columns or not
     * @param asyncCallback SomClusteringResult that have all information
     * required for clustering visualization
     *
     */
    public void computeSomClustering(int linkage, int distanceMeasure, boolean clusterColumns, AsyncCallback<SomClusteringResult> asyncCallback);

    /**
     * This method is responsible for invoking the PCA analysis and
     * visualization method on the server side
     *
     * @param comI principal component 1 index
     * @param comII principal component 2 index
     * @param asyncCallback PCAImageResult that have all information required
     * for PCA visualization
     *
     */
    public void computePCA(int comI, int comII, AsyncCallback<PCAImageResult> asyncCallback);

    /**
     * This method is responsible for invoking the ranking analysis method on
     * the server side
     *
     * @param perm perm value in string format
     * @param seed seed value in string format
     * @param colGropNames list of groups name used for ranking comparison
     * @param log2
     * @param defaultRank is the rank is default for the dataset (will be loaded
     * automatically on loading dataset)
     * @param asyncCallback RankResult that have all information required for
     * initializing ranking tables
     *
     */
    public void computeRank(String perm, String seed, List<String> colGropNames, String log2, boolean defaultRank, AsyncCallback<RankResult> asyncCallback);

    /**
     * This method is responsible for loading the default ranking method on
     * initial loading for the dataset in the web page
     *
     * @param asyncCallback RankResult that have all information required for
     * initializing ranking tables
     *
     */
    public void getDefaultRank(AsyncCallback<RankResult> asyncCallback);

    /**
     * This method is responsible for creating row groups
     *
     * @param name group name
     * @param color group string color
     * @param discription group description
     * @param selection group members
     * @param asyncCallback DatasetInformation that have all information
     * required for initializing omics tables
     *
     */
    public void createRowGroup(String name, String color, String discription, int[] selection, AsyncCallback<DatasetInformation> asyncCallback);

    /**
     * This method is responsible for creating column groups
     *
     * @param name group name
     * @param color group string color
     * @param discription group description
     * @param selection group members
     * @param asyncCallback dataset information that have all information
     * required for dataset and omics information table
     *
     */
    public void createColGroup(String name, String color, String discription, int[] selection, AsyncCallback<DatasetInformation> asyncCallback);

    /**
     * This method is responsible for creating sub dataset
     *
     * @param name new sub dataset name
     * @param type from group or from selection
     * @param selection sub-dataset members
     * @param asyncCallback new subdataset name
     */
    public void createSubDataset(String name, String type, int[] selection, AsyncCallback<String> asyncCallback);

    /**
     * This method is responsible for get updated dataset information
     *
     * @param asyncCallback dataset information that have main information for
     * the dataset and omics tables information
     *
     */
    public void updateDatasetInfo(AsyncCallback<DatasetInformation> asyncCallback);

    /**
     * This method is responsible for activate special row groups the only data
     * visualized from active row groups
     *
     * @param rowGroups array of the selected active row groups
     * @param asyncCallback dataset information that have main information for
     * the dataset and omics tables information
     *
     */
    public void activateGroups(String[] rowGroups, AsyncCallback<DatasetInformation> asyncCallback);

    /**
     * This method is responsible for exporting dataset information data in
     * tab-based format based on groups or selected data
     *
     * @param rowGroup group to be exported
     * @param asyncCallback url for generated file
     *
     */
    public void exportData(String rowGroup, AsyncCallback<String> asyncCallback);

    /**
     * This method is responsible for exporting dataset ranking results in
     * tab-based format based on groups or selected data
     *
     * @param indexes indexes of exported data
     * @param asyncCallback url for generated file
     *
     */
    public void exportRankingData(int[] indexes, AsyncCallback<String> asyncCallback);

    /**
     * This method is responsible for exporting profile plot and pca charts as
     * pdf files
     *
     *
     * @param chartType profile plot or pca
     * @param quality heigh or low
     * @param asyncCallback url for generated file
     *
     */
    public void exportImgAsPdf(String chartType, String quality, AsyncCallback<String> asyncCallback);

    /**
     * This method is responsible for exporting clustering component as pdf
     * files
     *
     * @param quality heigh or low
     * @param asyncCallback url for generated file
     *
     */
    public void exportClusteringAsPdf(String quality, AsyncCallback<String> asyncCallback);

    /**
     * This method is responsible for saving the current dataset with any
     * updated row/column groups to share with other users
     *
     * @param newName new dataset name
     * @param asyncCallback final stored name for the new dataset
     *
     */
    public void saveDataset(String newName, AsyncCallback<String> asyncCallback);

   

    /**
     * This method is responsible for updating profilePlot selection updated
     * row/column groups to share with other users
     *
     * @param selection selected member indexes
     * @param asyncCallback image url
     *
     */
    public void updateProfilePlotSelection(int[] selection, AsyncCallback<String> asyncCallback);

    /**
     * This method is responsible for updating PCA selection updated row/column
     * groups to share with other users
     *
     * @param selection selected member indexes
     * @param asyncCallback image url
     *
     */
    public void updatePCASelection(int[] selection, AsyncCallback<String> asyncCallback);

    /**
     * This method is responsible for converting PCA user selection coordinates
     * into indexes
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param asyncCallback indexes of the selected data
     *
     */
    public void getPCASelection(int startX, int startY, int endX, int endY, AsyncCallback<int[]> asyncCallback);

    /**
     * This method is responsible for converting profilePlot user selection
     * coordinates into indexes
     *
     * @param startX
     * @param startY
     * @param asyncCallback indexes of the selected data
     *
     */
    public void getProfilePlotSelection(int startX, int startY, AsyncCallback<int[]> asyncCallback);

  
    /**
     * This method is responsible for updating the side tree of the clustering
     * and return selected indexes and updated image url
     *
     * @param x
     * @param y
     * @param w width
     * @param h height
     * @param asyncCallback SomClustTreeSelectionResult (indexes of the selected
     * data and image url)
     *
     */
    public void updateSideTree(int x, int y, double w, double h, AsyncCallback<SomClustTreeSelectionResult> asyncCallback);

    /**
     * This method is responsible for updating the top tree of the clustering
     * and return selected indexes and updated image url
     *
     * @param x
     * @param y
     * @param w width
     * @param h height
     * @param asyncCallback SomClustTreeSelectionResult (indexes of the selected
     * data and image url)
     *
     */
    public void updateUpperTree(int x, int y, double w, double h, AsyncCallback<SomClustTreeSelectionResult> asyncCallback);

    /**
     * This method is responsible for updating the interactive column of the
     * clustering and return selected indexes and updated image url
     *
     * @param selection selected members
     * @param asyncCallback InteractiveColumnsResults (image url)
     *
     */
    public void updateSomClustInteractiveColumn(int[] selection, AsyncCallback<InteractiveColumnsResults> asyncCallback);

    /**
     * This method is responsible for updating thePCA chart by showing all
     * selected and unselected members
     *
     * @param showAll show all members or selected only
     * @param selection selected members
     * @param asyncCallback string (image url)
     *
     */
    public void pcaShowAll(boolean showAll, int[] selection, AsyncCallback<String> asyncCallback);

    /**
     * This method is responsible for updating thePCA chart by zooming in
     * selection rectangle
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param asyncCallback PCAImageResult (image url and updated tooltips)
     *
     */
    public void pcaZoomIn(int startX, int startY, int endX, int endY, AsyncCallback<PCAImageResult> asyncCallback);

    /**
     * This method is responsible for updating thePCA chart by reset the plot to
     * default
     *
     * @param asyncCallback PCAImageResult (image url and updated tooltips)
     */
    public void pcaZoomReset(AsyncCallback<PCAImageResult> asyncCallback);
    
     /**
     * This method is responsible for getting the column groups required by
     * ranking panel
     *
     * @param asyncCallback List DivaGroup
     */
    public void getColGroups(AsyncCallback<List<DivaGroup>> asyncCallback);
    
//      public void indexToRank(int[] indexes, int type, AsyncCallback<int[]> asyncCallback);


}
