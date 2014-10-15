package web.diva.server;

import web.diva.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import javax.servlet.http.HttpSession;
import web.diva.server.model.Computing;
import web.diva.shared.beans.RankResult;
import web.diva.shared.model.core.model.dataset.DatasetInformation;
import web.diva.server.model.beans.DivaDataset;
import web.diva.shared.beans.PCAImageResult;
import web.diva.shared.beans.SomClustTreeSelectionUpdate;
import web.diva.shared.beans.SomClusteringResult;
//import web.diva.shared.unused.SomClusteringResult;

/**
 * The server side implementation of the RPC service.
 * main GWT Servlet
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    @Override
    public String pcaShowAll(boolean showAll,int[] selection) {
    return compute.pcaShowAll(showAll,selection);
    }

    @Override
    public String pcaZoom(boolean zoom, int xStart, int yStart, int yEnd, int xEnd) {
        return compute.pcaZoom(zoom, xStart, yStart, xEnd, yEnd);
    }

 
    @Override
    public int[] indexToRank(int[] indexes, int type) {
        int[] selectedRank = new int[indexes.length];
        if (type == 1) {
            for (int x = 0; x < selectedRank.length; x++) {
                selectedRank[x] = (rankResults.getPosIndexToRank()[indexes[x]] - 1);
            }
        } else if (type == 2) {
            for (int x = 0; x < selectedRank.length; x++) {
                selectedRank[x] = (rankResults.getNegIndexToRank()[indexes[x]] - 1);
            }
        }
        return selectedRank;

    }

    @Override
    public int[] getPCASelection(int startX, int startY, int endX, int endY) {
        return compute.getPCASelection(startX,startY,endX, endY);
    }
//
//    private PCAPoint[] pcaIndexTable;
    private PCAImageResult pcaImgResults;
    private DivaDataset divaDataset;
    private DatasetInformation datasetInfo;
    private String path;

    private final DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    private final Calendar cal = Calendar.getInstance();

    private final String imgColorName = "groupCol" + dateFormat.format(cal.getTime()).replace(":", " ");

//    private final String lineChartImage = "lineChartImg " + dateFormat.format(cal.getTime()).replace(":", " ");

    private final String pcaChartImage = "pcachart " + dateFormat.format(cal.getTime()).replace(":", " ");
    private final String hmImage = "heatMapImg" + dateFormat.format(cal.getTime()).replace(":", " ");

    private final String textFile = "Export Data" + dateFormat.format(cal.getTime()).replace(":", " ");
    private boolean initSession = true;
//    private HashSet<String> computingDataList;

    private  Computing compute ;

    @Override
    public TreeMap<Integer, String> getAvailableDatasets() {

         
        if (initSession) {
             HttpSession httpSession = getThreadLocalRequest().getSession();
        compute =(Computing) httpSession.getAttribute("computing");
        initSession = false;
        }
        TreeMap<Integer, String> datasetsMap = compute.getAvailableDatasetsMap();
        return datasetsMap;
    }

    @Override
    public DatasetInformation setMainDataset(int datasetId) {
        return compute.setMainDataset(datasetId);
    }

    @Override
    public DatasetInformation updateDatasetInfo() {
        return compute.getDatasetInformation();
    }
    @Override
    public SomClusteringResult computeSomClustering(int linkage, int distanceMeasure) throws IllegalArgumentException {
  return compute.computeSomClustering(linkage, distanceMeasure);
        
    }
     @Override
    public SomClustTreeSelectionUpdate updateSideTree(int x, int y, double w, double h) {
        return compute.updateSideTree(x, y, w, h);
    }
     @Override
    public SomClustTreeSelectionUpdate updateUpperTree(int x, int y, double w, double h) {
        return compute.updateUpperTree(x, y, w, h);
    }

    @Override
    public DatasetInformation activateGroups(String[] rowGroups, String[] colGroups) {
     return compute.activateGroups(rowGroups, colGroups);//datasetInfo;
    }

//    @Override
//    public SomClusteringResult computeSomClustering(int datasetId, int linkage, int distanceMeasure) throws IllegalArgumentException {
//               return compute.computeSomClustering(datasetId, linkage, distanceMeasure);
//    }
//
//    @Override
//    public HeatMapImageResult computeHeatmap(int datasetId, ArrayList indexer, ArrayList colIndexer) {
//
//       
//        HeatMapImageResult imge = compute.computeHeatmap(datasetId, indexer, colIndexer, divaDataset, path, hmImage);
//
//        return imge;
//    }

    @Override
    public PCAImageResult computePCA(int comI, int comII) {
       return compute.computePCA(comI, comII);
    }

    @Override
    public PCAImageResult updatePCASelection(int[] subSelectionData, int[] selection, boolean zoom, boolean selectAll, double w, double h) {

        
        pcaImgResults = compute.updatePCASelection(subSelectionData, selection, zoom, selectAll, w, h,path, pcaChartImage);
       
        return pcaImgResults;

    }
    private RankResult rankResults;

    @Override
    public RankResult computeRank(String perm, String seed, String[] colGropNames, String log2) {  
            return compute.computeRank(perm, seed, colGropNames, log2);
    }

    @Override
    public Boolean createRowGroup(String name, String color, String type, int[] selection) {
        
        divaDataset = compute.createRowGroup(name, color, type, selection, divaDataset);
        return true;
    }

    @Override
    public Boolean createColGroup(String name, String color, String type, String[] strSelection) {
       
        divaDataset = compute.createColGroup(name, color, type, strSelection, divaDataset);      
        return true;
    }

    @Override
    public Integer createSubDataset(String name, int[] selection) {
        String newDsName = "SUB - " + divaDataset.getName() + " - " + name + " - " + dateFormat.format(cal.getTime()).replace(":", " ");
        int id = compute.createSubDataset(name, selection, path, divaDataset, newDsName);
        return id;

    }

    @Override
    public String computeProfilePlot(double w, double h) {
      return compute.computeProfilePlot(w, h);
        
    }

    @Override
    public String updateLineChartSelection(int[] selection, double w, double h) {

        String results = compute.updateLineChartSelection(selection);
        return results;
    }

    @Override
    public Integer saveDataset(String newName) {

        newName = (divaDataset.getName() + " - " + newName + " - " + dateFormat.format(cal.getTime())).replace(":", " ");
        int id = compute.saveDataset(newName, divaDataset, path);
        return id;
    }

    @Override
    public LinkedHashMap<String, String>[] getGroupsPanelData() {
        LinkedHashMap<String, String>[] activeGroupsData = compute.getGroupsPanelData();
        return activeGroupsData;
    }

//    @Override
//    public String[] getPcaColNames(int datasetId) {
//        String[] pcaColNames = compute.getPcaColNames(divaDataset);
//        return pcaColNames;
//    }

    @Override
    public LinkedHashMap<String, String> getColNamesMaps() {
        LinkedHashMap<String, String> colNamesMap = compute.getColNamesMaps(divaDataset);
        return colNamesMap;
    }

    @Override
    public String exportData(String rowGroup) {
        String appPath = this.getServletContext().getRealPath("/");
        String url = this.getThreadLocalRequest().getRequestURL().toString();
        return compute.exportDataTotext(divaDataset, rowGroup, appPath, url.substring(0, (url.length() - 10)), textFile);
    }

}
