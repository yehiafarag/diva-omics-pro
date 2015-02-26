/*used
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.omicstables.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.FocusEvent;
import com.smartgwt.client.widgets.form.fields.events.FocusHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.HashSet;
import java.util.Set;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 * @author Yehia Farag omics information table that has rows ids, and activated
 * groups colors
 */
public final class OmicsTableComponent extends ModularizedListener implements IsSerializable {

    private final OmicsTable omicsIdTable;
    private GroupTable groupTable;

    public void setGroupTable(GroupTable groupTable) {
        this.groupTable = groupTable;
    }
    private final ListGridRecord[] records;
    private SelectionManager selectionManager;
    private SelectItem colSelectionTable;
    private boolean selfSelectionTag = false;
    private final String[] infoSearchingMap;

    @Override
    public String toString() {
        return "OmicsTable";
    }
    private final VLayout omicsTableLayout;
//    private final Label header;
    private final int rowNumber,colNumber;
    private TextItem searchingField;
    private final HandlerRegistration searchingFieldFocusReg, searchingFieldBlurReg, searchingFieldKeyPressReg, searchBtnReg;
    private HandlerRegistration omicsSelectionReg,omicsDargStartSelectionReg;
    private final SectionStackSection rowSelectionSection;
    private final RadioGroupItem controlItem;

