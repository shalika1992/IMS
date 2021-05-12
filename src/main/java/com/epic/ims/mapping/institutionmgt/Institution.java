package com.epic.ims.mapping.institutionmgt;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope("prototype")
public class Institution {
    private String institutionCode;
    private String institutionName;
    private String address;
    private String contactNumber;
    private String status;
    private Date createdTime;
    private Date lastUpdatedTime;
    private String lastUpdatedUser;

    public Institution() {
    }

    public Institution(String institutionCode, String institutionName, String address, String contactNumber, String status, Date createdTime, Date lastUpdatedTime, String lastUpdatedUser) {
        this.institutionCode = institutionCode;
        this.institutionName = institutionName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.status = status;
        this.createdTime = createdTime;
        this.lastUpdatedTime = lastUpdatedTime;
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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
