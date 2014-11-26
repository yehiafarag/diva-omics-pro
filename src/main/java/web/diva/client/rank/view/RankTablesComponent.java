/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.rank.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.List;
import web.diva.client.GreetingServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.ColumnGroup;
import web.diva.shared.beans.RankResult;

/**
 *
 * @author Yehia Farag
 * ranking tables container
 */
public class RankTablesComponent  extends ModularizedListener implements IsSerializable, com.smartgwt.client.widgets.events.ClickHandler{

    @Override
    public final void selectionChanged(Selection.TYPE type) {
         if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows();
            if (sel != null && sel.getMembers().length > 0) {
//                posRankTable.updateTable( sel.getMembers());
//                negRankTable.updateTable( sel.getMembers());
                minRankTable.updateTable(sel.getMembers());
                if (maxRankTable != null) {
                    maxRankTable.updateTable(sel.getMembers());
                }
            }
        }

       
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    private final RankTable posRankTable;
//    private final RankTable negRankTable;
    private final VerticalPanel mainRankLayout;
    private final UpdatedRankTable minRankTable;
    private  UpdatedRankTable maxRankTable;
    private final  SelectionManager selectionManager;
    private final int width=500;
    private final PopupPanel tablePopup;
    private final Label maxBtn;
    private final Label minBtn;
    private final UpdatedRankBtn minSettingBtn,maxSettingsBtn;
    private final GreetingServiceAsync GWTClientService;

    public RankTablesComponent(GreetingServiceAsync greetingService, final SelectionManager selectionManager, final RankResult results,List<ColumnGroup> colGroupsList,int width,int height) {
        this.classtype = 5;
        this.components.add(RankTablesComponent.this);
        this.GWTClientService =greetingService;
        this.selectionManager = selectionManager;
        selectionManager.addSelectionChangeListener(RankTablesComponent.this);
        mainRankLayout = new VerticalPanel();
        mainRankLayout.setSpacing(1);
//        mainRankLayout.setBorderWidth(1);
        mainRankLayout.setWidth(width-1+"px");
        mainRankLayout.setHeight(height+"px");
        
        HorizontalPanel topLayout = new HorizontalPanel();
      
        topLayout.setWidth(width+"px");
        topLayout.setHeight("18px");
        Label title = new Label("Rank Product (Differential Expression)");
        title.setStyleName("labelheader");
        topLayout.add(title);     
        
        title.setWidth((width-34)+"px");
        topLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);
        minSettingBtn = new UpdatedRankBtn(colGroupsList);

        topLayout.add(minSettingBtn);

        topLayout.setCellHorizontalAlignment(minSettingBtn, HorizontalPanel.ALIGN_RIGHT);
        maxBtn = new Label();
        maxBtn.addStyleName("maxmize");
        maxBtn.setHeight("16px");
        maxBtn.setWidth("16px");
        topLayout.add(maxBtn);
        
        topLayout.setCellHorizontalAlignment(maxBtn, HorizontalPanel.ALIGN_RIGHT);
        
        maxSettingsBtn = new UpdatedRankBtn(colGroupsList);        
        
        minBtn = new Label();
        minBtn.addStyleName("minmize");
        minBtn.setHeight("16px");
        minBtn.setWidth("16px");
        
