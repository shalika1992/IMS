package com.epic.ims.bean.usermgt.sysuser;

import com.epic.ims.bean.common.Status;
import com.epic.ims.mapping.user.usermgt.UserRole;
import com.epic.ims.util.common.DataTablesRequest;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class SystemUserInputBean extends DataTablesRequest {
    private String userName;
    private String fullName;
    private String email;
    private String userRoleCode;
    private Date expiryDate;
    private String status;
    private String mobileNumber;
    private int initialLoginStatus;
    private int ad;
    private String password;
    private String confirmPassword;
    private Date createdTime;
    private Date lastUpdatedTime;
    private String lastUpdatedUser;

    private List<Status> statusList;
    private List<Status> statusActList;
    private List<UserRole> userRoleList;
}
