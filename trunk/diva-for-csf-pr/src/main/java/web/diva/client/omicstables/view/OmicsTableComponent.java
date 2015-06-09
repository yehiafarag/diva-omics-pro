package web.diva.client.omicstables.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
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
import com.smartgwt.client.widgets.grid.events.BodyKeyPressEvent;
import com.smartgwt.client.widgets.grid.events.BodyKeyPressHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.HashSet;
import java.util.Set;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.client.view.core.InfoIcon;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 * main omics information table module the class work as container for omices
 * tables and responsible for updating all components on the left panel
 *
 * @author Yehia Farag omics information table that has rows ids, and activated
 *
 */
public final class OmicsTableComponent extends ModularizedListener implements IsSerializable {

    private final OmicsTable omicsIdTable;
    private GroupTable groupTable;

    private final ListGridRecord[] records;
    private SelectionManager selectionManager;
    private SelectItem colSelectionTable;
    private boolean selfSelectionTag = false;
    private final String[] infoSearchingMap;

    private final VLayout omicsTableLayout;
    private final int rowNumber, colNumber;
    private TextItem searchingField;
    private final HandlerRegistration searchingFieldFocusReg, searchingFieldBlurReg, searchingFieldKeyPressReg, searchBtnReg;
    private HandlerRegistration omicsSelectionReg, omicsDargStartSelectionReg;
    private final SectionStackSection rowSelectionSection;
    private final RadioGroupItem controlItem;
    private final Timer timer;
    private boolean dragStart;

    /**
     *
     * @param selectionManager main central manager
     * @param datasetInfo dataset information object
     * @param rowsNumber total number of rows in the dataset
     * @param rowSelectionSection reference to the parent
     * @param controlItem reference to row/column control panel
     *
     *
     *
     */
    public OmicsTableComponent(SelectionManager selectionManager, DatasetInformation datasetInfo, int rowsNumber, SectionStackSection rowSelectionSection, RadioGroupItem controlItem) {
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
        this.records = initRecodList(datasetInfo);
        omicsTableLayout = new VLayout();
        rowSelectionSection.setTitle("&nbsp;Selection (0 / " + rowsNumber + ")");
        omicsTableLayout.setHeight("70%");
        omicsTableLayout.setWidth("100%");

        HLayout searchInfoLayout = new HLayout();
        searchInfoLayout.setHeight(20);
        searchInfoLayout.setMembersMargin(10);
        omicsTableLayout.addMember(searchInfoLayout);
        /*searching field */
        final DynamicForm form = new DynamicForm();
        searchInfoLayout.addMember(form);
        InfoIcon icon = new InfoIcon("Selection and Groups", initInfoLayout(300, 600), 300, 600);
        icon.setAutoHorizontalAlignment(Label.ALIGN_CONTENT_START);

        searchInfoLayout.addMember(icon);
        searchInfoLayout.setAlign(Alignment.CENTER);
        searchInfoLayout.setMargin(10);

        form.setAutoFocus(true);
        form.setNumCols(3);
        form.setWidth100();
        form.setHeight(20);
        searchingField = new TextItem("Search");
        searchingField.setTitle("Search");
        searchingField.setDefaultValue("Enter One Keyword");
        searchingField.setWrapTitle(false);
        searchingField.setTitleAlign(Alignment.LEFT);
//        form.setMargin(5);

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
        form.setTop(-7);

        /*end*/
        this.omicsIdTable = new OmicsTable(datasetInfo);
        omicsTableLayout.addMember(omicsIdTable);
        initGrid();
        this.classtype = 0;
        this.components.add(OmicsTableComponent.this);
        this.selectionManager.addSelectionChangeListener(OmicsTableComponent.this);
        datasetInfo = null;
        if (this.selectionManager.getSelectedRows() != null && this.selectionManager.getSelectedRows().getMembers() != null && this.selectionManager.getSelectedRows().getMembers().length > 0) {
            selectionChanged(Selection.TYPE.OF_ROWS);
        }
    }

    /**
     * This method is responsible for searching for data within the dataset
     * information columns
     */
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

    /**
     *
     * @param groupTable row group table
     *
     */
    public void setGroupTable(GroupTable groupTable) {
        this.groupTable = groupTable;
    }

    @Override
    public String toString() {
        return "OmicsTable";
    }

