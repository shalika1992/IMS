package com.epic.ims.mapping.samplefile;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope("prototype")
public class SampleFile {
    private String id;
    private String referenceNo;
    private String receivedDate;
    private String institutionCode;
    private String name;
    private String age;
    private String gender;
    private String symptomatic;
    private String contactType;
    private String nic;
    private String address;
    private String status;
    private String residentDistrict;
    private String contactNumber;
    private String secondaryContactNumber;
    private String specimenid;
    private String barcode;
    private String ward;
    private String plateId;
    private String blockValue;
    private Date createdTime;
    private String createdUser;
    private Date lastUpdatedTime;
    private String lastUpdatedUser;

    public SampleFile() {
    }

    public SampleFile(String id, String referenceNo, String receivedDate, String name, String nic) {
        this.id = id;
        this.referenceNo = referenceNo;
        this.receivedDate = receivedDate;
        this.name = name;
        this.nic = nic;
    }

    public SampleFile(String id, String referenceNo, String receivedDate, String institutionCode, String name, String age, String gender, String symptomatic, String contactType, String nic, String address, String status, String residentDistrict, String contactNumber, String secondaryContactNumber, String specimenid, String barcode, String ward, String plateId, String blockValue, Date createdTime, String createdUser, Date lastUpdatedTime, String lastUpdatedUser) {
        this.id = id;
        this.referenceNo = referenceNo;
        this.receivedDate = receivedDate;
        this.institutionCode = institutionCode;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.symptomatic = symptomatic;
        this.contactType = contactType;
        this.nic = nic;
        this.address = address;
        this.status = status;
        this.residentDistrict = residentDistrict;
        this.contactNumber = contactNumber;
        this.secondaryContactNumber = secondaryContactNumber;
        this.specimenid = specimenid;
        this.barcode = barcode;
        this.ward = ward;
        this.plateId = plateId;
        this.blockValue = blockValue;
        this.createdTime = createdTime;
        this.createdUser = createdUser;
        this.lastUpdatedTime = lastUpdatedTime;
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResidentDistrict() {
        return residentDistrict;
    }

    public void setResidentDistrict(String residentDistrict) {
        this.residentDistrict = residentDistrict;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSecondaryContactNumber() {
        return secondaryContactNumber;
    }

    public void setSecondaryContactNumber(String secondaryContactNumber) {
        this.secondaryContactNumber = secondaryContactNumber;
    }

    public String getSpecimenid() {
        return specimenid;
    }

    public void setSpecimenid(String specimenid) {
        this.specimenid = specimenid;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
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
