/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.shared.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Yehia Farag
 */
public class ClientClusterNode implements IsSerializable{
    private int x;
    private int y;
    private double value;
    private int members;
    private ClientClusterNode right;
    private ClientClusterNode  left, parent;
    private String name;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public ClientClusterNode getRight() {
        return right;
    }

    public void setRight(ClientClusterNode right) {
        this.right = right;
    }

    public ClientClusterNode getLeft() {
        return left;
    }

    public void setLeft(ClientClusterNode left) {
        this.left = left;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientClusterNode getParent() {
        return parent;
    }

    public void setParent(ClientClusterNode parent) {
        this.parent = parent;
    }
    
    
    
    
}
