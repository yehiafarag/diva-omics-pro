/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.rank.view;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import web.diva.client.GreetingServiceAsync;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.RankResult;

/**
 *
 * @author Yehia Farag
 * ranking tables container
 */
public class RankTablesView extends HorizontalPanel {

    private final RankTable posRankTable;
    private final RankTable negRankTable;

    public RankTablesView(GreetingServiceAsync greetingService, SelectionManager selectionManager, RankResult results) {

        this.setBorderWidth(1);
        this.setWidth("" + RootPanel.get("RankTablesResults").getOffsetWidth() + "px");
        this.setHeight("270px");
        SectionStack secStackI = new SectionStack();
        secStackI.setVisibilityMode(VisibilityMode.MULTIPLE);
        secStackI.setWidth((RootPanel.get("RankTablesResults").getOffsetWidth() / 2));
        secStackI.setHeight(270);
        posRankTable = new RankTable(greetingService, selectionManager, results.getDatasetId(), results.getPosTableHeader(), results.getPosTableData(), results.getPosRankToIndex(), results.getPosIndexToRank(), "PosRankTable");
        SectionStackSection section1 = new SectionStackSection("Positive Score");
        section1.setExpanded(true);
        section1.addItem(posRankTable.getTable());
        secStackI.addSection(section1);
        this.add(secStackI);

        SectionStack secStackII = new SectionStack();
        secStackII.setVisibilityMode(VisibilityMode.MULTIPLE);
        secStackII.setWidth((RootPanel.get("RankTablesResults").getOffsetWidth() / 2));
        secStackII.setHeight(270);
        negRankTable = new RankTable(greetingService, selectionManager, results.getDatasetId(), results.getNegTableHeader(), results.getNegTableData(), results.getNegRankToIndex(), results.getNegIndexToRank(), "NegRankTable");

        SectionStackSection section11 = new SectionStackSection("Negative Score");
        section11.setExpanded(true);
        section11.addItem(negRankTable.getTable());
        secStackII.addSection(section11);
        this.add(secStackII);
        results = null;

    }

    public void enable() {
        posRankTable.getTable().enable();
        negRankTable.getTable().enable();

    }

    public void disable() {
        posRankTable.getTable().disable();
        negRankTable.getTable().disable();

    }

}
