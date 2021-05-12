package com.epic.ims.validation.institution;

import com.epic.ims.bean.institutionmgt.InstitutionInputBean;
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
public class InstitutionBeanValidator implements Validator {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    CommonVarList commonVarList;
    @Override
    public boolean supports(Class<?> aClass) {
        return InstitutionInputBean.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {

            if (o.getClass().equals(InstitutionInputBean.class)) {

                //validate input fields for empty fields
                    ValidationUtils.rejectIfEmpty(errors,"institutionCode", MessageVarList.INSTITUTION_MGT_EMPTY_CODE,"Institution Code can not be empty.");
                    ValidationUtils.rejectIfEmpty(errors,"institutionName", MessageVarList.INSTITUTION_MGT_EMPTY_NAME,"Institution name can not be empty.");
                    ValidationUtils.rejectIfEmpty(errors,"address", MessageVarList.INSTITUTION_MGT_EMPTY_ADDRESS, "Address can not be empty.");
                    ValidationUtils.rejectIfEmpty(errors,"contactNumber", MessageVarList.INSTITUTION_MGT_EMPTY_CONTACT_NUMBER, "Contact Number can not be empty.");
                    ValidationUtils.rejectIfEmpty(errors,"status", MessageVarList.INSTITUTION_MGT_EMPTY_STATUS, "Status can not be empty.");


            } else {
                errors.reject(commonVarList.COMMON_VALIDATION_INVALID_BEANTYPE);
            }
        } catch (Exception ex) {
            logger.error("Exception : ", ex);
            errors.reject(commonVarList.COMMON_VALIDATION_FAIL_CODE);
        }
    }



}
