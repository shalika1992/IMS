package com.epic.ims.mapping.result;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope("prototype")
public class Result {
    private String id;
    private String sampleId;
    private String referenceNo;
    private String institutionCode;
    private String name;
    private String age;
    private String gender;
    private String nic;
    private String address;
    private String district;
    private String contactNo;
    private String receivedDate;
    private String status;
    private String serialNo;
    private String specimenId;
    private String barCode;
    private String plateId;
    private String blockValue;
    private boolean pool;
    private String poolId;
    private Date createdDateTime;
    private String createdUser;

    public Result() {
    }

    public Result(String id, String sampleId, String referenceNo, String institutionCode, String name, String age, String gender, String nic, String address, String district, String contactNo, String receivedDate, String status, String serialNo, String specimenId, String barCode, String plateId, String blockValue, boolean pool, String poolId, Date createdDateTime, String createdUser) {
        this.id = id;
        this.sampleId = sampleId;
        this.referenceNo = referenceNo;
        this.institutionCode = institutionCode;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.nic = nic;
        this.address = address;
        this.district = district;
        this.contactNo = contactNo;
        this.receivedDate = receivedDate;
        this.status = status;
        this.serialNo = serialNo;
        this.specimenId = specimenId;
        this.barCode = barCode;
        this.plateId = plateId;
        this.blockValue = blockValue;
        this.pool = pool;
        this.poolId = poolId;
        this.createdDateTime = createdDateTime;
        this.createdUser = createdUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
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

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSpecimenId() {
        return specimenId;
    }

    public void setSpecimenId(String specimenId) {
        this.specimenId = specimenId;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getPlateId() {
        return plateId;
    }

    public void setPlateId(String plateId) {
        this.plateId = plateId;
    }

    public String getBlockValue() {
        return blockValue;
    }

    public void setBlockValue(String blockValue) {
        this.blockValue = blockValue;
    }

    public boolean isPool() {
        return pool;
    }

    public void setPool(boolean pool) {
        this.pool = pool;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }
}
