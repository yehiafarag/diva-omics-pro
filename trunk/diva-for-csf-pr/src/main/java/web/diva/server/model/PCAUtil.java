/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import java.util.HashMap;
import java.util.TreeMap;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.pca.computation.PcaCompute;
import no.uib.jexpress_modularized.pca.computation.PcaResults;
import web.diva.server.model.beans.DivaDataset;
import web.diva.shared.beans.PCAImageResult;
import web.diva.shared.beans.PCAPoint;

/**
 *
 * @author Yehia Farag
 */
public class PCAUtil {

    public PcaResults getPCAResults(DivaDataset divaDataset, int pcx, int pcy) {
        PcaCompute pcaCompute = new PcaCompute(divaDataset);
        no.uib.jexpress_modularized.pca.computation.PcaResults jResults = pcaCompute.createPCA();
        TreeMap<Integer, PCAPoint> pointList = new TreeMap<Integer, PCAPoint>();
        for (Group g : divaDataset.getRowGroups()) {
            if (g.getName().equalsIgnoreCase("ALL")) {

                for (int i = 0; i < g.getIndices().size(); i++) {
                    PCAPoint point = new PCAPoint();
                    point.setX(jResults.ElementAt(g.getIndices().get(i), pcx));
                    point.setY(jResults.ElementAt(g.getIndices().get(i), pcy));
                    point.setGeneIndex(g.getIndices().get(i));
                    point.setGeneName(g.getGeneList().get(i));
                    point.setColor(g.getHashColor());
                    pointList.put(point.getGeneIndex(), point);
                }
                continue;

            }

            for (int i = 0; i < g.getIndices().size() && g.isActive(); i++) {
                PCAPoint point = new PCAPoint();
                point.setX(jResults.ElementAt(g.getIndices().get(i), pcx));

                point.setY(jResults.ElementAt(g.getIndices().get(i), pcy));
                point.setGeneIndex(g.getIndices().get(i));
                point.setGeneName(g.getGeneList().get(i));
                point.setColor(g.getHashColor());
                if (!(pointList.containsKey(point.getGeneIndex()) && point.getColor().equalsIgnoreCase("#000000"))) {
                    pointList.remove(point.getGeneIndex());
                    pointList.put(point.getGeneIndex(), point);

                }
            }

        }

//        PCAResults res = new PCAResults();
//        res.setPoints(pointList);
//        res.setPcai(pcx);
//        res.setPcaii(pcy);
//        res.setHeader(divaDataset.getInfoHeaders()[0]);
        return jResults;
    }

    public Object[] getTooltips(PCAImageResult results, TreeMap<Integer, PCAPoint> pointList) {
        PCAPoint[] indexeMap = new PCAPoint[pointList.size()];
        HashMap<String, String> xyName = new HashMap<String, String>();
        double xTicksNum = results.getMaxX() - results.getMinX();
        double xArea = results.getDataAreaMaxX() - results.getDataAreaMinX();
        double xFactor = xArea / xTicksNum;
        double yTicksNum = results.getMaxY() - results.getMinY();
        double yArea = results.getDataAreaMaxY() - results.getDataAreaMinY();
        double yFactor = yArea / yTicksNum;
        for (PCAPoint p : pointList.values()) {
            double conX = (p.getX() - results.getMinX()) * xFactor;
            if (conX < 0.0) {
                conX = conX * -1.0;
            }
            conX = conX + results.getDataAreaMinX();

            double conY = (results.getMaxY() - p.getY()) * yFactor;
            if (conY < 0.0) {
                conY = conY * -1.0;
            }
            conY = conY + results.getDataAreaMinY();
            PCAPoint uP = new PCAPoint();
            uP.setX(conX);
            uP.setY(conY);
            uP.setGeneIndex(p.getGeneIndex());
            indexeMap[p.getGeneIndex()] = uP;
            String str = "" + ((int) conX) + "," + ((int) conY);
            if (xyName.containsKey(str)) {
                String tooltip = xyName.get(str);
                tooltip = tooltip + "," + p.getGeneName();
                xyName.put(str, tooltip);
            } else {
                xyName.put(str, p.getGeneName());
            }
        }
        Object[] obj = new Object[2];
        obj[0] = xyName;
        obj[1] = indexeMap;
        return obj;
    }
}
