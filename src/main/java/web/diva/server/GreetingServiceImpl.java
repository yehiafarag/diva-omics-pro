package web.diva.server;

import com.google.gwt.user.client.rpc.SerializationException;
import web.diva.client.DivaService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import web.diva.server.model.Computing;
import web.diva.shared.beans.RankResult;
import web.diva.shared.model.core.model.dataset.DatasetInformation;
import web.diva.server.model.beans.DivaDataset;
import web.diva.shared.beans.DivaGroup;
import web.diva.shared.beans.PCAImageResult;
import web.diva.shared.beans.SomClustTreeSelectionUpdate;
import web.diva.shared.beans.SomClusteringResult;
//import web.diva.shared.unused.SomClusteringResult;

/**
 * The server side implementation of the RPC service.
 * main GWT Servlet
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements DivaService {
    private HashMap<String,Computing> usersMap = new HashMap<String, Computing>();

    @Override
    public String processCall(String payload) throws SerializationException {
        System.out.println("process a call"+payload);
        return super.processCall(payload); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL, String strongName) {
         
         HttpSession session = request.getSession(true);
         System.out.println("start session  "+session.getId()+"  is new  "+session.isNew());
        return super.doGetSerializationPolicy(request, moduleBaseURL, strongName); //To change body of generated methods, choose Tools | Templates.
    }
    

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("do get is called");
        super.doGet(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    @Override
    public String pcaShowAll(boolean showAll,int[] selection) {
    return compute.pcaShowAll(showAll,selection);
    }

    @Override
    public PCAImageResult pcaZoomIn(int startX, int startY, int endX, int endY) {
        return compute.pcaZoomIn(startX, startY, endX, endY);
    }
    @Override
    public PCAImageResult pcaZoomReset() {
        return compute.pcaZoomReset();
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
//    private PCAImageResult pcaImgResults;
    private DivaDataset divaDataset;
//    private DatasetInformation datasetInfo;
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
    
    private  int userTabId = -1;

    @Override
    public TreeMap<Integer, String> getAvailableDatasets(int userTabId) {
        if (initSession) {
            HttpSession httpSession = getThreadLocalRequest().getSession();
            compute = (Computing) httpSession.getAttribute("computing");
            initSession = false;
            path = compute.getPath();
        }
//        if (this.userTabId == -1) {
//            this.userTabId = userTabId;
//        }
//
//        if (getThreadLocalRequest().getRequestedSessionId() == null) //same user another browser
//        {
//            return null;
//        } else if (this.userTabId != userTabId) //same user another tab in the same browser
//        {
//            compute = null;
//            getThreadLocalRequest().getSession().invalidate();
//            getThreadLocalRequest().getSession(true);
//            compute = new Computing(path);
//            
//            
//
//        }

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
    public SomClusteringResult computeSomClustering(int linkage, int distanceMeasure,boolean clusterColumns) throws IllegalArgumentException {
  return compute.computeSomClustering(linkage, distanceMeasure,clusterColumns);
        
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
    public DatasetInformation activateGroups(String[] rowGroups) {
     return compute.activateGroups(rowGroups);//datasetInfo;
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
    public String updatePCASelection(int[] selection) {
        return compute.updatePCASelection(selection);
    }
    private RankResult rankResults;

    @Override
    public RankResult computeRank(String perm, String seed, List<String> colGropNames, String log2) {  
            return compute.computeRank(perm, seed, colGropNames, log2);
    }

    @Override
    public DatasetInformation createRowGroup(String name, String color, String type, int[] selection) {
        
        DatasetInformation datasetinfo = compute.createRowGroup(name, color, type, selection);
        return datasetinfo;
    }

    @Override
    public DatasetInformation createColGroup(String name, String color, String type, int[] selection) {
       
       return compute.createColGroup(name, color, type, selection);      
        
    }

    @Override
    public Integer createSubDataset(String name,String type, int[] selection) {      
        int id = compute.createSubDataset(name,type, selection);
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

        int id = compute.saveDataset(newName);
        return id;
    }

    @Override
    public LinkedHashMap<String, String>[] getGroupsPanelData() {
        LinkedHashMap<String, String>[] activeGroupsData =  new LinkedHashMap[2];
        activeGroupsData[0] = compute.getRowGroupsPanelData();
         activeGroupsData[1] = compute.getColGroupsPanelData();  
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
        return compute.exportDataTotext(rowGroup, appPath, url.substring(0, (url.length() - 10)), textFile);
    }

    @Override
    public List<DivaGroup> getColGroups() {
        return compute.getColGroups();
    }

    
}
