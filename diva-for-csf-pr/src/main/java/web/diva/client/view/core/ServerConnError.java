/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;

/**
 *
 * @author Yehia Farag
 */
public class ServerConnError {
 private final String SERVER_ERROR = "An error occurred while attempting to contact the server, Reload DiVA?";
    public void connError() {
        BooleanCallback ok = new BooleanCallback() {

            @Override
            public void execute(Boolean value) {
                if (value) {
                    Window.Location.reload();
                }
            }
        };
        SC.confirm(SERVER_ERROR, ok);

    }

}
