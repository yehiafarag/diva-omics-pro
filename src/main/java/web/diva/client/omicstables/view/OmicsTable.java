/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.omicstables.view;

import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.Button;
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
        setHeight(100 + "%");
        setWidth("100%");
        setShowAllRecords(false);
        setShowRollOver(false);
        ListGridField[] fields = new ListGridField[(3 + datasetInfo.getRowGroupsNumb())];
        ListGridField nameField = new ListGridField("gene", datasetInfo.getDatasetInfo());
        nameField.setWidth("30%");
        nameField.setCanFilter(true);
        nameField.setType(ListGridFieldType.TEXT);
        ListGridField indexField = new ListGridField("index", " ");
        indexField.setWidth("50px");
        indexField.setType(ListGridFieldType.INTEGER);

        indexField.setCanFilter(false);
        ListGridField allField = new ListGridField("all", "ALL");
        allField.setWidth("8px");
        allField.setIconVAlign("center");
        allField.setCanFilter(false);
        allField.setType(ListGridFieldType.ICON);
        allField.setIcon("../images/b.png");

        fields[0] = indexField;
        fields[1] = (nameField);
        fields[2] = allField;
        for (int z = 0; z < datasetInfo.getRowGroupsNumb(); z++) {

            ListGridField temLGF = new ListGridField(datasetInfo.getRowGroupsNames()[z][0], datasetInfo.getRowGroupsNames()[z][0].toUpperCase());
            temLGF.setWidth("8px");
            temLGF.setShowValueIconOnly(true);
            temLGF.setCanFilter(false);
            temLGF.setIconVAlign("center");
            temLGF.setType(ListGridFieldType.IMAGE);
            temLGF.setIcon(datasetInfo.getOmicsTabelData()[z + 2][0]);
            fields[z + 3] = temLGF;
        }
        setFields(fields);

        setCanResizeFields(true);
        setCanSort(false);
//        setAutoFetchData(false);
        setFilterButtonPrompt("searching...");
//
        this.setShowFilterEditor(true);
//        this.setFilterOnKeypress(false);
        Button filterBtn = new Button();
        filterBtn.setIcon("http://www.culturainglesasp.com.br/wps/themes/html/Portal_Internet/icons/SearchButton.gif");
//        filterBtn.setWidth(15);
        filterBtn.setTitle("search");
        this.setFilterButtonProperties(filterBtn);

//        draw();
//        this.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
//
//            @Override
//            public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
//                Window.alert("filter is submitted " + event.getCriteria().getValues());
//            }
//        });
//        setSelectionType(SelectionStyle.MULTIPLE);
//        setLeaveScrollbarGap(false);
//        setCanDragSelect(true);

    }
}
