package web.diva.client.rank.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
//import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.List;
import web.diva.client.DivaServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.client.unused.RankPanel;
import web.diva.client.view.core.InfoIcon;
import web.diva.client.view.core.SaveAsPanel;
import web.diva.shared.beans.DivaGroup;
import web.diva.shared.beans.RankResult;

/**
 * Ranking Analysis component
 *
 * @author Yehia Farag
 */
public class RankTablesComponent extends ModularizedListener implements IsSerializable, com.smartgwt.client.widgets.events.ClickHandler {

    private final VLayout mainRankLayout;
    private final RankTableLayout minRankTable;
    private RankTableLayout maxRankTable;
    private final PopupPanel tablePopup;
    private final Label maxBtn;
    private final Label minBtn;
    private final RankSettingBtn minSettingBtn, maxSettingsBtn;
    private DivaServiceAsync GWTClientService;
    private SelectionManager Selection_Manager;
    private final RankPanel rankPanel;
    private final HandlerRegistration minSaveReg, maxmizeReg, minmizReg, maxSettingsReg, minSettingsReg, showSelectedReg;
    private HandlerRegistration maxShowASelectedReg, maxSaveReg;
    private CheckBox maxShowSelectedOnlyBtn, showSelectedOnlyBtn;
//    private RankResult rankResults;

    /**
     *
     * @param Selection_Manager main central manager
     * @param results ranking information
     * @param colGroupsList list of column groups
     * @param divaService diva GWTClientService
     */
    public RankTablesComponent(DivaServiceAsync divaService, final SelectionManager Selection_Manager, final RankResult results, List<DivaGroup> colGroupsList) {
//        this.rankResults = results;
        this.classtype = 3;
        this.components.add(RankTablesComponent.this);
        this.GWTClientService = divaService;
        this.Selection_Manager = Selection_Manager;
        Selection_Manager.addSelectionChangeListener(RankTablesComponent.this);
        this.rankPanel = new RankPanel();
        mainRankLayout = new VLayout();
        mainRankLayout.setHeight("100%");
        mainRankLayout.setWidth("100%");
        mainRankLayout.setStyleName("rank");
        mainRankLayout.setMargin(0);
        HorizontalPanel topLayout = new HorizontalPanel();
        mainRankLayout.addMember(topLayout);
        topLayout.setWidth("100%");
        topLayout.setHeight("18px");
        Label title = new Label("Rank Product (Differential Expression)");
        title.setStyleName("labelheader");

        title.setWidth("98%");
        topLayout.add(title);
        title.setHeight("18px");
        topLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);
        topLayout.setCellVerticalAlignment(title, HorizontalPanel.ALIGN_TOP);

        HorizontalPanel btnsLayout = new HorizontalPanel();
        btnsLayout.setWidth("85px");
        btnsLayout.setHeight("18px");
        btnsLayout.setSpacing(1);
        topLayout.add(btnsLayout);
        topLayout.setCellHorizontalAlignment(btnsLayout, HorizontalPanel.ALIGN_RIGHT);
        topLayout.setCellVerticalAlignment(btnsLayout, HorizontalPanel.ALIGN_TOP);

        minSettingBtn = new RankSettingBtn(GWTClientService, rankPanel);
        btnsLayout.add(minSettingBtn);
        btnsLayout.setCellHorizontalAlignment(minSettingBtn, HorizontalPanel.ALIGN_RIGHT);

        Label minSaveBtn = new Label();
        minSaveBtn.addStyleName("save");
        minSaveBtn.setHeight("16px");
        minSaveBtn.setWidth("16px");

        btnsLayout.add(minSaveBtn);
        btnsLayout.setCellHorizontalAlignment(minSaveBtn, HorizontalPanel.ALIGN_RIGHT);

