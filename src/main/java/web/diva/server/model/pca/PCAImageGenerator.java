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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.border.Border;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.pca.computation.PcaResults;
import no.uib.jexpress_modularized.pca.model.ArrayUtils;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartUtilities;
import web.diva.server.model.beans.DivaDataset;
import web.diva.shared.beans.UpdatedTooltip;

/**
 *
 * @author Yehia Farag
 */
public class PCAImageGenerator implements Serializable{
    //For speeding lookup

    private final DivaDataset divaDataset;
////    private DivaDataset subZoomDataset;
    private boolean antialias = true;
    private PcaPlot plot;
    private final no.uib.jexpress_modularized.pca.computation.PcaResults pcaResults;
    private int mode3d = 1; //1=rotate, 2=zoom
    private String title = null;
    private int dotTolerance = 2;//Draw dots within this heat area
    private int dotheatarea = 10;//the area around a dot that is heated
    private int dotsize = 1;  //The size of each dot.
    private boolean paintscale = false;   //Paint the scale?
    private boolean  paintstats = false;   //paint the stats?
    private boolean paintdensities = true;
    private boolean zoom = false;
    private double[] zoomedRect = new double[4];
    private UpdatedTooltip tooltips;
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
    
    private int[] notshadIndex ;
    
    
    public boolean[] zoomedSelectionChange(int[] sel){
    ArrayList<Integer> reIndexSel = new ArrayList<Integer>();
            for (int x : sel) {
                if (indexToZoomed[x]!= -100) {
                    reIndexSel.add(indexToZoomed[x]);
                }
            }
            int[] zoomSel = new int[reIndexSel.size()];
            for(int x=0;x<zoomSel.length;x++){
                zoomSel[x]= reIndexSel.get(x);
            }
            boolean[] notShaded = getSelectedIndexes(zoomSel);
            return notShaded;
    
    }

    public void selectionChanged(int[] sel) {
//        if (zoom) {
//            ArrayList<Integer> reIndexSel = new ArrayList<Integer>();
//            for (int x : sel) {
//                if (indexToZoomed[x]!= -100) {
//                    reIndexSel.add(indexToZoomed[x]);
//                }
//            }
//            int[] zoomSel = new int[reIndexSel.size()];
//            for(int x=0;x<zoomSel.length;x++){
//                zoomSel[x]= reIndexSel.get(x);
//            }
//            boolean[] notShaded = getSelectedIndexes(zoomSel);
//          
////            plot.setNotShaded(notShaded);
//
//        } 
//        else {
            notshadIndex = sel;
            boolean[] notShaded = getSelectedIndexes(sel);
            plot.setNotShaded(notShaded);
            
//        }
        if (shadowUnselected == false) {                
                plot.forceFullRepaint();
            }
    }

    public boolean isShadowUnselected() {
        return shadowUnselected;
    }

    public void setShadowUnselected(boolean shadowUnselected) {
        this.shadowUnselected = shadowUnselected;
    }

//    private void updateSelectionOnDataSet(boolean[] members) {
//        //OBS
////        int[] selectedIndices = ArrayUtils.toIntArray(members);
////        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
////        SelectionManager.getSelectionManager().setSelectedRows(points, selection);
//    }

    public PCAImageGenerator(PcaResults pcaResults, DivaDataset divaDataset,int pcax,int pcay) {

        this.divaDataset = divaDataset;
        this.pcaResults = pcaResults;
        this.pcax = pcax;
        this.pcay = pcay;
        this.plot = new PcaPlot();
        plot.setMaximumSize(new Dimension(32767, 32767));
        plot.setMinimumSize(new Dimension(700,400));
        plot.setPreferredSize(new Dimension(700,400));
//        isneurons = false;
        plot.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 120, 120)));
        plot.setLayout(new java.awt.FlowLayout(0, 5, 1));
        plot.setSize(700,400);
