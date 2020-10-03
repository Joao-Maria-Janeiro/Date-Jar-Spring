package com.joaomariajaneiro.datejar.model;

import com.joaomariajaneiro.datejar.model.enums.Type;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "cart")
    private Set<SubCategory> subCategories;


    public Category(String name) {
        this.name = name;
    }

    public Category() {
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

    public Set<SubCategory> getSubCategories() {
        return subCategories;
    }

    public Category setSubCategories(Set<SubCategory> subCategories) {
        this.subCategories = subCategories;
        return this;
    }

    public void addSubCategory(SubCategory subCategory) {
        subCategories.add(subCategory);
    }
}
