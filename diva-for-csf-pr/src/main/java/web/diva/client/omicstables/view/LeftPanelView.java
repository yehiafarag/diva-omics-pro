
package web.diva.client.omicstables.view;

import com.google.gwt.event.shared.HandlerRegistration;
//import com.google.gwt.user.client.Window;
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
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
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
import web.diva.client.view.core.Notification;
import web.diva.client.view.core.RowGroupPanel;
import web.diva.client.view.core.SaveAsPanel;
import web.diva.client.view.core.SaveDatasetPanel;
import web.diva.shared.beans.DivaGroup;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 * container for omics data information table row groups tables and columns
 * group tables
 *
 * @author Yehia Farag this component represents the left panel on DIVA it
 *
 */
public class LeftPanelView extends SectionStack {

    private SelectItem colSelectionTable = null;
    private RowGroupPanel exportPanel = null, subDsPanel, activeGroupPanel = null;
    private DatasetInformation datasetInfo = null;
    private SelectionManager Selection_Manager = null;
    private DivaServiceAsync GWTClientService = null;
    private OmicsTableComponent omicsTable = null;
    private IButton colGroupBtn;
    private GroupPanel groupPanel;
    private GroupTable rowGroupTable;
    private final GroupTable colGroupTable;
    private HLayout gBtnLayout;
    private SaveDatasetPanel saveDsPanel;
    private final SectionStackSection rowSelectionSection;
    private final HandlerRegistration controlItemChangeHandlerReg, itemClickHandlerReg, colGroupBtnReg;
    private HandlerRegistration createRowGroupHandlerReg, colGroupHandlerReg, selectColsReg, exportPanelReg, activeGroupPanelReg, subDsPanelReg, saveDsPanelReg;

