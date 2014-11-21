/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.rank.view;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.ReadOnlyDisplayAppearance;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.LinkedHashMap;

/**
 *
 * @author Yehia Farag
 * pup-up window for rank panel user inputs
 */
public final class RankPanel extends Window {

    private final SelectItem selectColGroups;
    private final RadioGroupItem radioGroupItem;
    private final TextItem permutation, seed;
    private final IButton okBtn;
    private final HTML errorlabl;
    private final DynamicForm form2;

    public DynamicForm getForm2() {
        return form2;
    }

    public HTML getErrorlabl() {
        return errorlabl;
    }

    public RankPanel(LinkedHashMap<String, String> colGroupsNamesMap) {
        this.setWidth(400);
        this.setHeight(400);
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

        VLayout vlo = new VLayout();
        vlo.setWidth("100%");
        vlo.setHeight("100%");
        this.addItem(vlo);

        DynamicForm form = new DynamicForm();
        form.setHeight("50%");
        form.setIsGroup(true);
        form.setWidth("100%");
        form.setPadding(5);
        selectColGroups = new SelectItem();
        selectColGroups.setTitle("COLUMN GROUPS (MAX 2)");
        selectColGroups.setTitleOrientation(TitleOrientation.TOP);
        selectColGroups.setTextAlign(Alignment.CENTER);
        selectColGroups.setTitleAlign(Alignment.CENTER);
        selectColGroups.setMultiple(true);
        selectColGroups.setMultipleAppearance(MultipleAppearance.GRID);
        selectColGroups.setWidth("100%");
        selectColGroups.setHeight("60%");
        this.updateData(colGroupsNamesMap);

        radioGroupItem = new RadioGroupItem();
        radioGroupItem.setHeight("20%");
        radioGroupItem.setWidth("100%");
        radioGroupItem.setTitle("Values");
        radioGroupItem.setValueMap("Log 2", "Linear");
        radioGroupItem.setValue("Log 2");
        radioGroupItem.setShouldSaveValue(true);

        form2 = new DynamicForm();
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

        form.setFields(selectColGroups, radioGroupItem);
        form2.setFields(permutation, seed);
        form.redraw();
        form2.redraw();
        vlo.addMember(form);
        vlo.addMember(form2);

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
        vlo.addMember(hlo);

        errorlabl = new HTML("<h4 style='color:red;margin-left: 20px;height=20px;'>PLEASE CHECK YOUR DATA INPUT .. PLEASE NOTE THAT YOU CAN NOT SELECT MORE THAN 2 GROUPS</h4>");
        errorlabl.setVisible(false);
        errorlabl.setHeight("15%");
        errorlabl.setWidth("100%");
        vlo.addMember(errorlabl);
        colGroupsNamesMap = null;

        newSeedBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                seed.setValue(Random.nextInt(1000000001));
            }
        });

    }

    public void updateData(LinkedHashMap<String, String> colGroupsNamesMap) {
        if (errorlabl != null) {
            errorlabl.setVisible(false);
        }
        if (form2 != null) {
            form2.clearErrors(true);
            form2.redraw();
        }

        selectColGroups.setValueMap(colGroupsNamesMap);

    }

    public String[] getSelectColGroups() {
        return selectColGroups.getValues();
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

}
