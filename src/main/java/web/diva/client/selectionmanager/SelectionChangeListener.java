/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.selectionmanager;
/**
 *
 * Any window (view) that should be notified of selection changes on any
 * particular dataset, should implement the SelectionChangeListener interface,
 * and register in the SelectionManager as a listener for this particular dataset. 
 * @author pawels
 * @author Yehia Farag
 */
public interface SelectionChangeListener  {
    public void selectionChanged(Selection.TYPE type);
    public void remove();

}
