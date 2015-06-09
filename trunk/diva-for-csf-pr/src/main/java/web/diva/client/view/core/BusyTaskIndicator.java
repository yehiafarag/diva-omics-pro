/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 * @author Yehia Farag
 */
public class BusyTaskIndicator {

    private final PopupPanel progressPanel;

    public BusyTaskIndicator() {
        progressPanel = new PopupPanel(false, true);
        progressPanel.ensureDebugId("cwBasicPopup-imagePopup");

        VerticalPanel mainProgressPopupBodyLayout = new VerticalPanel();
        mainProgressPopupBodyLayout.setWidth(337 + "px");
        mainProgressPopupBodyLayout.setHeight(80 + "px");
        mainProgressPopupBodyLayout.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        mainProgressPopupBodyLayout.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);

        VerticalPanel progressImgLayout = new VerticalPanel();
        Image progressImage = new Image("images/progress.gif");
        progressImgLayout.add(progressImage);

//        progressImgLayout.setBorderWidth(1);
        mainProgressPopupBodyLayout.add(progressImgLayout);

        progressPanel.setWidget(mainProgressPopupBodyLayout);
        mainProgressPopupBodyLayout.setStyleName("progress");

    }

    private boolean busy=false;
    public void busyTask(boolean busy,boolean slow) {
        this.busy= busy;
        if (busy) {
            
        progressPanel.setAnimationEnabled(slow);
            progressPanel.center();
            progressPanel.show(); //To change body of generated methods, choose Tools | Templates.
        } else {
            progressPanel.hide();
        }
    }

    public boolean isBusy() {
        return busy;
    }

}
