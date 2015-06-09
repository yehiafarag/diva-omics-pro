package web.diva.client.rank.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.BodyKeyPressEvent;
import com.smartgwt.client.widgets.grid.events.BodyKeyPressHandler;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.RankResult;

/**
 * Ranking Table
 *
 * @author Yehia Farag
 */
public class RankTableLayout extends ListGrid implements IsSerializable {

    private ListGridRecord[] posRecordMap, negRecordMap, selectedRows;
    private boolean showSelectedOnly;
    private ListGridRecord[] records;
    private final SelectionManager selectionManager;
    private int[] posRankToIndex, negRankToIndex;
    private boolean selectionTag = false;
    private HandlerRegistration clickReg, dragStartReg, dragStopReg;

    /**
     * @param selectionManager main central manager
     * @param results RankResult that contains all ranking information
     */
    public RankTableLayout(SelectionManager selectionManager, RankResult results) {
        this.setOverflow(Overflow.HIDDEN);
        this.selectionManager = selectionManager;
        initGrid(results.getHeaders());
        this.updateRecords(results);

    }

    /**
     * This method is responsible for initializing the ranking table
     *
     * @param headers the headers of the table
     *
     */
    private void initGrid(String[] headers) {
        setShowRecordComponents(true);
        setShowRecordComponentsByCell(true);
        setCanRemoveRecords(false);
        setShowHeaderContextMenu(false);
        setHeight100();
        setShowEdges(false);
        setStyleName("borderless");
        setShowAllRecords(false);
        setCanSort(Boolean.TRUE);

        ListGridField[] fields = new ListGridField[headers.length + 1];
        ListGridField index = (ListGridField) new ListGridField("index", "index".toUpperCase());
        index.setType(ListGridFieldType.INTEGER);
        index.setHidden(true);
        fields[0] = index;
        for (int z = 1; z <= headers.length; z++) {
            ListGridField l = (ListGridField) new ListGridField(headers[z - 1], headers[z - 1].toUpperCase());
            if (z == 1 || z == 7) {
                l.setWidth("72px");
                l.setTitle("" + headers[z - 1].toUpperCase() + "");
                l.setType(ListGridFieldType.INTEGER);
                l.setCanSort(true);
            } else if (z == 2) {
                l.setAutoFitWidth(false);
                l.setType(ListGridFieldType.TEXT);

            } else {
                l.setType(ListGridFieldType.FLOAT);
                l.setFormat("0.000");
            }

            fields[z] = l;
        }
        sort(headers[4], SortDirection.ASCENDING);
        setFields(fields);
        setCanResizeFields(true);
        setSelectionType(SelectionStyle.MULTIPLE);
        setLeaveScrollbarGap(false);
        setCanDragSelect(true);
        draw();
        clickReg = addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ListGridRecord[] selectionRecord = getSelectedRecords();
                updateTableSelection(selectionRecord);
            }
        });
        dragStartReg = addDragStartHandler(new DragStartHandler() {

            @Override
            public void onDragStart(DragStartEvent event) {
            }
        });
        dragStopReg = addDragStopHandler(new DragStopHandler() {
            @Override
            public void onDragStop(DragStopEvent event) {
                ListGridRecord[] selectionRecord = getSelectedRecords();

                updateTableSelection(selectionRecord);
            }
        });

        final Timer t = new Timer() {

            @Override
            public void run() {
                ListGridRecord[] selectionRecord = getSelectedRecords();
                updateTableSelection(selectionRecord);
            }
        };

        this.addBodyKeyPressHandler(new BodyKeyPressHandler() {

            @Override
            public void onBodyKeyPress(BodyKeyPressEvent event) {
                t.schedule(500);
            }
        });
    }

    /**
     * This method is responsible for re indexing the selected indexes and map
     * it to ranking records
     *
     * @param selection indexes of the selected data
     *
     */
    public void updateTable(int[] selection) {
        if (selectionTag) {
            selectionTag = false;
        } else {
            this.deselectAllRecords();
            if (selection.length == 0) {
                selectedRows = new ListGridRecord[]{};
                return;
            }
            ListGridRecord[] reIndexSelection = new ListGridRecord[selection.length];
            int i = 0;

            for (int z : selection) {
                reIndexSelection[i++] = posRecordMap[z];
            }
            selectedRows = reIndexSelection;
            if (showSelectedOnly) {
                this.setRecords(reIndexSelection);
                this.selectAllRecords();
                this.scrollToTop();
            } else {
                this.setRecords(records);
                this.selectRecords(reIndexSelection);
                this.scrollToRow(this.getRecordIndex(reIndexSelection[0]));
            }

        }

    }

    /**
     * This method is responsible for convert ranking records into default data
     * indexes
     *
     * @return record indexes
     *
     */
    public int[] getIndexSelection() {
        ListGridRecord[] selectionRecord = getSelectedRecords();
        if (selectionRecord.length > 0) {
            int[] selectedIndices = new int[selectionRecord.length];
            for (int index = 0; index < selectionRecord.length; index++) {
                ListGridRecord rec = selectionRecord[index];
                selectedIndices[index] = (rec.getAttributeAsInt("Pos Rank") - 1);
            }
            return (selectedIndices);
        } else {
            return (new int[]{});
        }

    }

    /**
     * This method is responsible for show selected data only in the ranking
     * table
     *
     * @param showSelectedOnly show only selected records
     */
    public void showSelectedOnly(boolean showSelectedOnly) {
        this.showSelectedOnly = showSelectedOnly;
        if (showSelectedOnly) {
            selectedRows = this.getSelectedRecords();
            this.setRecords(selectedRows);
            this.selectAllRecords();
            this.scrollToTop();

        } else {
            this.setRecords(records);
            if (selectedRows != null && selectedRows.length > 0) {
                this.selectRecords(selectedRows);
                this.scrollToRow(this.getRecordIndex(selectedRows[0]));
            }

        }

    }

    /**
     * This method is responsible for updating the view by selecting records on
     * ranking table
     *
     * @param selectionRecord selected records
     */
    private void updateTableSelection(ListGridRecord[] selectionRecord) {
        if (selectionRecord.length > 0) {
            int[] selectedIndices = new int[selectionRecord.length];
            for (int index = 0; index < selectionRecord.length; index++) {
                ListGridRecord rec = selectionRecord[index];
                selectedIndices[index] = rec.getAttributeAsInt("index");
            }
            updateSelectionManager(selectedIndices);
        } else {
            updateSelectionManager(new int[]{});
        }

    }

    /**
     * This method is responsible for initializing the record list
     *
     * @param datasetInfo
     *
     */
    private ListGridRecord[] initRecodList(double[][] tableData, String[] headers, String[] rowIds, int[] posRank, int[] negRank, int[] posRankToNegRank) {

        ListGridRecord[] recordsInit = new ListGridRecord[rowIds.length];
        posRecordMap = new ListGridRecord[recordsInit.length];
        negRecordMap = new ListGridRecord[recordsInit.length];
        for (int x = 0; x < recordsInit.length; x++) {
            ListGridRecord record = new ListGridRecord();

            int posReIndexer = posRankToIndex[x];
            int negReIndexer = negRankToIndex[posRankToNegRank[x] - 1];
            record.setAttribute("index", (Integer) posReIndexer);
            int coulmnReindex = 0;
//            for (int i = 0; i < 7; i++) {
//                record.setAttribute(i + "", labelsUrl[x][i]);
//
//            }
            for (int y = 0; y < headers.length; y++) {

                try {
                    if (y == 0) {
                        record.setAttribute(headers[y], (Integer) posRank[x]);
                    } else if (y == 1) {
                        record.setAttribute(headers[y], rowIds[x]);
                    } else if (y == 6) {
                        record.setAttribute(headers[y], (Integer) negRank[x]);
                    } else {
                        record.setAttribute(headers[y], tableData[coulmnReindex][x]);
                        coulmnReindex++;

                    }
                } catch (NumberFormatException nexp) {
                    Window.alert(nexp.getMessage());
                }
            }

            recordsInit[x] = record;
            posRecordMap[posReIndexer] = record;
            negRecordMap[negReIndexer] = record;
        }
        tableData = null;
        headers = null;
        return recordsInit;
    }

    /**
     * This method is responsible for updating the selection manager on user
     * selection on table
     *
     * @param selectedIndices selected data indexes
     *
     */
    private void updateSelectionManager(int[] selectedIndices) {
        selectionTag = true;
        SelectionManager.Busy_Task(true, false);
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        selectionManager.setSelectedRows(selection);

    }

    @Override
    public String toString() {
        return "rank table";
    }

    /**
     * This method is responsible for updating the ranking records
     *
     * @param results RankResult that has all ranking information
     *
     */
    public final void updateRecords(RankResult results) {
        this.posRankToIndex = results.getPosRank();
        this.posRankToIndex = results.getPosRankToIndex();
        this.negRankToIndex = results.getNegRankToIndex();
        this.records = initRecodList(results.getTableData(), results.getHeaders(), results.getRowIds(), results.getPosRank(), results.getNegRank(), results.getPosRankToNegRank());
        setData(records);

    }

    /**
     * This method is responsible for cleaning on removing the component from
     * the container
     */
    public void remove() {
        clickReg.removeHandler();
        dragStartReg.removeHandler();
        dragStopReg.removeHandler();

    }
}
