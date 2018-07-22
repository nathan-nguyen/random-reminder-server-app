package com.noiprocs.gnik.randomreminder.model;

public class Edge extends Node {
    private String child;

    public Edge(String parent, String child, int activate) {
        this.parent = parent;
        this.child = child;
        this.activate = activate;
    }

    @Override
    public String toString(){
        return parent + " - " + child;
    }

    public String getParent() {
        return parent;
    }

    @Override
    public String getValue() {
        return child;
    }

    public String getChild() {
        return child;
    }
}