    /**
     *
     * @param selectionManagers main central manager
     * @param datasetInfos dataset information object
     * @param GWTClientService access for client service
     * @param width panel width
     * @param height panel height
     *
     *
     *
     */
    public LeftPanelView(SelectionManager selectionManagers, DivaServiceAsync GWTClientService, DatasetInformation datasetInfos, int width, int height) {

        this.setVisibilityMode(VisibilityMode.MULTIPLE);
        this.Selection_Manager = selectionManagers;
        this.GWTClientService = GWTClientService;
        this.datasetInfo = datasetInfos;

        this.setBorder("1px solid #F6F5F5");
        this.setWidth(width + "px");
        this.setHeight(height + "px");
        this.setMargin(2);
        this.setScrollSectionIntoView(false);
        rowSelectionSection = new SectionStackSection("&nbsp;Selection (" + 0 + "/" + datasetInfo.getRowsNumb() + ")");

      
        rowSelectionSection.setCanCollapse(false);
        this.addSection(rowSelectionSection);
        final VLayout rowSelectionLayout = new VLayout();
        final VLayout colSelectionLayout = new VLayout();
        colSelectionLayout.setMembersMargin(5);
        final String rowControl = "Rows";
        final String colControl = "Columns";
        final LinkedHashMap<String, String> controls = new LinkedHashMap<String, String>();
        controls.put(rowControl, rowControl);
        controls.put(colControl, colControl);

        final RadioGroupItem controlItem = new RadioGroupItem();
        controlItem.setShowTitle(false);
        controlItem.setWidth(100);
        controlItem.setHeight("12px");
        controlItem.setVertical(false);
        controlItem.setAlign(Alignment.RIGHT);

        controlItem.setValueMap(controls);
        

        controlItemChangeHandlerReg = controlItem.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

            @Override
            public void onChange(com.smartgwt.client.widgets.form.fields.events.ChangeEvent event) {
                if (event.getValue().toString().equalsIgnoreCase(rowControl)) {

                    if (Selection_Manager.getSelectedRows() != null && Selection_Manager.getSelectedRows().getMembers() != null) {
                        rowSelectionSection.setTitle("&nbsp;Selection (" + Selection_Manager.getSelectedRows().getMembers().length + "/" + datasetInfo.getRowsNumb() + ")");
                    } else {
                        rowSelectionSection.setTitle("&nbsp;Selection (" + 0 + "/" + datasetInfo.getRowsNumb() + ")");
                    }
                    rowSelectionLayout.setVisible(true);
                    colSelectionLayout.setVisible(false);

                } else {
                    rowSelectionLayout.setVisible(false);
                    colSelectionLayout.setVisible(true);
                    colSelectionLayout.setWidth("100%");
                    colSelectionLayout.setHeight("2%");

                    if (Selection_Manager.getSelectedColumns() != null && Selection_Manager.getSelectedColumns().getMembers() != null) {
                        rowSelectionSection.setTitle("&nbsp;Selection (" + Selection_Manager.getSelectedColumns().getMembers().length + "/" + datasetInfo.getColNumb() + ")");
                    } else {
                        rowSelectionSection.setTitle("&nbsp;Selection (" + 0 + "/" + datasetInfo.getColNumb() + ")");
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

        omicsTable = new OmicsTableComponent(Selection_Manager, datasetInfos, datasetInfos.getRowsNumb(), rowSelectionSection, controlItem);
        rowSelectionLayout.addMember(omicsTable.getOmicsTableLayout());

        HorizontalPanel rowGBtnLayout = new HorizontalPanel();
        rowSelectionLayout.addMember(rowGBtnLayout);
        rowSelectionLayout.setMembersMargin(1);
        rowGBtnLayout.setHeight(30 + "px");
        rowGBtnLayout.setWidth(100 + "%");
        final com.smartgwt.client.widgets.events.ClickHandler createRowGroupHandler = new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Selection sel = Selection_Manager.getSelectedRows();
                if (groupPanel.getForm().validate()) {
                    createRowGroup(groupPanel.getName(), groupPanel.getColor(), groupPanel.getDescription(), sel.getMembers());
                }
            }
        };

        /* new group and ds  layout*/
//        MenuItem createRowGroupMenuItem = new MenuItem("Create Row Groups");
//        MenuItem activateRowGroupMenuItem = new MenuItem("Activate Row Groups");
//        Menu rowGroupMenu = new Menu();
//        rowGroupMenu.setCanSelectParentItems(true);
//        rowGroupMenu.setData(createRowGroupMenuItem, activateRowGroupMenuItem);
//        rowGroupMenu.setWidth(100);
//
//        rowGroupMenu.addItemClickHandler(new ItemClickHandler() {
//            @Override
//            public void onItemClick(ItemClickEvent event) {
//
//                if (event.getItem().getTitle().equalsIgnoreCase("Create Row Groups")) {
//                    if (groupPanel == null) {
//                        groupPanel = new GroupPanel();
//                        createRowGroupHandlerReg = groupPanel.getOkBtn().addClickHandler(createRowGroupHandler);
//                    }
//                    updateAndShowGroupPanel("row");
//                } else {
//                    if (activeGroupPanel == null) {
//                        initActiveGroupPanel(datasetInfo.getRowGroupList());
//                    }
//                    if(datasetInfo.getRowGroupList().size()>1){
//                    updateActiveGroupPanelPanel();
//                    activeGroupPanel.center();
//                    activeGroupPanel.show();
//                    }else{
//                    Notification.notifi("","Only One Group Available");
//                    
//                    }
//
//                }
//            }
//        });
//
//        IMenuButton rowGroupBtn = new IMenuButton("Row Groups", rowGroupMenu);
////        rowGroupBtn.setShowMenuButtonImage(false);
        IButton rowGroupBtn = new IButton("Create Row Group");
        rowGroupBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (groupPanel == null) {
                    groupPanel = new GroupPanel();
                    createRowGroupHandlerReg = groupPanel.getOkBtn().addClickHandler(createRowGroupHandler);
                }
                updateAndShowGroupPanel("row");
            }
        });
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
        datasetMenu.setData(/*createSDMenuItem,*/exportDatasetMenuItem/*, saveDatasetMenuItem*/);
        datasetMenu.setWidth(100);

        itemClickHandlerReg = datasetMenu.addItemClickHandler(new ItemClickHandler() {
            @Override
            public void onItemClick(ItemClickEvent event) {
                if (event.getItem().getTitle().equalsIgnoreCase("Create Sub-Dataset")) {
                    if (subDsPanel == null) {
                        initSubDsPanel(datasetInfo.getRowGroupList());
                    }
                    if (Selection_Manager.getSelectedRows() == null && datasetInfo.getRowGroupList().size() == 1) {
                        Notification.notifi("", "You Need To Select Data or Create Row Group First ");
                    } else {
                        updateSubDsPanel();
                        subDsPanel.center();
                        subDsPanel.show();
                    }
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

        IMenuButton datasetMenuBtn = new IMenuButton("Export Dataset", datasetMenu);
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
        rowGroupTable = new GroupTable(Selection_Manager, "row");
        rowGroupTable.updateRecords(datasetInfo.getRowGroupList());
        rowSelectionLayout.addMember(rowGroupTable);

        omicsTable.setGroupTable(rowGroupTable);
        this.expandSection(rowSelectionSection.getID());
        this.redraw();

        final com.smartgwt.client.widgets.events.ClickHandler colGroupHandler = new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Selection sel = Selection_Manager.getSelectedColumns();
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
        colGroupBtn.setWidth100();
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
        colGroupTable = new GroupTable(Selection_Manager, "col");
        colGroupTable.updateRecords(datasetInfo.getColGroupsList());
        colSelectionLayout.addMember(colGroupTable);
        rowSelectionLayout.setVisible(true);
        colSelectionLayout.setVisible(false);
        controlItem.setValue(rowControl);

    }

    /**
     * This method is responsible for updating groups panels (for create group)
     * and visualize it
     *
     * @param type type of the group col or row
     */
    private void updateAndShowGroupPanel(String type) {
        if (type.equalsIgnoreCase("col")) {
            if (Selection_Manager.getSelectedColumns() == null) {
                Notification.notifi("", "You Need To Select Data First");
            } else {
                groupPanel.setCount(Selection_Manager.getSelectedColumns().getMembers().length);
                groupPanel.center();
                groupPanel.show();
            }
        } else if (type.equalsIgnoreCase("row")) {
            if (Selection_Manager.getSelectedRows() == null) {
                Notification.notifi("", "You Need To Select Data First");
            } else {
                groupPanel.setCount(Selection_Manager.getSelectedRows().getMembers().length);
                groupPanel.center();
                groupPanel.show();
            }

        }

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
                Selection_Manager.setSelectedColumns(selection);
            }
        });
        return selectCols;
    }

    /**
     * This method is responsible for getting the column selection list
     *
     * @return SelectItem -column selection list
     */
    public SelectItem getColSelectionTable() {
        return colSelectionTable;
    }

    /**
     * This method is responsible for initializing exporting panel
     *
     * @param rowGroupList list of the row groups
     */
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

    /**
     * This method is responsible for sub-dataset creation panel
     *
     * @param rowGroupList list of the row groups
     */
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
                            createSubDataset(gName, gType, Selection_Manager.getSelectedRows().getMembers());
                        } else {
                            createSubDataset(gName, gType, null);

                        }
                    }
                }
            }
        });

    }

    /**
     * This method is responsible for initializing save dataset panel
     */
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

    /**
     * This method is responsible for updating the exporting panel with the
     * updated groups and selection
     */
    private void updateExportPanel() {
        exportPanel.updateData(datasetInfo.getRowGroupList());

    }

    /**
     * This method is responsible for updating the sub-dataset panel with the
     * updated groups and selection
     */
    private void updateSubDsPanel() {
        DivaGroup dg = new DivaGroup();
        dg.setColor("../images/lightgray.png");
        dg.setCount(Selection_Manager.getSelectedRows().getMembers().length);
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
                        Selection_Manager.connError();
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(String result) {
                        SaveAsPanel sa = new SaveAsPanel("File", result);
                        SelectionManager.Busy_Task(false, true);
                        sa.center();
                        sa.show();
                        exportPanel.hide(true);
                        result = null;

                    }
                });

    }

    /**
     * This method is responsible for invoking create row group method
     *
     * @param name - row group name
     * @param color - selected hashed color
     * @param description - group description
     * @param selectedRows - selected rows indexes
     */
    private void createRowGroup(String name, String color, String description, int[] selectedRows) {
        SelectionManager.Busy_Task(true, true);
        GWTClientService.createRowGroup(name, color, description, selectedRows,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Selection_Manager.connError();
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(DatasetInformation result) {
                        groupPanel.hide(true);
                        Selection_Manager.updateAllModules(result);

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
                       Selection_Manager.connError();
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(String datasetName) {
                        saveDsPanel.hide(true);
                        Selection_Manager.saveDataset(datasetName);
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
                        Selection_Manager.connError();
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(String datasetId) {
                        subDsPanel.hide(true);
                        Selection_Manager.updateDropdownList(datasetId);
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
     * @param description - group description
     */
    private void createColGroup(String name, String color, String description, int[] selection) {
        SelectionManager.Busy_Task(true, true);
        GWTClientService.createColGroup(name, color, description, selection,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Selection_Manager.connError();
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(DatasetInformation result) {
                        groupPanel.hide(true);
                        Selection_Manager.updateAllModules(result);
                    }
                });

    }

    /**
     * This method is responsible for cleaning on removing the component from
     * the container
     */
    public void remove() {
        omicsTable.remove();
        rowGroupTable.remove();
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
        Selection_Manager = null;
        GWTClientService = null;

        omicsTable = null;
        rowGroupTable = null;

    }

    //
//    private void initActiveGroupPanel(List<DivaGroup> rowGroupList) {
//        activeGroupPanel = new RowGroupPanel(rowGroupList, "Activate Row Group", SelectionStyle.SIMPLE);
//        activeGroupPanelReg = activeGroupPanel.getOkBtn().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
//
//            @Override
//            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
//                String[] gnames = activeGroupPanel.getSelectRowGroups();
//                if (gnames != null && gnames.length != 0) {
//                    activateGroups(gnames);
//                }
//            }
//        });
//
//    }
//    private void updateActiveGroupPanelPanel() {
//       
//        activeGroupPanel.updateData(datasetInfo.getRowGroupList());
//
//    }
}
