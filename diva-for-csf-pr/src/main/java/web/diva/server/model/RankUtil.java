/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import java.awt.Color;
import java.util.ArrayList;
//import java.util.Calendar;
import no.uib.jexpress_modularized.rank.computation.RPResult;
import org.apache.commons.lang3.math.NumberUtils;
import org.jfree.chart.plot.PlotOrientation;
import web.diva.shared.beans.RankResult;

/**
 *
 * @author Yehia Farag
 */
public class RankUtil {

    public RankResult handelRankTable(ArrayList<RPResult> jResults,boolean sparkline) {
        RankResult results = initFull(jResults);
        results.setSparkline(sparkline);
        if(sparkline){
        results.setLabels(initRankColumnSparkLine( results.getTableData(), results.getRowIds().length));
        results.setMaxValues(maxValues);
        
        }
        return results;
    }

    private RankResult initResults(RankResult res, RPResult table, int postive) {
        int[] rankToIndex = new int[table.getRowCount()];
        int[] indexToRank = new int[table.getRowCount()];
        String[] tableData[] = new String[table.getColumnCount()][table.getRowCount()];
        String[] headers = new String[table.getColumnCount()];
        for (int x = 0; x < table.getColumnCount(); x++) {
            headers[x] = table.getColumnName(x);
            String[] col = new String[table.getRowCount()];
            for (int y = 0; y < table.getRowCount(); y++) {
                col[y] = table.getValueAt(y, x).toString();
            }
            tableData[x] = col;
        }

        for (int x = 0; x < table.getRowCount(); x++) {
            rankToIndex[x] = table.getSortMap().get(x + 1);
        }
        for (int x = 0; x < table.getRowCount(); x++) {
            indexToRank[rankToIndex[x]] = x + 1;
        }

        if (postive == 0) {
            res.setPosTableHeader(headers);
            res.setPosTableData(tableData);
            res.setPosIndexToRank(indexToRank);
            res.setPosRankToIndex(rankToIndex);
        } else {
            res.setNegTableHeader(headers);
            res.setNegTableData(tableData);
            res.setNegIndexToRank(indexToRank);
            res.setNegRankToIndex(rankToIndex);
        }

        return res;

    }


    private RankResult initFull(ArrayList<RPResult> jResults) {

        RankResult results = new RankResult();
        int[] posRankToIndex = new int[jResults.get(0).getRowCount()];
        int[] posIndexToRank = new int[jResults.get(0).getRowCount()];

        int[] negRankToIndex = new int[jResults.get(1).getRowCount()];
        int[] indexToNegRank = new int[jResults.get(1).getRowCount()];
        int[] posRankToNegRank = new int[jResults.get(1).getRowCount()];

        
         for (int x = 0; x < jResults.get(0).getRowCount(); x++) {
            posRankToIndex[x] = jResults.get(0).getSortMap().get(x + 1);
        }
        for (int x = 0; x < jResults.get(0).getRowCount(); x++) {
            posIndexToRank[posRankToIndex[x]] = x + 1;
        }
        
        
          for (int x = 0; x < jResults.get(1).getRowCount(); x++) {
            negRankToIndex[x] = jResults.get(1).getSortMap().get(x + 1);
        }
        for (int x = 0; x < jResults.get(1).getRowCount(); x++) {
            indexToNegRank[negRankToIndex[x]] = x + 1;
        }
        
        
        for (int x = 0; x < jResults.get(0).getRowCount(); x++) {
             int index = posRankToIndex[x];
             posRankToNegRank[x] = indexToNegRank[index];
        }
        
        
        int headersCount = jResults.get(0).getColumnCount() + jResults.get(1).getColumnCount() - 2;
        
        double[] tableData[] = new double[headersCount-3][jResults.get(0).getRowCount()];        
        int[] posRank = new int[jResults.get(0).getRowCount()];
        int[] negRank = new int[jResults.get(0).getRowCount()];
        String[] rowIds = new String[jResults.get(0).getRowCount()];
        String[] headers = new String[headersCount];

        for (int x = 0; x < jResults.get(0).getColumnCount(); x++) {
            headers[x] = jResults.get(0).getColumnName(x);
        }
        int index = jResults.get(0).getColumnCount();
        for (int x = 0; x < jResults.get(1).getColumnCount(); x++) {
            if (x == 1 || x == 2) {
                continue;
            }
            headers[index++] = jResults.get(1).getColumnName(x);
        }
        headers[0] = "Pos " + headers[0];
        for (int x = 6; x < headersCount; x++) {
            if (!headers[x].contains("Neg")) {
                headers[x] = "Neg " + headers[x];
            }
        }
        index = 0;
        for (int rowIndex = 0; rowIndex < jResults.get(0).getRowCount(); rowIndex++) {
            int dataColumnReindex = 0;
            for (int columnIndex = 0; columnIndex < jResults.get(0).getColumnCount(); columnIndex++) {
                switch (columnIndex) {
                    case 0:
                        posRank[rowIndex] = (Integer) jResults.get(0).getValueAt(rowIndex, columnIndex);
                        break;
                    case 1:
                        rowIds[rowIndex] = jResults.get(0).getValueAt(rowIndex, columnIndex).toString();
                        break;
                    default:
                        tableData[dataColumnReindex][rowIndex] = (Double) jResults.get(0).getValueAt(rowIndex, columnIndex);
                        dataColumnReindex++;
                }
            }
        }
        for (int rowIndex = 0; rowIndex < jResults.get(0).getRowCount(); rowIndex++) {
            int dataColumnReIndex = jResults.get(0).getColumnCount() - 2;
            for (int columnIndex = 0; columnIndex < jResults.get(1).getColumnCount(); columnIndex++) {
                switch (columnIndex) {
                    case 0:
                        negRank[rowIndex] = (Integer) jResults.get(1).getValueAt(posRankToNegRank[rowIndex] - 1, columnIndex);

                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        tableData[dataColumnReIndex][rowIndex] = (Double) jResults.get(1).getValueAt(posRankToNegRank[rowIndex] - 1, columnIndex);

                        dataColumnReIndex++;
                }

            }
        }
        results.setHeaders(headers);
        results.setTableData(tableData);

        results.setPosIndexToRank(posIndexToRank);
        results.setPosRankToIndex(posRankToIndex);

        results.setNegIndexToRank(indexToNegRank);
        results.setNegRankToIndex(negRankToIndex);
        results.setPosRank(posRank);
        results.setNegRank(negRank);
        results.setRowIds(rowIds);
        results.setPosRankToNegRank(posRankToNegRank);
        return results;
    }

//    private JTable rankJTable;
    private final Color colorA = new Color(251, 51, 51);
    /**
     * The second example color.
     */
    private final Color colorB = new Color(51, 51, 251);
    /**
     * The third example color.
     */
    private final  Color colorC = new Color(110, 196, 97);
    
