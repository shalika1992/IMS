package com.epic.ims.service.common;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.usermgt.SystemUserRepository;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@Scope("prototype")
public class CommonService {
    private static Logger logger = LogManager.getLogger(CommonService.class);

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    CommonVarList commonVarList;

    @LogService
    public Date getPasswordExpiryDate() throws Exception {
        Date newExpiryDate;
        try {
            Date currentTime = commonRepository.getCurrentDate();
            //get the current date calender instance
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentTime);
            //get the expiry period  - password parameter from password policy
            int daysToExpire = Integer.parseInt(commonVarList.PWDEXPIRYPERIOD);
            if (daysToExpire == 0) {
                daysToExpire = commonVarList.PWDPARAM_DEFAULT_PWDAGE;
            }
            calendar.add(Calendar.DATE, daysToExpire);
            newExpiryDate = calendar.getTime();
        } catch (Exception e) {
            throw e;
        }
        return newExpiryDate;
    }
}
