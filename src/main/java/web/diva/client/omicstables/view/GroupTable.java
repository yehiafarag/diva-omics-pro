/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.omicstables.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.DivaGroup;

/**
 *
 * @author Yehia Farag
 */
public class GroupTable extends ListGrid {

    private SelectionManager selectionManager;
    private boolean dragStart;
    private final String tableType;

    public GroupTable(SelectionManager selectionManagerInst, String tableType) {
        this.selectionManager = selectionManagerInst;
        this.tableType=tableType;
        this.setHeight("29%");
        setWidth("100%");
        this.setLeaveScrollbarGap(true);
        this.setSelectionType(SelectionStyle.MULTIPLE);
        this.setStyleName("borderless");
        setShowRecordComponents(true);
        setShowRecordComponentsByCell(true);
        setCanRemoveRecords(false);
        setShowHeaderContextMenu(false);
        setShowEdges(false);
        setShowAllRecords(true);
        setShowRollOver(false);
        setCanSort(true);
        
        setCanDragSelect(true);

        ListGridField groupNameField = new ListGridField("groupName", "Group Name", 200);
        groupNameField.setAlign(Alignment.CENTER);
        groupNameField.setType(ListGridFieldType.TEXT);
        groupNameField.setAutoFitWidth(Boolean.TRUE);
        groupNameField.setWidth("200px");

        ListGridField colorField = new ListGridField("color", "");
        colorField.setAlign(Alignment.CENTER);
        colorField.setType(ListGridFieldType.IMAGE);
        colorField.setCanEdit(false);
        colorField.setWidth("23px");
        colorField.setIconVAlign("center");

        ListGridField countField = new ListGridField("count", "Count");
        countField.setAlign(Alignment.CENTER);
        countField.setType(ListGridFieldType.INTEGER);
        this.setFields(new ListGridField[]{groupNameField, countField, colorField});
        this.selectSingleRecord(0);

      
        dargStartSelectionReg = this.addDragStartHandler(new DragStartHandler() {

            @Override
            public void onDragStart(DragStartEvent event) {
                dragStart = true;
            }
        });

        DragStopHandler selectionUdateHandler = new DragStopHandler() {

            @Override
            public void onDragStop(DragStopEvent event) {
                dragStart= false;
                ListGridRecord[] selectionRecord = getSelectedRecords();
                if (selectionRecord != null && selectionRecord.length > 0) {
                    SelectionManager.Busy_Task(true, false);
                    updateSelectionManagerOnTableSelection(selectionRecord);
                }

            }
        };
        handlerReg = this.addDragStopHandler(selectionUdateHandler);

        clickHandlerReg = this.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (dragStart) {
                    return;
                }
                ListGridRecord[] selectionRecord = getSelectedRecords();
                if (selectionRecord != null && selectionRecord.length > 0) {
                    SelectionManager.Busy_Task(true, false);
                    
                    updateSelectionManagerOnTableSelection(selectionRecord);
                }
            }
        });
    }
    private final HandlerRegistration handlerReg, clickHandlerReg, dargStartSelectionReg;
    private List<DivaGroup> groupsList;

    public void updateRecords(List<DivaGroup> groupsList) {
        this.groupsList = groupsList;
        ListGridRecord[] records = new ListGridRecord[groupsList.size()];
        int index = 0;
        for (DivaGroup rGr : groupsList) {
            ListGridRecord record = new ListGridRecord();
            record.setAttribute("groupName", rGr.getName());
            record.setAttribute("color", rGr.getColor());
            record.setAttribute("count", rGr.getCount());
            records[index++] = record;

        }

        this.setData(records);
    }

    private void updateSelectionManagerOnTableSelection(ListGridRecord[] selectionRecord) {
        
        if (selectionRecord.length == 0) {
            return;
        }
        int[] selectedIndices = null;
        if (selectionRecord.length == 1) {
            for (ListGridRecord record : selectionRecord) {
                String groupName = record.getAttribute("groupName");

                for (DivaGroup group : groupsList) {
                    if (group.getName().equalsIgnoreCase(groupName)) {
                        selectedIndices = group.getMembers();
                        break;
                    }

                }
                break;

            }

        } else if (selectionRecord.length > 0) {
            Set<Integer> members = new HashSet<Integer>();

            for (ListGridRecord record : selectionRecord) {
                String groupName = record.getAttribute("groupName");

                for (DivaGroup group : groupsList) {
                    if (group.getName().equalsIgnoreCase(groupName)) {
                        for (int x : group.getMembers()) {
                            members.add(x);
                        }
                        break;
                    }

                }

            }

            selectedIndices = new int[members.size()];
            int x = 0;
            for (int z : members) {
                selectedIndices[x] = z;
                x++;
            }

        }
        if (selectedIndices != null) {
            updateSelectionManager(selectedIndices);
        }

    }
    private boolean groubTableSelection = false;

    private void updateSelectionManager(int[] selectedIndices) {
        groubTableSelection = true;
        if (tableType.equalsIgnoreCase("col")) {
            Selection selection = new Selection(Selection.TYPE.OF_COLUMNS, selectedIndices);
            selectionManager.setSelectedColumns(selection);
            this.deselectAllRecords();
        } else {
            Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
            selectionManager.setSelectedRows(selection);
        }
    }

    public boolean isGroubTableSelection() {
        return groubTableSelection;
    }

    public void setGroubTableSelection(boolean groubTableSelection) {
        this.groubTableSelection = groubTableSelection;
    }

    public void remove() {
        clickHandlerReg.removeHandler();
        handlerReg.removeHandler();
        dargStartSelectionReg.removeHandler();
        selectionManager = null;

    }

}
