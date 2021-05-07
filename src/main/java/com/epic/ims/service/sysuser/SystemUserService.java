package com.epic.ims.service.sysuser;

import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.bean.usermgt.sysuser.SystemUserInputBean;
import com.epic.ims.mapping.user.usermgt.SystemUser;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.usermgt.SystemUserRepository;
import com.epic.ims.util.security.SHA256Algorithm;
import com.epic.ims.util.varlist.MessageVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Scope("prototype")
public class SystemUserService {

    @Autowired
    SystemUserRepository systemUserRepository;

    @Autowired
    SHA256Algorithm sha256Algorithm;

    @Autowired
    MessageSource messageSource;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    CommonRepository commonRepository;

    public long getCount(SystemUserInputBean systemUserInputBean) throws Exception {
        long count = 0;

        try{
            count = systemUserRepository.getCount(systemUserInputBean);
        } catch (Exception ere) {
            throw ere;
        }

        return count;
    }

    public List<SystemUser> getSystemUserSearchResultList(SystemUserInputBean systemUserInputBean) {
        List<SystemUser> systemUserList;

        try{
            systemUserList = systemUserRepository.getSystemUserSearchList(systemUserInputBean);
        }catch (Exception exception){
            throw  exception;
        }

        return systemUserList;
    }

    public String insertSystemUser(SystemUserInputBean systemUserInputBean, Locale locale){
        String message = "";

        try {
            SystemUser existingUser = systemUserRepository.getSystemUser(systemUserInputBean.getUserName().trim());

            if (existingUser == null){

                //set the other values to input bean
                Date currentDate = commonRepository.getCurrentDate();
                String lastUpdatedUser = sessionBean.getUsername();
                String password = systemUserInputBean.getPassword();

                systemUserInputBean.setCreatedTime(currentDate);
                systemUserInputBean.setLastUpdatedUser("error");
                systemUserInputBean.setLastUpdatedTime(currentDate);
                systemUserInputBean.setPassword(sha256Algorithm.makeHash(password));

                message = systemUserRepository.insertSystemUser(systemUserInputBean);
            }else{
                message = MessageVarList.SYSTEMUSER_MGT_ALREADY_EXISTS;
            }
        }catch (DuplicateKeyException ex){
            message = MessageVarList.SYSTEMUSER_MGT_ALREADY_EXISTS;
        }catch (Exception exception){
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }

        return message;
    }

    public String deleteSystemUser(String userName) {
        return null;
    }
}
