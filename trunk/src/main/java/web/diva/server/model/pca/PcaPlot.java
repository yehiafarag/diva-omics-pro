/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model.pca;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JToolTip;
import no.uib.jexpress_modularized.core.computation.JDoubleSorter;
import no.uib.jexpress_modularized.core.computation.JIntSorter;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.core.visualization.BackgroundFactory;
import no.uib.jexpress_modularized.core.visualization.JeToolTip;
import no.uib.jexpress_modularized.core.visualization.LineStyles.LineMark;
import no.uib.jexpress_modularized.core.visualization.charts.Axis;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ColorFactory;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ControlPoint;

/**
 *
 * @author y-mok_000
 */
public class PcaPlot extends BasicChart{
    
    
      boolean equalScales = false;
        public PcaPlot() {
        super();
        resetAxis();

        //repaint();

    }

    public boolean isZoompca() {
        return zoompca;
    }
    public void setHex(boolean hex)
    {
        this.hex = hex;
    }
     public boolean isHex() {
        return hex;
    }

    public void setZoompca(boolean zoompca) {
        this.zoompca = zoompca;
    }

    public boolean isPaintNamesonClick() {
        return paintNamesonClick;
    }

    public void setPaintNamesonClick(boolean paintNamesonClick) {
        this.paintNamesonClick = paintNamesonClick;
    }
    boolean start = true;
//    private JeToolTip tip;  // @TODO: reimplement me!
    BackgroundFactory bgf = new BackgroundFactory();
    // public ScatterProps2 sp = new ScatterProps2(null,true);;
    //This is used to set the type of plot (plain/log-log/ MA)
    //Nothing besides setting the medianline for normalization is affected in this component.
    private final int plottype = 0;
    private final  float CAVectorThickness = 1.0f;
    private final   int PLAIN = 0;
    private final   int LOG = 1;
    private final   int MA = 2;
    private Dataset data;
    private final  int forcedStringSize = -1;
    private final  Color dotColor = Color.green;
    private boolean FormUpdated = false; //for initially insert values to properties dialog.
    private Color[] dotColors = null;
    private boolean[] visible;
    private boolean[] collapsedSquares;
    private boolean[] visibleLines;
    private Color[] correspondenceColors = null;
    private final  boolean allTransparent = false;
    //if deltalines>-1 they will be drawn
    private double deltaLines = 0.0;
    private final  Point.Double[] deltalines = null;
    
    /**
     * NOTE: This seems to be a variable that is important for this class when it comes to setting 
     * a lot of configurations, for example how things should be painted in the Scatter Plot (colors etc).
     * </p>
     * This variable is in JExpress initialized by getting the value for the key <code>plotName</code>
     * (see this class) from the practically omniscient field cluster.props. The 
     * returned value which is a <code>Hashtable</code>. The omniscient props field is loaded from a 
     * cfg file, but attempting to deserialize this file will lead to a ClassNotFoundException which terminates
     * the stream if not all the classes that are serialized in the file is present in the project. Instead, I 
     * chose to just run the JExpress project with my cfg-file 
     * and println to inspect all the values of the keys which are 
     * used in this <code>Hashtable</code>, and try to recreate them in the following static block.
     * </p>
     * There are some if-else blocks in this class which have to to with checking if props has some 
     * value (given by a key), but these are commented out. They are not deleted, since it might be 
     * beneficial to know the logical structure of the code at a later point, though it might seem to be more 
     * about setting a look and feel for the plot (to me).
     * </p>
     * All these values could of course be manually inserted instead of going through the Hashtable, if the 
     * values aren't supposed to be changed. But I chose to do it like this to maintain some of the intended
     * structure that was in JExpress (if that turns out to be relevant at a later point).
     */
    private final Hashtable Layout = new Hashtable();
    
     {
        Layout.put("topText", ""); //or is it null instead of empty string?
        Layout.put("miyticsSV", 2);
        Layout.put("trendtransSV", 50);
        Layout.put("col4SV", new Color(255, 204, 51));
        Layout.put("minpSV", 30.0);
        Layout.put("showTrendSV", false);
        Layout.put("axisbgSV",Color.GRAY);// new Color(0, 51, 153));
        Layout.put("weightSV", 0.3);
        Layout.put("yticsbothsidesSV", true);
        Layout.put("xmin", -3.86);
        Layout.put("stretchSV", true);
        Layout.put("SbgSV", 0);
        Layout.put("xaxSV", true);
        Layout.put("nullSV", new Color(102, 102, 255));
        Layout.put("tileSV", false);
        Layout.put("xmax", 1.661);
        Layout.put("col2SV", new Color(204, 204, 255));
        Layout.put("ymin", -2.378);
        Layout.put("mixticsSV", 2);
        Layout.put("paintGridSV", true);
        Layout.put("tresholdSV", 100);
        Layout.put("col5SV", new Color(255, 0, 0));
        Layout.put("ymax", 1.922);
        Layout.put("colorsSV", 50);
        Layout.put("singlebgSV", new Color(255, 254, 254));
        Layout.put("grad1SV", new Color(51, 153, 255));
        Layout.put("trendcSV", new Color(0, 0, 0));
        Layout.put("xticsbothsidesSV", true);
        Layout.put("chartbgSV", Color.WHITE);
        Layout.put("frameSV", false);
        Layout.put("col3SV", new Color(102, 255, 102));
        Layout.put("gridColorSV", new Color(102, 102, 102));
        Layout.put("densareaSV", 100);
        Layout.put("dotsizeSV", 4);
        Layout.put("pathSV", ""); //or is it null instead of empty string?
        Layout.put("widthSV", 6.0);
        Layout.put("yaxisTitle", "Principal Component2");
        Layout.put("numpSV", false);
        Layout.put("transparencySV", 66);
        Layout.put("circSV", false);
        Layout.put("grad2SV", new Color(204, 204, 255));
        Layout.put("pointsSV", 0.05);
        Layout.put("endlabelsSV", false);
        Layout.put("col1SV", new Color(255, 254, 254));
        Layout.put("xaxisTitle", "Principal Component1");
        Layout.put("gradtypeSV", 0);
    }
    
    private final boolean paintOnlyIdentifiers = false;
    private boolean[] inActiveGroup = null;
    private boolean hex = true;  //When drawing neurons..
    private int frameType = 0;
    private Color frameBG = Color.white;
//    public JLabel scaleLabel;
//    public JPanel VariancePanel;
    private boolean zoompca = true;
    private boolean paintNamesonClick = false;
    private double[][] neurons;
    private Color SpotFrameColor = Color.black;
    private boolean[] invalidValues; //this controls valid values and is for instance true for log values of 0 and below..
    private boolean[] notShaded;
    //private ImageIcon scaleIcon = new ImageIcon();
    private String plotName = "Scatterplot"; //Defines the loadform name.
    private final boolean medianLine = false;
    private final double medianSkew = Double.NaN;  //If the data has been normalized with a skew..
    private final Vector medianSkews = null;
    private BufferedImage plot;
    
    private boolean mouseDrag = false;
    private final Vector paths = new Vector();
    private int startx, starty, endx, endy;  //For mouse event framing
    
    private boolean FullRepaint = true;
    private final boolean LockFullRepaint = false;
    private Rectangle valueArea;
    private double[] zoom, 
                          zoomedArea; //The corners of the zoomed valueArea.
    private Point highLightNeuron;
    private final int unselectedTransparency = 23;
    private double[][] line;    //If the scatterplot should also contain a line, this is it (For example for lowess normalization)
    private Vector lines; //same as above, with multilines..
    private Vector lineColors;//If the lines should be colored..
    private JLabel sizeMonitor; //If this is not null, the plot size will be set here after repaint..
    //-------
    //For correspondence analysis..
    private String[][] TEXT;
    private double[] TEXTX;
    private double[] TEXTY;
    public Font CF = null;//new Font("ARIAL",0,13);
    private int CAColor = 250;
    private final Font font = new Font("Times New Roman", 0, 12);
    //0 = none
    //1 = num points
    //2 = num x axis
    private int trendline = 0;
    private double trendpoints = 0.2;
    private double trendweight = 0.3;
    private float trendwidht = 4.0f;
    private Color trendcolor = Color.blue;
    private int trendtrans = 100;
    private int trendminp = 30;
    private boolean[] trendSource;
    private String[][] SpotNames = null;
    private final Vector CAGroupMedians = null;
    private final LineMark[] CALineMarks = null;  
    private final boolean drawCAText = false;
    private final boolean drawCAMedians = true;
    private int CAPsize = 10;
    private ColorFactory factory;
    //private cluster cl;

    public void setSpotNames(String[][] SpotNames) {
        this.SpotNames = SpotNames;
    }

    public void setColorFactory(ColorFactory factory) {
        this.factory = factory;
    }


    public void createDefaultDensityColorFactory() {
        ColorFactory cf = new ColorFactory();
        cf.setMirror(false);
        cf.initControlPoints(false);
        List<ControlPoint> cp = cf.getControlPoints();
        ControlPoint cpo = new ControlPoint();

        cp.get(0).setColor(Color.white);
        cp.get(1).setColor(Color.YELLOW);

        cpo.setLocation(0.3);
        cpo.setColor(new Color(170, 245, 170));
        cp.add(cpo);

        cpo = new ControlPoint();
        cpo.setLocation(0.6);
        cpo.setColor(Color.red);
        cp.add(cpo);

        cf.setMissing(Color.blue);
        setColorFactory(cf);
    }

    public BackgroundFactory getBackgroundFactory() {
        return bgf;
    }

    public void setLine(double[][] line) {
        this.line = line;
    }

    public void setLines(Vector lines) {
        this.lines = lines;
    }

    public void setVisible(boolean[] visible) {
        this.visible = visible;
    }

    public void setNotShaded(boolean[] notShaded) {
        this.notShaded = notShaded;
    }

    public void setVisibleLines(boolean[] visibleLines) {
        this.visibleLines = visibleLines;
    }

   

    public void setLIneColors(Vector lineColors) {
        this.lineColors = lineColors;
    }

    public void setCorrespondeceValues(String[][] TEXT, double[] TEXTX, double[] TEXTY, Color[] correspondenceColors) {
        this.TEXT = TEXT;
        this.TEXTX = TEXTX;
        this.TEXTY = TEXTY;
        this.correspondenceColors = correspondenceColors;
    }

    public void setHighLightedNeuron(Point highLightNeuron) {
        this.highLightNeuron = highLightNeuron;
    }

    public Dimension getPlotSize() {
        if (valueArea != null) {
            return valueArea.getSize();
        } else {
            return null;
        }
    }

    public void setFrameBG(Color frameBG) {
        this.frameBG = frameBG;
        this.forceFullRepaint();
        //repaint();
    }

   

    public void setframeType(int frameType) {
        this.frameType = frameType;
    }

    public int getframeType() {
        return frameType;
    }

    public final void resetAxis() {
        invalidValues = xaxis.resetAxis(Rx);
        invalidValues = addInvalidValues(invalidValues, yaxis.resetAxis(Ry));
        updateAxisValues();
    }

    
    

    public PcaPlot(Hashtable props, double[] Rx, double[] Ry) {
        boolean init = false;

        if (this.props == null) {
            init = true;
        }

        this.props = props;

        if (init) {
            readValues();   //put the stored values to the properties dialog.
        }
        setData(Rx, Ry);
    }

    public void setPropsAndData(double[] Rxn, double[] Ryn) {

        double minX=0,maxX=0,minY=0,maxY= 0.0;
        for(double d1: Rxn)
        {
           if(d1 > maxX)
               maxX = d1;
           if(d1<minX)
               minX=d1;
           
        }
         
         for(double d2: Ryn)
        {if(d2 > maxY)
               maxY = d2;
           if(d2<minY)
               minY=d2;
        }
        invalidValues = null;
        boolean init = false;

        if (this.props == null) {
            init = true;
        }
        FullRepaint = true;

        if (init) {
            readValues();   
        }
        setData(Rxn, Ryn);
        readForm();
        repaint();
    }

    public void setProps(Hashtable props) {
        boolean init = false;
        if (this.props == null) {
            init = true;
        }

        this.props = props;
        FullRepaint = true;
        if (init) {
            readValues();  
        }
    }

    public void setPropsAndData(double[] Rxn, double[] Ryn, double[] zoom) {
        invalidValues = null;
        boolean init = false;
        if (this.props == null) {
            init = true;
        }

        this.zoom = zoom;
        FullRepaint = true;
        if (init) {
            readValues();   //put the stored values to the properties dialog.
        }
        setData(Rxn, Ryn);
        this.zoom(zoom);
    }

    public boolean isEqualScales() {
        return equalScales;
    }

    public void setEqualScales(boolean equalScales) {
        this.equalScales = equalScales;

        if (equalScales) {

            double max = Math.max(xaxis.maximum, yaxis.maximum);
            double min = Math.min(xaxis.minimum, yaxis.minimum);

            yaxis.setManualRange(true);

            xaxis.setManualRange(true);


            xaxis.minimum = min;
            xaxis.maximum = max;
            yaxis.minimum = min;
            yaxis.maximum = max;
            updateAxisValues();
        } else {


            yaxis.setManualRange(false);
            xaxis.setManualRange(false);
            invalidValues = xaxis.resetAxis(Rx);
            invalidValues = addInvalidValues(invalidValues, yaxis.resetAxis(Ry));
            updateAxisValues();
        }
        FullRepaint = true;

    }

    public final void setData(double[] Rxn, double[] Ryn) {
        //OBS
        invalidValues = null;
        if (Rxn != null) {
            this.Rx = Rxn;
        } else {
            this.Rx = new double[]{};
        }

        if (Ryn != null) {
            this.Ry = Ryn;
        } else {
            this.Ry = new double[]{};
        }

        invalidValues = xaxis.resetAxis(Rxn);
        invalidValues = addInvalidValues(invalidValues, yaxis.resetAxis(Ry));
        
        updateAxisValues();

        repaint();
    }

    public void zoomout() {

        yaxis.setManualRange(false);
        yaxis.force_end_labels = ((Boolean) Layout.get("endlabelsSV")); //sp.endlabelsSV.isSelected();

        xaxis.setManualRange(false);
        xaxis.force_end_labels = ((Boolean) Layout.get("endlabelsSV"));//sp.endlabelsSV.isSelected();

        invalidValues = xaxis.resetAxis(Rx);
        invalidValues = addInvalidValues(invalidValues, yaxis.resetAxis(Ry));

        updateAxisValues();

        this.FullRepaint = true;
        repaint();
    }


    public void setXaxisTitle(String label) {
        if (Layout == null) {
            return;
        }
        xaxis.setTitleText(label);
        if (label != null) {
            Layout.put("xaxisTitle", label);
        }

        // Layout.put("endlabelsSV")).booleanValue(); sp.Xaxis.setText(label);
    }
//

    public void setYaxisTitle(String label) {
        if (Layout == null) {
            return;
        }
        yaxis.setTitleText(label);
        if (label != null) {
            Layout.put("yaxisTitle", label);
        }
        //sp.Yaxis.setText(label);
    }

    public Vector getIndexesAtPoint(Point p, int radius) {
        Vector ret = new Vector();
        Vector dst = new Vector();
        //double rpx = xaxis.getDouble(p.x);
        //double rpy = yaxis.getDouble(p.y);

        boolean[] tolerated = bgf.getTolerated();
        double d = 0.0;
        double d1 = 0.0;
        double d2 = 0.0;
        for (int i = 0; i < Rx.length; i++) {

            if ((dotColors == null || dotColors[i] != null) && (tolerated == null || tolerated[i])) {
                //if( (dotColors==null || dotColors[i]!=null) && (tolerated==null || tolerated[i])){

                if (visible == null || visible[i]) {
                    if (xaxis.getInteger(Rx[i]) > p.x - radius
                            && xaxis.getInteger(Rx[i]) < p.x + radius
                            && yaxis.getInteger(Ry[i]) > p.y - radius
                            && yaxis.getInteger(Ry[i]) < p.y + radius) {


                        ret.add(i);

                        d1 = xaxis.getInteger(Rx[i]) - p.x;
                        d1 = d1 * d1;

                        d2 = yaxis.getInteger(Ry[i]) - p.y;
                        d2 = d2 * d2;

                        d = Math.sqrt(d1 + d2);
                        //System.out.print("\n"+d1);
                        dst.add(d);
                    }
                }
            }
            if (Thread.interrupted()) {
                return null;
            }
        }


        Vector sret = new Vector();
        double[] dist = new double[dst.size()];
        for (int i = 0; i < dist.length; i++) {
            dist[i] = ((Double) dst.elementAt(i));
        }
        int[] srt = JDoubleSorter.quickSort(dist);

        for (int i = 0; i < srt.length; i++) {
            sret.addElement(ret.elementAt(srt[i]));
        }

        return sret;
    }

    public void forceFullRepaint() {
        FullRepaint = true;
        repaint();
    }



    @Override
    public int Height() {
        return getSize().height;
    }

    @Override
    public int Width() {
        return getSize().width;
    }//return size.width;}

