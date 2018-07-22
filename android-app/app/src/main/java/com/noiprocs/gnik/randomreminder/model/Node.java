package com.noiprocs.gnik.randomreminder.model;

public abstract class Node {
    protected String parent;
    int activate;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public boolean isActivate() {
        return this.activate > 0;
    }

    public void setActivate(int activate) {
        this.activate = activate;
    }

    public abstract String getValue();
}
