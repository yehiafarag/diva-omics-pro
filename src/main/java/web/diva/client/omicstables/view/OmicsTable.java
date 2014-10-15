/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.omicstables.view;

import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 *
 * @author Yehia Farag
 */
public class OmicsTable extends ListGrid{
    
    
    public OmicsTable(DatasetInformation datasetInfo,int height)
    {
         setShowRecordComponents(true);
        setShowRecordComponentsByCell(true);
        setCanRemoveRecords(false);
        setHeight(height+"px");
        setWidth("100%");
        setShowAllRecords(false);
        setShowRollOver(false);
        ListGridField[] fields = new ListGridField[(3 + datasetInfo.getRowGroupsNumb())];
        ListGridField nameField = new ListGridField("gene", datasetInfo.getDatasetInfo());
        nameField.setWidth("30%");
        nameField.setType(ListGridFieldType.TEXT);
        ListGridField indexField = new ListGridField("index", " ");
        indexField.setWidth("50px");
        indexField.setType(ListGridFieldType.INTEGER);

        ListGridField allField = new ListGridField("all", "ALL");
        allField.setWidth("8px");
        allField.setIconVAlign("center");

        allField.setType(ListGridFieldType.ICON);
        allField.setIcon("../images/b.png");

        fields[0] = indexField;
        fields[1] = (nameField);
        fields[2] = allField;
        for (int z = 0; z < datasetInfo.getRowGroupsNumb(); z++) {
            ListGridField temLGF = new ListGridField(datasetInfo.getRowGroupsNames()[z][0], datasetInfo.getRowGroupsNames()[z][0].toUpperCase());
            temLGF.setWidth("8px");
            temLGF.setIconVAlign("center");
            temLGF.setType(ListGridFieldType.IMAGE);
            temLGF.setIcon(datasetInfo.getOmicsTabelData()[z + 2][0]);
            fields[z + 3] = temLGF;
        }
        setFields(fields);
        setCanResizeFields(true);
        setCanSort(false);
//        setSelectionType(SelectionStyle.NONE);
//        setLeaveScrollbarGap(false);
//        setCanDragSelect(false);
    
    }
}
