/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import web.diva.server.model.SomClustering.SomClustImgGenerator;
import web.diva.server.model.profileplot.ProfilePlotImgeGenerator;
import web.diva.server.model.pca.PCAImageGenerator;
import java.awt.Color;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    private DatasetInformation datasetInfo;
    private final DB database = new DB();
    private final DivaUtil util = new DivaUtil();
    private final GroupColorUtil colorImageGenerator = new GroupColorUtil();
    private final String path;
    private TreeMap<Integer, String> datasetsMap;
    private HashSet<String> computingDataList;
    private final DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    private final Calendar cal = Calendar.getInstance();

    private final HashMap<String, Color> colorMap = new HashMap<String, Color>();

    private SomClustImgGenerator somClustImgGenerator;
    private PCAResults PCAResult;

    private ProfilePlotImgeGenerator profilePlotGenerator;
    private PCAImageGenerator mainPCAGenerator;

    public Computing(String path) {
        database.initDivaDatasets(path);
        this.path = path;
        getAvailableDatasetsMap();
        computingDataList = getAvailableComputingFileList();

    }

    public String getPath() {
        return path;
    }

    /**
     * This method is used to get the available datasets from file system layer
     * (to be replaced by DB later)
     *
     * @return datasetsMap
     */
    public final TreeMap<Integer, String> getAvailableDatasetsMap() {
        datasetsMap = database.getAvailableDatasets(path);
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
        datasetInfo.setAnnotationHeaders(divaDataset.getAnnotationHeaders());
        datasetInfo.setAnnotations(divaDataset.getAnnotations());
        System.out.println("ann length "+divaDataset.getAnnotations().length);
        for(String str: divaDataset.getAnnotationHeaders())
            System.err.println("anno header : "+str);
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
        omicsTableData[0] = divaDataset.getRowIds();
        int index = 1;
        colorMap.put("#000000", Color.BLACK);
        colorMap.put("#BDBDBD", Color.LIGHT_GRAY);
        // init groups name and images
        Map<String, String> groupImgMap = new HashMap<String, String>();

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
            som = null;
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

        String interactiveColumnUrl = somClustImgGenerator.generateInteractiveColumn(divaDataset, new int[]{});

        res.setSideTreeImgUrl(sideTreeBase64);

        res.setHeatMapImgUrl(heatmapUrl);
        res.setScaleImgUrl(scaleUrl);
        res.setDistanceMeasure(distanceMeasureStr);
        res.setLinkage(linkageStr);

        res.setRowNode(somClustImgGenerator.getTooltipsSideNode());

        res.setInteractiveRowImgUrl(interactiveColumnUrl);
        res.setRowNames(rowNames);
        res.setValues(values);
        res.setSideTreeHeight(somClustImgGenerator.getLeftTreeHeight());
        res.setSideTreeWidth(somClustImgGenerator.getLeftTreeWidth());
        res.setTopTreeHeight(somClustImgGenerator.getTopTreeHeight());
        res.setTopTreeWidth(somClustImgGenerator.getTopTreeWidth());
        System.gc();
        return res;

    }

    public SomClustTreeSelectionUpdate updateSideTree(int x, int y, double w, double h) {
        return somClustImgGenerator.updateSideTreeSelection(x, y, w, h);
    }

    public SomClustTreeSelectionUpdate updateUpperTree(int x, int y, double w, double h) {
        return somClustImgGenerator.updateUpperTreeSelection(x, y, w, h);
    }

    public String updateSomClustInteractiveColumn(int[] selection) {
        return somClustImgGenerator.generateInteractiveColumn(divaDataset, selection);
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
        PCAResult = pcaUtil.getPCAResults(divaDataset, comI, comII);
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

    public PCAImageResult pcaZoomIn(int startX, int startY, int endX, int endY) {
        mainPCAGenerator.setZoom(true, (startX), (startY), (endX), (endY));
        PCAImageResult pcaImgResults = new PCAImageResult();
        pcaImgResults.setDatasetId(divaDataset.getId());
        pcaImgResults.setImgString(mainPCAGenerator.toImage());
        pcaImgResults.setTooltipInformatinData(mainPCAGenerator.getTooltipsInformationData());
        return pcaImgResults;
    }

    public PCAImageResult pcaZoomReset() {
        mainPCAGenerator.zoomOut();
        PCAImageResult pcaImgResults = new PCAImageResult();
        pcaImgResults.setDatasetId(divaDataset.getId());
        //set current selection 
        pcaImgResults.setImgString(mainPCAGenerator.toImage());
        pcaImgResults.setTooltipInformatinData(mainPCAGenerator.getTooltipsInformationData());
        return pcaImgResults;

    }

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
                g.setActive(false);

                updatedActiveGroupList.add(g);
            }
            divaDataset.getRowGroups().clear();
            divaDataset.getRowGroups().addAll(updatedActiveGroupList);
            updatedActiveGroupList.clear();
            for (Group g : divaDataset.getRowGroups()) {
                for (String gName : rowGroups) {

                    if (gName.equalsIgnoreCase(g.getName())) {
                        g.setActive(true);
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
    public String createSubDataset(String name, String type, int[] selection) {
        getAvailableDatasetsMap();
        String newDsName = "";
        int id = datasetsMap.lastKey() + 1;

        newDsName = name.toUpperCase() + " ( SUB DS - " + divaDataset.getParentDsName() + " )";
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
        String[][] newAnnotations = new String[selection.length][divaDataset.getAnnotationHeaders().length];
        int[] newIndexes = new int[selection.length];
        for (int x = 0; x < selection.length; x++) {
            double[] row = divaDataset.getData()[selection[x]];
            newdata[x] = row;
            newRowIds[x] = divaDataset.getRowIds()[selection[x]];
            boolean[] mm = divaDataset.getMissingMeasurements()[selection[x]];
            newMissingMeasurments[x] = mm;
            newIndexes[x] = x;
           String[] annotationRow = divaDataset.getAnnotations()[x];            
            newAnnotations[x] = annotationRow;
        }

        DivaDataset newDS = new DivaDataset(newdata, newRowIds, divaDataset.getColumnIds());
        newDS.setParentDsName(divaDataset.getParentDsName());
        newDS.setColumnIds(divaDataset.getColumnIds());
        newDS.setMissingMeasurements(newMissingMeasurments);
        newDS.addRowAnnotationNameInUse(divaDataset.getInfoHeaders()[0]);
        newDS.setName(newDsName);

        newDS.setAnnotationHeaders(divaDataset.getAnnotationHeaders());
        newDS.setAnnotations(newAnnotations);
        newDS.setRowIds(newRowIds);
        newDS.getColumnGroups().clear();

        newDS.getColumnGroups().addAll(divaDataset.getColumnGroups());
        newDS.getRowGroups().clear();

        Group g = new Group("all", Color.BLACK, new Selection(Selection.TYPE.OF_ROWS, newIndexes));
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

        return divaDataset.getName();

    }
    /*
     * method to save text dataset   
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
    public String saveDataset(String newName) {

        newName = (newName.toUpperCase() + " ( " + divaDataset.getName() + " )");
        getAvailableDatasetsMap();
        if (datasetsMap.containsValue(newName)) {
            newName += " - " + dateFormat.format(cal.getTime()).replace(":", " ");
        }

        divaDataset.setName(newName);
        database.setDataset(divaDataset);
        getAvailableDatasetsMap();
        for (String dsName : datasetsMap.values()) {
            if (newName.equalsIgnoreCase(dsName)) {
                return newName;
            }
        }
        return "";
    }
      /**
     * This method is used to store updated datasets
     *
     * @return dataset id
     */
    public String saveCurrentDataset() {
        if (divaDataset.getParentDsName().equalsIgnoreCase(divaDataset.getName())) {
            getAvailableDatasetsMap();

            System.out.println("its new dataset to save");
            DivaDataset newDS = new DivaDataset(divaDataset.getData(), divaDataset.getRowIds(), divaDataset.getColumnIds());
            newDS.setParentDsName(divaDataset.getParentDsName());
            newDS.setColumnIds(divaDataset.getColumnIds());
            newDS.setMissingMeasurements(divaDataset.getMissingMeasurements());
            newDS.addRowAnnotationNameInUse(divaDataset.getInfoHeaders()[0]);
            newDS.setName(divaDataset.getName());

            newDS.setAnnotationHeaders(divaDataset.getAnnotationHeaders());
            newDS.setAnnotations(divaDataset.getAnnotations());
            newDS.setRowIds(divaDataset.getRowIds());
            newDS.getColumnGroups().clear();

            newDS.getColumnGroups().addAll(divaDataset.getColumnGroups());
            newDS.getRowGroups().clear();

            for (Group g : divaDataset.getRowGroups()) {
                if (g.getName().equalsIgnoreCase("All")) {
                    newDS.addRowGroup(g);
                    break;
                }
            }
            newDS.setDefaultRankingName(divaDataset.getDefaultRankingName());
            newDS = util.initDivaDs(newDS, divaDataset.getId());
            newDS.setGeneNamesArr(divaDataset.getGeneNamesArr());
            database.setDataset(newDS);
        } else {
            
            System.out.println("its not dataset to save");
            database.setDataset(divaDataset);
        }
        return "";
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
        if (colorStr.equalsIgnoreCase("red")) {
            return Color.red;
        }
        if (!colorStr.contains("#")) {
            return Color.decode(colorStr);
        } else {
            return new Color(
                    Integer.valueOf(colorStr.substring(1, 3), 16),
                    Integer.valueOf(colorStr.substring(3, 5), 16),
                    Integer.valueOf(colorStr.substring(5, 7), 16));
        }
    }

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
    public RankResult processRank(int datasetId, String perm, String seed, List<String> colGropNames, String log2) {
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
    
    
    /**
     * This method is responsible for computing Ranking
     *@return RankResult
     */
    public RankResult getDefaultRank() {

        if (divaDataset.getDefaultRankingName() == null || !computingDataList.contains(divaDataset.getDefaultRankingName())) {
            String type = "OneClass";
            int iPerm = 400;
            int iSeed = 288848379;
            boolean log = true;
            int[] col1 = null;
            for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getColumnGroups()) {
                if (g.getName().equalsIgnoreCase("All")) {
                    col1 = g.getMembers();
                }
            }

            ComputeRank cr = new ComputeRank(divaDataset);
            ArrayList<RPResult> jResults = cr.createResult(type, iPerm, iSeed, col1, null, log);
            RankResult rankResults = rankUtil.handelRankTable(jResults);
            rankResults.setDatasetId(divaDataset.getId());
            String key = divaDataset.getName() + "_RANK_" + "All" + "_" + "Log 2" + ".ser";
            saveRankResult(key, rankResults);
            computingDataList.add(key);
            divaDataset.setDefaultRankingName(key);
            saveCurrentDataset();

            return rankResults;
        } else {
            RankResult rankResults = getRankResult(divaDataset.getDefaultRankingName());

            return rankResults;

        }
    }

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
    
    public RankResult getRankProductResults(String perm, String seed, List<String> colGropNames, String log2, boolean defaultRank) {
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
                rankResults = processRank(divaDataset.getId(), perm, seed, colGropNames, log2);
                saveRankResult(key, rankResults);
                computingDataList.add(key);
                
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        if (defaultRank) {
                    divaDataset.setDefaultRankingName(key);
                    saveCurrentDataset();
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
    }

}
