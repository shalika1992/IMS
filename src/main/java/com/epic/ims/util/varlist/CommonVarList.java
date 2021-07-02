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

    @Value("${statuscode.received}")
    public String STATUS_RECEIVED;

    @Value("${statuscode.pending}")
    public String STATUS_PENDING;

    @Value("${statuscode.validated}")
    public String STATUS_VALIDATED;

    @Value("${statuscode.invalid}")
    public String STATUS_INVALID;

    @Value("${statuscode.rejected}")
    public String STATUS_REJECT;

    @Value("${statuscode.samplenotfound}")
    public String STATUS_NOSAMPLEFOUND;

    @Value("${statuscode.plateassigned}")
    public String STATUS_PLATEASSIGNED;

    @Value("${statuscode.reported}")
    public String STATUS_REPORTED;

    @Value("${statuscode.repeated}")
    public String STATUS_REPEATED;

    @Value("${statuscode.completed}")
    public String STATUS_COMPLETED;

    //-------------------------------status codes---------------------------------------------------------------------//

    //-------------------------------user role code-------------------------------------------------------------------//
    @Value("${userrole.code.counterofficer}")
    public String USERROLE_CODE_COUNTEROFFICER;

    @Value("${userrole.code.medicalofficer}")
    public String USERROLE_CODE_MEDICALOFFICER;

    @Value("${userrole.code.admin}")
    public String USERROLE_CODE_ADMIN;

    @Value("${userrole.description.counterofficer}")
    public String USERROLE_DESCRIPTION_COUNTEROFFICER;

    @Value("${userrole.description.medicalofficer}")
    public String USERROLE_DESCRIPTION_MEDICALOFFICER;

    @Value("${userrole.description.admin}")
    public String USERROLE_DESCRIPTION_ADMIN;
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

    //-------------------------------password paramter default values ------------------------------------------------//
    @Value("${pwdparam.default.inacttime}")
    public int PWDPARAM_DEFAULT_INACTTIME;

    @Value("${pwdparam.default.pincount}")
    public int PWDPARAM_DEFAULT_PINCOUNT;

    @Value("${pwdparam.default.pwdage}")
    public int PWDPARAM_DEFAULT_PWDAGE;
    //-------------------------------password paramter default values ------------------------------------------------//

    //-------------------------------default password policy ---------------------------------------------------------//
    @Value("${default.passwordpolicy}")
    public int DEFAULT_PASSWORDPOLICY;
    //-------------------------------web password policy id-----------------------------------------------------------//

    //-------------------------------system user password expiry period-----------------------------------------------//
    @Value("${system.user.password.expire}")
    public String PWDEXPIRYPERIOD;
    //-------------------------------system user details--------------------------------------------------------------//

    //-------------------------------mandatory field count------------------------------------------------------------//
    @Value("${minimum.mandatory.fieldcount}")
    public static int MINIMUM_MANDATORY_FIELDCOUNT;
    //-------------------------------mandatory field count------------------------------------------------------------//

    //-------------------------------sample file upload batch size----------------------------------------------------//
    @Value("${bulkupload.batch.size}")
    public int BULKUPLOAD_BATCH_SIZE;

    @Value("${bulkupdate.batch.size}")
    public int BULKUPDATE_BATCH_SIZE;
    //-------------------------------sample file upload batch size----------------------------------------------------//

    //-------------------------------result codes---------------------------------------------------------------------//
    @Value("${result.code.detected}")
    public String RESULT_CODE_DETECTED;

    @Value("${result.code.notdetected}")
    public String RESULT_CODE_NOTDETECTED;

    @Value("${result.code.pending}")
    public String RESULT_CODE_PENDING;

    @Value("${result.code.repeated}")
    public String RESULT_CODE_REPEATED;

    @Value("${result.code.invalid}")
    public String RESULT_CODE_INVALID;

    @Value("${result.code.inconclusive}")
    public String RESULT_CODE_INCONCLUSIVE;

    @Value("${result.code.reject}")
    public String RESULT_CODE_REJECT;
    //-------------------------------result codes---------------------------------------------------------------------//

    //-------------------------------report content codes-------------------------------------------------------------//
    @Value("${report.consultant.name}")
    public String REPORT_CONSULTANT_NAME;

    @Value("${report.consultant.description}")
    public String REPORT_CONSULTANT_DESCRIPTION;

    @Value("${report.consultant2.name}")
    public String REPORT_CONSULTANT2_NAME;

    @Value("${report.consultant2.description}")
    public String REPORT_CONSULTANT2_DESCRIPTION;

    @Value("${report.test.method}")
    public String REPORT_TEST_METHOD;
    //-------------------------------report content codes-------------------------------------------------------------//

    //-------------------------------report content codes-------------------------------------------------------------//
    @Value("${plate.position.c3}")
    public String PLATE_POSITION_C3;

    @Value("${plate.position.g12}")
    public String PLATE_POSITION_G12;
    //-------------------------------report content codes-------------------------------------------------------------//

    //-------------------------------color codes-------------------------------------------------------------//
    @Value("${well.color.code.normal}")
    public String COLOR_CODE_NORMAL;
    @Value("${well.color.code.pbs}")
    public String COLOR_CODE_PBS;
    @Value("${well.color.code.blank}")
    public String COLOR_CODE_BLANK;
    @Value("${well.color.code.positive}")
    public String COLOR_CODE_POSITIVE;
    //-------------------------------color codes-------------------------------------------------------------//

    //-------------------------------report content codes-------------------------------------------------------------//
    @Value("${masterfile.linux.filepath}")
    public String MASTERFILE_LINUX_FILEPATH;

    @Value("${masterfile.windows.filepath}")
    public String MASTERFILE_WINDOWS_FILEPATH;
    //-------------------------------report content codes-------------------------------------------------------------//

    //-------------------------------report content codes-------------------------------------------------------------//
    @Value("${default.max.labcode}")
    public String DEFAULT_MAX_LABCODE;
    //-------------------------------report content codes-------------------------------------------------------------//

    //-------------------------------plate pool codes-----------------------------------------------------------------//
    @Value("${plate.poolcode.yes}")
    public String PLATE_POOLCODE_YES;

    @Value("${plate.poolcode.no}")
    public String PLATE_POOLCODE_NO;

    @Value("${plate.pooldescription.no}")
    public String PLATE_POOLDESCRIPTION_YES;

    @Value("${plate.pooldescription.yes}")
    public String PLATE_POOLDESCRIPTION_NO;
    //-------------------------------plate pool codes-----------------------------------------------------------------//

    //-------------------------------report content codes-------------------------------------------------------------//
    @Value("${rejectedfile.linux.jasperfilepath}")
    public String REJECTEDREPORTFILE_LINUX_JASPER_FILEPATH;

    @Value("${rejectedfile.windows.jasperfilepath}")
    public String REJECTEDREPORTFILE_WINDOWS_JASPER_FILEPATH;

    @Value("${masterfile.linux.jasperfilepath}")
    public String MASTERFILE_LINUX_JASPER_FILEPATH;

    @Value("${masterfile.windows.jasperfilepath}")
    public String MASTERFILE_WINDOWS_JASPER_FILEPATH;

    @Value("${masterreportfile.linux.jasperfilepath}")
    public String MASTERREPORTFILE_LINUX_JASPER_FILEPATH;

    @Value("${masterreportfile.windows.jasperfilepath}")
    public String MASTERREPORTFILE_WINDOWS_JASPER_FILEPATH;

    @Value("${individualreportfile.linux.jasperfilepath}")
    public String INDIVIDUALREPORTFILE_LINUX_JASPER_FILEPATH;

    @Value("${individualreportfile.windows.jasperfilepath}")
    public String INDIVIDUALREPORTFILE_WINDOWS_JASPER_FILEPATH;
    //-------------------------------report content codes-------------------------------------------------------------//
}

