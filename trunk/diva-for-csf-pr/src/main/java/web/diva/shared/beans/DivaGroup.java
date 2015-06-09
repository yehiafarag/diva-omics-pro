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
public class DivaGroup implements IsSerializable{
    private String name;
    private String color;
    private int count;
    private int[] members;
    private boolean active;
    private boolean defaultGroup;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int[] getMembers() {
        return members;
    }

    public void setMembers(int[] members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isDefaultGroup() {
        return defaultGroup;
    }

    public void setDefaultGroup(boolean defaultGroup) {
        this.defaultGroup = defaultGroup;
    }
    
    
}
