package com.epic.ims.repository.profile;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.profile.PasswordChangeBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.user.usermgt.User;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.security.SHA256Algorithm;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
@Scope("prototype")
public class ProfileRepository {
    private static Logger logger = LogManager.getLogger(ProfileRepository.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    Common common;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    SHA256Algorithm sha256Algorithm;

    private final String SQL_UPDATE_SYSTEMUSER = "update web_systemuser set password=? , expirydate=? ,lastloggeddate=?, status=? , lastupdateduser=? , lastupdatedtime=? where username=?";

    @LogRepository
    @Transactional
    public String changeUserPassword(PasswordChangeBean passwordChangeBean) throws Exception {
        String message = "";
        try {
            User user = sessionBean.getUser();
            if (user != null) {
                //set the new values to user oject
                Date currentDate = commonRepository.getCurrentDate();
                user.setPassword(passwordChangeBean.getNewHashPassword());
                user.setExpiryDate(passwordChangeBean.getPasswordExpiryDate());
                user.setLoggedDate(currentDate);
                user.setStatus(commonVarList.STATUS_ACTIVE);
                user.setLastUpdatedUser(sessionBean.getUsername());
                user.setLastUpdatedTime(currentDate);
                //update the system user table
                int i = jdbcTemplate.update(SQL_UPDATE_SYSTEMUSER, new Object[]{user.getPassword(), user.getExpiryDate(), user.getLoggedDate(), user.getStatus(), user.getLastUpdatedUser(), user.getLastUpdatedTime(), user.getUserName()});
                if (i != 1) {
                    message = MessageVarList.USER_NEWPASSWORD_RESET_ERROR;
                }
            }
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return message;
    }
}
