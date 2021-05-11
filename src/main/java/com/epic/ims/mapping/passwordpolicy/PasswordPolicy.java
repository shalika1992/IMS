package com.epic.ims.mapping.passwordpolicy;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope("prototype")
public class PasswordPolicy {
    private long passwordPolicyId;
    private long minimumLength;
    private long maximumLength;
    private long minimumSpecialCharacters;
    private long minimumUpperCaseCharacters;
    private long minimumNumericalCharacters;
    private long minimumLowerCaseCharacters;
    private Date createdTime;
    private String createdUser;
    private String lastUpdatedUser;
    private Date lastUpdatedTime;

    public PasswordPolicy() {
    }

    public PasswordPolicy(long passwordPolicyId, long minimumLength, long maximumLength, long minimumSpecialCharacters, long minimumUpperCaseCharacters, long minimumNumericalCharacters, long minimumLowerCaseCharacters, Date createdTime, String createdUser, String lastUpdatedUser, Date lastUpdatedTime) {
        this.passwordPolicyId = passwordPolicyId;
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
        this.minimumSpecialCharacters = minimumSpecialCharacters;
        this.minimumUpperCaseCharacters = minimumUpperCaseCharacters;
        this.minimumNumericalCharacters = minimumNumericalCharacters;
        this.minimumLowerCaseCharacters = minimumLowerCaseCharacters;
        this.createdTime = createdTime;
        this.createdUser = createdUser;
        this.lastUpdatedUser = lastUpdatedUser;
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public long getPasswordPolicyId() {
        return passwordPolicyId;
    }

    public void setPasswordPolicyId(long passwordPolicyId) {
        this.passwordPolicyId = passwordPolicyId;
    }

    public long getMinimumLength() {
        return minimumLength;
    }

    public void setMinimumLength(long minimumLength) {
        this.minimumLength = minimumLength;
    }

    public long getMaximumLength() {
        return maximumLength;
    }

    public void setMaximumLength(long maximumLength) {
        this.maximumLength = maximumLength;
    }

    public long getMinimumSpecialCharacters() {
        return minimumSpecialCharacters;
    }

    public void setMinimumSpecialCharacters(long minimumSpecialCharacters) {
        this.minimumSpecialCharacters = minimumSpecialCharacters;
    }

    public long getMinimumUpperCaseCharacters() {
        return minimumUpperCaseCharacters;
    }

    public void setMinimumUpperCaseCharacters(long minimumUpperCaseCharacters) {
        this.minimumUpperCaseCharacters = minimumUpperCaseCharacters;
    }

    public long getMinimumNumericalCharacters() {
        return minimumNumericalCharacters;
    }

    public void setMinimumNumericalCharacters(long minimumNumericalCharacters) {
        this.minimumNumericalCharacters = minimumNumericalCharacters;
    }

    public long getMinimumLowerCaseCharacters() {
        return minimumLowerCaseCharacters;
    }

    public void setMinimumLowerCaseCharacters(long minimumLowerCaseCharacters) {
        this.minimumLowerCaseCharacters = minimumLowerCaseCharacters;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
