/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.pca.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.IButton;
import java.util.HashMap;
import web.diva.client.GreetingServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.PCAImageResult;

/**
 *
 * @author Yehia Farag PCA plot container for pca-plot images
 */
public class PCAPlot extends ModularizedListener {

    private SelectionManager selectionManager;
    private boolean zoom = false;
    private boolean selectAll = false;
    private GreetingServiceAsync greetingService;
    private IButton resetPlotBtn;
    private boolean enable = true;
    private int[] currentDataSet;
    private int[] selectedRows;
    private final int height = 212;
    private final int width = 220;
    private MouseMoveHandler mouseMoveHandler;
    private MouseOutHandler mouseOutHandler;
    private MouseUpHandler mouseUpHandler;
    private MouseDownHandler mouseDownHandler;
    private HandlerRegistration moveRegHandler;
    private HandlerRegistration outRegHandler;
    private HandlerRegistration upRegHandler;
    private HandlerRegistration downRegHandler;
    private int startX;
    private int endX;
    private int startY;
    private int endY;
    private boolean clicked = false;
    private int absStartX, absStartY;
    private HashMap<String, String> tooltips;

    @Override
    public String toString() {
        return "PCAPlot";
    }

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows();
            if (sel != null && !zoom && !selectAll) {
                selectedRows = sel.getMembers();
                if (selectedRows != null && selectedRows.length != 0) {
                    updateSelection(selectedRows);
                }
            }
        }
    }
    private VerticalPanel MainPCALayout;
    private VerticalPanel imgLayout;
    private HorizontalPanel buttonsLayout;
//    private PCAPoint[] selectionIndexMap;
    private Image chart;
    private HTML toolTip = new HTML();
    private final HTML rect;

    public PCAPlot(final PCAImageResult results, SelectionManager selectionManager, GreetingServiceAsync greetingService) {

        this.greetingService = greetingService;
        this.classtype = 4;
        this.components.add(PCAPlot.this);
        this.selectionManager = selectionManager;
        this.selectionManager.addSelectionChangeListener(PCAPlot.this);
        MainPCALayout = new VerticalPanel();
        MainPCALayout.setHeight(height + "px");
        MainPCALayout.setWidth(width + "px");
        MainPCALayout.setBorderWidth(1);
       
        RootPanel.get("tooltip").add(toolTip);
        imgLayout = new VerticalPanel();

        imgLayout.setHeight((height - 25) + "px");
//        imgLayout.setHeight(height+ "px");
        imgLayout.setWidth(width + "px");

        buttonsLayout = new HorizontalPanel();
        buttonsLayout.setWidth(width + "px");
        buttonsLayout.setHeight("25px");

        MainPCALayout.add(imgLayout);
        CheckBox cb = new CheckBox("Zoom");
        resetPlotBtn = new IButton("Zoom Out");
        resetPlotBtn.setTooltip("Reset the Plot");
        resetPlotBtn.setSize("50px", "20px");
        cb.setChecked(false);    // Hook up a handler to find out when it's clicked.
        cb.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                zoom = ((CheckBox) event.getSource()).isChecked();
//                if (!zoom) {
//                    updateWithSelection();
////                }
            }
        });

        buttonsLayout.add(cb);

        CheckBox cb2 = new CheckBox("Show All");
        cb2.setChecked(selectAll);    // Hook up a handler to find out when it's clicked.
        cb2.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                selectAll = ((CheckBox) event.getSource()).isChecked();
                showAll(selectAll);
//                if (!selectAll) {
//                    showAll()
//                    
//                    updateWithSelection();
//                } else {
//                    updateSelection(null);
//                }
            }
        });
     
        buttonsLayout.setBorderWidth(1);
        buttonsLayout.add(cb2);

        resetPlotBtn.disable();
        resetPlotBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (!zoom) {
                    resetPlotBtn.disable();
                    currentDataSet = null;
                    updateWithSelection();
                }
            }
        }
        );

        this.chart = new Image();
        chart.setHeight((height - 25.0) + "px");
