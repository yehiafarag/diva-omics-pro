/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.unused;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
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
    private CheckboxItem defaultGroupSelectionItem;
    private TextItem permutation, seed;
    private  IButton okBtn;
    private final HTML errorlabl;
    private DynamicForm form2;
    private ListGrid selectionTable;
    private boolean defaultRank;

    public DynamicForm getForm2() {
        return form2;
    }

    public HTML getErrorlabl() {
        return errorlabl;
    }
//    private final VLayout mainBodyLayout;

    public RankPanel() {
        this.setAnimationEnabled(true);
        this.ensureDebugId("cwBasicPopup-imagePopup");
        this.setModal(false);

        VerticalPanel framLayout = new VerticalPanel();
        framLayout.setWidth("485px");
        framLayout.setHeight("230px");

        VerticalPanel mainBodyLayout = new VerticalPanel();
        mainBodyLayout.setWidth("483px");
        mainBodyLayout.setHeight("308px");
        mainBodyLayout.setStyleName("modalPanelBody");
        
        HorizontalPanel topLayout = new HorizontalPanel();
//        mainBodyLayout.addMember(topLayout);
        topLayout.setWidth(483 + "px");
        topLayout.setHeight("18px");
        topLayout.setStyleName("whiteLayout");
//        topLayout.setSpacing(3);

        Label title = new Label("Rank Product (Differential Expression)");
        topLayout.add(title);
        title.setStyleName("labelheader");
        title.setWidth(220 + "px");
        topLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);
        topLayout.setCellVerticalAlignment(title, HorizontalPanel.ALIGN_TOP);

        Label closeBtn = new Label();
        closeBtn.addStyleName("close");
        closeBtn.setHeight("16px");
        closeBtn.setWidth("16px");

        closeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                hide(true);
            }
        });

        topLayout.add(closeBtn);
        topLayout.setCellHorizontalAlignment(closeBtn, HorizontalPanel.ALIGN_RIGHT);
        topLayout.setCellVerticalAlignment(closeBtn, HorizontalPanel.ALIGN_TOP);

        try {

            VerticalPanel groupsTableLayout = new VerticalPanel();
            mainBodyLayout.add(groupsTableLayout);
            groupsTableLayout.setWidth(483 + "px");
            groupsTableLayout.setHeight("100px");
            groupsTableLayout.setStyleName("whiteLayout");

            Label tableTitle = new Label("Column Groups (Max 2)");
            tableTitle.setHeight("20px");
            tableTitle.setWidth("250px");
            tableTitle.setStyleName("secheadertitle");
            groupsTableLayout.add(tableTitle);

            groupsTableLayout.setCellVerticalAlignment(tableTitle, VerticalPanel.ALIGN_MIDDLE);

            selectionTable = new ListGrid();
            initSelectionTable();
            groupsTableLayout.add(selectionTable);
            groupsTableLayout.setCellVerticalAlignment(selectionTable, VerticalPanel.ALIGN_MIDDLE);
            groupsTableLayout.setCellHorizontalAlignment(selectionTable, VerticalPanel.ALIGN_CENTER);

            HorizontalPanel middleLayout = new HorizontalPanel();
            middleLayout.setWidth("483px");
            middleLayout.setHeight("100px");
            middleLayout.setStyleName("whiteLayout");

            DynamicForm form = new DynamicForm();
            form.setIsGroup(false);
            form.setWidth(150);
            form.setLayoutAlign(Alignment.CENTER);
            form.setHeight("100px");
            form.setNumCols(1);

            form.setLeft(20);
            
            
            radioGroupItem = new RadioGroupItem();
            radioGroupItem.setShowTitle(false);
            radioGroupItem.setHeight("50px");
            radioGroupItem.setWidth("150px");

            radioGroupItem.setTitle("Values ");
            radioGroupItem.setValueMap("Log 2", "Linear");
            radioGroupItem.setValue("Log 2");
            radioGroupItem.setShouldSaveValue(true);


            defaultGroupSelectionItem = new CheckboxItem();
            defaultGroupSelectionItem.setStartRow(true);
            defaultGroupSelectionItem.setTitle("Default Rank Product");
            defaultGroupSelectionItem.setShowTitle(false);
            defaultGroupSelectionItem.setValueMap("Default Rank Product");
            defaultGroupSelectionItem.setValue(true);
//            defaultGroupSelectionItem.setTitleOrientation(TitleOrientation.RIGHT);
            defaultGroupSelectionItem.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    defaultRank = (((Boolean) event.getValue()));
                }
            });
            defaultGroupSelectionItem.setTooltip("loaded by default on loading the dataset");
            form.setFields(radioGroupItem, defaultGroupSelectionItem);
            form.redraw();
            middleLayout.add(form);
            middleLayout.setCellWidth(form, "170px");
            middleLayout.setCellHorizontalAlignment(form, HorizontalPanel.ALIGN_CENTER);

            form2 = new DynamicForm();
            form2.setIsGroup(false);
            form2.setHeight("60px");
            form2.setWidth100();
            form2.setMargin(0);
            form2.setPadding(1);
            form2.setNumCols(3);
            
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

            final ButtonItem btn = new ButtonItem("seed", "seed");  
            btn.setStartRow(false);
            btn.setWidth(40);
            btn.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
                @Override
                public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                    seed.setValue(Random.nextInt(1000000001));
                }
            });
            form2.setFields(permutation, seed, btn);
            form2.setColWidths(new Object[]{100, "*", 60});
