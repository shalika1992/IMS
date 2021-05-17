package com.epic.ims.bean.rejectedsample;

import com.epic.ims.mapping.institution.Institution;
import com.epic.ims.util.common.DataTablesRequest;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RejectedSampleDataInputBean extends DataTablesRequest {
    private String referenceNo;
    private String institutionCode;
    private String name;
    private String age;
    private String gender;
    private String symptomatic;
    private String contactType;
    private String nic;
    private String address;
    private String contactNo;
    private Date receivedDate;
    private String status;
    private String district;
    private Timestamp createdTime;
    private String remark;

    private List<Institution> institutionList;
}