        final ClickHandler exportRankHandler = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                int[] selection = new int[]{};
                if (showSelectedOnlyBtn.getValue()) {
                    selection = minRankTable.getIndexSelection();
                }
                exportRank(selection);
            }
        };
        minSaveReg = minSaveBtn.addClickHandler(exportRankHandler);
        InfoIcon icon = new InfoIcon("Rank Product", initInfoLayout(310,600),310,600);
        btnsLayout.add(icon);
        btnsLayout.setCellHorizontalAlignment(icon, HorizontalPanel.ALIGN_RIGHT);
        
        
        maxBtn = new Label();
        btnsLayout.add(maxBtn);
        maxBtn.addStyleName("maxmize");
        maxBtn.setHeight("16px");
        maxBtn.setWidth("16px");

        btnsLayout.setCellHorizontalAlignment(maxBtn, HorizontalPanel.ALIGN_RIGHT);
        btnsLayout.setCellVerticalAlignment(maxBtn, HorizontalPanel.ALIGN_TOP);

        maxSettingsBtn = new RankSettingBtn(GWTClientService, rankPanel);

        minBtn = new Label();
        minBtn.addStyleName("minmize");
        minBtn.setHeight("16px");
        minBtn.setWidth("16px");

        maxmizeReg = maxBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (maxRankTable == null) {
                    VerticalPanel maxmizeTableLayout = new VerticalPanel();
                   

                    maxRankTable = new RankTableLayout(Selection_Manager, results);
                    maxRankTable.setWidth("80%");
                    maxRankTable.setHeight("80%"); 
                    int maxWidth = maxRankTable.getOffsetWidth();
                    
                    maxmizeTableLayout.setHeight((maxRankTable.getOffsetHeight()+72)+ "px");
                    maxmizeTableLayout.setWidth( maxWidth+ "px");

                    HorizontalPanel maxTopLayout = new HorizontalPanel();
                    maxTopLayout.setWidth(maxWidth + "px");
                    maxTopLayout.setHeight("20px");
                    maxTopLayout.setSpacing(2);
                    Label title = new Label();
                    title.setText("Rank Product (Differential Expression)");
                    title.setStyleName("labelheader");
                    title.setWidth((maxWidth - 100) + "px");

                    maxTopLayout.add(title);
                    maxTopLayout.add(maxSettingsBtn);
                     maxTopLayout.setCellHorizontalAlignment(maxSettingsBtn, HorizontalPanel.ALIGN_RIGHT);
                    Label maxSaveBtn = new Label();
                    maxSaveBtn.addStyleName("save");
                    maxSaveBtn.setHeight("16px");
                    maxSaveBtn.setWidth("16px");

                    maxTopLayout.add(maxSaveBtn);
                    maxTopLayout.setCellHorizontalAlignment(maxSaveBtn, HorizontalPanel.ALIGN_RIGHT);
                    maxSaveReg = maxSaveBtn.addClickHandler(exportRankHandler);
                    
                    InfoIcon maxIcon = new InfoIcon("Rank Product", initInfoLayout(310,600),310,600);
                    maxTopLayout.add(maxIcon);
                    maxTopLayout.setCellHorizontalAlignment(maxIcon, HorizontalPanel.ALIGN_RIGHT);
                    
                    maxTopLayout.add(minBtn);
                    maxTopLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);
                    maxTopLayout.setCellHorizontalAlignment(minBtn, HorizontalPanel.ALIGN_RIGHT);
                    maxTopLayout.setCellHorizontalAlignment(maxSettingsBtn, HorizontalPanel.ALIGN_RIGHT);
                    
                    maxmizeTableLayout.add(maxTopLayout);
                    maxmizeTableLayout.setCellHorizontalAlignment(maxTopLayout, VerticalPanel.ALIGN_CENTER);

                    maxmizeTableLayout.add(maxRankTable);

                    HorizontalPanel maxBottomLayout = new HorizontalPanel();
                    maxmizeTableLayout.add(maxBottomLayout);
                    maxBottomLayout.setWidth("100%");
                    maxBottomLayout.setHeight("50px");
                    maxShowSelectedOnlyBtn = new CheckBox("Show Selected Data Only");  
                    maxShowSelectedOnlyBtn.setStyleName("boldLabel");
                    maxShowSelectedOnlyBtn.setValue(showSelectedOnlyBtn.getValue());
                    maxShowASelectedReg = maxShowSelectedOnlyBtn.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            showSelectedOnlyBtn.setValue(((CheckBox) event.getSource()).getValue());
                            minRankTable.showSelectedOnly(((CheckBox) event.getSource()).getValue());
                            maxRankTable.showSelectedOnly(((CheckBox) event.getSource()).getValue());
                            maxRankTable.draw();
                        }
                    });
                    maxBottomLayout.add(maxShowSelectedOnlyBtn);
                    maxBottomLayout.setCellHorizontalAlignment(maxShowSelectedOnlyBtn, VerticalPanel.ALIGN_LEFT);
                    maxBottomLayout.setCellVerticalAlignment(maxShowSelectedOnlyBtn, VerticalPanel.ALIGN_MIDDLE);
                    tablePopup.setWidget(maxmizeTableLayout);
                    maxmizeTableLayout.setStyleName("modalLayout");
                    selectionChanged(Selection.TYPE.OF_ROWS);
                    
               
                }
                maxShowSelectedOnlyBtn.setValue(showSelectedOnlyBtn.getValue()); 
                maxRankTable.showSelectedOnly(showSelectedOnlyBtn.getValue());
               
                
                tablePopup.center();
                tablePopup.show(); 
                
            }
        });

        minmizReg = minBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showSelectedOnlyBtn.setValue(maxShowSelectedOnlyBtn.getValue());
                minRankTable.showSelectedOnly(maxShowSelectedOnlyBtn.getValue());
                tablePopup.hide();
            }
        });

        minRankTable = new RankTableLayout(Selection_Manager, results);
        mainRankLayout.addMember(minRankTable);
        HorizontalPanel bottomLayout = new HorizontalPanel();
        mainRankLayout.addMember(bottomLayout);
        minRankTable.setHeight("100%");
        minRankTable.draw();
        bottomLayout.setWidth("100%");
        bottomLayout.setHeight("50px");
        
        showSelectedOnlyBtn = new CheckBox("Show Selected Data Only");
        showSelectedOnlyBtn.setStyleName("boldLabel");
       
        showSelectedOnlyBtn.setValue(false);
        showSelectedReg = showSelectedOnlyBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (maxShowSelectedOnlyBtn != null) {
                    maxShowSelectedOnlyBtn.setValue(((CheckBox) event.getSource()).getValue());
                    maxRankTable.showSelectedOnly(((CheckBox) event.getSource()).getValue());
                }
                minRankTable.showSelectedOnly(((CheckBox) event.getSource()).getValue());
            }
        });

        bottomLayout.add(showSelectedOnlyBtn);
        bottomLayout.setCellHorizontalAlignment(showSelectedOnlyBtn, VerticalPanel.ALIGN_LEFT);
        bottomLayout.setCellVerticalAlignment(showSelectedOnlyBtn, VerticalPanel.ALIGN_MIDDLE);

        tablePopup = new PopupPanel(false, true);
        tablePopup.setAnimationEnabled(true);
        tablePopup.ensureDebugId("cwBasicPopup-imagePopup");
        
        
        
         tablePopup.hide(true);

        ClickHandler rankInputPanelListener = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (tablePopup != null) {
                    tablePopup.hide(true);
                }
            }
        };
        maxSettingsReg = maxSettingsBtn.addClickHandler(rankInputPanelListener);
        minSettingsReg = minSettingBtn.addClickHandler(rankInputPanelListener);

        minSettingBtn.setClickListener(RankTablesComponent.this);
        maxSettingsBtn.setClickListener(RankTablesComponent.this);
        selectionChanged(Selection.TYPE.OF_ROWS);

    }

    /**
     *
     * @return Ranking main body layout
     */
    public VLayout getMainRankLayout() {
        return mainRankLayout;
    }

    /**
     * on click method for ranking panel listener
     *
     * @param event
     */
    @Override
    public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
        if (rankPanel.isShowing()) {
            maxSettingsBtn.setErrorlablVisible(false);
            List<String> groups = maxSettingsBtn.getSelectColGroups();
            String seed = maxSettingsBtn.getSeed();
            String perm = maxSettingsBtn.getPerm();
            String log2 = maxSettingsBtn.getLog2();

            if (groups == null || groups.isEmpty() || groups.size() > 2 || seed == null || seed.equals("") || perm == null || perm.equals("")) {
                maxSettingsBtn.setErrorlablVisible(true);
                maxSettingsBtn.rankPanelvalidate();
            } else {
                viewRankTables(perm, seed, groups, log2, maxSettingsBtn.isDefaultRank());
                maxSettingsBtn.hidePanel();
            }
        }
    }

    /**
     * This method is responsible for invoking the ranking analysis method on
     * the server side
     *
     * @param perm perm value in string format
     * @param seed seed value in string format
     * @param colGropNames list of groups name used for ranking comparison
     * @param log2
     * @param defaultRank is the rank is default for the dataset (will be loaded
     * automatically on loading dataset)
     * @param asyncCallback RankResult that have all information required for
     * initializing ranking tables
     *
     */
    private void viewRankTables(String perm, String seed, List<String> colGropNames, String log2, boolean defaultRank) {
        SelectionManager.Busy_Task(true, true);
        GWTClientService.computeRank(perm, seed, colGropNames, log2, defaultRank,
                new AsyncCallback<RankResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Selection_Manager.connError();
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(RankResult result) {
//                        rankResults = result;
                        minRankTable.updateRecords(result);
                        if (maxRankTable != null) {
                            maxRankTable.updateRecords(result);
                        }
                        selectionChanged(Selection.TYPE.OF_ROWS);
                        SelectionManager.Busy_Task(false, true);

                    }
                });

    }

    /**
     * This method is responsible for exporting dataset ranking results in
     * tab-based format based on groups or selected data
     *
     * @param selection indexes of exported data
     *
     */
    private void exportRank(int[] selection) {
        SelectionManager.Busy_Task(true, true);

        GWTClientService.exportRankingData(selection, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Selection_Manager.connError();
                SelectionManager.Busy_Task(false, true);
            }

            @Override
            public void onSuccess(String result) {
                SaveAsPanel sa = new SaveAsPanel("Rank Product File ", result);
                SelectionManager.Busy_Task(false, true);
                sa.center();
                sa.show();
                SelectionManager.Busy_Task(false, true);

            }
        });

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
    public final void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = Selection_Manager.getSelectedRows();
            if (sel != null) {// && sel.getMembers().length > 0) {
                minRankTable.updateTable(sel.getMembers());
                if (maxRankTable != null) {
                    maxRankTable.updateTable(sel.getMembers());
                }
            }
        }

    }

    @Override
    public String toString() {
        return "RankTable";

    }

    /**
     * This method is responsible for cleaning on removing the component from
     * the container
     */
    @Override
    public void remove() {
        maxmizeReg.removeHandler();
        minmizReg.removeHandler();
        maxSettingsReg.removeHandler();
        minSettingsReg.removeHandler();
        showSelectedReg.removeHandler();
        minSaveReg.removeHandler();
        if (maxRankTable != null) {
            maxRankTable.remove();
            maxShowASelectedReg.removeHandler();
            maxSaveReg.removeHandler();
        }
        minRankTable.remove();
        Selection_Manager.removeSelectionChangeListener(this);
        Selection_Manager = null;
        GWTClientService = null;

    }

    private VerticalPanel initInfoLayout(int h, int w) {
        VerticalPanel infopanel = new VerticalPanel();
        infopanel.setWidth(w + "px");
        infopanel.setHeight(h + "px");
        infopanel.setStyleName("whiteLayout");

        HTML information = new HTML(
                "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>Click the maximize icon <img src='images/maxmize.png' alt='' style='width:auto;height:16px'/> to increase the size of the table. Use the maximized state to get better visualization.</p>."
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>The Rank Product Table supports single and multiple row selection using mouse clicks for single selection and select and drag for multiple selection.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>The Rank Product parameters can be changed by clicking the setting icon <img src='images/setting.gif' alt='' style='width:auto;height:16px'/>.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>Filter the Rank Product Table using the Show Selected Data Only checkbox in the lower left corner.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>Sort the table by clicking the desired column header.</p>"
                + "<p style='margin-left:30px;font-size:14px;line-height: 150%;'>Export the table content as a tabular file by clicking the save icon <img src='images/icon_save.gif' alt='' style='width:auto;height:16px'/>.</p>."
                 + "<p align=\"right\" style='margin-left:30px;font-size:14px;line-height: 150%;float: right;'><i>Full tutorial available <a target=\"_blank\" href='" +"tutorial/diva_tutorial.pdf" + "'>here</a>.</i></p>");


        infopanel.add(information);

        return infopanel;

    }

}