        maxBtn.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                if (maxRankTable == null) {
                    VerticalPanel maxmizeTableLayout = new VerticalPanel();
                    maxmizeTableLayout.setHeight("500px");
                    maxmizeTableLayout.setWidth("700px");
                    HorizontalPanel maxTopLayout = new HorizontalPanel();
                    maxTopLayout.setWidth("100%");
                    Label title = new Label();
                    title.setText("Rank Product (Differential Expression)");
                    title.setStyleName("labelheader");
                    title.setWidth((maxTopLayout.getOffsetWidth()-34)+"px");
                    
                    maxTopLayout.add(title);
                    maxTopLayout.add(maxSettingsBtn);
                    maxTopLayout.add(minBtn);
                    maxTopLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);
                    maxTopLayout.setCellHorizontalAlignment(minBtn, HorizontalPanel.ALIGN_RIGHT);
                    maxTopLayout.setCellHorizontalAlignment(maxSettingsBtn, HorizontalPanel.ALIGN_RIGHT);


                    
                    maxmizeTableLayout.add(maxTopLayout);
                    maxmizeTableLayout.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
                    maxRankTable = new UpdatedRankTable(selectionManager, results.getDatasetId(), results);
                    maxRankTable.setWidth("80%");
                    maxRankTable.setHeight("95%");
                    maxmizeTableLayout.add(maxRankTable);
                    maxmizeTableLayout.setBorderWidth(1);
                    tablePopup.setWidget(maxmizeTableLayout);
                    
                }
                tablePopup.center();
                tablePopup.show();
            }
        });
        
        minBtn.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                tablePopup.hide();
            }
        });
        
        mainRankLayout.add(topLayout);
        
        minRankTable = new UpdatedRankTable(selectionManager, results.getDatasetId(), results);
        minRankTable.setWidth(width);
        minRankTable.setHeight(height - 22);

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
        maxSettingsBtn.addClickHandler(rankInputPanelListener);
        minSettingBtn.addClickHandler(rankInputPanelListener);
        
        minSettingBtn.setClickListener(RankTablesComponent.this);
        maxSettingsBtn.setClickListener(RankTablesComponent.this);


        
        
        
        /***************************************start testing 
        SectionStack secStackI = new SectionStack();
        secStackI.setVisibilityMode(VisibilityMode.MULTIPLE);
        secStackI.setWidth((width / 2));
        secStackI.setHeight(213);
        posRankTable = new RankTable(selectionManager, results.getDatasetId(), results.getPosTableHeader(), results.getPosTableData(), results.getPosRankToIndex(), results.getPosIndexToRank(), "PosRankTable");
        SectionStackSection section1 = new SectionStackSection("Positive Score");
        section1.setExpanded(true);
        section1.addItem(posRankTable);
        secStackI.addSection(section1);
        mainRankLayout.add(secStackI);

        SectionStack secStackII = new SectionStack();
        secStackII.setVisibilityMode(VisibilityMode.MULTIPLE);
        secStackII.setWidth(width / 2);
        secStackII.setHeight(213);
        negRankTable = new RankTable(selectionManager, results.getDatasetId(), results.getNegTableHeader(), results.getNegTableData(), results.getNegRankToIndex(), results.getNegIndexToRank(), "NegRankTable");

        SectionStackSection section11 = new SectionStackSection("Negative Score");
        section11.setExpanded(true);
        section11.addItem(negRankTable);
        secStackII.addSection(section11);
        mainRankLayout.add(secStackII);
        * '*********************************  end testing*/
//        mainRankLayout.setSpacing(1);
        mainRankLayout.add(minRankTable);
        selectionChanged(Selection.TYPE.OF_ROWS);

    }
//
//    public void enable() {
//        posRankTable.enable();
//        negRankTable.enable();
//
//    }
//
//    public void disable() {
//        posRankTable.disable();
//        negRankTable.disable();
//
//    }

    public VerticalPanel getMainRankLayout() {
        return mainRankLayout;
    }

    @Override
    public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
        if (maxSettingsBtn.getRankPanel().isVisible()) {
            maxSettingsBtn.setErrorlablVisible(false);
            maxSettingsBtn.setErrorlablVisible(false);

            List<String> groups = maxSettingsBtn.getSelectColGroups();
            String seed = maxSettingsBtn.getSeed();
            String perm = maxSettingsBtn.getPerm();
            String log2 = maxSettingsBtn.getLog2();
            if (groups == null || groups.isEmpty() || groups.size() > 2 || seed == null || seed.equals("") || perm == null || perm.equals("")) {
                maxSettingsBtn.setErrorlablVisible(true);
                maxSettingsBtn.rankPanelvalidate();
            } else {
                viewRankTables(perm, seed, groups, log2);
                maxSettingsBtn.hidePanel();
            }
        } else if (minSettingBtn.getRankPanel().isVisible()) {
            minSettingBtn.setErrorlablVisible(false);
            minSettingBtn.setErrorlablVisible(false);

            List<String> groups = minSettingBtn.getSelectColGroups();
            String seed = minSettingBtn.getSeed();
            String perm = minSettingBtn.getPerm();
            String log2 = minSettingBtn.getLog2();
            if (groups == null || groups.isEmpty() || groups.size() > 2 || seed == null || seed.equals("") || perm == null || perm.equals("")) {
                minSettingBtn.setErrorlablVisible(true);
                minSettingBtn.rankPanelvalidate();
            } else {
                viewRankTables(perm, seed, groups, log2);
                minSettingBtn.hidePanel();
            }
        }
    }

       
       private void viewRankTables( String perm, String seed, List<String> colGropNames, String log2) {
        selectionManager.busyTask(true);
        GWTClientService.computeRank(perm, seed, colGropNames, log2,
                new AsyncCallback<RankResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("An error occurred while attempting to contact the server");
//                        init=false;
                        selectionManager.busyTask(false);
                    }

                    @Override
                    public void onSuccess(RankResult result) {
                       
                        RootPanel.get("datasetInformation").setVisible(true);
                        minRankTable.updateRecords(result);
                        maxRankTable.updateRecords(result);
                      
                    }
                });

    }

   

}
