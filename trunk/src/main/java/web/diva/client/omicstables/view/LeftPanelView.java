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
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import web.diva.client.GreetingServiceAsync;
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
    
//    private ListGrid selectionTable = null;
    private SelectItem colSelectionTable = null;
    private Label rowLab = null;
    private Label colLab = null;
    private Label rowGroup = null;
    private Label colGroup = null;
//    private Label exportBtn = null, publishBtn = null;//, spacer = null;
    private RowGroupPanel exportPanel = null, subDsPanel, activeGroupPanel = null;
    private DatasetInformation datasetInfo = null;
    private SelectionManager selectionManager = null;
    private GreetingServiceAsync GWTClientService = null;
    private OmicsTableComponent omicsTable = null;
    private final IButton   colGroupBtn,createRowGroupBtn;
//     rowGroupResultSectionBtn, colGroupResultSectionBtn, , activateRowGroupsBtncolGroupBtn,publishDsBtn, exportDsBtn, subDsBtn
    private GroupPanel groupPanel;
    
    private SaveDatasetPanel saveDsPanel;
    
//    private VLayout rowSelectionTableLayout = null;//, omicsTableLayout = null
    
    public LeftPanelView(SelectionManager selectionManagers, GreetingServiceAsync GWTClientService, DatasetInformation datasetInfos) {

        this.setVisibilityMode(VisibilityMode.MULTIPLE);
        this.selectionManager = selectionManagers;
        this.GWTClientService = GWTClientService;
        this.datasetInfo = datasetInfos;

        this.setBorder("1px solid #E6E6E6");
        this.setWidth("20%");
        this.setHeight("89%");
        this.setMargin(2);
        this.setScrollSectionIntoView(false);
        final SectionStackSection rowSelectionSection = new SectionStackSection("&nbsp;  Search And Selection");
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
//               controlItem.setValueMap(controls);
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
        rowGBtnLayout.setHeight(30+"px");
        rowGBtnLayout.setWidth(20+"%");
//        rowGBtnLayout.setStyleName("blackLayout");
//        Label spacer2 = new Label();
//        spacer2.setWidth(23 + "px");
//        rowGBtnLayout.addMember(spacer2);
        
         final com.smartgwt.client.widgets.events.ClickHandler rowGroupHandler = new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Selection sel = selectionManager.getSelectedRows();
                if (groupPanel.getForm().validate()) {
                    createRowGroup(groupPanel.getName(), groupPanel.getColor(), groupPanel.getDescription(), sel.getMembers());
                }
            }
        };
        createRowGroupBtn = new IButton("Create Row Group");
        createRowGroupBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                if (groupPanel == null) {
                    groupPanel = new GroupPanel();
                    groupPanel.getOkBtn().addClickHandler(rowGroupHandler);
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
//        rowGBtnLayout.setMembersMargin(2);
        rowSelectionLayout.setAlign(Alignment.CENTER);

        GroupTable groupTable = new GroupTable(selectionManager);
        groupTable.updateRecords(datasetInfo.getRowGroupList());
        rowSelectionLayout.addMember(groupTable);

        omicsTable.setGroupTable(groupTable);
        this.expandSection(rowSelectionSection.getID());
        this.redraw();

        
        
//        this.setScrollbarSize(5);
//        this.addSectionHeaderClickHandler(LeftPanelView.this);
//        
//        final SectionStackSection datasetInfoSection = new SectionStackSection("Dataset Information");
//        datasetInfoSection.setCanCollapse(false);
        
       
//        final SectionStackSection colSelectionSection = new SectionStackSection("Column Selections");
//        colSelectionSection.setCanCollapse(false);
//        final SectionStackSection rowSelectionSection = new SectionStackSection("Row Selections");
//        final SectionStackSection omicsInfoTableSection = new SectionStackSection("Search ");
//        omicsInfoTableSection.setCanCollapse(false);

//        this.addSection(datasetInfoSection);
//        datasetInfoSection.setExpanded(true);
        
//        this.addSection(colSelectionSection);
//        this.addSection(rowSelectionSection);
//        this.addSection(omicsInfoTableSection);
//        VLayout datasetMangmentLayout = new VLayout();
//        datasetMangmentLayout.setHeight("70px");
//        datasetMangmentLayout.setWidth(width);
//        datasetMangmentLayout.setMargin(5);
//        
//        rowLab = new Label("# Rows:    " + datasetInfos.getRowsNumb());
//        rowLab.setHeight("20px");
//        rowLab.setStyleName("datasetInformation");
//        colLab = new Label("# Columns: " + datasetInfos.getColNumb());
//        colLab.setHeight("20px");
//        colLab.setStyleName("datasetInformation");
//        rowGroup = new Label("# Row Groups:    " + (datasetInfos.getRowGroupsNumb()));
//        rowGroup.setHeight("20px");
//        rowGroup.setWidth("120px");
//        rowGroup.setStyleName("datasetInformation");
//        colGroup = new Label("# Column Groups: " + (datasetInfos.getColGroupsNumb()));
//        colGroup.setHeight("20px");
//        colGroup.setWidth("120px");
//        colGroup.setStyleName("datasetInformation");
//        
//        HLayout topDatasetInformationLayout = new HLayout();
//        topDatasetInformationLayout.setMembersMargin(2);
//        topDatasetInformationLayout.setWidth(width);
//        
//        topDatasetInformationLayout.addMember(rowLab);
//        topDatasetInformationLayout.addMember(rowGroup);
//        
//         HLayout BottomDatasetInformationLayout = new HLayout();
//        BottomDatasetInformationLayout.setMembersMargin(2);
//        BottomDatasetInformationLayout.setWidth(width);
//        
//        BottomDatasetInformationLayout.addMember(colLab);
//        BottomDatasetInformationLayout.addMember(colGroup);
//         datasetMangmentLayout.addMember(topDatasetInformationLayout);
//          datasetMangmentLayout.addMember(BottomDatasetInformationLayout);
//        datasetInfoSection.addItem(datasetMangmentLayout);
//        
//        VLayout btnsLayout = new VLayout();
//        btnsLayout.setMembersMargin(3);
//        btnsLayout.setHeight("20px");
//        btnsLayout.setWidth(width);
//        datasetMangmentLayout.addMember(btnsLayout);
////        btnsLayout.setAlign(VerticalAlignment.CENTER);
//        
//       
        final com.smartgwt.client.widgets.events.ClickHandler colGroupHandler = new com.smartgwt.client.widgets.events.ClickHandler() {            
            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Selection sel = selectionManager.getSelectedColumns();
                if (groupPanel.getForm().validate()) {
                    createColGroup(groupPanel.getName(), groupPanel.getColor(), groupPanel.getDescription(), sel.getMembers());
                }
            }
        };
