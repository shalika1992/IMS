package com.epic.ims.validation.samplefileupload;

import com.epic.ims.bean.samplefileupload.SampleFileInputBean;
import com.epic.ims.util.validation.Validation;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import com.epic.ims.util.varlist.TaskVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.lang.reflect.Field;
import java.util.SortedMap;
import java.util.TreeMap;

@Component
public class SampleFileUploadValidator implements Validator {
    private static Logger logger = LogManager.getLogger(SampleFileUploadValidator.class);

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    Validation validation;

    @Override
    public boolean supports(Class<?> aClass) {
        return SampleFileInputBean.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {
            SortedMap<String, Field> allFields = new TreeMap<>();
            //get fields from dto
            for (Field field : o.getClass().getDeclaredFields()) {
                allFields.put(field.getName(), field);
            }

            if (o.getClass().equals(SampleFileInputBean.class)) {
                Field[] requiredFields = this.getRequiredFields(allFields, (SampleFileInputBean) o);
                //validate fields
                for (Field field : requiredFields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();

                    //validate the system user add and edit
                    String userTask = ((SampleFileInputBean) o).getUserTask();
                    if (userTask.equals(TaskVarList.ADD_TASK)) {
                        if (fieldName.equals("referenceNo")) {
                            //validate the reference no
                            String referenceNo = ((SampleFileInputBean) o).getReferenceNo();
                            if (validation.isEmptyFieldValue(referenceNo)) {
                                errors.rejectValue(fieldName, MessageVarList.SAMPLERECORD_EMPTY_REFNO, MessageVarList.SAMPLERECORD_EMPTY_REFNO);
                            }

                        } else if (fieldName.equals("institutionCode")) {
                            //validate the institution code
                            String institutionCode = ((SampleFileInputBean) o).getInstitutionCode();
                            if (validation.isEmptyFieldValue(institutionCode)) {
                                errors.rejectValue(fieldName, MessageVarList.SAMPLERECORD_EMPTY_INSTITUTION, MessageVarList.SAMPLERECORD_EMPTY_INSTITUTION);
                            }

                        } else if (fieldName.equals("name")) {
                            //validate name
                            String name = ((SampleFileInputBean) o).getName();
                            if (validation.isEmptyFieldValue(name)) {
                                errors.rejectValue(fieldName, MessageVarList.SAMPLERECORD_EMPTY_NAME, MessageVarList.SAMPLERECORD_EMPTY_NAME);
                            }

                        } else if (fieldName.equals("age")) {
                            //validate name
                            String name = ((SampleFileInputBean) o).getAge();
                            if (validation.isEmptyFieldValue(name)) {
                                errors.rejectValue(fieldName, MessageVarList.SAMPLERECORD_EMPTY_AGE, MessageVarList.SAMPLERECORD_EMPTY_AGE);
                            }

                        } else if (fieldName.equals("gender")) {
                            //validate name
                            String name = ((SampleFileInputBean) o).getGender();
                            if (validation.isEmptyFieldValue(name)) {
                                errors.rejectValue(fieldName, MessageVarList.SAMPLERECORD_EMPTY_GENDER, MessageVarList.SAMPLERECORD_EMPTY_GENDER);
                            }

                        } else if (fieldName.equals("address")) {
                            //validate name
                            String address = ((SampleFileInputBean) o).getAddress();
                            if (validation.isEmptyFieldValue(address)) {
                                errors.rejectValue(fieldName, MessageVarList.SAMPLERECORD_EMPTY_GENDER, MessageVarList.SAMPLERECORD_EMPTY_GENDER);
                            }

                        }
//                        else if (fieldName.equals("nic")) {
//                            //validate nic
//                            String nic = ((SampleFileInputBean) o).getNic();
//                            if (validation.isEmptyFieldValue(nic)) {
//                                errors.rejectValue(fieldName, MessageVarList.SAMPLERECORD_EMPTY_NIC, MessageVarList.SAMPLERECORD_EMPTY_NIC);
//                            }
//                        }
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

    private Field[] getRequiredFields(SortedMap<String, Field> allFields, SampleFileInputBean o) {
        return new Field[]{
                allFields.get("userTask"),
                allFields.get("referenceNo"),
                allFields.get("institutionCode"),
                allFields.get("name"),
                allFields.get("age"),
                allFields.get("gender"),
                allFields.get("address")
                // allFields.get("nic")
        };
    }
}
