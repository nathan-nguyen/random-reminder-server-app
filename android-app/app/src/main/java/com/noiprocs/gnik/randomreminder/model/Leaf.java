package com.noiprocs.gnik.randomreminder.model;

public class Leaf extends Node {
    private int id;
    private String leaf;

    public Leaf(int id, String parent, String leaf, int activate) {
        this.id = id;
        this.parent = parent;
        this.leaf = leaf;
        this.activate = activate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + " - " + parent + " - " + leaf;
    }

    @Override
    public String getValue() {
        return leaf;
    }
}
