package web.diva.client.view.core;


import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Yehia Farag
 */
public  class Notification{
    
    private static final PopupPanel notification = new PopupPanel(true,true);
    private static final Label notificationMessage = new Label();

    private Notification() {
      
        
        
    }
    
    public static void notifi(String message) {
        notification.setAnimationEnabled(true);
        notification.setWidget(notificationMessage);
        int width =200;
        if((message.length()*10)  > 200)
            width = (message.length()*10) ;
        notification.setWidth(width+"px");
        notification.setStyleName("notification");
        notification.setHeight("100px");
        notificationMessage.setText(message);
        notification.show();
        notification.center();
        Timer t = new Timer() {
            @Override
            public void run() {
                notification.hide();
            }
        };

        // Schedule the timer to close the popup in 3 seconds.
        t.schedule(4000);

    }

    
}
