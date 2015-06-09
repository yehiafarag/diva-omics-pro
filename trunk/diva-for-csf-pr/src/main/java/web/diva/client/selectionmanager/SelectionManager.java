
package web.diva.client.selectionmanager;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import web.diva.client.DivaMain;
import web.diva.client.view.core.BusyTaskIndicator;
import web.diva.client.view.core.ServerConnError;
import web.diva.shared.beans.SomClusteringResult;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 *Central Selection Manager 
 * @author Yehia Farag
 */
public class SelectionManager {

    /**
     * Column selections per dataset
     */
    private Selection selectedColumns = new Selection(Selection.TYPE.OF_COLUMNS, new int[]{});
    /**
     * Row selections per dataset
     */
    private Selection selectedRows = new Selection(Selection.TYPE.OF_ROWS, new int[]{});
    /**
     * Registry of listeners for every dataset
     */
    private final SelectionChangeListener[] selectionChangeListeners;

    private DivaMain mainAppController;

    public SelectionManager() {
        selectionChangeListeners = new SelectionChangeListener[5];
        selectionTimer = new Timer() {
            @Override
            public void run() {
               selectionChangedEvent(Selection.TYPE.OF_ROWS);
            }
        };
    }


    /**
     * @return selected columns, or null if no selection has been made
     */
    public Selection getSelectedColumns() {

        return selectedColumns;
    }

    /**
     * @return selected rows, or null if no selection has been made
     */
    public Selection getSelectedRows() {
        return selectedRows;//.get(datasetId);
    }

    /**
     *
     * @param s - selection of columns, if null - no selection
     */
    public void setSelectedColumns(Selection s) {
        selectedColumns = s;
        selectionChangedEvent(Selection.TYPE.OF_COLUMNS);
    }
    private final Timer selectionTimer ;

    /**
     * @param s - selection of rows, if null - no selection
     */
    public void setSelectedRows(Selection s) {
        if (s == null) {
            selectedRows = new Selection(Selection.TYPE.OF_ROWS, new int[]{});
        }else
        selectedRows = s;
        selectionTimer.schedule(500);
    }


    /**
     * Mediate information about selection change on a dataset.
     * @param type - of the selection (on row/columns)
     */
    private synchronized void selectionChangedEvent(final Selection.TYPE type) {

        for (SelectionChangeListener listener : selectionChangeListeners) {
            listener.selectionChanged(type);
        }

    }

    /**
     * Add SelectionChangeListener for dataset
     *
     * @param listener
     */
    public void addSelectionChangeListener(SelectionChangeListener listener) {
        if (listener.toString().equalsIgnoreCase("OmicsTable"))
            selectionChangeListeners[0] = listener;
        else if (listener.toString().equalsIgnoreCase("ProfilePlot")) {
            selectionChangeListeners[1] = listener;
        } else if (listener.toString().equalsIgnoreCase("PCAPlot")) {
            selectionChangeListeners[2] = listener;
        }
        else if (listener.toString().equalsIgnoreCase("RankTable")) {
            selectionChangeListeners[3] = listener;
        }
        else if (listener.toString().equalsIgnoreCase("SomClust")) {
            selectionChangeListeners[4] = listener;
        }
        else
            SC.say(listener.toString());

    }

    /**
     * Remove SelectionChangeListener from dataset
     *
     * @param listener
     */
    public void removeSelectionChangeListener(SelectionChangeListener listener) {
        if (listener.toString().equalsIgnoreCase("OmicsTable")) {
            selectionChangeListeners[0] = null;
        } else if (listener.toString().equalsIgnoreCase("ProfilePlot")) {
            selectionChangeListeners[1] = null;
        } else if (listener.toString().equalsIgnoreCase("PCAPlot")) {
            selectionChangeListeners[2] = null;
        } else if (listener.toString().equalsIgnoreCase("RankTable")) {
            selectionChangeListeners[3] = null;
        } else if (listener.toString().equalsIgnoreCase("SomClust")) {
            selectionChangeListeners[4] = null;
        } else {
            SC.say(listener.toString());
        }
    }

    /**
     * remove the stored selection in the selection manager
     */
    public void resetSelection() {
        selectedColumns = new Selection(Selection.TYPE.OF_COLUMNS, new int[]{});;
        selectedRows = new Selection(Selection.TYPE.OF_ROWS, new int[]{});;

    }

   
    private static final BusyTaskIndicator busyIndicator = new BusyTaskIndicator();

     /**
     * view loading image
     * @param busy busy task or not
     * @param slow start with animation or view direct
     */
    public static void Busy_Task(boolean busy, boolean slow) {
        busyIndicator.busyTask(busy, slow);

    }

    
    /**
     * This method is responsible for re-initializing the application layout on
     * creating groups the selection manager is responsible for calling this
     * method on creating new groups
     *
     * @param datasetInfo updated dataset information
     *
     */
    public void updateAllModules(DatasetInformation datasetInfo) {
        mainAppController.updateApp(datasetInfo);
    }

     /**
     * This method is responsible for re-initializing the drop down list and
     * dataset title on new dataset selection or creating sub-dataset or saving
     * current dataset
     *
     * @param datasetNewName dataset new name
     *
     */
    public void saveDataset(String datasetNewName) {
        mainAppController.updateDatasetDetails(datasetNewName);
    }

     /**
     * This method is responsible for re-initializing the drop down list and
     * dataset title on new dataset selection or creating sub-dataset or saving
     * current dataset
     *
     * @param datasetName dataset new name
     *
     */
    public void updateDropdownList(String datasetName) {
        mainAppController.updateDropDownList(datasetName);

    }

    
    /**
     * This method is responsible for updating clustering panel the selection
     * manager invoke this method on updating clustering input parameters by
     * users
     *
     * @param result - SomClusteringResult
     * @param clusterColumn - cluster columns or not
     *
     */
    public void updateClusteringPanel(SomClusteringResult result, boolean clusterColumn) {
        mainAppController.updateClusteringPanel(result, clusterColumn);
    }

    public void setMainAppController(DivaMain mainAppController) {
        this.mainAppController = mainAppController;
    }

    public static boolean isBusy() {
        return busyIndicator.isBusy();
    }

    private final ServerConnError SERVER_ERROR = new ServerConnError();

    public void connError() {
        Busy_Task(false, true);
        SERVER_ERROR.connError();

    }

}
