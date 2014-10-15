/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import web.diva.server.model.pca.PCAImageGenerator;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.pca.computation.PcaCompute;
import no.uib.jexpress_modularized.rank.computation.ComputeRank;
import no.uib.jexpress_modularized.rank.computation.RPResult;
import no.uib.jexpress_modularized.somclust.computation.SOMClustCompute;
import no.uib.jexpress_modularized.somclust.model.ClusterParameters;
import no.uib.jexpress_modularized.somclust.model.ClusterResults;
import org.apache.commons.lang3.ArrayUtils;
import web.diva.server.filesystem.DB;
import web.diva.server.model.beans.DivaDataset;
import web.diva.shared.beans.PCAImageResult;
import web.diva.shared.beans.PCAPoint;
import web.diva.shared.beans.PCAResults;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClustTreeSelectionUpdate;
import web.diva.shared.beans.SomClusteringResult;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 *
 * @author yehia Farag this class represents main diva logic layer
 */
public class Computing implements Serializable{

    private DivaDataset divaDataset;
    private DatasetInformation datasetInfo;
    private final DB database = new DB();
    private final DivaUtil util = new DivaUtil();
    private final GroupColorUtil colGen = new GroupColorUtil();
    private final String path;
    private TreeMap<Integer, String> datasetsMap;
    private HashSet<String> computingDataList;
    private final DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    private final Calendar cal = Calendar.getInstance();
    private final String img_color_name = "groupCol" + dateFormat.format(cal.getTime()).replace(":", " ");

    private final HashMap<String, Color> colorMap = new HashMap<String, Color>();

    private final String text_file_name = "Export Data" + dateFormat.format(cal.getTime()).replace(":", " ");
    private  SomClustImgGenerator somClustImgGenerator ;    
    private PCAPoint[] pcaIndexTable;    
    private PCAResults PCAResult;
    
    private ProfilePlotImgeGenerator profilePlotGenerator;
    private  PCAImageGenerator updatedPCAGenerator;
    public Computing(HttpSession httpSession, String path) {
        if (httpSession != null) {
            httpSession.setAttribute("imgColorName", img_color_name);
            httpSession.setAttribute("textFile", text_file_name);
        }
        database.initDivaDatasets(path);
        this.path = path;
        datasetsMap = getAvailableDatasetsMap();
        computingDataList = getAvailableComputingFileList();

    }

    /**
     * This method is used to get the available datasets from file system layer
     * (to be replaced by DB later)
     *
     * @return datasetsMap
     */
    public final TreeMap<Integer, String> getAvailableDatasetsMap() {
        if (datasetsMap == null) {
            datasetsMap = database.getAvailableDatasets(path);
        }
        return datasetsMap;
    }

    /**
     * This method is used to get the available computing files from file system
     * layer (to be replaced by DB later)
     *
     * @return datasetsMap
     */
    public final HashSet< String> getAvailableComputingFileList() {
        if (computingDataList == null) {
            computingDataList = database.getAvailableComputingFileList(path);
        }
        return computingDataList;
    }

