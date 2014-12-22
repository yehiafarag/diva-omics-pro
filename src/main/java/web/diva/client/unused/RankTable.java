/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.unused;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
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
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;

/**
 *
 * @author Yehia Farag
 * rank table components
 */
public final class RankTable  extends ListGrid implements SelectionChangedHandler,IsSerializable{

    private boolean mouseSelection = false;
    private final String type;
    private ListGridRecord[] recordMap;
    public void updateTable(int[] selection) {
        if (selectionTag) {
            selectionTag = false;            
        } else {
            this.deselectAllRecords();
            ListGridRecord[] reIndexSelection = new ListGridRecord[selection.length];
            int i = 0;
            for (int z : selection) {
                reIndexSelection[i++] = recordMap[z];
            }
            this.selectRecords(reIndexSelection);
            try {
                int scrollTo = indexToRank[selection[0]];
                this.scrollToRow(scrollTo);
                this.redraw();
            } catch (Exception e) {
                Window.alert(e.getMessage());
            }
        }

    }

//    @Override
//    public void selectionChanged(Selection.TYPE type) {
//        if (selectionTag) {
//            selectionTag = false;
//            return;
//        }
//        if (type == Selection.TYPE.OF_ROWS) {
//            Selection sel = selectionManager.getSelectedRows();
//            if (sel != null && sel.getMembers().length > 0) {
//                this.indexToRank(sel.getMembers());
//
//            }
//        }
//    }
//    /**
//     * This method is responsible for re-indexing omics data indexes to rank indexes (pre-update visualization method)
//     * @param index - selected omics data indexes
//     */
//    private int[] indexToRank(int[] index) {
//        greetingService.indexToRank(index, tableType, new AsyncCallback<int[]>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                RootPanel.get("loaderImage").setVisible(false);
//            }
//
//            @Override
//            public void onSuccess(int[] result) {
//                if (result.length != 0) {
//                    deselectAllRecords();
//                    selectRecords(result);
//                    scrollToRow(result[0]);
//                }
//
//            }
//        });
//        return null;
//    }

    @Override
    public void onSelectionChanged(SelectionEvent event) {
        if (mouseSelection) {
            return;
        }
        ListGridRecord[] selectionRecord = getSelectedRecords();
        updateTableSelection(selectionRecord);

    }

//    private final ListGrid table;
    private ListGridRecord[] records;
    private final SelectionManager selectionManager;
    private final int[] rankToIndex, indexToRank;
    private boolean selectionTag = false;
//    private int tableType = 1;
//    private final GreetingServiceAsync greetingService;

    public RankTable(SelectionManager selectionManager, int datasetId, String[] headers, String[][] tableData, int[] rankToIndex, int[] indexToRank, String type) {
//        this.greetingService = greetingService;
//        this.datasetId = datasetId;
        this.type = type;
//        if (type.equalsIgnoreCase("NegRankTable")) {
//            tableType = 2;
//        }
        this.indexToRank = indexToRank;
        this.rankToIndex = rankToIndex;
        this.records = getRecodList(tableData, headers);      
        this.selectionManager = selectionManager;
        initGrid(headers);       
        records = null;
        headers = null;
        tableData = null;
        
    }

//    private int[] recordToIndex;
    private void initGrid(String[] headers) {
        setShowRecordComponents(true);
        setShowRecordComponentsByCell(true);
        setCanRemoveRecords(false);
        setHeight("180px");
        setShowAllRecords(false);
        setCanSort(Boolean.FALSE);
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
        sort(headers[0], SortDirection.ASCENDING);

        setFields(fields);
        setCanResizeFields(true);

        setData(records);
        setSelectionType(SelectionStyle.MULTIPLE);
        setLeaveScrollbarGap(false);
        setCanDragSelect(true);
        draw();
        addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ListGridRecord[] selectionRecord = getSelectedRecords();
                updateTableSelection(selectionRecord);
            }
        });
        addDragStartHandler(new DragStartHandler() {

            @Override
            public void onDragStart(DragStartEvent event) {
                mouseSelection = true;
            }
        });
        addDragStopHandler(new DragStopHandler() {
            @Override
            public void onDragStop(DragStopEvent event) {
                ListGridRecord[] selectionRecord = getSelectedRecords();
               
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
       recordMap = new ListGridRecord[recordsInit.length];
       
//        recordToIndex = new int[recordMap.length];
        for (int x = 0; x < recordsInit.length; x++) {
            ListGridRecord record = new ListGridRecord();
            int reIndexer = rankToIndex[x];
            record.setAttribute("index", reIndexer);
            for (int y = 0; y < headers.length; y++) {
                record.setAttribute(headers[y], tableData[y][x]);  
//                if(y==0){
//                    int ra = Integer.valueOf(tableData[y][x]);
//                    recordToIndex[ra-1]= ra;
//                }
            }
           
            recordsInit[x] = record;
            recordMap[reIndexer] = record;

        }
        tableData = null;
        headers = null;
        return recordsInit;
    }
    

    private void updateSelectionManager(int[] selectedIndices) {
        selectionTag = true;
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        selectionManager.setSelectedRows(selection);

    }

//    @Override
    public void remove() {
    }

    @Override
    public String toString() {
        return type;
    }
}
