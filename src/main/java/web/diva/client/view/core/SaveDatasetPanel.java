/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author Yehia Farag
 */
public class SaveDatasetPanel extends PopupPanel{
     private final IButton okBtn;
     private final TextItem name;
     private final VLayout mainBodyLayout;
     private final DynamicForm form;
     private final HTML errorlabl;
    public SaveDatasetPanel(){
    this.setAnimationEnabled(true);
        this.ensureDebugId("cwBasicPopup-imagePopup");
        this.setModal(false);
        this.setWidth(300 + "px");
        this.setHeight(50 + "px");
        mainBodyLayout = new VLayout();
        
        mainBodyLayout.setWidth(300);
        mainBodyLayout.setHeight(50);
        this.add(mainBodyLayout);
        
         HLayout topLayout = new HLayout();
        topLayout.setMembersMargin(1);
        topLayout.setWidth(230);
        topLayout.setHeight(20);
        mainBodyLayout.addMember(topLayout);
        com.smartgwt.client.widgets.Label title = new com.smartgwt.client.widgets.Label("");
        title.setStyleName("labelheader");
        topLayout.addMember(title);
        title.setWidth(283 + "px");

        com.smartgwt.client.widgets.Label closeBtn = new com.smartgwt.client.widgets.Label();

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
       
        
        form = new DynamicForm();
        form.setGroupTitle("Publish Dataset Online");
        form.setIsGroup(true);
        form.setWidth(300);
        form.setLayoutAlign(Alignment.CENTER);

       mainBodyLayout.addMember(form);
        name = new TextItem();
        name.setTitle("Dataset Name");
        name.setWidth(300);
        name.setRequired(true);
        form.setFields(name);
         HLayout hlo = new HLayout();
        hlo.setWidth("100%");
        hlo.setHeight("10%");
        okBtn = new IButton("Process");
        Label spacer = new Label();
        spacer.setWidth(100 + "px");
        hlo.addMember(spacer);
        hlo.addMember(okBtn);

        hlo.setMargin(5);
        hlo.setMembersMargin(1);
        hlo.setAlign(Alignment.CENTER);
        
        mainBodyLayout.addMember(hlo);
        
         errorlabl = new HTML("<h4 style='color:red;margin-left: 20px;height=20px;'>PLEASE CHECK YOUR DATA INPUT ..</h4>");
        
        errorlabl.setVisible(false);
        errorlabl.setHeight("15%");
        errorlabl.setWidth("100%");
        mainBodyLayout.addMember(errorlabl);
        mainBodyLayout.redraw();
        this.show();
        this.hide();
    
    }

    public IButton getOkBtn() {
        return okBtn;
    }

    public String getName() {
        return name.getValueAsString();
    }

    public DynamicForm getForm() {
        return form;
    }
}
