package com.epic.ims.validation.sysuser;

import com.epic.ims.bean.usermgt.sysuser.SystemUserInputBean;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.lang.reflect.Field;
import java.util.SortedMap;
import java.util.TreeMap;

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

    }

    private Field[] getRequiredFields(SortedMap<String, Field> allFields, SystemUserInputBean o) {
        return new Field[]{
                allFields.get("userTask"),
                allFields.get("userName"),
                allFields.get("fullName"),
                allFields.get("email"),
                allFields.get("userRoleCode"),
                allFields.get("status"),
                allFields.get("mobileNumber"),
                allFields.get("password"),
                allFields.get("confirmPassword")
        };
    }
}
