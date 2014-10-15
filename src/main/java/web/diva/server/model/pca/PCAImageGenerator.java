/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model.pca;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import javax.swing.border.Border;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.core.visualization.Tools;
import no.uib.jexpress_modularized.core.visualization.charts.DensScatterPlot;
import no.uib.jexpress_modularized.core.visualization.documentation.MetaInfoNode;
import no.uib.jexpress_modularized.pca.computation.PcaResults;
import no.uib.jexpress_modularized.pca.model.ArrayUtils;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartUtilities;
import web.diva.server.model.beans.DivaDataset;

/**
 *
 * @author Yehia Farag
 */
public class PCAImageGenerator {
    //For speeding lookup

    private final DivaDataset divaDataset;
    private boolean antialias = true;
    private PcaPlot plot;
    private no.uib.jexpress_modularized.pca.computation.PcaResults pcaResults;
    private int mode3d = 1; //1=rotate, 2=zoom
    private String title = null;
    private int dotTolerance = 2;//Draw dots within this heat area
    private int dotheatarea = 10;//the area around a dot that is heated
    private int dotsize = 1;  //The size of each dot.
    private boolean paintscale = false;   //Paint the scale?
    private boolean  paintstats = false;   //paint the stats?
    private boolean paintdensities = true;
    private boolean zoom = false;
    private double[] zoomedRect;
    /**
     * The first color in the density map
     */
    private Color color1 = Color.white;
    /**
     * The second color in the density map
     */
    private Color color2 = new Color(200, 200, 250);
    /**
     * The third color in the density map
     */
    private Color color3 = Color.blue;
    /**
     * The fourth color in the density map
     */
    private Color color4 = Color.red;
    /**
     * The fifth color in the density map
     */
    private Color color5 = Color.yellow;
    private int numcolors = 299;
    private int pcax = 0;
    private int pcay = 1;
    private int pcaz = 2;
//    private boolean[] painted;    //If a point in the scatter plot is not selected (by the colorclasses) this index will
    //be false and it will not be sweeped.
//    private double[][][] neurons;   //the nodes in a SOM (if a som is assigned)
    private int clickx = -1;  //If a node has been selected, these are the coordinates in the latice.
    private int clicky = -1;  //and the node at clickx,clicky will be colored yellow
    private boolean shadowUnselected = false;
//    private boolean isneurons = true;
    private int zoomedpca = 0;
    private boolean quadratic = true;
    private boolean showPCA = true;

    public void selectionChanged(int[] sel) {
        boolean[] notShaded = getSelectedIndexes(sel);
        if (shadowUnselected == false) {
            plot.setNotShaded(notShaded);
            plot.forceFullRepaint();
        }
    }

    public boolean isShadowUnselected() {
        return shadowUnselected;
    }

    public void setShadowUnselected(boolean shadowUnselected) {
        this.shadowUnselected = shadowUnselected;
    }

    private void updateSelectionOnDataSet(boolean[] members) {
        //OBS
//        int[] selectedIndices = ArrayUtils.toIntArray(members);
//        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
//        SelectionManager.getSelectionManager().setSelectedRows(points, selection);
    }

    public PCAImageGenerator(PcaResults pcaResults, DivaDataset divaDataset,int pcax,int pcay) {

        this.divaDataset = divaDataset;
        this.pcaResults = pcaResults;
        this.pcax = pcax;
        this.pcay = pcay;
        this.plot = new PcaPlot();
        plot.setMaximumSize(new Dimension(32767, 32767));
        plot.setMinimumSize(new Dimension(12, 4));
        plot.setPreferredSize(new Dimension(220, 212));
//        isneurons = false;
        plot.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 120, 120)));
        plot.setLayout(new java.awt.FlowLayout(0, 5, 1));
        plot.setSize(220,212);
        plot.dotsize = 2;
        updatePlot(null);
       }

   
    
    /**
     * If this is a som representation, light up the neuron at point p.
     *
     * @param p The neuron to be lit.
     */
    public void setSelected(java.awt.Point p) {
        plot.setHighLightedNeuron(p);

    }
//
//    public void setframeType(int frameType) {
//        plot.setframeType(frameType);
//    }

//    public void setFrameBG(Color color) {
//        plot.setFrameBG(color);
//    }

    public void forceFullRepaint() {
        plot.forceFullRepaint();
    }
    
    public void zoom(boolean zoom, int[] indexes){
        if(zoom){
            updatePlot(indexes);
        
        }
        else{
            
            updatePlot(null);
        }
    
    }

//    public boolean isHex() {
//        return plot.isHex();
//    }
//
//    public void setHex(boolean hex) {
//        plot.hex = hex;
//    }