    public DatasetInformation setMainDataset(int datasetId) {

        try {
            divaDataset = database.getDivaDataset(datasetId);
            divaDataset.setGeneColorArr(util.updateGroupsColorArray(divaDataset.getGeneNamesArr(), divaDataset.getRowGroups()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        initOmicsTableData();

        return datasetInfo;
    }

    /**
     * This method is used to update the dataset information on adding groups or
     * loading datasets
     *
     * @param datasetId - dataset id
     * @param divaDataset - update diva dataset information
     * @param path - divaFile folder path
     * @param imgColorName - string color name
     * @return datasetInformation
     */
    private DatasetInformation updateDatasetInfo() {

        String[] geneTableData[] = new String[divaDataset.getRowGroups().size() + 1][divaDataset.getRowIds().length];
        //init gene names with index
        String[] geneNamesArr = divaDataset.getGeneNamesArr();
        geneTableData[0] = divaDataset.getGeneNamesArr();
        divaDataset.setGeneColorArr(util.updateGroupsColorArray(geneNamesArr, divaDataset.getRowGroups()));
        int index = 1;
        String[][] rowGroupsNames = new String[divaDataset.getRowGroups().size() - 1][];

        colorMap.clear();
        colorMap.put("#000000", Color.BLACK);
        colorMap.put("#BDBDBD", Color.LIGHT_GRAY);
        // init groups name and images
        for (int x = 0; x < rowGroupsNames.length; x++) {
            Group g = divaDataset.getRowGroups().get(x + 1);
            String color = colGen.getImageColor(g.getHashColor(), path, img_color_name + g.getName());
            String[] groupFields = new String[]{g.getName(), color};
            rowGroupsNames[x] = groupFields;
            colorMap.put(g.getHashColor(), g.getColor());
        }

        for (Group g : divaDataset.getRowGroups()) {
            String[] col = new String[geneNamesArr.length];
            for (int x = 0; x < geneNamesArr.length; x++) {
                String color = "#FFFFFF";
                if (g.isActive() && g.getGeneList().contains(geneNamesArr[x])) {
                    for (String[] groupField : rowGroupsNames) {
                        if (groupField[0].equalsIgnoreCase(g.getName())) {
                            color = groupField[1];
                        }
                    }
                }
                col[x] = color;
            }
            geneTableData[index] = col;
            index++;

        }
        if (profilePlotGenerator != null) {
//            profilePlotGenerator.updateChartColors(geneNamesArr, colorMap);
        }
        datasetInfo = new DatasetInformation();
        datasetInfo.setId(divaDataset.getId());
        datasetInfo.setRowsNumb(divaDataset.getDataLength());
        datasetInfo.setColNumb(divaDataset.getDataWidth());
        datasetInfo.setRowGroupsNumb(divaDataset.getRowGroups().size() - 1);
        datasetInfo.setColGroupsNumb(divaDataset.getColumnGroups().size() - 1);
        datasetInfo.setDatasetInfo(divaDataset.getInfoHeaders()[0]);
        LinkedHashMap<String, String> colNamesMap = new LinkedHashMap<String, String>();
        for (int x = 0; x < divaDataset.getColumnIds().length; x++) {
            colNamesMap.put("" + x, divaDataset.getColumnIds()[x]);
        }
        datasetInfo.setOmicsTabelData(geneTableData);
        datasetInfo.setRowGroupsNames(rowGroupsNames);
        datasetInfo.setColNamesMap(colNamesMap);
        return datasetInfo;
    }

    private void initOmicsTableData() {
        String[][] omicsTableData = new String[divaDataset.getRowGroups().size() + 1][divaDataset.getRowIds().length];
        //init gene names with index
        String[] geneNamesArr = divaDataset.getGeneNamesArr();
        omicsTableData[0] = divaDataset.getGeneNamesArr();
        int index = 1;
        String[][] rowGroupsNames = new String[divaDataset.getRowGroups().size() - 1][];

        colorMap.clear();
        colorMap.put("#000000", Color.BLACK);
        colorMap.put("#BDBDBD", Color.LIGHT_GRAY);
        // init groups name and images
        for (int x = 0; x < rowGroupsNames.length; x++) {
            Group g = divaDataset.getRowGroups().get(x + 1);
            String color = colGen.getImageColor(g.getHashColor(), path, img_color_name + g.getName());
            String[] groupFields = new String[]{g.getName(), color};
            rowGroupsNames[x] = groupFields;
            colorMap.put(g.getHashColor(), g.getColor());
        }

        for (Group g : divaDataset.getRowGroups()) {
            String[] col = new String[geneNamesArr.length];
            for (int x = 0; x < geneNamesArr.length; x++) {
                String color = "#FFFFFF";
                if (g.isActive() && g.getGeneList().contains(geneNamesArr[x])) {
                    for (String[] groupField : rowGroupsNames) {
                        if (groupField[0].equalsIgnoreCase(g.getName())) {
                            color = groupField[1];
                        }
                    }
                }
                col[x] = color;
            }
            omicsTableData[index] = col;
            index++;

        }
////        if (profilePlotGenerator != null) {
////            profilePlotGenerator.updateChartColors(geneNamesArr, colorMap);
////        }
        datasetInfo = new DatasetInformation();
        datasetInfo.setId(divaDataset.getId());
        datasetInfo.setRowsNumb(divaDataset.getDataLength());
        datasetInfo.setColNumb(divaDataset.getDataWidth());
        datasetInfo.setRowGroupsNumb(divaDataset.getRowGroups().size() - 1);
        datasetInfo.setColGroupsNumb(divaDataset.getColumnGroups().size() - 1);
        datasetInfo.setDatasetInfo(divaDataset.getInfoHeaders()[0]);

        datasetInfo.setPcaColNames(getPcaColNames());
        LinkedHashMap<String, String> colNamesMap = new LinkedHashMap<String, String>();
        for (int x = 0; x < divaDataset.getColumnIds().length; x++) {
            colNamesMap.put("" + x, divaDataset.getColumnIds()[x]);
        }
        datasetInfo.setOmicsTabelData(omicsTableData);
        datasetInfo.setRowGroupsNames(rowGroupsNames);
        datasetInfo.setColNamesMap(colNamesMap);
    }

    public DatasetInformation getDatasetInformation() {
        return datasetInfo;
    }

    public SomClusteringResult computeSomClustering(int linkage, int distanceMeasure) throws IllegalArgumentException {
        String linkageStr = "WPGMA";
        String distanceMeasureStr = "";
        ClusterParameters.LINKAGE link = null;
        if (linkage == 0) {
            linkageStr = "SINGLE";
            link = ClusterParameters.LINKAGE.SINGLE;
        } else if (linkage == 1) {
            linkageStr = "WPGMA";
            link = ClusterParameters.LINKAGE.WPGMA;
        } else if (linkage == 2) {
            linkageStr = "UPGMA";
            link = ClusterParameters.LINKAGE.UPGMA;
        } else if (linkage == 3) {
            linkageStr = "COMPLETE";
            link = ClusterParameters.LINKAGE.COMPLETE;
        }
        System.err.println(distanceMeasure);
        switch (distanceMeasure) {
            case 0:
                distanceMeasureStr = "Squared Euclidean";
                break;
            case 1:
                distanceMeasureStr = "Euclidean";
                break;
            case 2:
                distanceMeasureStr = "Bray Curtis";
                break;
            case 3:
                distanceMeasureStr = "Manhattan";
                break;
            case 4:
                distanceMeasureStr = "Cosine Correlation";
                break;
            case 5:
                distanceMeasureStr = "Pearson Correlation";
                break;

            case 6:
                distanceMeasureStr = "Uncentered Pearson Correlation";
                break;

            case 7:
                distanceMeasureStr = "Euclidean (Nullweighted)";
                break;
            case 8:
                distanceMeasureStr = "Camberra";
                break;
            case 9:
                distanceMeasureStr = "Chebychev";
                break;
            case 10:
                distanceMeasureStr = "Spearman Rank Correlation";
                break;

        }

        ClusterParameters parameter = new ClusterParameters();
        parameter.setDistance(distanceMeasure);
        parameter.setClusterSamples(true);

        parameter.setLink(link);
        SOMClustCompute som = new SOMClustCompute(divaDataset, parameter);
        ClusterResults results = som.runClustering();

        somClustImgGenerator = new SomClustImgGenerator(results.getRowDendrogramRootNode(), results.getColumnDendrogramRootNode());

        String sideTreeBase64 = somClustImgGenerator.generateSideTree(results.getRowDendrogramRootNode());
        String upperTreeBase64 = somClustImgGenerator.generateTopTree(results.getColumnDendrogramRootNode());
        final java.text.NumberFormat numformat;
        numformat = java.text.NumberFormat.getNumberInstance(java.util.Locale.US);
        numformat.setMaximumFractionDigits(3);
        numformat.setMinimumFractionDigits(1);
        double[][] values = new double[divaDataset.getDataLength()][divaDataset.getDataWidth()];
        for (int x = 0; x < divaDataset.getDataLength(); x++) {
            double[] row = divaDataset.getData()[somClustImgGenerator.getSideTree().arrangement[x]];
            double[] arrangedColRow = new double[row.length];
            for (int y = 0; y < row.length; y++) {
                arrangedColRow[y] = Double.valueOf(numformat.format(row[somClustImgGenerator.getUpperTree().arrangement[y]]));
            }
            values[x] = arrangedColRow;
        }

        String[] colNames = new String[somClustImgGenerator.getUpperTree().arrangement.length];

        for (int x = 0; x < somClustImgGenerator.getUpperTree().arrangement.length; x++) {
            colNames[x] = divaDataset.getColumnIds()[somClustImgGenerator.getUpperTree().arrangement[x]];
        }

        String[] rowNames = new String[somClustImgGenerator.getSideTree().arrangement.length];

        for (int x = 0; x < somClustImgGenerator.getSideTree().arrangement.length; x++) {
            rowNames[x] = divaDataset.getRowIds()[somClustImgGenerator.getSideTree().arrangement[x]];
        }

        String heatmapUrl = somClustImgGenerator.generateHeatMap(divaDataset);
        String scaleUrl = somClustImgGenerator.generateScale(divaDataset);
        SomClusteringResult res = new SomClusteringResult();
        res.setSideTreeImgUrl(sideTreeBase64);
        res.setUpperTreeImgUrl(upperTreeBase64);
        res.setHeatMapImgUrl(heatmapUrl);
        res.setScaleImgUrl(scaleUrl);
        res.setDistanceMeasure(distanceMeasureStr);
        res.setLinkage(linkageStr);
        res.setColNode(somClustImgGenerator.getTooltipsUpperNode());
        res.setRowNode(somClustImgGenerator.getTooltipsSideNode());
        res.setColNames(colNames);
        res.setRowNames(rowNames);
        res.setValues(values);
        return res;

    }

    public SomClustTreeSelectionUpdate updateSideTree(int x, int y, double w, double h) {
        return somClustImgGenerator.updateSideTreeSelection(x, y, w, h);
    }

    public SomClustTreeSelectionUpdate updateUpperTree(int x, int y, double w, double h) {
        return somClustImgGenerator.updateUpperTreeSelection(x, y, w, h);
    }

    /**
     * This method is used to compute line chart
     *
     * @param width - width
     * @param height - height
     * @return line chart image results
     */
    public String computeProfilePlot(double width, double height) {

        boolean[] members = new boolean[divaDataset.getDataLength()];
        for (int i = 0; i < members.length; i++) {
            members[i] = true;
        }
        profilePlotGenerator = new ProfilePlotImgeGenerator(divaDataset, members);

        return profilePlotGenerator.toImage();
    }

    /**
     * This method is used to update line chart
     * @param selection - selected omics data indexes
     * @return line chart image results
     */
    public String updateLineChartSelection(int[] selection) {
        if (selection != null) {
            profilePlotGenerator.setDraw(profilePlotGenerator.getDataSelection(selection));
        }
        return profilePlotGenerator.toImage();
    }


    public PCAImageResult computePCA(int comI, int comII) {
        String key = divaDataset.getName() + "_PCA_" + comI + "_" + comII + ".ser";
        if (computingDataList.contains(key)) {
            PCAResult = getPCAResult(key);
            PCAResult.setDatasetId(divaDataset.getId());

        } else {
            PCAResult = computeDivaPCA(comI, comII);
            savePCAResult(key, PCAResult);
            computingDataList.add(key);
        }

        PCAImageResult pcaImgResults = new PCAImageResult();
        pcaImgResults.setDatasetId(divaDataset.getId());

        PcaCompute pcacompute = new PcaCompute(divaDataset);
        updatedPCAGenerator = new PCAImageGenerator(pcacompute.createPCA(), divaDataset, comI, comII);
        //to image
         updatedPCAGenerator.getFramedIndexes(0, 0, 0,0);
      
        pcaImgResults.setImgString(updatedPCAGenerator.toImage());

        return pcaImgResults;
    }
    
    /**
     * This method is responsible for retrieving PCA results
     *
     * @param id - pca results id
     * @return results - pca results result
     */
    public PCAResults getPCAResult(String id) {
        PCAResults results = database.getPCAResult(id);
        return results;
    }

 private final PCAUtil pcaUtil = new PCAUtil();

    /**
     * This method is used to compute pca
     *
     * @param datasetId - datasetId
     * @param comI - first selected principal component index
     * @param comII - second selected principal component index
     * @param divaDataset
     * @return PCA image results
     */
    private PCAResults computeDivaPCA(int comI, int comII) {
        PCAResults PCAResult = pcaUtil.getPCAResults(divaDataset, comI, comII);
        return PCAResult;
    }

    private final PCAGenerator pcaGen = new PCAGenerator();

    /**
     * This method is used to update pca
     *
     * @param subSelectionData - sub-selection indexes
     * @param selection - selected omics data indexes
     * @param selectAll - select all data indexes
     * @param zoom - boolean
     * @param w - width
     * @param h - height
     * @param path - path to divaFiles folder
     * @param pcaChartImage pca image name
     * @return PCA image results
     */
    public PCAImageResult updatePCASelection(int[] subSelectionData, int[] selection, boolean zoom, boolean selectAll, double w, double h, String path, String pcaChartImage) {
        PCAImageResult pcaImgResults = pcaGen.generateChart(path, PCAResult, subSelectionData, selection, zoom, selectAll, pcaChartImage, w, h, divaDataset);
        pcaImgResults.setDatasetId(divaDataset.getId());
        
        if(zoom){
            updatedPCAGenerator.zoom(zoom, selection);
            pcaImgResults.setImgString(updatedPCAGenerator.toImage());

        }
        else{
        
        Object[] obj = pcaUtil.getTooltips(pcaImgResults, PCAResult.getPoints());
        HashMap<String, String> tooltips = (HashMap<String, String>) obj[0];
        pcaIndexTable = (PCAPoint[]) obj[1];
        pcaImgResults.setXyName(tooltips);
        pcaImgResults.setIndexeMap(pcaIndexTable);
       

        updatedPCAGenerator.selectionChanged(selection);
        pcaImgResults.setImgString(updatedPCAGenerator.toImage());
        
        


        }
        
        return pcaImgResults;

    }
    
    public String pcaShowAll(boolean showAll, int[] sel){
      updatedPCAGenerator.setShadowUnselected(showAll);
            if (showAll == false) {
                updatedPCAGenerator.selectionChanged(sel);
            } else {
                updatedPCAGenerator.setNotShaded(null);

            }
      updatedPCAGenerator.forceFullRepaint();
//      System.out.println(showAll);
      updatedPCAGenerator.getFramedIndexes(0, 0, 0,0);
      return updatedPCAGenerator.toImage();
    
    }
     public String pcaZoom(boolean zoom,int xStart,int yStart,int xEnd,int yEnd){
      if(zoom){
          xStart = 222;
          yStart = 62;
          xEnd = 374;
          yEnd = 228;
         updatedPCAGenerator.setZoomPca(zoom);
         try{
         MouseEvent mousePressedEvent = new MouseEvent(updatedPCAGenerator.getPlot(), MouseEvent.MOUSE_PRESSED, 0, xStart, yStart, 1, yEnd, false);
         updatedPCAGenerator.getPlot().mousePressed(mousePressedEvent);
         
          MouseEvent mouseReleasedEvent = new MouseEvent(updatedPCAGenerator.getPlot(), MouseEvent.MOUSE_RELEASED, 0, xEnd, yEnd, 1, yEnd, false);
         updatedPCAGenerator.getPlot().mouseReleased(mouseReleasedEvent);
         }catch(Exception e){e.printStackTrace();}
         
      }else
      {
          updatedPCAGenerator.getPlot().zoomout();
      }
      updatedPCAGenerator.forceFullRepaint();
      return updatedPCAGenerator.toImage();
    
    }

     
     
     
     
     
     
     
     
     
     
     
     
     
     
//     
//      /**
//     * This method is used to load selected tempDivaDataset from the file system
//     *
//     * @param datasetId - dataset id
//     * @return tempDivaDataset
//     */
//    private DivaDataset getDivaDataset(int datasetId) {
//        DivaDataset tempDivaDataset = null;
//        try {
//            tempDivaDataset = database.getDivaDataset(datasetId);
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        return tempDivaDataset;
//
//    }
    
    
    
    
    
    
    

//    /**
//     * This method is responsible for initializing all datasets and convert them
//     * from text files to serialized files this method will be called at the
//     * startup of the application
//     *
//     *
//     */
//    public void initDivaDatasets() {
//        database.initDivaDatasets(path);
//    }

   

    
    
    
    /**
     * This method is used to get selected tempDivaDataset from the file system and
 initialize it
     *
     * @param datasetId - dataset id
     * @return tempDivaDataset
     */
    public DivaDataset getDataset(int datasetId) {
        DivaDataset tempDivaDataset = null;
        try {
            tempDivaDataset = database.getDivaDataset(datasetId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return tempDivaDataset;

    }
    
    
    

  
    
    

    /**
     * This method is used to activate groups
     *
     * @param rowGroups - row groups ids
     * @param colGroups - column groups ids
     * @param divaDataset - update diva dataset information
     * @return tempDivaDataset
     */
    public DivaDataset activateGroups(String[] rowGroups, String[] colGroups, DivaDataset divaDataset) {

        if (rowGroups != null && rowGroups.length > 0) {
            List<no.uib.jexpress_modularized.core.dataset.Group> updatedActiveGroupList = new ArrayList<no.uib.jexpress_modularized.core.dataset.Group>();
            for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getRowGroups()) {
                g.setActive(false);
                updatedActiveGroupList.add(g);
            }
            divaDataset.getRowGroups().clear();
            divaDataset.getRowGroups().addAll(updatedActiveGroupList);
            updatedActiveGroupList.clear();

            for (String str : rowGroups) {
                String gName = str.split(",")[1];
                if (gName.equalsIgnoreCase("ALL")) {
                    updatedActiveGroupList.clear();
                    for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getRowGroups()) {
                        if (g.getName().equalsIgnoreCase("ALL")) {
                            g.setActive(true);
                        } else {
                            g.setActive(false);
                        }
                        updatedActiveGroupList.add(g);
                    }
                    break;

                } else {

                    for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getRowGroups()) {
                        if (g.getName().equalsIgnoreCase(gName)) {
                            g.setActive(true);
                        }
                        updatedActiveGroupList.add(g);
                    }
                }
            }
        }

        if (colGroups != null && colGroups.length > 0) {
            List<no.uib.jexpress_modularized.core.dataset.Group> updatedActiveGroupList = new ArrayList<no.uib.jexpress_modularized.core.dataset.Group>();
            for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getColumnGroups()) {
                g.setActive(false);
                updatedActiveGroupList.add(g);
            }
            divaDataset.getColumnGroups().clear();
            divaDataset.getColumnGroups().addAll(updatedActiveGroupList);
            updatedActiveGroupList.clear();

            for (String str : colGroups) {
                String gName = str.split(",")[1];
                if (gName.equalsIgnoreCase("ALL")) {
                    updatedActiveGroupList.clear();
                    for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getColumnGroups()) {
                        if (g.getName().equalsIgnoreCase("ALL")) {
                            g.setActive(true);
                        } else {
                            g.setActive(false);
                        }
                        updatedActiveGroupList.add(g);
                    }
                    break;

                } else {

                    for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getColumnGroups()) {
                        if (g.getName().equalsIgnoreCase(gName)) {
                            g.setActive(true);
                        }
                        updatedActiveGroupList.add(g);
                    }
                }
            }
            divaDataset.getColumnGroups().clear();
            divaDataset.getColumnGroups().addAll(updatedActiveGroupList);
            updatedActiveGroupList.clear();

        }
        return divaDataset;

    }

    /**
     * This method is used to create row groups
     *
     * @param name - row group name
     * @param color - row group hashed color
     * @param type - group type (row)
     * @param selection - omics data indexes
     * @param divaDataset
     * @return tempDivaDataset
     */
    public DivaDataset createRowGroup(String name, String color, String type, int[] selection, DivaDataset divaDataset) {

        List<Group> updatedActiveGroupList = new ArrayList<Group>();
        for (int x = 0; x < divaDataset.getRowGroups().size(); x++) {
            no.uib.jexpress_modularized.core.dataset.Group g = divaDataset.getRowGroups().get(x);
            if (x == (divaDataset.getRowGroups().size() - 1)) {
                g.setActive(true);
            } else {
                g.setActive(false);
            }
            updatedActiveGroupList.add(g);
        }
        divaDataset.getRowGroups().clear();
        divaDataset.getRowGroups().addAll(updatedActiveGroupList);
        List<Group> updatedDevaActiveGroupList = new ArrayList<Group>();
        for (int x = 0; x < divaDataset.getRowGroups().size(); x++) {
            Group g = divaDataset.getRowGroups().get(x);
            if (x == (divaDataset.getRowGroups().size() - 1)) {
                g.setActive(true);
            } else {
                g.setActive(false);
            }
            updatedDevaActiveGroupList.add(g);
        }
        divaDataset.getRowGroups().clear();
        divaDataset.getRowGroups().addAll(updatedDevaActiveGroupList);
        Color c = null;
        if (color == null || color.equals("")) {
            c = generatRandColor();
        } else {
            c = hex2Rgb(color);
        }
        Selection.TYPE s = Selection.TYPE.OF_ROWS;
        Group jG = new Group(name, c, new Selection(s, selection));
        jG.setActive(true);
        jG.setGeneList(util.initGroupGeneList(divaDataset.getGeneIndexNameMap(), jG.getMembers()));
        divaDataset.addRowGroup(jG);
        return divaDataset;
    }

    /**
     * This method is used to create columns groups
     *
     * @param name - column group name
     * @param color - column group hashed color
     * @param type - group type (columns)
     * @param strSelection - column data ids
     * @param divaDataset
     * @return tempDivaDataset
     */
    public DivaDataset createColGroup( String name, String color, String type, String[] strSelection, DivaDataset divaDataset) {
        if (strSelection == null || strSelection.length == 0) {
            return null;
        }
        int[] selection = new int[strSelection.length];
        for (int x = 0; x < strSelection.length; x++) {
            selection[x] = Integer.valueOf(strSelection[x]);
        }
        Color c = null;
        if (color == null || color.equals("")) {
            c = generatRandColor();
        } else {
            c = hex2Rgb(color);
        }
        Selection.TYPE s = null;
        s = Selection.TYPE.OF_COLUMNS;
        no.uib.jexpress_modularized.core.dataset.Group jG = new no.uib.jexpress_modularized.core.dataset.Group(name, c, new Selection(s, selection));
        jG.setActive(true);
        divaDataset.addColumnGroup(jG);
        return divaDataset;
    }

    /**
     * This method is used to create and store sub-datasets
     *
     * @param name - dataset name
     * @param selection - omics data indexes
     * @param path - path to divaFiles folder
     * @param divaDataset
     * @param newDsName - new sub-dataset name
     * @return new dataset id
     */
    public Integer createSubDataset(String name, int[] selection, String path, DivaDataset divaDataset, String newDsName) {
        TreeMap<Integer, String> datasetsMap = getAvailableDatasetsMap();
        int id = datasetsMap.lastKey() + 1;
        double[][] newdata = new double[selection.length][];
        String[] newRowIds = new String[selection.length];
        boolean[][] newMissingMeasurments = new boolean[selection.length][];
        for (int x = 0; x < selection.length; x++) {
            double[] row = divaDataset.getData()[selection[x]];
            newdata[x] = row;
            newRowIds[x] = divaDataset.getRowIds()[selection[x]];
            boolean[] mm = divaDataset.getMissingMeasurements()[selection[x]];
            newMissingMeasurments[x] = mm;
        }

        DivaDataset newDS = new DivaDataset(newdata, newRowIds, divaDataset.getColumnIds());
        newDS.setColumnIds(divaDataset.getColumnIds());
        newDS.setMissingMeasurements(newMissingMeasurments);
        newDS.addRowAnnotationNameInUse(divaDataset.getInfoHeaders()[0]);
        newDS.setName(newDsName);
        database.setDataset(newDS, id);
        return id;

    }
    /*
     * method to save text dataset 
     *
     ***/

    /**
     * This method is used to store datasets
     *
     * @param datasetId - dataset id
     * @param divaDataset
     */
    public void saveTxtDataset(int datasetId, DivaDataset divaDataset) {
        database.setDataset(divaDataset, datasetId);
    }

    /**
     * This method is used to store updated datasets
     *
     * @param datasetId - dataset id
     * @param newName - new updated dataset name
     * @param divaDataset
     * @param path - path to divaFiles folder
     * @return dataset id
     */
    public Integer saveDataset(String newName, DivaDataset divaDataset, String path) {
        TreeMap<Integer, String> datasetsMap = getAvailableDatasetsMap();
        int id = datasetsMap.lastKey() + 1;
        divaDataset.setName(newName);
        database.setDataset(divaDataset, id);
        return id;
    }

    /**
     * This method is used to get column group panel initialization data
     *
     * @param divaDataset
     * @return column ids map
     */
    public LinkedHashMap<String, String> getColNamesMaps(DivaDataset divaDataset) {
        LinkedHashMap<String, String> colNamesMap = new LinkedHashMap<String, String>();
        for (int x = 0; x < divaDataset.getColumnIds().length; x++) {
            colNamesMap.put("" + x, divaDataset.getColumnIds()[x]);
        }
        return colNamesMap;
    }

    /**
     * This method is used to get pca panel initialization data
     *
     * @return pca panel labels
     */
    public String[] getPcaColNames() {
        String[] pcaColNames = new String[divaDataset.getColumnIds().length];
        for (int x = 0; x < pcaColNames.length; x++) {
            pcaColNames[x] = "Principal Component nr " + x;
        }
        return pcaColNames;
    }

    /**
     * This method is used to get activate group panel initialization data
     *
     * @param divaDataset
     * @return activate group data
     */
    public LinkedHashMap<String, String>[] getGroupsPanelData() {
        LinkedHashMap<String, String>[] activeGroupsData;
        activeGroupsData = new LinkedHashMap[2];

        LinkedHashMap<String, String> rowGroupsNamesMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> colGroupsNamesMap = new LinkedHashMap<String, String>();
        for (int x = 0; x < divaDataset.getRowGroups().size(); x++) {
            rowGroupsNamesMap.put(divaDataset.getRowGroups().get(x).isActive() + "," + divaDataset.getRowGroups().get(x).getName(), divaDataset.getRowGroups().get(x).getName());
        }
        for (int x = 0; x < divaDataset.getColumnGroups().size(); x++) {
            colGroupsNamesMap.put(divaDataset.getColumnGroups().get(x).isActive() + "," + divaDataset.getColumnGroups().get(x).getName(), divaDataset.getColumnGroups().get(x).getName());
        }
        activeGroupsData[0] = rowGroupsNamesMap;
        activeGroupsData[1] = colGroupsNamesMap;

        return activeGroupsData;
    }

    private final Random rand = new Random();

    /**
     * This method is used to generate random colors
     *
     * @return group color
     */
    private Color generatRandColor() {

        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        return randomColor;
    }

    /**
     * This method is used to convert hashed colors into awt colors
     *
     * @param colorStr -hashed color
     * @return group color
     */
    private Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    /**
     * ***** analysis methods ********
     */
