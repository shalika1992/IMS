package com.epic.ims.mapping.rejectedsampledata;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope("prototype")
public class RejectedSampleData {
    private int id;
    private String referenceNo;
    private String institutionCode;
    private String name;
    private String age;
    private String gender;
    private String symptomatic;
    private String contactType;
    private String nic;
    private String address;
    private String district;
    private String contactNo;
    private String secondaryContactNo;
    private String receivedDate;
    private String status;
    private String remark;
    private String createdUser;
    private Date createdTime;

    public RejectedSampleData() {
    }

    public RejectedSampleData(int id, String referenceNo, String institutionCode, String name, String age, String gender, String symptomatic, String contactType, String nic, String address, String district, String contactNo, String secondaryContactNo, String receivedDate, String status, String remark, String createdUser, Date createdTime) {
        this.id = id;
        this.referenceNo = referenceNo;
        this.institutionCode = institutionCode;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.symptomatic = symptomatic;
        this.contactType = contactType;
        this.nic = nic;
        this.address = address;
        this.district = district;
        this.contactNo = contactNo;
        this.secondaryContactNo = secondaryContactNo;
        this.receivedDate = receivedDate;
        this.status = status;
        this.remark = remark;
        this.createdUser = createdUser;
        this.createdTime = createdTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSymptomatic() {
        return symptomatic;
    }

    public void setSymptomatic(String symptomatic) {
        this.symptomatic = symptomatic;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getSecondaryContactNo() {
        return secondaryContactNo;
    }

    public void setSecondaryContactNo(String secondaryContactNo) {
        this.secondaryContactNo = secondaryContactNo;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }


}