//        chart.setHeight(height + "px");
        chart.setWidth(width + "px");
        imgLayout.add(chart);
        initChartImageListiners();
        //rectangle draw
        /**
         * ***
         */

        rect = new HTML();
        rect.setVisible(false);
        RootPanel.get().add(rect);

        /**
         * *****************
         */
        buttonsLayout.add(resetPlotBtn);

        buttonsLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        buttonsLayout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        MainPCALayout.add(buttonsLayout);
        MainPCALayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        MainPCALayout.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        updateWithSelection();

    }

    private void updateWithSelection() {
        Selection sel = selectionManager.getSelectedRows();
        if (sel != null) {
            selectedRows = sel.getMembers();
            this.updateSelection(selectedRows);
        } else {
            this.updateSelection(null);
        }

    }

    private void zoomIn() {
        greetingService.pcaZoom(zoom, startX, startY, endX, endY, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onSuccess(String result) {
                chart.setUrl(result);
                if (zoom) {
                    resetPlotBtn.enable();
                }
            }
        });

    }

    private void showAll(boolean showAll) {
        Selection sel = selectionManager.getSelectedRows();
        if (sel != null) {
            selectedRows = sel.getMembers();
        }
        greetingService.pcaShowAll(showAll, selectedRows, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onSuccess(String result) {
                chart.setUrl(result);
            }
        });

    }

    /**
     * This method is responsible for mapping the image selection coordinates to
     * omics data indexes
     */
    private void getSelection(int startX, int startY, int endX, int endY) {
        greetingService.getPCASelection(startX, startY, endX, endY, new AsyncCallback<int[]>() {
            @Override
            public void onFailure(Throwable caught) {
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(int[] result) {
                if (result != null && !zoom) {
                    updateSelectedList(result);
                } else if (result != null && zoom) {
                    updateSelection(result);

                }
            }
        });
    }

    private void updateSelectedList(int[] selIndex) {
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selIndex);
        selectionManager.setSelectedRows(selection);
    }

    public VerticalPanel PCAPlot() {
        return MainPCALayout;
    }

    private void updateSelection(int[] selection) {
        if (enable) {
            if (zoom) {
                currentDataSet = selection;
            }
            greetingService.updatePCASelection(currentDataSet, selectedRows, zoom, selectAll, width, (height - 25.0), new AsyncCallback<PCAImageResult>() {
                @Override
                public void onFailure(Throwable caught) {
                    RootPanel.get("loaderImage").setVisible(false);
                }

                @Override
                public void onSuccess(PCAImageResult result) {

                    chart.setUrl(result.getImgString());
                    tooltips = result.getXyName();
                    if (zoom) {
                        resetPlotBtn.enable();
                    }
                    RootPanel.get("loaderImage").setVisible(false);
                }
            });

        }
    }