//    private final SOMClustUtil somClustUtil = new SOMClustUtil();

//    /**
//     * This method is used to compute clustering analysis
//     *
//     * @param datasetId - datasetId
//     * @param linkage - selected clustering linkage type
//     * @param distanceMeasure - the selected clustering distance measurement for
//     * clustering
//     * @param tempDivaDataset
//     * @return clustering results
//     */
//    public SomClusteringResult computeSomClustering(int datasetId, int linkage, int distanceMeasure, DivaDataset tempDivaDataset) throws IllegalArgumentException {
//        String linkageStr = "WPGMA";
//        if (linkage == 0) {
//            linkageStr = "SINGLE";
//        } else if (linkage == 1) {
//            linkageStr = "WPGMA";
//        } else if (linkage == 2) {
//            linkageStr = "UPGMA";
//        } else if (linkage == 3) {
//            linkageStr = "COMPLETE";
//        }
//
//        SomClusteringResult results = somClustUtil.initHC(tempDivaDataset, distanceMeasure, linkageStr, true, tempDivaDataset.getId());
//
//        results = somClustUtil.initSelectedNodes(results);
//        HashMap<String, String> toolTipsMap = somClustUtil.initToolTips(results.getSideTree(), tempDivaDataset.getGeneIndexNameMap());
//        HashMap<String, String> topToolTipsMap = somClustUtil.initTopToolTips(results.getTopTree());
//        results.setToolTips(toolTipsMap);
//        results.setTopToolTips(topToolTipsMap);
//        results.setColsNames(tempDivaDataset.getColumnIds());
//        results.setGeneNames(tempDivaDataset.getRowIds());
//        System.gc();
//        return results;
//    }

    /**
     * This method is used to compute clustering heat-map
     *
     * @param datasetId - datasetId
     * @param indexers - new rows indexes based on side tree
     * @param colIndexer - new columns indexes based on top tree
     * @param divaDataset
     * @param path - path to divaFiles folder
     * @param hmImage - heat-map image name
     * @return heat-map image results
     */
