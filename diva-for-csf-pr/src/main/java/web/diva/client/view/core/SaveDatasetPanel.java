/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 *
 * @author Yehia Farag
 */
public class SaveDatasetPanel extends PopupPanel{
     private final IButton okBtn;
     private final TextItem name;
     private final DynamicForm form;
    public SaveDatasetPanel(){
    this.setAnimationEnabled(true);
      this.setAnimationEnabled(true);
        this.ensureDebugId("cwBasicPopup-imagePopup");
        this.setModal(false);
        VerticalPanel framLayout = new VerticalPanel();
        framLayout.setWidth("300px");
        framLayout.setHeight("100px");

        VerticalPanel mainBodyLayout = new VerticalPanel();
        mainBodyLayout.setWidth("298px");
        mainBodyLayout.setHeight("128px");        
        mainBodyLayout.setStyleName("modalPanelBody");
        
         HorizontalPanel topLayout = new HorizontalPanel();
        topLayout.setWidth("298px");
        topLayout.setHeight("18px");
        
        Label title = new Label("Publish Dataset Online");
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth(150 + "px");
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

        
        form = new DynamicForm();
        form.setGroupTitle("");
        
        form.setIsGroup(false);
        form.setWidth(288);
        form.setPadding(10);
        form.setLayoutAlign(Alignment.CENTER);

      
        name = new TextItem();
        name.setTitle("Dataset Name");
        name.setWidth(250);
        name.setRequired(true);
        name.setTitleOrientation(TitleOrientation.TOP);
        form.setFields(name);
        form.setBorder("0px solid");
        form.draw();
        
         mainBodyLayout.add(form);
        
         VerticalPanel hlo = new VerticalPanel();
        hlo.setWidth("298px");
        hlo.setHeight("20px");
        okBtn = new IButton("Process");
        hlo.add(okBtn);
        hlo.setCellHorizontalAlignment(okBtn, VerticalPanel.ALIGN_CENTER);
            hlo.setCellVerticalAlignment(okBtn, VerticalPanel.ALIGN_MIDDLE);
        
        mainBodyLayout.add(hlo);
        
        framLayout.add(topLayout);
        framLayout.add(mainBodyLayout);
           this.setWidget(framLayout);
        framLayout.setStyleName("modalPanelLayout");
    
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
