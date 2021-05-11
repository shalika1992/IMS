package com.epic.ims.util.validation;

import com.epic.ims.util.varlist.CommonVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Validation {
    private static Logger logger = LogManager.getLogger(Validation.class);

    @Autowired
    CommonVarList commonVarList;

    /**
     * @Author shalika_w
     * @CreatedTime 2021-01-12 09:31:16 AM
     * @Version V1.00
     * @MethodName isEmptyFieldValue
     * @MethodParams [value]
     * @MethodDescription - Validate the null and empty string
     */
    public boolean isEmptyFieldValue(String value) {
        boolean isErrorField = true;
        try {
            if (value != null && !value.isEmpty()) {
                isErrorField = false;
            }
        } catch (Exception e) {
            logger.error("Exception : ", e);
        }
        return isErrorField;
    }

    /**
     * @Author shalika_w
     * @CreatedTime 2021-01-12 09:35:07 AM
     * @Version V1.00
     * @MethodName checkSpecialCharacters
     * @MethodParams [input]
     * @MethodDescription - Validate the special characters in a string.
     */
    public boolean checkSpecialCharacters(String input) {
        boolean status = false;
        if (!input.matches("[A-Za-z0-9]+")) {
            status = true;
        }
        return status;
    }
}
