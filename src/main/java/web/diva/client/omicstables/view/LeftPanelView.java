/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.omicstables.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.IMenuButton;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ItemClickEvent;
import com.smartgwt.client.widgets.menu.events.ItemClickHandler;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import web.diva.client.DivaServiceAsync;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.client.view.core.GroupPanel;
import web.diva.client.view.core.RowGroupPanel;
import web.diva.client.view.core.SaveAsPanel;
import web.diva.client.view.core.SaveDatasetPanel;
import web.diva.shared.beans.DivaGroup;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 *
 * @author Yehia Farag this component represents the left panel on DIVA it
 * contains omics data information table rows sel tables columns sel tables
 */
public class LeftPanelView extends SectionStack {

    private SelectItem colSelectionTable = null;
    private RowGroupPanel exportPanel = null, subDsPanel, activeGroupPanel = null;
    private DatasetInformation datasetInfo = null;
    private SelectionManager selectionManager = null;
    private DivaServiceAsync GWTClientService = null;
    private OmicsTableComponent omicsTable = null;
    private IButton colGroupBtn;
    private GroupPanel groupPanel;
    private GroupTable groupTable;

    private SaveDatasetPanel saveDsPanel;
    private final SectionStackSection rowSelectionSection;
    private final HandlerRegistration controlItemChangeHandlerReg, itemClickHandlerReg, colGroupBtnReg;
    private HandlerRegistration createRowGroupHandlerReg, colGroupHandlerReg, selectColsReg, exportPanelReg, activeGroupPanelReg, subDsPanelReg, saveDsPanelReg;