//    public Axis getXaxis() {
//        return plot.xaxis;
//    }
//
//    public Axis getYaxis() {
//        return plot.yaxis;
//    }
//
//    public boolean[] getTolerated() {
//        return plot.getTolerated();
//    }

    public Vector getIndexesAtPoint(Point point, int radius) {
        return plot.getIndexesAtPoint(point, radius);
    }

    public boolean isZoompca() {
        return plot.zoompca;
    }

    public boolean isPaintNamesonClick() {

        return plot.paintNamesonClick;
    }

    public boolean[] getFramedIndexes() {
        return plot.getFramedIndexes();
    }

    public double[] getZoomedArea() {
        return plot.getZoomedArea();
    }

    public void setSpotNames(String[][] SpotNames) {
        plot.setSpotNames(SpotNames);
    }

//    @Override
    public void setBorder(Border border) {
        plot.setBorder(border);

    }

//    @Override
    public void setLayout(LayoutManager loutManager) {
        plot.setLayout(loutManager);
    }

//    @Override
//    public void addMouseMotionListener(MouseMotionListener mml) {
//        plot.addMouseMotionListener(mml);
//    }
//
//    @Override
//    public void addMouseListener(MouseListener ml) {
//        plot.addMouseListener(ml);
//    }

    public void setNotShaded(boolean[] notShaded) {
        plot.setNotShaded(notShaded);
    }

    /**
     * @param sel selection indexes
     * @return an array where the selected indexes are flagged as true. If there
     * are no indexes that are currently selected, or if the current Selection
     * from the SelectionManager is null, then an array of only false values are
     * returned.
     */
    private boolean[] getSelectedIndexes(int[] sel) {    
            if (sel == null) {
                return new boolean[divaDataset.getDataLength()];
            }
            boolean[] ret = ArrayUtils.toBooleanArray(divaDataset.getDataLength(), sel);
            return ret;
        
    }

    private double[][] points;

    public double[][] getPoints() {
        return points;
    }
    private void updatePlot(int[] selection) {
        if (pcaResults == null) {
            return;
        }
        if(selection == null){
            points = new double[2][(int) pcaResults.nrPoints()];
            for (int i = 0; i < pcaResults.nrPoints(); i++) {
            points[0][i] = pcaResults.ElementAt(i, pcax);
            points[1][i] = pcaResults.ElementAt(i, pcay);
           
        }
        }
        else{
            for (int i = 0; i < selection.length; i++) {
            points[0][i] = pcaResults.ElementAt(selection[i], pcax);
            points[1][i] = pcaResults.ElementAt(selection[i], pcay);
           
        }
        }
        
        plot.data = divaDataset;
        if (zoom) {
            plot.setPropsAndData(points[0], points[1], zoomedRect);
        } else {
            plot.setPropsAndData(points[0], points[1]);
        }
        plot.setXaxisTitle("Principal Component " + (pcax + 1));
        plot.setYaxisTitle("Principal Component" + (pcay + 1));
        plot.FullRepaint = true;
        plot.repaint();
      
        
      
        
        
    }
    
   public String toImage(){
        BufferedImage image = (BufferedImage)plot.getImage();
        byte[] imageData = null;
        try{
        imageData = ChartUtilities.encodeAsPNG(image);
        }catch(Exception e){e.printStackTrace();}
        String base64 = Base64.encodeBase64String(imageData);
        base64 = "data:image/png;base64," + base64;
        return base64;

    }

//    /**
//     * Sweep the points within a square created by mouse dragging
//     */
    private void sweep(boolean[] members) {
        System.out.println("PCAComponent: sweep()");
        updateSelectionOnDataSet(members);
        Tools t = new Tools();
        List<Integer> v = new ArrayList<Integer>();
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < members.length; i++) {
            if (members[i]) {
                b.append(i);
                b.append(",");
                v.add(i);
            }
        }

        if (b.length() > 1) {
            b.setLength(b.length() - 1);
        }

        MetaInfoNode metainfonode = new MetaInfoNode(MetaInfoNode.pca);

        if (v.size() > 0) {

            metainfonode.put("Plot_Size", plot.getPlotSize().width + "," + plot.getPlotSize().height);
            metainfonode.put("X-Component", String.valueOf(pcax + 1));
            metainfonode.put("Y-Component", String.valueOf(pcay + 1));

            Group Class = null;
            boolean activeSet = false;
            StringBuilder visibleGroups = new StringBuilder();
            if (divaDataset.getRowGroups().size() > 1) {
                for (int j = 0; j < divaDataset.getRowGroups().size(); j++) {
                    Class = (Group) divaDataset.getRowGroups().get(j);
                    activeSet = Class.isActive();//(Boolean)Class.elementAt(0);
                    if (activeSet) {
                        visibleGroups.append(Class.getName() + ",");
                    }
                }
                visibleGroups.setLength(visibleGroups.length() - 1);
                metainfonode.put("Groups_Visible", visibleGroups.toString());
            }

            metainfonode.put("Frame", plot.getFrameDescription());

            metainfonode.put("Row indices", b.toString());


            int i = ((Integer) plot.Layout.get("SbgSV")).intValue();
            int colors = ((Integer) plot.Layout.get("colorsSV")).intValue();
            int threshold = ((Integer) plot.Layout.get("tresholdSV")).intValue();

            if (i == 0) {
                metainfonode.put("Density Map", "On");
                metainfonode.put("Density Map Colors", String.valueOf(colors));
                metainfonode.put("Density Map Threshold", String.valueOf(threshold));
            } else {
                metainfonode.put("Density Map", "Off");
            }
        }
    }

