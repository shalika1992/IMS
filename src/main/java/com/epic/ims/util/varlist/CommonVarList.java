package com.epic.ims.util.varlist;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonVarList {
    //-------------------------------system user details--------------------------------------------------------------//
    @Value("${system.username}")
    public String SYSTEMUSERNAME;

    //-------------------------------system user password expiry period--------------------------------------------------------------//
    @Value("${system.user.password.expire}")
    public String PWDEXPIRYPERIOD;

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

    @Value("${statuscode.new}")
    public String STATUS_NEW;

    @Value("${statuscode.reset}")
    public String STATUS_RESET;

    @Value("${statuscode.changed}")
    public String STATUS_CHANGED;

    @Value("${statuscode.expired}")
    public String STATUS_EXPIRED;
    //-------------------------------status codes---------------------------------------------------------------------//

    //-------------------------------user role code-------------------------------------------------------------------//
    @Value("${userrole.code.admin}")
    public String USERROLE_CODE_ADMIN;
    //-------------------------------user role code-------------------------------------------------------------------//

    //-------------------------------password param code--------------------------------------------------------------//
    @Value("${paramcode.password.expiryperiod}")
    public String PARAMCODE_PASSWORD_EXPIRYPERIOD;

    @Value("${paramcode.noof.invalidloginattempts}")
    public String PARAMCODE_NOOF_INVALIDLOGINATTEMPTS;

    @Value("${paramcode.idleaccount.expiryperiod}")
    public String PARAMCODE_IDLEACCOUNT_EXPIRYPERIOD;

    @Value("${paramcode.noof.historypassword}")
    public String PARAMCODE_NOOF_HISTORYPASSWORD;

    @Value("${paramcode.passwordexpiry.notificationperiod}")
    public String PARAMCODE_PASSWORDEXPIRY_NOTIFICATIONPERIOD;
    //-------------------------------password param code--------------------------------------------------------------//

    //-------------------------------timestamp per day----------------------------------------------------------------//
    @Value("${timestamp.value.perday}")
    public long TIMESTAMP_VALUE_PERDAY;
    //-------------------------------password param code--------------------------------------------------------------//

    //-------------------------------enable ad authentication code----------------------------------------------------//
    @Value("${enable.ad.authentication}")
    public Byte ENABLE_AD_AUTHENTICATION;
    //-------------------------------enable ad authentication code----------------------------------------------------//

    //-------------------------------password paramter default values ------------------------------------------------//
    @Value("${pwdparam.default.inacttime}")
    public int PWDPARAM_DEFAULT_INACTTIME;

    @Value("${pwdparam.default.pincount}")
    public int PWDPARAM_DEFAULT_PINCOUNT;

    @Value("${pwdparam.default.pwdage}")
    public int PWDPARAM_DEFAULT_PWDAGE;
    //-------------------------------password paramter default values ------------------------------------------------//

    //-------------------------------category paramter default values ------------------------------------------------//
    @Value("${category.isbulk.yes}")
    public String CATEGORY_ISBULK_YES;

    @Value("${category.isbulk.no}")
    public String CATEGORY_ISBULK_NO;
    //-------------------------------category paramter default values ------------------------------------------------//
}