//        plot.dotsize = 5;
        indexToZoomed = new int[divaDataset.getDataLength()];
       
//        
        updatePlot();
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
        plot.FullRepaint = true;
        plot.repaint();
        plot.forceFullRepaint();
    }

//    public void zoomIn(int startX, int startY, int endX, int endY) {
//        int[] indexes = getPCASelection(startX, startY, endX, endY);
//
//        if (indexes != null && indexes.length > 2) {
////            zoom = true;
//            zoomDataset(indexes);
//            updatePlot(indexes);
//            if(notshadIndex !=null)
//                selectionChanged(notshadIndex);
//
//        } else {
//            indexes = getPCASelection((startX - 5), (startY - 5), (endX + 5), (endY + 5));
//            if (indexes != null && indexes.length > 2) {
//
////                zoom = true;
//                zoomDataset(indexes);
//                updatePlot(indexes);
//                if(notshadIndex !=null)
//                    selectionChanged(notshadIndex);
//            }
//        }
//
//    }

//    private void zoomDataset(int[] selection) {
//        double[][] subData = new double[selection.length][divaDataset.getDataWidth()];
//        String[] names = new String[selection.length];
//
//        for (int x = 0; x < selection.length; x++) {
//            subData[x] = divaDataset.getData()[selection[x]];
//            names[x] = divaDataset.getRowIds()[selection[x]];
//        }
//
//        this.subZoomDataset = new DivaDataset(subData, names, divaDataset.getColumnIds());
//        for (Group g : divaDataset.getRowGroups()) {
//            ArrayList<Integer> ind = new ArrayList<Integer>();
//            for (int x : selection) {
//                if (g.getIndices().contains(x)) {
//                    ind.add(x);
//
//                }
//
//            }
//////            if (ind.size() > 0) {
//                int[] indexes = new int[ind.size()];
//                for (int x = 0; x < ind.size(); x++) {
//                    indexes[x] = ind.get(x);
//                }
//                Group tg = new Group(g.getName(), g.getColor(), Selection.TYPE.OF_ROWS, indexes);
//                tg.setActive(g.isActive());
//                subZoomDataset.addRowGroup(tg);
////            }
//
//        }
//
//    }
     public void zoomOut(){       
            zoom = false;
            updatePlot();
        
    
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
//        if(zoom)
//        {
//             if (sel == null) {
//                return new boolean[subZoomDataset.getDataLength()];
//            }
//            boolean[] ret = ArrayUtils.toBooleanArray(subZoomDataset.getDataLength(), sel);
//            return ret;
//        }else{
            if (sel == null) {
                return new boolean[divaDataset.getDataLength()];
            }
            boolean[] ret = ArrayUtils.toBooleanArray(divaDataset.getDataLength(), sel);
            return ret;
//        }
        
    }

    private double[][] points;