    @Override
    public void paintAll(Graphics gr){
        paintComponent(gr);    
    }
    @Override
      @SuppressWarnings("null")
    public void paintComponent(Graphics gr) {
        
        if (Layout == null) {
            return;
        }
        
        readForm();
        setForeground((Color) Layout.get("axisbgSV"));
        if (yaxis.getTitleText().startsWith("Log")) {
            if (yaxis.getTitleText().charAt(3) == '2') {
            } else if (yaxis.getTitleText().charAt(3) == '1') {
            } else {
                String tit = yaxis.getTitleText().substring(0, 3);
                if (yaxis.useLog2) {
                    tit += "2";
                } else {
                    tit += "10";
                }

                tit += yaxis.getTitleText().substring(3, yaxis.getTitleText().length());
                yaxis.setTitleText(tit);
            }
        }

        if (xaxis.getTitleText().startsWith("Log")) {
            if (xaxis.getTitleText().charAt(3) == '2') {
            } else if (xaxis.getTitleText().charAt(3) == '1') {
            } else {
                String tit = xaxis.getTitleText().substring(0, 3);
                if (xaxis.useLog2) {
                    tit += "2";
                } else {
                    tit += "10";
                }

                tit += xaxis.getTitleText().substring(3, xaxis.getTitleText().length());
                xaxis.setTitleText(tit);
            }
        }



        if (FullRepaint || LockFullRepaint) {

            Graphics g = null;
            if (!LockFullRepaint) {
                plot = new BufferedImage(Width(), Height(), BufferedImage.TYPE_INT_ARGB);
                g = plot.getGraphics();
            } else {
                g = gr;
            }



            int TitleHeight = topText.getHeight(g);

            int HTitlePos = Width() / 2 - (topText.getWidth(g) / 2);

            if (Width() == 0 || Height() == 0) {
                return;
            }

            g.setColor(getBackground());
            if (this.isOpaque()) {
                g.fillRect(0, 0, Width(), Height());
            }




            g.setColor(Color.black);
            if (topText.getText() != null && topText.getText().length() > 0) {
                topText.draw(g, HTitlePos, top + 6);
            }

            if (PaintLegend) {
                LegendWidth = paintLegend(g);
            }
            left = yaxis.getAxisWidth(g);
            bottom = xaxis.getAxisWidth(g);

            xaxis.positionAxis(left, Width() - right - LegendWidth, Height() - bottom, Height() - bottom);
            yaxis.positionAxis(left, left, top + TitleHeight, Height() - bottom);

            xaxis.data_window.setSize(10, Height() - bottom - top - TitleHeight);
            yaxis.data_window.setSize(Width() - left - right - LegendWidth, 10);

            valueArea = new Rectangle(left, top + TitleHeight, Width() - right - left, Height() - bottom - top - TitleHeight);

            Nx = getXValues();
            Ny = getYValues();

            bgf.setDensCords(Nx, Ny);

            bgf.setColorFactory(factory);
            bgf.externalImage = (String) Layout.get("pathSV");//sp.pathSV.getText();
            bgf.tileImages = ((Boolean) Layout.get("tileSV"));//sp.tileSV.isSelected();

            bgf.paintBackground(g, valueArea, ((Integer) Layout.get("SbgSV"))); //sp.SbgSV.getSelectedIndex());


            xaxis.axiscolor = getForeground();
            yaxis.axiscolor = getForeground();

            Graphics2D g2d = (Graphics2D) g;

            if (!FormUpdated) {
                updateForm();
                FormUpdated = true;
                repaint();
            }

            if (!LockFullRepaint) {
                g.setClip(valueArea);
            }

            boolean[] tolerated = bgf.getTolerated();

            int halfSize = (dotsize / 2);


            if (dotsize < 0) {
                return;
            }
            int pointSize = dotsize;
            boolean circ = ((Boolean) Layout.get("circSV"));//sp.circSV.isSelected();
            boolean frame = ((Boolean) Layout.get("frameSV"));//sp.frameSV.isSelected();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (data != null) {
                List<Group> groups = data.getRowGroups();
                Group group = null;
                Color gcol = null;
                for (int grp = groups.size() - 1; grp > -1; grp--) {

                    group = (Group) groups.get(grp);
                    if (!group.isActive()) {
                        continue;
                    }
                    gcol = group.getColor();
                    g2d.setColor(gcol);
                    Color dot = Color.BLACK;
                    for (int i = 0; i < Nx.length; i++) {
                        if (!group.hasMember(i)) {
                            continue;
                        }
                        if (valueArea != null && !valueArea.contains(Nx[i], Ny[i])) {
                            continue;
                        }
                        if (visible != null && !visible[i]) {
                            continue;
                        }
                        if (tolerated != null && !tolerated[i]) {
                            continue;
                        }
                        if (((notShaded != null && !notShaded[i]) || allTransparent)) {
                            dot = new Color(gcol.getRed(), gcol.getGreen(), gcol.getBlue(), unselectedTransparency);
                            if (frame) {
                                SpotFrameColor = new Color(0, 0, 0, unselectedTransparency);
                            }
                        } else {
                            dot = gcol;
                        }

                        if (SpotNames != null && SpotNames[i] != null && SpotNames[i].length > 0 && SpotNames[i][0] != null) {
                            StringBuilder sb = new StringBuilder();
                            if (forcedStringSize == -1) {
                                g2d.setFont(new Font("Arial", 0, dotsize + 4));
                            } else {
                                g2d.setFont(new Font("Arial", 0, forcedStringSize));
                            }

                            for (int h = 0; h < SpotNames[i].length; h++) {
                                if (h < SpotNames[i].length - 1) {
                                    sb.append(SpotNames[i][h]).append(" : ");
                                } else {
                                    sb.append(SpotNames[i][h]);
                                }
                            }
                            g.setColor(gcol);
                            g2d.drawString(sb.toString(), Nx[i] + halfSize + 2, Ny[i] + halfSize + 2);
                        }

                        if (collapsedSquares != null) {
                            if (collapsedSquares[i]) {
                                pointSize = 1;
                            } else {
                                pointSize = dotsize;
                            }
                        }
                        if ((!paintOnlyIdentifiers) || (SpotNames == null || SpotNames[i] == null || SpotNames[i][0] == null)) {
                            if (circ) {
                                g.setColor(dot);
                                g.fillOval(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
                                g.setColor(SpotFrameColor);
                                if (frame) {
                                    g2d.setStroke(new BasicStroke(0.5f));
                                    g.drawOval(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
                                }
                            } else {
                                Stroke old = g2d.getStroke();
                                g.setColor(dot);
                                g.fillRect(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
                                g.setColor(SpotFrameColor);
                                if (frame) {
                                    g2d.setStroke(new BasicStroke(0.5f));
                                    g.drawRect(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
                                    g2d.setStroke(old);
                                }
                            }
                        }






                    }



                }



            } else {
                for (int i = 0; i < Nx.length; i++) {
                    if (dotColors == null) {
                        g2d.setColor(new Color(150, 170, 170));
                    }
                    if (valueArea.contains(Nx[i], Ny[i])) {
                        if ((visible == null || (visible.length == Nx.length && visible[i])) && (invalidValues == null || !invalidValues[i])) {
                            if (((tolerated == null) || (tolerated != null && tolerated[i])) && ((inActiveGroup == null) || (inActiveGroup != null && inActiveGroup[i]))) {
                                if (frame) {
                                    SpotFrameColor = Color.black;
                                }
                                if (((notShaded != null && !notShaded[i]) || allTransparent) && dotColors != null && dotColors[i] != null) {
                                    g.setColor(new Color(dotColors[i].getRed(), dotColors[i].getGreen(), dotColors[i].getBlue(), unselectedTransparency));
                                    if (frame) {
                                        SpotFrameColor = new Color(0, 0, 0, unselectedTransparency);
                                    }
                                } else if (dotColors != null && dotColors.length == Nx.length) {
                                    g.setColor(dotColors[i]);
                                }
                                if (SpotNames != null && SpotNames[i] != null && SpotNames[i][0] != null) {
                                    StringBuilder sb = new StringBuilder();
                                    if (forcedStringSize == -1) {
                                        g2d.setFont(new Font("Arial", 0, dotsize + 4));
                                    } else {
                                        g2d.setFont(new Font("Arial", 0, forcedStringSize));
                                    }

                                    for (int h = 0; h < SpotNames[i].length; h++) {
                                        if (h < SpotNames[i].length - 1) {
                                            sb.append(SpotNames[i][h]).append(" : ");
                                        } else {
                                            sb.append(SpotNames[i][h]);
                                        }
                                    }
                                    g2d.drawString(sb.toString(), Nx[i] + halfSize + 2, Ny[i] + halfSize + 2);
                                }

                                if (collapsedSquares != null) {
                                    if (collapsedSquares[i]) {
                                        pointSize = 1;
                                    } else {
                                        pointSize = dotsize;
                                    }
                                }
                                if ((!paintOnlyIdentifiers) || (SpotNames == null || SpotNames[i] == null || SpotNames[i][0] == null)) {
                                    if (circ) {
                                        g.fillOval(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
                                        g.setColor(SpotFrameColor);
                                        if (frame) {
                                            g2d.setStroke(new BasicStroke(0.5f));
                                            g.drawOval(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
                                        }
                                    } else {
                                        Stroke old = g2d.getStroke();
                                        g.fillRect(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
                                        g.setColor(SpotFrameColor);
                                        if (frame) {
                                            g2d.setStroke(new BasicStroke(0.5f));
                                            g.drawRect(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
                                            g2d.setStroke(old);
                                        }
                                    }
                                }
                            } else {
                            }
                        }
                    }

                }

            }


            Stroke tmps = g2d.getStroke();

            if (CAGroupMedians != null && drawCAMedians) {
                Vector grm = CAGroupMedians;

                for (int k = 0; k < grm.size(); k++) {
                    Object[] ob = (Object[]) grm.elementAt(k);
                    Point2D p2d = (Point2D) ob[0];
                    Color c = (Color) ob[1];
                    Color ca = c;//new Color(c.getRed(),c.getGreen(),c.getBlue(),CAColor);

                    g2d.setColor(ca);

                    int xXx = xaxis.getInteger(p2d.getX());
                    int yYy = yaxis.getInteger(p2d.getY());

                    g2d.fillOval(xXx - 3, yYy - 3, 6, 6);

                    g2d.setColor(Color.black);
                    g2d.drawOval(xXx - 5, yYy - 5, 9, 9);

                    int x0 = xaxis.getInteger(0.0);
                    int y0 = yaxis.getInteger(0.0);

                    Rectangle r = g.getClipBounds();
                    Rectangle vla = valueArea;
                    Rectangle r2 = new Rectangle(vla.x + 5, vla.y + 5, vla.width - 10, vla.height - 10);
                    g.setClip(r2);

                    //int xxo = (int)((xXx-x0)*1000.0);
                    //int yyo = (int)((yYy-y0)*1000.0);

                    int xxo = (int) ((xXx - x0) * 10000.0);
                    int yyo = (int) ((yYy - y0) * 10000.0);

                    g2d.setColor(ca);


                    g2d.setStroke(new BasicStroke(CAVectorThickness));

                    g2d.drawLine(x0, y0, xxo, yyo);

                    g2d.setStroke(tmps);

                    g.setClip(r);
                }

            }

            g2d.setStroke(tmps);

            if (trendline == 1) {
                paintTrend2(valueArea, g, Nx, Ny);
            } else if (trendline == 2) {
                paintTrend(valueArea, g, Nx, Ny);
            }
            if (lines != null) {


                double[][] templine = null;

                for (int m = 0; m < lines.size(); m++) {

                    if (visibleLines == null || (visibleLines.length == lines.size() && visibleLines[m])) {
                        if (!(lines.elementAt(m) instanceof double[][])) {
                            break;
                        }

                        templine = (double[][]) lines.elementAt(m);

                        int[][] Iline = new int[2][templine[0].length];

                        for (int i = 0; i < templine[0].length; i++) {
                            Iline[0][i] = xaxis.getInteger(templine[0][i]);
                            Iline[1][i] = yaxis.getInteger(templine[1][i]);


                        }
                        g2d.setStroke(new BasicStroke(1f));
                        if (lineColors != null) {
                            g.setColor((Color) lineColors.elementAt(m));
                        } else {
                            g.setColor(Color.cyan);
                        }
                        g.drawPolyline(Iline[0], Iline[1], Iline[0].length);

                    }
                }
            }



            //g2d.setStroke(new BasicStroke(1f));



            g.setClip(null);

            g.setFont(font);

            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            if (xaxis != null) {
                xaxis.drawAxis(g);
            }
            if (yaxis != null) {
                yaxis.drawAxis(g);
            }


            Layout.put("ymin", yaxis.minimum);
            Layout.put("ymax", yaxis.maximum);
            Layout.put("xmin", xaxis.minimum);
            Layout.put("xmax", xaxis.maximum);

            g.setColor(getForeground());
            g.drawLine(left + 1, top + TitleHeight, Width() - right - LegendWidth, top + TitleHeight);
            g.drawLine(Width() - right - LegendWidth, top + TitleHeight, Width() - right - LegendWidth, Height() - bottom);

            g.setColor(new Color(100, 100, 250));
            if (medianLine) {
                g.drawLine(left + 1, Height() - bottom, Width() - right - LegendWidth, top + TitleHeight);
            }
            //g.drawLine(xaxis.getInteger(0.0),yaxis.getInteger(0.0),xend,yend);

            if (medianSkew != Double.NaN && medianSkew > 0) {

                if (!LockFullRepaint) {
                    g.setClip(valueArea);
                }
                g.setColor(Color.cyan);

                double tp = yaxis.getDouble(top + TitleHeight);
                double tp2 = yaxis.getDouble(Height() - bottom);

                if (yaxis.transform == Axis.Log_Transform) {

                    double val = Math.pow(10, tp);
                    double val2 = Math.pow(10, tp2);
                    if (plottype != MA) {

                        val = val * (1.0 / medianSkew);
                        val2 = val2 * (1.0 / medianSkew);
                        if (medianLine) {
                            g.drawLine(left + 1, yaxis.getInteger(val2), Width() - right - LegendWidth, yaxis.getInteger(val));
                        }
                    } else {

                        val = 1.0;
                        val2 = 1.0;
                        val2 = val2 * medianSkew;
                        //g.drawLine(left+1, yaxis.getInteger(val2) , Width()-right-LegendWidth , yaxis.getInteger(val));
                        g.drawLine(left + 1, yaxis.getInteger(val2), Width() - right - LegendWidth, yaxis.getInteger(val2));
                    }
                } else {
                    double val = tp;
                    double val2 = tp2;
                    val = val * (1.0 / medianSkew);
                    val2 = val2 * (1.0 / medianSkew);
                    //if(medianLine) g.drawLine(xaxis.nullI, yaxis.nullI , Width()-right-LegendWidth , yaxis.getInteger(tp));
                    if (medianLine) {
                        g.drawLine(left + 1, yaxis.getInteger(val2), Width() - right - LegendWidth, yaxis.getInteger(val));
                    }
                }
            }


            //If several different (factor)normalization routines are used, the different values are
            //put in the medianSkews vector. If that is not empty, draw all the lines that are selected.

            if (medianSkews != null) {
                if (!LockFullRepaint) {
                    g.setClip(valueArea);
                }
                for (int i = 0; i < medianSkews.size(); i++) {

                    if (visibleLines == null || (visibleLines.length == medianSkews.size() && visibleLines[i])) {
                        double tempMedianSkew = ((Double) medianSkews.elementAt(i));
                        if (tempMedianSkew != Double.NaN && tempMedianSkew > 0) {

                            if (lineColors != null) {
                                g.setColor((Color) lineColors.elementAt(i));
                            } else {
                                g.setColor(Color.cyan);
                            }

                            double tp = yaxis.getDouble(top + TitleHeight);
                            double tp2 = yaxis.getDouble(Height() - bottom);

                            if (yaxis.transform == Axis.Log_Transform) {

                                double val = Math.pow(10, tp);
                                double val2 = Math.pow(10, tp2);
                                if (plottype != MA) {

                                    val = val * (1.0 / tempMedianSkew);
                                    val2 = val2 * (1.0 / tempMedianSkew);
                                    if (medianLine && val != 0 && val2 != 0) {
                                        g.drawLine(left + 1, yaxis.getInteger(val2), Width() - right - LegendWidth, yaxis.getInteger(val));
                                    }
                                } else {

                                    val = 1.0;
                                    val2 = 1.0;
                                    val2 = val2 * tempMedianSkew;
                                    //g.drawLine(left+1, yaxis.getInteger(val2) , Width()-right-LegendWidth , yaxis.getInteger(val));
                                    if (val != 0 && val2 != 0) {
                                        g.drawLine(left + 1, yaxis.getInteger(val2), Width() - right - LegendWidth, yaxis.getInteger(val2));
                                    }
                                }
                            } else {
                                double val = tp;
                                double val2 = tp2;
                                val = val * (1.0 / tempMedianSkew);
                                val2 = val2 * (1.0 / tempMedianSkew);
                                //if(medianLine) g.drawLine(xaxis.nullI, yaxis.nullI , Width()-right-LegendWidth , yaxis.getInteger(tp));
                                if (medianLine) {
                                    g.drawLine(left + 1, yaxis.getInteger(val2), Width() - right - LegendWidth, yaxis.getInteger(val));
                                }
                            }

                        }
                    }
                }
            }


            g.setClip(valueArea);


            if (deltalines != null) {

                for (int i = 0; i < deltalines.length / 2; i++) {

                

                    double x1 = deltalines[i * 2].getX();
                    double y1 = deltalines[i * 2].getY();

                    double x2 = deltalines[(i * 2) + 1].getX();
                    double y2 = deltalines[(i * 2) + 1].getY();


                    int lx1 = xaxis.getInteger(x1);
                    int ly1 = yaxis.getInteger(y1);
                    int lx2 = xaxis.getInteger(x2);
                    int ly2 = yaxis.getInteger(y2);



                    g.drawLine(lx1, ly1, lx2, ly2);

                }


            }


            Object ali = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (line != null && false) {
                int[][] Iline = new int[2][line[0].length];

                for (int i = 0; i < line[0].length; i++) {
                    Iline[0][i] = xaxis.getInteger(line[0][i]);
                    Iline[1][i] = yaxis.getInteger(line[1][i]);
                }
                g2d.setStroke(new BasicStroke(1f));
                g.setColor(Color.cyan);


                for (int i = 1; i < line[0].length; i++) {

                    g.setColor(new Color(100, 220, 220));

                    g.drawLine(Iline[0][i - 1], Iline[1][i - 1], Iline[0][i], Iline[1][i]);

                }

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, ali);
            }
            g.setClip(null);
            //}

            //  }






            //Draw the correspondence text..

            if (!LockFullRepaint) {
                g.setClip(valueArea);
            }

            int CX = 0;
            int CY = 0;

            StringBuilder sb = new StringBuilder();

            boolean[] us = null;
            if (data != null) {
                //OLD
//                us = data.getusedColInfos();
                //NEW
                us = data.getusedColInfos();

            
            
            }


            Font before = g.getFont();
            //@TODO: reimplement me ?
//            if (CF == null) {
//                createCAFont();
//            }
            g.setFont(CF);

            if (TEXT != null) {

                if (props.containsKey("CAFONT")) {

                    CAColor = ((Integer) props.get("CAColor"));
                }

                if (props.containsKey("CAPSize")) {
                    //props.put("CAPSize",(String)size1.getText());
                    try {
                        CAPsize = Integer.parseInt((String) props.get("CAPSize"));
                    } catch (Exception ex) {
                        CAPsize = 5;
                    }
                }


                for (int i = 0; i < TEXT.length; i++) {

                    CX = xaxis.getInteger(TEXTX[i]);
                    CY = yaxis.getInteger(TEXTY[i]);

                    //g.setColor(new Color(100,255,100,170));




                    if (correspondenceColors != null && correspondenceColors[i] != null) {
                        Color ca = new Color(correspondenceColors[i].getRed(), correspondenceColors[i].getGreen(), correspondenceColors[i].getBlue(), CAColor);
                        g.setColor(ca);

                        //System.out.print("\n"+CAColor);

                        //else g.setColor(CAColor);

                        if (CALineMarks != null && CALineMarks[i] != null) {
                            CALineMarks[i].setColor(ca);
                            CALineMarks[i].paintAt(CX, CY, g);

                        } else {

                            //g.fillRect(CX-(CAPsize/2),CY-(CAPsize/2),CAPsize,CAPsize);

                            // if(frame){
                            //g.setColor(new Color(50,50,50,150));
                            g.drawRect(CX - 2 - (CAPsize / 2), CY - 2 - (CAPsize / 2), CAPsize + 4 - (CAPsize / 2), CAPsize + 4 - (CAPsize / 2));
                            // }

                        }
                        //g.setColor(new Color(50,50,50,150));
                        //g.drawOval(CX,CY,dotsize+1-(dotsize/2),dotsize+1-(dotsize/2));

                        //g.setColor(new Color(100,255,100,170));
                        CX += (dotsize + 4);
                        CY += (g.getFontMetrics().getHeight() / 2) - 4;//((dotsize/2)+4);




                        if (drawCAText) {

                            int plus = 40;


                            int centerx = xaxis.getInteger(0.0);
                            int centery = yaxis.getInteger(0.0);


                            double vf1 = (((double) CX - (double) centerx) * 1.5);
                            double vf2 = (((double) CY - (double) centery) * 1.5);

                            //int nX = (int)vf1;
                            //int nY = (int)vf2;

                            //int nX =(int) ((double)centerx+ vf1);
                            //int nY = (int) ((double)centery+ vf2);

                            int nX = (int) ((double) centerx + vf1);
                            int nY = (int) ((double) centery + vf2);

                            sb.setLength(0);
                            if (us == null) {
                                sb.append(TEXT[i][0]);
                            } else {
                                for (int k = 0; k < TEXT[i].length; k++) {
                                    if (us[k]) {
                                        sb.append(TEXT[i][k]).append("  ");
                                    }
                                }
                            }
                            sb.setLength(sb.length() - 2); //remove the last spaces
                            //g.drawString(sb.toString(),nX,nY);
                            g.drawString(sb.toString(), CX, CY);

                        }
                    }
                }
            }
            g.setFont(before);

//            repaintScale();
        }//fullrepaint...
        FullRepaint = false;



        Graphics tg = null;
        BufferedImage plot2 = null;

        if (!LockFullRepaint) {

            plot2 = new BufferedImage(Width(), Height(), BufferedImage.TYPE_INT_ARGB);
            tg = plot2.getGraphics();
            //tg.drawImage(plot,0,0,null);
            plot2.setData(plot.getRaster());
        } else {
            tg = gr;
        }

//        if (false) {
//            if (!LockFullRepaint) {
//                tg.setClip(valueArea);
//            }
//            drawNeurons(tg);
//        }

        Graphics2D g2d = (Graphics2D) tg;

        g2d.setColor(frameBG);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

        if (mouseDrag) {
            if (frameBG != null && frameBG.getRGB() != Color.white.getRGB()) {
                for (int i = 0; i < paths.size(); i++) {
                    g2d.fill((Shape) paths.elementAt(i));
                }
            }
        }



        Stroke str = g2d.getStroke();
        float[] dashPattern = new float[]{3, 3, 3, 3};
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 10, dashPattern, 0));
        g2d.setColor(new Color(169, 160, 160));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        for (int i = 0; i < paths.size(); i++) {
            g2d.draw((Shape) paths.elementAt(i));
        }

        g2d.setStroke(str);

        if (!LockFullRepaint) {
            gr.drawImage(plot2, 0, 0, null);
        }
        if (sizeMonitor != null) {
            sizeMonitor.setText("Plot Size : (" + getPlotSize().width + "," + getPlotSize().height + ")");
        }

    }

    
    //tohrtr
    public void paintTrend2(Rectangle valueArea, Graphics g, int[] Nx, int[] Ny) {
        if (Nx == null || Nx.length < 1) {
            return;
        }


        Vector polygoon = new Vector();
        int window = (int) (valueArea.width * trendweight);
        int steps = (int) (valueArea.width * trendpoints);

//        int window = 80;
        int[] sr = JIntSorter.quickSort(Nx);

        //      int steps = 10;

        int x = 0;
        int y = 0;

        double mean = 0.0;
        int cnt = 0;
        int n = Nx.length;

        int used = 0;

        int lastx = 0;
        int lasty = 0;

        double maxw = 0.0;
        boolean tStart = true;

        Graphics2D g2d = (Graphics2D) g;


        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(trendwidht));
        g2d.setColor(new Color(trendcolor.getRed(), trendcolor.getGreen(), trendcolor.getBlue(), trendtrans));

        int tmpx = 0;

        for (int i = 0; i < Nx.length; i++) {
            x = Nx[sr[i]];
            if (Double.isNaN(x) || (trendSource != null && !trendSource[i])) {
                continue;
            }
            if (Math.abs(lastx - x) < steps && !tStart) {
                continue;
            }
            tStart = false;
            used = 0;
            mean = 0;

            for (int j = 0; j < window; j++) {
                if (i - j < 0 || i + j >= sr.length) {
                    break;
                }

                if (Double.isNaN(Ny[sr[i - j]]) || (trendSource != null && !trendSource[i - j])) {
                    continue;
                }
                if (Double.isNaN(Ny[sr[i + j]]) || (trendSource != null && !trendSource[i + j])) {
                    continue;
                }
                if (Ny[sr[i - j]] < 0 || Ny[sr[i - j]] > valueArea.height) {
                    continue;
                }
                if (Ny[sr[i + j]] < 0 || Ny[sr[i + j]] > valueArea.height) {
                    continue;
                }




                mean += Ny[sr[i - j]];
                mean += Ny[sr[i + j]];
                used += 2;
            }
            y = (int) (mean / used);

            if (x > 0 && y > 0 && used >= trendminp) {
                polygoon.addElement(new Point(x, y));// g.drawLine(lastx,lasty,x,y);
            }
            lastx = x;
        }



        int[] xx = new int[polygoon.size()];
        int[] yy = new int[polygoon.size()];

        for (int i = 0; i < polygoon.size(); i++) {
            xx[i] = ((Point) polygoon.elementAt(i)).x;
            yy[i] = ((Point) polygoon.elementAt(i)).y;
        }

        g2d.drawPolyline(xx, yy, xx.length);

        g2d.setStroke(new BasicStroke(1.0f));

    }

    public void paintTrend(Rectangle valueArea, Graphics g, int[] Nx, int[] Ny) {
        if (Nx == null || Nx.length < 1) {
            return;
        }
        g.setColor(new Color(40, 40, 240, 100));

        Vector polygoon = new Vector();


        // for(int i=0;i<Ny.length;i++) System.out.print("\n"+Ny[i]);

        int window = (int) (valueArea.width * trendweight);
        int steps = (int) (valueArea.width * trendpoints);


        //int window = valueArea.width/10;

        int[] sr = JIntSorter.quickSort(Nx);

        //int steps = valueArea.width/30;

        int x = 0;
        int y = 0;

        double mean = 0.0;
        int cnt = 0;
        int n = Nx.length;

        int used = 0;

        int lastx = 0;
        int lasty = 0;

        double maxw = 0.0;
        boolean tStart = true;

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(trendwidht));
        g2d.setColor(new Color(trendcolor.getRed(), trendcolor.getGreen(), trendcolor.getBlue(), trendtrans));

        int tmpx1 = 0;
        int tmpx2 = 0;

        for (int i = 0; i < Nx.length; i++) {
            x = Nx[sr[i]];
            if (Double.isNaN(x) || (trendSource != null && !trendSource[sr[i]])) {
                continue;
            }
            if (Math.abs(lastx - x) < steps && !tStart) {
                continue;
            }
            tStart = false;
            used = 0;
            mean = 0;

            //for(int j=0;j<window;j++){
            //if(i-j<0 || i+j>=sr.length) break;
            cnt = 0;


            while (true) {

                if (i - cnt == 0) {
                    break;
                }
                if (i + cnt == sr.length - 1) {
                    break;
                }

                tmpx1 = Nx[sr[i - cnt]];
                tmpx2 = Nx[sr[i + cnt]];

                if (x - tmpx1 > window) {
                    break;
                }
                if (tmpx2 - x > window) {
                    break;
                }


                if (Double.isNaN(Ny[sr[i - cnt]]) || (trendSource != null && !trendSource[sr[i - cnt]])) {
                    cnt++;
                    continue;
                }
                if (Double.isNaN(Ny[sr[i + cnt]]) || (trendSource != null && !trendSource[sr[i + cnt]])) {
                    cnt++;
                    continue;
                }
                if (Ny[sr[i - cnt]] < 0 || Ny[sr[i - cnt]] > valueArea.height) {
                    cnt++;
                    continue;
                }
                if (Ny[sr[i + cnt]] < 0 || Ny[sr[i + cnt]] > valueArea.height) {
                    cnt++;
                    continue;
                }


                //System.out.print("\n"+Ny[sr[i+cnt]]);

                //if(!trendSource[i-cnt])System.out.print("\n*");
                //else System.out.print("\n-");

                mean += Ny[sr[i - cnt]];
                mean += Ny[sr[i + cnt]];
                used += 2;
                cnt++;
                if (cnt == 10000) {
                    break;  //safety.... :)
                }
            }
            y = (int) (mean / used);

            if (x > 0 && y > 0 && used >= trendminp) {
                polygoon.addElement(new Point(x, y));// g.drawLine(lastx,lasty,x,y);
            }
            lastx = x;
        }


        int[] xx = new int[polygoon.size()];
        int[] yy = new int[polygoon.size()];

        for (int i = 0; i < polygoon.size(); i++) {
            xx[i] = ((Point) polygoon.elementAt(i)).x;
            yy[i] = ((Point) polygoon.elementAt(i)).y;
        }

        g2d.drawPolyline(xx, yy, xx.length);

        g2d.setStroke(new BasicStroke(1.0f));


    }

    public void paintCuveTrough(Graphics g, double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {


        int xlen = (int) (x3 - x0);
        int lastx = -1;
        int lasty = -1;

        int nowx = -1;
        int nowy = -1;

        for (int i = 0; i < xlen; i++) {
            double t = (double) i / (double) xlen;
            //use Berstein polynomials
            nowx = (int) ((x0 + t * (-x0 * 3 + t * (3 * x0 - x0 * t))) + t * (3 * x1 + t * (-6 * x1 + x1 * 3 * t)) + t * t * (x2 * 3 - x2 * 3 * t) + x3 * t * t * t);
            nowy = (int) ((y0 + t * (-y0 * 3 + t * (3 * y0 - y0 * t))) + t * (3 * y1 + t * (-6 * y1 + y1 * 3 * t)) + t * t * (y2 * 3 - y2 * 3 * t) + y3 * t * t * t);


            if (lastx > 0 && lasty > 0) {
                g.drawLine(lastx, lasty, nowx, nowy);
            }

            lastx = nowx;
            lasty = nowy;

        }

    }

    public void drawNeurons(Graphics g) {
        //private void paintneurons(Graphics g, double[][] netLocations, int xoffset, int yoffset){

        //BufferedImage bim;
        int neuronsize = 5;
        int xoffset = 0;// left-(neuronsize/2);
        int yoffset = 0;// top-(neuronsize/2);


        int netx = 0, nety = 0, net2x = 0, net2y = 0;
        int[][] Ineurons = new int[2][neurons[0].length];//this.normalize(lastNormWidth,lastNormHeight,netLocations);
        for (int i = 0; i < neurons[0].length; i++) {
            Ineurons[0][i] = xaxis.getInteger(neurons[0][i]);
            Ineurons[1][i] = yaxis.getInteger(neurons[1][i]);
        }

        int n = 0;
        int m = 0;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Stroke oldStroke = g2d.getStroke();

        float[] dashPattern = {2, 2};
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 10, dashPattern, 0));

        if (!hex) {

            //Vertical lines
            g.setColor(new Color(20, 50, 20));
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < m; j++) {
                    netx = Ineurons[0][(j) + (i * m)];
                    nety = Ineurons[1][(j) + (i * m)];

                    net2x = Ineurons[0][(j) + ((i + 1) * m)];
                    net2y = Ineurons[1][(j) + ((i + 1) * m)];

                    g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
                }
            }

            //Horizontal lines
            g.setColor(new Color(10, 130, 10));
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m - 1; j++) {
                    netx = Ineurons[0][(j) + (i * m)];
                    nety = Ineurons[1][(j) + (i * m)];

                    net2x = Ineurons[0][(j) + (i * m) + 1];
                    net2y = Ineurons[1][(j) + (i * m) + 1];

                    g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
                }
            }

        } else {
            //Draw the lines for the hexagonal neuron layer.


            //Horizontal lines
            g.setColor(new Color(10, 130, 10));
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m - 1; j++) {
                    netx = Ineurons[0][(j) + (i * m)];
                    nety = Ineurons[1][(j) + (i * m)];

                    net2x = Ineurons[0][(j) + (i * m) + 1];
                    net2y = Ineurons[1][(j) + (i * m) + 1];

                    g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
                }
            }



            //Vertical lines
            g.setColor(new Color(20, 50, 20));
            for (int i = 0; i < n - 1; i++) {

                for (int j = 0; j < m; j++) {

                    netx = Ineurons[0][(j) + (i * m)];
                    nety = Ineurons[1][(j) + (i * m)];

                    //backward down
                    if (i % 2 == 0) {
                        if (j > 0) {
                            net2x = Ineurons[0][(j) + (i * m) + m - 1];
                            net2y = Ineurons[1][(j) + (i * m) + m - 1];
                            g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
                        }
                    } else {
                        net2x = Ineurons[0][(j) + (i * m) + m];
                        net2y = Ineurons[1][(j) + (i * m) + m];
                        g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
                    }

                    //Forward down..
                    if (i % 2 == 0) {
                        net2x = Ineurons[0][(j) + (i * m) + m];
                        net2y = Ineurons[1][(j) + (i * m) + m];
                        g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
                    } else {
                        if (j < m - 1) {
                            net2x = Ineurons[0][(j) + (i * m) + m + 1];
                            net2y = Ineurons[1][(j) + (i * m) + m + 1];
                            g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
                        }
                    }

                }
            }
        }

        g2d.setStroke(oldStroke);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                netx = Ineurons[0][(i * m) + j];
                nety = Ineurons[1][(i * m) + j];

                if (highLightNeuron != null && i == highLightNeuron.x && j == highLightNeuron.y) {
                    g.setColor(Color.yellow);
                    g.fillOval(-1 + netx + xoffset - (neuronsize / 2), -1 + nety + yoffset - (neuronsize / 2), neuronsize, neuronsize);
                    g.setColor(Color.black);
                    g.drawOval(-1 + netx + xoffset - (neuronsize / 2), -1 + nety + yoffset - (neuronsize / 2), neuronsize, neuronsize);
                    //g.drawRect(netx-6+xoffset,nety-6+yoffset,13,13);
                } else {
                    g.setColor(Color.red);
                    g.fillOval(-1 + netx + xoffset - (neuronsize / 2), -1 + nety + yoffset - (neuronsize / 2), neuronsize, neuronsize);
                    g.setColor(Color.black);
                    g.drawOval(-1 + netx + xoffset - (neuronsize / 2), -1 + nety + yoffset - (neuronsize / 2), neuronsize, neuronsize);
                }
            }
        }

        // Stroke s = g2d.getStroke();
        // g2d.setStroke(oldStroke);
        // g.setColor(Color.black);

    }

    public void setColors(Color[] dotColors) {
        this.dotColors = dotColors;
    }

    public Image getImage() {



        if (getWidth() == 0 || getHeight() == 0) {
            return null;
        }
        BufferedImage bim = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics gg = bim.getGraphics();
        paintComponent(gg);
        return bim;

    }

    public void setInActiveGroup(boolean[] inActiveGroup) {
        this.inActiveGroup = inActiveGroup;
    }

    public boolean[] getTolerated() {
        return bgf.getTolerated();
    }

    public void updateForm() {
        Layout.put("mixticsSV", xaxis.minor_tic_count);
        Layout.put("miyticsSV", yaxis.minor_tic_count);
        Layout.put("paintGridSV", (xaxis.paintGrid & yaxis.paintGrid));
        Layout.put("gridColorSV", yaxis.gridcolor);



        // sp.mixticsSV.setValue(xaxis.minor_tic_count);
        // sp.miyticsSV.setValue(yaxis.minor_tic_count);
        // sp.paintGridSV.setSelected(xaxis.paintGrid & yaxis.paintGrid);
        // sp.paintGridSV.setSelected(xaxis.paintGrid & yaxis.paintGrid);
        // sp.gridColorSV.setBackground(yaxis.gridcolor);



        if (xaxis.getTitleText() != null) {
            Layout.put("xaxisTitle", xaxis.getTitleText());
        }
        if (yaxis.getTitleText() != null) {
            Layout.put("yaxisTitle", yaxis.getTitleText());
        }

        //sp.Xaxis.setText(xaxis.getTitleText());
        //sp.Yaxis.setText(yaxis.getTitleText());

    }

    public void setTitle(String title) {

        //   sp.Header.setText(title);
        Layout.put("topText", title);
        topText.setText(title);

    }

      @SuppressWarnings("UnnecessaryUnboxing")
    public void readForm() {
        topText.setText((String) Layout.get("topText"));//setText(sp.Header.getText());


        yaxis.correctForCloseValues = true;
        xaxis.correctForCloseValues = true;

        xaxis.minor_tic_count = ((Integer) Layout.get("mixticsSV"));//sp.mixticsSV.getValue();
        yaxis.minor_tic_count = ((Integer) Layout.get("miyticsSV"));//sp.miyticsSV.getValue();

        xaxis.paintGrid = ((Boolean) Layout.get("paintGridSV"));//yaxis.paintGrid=sp.paintGridSV.isSelected();
        yaxis.paintGrid = ((Boolean) Layout.get("paintGridSV"));//yaxis.paintGrid=sp.paintGridSV.isSelected();
        xaxis.gridcolor = (Color) Layout.get("gridColorSV");//sp.gridColor.getColor();
        yaxis.gridcolor = (Color) Layout.get("gridColorSV");//sp.gridColor.getColor();

        xaxis.zerocolor = (Color) Layout.get("nullSV");//sp.nullSV.getColor();
        yaxis.zerocolor = (Color) Layout.get("nullSV");//sp.nullSV.getColor();

        //if(sp.Xaxis.getText().length()>0)
        xaxis.setTitleText((String) Layout.get("xaxisTitle"));//sp.Xaxis.getText());
        //if(sp.Yaxis.getText().length()>0)
        yaxis.setTitleText((String) Layout.get("yaxisTitle"));//sp.Yaxis.getText());

        xaxis.TICS_IN_BOTH_ENDS = ((Boolean) Layout.get("xticsbothsidesSV"));//sp.xticsbothsidesSV.isSelected();
        yaxis.TICS_IN_BOTH_ENDS = ((Boolean) Layout.get("yticsbothsidesSV"));//sp.yticsbothsidesSV.isSelected();

        xaxis.transparency = ((Integer) Layout.get("transparencySV"));//sp.transparencySV.getValue();
        yaxis.transparency = ((Integer) Layout.get("transparencySV"));//sp.transparencySV.getValue();

        //bgf.setDensColors(sp.col1SV.getColor(),sp.col2SV.getColor(),sp.col3SV.getColor(),sp.col4SV.getColor(),sp.col5SV.getColor());

        bgf.setDensColors((Color) Layout.get("col1SV"), (Color) Layout.get("col2SV"), (Color) Layout.get("col3SV"), (Color) Layout.get("col4SV"), (Color) Layout.get("col5SV"));
        //sp.interpolator.setColors((Color)Layout.get("col1SV"),(Color)Layout.get("col2SV"),(Color)Layout.get("col3SV"),(Color)Layout.get("col4SV"),(Color)Layout.get("col5SV"));
        //sp.interpolator.setColors(sp.col1SV.getColor(),sp.col2SV.getColor(),sp.col3SV.getColor(),sp.col4SV.getColor(),sp.col5SV.getColor());


        bgf.setDensArea(((Integer) Layout.get("densareaSV")));//sp.densareaSV.getValue());
        bgf.setDensTolerance(((Integer) Layout.get("tresholdSV")));//sp.tresholdSV.getValue());
        bgf.setNumColors(((Integer) Layout.get("colorsSV")));//sp.colorsSV.getValue());

        bgf.GradientType = ((Integer) Layout.get("gradtypeSV"));//sp.gradtypeSV.getSelectedIndex();
        bgf.Single = (Color) Layout.get("singlebgSV");// sp.singlebgSV.getColor();
        bgf.gradient1 = (Color) Layout.get("grad1SV");//sp.grad1SV.getColor();
        bgf.gradient2 = (Color) Layout.get("grad2SV");//sp.grad2SV.getColor();

        //dotColor = sp.dotcolorSV.getColor();

        dotsize = ((Integer) Layout.get("dotsizeSV"));//sp.dotsizeSV.getValue();
        setBackground((Color) Layout.get("chartbgSV"));//sp.chartbgSV.getColor());


        boolean resetxAxis = false;
        boolean resetyAxis = false;

        double xmin = ((Double) Layout.get("xmin"));//sp.xmin.getValue();
        double xmax = ((Double) Layout.get("xmax"));//sp.xmax.getValue();
        double ymin = ((Double) Layout.get("ymin"));//sp.ymin.getValue();
        double ymax = ((Double) Layout.get("ymax"));//sp.ymax.getValue();

        if (!this.equalScales) {
            if (ymin != ymax && xmin != xmax) {

                if (ymin != yaxis.minimum) {
                    resetyAxis = true;
                }
                if (ymax != yaxis.maximum) {
                    resetyAxis = true;
                }
                if (xmin != xaxis.minimum) {
                    resetxAxis = true;
                }
                if (xmax != xaxis.maximum) {
                    resetxAxis = true;
                }

                if (resetxAxis) {
                    xaxis.force_end_labels = ((Boolean) Layout.get("endlabelsSV"));//sp.endlabelsSV.isSelected();
                    xaxis.minimum = xmin;
                    xaxis.maximum = xmax;
                }
                if (resetyAxis) {
                    yaxis.force_end_labels = ((Boolean) Layout.get("endlabelsSV"));//sp.endlabelsSV.isSelected();
                    yaxis.minimum = ymin;
                    yaxis.maximum = ymax;
                }
            }
        }

        // int trendline = 0;
        boolean showtrend = false;
        boolean nump = false;

        if (Layout.containsKey("showTrendSV")) {
            showtrend = ((Boolean) Layout.get("showTrendSV"));
        }

        if (showtrend) {
            if (Layout.containsKey("numpSV")) {
                nump = ((Boolean) Layout.get("numpSV"));
            }
            if (nump) {
                trendline = 1;
            } else {
                trendline = 2;
            }
        }


        if (Layout.containsKey("pointsSV")) {
            trendpoints = ((Double) Layout.get("pointsSV"));
        }

        if (Layout.containsKey("weightSV")) {
            trendweight = ((Double) Layout.get("weightSV"));
        }


        if (Layout.containsKey("trendtransSV")) {
            trendtrans = ((Integer) Layout.get("trendtransSV"));
        }

        if (Layout.containsKey("widthSV")) {
            trendwidht = (float) ((Double) Layout.get("widthSV")).doubleValue();
        }

        if (Layout.containsKey("trendcSV")) {
            trendcolor = (Color) Layout.get("trendcSV");
        }

        if (Layout.containsKey("minpSV")) {
            trendminp = (int) ((Double) Layout.get("minpSV")).doubleValue();
        }




    }

    public void updateAxisValues() {
        if (Layout == null) {
//            System.out.println("Layout == null "+Layout == null);
            return;
        }
//        System.out.println("ymin "+ new Double(yaxis.minimum)+"   ymax  "+ new Double(yaxis.maximum)+" xmin "+ new Double(xaxis.minimum)+" xmax "+ new Double(xaxis.maximum));
        Layout.put("ymin", yaxis.minimum);
        Layout.put("ymax", yaxis.maximum);
        Layout.put("xmin", xaxis.minimum);
        Layout.put("xmax", xaxis.maximum);
        

        //sp.ymin.setValue(yaxis.minimum);
        //sp.ymax.setValue(yaxis.maximum);
        //sp.xmin.setValue(xaxis.minimum);
        //sp.xmax.setValue(xaxis.maximum);
    }

    public void writeValues() {
        //System.out.print("\nWriting chart as "+plotName);
        if (props == null) {
            System.out.print("\nWARNING, No properties read for plot");
        }
        if (plotName == null) {
            System.out.print("\nWARNING, No plotName for plot");
        }
        if (Layout == null) {
            System.out.print("\nWARNING, No Layout Name for plot");
        }

        if (plotName != null && Layout != null) {
            props.put(this.plotName, Layout);
        }




        //tools.writeProperties(props);


        //if(props!=null)to.writedialogStatus(sp,plotName,props);
    }

    public final void readValues() {
        //props.remove(plotName);
        //tools.writeProperties(props);

        //@TODO: reimplement me 
//        int size = props.size();

        // @TODO: reimlement me?
//        if (props.containsKey("suite")) {
//            props = ((GenepixSuite) props.get("suite")).getProps();
//        }

//        //OLD
//        if (props.containsKey(plotName)) {


        //System.out.print("\nReading chart as "+plotName);

        //OLD
//            this.Layout = (Hashtable) ((Hashtable) props.get(plotName)).clone();
        //NEW
        
        

        //OLD
//        } 

        //OLD (the if-part of the above code is commented out, so the else-structure is illegal
//        else {
//
//            if (!props.containsKey("Scatterplot")) {
//                System.out.print("\nUnable to find Scatterplot default values..");
//            }
//
//            this.Layout = (Hashtable) ((Hashtable) props.get("Scatterplot")).clone();//(Hashtable)props.get("Scatterplot");
//            //if(Layout==null)
//            System.out.print("\nReading chart as default instead of name " + plotName);
//            writeValues();
//        }


        if (Layout != null) {
            Layout.put("yaxisTitle", "");
            Layout.put("xaxisTitle", "");
            Layout.put("topText", "");
        }
        //tools to = new tools();
        //if(props!=null)to.readialogStatus(sp,plotName,props);
        forceFullRepaint();
    }
    
    //@TODO: reimplement me?
