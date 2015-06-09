/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view.core;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;

/**
 *
 * @author Yehia Farag
 * header layout (logo, drop down dataset list, and dataset information)
 */
public class HeaderLayout extends HorizontalPanel {

    private final ListBox lb;

    public ListBox getLb() {
        return lb;
    }

    public HeaderLayout(String width, String height) {

//        this.setWidth(width);
//        this.setHeight(height);
//        Image image = new Image("js/diva-logo.png");
//        image.setWidth("361px");
//        image.setHeight("26px");
//        this.add(image);
        lb = new ListBox();
        lb.setWidth("300px");
        lb.addItem("Select Dataset");
//        this.add(lb);
    }

}