    private double[] maxValues;
    
    
//    private final Color backGroungC = new Color(240,240,240,0);
//
//    private final ImageEncoder in = ImageEncoderFactory.newInstance(ImageFormat.PNG, new Float(0.084666f));


    @SuppressWarnings("CallToPrintStackTrace")
    private String[][] initRankColumnSparkLine(double[][] values, int rowNumber) {
//        JScrollPane singleValuesJScrollPane = new javax.swing.JScrollPane();
//        rankJTable = new JTable() {
//            @Override
//            public boolean getScrollableTracksViewportHeight() {
//                return true;
//            }
//        };
//        String[] headers = new String[]{
//            "Fold change", "Pos Score", "Expected Score", "q-value", "Neg Score", "Neg Expected Score", "Neg q-value"
//        };
//        rankJTable.setModel(new javax.swing.table.DefaultTableModel(
//                new Object[][]{}, headers
//        ) {
//            Class[] types = new Class[]{
//                java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class
//            };
//            @Override
//            public Class getColumnClass(int columnIndex) {
//                return types[columnIndex];
//            }
//        });
//        rankJTable.setFillsViewportHeight(true);
//        rankJTable.setOpaque(false);
//        rankJTable.setSelectionBackground(new java.awt.Color(204, 204, 204));
//        singleValuesJScrollPane.setViewportView(rankJTable);
//        addDataSingleValues(values, rowNumber);

//        ArrayList<JSparklinesBarChartTableCellRenderer> sparkLineGenerators = new ArrayList<JSparklinesBarChartTableCellRenderer>();
//        double[] foldChange = getMaxMinValue(values[0]);
//        JSparklinesBarChartTableCellRenderer foldChangeRenderer = new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, foldChange[1], foldChange[0], colorB, colorA);
//        SparkLineGenerator sparklineGenerator= new SparkLineGenerator(PlotOrientation.HORIZONTAL, foldChange[1], foldChange[0], colorB, colorA);
//        foldChangeRenderer.setBackgroundColor(backGroungC);
//        rankJTable.getColumn("Fold change").setCellRenderer(foldChangeRenderer);
//        sparkLineGenerators.add(foldChangeRenderer);
//        for (int index = 1; index < values.length; index++) {
//            double[] calcScore = getMaxMinValue(values[index]);
//            JSparklinesBarChartTableCellRenderer renderer = new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, calcScore[1], colorC);
//            rankJTable.getColumn(headers[index]).setCellRenderer(renderer);
//            sparkLineGenerators.add(renderer);
//
//        }
//        for (int index = 0; index < values.length; index++) {
//            ((JSparklinesBarChartTableCellRenderer) rankJTable.getColumn(headers[index]).getCellRenderer()).showNumberAndChart(true, 40);
//        }
//        rankJTable.validate();
//        rankJTable.repaint();
//        System.out.println("start generating process");
//
//        String[][] labels = new String[rowNumber][7];
//        for (int colIdx = 0; colIdx < values.length; colIdx++) {            
//             JSparklinesBarChartTableCellRenderer renderer  = sparkLineGenerators.get(colIdx);
//            for (int rowIdx = 0; rowIdx < rankJTable.getRowCount(); rowIdx++) {                                                         //rankJTable.getValueAt(rowIdx, colIdx)
//                JSparklinesBarChartTableCellRenderer c = (JSparklinesBarChartTableCellRenderer) renderer.getTableCellRendererComponent(rankJTable,values[colIdx][rowIdx] , false, false,rowIdx, colIdx);//rankJTable.getColumn(headers[colIdx]).getCellRenderer().getTableCellRendererComponent(rankJTable, rankJTable.getValueAt(rowIdx, colIdx), false, false, rowIdx, colIdx);
//                try {
//                    c.getChart().getPlot().setBackgroundImageAlpha(0.0f);
//                    c.getChart().setBackgroundImageAlpha(0.0f);
//                    labels[rowIdx][colIdx] = "data:image/png;base64," +Base64.encodeBytes(in.encode(c.getChart().createBufferedImage(160,30)));
//                    System.out.println("from table img : "+ labels[rowIdx][colIdx]);
//                    System.out.println("from sparkline generator : "+sparklineGenerator.getSparkLine(values[colIdx][rowIdx]));
//                    break;
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//               
//            } 
//             System.out.println("end of column  "+colIdx);
//             
//            System.gc();break;
//        }
          maxValues = new double[7];
          ArrayList<SparkLineGenerator> sparkLineGenerators = new ArrayList<SparkLineGenerator>();
          int index =0;
        for (double[] value : values) {
            SparkLineGenerator sparkLineGenerator = null;
            double[] calcScore = getMaxMinValue(value);
            maxValues[index]= calcScore[0];
            if (calcScore[1] >= 0) {
                sparkLineGenerator = new SparkLineGenerator(PlotOrientation.HORIZONTAL, calcScore[0], colorC);
            } else {
                sparkLineGenerator = new SparkLineGenerator(PlotOrientation.HORIZONTAL, calcScore[1], calcScore[0], colorB, colorA);
            }
            sparkLineGenerators.add(sparkLineGenerator);
        }
        
        String[][] labels = new String[rowNumber][7];
      
        int gcTrigger=0;
        System.err.println("at start generating label ");
        for (int colIdx = 0; colIdx < values.length; colIdx++) {            
             SparkLineGenerator sparkLineGenerator  = sparkLineGenerators.get(colIdx);
            for (int rowIdx = 0; rowIdx <values[0].length; rowIdx++) {     
                    labels[rowIdx][colIdx] = sparkLineGenerator.getSparkLine(values[colIdx][rowIdx]);
//                    break;
                    if(gcTrigger == 3000){
                        System.gc();
                        gcTrigger=0;                    
                    }
                    gcTrigger++;
            } 
            System.err.println("at done with column "+colIdx);
             
            System.gc();
        }


        return labels;

    }

//    private void addDataSingleValues(double[][] values, int rowNumbers) {
//        Object[][] rowArray = new Object[rowNumbers][7];
//        try {
//
//            for (int x = 0; x < rowNumbers; x++) {
//              Object[] row = new Object[7];
//              for (int z = 0; z < 7; z++) {                
//                  row[z] = values[z][x];
//              }
//              rowArray[x] = row;
//             
//          }
//          System.out.println("rowArray length " + rowArray.length + "  " + rowArray[0].length);
//          for (Object d : rowArray[0]) {
//              System.out.print(d + " , ");
//          }
//          System.out.println();
//      } catch (Exception e) {
//          e.printStackTrace();}
//
//      
//      for(Object[] row:rowArray)      
//        ((DefaultTableModel) rankJTable.getModel()).addRow(row);
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein A", 4.44, 12, 30});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein B", -2.19, 11, 13});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein C", 1.86, 2, 5});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein D", -2.17, 17, 32});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein E", 3.01, 32, 57});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein F", 2.62, 12, 28});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein G", 5.33, 16, 37});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein H", 5.65, 47, 61});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein I", 1.81, 23, 45});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein J", -1.91, 78, 34});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein K", 2.6, 15, 31});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein L", 2.3, 31, 44});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein M", -2.45, 5, 14});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein N", 3.59, 18, 56});
////        ((DefaultTableModel) rankJTable.getModel()).addRow(new Object[]{"Protein O", 2.24, 25, 43});
//    }
     private double[] getMaxMinValue(double[] values){
         double[] measuredValues = new double[2];
//         double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
//         for (double d : values) {
//             if (d > max) {
//                 max = d;
//             }
//             if (min > d) {
//                 min = d;
//             }
//
//         }
//        List<Double> b = Arrays.asList(ArrayUtils.toObject(values));
//
//        TreeSet<Double> ts = new TreeSet<Double>(b);

        measuredValues[0] = NumberUtils.max(values);
        measuredValues[1] = NumberUtils.min(values);
        System.out.println("max = " + measuredValues[0] + " min " + measuredValues[1]);

        return measuredValues;
    }

}