//    public static void main(String[] args) {
//
//        double[] Tx = new double[]{0.0021, -0.0004, -000.15, -00.0004, 0.0021, -0.0014, -0.003, -0.0007, -0.0036, -0.0014, -0.00042, -0.0034, -0.00023, -0.0017}; //Raw X
//        double[] Ty = new double[]{0.0016, -0.0034, -000.32, -0.0034, -0.0012, -0.00034, -0.0027, -0.00015, -0.0015, -0.0051, -0.003, -0.00025, -0.00012, -0.004}; //Raw Y
//
//
//        expresscomponents.DataSet data = jexpress.cluster.loadDataSet("C:/projects/jexpress/resources/testproject.pro", "Raw data");//"alpha1-18_2");//"nci60_fig3_nomiss.txt","Baldwin_alltimecourses_nomiss.txt","nci60_fig3_nomiss.txt","Alizadeh_65col_nomiss.txt"
//
//        double[] x = new double[data.getDataLength()];
//        double[] y = new double[data.getDataLength()];
//
//        double[][] dt = data.getData();
//
//        for (int i = 0; i < x.length; i++) {
//            x[i] = dt[i][2];
//            y[i] = dt[i][3];
//        }
//
//
//        Tx = x;
//        Ty = y;
//
//
//
//        Hashtable props = null;
//
//        try {
//            java.io.FileInputStream istream = new java.io.FileInputStream("jexpress.cfg");
//            ObjectInputStream p = new ObjectInputStream(istream);
//            props = (java.util.Hashtable) p.readObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        final DensScatterPlot pl = new DensScatterPlot();
//
//        pl.setPropsAndData(props, Tx, Ty);
//
//        //final DensScatterPlot pl = new DensScatterPlot(props,null,null);
//
//
//        JFrame f = new JFrame();
//        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
//
//        f.getContentPane().add("Center", pl);
//
//        pl.setSize(700, 400);
//        f.setPreferredSize(new Dimension(600, 600));
//
//        JButton b = new JButton("test");
//        b.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                // these are temporary!
//
//                pl.printImage();
//
//                //  pl.setData(Tx,Ty);
//                //pl.repaint();
//            }
//        });
//
//        f.getContentPane().add("South", b);
//
//
//        f.pack();
//        f.setVisible(true);
//
//    }

    @Override
    public JToolTip createToolTip() {

        return (new JeToolTip(this));
    }

    /**
     *
     */
    @Override
      @SuppressWarnings({"FinalizeDeclaration", "CallToPrintStackTrace"})
    public void finalize() {
        //This is just used for garbage collecting monitoring
        //System.out.print("Collected Scatterplot..");
        //this.sp.dispose();
        try {
            super.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

  
    
    public void setDotSize(int dotSize) {
        Layout.put("dotsizeSV", dotSize);
        this.dotsize = dotSize;
    }

    public void setFrameDots(boolean frame) {
        Layout.put("frameSV", frame);
    }

    public void zoom(double[] frame) {
        zoomedArea = frame;
        //    xaxis.minimum=frame[0];
        //    xaxis.maximum=frame[1];
        //    yaxis.minimum=frame[2];
        //    yaxis.maximum=frame[3];




        Layout.put("xmin", frame[0]);
        Layout.put("xmax", frame[1]);
        Layout.put("ymin", frame[2]);
        Layout.put("ymax", frame[3]);

        //sp.xmin.setValue(frame[0]);
        //sp.xmax.setValue(frame[1]);
        //sp.ymin.setValue(frame[2]);
        //sp.ymax.setValue(frame[3]);

        //   yaxis.setManualRange(true);
        yaxis.force_end_labels = ((Boolean) Layout.get("endlabelsSV"));//sp.endlabelsSV.isSelected();

        //   xaxis.setManualRange(true);
        xaxis.force_end_labels = ((Boolean) Layout.get("endlabelsSV"));//sp.endlabelsSV.isSelected();

        FullRepaint = true;
        paths.clear();
        mouseDrag = false;
        repaint();
    }
//

    public double[] getZoomedArea() {
        return zoomedArea;
    }

   

    

    
    /**
     * Set the lower right corner of the sweep frame
     *
     * @return 
     */
    public String getFrameDescription() {

        final java.text.NumberFormat form = java.text.NumberFormat.getNumberInstance();
        form.setMinimumFractionDigits(3);

        StringBuilder b = new StringBuilder();
        Rectangle tmp = null;
        for (int j = 0; j < paths.size(); j++) {

            if (paths.elementAt(j) instanceof Rectangle) {
                tmp = (Rectangle) paths.elementAt(j);
                b.append("Rectangle X1:");
                b.append(form.format(xaxis.getDouble(tmp.x)));
                b.append(" Y1:");
                b.append(form.format(yaxis.getDouble(tmp.y)));
                b.append(" X2:");
                b.append(form.format(yaxis.getDouble(tmp.x + tmp.width)));
                b.append(" Y2:");
                b.append(form.format(yaxis.getDouble(tmp.y + tmp.height)));
            } else {
                b.append("NON-Rectangular shape");
            }

            if (j < paths.size() - 1) {
                b.append(" + ");
            }
        }
        return b.toString();
    }

    public boolean[] getFramedIndexes() {

        boolean[] ret = new boolean[Nx.length];

        boolean[] tolerated = bgf.getTolerated();

//        if (frameType == 0) {
//            int sqxstart = Math.min(startx, endx);
//            int sqystart = Math.min(starty, endy);
//            int sqxend = (Math.max(startx, endx) - Math.min(startx, endx));
//            int sqyend = (Math.max(starty, endy) - Math.min(starty, endy));
////            sweepFrom = new Point(sqxstart, sqystart);
////            sweepTo = new Point(sqxend, sqyend);
//        }
        for (int i = 0; i < ret.length; i++) {

            //System.out.print("\n*");

            for (int j = 0; j < paths.size(); j++) //if( (dotColors!=null && dotColors[i]!=null) && (tolerated==null || tolerated[i]) && ((Shape)paths.elementAt(j)).contains(Nx[i], Ny[i])){
            {
                if ((tolerated == null || tolerated[i]) && ((Shape) paths.elementAt(j)).contains(Nx[i], Ny[i])) {
                    if (visible == null || visible[i]) {
                        ret[i] = true;
                    }

                }
            }

        }

        return ret;
    }

//    public void setScaleLabel(final JLabel container, int height, boolean text) {
//        scaleLabel = container;
//
//
//        final JPopupMenu jp = new JPopupMenu();
//
//        JMenuItem i1 = new JMenuItem("Save Scale Image");
//        JMenuItem i2 = new JMenuItem("Copy Scale Image");
//
//        i1.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                Component owner = DensScatterPlot.this.getRootPane().getTopLevelAncestor();
//                JComponent c = new JComponent() {
//
//                    public void paintComponent(Graphics g) {
//                        paintScale(container.getWidth(), container.getSize().height, true, g);
//                    }
//                };
//                c.setPreferredSize(container.getPreferredSize());
//                JFrame f = new JFrame();
//                f.getContentPane().add(c);
//                f.validate();
//                f.pack();
//
//                //@TODO: reimplement me ?
////                org.freehep.util.export.ExportDialog export = new org.freehep.util.export.ExportDialog("Export");
////                export.showExportDialog(owner, "Export view as ...", c, "export");
//
//                f.dispose();
//            }
//        });
//
//        i2.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                JComponent c = new JComponent() {
//
//                    public void paintComponent(Graphics g) {
//                        paintScale(container.getWidth(), container.getSize().height, true, g);
//                    }
//                };
//                c.setPreferredSize(container.getPreferredSize());
//                JFrame f = new JFrame();
//                f.getContentPane().add(c);
//                f.validate();
//                f.pack();
//                Component owner = DensScatterPlot.this.getRootPane().getTopLevelAncestor();
//
//                //@TODO: reimplement me ?
////                org.freehep.util.export.ExportDialog export = new org.freehep.util.export.ExportDialog();
////                org.freehep.util.export.VectorGraphicsTransferable vt = new org.freehep.util.export.VectorGraphicsTransferable(c);
//
//                java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
//
//                //@TODO: reimplement me ?
////                clipboard.setContents(vt, vt);
//
//                f.dispose();
//
//            }
//        });
//
//
//        jp.add(i1);
//        jp.add(i2);
//
//
//        container.addMouseListener(new MouseAdapter() {
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                if (!(javax.swing.SwingUtilities.isRightMouseButton(e))) {
//                    return;
//                }
//                jp.show(container, e.getX(), e.getY());
//            }
//        });
//
//
//        scaleLabel.getParent().setBackground(getBackground());
//
//        if (scaleLabel.getWidth() == 0 || height == 0) {
//            return;
//        }
//        scaleLabel.setIcon(new ImageIcon(getScale(scaleLabel.getWidth(), height, true)));
//
//        //return scaleIcon;
//    }
//
//    public void setVariancePanel(JPanel VariancePanel) {
//        this.VariancePanel = VariancePanel;
//    }
//     public void repaintScale(JLabel scaleLabel) {
//         this.scaleLabel = scaleLabel;
//        int showScale = ((Integer) Layout.get("SbgSV")).intValue();//sp.SbgSV.getSelectedIndex();
//
//
//        if (showScale != 0) {
//            if (scaleLabel != null) {
//                scaleLabel.setBackground(getBackground());
//
//                BufferedImage scale = new BufferedImage(scaleLabel.getWidth(), 50, BufferedImage.TYPE_INT_ARGB);
//                Graphics g = scale.getGraphics();
//                g.setColor(getBackground());
//                g.fillRect(0, 0, scaleLabel.getWidth(), 50);
//
//
//                if (scaleLabel != null) {
//                    scaleLabel.setIcon(new ImageIcon(scale));
//                }
//            }
//            return;
//        }
//
//        if (scaleLabel != null) {
//            scaleLabel.getParent().setBackground(getBackground());
//            scaleLabel.setBackground(getBackground());
//
//        }
//
//        if (scaleLabel == null || scaleLabel.getWidth() == 0) {
//            return;
//        }
//        //pd.colorscale.setIcon(pd.plot.getScale(200, 50, true));
//        // scaleIcon.setImage(paintScale(200,50,true));
//        //scaleLabel.setImage(paintScale(scaleLabel.getWidth(),height,text));
//        scaleLabel.setIcon(new ImageIcon(getScale(scaleLabel.getWidth(), 50, true)));
//    }
//
//    public void repaintScale() {
//        int showScale = ((Integer) Layout.get("SbgSV")).intValue();//sp.SbgSV.getSelectedIndex();
//
//
//        if (showScale != 0) {
//            if (scaleLabel != null) {
//                scaleLabel.setBackground(getBackground());
//
//                BufferedImage scale = new BufferedImage(scaleLabel.getWidth(), 50, BufferedImage.TYPE_INT_ARGB);
//                Graphics g = scale.getGraphics();
//                g.setColor(getBackground());
//                g.fillRect(0, 0, scaleLabel.getWidth(), 50);
//
//
//                if (scaleLabel != null) {
//                    scaleLabel.setIcon(new ImageIcon(scale));
//                }
//            }
//            return;
//        }
//
//        if (scaleLabel != null) {
//            scaleLabel.getParent().setBackground(getBackground());
//            scaleLabel.setBackground(getBackground());
//
//        }
//
//        if (scaleLabel == null || scaleLabel.getWidth() == 0) {
//            return;
//        }
//        //pd.colorscale.setIcon(pd.plot.getScale(200, 50, true));
//        // scaleIcon.setImage(paintScale(200,50,true));
//        //scaleLabel.setImage(paintScale(scaleLabel.getWidth(),height,text));
//        scaleLabel.setIcon(new ImageIcon(getScale(scaleLabel.getWidth(), 50, true)));
//    }
//
//    public BufferedImage getScale(int width, int height, boolean text) {
//        BufferedImage scale = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//
//        Graphics g = scale.getGraphics();
//        paintScale(width, height, text, g);
//        return scale;
//    }
//
//    public void paintScale(int width, int height, boolean text, Graphics g) {
//
//        g.setColor(getBackground());
//        g.fillRect(0, 0, width, height);
//
//        if (this.factory == null) {
//            return;
//        }
//
//        ScaleAndAxis sc = new ScaleAndAxis();
//        sc.setForceNoValues(true);
//        sc.setPreviewMode(true);
//        sc.setColorFactory(factory);
//        sc.setSize(width, height);
//
//        sc.paintComponent(g);
//
//
//        /*
//         * int textSpace=0; int x=0; int y=0;
//         *
//         * //BufferedImage tempim=new BufferedImage(width,height,
//         * BufferedImage.TYPE_INT_ARGB); //Graphics tempg=tempim.getGraphics();
//         *
//         * String max="Max",min="Min",density="Density"; if(text){ textSpace=20;
//         * }
//         *
//         *
//         * int colw=width-20,colh=20; int[] colorlist =
//         * getInterpolatedColors(colw,(Color)Layout.get("col1SV"),(Color)Layout.get("col2SV"),(Color)Layout.get("col3SV"),(Color)Layout.get("col4SV"),(Color)Layout.get("col5SV"));//sp.col1SV.getColor(),sp.col2SV.getColor(),sp.col3SV.getColor(),sp.col4SV.getColor(),sp.col5SV.getColor());
//         * int[] colors = new int[colw*colh];
//         *
//         * for(int i=0;i<colors.length;i++){ colors[i] = colorlist[i%colw]; }
//         *
//         * g.setColor(new Color(colors[0])); g.setColor(new
//         * Color(colors[colors.length-1]));
//         *
//         * BufferedImage scale = new BufferedImage(colw,colh,
//         * BufferedImage.TYPE_INT_ARGB);
//         *
//         * //scale.setRGB(10,19,colw,colh,colors,0,colw);
//         * scale.setRGB(0,0,colw,colh,colors,0,colw);
//         *
//         * g.drawImage(scale,10,19,this);
//         *
//         * Font old = g.getFont();
//         *
//         * Graphics2D g2d = (Graphics2D)g;
//         * g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING ,
//         * RenderingHints.VALUE_ANTIALIAS_ON );
//         *
//         *
//         * FontMetrics fm = g.getFontMetrics(); g.setColor(getForeground());
//         * g.drawString(min, 2,12); g.drawString(max, (width) -
//         * (fm.stringWidth(max)) -4 ,12);
//         *
//         * int minend = fm.stringWidth(min); int maxStart = (width) -
//         * (fm.stringWidth(max)) -2;
//         *
//         * int left = maxStart-minend; int densityLength=
//         * fm.stringWidth(density);
//         *
//         * if(left>densityLength+20){
//         *
//         * int densityLocation=(int)((width/2.0)-(densityLength/2.0));
//         *
//         * g.setFont(new Font("Times New Roman",1,14)); g.drawString(density,
//         * densityLocation ,12); g.setFont(old); }
//         *
//         *
//         * //g.drawRect(0,0,width-1,height-1);
//         *
//         * g.setColor(new Color(100,100,100));
//         * g.drawRect(10,19,scale.getWidth(),scale.getHeight());
//         * //g.drawImage(tempim,x,y,this);
//         *
//         * //return scale;
//         */
//    }

    public int[] getInterpolatedColors(int length, Color color1, Color color2, Color color3, Color color4, Color color5) {

        int[] ret = new int[length];

        Dimension d = this.getSize();
        int frameheight = d.height;
        int framewidth = d.width;

        Color temp = null;
        int newred = 0;
        int newgreen = 0;
        int newblue = 0;

        double[] red = new double[5];
        double[] green = new double[5];
        double[] blue = new double[5];

        red[0] = (double) color1.getRed();
        green[0] = (double) color1.getGreen();
        blue[0] = (double) color1.getBlue();

        red[1] = (double) color2.getRed();
        green[1] = (double) color2.getGreen();
        blue[1] = (double) color2.getBlue();

        red[2] = (double) color3.getRed();
        green[2] = (double) color3.getGreen();
        blue[2] = (double) color3.getBlue();

        red[3] = (double) color4.getRed();
        green[3] = (double) color4.getGreen();
        blue[3] = (double) color4.getBlue();

        red[4] = (double) color5.getRed();
        green[4] = (double) color5.getGreen();
        blue[4] = (double) color5.getBlue();

        double dqlength = (((double) length + 1.0) / 4.0);

        double tmp = 0, tmp2 = 0;
        int itmp = 0, itmp2 = 0;

        for (int i = 0; i < ret.length; i++) {

            tmp = ((double) i) % (dqlength);
            tmp2 = ((double) i) / dqlength;

            itmp = (int) tmp;
            itmp2 = (int) Math.floor(tmp2);

            newred = (int) ((double) red[itmp2] + (((double) (red[itmp2 + 1] - red[itmp2]) / dqlength) * tmp));
            newgreen = (int) ((double) green[itmp2] + (((double) (green[itmp2 + 1] - green[itmp2]) / dqlength) * tmp));
            newblue = (int) ((double) blue[itmp2] + (((double) (blue[itmp2 + 1] - blue[itmp2]) / dqlength) * tmp));

            ret[i] = new Color(newred, newgreen, newblue).getRGB();

        }

        return ret;
    }

      @SuppressWarnings("null")
    public boolean[] addInvalidValues(boolean[] x, boolean[] y) {
        if (x == null && y != null) {
            return y;
        } else if (x != null && y == null) {
            return x;
        } else if (x == null && y == null) {
            return null;
        } else {
            for (int i = 0; i < x.length; i++) {
                x[i] = x[i] | y[i];
            }
            return x;
        }
    }

    public int getInvalidValues() {
        if (invalidValues == null) {
            return 0;
        } else {
            int ret = 0;
            for (int i = 0; i < invalidValues.length; i++) {
                if (invalidValues[i]) {
                    ret++;
                }
            }
            return ret;
        }
    }

    /**
     * Getter for property plotName.
     *
     * @return Value of property plotName.
     */
    public String getPlotName() {
        return plotName;
    }

    /**
     * Setter for property plotName.
     *
     * @param plotName New value of property plotName.
     */
    public void setPlotName(java.lang.String plotName) {
        this.plotName = plotName;
    }

    public double getDeltaLines() {
        return deltaLines;
    }

    public void setDeltaLines(double deltaLines) {
        this.deltaLines = deltaLines;
    }
    
    /*
     * NOTE: The follwing methods are methods that (try!) to mimic methods that the old DataSet class has, so 
     * that the method-calls in this class to old methods don't have to be changed much (especially
     * with regards to datatypes that are used and returned by these methods)
     */

    /**
     * Method for getting row info headers on the old format, ie the one used in
     * J-Express (array of Strings). </p> Method is a reworked version of a
     * method from the old DataSet class (in JExpress)
     */
//    private static String[] getInfoHeaders(no.uib.jexpress_modularized.core.dataset.DataSet m_data) {
//        AnnotationManager am = AnnotationManager.getAnnotationManager();
//        Set<String> rowAnnotations = m_data.getRowAnnotationNamesInUse();
//        if (rowAnnotations == null) {
//            rowAnnotations = am.getManagedColumnAnnotationNames();
//        }
//        String[] rowHeaders = null;
//        if (rowAnnotations.isEmpty()) {
//            rowHeaders = new String[]{"Column ID"};
//        } else {
//            rowHeaders = rowAnnotations.toArray(new String[rowAnnotations.size()]);
//        }
//        return rowHeaders;
//    }

    /**
     * Method for getting column info headers on the old format, ie the one used
     * in J-Express (array of Strings).
     */
//    private static String[] getColInfoHeaders(no.uib.jexpress_modularized.core.model.DataSet m_data) {
//        AnnotationManager am = AnnotationManager.getAnnotationManager();
//        Set<String> columnAnnotations = m_data.getColumnAnnotationNamesInUse();
//        if (columnAnnotations == null) {
//            columnAnnotations = am.getManagedColumnAnnotationNames();
//        }
//        String[] colHeaders = null;
//        if (columnAnnotations.isEmpty()) {
//            colHeaders = new String[]{"Column ID"};
//        } else {
//            colHeaders = columnAnnotations.toArray(new String[columnAnnotations.size()]);
//        }
//        return colHeaders;
//    }
//
//    /**
//     * Only returns an array with all the rows marked as used (each cell in the
//     * array has the value of
//     * <code>true</code>). </p> Method is a reworked version of a method from
//     * the old DataSet class (in JExpress)
//     */
//    private static boolean[] getusedInfos(no.uib.jexpress_modularized.core.dataset.DataSet m_data) {
//        boolean[] usedInfos = new boolean[getInfoHeaders(m_data).length + 2];
//        java.util.Arrays.fill(usedInfos, true);
//        return usedInfos;
//    }

    /**
     * Only returns an array with all the rows marked as used (each cell in the
     * array has the value of
     * <code>true</code>).
     */
//    private static boolean[] getusedColInfos(no.uib.jexpress_modularized.core.model.DataSet m_data) {
//        boolean[] usedColInfos = new boolean[getColInfoHeaders(m_data).length + 2];
//        java.util.Arrays.fill(usedColInfos, true);
//        return usedColInfos;
//    }

    /**
     * Method that returns the annotations for the columns of the DataSet. This
     * method has the same name as the method in the old DataSet class
     * (J-Express), and returns the annotations on the same format as the old
     * DataSet class. This method is only used for legacy-methods that rely on
     * the old way of storing annotations (for the new way for handling
     * annotations on columns and rows on datasets, see AnnotationLibrary and
     * AnnotationManager in the core package).
     */
//    private static String[][] getColInfos(no.uib.jexpress_modularized.core.model.DataSet m_data) {
//        AnnotationManager am = AnnotationManager.getAnnotationManager();
//        Set<String> annotations = m_data.getColumnAnnotationNamesInUse();
//        if (annotations == null) {
//            annotations = am.getManagedColumnAnnotationNames();
//        }
//
//        String[] columnIds = m_data.getColumnIds();
//        String[][] all; //column annotation matrix
//        if (annotations.isEmpty()) {
//            assert m_data.getDataWidth() == columnIds.length : "length of column ids and width of dataset should be equal (?)";
//            all = new String[m_data.getDataWidth()][1];
//            for (int i = 0; i < all.length; i++) {
//                all[i][0] = columnIds[i];
//            }
//        } else {
//            String[] headers = annotations.toArray(new String[annotations.size()]);
//            all = new String[m_data.getDataWidth()][annotations.size()];
//            for (int i = 0; i < headers.length; i++) {
//                AnnotationLibrary anns = am.getColumnAnnotations(headers[i]);
//                for (int j = 0; j < all.length; j++) {
//                    all[j][i] = anns.getAnnotation(columnIds[j]);
//                }
//            }
//        }
//        return all;
//    }
    
    public void clearPath()
    {
        this.paths.clear();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //start
    
    
    
    
    
    
    
    
//    private BufferedImage plot;
//    private int LegendWidth;
//    private Rectangle valueArea ;
//    private final BackgroundFactory bgf = new BackgroundFactory();
//    private ColorFactory factory;
//    private boolean FormUpdated = false;
//    private String[][] SpotNames = null;
//    private Vector lines;
//    private Vector lineColors;
//    private double[][] line; 
//    public PcaPlot(){
//        super();
//        resetAxis();
//    }
//    
//    @Override
//     public void forceFullRepaint() {
//        FullRepaint = true;
//        repaint();
//    }
//    @Override
//      public void setLine(double[][] line) {
//        this.line = line;
//    }
//
//     @Override
//      public void setLIneColors(Vector lineColors) {
//        this.lineColors = lineColors;
//    }
//     @Override
//     public void setLines(Vector lines) {
//        this.lines = lines;
//    }
//    @Override
//    public void setSpotNames(String[][] SpotNames) {
//        this.SpotNames = SpotNames;
//    }
//    @Override
//     public void setColorFactory(ColorFactory factory) {
//        this.factory = factory;
//    }
//    
//    @Override
//    public void paintComponent(Graphics gr) {        
//       
//        readForm();
//        setForeground(new Color(0, 51, 153));
//
//        if (yaxis.getTitleText().startsWith("Log")) {
//            if (yaxis.getTitleText().charAt(3) == '2') {
//            } else if (yaxis.getTitleText().charAt(3) == '1') {
//            } else {
//                String tit = yaxis.getTitleText().substring(0, 3);
//                if (yaxis.useLog2) {
//                    tit += "2";
//                } else {
//                    tit += "10";
//                }
//
//                tit += yaxis.getTitleText().substring(3, yaxis.getTitleText().length());
//                yaxis.setTitleText(tit);
//            }
//        }
//
//        if (xaxis.getTitleText().startsWith("Log")) {
//            if (xaxis.getTitleText().charAt(3) == '2') {
//            } else if (xaxis.getTitleText().charAt(3) == '1') {
//            } else {
//                String tit = xaxis.getTitleText().substring(0, 3);
//                if (xaxis.useLog2) {
//                    tit += "2";
//                } else {
//                    tit += "10";
//                }
//
//                tit += xaxis.getTitleText().substring(3, xaxis.getTitleText().length());
//                xaxis.setTitleText(tit);
//            }
//        }
//
//
//
//        if (FullRepaint || LockFullRepaint) {
//
//            Graphics g = null;
//
//            if (!LockFullRepaint) {
//                plot = new BufferedImage(Width(), Height(), BufferedImage.TYPE_INT_ARGB);
//                g = plot.getGraphics();
//            } else {
//                g = gr;
//            }
//
//
//
//            int TitleHeight = topText.getHeight(g);
//
//            int HTitlePos = Width() / 2 - (topText.getWidth(g) / 2);
//
//            if (Width() == 0 || Height() == 0) {
//                return;
//            }
//           
//            g.setColor(getBackground());
//            if (this.isOpaque()) {
//                g.fillRect(0, 0, Width(), Height());
//            }
//
//
//
//
//            g.setColor(Color.black);
//            if (topText.getText() != null && topText.getText().length() > 0) {
//                topText.draw(g, HTitlePos, top + 6);
//            }
//            if (PaintLegend) {
//                LegendWidth = paintLegend(g);
//            }
//            left = yaxis.getAxisWidth(g);
//            bottom = xaxis.getAxisWidth(g);
//
//            xaxis.positionAxis(left, Width() - right - LegendWidth, Height() - bottom, Height() - bottom);
//            yaxis.positionAxis(left, left, top + TitleHeight, Height() - bottom);
//
//            xaxis.data_window.setSize(10, Height() - bottom - top - TitleHeight);
//            yaxis.data_window.setSize(Width() - left - right - LegendWidth, 10);
//
//            valueArea = new Rectangle(left, top + TitleHeight, Width() - right - left, Height() - bottom - top - TitleHeight);
//
//            //if(xaxis!=null) xaxis.drawAxis(g);
//            //if(yaxis!=null) yaxis.drawAxis(g);
//
//            int []Nx = getXValues();
//            int[] Ny = getYValues();
//
//            Rectangle ori = new Rectangle(0, 0, valueArea.width, valueArea.height);
//
//            //bgf.setDensColors(sp.col1SV.getColor(),sp.col2SV.getColor(),sp.col3SV.getColor(),sp.col4SV.getColor(),sp.col5SV.getColor());
//            //bgf.setDensColors((Color)Layout.get("col1SV"),(Color)Layout.get("col2SV"),(Color)Layout.get("col3SV"),(Color)Layout.get("col4SV"),(Color)Layout.get("col5SV"));
//            bgf.setDensCords(Nx, Ny);
//
//            bgf.setColorFactory(factory);
//            bgf.externalImage = "";//sp.pathSV.getText();
//            bgf.tileImages = false;//sp.tileSV.isSelected();
//
//            bgf.paintBackground(g, valueArea, 0); //sp.SbgSV.getSelectedIndex());
//
//
////            repaintScale();
//
//
//
//
////            if (((Integer) Layout.get("SbgSV")).intValue() != 0 && scaleLabel != null) {
////                scaleLabel.setIcon(null);
////            }
//
//
//
//
//
//            //g.setColor(Color.black);
//            xaxis.axiscolor = getForeground();
//            yaxis.axiscolor = getForeground();
//
//            Graphics2D g2d = (Graphics2D) g;
//
//            //      if(Nx!=null && Ny!=null){
//
//            if (!FormUpdated) {
//                updateForm();
//                FormUpdated = true;
//                repaint();
//            }
//
//            if (!LockFullRepaint) {
//                g.setClip(valueArea);
//            }
//
//            boolean[] tolerated = bgf.getTolerated();
//
//            int halfSize = (dotsize / 2);
//
//
//            if (dotsize < 0) {
//                return;
//            }
//            int pointSize = dotsize;
//            boolean circ =false;//sp.circSV.isSelected();
//            boolean frame = this.frameSV;//sp.frameSV.isSelected();
//
//            //if(visible!=null && Nx!=null && visible.length!=Nx.length){
//            //            System.out.print("-");
//            //        }
//            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//            if (data != null) {
//
//                //boolean[] painted = new boolean[Nx.length];
//
//                List<Group> groups = data.getRowGroups();
//                Group group = null;
//                Color gcol = null;
//                //OLD
////                boolean[] usedInfos = data.getusedInfos();
//                //NEW
////                boolean[] usedInfos =data.getusedInfos();
//
//                for (int grp = groups.size() - 1; grp > -1; grp--) {
//
//                    group = (Group) groups.get(grp);
//                    if (!group.isActive()) {
//                        continue;
//                    }
//                    gcol = group.getColor();
//                    g2d.setColor(gcol);
//                    //System.out.print("\n"+group.getName());
//
//                    //NOTE: uses groups.hasMember() instead of creating an array of boolean
//                    //OLD
////                    boolean mem = group.getMembers();
//
//                    Color dot = Color.BLACK;
//
//                    for (int i = 0; i < Nx.length; i++) {
//                        if (!group.hasMember(i)) {
//                            continue;
//                        }
//                        if (valueArea != null && !valueArea.contains(Nx[i], Ny[i])) {
//                            continue;
//                        }
//                        if (visible != null && !visible[i]) {
//                            continue;
//                        }
//                        if (tolerated != null && !tolerated[i]) {
//                            continue;
//                        }
//                        //if(painted[i]) continue;
//
//                        // painted[i] = true;
//                        if (((notShaded != null && !notShaded[i]) || allTransparent)) {
//                            dot = new Color(gcol.getRed(), gcol.getGreen(), gcol.getBlue(), unselectedTransparency);
//                            if (frame) {
//                                SpotFrameColor = new Color(0, 0, 0, unselectedTransparency);
//                            }
//                        } else {
//                            dot = gcol;
//                        }
//
//                        if (SpotNames != null && SpotNames[i] != null && SpotNames[i].length > 0 && SpotNames[i][0] != null) {
//                            StringBuilder sb = new StringBuilder();
//                            if (forcedStringSize == -1) {
//                                g2d.setFont(new Font("Arial", 0, dotsize + 4));
//                            } else {
//                                g2d.setFont(new Font("Arial", 0, forcedStringSize));
//                            }
//
//                            for (int h = 0; h < SpotNames[i].length; h++) {
//                                //   if(!usedInfos[h]) continue;
//                                if (h < SpotNames[i].length - 1) {
//                                    sb.append(SpotNames[i][h]).append(" : ");
//                                } else {
//                                    sb.append(SpotNames[i][h]);
//                                }
//                            }
//                            g.setColor(gcol);
//                            g2d.drawString(sb.toString(), Nx[i] + halfSize + 2, Ny[i] + halfSize + 2);
//                        }
//
//                        if (collapsedSquares != null) {
//                            if (collapsedSquares[i]) {
//                                pointSize = 1;
//                            } else {
//                                pointSize = dotsize;
//                            }
//                        }
//                        if ((!paintOnlyIdentifiers) || (SpotNames == null || SpotNames[i] == null || SpotNames[i][0] == null)) {
//                            if (circ) {
//                                //bjarte
//                                //g.setColor(new Color(gcol.getRed(),gcol.getGreen(),gcol.getBlue(),unselectedTransparency));
//                                g.setColor(dot);
//                                g.fillOval(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
//                                g.setColor(SpotFrameColor);
//                                if (frame) {
//                                    g2d.setStroke(new BasicStroke(0.5f));
//                                    g.drawOval(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
//                                }
//                            } else {
//                                Stroke old = g2d.getStroke();
//                                g.setColor(dot);
//                                g.fillRect(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
//                                g.setColor(SpotFrameColor);
//                                if (frame) {
//                                    g2d.setStroke(new BasicStroke(0.5f));
//                                    g.drawRect(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
//                                    g2d.setStroke(old);
//                                }
//                            }
//                        }
//
//
//
//
//
//
//                    }
//
//
//
//                }
//
//
//
//            } else {
//                for (int i = 0; i < Nx.length; i++) {
//                    if (dotColors == null) {
//                        g2d.setColor(new Color(150, 170, 170));
//                    }
//                    if (valueArea.contains(Nx[i], Ny[i])) {
//                        if ((visible == null || (visible.length == Nx.length && visible[i])) && (invalidValues == null || !invalidValues[i])) {
//                            if (((tolerated == null) || (tolerated[i])) && ((inActiveGroup == null) || (inActiveGroup != null && inActiveGroup[i]))) {
//                                if (frame) {
//                                    SpotFrameColor = Color.black;
//                                }
//                                if (((notShaded != null && !notShaded[i]) || allTransparent) && dotColors != null && dotColors[i] != null) {
//                                    g.setColor(new Color(dotColors[i].getRed(), dotColors[i].getGreen(), dotColors[i].getBlue(), unselectedTransparency));
//                                    if (frame) {
//                                        SpotFrameColor = new Color(0, 0, 0, unselectedTransparency);
//                                    }
//                                } else if (dotColors != null && dotColors.length == Nx.length) {
//                                    g.setColor(dotColors[i]);
//                                }
//                                if (SpotNames != null && SpotNames[i] != null && SpotNames[i][0] != null) {
//                                    StringBuilder sb = new StringBuilder();
//                                    if (forcedStringSize == -1) {
//                                        g2d.setFont(new Font("Arial", 0, dotsize + 4));
//                                    } else {
//                                        g2d.setFont(new Font("Arial", 0, forcedStringSize));
//                                    }
//
//                                    for (int h = 0; h < SpotNames[i].length; h++) {
//                                        if (h < SpotNames[i].length - 1) {
//                                            sb.append(SpotNames[i][h]).append(" : ");
//                                        } else {
//                                            sb.append(SpotNames[i][h]);
//                                        }
//                                    }
//                                    g2d.drawString(sb.toString(), Nx[i] + halfSize + 2, Ny[i] + halfSize + 2);
//                                }
//
//                                if (collapsedSquares != null) {
//                                    if (collapsedSquares[i]) {
//                                        pointSize = 1;
//                                    } else {
//                                        pointSize = dotsize;
//                                    }
//                                }
//                                if ((!paintOnlyIdentifiers) || (SpotNames == null || SpotNames[i] == null || SpotNames[i][0] == null)) {
//                                    if (circ) {
//                                        g.fillOval(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
//                                        g.setColor(SpotFrameColor);
//                                        if (frame) {
//                                            g2d.setStroke(new BasicStroke(0.5f));
//                                            g.drawOval(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
//                                        }
//                                    } else {
//                                        Stroke old = g2d.getStroke();
//                                        g.fillRect(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
//                                        g.setColor(SpotFrameColor);
//                                        if (frame) {
//                                            g2d.setStroke(new BasicStroke(0.5f));
//                                            g.drawRect(Nx[i] - halfSize, Ny[i] - halfSize, pointSize, pointSize);
//                                            g2d.setStroke(old);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                }
//
//            }
//
//
//            Stroke tmps = g2d.getStroke();
//
//            if (CAGroupMedians != null && drawCAMedians) {
//                Vector grm = CAGroupMedians;
//
//                for (int k = 0; k < grm.size(); k++) {
//                    Object[] ob = (Object[]) grm.elementAt(k);
//                    Point2D p2d = (Point2D) ob[0];
//                    Color c = (Color) ob[1];
//                    Color ca = c;//new Color(c.getRed(),c.getGreen(),c.getBlue(),CAColor);
//
//                    g2d.setColor(ca);
//
//                    int xXx = xaxis.getInteger(p2d.getX());
//                    int yYy = yaxis.getInteger(p2d.getY());
//
//                    g2d.fillOval(xXx - 3, yYy - 3, 6, 6);
//
//                    g2d.setColor(Color.black);
//                    g2d.drawOval(xXx - 5, yYy - 5, 9, 9);
//
//                    int x0 = xaxis.getInteger(0.0);
//                    int y0 = yaxis.getInteger(0.0);
//
//                    Rectangle r = g.getClipBounds();
//                    Rectangle vla = valueArea;
//                    Rectangle r2 = new Rectangle(vla.x + 5, vla.y + 5, vla.width - 10, vla.height - 10);
//                    g.setClip(r2);
//
//                    //int xxo = (int)((xXx-x0)*1000.0);
//                    //int yyo = (int)((yYy-y0)*1000.0);
//
//                    int xxo = (int) ((xXx - x0) * 10000.0);
//                    int yyo = (int) ((yYy - y0) * 10000.0);
//
//                    g2d.setColor(ca);
//
//
//                    g2d.setStroke(new BasicStroke(CAVectorThickness));
//
//                    g2d.drawLine(x0, y0, xxo, yyo);
//
//                    g2d.setStroke(tmps);
//
//                    g.setClip(r);
//                }
//
//            }
//
//            g2d.setStroke(tmps);
//
//
//            int invalidSpots = getInvalidValues();
//
////            if(invalidSpots>0){
////                String st = "Warning,"+invalidSpots+" "+"invalid values not plotted";
////                g.setFont(new Font("ARIAL",0,10));
////                g.setColor(new Color(250,60,60,120));
////                int X = (valueArea.x+valueArea.width) - (g.getFontMetrics().stringWidth(st)+5);
////                int Y = (valueArea.y+valueArea.height) - (g.getFontMetrics().getHeight());
////                
////                g.drawString(st,X,Y);
////                
////            }
//
//
//            if (trendline == 1) {
//                paintTrend2(valueArea, g, Nx, Ny);
//            } else if (trendline == 2) {
//                paintTrend(valueArea, g, Nx, Ny);
//            }
//
//
//
//
//
//
//
//            if (lines != null) {
//
//
//                double[][] templine = null;
//
//                for (int m = 0; m < lines.size(); m++) {
//
//                    if (visibleLines == null || (visibleLines.length == lines.size() && visibleLines[m])) {
//                        if (!(lines.elementAt(m) instanceof double[][])) {
//                            break;
//                        }
//
//                        templine = (double[][]) lines.elementAt(m);
//
//                        int[][] Iline = new int[2][templine[0].length];
//
//                        for (int i = 0; i < templine[0].length; i++) {
//                            Iline[0][i] = xaxis.getInteger(templine[0][i]);
//                            Iline[1][i] = yaxis.getInteger(templine[1][i]);
//
//
//                        }
//
//
//                        //  double scale  = (double)(yaxis.amax.y - yaxis.amin.y)/(yaxis.maximum - yaxis.minimum);
//                        //   double res = yaxis.amin.y + (int)( (yaxis.maximum- 4538.229169045257 )*scale );
//                        g2d.setStroke(new BasicStroke(1f));
//
//                        //if( visibleLines==null || (visibleLines.length==lines.size() && visibleLines[m]) ){
//                        if (lineColors != null) {
//                            g.setColor((Color) lineColors.elementAt(m));
//                        } else {
//                            g.setColor(Color.cyan);
//                        }
//                        g.drawPolyline(Iline[0], Iline[1], Iline[0].length);
//
//                    }
//                }
//            }
//
//
//
//            //g2d.setStroke(new BasicStroke(1f));
//
//
//
//            g.setClip(null);
//
//            g.setFont(font);
//
//            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
//            if (xaxis != null) {
//                xaxis.drawAxis(g);
//            }
//            if (yaxis != null) {
//                yaxis.drawAxis(g);
//            }
//
//
//           ymin = yaxis.minimum;
//           ymax = yaxis.maximum;
//           xmin = xaxis.minimum;
//           xmax = xaxis.maximum;
////            Layout.put("ymin", new Double());
////            Layout.put("ymax", new Double());
////            Layout.put("xmin", new Double());
////            Layout.put("xmax", new Double());
//            //sp.ymin.setValue(yaxis.minimum);
//            //sp.ymax.setValue(yaxis.maximum);
//            //sp.xmin.setValue(xaxis.minimum);
//            //sp.xmax.setValue(xaxis.maximum);
//
//            g.setColor(getForeground());
//            g.drawLine(left + 1, top + TitleHeight, Width() - right - LegendWidth, top + TitleHeight);
//            g.drawLine(Width() - right - LegendWidth, top + TitleHeight, Width() - right - LegendWidth, Height() - bottom);
//
//
//
//            //if(medianLine){
//
//            g.setColor(new Color(100, 100, 250));
//
//            //int xend =   xaxis.getInteger( Math.min(xaxis.maximum,yaxis.maximum));
//            //int yend =   yaxis.getInteger( Math.min(xaxis.maximum,yaxis.maximum));
//
//
//
//            /*
//             * if(getDeltaLines()>0){
//             *
//             * double xmin = xaxis.minimum; double xmax = xaxis.maximum;
//             *
//             * double ymin = yaxis.minimum; double ymax = yaxis.maximum;
//             *
//             * int lx1 = xaxis.getInteger(xmin); int ly1 = Math.max(0,
//             * yaxis.getInteger(ymin+deltaLines));
//             *
//             * System.out.print("\nmin: "+ymin); System.out.print("\nlyl:
//             * "+(ymin+deltaLines));
//             *
//             *
//             *
//             * int rx1 = xaxis.getInteger(xmax-deltaLines); int ry1 =
//             * yaxis.getInteger(ymax);
//             *
//             *
//             * int lx2 = xaxis.getInteger(xmin+deltaLines); int ly2 =
//             * yaxis.getInteger(ymin);
//             *
//             * int rx2 = xaxis.getInteger(xmax); int ry2 =
//             * yaxis.getInteger(ymax-deltaLines);
//             *
//             *
//             *
//             * g.setColor(Color.LIGHT_GRAY); g.drawLine(lx1,ly1,rx1,ry1);
//             * g.drawLine(lx2,ly2,rx2,ry2);
//             *
//             * }
//             */
//
//            if (medianLine) {
//                g.drawLine(left + 1, Height() - bottom, Width() - right - LegendWidth, top + TitleHeight);
//            }
//            //g.drawLine(xaxis.getInteger(0.0),yaxis.getInteger(0.0),xend,yend);
//
//            if (medianSkew != Double.NaN && medianSkew > 0) {
//
//                if (!LockFullRepaint) {
//                    g.setClip(valueArea);
//                }
//                g.setColor(Color.cyan);
//
//                double tp = yaxis.getDouble(top + TitleHeight);
//                double tp2 = yaxis.getDouble(Height() - bottom);
//
//                if (yaxis.transform == 1) {
//
//                    double val = Math.pow(10, tp);
//                    double val2 = Math.pow(10, tp2);
//                    if (plottype != MA) {
//
//                        val = val * (1.0 / medianSkew);
//                        val2 = val2 * (1.0 / medianSkew);
//                        if (medianLine) {
//                            g.drawLine(left + 1, yaxis.getInteger(val2), Width() - right - LegendWidth, yaxis.getInteger(val));
//                        }
//                    } else {
//
//                        val = 1.0;
//                        val2 = 1.0;
//                        val2 = val2 * medianSkew;
//                        //g.drawLine(left+1, yaxis.getInteger(val2) , Width()-right-LegendWidth , yaxis.getInteger(val));
//                        g.drawLine(left + 1, yaxis.getInteger(val2), Width() - right - LegendWidth, yaxis.getInteger(val2));
//                    }
//                } else {
//                    double val = tp;
//                    double val2 = tp2;
//                    val = val * (1.0 / medianSkew);
//                    val2 = val2 * (1.0 / medianSkew);
//                    //if(medianLine) g.drawLine(xaxis.nullI, yaxis.nullI , Width()-right-LegendWidth , yaxis.getInteger(tp));
//                    if (medianLine) {
//                        g.drawLine(left + 1, yaxis.getInteger(val2), Width() - right - LegendWidth, yaxis.getInteger(val));
//                    }
//                }
//            }
//
//
//            //If several different (factor)normalization routines are used, the different values are
//            //put in the medianSkews vector. If that is not empty, draw all the lines that are selected.
//
//            if (medianSkews != null) {
//                if (!LockFullRepaint) {
//                    g.setClip(valueArea);
//                }
//                for (int i = 0; i < medianSkews.size(); i++) {
//
//                    if (visibleLines == null || (visibleLines.length == medianSkews.size() && visibleLines[i])) {
//                        double localMedianSkew = ((Double) medianSkews.elementAt(i));
//                        if (localMedianSkew != Double.NaN && localMedianSkew > 0) {
//
//                            if (lineColors != null) {
//                                g.setColor((Color) lineColors.elementAt(i));
//                            } else {
//                                g.setColor(Color.cyan);
//                            }
//
//                            double tp = yaxis.getDouble(top + TitleHeight);
//                            double tp2 = yaxis.getDouble(Height() - bottom);
//
//                            if (yaxis.transform == 1) {
//
//                                double val = Math.pow(10, tp);
//                                double val2 = Math.pow(10, tp2);
//                                if (plottype != MA) {
//
//                                    val = val * (1.0 / localMedianSkew);
//                                    val2 = val2 * (1.0 / localMedianSkew);
//                                    if (medianLine && val != 0 && val2 != 0) {
//                                        g.drawLine(left + 1, yaxis.getInteger(val2), Width() - right - LegendWidth, yaxis.getInteger(val));
//                                    }
//                                } else {
//
//                                    val = 1.0;
//                                    val2 = 1.0;
//                                    val2 = val2 * localMedianSkew;
//                                    //g.drawLine(left+1, yaxis.getInteger(val2) , Width()-right-LegendWidth , yaxis.getInteger(val));
//                                    if (val != 0 && val2 != 0) {
//                                        g.drawLine(left + 1, yaxis.getInteger(val2), Width() - right - LegendWidth, yaxis.getInteger(val2));
//                                    }
//                                }
//                            } else {
//                                double val = tp;
//                                double val2 = tp2;
//                                val = val * (1.0 / localMedianSkew);
//                                val2 = val2 * (1.0 / localMedianSkew);
//                                //if(medianLine) g.drawLine(xaxis.nullI, yaxis.nullI , Width()-right-LegendWidth , yaxis.getInteger(tp));
//                                if (medianLine) {
//                                    g.drawLine(left + 1, yaxis.getInteger(val2), Width() - right - LegendWidth, yaxis.getInteger(val));
//                                }
//                            }
//
//                        }
//                    }
//                }
//            }
//
//
//            g.setClip(valueArea);
//
//
////            double xmin = xaxis.minimum;
////            double xmax = xaxis.maximum;
////
////            double ymin = yaxis.minimum;
////            double ymax = yaxis.maximum;
//
//            if (deltalines != null) {
//
//                for (int i = 0; i < deltalines.length / 2; i++) {
//
//                    /*
//                     * double x1 = Math.max(deltalines[i*2].getX(),xmin); double
//                     * y1 = Math.max(deltalines[i*2].getY(),ymin);
//                     *
//                     * double x2 = Math.min(deltalines[(i*2)+1].getX(),xmax);
//                     * double y2 = Math.min(deltalines[(i*2)+1].getY(),ymax);
//                     */
//
//
//                    double x1 = deltalines[i * 2].getX();
//                    double y1 = deltalines[i * 2].getY();
//
//                    double x2 = deltalines[(i * 2) + 1].getX();
//                    double y2 = deltalines[(i * 2) + 1].getY();
//
//
//                    int lx1 = xaxis.getInteger(x1);
//                    int ly1 = yaxis.getInteger(y1);
//                    int lx2 = xaxis.getInteger(x2);
//                    int ly2 = yaxis.getInteger(y2);
//
//
//                    //System.out.print("\n"+x1+","+y1+":"+x2+","+y2);
//
//                    g.drawLine(lx1, ly1, lx2, ly2);
//
//                }
//
//
//            }
//
//
//            Object ali = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
//            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            if (line != null && false) {
//                int[][] Iline = new int[2][line[0].length];
//
//                for (int i = 0; i < line[0].length; i++) {
//                    Iline[0][i] = xaxis.getInteger(line[0][i]);
//                    Iline[1][i] = yaxis.getInteger(line[1][i]);
//                }
//                g2d.setStroke(new BasicStroke(1f));
//                g.setColor(Color.cyan);
//
//
//                for (int i = 1; i < line[0].length; i++) {
//
//                    //  if(Double.isNaN(Iline[0][i-1]) || Double.isNaN(Iline[1][i-1]) || Double.isNaN(Iline[0][i]) || Double.isNaN(Iline[1][i])) g.setColor(new Color(250,210,220,110));
//                    //  else if(Iline[0][i-1]==Iline[1][i-1] || Iline[0][i]==Iline[1][i]) g.setColor(new Color(100,220,220,150));
//                    //else
//                    g.setColor(new Color(100, 220, 220));
//
//                    g.drawLine(Iline[0][i - 1], Iline[1][i - 1], Iline[0][i], Iline[1][i]);
//                    //Iline[0][i]
//                    //Iline[1][i]
//
//                }
//
//                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, ali);
//                //g.drawPolyline(Iline[0],Iline[1],Iline[0].length);
//            }
//            g.setClip(null);
//            //}
//
//            //  }
//
//
//
//
//
//
//            //Draw the correspondence text..
//
//            if (!LockFullRepaint) {
//                g.setClip(valueArea);
//            }
//
//            int CX = 0;
//            int CY = 0;
//
//            StringBuilder sb = new StringBuilder();
//
//            boolean[] us = null;
//            if (data != null) {
//                us = data.getusedColInfos();          
//            
//            }
//            Font before = g.getFont();
//            g.setFont(CF);
//
//            if (TEXT != null) {
//
//                if (props.containsKey("CAFONT")) {
//
//                    CAColor = ((Integer) props.get("CAColor"));
//                }
//
//                if (props.containsKey("CAPSize")) {
//                    try {
//                        CAPsize = Integer.parseInt((String) props.get("CAPSize"));
//                    } catch (Exception ex) {
//                        CAPsize = 5;
//                    }
//                }
//
//
//                for (int i = 0; i < TEXT.length; i++) {
//
//                    CX = xaxis.getInteger(TEXTX[i]);
//                    CY = yaxis.getInteger(TEXTY[i]);
//
//                    //g.setColor(new Color(100,255,100,170));
//
//
//
//
//                    if (correspondenceColors != null && correspondenceColors[i] != null) {
//                        Color ca = new Color(correspondenceColors[i].getRed(), correspondenceColors[i].getGreen(), correspondenceColors[i].getBlue(), CAColor);
//                        g.setColor(ca);
//
//                        //System.out.print("\n"+CAColor);
//
//                        //else g.setColor(CAColor);
//
//                        if (CALineMarks != null && CALineMarks[i] != null) {
//                            CALineMarks[i].setColor(ca);
//                            CALineMarks[i].paintAt(CX, CY, g);
//
//                        } else {
//
//                            //g.fillRect(CX-(CAPsize/2),CY-(CAPsize/2),CAPsize,CAPsize);
//
//                            // if(frame){
//                            //g.setColor(new Color(50,50,50,150));
//                            g.drawRect(CX - 2 - (CAPsize / 2), CY - 2 - (CAPsize / 2), CAPsize + 4 - (CAPsize / 2), CAPsize + 4 - (CAPsize / 2));
//                            // }
//
//                        }
//                        //g.setColor(new Color(50,50,50,150));
//                        //g.drawOval(CX,CY,dotsize+1-(dotsize/2),dotsize+1-(dotsize/2));
//
//                        //g.setColor(new Color(100,255,100,170));
//                        CX += (dotsize + 4);
//                        CY += (g.getFontMetrics().getHeight() / 2) - 4;//((dotsize/2)+4);
//
//
//
//
//                        if (drawCAText) {
//
//                            int plus = 40;
//
//
//                            int centerx = xaxis.getInteger(0.0);
//                            int centery = yaxis.getInteger(0.0);
//
//
//                            double vf1 = (((double) CX - (double) centerx) * 1.5);
//                            double vf2 = (((double) CY - (double) centery) * 1.5);
//
//                            //int nX = (int)vf1;
//                            //int nY = (int)vf2;
//
//                            //int nX =(int) ((double)centerx+ vf1);
//                            //int nY = (int) ((double)centery+ vf2);
//
//                            int nX = (int) ((double) centerx + vf1);
//                            int nY = (int) ((double) centery + vf2);
//
//                            sb.setLength(0);
//                            if (us == null) {
//                                sb.append(TEXT[i][0]);
//                            } else {
//                                for (int k = 0; k < TEXT[i].length; k++) {
//                                    if (us[k]) {
//                                        sb.append(TEXT[i][k]).append("  ");
//                                    }
//                                }
//                            }
//                            sb.setLength(sb.length() - 2); //remove the last spaces
//                            //g.drawString(sb.toString(),nX,nY);
//                            g.drawString(sb.toString(), CX, CY);
//
//                        }
//                    }
//                }
//            }
//            g.setFont(before);
//
////            repaintScale();
//        }//fullrepaint...
//        FullRepaint = false;
//
//
//
//        Graphics tg = null;
//        BufferedImage plot2 = null;
//
//        if (!LockFullRepaint) {
//
//            plot2 = new BufferedImage(Width(), Height(), BufferedImage.TYPE_INT_ARGB);
//            tg = plot2.getGraphics();
//            //tg.drawImage(plot,0,0,null);
//            plot2.setData(plot.getRaster());
//        } else {
//            tg = gr;
//        }
//
//        if (repaintNeurons) {
//            if (!LockFullRepaint) {
//                tg.setClip(valueArea);
//            }
//            drawNeurons(tg);
//        }
//
//        Graphics2D g2d = (Graphics2D) tg;
//
//        g2d.setColor(frameBG);
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
//
////        if (mouseDrag) {
////            if (frameBG != null && frameBG.getRGB() != Color.white.getRGB()) {
////                for (int i = 0; i < paths.size(); i++) {
////                    g2d.fill((Shape) paths.elementAt(i));
////                }
////            }
////        }
//
//
//
//        Stroke str = g2d.getStroke();
//        float[] dashPattern = new float[]{3, 3, 3, 3};
//        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 10, dashPattern, 0));
//        g2d.setColor(new Color(169, 160, 160));
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
////        for (int i = 0; i < paths.size(); i++) {
////            g2d.draw((Shape) paths.elementAt(i));
////        }
//
//        g2d.setStroke(str);
//
//        if (!LockFullRepaint) {
//            gr.drawImage(plot2, 0, 0, null);
//        }
//        if (sizeMonitor != null) {
//            sizeMonitor.setText("Plot Size : (" + getPlotSize().width + "," + getPlotSize().height + ")");
//        }
//
//    }
//    private double xmin = -3.86;//sp.xmin.getValue();
//    private double xmax = 1.661;//sp.xmax.getValue();
//    private double ymin = -2.378;//sp.ymin.getValue();
//    private double ymax = 1.922;
//    @Override
//     public void readForm() {
//        topText.setText("");
//
//
//        yaxis.correctForCloseValues = true;
//        xaxis.correctForCloseValues = true;
//
//        xaxis.minor_tic_count = mixticsSV;
//        yaxis.minor_tic_count = miyticsSV;
//
//        xaxis.paintGrid = paintGridSV;//yaxis.paintGrid=sp.paintGridSV.isSelected();
//        yaxis.paintGrid = paintGridSV;//yaxis.paintGrid=sp.paintGridSV.isSelected();
//        xaxis.gridcolor = gridColorSV;//sp.gridColor.getColor();
//        yaxis.gridcolor = gridColorSV;//sp.gridColor.getColor();
//
//        xaxis.zerocolor = new Color(102, 102, 255);//sp.nullSV.getColor();
//        yaxis.zerocolor = new Color(102, 102, 255);//sp.nullSV.getColor();
//
//        //if(sp.Xaxis.getText().length()>0)
//        xaxis.setTitleText("Principal Component1");//sp.Xaxis.getText());
//        //if(sp.Yaxis.getText().length()>0)
//        yaxis.setTitleText("Principal Component2");//sp.Yaxis.getText());
//
//        xaxis.TICS_IN_BOTH_ENDS = true;//sp.xticsbothsidesSV.isSelected();
//        yaxis.TICS_IN_BOTH_ENDS = true;//sp.yticsbothsidesSV.isSelected();
//
//        xaxis.transparency = 66;
//        yaxis.transparency = 66;
//
//        bgf.setDensColors(new Color(255, 254, 254), new Color(204, 204, 255), new Color(102, 255, 102), new Color(255, 204, 51), new Color(255, 0, 0));
//
//        bgf.setDensArea(100);//sp.densareaSV.getValue());
//        bgf.setDensTolerance(100);//sp.tresholdSV.getValue());
//        bgf.setNumColors(50);//sp.colorsSV.getValue());
//
//        bgf.GradientType = 0;//sp.gradtypeSV.getSelectedIndex();
//        bgf.Single =  new Color(255, 254, 254);// sp.singlebgSV.getColor();
//        bgf.gradient1 = new Color(51, 153, 255);//sp.grad1SV.getColor();
//        bgf.gradient2 = new Color(204, 204, 255);//sp.grad2SV.getColor();
//
//        dotsize = this.dotsizeSV;
//        setBackground(Color.WHITE);//sp.chartbgSV.getColor());
//
//
//        boolean resetxAxis = false;
//        boolean resetyAxis = false;
//
//    
//        boolean showtrend = false;
//        boolean nump = false;
//
//            showtrend = false;
//            trendpoints =  0.05;
////        }
//
////        if (Layout.containsKey("weightSV")) {
//            trendweight = 0.3;
////        }
//
//
////        if (Layout.containsKey("trendtransSV")) {
//            trendtrans = 50;
////        }
//
////        if (Layout.containsKey("widthSV")) {
//            trendwidht =  6.0f;
////        }
//
////        if (Layout.containsKey("trendcSV")) {
//            trendcolor = new Color(0, 0, 0);
////        }
//
////        if (Layout.containsKey("minpSV")) {
//            trendminp = 30;
////        }
//
//
//
//
//    }
//
//    @Override
//    public void setPropsAndData(double[] Rxn, double[] Ryn) {
//
//        double minX = 0, maxX = 0, minY = 0, maxY = 0.0;
//        for (double d1 : Rxn) {
//            if (d1 > maxX) {
//                maxX = d1;
//            }
//            if (d1 < minX) {
//                minX = d1;
//            }
//
//        }
//
//        for (double d2 : Ryn) {
//            if (d2 > maxY) {
//                maxY = d2;
//            }
//            if (d2 < minY) {
//                minY = d2;
//            }
//        }
//        invalidValues = null;
//        boolean init = false;
//
//        if (this.props == null) {
//            init = true;
//        }
//        setupListeners();
//        FullRepaint = true;
//        if (init) {
//            readValues();
//        }
//        setData(Rxn, Ryn);
//        readForm();
//        repaint();
//    }
//    private double[] zoomedArea;
//
//    @Override
//    public void zoom(double[] frame) {
//        zoomedArea = frame;
//
//        xmin = frame[0];
//        xmax = frame[1];
//        ymin = frame[2];
//        ymax = frame[3];
//        yaxis.force_end_labels = false;
//        xaxis.force_end_labels = false;
//
//        FullRepaint = true;
//        repaint();
//    }
//
//    @Override
//        public void createDefaultDensityColorFactory() {
//        ColorFactory cf = new ColorFactory();
//        cf.setMirror(false);
//        cf.initControlPoints(false);
//        List<ControlPoint> cp = cf.getControlPoints();
//        ControlPoint cpo = new ControlPoint();
//        cp.get(0).setColor(Color.white);
//        cp.get(1).setColor(Color.YELLOW);
//
//        cpo.setLocation(0.3);
//        cpo.setColor(new Color(170, 245, 170));
//        cp.add(cpo);
//
//        cpo = new ControlPoint();
//        cpo.setLocation(0.6);
//        cpo.setColor(Color.red);
//        cp.add(cpo);
//        cf.setMissing(Color.blue);
//        setColorFactory(cf);
//    }
//
//    @Override
//    public BackgroundFactory getBackgroundFactory() {
//        return bgf;
//    }
//
//    @Override
//    public void setVisible(boolean[] visible) {
//        this.visible = visible;
//    }
//
//    @Override
//    public void setNotShaded(boolean[] notShaded) {
//        this.notShaded = notShaded;
//    }
//
//    @Override
//    public void setVisibleLines(boolean[] visibleLines) {
//        this.visibleLines = visibleLines;
//    }
//
//    @Override
//    public void setCorrespondeceValues(String[][] TEXT, double[] TEXTX, double[] TEXTY, Color[] correspondenceColors) {
//        this.TEXT = TEXT;
//        this.TEXTX = TEXTX;
//        this.TEXTY = TEXTY;
//        this.correspondenceColors = correspondenceColors;
//    }
//
//    @Override
//    public void setHighLightedNeuron(Point highLightNeuron) {
//        this.highLightNeuron = highLightNeuron;
//    }
//
//    @Override
//    public Dimension getPlotSize() {
//        if (valueArea != null) {
//            return valueArea.getSize();
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public void setFrameBG(Color frameBG) {
//        this.frameBG = frameBG;
//        this.forceFullRepaint();
//        //repaint();
//    }
//
//    @Override
//    public void setframeType(int frameType) {
//        this.frameType = frameType;
//    }
//
//    @Override
//    public int getframeType() {
//        return frameType;
//    }
//
//    @Override
//    public final void resetAxis() {
//        invalidValues = xaxis.resetAxis(Rx);
//        invalidValues = addInvalidValues(invalidValues, yaxis.resetAxis(Ry));
//        updateAxisValues();
//    }
//
//    @Override
//    public void setProps(Hashtable props) {
//        boolean init = false;
//        if (this.props == null) {
//            init = true;
//        }
//
//        this.props = props;
////        setupListeners();
//        FullRepaint = true;
//        if (init) {
//            readValues(); 
//        }
//    }
//
//    private double[] zoom;
////    private  Hashtable props;
//    @Override
//    public void setPropsAndData(double[] Rxn, double[] Ryn, double[] zoom) {
//        invalidValues = null;
//        boolean init = false;
//        if (this.props == null) {
//            init = true;
//        }
//
//        this.zoom = zoom;
////        setupListeners();
//        FullRepaint = true;
//        if (init) {
//            readValues();   //put the stored values to the properties dialog.
//        }
//        setData(Rxn, Ryn);
//        this.zoom(zoom);
//    }
//
//    private boolean equalScales;
//    @Override
//    public boolean isEqualScales() {
//        return equalScales;
//    }
//    @Override
//    public void setEqualScales(boolean equalScales) {
//        this.equalScales = equalScales;
//
//        if (equalScales) {
//
//            double max = Math.max(xaxis.maximum, yaxis.maximum);
//            double min = Math.min(xaxis.minimum, yaxis.minimum);
//            yaxis.setManualRange(true);
//            xaxis.setManualRange(true);
//            xaxis.minimum = min;
//            xaxis.maximum = max;
//            yaxis.minimum = min;
//            yaxis.maximum = max;
//            updateAxisValues();
//        } else {
//
//            yaxis.setManualRange(false);
//            xaxis.setManualRange(false);
//            invalidValues = xaxis.resetAxis(Rx);
//            invalidValues = addInvalidValues(invalidValues, yaxis.resetAxis(Ry));
//            updateAxisValues();
//        }
//        FullRepaint = true;
//
//    }
//
//    @Override
//    public void setData(double[] Rxn, double[] Ryn) {
//        invalidValues = null;
//        if (Rxn != null) {
//            this.Rx = Rxn;
//        } else {
//            this.Rx = new double[]{};
//        }
//
//        if (Ryn != null) {
//            this.Ry = Ryn;
//        } else {
//            this.Ry = new double[]{};
//        }
//
//        invalidValues = xaxis.resetAxis(Rxn);
//        invalidValues = addInvalidValues(invalidValues, yaxis.resetAxis(Ry));
//        updateAxisValues();
//        repaint();
//    }
//    
//    @Override
//    public void zoomout() {
//
//        yaxis.setManualRange(false);
//        yaxis.force_end_labels = false;
//
//        xaxis.setManualRange(false);
//        xaxis.force_end_labels = false;
//
//        invalidValues = xaxis.resetAxis(Rx);
//        invalidValues = addInvalidValues(invalidValues, yaxis.resetAxis(Ry));
//
//        updateAxisValues();
//
//        this.FullRepaint = true;
//        repaint();
//    }
//
//
//    @Override
//    public void setXaxisTitle(String label) {       
//        xaxis.setTitleText(label);
//    }
//
//    @Override
//    public void setYaxisTitle(String label) {
//        yaxis.setTitleText(label);       
//    }
//
//    @Override
//    public Vector getIndexesAtPoint(Point p, int radius) {
//        Vector ret = new Vector();
//        Vector dst = new Vector();
//        boolean[] tolerated = bgf.getTolerated();
//        double d = 0.0;
//        double d1 = 0.0;
//        double d2 = 0.0;
//        for (int i = 0; i < Rx.length; i++) {
//
//            if ((dotColors == null || dotColors[i] != null) && (tolerated == null || tolerated[i])) {
//                if (visible == null || visible[i]) {
//                    if (xaxis.getInteger(Rx[i]) > p.x - radius
//                            && xaxis.getInteger(Rx[i]) < p.x + radius
//                            && yaxis.getInteger(Ry[i]) > p.y - radius
//                            && yaxis.getInteger(Ry[i]) < p.y + radius) {
//
//
//                        ret.add(i);
//
//                        d1 = xaxis.getInteger(Rx[i]) - p.x;
//                        d1 = d1 * d1;
//
//                        d2 = yaxis.getInteger(Ry[i]) - p.y;
//                        d2 = d2 * d2;
//
//                        d = Math.sqrt(d1 + d2);
//                        dst.add(d);
//                    }
//                }
//            }
//            if (Thread.interrupted()) {
//                return null;
//            }
//        }
//
//        Vector sret = new Vector();
//        double[] dist = new double[dst.size()];
//        for (int i = 0; i < dist.length; i++) {
//            dist[i] = ((Double) dst.elementAt(i));
//        }
//        int[] srt = JDoubleSorter.quickSort(dist);
//
//        for (int i = 0; i < srt.length; i++) {
//            sret.addElement(ret.elementAt(srt[i]));
//        }
//
//        return sret;
//    }
//    
//    @Override
//     public void paintTrend2(Rectangle valueArea, Graphics g, int[] Nx, int[] Ny) {
//        if (Nx == null || Nx.length < 1) {
//            return;
//        }
//
//        Vector polygoon = new Vector();
//        int window = (int) (valueArea.width * trendweight);
//        int steps = (int) (valueArea.width * trendpoints);
//
//        int[] sr = JIntSorter.quickSort(Nx);
//
//        int x = 0;
//        int y = 0;
//
//        double mean = 0.0;
//        int cnt = 0;
//        int n = Nx.length;
//
//        int used = 0;
//
//        int lastx = 0;
//        int lasty = 0;
//
//        double maxw = 0.0;
//        boolean start = true;
//
//        Graphics2D g2d = (Graphics2D) g;
//
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setStroke(new BasicStroke(trendwidht));
//        g2d.setColor(new Color(trendcolor.getRed(), trendcolor.getGreen(), trendcolor.getBlue(), trendtrans));
//
//        int tmpx = 0;
//
//        for (int i = 0; i < Nx.length; i++) {
//            x = Nx[sr[i]];
//            if (Double.isNaN(x) || (trendSource != null && !trendSource[i])) {
//                continue;
//            }
//            if (Math.abs(lastx - x) < steps && !start) {
//                continue;
//            }
//            start = false;
//            used = 0;
//            mean = 0;
//
//            for (int j = 0; j < window; j++) {
//                if (i - j < 0 || i + j >= sr.length) {
//                    break;
//                }
//
//                if (Double.isNaN(Ny[sr[i - j]]) || (trendSource != null && !trendSource[i - j])) {
//                    continue;
//                }
//                if (Double.isNaN(Ny[sr[i + j]]) || (trendSource != null && !trendSource[i + j])) {
//                    continue;
//                }
//                if (Ny[sr[i - j]] < 0 || Ny[sr[i - j]] > valueArea.height) {
//                    continue;
//                }
//                if (Ny[sr[i + j]] < 0 || Ny[sr[i + j]] > valueArea.height) {
//                    continue;
//                }
//
//                mean += Ny[sr[i - j]];
//                mean += Ny[sr[i + j]];
//                used += 2;
//            }
//            y = (int) (mean / used);
//
//            if (x > 0 && y > 0 && used >= trendminp) {
//                polygoon.addElement(new Point(x, y));
//            }
//            lastx = x;
//        }
//
//        int[] xx = new int[polygoon.size()];
//        int[] yy = new int[polygoon.size()];
//
//        for (int i = 0; i < polygoon.size(); i++) {
//            xx[i] = ((Point) polygoon.elementAt(i)).x;
//            yy[i] = ((Point) polygoon.elementAt(i)).y;
//        }
//
//        g2d.drawPolyline(xx, yy, xx.length);
//
//        g2d.setStroke(new BasicStroke(1.0f));
//
//    }
//
//     @Override
//    public void paintTrend(Rectangle valueArea, Graphics g, int[] Nx, int[] Ny) {
//        if (Nx == null || Nx.length < 1) {
//            return;
//        }
//        g.setColor(new Color(40, 40, 240, 100));
//        Vector polygoon = new Vector();
//        int window = (int) (valueArea.width * trendweight);
//        int steps = (int) (valueArea.width * trendpoints);
//        int[] sr = JIntSorter.quickSort(Nx);
//        int x = 0;
//        int y = 0;
//
//        double mean = 0.0;
//        int cnt = 0;
//        int n = Nx.length;
//
//        int used = 0;
//
//        int lastx = 0;
//        int lasty = 0;
//
//        double maxw = 0.0;
//        boolean start = true;
//
//        Graphics2D g2d = (Graphics2D) g;
//
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setStroke(new BasicStroke(trendwidht));
//        g2d.setColor(new Color(trendcolor.getRed(), trendcolor.getGreen(), trendcolor.getBlue(), trendtrans));
//
//        int tmpx1 = 0;
//        int tmpx2 = 0;
//
//        for (int i = 0; i < Nx.length; i++) {
//            x = Nx[sr[i]];
//            if (Double.isNaN(x) || (trendSource != null && !trendSource[sr[i]])) {
//                continue;
//            }
//            if (Math.abs(lastx - x) < steps && !start) {
//                continue;
//            }
//            start = false;
//            used = 0;
//            mean = 0;
//            cnt = 0;
//            while (true) {
//
//                if (i - cnt == 0) {
//                    break;
//                }
//                if (i + cnt == sr.length - 1) {
//                    break;
//                }
//
//                tmpx1 = Nx[sr[i - cnt]];
//                tmpx2 = Nx[sr[i + cnt]];
//
//                if (x - tmpx1 > window) {
//                    break;
//                }
//                if (tmpx2 - x > window) {
//                    break;
//                }
//
//
//                if (Double.isNaN(Ny[sr[i - cnt]]) || (trendSource != null && !trendSource[sr[i - cnt]])) {
//                    cnt++;
//                    continue;
//                }
//                if (Double.isNaN(Ny[sr[i + cnt]]) || (trendSource != null && !trendSource[sr[i + cnt]])) {
//                    cnt++;
//                    continue;
//                }
//                if (Ny[sr[i - cnt]] < 0 || Ny[sr[i - cnt]] > valueArea.height) {
//                    cnt++;
//                    continue;
//                }
//                if (Ny[sr[i + cnt]] < 0 || Ny[sr[i + cnt]] > valueArea.height) {
//                    cnt++;
//                    continue;
//                }
//
//                mean += Ny[sr[i - cnt]];
//                mean += Ny[sr[i + cnt]];
//                used += 2;
//                cnt++;
//                if (cnt == 10000) {
//                    break;  
//                }
//            }
//            y = (int) (mean / used);
//
//            if (x > 0 && y > 0 && used >= trendminp) {
//                polygoon.addElement(new Point(x, y));// g.drawLine(lastx,lasty,x,y);
//            }
//            lastx = x;
//        }
//
//
//        int[] xx = new int[polygoon.size()];
//        int[] yy = new int[polygoon.size()];
//
//        for (int i = 0; i < polygoon.size(); i++) {
//            xx[i] = ((Point) polygoon.elementAt(i)).x;
//            yy[i] = ((Point) polygoon.elementAt(i)).y;
//        }
//
//        g2d.drawPolyline(xx, yy, xx.length);
//
//        g2d.setStroke(new BasicStroke(1.0f));
//
//    }
//
//    @Override
//    public void paintCuveTrough(Graphics g, double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
//
//        int xlen = (int) (x3 - x0);
//        int lastx = -1;
//        int lasty = -1;
//
//        int nowx = -1;
//        int nowy = -1;
//
//        for (int i = 0; i < xlen; i++) {
//            double t = (double) i / (double) xlen;
//            //use Berstein polynomials
//            nowx = (int) ((x0 + t * (-x0 * 3 + t * (3 * x0 - x0 * t))) + t * (3 * x1 + t * (-6 * x1 + x1 * 3 * t)) + t * t * (x2 * 3 - x2 * 3 * t) + x3 * t * t * t);
//            nowy = (int) ((y0 + t * (-y0 * 3 + t * (3 * y0 - y0 * t))) + t * (3 * y1 + t * (-6 * y1 + y1 * 3 * t)) + t * t * (y2 * 3 - y2 * 3 * t) + y3 * t * t * t);
//
//
//            if (lastx > 0 && lasty > 0) {
//                g.drawLine(lastx, lasty, nowx, nowy);
//            }
//
//            lastx = nowx;
//            lasty = nowy;
//
//        }
//
//    }
//@Override
//    public void drawNeurons(Graphics g) {
//        int neuronsize = 5;
//        int xoffset = 0;
//        int yoffset = 0;
//
//
//        int netx = 0, nety = 0, net2x = 0, net2y = 0;
//        int[][] Ineurons = new int[2][neurons[0].length];
//        for (int i = 0; i < neurons[0].length; i++) {
//            Ineurons[0][i] = xaxis.getInteger(neurons[0][i]);
//            Ineurons[1][i] = yaxis.getInteger(neurons[1][i]);
//        }
//
//        int n = this.neuronWidth;
//        int m = this.neuronLength;
//
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        Stroke oldStroke = g2d.getStroke();
//
//        float[] dashPattern = {2, 2};
//        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 10, dashPattern, 0));
//
//        if (!hex) {
//
//            //Vertical lines
//            g.setColor(new Color(20, 50, 20));
//            for (int i = 0; i < n - 1; i++) {
//                for (int j = 0; j < m; j++) {
//                    netx = Ineurons[0][(j) + (i * m)];
//                    nety = Ineurons[1][(j) + (i * m)];
//
//                    net2x = Ineurons[0][(j) + ((i + 1) * m)];
//                    net2y = Ineurons[1][(j) + ((i + 1) * m)];
//
//                    g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
//                }
//            }
//            g.setColor(new Color(10, 130, 10));
//            for (int i = 0; i < n; i++) {
//                for (int j = 0; j < m - 1; j++) {
//                    netx = Ineurons[0][(j) + (i * m)];
//                    nety = Ineurons[1][(j) + (i * m)];
//
//                    net2x = Ineurons[0][(j) + (i * m) + 1];
//                    net2y = Ineurons[1][(j) + (i * m) + 1];
//
//                    g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
//                }
//            }
//
//        } else {
//            //Horizontal lines
//            g.setColor(new Color(10, 130, 10));
//            for (int i = 0; i < n; i++) {
//                for (int j = 0; j < m - 1; j++) {
//                    netx = Ineurons[0][(j) + (i * m)];
//                    nety = Ineurons[1][(j) + (i * m)];
//
//                    net2x = Ineurons[0][(j) + (i * m) + 1];
//                    net2y = Ineurons[1][(j) + (i * m) + 1];
//
//                    g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
//                }
//            }
//
//
//
//            //Vertical lines
//            g.setColor(new Color(20, 50, 20));
//            for (int i = 0; i < n - 1; i++) {
//
//                for (int j = 0; j < m; j++) {
//
//                    netx = Ineurons[0][(j) + (i * m)];
//                    nety = Ineurons[1][(j) + (i * m)];
//
//                    //backward down
//                    if (i % 2 == 0) {
//                        if (j > 0) {
//                            net2x = Ineurons[0][(j) + (i * m) + m - 1];
//                            net2y = Ineurons[1][(j) + (i * m) + m - 1];
//                            g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
//                        }
//                    } else {
//                        net2x = Ineurons[0][(j) + (i * m) + m];
//                        net2y = Ineurons[1][(j) + (i * m) + m];
//                        g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
//                    }
//
//                    //Forward down..
//                    if (i % 2 == 0) {
//                        net2x = Ineurons[0][(j) + (i * m) + m];
//                        net2y = Ineurons[1][(j) + (i * m) + m];
//                        g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
//                    } else {
//                        if (j < m - 1) {
//                            net2x = Ineurons[0][(j) + (i * m) + m + 1];
//                            net2y = Ineurons[1][(j) + (i * m) + m + 1];
//                            g.drawLine(netx + xoffset, nety + yoffset, net2x + xoffset, net2y + yoffset);
//                        }
//                    }
//
//                }
//            }
//        }
//
//        g2d.setStroke(oldStroke);
//
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < m; j++) {
//                netx = Ineurons[0][(i * m) + j];
//                nety = Ineurons[1][(i * m) + j];
//
//                if (highLightNeuron != null && i == highLightNeuron.x && j == highLightNeuron.y) {
//                    g.setColor(Color.yellow);
//                    g.fillOval(-1 + netx + xoffset - (neuronsize / 2), -1 + nety + yoffset - (neuronsize / 2), neuronsize, neuronsize);
//                    g.setColor(Color.black);
//                    g.drawOval(-1 + netx + xoffset - (neuronsize / 2), -1 + nety + yoffset - (neuronsize / 2), neuronsize, neuronsize);
//                    //g.drawRect(netx-6+xoffset,nety-6+yoffset,13,13);
//                } else {
//                    g.setColor(Color.red);
//                    g.fillOval(-1 + netx + xoffset - (neuronsize / 2), -1 + nety + yoffset - (neuronsize / 2), neuronsize, neuronsize);
//                    g.setColor(Color.black);
//                    g.drawOval(-1 + netx + xoffset - (neuronsize / 2), -1 + nety + yoffset - (neuronsize / 2), neuronsize, neuronsize);
//                }
//            }
//        }
//
//    }
//
//    @Override
//    public void setColors(Color[] dotColors) {
//        this.dotColors = dotColors;
//    }
//
//    @Override
//    public Image getImage() {
//        if (getWidth() == 0 || getHeight() == 0) {
//            return null;
//        }
//        BufferedImage bim = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
//        Graphics gg = bim.getGraphics();
//        paintComponent(gg);
//        return bim;
//
//    }
//@Override
//    public void setInActiveGroup(boolean[] inActiveGroup) {
//        this.inActiveGroup = inActiveGroup;
//    }
//@Override
//    public boolean[] getTolerated() {
//        return bgf.getTolerated();
//    }
//    
//    private int mixticsSV = 2,miyticsSV=2;
//    private boolean paintGridSV=true;
//    private Color gridColorSV =new Color(102, 102, 102);
//@Override
//    public void updateForm() {
//        mixticsSV= xaxis.minor_tic_count;
//        miyticsSV= yaxis.minor_tic_count;
//        paintGridSV= (xaxis.paintGrid & yaxis.paintGrid);
//        gridColorSV= yaxis.gridcolor;
//
//    }
//
//    public void setTitle(String title) {
//        topText.setText(title);
//    }
//
// @Override
//    public void updateAxisValues() {
//        ymin = yaxis.minimum;
//        ymax = yaxis.maximum;
//        xmin = xaxis.minimum;
//        xmax = xaxis.maximum;
//
//    }
//
//    @Override
//    public void writeValues() {
//        Tools to = new Tools();
//        //System.out.print("\nWriting chart as "+plotName);
//        if (props == null) {
//            System.out.print("\nWARNING, No properties read for plot");
//        }
//        if (plotName == null) {
//            System.out.print("\nWARNING, No plotName for plot");
//        }
//
//    }
//
//    @Override
//    public void readValues() {
//        forceFullRepaint();
//    }
//private int dotsizeSV = 4;
//   @Override
// public void setDotSize(int dotSize) {      
//        this.dotsize = dotSize;
//    }
//
// private boolean frameSV=false;
// @Override
//    public void setFrameDots(boolean frame) {
//        frameSV= frame;
//    }
//
//  @Override
//    public double[] getZoomedArea() {
//        return zoomedArea;
//    }
//
//    
//    
//private int[] Nx;
//    
////@Override
////    public boolean[] getFramedIndexes() {
////        boolean[] ret = new boolean[Nx.length];
////
////        boolean[] tolerated = bgf.getTolerated();
////
////        if (frameType == 0) {
////            int sqxstart = Math.min(startx, endx);
////            int sqystart = Math.min(starty, endy);
////            int sqxend = (Math.max(startx, endx) - Math.min(startx, endx));
////            int sqyend = (Math.max(starty, endy) - Math.min(starty, endy));
////            sweepFrom = new Point(sqxstart, sqystart);
////            sweepTo = new Point(sqxend, sqyend);
////        }
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
////
////        return ret;
////    }
//
//@Override
//    public int[] getInterpolatedColors(int length, Color color1, Color color2, Color color3, Color color4, Color color5) {
//
//        int[] ret = new int[length];
//
//        Dimension d = this.getSize();
//        int frameheight = d.height;
//        int framewidth = d.width;
//
//        Color temp = null;
//        int newred = 0;
//        int newgreen = 0;
//        int newblue = 0;
//
//        double[] red = new double[5];
//        double[] green = new double[5];
//        double[] blue = new double[5];
//
//        red[0] = (double) color1.getRed();
//        green[0] = (double) color1.getGreen();
//        blue[0] = (double) color1.getBlue();
//
//        red[1] = (double) color2.getRed();
//        green[1] = (double) color2.getGreen();
//        blue[1] = (double) color2.getBlue();
//
//        red[2] = (double) color3.getRed();
//        green[2] = (double) color3.getGreen();
//        blue[2] = (double) color3.getBlue();
//
//        red[3] = (double) color4.getRed();
//        green[3] = (double) color4.getGreen();
//        blue[3] = (double) color4.getBlue();
//
//        red[4] = (double) color5.getRed();
//        green[4] = (double) color5.getGreen();
//        blue[4] = (double) color5.getBlue();
//
//        double dqlength = (((double) length + 1.0) / 4.0);
//
//        double tmp = 0, tmp2 = 0;
//        int itmp = 0, itmp2 = 0;
//
//        for (int i = 0; i < ret.length; i++) {
//
//            tmp = ((double) i) % (dqlength);
//            tmp2 = ((double) i) / dqlength;
//
//            itmp = (int) tmp;
//            itmp2 = (int) Math.floor(tmp2);
//
//            newred = (int) ((double) red[itmp2] + (((double) (red[itmp2 + 1] - red[itmp2]) / dqlength) * tmp));
//            newgreen = (int) ((double) green[itmp2] + (((double) (green[itmp2 + 1] - green[itmp2]) / dqlength) * tmp));
//            newblue = (int) ((double) blue[itmp2] + (((double) (blue[itmp2 + 1] - blue[itmp2]) / dqlength) * tmp));
//
//            ret[i] = new Color(newred, newgreen, newblue).getRGB();
//
//        }
//
//        return ret;
//    }
//@Override
//    public boolean[] addInvalidValues(boolean[] x, boolean[] y) {
//        if (x == null && y != null) {
//            return y;
//        } else if (x != null && y == null) {
//            return x;
//        } else if (x == null && y == null) {
//            return null;
//        } else {
//            for (int i = 0; i < x.length; i++) {
//                x[i] = x[i] | y[i];
//            }
//            return x;
//        }
//    }
//@Override
//    public int getInvalidValues() {
//        if (invalidValues == null) {
//            return 0;
//        } else {
//            int ret = 0;
//            for (int i = 0; i < invalidValues.length; i++) {
//                if (invalidValues[i]) {
//                    ret++;
//                }
//            }
//            return ret;
//        }
//    }
//
//    /**
//     * Getter for property plotName.
//     *
//     * @return Value of property plotName.
//     */
//    @Override
//    public String getPlotName() {
//        return plotName;
//    }
//
//    /**
//     * Setter for property plotName.
//     *
//     * @param plotName New value of property plotName.
//     */
//    @Override
//    public void setPlotName(String plotName) {
//        this.plotName = plotName;
//    }
//

    public Dataset getData() {
        return data;
    }

    public void setData(Dataset data) {
        this.data = data;
    }

    public boolean isFullRepaint() {
        return FullRepaint;
    }

    public void setFullRepaint(boolean FullRepaint) {
        this.FullRepaint = FullRepaint;
    }

    public double[] getZoom() {
        return zoom;
    }

    public void setZoom(double[] zoom) {
        this.zoom = zoom;
    }

    
}

