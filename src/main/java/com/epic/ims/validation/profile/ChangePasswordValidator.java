package com.epic.ims.validation.profile;

import com.epic.ims.bean.profile.PasswordChangeBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.passwordpolicy.PasswordPolicy;
import com.epic.ims.mapping.user.usermgt.User;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.passwordpolicy.PasswordPolicyRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.security.SHA256Algorithm;
import com.epic.ims.util.validation.Validation;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

@Component
public class ChangePasswordValidator implements Validator {
    private static Logger logger = LogManager.getLogger(ChangePasswordValidator.class);

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    Validation validation;

    @Autowired
    PasswordPolicyRepository passwordPolicyRepository;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    SHA256Algorithm sha256Algorithm;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    Common common;

    @Autowired
    MessageSource messageSource;

    @Override
    public boolean supports(Class<?> aClass) {
        return PasswordChangeBean.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {
            Locale locale = Locale.getDefault();
            SortedMap<String, Field> allFields = new TreeMap<>();
            //get fields from dto
            for (Field field : o.getClass().getDeclaredFields()) {
                allFields.put(field.getName(), field);
            }
            if (o.getClass().equals(PasswordChangeBean.class)) {
                boolean isNewPasswordOk = true;
                boolean isVisitedNewPassword = false;

                boolean isNewConfirmPasswordOk = true;
                boolean isVisitedNewConfirmPassword = false;

                Field[] requiredFields = this.getRequiredFields(allFields, (PasswordChangeBean) o);
                //validate fields
                for (Field field : requiredFields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();

                    if (fieldName.equals("oldPassword")) {
                        //validate the empty and null in old password
                        String oldPassword = ((PasswordChangeBean) o).getOldPassword();
                        if (validation.isEmptyFieldValue(oldPassword)) {
                            errors.rejectValue(fieldName, messageSource.getMessage(MessageVarList.USER_CURRENTPASSWORD_EMPTY, null, locale), messageSource.getMessage(MessageVarList.USER_CURRENTPASSWORD_EMPTY, null, locale));
                        }
                        //check the current password is match with user password
                        User user = sessionBean.getUser();
                        String password = user.getPassword();
                        String hashPassword = sha256Algorithm.makeHash(oldPassword);
                        if (!password.equals(hashPassword)) {
                            errors.rejectValue(fieldName, messageSource.getMessage(MessageVarList.USER_CURRENTPASSWORD_INVALID, null, locale), messageSource.getMessage(MessageVarList.USER_CURRENTPASSWORD_INVALID, null, locale));
                        }

                    } else if (fieldName.equals("newPassword")) {
                        isVisitedNewPassword = true;
                        //validate the empty and null in new password
                        String newPassword = ((PasswordChangeBean) o).getNewPassword();
                        if (validation.isEmptyFieldValue(newPassword)) {
                            isNewPasswordOk = false;
                            errors.rejectValue(fieldName, messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_EMPTY, null, locale), messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_EMPTY, null, locale));
                        }

                    } else if (fieldName.equals("confirmNewPassword")) {
                        isVisitedNewConfirmPassword = true;
                        //validate the empty and null in confirm new password
                        String newConfirmPassword = ((PasswordChangeBean) o).getConfirmNewPassword();
                        if (validation.isEmptyFieldValue(newConfirmPassword)) {
                            isNewConfirmPasswordOk = false;
                            errors.rejectValue(fieldName, messageSource.getMessage(MessageVarList.USER_NEWCONFIRMPASSWORD_EMPTY, null, locale), MessageVarList.USER_NEWCONFIRMPASSWORD_EMPTY);
                        }
                    }

                    //check the custom validations
                    if (isVisitedNewPassword && isNewPasswordOk && isVisitedNewConfirmPassword && isNewConfirmPasswordOk) {
                        int minUpperCharacters = 0;
                        int minLowerCharacters = 0;
                        int minNumericCharacters = 0;
                        int minSpecialCharacters = 0;

                        String newPassword = ((PasswordChangeBean) o).getNewPassword();
                        for (int i = 0; i < newPassword.length(); i++) {
                            char letter = newPassword.charAt(i);

                            if (Character.isUpperCase(letter)) {
                                minUpperCharacters++;
                            } else if (Character.isLowerCase(letter)) {
                                minLowerCharacters++;
                            } else if (Character.isDigit(letter)) {
                                minNumericCharacters++;
                            } else if (!Character.isLetterOrDigit(letter)) {
                                minSpecialCharacters++;
                            }
                        }

                        //check the password policy
                        PasswordPolicy passwordPolicy = sessionBean.getPasswordPolicy();
                        if (passwordPolicy != null) {
                            if (passwordPolicy.getMinimumLength() > newPassword.length()) {
                                errors.rejectValue(fieldName, messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_TOO_SHORT, null, locale), messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_TOO_SHORT, null, locale));

                            } else if (passwordPolicy.getMaximumLength() < newPassword.length()) {
                                errors.rejectValue(fieldName, messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_TOO_LONG, null, locale), messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_TOO_LONG, null, locale));

                            } else if (passwordPolicy.getMinimumSpecialCharacters() > minSpecialCharacters) {
                                errors.rejectValue(fieldName, messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_LESS_SPECIALCHARACTERS, null, locale), messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_LESS_SPECIALCHARACTERS, null, locale));

                            } else if (passwordPolicy.getMinimumUpperCaseCharacters() > minUpperCharacters) {
                                errors.rejectValue(fieldName, messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_LESS_UPPERCHARACTERS, null, locale), messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_LESS_UPPERCHARACTERS, new Object[]{(int) passwordPolicy.getMinimumUpperCaseCharacters()}, locale));

                            } else if (passwordPolicy.getMinimumLowerCaseCharacters() > minLowerCharacters) {
                                errors.rejectValue(fieldName, messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_LESS_LOWERCHARACTERS, null, locale), messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_LESS_LOWERCHARACTERS, null, locale));

                            } else if (passwordPolicy.getMinimumNumericalCharacters() > minNumericCharacters) {
                                errors.rejectValue(fieldName, messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_LESS_NUMERICCHARACTERS, null, locale), messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_LESS_NUMERICCHARACTERS, null, locale));

                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Exception : ", ex);
            errors.reject(commonVarList.COMMON_VALIDATION_FAIL_CODE);
        }
    }

    private Field[] getRequiredFields(SortedMap<String, Field> allFields, PasswordChangeBean o) {
        return new Field[]{allFields.get("oldPassword"), allFields.get("newPassword"), allFields.get("confirmNewPassword")};
    }
}
