/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.unused;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.somclust.computation.SOMClustCompute;
import no.uib.jexpress_modularized.somclust.model.ClusterParameters;
import no.uib.jexpress_modularized.somclust.model.ClusterResults;
import no.uib.jexpress_modularized.somclust.model.Node;
import web.diva.shared.CustomNode;
import web.diva.shared.Unit;
import web.diva.shared.unused.SomClusteringResult;

/**
 *
 * @author Yehia Farag
 */
public class SOMClustUtil {

    private DecimalFormat df = null;

    public Map<String, CustomNode> getNodesMap() {
        return nodesMap;
    }

    public SomClusteringResult initSelectedNodes(SomClusteringResult results) {
        HashMap<String, CustomNode> tNodesMap = results.getSideTree().getNodesMap();
        HashMap<String, CustomNode> updatedNodesMap = results.getSideTree().getNodesMap();
        for (String key : tNodesMap.keySet()) {
            CustomNode cn = tNodesMap.get(key);
            List<Integer> tempList = new ArrayList<Integer>();
            for (String str : cn.getChildernList()) {
                if (!str.contains("gene")) {
                    tempList.add(Integer.valueOf(str));
                }
            }

            int[] indexArr = new int[tempList.size()];
            for (int index = 0; index < tempList.size(); index++) {
                indexArr[index] = tempList.get(index);

            }
            cn.setSelectedNodes(indexArr);
            updatedNodesMap.put(key, cn);
        }
        results.getSideTree().setNodesMap(updatedNodesMap);
        results = initTopSelectedNodes(results);

        return results;
    }

    private SomClusteringResult initTopSelectedNodes(SomClusteringResult results) {
        HashMap<String, CustomNode> tNodesMap = results.getTopTree().getNodesMap();
        HashMap<String, CustomNode> updatedNodesMap = results.getTopTree().getNodesMap();
        for (String key : tNodesMap.keySet()) {
            CustomNode cn = tNodesMap.get(key);
            List<Integer> tempList = new ArrayList<Integer>();
            for (String str : cn.getChildernList()) {
                if (!str.contains("col")) {
                    tempList.add(Integer.valueOf(str));
                }
            }
            int[] indexArr = new int[tempList.size()];
            for (int index = 0; index < tempList.size(); index++) {
                indexArr[index] = tempList.get(index);
            }
            cn.setSelectedNodes(indexArr);
            updatedNodesMap.put(key, cn);
        }
        results.getTopTree().setNodesMap(updatedNodesMap);
        return results;
    }

    public SomClusteringResult initHC(Dataset dataset, int distance, String linkageType, boolean clusterColumn, int datasetId) {
        ClusterParameters parameter = new ClusterParameters();
        parameter.setDistance(distance);
        parameter.setClusterSamples(clusterColumn);
        ClusterParameters.LINKAGE link = null;
        if (linkageType.equalsIgnoreCase("UPGMA")) {
            link = ClusterParameters.LINKAGE.UPGMA;
        } else if (linkageType.equalsIgnoreCase("SINGLE")) {
            link = ClusterParameters.LINKAGE.SINGLE;
        } else if (linkageType.equalsIgnoreCase("COMPLETE")) {
            link = ClusterParameters.LINKAGE.COMPLETE;
        } else if (linkageType.equalsIgnoreCase("WPGMA")) {
            link = ClusterParameters.LINKAGE.WPGMA;
        }
        parameter.setLink(link);
        SOMClustCompute som = new SOMClustCompute(dataset, parameter);
        ClusterResults results = som.runClustering();
        SomClusteringResult somClustResults = new SomClusteringResult();
        if (clusterColumn) {
            Unit topTree = initTopTree(results.getColumnDendrogramRootNode());
            somClustResults.setTopTree(topTree);
        }
        Unit sideTreeUnit = initSideTree(results.getRowDendrogramRootNode());
        somClustResults.setSideTree(sideTreeUnit);
        somClustResults.setHeight(this.getHeightPix(dataset.getRowIds().length));
        somClustResults.setDatasetId(datasetId);
        return somClustResults;
    }

    private Unit initSideTree(Node root) {
        int index = 0;
        CustomNode parent = new CustomNode();
        parent.setIndex(index);
        parent.setName("gene " + index);
        parent.setIndex(index);

        parent.setChildernList(new HashSet<String>());
        parent.getChildernList().add("gene " + index);
        parent.setValue(root.getval());

        nodesMap.put("gene " + index, parent);
        Unit unit = new Unit("gene " + index, initSideUnit(root.left, parent), initSideUnit(root.right, parent));
        cleanNodesMap();
        unit.setNodesMap(nodesMap);
        unit.value = root.getval();

        return unit;

    }

    private Unit initTopTree(Node root) {
        int index = 0;
        CustomNode parent = new CustomNode();
        parent.setIndex(index);
        parent.setName("col " + index);
        parent.setIndex(index);

        parent.setChildernList(new HashSet<String>());
        parent.getChildernList().add("col " + index);
        parent.setValue(root.getval());

        colNodesMap.put("col " + index, parent);
        Unit unit = new Unit("col " + index, initTopUnit(root.left, parent), initTopUnit(root.right, parent));
        cleanColNodesMap();
        unit.setNodesMap(colNodesMap);
        unit.value = root.getval();

        return unit;

    }

