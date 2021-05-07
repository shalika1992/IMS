package com.epic.ims.controller.login;

import com.epic.ims.bean.login.LoginBean;
import com.epic.ims.bean.profile.PasswordChangeBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.user.usermgt.Page;
import com.epic.ims.mapping.user.usermgt.Section;
import com.epic.ims.mapping.user.usermgt.User;
import com.epic.ims.service.login.LoginService;
import com.epic.ims.service.passwordpolicy.PasswordPolicyService;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import com.epic.ims.validation.RequestBeanValidation;
import com.epic.ims.validation.login.LoginValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@Scope("request")
public class LoginController implements RequestBeanValidation<Object> {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    SessionBean sessionBean;

    @Autowired
    MessageSource messageSource;

    @Autowired
    ServletContext servletContext;

    @Autowired
    LoginValidator loginValidator;

    @Autowired
    LoginService loginService;

    @Autowired
    PasswordPolicyService passwordPolicyService;

    @Autowired
    CommonVarList commonVarList;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLogin(@RequestParam(value = "error", required = false) Integer error, ModelMap modelMap, Locale locale) {
        return new ModelAndView("login/login", "loginform", new LoginBean());
    }

    @RequestMapping(value = "/checkuser", method = RequestMethod.GET)
    public ModelAndView getUserLogin(ModelMap modelMap, Locale locale) {
        return new ModelAndView("login/login", "loginform", new LoginBean());
    }

    @RequestMapping(value = "/checkuser", method = RequestMethod.POST)
    public ModelAndView postCheckUser(@ModelAttribute("loginform") LoginBean loginBean, ModelMap modelMap, HttpServletRequest httpServletRequest, Locale locale) {
        ModelAndView modelAndView = null;
        try {
            BindingResult bindingResult = validateRequestBean(loginBean);
            if (bindingResult.hasErrors()) {
                String errorMsg = bindingResult.getFieldErrors().stream().findFirst().get().getDefaultMessage();
                //set the error message to model map
                modelMap.put("msg", errorMsg);
                modelAndView = new ModelAndView("login/login", modelMap);
            } else {
                String message = loginService.getUser(loginBean, httpServletRequest);
                //check the return message from service
                if (!message.isEmpty()) {
                    modelMap.put("msg", messageSource.getMessage(message, null, locale));
                    modelAndView = new ModelAndView("login/login", modelMap);
                } else {
                    if (loginBean.getUsername().equals(commonVarList.SYSTEMUSERNAME)) {
                        //handle the user session
                        //get user section list and page list and set to session bean
                        List<Section> sectionList = loginService.getUserSectionListByUserRoleCode(commonVarList.USERROLE_CODE_ADMIN);
                        Map<String, List<Page>> pageList = loginService.getUserPageListByByUserRoleCode(commonVarList.USERROLE_CODE_ADMIN);
                        sessionBean.setSectionList(sectionList);
                        sessionBean.setPageMap(pageList);
                        //redirect to home page
                        modelAndView = new ModelAndView("home/home", modelMap);
                    } else {
                        User user = sessionBean.getUser();
                        if (user.getStatus().equalsIgnoreCase(commonVarList.STATUS_NEW)) {
                            //set section list and page list and to session bean
                            sessionBean.setSectionList(new ArrayList<>());
                            sessionBean.setPageMap(new HashMap<>());
                            //redirect to change password page
                            modelMap.put("msg", messageSource.getMessage(MessageVarList.PASSWORDRESET_NEWUSER, null, locale));
                            modelAndView = new ModelAndView("profile/changepassword", "passwordchangeform", getPasswordPolicyBean());

                        } else if (user.getStatus().equalsIgnoreCase(commonVarList.STATUS_RESET)) {
                            //set section list and page list and to session bean
                            sessionBean.setSectionList(new ArrayList<>());
                            sessionBean.setPageMap(new HashMap<>());
                            //redirect to change password page
                            modelMap.put("msg", messageSource.getMessage(MessageVarList.PASSWORDRESET_RESETUSER, null, locale));
                            modelAndView = new ModelAndView("profile/changepassword", "passwordchangeform", getPasswordPolicyBean());

                        } else if (user.getStatus().equalsIgnoreCase(commonVarList.STATUS_CHANGED)) {
                            //set section list and page list and to session bean
                            sessionBean.setSectionList(new ArrayList<>());
                            sessionBean.setPageMap(new HashMap<>());
                            //redirect to change password page
                            modelMap.put("msg", messageSource.getMessage(MessageVarList.PASSWORDRESET_CHANGEPWD, null, locale));
                            modelAndView = new ModelAndView("profile/changepassword", "passwordchangeform", getPasswordPolicyBean());

                        } else if (user.getStatus().equalsIgnoreCase(commonVarList.STATUS_EXPIRED)) {
                            //set section list and page list and to session bean
                            sessionBean.setSectionList(new ArrayList<>());
                            sessionBean.setPageMap(new HashMap<>());
                            //redirect to change password page
                            modelMap.put("msg", messageSource.getMessage(MessageVarList.PASSWORDRESET_EXPPWD, null, locale));
                            modelAndView = new ModelAndView("profile/changepassword", "passwordchangeform", getPasswordPolicyBean());

                        } else {
                            int daysToExpire = loginService.getPwdExpNotification();
                            //handle the user session
                            //get user section list and page list and set to session bean
                            //get user page task list and set to session bean
                            List<Section> sectionList = loginService.getUserSectionListByUserRoleCode(user.getUserRole());
                            Map<String, List<Page>> pageList = loginService.getUserPageListByByUserRoleCode(user.getUserRole());
                            sessionBean.setSectionList(sectionList);
                            sessionBean.setPageMap(pageList);
                            sessionBean.setDaysToExpire(daysToExpire);
                            //redirect to home page
                            modelAndView = new ModelAndView("home/home", "beanmap", new ModelMap());
                        }
                    }
                }
            }
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Exception  :  ", ex);
            //set the error message to model map
            modelMap.put("msg", messageSource.getMessage(MessageVarList.LOGIN_INVALID, null, locale));
            modelAndView = new ModelAndView("login/login", modelMap);
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            //set the error message to model map
            modelMap.put("msg", messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
            modelAndView = new ModelAndView("login/login", modelMap);
        }
        return modelAndView;
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

    private PasswordChangeBean getPasswordPolicyBean() {
        PasswordChangeBean passwordChangeBean = null;
        try {
            passwordChangeBean = new PasswordChangeBean();
            //set the password policy to session bean
            passwordPolicyService.getWebPasswordPolicy(commonVarList.DEFAULT_PASSWORDPOLICY);
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return passwordChangeBean;
    }

    @Override
    public BindingResult validateRequestBean(Object o) {
        DataBinder dataBinder = new DataBinder(o);
        dataBinder.setValidator(loginValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }
}
