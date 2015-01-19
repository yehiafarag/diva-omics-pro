/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.profileplot.view;


import com.smartgwt.client.widgets.layout.VLayout;  


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


import web.diva.client.DivaServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;

/**
 *
 * @author Yehia Farag line chart component
 */
public class ProfilePlotComponent extends ModularizedListener {

    private SelectionManager selectionManager;
    private VLayout thumbLayout;
    private final Image profilePlotMaxImage;
    private final Image thumbImage;
    private DivaServiceAsync greetingService;
    private final int height = 212;
    private final int width = 250;

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

    public ProfilePlotComponent(String results, SelectionManager selectionManager, DivaServiceAsync greetingService) {
        this.greetingService = greetingService;
        this.classtype = 3;
        this.components.add(ProfilePlotComponent.this);
        this.selectionManager = selectionManager;

        this.selectionManager.addSelectionChangeListener(ProfilePlotComponent.this);
        thumbLayout = new VLayout();
        thumbLayout.setHeight("46%");
        thumbLayout.setWidth("25%");
        thumbLayout.setMargin(0);
//        thumbLayout.setBorder("1px solid #E6E6E6");
        thumbLayout.setStyleName("profileplot");

        HorizontalPanel topLayout = new HorizontalPanel();
        thumbLayout.addMember(topLayout);
        topLayout.setWidth("100%");
        topLayout.setHeight("18px");
        topLayout.setStyleName("whiteLayout");
        Label title = new Label("Profile Plot");
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth("90%");
        topLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);
       

        Label maxmizeBtn = new Label();
        maxmizeBtn.addStyleName("maxmize");
        maxmizeBtn.setHeight("16px");
        maxmizeBtn.setWidth("16px");
        topLayout.add(maxmizeBtn);
        maxmizeBtn.setHorizontalAlignment(Label.ALIGN_RIGHT);
        topLayout.setCellHorizontalAlignment(maxmizeBtn, HorizontalPanel.ALIGN_RIGHT);
        
        

        final PopupPanel imagePopup = new PopupPanel(true, true);
        imagePopup.setAnimationEnabled(true);
        imagePopup.ensureDebugId("cwBasicPopup-imagePopup");
        maxmizeBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                imagePopup.center();
                imagePopup.show();
            }
        });

        
        
     
        
        
        
        
        
        
        
        
        
      

        profilePlotMaxImage = new Image(results);
        
        int maxHeight = 700;//(profilePlotMaxImage.getHeight());
        int maxWidth = 900;//profilePlotMaxImage.getWidth();
        
        profilePlotMaxImage.setHeight(maxHeight+"px");
        profilePlotMaxImage.setWidth(maxWidth+"px");
        VerticalPanel popupLayout = new VerticalPanel();
        popupLayout.setHeight((maxHeight+23)+"px");
        popupLayout.setWidth(maxWidth+"px");
        
        HorizontalPanel maxTopLayout = new HorizontalPanel();
        popupLayout.add(maxTopLayout);
        
        maxTopLayout.setWidth(maxWidth + "px");
        maxTopLayout.setHeight("18px");
        maxTopLayout.setSpacing(3);
        maxTopLayout.setStyleName("whiteLayout");
        Label maxTitle = new Label("Profile Plot");
        maxTitle.setStyleName("labelheader");
        maxTopLayout.add(maxTitle);

        maxTitle.setWidth((maxWidth - 150) + "px");
        maxTopLayout.setCellHorizontalAlignment(maxTitle, HorizontalPanel.ALIGN_LEFT);
        
        Label saveBtn = new Label();
        saveBtn.addStyleName("save");
        saveBtn.setHeight("16px");
        saveBtn.setWidth("16px");
      
        maxTopLayout.add(saveBtn);
        maxTopLayout.setCellHorizontalAlignment(saveBtn, HorizontalPanel.ALIGN_RIGHT);
        Label minmizeBtn = new Label();
        minmizeBtn.addStyleName("minmize");
        minmizeBtn.setHeight("16px");
        minmizeBtn.setWidth("16px");
        maxTopLayout.add(minmizeBtn);
          maxTopLayout.setCellHorizontalAlignment(minmizeBtn, HorizontalPanel.ALIGN_RIGHT);
        
        saveBtn.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
//                Window.open(profilePlotMaxImage.getUrl(), "downlodwindow", "status=0,toolbar=0,menubar=0,location=0");
                Window.open(profilePlotMaxImage.getUrl(), "Download Image", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=no,toolbar=true, width=" + Window.getClientWidth() + ",height=" + Window.getClientHeight());
                
            }
        });
//        
        popupLayout.add(profilePlotMaxImage);
        imagePopup.setWidget(popupLayout);
        
        popupLayout.setStyleName("modalLayout");
        
        minmizeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                imagePopup.hide(true);
            }
        });
        

        thumbImage = new Image(results);
        thumbImage.ensureDebugId("cwBasicPopup-thumb");
        thumbImage.addStyleName("cw-BasicPopup-thumb");

        thumbLayout.addMember(thumbImage);
        
        thumbImage.setHeight("100%");
        thumbImage.setWidth("100%");
//       
//        thumbLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//        thumbLayout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        Selection sel = selectionManager.getSelectedRows();
        if (sel != null) {
            int[] selectedRows = sel.getMembers();
            this.updateSelection(selectedRows);
        }

    }

    private void updateSelection(int[] selection) {
        selectionManager.busyTask(true,false);
        if (selection != null && selection.length > 0) {
            greetingService.updateLineChartSelection(selection, width, height, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    
                        Window.alert("ERROR IN SERVER CONNECTION");
                        selectionManager.busyTask(false,false);
                }

                @Override
                public void onSuccess(String result) {
                    profilePlotMaxImage.setUrl(result);
                    thumbImage.setUrl(result);
                    selectionManager.busyTask(false,false);
                }
            });
        }

    }

    public VLayout getLayout() {
        return thumbLayout;
    }

    @Override
    public void remove() {
        greetingService = null;
        selectionManager.removeSelectionChangeListener(ProfilePlotComponent.this);
        selectionManager = null;
        thumbLayout = null;

    }
    
   
}
