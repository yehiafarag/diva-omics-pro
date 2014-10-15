/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.unused;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import org.apache.commons.codec.binary.Base64;
import org.tc33.jheatchart.HeatChart;
import web.diva.shared.beans.HeatMapImageResult;

/**
 *
 * @author Yehia Farag
 */
public class HeatMapGenerator {

    private final String path;
    private final Dataset dataset;
    private final String hmImageName;

    private List<String> indexer;

    private int[] indexerArr;
    private int[] colIndexerArr;
    private final HeatMapImageResult results;
    private List<String> colIndexer;

    public HeatMapGenerator(String path, Dataset dataset, List<String> indexer, List<String> colIndexer, String hmImage) {
        this.dataset = dataset;
        this.hmImageName = hmImage;
        this.path = path;
        this.indexer = indexer;
        this.colIndexer = colIndexer;
        this.results = new HeatMapImageResult();
        generateImage();

    }

    public HeatMapGenerator(String path, Dataset dataset, int[] indexer, int[] colIndexer, String hmImage) {
        this.dataset = dataset;
        this.hmImageName = hmImage;
        this.path = path;
        this.indexerArr = indexer;
        this.colIndexerArr = colIndexer;
        this.results = new HeatMapImageResult();
        generateImage1();

    }

    private String generateImage1() {

        double[][] data = this.dataset.getData();
        double[][] sortedRowData = new double[data.length][];
//        int[] reIndex = new int[indexer.size()];
        String[] geneNames = new String[indexerArr.length];
        for (int x = 0; x < data.length; x++) {
            double[] d = data[indexerArr[x]];
            sortedRowData[x] = d;
//            reIndex[x] = Integer.valueOf(indexer.get(x));
            geneNames[x] = this.dataset.getRowIds()[indexerArr[x]];
        }

        double[][] sortedRowColData = new double[data.length][];
//        int[] colReIndex = new int[colIndexerArr.length];
        String[] colNames = new String[colIndexerArr.length];

        for (int x = 0; x < data.length; x++) {
            double[] d = sortedRowData[x];
            double[] sortedD = new double[d.length];
            for (int z = 0; z < d.length; z++) {
                sortedD[z] = d[colIndexerArr[z]];
            }
            sortedRowColData[x] = sortedD;
        }
        for (int x = 0; x < colIndexerArr.length; x++) {
//            colReIndex[x] = Integer.valueOf(colIndexer.get(x));
            colNames[x] = this.dataset.getColumnIds()[colIndexerArr[x]];
        }

// Step 1: Create our heat map chart using our data.
        HeatChart map = new HeatChart(sortedRowColData) {

            @Override
            public void setHighValueColour(Color color) {
                super.setHighValueColour(color); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setLowValueColour(Color color) {
                super.setLowValueColour(color); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setColourScale(double d) {
                super.setColourScale(d); //To change body of generated methods, choose Tools | Templates.
            }
        };

// Step 2: Customise the chart.
        Object[] lab = this.dataset.getRowIds();
        Object[] col = this.dataset.getColumnIds();

        map.setYValues(lab);
        Object[] sortedCol = new Object[col.length];
        System.arraycopy(colNames, 0, sortedCol, 0, col.length);
        map.setXValues(sortedCol);
        map.setShowYAxisValues(false);
        map.setShowXAxisValues(false);

        map.setHighValueColour(new Color(0, 255, 0));//(255,154,154));
        map.setColourScale(1.0);
        map.setLowValueColour(new Color(255, 0, 0));//(56,73,187));
        map.setBackgroundColour(Color.gray);
        map.setAxisThickness(0);
        map.setChartMargin(0);

        // Step 3: Output the chart to a file.
        results.setColNum(dataset.getDataWidth());
        results.setMaxColour("#00FF00");//"#FF0000");//("#FF9A9A");
        results.setMinColour("#FF0000");//"#3849BB");

        if (map.getLowValue() >= 0.0) {
            //only positive value
            results.setMinColour("#d3d3d3");
            map.setLowValueColour(Color.LIGHT_GRAY);
        } else if (map.getHighValue() <= 0.0) {
            results.setMaxColour("#d3d3d3");
            map.setHighValueColour(Color.LIGHT_GRAY);
        }
        results.setMaxValue(map.getHighValue());
        results.setMinValue(map.getLowValue());
        results.setRowNum(data.length);
        results.setGeneReindex(indexerArr);
        results.setGeneName(geneNames);
        results.setColNames(colNames);
        results.setColReindex(colIndexerArr);
        File img = new File(path, hmImageName + ".png");

        String base64 = "";
        try {
            if (!img.exists()) {
                img.createNewFile();
            }
            map.saveToFile(img);

            // Reading a Image file from file system
            FileInputStream imageInFile = new FileInputStream(img);
            byte imageData[] = new byte[(int) img.length()];
            imageInFile.read(imageData);
            base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;

        } catch (IOException exp) {
            System.err.println(exp.getMessage());
        }

        results.setImgString(base64);
        return base64;
    }

    private String generateImage() {

        double[][] data = this.dataset.getData();
        double[][] sortedRowData = new double[data.length][];
        int[] reIndex = new int[indexer.size()];
        String[] geneNames = new String[indexer.size()];
        for (int x = 0; x < data.length; x++) {
            double[] d = data[Integer.valueOf(indexer.get(x))];
            sortedRowData[x] = d;
            reIndex[x] = Integer.valueOf(indexer.get(x));
            geneNames[x] = this.dataset.getRowIds()[Integer.valueOf(indexer.get(x))];
        }

        double[][] sortedRowColData = new double[data.length][];
        int[] colReIndex = new int[colIndexer.size()];
        String[] colNames = new String[colIndexer.size()];

        for (int x = 0; x < data.length; x++) {
            double[] d = sortedRowData[x];
            double[] sortedD = new double[d.length];
            for (int z = 0; z < d.length; z++) {
                sortedD[z] = d[Integer.valueOf(colIndexer.get(z))];
            }
            sortedRowColData[x] = sortedD;
        }
        for (int x = 0; x < colIndexer.size(); x++) {
            colReIndex[x] = Integer.valueOf(colIndexer.get(x));
            colNames[x] = this.dataset.getColumnIds()[Integer.valueOf(colIndexer.get(x))];
        }

// Step 1: Create our heat map chart using our data.
        HeatChart map = new HeatChart(sortedRowColData) {

            @Override
            public void setHighValueColour(Color color) {
                super.setHighValueColour(color); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setLowValueColour(Color color) {
                super.setLowValueColour(color); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setColourScale(double d) {
                super.setColourScale(d); //To change body of generated methods, choose Tools | Templates.
            }
        };

// Step 2: Customise the chart.
        Object[] lab = this.dataset.getRowIds();
        Object[] col = this.dataset.getColumnIds();

        map.setYValues(lab);
        Object[] sortedCol = new Object[col.length];
        System.arraycopy(colNames, 0, sortedCol, 0, col.length);
        map.setXValues(sortedCol);
        map.setShowYAxisValues(false);
        map.setShowXAxisValues(false);

        map.setHighValueColour(new Color(0, 255, 0));//(255,154,154));
        map.setColourScale(1.0);
        map.setLowValueColour(new Color(255, 0, 0));//(56,73,187));
        map.setBackgroundColour(Color.gray);
        map.setAxisThickness(0);
        map.setChartMargin(0);

        // Step 3: Output the chart to a file.
        results.setColNum(dataset.getDataWidth());
        results.setMaxColour("#00FF00");//"#FF0000");//("#FF9A9A");
        results.setMinColour("#FF0000");//"#3849BB");

        if (map.getLowValue() >= 0.0) {
            //only positive value
            results.setMinColour("#d3d3d3");
            map.setLowValueColour(Color.LIGHT_GRAY);
        } else if (map.getHighValue() <= 0.0) {
            results.setMaxColour("#d3d3d3");
            map.setHighValueColour(Color.LIGHT_GRAY);
        }
        results.setMaxValue(map.getHighValue());
        results.setMinValue(map.getLowValue());
        results.setRowNum(data.length);
        results.setGeneReindex(reIndex);
        results.setGeneName(geneNames);
        results.setColNames(colNames);
        results.setColReindex(colReIndex);
        File img = new File(path, hmImageName + ".png");

        String base64 = "";
        try {
            if (!img.exists()) {
                img.createNewFile();
            }
            map.saveToFile(img);

            // Reading a Image file from file system
            FileInputStream imageInFile = new FileInputStream(img);
            byte imageData[] = new byte[(int) img.length()];
            imageInFile.read(imageData);
            base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;

        } catch (IOException exp) {
            System.err.println(exp.getMessage());
        }

        results.setImgString(base64);
        return base64;
    }

    public HeatMapImageResult getHeatMapResults() {
        return results;
    }
}
