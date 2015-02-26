package web.diva.server.model;
/*
*
*used
 */
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.uib.jexpress_modularized.core.dataset.Group;
//import org.apache.batik.transcoder.Transcoder;
import org.apache.fop.svg.PDFTranscoder;
import web.diva.server.model.beans.DivaDataset;
import java.awt.Rectangle;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg12.SVG12DOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
//import org.apache.batik.svggen.SVGGraphics2D;
//import org.apache.batik.dom.svg.SVGDOMImplementation;
//import org.apache.batik.dom.svg12.SVG12DOMImplementation;
//
//import org.apache.batik.transcoder.TranscoderInput;
//import org.apache.batik.transcoder.TranscoderOutput;


import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;
import web.diva.server.model.SomClustering.SomClustImgGenerator;
import web.diva.shared.beans.RankResult;

public class DivaUtil implements Serializable{

    public Map<Integer, String> initIndexNameGeneMap(String[] rowIds) {
        Map<Integer, String> geneMap = new HashMap<Integer, String>();

        for (int index = 0; index < rowIds.length; index++) {
            geneMap.put(index, rowIds[index]);
        }
        return geneMap;
    }

    public DivaDataset initDivaDs(DivaDataset divaDataset, int datasetId) {
        divaDataset.setId(datasetId);
        Map<Integer, String> geneIndexNameMap = initIndexNameGeneMap(divaDataset.getRowIds());
        divaDataset.setGeneIndexNameMap(geneIndexNameMap);
        divaDataset.setGeneNameIndexMap(initNameIndexGeneMap(geneIndexNameMap));
        if (!divaDataset.getColumnGroups().isEmpty()) {
            List<Group> tgl = new ArrayList<Group>();
            for (Group g : divaDataset.getColumnGroups()) {
                g.setGeneList(initGroupGeneList(divaDataset.getGeneIndexNameMap(), g.getMembers()));
                tgl.add(g);
            }
            divaDataset.getColumnGroups().clear();
            divaDataset.getColumnGroups().addAll(tgl);
        }

        if (!divaDataset.getRowGroups().isEmpty()) {
            List<Group> tempGroupList = new ArrayList<Group>();
            for (Group g : divaDataset.getRowGroups()) {
                g.setGeneList(initGroupGeneList(divaDataset.getGeneIndexNameMap(), g.getMembers()));
                tempGroupList.add(g);
            }
            divaDataset.getRowGroups().clear();
            divaDataset.getRowGroups().addAll(tempGroupList);

        }
//        Map<Integer, Number[]> membersMap = initIndexMembers(divaDataset);
//        divaDataset.setMembersMap(membersMap);
        return divaDataset;
    }

    public Map<String, Integer> initNameIndexGeneMap(Map<Integer, String> indexNameMap) {
        Map<String, Integer> geneMap = new HashMap<String, Integer>();
        for (int key : indexNameMap.keySet()) {
            geneMap.put(indexNameMap.get(key), key);
        }
        return geneMap;
    }

    public List<String> initGroupGeneList(Map<Integer, String> geneIndexNameMap, int[] members) {
        List<String> geneList = new ArrayList<String>();
        for (int x : members) {
            geneList.add(geneIndexNameMap.get(x));
        }

        return geneList;

    }

//    public Map<Integer, Number[]> initIndexMembers(DivaDataset dataset) {
//        Map<Integer, Number[]> memberMaps = new HashMap<Integer, Number[]>();
//        for (int x = 0; x < dataset.getDataLength(); x++) {
//            double[] row = dataset.getData()[x];
//            Number[] points = new Number[row.length];
//            for (int y = 0; y < dataset.getDataWidth(); y++) {
//                points[y] = row[y];
//            }
//
//            memberMaps.put(x, points);
//
//        }
//
//        return memberMaps;
//    }

