/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ColorPickerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author Yehia Farag
 */
public class GroupPanel extends PopupPanel{
    private final IButton okBtn;
    private HTML errorlabl;   
    private DynamicForm form;
    private final VLayout mainBodyLayout;
     private final TextItem name, description,members;
     private final HTML colorLable = new HTML("<p style='height:10px;width:10px;font-weight: bold; color:white;font-size: 10px;background: #ffffff; border-style:double;'></p>");
     private String color;
     private int count;
     private ColorPickerItem colorPicker;
    public GroupPanel(){
        
        this.setAnimationEnabled(true);
        this.ensureDebugId("cwBasicPopup-imagePopup");
        this.setModal(false);
      
//        this.setWidth(230 + "px");
//        this.setHeight(170 + "px");
        mainBodyLayout = new VLayout();        
        mainBodyLayout.setWidth(230);
        mainBodyLayout.setHeight(170);
       
        try{

            HorizontalPanel topLayout = new HorizontalPanel();
        topLayout.setWidth("230px");
        topLayout.setHeight("20px");
        topLayout.setSpacing(3);
        mainBodyLayout.addMember(topLayout);
        Label title = new Label("Create Group");
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth(200+ "px");
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
        mainBodyLayout.setMembersMargin(2);
        form = new DynamicForm();
//        form.setIsGroup(true);
//        form.setTitle("Create Group");
        form.setGroupTitle("Create Group");
        form.setWidth(230);
        form.setLayoutAlign(Alignment.CENTER);

       
        name = new TextItem();
        name.setTitle("Group Name");
        name.setRequired(true);

        description = new TextItem();
        description.setTitle("Description");
        description.setRequired(true);
         colorPicker = new ColorPickerItem("groupColor","Group Color");  
         colorPicker.setRequired(Boolean.TRUE);
        
         
         members = new TextItem();
        members.setTitle("Memebers");
        members.setRequired(true);
        
        members.disable();
        
  
        
//       HLayout hloCol = new HLayout();
//        hloCol.setWidth(230);
//        hloCol.setHeight(50);

//
//        hloCol.addMember(button);
//        hloCol.addMember(colorLable);
//        hloCol.setMargin(10);
//        hloCol.setMembersMargin(20);
//        hloCol.setAlign(Alignment.CENTER);
//        button.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                colorLable.setVisible(false);
//                picker.setVisible(true);
//            }
//        });
//        hloCol.addMember(picker);
        form.setFields(name, description,colorPicker,members);
        form.redraw();
        mainBodyLayout.addMember(form);
        
        
        
        
//        mainBodyLayout.addMember(hloCol);

        HLayout hlo = new HLayout();
        mainBodyLayout.addMember(hlo);
        hlo.setWidth("100%");
        hlo.setHeight("10%");
        okBtn = new IButton("Process");
        hlo.addMember(okBtn);

        hlo.setMargin(5);
        hlo.setMembersMargin(1);
        hlo.setAlign(Alignment.CENTER);
        

        errorlabl = new HTML("<h4 style='color:red;margin-left: 20px;height=20px;'>PLEASE CHECK YOUR DATA INPUT ..</h4>");
        mainBodyLayout.addMember(errorlabl);
        errorlabl.setVisible(false);
        errorlabl.setHeight("20px");
        errorlabl.setWidth("100%");
        
         this.setWidget(mainBodyLayout);
         mainBodyLayout.setStyleName("modalLayout");
        mainBodyLayout.redraw();
        this.show();
        this.hide();

        
        
    }
    

  

   
    public IButton getOkBtn() {
        return okBtn;
    }

    public void setCount(int count) {
        this.count = count;
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
    

