/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.unused;

import java.awt.Color;
import java.util.HashSet;
import java.util.TreeMap;
import no.uib.jexpress_modularized.core.dataset.Group;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import web.diva.server.model.JFreeImgGenerator;
import web.diva.server.model.beans.DivaDataset;
import web.diva.shared.beans.PCAImageResult;
import web.diva.shared.beans.PCAPoint;
import web.diva.shared.beans.PCAResults;

/**
 *
 * @author Yehia Farag
 */
public class PCAGenerator {

    private final JFreeImgGenerator imgGenerator;

    public PCAGenerator() {
        imgGenerator = new JFreeImgGenerator();
    }

    private TreeMap<String, XYSeries> seriesList;

    /**
     *
     *
     * @return dataset.
     */
    private XYDataset createDataset(TreeMap<Integer, PCAPoint> points, int[] subSelectionData, int[] selection, boolean zoom, DivaDataset divaDataset) {

        final XYSeriesCollection dataset = new XYSeriesCollection();
        seriesList = new TreeMap<String, XYSeries>();
        seriesList.put("#000000", new XYSeries("#000000"));
        seriesList.put("unGrouped", new XYSeries("LIGHT_GRAY"));

        for (Group g : divaDataset.getRowGroups()) {
            if (g.isActive() && !g.getName().equalsIgnoreCase("all")) {
                seriesList.put(g.getHashColor(), new XYSeries(g.getHashColor()));
            }
        }

        if (!zoom && (selection == null || selection.length == 0) && subSelectionData == null) {
            for (int key : points.keySet()) {
                PCAPoint point = points.get(key);
                if (seriesList.containsKey(point.getColor())) {
//                    seriesList.get(divaDataset.getGeneColorArr()[point.getGeneIndex()]).add(point.getX(), point.getY());
                } else {
                    seriesList.get("unGrouped").add(point.getX(), point.getY());
                }

            }

        } else if (zoom) {
            selectionSet.clear();
            for (int i : selection) {
                selectionSet.add(i);
            }

            for (int x : subSelectionData) {
                PCAPoint point = points.get(x);
                if (selectionSet.contains(point.getGeneIndex())) {
                    if (seriesList.containsKey(point.getColor())) {
//                        seriesList.get(divaDataset.getGeneColorArr()[point.getGeneIndex()]).add(point.getX(), point.getY());

                    } else {

                        seriesList.get("#000000").add(point.getX(), point.getY());
                    }

                } else {
                    seriesList.get("unGrouped").add(point.getX(), point.getY());
                }
            }

        } else if (subSelectionData != null) {
            selectionSet.clear();
            for (int i : selection) {
                selectionSet.add(i);
            }
//            for (int key : subSelectionData) {
//                PCAPoint point = points.get(key);
//                if (selectionSet.contains(point.getGeneIndex())) {
//                    if (seriesList.containsKey(divaDataset.getGeneColorArr()[point.getGeneIndex()])) {
//                        seriesList.get(divaDataset.getGeneColorArr()[point.getGeneIndex()]).add(point.getX(), point.getY());
//
//                    } else {
//
//                        seriesList.get("#000000").add(point.getX(), point.getY());
//                    }
//
//                } else {
//
//                    seriesList.get("unGrouped").add(point.getX(), point.getY());
//                }
//
//            }

        } else //selection without zoom
        {
            selectionSet.clear();
            for (int i : selection) {
                selectionSet.add(i);
            }
//            for (int key : points.keySet()) {
//                PCAPoint point = points.get(key);
//
//                if (selectionSet.contains(point.getGeneIndex())) {
//                    if (seriesList.containsKey(divaDataset.getGeneColorArr()[point.getGeneIndex()])) {
//                        seriesList.get(divaDataset.getGeneColorArr()[point.getGeneIndex()]).add(point.getX(), point.getY());
//
//                    } else {
//
//                        seriesList.get("#000000").add(point.getX(), point.getY());
//                    }
//
//                } else {
//
//                    seriesList.get("unGrouped").add(point.getX(), point.getY());
//                }
//
//            }

        }
        for (XYSeries ser : seriesList.values()) {
            dataset.addSeries(ser);
        }

        return dataset;

    }

