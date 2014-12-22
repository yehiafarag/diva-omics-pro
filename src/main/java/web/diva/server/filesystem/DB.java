/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.filesystem;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.somclust.model.ClusterResults;
import web.diva.server.model.DivaUtil;
import web.diva.server.model.beans.DivaDataset;
import web.diva.shared.beans.PCAResults;
import web.diva.shared.beans.RankResult;

/**
 *
 * @author Yehia Farag
 * this class represents the main file system class in DIVA
 */
public class DB implements Serializable{

    private final FileSystemUtil databaseUtil = new FileSystemUtil();
    private Map<Integer, String> datasetsNameMap;
    private String path;
    private final DivaUtil util = new DivaUtil();

    /**
     * This method is responsible for initializing diva datasets
     * it convert all dataset text files into serialized stored objects
     * the method invokes on system start up and re checks the files on start up sessions
     * @param fileFolderPath  - path to divaFiles folder
     */
    public void initDivaDatasets(String fileFolderPath) {
        path = fileFolderPath;
        File appFolder = new File(path);
        for (File datasetFile : appFolder.listFiles()) {
            if (datasetFile.getName().endsWith(".txt")) {
                DivaDataset divaDs = this.getDataset(0, datasetFile.getName());
                this.setDataset(divaDs);

            }
        }

    }
    /**
     * This method is responsible for getting available datasets names and ids
     * @param fileFolderPath - divaFiles folder path
     * @return datasetsTitleMap
     */
    public TreeMap<Integer, String> getAvailableDatasets(String fileFolderPath) {
        path = fileFolderPath;
        datasetsNameMap = databaseUtil.getDatasetsNameMap(fileFolderPath);
        TreeMap<Integer, String> datasetsTitleMap = new TreeMap<Integer, String>();
        for (int key : datasetsNameMap.keySet()) {
            String name = datasetsNameMap.get(key);
            name = name.substring(0, (name.length() - 4));
            datasetsTitleMap.put(key, name);
        }
        return datasetsTitleMap;
    }

     /**
     * This method is responsible for getting available computing files ids
     * @param fileFolderPath - divaFiles folder path
     * @return computingFileList
     */
    public HashSet<String> getAvailableComputingFileList(String fileFolderPath) {

        HashSet<String> computingFileList = databaseUtil.getAvailableComputingFileList(fileFolderPath);
        return computingFileList;
    }

    /**
     * This method is responsible for getting and de-serialize dataset
     * @param datasetId  - dataset id
     * @return DivaDataset
     */
    private DivaDataset getDataset(int datasetId, String datasetName) {
        if (datasetName == null) {
            datasetName = datasetsNameMap.get(datasetId);
        }
        Dataset jDataset = databaseUtil.getJexpressDataset(datasetName, path);
        DivaDataset newDS = new DivaDataset(jDataset.getData(), jDataset.getRowIds(), jDataset.getColumnIds());
        newDS.setColumnIds(jDataset.getColumnIds());
        newDS.setMissingMeasurements(jDataset.getMissingMeasurements());
        newDS.setRowIds(jDataset.getRowIds());
        newDS.getColumnGroups().clear();
        newDS.addRowAnnotationNameInUse(jDataset.getInfoHeaders()[0]);
        newDS.getColumnGroups().addAll(jDataset.getColumnGroups());
        newDS.getRowGroups().clear();
//        Group g = new Group("t all", Color.yellow, new Selection(Selection.TYPE.OF_ROWS,new int[]{}));
//        jDataset.addRowGroup(g);
        newDS.getRowGroups().addAll(jDataset.getRowGroups());
        newDS.setName(jDataset.getName());
        newDS = util.initDivaDs(newDS, datasetId);
        String[] geneNamesArr = util.initGeneNamesArr(newDS.getGeneIndexNameMap());
        newDS.setGeneNamesArr(geneNamesArr);
       

        return newDS;
    }

    /**
     * This method is responsible for getting DIVA dataset
     * @param datasetId  - dataset id
     * @return DivaDataset
     */
    public DivaDataset getDivaDataset(int datasetId) {
        DivaDataset divaDS = databaseUtil.getDivaDataset(datasetsNameMap.get(datasetId), path);
        divaDS.setId(datasetId);
        return divaDS;
    }
    

    /**
     * This method is responsible for storing DIVA dataset
     * @param ds - divaDataset to store
     */
    public void setDataset(DivaDataset ds) {
        try {
            File dbFile = new File(path, ds.getName() + ".ser");
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            } else {
                return;
            }
            OutputStream file = new FileOutputStream(dbFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(ds);
            output.flush();
            output.close();
            System.gc();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
//        if (datasetsNameMap != null) {
//            datasetsNameMap.put(id, ds.getName() + ".ser");
//        }

    }

    /**
     * This method is responsible for storing clustering results
     * @param id  - clustering id
     * @param results - clustering result to store
     */
    public void saveSomClustResult(String id, ClusterResults results) {

        try {
            File dbFile = new File(path + "/computing", id);
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            } else {
                return;
            }
            OutputStream file = new FileOutputStream(dbFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(results);
            output.flush();
            output.close();
            System.gc();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }

    /**
     * This method is responsible for retrieving clustering results
     * @param id  - clustering id
     * @return results - clustering result
     */
    public ClusterResults getSomClustResult(String id) {
        ClusterResults results = databaseUtil.deSerializeSomClustResult(id, path + "/computing");
        return results;
    }

    /**
     * This method is responsible for storing PCA results
     * @param id  - clustering results id
     * @param results - pca result to store
     */
    public void savePCAResult(String id, PCAResults results) {

        try {
            File dbFile = new File(path + "/computing", id);
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            } else {
                return;
            }
            OutputStream file = new FileOutputStream(dbFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(results);
            output.flush();
            output.close();
            System.gc();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }

    /**
     * This method is responsible for retrieving PCA results
     * @param id  - pca results id
     * @return results - pca results result 
     */
    public PCAResults getPCAResult(String id) {
        PCAResults results = databaseUtil.deSerializePCAResult(id, path + "/computing");
        return results;
    }
      /**
     * This method is responsible for storing ranking results
     * @param id  - ranking results id
     * @param results - ranking result to store
     */
    public void saveRankResult(String id, RankResult results) {

        try {
            File dbFile = new File(path + "/computing", id);
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            } else {
                return;
            }
            OutputStream file = new FileOutputStream(dbFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(results);
            output.flush();
            output.close();
            System.gc();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }

      /**
     * This method is responsible for retrieving rank results
     * @param id  - ranking results id
     * @return results - ranking results result
     */
    public RankResult getRankResult(String id) {
        RankResult results = databaseUtil.deSerializeRankResult(id, path + "/computing");
        return results;
    }
}
