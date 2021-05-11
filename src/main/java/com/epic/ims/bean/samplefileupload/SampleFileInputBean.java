package com.epic.ims.bean.samplefileupload;

import com.epic.ims.util.common.DataTablesRequest;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SampleFileInputBean extends DataTablesRequest {
    private String receivedDate;
    private String referenceNo;
    private String institutionCode;
    private String name;
    private String age;
    private String symptomatic;
    private String contactType;
    private String nic;
    private String address;
    private String residentDistrict;
    private String contactNumber;
    private String secondaryContactNumber;
    private Date createdTime;
    private String createdUser;
    private Date lastUpdatedTime;
    private String lastUpdatedUser;
}
