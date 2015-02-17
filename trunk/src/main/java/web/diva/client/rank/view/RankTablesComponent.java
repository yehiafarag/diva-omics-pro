/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.rank.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.List;
import web.diva.client.DivaServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.client.unused.RankPanel;
import web.diva.shared.beans.DivaGroup;
import web.diva.shared.beans.RankResult;

/**
 *
 * @author Yehia Farag Ranking Tables Module
 */
public class RankTablesComponent extends ModularizedListener implements IsSerializable, com.smartgwt.client.widgets.events.ClickHandler {

    @Override
    public final void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows();
            if (sel != null && sel.getMembers().length > 0) {
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

    @Override
    public void remove() {
        maxmizeReg.removeHandler();
        minmizReg.removeHandler();
        maxSettingsReg.removeHandler();
        minSettingsReg.removeHandler();
        showSelectedReg.removeHandler();
        if (maxRankTable != null) {
            maxRankTable.remove();
            maxShowASelectedReg.removeHandler();
        }
        minRankTable.remove();
        selectionManager.removeSelectionChangeListener(this);
        selectionManager = null;
        GWTClientService = null;

    }

    private final VLayout mainRankLayout;
    private final RankTableLayout minRankTable;
    private RankTableLayout maxRankTable;

    private final PopupPanel tablePopup;
    private final Label maxBtn;
    private final Label minBtn;
    private final RankSettingBtn minSettingBtn, maxSettingsBtn;
    private DivaServiceAsync GWTClientService;
    private SelectionManager selectionManager;
    private final RankPanel rankPanel;
    private final HandlerRegistration maxmizeReg, minmizReg, maxSettingsReg, minSettingsReg, showSelectedReg;
    private HandlerRegistration  maxShowASelectedReg;
    private  CheckBox maxShowSelectedOnlyBtn,showSelectedOnlyBtn;

    public RankTablesComponent(DivaServiceAsync greetingService, final SelectionManager selectionManager, final RankResult results, List<DivaGroup> colGroupsList) {
        this.classtype = 3;
        this.components.add(RankTablesComponent.this);
        this.GWTClientService = greetingService;
        this.selectionManager = selectionManager;
        selectionManager.addSelectionChangeListener(RankTablesComponent.this);
        
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
        btnsLayout.setWidth("34px");
        btnsLayout.setHeight("18px");
        topLayout.add(btnsLayout);
        topLayout.setCellHorizontalAlignment(btnsLayout, HorizontalPanel.ALIGN_RIGHT);
        topLayout.setCellVerticalAlignment(btnsLayout, HorizontalPanel.ALIGN_TOP);

        minSettingBtn = new RankSettingBtn(GWTClientService,rankPanel);
        btnsLayout.add(minSettingBtn);
        btnsLayout.setCellHorizontalAlignment(minSettingBtn, HorizontalPanel.ALIGN_LEFT);
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
                    maxmizeTableLayout.setHeight("80%");
                    maxmizeTableLayout.setWidth("80%");

                    maxRankTable = new RankTableLayout(selectionManager, results.getDatasetId(), results);
                    maxRankTable.setWidth("80%");
                    maxRankTable.setHeight("80%");

                    HorizontalPanel maxTopLayout = new HorizontalPanel();
                    maxTopLayout.setWidth(maxRankTable.getOffsetWidth() + "px");
                    Label title = new Label();
                    title.setText("Rank Product (Differential Expression)");
                    title.setStyleName("labelheader");
                    title.setWidth((maxRankTable.getOffsetWidth() - 44) + "px");

                    maxTopLayout.add(title);
                    maxTopLayout.add(maxSettingsBtn);
                    maxTopLayout.add(minBtn);
                    maxTopLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);
                    maxTopLayout.setCellHorizontalAlignment(minBtn, HorizontalPanel.ALIGN_RIGHT);
                    maxTopLayout.setCellHorizontalAlignment(maxSettingsBtn, HorizontalPanel.ALIGN_RIGHT);

                    maxmizeTableLayout.add(maxTopLayout);
                    maxmizeTableLayout.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);

                    maxmizeTableLayout.add(maxRankTable);

                    HorizontalPanel maxBottomLayout = new HorizontalPanel();
                    maxmizeTableLayout.add(maxBottomLayout);
                    maxBottomLayout.setWidth("100%");
                    maxBottomLayout.setHeight("30px");
                    maxShowSelectedOnlyBtn = new CheckBox("Show Selected Data Only");
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
//                    maxShowSelectedOnlyBtn.setValue(showSelectedOnlyBtn.getValue());
//                    maxRankTable.showSelectedOnly(showSelectedOnlyBtn.getValue());
                    
                    maxBottomLayout.add(maxShowSelectedOnlyBtn);
                    maxBottomLayout.setCellHorizontalAlignment(maxShowSelectedOnlyBtn, VerticalPanel.ALIGN_LEFT);
                    maxBottomLayout.setCellVerticalAlignment(maxShowSelectedOnlyBtn, VerticalPanel.ALIGN_BOTTOM);
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

        minRankTable = new RankTableLayout(selectionManager, results.getDatasetId(), results);
        mainRankLayout.addMember(minRankTable);
        HorizontalPanel bottomLayout = new HorizontalPanel();
        mainRankLayout.addMember(bottomLayout);
        minRankTable.setHeight("100%");
        minRankTable.draw();
        bottomLayout.setWidth("100%");
        bottomLayout.setHeight("30px");
        showSelectedOnlyBtn = new CheckBox("Show Selected Data Only");
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
        bottomLayout.setCellVerticalAlignment(showSelectedOnlyBtn, VerticalPanel.ALIGN_BOTTOM);

        tablePopup = new PopupPanel(false, true);
        tablePopup.setAnimationEnabled(true);
        tablePopup.ensureDebugId("cwBasicPopup-imagePopup");

        ClickHandler rankInputPanelListener = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (tablePopup != null) {
                    tablePopup.hide();
                }
            }
        };
        maxSettingsReg = maxSettingsBtn.addClickHandler(rankInputPanelListener);
        minSettingsReg = minSettingBtn.addClickHandler(rankInputPanelListener);

        minSettingBtn.setClickListener(RankTablesComponent.this);
        maxSettingsBtn.setClickListener(RankTablesComponent.this);
        selectionChanged(Selection.TYPE.OF_ROWS);

    }

    public VLayout getMainRankLayout() {
        return mainRankLayout;
    }

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
                viewRankTables(perm, seed, groups, log2,maxSettingsBtn.isDefaultRank());
                maxSettingsBtn.hidePanel();
            }
        } 
    }

    private void viewRankTables(String perm, String seed, List<String> colGropNames, String log2, boolean defaultRank) {
        SelectionManager.Busy_Task(true, true);
        GWTClientService.computeRank(perm, seed, colGropNames, log2,defaultRank,
                new AsyncCallback<RankResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("An error occurred while attempting to contact the server");
                        SelectionManager.Busy_Task(false, true);
                    }

                    @Override
                    public void onSuccess(RankResult result) {
                        minRankTable.updateRecords(result);
                        if(maxRankTable != null)
                            maxRankTable.updateRecords(result);
                        selectionChanged(Selection.TYPE.OF_ROWS);
                        SelectionManager.Busy_Task(false, true);

                    }
                });

    }

}
