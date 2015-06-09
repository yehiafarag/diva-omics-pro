package web.diva.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
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
 * The client side stub for the RPC service.
 *
 * @author Yehia Farag
 */
@RemoteServiceRelativePath("greet")
public interface DivaService extends RemoteService {

    /**
     * This method is responsible for getting the available dataset ids from
     * server
     *
     * @return list of the available datasets
     *
     */
    TreeMap<Integer, String> getAvailableDatasets();

    /**
     * This method is responsible for setting the selected dataset as main
     * dataset on the serverside
     *
     * @param datasetId
     * @return dataset information that have main information for the dataset
     * and omics tables information
     *
     */
    DatasetInformation registerMainDataset(int datasetId);

    /**
     * This method is responsible for invoking the profile plot visualization
     * method on the server side
     *
     * @param w width of the profile plot panel
     * @param h heights of the profile plot panel
     * @return ProfilePlotResults that have image url in addition to any other
     * information
     *
     */
    ProfilePlotResults computeProfilePlot(double w, double h);

    /**
     * This method is responsible for invoking the clustering analysis and
     * visualization method on the server side
     *
     * @param linkage clustering linkage
     * @param distanceMeasure selected distance
     * @param clusterColumns clustering columns or not
     * @return SomClusteringResult that have all information required for
     * clustering visualization
     *
     */
    SomClusteringResult computeSomClustering(int linkage, int distanceMeasure, boolean clusterColumns) throws IllegalArgumentException;

    /**
     * This method is responsible for invoking the PCA analysis and
     * visualization method on the server side
     *
     * @param comI principal component 1 index
     * @param comII principal component 2 index
     * @return PCAImageResult that have all information required for PCA
     * visualization
     *
     */
    PCAImageResult computePCA(int comI, int comII);

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
     * @return RankResult that have all information required for initializing
     * ranking tables
     *
     */
    RankResult computeRank(String perm, String seed, List<String> colGropNames, String log2, boolean defaultRank);

    /**
     * This method is responsible for loading the default ranking method on
     * initial loading for the dataset in the web page
     *
     * @return RankResult that have all information required for initializing
     * ranking tables
     *
     */
    RankResult getDefaultRank();

    /**
     * This method is responsible for creating row groups
     *
     * @param name group name
     * @param color group string color
     * @param discription group description
     * @param selection group members
     * @return DatasetInformation that have all information required for
     * initializing omics tables
     *
     */
    DatasetInformation createRowGroup(String name, String color, String discription, int[] selection);

    /**
     * This method is responsible for creating column groups
     *
     * @param name group name
     * @param color group string color
     * @param discription group type (column by default)
     * @param selection group members
     * @return dataset information that have all information required for
     * dataset and omics information table
     *
     */
    DatasetInformation createColGroup(String name, String color, String discription, int[] selection);

    /**
     * This method is responsible for creating sub dataset
     *
     * @param name new sub dataset name
     * @param type from group or from selection
     * @param selection sub-dataset members
     * @return new subdataset name
     */
    String createSubDataset(String name, String type, int[] selection);

    /**
     * This method is responsible for get updated dataset information
     *
     * @return dataset information that have main information for the dataset
     * and omics tables information
     *
     */
    DatasetInformation updateDatasetInfo();

    /**
     * This method is responsible for activate special row groups the only data
     * visualized from active row groups
     *
     * @param rowGroups array of the selected active row groups
     * @return dataset information that have main information for the dataset
     * and omics tables information
     *
     */
    DatasetInformation activateGroups(String[] rowGroups);

    /**
     * This method is responsible for exporting dataset information data in
     * tab-based format based on groups or selected data
     *
     * @param rowGroup group to be exported
     * @return url for generated file
     *
     */
    String exportData(String rowGroup);

    /**
     * This method is responsible for exporting dataset ranking results in
     * tab-based format based on groups or selected data
     *
     * @param indexes indexes of exported data
     * @return url for generated file
     *
     */
    String exportRankingData(int[] indexes);

    /**
     * This method is responsible for exporting profile plot and pca charts as
     * pdf files
     *
     *
     * @param chartType profile plot or pca
     * @param quality heigh or low
     * @return url for generated file
     *
     */
    String exportImgAsPdf(String chartType, String quality);

    /**
     * This method is responsible for exporting clustering component as pdf
     * files
     *
     * @param quality heigh or low
     * @return url for generated file
     *
     */
    String exportClusteringAsPdf(String quality);

    /**
     * This method is responsible for saving the current dataset with any
     * updated row/column groups to share with other users
     *
     * @param newName new dataset name
     * @return final stored name for the new dataset
     *
     */
    String saveDataset(String newName);

    /**
     * This method is responsible for updating profilePlot selection updated
     * row/column groups to share with other users
     *
     * @param selection selected member indexes
     * @return image url
     *
     */
    String updateProfilePlotSelection(int[] selection);

    /**
     * This method is responsible for updating PCA selection updated row/column
     * groups to share with other users
     *
     * @param selection selected member indexes
     * @return image url
     *
     */
    String updatePCASelection(int[] selection);

    /**
     * This method is responsible for converting PCA user selection coordinates
     * into indexes
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @return indexes of the selected data
     *
     */
    int[] getPCASelection(int startX, int startY, int endX, int endY);

    /**
     * This method is responsible for converting profilePlot user selection
     * coordinates into indexes
     *
     * @param startX
     * @param startY
     * @return indexes of the selected data
     *
     */
    int[] getProfilePlotSelection(int startX, int startY);

    /**
     * This method is responsible for updating the side tree of the clustering
     * and return selected indexes and updated image url
     *
     * @param x
     * @param y
     * @param w width
     * @param h height
     * @return SomClustTreeSelectionResult (indexes of the selected data and
     * image url)
     *
     */
    SomClustTreeSelectionResult updateSideTree(int x, int y, double w, double h);

    /**
     * This method is responsible for updating the top tree of the clustering
     * and return selected indexes and updated image url
     *
     * @param x
     * @param y
     * @param w width
     * @param h height
     * @return SomClustTreeSelectionResult (indexes of the selected data and
     * image url)
     *
     */
    SomClustTreeSelectionResult updateUpperTree(int x, int y, double w, double h);

    /**
     * This method is responsible for updating the interactive column of the
     * clustering and return selected indexes and updated image url
     *
     * @param selection selected members
     * @return InteractiveColumnsResults (image url)
     *
     */
    InteractiveColumnsResults updateSomClustInteractiveColumn(int[] selection);

    /**
     * This method is responsible for updating thePCA chart by showing all
     * selected and unselected members
     *
     * @param showAll show all members or selected only
     * @param selection selected members
     * @return string (image url)
     *
     */
    String pcaShowAll(boolean showAll, int[] selection);

    /**
     * This method is responsible for updating thePCA chart by zooming in
     * selection rectangle
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @return PCAImageResult (image url and updated tooltips)
     *
     */
    PCAImageResult pcaZoomIn(int startX, int startY, int endX, int endY);

    /**
     * This method is responsible for updating thePCA chart by reset the plot to
     * default
     *
     * @return PCAImageResult (image url and updated tooltips)
     */
    PCAImageResult pcaZoomReset();

    /**
     * This method is responsible for getting the column groups required by
     * ranking panel
     *
     * @return List DivaGroup
     */
    List<DivaGroup> getColGroups();

}
