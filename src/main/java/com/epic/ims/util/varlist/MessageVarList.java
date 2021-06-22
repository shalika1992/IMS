package com.epic.ims.util.varlist;

public class MessageVarList {
    //-------------------------- start common messages----------------------------------------------------------------//
    public static final String COMMON_ERROR_PROCESS = "common.error.process";
    public static final String COMMON_ERROR_RECORD_DOESNOT_EXISTS = "common.error.record.doesnot.exists";
    public static final String COMMON_ERROR_NO_VALUE_CHANGE = "common.error.no.value.change";
    public static final String COMMON_ERROR_ALRADY_USE = "common.error.alreadyuse";
    public static final String COMMON_ERROR_RECORD_ALREADY_EXISTS = "common.error.record.alreadyexists";
    //-------------------------- end common messages------------------------------------------------------------------//

    //-------------------------- start user login messages------------------------------------------------------------//
    public static final String LOGIN_USERNAME_EMPTY = "login.username.empty";
    public static final String LOGIN_USERNAME_INVALID = "login.username.invalid";
    public static final String LOGIN_PASSWORD_EMPTY = "login.password.empty";
    public static final String LOGIN_INVALID = "login.invalid";
    public static final String LOGIN_DEACTIVE = "login.deactive";
    public static final String LOGIN_IDLEDEACTIVE = "login.idledeactive";
    public static final String LOGIN_EXPIRYWARNING = "login.expirywarning";
    public static final String LOGIN_STATUSTIMEOUT = "login.statustimeout";
    //-------------------------- end user login messages--------------------------------------------------------------//

    //-------------------------- start password reset mgt-------------------------------------------------------------//
    public static final String PASSWORDRESET_NEWUSER = "passwordreset.newuser";
    public static final String PASSWORDRESET_RESETUSER = "passwordreset.resetuser";
    public static final String PASSWORDRESET_CHANGEPWD = "passwordreset.changepwd";
    public static final String PASSWORDRESET_EXPPWD = "passwordreset.exppwd";
    public static final String PASSWORDRESET_SUCCESS = "passwordreset.changepwd.success";
    public static final String PASSWORD_SAME_AS_PREVIOUS = "password.sameas.previous";
    //-------------------------- start password reset mgt-------------------------------------------------------------//

    //-------------------------- start user password change ----------------------------------------------------------//
    public static final String USER_CURRENTPASSWORD_EMPTY = "user.currentpassword.empty";
    public static final String USER_CURRENTPASSWORD_INVALID = "user.currentpassword.invalid";
    public static final String USER_NEWPASSWORD_EMPTY = "user.newpassword.empty";
    public static final String USER_NEWCONFIRMPASSWORD_EMPTY = "user.newconfirmpassword.empty";
    public static final String USER_NEWPASSWORD_TOO_SHORT = "user.newpassword.tooshort";
    public static final String USER_NEWPASSWORD_TOO_LONG = "user.newpassword.toolong";
    public static final String USER_NEWPASSWORD_LESS_SPECIALCHARACTERS = "user.newpassword.less.specialcharacters";
    public static final String USER_NEWPASSWORD_LESS_UPPERCHARACTERS = "user.newpassword.less.uppercharacters";
    public static final String USER_NEWPASSWORD_LESS_LOWERCHARACTERS = "user.newpassword.less.lowercharacters";
    public static final String USER_NEWPASSWORD_LESS_NUMERICCHARACTERS = "user.newpassword.less.numericcharacters";
    public static final String USER_NEWPASSWORD_MORE_REPEATCHARACTERS = "user.newpassword.more.repeatcharacters";
    public static final String USER_NEWPASSWORD_EXIST_PASSWORDHISTORY = "user.newpassword.exist.passwordhistory";
    public static final String USER_NEWPASSWORD_RESET_ERROR = "user.newpassword.reset.error";
    public static final String USER_SESSION_NOTFOUND = "user.session.notfound";
    public static final String USER_SESSION_INVALID = "user.session.invalid";
    public static final String USER_REQUESTED_PASSWORDCHANGE = "user.requested.passwordchange";
    public static final String USER_PRIVILEGE_INSUFFICIENT = "user.privilege.insufficient";
    //-------------------------- start user password change-----------------------------------------------------------//

