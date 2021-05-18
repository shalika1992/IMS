package com.epic.ims.bean.samplefileverification;

import com.epic.ims.bean.common.Status;
import com.epic.ims.mapping.institution.Institution;
import com.epic.ims.util.common.DataTablesRequest;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SampleFileVerificationInputBean extends DataTablesRequest {
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
    private String status;
    private String address;
    private String residentDistrict;
    private String contactNumber;
    private String secondaryContactNumber;
    private Date createdTime;
    private String createdUser;
    private Date lastUpdatedTime;
    private String lastUpdatedUser;
    private List<Institution> institutionList;
    private List<Status> statusList;
}
