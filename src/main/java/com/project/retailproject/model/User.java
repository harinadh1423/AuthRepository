package com.project.retailproject.model;


import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;   // plain text (no encoder)

    private String role;       // ADMIN, STORE_ASSOCIATE, etc.

    private String phoneNumber;

    public User() {}

    public Long getUserId()              { return userId; }
    public void setUserId(Long v)        { this.userId = v; }
    public String getUserName()          { return userName; }
    public void setUserName(String v)    { this.userName = v; }
    public String getEmail()             { return email; }
    public void setEmail(String v)       { this.email = v; }
    public String getPassword()          { return password; }
    public void setPassword(String v)    { this.password = v; }
    public String getRole()              { return role; }
    public void setRole(String v)        { this.role = v; }
    public String getPhoneNumber()       { return phoneNumber; }
    public void setPhoneNumber(String v) { this.phoneNumber = v; }
}