//        
//        
//        HLayout topControlBtnsLayout = new HLayout();
//        topControlBtnsLayout.setHeight(25);
//        topControlBtnsLayout.setWidth(width);
//        btnsLayout.addMember(topControlBtnsLayout);
//        topControlBtnsLayout.setMembersMargin(2);
//        topControlBtnsLayout.setAlign(Alignment.LEFT);
////        topControlBtnsLayout.setAlign(VerticalAlignment.CENTER);
//        
//        exportDsBtn = new IButton("Export");
//        exportDsBtn.setAutoFit(true);
//        exportDsBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
//            
//            @Override
//            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
//                if (exportPanel == null) {
//                    initExportPanel(datasetInfo.getRowGroupList());
//                }
//                updateExportPanel();
//                exportPanel.show();
//                exportPanel.center();
//            }
//        });
//        topControlBtnsLayout.addMember(exportDsBtn);
//        
//        subDsBtn = new IButton("Sub Dataset");
//        subDsBtn.setAutoFit(true);
//        subDsBtn.setTooltip("Create Sub Dataset");
//        subDsBtn.addClickHandler(new ClickHandler() {
//            
//            @Override
//            public void onClick(ClickEvent event) {
//                if (subDsPanel == null) {
//                    initSubDsPanel(datasetInfo.getRowGroupList());
//                }
//                updateSubDsPanel();
//                subDsPanel.show();
//                subDsPanel.center();
//            }
//            
//        });
//        topControlBtnsLayout.addMember(subDsBtn);
//        
//        publishDsBtn = new IButton("Save");
//        publishDsBtn.setAutoFit(true);
//        publishDsBtn.setTooltip("Share The Dataset With Other Users ");
//        publishDsBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
//            
//            @Override
//            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
//                if (saveDsPanel == null) {
////                    initSaveDsPanel();
//                }
//                saveDsPanel.show();
//                saveDsPanel.center();
//            }
//        });
//
//        topControlBtnsLayout.addMember(publishDsBtn);

        //end of dataset information layout
        
        
        
        
        
        
        
       
       