    //-------------------------- system user mgt----------------------------------------------------------------------//
    public static final String SYSTEMUSER_MGT_EMPTY_USERNAME = "systemuser.empty.username";
    public static final String SYSTEMUSER_MGT_EMPTY_FULLNAME = "systemuser.empty.fullname";
    public static final String SYSTEMUSER_MGT_EMPTY_EMAIL = "systemuser.empty.email";
    public static final String SYSTEMUSER_MGT_EMPTY_USERROLECODE = "systemuser.empty.userrolecode";
    public static final String SYSTEMUSER_MGT_EMPTY_STATUS = "systemuser.empty.status";
    public static final String SYSTEMUSER_MGT_EMPTY_MOBILENUMBER = "systemuser.empty.mobilenumber";
    public static final String SYSTEMUSER_MGT_EMPTY_PASSWORD = "systemuser.empty.password";
    public static final String SYSTEMUSER_MGT_EMPTY_CONFIRMPASSWORD = "systemuser.empty.confirmpassword";
    public static final String SYSTEMUSER_MGT_PASSWORDS_MISMATCH = "systemuser.password.mismatch";
    public static final String SYSTEMUSER_MGT_ADDED_SUCCESSFULLY = "systemuser.added.success";
    public static final String SYSTEMUSER_MGT_UPDATE_SUCCESSFULLY = "systemuser.updated.success";
    public static final String SYSTEMUSER_MGT_DELETE_SUCCESSFULLY = "systemuser.delete.success";
    public static final String SYSTEMUSER_MGT_CONFIRM_SUCCESSFULLY = "systemuser.confirm.success";
    public static final String SYSTEMUSER_MGT_REJECT_SUCCESSFULLY = "systemuser.reject.success";
    public static final String SYSTEMUSER_MGT_ALREADY_EXISTS = "systemuser.already.exists";
    public static final String SYSTEMUSER_MGT_NORECORD_FOUND = "systemuser.norecord.found";
    public static final String SYSTEMUSER_MGT_CHANGE_PASSWORD_SUCCESSFULLY = "systemuser.changed.password.success";
    //-------------------------- system user mgt----------------------------------------------------------------------//

    //-------------------------- institution user mgt-----------------------------------------------------------------//
    public static final String INSTITUTION_MGT_ALREADY_EXISTS = "institution.already.exists";
    public static final String INSTITUTION_BULK_MGT_ALREADY_EXISTS = "institutionbulk.already.exists";
    public static final String INSTITUTION_MGT_ADDED_SUCCESSFULLY = "institution.added.success";
    public static final String INSTITUTION_BULK_MGT_ADDED_SUCCESSFULLY = "institutionbulk.added.success";
    public static final String INSTITUTION_MGT_EMPTY_CODE = "institution.empty.code";
    public static final String INSTITUTION_MGT_EMPTY_NAME = "institution.empty.name";
    public static final String INSTITUTION_MGT_EMPTY_ADDRESS = "institution.empty.address";
    public static final String INSTITUTION_MGT_EMPTY_CONTACT_NUMBER = "institution.empty.contactNumber";
    public static final String INSTITUTION_MGT_EMPTY_STATUS = "institution.empty.status";
    public static final String INSTITUTION_MGT_EMPTY_FILE = "institution.empty.file";
    public static final String INSTITUTION_MGT_NORECORD_FOUND = "institution.norecord.found";
    public static final String INSTITUTION_MGT_UPDATE_SUCCESSFULLY = "institution.updated.success";
    public static final String INSTITUTION_MGT_DELETE_SUCCESSFULLY = "institution.delete.success";
    public static final String INSTITUTION_MGT_INVALID_FILE = "institution.invalid.file";
    public static final String INSTITUTION_MGT_INVALID_COLUMN_ORDER = "institution.invalid.columnorder";
    public static final String INSTITUTION_MGT_DUPLICATE_RECORD = "institution.duplicate.record";
    public static final String INSTITUTION_MGT_EMPTY_SHEET = "institution.empty.sheet";
    public static final String INSTITUTION_MGT_INVALID_DATA = "institution.invalid.data";

