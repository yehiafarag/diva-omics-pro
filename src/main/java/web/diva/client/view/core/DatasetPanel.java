/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.ColorPicker;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ColorSelectedEvent;
import com.smartgwt.client.widgets.form.events.ColorSelectedHandler;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.LinkedHashMap;

/**
 *
 * @author Yehia Farag
 * pup-up panel for dataset  user input parameter panel
 */
public final class DatasetPanel extends Window {

    private final RadioGroupItem radioGroupItem;
    private final RadioGroupItem processType;
    private final TextItem name, datasetName;
    private final IButton okBtn;
    private final SelectItem selectCols;
    private final VLayout vlo;
    private final HTML colorLable = new HTML("<p style='height:10px;width:10px;font-weight: bold; color:white;font-size: 10px;background: #ffffff; border-style:double;'></p>");

    public String[] getSelectColsValue() {
        return selectCols.getValues();
    }

    public String getRadioGroupItemValue() {
        return radioGroupItem.getValueAsString();
    }

    public String getProcessType() {
        return processType.getValueAsString();
    }

    public TextItem getName() {
        return name;
    }

    public TextItem getDatasetName() {
        return datasetName;
    }

    public IButton getOkBtn() {
        return okBtn;
    }

    public HTML getErrorlabl() {
        return errorlabl;
    }

    public DynamicForm getForm2() {
        return form2;
    }

    public DynamicForm getForm() {
        return form;
    }
    private final HTML errorlabl;
    private final DynamicForm form2;
    private final DynamicForm form;
    private int[] rowSelection, colSelection;
    private String color;

    public int[] getRowSelection() {
        return rowSelection;
    }

    public int[] getColSelection() {
        return colSelection;
    }

