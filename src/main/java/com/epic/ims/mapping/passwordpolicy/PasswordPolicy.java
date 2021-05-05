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
    private long noOfInvalidLoginAttempt;
    private long repeatCharactersAllow;
    private long initialPasswordExpiryStatus;
    private long passwordExpiryPeriod;
    private long noOfHistoryPassword;
    private long minimumPasswordChangePeriod;
    private long idleAccountExpiryPeriod;
    private String description;
    private Date createdTime;
    private String createdUser;
    private String lastUpdatedUser;
    private Date lastUpdatedTime;

    public PasswordPolicy() {
    }

    public PasswordPolicy(long passwordPolicyId, long minimumLength, long maximumLength, long minimumSpecialCharacters, long minimumUpperCaseCharacters, long minimumNumericalCharacters, long minimumLowerCaseCharacters, long noOfInvalidLoginAttempt, long repeatCharactersAllow, long initialPasswordExpiryStatus, long passwordExpiryPeriod, long noOfHistoryPassword, long minimumPasswordChangePeriod, long idleAccountExpiryPeriod, String description, Date createdTime, String createdUser, String lastUpdatedUser, Date lastUpdatedTime) {
        this.passwordPolicyId = passwordPolicyId;
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
        this.minimumSpecialCharacters = minimumSpecialCharacters;
        this.minimumUpperCaseCharacters = minimumUpperCaseCharacters;
        this.minimumNumericalCharacters = minimumNumericalCharacters;
        this.minimumLowerCaseCharacters = minimumLowerCaseCharacters;
        this.noOfInvalidLoginAttempt = noOfInvalidLoginAttempt;
        this.repeatCharactersAllow = repeatCharactersAllow;
        this.initialPasswordExpiryStatus = initialPasswordExpiryStatus;
        this.passwordExpiryPeriod = passwordExpiryPeriod;
        this.noOfHistoryPassword = noOfHistoryPassword;
        this.minimumPasswordChangePeriod = minimumPasswordChangePeriod;
        this.idleAccountExpiryPeriod = idleAccountExpiryPeriod;
        this.description = description;
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

    public long getNoOfInvalidLoginAttempt() {
        return noOfInvalidLoginAttempt;
    }

    public void setNoOfInvalidLoginAttempt(long noOfInvalidLoginAttempt) {
        this.noOfInvalidLoginAttempt = noOfInvalidLoginAttempt;
    }

    public long getRepeatCharactersAllow() {
        return repeatCharactersAllow;
    }

    public void setRepeatCharactersAllow(long repeatCharactersAllow) {
        this.repeatCharactersAllow = repeatCharactersAllow;
    }

    public long getInitialPasswordExpiryStatus() {
        return initialPasswordExpiryStatus;
    }

    public void setInitialPasswordExpiryStatus(long initialPasswordExpiryStatus) {
        this.initialPasswordExpiryStatus = initialPasswordExpiryStatus;
    }

    public long getPasswordExpiryPeriod() {
        return passwordExpiryPeriod;
    }

    public void setPasswordExpiryPeriod(long passwordExpiryPeriod) {
        this.passwordExpiryPeriod = passwordExpiryPeriod;
    }

    public long getNoOfHistoryPassword() {
        return noOfHistoryPassword;
    }

    public void setNoOfHistoryPassword(long noOfHistoryPassword) {
        this.noOfHistoryPassword = noOfHistoryPassword;
    }

    public long getMinimumPasswordChangePeriod() {
        return minimumPasswordChangePeriod;
    }

    public void setMinimumPasswordChangePeriod(long minimumPasswordChangePeriod) {
        this.minimumPasswordChangePeriod = minimumPasswordChangePeriod;
    }

    public long getIdleAccountExpiryPeriod() {
        return idleAccountExpiryPeriod;
    }

    public void setIdleAccountExpiryPeriod(long idleAccountExpiryPeriod) {
        this.idleAccountExpiryPeriod = idleAccountExpiryPeriod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
