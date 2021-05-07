package com.epic.ims.service.passwordpolicy;

import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.passwordpolicy.PasswordPolicy;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.passwordpolicy.PasswordPolicyRepository;
import com.epic.ims.util.common.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PasswordPolicyService {

    @Autowired
    SessionBean sessionBean;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    Common common;

    @Autowired
    MessageSource messageSource;

    @Autowired
    PasswordPolicyRepository passwordPolicyRepository;

    public PasswordPolicy getWebPasswordPolicy(int passwordPolicyCode) {
        PasswordPolicy passwordPolicy = null;
        try {
            passwordPolicy = passwordPolicyRepository.getWebPasswordPolicy(passwordPolicyCode);
            //set password policy to session bean
            sessionBean.setPasswordPolicy(passwordPolicy);
        } catch (Exception e) {
            throw e;
        }
        return passwordPolicy;
    }
}
