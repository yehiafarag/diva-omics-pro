/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.rank.view;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.ArrayList;
import java.util.List;
import web.diva.shared.beans.DivaGroup;

/**
 *
 * @author Yehia Farag pup-up window for rank panel user inputs
 */
public final class RankPanel extends PopupPanel {

//    private final SelectItem selectColGroups;
    private RadioGroupItem radioGroupItem;
    private TextItem permutation, seed;
    private final IButton okBtn;
    private HTML errorlabl;
    private DynamicForm form2;
    private ListGrid selectionTable;

    public DynamicForm getForm2() {
        return form2;
    }

    public HTML getErrorlabl() {
        return errorlabl;
    }
    private final VLayout mainBodyLayout;

    public RankPanel() {
        this.setAnimationEnabled(true);
        this.ensureDebugId("cwBasicPopup-imagePopup");
        this.setModal(true);

//        this.setWidth(400 + "px");
//        this.setHeight(300 + "px");
        mainBodyLayout = new VLayout();
        
        mainBodyLayout.setWidth(400);
        mainBodyLayout.setHeight(300);
       

        HLayout topLayout = new HLayout();
        topLayout.setMembersMargin(1);
        topLayout.setWidth(400);
        topLayout.setHeight(20);
        mainBodyLayout.addMember(topLayout);
        Label title = new Label("Rank Product (Differential Expression)");
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

            Label tableTitle = new Label("COLUMN GROUPS (MAX 2)");
            tableTitle.setHeight("20px");
            mainBodyLayout.addMember(tableTitle);
            selectionTable = new ListGrid();
            initSelectionTable();
            mainBodyLayout.addMember(selectionTable);
//            this.updateData(colGroupsList);
//            selectionTable.addCellSelectionChangedHandler(new CellSelectionChangedHandler() {
//
//                @Override
//                public void onCellSelectionChanged(CellSelectionChangedEvent event) {
//
//                    ListGridRecord[] records = selectionTable.getRecords();
//                    int counter = 0;
//                    for (ListGridRecord record : records) {
//                        if (Boolean.valueOf(record.getAttribute("selection")) == true) {
//                            counter++;
//                            if (counter > 1) {
//                                event.cancel();
//                                errorlabl.setVisible(true);
//
//                            }
//                        }
//                    }
//                }
//
//            });

            DynamicForm form = new DynamicForm();
            form.setIsGroup(true);
            form.setWidth("100%");
            form.setMargin(5);
            form.setGroupTitle("Values");
            form.setHeight("25%");
            form.setWidth("100%");

            radioGroupItem = new RadioGroupItem();
            radioGroupItem.setHeight("20%");
            radioGroupItem.setWidth("100%");
            radioGroupItem.setTitle("");
            radioGroupItem.setValueMap("Log 2", "Linear");
            radioGroupItem.setValue("Log 2");
            radioGroupItem.setShouldSaveValue(true);
            form.setFields(radioGroupItem);
            form.redraw();
            mainBodyLayout.addMember(form);

            form2 = new DynamicForm();
            form2.setGroupTitle("Permutations");
            form2.setIsGroup(true);
            form2.setHeight("25%");
            form2.setWidth("100%");
            form2.setMargin(5);
            form2.setPadding(1);
            permutation = new TextItem();
            permutation.setTitle("Permutation");
            permutation.setBrowserInputType("digits");
            permutation.setRequired(true);

            permutation.setValidators(new IsIntegerValidator());
            permutation.setValue(400);

            seed = new TextItem();
            seed.setTitle("Seed");
            seed.setRequired(true);
            seed.setBrowserInputType("digits");
            seed.disable();
            seed.setValue(Random.nextInt(1000000001));

            ButtonItem btn = new ButtonItem();
            btn.setShowTitle(true);
            btn.setTitle("New seed");
            btn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                @Override
                public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                    seed.setValue(Random.nextInt(1000000001));
                }
            });


            form2.setFields(permutation, seed, btn);
            

            form2.redraw();
            mainBodyLayout.addMember(form2);

        } catch (Exception e) {
            Window.alert("error is ");
        }

        HLayout hlo = new HLayout();
        hlo.setWidth("100%");
        hlo.setHeight("10%");

