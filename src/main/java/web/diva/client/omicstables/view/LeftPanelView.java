/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.omicstables.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
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
import web.diva.client.view.core.Notification;
import web.diva.client.view.core.RowGroupPanel;
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
    private Label rowLab = null;
    private Label colLab = null;
    private Label rowGroup = null;
    private Label colGroup = null;
    private RowGroupPanel exportPanel = null, subDsPanel, activeGroupPanel = null;
    private DatasetInformation datasetInfo = null;
    private SelectionManager selectionManager = null;
    private DivaServiceAsync GWTClientService = null;
    private OmicsTableComponent omicsTable = null;
    private IButton   colGroupBtn,createRowGroupBtn;
    private GroupPanel groupPanel ;
    
    private SaveDatasetPanel saveDsPanel;
    private SectionStackSection rowSelectionSection;
    
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
 
        rowSelectionSection = new SectionStackSection("&nbsp;  Search And Selection");
       
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
 
        controlItem.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

            @Override
            public void onChange(com.smartgwt.client.widgets.form.fields.events.ChangeEvent event) {
                if (event.getValue().toString().equalsIgnoreCase(rowControl)) {

                    rowSelectionLayout.setVisible(true);
                    colSelectionLayout.setVisible(false);

                } else {
                    rowSelectionLayout.setVisible(false);
                    colSelectionLayout.setVisible(true);
                    colSelectionLayout.setWidth("100%");
                    colSelectionLayout.setHeight("2%");

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

            omicsTable = new OmicsTableComponent(selectionManager, datasetInfos, datasetInfos.getRowsNumb());
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
          final com.smartgwt.client.widgets.events.ClickHandler activateRowGroupHandler = new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (activeGroupPanel.getForm().validate()) {
                    activateGroups(activeGroupPanel.getSelectRowGroups());
                   }
            }
        };
        
        
        /* nice layout
        
        
        createRowGroupBtn = new IButton("Create Row Group");
        createRowGroupBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (groupPanel == null) {
                    groupPanel = new GroupPanel();
                    groupPanel.getOkBtn().addClickHandler(createRowGroupHandler);
                }
                updateAndShowGroupPanel("row");
            }
        });
         rowGBtnLayout.add(createRowGroupBtn);
         createRowGroupBtn.setAlign(Alignment.CENTER);
         rowSelectionLayout.setAlign(Alignment.CENTER);
         rowGBtnLayout.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
         rowGBtnLayout.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);

         createRowGroupBtn.setWidth("19%");
         createRowGroupBtn.setLeft(7);
         createRowGroupBtn.setMargin(0);
         rowSelectionLayout.setAlign(Alignment.CENTER);
        
         */
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
//                    try {
                    if (groupPanel == null) {
                        groupPanel = new GroupPanel();
                        groupPanel.getOkBtn().addClickHandler(createRowGroupHandler);
                    }
                    updateAndShowGroupPanel("row");
//                    } catch (Exception e) {
//                        SC.say("error " + e.getMessage());
//                    }

                }
                else{
                    if (activeGroupPanel == null) {
                        initActiveGroupPanel(datasetInfo.getRowGroupList());
                    }
                    activeGroupPanel.center();
                    activeGroupPanel.show();
                    

                }
            }
        });

        IMenuButton rowGroupBtn = new IMenuButton("Row Groups", rowGroupMenu);
        rowGroupBtn.setShowMenuButtonImage(false);

        rowGBtnLayout.add(rowGroupBtn);
        rowGroupBtn.setWidth("9%");
        rowGroupBtn.setAlign(Alignment.CENTER);

        rowGBtnLayout.setCellVerticalAlignment(rowGroupBtn, HorizontalPanel.ALIGN_MIDDLE);
        rowGBtnLayout.setCellHorizontalAlignment(rowGroupBtn, HorizontalPanel.ALIGN_CENTER);

        rowSelectionLayout.setAlign(Alignment.CENTER);
        rowGBtnLayout.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        rowGBtnLayout.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);

