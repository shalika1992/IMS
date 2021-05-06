package com.epic.ims.validation.sysuser;

import com.epic.ims.bean.usermgt.sysuser.SystemUserInputBean;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component
public class SystemUserValidator implements Validator {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    CommonVarList commonVarList;

    @Override
    public boolean supports(Class<?> aClass) {
        return SystemUserInputBean.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {


            if (o.getClass().equals(SystemUserInputBean.class)) {

                //validate input fields for empty fields
                ValidationUtils.rejectIfEmpty(errors,"userName", MessageVarList.SYSTEMUSER_MGT_EMPTY_USERNAME,"Username can not be empty.");
                ValidationUtils.rejectIfEmpty(errors,"fullName", MessageVarList.SYSTEMUSER_MGT_EMPTY_FULLNAME,"Full name can not be empty.");
                ValidationUtils.rejectIfEmpty(errors,"email", MessageVarList.SYSTEMUSER_MGT_EMPTY_EMAIL, "Email can not be empty.");
                ValidationUtils.rejectIfEmpty(errors,"userRoleCode", MessageVarList.SYSTEMUSER_MGT_EMPTY_USERROLECODE, "Empty user role..");
                ValidationUtils.rejectIfEmpty(errors,"userRoleCode", MessageVarList.SYSTEMUSER_MGT_EMPTY_USERROLECODE, "Mt Port can not be empty.");
                ValidationUtils.rejectIfEmpty(errors,"status", MessageVarList.SYSTEMUSER_MGT_EMPTY_STATUS, "Status can not be empty.");
                ValidationUtils.rejectIfEmpty(errors,"mobileNumber", MessageVarList.SYSTEMUSER_MGT_EMPTY_MOBILENUMBER, "Mobile Number can not be empty.");
                ValidationUtils.rejectIfEmpty(errors,"password", MessageVarList.SYSTEMUSER_MGT_EMPTY_PASSWORD, "Password can not be empty.");
                ValidationUtils.rejectIfEmpty(errors, "confirmPassword", MessageVarList.SYSTEMUSER_MGT_EMPTY_CONFIRMPASSWORD, "Confirm Password can not be empty.");

                //check password equality
                String password = ((SystemUserInputBean) o).getPassword();
                String confirmPassword = ((SystemUserInputBean) o).getConfirmPassword();

                if (password!=null && !password.isEmpty() && confirmPassword!=null && !confirmPassword.isEmpty()){
                    if (!password.equals(confirmPassword)){
                        errors.rejectValue("confirmPassword", MessageVarList.SYSTEMUSER_MGT_PASSWORDS_MISMATCH, MessageVarList.SYSTEMUSER_MGT_PASSWORDS_MISMATCH);
                        errors.reject("confirmPassword", new Object[]{MessageVarList.SYSTEMUSER_MGT_PASSWORDS_MISMATCH}, MessageVarList.SYSTEMUSER_MGT_PASSWORDS_MISMATCH);
                    }
                }

            } else {
                errors.reject(commonVarList.COMMON_VALIDATION_INVALID_BEANTYPE);
            }
        } catch (Exception ex) {
            logger.error("Exception : ", ex);
            errors.reject(commonVarList.COMMON_VALIDATION_FAIL_CODE);
        }
    }

}