    public LeftPanelView(SelectionManager selectionManagers, DivaServiceAsync GWTClientService, DatasetInformation datasetInfos) {

        this.setVisibilityMode(VisibilityMode.MULTIPLE);
        this.selectionManager = selectionManagers;
        this.GWTClientService = GWTClientService;
        this.datasetInfo = datasetInfos;

        this.setBorder("1px solid #E6E6E6");
        this.setWidth("20%");
        this.setHeight("89%");
        this.setMargin(2);
        this.setScrollSectionIntoView(false);
        rowSelectionSection = new SectionStackSection("&nbsp;Selection ( " +  0+ " / " + datasetInfo.getRowsNumb() + " )");     

        rowSelectionSection.setControls();
        rowSelectionSection.setCanCollapse(false);
        this.addSection(rowSelectionSection);
        final VLayout rowSelectionLayout = new VLayout();
        final VLayout colSelectionLayout = new VLayout();
        colSelectionLayout.setMembersMargin(5);
        final String rowControl = "Rows";
        final String colControl = "Columns";
        final LinkedHashMap<String, String> controls = new LinkedHashMap<String, String>();
        controls.put(colControl, colControl);
        controls.put(rowControl, rowControl);

        final RadioGroupItem controlItem = new RadioGroupItem();
        controlItem.setShowTitle(false);
        controlItem.setWidth(100);
        controlItem.setHeight("12px");
        controlItem.setVertical(false);
        controlItem.setAlign(Alignment.RIGHT);

        controlItem.setValueMap(controls);
        controlItem.setValue(rowControl);

        controlItemChangeHandlerReg = controlItem.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

            @Override
            public void onChange(com.smartgwt.client.widgets.form.fields.events.ChangeEvent event) {
                if (event.getValue().toString().equalsIgnoreCase(rowControl)) {

                     if (selectionManager.getSelectedRows()!= null && selectionManager.getSelectedRows().getMembers()!= null) {
                        rowSelectionSection.setTitle("&nbsp;Selection ( " + selectionManager.getSelectedRows().getMembers().length + " / " + datasetInfo.getRowsNumb()+ " )");
                    } else {
                        rowSelectionSection.setTitle("&nbsp;Selection ( " + 0 + " / " + datasetInfo.getRowsNumb()+ " )");
                    }
                    rowSelectionLayout.setVisible(true);
                    colSelectionLayout.setVisible(false);

                } else {
                    rowSelectionLayout.setVisible(false);
                    colSelectionLayout.setVisible(true);
                    colSelectionLayout.setWidth("100%");
                    colSelectionLayout.setHeight("2%");

                    if (selectionManager.getSelectedColumns() != null && selectionManager.getSelectedColumns().getMembers()!= null) {
                        rowSelectionSection.setTitle("&nbsp;Selection ( " + selectionManager.getSelectedColumns().getMembers().length + " / " + datasetInfo.getColNumb() + " )");
                    } else {
                        rowSelectionSection.setTitle("&nbsp;Selection ( " + 0 + " / " + datasetInfo.getColNumb() + " )");
                    }
                    colSelectionTable.setWidth(omicsTable.getOmicsTableLayout().getWidth());
                    gBtnLayout.setWidth("100%");
                    colGroupBtn.setWidth(100 + "%");

                }
            }
        });
        DynamicForm form = new DynamicForm();

        form.setItems(controlItem);
        form.setWidth(100);
        form.setAlign(Alignment.RIGHT);
        form.setHeight(12);

        rowSelectionSection.setControls(form);

        //start of row layout 
        final VLayout mainBodyLayout = new VLayout();
        rowSelectionSection.addItem(mainBodyLayout);
        mainBodyLayout.setWidth("100%");
        mainBodyLayout.setHeight("90%");
        mainBodyLayout.setAlign(Alignment.CENTER);
        mainBodyLayout.setAlign(VerticalAlignment.TOP);
        mainBodyLayout.addMembers(rowSelectionLayout, colSelectionLayout);
        rowSelectionLayout.setWidth("100%");
        rowSelectionLayout.setHeight("100%");

        colSelectionLayout.setVisible(false);
        colSelectionLayout.setHeight(30);
        colSelectionLayout.setAlign(VerticalAlignment.TOP);

        omicsTable = new OmicsTableComponent(selectionManager, datasetInfos, datasetInfos.getRowsNumb(),rowSelectionSection);
        rowSelectionLayout.addMember(omicsTable.getOmicsTableLayout());

        HorizontalPanel rowGBtnLayout = new HorizontalPanel();
        rowSelectionLayout.addMember(rowGBtnLayout);
        rowSelectionLayout.setMembersMargin(1);
        rowGBtnLayout.setHeight(30 + "px");
        rowGBtnLayout.setWidth(100 + "%");
        final com.smartgwt.client.widgets.events.ClickHandler createRowGroupHandler = new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Selection sel = selectionManager.getSelectedRows();
                if (groupPanel.getForm().validate()) {
                    createRowGroup(groupPanel.getName(), groupPanel.getColor(), groupPanel.getDescription(), sel.getMembers());
                }
            }
        };

        /* new group and ds  layout*/
        MenuItem createRowGroupMenuItem = new MenuItem("Create Row Groups");
        MenuItem activateRowGroupMenuItem = new MenuItem("Activate Row Groups");

        Menu rowGroupMenu = new Menu();
        rowGroupMenu.setCanSelectParentItems(true);
        rowGroupMenu.setData(createRowGroupMenuItem, activateRowGroupMenuItem);
        rowGroupMenu.setWidth(100);

        rowGroupMenu.addItemClickHandler(new ItemClickHandler() {
            @Override
            public void onItemClick(ItemClickEvent event) {

                if (event.getItem().getTitle().equalsIgnoreCase("Create Row Groups")) {
                    if (groupPanel == null) {
                        groupPanel = new GroupPanel();
                        createRowGroupHandlerReg = groupPanel.getOkBtn().addClickHandler(createRowGroupHandler);
                    }
                    updateAndShowGroupPanel("row");
                } else {
                    if (activeGroupPanel == null) {
                        initActiveGroupPanel(datasetInfo.getRowGroupList());
                    }
                    if(datasetInfo.getRowGroupList().size()>1){
                    updateActiveGroupPanelPanel();
                    activeGroupPanel.center();
                    activeGroupPanel.show();
                    }

                }
            }
        });

        IMenuButton rowGroupBtn = new IMenuButton("Row Groups", rowGroupMenu);
