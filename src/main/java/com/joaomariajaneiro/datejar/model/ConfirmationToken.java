package com.joaomariajaneiro.datejar.model;

import java.sql.Date;
import java.util.Calendar;
import java.util.UUID;

public class ConfirmationToken {
    private long id;

    private String token;

    private Date createdDate;

    private Long userId;

    public ConfirmationToken(long id, String token, Date createdDate, Long userId) {
        this.id = id;
        this.token = token;
        this.createdDate = createdDate;
        this.userId = userId;
    }

    public ConfirmationToken() {
        this.token = UUID.randomUUID().toString();
        this.createdDate = new Date(Calendar.getInstance().getTime().getTime());
    }

    public ConfirmationToken(long id, Long userId) {
        this.id = id;
        this.token = UUID.randomUUID().toString();
        this.createdDate = new Date(Calendar.getInstance().getTime().getTime());
        this.userId = userId;
    }

    public ConfirmationToken(Long userId) {
        this.userId = userId;
        createdDate = new Date(Calendar.getInstance().getTime().getTime());
        token = UUID.randomUUID().toString();
    }

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public ConfirmationToken setToken(String token) {
        this.token = token;
        return this;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public ConfirmationToken setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public ConfirmationToken setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}
