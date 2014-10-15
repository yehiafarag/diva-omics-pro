/*not used anymore
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.unused;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import web.diva.server.model.JFreeImgGenerator;

/**
 *
 * @author Yehia Farag
 */
public class ProfilePlotGenerator {

    private JFreeChart chart;
    private final JFreeImgGenerator imgGenerator;
    private  String defaultImageString;
    private HashSet<Integer> indexMarker = new HashSet<Integer>();
    public Color shadowColor = Color.LIGHT_GRAY;

    public ProfilePlotGenerator(Number[][] pointsData, String[] rowIds, String[] columnIds, String[] colors, HashMap<String, Color> colorMap) {

        imgGenerator = new JFreeImgGenerator();
        XYDataset dataset = createDataset(pointsData);
        createChart(dataset, columnIds, colors, colorMap);
//        defaultImageString = generateChart(chart);  
        
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();
        renderer = this.chartColorUpdate(renderer, colors, colorMap);
        for (int x = 0; x < colors.length; x++) {
            renderer.setSeriesLinesVisible(x, false);
        }
    }
    
    
  
    public String getDefaultChartImg() {        
        return defaultImageString;

    }

    /**
     *
     * @return dataset.
     */
    private XYDataset createDataset(Number[][] pointsData) {

        final XYSeriesCollection dataset = new XYSeriesCollection();
         
        for (int x = 0; x < pointsData.length; x++) {
           XYSeries series = new XYSeries(x);
            Number[] data = pointsData[x];
            for (int y = 0; y < data.length; y++) {
                series.add(y, data[y]);
            }
         
            dataset.addSeries(series);

        }
        return dataset;

    }
    Calendar cal ;
    /**
     * Creates a chart.
     *
     * @param dataset the data for the chart.
     * @param columnIds
     *
     * @param lcr the line chart result
     *
     * @return a chart.
     */
    private void createChart(final XYDataset dataset, String[] columnIds, String[] colors, HashMap<String, Color> colorMap) {
        chart = createXYLineChart(
                "", // chart title
                columnIds,
                dataset, // data
                PlotOrientation.VERTICAL,
                false, // include legend
                true // urls
        );
      
        
        
        

    }

    public void updateChartColors(String[] colors, HashMap<String, Color> colorMap) {
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();
        this.chartColorUpdate(renderer, colors, colorMap);

    }

    private XYLineAndShapeRenderer chartColorUpdate(XYLineAndShapeRenderer renderer, String[] colors, HashMap<String, Color> colorMap) {
        if (renderer == null) {
            renderer = new XYLineAndShapeRenderer();
        }
        renderer.setBaseShapesVisible(false);//setShapesVisible(false);
        if (colorMap.size() > 2) {
            for (int x = 0; x < colors.length; x++) {
                if (colors[x].equalsIgnoreCase("#000000")) {
                    renderer.setSeriesPaint(x, Color.BLACK);
                    continue;
                }
                renderer.setSeriesPaint(x, colorMap.get(colors[x]));
            }
        } else {
            renderer.setPaint(Color.BLACK);
            
        }
        return renderer;

    }

    public String selectionUpdate(int[] selection) {
        String chartUrl = "";
        System.out.println("start");
        if (selection != null) {

            XYPlot plot = chart.getXYPlot();
       
            
            XYLineAndShapeRenderer updatedRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
//            updatedRenderer.setBaseSeriesVisible(false, true);
            
            for (int x : indexMarker) {
                updatedRenderer.setSeriesLinesVisible(x, false);
            }
            indexMarker.clear();
            for (int x = 0; x < selection.length; x++) {
                updatedRenderer.setSeriesLinesVisible(selection[x], true);
                indexMarker.add(selection[x]);
            }
        }
        System.out.println("end 1");
        chartUrl = generateChart(chart);
        System.out.println("end 2");
        return chartUrl;
    }

    private String generateChart(JFreeChart chart) {
        String imgurl = imgGenerator.saveToFile(chart, 400, 400, null);

        return imgurl;
    }
    
    /**
     * Creates a line chart (based on an {@link XYDataset}) with default
     * settings.
     *
     * @param title the chart title (<code>null</code> permitted).
     * @param xAxisLabel a label for the X-axis (<code>null</code> permitted).
     * @param yAxisLabel a label for the Y-axis (<code>null</code> permitted).
     * @param dataset the dataset for the chart (<code>null</code> permitted).
     * @param orientation the plot orientation (horizontal or vertical)
     * (<code>null</code> NOT permitted).
     * @param legend a flag specifying whether or not a legend is required.
     * @param tooltips configure chart to generate tool tips?
     * @param urls configure chart to generate URLs?
     *
     * @return The chart.
     */
    private  JFreeChart createXYLineChart(String title,String[] columnIds,
            XYDataset dataset,
            PlotOrientation orientation,
            boolean legend,
            boolean urls) {

        if (orientation == null) {
            throw new IllegalArgumentException("Null 'orientation' argument.");
        }
         Font f = new Font("ARIAL", 1, 7);
//        NumberAxis xAxis = new NumberAxis(xAxisLabel);
//        xAxis.setAutoRangeIncludesZero(false);
         SymbolAxis xAxis = new SymbolAxis("", columnIds);
        xAxis.setAxisLineVisible(false);
        xAxis.setGridBandsVisible(false);
        xAxis.setVerticalTickLabels(true);
        xAxis.setVisible(true);
       
        xAxis.setTickLabelPaint(shadowColor);
        xAxis.setTickLabelFont(f);
        xAxis.setFixedDimension(51.0);

        boolean auto = xAxis.getAutoRangeIncludesZero();
        xAxis.setAutoRangeIncludesZero(true ^ auto);
        xAxis.setTickUnit(new NumberTickUnit(1));
        xAxis.setRange(0, columnIds.length);
//        NumberAxis yAxis = new NumberAxis(yAxisLabel);
         NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRangeIncludesZero(true ^ auto);
        yAxis.setAxisLineVisible(false);
        yAxis.setTickLabelFont(f);
        yAxis.setTickLabelPaint(Color.BLUE);
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setBaseShapesVisible(false);
        renderer.setPaint(shadowColor);
        
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setOrientation(orientation);
        
        plot.setBackgroundPaint(Color.WHITE);
      
        plot.setDomainGridlinePaint(shadowColor);
        plot.setRangeGridlinePaint(shadowColor);
        plot.setOutlinePaint(Color.BLUE);
//        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
       
       
//        plot.setRenderer(renderer);
        plot.setSeriesRenderingOrder(SeriesRenderingOrder.REVERSE);

        
        plot.setDomainAxis(xAxis);
        

       
        
        
       
        if (urls) {
            renderer.setURLGenerator(new StandardXYURLGenerator());
        }

        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
                plot, legend);
        return chart;

    }

}
