package web.diva.client.view.core;


import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.smartgwt.client.util.SC;

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
    
    private static final Timer  timer = new Timer() {

            @Override
            public void run() {
                SC.dismissCurrentDialog();
            }
        };

    private Notification() {
      
        
        
    }
    
    public static void notifi(String title,String message) {
        SC.warn(title, message.toUpperCase());
            // Schedule the timer to close the popup in 3 seconds.
        timer.schedule(3000);

        
   

    }

    
}
