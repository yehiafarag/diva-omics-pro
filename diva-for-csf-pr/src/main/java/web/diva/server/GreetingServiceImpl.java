package web.diva.server;

import com.google.gwt.user.client.rpc.SerializationException;
import web.diva.client.DivaService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import web.diva.server.model.Computing;
import web.diva.shared.beans.RankResult;
import web.diva.shared.model.core.model.dataset.DatasetInformation;
import web.diva.shared.beans.DivaGroup;
import web.diva.shared.beans.FullDataObject;
import web.diva.shared.beans.InteractiveColumnsResults;
import web.diva.shared.beans.ProfilePlotResults;
import web.diva.shared.beans.PCAImageResult;
import web.diva.shared.beans.SomClustTreeSelectionResult;
import web.diva.shared.beans.SomClusteringResult;

/**
 * The server side implementation of the RPC service.
 * main GWT Servlet
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements DivaService {

    @Override
    public String processCall(String payload) throws SerializationException {
        return super.processCall(payload);
    }

    @Override
    protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL, String strongName) {         
        request.getSession(true);         
        return super.doGetSerializationPolicy(request, moduleBaseURL, strongName); //To change body of generated methods, choose Tools | Templates.
    }   

    @Override
    public String pcaShowAll(boolean showAll,int[] selection) {
    return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).pcaShowAll(showAll,selection);
    }

    @Override
    public PCAImageResult pcaZoomIn(int startX, int startY, int endX, int endY) {
        return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).pcaZoomIn(startX, startY, endX, endY);
    }
    @Override
    public PCAImageResult pcaZoomReset() {
        return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).pcaZoomReset();
    }

 
//    @Override
//    public int[] indexToRank(int[] indexes, int type) {
//        int[] selectedRank = new int[indexes.length];
//        if (type == 1) {
//            for (int x = 0; x < selectedRank.length; x++) {
////                selectedRank[x] = (rankResults.getPosIndexToRank()[indexes[x]] - 1);
//            }
//        } else if (type == 2) {
//            for (int x = 0; x < selectedRank.length; x++) {
////                selectedRank[x] = (rankResults.getNegIndexToRank()[indexes[x]] - 1);
//            }
//        }
//        return selectedRank;
//
//    }

    @Override
    public int[] getPCASelection(int startX, int startY, int endX, int endY) {
        return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).getPCASelection(startX,startY,endX, endY);
    }

    @Override
    public int[] getProfilePlotSelection(int startX, int startY) {
         return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).getProfilePlotSelection(startX,startY);

    }

    @Override
    public TreeMap<Integer, String> getAvailableDatasets() {
        TreeMap<Integer, String> datasetsMap = ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).getAvailableDatasetsMap();
        return datasetsMap;
    }

    @Override
    public DatasetInformation registerMainDataset(int datasetId) {
        
      
        return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).setMainDataset(datasetId);
    }

    @Override
    public DatasetInformation updateDatasetInfo() {
        return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).getDatasetInformation();
    }
    @Override
    public SomClusteringResult computeSomClustering(int linkage, int distanceMeasure,boolean clusterColumns) throws IllegalArgumentException {
  return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).computeSomClustering(linkage, distanceMeasure,clusterColumns);
        
    }
     @Override
    public SomClustTreeSelectionResult updateSideTree(int x, int y, double w, double h) {
        return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).updateSideTree(x, y, w, h);
    }
     @Override
    public SomClustTreeSelectionResult updateUpperTree(int x, int y, double w, double h) {
        return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).updateUpperTree(x, y, w, h);
    }
    @Override
     public InteractiveColumnsResults updateSomClustInteractiveColumn(int[] selection) {
         InteractiveColumnsResults url = ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).updateSomClustInteractiveColumn(selection);
        return url;
    }

    @Override
    public DatasetInformation activateGroups(String[] rowGroups) {
     return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).activateGroups(rowGroups);//datasetInfo;
    }
    @Override
    public PCAImageResult computePCA(int comI, int comII) {
       return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).computePCA(comI, comII);
    }

    @Override
    public String updatePCASelection(int[] selection) {
        return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).updatePCASelection(selection);
    }
//    private RankResult rankResults;

    @Override
    public RankResult computeRank(String perm, String seed, List<String> colGropNames, String log2,boolean defaultRank) {  
            return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).getRankProductResults(perm, seed, colGropNames, log2,defaultRank);
    }
    @Override
    public RankResult  getDefaultRank(){
        return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).getDefaultRank();
    }

    @Override
    public DatasetInformation createRowGroup(String name, String color, String type, int[] selection) {
        
        DatasetInformation datasetinfo = ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).createRowGroup(name, color, type, selection);
        return datasetinfo;
    }

    @Override
    public DatasetInformation createColGroup(String name, String color, String type, int[] selection) {
       
       return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).createColGroup(name, color, type, selection);      
        
    }

    @Override
    public String createSubDataset(String name,String type, int[] selection) {      
        String id = ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).createSubDataset(name,type, selection);
        return id;

    }

    @Override
    public ProfilePlotResults computeProfilePlot(double w, double h) {
      return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).computeProfilePlot(w, h);
        
    }

    @Override
    public String updateProfilePlotSelection(int[] selection) {

       
        String results = ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).updateLineChartSelection(selection); 
        System.out.println("updateing profile plot with "+ selection.length);
        return results;
    }

    @Override
    public String saveDataset(String newName) {

        String id = ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).saveDataset(newName);
        return id;
    }

//    @Override
//    public LinkedHashMap<String, String>[] getGroupsPanelData() {
//        LinkedHashMap<String, String>[] activeGroupsData =  new LinkedHashMap[2];
//        activeGroupsData[0] = ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).getRowGroupsPanelData();
//         activeGroupsData[1] = ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).getColGroupsPanelData();  
//        return activeGroupsData;
//    }



    @Override
    public String exportData(String rowGroup) {
        String appPath = this.getServletContext().getRealPath("/");
        String url = this.getThreadLocalRequest().getRequestURL().toString();
        return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).exportDataTotext(rowGroup, appPath, url.substring(0, (url.length() - 10)));
    }
    
     @Override
    public String exportImgAsPdf(String chartType,String quality) {
        String appPath = this.getServletContext().getRealPath("/");
        String url = this.getThreadLocalRequest().getRequestURL().toString();
        return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).exportImgAsPdf(chartType, appPath, getThreadLocalRequest().getSession().getId(), url.substring(0, (url.length() - 10)), quality);
    }

    @Override
    public List<DivaGroup> getColGroups() {
        return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).getColGroups();
    }

    @Override
    public String exportRankingData(int[] selection) {
        String appPath = this.getServletContext().getRealPath("/");
        String url = this.getThreadLocalRequest().getRequestURL().toString();
    return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).exportRankTableToTextFile(appPath, getThreadLocalRequest().getSession().getId(), url.substring(0, (url.length() - 10)),selection);
        
    }

    @Override
    public String exportClusteringAsPdf(String quality) {
        String appPath = this.getServletContext().getRealPath("/");
        String url = this.getThreadLocalRequest().getRequestURL().toString();
        return ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).exportClusteringAsPdf(appPath, getThreadLocalRequest().getSession().getId(), url.substring(0, (url.length() - 10)), quality);
    
    }

//    @Override
//    public FullDataObject getReloadData() {
//    return  ((Computing) getThreadLocalRequest().getSession().getAttribute("computing")).getReloadData();
//    
//    }

    
    
}
