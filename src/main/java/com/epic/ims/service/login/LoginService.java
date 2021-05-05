package com.epic.ims.service.login;

import com.epic.ims.bean.login.LoginBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.user.User;
import com.epic.ims.repository.login.LoginRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.security.SHA256Algorithm;
import com.epic.ims.util.varlist.CommonVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Service
@Scope("prototype")
public class LoginService {

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

    public String getUser(LoginBean loginBean, HttpServletRequest httpServletRequest) {
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

            }
        } catch (EmptyResultDataAccessException ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        return message;
    }
}
