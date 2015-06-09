/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import java.awt.Color;
import java.io.IOException;
import net.iharder.Base64;
import no.uib.jsparklines.renderers.util.BarChartColorRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.encoders.ImageEncoder;
import org.jfree.chart.encoders.ImageEncoderFactory;
import org.jfree.chart.encoders.ImageFormat;

/**
 *
 * @author y-mok_000
 */
public class SparkLineGenerator {
     /**
     * The chart to display.
     */
    private JFreeChart chart;
    /**
     * The minimum value to display as a chart. Values smaller than this lower
     * limit are shown as this minimum value when shown as a chart. This to make
     * sure that the chart is visible at all.
     */
    private double minimumChartValue = 0.05;
    
    
     /**
     * The colors to use for the bars with negative numbers.
     */
    private Color negativeValuesColor = new Color(51, 51, 255);
    /**
     * The colors to use for the bars with positive numbers.
     */
    private Color positiveValuesColor = new Color(255, 51, 51);
    /**
     * The color to use for the non-significant values.
     */
    private Color nonSignificantColor = Color.GRAY;
    /**
     * The upper level for when to use the significant values color.
     */
    private double significanceLevel = 1;
    /**
     * The maximum value. Used to set the maximum range for the chart.
     */
    private double maxValue = 1;
    /**
     * The minimum value. Used to set the minimum range for the chart.
     */
    private double minValue = 0;
    
    private double rangeFactor=1;
    
       

    private final Color plotBackgroundColor = new Color(240, 240, 240, 0);
    private final ImageEncoder in = ImageEncoderFactory.newInstance(ImageFormat.PNG, new Float(0.084666f));
    
    
    
    public SparkLineGenerator(PlotOrientation plotOrientation, double minValue, double maxValue,
            Color negativeValuesColor, Color positiveValuesColor){
       
        this(plotOrientation, minValue, maxValue, negativeValuesColor, positiveValuesColor, Color.GRAY, 1);
    
    }
    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor
     * when positive and negative values are to be plotted. Note that to use the
     * significance color coding the object in the table cell has to be of type
     * XYDataPoint.
     *
     * @param plotOrientation the orientation of the plot
     * @param minValue the minium value to be plotted, used to make sure that
     * all plots in the same column has the same minimum value and are thus
     * comparable
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable
     * @param negativeValuesColor the color to use for the negative values if
     * two sided data is shown
     * @param positiveValuesColor the color to use for the positive values if
     * two sided data is shown, and the color used for one sided data
     * @param nonSignificantColor the color to use for the non-significant
     * values
     * @param significanceLevel the upper level for when to use the significant
     * values color
     * @throws IllegalArgumentException if minValue &gt; maxValue
     */
    public SparkLineGenerator(
            PlotOrientation plotOrientation, double minValue, double maxValue,
            Color negativeValuesColor, Color positiveValuesColor,
            Color nonSignificantColor, double significanceLevel) {
//         System.out.println("the 2 line mode ");

        this.negativeValuesColor = negativeValuesColor;
        this.positiveValuesColor = positiveValuesColor;
        rangeFactor = Math.max(Math.abs(maxValue), Math.abs(minValue));
        this.maxValue = maxValue/rangeFactor;
        this.minValue = minValue/rangeFactor;
        
        this.nonSignificantColor = nonSignificantColor;
        this.significanceLevel = significanceLevel;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        chart = ChartFactory.createBarChart(null, null, null, dataset, plotOrientation, false, false, false);
//        this.chartPanel = new ChartPanel(chart);
        plot = chart.getCategoryPlot();

        plot.getRangeAxis().setRange(this.minValue, this.maxValue);

        // hide unwanted chart details
        plot.setOutlineVisible(false);
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.setRangeGridlinesVisible(false);

        plot.setBackgroundAlpha(0.0f);
        plot.setBackgroundImageAlpha(0.0f);
        plot.setBackgroundPaint(plotBackgroundColor);

        chart.setBackgroundImageAlpha(0.0f);
        chart.setBackgroundPaint(plotBackgroundColor);



        if (minValue > maxValue) {
            throw new IllegalArgumentException("minValue has to be smaller than maxValue! Current values: minValue: " + minValue + ", maxValue: " + maxValue + ".");
        }
    }
    private CategoryPlot plot ;
    
    /**
     * Creates a new JSparklinesBarChartTableCellRenderer. Use this constructor
     * when only positive values are to be plotted.
     *
     * @param plotOrientation the orientation of the plot
     * @param maxValue the maximum value to be plotted, used to make sure that
     * all plots in the same column has the same maximum value and are thus
     * comparable (this is the same as setting the minimum value to 0)
     * @param positiveValuesColor the color to use for the positive values if
     * two sided data is shown, and the color used for one sided data
     * @throws IllegalArgumentException if maxValue &lt; 0.0
     */
    public SparkLineGenerator(PlotOrientation plotOrientation, double maxValue, Color positiveValuesColor) {
        this(plotOrientation, 0.0, maxValue, null, positiveValuesColor);
    }
    

    @SuppressWarnings("CallToPrintStackTrace")
    public synchronized  String getSparkLine( double value) {

        
        // create the bar chart
        DefaultCategoryDataset plotdataset = new DefaultCategoryDataset();

//        if (value < minimumChartValue && value > 0) {
//            value = minimumChartValue;
//        }
        value = value/rangeFactor;
        plotdataset.addValue(value, "1", "1");
        

        // fine tune the chart properites

        // add the plotdataset
        plot.setDataset(plotdataset);

   
        // set up the chart renderer
        CategoryItemRenderer renderer = null;


        

            if (value >= 0) {
                renderer = new BarChartColorRenderer(positiveValuesColor);
            } else {
                renderer = new BarChartColorRenderer(negativeValuesColor);
            }

      

        plot.setRenderer(renderer);
        
        try {            
                  

      
            return "data:image/png;base64," + Base64.encodeBytes(in.encode(chart.createBufferedImage(150, 30)));
         
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
//            Base64.en
           
        }

        return "";

    }

     /**
     * Returns the minimum chart value to plot.
     *
     * @return the minimumChartValue
     */
    public double getMinimumChartValue() {
        return minimumChartValue;
    }

    /**
     * Set the minimum chart value to plot.
     *
     * @param minimumChartValue the minimumChartValue to set
     */
    public void setMinimumChartValue(double minimumChartValue) {
        this.minimumChartValue = minimumChartValue;
    }
    
    
    
}
