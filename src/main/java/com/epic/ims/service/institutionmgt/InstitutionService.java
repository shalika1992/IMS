package com.epic.ims.service.institutionmgt;

import com.epic.ims.bean.institutionmgt.InstitutionInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.bean.usermgt.sysuser.SystemUserInputBean;
import com.epic.ims.mapping.institutionmgt.Institution;
import com.epic.ims.mapping.user.usermgt.SystemUser;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.institutionmgt.InstitutionRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.MessageVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Scope("prototype")
public class InstitutionService {
    @Autowired
    InstitutionRepository institutionRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    Common common;

    public String deleteInstitution(String institutionCode) {
        String message = "";
        Institution existingInstitution = null;
        try {
            existingInstitution = institutionRepository.getInstitution(institutionCode);
            if (existingInstitution != null) {
                message = institutionRepository.deleteInstitution(institutionCode);
            } else {
                message = MessageVarList.INSTITUTION_MGT_NORECORD_FOUND;
            }
        } catch (EmptyResultDataAccessException ere) {
            message = MessageVarList.INSTITUTION_MGT_NORECORD_FOUND;

        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    public String updateInstitution(InstitutionInputBean institutionInputBean) {
        String message = "";
        Institution existingInstitution = null;
        try {
            existingInstitution = institutionRepository.getInstitution(institutionInputBean.getInstitutionCode());
            if (existingInstitution != null) {
                //check changed values
                String oldValueAsString = this.getInstitutionAsString(existingInstitution, true);
                String newValueAsString = this.getInstitutionAsString(institutionInputBean, true);
                //check the old value and new value
                if (oldValueAsString.equals(newValueAsString)) {
                    message = MessageVarList.COMMON_ERROR_NO_VALUE_CHANGE;
                } else {
                    //set the other values to input bean
                    Date currentDate = commonRepository.getCurrentDate();
                    String lastUpdatedUser = sessionBean.getUsername();

                    institutionInputBean.setCreatedTime(currentDate);
                    institutionInputBean.setLastUpdatedTime(currentDate);
                    institutionInputBean.setLastUpdatedUser(lastUpdatedUser);

                    message = institutionRepository.updateInstitution(institutionInputBean);

                }
            } else {
                message = MessageVarList.INSTITUTION_MGT_NORECORD_FOUND;
            }
        } catch (EmptyResultDataAccessException ere) {
            message = MessageVarList.INSTITUTION_MGT_NORECORD_FOUND;

        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    private String getInstitutionAsString(Institution institution, boolean checkChanges) {
        StringBuilder institutionStringBuilder = new StringBuilder();
        try {
            if (institution != null) {

                if (institution.getInstitutionCode() != null && !institution.getInstitutionCode().isEmpty()) {
                    institutionStringBuilder.append(institution.getInstitutionCode());
                } else {
                    institutionStringBuilder.append("error");
                }

                institutionStringBuilder.append("|");
                if (institution.getInstitutionName() != null && !institution.getInstitutionName().isEmpty()) {
                    institutionStringBuilder.append(institution.getInstitutionName());
                } else {
                    institutionStringBuilder.append("--");
                }

                institutionStringBuilder.append("|");
                if (institution.getAddress() != null && !institution.getAddress().isEmpty()) {
                    institutionStringBuilder.append(institution.getAddress());
                } else {
                    institutionStringBuilder.append("--");
                }

                institutionStringBuilder.append("|");
                if (institution.getContactNumber() != null && !institution.getContactNumber().isEmpty()) {
                    institutionStringBuilder.append(institution.getContactNumber());
                } else {
                    institutionStringBuilder.append("--");
                }

                institutionStringBuilder.append("|");
                if (institution.getStatus() != null && !institution.getStatus().isEmpty()) {
                    institutionStringBuilder.append(institution.getStatus());
                } else {
                    institutionStringBuilder.append("--");
                }

            }
        } catch (Exception e) {
            throw e;
        }
        return institutionStringBuilder.toString();
    }

    private String getInstitutionAsString(InstitutionInputBean institutionInputBean, boolean checkChanges) {
        StringBuilder institutionStringBuilder = new StringBuilder();
        try {
            if (institutionInputBean != null) {

                if (institutionInputBean.getInstitutionCode() != null && !institutionInputBean.getInstitutionCode().isEmpty()) {
                    institutionStringBuilder.append(institutionInputBean.getInstitutionCode());
                } else {
                    institutionStringBuilder.append("error");
                }

                institutionStringBuilder.append("|");
                if (institutionInputBean.getInstitutionName() != null && !institutionInputBean.getInstitutionName().isEmpty()) {
                    institutionStringBuilder.append(institutionInputBean.getInstitutionName());
                } else {
                    institutionStringBuilder.append("--");
                }

                institutionStringBuilder.append("|");
                if (institutionInputBean.getAddress() != null && !institutionInputBean.getAddress().isEmpty()) {
                    institutionStringBuilder.append(institutionInputBean.getAddress());
                } else {
                    institutionStringBuilder.append("--");
                }

                institutionStringBuilder.append("|");
                if (institutionInputBean.getContactNumber() != null && !institutionInputBean.getContactNumber().isEmpty()) {
                    institutionStringBuilder.append(institutionInputBean.getContactNumber());
                } else {
                    institutionStringBuilder.append("--");
                }

                institutionStringBuilder.append("|");
                if (institutionInputBean.getStatus() != null && !institutionInputBean.getStatus().isEmpty()) {
                    institutionStringBuilder.append(institutionInputBean.getStatus());
                } else {
                    institutionStringBuilder.append("--");
                }

            }
        } catch (Exception e) {
            throw e;
        }
        return institutionStringBuilder.toString();
    }

    public long getCount(InstitutionInputBean institutionInputBean) throws Exception {
        long count = 0;

        try{
            count = institutionRepository.getCount(institutionInputBean);
        } catch (Exception ere) {
            throw ere;
        }

        return count;
    }

    public List<Institution> getInstitutionSearchResultList(InstitutionInputBean institutionInputBean) {
        List<Institution> institutionList;

        try{
            institutionList = institutionRepository.getInstitutionSearchList(institutionInputBean);
        }catch (Exception exception){
            throw  exception;
        }

        return institutionList;
    }

    public String insertInstitution(InstitutionInputBean institutionInputBean, Locale locale){
        String message = "";

        try {
            Institution existingInstitution = institutionRepository.getInstitution(institutionInputBean.getInstitutionCode().trim());

            if (existingInstitution == null){

                //set the other values to input bean
                Date currentDate = commonRepository.getCurrentDate();
                String lastUpdatedUser = sessionBean.getUsername();

                institutionInputBean.setCreatedTime(currentDate);
                institutionInputBean.setLastUpdatedUser("error");
                institutionInputBean.setLastUpdatedTime(currentDate);

                message = institutionRepository.insertInstitution(institutionInputBean);
            }else{
                message = MessageVarList.INSTITUTION_MGT_ALREADY_EXISTS;
            }
        }catch (DuplicateKeyException ex){
            message = MessageVarList.INSTITUTION_MGT_ALREADY_EXISTS;
        }catch (Exception exception){
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }

        return message;
    }

    public Institution getInstitution(String institutionCode) throws Exception {
        Institution institution;
        try {
            institution = institutionRepository.getInstitution(institutionCode);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return institution;
    }
}
