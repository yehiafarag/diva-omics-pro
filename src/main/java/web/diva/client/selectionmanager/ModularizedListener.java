/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.selectionmanager;

/**
 * Template for the main classes.
 */
public abstract class ModularizedListener implements SelectionChangeListener {

    //NOTE: mode this variable protected (had default modifier originally)
    protected java.util.Vector components;
    //NOTE: made this variable protected (had default modifier originally)
//    protected int datasetId = 0;
    //NOTE: made this variable protected (had default modifier originally)
    protected int classtype = 0;

    /**
     * Set the type of component.
     *
     * @param classtype The class type.
     *
     * <dir>
     *
     * <li>1 GeneTable</li>
     *
     * <li>2 somClust</li>
     *
     * <li>3 LineChart</li>
     *
     * <li>4 PCA</li>
     *
     * <li>5 Rank Table</li>
     *
     * <li>10 Empty selection</li>
     *
     *
     * </dir>
     *
     */
    public void setClassType(int classtype) {

        this.classtype = classtype;

    }

    /**
     * Returns the class type.
     *
     * @return int type
     *
     */
    public int getClassType() {

        return classtype;

    }

    /**
     * Creates new ModularizedListener
     */
    public ModularizedListener() {
        this.components = new java.util.Vector();
    }

    /**
     * Add a component to this objects list of components.
     *
     * @param comp A component that is used by this object
     *
     */
    public void addComponent(Object comp) {

        components.add(comp);

    }

    /**
     * Get the component at location index.
     *
     * @param index The index of the wanted component
     *
     * @return The component at the given index
     *
     */
    public Object getComponentAt(int index) {

        return components.elementAt(index);

    }

    /**
     * Remove a component from this objects list of used components.
     *
     * @param comp The component to be removed
     *
     */
    public void removeComponent(Object comp) {

        components.removeElement(comp);

    }

}
