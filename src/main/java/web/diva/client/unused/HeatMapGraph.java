/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.unused;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import web.diva.shared.beans.HeatMapImageResult;

/**
 *
 * @author Yehia Farag heat-map component container
 */
public class HeatMapGraph extends VerticalPanel {

    private final String width;
    private final Image image;

    public HeatMapGraph(final HeatMapImageResult imageResult, int width, double height) {
        this.image = new Image(imageResult.getImgString());
        this.width = ((double) width / 3.0) + "px";
        this.image.setWidth(this.width);
        this.image.setHeight(height + "px");
        final double colIndicator = ((double) width / 3.0) / (double) imageResult.getColNum();
        final double rowIndicator = height / (double) imageResult.getRowNum();
        final HTML toolTip = new HTML();
        toolTip.setVisible(false);
        RootPanel.get("tooltip").add(toolTip);
        this.image.addMouseMoveHandler(new MouseMoveHandler() {
            @Override
            public void onMouseMove(MouseMoveEvent event) {
                int y = (int) ((double) event.getY() / rowIndicator);
                int x = ((int) (event.getX() / colIndicator));
                if (x < imageResult.getColNames().length && y < imageResult.getGeneName().length) {
                    toolTip.setHTML("<p style='font-weight: bold; color:white;font-size: 15px;background: #819FF7; border-style:double;'>" + imageResult.getColNames()[x] + "<br/>" + imageResult.getGeneName()[y] + "</p>");
                    toolTip.setVisible(true);
                } else {
                    toolTip.setText("");
                    toolTip.setVisible(false);
                }
            }
        });
        this.image.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                toolTip.setText("");
                toolTip.setVisible(false);
            }
        });
        this.add(this.image);
        HTML scale = new HTML();
        scale.setWidth(this.width);
        String scaleCode = "<div align='center' ><b><font  color = " + imageResult.getMinColour() + " >" + (int) imageResult.getMinValue() + " &#8592; SC</font>A<font color = " + imageResult.getMaxColour() + " >LE &#8594;" + (int) imageResult.getMaxValue() + "</font></b> </div>";
        scale.setHTML(scaleCode);
        this.add(scale);

    }

}
