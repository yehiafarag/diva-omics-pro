/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.profileplot.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.util.Page;

import web.diva.client.DivaServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.client.view.core.InfoIcon;
import web.diva.client.view.core.SaveAsPanel;

/**
 *
 * @author Yehia Farag line chart component
 */
public class ProfilePlotComponent extends ModularizedListener {

    private SelectionManager selectionManager;
    private VerticalPanel mainBodyLayout;
    private final Image profilePlotMaxImage;
    private final Image thumbImage;
    private DivaServiceAsync GWTClientService;
    private final HandlerRegistration lablePopupClickHandlerReg, imagePopupClickHandlerReg;

    private final VerticalPanel thumbImageLayout;
    private final HorizontalPanel topLayout;

    private final int profilePlotPanelWidth;
    private final int profilePlotPanelHeight;

    /**
     *
     * @param selectionManager main central manager
     * @param results main image url
     * @param divaService diva GWTClientService
     * @param imgHeight
     * @param imgWidth
     * @param midPanelWidth the width of the mid panel
     *
     *
     */
    public ProfilePlotComponent(String results, SelectionManager selectionManager, DivaServiceAsync divaService, final int imgHeight, final int imgWidth, int midPanelWidth) {
        this.GWTClientService = divaService;
        this.classtype = 1;
        this.components.add(ProfilePlotComponent.this);
        this.selectionManager = selectionManager;
        this.selectionManager.addSelectionChangeListener(ProfilePlotComponent.this);

        int newWidth = (midPanelWidth / 2);
        profilePlotPanelWidth = newWidth;
        profilePlotPanelHeight = newWidth + 22;
        mainBodyLayout = new VerticalPanel();
        mainBodyLayout.setHeight(profilePlotPanelHeight + "px");
        mainBodyLayout.setWidth(profilePlotPanelWidth + "px");
        mainBodyLayout.setStyleName("profileplot");

        topLayout = new HorizontalPanel();
        mainBodyLayout.add(topLayout);
        topLayout.setWidth(profilePlotPanelWidth + "px");
        topLayout.setHeight("18px");
        topLayout.setStyleName("whiteLayout");
        Label title = new Label("Profile Plot");
        title.setStyleName("labelheader");
        topLayout.add(title);
        title.setWidth("70px");
        topLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);

        HorizontalPanel btnsPanel = new HorizontalPanel();
        btnsPanel.setWidth("34px");
        btnsPanel.setHeight("20px");
        btnsPanel.add(new InfoIcon("Profile Plot", initInfoLayout(160,600),160,600));
        
          Label maxmizeBtn = new Label();
        maxmizeBtn.addStyleName("maxmize");
        maxmizeBtn.setHeight("16px");
        maxmizeBtn.setWidth("16px");
        btnsPanel.add(maxmizeBtn);
        maxmizeBtn.setHorizontalAlignment(Label.ALIGN_RIGHT);
        btnsPanel.setCellHorizontalAlignment(maxmizeBtn, HorizontalPanel.ALIGN_RIGHT);
        
        topLayout.add(btnsPanel);
        topLayout.setCellHorizontalAlignment(btnsPanel, HorizontalPanel.ALIGN_RIGHT);
        
        
        
      
        
        
        
        

        /**
         * ** end of top layout *****
         */
        final PopupPanel imagePopup = new PopupPanel(true, true);
        imagePopup.setAnimationEnabled(true);
        imagePopup.ensureDebugId("cwBasicPopup-imagePopup");
        ClickHandler popupClickHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                imagePopup.center();
                imagePopup.show();
            }
        };

        lablePopupClickHandlerReg = maxmizeBtn.addClickHandler(popupClickHandler);

        profilePlotMaxImage = new Image(results);

        int updatedImgWidth = calcMaxImgResize();
        int maxHeight = updatedImgWidth + 50;
        int maxWidth = updatedImgWidth + 2;
        profilePlotMaxImage.setWidth(updatedImgWidth + "px");
        profilePlotMaxImage.setHeight(updatedImgWidth + "px");

        VerticalPanel popupLayout = new VerticalPanel();
        popupLayout.setHeight((maxHeight) + "px");
        popupLayout.setWidth(maxWidth + "px");
        popupLayout.setStyleName("modalLayout");

        HorizontalPanel maxTopLayout = new HorizontalPanel();
        maxTopLayout.setWidth(maxWidth + "px");
        maxTopLayout.setHeight("18px");
        maxTopLayout.setSpacing(3);
        maxTopLayout.setStyleName("whiteLayout");

        Label maxTitle = new Label("Profile Plot");
        maxTitle.setStyleName("labelheader");
        maxTitle.setWidth((maxWidth - 150) + "px");

        Label saveBtn = new Label();
        saveBtn.addStyleName("save");
        saveBtn.setHeight("16px");
        saveBtn.setWidth("16px");

        Label minmizeBtn = new Label();
        minmizeBtn.addStyleName("minmize");
        minmizeBtn.setHeight("16px");
        minmizeBtn.setWidth("16px");

        saveBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SelectionManager.Busy_Task(true, false);
