package com.epic.ims.service.login;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.login.LoginBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.user.usermgt.Page;
import com.epic.ims.mapping.user.usermgt.Section;
import com.epic.ims.mapping.user.usermgt.User;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.login.LoginRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.security.SHA256Algorithm;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope("prototype")
public class LoginService {
    private static Logger logger = LogManager.getLogger(LoginService.class);

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    Common common;

    @Autowired
    SHA256Algorithm sha256Algorithm;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    ServletContext servletContext;

    @LogService
    public String getUser(LoginBean loginBean, HttpServletRequest httpServletRequest) throws Exception {
        String message = "";
        try {
            //get hash 256 password
            String hashPassword = sha256Algorithm.makeHash(loginBean.getPassword());
            if (loginBean.getUsername().equals(commonVarList.SYSTEMUSERNAME) && hashPassword.equals(commonVarList.SYSTEMUSERPWD)) {
                User user = new User();
                user.setUserName(commonVarList.SYSTEMUSERNAME);
                user.setPassword(commonVarList.SYSTEMUSERPWD);
                //set the user data to session bean
                sessionBean.setUser(user);
            } else {
                User user = loginRepository.getUser(loginBean);
                //check user validation from database
                if (user == null) {
                    message = MessageVarList.LOGIN_INVALID;

                } else if (!hashPassword.equals(user.getPassword())) {
                    int noOfAttempts = user.getNoOfInvlidAttempt() == null ? 1 : user.getNoOfInvlidAttempt() + 1;
                    //check user no of attempts
                    if (user.getNoOfInvlidAttempt() >= commonRepository.getPasswordParam(commonVarList.PARAMCODE_NOOF_INVALIDLOGINATTEMPTS)) {
                        loginBean.setAttempts(noOfAttempts);
                        loginBean.setStatusCode(commonVarList.STATUS_DEACTIVE);
                        //set message
                        message = MessageVarList.LOGIN_DEACTIVE;
                    } else {
                        loginBean.setAttempts(noOfAttempts);
                        loginBean.setStatusCode(user.getStatus());
                        //set message
                        message = MessageVarList.LOGIN_INVALID;
                    }
                    //update the user
                    loginRepository.updateUser(loginBean, false);
                } else if (user.getStatus().equals(commonVarList.STATUS_DEACTIVE)) {
                    message = MessageVarList.LOGIN_DEACTIVE;
                } else if (this.checkUserRoleDeactive(user.getUserRole())) {
                    message = MessageVarList.LOGIN_DEACTIVE;
                } else if (this.checkUserIncative(user)) {
                    //set message
                    message = MessageVarList.LOGIN_IDLEDEACTIVE;
                    //update the user
                    loginBean.setStatusCode(commonVarList.STATUS_DEACTIVE);
                    loginRepository.updateUser(loginBean, false);
                } else {
                    if (user.getStatus().equalsIgnoreCase(commonVarList.STATUS_NEW) || user.getStatus().equalsIgnoreCase(commonVarList.STATUS_RESET) || user.getStatus().equalsIgnoreCase(commonVarList.STATUS_CHANGED) || user.getStatus().equalsIgnoreCase(commonVarList.STATUS_EXPIRED)) {
                        sessionBean.setChangePwdMode(true);
                    }

                    if (user.getUserRole().equals(commonVarList.USERROLE_CODE_MEDICALOFFICER)) {
                        user.setUserRoleDescription(commonVarList.USERROLE_DESCRIPTION_MEDICALOFFICER);

                    } else if (user.getUserRole().equals(commonVarList.USERROLE_CODE_COUNTEROFFICER)) {
                        user.setUserRoleDescription(commonVarList.USERROLE_DESCRIPTION_COUNTEROFFICER);
                    }
                    if (user.getUserRole().equals(commonVarList.USERROLE_CODE_ADMIN)) {
                        user.setUserRoleDescription(commonVarList.USERROLE_DESCRIPTION_ADMIN);
                    }

                    loginBean.setAttempts(new Byte("0"));
                    loginBean.setStatusCode(user.getStatus());
                    loginRepository.updateUser(loginBean, true);
                    //set the user data to session bean
                    HttpSession httpSession = httpServletRequest.getSession(true);
                    sessionBean.setSessionid(httpSession.getId());
                    sessionBean.setUsername(loginBean.getUsername());
                    sessionBean.setUser(user);

                    //get the session map
                    Map<String, String> sessionMap = (Map<String, String>) servletContext.getAttribute("sessionMap");
                    if (sessionMap == null) {
                        // Create a sessionMap instance.
                        sessionMap = new HashMap<>();
                    }
                    sessionMap.put(loginBean.getUsername(), httpSession.getId());
                    servletContext.setAttribute("sessionMap", sessionMap);
                }
            }
        } catch (EmptyResultDataAccessException ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        return message;
    }

    @LogService
    public List<Section> getUserSectionListByUserRoleCode(String userRoleCode) {
        List<Section> userRoleSectionList = null;
        try {
            userRoleSectionList = loginRepository.getUserSectionListByUserRoleCode(userRoleCode);
        } catch (Exception e) {
            throw e;
        }
        return userRoleSectionList;
    }

    @LogService
    public Map<String, List<Page>> getUserPageListByByUserRoleCode(String userRoleCode) {
        Map<String, List<Page>> userRolePageList = null;
        try {
            userRolePageList = loginRepository.getUserPageListByUserRoleCode(userRoleCode);
        } catch (Exception e) {
            throw e;
        }
        return userRolePageList;
    }

    @LogService
    public int getPwdExpNotification() throws Exception {
        int daysToExpire = 0;
        try {
            Date currentDate = commonRepository.getCurrentDate();
            int notificationPeriod = commonRepository.getPasswordParam(commonVarList.PARAMCODE_PASSWORDEXPIRY_NOTIFICATIONPERIOD);
            //calculate the max inactive time
            long maxInactiveTime = notificationPeriod * commonVarList.TIMESTAMP_VALUE_PERDAY;
            //get the user from the session beans
            User user = sessionBean.getUser();
            if (currentDate != null && user.getExpiryDate() != null && notificationPeriod > 0) {
                long currentInactiveTime = user.getExpiryDate().getTime() - currentDate.getTime();
                if (maxInactiveTime >= currentInactiveTime) {
                    long diffInSeconds = currentInactiveTime / 1000 % 60;
                    long diffInMinutes = currentInactiveTime / (60 * 1000) % 60;
                    long diffInHours = currentInactiveTime / (60 * 60 * 1000) % 24;
                    long diffInDays = currentInactiveTime / (24 * 60 * 60 * 1000);

                    if (diffInDays > 0) {
                        daysToExpire = Long.valueOf(diffInDays).intValue();
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return daysToExpire;
    }

    @LogService
    private boolean checkUserRoleDeactive(String userrole) throws Exception {
        boolean isUserRoleDeactive = false;
        try {
            String userRoleStatusCode = commonRepository.getUserRoleStatusCode(userrole);
            if (commonVarList.STATUS_DEACTIVE.equals(userRoleStatusCode)) {
                isUserRoleDeactive = true;
            }
        } catch (Exception e) {
            throw e;
        }
        return isUserRoleDeactive;
    }

    @LogService
    private boolean checkUserIncative(User user) throws Exception {
        boolean isUserInactive = false;
        try {
            Date currentDate = commonRepository.getCurrentDate();
            int minInactiveDays = commonRepository.getPasswordParam(commonVarList.PARAMCODE_IDLEACCOUNT_EXPIRYPERIOD);
            //calculate the max inactive time
            long maxInactiveTime = minInactiveDays * commonVarList.TIMESTAMP_VALUE_PERDAY;
            //check the user inactive times
            if (currentDate != null && user.getLoggedDate() != null && minInactiveDays > 0) {
                long currnetIncativeTime = currentDate.getTime() - user.getLoggedDate().getTime();
                if (currnetIncativeTime >= maxInactiveTime) {
                    isUserInactive = true;
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return isUserInactive;
    }
}
