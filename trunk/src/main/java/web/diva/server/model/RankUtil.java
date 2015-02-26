/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import java.util.ArrayList;
import no.uib.jexpress_modularized.rank.computation.RPResult;
import web.diva.shared.beans.RankResult;

/**
 *
 * @author Yehia Farag
 */
public class RankUtil {

    public RankResult handelRankTable(ArrayList<RPResult> jResults) {
        RankResult results = initFull(jResults);        
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
    private RankResult initFull(ArrayList<RPResult> jResults){       
        
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

        for(int x=0;x<jResults.get(0).getColumnCount();x++){
        headers[x]= jResults.get(0).getColumnName(x);
        }
        int index =jResults.get(0).getColumnCount();
        for(int x=0;x<jResults.get(1).getColumnCount();x++){
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
//         for(String str:headers)
//            System.out.print(str+"  --  ");
//         System.out.println();
//         System.out.println(posRank[0]+"        --  "+rowIds[0]+"  --  "+tableData[0][0]+"  --  "+tableData[1][0]+"  --  "+tableData[2][0]+"  --  "+tableData[3][0]+"  --  "          
//         +negRank[0]+"  --  "+tableData[4][0]+"  --  "+tableData[5][0]+"  --  "+tableData[6][0]+"  --  ");
//           System.out.println(posRank[1]+"        --  "+rowIds[1]+"  --  "+tableData[0][1]+"  --  "+tableData[1][1]+"  --  "+tableData[2][1]+"  --  "+tableData[3][1]+"  --  "          
//         +negRank[1]+"  --  "+tableData[4][1]+"  --  "+tableData[5][1]+"  --  "+tableData[6][1]+"  --  ");
//
//        
//        
        
        
//        index=0;
//        for (int x = 0; x < headersCount; x++) {
//            if(x < jResults.get(0).getColumnCount()){
//            headers[x] = jResults.get(0).getColumnName(x);
//            String[] col = new String[jResults.get(0).getRowCount()];
//            for (int y = 0; y < jResults.get(0).getRowCount(); y++) {
//                col[y] = jResults.get(0).getValueAt(y, x).toString();
//            }
//            postivetableData[x] = col;
//            }else{
//            headers[x] = jResults.get(1).getColumnName(index);
//            String[] col = new String[jResults.get(1).getRowCount()];
//            for (int y = 0; y < jResults.get(1).getRowCount(); y++) {
//                col[y] = jResults.get(1).getValueAt(y, index).toString();
//               
//            } 
//            index++;
//            postivetableData[x] = col;
//            
//            }
//        }

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
       
//         results.setPosTableHeader(headers);
//            results.setPosTableData(postivetableData);

        return results;
    }
}
