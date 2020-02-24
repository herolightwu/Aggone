package com.odelan.yang.aggone.Model;

public class Sport {
    public int id;
    public String name;
    public int icon;
    public boolean selected;

    public Sport(int id, String name, int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        selected = false;
    }
}