//    private String getMeta() {
//
//
//        String meta = "";
//        if (plot.getframeType() == 0) {
//        } else if (plot.getframeType() == 1) {
//        }
//
//        meta += "Plot_Size_: (" + plot.getPlotSize().width + "," + plot.getPlotSize().height + ")\n";
//        meta += "X-Component: " + (pcax + 1) + "\n";
//        meta += "Y-Component: " + (pcay + 1) + "\n";
//
//        Group group = null;
//        boolean activeSet = false;
//
//        if (divaDataset.getRowGroups().size() > 1) {
//            meta += "Groups Visible: \n";
//
//            for (int j = 0; j < divaDataset.getRowGroups().size(); j++) {
//
//                group = (Group) divaDataset.getRowGroups().get(j);
//                activeSet = group.isActive();
//
//                if (activeSet) {
//                    meta += group.getName() + " (n=" + group.size() + ")\n";
//                }
//            }
//            meta += "Note: Members of theese groups are not known.\n";
//
//        }
//
//        meta += "Colors: " + numcolors + "\n";
//        meta += "Dot plot density threshold: " + dotTolerance;
//
//        return meta;
//    }

//    @Override
//    public void finalize() {
//        //This is just used for garbage collecting monitoring
//        try {
//            super.finalize();
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

//    public void ColumnSelectionHasChanged(Object source) {
//    }

    public void zoomout() {
        plot.zoomout();
    }

    public void setZoomPca(boolean zoom) {
        plot.zoompca = zoom;
    }

    public void setPaintNamesonClick(boolean paint) {
        plot.paintNamesonClick = paint;
    }

    

    public int getPcax() {
        return pcax;
    }

    public void setPcax(int pcax) {
        this.pcax = pcax;
    }

    public int getPcay() {
        return pcay;
    }

    public void setPcay(int pcay) {
        this.pcay = pcay;
    }

    public int getPcaz() {
        return pcaz;
    }

    public void setPcaz(int pcaz) {
        this.pcaz = pcaz;
    }

    public no.uib.jexpress_modularized.core.visualization.charts.DensScatterPlot getPlot() {
        return plot;
    }
    
    
    

