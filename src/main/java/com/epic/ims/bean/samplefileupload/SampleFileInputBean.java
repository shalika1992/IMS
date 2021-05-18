package com.epic.ims.bean.samplefileupload;

import com.epic.ims.mapping.district.District;
import com.epic.ims.mapping.institution.Institution;
import com.epic.ims.util.common.DataTablesRequest;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SampleFileInputBean extends DataTablesRequest {
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
    private String residentDistrict;
    private String contactNumber;
    private String secondaryContactNumber;
    private Date createdTime;
    private String createdUser;
    private Date lastUpdatedTime;
    private String lastUpdatedUser;
    private MultipartFile sampleFile;
    private List<Institution> institutionList;
    private List<District> districtList;
}
