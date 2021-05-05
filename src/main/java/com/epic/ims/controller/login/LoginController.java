package com.epic.ims.controller.login;

import com.epic.ims.bean.login.LoginBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.util.varlist.MessageVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Map;

@Controller
@Scope("request")
public class LoginController {
    @Autowired
    SessionBean sessionBean;

    @Autowired
    MessageSource messageSource;

    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLogin(@RequestParam(value = "error", required = false) Integer error, ModelMap modelMap, Locale locale) {
        return new ModelAndView("login/login", "loginform", new LoginBean());
    }

    @RequestMapping(value = "/checkuser", method = RequestMethod.GET)
    public ModelAndView getUserLogin(ModelMap modelMap, Locale locale) {
        return new ModelAndView("login/login", "loginform", new LoginBean());
    }

    @RequestMapping(value = "/logout")
    public ModelAndView getLogout(@RequestParam(value = "error", required = false) Integer error, ModelMap modelMap, HttpSession httpSession, Locale locale) {
        ModelAndView modelAndView = null;
        try {
            modelAndView = new ModelAndView("login/login", "loginform", new LoginBean());
            if (error != null) {
                if (error == 1) {
                    String userName = sessionBean.getUsername();
                    //remove the username from session map
                    Map<String, String> sessionMap = (Map<String, String>) servletContext.getAttribute("sessionMap");
                    sessionMap.remove(userName);
                    //set the session bean to null
                    this.cleanUp();
                    this.sessionBean = null;
                    //set the message to model and view
                    modelAndView.addObject("msg", messageSource.getMessage(MessageVarList.LOGIN_STATUSTIMEOUT, null, locale));

                } else if (error == 2) {
                    String userName = sessionBean.getUsername();
                    //remove the username from session map
                    Map<String, String> sessionMap = (Map<String, String>) servletContext.getAttribute("sessionMap");
                    sessionMap.remove(userName);
                    //set the session bean to null
                    this.cleanUp();
                    this.sessionBean = null;
                    //set the message to model and view
                    modelAndView.addObject("msg", messageSource.getMessage(MessageVarList.USER_NEWPASSWORD_RESET_ERROR, null, locale));

                } else if (error == 3) {
                    String userName = sessionBean.getUsername();
                    //remove the username from session map
                    Map<String, String> sessionMap = (Map<String, String>) servletContext.getAttribute("sessionMap");
                    sessionMap.remove(userName);
                    //set the session bean to null
                    this.cleanUp();
                    this.sessionBean = null;
                    //set the message to model and view
                    modelAndView.addObject("msg", messageSource.getMessage(MessageVarList.USER_SESSION_NOTFOUND, null, locale));

                } else if (error == 4) {
                    String userName = sessionBean.getUsername();
                    //remove the username from session map
                    Map<String, String> sessionMap = (Map<String, String>) servletContext.getAttribute("sessionMap");
                    sessionMap.remove(userName);
                    //set the session bean to null
                    this.cleanUp();
                    this.sessionBean = null;
                    //set the message to model and view
                    modelAndView.addObject("msg", messageSource.getMessage(MessageVarList.USER_SESSION_INVALID, null, locale));

                } else if (error == 5) {
                    String userName = sessionBean.getUsername();
                    //remove the username from session map
                    Map<String, String> sessionMap = (Map<String, String>) servletContext.getAttribute("sessionMap");
                    sessionMap.remove(userName);
                    //set the session bean to null
                    this.cleanUp();
                    this.sessionBean = null;
                    //set the message to model and view
                    modelAndView.addObject("msg", messageSource.getMessage(MessageVarList.USER_REQUESTED_PASSWORDCHANGE, null, locale));

                } else if (error == 6) {
                    String userName = sessionBean.getUsername();
                    //remove the username from session map
                    Map<String, String> sessionMap = (Map<String, String>) servletContext.getAttribute("sessionMap");
                    sessionMap.remove(userName);
                    //set the session bean to null
                    this.cleanUp();
                    this.sessionBean = null;
                    //set the message to model and view
                    modelAndView.addObject("msg", messageSource.getMessage(MessageVarList.USER_PRIVILEGE_INSUFFICIENT, null, locale));

                } else {
                    String userName = sessionBean.getUsername();
                    //remove the username from session map
                    Map<String, String> sessionMap = (Map<String, String>) servletContext.getAttribute("sessionMap");
                    sessionMap.remove(userName);
                    //set the session bean to null
                    this.cleanUp();
                    this.sessionBean = null;
                }
            } else {
                String userName = sessionBean.getUsername();
                //remove the username from session map
                Map<String, String> sessionMap = (Map<String, String>) servletContext.getAttribute("sessionMap");
                sessionMap.remove(userName);
                //set the session bean to null
                this.cleanUp();
                this.sessionBean = null;
            }
        } catch (Exception e) {
            //set the session bean to null
            this.cleanUp();
            this.sessionBean = null;
        }
        //set the http session to invalidate
        httpSession.invalidate();
        return modelAndView;
    }

    @ModelAttribute
    public void getLoginbean(Model map) throws Exception {
        map.addAttribute("loginform", new LoginBean());
    }

    public void cleanUp() {
        sessionBean.setSessionid(null);
        sessionBean.setUsername(null);
        sessionBean.setUser(null);
        sessionBean.setChangePwdMode(false);
        sessionBean.setDaysToExpire(0);
        sessionBean.setSectionList(null);
        sessionBean.setPageMap(null);
        sessionBean.setPasswordPolicy(null);
    }
}
