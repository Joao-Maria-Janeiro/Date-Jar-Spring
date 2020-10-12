package com.joaomariajaneiro.datejar.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ACTIVITY")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany
    @JoinColumn(name = "ACTIVITY_ID")
    private List<Activity> activities;

    public Activity() {
    }

    public Activity(String name) {
        this.name = name;
    }

    public Activity(String name, List<Activity> activities) {
        this.name = name;
        this.activities = activities;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
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
