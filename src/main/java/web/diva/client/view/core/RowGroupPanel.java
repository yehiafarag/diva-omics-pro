/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellSelectionChangedEvent;
import com.smartgwt.client.widgets.grid.events.CellSelectionChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.List;
import web.diva.shared.beans.DivaGroup;

/**
 *
 * @author Yehia Farag
 */
public class RowGroupPanel extends PopupPanel{
    
      private final IButton okBtn;
    private HTML errorlabl;
    private ListGrid selectionTable;    
    private final VLayout mainBodyLayout;
    private DynamicForm form;
     private TextItem name;

    public DynamicForm getForm() {
        return form;
    }

    public String getName() {
        return name.getValueAsString();
    }
    
    public RowGroupPanel(List<DivaGroup> rowGroupsList,String type,SelectionStyle selectionStyle){
        
        this.setAnimationEnabled(true);
        this.ensureDebugId("cwBasicPopup-imagePopup");
        this.setModal(false);
        this.setWidth(400 + "px");
        this.setHeight(256+ "px");
        mainBodyLayout = new VLayout();
        
        mainBodyLayout.setWidth("100%");
        mainBodyLayout.setHeight("100%");
        this.add(mainBodyLayout);

        HLayout topLayout = new HLayout();
        topLayout.setMembersMargin(1);
        topLayout.setWidth(400);
        topLayout.setHeight(20);
        mainBodyLayout.addMember(topLayout);
        Label title = new Label(type);
        title.setStyleName("labelheader");
        topLayout.addMember(title);
        title.setWidth(384 + "px");

        Label closeBtn = new Label();

        closeBtn.addStyleName("close");
        closeBtn.setHeight("16px");
        closeBtn.setWidth("16px");

        closeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });

        topLayout.addMember(closeBtn);
        mainBodyLayout.setMargin(5);
        try {
            mainBodyLayout.setMembersMargin(5);

            selectionTable = new ListGrid();
            initSelectionTable(selectionStyle);
            mainBodyLayout.addMember(selectionTable);
            this.updateData(rowGroupsList);
            selectionTable.addCellSelectionChangedHandler(new CellSelectionChangedHandler() {

                @Override
                public void onCellSelectionChanged(CellSelectionChangedEvent event) {

                    ListGridRecord[] records = selectionTable.getRecords();
                    int counter = 0;
                    for (ListGridRecord record : records) {
                        if (Boolean.valueOf(record.getAttribute("selection")) == true) {
                            counter++;
                            if (counter > 1) {
                                event.cancel();
                                errorlabl.setVisible(true);

                            }
                        }
                    }
                }

            });


        } catch (Exception e) {
            Window.alert("error is ");
        }

        if (type.equalsIgnoreCase("Create Sub-Dataset")) {
            form = new DynamicForm();
            form.setGroupTitle("");
            form.setWidth(400);
            form.setLayoutAlign(Alignment.CENTER);

            name = new TextItem();
            name.setTitle("Sub-DS Name");
            name.setRequired(true);
            name.setWidth(200);
            form.setFields(name);
            form.redraw();
            mainBodyLayout.addMember(form);

        }

        HLayout hlo = new HLayout();
        hlo.setWidth("100%");
        hlo.setHeight("10%");
        okBtn = new IButton(type);
        okBtn.setWidth(200);
        hlo.addMember(okBtn);

        hlo.setMargin(10);
        hlo.setMembersMargin(20);
        hlo.setAlign(Alignment.CENTER);
        mainBodyLayout.addMember(hlo);

        errorlabl = new HTML("<h4 style='color:red;margin-left: 20px;height=20px;'>PLEASE CHECK YOUR DATA INPUT ..</h4>");
        errorlabl.setVisible(false);
        errorlabl.setHeight("15%");
        errorlabl.setWidth("100%");
        mainBodyLayout.addMember(errorlabl);
        rowGroupsList = null;
        mainBodyLayout.redraw();
        this.show();
        this.hide();

        
        
    }
     public final void updateData(List<DivaGroup> rowGroupsList) {
        if (errorlabl != null) {
            errorlabl.setVisible(false);
        }        
        updateRecors(rowGroupsList);
    }

    public String getSelectRowGroup() {
        ListGridRecord[] records = selectionTable.getSelectedRecords();       
        return records[0].getAttribute("groupName");

    }
     public String[] getSelectRowGroups() {
        ListGridRecord[] records = selectionTable.getSelectedRecords(); 
        String[] rowGroups = new String[records.length];
        for(int x=0;x<rowGroups.length;x++)
            rowGroups[x]=records[x].getAttribute("groupName");
            
        return rowGroups;

    }


   
    public IButton getOkBtn() {
        return okBtn;
    }

    private void initSelectionTable(SelectionStyle selectionStyle) {

        selectionTable.setWidth100();
        selectionTable.setHeight(200);
        selectionTable.setShowHeaderContextMenu(false);
        selectionTable.setLeaveScrollbarGap(false);
        selectionTable.setSelectionType(selectionStyle);

        ListGridField groupNameField = new ListGridField("groupName", "Group Name", 50);
        groupNameField.setAlign(Alignment.CENTER);
        groupNameField.setType(ListGridFieldType.TEXT);
        groupNameField.setAutoFitWidth(Boolean.TRUE);

        ListGridField colorField = new ListGridField("color", "Color");
        colorField.setAlign(Alignment.CENTER);
        colorField.setType(ListGridFieldType.IMAGE);
        colorField.setCanEdit(false);

        ListGridField countField = new ListGridField("count", "Count");
        countField.setAlign(Alignment.CENTER);
        countField.setType(ListGridFieldType.INTEGER);
        selectionTable.setFields(new ListGridField[]{ groupNameField, colorField, countField});
        selectionTable.selectSingleRecord(0);
        

    }

    private void updateRecors(List<DivaGroup> rowGroupsList) {
        ListGridRecord[] records = new ListGridRecord[rowGroupsList.size()];
        int index = 0;
        for (DivaGroup rGr : rowGroupsList) {
            ListGridRecord record = new ListGridRecord();
            record.setAttribute("groupName", rGr.getName());
            record.setAttribute("color", rGr.getColor());
            record.setAttribute("count", rGr.getCount());
            records[index++] = record;

        }

        selectionTable.setData(records);
    }


    
}
