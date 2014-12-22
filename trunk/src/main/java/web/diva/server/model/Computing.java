/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import web.diva.server.model.profileplot.ProfilePlotImgeGenerator;
import web.diva.server.model.pca.PCAImageGenerator;
import java.awt.Color;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.pca.computation.PcaCompute;
import no.uib.jexpress_modularized.rank.computation.ComputeRank;
import no.uib.jexpress_modularized.rank.computation.RPResult;
import no.uib.jexpress_modularized.somclust.computation.SOMClustCompute;
import no.uib.jexpress_modularized.somclust.model.ClusterParameters;
import no.uib.jexpress_modularized.somclust.model.ClusterResults;
import web.diva.server.filesystem.DB;
import web.diva.server.model.beans.DivaDataset;
import web.diva.shared.beans.DivaGroup;
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
public class Computing implements Serializable {

    private DivaDataset divaDataset;

    public String getPath() {
        return path;
    }
    private DatasetInformation datasetInfo;
    private final DB database = new DB();
    private final DivaUtil util = new DivaUtil();
    private final GroupColorUtil colorImageGenerator = new GroupColorUtil();
    private final String path;
    private TreeMap<Integer, String> datasetsMap;
    private HashSet<String> computingDataList;
    private final DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    private final Calendar cal = Calendar.getInstance();
//    private final String img_color_name = "groupCol" + dateFormat.format(cal.getTime()).replace(":", " ");

    private final HashMap<String, Color> colorMap = new HashMap<String, Color>();

//    private final String text_file_name = "Export Data" + dateFormat.format(cal.getTime()).replace(":", " ");
    private SomClustImgGenerator somClustImgGenerator;
    private PCAPoint[] pcaIndexTable;
    private PCAResults PCAResult;

    private ProfilePlotImgeGenerator profilePlotGenerator;
    private PCAImageGenerator mainPCAGenerator;//zoomedPCAGenerator,inusePCAGenerator;

    public Computing(String path) {
//        if (httpSession != null) {
//            httpSession.setAttribute("imgColorName", img_color_name);
//            httpSession.setAttribute("textFile", text_file_name);
//        }
        database.initDivaDatasets(path);
        this.path = path;
        getAvailableDatasetsMap();
        computingDataList = getAvailableComputingFileList();

    }

