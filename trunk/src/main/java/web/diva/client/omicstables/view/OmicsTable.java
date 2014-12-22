/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.omicstables.view;

import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 *
 * @author Yehia Farag
 */
public class OmicsTable extends ListGrid {
    
    public OmicsTable(DatasetInformation datasetInfo) {
        setShowRecordComponents(true);
        setShowRecordComponentsByCell(true);
        setCanRemoveRecords(false);
        setShowHeaderContextMenu(false);
        setShowAllRecords(false);
        setCanSort(Boolean.TRUE);        
        setSelectionType(SelectionStyle.MULTIPLE);
        setCanResizeFields(true);
        setShowEdges(false);

//        setLeaveScrollbarGap(false);
        setCanDragSelect(true);
        
        this.setStyleName("borderless");
//        this.setBorder("0px sold black");
        
        ListGridField indexField = new ListGridField("index", " ");
        indexField.setWidth("50px");
        indexField.setType(ListGridFieldType.INTEGER);
        indexField.setCanFilter(false);
        
        ListGridField nameField = new ListGridField("gene", datasetInfo.getDatasetInfo());
//        nameField.setAutoFitWidth(true);
        nameField.setCanFilter(true);
        nameField.setType(ListGridFieldType.TEXT);
        
        ListGridField allField = new ListGridField("all", "All Indexes");
        allField.setWidth("23px");
        allField.setIconVAlign("center");
        allField.setCanFilter(false);
        allField.setType(ListGridFieldType.ICON);
        allField.setIcon("../images/b.png");
        allField.setShowTitle(false);
        allField.setShowDownIcon(true);
        
        ListGridField[] fields = new ListGridField[(3 + datasetInfo.getRowGroupsNumb())];
        fields[0] = indexField;
        fields[1] = (nameField);
        fields[2] = allField;
        for (int z = 0; z < datasetInfo.getRowGroupsNumb(); z++) {
            ListGridField rowGroupListGridField = new ListGridField(datasetInfo.getRowGroupList().get(z).getName(), datasetInfo.getRowGroupList().get(z).getName());            
            rowGroupListGridField.setShowValueIconOnly(false);
            rowGroupListGridField.setCanFilter(false);
            rowGroupListGridField.setShowTitle(false);
            rowGroupListGridField.setIconVAlign("center");
            rowGroupListGridField.setType(ListGridFieldType.IMAGE);
            rowGroupListGridField.setWidth("23px");
            rowGroupListGridField.setIcon(datasetInfo.getRowGroupList().get(z).getColor());
            fields[z + 3] = rowGroupListGridField;
        }
        setFields(fields);
        setCanResizeFields(true);
        setFilterButtonPrompt("searching...");
        
        this.setShowFilterEditor(true);
        Button filterBtn = new Button();
        filterBtn.setIcon("../images/searchBtn.png");        
        filterBtn.setTitle("search");
        this.setFilterButtonProperties(filterBtn);
        draw();
        
    }
}
