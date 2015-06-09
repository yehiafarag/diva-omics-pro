
package web.diva.client.omicstables.view;

import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 * Omics information table
 * @author Yehia Farag
 */
public class OmicsTable extends ListGrid {

    /**
     * @param datasetInfo dataset information contains all the omics information
     * data
     */
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
        setCanDragSelect(true);
        this.setStyleName("borderless");

        ListGridField indexField = new ListGridField("index", " ");
        indexField.setWidth("50px");
        indexField.setType(ListGridFieldType.INTEGER);
        indexField.setCanFilter(false);
        ListGridField[] fields = new ListGridField[(2 + datasetInfo.getAnnotationHeaders().length + datasetInfo.getRowGroupsNumb())];
        fields[0] = indexField;
        int x = 1;
        for (String annotationHeader : datasetInfo.getAnnotationHeaders()) {
            ListGridField nameField = new ListGridField(annotationHeader, annotationHeader);
            nameField.setCanFilter(false);
            nameField.setType(ListGridFieldType.TEXT);
            fields[x++] = (nameField);
        }

        ListGridField allField = new ListGridField("all", "All Indexes");
        allField.setWidth("23px");
        allField.setIconVAlign("center");
        allField.setCanFilter(false);
        allField.setType(ListGridFieldType.ICON);
        allField.setIcon("../images/b.png");
        allField.setShowTitle(false);
        allField.setShowDownIcon(true);
        fields[x++] = allField;

        for (int z = 0; z < datasetInfo.getRowGroupsNumb(); z++) {
            ListGridField rowGroupListGridField = new ListGridField(datasetInfo.getRowGroupList().get(z).getName(), datasetInfo.getRowGroupList().get(z).getName());
            rowGroupListGridField.setShowValueIconOnly(false);
            rowGroupListGridField.setCanFilter(false);
            rowGroupListGridField.setShowTitle(false);
            rowGroupListGridField.setIconVAlign("center");
            rowGroupListGridField.setType(ListGridFieldType.IMAGE);
            rowGroupListGridField.setWidth("23px");
            rowGroupListGridField.setIcon(datasetInfo.getRowGroupList().get(z).getColor());
            fields[x++] = rowGroupListGridField;
        }
        setFields(fields);
        setCanResizeFields(true);
        draw();

    }

}