    /**
     * This method is used to get the available datasets from file system layer
     * (to be replaced by DB later)
     *
     * @return datasetsMap
     */
    public final TreeMap<Integer, String> getAvailableDatasetsMap() {
//        if (datasetsMap == null) {
        datasetsMap = database.getAvailableDatasets(path);
        return datasetsMap;
//        }
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
//            System.out.println("computingDataList "+computingDataList);
        }
        return computingDataList;
    }

    public DatasetInformation setMainDataset(int datasetId) {

        try {
            divaDataset = database.getDivaDataset(datasetId);
//            divaDataset.setGeneColorArr(util.updateGroupsColorArray(divaDataset.getGeneNamesArr(), divaDataset.getRowGroups()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        updateDatasetInfo();
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
    private void updateDatasetInfo() {
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
        datasetInfo.setColNamesMap(colNamesMap);
        datasetInfo.setColGroupsNamesMap(this.getColGroupsPanelData());
        datasetInfo.setColGroupsList(this.initDivaColumnGroups());
        datasetInfo.setRowGroupList(this.initDivaRowGroups());

    }

    private void initOmicsTableData() {
        String[][] omicsTableData = new String[divaDataset.getRowGroups().size()][divaDataset.getRowIds().length];
        System.out.println("row groups number " + divaDataset.getRowGroups().size());
        omicsTableData[0] = divaDataset.getRowIds();
        int index = 1;
        colorMap.put("#000000", Color.BLACK);
        colorMap.put("#BDBDBD", Color.LIGHT_GRAY);
        // init groups name and images
        Map<String, String> groupImgMap = new HashMap<String, String>();//String[]{rowGroup.getName(), color};

        for (int x = 0; x < divaDataset.getRowGroups().size(); x++) {
            Group g = divaDataset.getRowGroups().get(x);
            if (g.getName().equalsIgnoreCase("All")) {
                continue;
            }

            String colorUrl = colorImageGenerator.getImageColor(g.getHashColor());
            groupImgMap.put(g.getName(), colorUrl);
            colorMap.put(g.getHashColor(), g.getColor());
        }

        for (Group rowGroup : divaDataset.getRowGroups()) {

            if (rowGroup.getName().equalsIgnoreCase("All")) {
                continue;
            }
            Set geneList = new HashSet(rowGroup.getGeneList());
            String[] rowGroupColumn = new String[divaDataset.getDataLength()];
            for (int x = 0; x < divaDataset.getDataLength(); x++) {
                String color = "#FFFFFF";
                if (rowGroup.isActive() && geneList.contains(divaDataset.getRowIds()[x])) {
                    rowGroupColumn[x] = groupImgMap.get(rowGroup.getName());
                } else {
                    rowGroupColumn[x] = color;
                }

            }
            omicsTableData[index] = rowGroupColumn;
            index++;

        }
        datasetInfo.setOmicsTabelData(omicsTableData);

    }

    public DatasetInformation getDatasetInformation() {
        return datasetInfo;
    }

    public SomClusteringResult computeSomClustering(int linkage, int distanceMeasure, boolean clusterColumns) throws IllegalArgumentException {

        System.out.println("clust colo is " + clusterColumns);
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

        ClusterResults results = null;
        String key = divaDataset.getName() + "_SomClust_" + linkage + "_" + distanceMeasure + "_" + clusterColumns + ".ser";
        if (computingDataList.contains(key)) {
            results = database.getSomClustResult(key);
        } else {
            ClusterParameters parameter = new ClusterParameters();
            parameter.setDistance(distanceMeasure);
            parameter.setClusterSamples(clusterColumns);

            parameter.setLink(link);
            SOMClustCompute som = new SOMClustCompute(divaDataset, parameter);
            results = som.runClustering();
            database.saveSomClustResult(key, results);
            computingDataList.add(key);

        }

        SomClusteringResult res = new SomClusteringResult();
        String[] colNames = new String[divaDataset.getColumnIds().length];
        int[] colArrangement = new int[divaDataset.getColumnIds().length];
        if (clusterColumns) {
            somClustImgGenerator = new SomClustImgGenerator(results.getRowDendrogramRootNode(), results.getColumnDendrogramRootNode());
            String upperTreeBase64 = somClustImgGenerator.generateTopTree(results.getColumnDendrogramRootNode());
            res.setUpperTreeImgUrl(upperTreeBase64);
            res.setColNode(somClustImgGenerator.getTooltipsUpperNode());

            for (int x = 0; x < somClustImgGenerator.getUpperTree().arrangement.length; x++) {
                colNames[x] = divaDataset.getColumnIds()[somClustImgGenerator.getUpperTree().arrangement[x]];
                colArrangement[x] = somClustImgGenerator.getUpperTree().arrangement[x];
            }
        } else {
            somClustImgGenerator = new SomClustImgGenerator(results.getRowDendrogramRootNode(), null);
            colNames = divaDataset.getColumnIds();
            for (int x = 0; x < divaDataset.getColumnIds().length; x++) {
                colArrangement[x] = x;
            }

        }

        res.setColNames(colNames);
        String sideTreeBase64 = somClustImgGenerator.generateSideTree(results.getRowDendrogramRootNode());
       

        final java.text.NumberFormat numformat;
        numformat = java.text.NumberFormat.getNumberInstance(java.util.Locale.US);
        numformat.setMaximumFractionDigits(3);
        numformat.setMinimumFractionDigits(1);
        double[][] values = new double[divaDataset.getDataLength()][divaDataset.getDataWidth()];
        for (int x = 0; x < divaDataset.getDataLength(); x++) {
            double[] row = divaDataset.getData()[somClustImgGenerator.getSideTree().arrangement[x]];
            double[] arrangedColRow = new double[row.length];
            for (int y = 0; y < row.length; y++) {
                arrangedColRow[y] = Double.valueOf(numformat.format(row[colArrangement[y]]));
            }
            values[x] = arrangedColRow;
        }

        String[] rowNames = new String[somClustImgGenerator.getSideTree().arrangement.length];

        for (int x = 0; x < somClustImgGenerator.getSideTree().arrangement.length; x++) {
            rowNames[x] = divaDataset.getRowIds()[somClustImgGenerator.getSideTree().arrangement[x]];
        }

        String heatmapUrl = somClustImgGenerator.generateHeatMap(divaDataset, clusterColumns);
        String scaleUrl = somClustImgGenerator.generateScale(divaDataset, clusterColumns);

        res.setSideTreeImgUrl(sideTreeBase64);

        res.setHeatMapImgUrl(heatmapUrl);
        res.setScaleImgUrl(scaleUrl);
        res.setDistanceMeasure(distanceMeasureStr);
        res.setLinkage(linkageStr);

        res.setRowNode(somClustImgGenerator.getTooltipsSideNode());

        res.setRowNames(rowNames);
        res.setValues(values); 
        res.setSideTreeHeight(somClustImgGenerator.getLeftTreeHeight());
        res.setSideTreeWidth(somClustImgGenerator.getLeftTreeWidth());
        res.setTopTreeHeight(somClustImgGenerator.getTopTreeHeight());
        res.setTopTreeWidth(somClustImgGenerator.getTopTreeWidth());
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
     *
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
        mainPCAGenerator = new PCAImageGenerator(pcacompute.createPCA(), divaDataset, comI, comII);
        //to image
        pcaImgResults.setTooltipInformatinData(mainPCAGenerator.getTooltipsInformationData());
        pcaImgResults.setImgString(mainPCAGenerator.toImage());
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

    /**
     * This method is used to update pca
     *
     * @param selection selected indexes
     * @return PCA image results
     */
    public String updatePCASelection(int[] selection) {

        mainPCAGenerator.selectionChanged(selection);

        return mainPCAGenerator.toImage();

    }

    public String pcaShowAll(boolean showAll, int[] sel) {
        mainPCAGenerator.setShadowUnselected(showAll);
        if (showAll == false) {
            mainPCAGenerator.selectionChanged(sel);
        } else {
            mainPCAGenerator.setNotShaded(null);

        }
        mainPCAGenerator.forceFullRepaint();

        return mainPCAGenerator.toImage();

    }

    private boolean zoom = false;

    public PCAImageResult pcaZoomIn(int startX, int startY, int endX, int endY) {
//        int[] indexes = null;
//        DivaDataset zoomedDataset = null;

//        if (zoom) {
//            indexes = zoomedPCAGenerator.getPCASelection((startX), (startY), (endX), (endY));
//        } else{
//            indexes = getPCASelection((startX), (startY ), (endX ), (endY ));
//        }
//            if (indexes != null && indexes.length > 2) {
//               zoomedDataset=  zoomDataset(indexes);
//        }
//        if(zoomedDataset !=null){
//            zoom = true;
//        PcaCompute pcacompute = new PcaCompute(zoomedDataset);
//        zoomedPCAGenerator = new PCAImageGenerator(pcacompute.createPCA(), zoomedDataset, mainPCAGenerator.getPcax(),mainPCAGenerator.getPcay());
//        updatedPCAGenerator = new PCAImageGenerator(, zoomDataset(indexes), 0, 1);
//         boolean[] notShad = mainPCAGenerator.getZoomedNotshadIndex();
//         if(notShad != null)
//            zoomedPCAGenerator.setNotShaded(notShad);
//         else
//             zoomedPCAGenerator.selectionChanged(new int[]{});
//            System.err.println("start zooming");
//        zoomedPCAGenerator.forceFullRepaint();
        System.out.println("zoom in is working");
        mainPCAGenerator.setZoom(true, (startX), (startY), (endX), (endY));
        PCAImageResult pcaImgResults = new PCAImageResult();
        pcaImgResults.setDatasetId(divaDataset.getId());
        pcaImgResults.setImgString(mainPCAGenerator.toImage());
        pcaImgResults.setTooltipInformatinData(mainPCAGenerator.getTooltipsInformationData());

//        mainPCAGenerator.
        return pcaImgResults;
//        }
//        return null;
    }

   
    public PCAImageResult pcaZoomReset() {
        zoom = false;

//       int startx,int starty,int endx,int endy
//        updatedPCAGenerator.zoomOut();
        //get selection
        //remap it 
        //generate new generator
//        updatedPCAGenerator = new 
//        updatedPCAGenerator.forceFullRepaint();
        mainPCAGenerator.zoomOut();
        PCAImageResult pcaImgResults = new PCAImageResult();
        pcaImgResults.setDatasetId(divaDataset.getId());
        //set current selection 
        pcaImgResults.setImgString(mainPCAGenerator.toImage());
        pcaImgResults.setTooltipInformatinData(mainPCAGenerator.getTooltipsInformationData());
        return pcaImgResults;

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
     * This method is used to get selected tempDivaDataset from the file system
     * and initialize it
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
     * @return tempDivaDataset
     */
    public DatasetInformation activateGroups(String[] rowGroups) {

        if (rowGroups != null && rowGroups.length > 0) {
            List<no.uib.jexpress_modularized.core.dataset.Group> updatedActiveGroupList = new ArrayList<no.uib.jexpress_modularized.core.dataset.Group>();
            for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getRowGroups()) {

//                if(g.getName().equalsIgnoreCase("All")){
//                g.setColor(Color.LIGHT_GRAY);
//                }else
                g.setActive(false);

                updatedActiveGroupList.add(g);
            }
            divaDataset.getRowGroups().clear();
            divaDataset.getRowGroups().addAll(updatedActiveGroupList);
            updatedActiveGroupList.clear();
            for (Group g : divaDataset.getRowGroups()) {
                for (String gName : rowGroups) {

                    if (gName.equalsIgnoreCase(g.getName())) {
//                        if (g.getName().equalsIgnoreCase("All")) {
//                            g.setColor(Color.BLACK);
//                        } else {
                        g.setActive(true);
//                        }
                        break;
                    }

                }
                updatedActiveGroupList.add(g);

            }
            divaDataset.getRowGroups().clear();
            divaDataset.getRowGroups().addAll(updatedActiveGroupList);
        }
        return getDatasetInformation();

    }

    /**
     * This method is used to create row groups
     *
     * @param name - row group name
     * @param color - row group hashed color
     * @param description - group description (row)
     * @param selection - omics data indexes
     * @return tempDivaDataset
     */
    public DatasetInformation createRowGroup(String name, String color, String description, int[] selection) {

//        System.out.println("Create group "+name+"  "+ color+"  "+description+"  "+selection.length);
//        List<Group> updatedActiveGroupList = new ArrayList<Group>();
//        for (int x = 0; x < divaDataset.getRowGroups().size(); x++) {
//            no.uib.jexpress_modularized.core.dataset.Group rowGroup = divaDataset.getRowGroups().get(x);
//            if (x == (divaDataset.getRowGroups().size() - 1)) {
//                rowGroup.setActive(true);
//            } else {
//                rowGroup.setActive(false);
//            }
//            updatedActiveGroupList.add(rowGroup);
//        }
//        divaDataset.getRowGroups().clear();
//        divaDataset.getRowGroups().addAll(updatedActiveGroupList);
//        List<Group> updatedDevaActiveGroupList = new ArrayList<Group>();
//        for (int x = 0; x < divaDataset.getRowGroups().size(); x++) {
//            Group rowGroup = divaDataset.getRowGroups().get(x);
//            if (x == (divaDataset.getRowGroups().size() - 1)) {
//                rowGroup.setActive(true);
//            } else {
//                rowGroup.setActive(false);
//            }
//            updatedDevaActiveGroupList.add(rowGroup);
//        }
//        divaDataset.getRowGroups().clear();
//        divaDataset.getRowGroups().addAll(updatedDevaActiveGroupList);
        Color c = null;
        if (color == null || color.equals("")) {
            c = generatRandColor();
        } else {
            c = hex2Rgb(color);
        }
        Selection.TYPE s = Selection.TYPE.OF_ROWS;
        Group jG = new Group(name, c, new Selection(s, selection));
        jG.setActive(true);
        jG.setHashColor(color);
        jG.setDescription(description);
        jG.setGeneList(util.initGroupGeneList(divaDataset.getGeneIndexNameMap(), jG.getMembers()));
        Collections.reverse(divaDataset.getRowGroups());
        divaDataset.addRowGroup(jG);
        Collections.reverse(divaDataset.getRowGroups());

//        divaDataset.addRowGroup(jG);
        this.updateDatasetInfo();
        this.initOmicsTableData();
        return this.getDatasetInformation();
    }

    /**
     * This method is used to create columns groups
     *
     * @param name - column group name
     * @param color - column group hashed color
     * @param description - group description (columns)
     * @param selection selection indexes
     * @return tempDivaDataset
     */
    public DatasetInformation createColGroup(String name, String color, String description, int[] selection) {
        Color c = null;
        if (color == null || color.equals("")) {
            c = generatRandColor();
        } else {
            c = hex2Rgb(color);
        }
        Selection.TYPE s = Selection.TYPE.OF_COLUMNS;
        Group jG = new Group(name, c, new Selection(s, selection));
        jG.setActive(true);
        jG.setHashColor(color);
        jG.setDescription(description);
        divaDataset.addColumnGroup(jG);
        this.updateDatasetInfo();
        this.initOmicsTableData();

        return this.getDatasetInformation();
    }

    /**
     * This method is used to create and store sub-datasets
     *
     * @param name - dataset name
     * @param selection - omics data indexes
     * @param type - group name
     *
     * @return new dataset id
     */
    public Integer createSubDataset(String name, String type, int[] selection) {
        getAvailableDatasetsMap();
        int id = datasetsMap.lastKey() + 1;
        String newDsName = name.toUpperCase() + " ( SUB DS - " + divaDataset.getName() + " )";
        if (datasetsMap.containsValue(newDsName)) {
            newDsName = newDsName + " - " + dateFormat.format(cal.getTime()).replace(":", " ") + "";
        }

        if (selection == null) {
            for (Group g : divaDataset.getRowGroups()) {
                if (g.getName().equalsIgnoreCase(type)) {
                    selection = g.getMembers();
                    break;
                }

            }

        }
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

        newDS.setRowIds(newRowIds);
        newDS.getColumnGroups().clear();

        newDS.getColumnGroups().addAll(divaDataset.getColumnGroups());
        newDS.getRowGroups().clear();
        Group g = new Group("all", Color.BLACK, new Selection(Selection.TYPE.OF_ROWS, selection));
        newDS.addRowGroup(g);
        newDS = util.initDivaDs(newDS, id);
        String[] geneNamesArr = util.initGeneNamesArr(newDS.getGeneIndexNameMap());
        newDS.setGeneNamesArr(geneNamesArr);

        for (int dsKey : datasetsMap.keySet()) {
            String dsName = datasetsMap.get(dsKey);
            if (newDsName.equalsIgnoreCase(dsName)) {
                newDS.setId(dsKey);
                break;
            }
        }
        database.setDataset(newDS);

        return newDS.getId();

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
        database.setDataset(divaDataset);
    }

    /**
     * This method is used to store updated datasets
     *
     * @param newName - new updated dataset name
     * @return dataset id
     */
    public Integer saveDataset(String newName) {

        newName = (newName.toUpperCase() + " (" + divaDataset.getName() + " )");
        getAvailableDatasetsMap();
        if (datasetsMap.containsValue(newName)) {
            newName += " - " + dateFormat.format(cal.getTime()).replace(":", " ");
        }

        divaDataset.setName(newName);
        database.setDataset(divaDataset);
        getAvailableDatasetsMap();
        for (int dsKey : datasetsMap.keySet()) {
            String dsName = datasetsMap.get(dsKey);
            if (newName.equalsIgnoreCase(dsName)) {
                return dsKey;
            }
        }
        return 0;
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
            pcaColNames[x] = "Principal Component nr " + (x + 1);
        }
        return pcaColNames;
    }

    /**
     * This method is used to get activate row group panel initialization data
     *
     * @return activate row group data
     */
    public LinkedHashMap<String, String> getRowGroupsPanelData() {

        LinkedHashMap<String, String> rowGroupsNamesMap = new LinkedHashMap<String, String>();
        for (int x = 0; x < divaDataset.getRowGroups().size(); x++) {
            rowGroupsNamesMap.put(divaDataset.getRowGroups().get(x).isActive() + "," + divaDataset.getRowGroups().get(x).getName(), divaDataset.getRowGroups().get(x).getName());
        }

        return rowGroupsNamesMap;
    }

    /**
     * This method is used to get activate column group panel initialization
     * data
     *
     * @return activate column group data
     */
    public LinkedHashMap<String, String> getColGroupsPanelData() {
        LinkedHashMap<String, String> colGroupsNamesMap = new LinkedHashMap<String, String>();
        for (int x = 0; x < divaDataset.getColumnGroups().size(); x++) {
            colGroupsNamesMap.put(divaDataset.getColumnGroups().get(x).isActive() + "," + divaDataset.getColumnGroups().get(x).getName(), divaDataset.getColumnGroups().get(x).getName());
        }
        return colGroupsNamesMap;
    }

    /**
     * This method is used to get activate column group panel initialization
     * data
     *
     * @return activate column group data
     */
    public List<DivaGroup> initDivaColumnGroups() {
        List<DivaGroup> colGroupsList = new ArrayList<DivaGroup>();
        for (int x = 0; x < divaDataset.getColumnGroups().size(); x++) {
            DivaGroup cg = new DivaGroup();
            cg.setName(divaDataset.getColumnGroups().get(x).getName());
            String color = colorImageGenerator.getImageColor(divaDataset.getColumnGroups().get(x).getHashColor());
            cg.setColor(color);
            int count = 0;
            if (divaDataset.getColumnGroups().get(x).getMembers() == null) {
                count = divaDataset.getColumnGroups().size();
            } else {
                count = divaDataset.getColumnGroups().get(x).getMembers().length;
            }
            cg.setCount(count);
            colGroupsList.add(cg);
        }
        return colGroupsList;
    }

    /**
     * This method is used to get activate column group panel initialization
     * data
     *
     * @return activate column group data
     */
    public List<DivaGroup> initDivaRowGroups() {
        Set<String> groups = new HashSet<String>();
        List<DivaGroup> rowGroupsList = new ArrayList<DivaGroup>();
        for (int x = 0; x < divaDataset.getRowGroups().size(); x++) {
            if (groups.contains(divaDataset.getRowGroups().get(x).getName())) {
                continue;
            }
            groups.add(divaDataset.getRowGroups().get(x).getName());
            DivaGroup divaGroup = new DivaGroup();
            divaGroup.setName(divaDataset.getRowGroups().get(x).getName());
            String color = colorImageGenerator.getImageColor(divaDataset.getRowGroups().get(x).getHashColor());
            divaGroup.setColor(color);
            divaGroup.setMembers(divaDataset.getRowGroups().get(x).getMembers());
            divaGroup.setActive(divaDataset.getRowGroups().get(x).isActive());
            int count = 0;
            if (divaDataset.getRowGroups().get(x).getMembers() == null) {
                count = divaDataset.getRowGroups().size();
            } else {
                count = divaDataset.getRowGroups().get(x).getMembers().length;
            }
            divaGroup.setCount(count);
            rowGroupsList.add(divaGroup);
        }
        return rowGroupsList;
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
        if(colorStr.equalsIgnoreCase("red"))
            return Color.red;
        if(!colorStr.contains("#"))
            return  Color.decode(colorStr);
                    else{
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
        }
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
     * @return rank results
     */
    public RankResult computeRank(int datasetId, String perm, String seed, List<String> colGropNames, String log2) {
        String type = "TwoClassUnPaired";
        int iPerm = Integer.valueOf(perm);
        int iSeed = Integer.valueOf(seed);
        boolean log = false;
        if (log2.equalsIgnoreCase("log 2")) {
            log = true;
        }
        int[] col1 = null;
        int[] col2 = null;
        if (colGropNames.size() == 1) {
            type = "OneClass";
            for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getColumnGroups()) {
                if (colGropNames.get(0).equalsIgnoreCase(g.getName())) {
                    col1 = g.getMembers();
                }
            }
        } else if (colGropNames.size() == 2) {
            type = "TwoClassUnPaired";
            for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getColumnGroups()) {
                if (colGropNames.get(0).equalsIgnoreCase(g.getName())) {
                    col1 = g.getMembers();
                }
                if (colGropNames.get(1).equalsIgnoreCase(g.getName())) {
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
//      public SomClusteringResult computeSomClustering(int linkage, int distanceMeasure) throws IllegalArgumentException {
//        SomClusteringResult results = null;
//        String key = divaDataset.getName() + "_SomClust_" + linkage + "_" + distanceMeasure + ".ser";
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

    public RankResult computeRank(String perm, String seed, List<String> colGropNames, String log2) {
//        System.out.println("log2 "+log2);
        String colGroupName = "";
        RankResult rankResults = null;
        for (String str : colGropNames) {
            colGroupName = colGroupName + str + "_";
        }
        String key = divaDataset.getName() + "_RANK_" + colGroupName + "_" + log2 + ".ser";
        if (computingDataList.contains(key)) {
            rankResults = getRankResult(key);

        } else {
            try {
                rankResults = computeRank(divaDataset.getId(), perm, seed, colGropNames, log2);
                saveRankResult(key, rankResults);
                computingDataList.add(key);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return rankResults;
    }

    /**
     * This method is responsible for exporting data
     *
     * @param gName - selected group name
     * @param path - path to divaFiles folder
     * @param url - context url
     * @param textFileName - exported file name
     * @return exported file url
     */
    public String exportDataTotext(String gName, String path, String url, String textFileName) {
        Group g = null;
        for (Group gr : divaDataset.getRowGroups()) {
            if (gr.getName().equalsIgnoreCase(gName)) {
                g = gr;
                break;
            }
        }
        return util.exportDataTotext(divaDataset, g, path, url, textFileName);

    }

     public List<DivaGroup> getColGroups() {
        return datasetInfo.getColGroupsList();
    }

    public int[] getPCASelection(int startX, int startY, int endX, int endY) {

        return mainPCAGenerator.getPCASelection(startX, startY, endX, endY);
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

//    public DatasetInformation activateGroups(String[] rowGroups) {
//        divaDataset = activateGroups(rowGroups, colGroups, divaDataset);
//       
//        this.updateDatasetInfo();
//        return datasetInfo;
//    }
    
}