//         rowGroupBtn.setLeft(7);
        rowGroupBtn.setMargin(0);
        rowSelectionLayout.setAlign(Alignment.CENTER);

        MenuItem createSDMenuItem = new MenuItem("Create Sub-Dataset");
        MenuItem exportDatasetMenuItem = new MenuItem("Export Data");
        MenuItem saveDatasetMenuItem = new MenuItem("Save Current Dataset");

        Menu datasetMenu = new Menu();
        datasetMenu.setCanSelectParentItems(true);
        datasetMenu.setData(createSDMenuItem, exportDatasetMenuItem, saveDatasetMenuItem);
        datasetMenu.setWidth(100);

        datasetMenu.addItemClickHandler(new ItemClickHandler() {
            @Override
            public void onItemClick(ItemClickEvent event) {
                SC.say("You picked the \"" + event.getItem().getTitle()
                        + "\" department.");
            }
        });
  
        IMenuButton datasetMenuBtn = new IMenuButton("Dataset", datasetMenu);  
        datasetMenuBtn.setShowMenuButtonImage(false);
        
        rowGBtnLayout.add(datasetMenuBtn);
        datasetMenuBtn.setWidth("9%");  
         datasetMenuBtn.setAlign(Alignment.CENTER);
         
         
         rowGBtnLayout.setCellVerticalAlignment(datasetMenuBtn,HorizontalPanel.ALIGN_MIDDLE);
           rowGBtnLayout.setCellHorizontalAlignment(datasetMenuBtn,HorizontalPanel.ALIGN_CENTER);
        
           rowSelectionLayout.setAlign(Alignment.CENTER);
         rowGBtnLayout.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
         rowGBtnLayout.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);

         rowGBtnLayout.setSpacing(2);
//         datasetMenuBtn.setLeft(7);
         datasetMenuBtn.setMargin(0);
         rowSelectionLayout.setAlign(Alignment.CENTER);
        
        
        /* end of new  group and ds  layout*/
        GroupTable groupTable = new GroupTable(selectionManager);
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

        colGroupBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            
            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (groupPanel == null) {
                    groupPanel = new GroupPanel();
                    groupPanel.getOkBtn().addClickHandler(colGroupHandler);
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
        selectCols.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {
            
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
        exportPanel.getOkBtn().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            
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
        activeGroupPanel.getOkBtn().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            
            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                String[] gnames = activeGroupPanel.getSelectRowGroups();
                if (gnames != null) {
                    activateGroups(gnames);
                }
            }
        });
        
    }
    
    private void initSubDsPanel(List<DivaGroup> rowGroupList) {
        subDsPanel = new RowGroupPanel(rowGroupList, "Create Sub-Dataset", SelectionStyle.SINGLE);
        subDsPanel.getOkBtn().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            
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
        saveDsPanel.getOkBtn().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            
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
        dg.setColor("#ffffff");
        dg.setCount(selectionManager.getSelectedRows().getMembers().length);
        dg.setName("Current Selected Indexes");
        List<DivaGroup> list = new ArrayList<DivaGroup>();
        list.addAll(datasetInfo.getRowGroupList());
        list.remove(datasetInfo.getRowGroupList().get(0));
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
        selectionManager.busyTask(true,true);
        GWTClientService.exportData(rowGroup,
                new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        selectionManager.busyTask(false,true);
                    }
                    
                    @Override
                    public void onSuccess(String result) {
                        Window.open(result, "downlod window", "status=0,toolbar=0,menubar=0,location=0");
                       
                        selectionManager.busyTask(false,true);
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
     * @param type - group type (row)
     * @param selectedRows - selected rows indexes
     */
    private void createRowGroup(String name, String color, String type, int[] selectedRows) {
        selectionManager.busyTask(true,true);
        GWTClientService.createRowGroup(name, color, type, selectedRows,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        selectionManager.busyTask(false,true);
                        
                    }
                    
                    @Override
                    public void onSuccess(DatasetInformation result) {
                        selectionManager.busyTask(false,true);
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
        selectionManager.busyTask(true,true);
        GWTClientService.saveDataset(name,
                new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        selectionManager.busyTask(false,true);                        
                    }
                    
                    @Override
                    public void onSuccess(Integer datasetId) {
                        selectionManager.busyTask(false,true);
                        saveDsPanel.hide(true);
                        selectionManager.loadDataset(datasetId);
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
        selectionManager.busyTask(true,true);
        GWTClientService.createSubDataset(name, type, selectedRows,
                new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        selectionManager.busyTask(false,true);
                    }
                    
                    @Override
                    public void onSuccess(Integer datasetId) {
                        selectionManager.busyTask(false,true);
                        subDsPanel.hide(true);
                        Notification.notifi("Successfully Stored Sub-Dataset");
                        selectionManager.updateAllModules(datasetInfo);
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
        selectionManager.busyTask(true,true);
        GWTClientService.createColGroup(name, color, type, selection,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        selectionManager.busyTask(false,true);
                    }
                    
                    @Override
                    public void onSuccess(DatasetInformation result) {
                        selectionManager.busyTask(false,true);
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

        selectionManager.busyTask(true,true);
        GWTClientService.activateGroups(rowGroups,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        selectionManager.busyTask(false,true);
                    }

                    @Override
                    public void onSuccess(DatasetInformation result) {
                        activeGroupPanel.hide(true);
                        selectionManager.busyTask(false,true);
                        selectionManager.updateAllModules(result);
                        result = null;

                    }
                });

    }

}
