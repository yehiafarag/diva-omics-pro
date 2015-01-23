/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.selectionmanager;


/**
 *
 * Simple set of rows or columns
 *
 * @author pawels
 * @author Yehia Farag
 */
public class Selection {

    public enum TYPE {

        OF_ROWS, OF_COLUMNS
    };
    private int[] members;
    private Selection.TYPE type;
    private boolean active = true;

    /**
     * Create empty selection of given type
     *
     * @param selectionType - type of selection (ON_ROWS or ON_COLUMNS)
     */
    public Selection(Selection.TYPE selectionType) {
        type = selectionType;
        members = new int[]{};
    }

    /**
     * Create selection of selected row/column indices
     *
     * @param selectionType - type of selection (OF_ROWS or OF_COLUMNS)
     * @param selectedIndices - selected row/column indices
     */
    public Selection(Selection.TYPE selectionType, int[] selectedIndices) {
        this(selectionType);
        members=selectedIndices;
//        addMembers(selectedIndices);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Selection.TYPE getType() {
        return type;
    }

//    public void addMember(int id) {
//        members.add(id);
//    }

//    private void addMembers(int[] ids) {
//        if (ids == null) {
//            throw new IllegalArgumentException("Array od selected indices is null");
//        }
//        for (int id : ids) {
//            members.add(id);
//        }
//    }

//    public boolean hasMember(int member) {
//        return members.contains(member);
//    }

    public int[] getMembers() {
//        if (members.isEmpty()) {
//            return null;
//        }
//        int[] ms = new int[members.size()];
//        Iterator<Integer> it = members.iterator();
//        for (int i = 0; it.hasNext(); ms[i++] = it.next()) {
//        }
        return members;
    }

    public void clear() {
        members = new int[]{};
    }

    public int size() {
        return members.length;
    }

    @Override
    public String toString() {
        return "" + getClass() + ": type: " + type + ", " + " active: " + active + ", members: " + members;
    }
}
