/*
 * To change this template, choose Tools | Templates
 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Yehia Farag
 */
public class Unit implements IsSerializable, Serializable {

    public Unit[] children;

    public double value;

    public String name;

    private HashMap<String, CustomNode> nodesMap;

    public Unit() {
    }

    public Unit(String name, double value) {
        this.value = value;
        this.name = name;
    }

    public Unit(String name, Unit... children) {
        this.children = children;
        this.name = name;
    }

    public HashMap<String, CustomNode> getNodesMap() {
        return nodesMap;
    }

    public void setNodesMap(HashMap<String, CustomNode> nodesMap) {
        this.nodesMap = nodesMap;
    }

}
