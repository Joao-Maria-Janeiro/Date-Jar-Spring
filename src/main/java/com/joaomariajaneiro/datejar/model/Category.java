package com.joaomariajaneiro.datejar.model;

import com.joaomariajaneiro.datejar.model.enums.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    private Type type;

    @OneToMany
    @JoinColumn(name = "ACTIVITY_ID")
    private List<Activity> activities;

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
