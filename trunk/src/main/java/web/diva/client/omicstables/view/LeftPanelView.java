/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.omicstables.view;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import java.util.LinkedHashMap;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 *
 * @author Yehia Farag
 * this component represents the left panel on DIVA
 * it contains omics data information table
 * rows selection tables
 * columns selection tables
 */
public class LeftPanelView extends SectionStack {

    private final ListGrid selectionTable;
    private final SelectItem colSelectionTable;

    public LeftPanelView(SelectionManager selectionManager, DatasetInformation datasetInfo,int width) {
        this.setVisibilityMode(VisibilityMode.MUTEX);

        this.setWidth("20%");
        this.setHeight("80%");
        this.setScrollSectionIntoView(true);

        SectionStackSection section1 = new SectionStackSection(datasetInfo.getDatasetInfo());
        section1.setExpanded(true);

        OmicsTableComponent omicsTable = new OmicsTableComponent(selectionManager, datasetInfo);
        section1.addItem(omicsTable.getGwtTable());

        SectionStackSection section2 = new SectionStackSection("Row Selections");
        section2.setExpanded(false);
        selectionTable = initRowSelectionTable(datasetInfo);
        section2.addItem(selectionTable);
        omicsTable.setSelectionTable(selectionTable);

        SectionStackSection section3 = new SectionStackSection("Column Selections");
        section3.setExpanded(false);
        colSelectionTable = initColSelectionTable(datasetInfo.getColNamesMap(),width);
        colSelectionTable.setHeight("375px");
        DynamicForm form = new DynamicForm();
        form.setItems(colSelectionTable);
        form.setWidth(width);
        form.setHeight("375px");
        section3.addItem(form);
        omicsTable.setColSelectionTable(colSelectionTable);

        this.addSection(section3);
        this.addSection(section2);
        this.addSection(section1);
        this.redraw();
        datasetInfo = null;

    }

     /**
     * This method is responsible for initializing row selection table
     * @param datasetInfo   - dataset information
     */
    private ListGrid initRowSelectionTable(DatasetInformation datasetInfo) {
        OmicsTable omicsTable = new OmicsTable(datasetInfo,300);
        omicsTable.setSelectionType(SelectionStyle.NONE);
        omicsTable.setLeaveScrollbarGap(false);
        omicsTable.setCanDragSelect(false);
        return omicsTable;
    }

     /**
     * This method is responsible for initializing columns selection table 
     * @param colNamesMap   - selected columns name 
     */
    private SelectItem initColSelectionTable(LinkedHashMap<String, String> colNamesMap,int width) {
        SelectItem selectCols = new SelectItem();
        selectCols.setTitle(" Selected Columns ");
        selectCols.setWidth(width);
        selectCols.setTextAlign(Alignment.CENTER);
        selectCols.setTitleAlign(Alignment.CENTER);
        selectCols.setTitleOrientation(TitleOrientation.TOP);
        selectCols.setMultiple(true);
        selectCols.setMultipleAppearance(MultipleAppearance.GRID);
        selectCols.setValueMap(colNamesMap);
        selectCols.disable();

        return selectCols;
    }

    public ListGrid getSelectionTable() {
        return selectionTable;
    }

    public SelectItem getColSelectionTable() {
        return colSelectionTable;
    }

}