//    private double[][] subZoomPoints;
    private int[] zoomedToNormalIndex ;
    private final int[] indexToZoomed ;
    private String[] rowIds;

    public double[][] getPoints() {
        return points;
    }
    private void updatePlot() {
        if (pcaResults == null) {
            return;
        }

            points = new double[2][(int) pcaResults.nrPoints()];
            for (int i = 0; i < pcaResults.nrPoints(); i++) {
                points[0][i] = pcaResults.ElementAt(i, pcax);
                points[1][i] = pcaResults.ElementAt(i, pcay);
               
            } 
            plot.data = divaDataset;
            rowIds = divaDataset.getRowIds();
            if(zoom)
                plot.setPropsAndData(points[0], points[1],zoomedRect);
            else
                plot.setPropsAndData(points[0], points[1]);

        
        plot.setXaxisTitle("Principal Component " + (pcax + 1));
        plot.setYaxisTitle("Principal Component" + (pcay + 1));
        plot.FullRepaint = true;        
        plot.repaint();
        plot.forceFullRepaint();
      
        
      
        
        
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


//    public void zoomout() {
//        subZoomDataset = null;
//        updatePlot(null);
//    }

    public void setZoomPca(boolean zoom) {
        plot.zoompca = zoom;
    }
     public void setZoom(boolean zoom,int startX, int startY, int endX, int endY) {
        this.zoom = zoom;        
        this.zoomedRect = this.getSelectionRecatangle(startX, startY, endX, endY);
        this.updatePlot();
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
    public UpdatedTooltip getTooltipsInformationData() {
//        if(tooltips !=null){
//            return tooltips;
//        }
//        else if(zoom){
//        UpdatedTooltip zoomTooltips = new UpdatedTooltip();
//         int yAxisFactor = plot.left- plot.yaxis.predictWidth();
//         int xAxisFactor = plot.bottom - plot.xaxis.predictWidth();
//        int plotWidthArea = plot.Width() - (plot.left) - plot.right+yAxisFactor;
//        int plotHeightArea = plot.getHeight() - plot.top - plot.bottom +xAxisFactor;
//        zoomTooltips.setPlotWidth(plot.getWidth());
//        zoomTooltips.setPlotHeight(plot.getHeight());
//        zoomTooltips.setPlotHeightArea(plotHeightArea);
//        zoomTooltips.setPlotWidthArea(plotWidthArea);
//        zoomTooltips.setPlotRight(plot.right);
//        zoomTooltips.setPlotTop(plot.top);
//        zoomTooltips.setPlotBottom(plot.bottom);
//        zoomTooltips.setPlotLeft(plot.left);
//        zoomTooltips.setyAxisFactor(yAxisFactor);
//        zoomTooltips.setxAxisFactor(xAxisFactor);
//        double xDataArea = plot.xaxis.getDataMax() - plot.xaxis.getDataMin();
//        double xUnitPix = xDataArea / (double) plotWidthArea;
//        zoomTooltips.setxUnitPix(xUnitPix);
//        double yDataArea = plot.yaxis.getDataMax() - plot.yaxis.getDataMin();
//        double yUnitPix = yDataArea / (double) plotHeightArea;
//        zoomTooltips.setyUnitPix(yUnitPix);
//        zoomTooltips.setMinX(plot.xaxis.getDataMin());
//        zoomTooltips.setMaxY(plot.yaxis.getDataMax());
////        if(zoom)
//            zoomTooltips.setPoints(subZoomPoints);
//////        else
//////            zoomTooltips.setPoints(points);
//        zoomTooltips.setRowIds(rowIds); 
//        
//        printPlotData(plotHeightArea, plotWidthArea, xAxisFactor, yAxisFactor);
//        //System.out.println("zoomed plot width "+(plot.getWidth())+" tooltips.setPlotHeight  "+(plot.getHeight())+"  tooltips.setPlotHeightArea  "+(plotHeightArea)+"tooltips.setPlotWidthArea  "+(plotWidthArea)+"   tooltips.setPlotRight   "+(plot.right)+" tooltips.setPlotTop   "+(plot.top)+" tooltips.setPlotBottom   "+(plot.bottom)+"tooltips.setPlotLeft  "+(plot.left)+"  tooltips.setyAxisFactor "+(yAxisFactor)+"      tooltips.setxAxisFactor "+(xAxisFactor));
//        return zoomTooltips;
//        
//        }
//        else{
        tooltips = new UpdatedTooltip();
        if (zoom) {
            
            try{
            System.out.println("updated tooltip");
            int plotWidthArea = (plot.getWidth() - plot.left - plot.right);
            int plotHeightArea = plot.getHeight() - plot.top - plot.bottom;

            double xDataArea = plot.getZoomedArea()[1] - plot.getZoomedArea()[0];
            double xUnitPix = xDataArea / (double) plotWidthArea;

            tooltips.setPlotWidth(plot.getWidth());
            tooltips.setPlotHeight(plot.getHeight());
            tooltips.setPlotHeightArea(plotHeightArea);
            tooltips.setPlotWidthArea(plotWidthArea);
            tooltips.setPlotRight(plot.right);
            tooltips.setPlotTop(plot.top);
            tooltips.setPlotBottom(plot.bottom);
            tooltips.setPlotLeft(plot.left);
            tooltips.setyAxisFactor(0);
            tooltips.setxAxisFactor(0);
//        double xDataArea =  plot.getZoomedArea()[1] -  plot.getZoomedArea()[0];
//        double xUnitPix = xDataArea / (double) plotWidthArea;
            tooltips.setxUnitPix(xUnitPix);
            double yDataArea = plot.getZoomedArea()[3] - plot.getZoomedArea()[2];
            double yUnitPix = yDataArea / (double) plotHeightArea;
            tooltips.setyUnitPix(yUnitPix);
            tooltips.setMinX(plot.getZoomedArea()[0]);
            tooltips.setMaxY(plot.getZoomedArea()[3]);
            tooltips.setPoints(points);
            tooltips.setRowIds(rowIds);
            }catch(Exception exp){exp.printStackTrace();}

        } else {
            int yAxisFactor = plot.left - plot.yaxis.predictWidth();
            int xAxisFactor = plot.bottom - plot.xaxis.predictWidth();
            int plotWidthArea = plot.Width() - (plot.left) - plot.right + yAxisFactor;
            int plotHeightArea = plot.getHeight() - plot.top - plot.bottom + xAxisFactor;
            tooltips.setPlotWidth(plot.getWidth());
            tooltips.setPlotHeight(plot.getHeight());
            tooltips.setPlotHeightArea(plotHeightArea);
            tooltips.setPlotWidthArea(plotWidthArea);
            tooltips.setPlotRight(plot.right);
            tooltips.setPlotTop(plot.top);
            tooltips.setPlotBottom(plot.bottom);
            tooltips.setPlotLeft(plot.left);
            tooltips.setyAxisFactor(yAxisFactor);
            tooltips.setxAxisFactor(xAxisFactor);
            double xDataArea = plot.xaxis.getDataMax() - plot.xaxis.getDataMin();
            double xUnitPix = xDataArea / (double) plotWidthArea;
            tooltips.setxUnitPix(xUnitPix);
            double yDataArea = plot.yaxis.getDataMax() - plot.yaxis.getDataMin();
            double yUnitPix = yDataArea / (double) plotHeightArea;
            tooltips.setyUnitPix(yUnitPix);
            tooltips.setMinX(plot.xaxis.getDataMin());
            tooltips.setMaxY(plot.yaxis.getDataMax());
//        if(zoom)
//            tooltips.setPoints(subZoomPoints);
//        else
            tooltips.setPoints(points);
            tooltips.setRowIds(rowIds);
        }
//        printPlotData(plotHeightArea, plotWidthArea, xAxisFactor, yAxisFactor);
        return tooltips;
//        }

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
//    private Point sweepFrom,sweepTo;
//     public boolean[] getFramedIndexes( int startx,int starty,int endx,int endy) {
//
//        boolean[] ret = new boolean[plot.getXValues().length];
//
//        boolean[] tolerated = plot.getBackgroundFactory().getTolerated();
//
//        if (plot.getframeType() == 0) {
//            int sqxstart = Math.min(startx, endx);
//            int sqystart = Math.min(starty, endy);
//            int sqxend = (Math.max(startx, endx) - Math.min(startx, endx));
//            int sqyend = (Math.max(starty, endy) - Math.min(starty, endy));
//            sweepFrom = new Point(sqxstart, sqystart);
//            sweepTo = new Point(sqxend, sqyend);
//        }
////        System.out.println(plot.getXValues().length);
//        for(int z=0;z<plot.getXValues().length;z++){
//            int x = plot.getXValues()[z];
//            int y = plot.getYValues()[z];
////            System.out.println("on position "+z+"  x = "+x+"  and y = "+y);
//        
//        }
////        for (int i = 0; i < ret.length; i++) {
////
////            //System.out.print("\n*");
////
////            for (int j = 0; j < paths.size(); j++) //if( (dotColors!=null && dotColors[i]!=null) && (tolerated==null || tolerated[i]) && ((Shape)paths.elementAt(j)).contains(Nx[i], Ny[i])){
////            {
////                if ((tolerated == null || tolerated[i]) && ((Shape) paths.elementAt(j)).contains(Nx[i], Ny[i])) {
////                    if (visible == null || visible[i]) {
////                        ret[i] = true;
////                    }
////
////                }
////            }
////
////        }
//
//        return ret;
//    }
     
     public int[] getPCASelection(int startX, int startY, int endX, int endY) {
         
//         int maxXM = Math.max(startX, endX);
//         int minXM = Math.min(startX, endX);
//         int maxYM = Math.max(startY, endY);
//         int minYM = Math.min(startY, endY);
//
//         int yAxisFactor = plot.left- plot.yaxis.predictWidth();
//         int xAxixFactor = plot.bottom - plot.xaxis.predictWidth();
//         int plotWidthArea = (plot.getWidth() - plot.left - plot.right)+(yAxisFactor);
//         int plotHeightArea = plot.getHeight() - plot.top - plot.bottom + xAxixFactor;
//        
//         if((minXM<(plot.left-yAxisFactor) && maxXM < (plot.left-yAxisFactor))||(minXM>(plot.left+plotWidthArea))){
//            return  null;
//        }
//        if((minYM<plot.top && maxXM < plot.left)||(minYM>plot.top+plotHeightArea)){
//            return  null;
//        }
//        
//         minXM= minXM - plot.left +yAxisFactor;
//         maxXM= maxXM - plot.left + yAxisFactor;
//         minYM-=plot.top;
//         maxYM-=plot.top;
//        
//        if((minXM<0 && maxXM >= 0))
//             minXM =0;// plot.left;
//        if(maxXM > plotWidthArea && minXM>= 0)
//              maxXM = plotWidthArea;
//        if((minYM<=0 && maxYM > 0))//plot.top))
//             minYM = 0;//plot.top;
//        if(maxYM >plotHeightArea&& minYM>= 0)
//              maxXM = plotHeightArea;
//        
//        
//          double xDataArea = plot.xaxis.maximum - plot.xaxis.minimum;
//         double xUnitPix = xDataArea/(double) plotWidthArea ;
//         double modStartX = (minXM * xUnitPix) + plot.xaxis.minimum;//xstart units from min         
////         zoomedRect[0]=modStartX;
//         double modEndX = (maxXM * xUnitPix) + plot.xaxis.minimum;
////         zoomedRect[1]=modEndX;
//         
//         double yDataArea = plot.yaxis.maximum - plot.yaxis.minimum;
//         double yUnitPix =  yDataArea/(double) plotHeightArea ;
//         double modStartY = plot.yaxis.maximum - (maxYM * yUnitPix);
////         zoomedRect[2]= modStartY;
//         double modEndY = plot.yaxis.maximum - (minYM * yUnitPix);
//          zoomedRect[3]= modEndY;
         double[] selectRect = null;
         if (zoom) {
             try {
                 selectRect = getZoomedSelectionRecatangle(startX, startY, endX, endY);
                  System.out.println("pointX "+selectRect[0] +"   pointY  "+selectRect[2] +"  plot.left -  "+plot.left+"  plot.yaxis.predictWidth()  "+plot.yaxis.predictWidth());
        
//         System.out.println("zoomedpointX "+selectrectZoom[0] +"   zoomedpointY  "+selectrectZoom[2] +"  zoomedplot.left -  "+plot.left+"  zoomedplot.yaxis.predictWidth()  "+plot.yaxis.predictWidth());

             } catch (Exception exp) {
                 exp.printStackTrace();
             }

         } else {
             selectRect = this.getSelectionRecatangle(startX, startY, endX, endY);
         }

//         System.out.println("selection index "+selectRect[0]+"  "+selectRect[1]+"  "+selectRect[2]+"  "+selectRect[3]+"  ");
         HashSet<Integer> selectedPoints = new HashSet<Integer>();
//         if (zoom) {
//             for (int x = 0; x < subZoomPoints[0].length; x++) {
//                 double pointX = subZoomPoints[0][x];
//                 double pointY = subZoomPoints[1][x];
//                 if (pointX >= modStartX && pointX <= modEndX && pointY >= modStartY && pointY <= modEndY) {
//                     selectedPoints.add(zoomedToNormalIndex[x]);
//                 }
//
//             }
//         } else {

         for (int x = 0; x < points[0].length; x++) {
             double pointX = points[0][x];
             double pointY = points[1][x];
             if (pointX >= selectRect[0] && pointX <= selectRect[1] && pointY >= selectRect[2] && pointY <= selectRect[3]) {
                 selectedPoints.add(x);
             }

         }
//         }
         if (selectedPoints.size() > 0) {

             Integer[] selectedIndexes = new Integer[selectedPoints.size()];
             System.arraycopy(selectedPoints.toArray(), 0, selectedIndexes, 0, selectedIndexes.length);

//                 System.out.println("Selected indexes are ---->>> " + selectedPoints.toString());
             int[] arr = new int[selectedIndexes.length];
             arr = org.apache.commons.lang3.ArrayUtils.toPrimitive(selectedIndexes, selectedIndexes.length);
             return arr;
         }

         return null;
    }
     
     private double[] getZoomedSelectionRecatangle(int startX, int startY, int endX, int endY) {
        
         
//         System.out.println("------ selection--->> "+plot.getZoomedArea()[0] +" ---  "+ plot.getZoomedArea()[1]);
//         System.out.println("zoomed selection--->> "+plot.xaxis.getDataMax() +" ---  "+ plot.xaxis.getDataMin());
        double[] selectionRect = new double[4];
        int maxXM = Math.max(startX, endX);
        int minXM = Math.min(startX, endX);
        int maxYM = Math.max(startY, endY);
        int minYM = Math.min(startY, endY);
         System.out.println("stage 1 is ok maxXM "+maxXM+"  maxYM  "+maxYM);
        int plotWidthArea = (plot.getWidth() - plot.left - plot.right);
        int plotHeightArea = plot.getHeight() - plot.top - plot.bottom;

        if ((minXM < (plot.left) && maxXM < (plot.left)) || (minXM > (plot.left+plotWidthArea))) {
            System.out.println("stage 2 is ok "+(minXM < (plot.left) && maxXM < (plot.left))+"  "+(minXM > (plot.left)));
            return null;
        }
        if ((minYM < plot.top && maxXM < plot.left) || (minYM > plot.top + plotHeightArea)) {
            System.out.println("stage 3 is ok ");
            return null;
        }
        minXM = minXM - plot.left ;
        
         System.out.println("stage4 is ok minXM "+minXM+"  plot.left  "+plot.left);
        
         maxXM= maxXM - plot.left ;
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
        
        
          double xDataArea = plot.getZoomedArea()[1]  - plot.getZoomedArea()[0] ;
         double xUnitPix = xDataArea/(double) plotWidthArea ;
         double modStartX = (minXM * xUnitPix) + plot.getZoomedArea()[0] ;//xstart units from min    
         selectionRect[0]= modStartX;
//         zoomedRect[0]=modStartX;
         double modEndX = (maxXM * xUnitPix) + plot.getZoomedArea()[0] ;
//         zoomedRect[1]=modEndX;
         selectionRect[1]= modEndX;
         
         double yDataArea = plot.getZoomedArea()[3]  - plot.getZoomedArea()[2] ;
         double yUnitPix =  yDataArea/(double) plotHeightArea ;
         double modStartY = plot.getZoomedArea()[3]  - (maxYM * yUnitPix);
         selectionRect[2]=modStartY;
//         zoomedRect[2]= modStartY;
         double modEndY = plot.getZoomedArea()[3]  - (minYM * yUnitPix);
         selectionRect[3]=modEndY;
         return selectionRect;
     
     }

    private double[] getSelectionRecatangle(int startX, int startY, int endX, int endY) {
        double[] selectionRect = new double[4];
        int maxXM = Math.max(startX, endX);
        int minXM = Math.min(startX, endX);
        int maxYM = Math.max(startY, endY);
        int minYM = Math.min(startY, endY);

        int yAxisFactor = plot.left - plot.yaxis.predictWidth();
        int xAxixFactor = plot.bottom - plot.xaxis.predictWidth();
        int plotWidthArea = (plot.getWidth() - plot.left - plot.right) + (yAxisFactor);
        int plotHeightArea = plot.getHeight() - plot.top - plot.bottom + xAxixFactor;

        if ((minXM < (plot.left - yAxisFactor) && maxXM < (plot.left - yAxisFactor)) || (minXM > (plot.left + plotWidthArea))) {
            return null;
        }
        if ((minYM < plot.top && maxXM < plot.left) || (minYM > plot.top + plotHeightArea)) {
            return null;
        }
//         System.out.println("plot.left  "+plot.left+"   plot.yaxis.predictWidth() "+plot.yaxis.getAWidth()+"  plot.bottom  " +plot.bottom +"plot.xaxis.predictWidth()  "+plot.xaxis.getAWidth()+"  yAxisFactor  "+yAxisFactor+"   xAxisFactor "+xAxixFactor);
        minXM = minXM - plot.left + yAxisFactor;
         maxXM= maxXM - plot.left + yAxisFactor;
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
         selectionRect[0]= modStartX;
//         zoomedRect[0]=modStartX;
         double modEndX = (maxXM * xUnitPix) + plot.xaxis.minimum;
//         zoomedRect[1]=modEndX;
         selectionRect[1]= modEndX;
         
         double yDataArea = plot.yaxis.maximum - plot.yaxis.minimum;
         double yUnitPix =  yDataArea/(double) plotHeightArea ;
         double modStartY = plot.yaxis.maximum - (maxYM * yUnitPix);
         selectionRect[2]=modStartY;
//         zoomedRect[2]= modStartY;
         double modEndY = plot.yaxis.maximum - (minYM * yUnitPix);
         selectionRect[3]=modEndY;
         return selectionRect;
     
     }
     

    public void initZoomInteraction(int[] selection) {

        zoomedToNormalIndex = selection;
        for (int x = 0; x < indexToZoomed.length; x++) {
            indexToZoomed[x] = -100;
        }
        for (int i = 0; i < selection.length; i++) {
            indexToZoomed[selection[i]] = i;
        }
    }

    public boolean[] getZoomedNotshadIndex() {

        if (notshadIndex != null) {
            return zoomedSelectionChange(notshadIndex);//notshadIndex
        } else {
            return null;
        }

     
     }
     
     public int[] reindexZoomedSelectionIndexes(int[] zoomedSelection){
     int [] reindexSelection = new int[zoomedSelection.length];
     for(int z=0;z<zoomedSelection.length;z++){
         reindexSelection[z] = zoomedToNormalIndex[zoomedSelection[z]];
     }
     return reindexSelection;
//         if (zoom) {
//             for (int x = 0; x < subZoomPoints[0].length; x++) {
//                 double pointX = subZoomPoints[0][x];
//                 double pointY = subZoomPoints[1][x];
//                 if (pointX >= modStartX && pointX <= modEndX && pointY >= modStartY && pointY <= modEndY) {
//                     selectedPoints.add(zoomedToNormalIndex[x]);
//                 }
//
//             }
//         }
     
     }
}