    /**
     * This method is responsible for initializing the omics information table
     *
     * @param selectionRecord list of the selected records
     *
     */
    private void initGrid() {
        omicsIdTable.setHeight("60%");
        omicsIdTable.setWidth("100%");
        omicsSelectionReg = omicsIdTable.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (dragStart) {
                    return;
                }
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
                dragStart = true;
            }
        });

        omicsSelectionReg = omicsIdTable.addDragStopHandler(new DragStopHandler() {

            @Override
            public void onDragStop(DragStopEvent event) {
                dragStart = false;
                ListGridRecord[] selectionRecord = omicsIdTable.getSelectedRecords();
                if (selectionRecord != null && selectionRecord.length > 0) {
                    SelectionManager.Busy_Task(true, false);
                    updateSelectionManagerOnTableSelection(selectionRecord);
                }
            }
        });
        final Timer t = new Timer() {

            @Override
            public void run() {
                ListGridRecord[] selectionRecord = omicsIdTable.getSelectedRecords();
                if (selectionRecord != null && selectionRecord.length > 0) {
                    SelectionManager.Busy_Task(true, false);
                    updateSelectionManagerOnTableSelection(selectionRecord);
                }
            }
        };

        omicsIdTable.addBodyKeyPressHandler(new BodyKeyPressHandler() {

            @Override
            public void onBodyKeyPress(BodyKeyPressEvent event) {
                t.schedule(500);
            }
        });

    }

    /**
     * This method is responsible for updating the selection manager on user
     * selection on table
     *
     * @param selectedIndices selected data indexes
     *
     */
    private void updateSelectionManager(int[] selectedIndices) {
        selfSelectionTag = true;
        SelectionManager.Busy_Task(true, false);
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        selectionManager.setSelectedRows(selection);
    }

    /**
     *
     * @return omicsTableLayout the main body layout
     *
     */
    public VLayout getOmicsTableLayout() {
        return omicsTableLayout;
    }

    /**
     * This method is responsible for initializing the record list
     *
     * @param datasetInfo
     *
     */
    private ListGridRecord[] initRecodList(DatasetInformation datasetInfo) {

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

    /**
     * This method is responsible for converting the selected records into
     * selection indexes to update the selection manager on user selection on
     * table
     *
     * @param selectionRecord list of the selected records
     *
     */
    private void updateSelectionManagerOnTableSelection(ListGridRecord[] selectionRecord) {
        if (selectionRecord.length > 0) {
            int[] selectedIndices = new int[selectionRecord.length];
            for (int index = 0; index < selectionRecord.length; index++) {
                ListGridRecord rec = selectionRecord[index];
                selectedIndices[index] = rec.getAttributeAsInt("index");
            }
            selfSelectionTag = true;
            updateSelectionManager(selectedIndices);
        } else {

            selfSelectionTag = true;
            updateSelectionManager(new int[]{});

        }

    }

    /**
     * This method is the listener implementation for the central manager the
     * method responsible for notifi the components there is selection event
     *
     * @param type Selection.TYPE row or column
     *
     */
    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (selfSelectionTag) {
            selfSelectionTag = false;
            if (((type != Selection.TYPE.OF_ROWS || selectionManager.getSelectedRows().getMembers() == null) || selectionManager.getSelectedRows().getMembers().length == 0) || !omicsIdTable.isVisible()) {
                if (type == Selection.TYPE.OF_COLUMNS && selectionManager.getSelectedColumns().getMembers() != null && selectionManager.getSelectedColumns().getMembers().length != 0 && !omicsIdTable.isVisible()) {
                    rowSelectionSection.setTitle("&nbsp;Selection (" + selectionManager.getSelectedColumns().getMembers().length + "/" + colNumber + ")");
                } else {
                    rowSelectionSection.setTitle("&nbsp;Selection (" + selectionManager.getSelectedRows().getMembers().length + "/" + rowNumber + ")");
                    if (groupTable != null && !groupTable.isGroubTableSelection()) {
                        groupTable.deselectAllRecords();
                    } else {
                        if (groupTable != null) {
                            groupTable.setGroubTableSelection(false);
                        }
                    }
                }
            }

        } else if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows();
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                //update table selection             
                if (selectedRows != null) {// selectedRows.length != 0) {
                    sendOnChangeEvent(controlItem.getForm(), controlItem.getName(), controlItem.getValueAsString(), "Rows");
                    controlItem.setValue("Rows");
                    ListGridRecord[] reIndexSelection = new ListGridRecord[selectedRows.length];
                    int i = 0;
                    for (int z : selectedRows) {
                        reIndexSelection[i++] = records[z];
                    }
                    omicsIdTable.setRecords(reIndexSelection);
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
                } else {
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
                if (selectedColumn != null /*&& selectedColumn.length != 0 */ && colSelectionTable != null) {
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

    /**
     * This method is responsible for setting colSelectionTable into the
     * component the component responsible for updating all omics tables
     *
     * @param colSelectionTable column selection list
     *
     */
    public void setColSelectionTable(SelectItem colSelectionTable) {
        this.colSelectionTable = colSelectionTable;
        selectionChanged(Selection.TYPE.OF_COLUMNS);
    }

    /**
     * This method is responsible for cleaning on removing the component from
     * the container
     */
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

    private VerticalPanel initInfoLayout(int h, int w) {
        VerticalPanel infopanel = new VerticalPanel();
        infopanel.setWidth(w + "px");
        infopanel.setHeight(h + "px");

        HTML information = new HTML(
                "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>The dataset annotations table allows the user to search for and select specific proteins or genes using keywords from any of the columns. Multiple selection is supported using mouse select and drag.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>Sort the table by clicking the desired column header.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>Switch between row and column selection mode using the top control panel <br/> "
                + "<img src='images/controller.png' alt='' style='width:auto;height:16px'/></p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>Export full or partial dataset (row groups) in tabular file format via the Export Dataset option <br/> "
                + "<img src='images/dsExpBtn.png' alt='' style='width:auto;height:16px'/>.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>Create a colored groups using Create Row Group <img src='images/rowGrBtn.png' alt='' style='width:auto;height:16px'/> and Create Column Group <img src='images/colGrBtn.png' alt='' style='width:auto;height:16px'/>.</p>"
                + "<p align=\"right\" style='margin-left:30px;font-size:14px;line-height: 150%;float: right;'><i>Full tutorial available <a target=\"_blank\" href='" + "tutorial/diva_tutorial.pdf" + "'>here</a>.</i></p>");

        infopanel.add(information);

        return infopanel;

    }

    private native void sendOnChangeEvent(DynamicForm form, String formItemName, String oldValue, String newValue) /*-{
     var formWidget = form.@com.smartgwt.client.widgets.form.DynamicForm::getOrCreateJsObj()();
     var formItem = formWidget.getField(formItemName);
     if (typeof formItem.change == "function") {
     formItem.change(formItem.form, formItem, newValue, oldValue);
     } 
     }-*/;
}