//        IButton newSeedBtn = new IButton("Create new seed");
//        hlo.addMember(newSeedBtn);
        okBtn = new IButton("Process");
        hlo.addMember(okBtn);

        hlo.setMargin(10);
        hlo.setMembersMargin(20);
        hlo.setAlign(Alignment.CENTER);
        mainBodyLayout.addMember(hlo);

        errorlabl = new HTML("<h4 style='color:red;margin-left: 20px;height=20px;'>PLEASE CHECK YOUR DATA INPUT .. PLEASE NOTE THAT YOU CAN NOT SELECT MORE THAN 2 GROUPS</h4>");
        errorlabl.setVisible(false);
        errorlabl.setHeight("15%");
        errorlabl.setWidth("100%");
        mainBodyLayout.addMember(errorlabl);
//        colGroupsList = null;
        mainBodyLayout.redraw();
         this.add(mainBodyLayout);
        mainBodyLayout.setStyleName("modalLayout");
        this.show();
        this.hide();

    }

    public void updateData(List<DivaGroup> colGroupsList) {
        if (errorlabl != null) {
            errorlabl.setVisible(false);
        }
        if (form2 != null) {
            form2.clearErrors(true);
            form2.redraw();
        }
        updateRecords(colGroupsList);

    }

    public List<String> getSelectColGroups() {
       
      
//        for (int x=0;x<rownumber;x++) {
//            Record record= selectionTable.getRecordList().get(x);
//            Window.alert(record.getAttributeAsBoolean("selection")+"");
//            if (record.getAttributeAsBoolean("selection") == true) {                
//            
//            selectionList.add(record.getAttribute("groupName"));
//            }
//        }
        return selectionList;

    }

    private  List<String> selectionList = new ArrayList<String>();
    public String getRadioGroupItem() {
        return radioGroupItem.getValueAsString();
    }

    public String getPermutation() {
        return permutation.getValueAsString();
    }

    public String getSeed() {
        return seed.getValueAsString();
    }

    public IButton getOkBtn() {
        return okBtn;
    }

    private void initSelectionTable() {

        selectionTable.setWidth100();
        selectionTable.setHeight(100);
        selectionTable.setLeaveScrollbarGap(false);
        selectionTable.setShowHeaderContextMenu(false);
        ListGridField selectField = new ListGridField("Selection", "Selection");
        selectField.setType(ListGridFieldType.BOOLEAN);
        selectField.setCanEdit(true);
//        selectField.setAutoFitWidth(true);

        ListGridField groupNameField = new ListGridField("groupName", "Group Name", 50);
        groupNameField.setAlign(Alignment.CENTER);
        groupNameField.setType(ListGridFieldType.TEXT);
        groupNameField.setWidth(107);
        ListGridField colorField = new ListGridField("color", "Color");
        colorField.setAlign(Alignment.CENTER);
        colorField.setType(ListGridFieldType.IMAGE);
        colorField.setCanEdit(false);

        ListGridField countField = new ListGridField("count", "Count");
        countField.setAlign(Alignment.CENTER);
        countField.setType(ListGridFieldType.INTEGER);
        selectionTable.setFields(new ListGridField[]{selectField, groupNameField, colorField, countField});
        selectionTable.addCellClickHandler(new CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
               
//               boolean test =  event.getRecord().getAttributeAsBoolean("Selection");
//               Window.alert("test is "+test+"  gname is : "+event.getRecord().getAttributeAsString("groupName"));
               
            }
        });
        selectionTable.addEditCompleteHandler(new EditCompleteHandler() {

            @Override
            public void onEditComplete(EditCompleteEvent event) {
                 boolean test =  event.getNewValuesAsRecord().getAttributeAsBoolean("Selection");
                  
                  if(selectionList.contains(event.getOldValues().getAttributeAsString("groupName"))){
                      if(!test){
                          selectionList.remove(event.getOldValues().getAttributeAsString("groupName"));                         
                      }
                  }else if(test){
                      selectionList.add(event.getOldValues().getAttributeAsString("groupName"));
                  }
            }
        });
      

    }

    private void updateRecords(List<DivaGroup> colGroupsList) {
       selectionList.clear();
        ListGridRecord[] records = new ListGridRecord[colGroupsList.size()];
        int index = 0;
        for (DivaGroup cgr : colGroupsList) {
            ListGridRecord record = new ListGridRecord();
            record.setAttribute("selection", false);
           
            record.setAttribute("groupName", cgr.getName().toUpperCase());
            record.setAttribute("color", cgr.getColor());
            record.setAttribute("count", cgr.getCount());
            records[index++] = record;

        }

        selectionTable.setData(records);
    }

}