//                Window.open(profilePlotMaxImage.getUrl(), "downlodwindow", "status=0,toolbar=0,menubar=0,location=0");
                String quality = "normal";
                GWTClientService.exportImgAsPdf("Profile_Plot", quality, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("ERROR IN SERVER CONNECTION");
                        SelectionManager.Busy_Task(false, false);
                    }

                    @Override
                    public void onSuccess(String result) {
                        SaveAsPanel sa = new SaveAsPanel("Profile Plot Image", result);
                        sa.center();
                        sa.show();
                        SelectionManager.Busy_Task(false, false);
                    }
                });
            }
        });

        maxTopLayout.add(maxTitle);
        maxTopLayout.setCellHorizontalAlignment(maxTitle, HorizontalPanel.ALIGN_LEFT);
        maxTopLayout.add(new InfoIcon("Profile Plot", initInfoLayout(160,600),160,600));
        maxTopLayout.add(saveBtn);
        maxTopLayout.setCellHorizontalAlignment(saveBtn, HorizontalPanel.ALIGN_RIGHT);
        maxTopLayout.add(minmizeBtn);
        maxTopLayout.setCellHorizontalAlignment(minmizeBtn, HorizontalPanel.ALIGN_RIGHT);
        popupLayout.add(maxTopLayout);
        popupLayout.add(profilePlotMaxImage);
        imagePopup.setWidget(popupLayout);

        minmizeBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                imagePopup.hide(true);
            }
        });

        thumbImage = new Image(results);
        thumbImage.ensureDebugId("cwBasicPopup-thumb");
        thumbImage.addStyleName("clickableImg");

        imagePopupClickHandlerReg = thumbImage.addClickHandler(popupClickHandler);

        thumbImageLayout = new VerticalPanel();

        thumbImageLayout.setHeight((newWidth) + "px");
        thumbImageLayout.setWidth((profilePlotPanelWidth) + "px");

        thumbImage.setWidth((newWidth - 2) + "px");
        thumbImage.setHeight((newWidth - 2) + "px");
        thumbImageLayout.add(thumbImage);
        thumbImageLayout.setCellHorizontalAlignment(thumbImage, VerticalPanel.ALIGN_CENTER);
        thumbImageLayout.setCellVerticalAlignment(thumbImage, VerticalPanel.ALIGN_MIDDLE);

        mainBodyLayout.add(thumbImageLayout);

        Selection sel = selectionManager.getSelectedRows();
        if (sel != null) {
            final int[] selectedRows = sel.getMembers();
            Timer t = new Timer() {
                @Override
                public void run() {
                    updateSelection(selectedRows);
                }
            };
            // Schedule the timer to run once in 0.5 seconds.
            t.schedule(500);

        }

    }

    @Override
    public String toString() {

        return "ProfilePlot";
    }

    /**
     * This method is the listener implementation for the central manager the
     * method responsible for the component notification there is selection
     * event
     *
     * @param type Selection.TYPE row or column
     *
     */
    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows();
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                this.updateSelection(selectedRows);
            } else {
            }
        }
    }

    /**
     * This method is responsible for calculates the max image size( image width
     * and height)
     */
    @SuppressWarnings("UnnecessaryBoxing")
    private int calcMaxImgResize() {
        int width = Page.getScreenHeight() - 200;
        return width;

    }

    /**
     * This method is responsible for updating chart with the user selected
     * indexes
     *
     * @param selectedIndices selected data indexes
     *
     */
    private void updateSelection(int[] selection) {
        if (selection != null) {// && selection.length > 0) {
            GWTClientService.updateProfilePlotSelection(selection, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("ERROR IN SERVER CONNECTION");
                }

                @Override
                public void onSuccess(String result) {
                    profilePlotMaxImage.setUrl(result);
                    thumbImage.setUrl(result);
                }
            });
        }

    }

    /**
     *
     * @return Profile plot main body layout
     *
     */
    public VerticalPanel getLayout() {
        return mainBodyLayout;
    }

    /**
     * This method is responsible for cleaning on removing the component from
     * the container
     */
    @Override
    public void remove() {
        imagePopupClickHandlerReg.removeHandler();
        lablePopupClickHandlerReg.removeHandler();
        GWTClientService = null;
        selectionManager.removeSelectionChangeListener(ProfilePlotComponent.this);
        selectionManager = null;
        mainBodyLayout = null;

    }

    @Deprecated
    private void getSelection(int startX, int startY) {
        SelectionManager.Busy_Task(true, false);
        GWTClientService.getProfilePlotSelection(startX, startY, new AsyncCallback<int[]>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("ERROR IN SERVER CONNECTION");
            }

            @Override
            public void onSuccess(int[] result) {
                updateSelection(result);
                Selection selection = new Selection(Selection.TYPE.OF_ROWS, result);
                selectionManager.setSelectedRows(selection);

            }
        });
    }

    private VerticalPanel initInfoLayout(int h,int w) {
        VerticalPanel infopanel = new VerticalPanel();
        infopanel.setWidth(w+"px");
        infopanel.setHeight(h+"px");

        HTML information = new HTML("<p style='margin-left:30px;font-size:14px;line-height: 150%;'>Click the maximize icon <img src='images/maxmize.png' alt='' style='width:auto;height:16px'/> to increase the size of the plot.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>In the maximized mode one can export the profile plot as PDF by clicking the save icon <img src='images/icon_save.gif' alt='' style='width:auto;height:16px'/>.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>Please note that the current version of DiVA does not support user selection for the profile plots.</p>"
                + "<p align=\"right\" style='margin-left:30px;font-size:14px;line-height: 150%; float: right;'><i>Full tutorial available <a target=\"_blank\" href='" +"tutorial/diva_tutorial.pdf" + "'>here</a>.</i></p>");

        infopanel.add(information);


        return infopanel;

    }
}
