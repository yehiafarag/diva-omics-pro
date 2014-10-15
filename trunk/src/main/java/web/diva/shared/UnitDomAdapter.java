/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import org.thechiselgroup.choosel.protovis.client.PVDomAdapter;

/**
 * @author Yehia Farag
 */
public class UnitDomAdapter implements IsSerializable, Serializable, PVDomAdapter<Unit> {

    @Override
    public Unit[] getChildren(Unit t) {
        return t.children == null ? new Unit[0] : t.children;
    }

    @Override
    public String getNodeName(Unit t) {
        return t.name;
    }

    @Override
    public double getNodeValue(Unit t) {
        return t.value;
    }
}
