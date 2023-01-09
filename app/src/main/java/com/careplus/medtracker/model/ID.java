package com.careplus.medtracker.model;

public class ID {
    int id;

    // Default constructor
    public ID()
    {   }

    // Parameterized constructor
    public ID(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