//    public HeatMapImageResult computeHeatmap(int datasetId, List<String> indexers, List<String> colIndexer, DivaDataset tempDivaDataset, String path, String hmImage) {
//        HeatMapGenerator hmGenerator = new HeatMapGenerator(path, tempDivaDataset, indexers, colIndexer, hmImage);
//        HeatMapImageResult imge = hmGenerator.getHeatMapResults();
//        return imge;
//    }

   
   
    private final RankUtil rankUtil = new RankUtil();

    /**
     * This method is responsible for computing Ranking
     *
     * @param datasetId - datasetId
     * @param perm
     * @param seed
     * @param colGropNames selected ranking columns indexes
     * @param log2
     * @param divaDataset
     * @return rank results
     */
    public RankResult computeRank(int datasetId, String perm, String seed, String[] colGropNames, String log2, DivaDataset divaDataset) {
        String type = "TwoClassUnPaired";
        int iPerm = Integer.valueOf(perm);
        int iSeed = Integer.valueOf(seed);
        boolean log = false;
        if (log2.equalsIgnoreCase("log 2")) {
            log = true;
        }
        int[] col1 = null;
        int[] col2 = null;
        if (colGropNames.length == 1) {
            type = "OneClass";
            for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getColumnGroups()) {
                if (colGropNames[0].split(",")[1].equalsIgnoreCase(g.getName())) {
                    col1 = g.getMembers();
                }
            }
        } else if (colGropNames.length == 2) {
            type = "TwoClassUnPaired";
            for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getColumnGroups()) {
                if (colGropNames[0].split(",")[1].equalsIgnoreCase(g.getName())) {
                    col1 = g.getMembers();
                }
                if (colGropNames[1].split(",")[1].equalsIgnoreCase(g.getName())) {
                    col2 = g.getMembers();
                }
            }
        }
        ComputeRank cr = new ComputeRank(divaDataset);
        ArrayList<RPResult> jResults = cr.createResult(type, iPerm, iSeed, col1, col2, log);
        RankResult rankResults = rankUtil.handelRankTable(jResults);
        rankResults.setDatasetId(datasetId);
        return rankResults;
    }

