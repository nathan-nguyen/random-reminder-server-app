package com.noiprocs.gnik.randomreminder.model;

public class Edge {
    private String parent;
    private String child;

    public Edge(String parent, String child) {
        this.parent = parent;
        this.child = child;
    }

    @Override
    public String toString(){
        return parent + " - " + child;
    }
}