//    public no.uib.jexpress_modularized.pca.computation.PcaResults getPcaResults() {
//        return pcaResults;
//    }
    
    
//    public void updatePCAPlotSeklection(boolean zoom,Point point){
//    plot.setZoompca(zoom);
//    if (!plot.isZoompca()) {
//            if (plot.getFramedIndexes() != null) {
//                sweep(plot.getFramedIndexes());
//            }
//            plot.repaint();
//
//        } else if (plot.isPaintNamesonClick()) {
//            Vector sel = new Vector();
//            boolean[] bl = plot.getFramedIndexes();
//
//            for (int i = 0; i < bl.length; i++) {
//                if (bl[i]) {
//                    sel.addElement(new Integer(i));
//                }
//            }
//            if (sel.isEmpty()) {
//                sel = plot.getIndexesAtPoint(point, plot.dotsize);
//            }
//
//            if (sel.isEmpty()) {
//                return;
//            }
//
//            if (SpotNames == null) {
//                SpotNames = new String[plot.getDataSet().getDataLength()][];
//                plot.getPlot().setSpotNames(SpotNames);
//            }
//
//            int cnt = 0;
//            boolean[] b = plot.getDataSet().getusedInfos();
//            for (int i = 1; i < b.length - 1; i++) {
//                if (b[i]) {
//                    cnt++;
//                }
//            }
//            int cnt2 = 0;
//
//            String s = null;
//            for (int i = 0; i < sel.size(); i++) {
//
//                if (SpotNames[((Integer) sel.elementAt(i)).intValue()] != null) {
//                    SpotNames[((Integer) sel.elementAt(i)).intValue()] = null;
//                } else {
//
//
//                    SpotNames[((Integer) sel.elementAt(i)).intValue()] = new String[cnt];
//                    cnt2 = 0;
//                    for (int j = 1; j < b.length - 1; j++) {
//
//                        if (b[j]) {
//                            s = plot.getDataSet().getInfos()[((Integer) sel.elementAt(i)).intValue()][j - 1];
//                            SpotNames[((Integer) sel.elementAt(i)).intValue()][cnt2] = s;
//                            cnt2++;
//                        }
//                    }
//
//                }
//            }
//
//            plot.getPlot().forceFullRepaint();
//
//        }
//    
//    
//    }
    private Point sweepFrom,sweepTo;
     public boolean[] getFramedIndexes( int startx,int starty,int endx,int endy) {

        boolean[] ret = new boolean[plot.getXValues().length];

        boolean[] tolerated = plot.getBackgroundFactory().getTolerated();

        if (plot.getframeType() == 0) {
            int sqxstart = Math.min(startx, endx);
            int sqystart = Math.min(starty, endy);
            int sqxend = (Math.max(startx, endx) - Math.min(startx, endx));
            int sqyend = (Math.max(starty, endy) - Math.min(starty, endy));
            sweepFrom = new Point(sqxstart, sqystart);
            sweepTo = new Point(sqxend, sqyend);
        }
//        System.out.println(plot.getXValues().length);
        for(int z=0;z<plot.getXValues().length;z++){
            int x = plot.getXValues()[z];
            int y = plot.getYValues()[z];
//            System.out.println("on position "+z+"  x = "+x+"  and y = "+y);
        
        }
//        for (int i = 0; i < ret.length; i++) {
//
//            //System.out.print("\n*");
//
//            for (int j = 0; j < paths.size(); j++) //if( (dotColors!=null && dotColors[i]!=null) && (tolerated==null || tolerated[i]) && ((Shape)paths.elementAt(j)).contains(Nx[i], Ny[i])){
//            {
//                if ((tolerated == null || tolerated[i]) && ((Shape) paths.elementAt(j)).contains(Nx[i], Ny[i])) {
//                    if (visible == null || visible[i]) {
//                        ret[i] = true;
//                    }
//
//                }
//            }
//
//        }

        return ret;
    }
     
     public int[] getPCASelection(int startX, int startY, int endX, int endY) {
         
         int maxXM = Math.max(startX, endX);
         int minXM = Math.min(startX, endX);
         int maxYM = Math.max(startY, endY);
         int minYM = Math.min(startY, endY);

         int plotWidthArea = plot.Width() - plot.left - plot.right;
         int plotHeightArea = plot.getHeight() - plot.top - plot.bottom;
        if((minXM<plot.left && maxXM < plot.left)||(minXM>(plot.left+plotWidthArea))){
            return  null;
        }
        if((minYM<plot.top && maxXM < plot.left)||(minYM>plot.top+plotHeightArea)){
            return  null;
        }
        
         minXM-=plot.left;
         maxXM-=plot.left;
         minYM-=plot.top;
         maxYM-=plot.top;
        
        if((minXM<0 && maxXM >= 0))
             minXM =0;// plot.left;
        if(maxXM > plotWidthArea && minXM>= 0)
              maxXM = plotWidthArea;
        if((minYM<=0 && maxYM > 0))//plot.top))
             minYM = 0;//plot.top;
        if(maxYM >plotHeightArea&& minYM>= 0)
              maxXM = plotHeightArea;
        
        
          double xDataArea = plot.xaxis.maximum - plot.xaxis.minimum;
         double xUnitPix = xDataArea/(double) plotWidthArea ;
         double modStartX = (minXM * xUnitPix) + plot.xaxis.minimum;//xstart units from min         
         double modEndX = (maxXM * xUnitPix) + plot.xaxis.minimum;

         
         double yDataArea = plot.yaxis.maximum - plot.yaxis.minimum;
         double yUnitPix =  yDataArea/(double) plotHeightArea ;
         double modStartY = plot.yaxis.maximum - (maxYM * yUnitPix);
         double modEndY = plot.yaxis.maximum - (minYM * yUnitPix);
          
          HashSet<Integer> selectedPoints = new HashSet<Integer>();
        for(int x=0;x<points[0].length;x++){ 
            double pointX = points[0][x];
            double pointY = points[1][x];
            if (pointX >= modStartX && pointX<= modEndX && pointY >= modStartY && pointY<= modEndY) {
                selectedPoints.add(x);
            }
        
        }if (selectedPoints.size() > 0) {

            Integer[] selectedIndexes = new Integer[selectedPoints.size()];
            System.arraycopy(selectedPoints.toArray(), 0, selectedIndexes, 0, selectedIndexes.length);

            int[] arr = new int[selectedIndexes.length];
            arr = org.apache.commons.lang3.ArrayUtils.toPrimitive(selectedIndexes, selectedIndexes.length);
            return arr;
        }
        
        
        return null;
    }
    
}
