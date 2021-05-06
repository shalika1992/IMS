package com.epic.ims.controller.login;

import com.epic.ims.bean.login.LoginBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.service.login.LoginService;
import com.epic.ims.util.varlist.MessageVarList;
import com.epic.ims.validators.RequestBeanValidation;
import com.epic.ims.validators.login.LoginValidator;
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
import java.util.Locale;
import java.util.Map;

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

    @Override
    public BindingResult validateRequestBean(Object o) {
        DataBinder dataBinder = new DataBinder(o);
        dataBinder.setValidator(loginValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }
}
