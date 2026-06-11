package com.project.retailproject.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuditLogRequestDTO {


    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "Action is required")
    private String action;



    public String getUsername() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
}