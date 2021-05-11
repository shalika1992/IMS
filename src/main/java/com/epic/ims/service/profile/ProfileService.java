package com.epic.ims.service.profile;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.profile.PasswordChangeBean;
import com.epic.ims.repository.profile.ProfileRepository;
import com.epic.ims.service.common.CommonService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.security.SHA256Algorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@Scope("prototype")
public class ProfileService {
    private static Logger logger = LogManager.getLogger(ProfileService.class);

    @Autowired
    SHA256Algorithm sha256Algorithm;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    Common common;

    @Autowired
    CommonService commonService;

    @Autowired
    ProfileRepository profileRepository;

    @LogService
    public String changePassword(PasswordChangeBean passwordChangeBean) throws Exception {
        String message;
        try {
            Date userPasswordExpiryDate = commonService.getPasswordExpiryDate();
            //create the hash password
            String oldHashPassword = sha256Algorithm.makeHash(passwordChangeBean.getOldPassword());
            String newHashPassword = sha256Algorithm.makeHash(passwordChangeBean.getNewPassword());
            passwordChangeBean.setOldHashPassword(oldHashPassword);
            passwordChangeBean.setNewHashPassword(newHashPassword);
            passwordChangeBean.setPasswordExpiryDate(userPasswordExpiryDate);
            //update the user password
            message = profileRepository.changeUserPassword(passwordChangeBean);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return message;
    }
}
