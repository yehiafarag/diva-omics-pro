/*used
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.omicstables.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.DragCompleteEvent;
import com.smartgwt.client.widgets.events.DragCompleteHandler;
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import java.util.HashMap;
import java.util.Map;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 * @author Yehia Farag
 * omics information table that has rows ids, and activated groups colors
 */
public final class OmicsTableComponent extends ModularizedListener implements IsSerializable, FilterEditorSubmitHandler{

    private final OmicsTable omicsIdTable;
    private ListGridRecord[] records;
    private final SelectionManager selectionManager;
    private ListGrid selectionTable;
    private SelectItem colSelectionTable;
    private boolean mouseSelection = false;
    private boolean selectionTag = false;
    private final Map<String,ListGridRecord> infoSearchingMap= new HashMap<String,ListGridRecord>();

    @Override
    public String toString() {
        return "OmicsTable";
    }

    public void setSelectionTable(ListGrid selectionTable) {
        this.selectionTable = selectionTable;
        selectionTable.setShowFilterEditor(false);
        selectionChanged(Selection.TYPE.OF_ROWS);
    }

    public OmicsTableComponent(SelectionManager selectionManager, DatasetInformation datasetInfo) {
         
        this.records = getRecodList(datasetInfo);
        this.omicsIdTable = new OmicsTable(datasetInfo, 375);
        
        this.selectionManager = selectionManager;
        initGrid();
        this.classtype = 1;
        this.components.add(OmicsTableComponent.this);
        this.selectionManager.addSelectionChangeListener(OmicsTableComponent.this);
        datasetInfo = null;
        selectionChanged(Selection.TYPE.OF_ROWS);
    }

    private void initGrid() {
//       
        omicsIdTable.setData(records);
        omicsIdTable.setSelectionType(SelectionStyle.MULTIPLE);
        omicsIdTable.setLeaveScrollbarGap(false);
        omicsIdTable.setCanDragSelect(true);
        omicsIdTable.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if(mouseSelection)
                    return;
                ListGridRecord[] selectionRecord = omicsIdTable.getSelectedRecords();
                if (selectionTable != null) {
                    selectionTable.setRecords(selectionRecord);
                    selectionTable.setShowAllRecords(false);
                    selectionTable.redraw();
                }
                updateSelectionManagerOnTableSelection(selectionRecord);
            }
        });
        omicsIdTable.addDragStartHandler(new DragStartHandler() {

            @Override
            public void onDragStart(DragStartEvent event) {
                mouseSelection = true;
            }
        });
                omicsIdTable.addDragCompleteHandler(new DragCompleteHandler() {

            @Override
            public void onDragComplete(DragCompleteEvent event) {
            mouseSelection = false;   
            }
        });

        omicsIdTable.addDragStopHandler(new DragStopHandler() {
            @Override
            public void onDragStop(DragStopEvent event) {
                ListGridRecord[] selectionRecord = omicsIdTable.getSelectedRecords();
                if (selectionTable != null) {
                    selectionTable.setRecords(selectionRecord);
                    selectionTable.redraw();
                }
                
                updateSelectionManagerOnTableSelection(selectionRecord);
                mouseSelection = false;
            }
        });
        omicsIdTable.setCanSort(false);
        records = null;
        omicsIdTable.addFilterEditorSubmitHandler(OmicsTableComponent.this);

    }

    private void updateSelectionManager(int[] selectedIndices) {
        selectionTag = true;
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        selectionManager.setSelectedRows(selection);
    }

    public ListGrid getGwtTable() {
        return omicsIdTable;
    }

    private ListGridRecord[] getRecodList(DatasetInformation datasetInfo) {

        ListGridRecord[] recordsInit = new ListGridRecord[datasetInfo.getOmicsTabelData()[0].length];
        infoSearchingMap.clear();
        for (int x = 0; x < recordsInit.length; x++) {
            ListGridRecord record = new ListGridRecord();
            record.setAttribute("gene", datasetInfo.getOmicsTabelData()[0][x]);
            for (int c = 0; c < datasetInfo.getRowGroupsNames().length; c++) {
                if (!datasetInfo.getOmicsTabelData()[c + 2][x].equalsIgnoreCase("#FFFFFF")) {
                    record.setAttribute(datasetInfo.getRowGroupsNames()[c][0], datasetInfo.getOmicsTabelData()[c + 2][x]);
                }
            }
            record.setAttribute("index", x);
            infoSearchingMap.put(datasetInfo.getOmicsTabelData()[0][x].toUpperCase(), record);
            recordsInit[x] = record;
        }
        return recordsInit;
    }

    private void updateSelectionManagerOnTableSelection(ListGridRecord[] selectionRecord) {
        if (selectionRecord.length > 0) {
            int[] selectedIndices = new int[selectionRecord.length];
            for (int index = 0; index < selectionRecord.length; index++) {
                ListGridRecord rec = selectionRecord[index];
                selectedIndices[index] = rec.getAttributeAsInt("index");
            }
            selectionTag = true;
            updateSelectionManager(selectedIndices);
        }

    }

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (selectionTag) {
            selectionTag = false;
            return;
        }
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows();
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                //update table selection             
                if (selectedRows != null && selectedRows.length != 0) {
                  
                    omicsIdTable.deselectAllRecords(); 
                    try{
//                    if (true) {
                        omicsIdTable.selectRecords(selectedRows);
                        if (selectionTable != null) {
                            selectionTable.setRecords(omicsIdTable.getSelectedRecords());
                            selectionTable.redraw();
                        }
//                    }
                    omicsIdTable.scrollToRow(omicsIdTable.getRecordIndex(omicsIdTable.getSelectedRecords()[0]));
//                        omicsIdTable.fetchData();
                }catch(Exception e){Window.alert(e.getLocalizedMessage());}
                }
            }
        } else if (type == Selection.TYPE.OF_COLUMNS) {
            Selection sel = selectionManager.getSelectedColumns();
            if (sel != null) {
                int[] selectedColumn = sel.getMembers();
                //update table selection             
                if (selectedColumn != null && selectedColumn.length != 0 && colSelectionTable != null) {
                    String[] values = new String[selectedColumn.length];
                    for (int x = 0; x < selectedColumn.length; x++) {
                        values[x] = "" + selectedColumn[x];
                    }

                    colSelectionTable.setValues(values);
                    colSelectionTable.redraw();
                }
            }
        }
    }

    public void setColSelectionTable(SelectItem colSelectionTable) {
        this.colSelectionTable = colSelectionTable;
        selectionChanged(Selection.TYPE.OF_COLUMNS);
    }

    @Override
    public void remove() {
    }

    @Override
    public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
        String keyword = event.getCriteria().getValues().get("gene").toString();
        if (infoSearchingMap.containsKey(keyword.toUpperCase())) {
            Selection selection = new Selection(Selection.TYPE.OF_ROWS, new int[]{infoSearchingMap.get(keyword).getAttributeAsInt("index")});
            selectionManager.setSelectedRows(selection);
        }
    }

}