    public DatasetPanel(LinkedHashMap<String, String> colNamesMap, int[] rowSelection, int[] colSelection) {

        this.setWidth(400);
        this.setHeight(500);
        this.setTitle("Create Groups / Datasets");
        this.setShowMinimizeButton(false);
        this.setIsModal(false);
        this.centerInPage();
        this.addCloseClickHandler(new CloseClickHandler() {
            @Override
            public void onCloseClick(CloseClickEvent event) {
                hide();
            }
        });

        vlo = new VLayout();
        vlo.setWidth(380);
        vlo.setHeight(450);
        this.addItem(vlo);
        DynamicForm initForm = new DynamicForm();
        initForm.setHeight(40);
        initForm.setIsGroup(true);
        initForm.setGroupTitle("Select");
        initForm.setWidth(380);
        initForm.setPadding(5);
        processType = new RadioGroupItem();
        processType.setTitle("Select");
        processType.setValueMap("Groups", "Sub-Datasets");
        processType.setValue("Groups");
        processType.setShouldSaveValue(true);
        initForm.setItems(processType);
        initForm.redraw();
        vlo.addMember(initForm);

        form = new DynamicForm();
        form.setIsGroup(true);
        form.setTitle("Create Group");
        form.setGroupTitle("Create Group");
        form.setWidth(380);
        form.setPadding(5);
        form.setLayoutAlign(VerticalAlignment.BOTTOM);

        radioGroupItem = new RadioGroupItem();
        name = new TextItem();

        radioGroupItem.setTitle("Type");
        radioGroupItem.setValueMap("ROW GROUPS", "COLUMN GROUPS");
        radioGroupItem.setValue("ROW GROUPS");
        radioGroupItem.setShouldSaveValue(true);

        selectCols = new SelectItem();
        selectCols.setRequired(true);
        selectCols.setTitle("SELECT COLUMNS TO GROUP");
        selectCols.setTextAlign(Alignment.CENTER);
        selectCols.setTitleAlign(Alignment.CENTER);
        selectCols.setMultiple(true);
        selectCols.setMultipleAppearance(MultipleAppearance.GRID);
        selectCols.setValueMap(colNamesMap);
        updateDataValues(rowSelection, colSelection);
        colNamesMap = null;
        name.setTitle("Group Name");
        name.setRequired(true);

        final ColorPicker picker = new ColorPicker();
        final IButton button = new IButton("Select Color");
        picker.setVisible(false);
        picker.setAllowComplexMode(false);
        picker.setWidth("179px");
        picker.setHeight("156px");
        picker.addColorSelectedHandler(new ColorSelectedHandler() {

            @Override
            public void onColorSelected(ColorSelectedEvent event) {
                color = event.getColor();
                getColorLable().setHTML("<p style='height:10px;width:10px;font-weight: bold; color:white;font-size: 10px;background:" + color + "; border-style:double;'></p>");
                colorLable.setVisible(true);
            }

        });

        HLayout hloCol = new HLayout();
        hloCol.setWidth(380);
        hloCol.setHeight(50);

//
        hloCol.addMember(button);
        hloCol.addMember(colorLable);
        hloCol.setMargin(10);
        hloCol.setMembersMargin(20);
        hloCol.setAlign(Alignment.CENTER);
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                colorLable.setVisible(false);
                picker.setVisible(true);
            }
        });
        hloCol.addMember(picker);

        form.setFields(radioGroupItem, selectCols, name);
        form.draw();
        form2 = new DynamicForm();
        form2.setGroupTitle("Create Sub-Dataset");
        form2.setIsGroup(true);

        datasetName = new TextItem();
        datasetName.setTitle("Dataset Name");
        datasetName.setRequired(true);
        form2.setFields(datasetName);
        form2.disable();
        vlo.addMember(form);
        vlo.addMember(hloCol);
        vlo.addMember(form2);

        HLayout hlo = new HLayout();
        hlo.setWidth(380);
        hlo.setHeight(20);

        okBtn = new IButton("OK");
        hlo.addMember(okBtn);
        hlo.setMargin(10);
        hlo.setMembersMargin(20);
        hlo.setAlign(Alignment.CENTER);
        vlo.addMember(hlo);

        errorlabl = new HTML("<h4 style='color:red;margin-left: 20px;height=20px;'>PLEASE CHECK YOUR DATA INPUT  </h4>");
        errorlabl.setHeight("25px");
        errorlabl.setWidth("380px");
        errorlabl.setVisible(false);
        vlo.addMember(errorlabl);

        vlo.redraw();

        processType.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                if (processType.getValueAsString().equals("Groups")) {
                    form.enable();
                    form2.disable();
                    vlo.redraw();

                } else {
                    form2.enable();
                    form.disable();
                    vlo.redraw();

                }
            }
        });
        rowSelection = null;
        colSelection = null;

    }

    public void updateDataValues(int[] rowSelection, int[] colSelection) {
        this.rowSelection = rowSelection;
        this.colSelection = colSelection;
        this.colorLable.setHTML("<p style='height:10px;width:10px;font-weight: bold; color:white;font-size: 10px;background:#ffffff; border-style:double;'></p>");

        if (errorlabl != null) {
            errorlabl.setVisible(false);
        }
        if (form != null) {
            form.clearErrors(true);
            form.clearValues();
            form.redraw();
        }
        if (okBtn != null) {
            okBtn.enable();
        }
        if (form2 != null) {
            form2.clearErrors(true);
        }
        if (name != null) {
            name.setValue("");
        }
        if (datasetName != null) {
            datasetName.setValue("");
        }
        if (colSelection != null && colSelection.length > 0) {
            String[] values = new String[colSelection.length];
            for (int x = 0; x < colSelection.length; x++) {
                values[x] = "" + colSelection[x];
            }
            selectCols.setValues(values);
            selectCols.redraw();
            selectCols.disable();
        }

        if (rowSelection == null || rowSelection.length == 0) {
            radioGroupItem.setValue("COLUMN GROUPS");
            selectCols.enable();
            radioGroupItem.disable();
            processType.disable();
        } else {
            radioGroupItem.setValue("ROW GROUPS");
            processType.enable();
            radioGroupItem.enable();
            selectCols.disable();

            radioGroupItem.addChangedHandler(new ChangedHandler() {
                @Override
                public void onChanged(ChangedEvent event) {
                    if (radioGroupItem.getValueAsString().equals("COLUMN GROUPS")) {
                        selectCols.enable();
                    } else {
                        selectCols.disable();
                    }
                }
            });

        }

    }

    public String getColor() {
        return color;
    }

    public HTML getColorLable() {
        return colorLable;
    }

}
