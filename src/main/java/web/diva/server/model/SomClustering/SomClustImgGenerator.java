/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model.SomClustering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ColorFactory;
import no.uib.jexpress_modularized.core.visualization.colors.ui.ScaleAndAxis;
import no.uib.jexpress_modularized.somclust.model.Node;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartUtilities;
import web.diva.server.model.HeatmapColorFactory;
import web.diva.shared.beans.ClientClusterNode;
import web.diva.shared.beans.SomClustTreeSelectionUpdate;

/**
 *
 * @author Yehia Farag
 */
public class SomClustImgGenerator {

    private TreeView upperTree, sideTree,maxmizeUpperTree, maxmizeSideTree;
    private final Node rowNode, colNode;
    private final boolean gengenscale = false;
    private final Color GridCol = Color.DARK_GRAY;

    private final int squareW = 12;
    private final int squareL = 2;
    private final int LeftTreeWidth = 200;
    private final int TopTreeHeight = 70;
    private int LeftTreeHeight,TopTreeWidth;
    
    
    private String rotatedTopTreeImgUrl,rotatedSideTreeImgUrl,rotatedHeatMapImgUrl,rotatedInteractiveRowImgUrl;
    

    public int getLeftTreeHeight() {
        return LeftTreeHeight;
    }

    public int getTopTreeWidth() {
        return TopTreeWidth;
    }

    
    private final boolean ValueDistances = true;
    private HeatmapColorFactory colorFactory;
    private ColorFactory colors;
    private final java.text.NumberFormat numformat;

    public SomClustImgGenerator(Node rowNode, Node colNode) {
        this.rowNode = rowNode;
        this.colNode = colNode;
        numformat = java.text.NumberFormat.getNumberInstance(java.util.Locale.US);
        numformat.setMaximumFractionDigits(3);
        numformat.setMinimumFractionDigits(1);

    }

    public TreeView getUpperTree() {
        return upperTree;
    }

    public TreeView getSideTree() {
        return sideTree;
    }

    public String generateSideTree(Node root) {
        int verticalItems = countgenes(root);
        root.mark = true;
        sideTree = new TreeView(root, verticalItems, Color.WHITE, Color.black);//"#e3e3e3"Color.decode("#e3e3e3")
        sideTree.leafdist = squareL;
        sideTree.actualLength = verticalItems;
        sideTree.leftmargin = (int) Math.round(squareL / 2);
        sideTree.drawframe = false;
        sideTree.valuedistances = true;
        sideTree.rightmargin = 0;
        sideTree.drawrects = false;
        sideTree.bottommargin = 0;
        //if(result!=null)
        sideTree.treewidth = LeftTreeWidth;
        sideTree.generatecoords();
        tooltipsSideNode = initToolTips(root, null);
        LeftTreeHeight = sideTree.getHeight();

        maxmizeSideTree = new TreeView(root, verticalItems, Color.WHITE, Color.black);//"#e3e3e3"Color.decode("#e3e3e3")
        maxmizeSideTree.leafdist = squareL;
        maxmizeSideTree.setHorizontal(false);
        maxmizeSideTree.actualLength = verticalItems;
        maxmizeSideTree.leftmargin = (int) Math.round(squareL / 2);
        maxmizeSideTree.drawframe = false;
        maxmizeSideTree.valuedistances = true;
        maxmizeSideTree.rightmargin = 0;
        maxmizeSideTree.drawrects = false;
        maxmizeSideTree.bottommargin = 0;
        //if(result!=null)
        maxmizeSideTree.treewidth = LeftTreeWidth;
        maxmizeSideTree.generatecoords();
        
        rotatedSideTreeImgUrl = this.generateEncodedImg(maxmizeSideTree.getImage());
        
        
        
        
        
        return this.generateEncodedImg(sideTree.getImage());

    }

