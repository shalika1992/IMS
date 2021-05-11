package com.epic.ims.service.institutionmgt;

import com.epic.ims.bean.institutionmgt.InstitutinInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.bean.usermgt.sysuser.SystemUserInputBean;
import com.epic.ims.mapping.institutionmgt.Institution;
import com.epic.ims.mapping.user.usermgt.SystemUser;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.institutionmgt.InstitutionRepository;
import com.epic.ims.repository.usermgt.SystemUserRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.security.SHA256Algorithm;
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

    public long getCount(InstitutinInputBean institutinInputBean) throws Exception {
        long count = 0;

        try{
            count = institutionRepository.getCount(institutinInputBean);
        } catch (Exception ere) {
            throw ere;
        }

        return count;
    }

    public List<Institution> getInstitutionSearchResultList(InstitutinInputBean institutinInputBean) {
        List<Institution> institutionList;

        try{
            institutionList = institutionRepository.getInstitutionSearchList(institutinInputBean);
        }catch (Exception exception){
            throw  exception;
        }

        return institutionList;
    }

    public String insertInstitution(InstitutinInputBean institutinInputBean, Locale locale){
        String message = "";

        try {
            Institution existingInstitution = institutionRepository.getInstitution(institutinInputBean.getInstitutionCode().trim());

            if (existingInstitution == null){

                //set the other values to input bean
                Date currentDate = commonRepository.getCurrentDate();
                String lastUpdatedUser = sessionBean.getUsername();

                institutinInputBean.setCreatedTime(currentDate);
                institutinInputBean.setLastUpdatedUser("error");
                institutinInputBean.setLastUpdatedTime(currentDate);

                message = institutionRepository.insertInstitution(institutinInputBean);
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
