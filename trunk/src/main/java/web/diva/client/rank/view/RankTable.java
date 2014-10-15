/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.rank.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import web.diva.client.GreetingServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;

/**
 *
 * @author Yehia Farag
 * rank table components
 */
public final class RankTable extends ModularizedListener implements SelectionChangedHandler, IsSerializable {

    private boolean mouseSelection = false;
    private final String type;

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (selectionTag) {
            selectionTag = false;
            return;
        }
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows();
            if (sel != null && sel.getMembers().length > 0) {
                this.indexToRank(sel.getMembers());

            }
        }
    }
    /**
     * This method is responsible for re-indexing omics data indexes to rank indexes (pre-update visualization method)
     * @param index - selected omics data indexes
     */
    private int[] indexToRank(int[] index) {
        greetingService.indexToRank(index, tableType, new AsyncCallback<int[]>() {
            @Override
            public void onFailure(Throwable caught) {
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(int[] result) {
                if (result.length != 0) {
                    table.deselectAllRecords();
                    table.selectRecords(result);
                    table.scrollToRow(result[0]);
                }

            }
        });
        return null;
    }

    @Override
    public void onSelectionChanged(SelectionEvent event) {
        if (mouseSelection) {
            return;
        }
        ListGridRecord[] selectionRecord = table.getSelectedRecords();
        updateTableSelection(selectionRecord);

    }

    private final ListGrid table;
    private ListGridRecord[] records;
    private final SelectionManager selectionManager;
    private final int[] rankToIndex, indexToRank;
    private boolean selectionTag = false;
    private int tableType = 1;
    private GreetingServiceAsync greetingService;

    public RankTable(GreetingServiceAsync greetingService, SelectionManager selectionManager, int datasetId, String[] headers, String[][] tableData, int[] rankToIndex, int[] indexToRank, String type) {
        this.greetingService = greetingService;
//        this.datasetId = datasetId;
        this.type = type;
        if (type.equalsIgnoreCase("NegRankTable")) {
            tableType = 2;
        }
        this.indexToRank = indexToRank;
        this.rankToIndex = rankToIndex;
        this.records = getRecodList(tableData, headers);
        this.table = new ListGrid();

        this.selectionManager = selectionManager;
        initGrid(headers);
        this.classtype = 5;
        this.components.add(RankTable.this);
        this.selectionManager.addSelectionChangeListener( RankTable.this);
        records = null;
        headers = null;
        tableData = null;
        selectionChanged(Selection.TYPE.OF_ROWS);
    }

    private void initGrid(String[] headers) {
        table.setShowRecordComponents(true);
        table.setShowRecordComponentsByCell(true);
        table.setCanRemoveRecords(false);
        table.setHeight("240px");
        table.setShowAllRecords(false);
        table.setCanSort(Boolean.FALSE);
        ListGridField[] fields = new ListGridField[headers.length + 1];
        ListGridField index = (ListGridField) new ListGridField("index", "index".toUpperCase());
        index.setHidden(true);
        fields[0] = index;
        for (int z = 1; z <= headers.length; z++) {
            ListGridField l = (ListGridField) new ListGridField(headers[z - 1], headers[z - 1].toUpperCase());
            if (z == 2) {
                l.setWidth("35%");
            }
            fields[z] = l;
        }
        table.sort(headers[0], SortDirection.ASCENDING);

        table.setFields(fields);
        table.setCanResizeFields(true);

        table.setData(records);
        table.setSelectionType(SelectionStyle.MULTIPLE);
        table.setLeaveScrollbarGap(false);
        table.setCanDragSelect(true);
        table.draw();
        table.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ListGridRecord[] selectionRecord = table.getSelectedRecords();
                updateTableSelection(selectionRecord);
            }
        });
        table.addDragStartHandler(new DragStartHandler() {

            @Override
            public void onDragStart(DragStartEvent event) {
                mouseSelection = true;
            }
        });
        table.addDragStopHandler(new DragStopHandler() {
            @Override
            public void onDragStop(DragStopEvent event) {
                ListGridRecord[] selectionRecord = table.getSelectedRecords();
                updateTableSelection(selectionRecord);
                mouseSelection = false;
            }
        });
    }

    private void updateTableSelection(ListGridRecord[] selectionRecord) {
        if (selectionRecord.length > 0) {

            int[] selectedIndices = new int[selectionRecord.length];
            for (int index = 0; index < selectionRecord.length; index++) {
                ListGridRecord rec = selectionRecord[index];
                selectedIndices[index] = rec.getAttributeAsInt("index");
            }
            updateSelectionManager(selectedIndices);
        }

    }

    private ListGridRecord[] getRecodList(String[][] tableData, String[] headers) {

        ListGridRecord[] recordsInit = new ListGridRecord[tableData[0].length + 1];

        for (int x = 0; x < recordsInit.length; x++) {
            ListGridRecord record = new ListGridRecord();
            record.setAttribute("index", rankToIndex[x]);
            for (int y = 0; y < headers.length; y++) {
                record.setAttribute(headers[y], tableData[y][x]);
            }
            recordsInit[x] = record;

        }
        tableData = null;
        headers = null;
        return recordsInit;
    }

    public ListGrid getTable() {
        return table;
    }

    private void updateSelectionManager(int[] selectedIndices) {
        selectionTag = true;
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        selectionManager.setSelectedRows(selection);

    }

    @Override
    public void remove() {
    }

    @Override
    public String toString() {
        return type;
    }
}
