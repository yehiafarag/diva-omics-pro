/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.unused;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.Comparator;

import java.util.HashMap;
import org.thechiselgroup.choosel.protovis.client.PV;
import org.thechiselgroup.choosel.protovis.client.PVClusterLayout;
import org.thechiselgroup.choosel.protovis.client.PVDom;
import org.thechiselgroup.choosel.protovis.client.PVDomNode;
import org.thechiselgroup.choosel.protovis.client.PVEventHandler;
import org.thechiselgroup.choosel.protovis.client.PVLine;
import org.thechiselgroup.choosel.protovis.client.PVPanel;
import org.thechiselgroup.choosel.protovis.client.ProtovisWidget;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsArgs;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsDoubleFunction;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsStringFunction;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.CustomNode;
import web.diva.shared.Unit;
import web.diva.shared.UnitDomAdapter;
import web.diva.shared.unused.SomClusteringResult;

/**
 *
 * @author Yehia Farag
 * top-tree drawer
 */
public class TopTreeGraph extends ProtovisWidget implements IsSerializable {

    private Unit root;
    private SelectionManager selectionManager;
    private HashMap<String, CustomNode> nodesMap;
    private ArrayList<String> indexers = new ArrayList<String>();
    private boolean initIndexer = true;
    private double height;
    private double width;
    private final int datasetId;

    public void resize(double width, double height) {
        this.height = (int) height;
        this.width = (int) width;
        getPVPanel().render();

    }

    public ArrayList<String> getIndexers() {
        return indexers;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    public TopTreeGraph(SomClusteringResult results, String orintation, SelectionManager selectionManager, double height, double width) {

        this.datasetId = results.getDatasetId();
        this.root = results.getTopTree();
        this.nodesMap = new HashMap<String, CustomNode>();
        nodesMap.putAll(root.getNodesMap());
        this.selectionManager = selectionManager;
        this.width = (width / 3.0);
        this.height = height;
        results = null;

    }

    @Override
    protected void onAttach() {
        super.onAttach();
        initPVPanel();
        createVisualization(root);
        getPVPanel().render();
        initIndexer = false;
        root = null;
    }

    @Override
    protected void onDetach() {
        super.onDetach(); //To change body of generated methods, choose Tools | Templates.
        root = null;
        nodesMap = null;
        selectionManager = null;
        vis = null;
        indexers = null;
    }
    PVEventHandler nodeMouseClickHandler = new PVEventHandler() {
        @Override
        public void onEvent(com.google.gwt.user.client.Event e, String pvEventType, JsArgs args) {
            PVClusterLayout _this = args.getThis();
            PVDomNode d = args.getObject();
            if (_this != null && d != null && d.firstChild() != null && (!d.nodeName().equalsIgnoreCase(clickNode))) {
                clickNode = d.nodeName();
                clickedNode = nodesMap.get(clickNode);
                vis.render();
                clear = false;
                updateSelectedList(clickedNode.getSelectedNodes());
            }
        }
    };
    PVEventHandler nodeMouseOverHandler = new PVEventHandler() {
        @Override
        public void onEvent(com.google.gwt.user.client.Event e, String pvEventType, JsArgs args) {
            PVClusterLayout _this = args.getThis();
            PVDomNode d = args.getObject();
            if (_this != null && d != null) {
                overNode = d.nodeName();
                vis.render();
            }
        }
    };
    private PVPanel vis;
    private String clickNode = "";
    private String overNode = "";
    private CustomNode clickedNode;
    private boolean clear = true;

    private void createVisualization(Unit root) {
        vis = getPVPanel().width(width).height(height).left(0).right(5)
                .top(10).bottom(0);
        PVClusterLayout layout = vis
                .add(PV.Layout.Cluster())
                .nodes(PVDom.create(root, new UnitDomAdapter())
                        .sort(new Comparator<PVDomNode>() {
                            @Override
                            public int compare(PVDomNode o1, PVDomNode o2) {
                                return o1.nodeName().compareTo(o2.nodeName());
                            }
                        })
                        .nodes()).group(false).orient("top");

        layout.node().add(PV.Dot).radius(new JsDoubleFunction() {
            @Override
            public double f(JsArgs args) {
                PVDomNode n = args.getObject();
                if (initIndexer && indexers != null) {
                    if (n.firstChild() == null) {
                        indexers.add(n.nodeName());
                    }
                }

                if (n.nodeName().equalsIgnoreCase(overNode)) {
                    return 3.0;
                }
                return 1.0;
            }
        }).shape("square")
                .fillStyle(new JsStringFunction() {
                    @Override
                    public String f(JsArgs args) {
                        PVDomNode n = args.getObject();
                        if (n.nodeName().equalsIgnoreCase(overNode)) {
                            return "#FF4000";

                        }
                        return "#ccc";
                    }
                })
                .title(
                        new JsStringFunction() {
                            @Override
                            public String f(JsArgs args) {
                                PVDomNode n = args.getObject();
                                return n.firstChild() == null ? n.nodeName() : "";
                            }
                        })
                .events("all")
                .event(PV.Event.CLICK, nodeMouseClickHandler)
                .event(PV.Event.MOUSEOVER, nodeMouseOverHandler);

        PVLine line = layout.link().add(PV.Line);

        line.lineWidth(1d).strokeStyle(new JsStringFunction() {
            @Override
            public String f(JsArgs args) {
                PVDomNode n = args.getObject();
                if (clickedNode != null && clickedNode.getChildernList().contains(n.nodeName())) {
                    return "#FF4000";
                }

                return "#ccc";
            }
        });

    }

    private void updateSelectedList(int[] selIndex) {
        Selection selection = new Selection(Selection.TYPE.OF_COLUMNS, selIndex);
        selectionManager.setSelectedColumns(selection);

    }

    public void clearIndexer() {
        indexers = null;
    }

    public void clearSelection() {
        if (!clear) {
            clickedNode = null;
            overNode = "";
            clear = true;
            vis.render();
        }
    }

}
