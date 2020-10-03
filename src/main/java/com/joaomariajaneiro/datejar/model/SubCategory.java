package com.joaomariajaneiro.datejar.model;

import com.joaomariajaneiro.datejar.model.enums.Type;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORY")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "IMAGE", length = 5000000)
    private String image;


    public SubCategory() {
    }

    public SubCategory(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public SubCategory setName(String name) {
        this.name = name;
        return this;
    }

    public String getImage() {
        return image;
    }

    public SubCategory setImage(String image) {
        this.image = image;
        return this;
    }

    public long getId() {
        return id;
    }
}