//        rowGroupBtn.setShowMenuButtonImage(false);

        rowGBtnLayout.add(rowGroupBtn);
        rowGroupBtn.setWidth("9%");
        rowGroupBtn.setAlign(Alignment.CENTER);

        rowGBtnLayout.setCellVerticalAlignment(rowGroupBtn, HorizontalPanel.ALIGN_MIDDLE);
        rowGBtnLayout.setCellHorizontalAlignment(rowGroupBtn, HorizontalPanel.ALIGN_CENTER);

        rowSelectionLayout.setAlign(Alignment.CENTER);
        rowGBtnLayout.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        rowGBtnLayout.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);

        rowGroupBtn.setMargin(0);
        rowSelectionLayout.setAlign(Alignment.CENTER);

        MenuItem createSDMenuItem = new MenuItem("Create Sub-Dataset");
        MenuItem exportDatasetMenuItem = new MenuItem("Export Data");
        MenuItem saveDatasetMenuItem = new MenuItem("Save Current Dataset");

        Menu datasetMenu = new Menu();
        datasetMenu.setCanSelectParentItems(true);
        datasetMenu.setData(createSDMenuItem, exportDatasetMenuItem, saveDatasetMenuItem);
        datasetMenu.setWidth(100);

        itemClickHandlerReg = datasetMenu.addItemClickHandler(new ItemClickHandler() {
            @Override
            public void onItemClick(ItemClickEvent event) {
                if (event.getItem().getTitle().equalsIgnoreCase("Create Sub-Dataset")) {
                    if (subDsPanel == null) {
                        initSubDsPanel(datasetInfo.getRowGroupList());
                    }
                    updateSubDsPanel();
                    subDsPanel.center();
                    subDsPanel.show();
                } else if (event.getItem().getTitle().equalsIgnoreCase("Save Current Dataset")) {
                    if (saveDsPanel == null) {
                        initSaveDsPanel();
                    }

                    saveDsPanel.center();
                    saveDsPanel.show();
                } else if (event.getItem().getTitle().equalsIgnoreCase("Export Data")) {
                    if (exportPanel == null) {
                        initExportPanel(datasetInfo.getRowGroupList());
                    }
                    updateExportPanel();
                    exportPanel.center();
                    exportPanel.show();
                }
            }
        });

        IMenuButton datasetMenuBtn = new IMenuButton("Dataset", datasetMenu);
        rowGBtnLayout.add(datasetMenuBtn);
        datasetMenuBtn.setWidth("9%");
        datasetMenuBtn.setAlign(Alignment.CENTER);

        rowGBtnLayout.setCellVerticalAlignment(datasetMenuBtn, HorizontalPanel.ALIGN_MIDDLE);
        rowGBtnLayout.setCellHorizontalAlignment(datasetMenuBtn, HorizontalPanel.ALIGN_CENTER);

        rowSelectionLayout.setAlign(Alignment.CENTER);
        rowGBtnLayout.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        rowGBtnLayout.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);

        rowGBtnLayout.setSpacing(2);
        datasetMenuBtn.setMargin(0);
        rowSelectionLayout.setAlign(Alignment.CENTER);

        /* end of new  group and ds  layout*/
        groupTable = new GroupTable(selectionManager);
        groupTable.updateRecords(datasetInfo.getRowGroupList());
        rowSelectionLayout.addMember(groupTable);

        omicsTable.setGroupTable(groupTable);
        this.expandSection(rowSelectionSection.getID());
        this.redraw();

        final com.smartgwt.client.widgets.events.ClickHandler colGroupHandler = new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Selection sel = selectionManager.getSelectedColumns();
                if (groupPanel.getForm().validate()) {
                    createColGroup(groupPanel.getName(), groupPanel.getColor(), groupPanel.getDescription(), sel.getMembers());
                }
            }
        };
        colSelectionTable = initColSelectionTable(datasetInfos.getColNamesMap());
        colSelectionTable.setHeight((datasetInfos.getColNamesMap().size() * 20));

        DynamicForm columnForm = new DynamicForm();
        colSelectionLayout.setMembers(columnForm);
        columnForm.setItems(colSelectionTable);
        columnForm.setAlwaysShowScrollbars(false);
        columnForm.setWidth("100%");
        columnForm.setHeight((datasetInfos.getColNamesMap().size() * 20));
        columnForm.setStyleName("borderless");
        columnForm.setShowEdges(false);
        columnForm.setScrollbarSize(5);

        omicsTable.setColSelectionTable(colSelectionTable);

        gBtnLayout = new HLayout();
        colSelectionLayout.addMember(gBtnLayout);
        gBtnLayout.setHeight(30);

        colGroupBtn = new IButton("Create Column Group");
        gBtnLayout.addMember(colGroupBtn);

        colSelectionLayout.setAlign(Alignment.CENTER);
        colSelectionLayout.setAlign(VerticalAlignment.TOP);

        gBtnLayout.setAlign(Alignment.CENTER);
        gBtnLayout.setAlign(VerticalAlignment.CENTER);

        colGroupBtnReg = colGroupBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (groupPanel == null) {
                    groupPanel = new GroupPanel();
                    colGroupHandlerReg = groupPanel.getOkBtn().addClickHandler(colGroupHandler);
                }
                updateAndShowGroupPanel("col");
            }
        });

    }
    private HLayout gBtnLayout;

    private void updateAndShowGroupPanel(String type) {
        if (type.equalsIgnoreCase("col")) {
            groupPanel.setCount(selectionManager.getSelectedColumns().getMembers().length);
        } else if (type.equalsIgnoreCase("row")) {
            groupPanel.setCount(selectionManager.getSelectedRows().getMembers().length);
        }
        groupPanel.center();
        groupPanel.show();

    }

    /**
     * This method is responsible for initializing columns sel table
     *
     * @param colNamesMap - selected columns name
     */
    private SelectItem initColSelectionTable(LinkedHashMap<String, String> colNamesMap) {
        final SelectItem selectCols = new SelectItem();
        selectCols.setTitle(" Selected Columns ");
        selectCols.setShowTitle(false);

        selectCols.setTextAlign(Alignment.CENTER);
        selectCols.setTitleAlign(Alignment.CENTER);
        selectCols.setTitleOrientation(TitleOrientation.TOP);
        selectCols.setMultiple(true);
        selectCols.setShowAllOptions(true);
        selectCols.setMultipleAppearance(MultipleAppearance.GRID);
        selectCols.setValueMap(colNamesMap);
        selectColsReg = selectCols.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

            @Override
            public void onChange(com.smartgwt.client.widgets.form.fields.events.ChangeEvent event) {
                String[] stringValues = event.getValue().toString().split(",");
                int[] sel = new int[stringValues.length];
                for (int x = 0; x < stringValues.length; x++) {
                    sel[x] = Integer.valueOf(stringValues[x]);
                }
                Selection selection = new Selection(Selection.TYPE.OF_COLUMNS, sel);
                selectionManager.setSelectedColumns(selection);
            }
        });
        return selectCols;
    }

    public SelectItem getColSelectionTable() {
        return colSelectionTable;
    }

    private void initExportPanel(List<DivaGroup> rowGroupList) {
        exportPanel = new RowGroupPanel(rowGroupList, "Export Row Group", SelectionStyle.SINGLE);
        exportPanelReg = exportPanel.getOkBtn().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                String gname = exportPanel.getSelectRowGroup();
                if (gname != null) {
                    exportData(gname);
                }
            }
        });

    }

    private void initActiveGroupPanel(List<DivaGroup> rowGroupList) {
        activeGroupPanel = new RowGroupPanel(rowGroupList, "Activate Row Group", SelectionStyle.SIMPLE);
        activeGroupPanelReg = activeGroupPanel.getOkBtn().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                String[] gnames = activeGroupPanel.getSelectRowGroups();
                if (gnames != null && gnames.length != 0) {
                    activateGroups(gnames);
                }
            }
        });

    }

    private void initSubDsPanel(List<DivaGroup> rowGroupList) {
        subDsPanel = new RowGroupPanel(rowGroupList, "Create Sub-Dataset", SelectionStyle.SINGLE);
        subDsPanelReg = subDsPanel.getOkBtn().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (subDsPanel.getForm().validate()) {
                    String gType = subDsPanel.getSelectRowGroup();
                    String gName = subDsPanel.getName();

                    if (gType != null) {
                        if (gType.equalsIgnoreCase("Current Selected Indexes")) {
                            createSubDataset(gName, gType, selectionManager.getSelectedRows().getMembers());
                        } else {
                            createSubDataset(gName, gType, null);

                        }
                    }
                }
            }
        });

    }

    private void initSaveDsPanel() {
        saveDsPanel = new SaveDatasetPanel();
        saveDsPanelReg = saveDsPanel.getOkBtn().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (saveDsPanel.getForm().validate()) {
                    String dsName = saveDsPanel.getName();
                    saveDataset(dsName);
                }
            }
        });

    }

    private void updateExportPanel() {
        exportPanel.updateData(datasetInfo.getRowGroupList());

    }

    private void updateActiveGroupPanelPanel() {
        activeGroupPanel.updateData(datasetInfo.getRowGroupList());

    }

    private void updateSubDsPanel() {
        DivaGroup dg = new DivaGroup();
        dg.setColor("../images/lightgray.png");
        dg.setCount(selectionManager.getSelectedRows().getMembers().length);
        dg.setName("Current Selected Indexes");
        List<DivaGroup> list = new ArrayList<DivaGroup>();
        list.addAll(datasetInfo.getRowGroupList());
        list.remove(datasetInfo.getRowGroupList().get(datasetInfo.getRowGroupList().size() - 1));
        list.add(dg);
        subDsPanel.updateData(list);

    }

    /**
     * This method is responsible for invoking export data method
     *
     * @param datasetId - datasetId
     * @param rowGroup row group to export as text file
     */
    private void exportData(String rowGroup) {
        SelectionManager.Busy_Task(true, true);
        GWTClientService.exportData(rowGroup,
                new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(String result) {
//                        Window.open(result, "downlod window", "status=0,toolbar=0,menubar=0,location=0");     
                        sa.setResourceUrl(result);
                        SelectionManager.Busy_Task(false, true);
                        sa.center();
                        sa.show();
                        exportPanel.hide(true);
                        result = null;

                    }
                });

    }
    private final SaveAsPanel sa = new SaveAsPanel("File");

    /**
     * This method is responsible for invoking create row group method
     *
     * @param name - row group name
     * @param color - selected hashed color
     * @param type - group type (row)
     * @param selectedRows - selected rows indexes
     */
    private void createRowGroup(String name, String color, String type, int[] selectedRows) {
        SelectionManager.Busy_Task(true, true);
        GWTClientService.createRowGroup(name, color, type, selectedRows,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(DatasetInformation result) {
                        groupPanel.hide(true);
                        selectionManager.updateAllModules(result);

                    }
                });

    }

    public void setDatasetInfo(DatasetInformation datasetInfo) {
        this.datasetInfo = datasetInfo;
    }

    /**
     * This method is responsible for invoking save dataset method
     *
     * @param name - new dataset name
     */
    private void saveDataset(String name) {
        SelectionManager.Busy_Task(true, true);
        GWTClientService.saveDataset(name,
                new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(String datasetName) {                       
                        saveDsPanel.hide(true);
                        selectionManager.saveDataset(datasetName);
                          SC.say("Done", "Dataset Successfully Saved");
                    }
                });

    }

    /**
     * This method is responsible for invoking create sub-dataset method
     *
     * @param name - new sub-dataset name
     * @param selectedRows - selected rows indexes
     */
    private void createSubDataset(String name, String type, int[] selectedRows) {
        SelectionManager.Busy_Task(true, true);
        GWTClientService.createSubDataset(name, type, selectedRows,
                new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(String datasetId) {
                        subDsPanel.hide(true);
                        selectionManager.updateDropdownList(datasetId);
                        SC.say("Done", "Successfully Stored Sub-Dataset");
                    }
                });

    }

    /**
     * This method is responsible for invoking create colum group method
     *
     * @param datasetId - datasetId
     * @param name - column group name
     * @param color - selected hashed color
     * @param selection - selected column indexes
     * @param type - group type (column)
     */
    private void createColGroup(String name, String color, String type, int[] selection) {
        SelectionManager.Busy_Task(true, true);
        GWTClientService.createColGroup(name, color, type, selection,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(DatasetInformation result) {
                        SelectionManager.Busy_Task(false, true);
                        groupPanel.hide(true);
                        selectionManager.updateAllModules(result);
                    }
                });

    }

    /**
     * This method is responsible for invoking activate rows groups method
     *
     * @param datasetId - datasetId
     * @param rowGroups row groups indexes to activate
     * @param colGroups column groups indexes to activate
     * @param colGropNames selected ranking columns indexes
     */
    private void activateGroups(String[] rowGroups) {

        SelectionManager.Busy_Task(true, true);
        GWTClientService.activateGroups(rowGroups,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        SelectionManager.Busy_Task(false, true);
                    }
                    @Override
                    public void onSuccess(DatasetInformation result) {
                        activeGroupPanel.hide(true);
                        selectionManager.updateAllModules(result);

                    }
                });

    }

    public void remove() { 
        omicsTable.remove();
        groupTable.remove();
        controlItemChangeHandlerReg.removeHandler();
        if (createRowGroupHandlerReg != null) {
            createRowGroupHandlerReg.removeHandler();
        }
        itemClickHandlerReg.removeHandler();
        if (colGroupHandlerReg != null) {
            colGroupHandlerReg.removeHandler();
        }
        if (selectColsReg != null) {
            selectColsReg.removeHandler();
        }
        colGroupBtnReg.removeHandler();
        if (exportPanelReg != null) {
            exportPanelReg.removeHandler();
        }
        if (activeGroupPanelReg != null) {
            activeGroupPanelReg.removeHandler();
        }
        if (subDsPanelReg != null) {
            subDsPanelReg.removeHandler();
        }
        if (saveDsPanelReg != null) {
            saveDsPanelReg.removeHandler();
        }
        exportPanel = null;
        subDsPanel = null;
        activeGroupPanel = null;
        datasetInfo = null;
        selectionManager = null;
        GWTClientService = null;
       
        omicsTable = null;
        groupTable = null;

    }

}
