package com.epic.ims.service.sysuser;

import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.bean.usermgt.sysuser.SystemUserInputBean;
import com.epic.ims.mapping.user.usermgt.SystemUser;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.usermgt.SystemUserRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.security.SHA256Algorithm;
import com.epic.ims.util.varlist.MessageVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
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

    @Autowired
    Common common;

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

    public String updateSystemUser(SystemUserInputBean systemUserInputBean, Locale locale) {
        String message = "";
        SystemUser existingSystemUser = null;
        try {
            existingSystemUser = systemUserRepository.getSystemUser(systemUserInputBean.getUserName());
            if (existingSystemUser != null) {
                //check changed values
                String oldValueAsString = this.getSystemUserAsString(existingSystemUser, true);
                String newValueAsString = this.getSystemUserAsString(systemUserInputBean, true);
                //check the old value and new value
                if (oldValueAsString.equals(newValueAsString)) {
                    message = MessageVarList.COMMON_ERROR_NO_VALUE_CHANGE;
                } else {
                    //set the other values to input bean
                    Date currentDate = commonRepository.getCurrentDate();
                    String lastUpdatedUser = sessionBean.getUsername();

                    systemUserInputBean.setCreatedTime(currentDate);
                    systemUserInputBean.setLastUpdatedTime(currentDate);
                    systemUserInputBean.setLastUpdatedUser(lastUpdatedUser);

                    message = systemUserRepository.updateSystemUser(systemUserInputBean);

                }
            } else {
                message = MessageVarList.SYSTEMUSER_MGT_NORECORD_FOUND;
            }
        } catch (EmptyResultDataAccessException ere) {
            message = MessageVarList.SYSTEMUSER_MGT_NORECORD_FOUND;

        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    private String getSystemUserAsString(SystemUser systemUser, boolean checkChanges) {
        StringBuilder systemUserStringBuilder = new StringBuilder();
        try {
            if (systemUser != null) {

                if (systemUser.getUserName() != null && !systemUser.getUserName().isEmpty()) {
                    systemUserStringBuilder.append(systemUser.getUserName());
                } else {
                    systemUserStringBuilder.append("error");
                }

                systemUserStringBuilder.append("|");
                if (systemUser.getFullName() != null && !systemUser.getFullName().isEmpty()) {
                    systemUserStringBuilder.append(systemUser.getFullName());
                } else {
                    systemUserStringBuilder.append("--");
                }

                systemUserStringBuilder.append("|");
                if (systemUser.getEmail() != null && !systemUser.getEmail().isEmpty()) {
                    systemUserStringBuilder.append(systemUser.getEmail());
                } else {
                    systemUserStringBuilder.append("--");
                }

                systemUserStringBuilder.append("|");
                if (systemUser.getUserRoleCode() != null && !systemUser.getUserRoleCode().isEmpty()) {
                    systemUserStringBuilder.append(systemUser.getUserRoleCode());
                } else {
                    systemUserStringBuilder.append("--");
                }

                systemUserStringBuilder.append("|");
                if (systemUser.getStatus() != null && !systemUser.getStatus().isEmpty()) {
                    systemUserStringBuilder.append(systemUser.getStatus());
                } else {
                    systemUserStringBuilder.append("--");
                }

                systemUserStringBuilder.append("|");
                if (systemUser.getMobileNumber() != null && !systemUser.getMobileNumber().isEmpty()) {
                    systemUserStringBuilder.append(systemUser.getMobileNumber());
                } else {
                    systemUserStringBuilder.append("--");
                }

                if (!checkChanges) {
                    systemUserStringBuilder.append("|");
                    if (systemUser.getCreatedTime() != null) {
                        systemUserStringBuilder.append(common.formatDateToString(systemUser.getCreatedTime()));
                    } else {
                        systemUserStringBuilder.append("--");
                    }

                    systemUserStringBuilder.append("|");
                    if (systemUser.getLastUpdatedTime() != null) {
                        systemUserStringBuilder.append(common.formatDateToString(systemUser.getLastUpdatedTime()));
                    } else {
                        systemUserStringBuilder.append("--");
                    }

                    systemUserStringBuilder.append("|");
                    if (systemUser.getLastUpdatedUser() != null) {
                        systemUserStringBuilder.append(systemUser.getLastUpdatedUser());
                    } else {
                        systemUserStringBuilder.append("--");
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return systemUserStringBuilder.toString();
    }

    private String getSystemUserAsString(SystemUserInputBean systemUserInputBean, boolean checkChanges) {
        StringBuilder systemUserStringBuilder = new StringBuilder();
        try {
            if (systemUserInputBean != null) {

                if (systemUserInputBean.getUserName() != null && !systemUserInputBean.getUserName().isEmpty()) {
                    systemUserStringBuilder.append(systemUserInputBean.getUserName());
                } else {
                    systemUserStringBuilder.append("error");
                }

                systemUserStringBuilder.append("|");
                if (systemUserInputBean.getFullName() != null && !systemUserInputBean.getFullName().isEmpty()) {
                    systemUserStringBuilder.append(systemUserInputBean.getFullName());
                } else {
                    systemUserStringBuilder.append("--");
                }

                systemUserStringBuilder.append("|");
                if (systemUserInputBean.getEmail() != null && !systemUserInputBean.getEmail().isEmpty()) {
                    systemUserStringBuilder.append(systemUserInputBean.getEmail());
                } else {
                    systemUserStringBuilder.append("--");
                }

                systemUserStringBuilder.append("|");
                if (systemUserInputBean.getUserRoleCode() != null && !systemUserInputBean.getUserRoleCode().isEmpty()) {
                    systemUserStringBuilder.append(systemUserInputBean.getUserRoleCode());
                } else {
                    systemUserStringBuilder.append("--");
                }

                systemUserStringBuilder.append("|");
                if (systemUserInputBean.getStatus() != null && !systemUserInputBean.getStatus().isEmpty()) {
                    systemUserStringBuilder.append(systemUserInputBean.getStatus());
                } else {
                    systemUserStringBuilder.append("--");
                }

                systemUserStringBuilder.append("|");
                if (systemUserInputBean.getMobileNumber() != null && !systemUserInputBean.getMobileNumber().isEmpty()) {
                    systemUserStringBuilder.append(systemUserInputBean.getMobileNumber());
                } else {
                    systemUserStringBuilder.append("--");
                }

                if (!checkChanges) {
                    systemUserStringBuilder.append("|");
                    if (systemUserInputBean.getCreatedTime() != null) {
                        systemUserStringBuilder.append(common.formatDateToString(systemUserInputBean.getCreatedTime()));
                    } else {
                        systemUserStringBuilder.append("--");
                    }

                    systemUserStringBuilder.append("|");
                    if (systemUserInputBean.getLastUpdatedTime() != null) {
                        systemUserStringBuilder.append(common.formatDateToString(systemUserInputBean.getLastUpdatedTime()));
                    } else {
                        systemUserStringBuilder.append("--");
                    }

                    systemUserStringBuilder.append("|");
                    if (systemUserInputBean.getLastUpdatedUser() != null) {
                        systemUserStringBuilder.append(systemUserInputBean.getLastUpdatedUser());
                    } else {
                        systemUserStringBuilder.append("--");
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return systemUserStringBuilder.toString();
    }

    public SystemUser getSystemUser(String userName) throws Exception {
        SystemUser systemUser;
        try {
            systemUser = systemUserRepository.getSystemUser(userName);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return systemUser;
    }
}
