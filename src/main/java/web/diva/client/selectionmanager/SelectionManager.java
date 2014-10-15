/*used
 */
package web.diva.client.selectionmanager;

//import com.google.gwt.user.client.Timer;
//import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.ui.RootPanel;

import com.google.gwt.user.client.Window;
import java.util.ArrayList;
import java.util.List;
//import java.util.HashMap;

/**
 *
 * @author Yehia Farag
 */
public class SelectionManager {

    /**
     * Column selections per dataset
     */
    private Selection selectedColumns;
    /**
     * Row selections per dataset
     */
    private Selection selectedRows;
    /**
     * Registry of listeners for every dataset
     */
    //private final HashMap<Integer, List<SelectionChangeListener>> selectionChangeListeners;
     private final  List<SelectionChangeListener> selectionChangeListeners;
   
    private static final SelectionManager selection_Mananger = new SelectionManager();

    private SelectionManager() {        
        selectionChangeListeners = new ArrayList<SelectionChangeListener>();
    }
    
    public static SelectionManager getInstance(){
    
        return selection_Mananger;
    }

//    /**
//     *
//     * @param datasetId
//     * @return selected columns, or null if no selection has been made
//     */
//    public Selection getSelectedColumns(int datasetId) {
//        if (!selectedColumns.containsKey(datasetId)) {
//            return null;
//        }
//        return selectedColumns.get(datasetId);
//    }
    
    /**
     *
//     * @param datasetId
     * @return selected columns, or null if no selection has been made
     */
    public Selection getSelectedColumns() {
        
        return selectedColumns;
    }

    /**
     *
//     * @param datasetId
     * @return selected rows, or null if no selection has been made
     */
    public Selection getSelectedRows() {
//        if (!selectedRows.containsKey(datasetId)) {
//            return null;
//        }
        return selectedRows;//.get(datasetId);
    }

    /**
     *
//     * @param datasetId - dataset to set selection on
     * @param s - selection of columns, if null - no selection
     */
    public void setSelectedColumns(Selection s) {
//        if (datasetId == 0) {
//            throw new IllegalArgumentException("Trying to select columns in a null DataSet");
//        }
//        if (s == null) {
//            selectedColumns.remove(datasetId);
//        } else if (!s.type.equals(Selection.TYPE.OF_COLUMNS)) {
//            throw new IllegalArgumentException("Trying to select columns with an incorrect type of selection");
//        } else {
            selectedColumns= s;
//        }
        selectionChangedEvent(Selection.TYPE.OF_COLUMNS);
    }

    /**
     *
     * @param datasetId - dataset id to set selection on
     * @param s - selection of rows, if null - no selection
     */
    public void setSelectedRows(Selection s) {
//        if (datasetId == 0) {
//            Window.alert("dataset is 0");
//            throw new IllegalArgumentException("Trying to select rows in a null DataSet");
//        }
//        if (s == null) {
//            Window.alert("selection is null");
//            selectedRows.remove(datasetId);
//        } else if (!s.type.equals(Selection.TYPE.OF_ROWS)) {
//            Window.alert("selection type is not rows");
//            throw new IllegalArgumentException("Trying to select rows with an incorrect type of selection");
//        } else {

            selectedRows= s;
//        }
        selectionChangedEvent(Selection.TYPE.OF_ROWS);
    }
//    private int selectionTimerIndex = 0;
//    private Timer timer;

    /**
     * Mediate information about selection change on a dataset.
     *
     * @param datasetId - dataset that has changed selection
     * @param type - of the selection (on row/columns)
     * @param size
     */
    private synchronized void selectionChangedEvent( final Selection.TYPE type) {
//        if (datasetId == 0 || !selectionChangeListeners.containsKey(datasetId)) {
//            return;
//        }
//        selectionTimerIndex = 0;
//        timer = new Timer() {
//            @Override
//            public void run() {
//                if (selectionTimerIndex < selectionChangeListeners.get(datasetId).size()) {
//                    selectionChangeListeners.get(datasetId).get(selectionTimerIndex).selectionChanged(type);
//                   
        for(SelectionChangeListener listener:selectionChangeListeners){
                        listener.selectionChanged(type);
                    }
//                    selectionTimerIndex++;
//                } else {
//                    timer.cancel();
//                    RootPanel.get("loaderImage").setVisible(false);
//                }
//            }
//        };
//        RootPanel.get("loaderImage").setVisible(true);
//        timer.scheduleRepeating(Math.min(selectionSize, 1000));

    }

    /**
     * Add SelectionChangeListener for dataset
     *
     * @param datasetId - dataset to 'listen to'
     * @param listener
     */
    public void addSelectionChangeListener( SelectionChangeListener listener) {
//        if (datasetId == 0 || listener == null) {
//            throw new IllegalArgumentException("Dataset or listener is null");
//        }

//        if (!selectionChangeListeners.containsKey(datasetId)) {
//            selectionChangeListeners.clear();
//            selectionChangeListeners.put(datasetId, new ArrayList<SelectionChangeListener>());
//        }
//        List<SelectionChangeListener> listeners = selectionChangeListeners.get(datasetId);
//        for (SelectionChangeListener l : listeners) {
//            if (l.toString().equals(listener.toString())) {
//                l.remove();
//                listeners.remove(l);
//                break;
//            }
//        }
//        if (!listeners.contains(listener)) {
//            listeners.add(listener);
//        }
        for(int x = 0;x<selectionChangeListeners.size();x++){
        SelectionChangeListener l = selectionChangeListeners.get(x);
        if (l.toString().equals(listener.toString())) {
               selectionChangeListeners.set(x, listener);
                return;
            }
        }
        selectionChangeListeners.add(listener);

    }

    /**
     * Remove SelectionChangeListener from dataset
     *
     * @param datasetId - dataset to stop listening to
     * @param listener
     */
    public void removeSelectionChangeListener( SelectionChangeListener listener) {
//        if (datasetId == 0 || listener == null) {
//            throw new IllegalArgumentException("Dataset or listener is null");
//        }
//        if (!selectionChangeListeners.containsKey(datasetId)) {
//            return;
//        }

//        selectionChangeListeners.get(datasetId).remove(listener);
        selectionChangeListeners.remove(listener);
    }
}
