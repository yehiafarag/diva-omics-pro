package web.diva.client.pca.view;

import web.diva.shared.unused.PCAPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

/**
 * Setting btn for PCA panel
 *
 * @author Yehia Farag
 */
public class PCABtn extends Label {

    private final PCAPanel pcaPanel;

    /**
     * @param colsNames principal components analysis label names
     */
    public PCABtn(String[] colsNames) {
        super("PCA");
        pcaPanel = new PCAPanel(colsNames);
        this.addStyleName("clickable");
        this.setHeight("20px");
        this.setWidth("PCA".length() * 6 + "px");
        this.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getPcaPanel().show();
            }
        });

    }

    public PCAPanel getPcaPanel() {
        return pcaPanel;
    }

    public void setClickListener(com.smartgwt.client.widgets.events.ClickHandler handler) {
        pcaPanel.getOkBtn().addClickHandler(handler);
    }

}
