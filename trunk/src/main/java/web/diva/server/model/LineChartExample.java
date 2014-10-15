/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;
/**
 *
 * The MIT License
 *
 * Copyright (c) 2008 the original author or authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import static com.googlecode.charts4j.Color.*;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.DataUtil;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Line;
import com.googlecode.charts4j.LineChart;
import com.googlecode.charts4j.LineStyle;
import com.googlecode.charts4j.LinearGradientFill;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.Shape;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;


/**
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 */
public class LineChartExample {

    
   
    public String example1() {

        // EXAMPLE CODE START

        // Defining lines
        final int NUM_POINTS = 25;
        final double[] competition = new double[NUM_POINTS];
        final double[] mywebsite = new double[NUM_POINTS];
        for (int i = 0; i < NUM_POINTS; i++) {
            competition[i] = 100-(Math.cos(30*i*Math.PI/180)*10 + 50)*i/20;
            mywebsite[i] = (Math.cos(30*i*Math.PI/180)*10 + 50)*i/20;
        }
        List<Line> plots = new ArrayList<Line>();
        for(int x = 0; x<25;x++){
         Line line1 = Plots.newLine(Data.newData(new double[]{x,x+1,x*2,x*3}), Color.GRAY, "My Website.com");
        line1.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
//        line1.addShapeMarkers(null, Color.GRAY, 12);
        plots.add(line1);
        
        }
       
        Line line2 = Plots.newLine(Data.newData(competition), SKYBLUE, "Competition.com");
        line2.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
        line2.addShapeMarkers(Shape.DIAMOND, SKYBLUE, 12);
        line2.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 8);


        // Defining chart.
        LineChart chart = GCharts.newLineChart(plots);
        chart.setSize(600, 450);
        chart.setTitle("Web Traffic|(in billions of hits)", WHITE, 14);
        chart.addHorizontalRangeMarker(40, 60, Color.newColor(RED, 30));
        chart.addVerticalRangeMarker(70, 90, Color.newColor(GREEN, 30));
        chart.setGrid(25, 25, 3, 2);

        // Defining axis info and styles
        AxisStyle axisStyle = AxisStyle.newAxisStyle(WHITE, 12, AxisTextAlignment.CENTER);
        AxisLabels xAxis = AxisLabelsFactory.newAxisLabels("Nov", "Dec", "Jan", "Feb", "Mar");
        xAxis.setAxisStyle(axisStyle);
        AxisLabels xAxis2 = AxisLabelsFactory.newAxisLabels("2007", "2007", "2008", "2008", "2008");
        xAxis2.setAxisStyle(axisStyle);
        AxisLabels yAxis = AxisLabelsFactory.newAxisLabels("", "25", "50", "75", "100");
        AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("Month", 50.0);
        xAxis3.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 14, AxisTextAlignment.CENTER));
        yAxis.setAxisStyle(axisStyle);
        AxisLabels yAxis2 = AxisLabelsFactory.newAxisLabels("Hits", 50.0);
        yAxis2.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 14, AxisTextAlignment.CENTER));
        yAxis2.setAxisStyle(axisStyle);

        // Adding axis info to chart.
        chart.addXAxisLabels(xAxis);
        chart.addXAxisLabels(xAxis2);
        chart.addXAxisLabels(xAxis3);
        chart.addYAxisLabels(yAxis);
        chart.addYAxisLabels(yAxis2);

        // Defining background and chart fills.
        chart.setBackgroundFill(Fills.newSolidFill(Color.newColor("1F1D1D")));
        LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.newColor("363433"), 100);
        fill.addColorAndOffset(Color.newColor("2E2B2A"), 0);
        chart.setAreaFill(fill);
        String url = chart.toURLString();
        // EXAMPLE CODE END. Use this url string in your web or
        // Internet application.
        return url;
    }

    public String example2() {
        // EXAMPLE CODE START
        // Defining Line
        final double[] sp500 = { 62.960, 74.560, 84.300, 92.200, 95.890, 103.800, 91.600, 92.270, 96.870, 116.930, 97.540, 67.160, 89.770, 106.880, 94.750, 96.280, 107.840, 135.330, 122.300, 140.340, 164.860, 166.260, 210.680, 243.370,
                247.840, 277.080, 350.680, 328.710, 415.140, 438.820, 468.660, 460.920, 614.120, 753.850, 970.840, 1231.93, 1464.47, 1334.22, 1161.02, 879.390, 1109.64, 1213.55, 1258.17, 1424.71, 1475.25 };
        final double INFLATION = 0.035;

        final double[] inflation = new double[sp500.length];
        inflation[0] = sp500[0];
        for (int i = 1; i < inflation.length; i++) {
            inflation[i] = inflation[i-1] *INFLATION + inflation[i-1];
        }

        Line line1 = Plots.newLine(DataUtil.scaleWithinRange(0,1500,sp500), YELLOW, "S & P 500");
        line1.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
        line1.addShapeMarkers(Shape.CIRCLE, YELLOW, 10);
        line1.addShapeMarkers(Shape.CIRCLE, BLACK, 7);
        line1.addShapeMarker(Shape.VERTICAL_LINE_PARTIAL, BLUE,3,8);
        line1.addShapeMarker(Shape.VERTICAL_LINE_PARTIAL, BLUE,3,17);
        line1.addShapeMarker(Shape.VERTICAL_LINE_PARTIAL, BLUE,3,24);
        line1.addShapeMarker(Shape.VERTICAL_LINE_PARTIAL, BLUE,3,40);
        line1.setFillAreaColor(LIGHTYELLOW);

        Line line2 = Plots.newLine(DataUtil.scaleWithinRange(0,1500,inflation), LIMEGREEN, "Inflation");
        line2.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
        line2.addShapeMarkers(Shape.CIRCLE, LIME, 10);
        line2.addShapeMarkers(Shape.CIRCLE, BLACK, 7);
        line2.setFillAreaColor(LIGHTGREEN);


        // Defining chart.
        LineChart chart = GCharts.newLineChart(line1,line2);
        chart.setSize(600, 450);
        chart.setTitle("S & P 500|1962 - 2008", WHITE, 14);

        // Defining axis info and styles
        AxisStyle axisStyle = AxisStyle.newAxisStyle(WHITE, 12, AxisTextAlignment.CENTER);
        AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(0, sp500[sp500.length-1]);
        yAxis.setAxisStyle(axisStyle);
        AxisLabels xAxis1 = AxisLabelsFactory.newAxisLabels(Arrays.asList("Fed Chiefs:","Burns","Volcker","Greenspan","Bernanke"), Arrays.asList(5,18,39,55,92));
        xAxis1.setAxisStyle(axisStyle);
        AxisLabels xAxis2 = AxisLabelsFactory.newNumericRangeAxisLabels(1962, 2008);
        xAxis2.setAxisStyle(axisStyle);
        AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("Year", 50.0);
        xAxis3.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 14, AxisTextAlignment.CENTER));

        // Adding axis info to chart.
        chart.addYAxisLabels(yAxis);
        chart.addXAxisLabels(xAxis1);
        chart.addXAxisLabels(xAxis2);
        chart.addXAxisLabels(xAxis3);
        chart.setGrid(100, 6.78, 5, 0);

        // Defining background and chart fills.
        chart.setBackgroundFill(Fills.newSolidFill(BLACK));
        chart.setAreaFill(Fills.newSolidFill(Color.newColor("708090")));
        String url = chart.toURLString();
        // EXAMPLE CODE END. Use this url string in your web or
        // Internet application.
         return url;
       }

}