/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.filesystem;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.pca.computation.PcaResults;
import no.uib.jexpress_modularized.somclust.model.ClusterResults;
import web.diva.server.model.beans.DivaDataset;
import web.diva.shared.beans.RankResult;

/**
 *
 * @author Yehia Farag
 */
public class FileSystemUtil implements Serializable{

    private final DatasetFileReader reader = new DatasetFileReader();

    public Map<Integer, String> getDatasetsNameMap(String path) {
        Map<Integer, String> datasetsMap = new HashMap<Integer, String>();
        File appFolder = new File(path);
        int index = 1;
        for (File datasetFile : appFolder.listFiles()) {
            if (datasetFile.getName().endsWith(".ser")) {
                datasetsMap.put(index, datasetFile.getName());
                index++;

            }
        }
        return datasetsMap;

    }

    public TreeSet<String> getAvailableComputingFileList(String fileFolderPath) {
        TreeSet<String> computingFileList = new TreeSet<String>();
        File appFolder = new File(fileFolderPath + "/computing");
        for (File datasetFile : appFolder.listFiles()) {
            if (datasetFile.getName().endsWith(".ser")) {
                computingFileList.add(datasetFile.getName());

            }
        }
        return computingFileList;
    }

    public Dataset getJexpressDataset(String name, String path) {
        File appFolder = new File(path);
        Dataset ds = null;
        for (File f2 : appFolder.listFiles()) {
            if (f2.getName().equalsIgnoreCase(name)) {
                if (name.endsWith(".txt")) {
                    ds = reader.readDatasetFile(f2);
                }
            }
        }
        System.gc();
        return ds;

    }
    
      public DivaDataset loadDatasetAnnotation(File file,DivaDataset divaDs) {
        try {

            FileReader fr = new FileReader(file);
            BufferedReader bufRdr = new BufferedReader(fr);
            String headerLine = bufRdr.readLine();
            String[] headerArr = headerLine.split("\t");
          
           String line ="";
           divaDs.setAnnotationHeaders(headerArr);
           String[][] annotations = new String[divaDs.getDataLength()][headerArr.length];
           int index = 0;
            while ((line = bufRdr.readLine()) != null) {
               String[] annotationRowArr = line.split("\t");
               String[] defaultAnnotRow = new String[headerArr.length];
               for(int x=0;x<annotationRowArr.length;x++){
               defaultAnnotRow[x]= annotationRowArr[x];
               
               }
               annotations[index] = defaultAnnotRow;
               index++;
               
            }
            divaDs.setAnnotations(annotations);
        } catch (Exception e) {
            System.err.println("error : "+e.getMessage());;
        }
        System.gc();
        return divaDs;

    }

    public DivaDataset getDivaDataset(String name, String path) {
        File appFolder = new File(path);
        DivaDataset ds = null;
        for (File f2 : appFolder.listFiles()) {
            if (f2.getName().equalsIgnoreCase(name)) {
                ds = deSerializeDataset(name, path);
            }
        }
        System.gc();
        return ds;

    }

    private DivaDataset deSerializeDataset(String name, String path) {

        try {
            File dbFile = new File(path, name);
            if (!dbFile.exists()) {
                System.out.println("cant find the file");
                return null;
            }
            InputStream file = new FileInputStream(dbFile);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            //deserialize the List
            DivaDataset serDataset = (DivaDataset) input.readObject();
            System.gc();
            return serDataset;

        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public ClusterResults deSerializeSomClustResult(String name, String path) {

        try {
            File dbFile = new File(path, name);
            if (!dbFile.exists()) {
                System.out.println("cant find the file");
                return null;
            }
            InputStream file = new FileInputStream(dbFile);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            //deserialize the List
            ClusterResults serResult = (ClusterResults) input.readObject();
            System.gc();
            return serResult;

        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public PcaResults deSerializePCAResult(String name, String path) {

        try {
            File dbFile = new File(path, name);
            if (!dbFile.exists()) {
                System.out.println("cant find the file");
                return null;
            }
            InputStream file = new FileInputStream(dbFile);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            //deserialize the List
            PcaResults serResult = (PcaResults) input.readObject();
            System.gc();
            return serResult;

        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public RankResult deSerializeRankResult(String name, String path) {

        try {
            File dbFile = new File(path, name);
            if (!dbFile.exists()) {
                System.out.println("cant find the file");
                return null;
            }
            InputStream file = new FileInputStream(dbFile);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            //deserialize the List
            RankResult serResult = (RankResult) input.readObject();
            System.gc();
            return serResult;

        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

}
