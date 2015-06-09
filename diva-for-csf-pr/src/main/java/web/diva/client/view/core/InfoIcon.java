package web.diva.client.view.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 * @author Yehia Farag
 */
public class InfoIcon extends Label implements ClickHandler{
 private final PopupPanel imagePopup ;
    public InfoIcon(String moduleName,VerticalPanel infoLayout,int height,int width) {
        this.addStyleName("infoIcon");
        this.setHeight("16px");
        this.setWidth("16px");
        height = height+40;
        width = width+40;
        this.addClickHandler(InfoIcon.this);
        this.setTitle("Click to get more information");
       imagePopup= new PopupPanel(true, true);
        imagePopup.setAnimationEnabled(true);
        imagePopup.ensureDebugId("cwBasicPopup-imagePopup");
        imagePopup.setGlassEnabled(false);
        imagePopup.setModal(false);
      
        infoLayout.setStyleName("whiteLayout");        
        imagePopup.setAutoHideEnabled(true);
  
        VerticalPanel popupLayout = new VerticalPanel();
        popupLayout.setHeight(height + "px");
        popupLayout.setWidth(width + "px");
        popupLayout.setSpacing(10);
        popupLayout.setStyleName("modalLayout");
        HorizontalPanel topPanel = new HorizontalPanel();
        topPanel.setWidth((width-20)+"px");
        
        Label minmizeBtn = new Label();
        minmizeBtn.addStyleName("minmize");
        minmizeBtn.setHeight("16px");
        minmizeBtn.setWidth("16px");
        
        minmizeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                  if (imagePopup.isShowing()) {
                    imagePopup.hide(true);
                } else {
                    imagePopup.center();
                    imagePopup.show();
                }
            }
        });


//        popupLayout.getElement().setAttribute("style", "top:30%;");
        HTML title = new HTML("<b><span style='margin:30px;margin-top:50px;font-size:14px'>"+moduleName+"</span></b>");
        
        topPanel.add(title);
        topPanel.add(minmizeBtn);
        topPanel.setCellHorizontalAlignment(minmizeBtn, HorizontalPanel.ALIGN_RIGHT);
         topPanel.setCellVerticalAlignment(minmizeBtn, HorizontalPanel.ALIGN_TOP);
         popupLayout.add(topPanel);
        popupLayout.add(infoLayout);
        imagePopup.setWidget(popupLayout);
        
    

    }

    @Override
    public void onClick(ClickEvent event) {
        imagePopup.center();
        imagePopup.show();
        
    }
    
    

}
