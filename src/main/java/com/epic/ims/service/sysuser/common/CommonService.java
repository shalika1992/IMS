package com.epic.ims.service.sysuser.common;

import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.varlist.CommonVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@Scope("prototype")
public class CommonService {

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    CommonVarList commonVarList;

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