    private final HashMap<String, CustomNode> nodesMap = new HashMap<String, CustomNode>();
    private final HashMap<String, CustomNode> colNodesMap = new HashMap<String, CustomNode>();

    private Unit initSideUnit(Node root, CustomNode parent) {
        Unit unit = null;
        CustomNode node = null;
        if (root == null) {
            return null;
        }
        if (root.left == null && root.right == null) {
            long localIndex = System.currentTimeMillis() * (long) (Math.random() * 90000000.0);
            unit = new Unit("" + root.nme, 1000);
            node = new CustomNode();
            node.setValue(root.getval());
            node.setChildernList(new HashSet<String>());
            node.setName("" + root.nme);
            node.setGeneIndex(root.nme);
            node.getChildernList().add("" + root.nme);
            node.setIndex(localIndex);
            nodesMap.put("" + root.nme, node);
            unit.value = root.value;
            parent.getChildernList().addAll(node.getChildernList());
            return unit;
        } else {
            long localIndex = System.currentTimeMillis() * (long) (Math.random() * 90000000.0);
            node = new CustomNode();
            HashSet<String> newChildernList = new HashSet<String>();
            node.setChildernList(newChildernList);
            node.setIndex(localIndex);
            node.setValue(root.getval());
            node.setName("gene " + localIndex);
            nodesMap.put("gene " + localIndex, node);
            node.getChildernList().add("gene " + localIndex);
            unit = new Unit(("gene " + localIndex), initSideUnit(root.left, node), initSideUnit(root.right, node));
            unit.value = root.value;
            parent.getChildernList().addAll(node.getChildernList());

        }
        return unit;
    }

    private Unit initTopUnit(Node root, CustomNode parent) {
        Unit unit = null;
        CustomNode node = null;
        if (root == null) {
            return null;
        }
        if (root.left == null && root.right == null) {
            long localIndex = System.currentTimeMillis() * (long) (Math.random() * 90000000.0);
            unit = new Unit("" + root.nme, 1000);
            node = new CustomNode();
            node.setValue(root.getval());
            node.setChildernList(new HashSet<String>());
            node.setName("" + root.nme);
            node.setGeneIndex(root.nme);
            node.getChildernList().add("" + root.nme);
            node.setIndex(localIndex);
            colNodesMap.put("" + root.nme, node);
            unit.value = root.value;
            parent.getChildernList().addAll(node.getChildernList());
            return unit;
        } else {
            long localIndex = System.currentTimeMillis() * (long) (Math.random() * 9000000.0);
            node = new CustomNode();
            HashSet<String> newChildernList = new HashSet<String>();
            node.setChildernList(newChildernList);
            node.setIndex(localIndex);
            node.setValue(root.getval());
            node.setName("col " + localIndex);
            colNodesMap.put("col " + localIndex, node);
            node.getChildernList().add("col " + localIndex);
            unit = new Unit(("col " + localIndex), initTopUnit(root.left, node), initTopUnit(root.right, node));
            unit.value = root.value;
            parent.getChildernList().addAll(node.getChildernList());

        }
        return unit;
    }

    public HashMap<String, String> initToolTips(Unit results, Map<Integer, String> geneMap) {
        HashMap<String, String> toolTipsMap = new HashMap<String, String>();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.##", otherSymbols);
        for (Object o : results.getNodesMap().keySet()) {
            String key = (String) o;
            CustomNode cNode = (CustomNode) results.getNodesMap().get(key);
            String value = "";
            if (cNode.getName().contains("gene")) {
                value = "Merged at " + df.format(cNode.getValue()) + " #Nodes : " + (cNode.getSelectedNodes().length);
            } else {
                value = geneMap.get(cNode.getGeneIndex());
            }
            toolTipsMap.put(key, value);
        }
        return toolTipsMap;
    }

    public HashMap<String, String> initTopToolTips(Unit results) {
        HashMap<String, String> toolTipsMap = new HashMap<String, String>();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.##", otherSymbols);
        for (Object o : results.getNodesMap().keySet()) {
            String key = (String) o;
            CustomNode cNode = (CustomNode) results.getNodesMap().get(key);
            String value = "";
            if (cNode.getName().contains("col")) {
                value = "";
            } else {
                value = cNode.getName();
            }
            toolTipsMap.put(key, value);
        }
        return toolTipsMap;
    }

    public int getHeightPix(int rowNumb) {
        int height = (rowNumb * 22);
        return height;

    }

    public void cleanNodesMap() {
        HashMap<String, CustomNode> cleanNodesMap = new HashMap<String, CustomNode>();
        cleanNodesMap.putAll(nodesMap);
        nodesMap.clear();
        for (String key : cleanNodesMap.keySet()) {
            CustomNode cn = cleanNodesMap.get(key);
            cn.getChildernList().remove(cn.getName());
            nodesMap.put(key, cn);
        }

    }

    public void cleanColNodesMap() {
        HashMap<String, CustomNode> cleanNodesMap = new HashMap<String, CustomNode>();
        cleanNodesMap.putAll(colNodesMap);
        colNodesMap.clear();
        for (String key : cleanNodesMap.keySet()) {
            CustomNode cn = cleanNodesMap.get(key);
            cn.getChildernList().remove(cn.getName());
            colNodesMap.put(key, cn);
        }

    }

}
