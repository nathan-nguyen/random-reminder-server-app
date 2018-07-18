package com.noiprocs.gnik.randomreminder.model;

public class Leaf {
    private int id;
    private String parent;
    private String leaf;

    public Leaf(int id, String parent, String leaf) {
        this.id = id;
        this.parent = parent;
        this.leaf = leaf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return id + " - " + parent + " - " + leaf;
    }
}