//    /**
//     * This method is responsible for storing clustering results
//     *
//     * @param id - clustering id
//     * @param results - clustering result to store
//     */
//    public void saveSomClustResult(String id, SomClusteringResult results) {
//        database.saveSomClustResult(id, results);
//    }
//    /**
//     * This method is responsible for retrieving clustering results
//     *
//     * @param id - clustering id
//     * @return results - clustering result
//     */
//    public SomClusteringResult getSomClustResult(String id) {
//        SomClusteringResult results = database.getSomClustResult(id);
//        return results;
//    }
//      public SomClusteringResult computeSomClustering(int datasetId, int linkage, int distanceMeasure) throws IllegalArgumentException {
//        SomClusteringResult results = null;
//        String key = tempDivaDataset.getName() + "_SomClust_" + linkage + "_" + distanceMeasure + ".ser";
//        if (computingDataList.contains(key)) {
//            results = getSomClustResult(key);
//            results.setDatasetId(datasetId);
//        } else {
//            results = Computing.this.computeSomClustering(datasetId, linkage, distanceMeasure, tempDivaDataset);
//            saveSomClustResult(key, results);
//            computingDataList.add(key);
//
//        }
//
//        return results;
//    }
    /**
     * This method is responsible for storing PCA results
     *
     * @param id - clustering results id
     * @param results - pca result to store
     */
    public void savePCAResult(String id, PCAResults results) {
        database.savePCAResult(id, results);
    }

    /**
     * This method is responsible for storing ranking results
     *
     * @param id - ranking results id
     * @param results - ranking result to store
     */
    public void saveRankResult(String id, RankResult results) {
        database.saveRankResult(id, results);
    }

    /**
     * This method is responsible for retrieving rank results
     *
     * @param id - ranking results id
     * @return results - ranking results result
     */
    public RankResult getRankResult(String id) {
        RankResult results = database.getRankResult(id);
        return results;
    }

    public RankResult computeRank(String perm, String seed, String[] colGropNames, String log2) {
        String colGroupName = "";
        RankResult rankResults = null;
        for (String str : colGropNames) {
            colGroupName = colGroupName + str + "_";
        }
        String key = divaDataset.getName() + "_RANK_" + colGroupName + "_" + log2 + ".ser";
        if (computingDataList.contains(key)) {
            rankResults = getRankResult(key);

        } else {
            rankResults = computeRank(divaDataset.getId(), perm, seed, colGropNames, log2, divaDataset);
            saveRankResult(key, rankResults);
            computingDataList.add(key);

        }
        return rankResults;
    }

    /**
     * This method is responsible for exporting data
     *
     * @param divaDataset
     * @param groupName - selected group name
     * @param path - path to divaFiles folder
     * @param url - context url
     * @param textFileName - exported file name
     * @return exported file url
     */
    public String exportDataTotext(DivaDataset divaDataset, String groupName, String path, String url, String textFileName) {
        String gName = groupName.split(",")[1];
        Group g = null;
        for (Group gr : divaDataset.getRowGroups()) {
            if (gr.getName().equalsIgnoreCase(gName)) {
                g = gr;
                break;
            }
        }
        return util.exportDataTotext(divaDataset, g, path, url, textFileName);

    }

    public int[] getPCASelection(int startX, int startY, int endX, int endY) {

        return updatedPCAGenerator.getPCASelection(startX, startY, endX, endY);
//        int maxXM = Math.max(startX, endX);
//        int minXM = Math.min(startX, endX);
//        int maxYM = Math.max(startY, endY);
//        int minYM = Math.min(startY, endY);
//        int plotWidthArea = updatedPCAGenerator.getPlot().Width() - updatedPCAGenerator.getPlot().left - updatedPCAGenerator.getPlot().right;
//        int plotHeightArea = updatedPCAGenerator.getPlot().getHeight() - updatedPCAGenerator.getPlot().top - updatedPCAGenerator.getPlot().bottom;
//        if ((minXM < updatedPCAGenerator.getPlot().left && maxXM < updatedPCAGenerator.getPlot().left) || (minXM > (updatedPCAGenerator.getPlot().left + plotWidthArea))) {
//            return null;
//        }
//        if ((minYM < updatedPCAGenerator.getPlot().top && maxXM < updatedPCAGenerator.getPlot().left) || (minYM > updatedPCAGenerator.getPlot().top + plotHeightArea)) {
//            return null;
//        }
//
//        minXM -= updatedPCAGenerator.getPlot().left;
//        maxXM -= updatedPCAGenerator.getPlot().left;
//        minYM -= updatedPCAGenerator.getPlot().top;
//        maxYM -= updatedPCAGenerator.getPlot().top;
//
//        if ((minXM < 0 && maxXM >= 0)) {
//            minXM = 0;// updatedPCAGenerator.getPlot().left;
//        }
//        if (maxXM > plotWidthArea && minXM >= 0) {
//            maxXM = plotWidthArea;
//        }
//        if ((minYM <= 0 && maxYM > 0))//updatedPCAGenerator.getPlot().top))
//        {
//            minYM = 0;//updatedPCAGenerator.getPlot().top;
//        }
//        if (maxYM > plotHeightArea && minYM >= 0) {
//            maxXM = plotHeightArea;
//        }
//
//        double xDataArea = updatedPCAGenerator.getPlot().xaxis.maximum - updatedPCAGenerator.getPlot().xaxis.minimum;
//        double xUnitPix = xDataArea / (double) plotWidthArea;
//        double modStartX = (minXM * xUnitPix) + updatedPCAGenerator.getPlot().xaxis.minimum;//xstart units from min         
//        double modEndX = (maxXM * xUnitPix) + updatedPCAGenerator.getPlot().xaxis.minimum;
//
//        double yDataArea = updatedPCAGenerator.getPlot().yaxis.maximum - updatedPCAGenerator.getPlot().yaxis.minimum;
//        double yUnitPix = yDataArea / (double) plotHeightArea;
//        double modStartY = updatedPCAGenerator.getPlot().yaxis.maximum - (maxYM * yUnitPix);
//        double modEndY = updatedPCAGenerator.getPlot().yaxis.maximum - (minYM * yUnitPix);
//
//        double[][] data = updatedPCAGenerator.getPoints();
//        HashSet<Integer> selectedPoints = new HashSet<Integer>();
//        for (int x = 0; x < data[0].length; x++) {
//            double pointX = data[0][x];
//            double pointY = data[1][x];
//            if (pointX >= modStartX && pointX <= modEndX && pointY >= modStartY && pointY <= modEndY) {
//                selectedPoints.add(x);
//            }
//
//        }
//
//        if (selectedPoints.size() > 0) {
//
//            Integer[] selectedIndexes = new Integer[selectedPoints.size()];
//            System.arraycopy(selectedPoints.toArray(), 0, selectedIndexes, 0, selectedIndexes.length);
//
//            int[] arr = new int[selectedIndexes.length];
//            arr = ArrayUtils.toPrimitive(selectedIndexes, selectedIndexes.length);
//            return arr;
//        }
//
//        return null;
    }

    public DatasetInformation activateGroups(String[] rowGroups, String[] colGroups) {
        divaDataset = activateGroups(rowGroups, colGroups, divaDataset);
        datasetInfo = updateDatasetInfo();
        return datasetInfo;
    }
}