    public String generateTopTree(Node toproot) {
        int horizontalItems = countgenes(toproot);
        toproot.mark = true;
        upperTree = new TreeView(toproot, horizontalItems,  Color.WHITE, Color.black);//Color.decode("#e3e3e3")
        upperTree.leafdist = squareW;
        upperTree.actualLength = horizontalItems;
        upperTree.setHorizontal(false);
        upperTree.leftmargin = (int) Math.round(squareW / 2);
        upperTree.drawframe = false;
        upperTree.valuedistances = ValueDistances;
        upperTree.topmargin = 0;
        upperTree.drawrects = false;
        upperTree.bottommargin = 0;
        upperTree.rightmargin = 0;
        upperTree.treewidth = TopTreeHeight;
        upperTree.generatecoords();
        TopTreeWidth = upperTree.getWidth();
        tooltipsUpperNode = initToolTips(toproot, null);
        
       
        maxmizeUpperTree = new TreeView(toproot, horizontalItems,  Color.WHITE, Color.black);//Color.decode("#e3e3e3")
        maxmizeUpperTree.leafdist = squareW;
        maxmizeUpperTree.actualLength = horizontalItems;
        maxmizeUpperTree.setHorizontal(true);
        maxmizeUpperTree.leftmargin = (int) Math.round(squareW / 2);
        maxmizeUpperTree.drawframe = false;
        maxmizeUpperTree.valuedistances = ValueDistances;
        maxmizeUpperTree.topmargin = 0;
        maxmizeUpperTree.drawrects = false;
        maxmizeUpperTree.bottommargin = 0;
        maxmizeUpperTree.rightmargin = 0;
        maxmizeUpperTree.treewidth = TopTreeHeight;
        maxmizeUpperTree.generatecoords();        
        rotatedTopTreeImgUrl = this.generateEncodedImg(maxmizeUpperTree.getImage());
        
        
        
        

        return this.generateEncodedImg(upperTree.getImage());

    }

    public String getRotatedTopTreeImgUrl() {
        return rotatedTopTreeImgUrl;
    }

    public void setRotatedTopTreeImgUrl(String rotatedTopTreeImgUrl) {
        this.rotatedTopTreeImgUrl = rotatedTopTreeImgUrl;
    }

    public String getRotatedSideTreeImgUrl() {
        return rotatedSideTreeImgUrl;
    }

    public void setRotatedSideTreeImgUrl(String rotatedSideTreeImgUrl) {
        this.rotatedSideTreeImgUrl = rotatedSideTreeImgUrl;
    }

    public String getRotatedHeatMapImgUrl() {
        return rotatedHeatMapImgUrl;
    }

    public void setRotatedHeatMapImgUrl(String rotatedHeatMapImgUrl) {
        this.rotatedHeatMapImgUrl = rotatedHeatMapImgUrl;
    }

    public String getRotatedInteractiveRowImgUrl() {
        return rotatedInteractiveRowImgUrl;
    }

    public void setRotatedInteractiveRowImgUrl(String rotatedInteractiveRowImgUrl) {
        this.rotatedInteractiveRowImgUrl = rotatedInteractiveRowImgUrl;
    }
    private ClientClusterNode tooltipsUpperNode;
    private ClientClusterNode tooltipsSideNode;