//         HLayout rowGBtnLayout = new HLayout();
//        rowSelectionLayout.addMember(rowGBtnLayout);
//        rowSelectionLayout.setMembersMargin(5);
//        rowGBtnLayout.setHeight(30);
//        rowGBtnLayout.setWidth(width);
//        Label spacer2 = new Label();
//        spacer2.setWidth(23 + "px");
//        rowGBtnLayout.addMember(spacer2);        
//        createRowGroupBtn = new IButton("Create Row Group");        
//        createRowGroupBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
//            @Override
//            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
//                if (groupPanel == null) {
//                    groupPanel = new GroupPanel();
//                    groupPanel.getOkBtn().addClickHandler(rowGroupHandler);
//                }
//                updateAndShowGroupPanel("row");
//            }
//        });
//        
//        rowGBtnLayout.addMember(createRowGroupBtn);
//        createRowGroupBtn.setWidth((width - 50) + "px");
//        createRowGroupBtn.setAlign(Alignment.CENTER);
        
        
        
       
        
//         VLayout groupMangmentLayout = new VLayout();
//         rowSelectionSection.addItem(groupMangmentLayout);
//        groupMangmentLayout.setHeight("120px");
//        groupMangmentLayout.setWidth(width);
//        groupMangmentLayout.setMembersMargin(2);
        
//        GroupTable groupTable = new GroupTable(selectionManager);
//        groupTable.updateRecords(datasetInfo.getRowGroupList());
//        groupMangmentLayout.addMember(groupTable);
          
//        HLayout bottomControlBtnsLayout = new HLayout();
//        bottomControlBtnsLayout.setHeight(25);
//        bottomControlBtnsLayout.setWidth(width);
//        groupMangmentLayout.addMember(bottomControlBtnsLayout);
//        bottomControlBtnsLayout.setMembersMargin(2);
//        bottomControlBtnsLayout.setMargin(5);
//        bottomControlBtnsLayout.setAlign(Alignment.LEFT);
//        
//        rowGroupResultSectionBtn = new IButton("Row Group");
//        rowGroupResultSectionBtn.setTooltip("Create Row Group");
//        rowGroupResultSectionBtn.setAutoFit(true);
//        rowGroupResultSectionBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
//            
//            @Override
//            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
////                rowSelectionSection.setExpanded(true);
//                if (groupPanel == null) {
//                    groupPanel = new GroupPanel();
//                    groupPanel.getOkBtn().addClickHandler(rowGroupHandler);
//                }
//                updateAndShowGroupPanel("row");
//            }
//        });
//        bottomControlBtnsLayout.addMember(rowGroupResultSectionBtn);
        
//        colGroupResultSectionBtn = new IButton("Column Group");
//        colGroupResultSectionBtn.setTooltip("Create Column Group");
//        colGroupResultSectionBtn.setAutoFit(true);
//        colGroupResultSectionBtn.addClickHandler(new ClickHandler() {
//            
//            @Override
//            public void onClick(ClickEvent event) {
//                colSelectionSection.setExpanded(true);
//                if (groupPanel == null) {
//                    groupPanel = new GroupPanel();
//                    groupPanel.getOkBtn().addClickHandler(colGroupHandler);
//                }
//                updateAndShowGroupPanel("col");
//            }
//        });
//        bottomControlBtnsLayout.addMember(colGroupResultSectionBtn);
//        activateRowGroupsBtn = new IButton("Activate Group");
//        activateRowGroupsBtn.setTooltip("Activate/deactivate  Row Groups");
//        activateRowGroupsBtn.setAutoFit(true);
//        activateRowGroupsBtn.addClickHandler(new ClickHandler() {
//            
//            @Override
//            public void onClick(ClickEvent event) {
//                if (activeGroupPanel == null) {
//                    initActiveGroupPanel(datasetInfo.getRowGroupList());
//                }
//                updateActiveGroupPanelPanel();
//                activeGroupPanel.show();
//                activeGroupPanel.center();
//                
//            }
//        });
//        bottomControlBtnsLayout.addMember(activateRowGroupsBtn);

//        rowSelectionSection.setExpanded(false);
        
//        VLayout rowSelectionLayout = new VLayout();
//        rowSelectionLayout.setWidth(width);
//        rowSelectionLayout.setHeight(120);
//        rowSelectionSection.addItem(rowSelectionLayout);
        
//        selectionTable = initRowSelectionTable(datasetInfos);
        
//        rowSelectionTableLayout = new VLayout();
//        rowSelectionLayout.addMember(rowSelectionTableLayout);
//        rowSelectionTableLayout.addMember(selectionTable);
        
