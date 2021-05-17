package com.epic.ims.bean.reportmgt;

import com.epic.ims.bean.common.CommonInstitution;
import com.epic.ims.bean.common.Result;
import com.epic.ims.bean.common.Status;
import lombok.*;

import javax.servlet.annotation.MultipartConfig;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@MultipartConfig
public class MasterDataInputBeen {

    private long sampleID;
    private String referenceNumber;
    private String institutionCode;
    private String name;
    private String age;
    private String gender;
    private String nic;
    private String contactNumber;
    private String serialNumber;
    private String specimenID;
    private String barcode;
    private Date receivedDate;
    private String status;
    private int plateID;
    private String blockValue;
    private boolean isPool;
    private boolean isVerified;
    private boolean isComplete;
    private String result;
    private String createdUser;
    private Date createdTime;
    private Date reportTime;


    private List<Status> statusList;
    private List<Result> resultList;
    private List<CommonInstitution> commonInstitutionList;

}
