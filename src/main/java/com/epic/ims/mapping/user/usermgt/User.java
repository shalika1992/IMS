package com.epic.ims.mapping.user.usermgt;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope("prototype")
public class User {
    private String userName;
    private String password;
    private String userRole;
    private Date expiryDate;
    private String fullName;
    private String email;
    private String mobile;
    private Byte noOfInvlidAttempt;
    private Date loggedDate;
    private String initialLoginStatus;
    private String status;
    private Date createdTime;
    private Date createdUser;
    private Date lastUpdatedTime;
    private String lastUpdatedUser;

    public User() {
    }

    public User(String userName, String password, String userRole, Date expiryDate, String fullName, String email, String mobile, Byte noOfInvlidAttempt, Date loggedDate, String initialLoginStatus, String status, Date createdTime, Date createdUser, Date lastUpdatedTime, String lastUpdatedUser) {
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
        this.expiryDate = expiryDate;
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.noOfInvlidAttempt = noOfInvlidAttempt;
        this.loggedDate = loggedDate;
        this.initialLoginStatus = initialLoginStatus;
        this.status = status;
        this.createdTime = createdTime;
        this.createdUser = createdUser;
        this.lastUpdatedTime = lastUpdatedTime;
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Byte getNoOfInvlidAttempt() {
        return noOfInvlidAttempt;
    }

    public void setNoOfInvlidAttempt(Byte noOfInvlidAttempt) {
        this.noOfInvlidAttempt = noOfInvlidAttempt;
    }

    public Date getLoggedDate() {
        return loggedDate;
    }

    public void setLoggedDate(Date loggedDate) {
        this.loggedDate = loggedDate;
    }

    public String getInitialLoginStatus() {
        return initialLoginStatus;
    }

    public void setInitialLoginStatus(String initialLoginStatus) {
        this.initialLoginStatus = initialLoginStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(Date createdUser) {
        this.createdUser = createdUser;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }
}
