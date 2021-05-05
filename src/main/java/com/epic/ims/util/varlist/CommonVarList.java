package com.epic.ims.util.varlist;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonVarList {
    //-------------------------------system user details--------------------------------------------------------------//
    @Value("${system.username}")
    public String SYSTEMUSERNAME;

    @Value("${system.password}")
    public String SYSTEMUSERPWD;

    @Value("${system.userrole.superuser}")
    public String SUPERUSER;
    //-------------------------------system user details--------------------------------------------------------------//

    //-------------------------------common validation codes----------------------------------------------------------//
    @Value("${common.validation.failcode}")
    public String COMMON_VALIDATION_FAIL_CODE;

    @Value("${common.validation.invalid.bean}")
    public String COMMON_VALIDATION_INVALID_BEANTYPE;
    //-------------------------------common validation codes----------------------------------------------------------//

    //-------------------------------status codes---------------------------------------------------------------------//
    @Value("${statuscode.active}")
    public String STATUS_ACTIVE;

    @Value("${statuscode.deactive}")
    public String STATUS_DEACTIVE;
    //-------------------------------status codes---------------------------------------------------------------------//
}
