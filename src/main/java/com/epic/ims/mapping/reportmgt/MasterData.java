package com.epic.ims.mapping.reportmgt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Scope("request")
public class MasterData {
    private long id;
    private long sampleID;
    private String referenceNumber;
    private String institutionCode;
    private String institutionName;
    private String name;
    private String age;
    private String gender;
    private String nic;
    private String address;
    private String contactNumber;
    private String serialNumber;
    private String specimenID;
    private String barcode;
    private String receivedDate;
    private String statusCode;
    private String statusDescription;
    private String result;
    private String resultDescription;
    private String plateCode;
    private String blockValue;
    private String createdUser;
    private Date createdTime;
    private Date reportTime;
    private String ct_target1;
    private String ct_target2;
}
