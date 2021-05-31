package com.epic.ims.mapping.mastertemp;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope("prototype")
public class MasterTemp {
    private int id;
    private String sampleId;
    private String referenceNo;
    private String institutionCode;
    private String name;
    private String age;
    private String gender;
    private String nic;
    private String symptomatic;
    private String contactType;
    private String address;
    private String residentDistrict;
    private String contactNumber;
    private String secondaryContactNumber;
    private String serialNo;
    private String specimenid;
    private String barcode;
    private String receivedDate;
    private String status;
    private String plateId;
    private String blockValue;
    private String indexValue;
    private String isPool;
    private String ward;
    private Date createdTime;
    private String createdUser;

    public MasterTemp() {
    }

    public MasterTemp(int id, String sampleId, String referenceNo, String institutionCode, String name, String age, String gender, String nic, String symptomatic, String contactType, String address, String residentDistrict, String contactNumber, String secondaryContactNumber, String serialNo, String specimenid, String barcode, String receivedDate, String status, String plateId, String blockValue, String indexValue, String isPool, String ward, Date createdTime, String createdUser) {
        this.id = id;
        this.sampleId = sampleId;
        this.referenceNo = referenceNo;
        this.institutionCode = institutionCode;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.nic = nic;
        this.symptomatic = symptomatic;
        this.contactType = contactType;
        this.address = address;
        this.residentDistrict = residentDistrict;
        this.contactNumber = contactNumber;
        this.secondaryContactNumber = secondaryContactNumber;
        this.serialNo = serialNo;
        this.specimenid = specimenid;
        this.barcode = barcode;
        this.receivedDate = receivedDate;
        this.status = status;
        this.plateId = plateId;
        this.blockValue = blockValue;
        this.indexValue = indexValue;
        this.isPool = isPool;
        this.ward = ward;
        this.createdTime = createdTime;
        this.createdUser = createdUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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

    public String getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(String indexValue) {
        this.indexValue = indexValue;
    }

    public String getIsPool() {
        return isPool;
    }

    public void setIsPool(String isPool) {
        this.isPool = isPool;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
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
}