//            form2.redraw();
            okBtn = new IButton("Process");
            okBtn.setWidth("200px");
            okBtn.setHeight("20px");
            okBtn.setAlign(Alignment.CENTER);
            VLayout hlo = new VLayout();
            hlo.setWidth("220px");
            hlo.setHeight("80px");
            hlo.setAlign(Alignment.CENTER);
            hlo.addMember(form2);

            form.redraw();

            middleLayout.add(hlo);
            
            mainBodyLayout.add(middleLayout);
            mainBodyLayout.add(okBtn);
            mainBodyLayout.setCellHorizontalAlignment(okBtn, VerticalPanel.ALIGN_CENTER);
        } catch (Exception e) {
            Window.alert("error is ");
        }

     

       

        errorlabl = new HTML("<p style='font-size: 10px;color:red;margin-left: 20px;height=20px;'>PLEASE CHECK YOUR DATA INPUT .. PLEASE NOTE THAT YOU CAN NOT SELECT MORE THAN 2 GROUPS</p>");
        errorlabl.setVisible(false);
        errorlabl.setHeight("50px");
        errorlabl.setWidth("398px");
        mainBodyLayout.add(errorlabl);
        framLayout.add(topLayout);
        framLayout.add(mainBodyLayout);
        this.setWidget(framLayout);
        framLayout.setStyleName("modalPanelLayout");
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

        return selectionList;

    }

    private List<String> selectionList = new ArrayList<String>();

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

        selectionTable.setWidth("483px");
        selectionTable.setHeight("100px");
//        selectionTable.setStyleName("borderless");
        selectionTable.setLeaveScrollbarGap(false);
        selectionTable.setShowHeaderContextMenu(false);
        ListGridField selectField = new ListGridField("Selection", "Selection");
        selectField.setType(ListGridFieldType.BOOLEAN);
        selectField.setCanEdit(true);

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

        selectionTable.addEditCompleteHandler(new EditCompleteHandler() {

            @Override
            public void onEditComplete(EditCompleteEvent event) {
                boolean test = event.getNewValuesAsRecord().getAttributeAsBoolean("Selection");

                if (selectionList.contains(event.getOldValues().getAttributeAsString("groupName"))) {
                    if (!test) {
                        selectionList.remove(event.getOldValues().getAttributeAsString("groupName"));
                    }
                } else if (test) {
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

    

    public boolean isDefaultRank() {
        return defaultRank;
    }

}