    public String generateHeatMap(Dataset dataset, boolean clustColumn) {

        colorFactory = new HeatmapColorFactory();
        BufferedImage heatMapImg = null;
        BufferedImage rotatedHeatmapImg = null;
        if (clustColumn) {
            heatMapImg = new BufferedImage((upperTree.getWidth() + 12), (sideTree.getHeight() - 7), BufferedImage.TYPE_INT_ARGB);
            rotatedHeatmapImg = new BufferedImage((upperTree.getWidth() + 12), (sideTree.getHeight() - 7), BufferedImage.TYPE_INT_ARGB);
        } else {
            heatMapImg = new BufferedImage((dataset.getColumnIds().length * 12 + 12), (sideTree.getHeight() - 7), BufferedImage.TYPE_INT_ARGB);
              rotatedHeatmapImg = new BufferedImage((dataset.getColumnIds().length * 12 + 12), (sideTree.getHeight() - 7), BufferedImage.TYPE_INT_ARGB);
            TopTreeWidth = dataset.getColumnIds().length * 12 + 12;

        }
        Graphics g = heatMapImg.getGraphics();
        g.setFont(getTableFont(12));

        drawSquares(g, new Point(0, 0), null, dataset, clustColumn);

        String defaultHeatMap = this.generateEncodedImg(heatMapImg);
       
        
        
          
  
        int         width  = heatMapImg.getWidth();  
        int         height = heatMapImg.getHeight();  
           rotatedHeatmapImg = new BufferedImage( height, width, heatMapImg.getType() );  
  
        for( int i=0 ; i < width ; i++ )  
            for( int j=0 ; j < height ; j++ )  
                rotatedHeatmapImg.setRGB( height-1-j, i, heatMapImg.getRGB(i,j) );   
   
        
//        AffineTransform xform = new AffineTransform();//.getScaleInstance(0,0);
//         
//
//        xform.translate(0.5 * heatMapImg.getHeight(null),0.5 * heatMapImg.getWidth(null));
//        double theta = Math.PI / 2;        
//        xform.rotate(theta);
//        
//        xform.translate(-0.5 * heatMapImg.getWidth(null), -0.5 * heatMapImg.getHeight(null));
//       
//        AffineTransformOp op = new AffineTransformOp(xform,AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//        rotatedHeatmapImg = op.filter(heatMapImg, null);
//        
//         AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
//        tx.translate(-rotatedHeatmapImg.getWidth(), -rotatedHeatmapImg.getHeight());
//        AffineTransformOp op1 = new AffineTransformOp(tx,AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//        rotatedHeatmapImg = op1.filter(rotatedHeatmapImg, null);
//
////      
//
//    
    
        rotatedHeatMapImgUrl = this.generateEncodedImg(rotatedHeatmapImg);

        return defaultHeatMap;
    }

    public String generateScale(Dataset dataset,boolean clustColumn) {
         int W = 0;
         if (clustColumn) {
              W = (Math.min((upperTree.getWidth() + 21), 250));
         }else{
             W = (Math.min((dataset.getColumnIds().length*12 + 21), 250));
         }
             BufferedImage nfo = new BufferedImage(W, 50, BufferedImage.TYPE_INT_ARGB);
             Graphics g = nfo.getGraphics();
             g.setFont(getTableFont(9));
             drawScale(g, new Point(0, 0), W, 30);
             return this.generateEncodedImg(nfo);

    }

    private Rectangle getSquaresBounds(Dataset dataset) {
        int[] upperArrangement = null;
        if (upperTree == null || upperTree.arrangement == null) {
            upperArrangement = new int[dataset.getDataWidth()];
        } else {
            upperArrangement = upperTree.arrangement;
        }
        if (upperArrangement == null || sideTree == null) {
            return new Rectangle(0, 0);
        }
        return new Rectangle(upperArrangement.length * squareW + 1, (sideTree.actualLength * squareL) + 1);
    }

    private void drawScale(Graphics scale, Point st, int width, int height) {
        Rectangle r = new Rectangle(st.x, st.y, width, height);
        if (width < 50 || height < 25) {
            return;
        }
        ScaleAndAxis sc = new ScaleAndAxis();
        sc.setColorFactory(colors);
        scale.translate(st.x, st.y);
        Rectangle bac = scale.getClipBounds();
        sc.setLocation(r.x, r.y);
        sc.setSize(r.width, r.height);
        sc.paintComponent(scale);
        scale.setClip(bac);
        scale.translate(-st.x, -st.y);
    }

