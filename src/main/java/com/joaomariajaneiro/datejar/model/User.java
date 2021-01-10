package com.joaomariajaneiro.datejar.model;

import java.util.List;
import java.util.Objects;

public class User {

    private long id;

    private String username;

    private String email;

    private String password;

    private String picture;

    private int partner_id;

    private List<Category> categories;

    private boolean isEnabled;


    public User(long id, String username, String email, String password, String picture,
                int partner_id, boolean isActive) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.partner_id = partner_id;
        this.isEnabled = isActive;
    }

    public User(long id, String username, String email, String password, String picture,
                int partner_id, List<Category> categories, boolean isActive) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.partner_id = partner_id;
        this.categories = categories;
        this.isEnabled = isActive;
    }

    public User() {
    }

    public User(long id, String username, String email, String password, String picture, int partner_id) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.partner_id = partner_id;
    }

    public User(long id, String username, String email, String password, String picture, List<Category> categories) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.categories = categories;
    }

    public User(String username, String email, String password, List<Category> categories, String picture) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.categories = categories;
    }

    public User(String username, String email, String password, String picture) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.picture = picture;
    }

    public int getPartner_id() {
        return partner_id;
    }

    public User setPartner_id(int partner_id) {
        this.partner_id = partner_id;
        return this;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public User setEnabled(boolean enabled) {
        isEnabled = enabled;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public User setCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(username, user.username) && Objects.equals(email,
                user.email) && Objects.equals(password, user.password) && Objects.equals(categories, user.categories);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", email='" + email + '\'' + ", password='" + password + '\'' + ", categories=" + categories + '}';
    }

}