//        HLayout rowGBtnLayout = new HLayout();
//        rowSelectionLayout.addMember(rowGBtnLayout);
//        rowSelectionLayout.setMembersMargin(5);
//        rowGBtnLayout.setHeight(30);
//        rowGBtnLayout.setWidth(width);
//        Label spacer2 = new Label();
//        spacer2.setWidth(23 + "px");
//        rowGBtnLayout.addMember(spacer2);        
//        createRowGroupBtn = new IButton("Create Row Group");        
//        createRowGroupBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
//            @Override
//            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
//                if (groupPanel == null) {
//                    groupPanel = new GroupPanel();
//                    groupPanel.getOkBtn().addClickHandler(rowGroupHandler);
//                }
//                updateAndShowGroupPanel("row");
//            }
//        });
//        
//        rowGBtnLayout.addMember(createRowGroupBtn);
//        createRowGroupBtn.setWidth((width - 50) + "px");
//        createRowGroupBtn.setAlign(Alignment.CENTER);
//        rowSelectionLayout.setAlign(Alignment.CENTER);
//        rowGBtnLayout.setMembersMargin(2);
        

//start column section 
        
//        colSelectionSection.setExpanded(false);
        colSelectionTable = initColSelectionTable(datasetInfos.getColNamesMap());
        colSelectionTable.setHeight((datasetInfos.getColNamesMap().size()*20));
     
     
//        VLayout columnSelectionLayout = new VLayout();
//        colSelectionLayout.addMember(columnSelectionLayout);
//        columnSelectionLayout.setWidth("100%");
//        columnSelectionLayout.setHeight("60%");
        
        DynamicForm columnForm = new DynamicForm(); 
       colSelectionLayout.setMembers(columnForm);
        columnForm.setItems(colSelectionTable);
        columnForm.setAlwaysShowScrollbars(false);
        columnForm.setWidth("100%");
        columnForm.setHeight((datasetInfos.getColNamesMap().size()*20));
        columnForm.setStyleName("borderless");
        columnForm.setShowEdges(false);
       columnForm.setScrollbarSize(5);
        
         omicsTable.setColSelectionTable(colSelectionTable);
        
        gBtnLayout = new HLayout();
        colSelectionLayout.addMember(gBtnLayout);
        gBtnLayout.setHeight(30);
        
//        Label spacer1 = new Label();
//        spacer1.setWidth((23) + "px");
//        gBtnLayout.addMember(spacer1);
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
        
        
        
//        rowSelectionSection.setExpanded(false);
        
      
//        rowGBtnLayout.setMembersMargin(2);
        
        
        
        
//        omicsTable.setSelectionTable(selectionTable);
//       datasetInfoSection.setExpanded(true);
//       this.expandSection(datasetInfoSection.getID());
       
//       this.expandSection(colSelectionSection.getID());
//       this.expandSection(omicsInfoTableSection.getID());
//       omicsTable.getOmicsTableLayout().scrollToBottom();       
        
        
    }
    private   HLayout gBtnLayout;
    
    private void updateAndShowGroupPanel(String type) {
        if (type.equalsIgnoreCase("col")) {
            
            groupPanel.setCount(selectionManager.getSelectedColumns().getMembers().length);
            
        } else if (type.equalsIgnoreCase("row")) {
            groupPanel.setCount(selectionManager.getSelectedRows().getMembers().length);
            
        }
        groupPanel.show();
        groupPanel.center();
        
    }

    /**
     * This method is responsible for initializing row sel table
     *
     * @param datasetInfo - dataset information
     */
//    private ListGrid initRowSelectionTable(DatasetInformation datasetInfo) {
//        OmicsTable tempRowSelectionTable = new OmicsTable(datasetInfo);
//        tempRowSelectionTable.setSelectionType(SelectionStyle.NONE);
//        tempRowSelectionTable.setLeaveScrollbarGap(false);
//        tempRowSelectionTable.setCanDragSelect(false);
//        tempRowSelectionTable.addViewStateChangedHandler(new ViewStateChangedHandler() {
//            
//            @Override
//            public void onViewStateChanged(ViewStateChangedEvent event) {
//                createRowGroupBtn.enable();
//            }
//        });
//        return tempRowSelectionTable;
//    }

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
    
//    public ListGrid getSelectionTable() {
//        return selectionTable;
//    }
    
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
        activeGroupPanel = new RowGroupPanel(rowGroupList, "Activate Row Group", SelectionStyle.MULTIPLE);
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
    
//    @Override
//    public void onSectionHeaderClick(SectionHeaderClickEvent event) {
//        if (event.getSection().getTitle().equalsIgnoreCase("Full Row IDs ")) {
////            omicsTable.getOmicsTableLayout().scrollToTop();
//            Selection s = selectionManager.getSelectedRows();
//            selectionManager.setSelectedRows(s);
//        }
//    }

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
