/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.rank.view;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellSelectionChangedEvent;
import com.smartgwt.client.widgets.grid.events.CellSelectionChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.ArrayList;
import java.util.List;
import web.diva.shared.beans.ColumnGroup;

/**
 *
 * @author Yehia Farag
 * pup-up window for rank panel user inputs
 */
public final class RankPanel extends Window {

//    private final SelectItem selectColGroups;
    private final RadioGroupItem radioGroupItem;
    private final TextItem permutation, seed;
    private final IButton okBtn;
    private final HTML errorlabl;
    private final DynamicForm form2;
    private ListGrid selectionTable;

    public DynamicForm getForm2() {
        return form2;
    }

    public HTML getErrorlabl() {
        return errorlabl;
    }

    public RankPanel(List<ColumnGroup> colGroupsList) {
        this.setWidth(400);
        this.setHeight(350);
        this.setTitle("Differential Expression");
        this.setShowMinimizeButton(false);
        this.setIsModal(false);
        this.centerInPage();
        this.addCloseClickHandler(new CloseClickHandler() {
            @Override
            public void onCloseClick(CloseClickEvent event) {
                hide();
            }
        });

        VLayout mainBodyLayout = new VLayout();
        mainBodyLayout.setWidth("100%");
        mainBodyLayout.setHeight("100%");
        this.addItem(mainBodyLayout);

        DynamicForm form = new DynamicForm();
        form.setIsGroup(true);
        form.setWidth("100%");
        form.setPadding(5);
        
        selectionTable = new ListGrid();
        initSelectionTable();
        mainBodyLayout.addMember(selectionTable);
//        selectColGroups = new SelectItem();
//        selectColGroups.setTitle("COLUMN GROUPS (MAX 2)");
//        selectColGroups.setTitleOrientation(TitleOrientation.TOP);
//        selectColGroups.setTextAlign(Alignment.CENTER);
//        selectColGroups.setTitleAlign(Alignment.CENTER);
//        selectColGroups.setMultiple(true);
//        selectColGroups.setMultipleAppearance(MultipleAppearance.GRID);
//        selectColGroups.setWidth("100%");
//        selectColGroups.setHeight("60%");
        this.updateData(colGroupsList);
        selectionTable.addCellSelectionChangedHandler(new CellSelectionChangedHandler() {

            @Override
            public void onCellSelectionChanged(CellSelectionChangedEvent event) {

                ListGridRecord[] records = selectionTable.getRecords();
                int counter = 0;
                for (ListGridRecord record : records) {
                    if (Boolean.valueOf(record.getAttribute("selection")) == true) {
                        counter++;
                        if (counter > 1)
                        {
                            event.cancel();
                            errorlabl.setVisible(true);
                            
                        }
                    }
                }
            }

        });

        radioGroupItem = new RadioGroupItem();
        radioGroupItem.setHeight("20%");
        radioGroupItem.setWidth("100%");
        radioGroupItem.setTitle("");
        radioGroupItem.setValueMap("Log 2", "Linear");
        radioGroupItem.setValue("Log 2");
        radioGroupItem.setShouldSaveValue(true);

        form.setGroupTitle("Values");
        form.setHeight("25%");
        form.setWidth("100%");
        form.setGroupBorderCSS("color:gray;");
        
        form2 = new DynamicForm();
        form2.setGroupBorderCSS("color:gray;");
        form2.setGroupTitle("Permutations");
        form2.setIsGroup(true);
        form2.setHeight("25%");
        form2.setWidth("100%");
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

        form.setFields(radioGroupItem);
        form2.setFields(permutation, seed);
        form.redraw();
        form2.redraw();
        mainBodyLayout.addMember(form);
        mainBodyLayout.addMember(form2);

        HLayout hlo = new HLayout();
        hlo.setWidth("100%");
        hlo.setHeight("10%");

        IButton newSeedBtn = new IButton("Create new seed");
        hlo.addMember(newSeedBtn);

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
        colGroupsList = null;

        newSeedBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                seed.setValue(Random.nextInt(1000000001));
            }
        });

    }

    public void updateData(List<ColumnGroup> colGroupsList) {
        if (errorlabl != null) {
            errorlabl.setVisible(false);
        }
        if (form2 != null) {
            form2.clearErrors(true);
            form2.redraw();
        }
        updateRecors(colGroupsList);

//        selectColGroups.setValueMap(colGroupsNamesMap);

    }

    public  List<String> getSelectColGroups() {
        List<String> selectionList = new ArrayList<String>();
        ListGridRecord[] records = selectionTable.getRecords();
        for (ListGridRecord record : records) {
            if (Boolean.valueOf(record.getAttribute("selection")) == true) {
                selectionList.add(record.getAttribute("groupName"));
            }
        }
        return selectionList;
        
    }

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
        
        selectionTable.setTitle("COLUMN GROUPS (MAX 2)");
        selectionTable.setWidth100();
        selectionTable.setHeight("70%");
        
        ListGridField selectField = new ListGridField("selection", "Selection");  
        selectField.setType(ListGridFieldType.BOOLEAN);  
        selectField.setCanEdit(true);  
        selectField.setAutoFitWidth(true);
        
        ListGridField groupNameField = new ListGridField("groupName", "Group Name", 50);  
        groupNameField.setAlign(Alignment.CENTER);  
        groupNameField.setType(ListGridFieldType.TEXT);  
        groupNameField.setAutoFitWidth(true);
       
        ListGridField colorField = new ListGridField("color", "Color");  
        colorField.setAlign(Alignment.CENTER);  
        colorField.setType(ListGridFieldType.IMAGE);          
        colorField.setCanEdit(false);  
        colorField.setAutoFitWidth(true);
        
        ListGridField countField = new ListGridField("count", "Count");  
        countField.setAlign(Alignment.CENTER);  
        countField.setType(ListGridFieldType.INTEGER); 
        countField.setAutoFitWidth(true);
  
        selectionTable.setFields(new ListGridField[] {selectField, groupNameField, colorField,countField});  
        
        
        
        

    }
    
    private void updateRecors(List<ColumnGroup> colGroupsList){
        ListGridRecord[] records = new ListGridRecord[colGroupsList.size()];
        int index=0;
        for(ColumnGroup cgr:colGroupsList){
             ListGridRecord record = new ListGridRecord();  
        record.setAttribute("selection", false);  
        record.setAttribute("groupName", cgr.getName());  
        com.google.gwt.user.client.Window.alert((cgr.getColor() == null)+cgr.getColor());
        record.setAttribute("color", new Image(cgr.getColor()));  
        record.setAttribute("count", cgr.getCount());  
        records[index++]=record;
        
        }
       
    selectionTable.setData(records);  
    }

}