    public OmicsTableComponent(SelectionManager selectionManager, DatasetInformation datasetInfo, int rowsNumber,SectionStackSection rowSelectionSection,RadioGroupItem controlItem) {
        timer = new Timer() {

            @Override
            public void run() {
                SC.dismissCurrentDialog();
            }
        };
        this.controlItem = controlItem;
        this.rowSelectionSection = rowSelectionSection;
        infoSearchingMap = new String[rowsNumber];

        this.selectionManager = selectionManager;
        this.rowNumber = rowsNumber;
        this.colNumber = datasetInfo.getColNumb();
        this.records = getRecodList(datasetInfo);
        omicsTableLayout = new VLayout();
        rowSelectionSection.setTitle("&nbsp;Selection (0 / " + rowsNumber + ")");
        omicsTableLayout.setHeight("70%");
        omicsTableLayout.setWidth("100%");

        /*searching field */
        final DynamicForm form = new DynamicForm();
        omicsTableLayout.addMember(form);
        form.setAutoFocus(true);
        form.setNumCols(3);
        form.setWidth100();
        form.setHeight(20);
        searchingField = new TextItem("Search");
        searchingField.setTitle("Search");
        searchingField.setDefaultValue("Enter One Keyword");
        searchingField.setWrapTitle(false);
        searchingField.setTitleAlign(Alignment.LEFT);
        form.setMargin(10);

        searchingFieldFocusReg = searchingField.addFocusHandler(new FocusHandler() {

            @Override
            public void onFocus(FocusEvent event) {
                searchingField.setValue(" ");
            }
        });

        searchingFieldBlurReg = searchingField.addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                if (searchingField.getValueAsString().trim().equals("")) {
                    searchingField.clearValue();
                }
            }
        });

        final ButtonItem button = new ButtonItem("search", "Search");
        searchingFieldKeyPressReg = searchingField.addKeyPressHandler(new com.smartgwt.client.widgets.form.fields.events.KeyPressHandler() {

            @Override
            public void onKeyPress(com.smartgwt.client.widgets.form.fields.events.KeyPressEvent event) {
                if (event.getKeyName().equalsIgnoreCase("Enter")) {
                    searchKeyword();

                }
            }
        });
        button.setStartRow(false);
        button.setWidth(80);
        button.setIcon("../images/searchBtn.png");
        searchBtnReg = button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                searchKeyword();
            }
        });

        form.setFields(searchingField, button);
        form.setColWidths(new Object[]{50, "*", 80});
        searchingField.setWidth("*");
        form.draw();

        /*end*/
        this.omicsIdTable = new OmicsTable(datasetInfo);
        omicsTableLayout.addMember(omicsIdTable);
        initGrid();
        this.classtype = 0;
        this.components.add(OmicsTableComponent.this);
        this.selectionManager.addSelectionChangeListener(OmicsTableComponent.this);
        datasetInfo = null;
        selectionChanged(Selection.TYPE.OF_ROWS);
    }

    private void searchKeyword() {

        if (searchingField.getValueAsString() == null || searchingField.getValueAsString().equalsIgnoreCase("") || searchingField.getValueAsString().equalsIgnoreCase("Enter One Keyword")) {
            return;
        }

        SelectionManager.Busy_Task(true, false);
        String keyword = searchingField.getValueAsString();
        keyword = keyword.trim().toUpperCase();
        Set<Integer> selectionIndex = new HashSet<Integer>();

        for (int x = 0; x < infoSearchingMap.length; x++) {
            String key = infoSearchingMap[x];
            if (key.toUpperCase().contains(keyword)) {
                selectionIndex.add(x);
            }

        }

        int[] sel = new int[selectionIndex.size()];
        int indexer = 0;
        for (int x : selectionIndex) {
            sel[indexer++] = x;

        }
        if (sel.length > 0) {
            Selection selection = new Selection(Selection.TYPE.OF_ROWS, sel);
            selectionManager.setSelectedRows(selection);
        } else {
            SelectionManager.Busy_Task(false, true);
            SC.warn("Not Available", keyword.toUpperCase() + " Not Available");
            timer.schedule(3000);

        }

    }
    private final Timer timer;
    private boolean dragStart;

    private void initGrid() {
        omicsIdTable.setHeight("60%");
        omicsIdTable.setWidth("100%");
       
        omicsSelectionReg = omicsIdTable.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if(dragStart)
                    return;
                ListGridRecord[] selectionRecord = omicsIdTable.getSelectedRecords();
                if (selectionRecord != null && selectionRecord.length > 0) {
                    SelectionManager.Busy_Task(true, false);
                    updateSelectionManagerOnTableSelection(selectionRecord);
                } else {
                    selfSelectionTag = true;
                    updateSelectionManager(new int[]{});
                }

            }
        });
        
        
        omicsDargStartSelectionReg = omicsIdTable.addDragStartHandler(new DragStartHandler() {

            @Override
            public void onDragStart(DragStartEvent event) {
               dragStart=true;
            }
        });
        
        omicsSelectionReg = omicsIdTable.addDragStopHandler(new DragStopHandler() {

            @Override
            public void onDragStop(DragStopEvent event) {
                 dragStart= false;
               ListGridRecord[] selectionRecord = omicsIdTable.getSelectedRecords();
                if (selectionRecord != null && selectionRecord.length > 0) {
                    SelectionManager.Busy_Task(true, false);
                    updateSelectionManagerOnTableSelection(selectionRecord);
                }
            }
        });
        
    

    }

    private void updateSelectionManager(int[] selectedIndices) {
        selfSelectionTag = true;
        SelectionManager.Busy_Task(true, false);
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        selectionManager.setSelectedRows(selection);
    }

    public VLayout getOmicsTableLayout() {
        return omicsTableLayout;
    }

    private ListGridRecord[] getRecodList(DatasetInformation datasetInfo) {

        ListGridRecord[] recordsInit = new ListGridRecord[datasetInfo.getRowsNumb()];
        for (int x = 0; x < recordsInit.length; x++) {
            ListGridRecord record = new ListGridRecord();
            record.setAttribute("index", x);
            String[] annotationsRow = datasetInfo.getAnnotations()[x];
            String searchKey = "";
            for (int z = 0; z < datasetInfo.getAnnotationHeaders().length; z++) {
                searchKey += annotationsRow[z] + "_";
                record.setAttribute(datasetInfo.getAnnotationHeaders()[z], annotationsRow[z]);

            }

            record.setAttribute("all", "");
            for (int c = 0; c < datasetInfo.getRowGroupsNumb(); c++) {
                if (!datasetInfo.getOmicsTabelData()[c + 1][x].equalsIgnoreCase("#FFFFFF")) {

                    record.setAttribute(datasetInfo.getRowGroupList().get(c).getName(), datasetInfo.getOmicsTabelData()[c + 1][x]);

                } else {
                    record.setAttribute(datasetInfo.getRowGroupList().get(c).getName(), "");
                }
            }

            infoSearchingMap[x] = searchKey;
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
            selfSelectionTag = true;
            updateSelectionManager(selectedIndices);
        }else{
        
            selfSelectionTag = true;
            updateSelectionManager(new int[]{});
        
        }

    }

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (selfSelectionTag) {
            selfSelectionTag = false;
            if (((type != Selection.TYPE.OF_ROWS || selectionManager.getSelectedRows().getMembers() == null) || selectionManager.getSelectedRows().getMembers().length == 0) || !omicsIdTable.isVisible()) if (type == Selection.TYPE.OF_COLUMNS && selectionManager.getSelectedColumns().getMembers() != null && selectionManager.getSelectedColumns().getMembers().length != 0 && !omicsIdTable.isVisible()) {
//                header.setText("Selected Rows Number ( " + selectionManager.getSelectedRows().getMembers().length + " / " + rowNumber + " )");
                rowSelectionSection.setTitle("&nbsp;Selection (" + selectionManager.getSelectedColumns().getMembers().length + "/" + colNumber + ")");
            } else {
                //                header.setText("Selected Rows Number ( " + selectionManager.getSelectedRows().getMembers().length + " / " + rowNumber + " )");
                rowSelectionSection.setTitle("&nbsp;Selection (" + selectionManager.getSelectedRows().getMembers().length + "/" + rowNumber + ")");
                if (groupTable != null && !groupTable.isGroubTableSelection()) {
                    groupTable.deselectAllRecords();
                } else {
                    if (groupTable != null) {
                        groupTable.setGroubTableSelection(false);
                    }
                }
            }

        } else if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows();
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                //update table selection             
                if (selectedRows != null ){// selectedRows.length != 0) {
                    sendOnChangeEvent(controlItem.getForm(), controlItem.getName(), controlItem.getValueAsString(), "Rows");
                    controlItem.setValue("Rows");
                    ListGridRecord[] reIndexSelection = new ListGridRecord[selectedRows.length];
                    int i = 0;
                    for (int z : selectedRows) {
                        reIndexSelection[i++] = records[z];
                    }
                    omicsIdTable.setRecords(reIndexSelection);
//                    header.setText("Selected Rows Number ( " + reIndexSelection.length + " / " + rowNumber + " )");
                    rowSelectionSection.setTitle("&nbsp;Selection (" + reIndexSelection.length + "/" + rowNumber + ")");
                    try {
                        omicsIdTable.selectAllRecords();
                    } catch (Exception e) {
                        Window.alert(e.getLocalizedMessage());
                    }
                    if (groupTable != null && !groupTable.isGroubTableSelection()) {
                        groupTable.deselectAllRecords();
                    } else {
                        if (groupTable != null) {
                            groupTable.setGroubTableSelection(false);
                        }

                    }
                }else
                {
                    omicsIdTable.deselectAllRecords();
                    if (groupTable != null) {
                            groupTable.deselectAllRecords();
                        }
                
                }
            }
        } else if (type == Selection.TYPE.OF_COLUMNS) {
            Selection sel = selectionManager.getSelectedColumns();
            if (sel != null) {
                int[] selectedColumn = sel.getMembers();
                if (selectedColumn != null /*&& selectedColumn.length != 0 */&& colSelectionTable != null) {
                     sendOnChangeEvent(controlItem.getForm(), controlItem.getName(), controlItem.getValueAsString(), "Columns");
                    controlItem.setValue("Columns");
                    String[] values = new String[selectedColumn.length];
                    for (int x = 0; x < selectedColumn.length; x++) {
                        values[x] = "" + selectedColumn[x];
                    }
                    colSelectionTable.setValues(values);
                    colSelectionTable.redraw();

                }
                if (!omicsIdTable.isVisible() && selectedColumn != null) {
                    rowSelectionSection.setTitle("&nbsp;Selection (" + selectedColumn.length + "/" + colNumber + ")");
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
        searchingFieldFocusReg.removeHandler();
        searchingFieldBlurReg.removeHandler();
        searchingFieldKeyPressReg.removeHandler();
        searchBtnReg.removeHandler();
        if (omicsSelectionReg != null) {
            omicsSelectionReg.removeHandler();
        }
        if (omicsDargStartSelectionReg != null) {
            omicsDargStartSelectionReg.removeHandler();
        }
        selectionManager.removeSelectionChangeListener(this);
        selectionManager = null;
    }
    
    
    private native void sendOnChangeEvent(DynamicForm form, String formItemName, String oldValue, String newValue) /*-{
		var formWidget = form.@com.smartgwt.client.widgets.form.DynamicForm::getOrCreateJsObj()();
		var formItem = formWidget.getField(formItemName);
		if (typeof formItem.change == "function") {
			formItem.change(formItem.form, formItem, newValue, oldValue);
		} 
	}-*/;
}
