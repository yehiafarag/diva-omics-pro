/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.profileplot.view;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
//import com.google.gwt.user.client.ui.RootPanel;
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
    private final Image image;
    private final Image thumbImage;
    private GreetingServiceAsync greetingService;
    private final int height = 212;
    private final int width =250;

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

    public ProfilePlotComponent(String results, SelectionManager selectionManager, GreetingServiceAsync greetingService, int height,int width) {
        this.greetingService = greetingService;
        this.classtype = 3;
        this.components.add(ProfilePlotComponent.this);
        this.selectionManager = selectionManager;

        this.selectionManager.addSelectionChangeListener(ProfilePlotComponent.this);
        layout = new VerticalPanel();   
        layout.setHeight(height+"px");
        layout.setWidth(width+"px");
        image = new Image(results);
       
        final PopupPanel imagePopup = new PopupPanel(true, true);
        imagePopup.setAnimationEnabled(true);
        imagePopup.ensureDebugId("cwBasicPopup-imagePopup");
        imagePopup.setWidget(image);
        
        image.addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                imagePopup.hide();
            }
        }, MouseOutEvent.getType());

        thumbImage = new Image(results);
        thumbImage.setStyleName("magnifying");
        thumbImage.ensureDebugId("cwBasicPopup-thumb");
        thumbImage.addStyleName("cw-BasicPopup-thumb");
        thumbImage.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
                imagePopup.center();
                imagePopup.show(); //To change body of generated methods, choose Tools | Templates.
            }
        });
        layout.add(thumbImage);
        
        thumbImage.setHeight(height+"px");
        thumbImage.setWidth(width+"px");
       

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
//            RootPanel.get("loaderImage").setVisible(true);
            greetingService.updateLineChartSelection(selection, width, height, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
//                    RootPanel.get("loaderImage").setVisible(false);
                }

                @Override
                public void onSuccess(String result) {
                    image.setUrl(result);
                    thumbImage.setUrl(result);
//                    RootPanel.get("loaderImage").setVisible(false);
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

    }
}