    public String[] initGeneNamesArr(Map<Integer, String> geneIndexName) {
        String[] geneNameArr = new String[geneIndexName.size()];
        for (int key : geneIndexName.keySet()) {
            geneNameArr[key] = geneIndexName.get(key);
        }
        return geneNameArr;
    }

    public String[] updateGroupsColorArray(String[] geneNameArr, List<Group> groupList) {
        String[] colorArr = new String[geneNameArr.length];
        for (int x = 0; x < geneNameArr.length; x++) {
            String color = "#BDBDBD";
            for (Group g : groupList) {
                if (g.getName().equalsIgnoreCase("all") && g.isActive()) {
                    color = "#000000";
                } else if (g.getGeneList().contains(geneNameArr[x]) && g.isActive()) {
                    color = g.getHashColor();
                }
                colorArr[x] = color;
            }
        }
        return colorArr;

    }

    public String exportDataTotext(DivaDataset divaDataset, int[] dataIndex, String path, String url, String textFileName) {
        File text = new File(path, textFileName + ".txt");
        try {
            if (text.exists()) {
                text.delete();
            }
            text.createNewFile();
            FileWriter outFile = new FileWriter(text, true);
            PrintWriter out1 = new PrintWriter(outFile);
            String header = divaDataset.getAnnotationHeaders()[0];//divaDataset.getInfoHeaders()[0];
            for (int z = 1; z < divaDataset.getAnnotationHeaders().length; z++) {
                header += "\t" + divaDataset.getAnnotationHeaders()[z];
            }

            for (String columnId : divaDataset.getColumnIds()) {
                header += "\t" + columnId;
            }
            try {
                out1.append(header);
                out1.println();
                Arrays.sort(dataIndex);
                for (int x : dataIndex) {
                    String[] annoRow = divaDataset.getAnnotations()[x];
                    String line = annoRow[0];
                    for (int z = 1; z < annoRow.length; z++) {
                        line += "\t" + annoRow[z];
                    }
                    double[] data = divaDataset.getData()[x];
                    for (int z = 0; z < data.length; z++) {
                        line += "\t" + data[z];
                    }
                    out1.append(line);
                    out1.println();

                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                out1.close();
                outFile.close();
            }
            System.gc();
            return url + text.getName();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.gc();
        }
        return "";

    }
    
     public String exportImgAsPdf(/*BufferedImage bimage,*/File userFolder, String url, String textFileName, Component component, Rectangle bounds) {
        try {

            final SVGGraphics2D svgGenerator = drawSvgGraphics(component, bounds);
            File pdfFile = new File(userFolder, textFileName + ".pdf");
            if (!pdfFile.exists()) {
                pdfFile.createNewFile();
            }
            // write the svg file
            File svgFile = new File(pdfFile.getAbsolutePath() + ".temp");

            OutputStream outputStream = new FileOutputStream(svgFile);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            final OutputStreamWriter out = new OutputStreamWriter(bos, "UTF-8");

            svgGenerator.stream(out, true /* use css */);
            System.gc();

            outputStream.flush();
            outputStream.close();
            bos.close();
            System.gc();

            String svgURI = svgFile.toURI().toString();
            TranscoderInput svgInputFile = new TranscoderInput(svgURI);

            OutputStream outstream = new FileOutputStream(pdfFile);
            bos = new BufferedOutputStream(outstream);
            TranscoderOutput output = new TranscoderOutput(bos);

            // write as pdf
            Transcoder pdfTranscoder = new PDFTranscoder();
            pdfTranscoder.addTranscodingHint(PDFTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, 0.084666f);
            pdfTranscoder.transcode(svgInputFile, output);

            outstream.flush();
            outstream.close();
            bos.close();
            System.gc();

//          
            return url + userFolder.getName() + "/" + pdfFile.getName();
        } catch (SVGGraphics2DIOException e) {
           e.printStackTrace();
        }catch(Exception exp){exp.printStackTrace();}

        return "";
    }

    public String exportImgAsPdf(File userFolder, String url, String textFileName, BufferedImage bimage) {
        try {

            SVGGraphics2D svgGenerator = drawSvgGraphics(new Dimension(bimage.getWidth()+10, bimage.getHeight()+10));
            File pdfFile = new File(userFolder, textFileName + ".pdf");
            if (!pdfFile.exists()) {
                pdfFile.createNewFile();
            }
            // write the svg file
            svgGenerator.drawImage(bimage, 10, 10, null);

           
            // write the svg file
            File svgFile = new File(pdfFile.getAbsolutePath() + ".temp");

            OutputStream outputStream = new FileOutputStream(svgFile);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            Writer out = new OutputStreamWriter(bos, "UTF-8");
            svgGenerator.stream(out, true /* use css */);
            outputStream.flush();
            outputStream.close();
            bos.flush();
            bos.close();

            String svgURI = svgFile.toURI().toString();
            TranscoderInput svgInputFile = new TranscoderInput(svgURI);

            OutputStream outstream = new FileOutputStream(pdfFile);
            bos = new BufferedOutputStream(outstream);
            TranscoderOutput output = new TranscoderOutput(bos);

            // write as pdf
            Transcoder pdfTranscoder = new PDFTranscoder();
            pdfTranscoder.addTranscodingHint(PDFTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, 0.084666f);
            pdfTranscoder.transcode(svgInputFile, output);

            outstream.flush();
            outstream.close();
            bos.close();
            System.gc();

            return url + userFolder.getName() + "/" + pdfFile.getName();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return "";
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public String exportClusteringImgAsPdf(File userFolder, String url, String textFileName, BufferedImage upperTreeBImg, BufferedImage sideTreeBImg, BufferedImage heatMapImg, BufferedImage interactiveColumnImg, boolean clustColumn) {
        try {
            int totalWidth = sideTreeBImg.getWidth() + heatMapImg.getWidth() + interactiveColumnImg.getWidth()+10;
            int totalHeight = 0;
            totalHeight = sideTreeBImg.getHeight()+10;
            if (clustColumn) {
                totalHeight += upperTreeBImg.getHeight();
            }
            // Get a SVGDOMImplementation and create an XML document
            DOMImplementation domImpl = new SVGDOMImplementation();
            String svgNS = "http://www.w3.org/2000/svg";
            SVGDocument svgDocument = (SVGDocument) domImpl.createDocument(svgNS, "svg", null);
            // Create an instance of the SVG Generator
            SVGGraphics2D svgGenerator = new SVGGraphics2D(svgDocument);
            svgGenerator.setSVGCanvasSize(new Dimension(totalWidth, totalHeight));
            svgGenerator.drawImage(upperTreeBImg, sideTreeBImg.getWidth()+5, 5, null);
            svgGenerator.drawImage(sideTreeBImg, 5, upperTreeBImg.getHeight()+5, null);
            svgGenerator.drawImage(heatMapImg, sideTreeBImg.getWidth()+5, upperTreeBImg.getHeight()+5, null);
            svgGenerator.drawImage(interactiveColumnImg, sideTreeBImg.getWidth() + heatMapImg.getWidth()+5, upperTreeBImg.getHeight()+5, null);

            File pdfFile = new File(userFolder, textFileName + ".pdf");
            if (!pdfFile.exists()) {
                pdfFile.createNewFile();
            }
            // write the svg file
            File svgFile = new File(pdfFile.getAbsolutePath() + ".temp");

            OutputStream outputStream = new FileOutputStream(svgFile);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            OutputStreamWriter out = new OutputStreamWriter(bos, "UTF-8");
            svgGenerator.stream(out, true /* use css */);
            outputStream.flush();
            outputStream.close();
            bos.close();

            String svgURI = svgFile.toURI().toString();
            TranscoderInput svgInputFile = new TranscoderInput(svgURI);

            OutputStream outstream = new FileOutputStream(pdfFile);
            bos = new BufferedOutputStream(outstream);
            TranscoderOutput output = new TranscoderOutput(bos);

            // write as pdf
            Transcoder pdfTranscoder = new PDFTranscoder();
            pdfTranscoder.addTranscodingHint(PDFTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, 0.084666f);
            pdfTranscoder.transcode(svgInputFile, output);

            outstream.flush();
            outstream.close();
            bos.close();
            System.gc();

            return url + userFolder.getName() + "/" + pdfFile.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private SVGGraphics2D drawSvgGraphics(Object component, Rectangle bounds) {

        // Get a SVGDOMImplementation and create an XML document
        DOMImplementation domImpl = new SVG12DOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg";
        SVGDocument svgDocument = (SVGDocument) domImpl.createDocument(svgNS, "svg", null);

        // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(svgDocument);
        svgGenerator.setSVGCanvasSize(bounds.getSize());
        ((JComponent) component).paintAll(svgGenerator);
        return svgGenerator;
    }

    private SVGGraphics2D drawSvgGraphics(Dimension bounds) {
        // Get a SVGDOMImplementation and create an XML document
        DOMImplementation domImpl = new SVGDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg";
        SVGDocument svgDocument = (SVGDocument) domImpl.createDocument(svgNS, "svg", null);
        // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(svgDocument);
        svgGenerator.setSVGCanvasSize(bounds.getSize());
        return svgGenerator;
    }

    public void getClusteringImg(SomClustImgGenerator clusteringComponent) {

//BufferedImage upperTreeImg = 
//BufferedImage overlay = ImageIO.read(new File(path, "overlay.png"));
//
//// create the new upperTreeImg, canvas size is the max. of both upperTreeImg sizes
//int w = Math.max(upperTreeImg.getWidth(), overlay.getWidth());
//int h = Math.max(upperTreeImg.getHeight(), overlay.getHeight());
//BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//
//// paint both images, preserving the alpha channels
//Graphics g = combined.getGraphics();
//g.drawImage(upperTreeImg, 0, 0, null);
//g.drawImage(overlay, 0, 0, null);
//
//// Save as new upperTreeImg
//ImageIO.write(combined, "PNG", new File(path, "combined.png"));
//   int totalWidth = clusteringComponent.getSideTree().getWidth()+clusteringComponent.getTopTreeWidth()+clusteringComponent.ge
//   BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//   
    }

    public String exportRankProductToText(File userFolder, String url, String textFileName, RankResult results) {

        File text = new File(userFolder, textFileName + ".txt");
        PrintWriter out1 = null;
        FileWriter outFile = null;
        try {
            if (text.exists()) {
                text.delete();
            }
            text.createNewFile();
            outFile = new FileWriter(text, true);
            out1 = new PrintWriter(outFile);
            String[] headers = results.getHeaders();
            String headerLine = "";
            for (String header : headers) {
                headerLine += header+"\t" ;
            }
            headerLine = headerLine.substring(0,headerLine.length()-2);

            out1.append(headerLine);
            out1.println();
            System.out.println(headerLine);
            for (int x = 0; x < results.getRowIds().length; x++) {
                String record = "";
//                int posReIndexer = results.getPosIndexToRank()[x];
//                int negReIndexer = results.getNegRankToIndex()[results.getPosIndexToRank()[x] - 1];
                int coulmnReindex = 0;
                for (int y = 0; y < headers.length; y++) {

                    if (y == 0) {
                        record += (Integer) results.getPosRank()[x];
                    } else if (y == 1) {
                        record += results.getRowIds()[x];
                    } else if (y == 6) {
                        record += results.getNegRank()[x];
                    } else {
                        record += results.getTableData()[coulmnReindex][x];
                        coulmnReindex++;

                    }
                    if ((x != (results.getRowIds().length - 1) && (y != headers.length - 1))) {
                        record += "\t";
                    }
                    
                }
                
                out1.append(record);                
                out1.println();
            }
            out1.flush();
            out1.close();
            outFile.flush();
            outFile.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            System.gc();
        }
        return url + userFolder.getName() + "/" +text.getName();
    }

}
