/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
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
      this.setAnimationEnabled(true);
        this.ensureDebugId("cwBasicPopup-imagePopup");
        this.setModal(false);
        mainBodyLayout = new VLayout();      
        
        mainBodyLayout.setWidth(300);
        mainBodyLayout.setHeight(150);
        
         HorizontalPanel topLayout = new HorizontalPanel();
        topLayout.setWidth("300px");
        topLayout.setHeight("18px");
        mainBodyLayout.addMember(topLayout);
        Label title = new Label("Publish Dataset Online");
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth(250 + "px");
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
        form.setWidth(250);
        form.setPadding(10);
        form.setLayoutAlign(Alignment.CENTER);

       mainBodyLayout.addMember(form);
        name = new TextItem();
        name.setTitle("Dataset Name");
        name.setWidth(250);
        name.setRequired(true);
        name.setTitleOrientation(TitleOrientation.TOP);
        form.setFields(name);
        form.setBorder("0px solid");
         VerticalPanel hlo = new VerticalPanel();
        hlo.setWidth("300px");
        hlo.setHeight("20px");
        okBtn = new IButton("Process");
//        Label spacer = new Label();
//        spacer.setWidth(100 + "px");
//        hlo.addMember(spacer);
        hlo.add(okBtn);
        hlo.setCellHorizontalAlignment(okBtn, VerticalPanel.ALIGN_CENTER);
            hlo.setCellVerticalAlignment(okBtn, VerticalPanel.ALIGN_MIDDLE);

//        hlo.setMargin(5);
//        hlo.setMembersMargin(1);
//        hlo.setAlign(Alignment.CENTER);
        
        mainBodyLayout.addMember(hlo);
        
         errorlabl = new HTML("<h4 style='color:red;margin-left: 20px;height=20px;'>PLEASE CHECK YOUR DATA INPUT ..</h4>");
        
        errorlabl.setVisible(false);
        errorlabl.setHeight("15%");
        errorlabl.setWidth("100%");
        mainBodyLayout.addMember(errorlabl);
        mainBodyLayout.redraw();
        this.setWidget(mainBodyLayout);
         mainBodyLayout.setStyleName("modalLayout");
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
