package com.joaomariajaneiro.datejar.model;

import com.joaomariajaneiro.datejar.model.enums.Type;

import java.util.List;

public class Category {
    private long id;

    private String name;

    private Type type;

    private List<Activity> activities;

    public Category(long id, String name, Type type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Category(long id, String name, Type type, List<Activity> activities) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.activities = activities;
    }

    public Category(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Category(String name, Type type, List<Activity> activities) {
        this.name = name;
        this.type = type;
        this.activities = activities;
    }

    public Category() {
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public Category setActivities(List<Activity> activities) {
        this.activities = activities;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public long getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
