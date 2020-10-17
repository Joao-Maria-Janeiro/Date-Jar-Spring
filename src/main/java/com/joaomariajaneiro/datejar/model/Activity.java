package com.joaomariajaneiro.datejar.model;

public class Activity {
    private long id;

    private String name;

    public Activity() {
    }

    public Activity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }
}
