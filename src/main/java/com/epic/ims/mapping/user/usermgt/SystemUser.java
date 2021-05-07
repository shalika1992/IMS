package com.epic.ims.mapping.user.usermgt;

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
public class SystemUser {

    private String userName;
    private String fullName;
    private String userRole;
    private String userRoleCode;
    private String email;
    private String mobileNumber;
    private int noOfInvalidAttempt;
    private Date expiryDate;
    private Date lastLoggedDate;
    private String status;
    private String password;
    private String confirmPassword;
    private Date createdTime;
    private Date lastUpdatedTime;
    private String lastUpdatedUser;

}
