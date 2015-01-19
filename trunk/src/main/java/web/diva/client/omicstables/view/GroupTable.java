/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.omicstables.view;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.DragCompleteEvent;
import com.smartgwt.client.widgets.events.DragCompleteHandler;
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

    private boolean mouseSelection = false;
     private final SelectionManager selectionManager;
    public GroupTable(SelectionManager selectionManager) {
        this.selectionManager = selectionManager;
        this.setHeight("30%");
        setWidth("100%");
        this.setLeaveScrollbarGap(true);
        this.setSelectionType(SelectionStyle.MULTIPLE);
        this.setStyleName("borderless");
        setShowRecordComponents(true);
        setShowRecordComponentsByCell(true);
        setCanRemoveRecords(false);
        setShowHeaderContextMenu(false);
        setShowEdges(false);
        setShowAllRecords(false);
        setShowRollOver(false);
        setCanSort(true);
        setCanDragSelect(true);

        ListGridField groupNameField = new ListGridField("groupName", "Group Name", 50);
        groupNameField.setAlign(Alignment.CENTER);
        groupNameField.setType(ListGridFieldType.TEXT);
        groupNameField.setAutoFitWidth(Boolean.TRUE);
        groupNameField.setWidth("63px");

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
          this.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (mouseSelection) {
                    return;
                }
                ListGridRecord[] selectionRecord = getSelectedRecords();                
                updateSelectionManagerOnTableSelection(selectionRecord);
            }
        });
        this.addDragStartHandler(new DragStartHandler() {

            @Override
            public void onDragStart(DragStartEvent event) {
                mouseSelection = true;
            }
        });
        this.addDragCompleteHandler(new DragCompleteHandler() {

            @Override
            public void onDragComplete(DragCompleteEvent event) {
                mouseSelection = false;
            }
        });

        this.addDragStopHandler(new DragStopHandler() {
            @Override
            public void onDragStop(DragStopEvent event) {
                ListGridRecord[] selectionRecord = getSelectedRecords();               
                updateSelectionManagerOnTableSelection(selectionRecord);
                mouseSelection = false;
            }
        });
        
        
    }
    private List<DivaGroup> rowGroupsList;
     public  void updateRecords(List<DivaGroup> rowGroupsList) {
        this.rowGroupsList = rowGroupsList;
        ListGridRecord[] records = new ListGridRecord[rowGroupsList.size()];
        int index = 0;
        for (DivaGroup rGr : rowGroupsList) {
            ListGridRecord record = new ListGridRecord();
            record.setAttribute("groupName", rGr.getName());
            record.setAttribute("color", rGr.getColor());
            record.setAttribute("count", rGr.getCount());
            records[index++] = record;

        }

        this.setData(records);
    }
     
      private void updateSelectionManagerOnTableSelection(ListGridRecord[] selectionRecord) {
       selectionManager.busyTask(true,false);
       if (selectionRecord.length > 0) {
            Set<Integer> members = new HashSet<Integer>();
            
            for(ListGridRecord record:selectionRecord){
                String groupName = record.getAttribute("groupName");
                
                for(DivaGroup group :rowGroupsList){
                    if(group.getName().equalsIgnoreCase(groupName))
                    {
                        for (int x : group.getMembers()) {
                            members.add(x);
                        }
                        break;
                    }
                
                }
                
            
            }
            
            
            
            int[] selectedIndices = new int[members.size()];
            int x=0;
            for(int z: members){
                selectedIndices[x]= z;
                x++;
            }                
            updateSelectionManager(selectedIndices);
        }

    }
 private void updateSelectionManager(int[] selectedIndices) {
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        selectionManager.setSelectedRows(selection);
        selectionManager.busyTask(false,false);
    }
     
}
