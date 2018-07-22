package com.noiprocs.gnik.randomreminder.model;

public class Edge {
    private String parent;
    private String child;
    private int activate;

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

    public String getChild() {
        return child;
    }
}
