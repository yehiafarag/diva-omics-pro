package web.diva.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import web.diva.shared.beans.DivaGroup;
import web.diva.shared.beans.PCAImageResult;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClustTreeSelectionUpdate;
import web.diva.shared.beans.SomClusteringResult;
//import web.diva.shared.unused.SomClusteringResult;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 * The client side stub for the RPC service.
 * @author Yehia Farag
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

    //load dataset information
    TreeMap<Integer, String> getAvailableDatasets(int userBrowserId);

    DatasetInformation setMainDataset(int datasetId);

    String computeProfilePlot(double w, double h);

//    SomClusteringResult computeSomClustering(int datasetId, int linkage, int distanceMeasure) throws IllegalArgumentException;
    SomClusteringResult computeSomClustering(int linkage, int distanceMeasure,boolean clusterColumns) throws IllegalArgumentException;

//    HeatMapImageResult computeHeatmap(int datasetId, ArrayList<String> indexer, ArrayList<String> colIndexer);

    PCAImageResult computePCA(int comI, int comII);

    RankResult computeRank( String perm, String seed, List<String> colGropNames, String log2);

    DatasetInformation createRowGroup(String name, String color, String type, int[] selection);

    DatasetInformation createColGroup(String name, String color, String type, int[] selection);

    Integer createSubDataset(String name,String type, int[] selection);

    DatasetInformation updateDatasetInfo();

    DatasetInformation activateGroups(String[] rowGroups);

    String exportData(String rowGroup);

    Integer saveDataset(String newName);

    LinkedHashMap<String, String>[] getGroupsPanelData();

    LinkedHashMap<String, String> getColNamesMaps();

    String updateLineChartSelection(int[] selection, double w, double h);

    String updatePCASelection( int[] selection);

    int[] getPCASelection(int startX, int startY, int endX, int endY);

    int[] indexToRank(int[] indexes, int type);

    SomClustTreeSelectionUpdate updateSideTree(int x, int y, double w, double h);

    SomClustTreeSelectionUpdate updateUpperTree(int x, int y, double w, double h);

    String pcaShowAll(boolean showAll,int[] selection);

    PCAImageResult pcaZoomIn(int startX, int startY, int endX, int endY);
    PCAImageResult pcaZoomReset();
    List<DivaGroup> getColGroups();


}