private int selectionHeight,selectionWidth;//,startSelectionX,startSelectionY;
    /**
     * This method is responsible for initializing image information
     */
    private void initChartImageListiners() {

        if (mouseMoveHandler == null) {

            mouseMoveHandler = new MouseMoveHandler() {
                @Override
                public void onMouseMove(MouseMoveEvent event) {
                    double yi = event.getY();
                    double xi = event.getX();

                    int x = (int) xi;
                    int y = (int) yi;

                    if (clicked) {
                        //show rectangle
//                        int width = 0;
//                        int height = 0;
                        if (absStartX < event.getClientX()) {
                            selectionWidth = event.getClientX() - absStartX;
                        } else {
                            selectionWidth = absStartX - event.getClientX();
                            absStartX = event.getClientX();
                        }
                        if (absStartY < event.getClientY()) {
                            selectionHeight = event.getClientY() - absStartY;
                        } else {
                            selectionHeight = absStartY - event.getClientY();
                            absStartY = event.getClientY();
                        }
                        rect.setHTML("<p style='opacity: 0.6;z-index:9000000;position: absolute;height:" + (selectionHeight) + "px; width:" + (selectionWidth) + "px; left:" + absStartX + "px;top:" + absStartY + "px;font-weight: bold; color:white;font-size: 10px;background: #CECEF6; border-style:solid;'>" + "</p>");
                        rect.setVisible(true);
                    } else {

                        String key = x + "," + y;
                        String key1 = (x + 1) + "," + y;
                        String key2 = (x - 1) + "," + y;
                        String key3 = x + "," + (y + 1);
                        String key4 = x + "," + (y - 1);
                        if (tooltips.containsKey(key)) {
                            updateToolTip(tooltips.get(key));

                        } else if (tooltips.containsKey(key1)) {
                            updateToolTip(tooltips.get(key1));
                        } else if (tooltips.containsKey(key2)) {
                            updateToolTip(tooltips.get(key2));
                        } else if (tooltips.containsKey(key3)) {
                            updateToolTip(tooltips.get(key3));
                        } else if (tooltips.containsKey(key4)) {
                            updateToolTip(tooltips.get(key4));
                        } else {
                            toolTip.setText("");
                            toolTip.setVisible(false);
                        }
                    }
                }
            };

        }
        if (mouseOutHandler == null) {
            mouseOutHandler = new MouseOutHandler() {
                @Override
                public void onMouseOut(MouseOutEvent event) {
                    toolTip.setText("");
                    toolTip.setVisible(false);
                    rect.setVisible(false);
                }
            };
        }
        if (moveRegHandler != null) {
            moveRegHandler.removeHandler();
        }
        if (outRegHandler != null) {
            outRegHandler.removeHandler();
        }
       

        moveRegHandler = chart.addMouseMoveHandler(mouseMoveHandler);
        outRegHandler = this.chart.addMouseOutHandler(mouseOutHandler);
        if (mouseUpHandler == null) {
            mouseUpHandler = new MouseUpHandler() {

                @Override
                public void onMouseUp(MouseUpEvent event) {
                    endX = event.getX();
                    endY = event.getY();
                    rect.setVisible(false);
                    clicked = false;
                    //update 
                    if (zoom) {
                        zoomIn();
                    } else {
                          getSelection(startX, startY, endX, endY);
                    }
                }
            };
        }
        if (upRegHandler != null) {
            upRegHandler.removeHandler();
        }
        upRegHandler = chart.addMouseUpHandler(mouseUpHandler);

        if (mouseDownHandler == null) {
            mouseDownHandler = new MouseDownHandler() {
                @Override
                public void onMouseDown(MouseDownEvent event) {
                    clicked = true;
                    startX = event.getX();
                    startY = event.getY();
                    absStartX = event.getClientX();
                    absStartY = event.getClientY();
                    rect.setHTML("<p style='opacity: 0.6;z-index:9000000; position: absolute;height:" + (2) + "px; width:" + (2) + "px; left:" + absStartX + "px;top:" + absStartY + "px;font-weight: bold; color:white;font-size: 10px;background: #CECEF6; border-style:solid;'>" + "</p>");
                    rect.setVisible(true);
                    event.preventDefault();

                }
            };
        }
        if (downRegHandler != null) {
            downRegHandler.removeHandler();
        }
        downRegHandler = chart.addMouseDownHandler(mouseDownHandler);

    }

    private void updateToolTip(String lable) {
        String nString = "";
        if (lable.length() >= 30) {
            String row = "";
            for (String str : lable.split(",")) {
                nString = nString + str + ",";
                row = row + str + ",";
                if (row.length() >= 30) {
                    nString = nString + "<br/>";
                    row = "";
                }

            }

        } else {
            nString = lable;
        }

        toolTip.setHTML("<p style='height:55px;font-weight: bold; color:white;font-size: 10px;background: #819FF7; border-style:double;'>" + nString + "</p>");
        toolTip.setVisible(true);
    }

    public void enable(boolean enabel) {
        this.enable = enabel;

    }

    /**
     * This method is responsible for removing all references on removing
     * components
     */
    @Override
    public void remove() {
        selectionManager = null;
        mouseMoveHandler = null;
        mouseOutHandler = null;
        mouseUpHandler = null;
        mouseDownHandler = null;
        moveRegHandler = null;
        outRegHandler = null;
        upRegHandler = null;
        downRegHandler = null;
        MainPCALayout = null;
        imgLayout = null;
        buttonsLayout = null;
//        selectionIndexMap = null;
        chart = null;
        toolTip = null;

        greetingService = null;
        resetPlotBtn = null;
        currentDataSet = null;
        selectedRows = null;
    }

}
