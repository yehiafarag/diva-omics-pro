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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.List;
import web.diva.client.DivaServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.DivaGroup;
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

    private final VLayout mainRankLayout;
    private final UpdatedRankTable minRankTable;
    private  UpdatedRankTable maxRankTable;
   
    private final PopupPanel tablePopup;
    private final Label maxBtn;
    private final Label minBtn;
    private final UpdatedRankBtn minSettingBtn,maxSettingsBtn;
    private final DivaServiceAsync GWTClientService;
 private final  SelectionManager selectionManager;
    public RankTablesComponent(DivaServiceAsync greetingService, final SelectionManager selectionManager, final RankResult results,List<DivaGroup> colGroupsList) {
        this.classtype = 5;
        this.components.add(RankTablesComponent.this);
        this.GWTClientService = greetingService;
        this.selectionManager = selectionManager;
        selectionManager.addSelectionChangeListener(RankTablesComponent.this);

        mainRankLayout = new VLayout();
        mainRankLayout.setHeight("40%");
        mainRankLayout.setWidth("50%");
        mainRankLayout.setStyleName("rank");
        mainRankLayout.setMargin(0);
//        mainRankLayout.setMembersMargin(2);

//        mainRankLayout = new VLayout();
//        mainRankLayout.setSpacing(1);
////        mainRankLayout.setBorderWidth(1);
//        mainRankLayout.setWidth(width-1+"px");
//        mainRankLayout.setHeight(height+"px");
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
    
        minSettingBtn = new UpdatedRankBtn(selectionManager, GWTClientService);
        btnsLayout.add(minSettingBtn);
        btnsLayout.setCellHorizontalAlignment(minSettingBtn, HorizontalPanel.ALIGN_LEFT);
        maxBtn = new Label(); 
        btnsLayout.add(maxBtn);
        maxBtn.addStyleName("maxmize");
        maxBtn.setHeight("16px");
        maxBtn.setWidth("16px");
       
        
        btnsLayout.setCellHorizontalAlignment(maxBtn, HorizontalPanel.ALIGN_RIGHT);
        btnsLayout.setCellVerticalAlignment(maxBtn, HorizontalPanel.ALIGN_TOP);
        
        maxSettingsBtn = new UpdatedRankBtn(selectionManager, GWTClientService);      
        
        minBtn = new Label();
        minBtn.addStyleName("minmize");
        minBtn.setHeight("16px");
        minBtn.setWidth("16px");
        
        maxBtn.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                if (maxRankTable == null) {
                    VerticalPanel maxmizeTableLayout = new VerticalPanel();
                    maxmizeTableLayout.setHeight("80%");
                    maxmizeTableLayout.setWidth("80%");
                    
                     maxRankTable = new UpdatedRankTable(selectionManager, results.getDatasetId(), results);
                    maxRankTable.setWidth("80%");
                    maxRankTable.setHeight("80%");
                    
                    HorizontalPanel maxTopLayout = new HorizontalPanel();
                    maxTopLayout.setWidth(maxRankTable.getOffsetWidth()+"px");
                    Label title = new Label();
                    title.setText("Rank Product (Differential Expression)");
                    title.setStyleName("labelheader");                  
                    title.setWidth((maxRankTable.getOffsetWidth()-34)+"px");
                    
                    maxTopLayout.add(title);
                    maxTopLayout.add(maxSettingsBtn);
                    maxTopLayout.add(minBtn);
                    maxTopLayout.setCellHorizontalAlignment(title, HorizontalPanel.ALIGN_LEFT);
                    maxTopLayout.setCellHorizontalAlignment(minBtn, HorizontalPanel.ALIGN_RIGHT);
                    maxTopLayout.setCellHorizontalAlignment(maxSettingsBtn, HorizontalPanel.ALIGN_RIGHT);


                    
                    maxmizeTableLayout.add(maxTopLayout);
                    maxmizeTableLayout.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
                   
                    maxmizeTableLayout.add(maxRankTable);
//                    
//                    maxmizeTableLayout.setBorderWidth(1);
//                    maxmizeTableLayout.setSpacing(1);
                    tablePopup.setWidget(maxmizeTableLayout); 
                    maxmizeTableLayout.setStyleName("modalLayout");
                    
                    
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
        
       
        
        minRankTable = new UpdatedRankTable(selectionManager, results.getDatasetId(), results);
        mainRankLayout.addMember(minRankTable);
//        minRankTable.setWidth("100%");
//        minRankTable.setHeight("30%");

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
        selectionChanged(Selection.TYPE.OF_ROWS);

    }


    public VLayout getMainRankLayout() {
        return mainRankLayout;
    }

    @Override
    public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
        if (maxSettingsBtn.getRankPanel().getPopupPanel().isShowing()) {
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
        } else if (minSettingBtn.getRankPanel().getPopupPanel().isShowing()) {
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
        selectionManager.busyTask(true,true);
        GWTClientService.computeRank(perm, seed, colGropNames, log2,
                new AsyncCallback<RankResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("An error occurred while attempting to contact the server");
//                        init=false;
                        selectionManager.busyTask(false,true);
                    }

                    @Override
                    public void onSuccess(RankResult result) {                       
                        minRankTable.updateRecords(result);
                        selectionManager.busyTask(false,true);
                      
                    }
                });

    }

  

   

}