    private final HashSet<Integer> selectionSet = new HashSet<Integer>();
    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo(new StandardEntityCollection());

    public PCAImageResult generateChart(String path, PCAResults pcaResults, int[] subSelectionData, int[] selection, boolean zoom, boolean selectAll, String imgName, double w, double h, DivaDataset divaDataset) {
        XYDataset dataset = this.createDataset(pcaResults.getPoints(), subSelectionData, selection, zoom, divaDataset);
        final JFreeChart chart = ChartFactory.createScatterPlot(
                "", // chart title
                "Principal Component" + (pcaResults.getPcai() + 1),// x axis label
                "Principal Component " + (pcaResults.getPcaii() + 1), // y axis label
                dataset, // data
                PlotOrientation.VERTICAL,
                false, // include legend
                true, // tooltips
                false // urls
        );
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
        XYDotRenderer renderer = new XYDotRenderer();
        renderer.setDotHeight(5);
        renderer.setDotWidth(5);

        if (selectAll) {
            int i = 0;
            for (String col : seriesList.keySet()) {
                if (col.equalsIgnoreCase("unGrouped")) {
                    col = "#000000";
                }
                renderer.setSeriesPaint(i, imgGenerator.hex2Rgb(col));
                i++;
            }

        } else if (selection == null) {
            renderer.setPaint(Color.LIGHT_GRAY);
            int i = 0;
            for (String col : seriesList.keySet()) {
                if (col.equalsIgnoreCase("unGrouped")) {
                    col = "#000000";
                }
                renderer.setSeriesPaint(i, imgGenerator.hex2Rgb(col));
                i++;
            }
        } else {
            int i = 0;
            for (String col : seriesList.keySet()) {
                if (col.equalsIgnoreCase("unGrouped")) {
                    renderer.setSeriesPaint(i, Color.LIGHT_GRAY);
                } else {
                    renderer.setSeriesPaint(i, imgGenerator.hex2Rgb(col));

                }
                i++;
            }
        }
        plot.setRenderer(renderer);
        plot.setSeriesRenderingOrder(SeriesRenderingOrder.REVERSE);
        NumberAxis xAxis = new NumberAxis("Principal Component" + (pcaResults.getPcai() + 1));
        xAxis.setVerticalTickLabels(true);
        boolean auto = xAxis.getAutoRangeIncludesZero();
        xAxis.setAutoRangeIncludesZero(true ^ auto);
        NumberAxis yAxis = new NumberAxis("Principal Component" + (pcaResults.getPcaii() + 1));
        yAxis.setAutoRangeIncludesZero(true ^ auto);
        yAxis.setTickUnit(new NumberTickUnit(1));
        plot.setDomainAxis(0, xAxis);
        plot.setRangeAxis(0, yAxis);

        double MaxX = xAxis.getRange().getUpperBound();
        double MinX = xAxis.getRange().getLowerBound();
        double MaxY = yAxis.getRange().getUpperBound();
        double MinY = yAxis.getRange().getLowerBound();

        chartRenderingInfo.clear();
        String imgUrl = imgGenerator.saveToFile(chart, w, h, chartRenderingInfo);
        PCAImageResult imgUtilRes = new PCAImageResult();
        imgUtilRes.setImgString(imgUrl);
        imgUtilRes.setDataAreaMaxX(chartRenderingInfo.getPlotInfo().getDataArea().getMaxX());
        imgUtilRes.setDataAreaMaxY(chartRenderingInfo.getPlotInfo().getDataArea().getMaxY());
        imgUtilRes.setDataAreaMinY(chartRenderingInfo.getPlotInfo().getDataArea().getMinY());
        imgUtilRes.setDataAreaMinX(chartRenderingInfo.getPlotInfo().getDataArea().getMinX());
        imgUtilRes.setMaxX(MaxX);
        imgUtilRes.setMaxY(MaxY);
        imgUtilRes.setMinX(MinX);
        imgUtilRes.setMinY(MinY);
        return imgUtilRes;
    }

}
