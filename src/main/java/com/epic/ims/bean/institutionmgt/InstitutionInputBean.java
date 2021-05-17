package com.epic.ims.bean.institutionmgt;

import com.epic.ims.bean.common.Status;
import com.epic.ims.util.common.DataTablesRequest;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
public class InstitutionInputBean extends DataTablesRequest {
    private String institutionCode;
    private String name;
    private String institutionName;
    private String address;
    private String contactNumber;
    private String status;
    private String userTask;
    private Date createdTime;
    private Date lastUpdatedTime;
    private String lastUpdatedUser;
    private CommonsMultipartFile institutionBulk;
    private int rowNumber;


    private List<Status> statusList;
    private List<Status> statusActList;

}
