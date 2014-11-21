/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.rank.view;

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
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.RankResult;

/**
 *
 * @author Yehia Farag
 */
public class UpdatedRankTable extends ListGrid implements SelectionChangedHandler, IsSerializable {

    private boolean mouseSelection = false;
    private ListGridRecord[] posRecordMap, negRecordMap;

    public void updateTable(int[] selection) {
        if (selectionTag) {
            selectionTag = false;
        } else {
            this.deselectAllRecords();
            ListGridRecord[] reIndexSelection = new ListGridRecord[selection.length];
            int i = 0;

            if (this.isSortField(headers[0]) || this.getSortField() == null) {

                if (this.getSortDirection().getValue().equalsIgnoreCase("descending")) {

                    
                    for (int z : selection) {
                        reIndexSelection[i++] = posRecordMap[z];
                    }
                    this.selectRecords(reIndexSelection);
                    try {
                        int scrollTo = posIndexToRank.length-posIndexToRank[selection[0]];
                        Window.alert("descending   "+scrollTo+"    " +posIndexToRank[selection[0]]);
                        this.scrollToRow(scrollTo);
                        this.redraw();
                    } catch (Exception e) {
                        Window.alert(e.getMessage());
                    }
                } else {
                    for (int z : selection) {
                        reIndexSelection[i++] = posRecordMap[z];
                    }
                    this.selectRecords(reIndexSelection);
                    try {
                        int scrollTo = posIndexToRank[selection[0]];
                        this.scrollToRow(scrollTo);
                        Window.alert("scrollTo   " + scrollTo+"               "+this.getSortDirection().getValue());
                        this.redraw();
                    } catch (Exception e) {
                        Window.alert(e.getMessage());
                    }
                }

            } else {

                if (this.getSortDirection().getValue().equalsIgnoreCase("descending")) {

                    for (int z : selection) {
                        reIndexSelection[i++] = negRecordMap[z];
                    }
                    this.selectRecords(reIndexSelection);
                    try {
                        int scrollTo = negIndexToRank.length-negIndexToRank[selection[0]];
                        this.scrollToRow(scrollTo);
                        this.redraw();
                    } catch (Exception e) {
                        Window.alert(e.getMessage());
                    }
                } else {
                    for (int z : selection) {
                        reIndexSelection[i++] = negRecordMap[z];
                    }
                    this.selectRecords(reIndexSelection);
                    try {
                        int scrollTo = negIndexToRank[selection[0]];
                        this.scrollToRow(scrollTo);
                        this.redraw();
                    } catch (Exception e) {
                        Window.alert(e.getMessage());
                    }

                }
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
//                this.posIndexToRank(sel.getMembers());
//
//            }
//        }
//    }
//    /**
//     * This method is responsible for re-indexing omics data indexes to rank indexes (pre-update visualization method)
//     * @param index - selected omics data indexes
//     */
//    private int[] posIndexToRank(int[] index) {
//        greetingService.posIndexToRank(index, tableType, new AsyncCallback<int[]>() {
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
    private final int[] posRankToIndex, posIndexToRank,negRankToIndex,negIndexToRank;
    private boolean selectionTag = false;
    private final String[] headers;
//    private int tableType = 1;
//    private final GreetingServiceAsync greetingService;

    public UpdatedRankTable(SelectionManager selectionManager, int datasetId, RankResult results) {
//        this.greetingService = greetingService;
//        this.datasetId = datasetId;

//        if (type.equalsIgnoreCase("NegRankTable")) {
//            tableType = 2;
//        }
        this.selectionManager = selectionManager;
        this.posIndexToRank = results.getPosIndexToRank();
        this.posRankToIndex = results.getPosRankToIndex();
        this.negRankToIndex = results.getNegRankToIndex();
        this.negIndexToRank= results.getNegIndexToRank();
        this.headers=results.getHeaders();
        
       this.records = getRecodList(results.getTableData(), results.getHeaders(),results.getRowIds(),results.getPosRank(),results.getNegRank(),results.getPosRankToNegRank());      
        
        initGrid(results.getHeaders());       
        records = null;
        results=null;
       
        
    }

//    private int[] recordToIndex;
    private void initGrid(String[] headers) {
        setShowRecordComponents(true);
        setShowRecordComponentsByCell(true);
        setCanRemoveRecords(false);
        setShowHeaderContextMenu(false);
        setHeight("200px");
        setShowAllRecords(false);
//        setCanSort(Boolean.FALSE);
      
        ListGridField[] fields = new ListGridField[headers.length + 1];
        ListGridField index = (ListGridField) new ListGridField("index", "index".toUpperCase());
        index.setType(ListGridFieldType.INTEGER);
        index.setHidden(true);
        index.setCanSort(false);
        fields[0] = index;
        for (int z = 1; z <= headers.length; z++) {
            ListGridField l = (ListGridField) new ListGridField(headers[z - 1],headers[z - 1].toUpperCase());
            if (z == 1 || z==7) {
                l.setWidth("72px");
                l.setTitle("<strong>"+ headers[z - 1].toUpperCase()+"</strong>");
                l.setType(ListGridFieldType.INTEGER);                
                l.setCanSort(true);
            }
            else if (z==2 ) {
                l.setAutoFitWidth(true);
                l.setCanSort(false);
                l.setType(ListGridFieldType.TEXT);
                
            }
            else{
            l.setCanSort(false);
            l.setType(ListGridFieldType.FLOAT);
            }
                
            fields[z] = l;
        }
        sort(headers[4], SortDirection.ASCENDING);

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

    private ListGridRecord[] getRecodList(double[][] tableData, String[] headers,String[]rowIds,int[] posRank,int[] negRank,int[] posRankToNegRank) {

        ListGridRecord[] recordsInit = new ListGridRecord[rowIds.length ];
       posRecordMap = new ListGridRecord[recordsInit.length];
       negRecordMap = new ListGridRecord[recordsInit.length];
       
//        recordToIndex = new int[posRecordMap.length];
        for (int x = 0; x < recordsInit.length; x++) {
            ListGridRecord record = new ListGridRecord();
            int posReIndexer = posRankToIndex[x];
            int negReIndexer = negRankToIndex[posRankToNegRank[x]-1];
            record.setAttribute("index",(Integer) posReIndexer);
           int coulmnReindex =0;
            for (int y = 0; y < headers.length; y++) { 
                
                try{
                if(y==0)
                    record.setAttribute(headers[y],(Integer) posRank[x]);
                else if(y==1)
                    record.setAttribute(headers[y],rowIds[x]);  
                else if(y == 6)
                    record.setAttribute(headers[y],(Integer) negRank[x]); 
                else{
                    record.setAttribute(headers[y],tableData[coulmnReindex][x]);
                    coulmnReindex++;
                
                }
//                if(y==0){
//                    int ra = Integer.valueOf(tableData[y][x]);
//                    recordToIndex[ra-1]= ra;
//                }
                }catch(NumberFormatException  nexp){Window.alert(nexp.getMessage());}
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
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        selectionManager.setSelectedRows(selection);

    }

//    @Override
    public void remove() {
    }

    @Override
    public String toString() {
        return "rank table";
    }


}
