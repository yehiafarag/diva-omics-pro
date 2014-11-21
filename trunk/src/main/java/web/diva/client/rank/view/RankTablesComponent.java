/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.rank.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import web.diva.client.GreetingServiceAsync;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.RankResult;

/**
 *
 * @author Yehia Farag
 * ranking tables container
 */
public class RankTablesComponent  extends ModularizedListener implements IsSerializable {

    @Override
    public final void selectionChanged(Selection.TYPE type) {
         if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows();
            if (sel != null && sel.getMembers().length > 0) {
//                posRankTable.updateTable( sel.getMembers());
//                negRankTable.updateTable( sel.getMembers());
                rankTabel.updateTable(sel.getMembers());
            }
        }
        
       
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    private final RankTable posRankTable;
//    private final RankTable negRankTable;
    private final HorizontalPanel mainRankLayout;
    private final UpdatedRankTable rankTabel;
    private final  SelectionManager selectionManager;
    private final int width=500;

    public RankTablesComponent(GreetingServiceAsync greetingService, SelectionManager selectionManager, RankResult results) {
        this.classtype = 5;
        this.components.add(RankTablesComponent.this);
        this.selectionManager = selectionManager;
        selectionManager.addSelectionChangeListener(RankTablesComponent.this);
        mainRankLayout = new HorizontalPanel();
//        mainRankLayout.setBorderWidth(1);
        mainRankLayout.setWidth(width+ "px");
        mainRankLayout.setHeight("213px");
        Label title = new Label();
        title.setText("Rank Product (Differential Expression)");
        
        
        rankTabel=new UpdatedRankTable(selectionManager, results.getDatasetId(), results);
        rankTabel.setWidth("500px");
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
        mainRankLayout.setSpacing(1);
        mainRankLayout.add(rankTabel);
        results = null;
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

    public HorizontalPanel getMainRankLayout() {
        return mainRankLayout;
    }

}