    private void drawSquares(Graphics squares, Point start, Rectangle bounds, Dataset dataset,boolean clusterColumns) {
//        ColorFactory colors = ColorFactoryList.getInstance().getActiveColorFactory(dataset);
        colors = colorFactory.getActiveColorFactory(dataset);
        Rectangle view = getSquaresBounds(dataset);
        squares.translate(start.x, start.y);
        int rows = this.countgenes(this.rowNode);
        int counter = 0;
        double[] gengenscalevals = null;
        int[] upperArrangement = null;
        if (clusterColumns) {
            upperArrangement = upperTree.arrangement;
        } else {
            upperArrangement = new int[dataset.getColumnIds().length];
            for (int x=0;x<dataset.getColumnIds().length;x++)
                upperArrangement[x]=x;
        }
        double[][] dat = null;
        dat = dataset.getData();
        if (sideTree == null) {
            return;
        }
        for (int i = 0; i < sideTree.arrangement.length; i++) {
            double v = 0;
            Rectangle sqr = new Rectangle(0, 0, squareW, squareL);
            for (int j = 0; j < upperArrangement.length; j++) {
                if (bounds == null || bounds.intersects((j * squareW), (i * squareL), squareW, squareL)) {

                    if (upperTree != null) {

                        sqr.setLocation((j * squareW), (i * squareL));
                        if (!view.intersects(sqr)) {
                            continue;
                        }

                        if (sideTree.arrangement[i] != -1 && upperArrangement[j] != -1) {

                            if (dataset.isMissing(sideTree.arrangement[i], upperArrangement[j])) {
                                squares.setColor(colors.getMissing());
                            } else {
                                if (!gengenscale) {
                                    v = dat[sideTree.arrangement[i]][upperArrangement[j]];
                                    squares.setColor(colors.getColor(v));
                                } else {
                                    v = gengenscalevals[upperArrangement[j]];
                                    squares.setColor(colors.getColor(v));
                                }
                            }
                            squares.fillRect((j * squareW), (i * squareL), squareW, squareL);
                        }
                    } else {
                        sqr.setLocation((j * squareW), (i * squareL));
                        if (!view.intersects(sqr)) {
                            continue;
                        }

                        v = dat[sideTree.arrangement[i]][upperArrangement[j]];

                        if (dataset.isMissing(sideTree.arrangement[i], upperArrangement[j])) {
                            squares.setColor(colors.getMissing());
                        } else {
                            squares.setColor(colors.getColor(v));
                        }

                        squares.fillRect((j * squareW), (i * squareL), squareW, squareL);
                    }
                }
            }
            counter++;
            if (counter == rows) {
                break;
            }
        }
        counter = 0;
        if (true) {
            squares.setColor(GridCol);
            for (int i = 0; i < sideTree.arrangement.length + 1; i++) {
                if (bounds == null || bounds.intersects(0, i * squareL, upperArrangement.length * squareW, i * squareL)) {
                    squares.drawLine(0, i * squareL, (upperArrangement.length * squareW) + 0, i * squareL);
                }
                counter++;
                if (counter > rows) {
                    break;
                }
            }
            for (int j = 0; j < upperArrangement.length; j++) {
                if (bounds == null || bounds.intersects(j * squareW, 0, j * squareW, rows * squareL)) {
                    squares.drawLine(j * squareW, 0, j * squareW, rows * squareL);
                }
            }

            if (bounds == null || bounds.intersects(upperArrangement.length * squareW, 0, upperArrangement.length * squareW, rows * squareL)) {
                squares.drawLine(upperArrangement.length * squareW, 0, upperArrangement.length * squareW, rows * squareL);
            }

        }
        squares.translate(-start.x, -start.y);
    }

    private Font getTableFont(int size) {
        Font f;
        f = new Font("Sans Serif", 0, size);
        return f.deriveFont((float) size);
    }

    public int countgenes(Node trunk) {
        java.util.Stack c = new java.util.Stack();
        int ret = 0;
        c.push(trunk);
        Node tr = trunk;

        if (trunk == null) {
            System.out.print("\n!No trunk\n");
        }

        while (!c.empty()) {
            tr = (Node) c.pop();

            if (tr.merged) {
                c.push(tr.left);
                c.push(tr.right);
            } else {
                ret++;
            }
        }

        return ret;
    }

    public Node getNodeAt(int xcor, int ycor, Node trunk) {
        Node ret = null;

        if (trunk != null) {

            if (trunk.getx() > xcor - squareL && trunk.getx() < xcor + squareL
                    && trunk.gety() > ycor - squareL && trunk.gety() < ycor + squareL) {
                ret = trunk;
            } else {
                ret = getNodeAt(xcor, ycor, trunk.right);
            }
            if (ret == null) {
                ret = getNodeAt(xcor, ycor, trunk.left);
            }
        }

        return ret;
    }

    private String generateEncodedImg(BufferedImage upperTreeBImage) {
        String sideTreeBase64 = "";
        try {
            byte[] imageData = ChartUtilities.encodeAsPNG(upperTreeBImage);
            sideTreeBase64 = Base64.encodeBase64String(imageData);
            sideTreeBase64 = "data:image/png;base64," + sideTreeBase64;
        } catch (IOException exp) {
            System.err.println(exp.getLocalizedMessage());
        }
        return sideTreeBase64;
    }

