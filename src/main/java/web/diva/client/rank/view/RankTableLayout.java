/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.rank.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.types.ListGridFieldType;
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
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.RankResult;

/**
 *
 * @author Yehia Farag
 */
public class RankTableLayout extends ListGrid implements  IsSerializable {

    private ListGridRecord[] posRecordMap, negRecordMap, selectedRows;
 

    public void remove() {
        clickReg.removeHandler();
        dragStartReg.removeHandler();
        dragStopReg.removeHandler();

    }
    private boolean showSelectedOnly;

    public void updateTable(int[] selection) {
        if (selectionTag) {
            selectionTag = false;
        } else {
            this.deselectAllRecords();
            ListGridRecord[] reIndexSelection = new ListGridRecord[selection.length];
            int i = 0;

            for (int z : selection) {
                reIndexSelection[i++] = posRecordMap[z];
            }
            selectedRows = reIndexSelection;
            if (showSelectedOnly) {
                this.setRecords(reIndexSelection);
                this.selectAllRecords();
                this.scrollToTop();
            } else {
                this.setRecords(records);
                this.selectRecords(reIndexSelection);
                this.scrollToRow(this.getRecordIndex(reIndexSelection[0]));
            }

        }

    }

    public void showSelectedOnly(boolean showSelectedOnly) {
        this.showSelectedOnly = showSelectedOnly;       
        if (showSelectedOnly) {    
            selectedRows = this.getSelectedRecords();      
            this.setRecords(selectedRows);            
            this.selectAllRecords();    
            this.scrollToTop();
            
        } else {
            this.setRecords(records);
            if(selectedRows != null){
            this.selectRecords(selectedRows);
            this.scrollToRow(this.getRecordIndex(selectedRows[0]));
            }
            
        }
        
    }
    

  

    private ListGridRecord[] records;
    private final SelectionManager selectionManager;
    private int[] posRankToIndex, negRankToIndex;
    private boolean selectionTag = false;
    
    private  HandlerRegistration clickReg,dragStartReg,dragStopReg;
    

    public RankTableLayout(SelectionManager selectionManager, int datasetId, RankResult results) {
       
        this.selectionManager = selectionManager;
        initGrid(results.getHeaders());
        this.updateRecords(results);
        results = null;

    }

    private void initGrid(String[] headers) {
        setShowRecordComponents(true);
        setShowRecordComponentsByCell(true);
        setCanRemoveRecords(false);
        setShowHeaderContextMenu(false);
        setHeight100();
        setShowEdges(false);
        setStyleName("borderless");
        setShowAllRecords(false);
        setCanSort(Boolean.TRUE);

        ListGridField[] fields = new ListGridField[headers.length + 1];
        ListGridField index = (ListGridField) new ListGridField("index", "index".toUpperCase());
        index.setType(ListGridFieldType.INTEGER);
        index.setHidden(true);
        fields[0] = index;
        for (int z = 1; z <= headers.length; z++) {
            ListGridField l = (ListGridField) new ListGridField(headers[z - 1], headers[z - 1].toUpperCase());
            if (z == 1 || z == 7) {
                l.setWidth("72px");
                l.setTitle("" + headers[z - 1].toUpperCase() + "");
                l.setType(ListGridFieldType.INTEGER);
                l.setCanSort(true);
            } else if (z == 2) {
                l.setAutoFitWidth(true);
                l.setType(ListGridFieldType.TEXT);

            } else {
                l.setType(ListGridFieldType.FLOAT);
                l.setFormat("0.0000");
            }

            fields[z] = l;
        }
        sort(headers[4], SortDirection.ASCENDING);
        setFields(fields);
        setCanResizeFields(true);
        setSelectionType(SelectionStyle.MULTIPLE);
        setLeaveScrollbarGap(false);
        setCanDragSelect(true);
        draw();
        clickReg = addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ListGridRecord[] selectionRecord = getSelectedRecords();
                updateTableSelection(selectionRecord);
            }
        });
        dragStartReg = addDragStartHandler(new DragStartHandler() {

            @Override
            public void onDragStart(DragStartEvent event) {
            }
        });
       dragStopReg=  addDragStopHandler(new DragStopHandler() {
            @Override
            public void onDragStop(DragStopEvent event) {
                ListGridRecord[] selectionRecord = getSelectedRecords();
                updateTableSelection(selectionRecord);
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

    private ListGridRecord[] getRecodList(double[][] tableData, String[] headers, String[] rowIds, int[] posRank, int[] negRank, int[] posRankToNegRank) {

        ListGridRecord[] recordsInit = new ListGridRecord[rowIds.length];
        posRecordMap = new ListGridRecord[recordsInit.length];
        negRecordMap = new ListGridRecord[recordsInit.length];
        for (int x = 0; x < recordsInit.length; x++) {
            ListGridRecord record = new ListGridRecord();
            int posReIndexer = posRankToIndex[x];
            int negReIndexer = negRankToIndex[posRankToNegRank[x] - 1];
            record.setAttribute("index", (Integer) posReIndexer);
            int coulmnReindex = 0;
            for (int y = 0; y < headers.length; y++) {

                try {
                    if (y == 0) {
                        record.setAttribute(headers[y], (Integer) posRank[x]);
                    } else if (y == 1) {
                        record.setAttribute(headers[y], rowIds[x]);
                    } else if (y == 6) {
                        record.setAttribute(headers[y], (Integer) negRank[x]);
                    } else {
                        record.setAttribute(headers[y], tableData[coulmnReindex][x]);
                        coulmnReindex++;

                    }
                } catch (NumberFormatException nexp) {
                    Window.alert(nexp.getMessage());
                }
            }

            recordsInit[x] = record;
            posRecordMap[posReIndexer] = record;
            negRecordMap[negReIndexer] = record;
        }
        tableData = null;
        headers = null;
        return recordsInit;
    }

    private void updateSelectionManager(int[] selectedIndices) {
        selectionTag = true;
        SelectionManager.Busy_Task(true, false);
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        selectionManager.setSelectedRows(selection);

    }

    @Override
    public String toString() {
        return "rank table";
    }

    public final void updateRecords(RankResult results) {
        this.posRankToIndex = results.getPosRankToIndex();
        this.negRankToIndex = results.getNegRankToIndex();
        this.records = getRecodList(results.getTableData(), results.getHeaders(), results.getRowIds(), results.getPosRank(), results.getNegRank(), results.getPosRankToNegRank());
        setData(records);
    }
}
