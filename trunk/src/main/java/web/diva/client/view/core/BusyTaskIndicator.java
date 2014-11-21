/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.jfree.ui.HorizontalAlignment;

/**
 *
 * @author Yehia Farag
 */
public class BusyTaskIndicator {
    
    private final PopupPanel imagePopup;
    public BusyTaskIndicator(){
        
        VerticalPanel vp = new VerticalPanel();
        vp.setStyleName("indicator");
        Image image = new Image("images/progress.gif");   
        vp.add(image);
        
//         imagePopup = new PopupPanel(true, true);
//        imagePopup.setAnimationEnabled(true);
//        imagePopup.ensureDebugId("cwBasicPopup-imagePopup");
//        imagePopup.setWidget(vp);     
        
         VerticalPanel chartLayout = new VerticalPanel();
        VerticalPanel chartImgLayout = new VerticalPanel();

        imagePopup = new PopupPanel(false, true);
        imagePopup.setAnimationEnabled(true);
        imagePopup.ensureDebugId("cwBasicPopup-imagePopup");
        imagePopup.setWidget(chartLayout);

        chartImgLayout.add(image);
        chartImgLayout.setBorderWidth(1);
        chartLayout.add(chartImgLayout);
        
    }
    
    public void busyTask(boolean busy){
        if(busy){
                imagePopup.center();
                imagePopup.show(); //To change body of generated methods, choose Tools | Templates.
            }
        else{
                imagePopup.hide();
            }
    }
    
}
