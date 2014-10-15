/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.linechart.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import web.diva.client.GreetingServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;

/**
 *
 * @author Yehia Farag
 * line chart component
 */
public class ProfilePlotComponent extends ModularizedListener {

    private SelectionManager selectionManager;
    private VerticalPanel layout;
    private Image image;
    private GreetingServiceAsync greetingService;
    private final int height = 212;
    private final int width =220;

    @Override
    public String toString() {

        return "LineChart";
    }

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows();
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                this.updateSelection(selectedRows);
            }
        }
    }

    public ProfilePlotComponent(String results, SelectionManager selectionManager, GreetingServiceAsync greetingService) {
        this.greetingService = greetingService;
        this.classtype = 3;
        this.components.add(ProfilePlotComponent.this);
        this.selectionManager = selectionManager;

        this.selectionManager.addSelectionChangeListener(ProfilePlotComponent.this);
        layout = new VerticalPanel();        
        layout.setHeight("" + height + "px");
        layout.setWidth("" + width + "px");
        layout.setBorderWidth(1);
        this.image = new Image(results);

        image.setHeight("" + height + "px");
        image.setWidth(width + "px");
        layout.add(image);
        layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        Selection sel = selectionManager.getSelectedRows();
        if (sel != null) {
            int[] selectedRows = sel.getMembers();
            this.updateSelection(selectedRows);
        }

    }

    private void updateSelection(int[] selection) {
        if (selection != null && selection.length > 0) {
            RootPanel.get("loaderImage").setVisible(true);
            greetingService.updateLineChartSelection(selection, width, height, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    RootPanel.get("loaderImage").setVisible(false);
                }

                @Override
                public void onSuccess(String result) {
                    image.setUrl(result);
                    RootPanel.get("loaderImage").setVisible(false);
                }
            });
        }

    }

    public VerticalPanel getLayout() {
        return layout;
    }

    @Override
    public void remove() {

        greetingService = null;
        selectionManager.removeSelectionChangeListener(ProfilePlotComponent.this);
        selectionManager = null;

        layout = null;
        image = null;

    }
}
