package com.epic.ims.mapping.institutionmgt;

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
public class Institution {

    private String institutionCode;
    private String institutionName;
    private String address;
    private String contactNumber;
    private String status;
    private Date createdTime;
    private Date lastUpdatedTime;
    private String lastUpdatedUser;
}