    public static final String INSTITUTION_MGT_EMPTY_MOBILENUMBER = "institution.empty.mobilenumber";
    public static final String INSTITUTION_MGT_EMPTY_PASSWORD = "institution.empty.password";
    public static final String INSTITUTION_MGT_EMPTY_CONFIRMPASSWORD = "institution.empty.confirmpassword";
    public static final String INSTITUTION_MGT_PASSWORDS_MISMATCH = "institution.password.mismatch";
    public static final String INSTITUTION_MGT_CONFIRM_SUCCESSFULLY = "institution.confirm.success";
    public static final String INSTITUTION_MGT_REJECT_SUCCESSFULLY = "institution.reject.success";
    //-------------------------- institution user mgt-----------------------------------------------------------------//

    //-------------------------- sample file upload mgt---------------------------------------------------------------//
    public static final String SAMPLERECORD_EMPTY_REFNO = "samplerecord.empty.refno";
    public static final String SAMPLERECORD_EMPTY_INSTITUTION = "samplerecord.empty.institution";
    public static final String SAMPLERECORD_EMPTY_NAME = "samplerecord.empty.name";
    public static final String SAMPLERECORD_EMPTY_AGE = "samplerecord.empty.age";
    public static final String SAMPLERECORD_EMPTY_GENDER = "samplerecord.empty.gender";
    public static final String SAMPLERECORD_EMPTY_NIC = "samplerecord.empty.nic";
    public static final String SAMPLERECORD_ALREADY_EXISTS = "samplerecord.already.exists";
    public static final String SAMPLERECORD_ADDED_SUCCESSFULLY = "samplerecord.added.success";
    public static final String SAMPLERECORD_UPDATE_SUCCESSFULLY = "samplefilerecord.updated.success";
    public static final String SAMPLEFILE_INVALID_FILE = "samplefile.invalid.file";
    public static final String SAMPLEFILE_INVALID_FILETYPE = "samplefile.invalid.filetype";
    public static final String SAMPLEFILE_CONTAIN_MAXRECORDS = "samplefile.contain.maxrecords";
    public static final String SAMPLEFILE_UPLOAD_SUCCESSFULLY = "samplefile.upload.success";
    public static final String SAMPLERECORD_NORECORD_FOUND = "samplerecord.norecord.found";
    public static final String SAMPLERECORD_REJECT_SUCCESSFULLY = "samplerecord.reject.success";
    public static final String SAMPLERECORD_VERIFY_SUCCESSFULLY = "samplerecord.verify.success";
    public static final String SAMPLERECORD_MANDATORYFILED_VALIDATIONFAIL = "samplerecord.mandatoryfield.validationfail";
    public static final String SAMPLERECORD_DUPLICATE_RECORDFOUND = "samplerecord.duplicate.recordfound";
    public static final String SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL = "samplerecord.specialcharacter.validationfail";
    //-------------------------- sample file upload mgt---------------------------------------------------------------//

    //-------------------------- result update mgt--------------------------------------------------------------------//
    public static final String SAMPLERESULT_UPDATE_SUCCESSFULLY = "sampleresult.update.success";
    //-------------------------- result update mgt--------------------------------------------------------------------//

    //-------------------------- sample verify file mgt---------------------------------------------------------------//
    public static final String SAMPLE_VERIFY_FILE_RECORD_UPDATE_SUCCESSFULLY = "sampleverifyfilerecord.updated.success";
    public static final String SAMPLE_VERIFY_FILE_RECORD_EMPTY_LABCODE = "sampleverifyfilerecord.empty.labcode";
    //------------------------- sample verify file mgt---------------------------------------------------------------//
}