    public SomClustTreeSelectionUpdate updateSideTreeSelection(int x, int y, double w, double h) {
        BufferedImage bImage = sideTree.getImage();
        BufferedImage rbImage = maxmizeSideTree.getImage();
        SomClustTreeSelectionUpdate result = new SomClustTreeSelectionUpdate();
        Node n = this.getNodeAt(x, y, rowNode);
        
        if (n != null) {
            sideTree.painttree(n, bImage.getGraphics(), Color.red);
             maxmizeSideTree.painttree(n, rbImage.getGraphics(), Color.red);
            Stack st = new Stack();
            Vector v = new Vector();
            n.fillMembers(v, st);
            int[] sel = new int[v.size()];
            for (int i = 0; i < v.size(); i++) {
                sel[i] = ((Integer) v.elementAt(i));
            }
            result.setSelectedIndices(sel);
        }
        try {
            byte[] imageData = ChartUtilities.encodeAsPNG(bImage);
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;
            result.setTreeImgUrl(base64);
            
            
            byte[] rImageData = ChartUtilities.encodeAsPNG(rbImage);
            String rbase64 = Base64.encodeBase64String(rImageData);
            rbase64 = "data:image/png;base64," + rbase64;
            result.setRotatedTreeImgUrl(rbase64);
            return result;

        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return null;

    }

    public SomClustTreeSelectionUpdate updateUpperTreeSelection(int x, int y, double w, double h) {
        BufferedImage bImage = upperTree.getImage();
         BufferedImage rbImage = maxmizeUpperTree.getImage();
        Node n = this.getNodeAt(y, x, colNode);
        SomClustTreeSelectionUpdate result = new SomClustTreeSelectionUpdate();
        if (n != null) {
            upperTree.painttree(n, bImage.getGraphics(), Color.red);
            maxmizeUpperTree.painttree(n, rbImage.getGraphics(), Color.red);
            
            Stack st = new Stack();
            Vector v = new Vector();
            n.fillMembers(v, st);
            int[] sel = new int[v.size()];
            for (int i = 0; i < v.size(); i++) {
                sel[i] = ((Integer) v.elementAt(i));
            }
            result.setSelectedIndices(sel);
        }
        try {
            byte[] imageData = ChartUtilities.encodeAsPNG(bImage);
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;
            result.setTreeImgUrl(base64);
            
            byte[] rImageData = ChartUtilities.encodeAsPNG(rbImage);
            String rbase64 = Base64.encodeBase64String(rImageData);
            rbase64 = "data:image/png;base64," + rbase64;
            result.setRotatedTreeImgUrl(rbase64);
            
            
            
            
            return result;
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return null;

    }


    private ClientClusterNode initToolTips(Node root, ClientClusterNode parent) {
        if (root != null) {
            ClientClusterNode clientClusterNode = new ClientClusterNode();
            clientClusterNode.setMembers(root.members);
            clientClusterNode.setX(root.getx());
            clientClusterNode.setY(root.gety());
            clientClusterNode.setName(root.nme + "");
            try {
                clientClusterNode.setValue(Double.valueOf(numformat.format(root.value)));
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                clientClusterNode.setValue(root.value);
            }
            clientClusterNode.setParent(parent);
            clientClusterNode.setRight(initToolTips(root.right, clientClusterNode));
            clientClusterNode.setLeft(initToolTips(root.left, clientClusterNode));
            return clientClusterNode;
        } else {
            return null;
        }

    }

    public ClientClusterNode getTooltipsUpperNode() {
        return tooltipsUpperNode;
    }

    public ClientClusterNode getTooltipsSideNode() {
        return tooltipsSideNode;
    }

    public int getLeftTreeWidth() {
        return LeftTreeWidth;
    }

    public int getTopTreeHeight() {
        return TopTreeHeight;
    }

    public Image rotatImg(BufferedImage upperTreeBImage) {
        Image image=null;
        AffineTransform identity = new AffineTransform();

        Graphics2D g2d = (Graphics2D) upperTreeBImage.getGraphics();
        AffineTransform trans = new AffineTransform();
        trans.setTransform(identity);
        trans.rotate(Math.toRadians(45));
        g2d.drawImage(image, trans, null);
        return image;

    }
}
