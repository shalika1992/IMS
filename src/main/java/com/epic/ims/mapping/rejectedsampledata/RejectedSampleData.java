package com.epic.ims.mapping.rejectedsampledata;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
@Data
@Scope("request")
@AllArgsConstructor
@NoArgsConstructor
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
    private Date receivedDate;
    private String status;
    private String remark;
    private String createdUser;
    private Timestamp createdTime;
}
