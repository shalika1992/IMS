package com.epic.ims.mapping.user.usermgt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Component
@Scope("prototype")
public class UserRole {
    private String userroleCode;
    private String description;
    private String status;
    private String section;
    private String page;
    private List<String> assignedList;
    private List<String> unAssignedList;
    private Date createdTime;
    private Date lastUpdatedTime;
    private String lastUpdatedUser;
}
