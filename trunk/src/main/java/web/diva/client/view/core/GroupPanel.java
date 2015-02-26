/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ColorPickerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author Yehia Farag
 */
public class GroupPanel extends PopupPanel {

    private final IButton okBtn;
    private final DynamicForm form;
    private final VLayout mainBodyLayout, framLayout;
    private final TextItem name, description, members;
    private final ColorPickerItem colorPicker;

    public GroupPanel() {

        this.setAnimationEnabled(true);
        this.ensureDebugId("cwBasicPopup-imagePopup");
        this.setModal(false);
//        this.getElement().setAttribute("style", "overflow: visible; visibility: visible; margin-left: 40%; margin-top: auto; position: absolute; left:0px; top:0px; background: none;width: 230px;height: 170px; clip:auto;");

        framLayout = new VLayout();
        framLayout.setWidth(230);
        framLayout.setHeight(170);

        mainBodyLayout = new VLayout();
        mainBodyLayout.setWidth(228);
        mainBodyLayout.setHeight(148);
        mainBodyLayout.setMembersMargin(5);

        try {

            HorizontalPanel topLayout = new HorizontalPanel();
            topLayout.setWidth("228px");
            topLayout.setHeight("20px");
            topLayout.setSpacing(3);
            framLayout.addMember(topLayout);

            Label title = new Label("Create Group");
            title.setStyleName("labelheader");
            topLayout.add(title);
            title.setWidth(170 + "px");
            title.setHeight("18px");
            topLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);
            topLayout.setCellVerticalAlignment(title, HorizontalPanel.ALIGN_TOP);

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

            topLayout.add(closeBtn);
            topLayout.setCellHorizontalAlignment(closeBtn, HorizontalPanel.ALIGN_RIGHT);
            topLayout.setCellVerticalAlignment(closeBtn, HorizontalPanel.ALIGN_TOP);
        } catch (Exception e) {
            Window.alert("error");
        }
        framLayout.addMember(mainBodyLayout);
        mainBodyLayout.setStyleName("modalPanelBody");
        form = new DynamicForm();
        form.setGroupTitle("Create Group");
        form.setWidth(228);
        form.setLayoutAlign(Alignment.CENTER);

        name = new TextItem();
        name.setTitle("Group Name");
        name.setRequired(true);

        description = new TextItem();
        description.setTitle("Description");
        description.setRequired(true);
        colorPicker = new ColorPickerItem("groupColor", "Group Color");
        colorPicker.setRequired(Boolean.TRUE);
//        colorPicker.getPicker().setZIndex(framLayout.getZIndex()+1000);
//       colorPicker.setValue("red");
//        SC.say(colorPicker.getPicker().getScClassName()+"  "+colorPicker.getPicker().getZIndex());
        members = new TextItem();
        members.setTitle("Memebers");
        members.setRequired(true);

        members.disable();

        form.setFields(name, description, colorPicker, members);
        form.redraw();
        mainBodyLayout.addMember(form);

        HorizontalPanel hlo = new HorizontalPanel();
        mainBodyLayout.addMember(hlo);
        hlo.setWidth("228px");
        hlo.setHeight("20px");
        okBtn = new IButton("Process");
        hlo.add(okBtn);
        hlo.setCellHorizontalAlignment(okBtn, HorizontalPanel.ALIGN_CENTER);
        hlo.setCellVerticalAlignment(okBtn, HorizontalPanel.ALIGN_MIDDLE);

        this.setWidget(framLayout);
        framLayout.setStyleName("modalPanelLayout");
        framLayout.redraw();
        this.show();
        this.hide();

    }

    public IButton getOkBtn() {
        return okBtn;
    }

    public void setCount(int count) {
        members.setValue(count);
    }

    public String getName() {
        return name.getValueAsString();
    }

    public String getDescription() {
        return description.getValueAsString();
    }

    public String getColor() {
        return colorPicker.getValueAsString();
    }

    public DynamicForm getForm() {
        return form;
    }

}
