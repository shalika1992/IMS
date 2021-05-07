package com.epic.ims.controller.systemuser;

import com.epic.ims.bean.common.Status;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.bean.usermgt.sysuser.SystemUserInputBean;
import com.epic.ims.mapping.user.usermgt.SystemUser;
import com.epic.ims.mapping.user.usermgt.UserRole;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.service.sysuser.SystemUserService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.common.DataTablesResponse;
import com.epic.ims.util.common.ResponseBean;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import com.epic.ims.util.varlist.StatusVarList;
import com.epic.ims.validation.RequestBeanValidation;
import com.epic.ims.validation.sysuser.SystemUserValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@Scope("request")
public class SystemUserController implements RequestBeanValidation<Object> {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    MessageSource messageSource;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    SystemUserService systemUserService;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    Common common;

    @Autowired
    SystemUserValidator systemUserValidator;

    @GetMapping(value = "/viewSystemUser")
    public ModelAndView viewSysUserPage(ModelMap modelMap, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "]  SYSTEM USER PAGE VIEW");

        ModelAndView modelAndView;

        try {
            modelAndView = new ModelAndView("systemuserview", "beanmap", new ModelMap());
        } catch (Exception exception) {
            logger.error(exception);
            //set the error message to model map
            modelMap.put("msg", messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
            modelAndView = new ModelAndView("systemuserview", modelMap);
        }

        return modelAndView;

    }

    @PostMapping(value = "/addSystemUser", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseBean addSystemUser(@ModelAttribute("systemuser") SystemUserInputBean systemUserInputBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "]  SYSTEM USER ADD");
        ResponseBean responseBean = null;
        try {
            BindingResult bindingResult = validateRequestBean(systemUserInputBean);
            if (bindingResult.hasErrors()) {
                responseBean = new ResponseBean(false, null, messageSource.getMessage(bindingResult.getAllErrors().get(0).getCode(), new Object[]{bindingResult.getAllErrors().get(0).getDefaultMessage()}, Locale.US));
            } else {
                String message = systemUserService.insertSystemUser(systemUserInputBean, locale);
                if (message.isEmpty()) {
                    responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.SYSTEMUSER_MGT_ADDED_SUCCESSFULLY, null, locale), null);
                } else {
                    responseBean = new ResponseBean(false, null, messageSource.getMessage(message, null, locale));
                }
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            responseBean = new ResponseBean(false, null, messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
        }
        return responseBean;
    }

    @PostMapping(value = "/listSystemUser", headers = {"content-type=application/json"})
    public @ResponseBody
    DataTablesResponse<SystemUser> searchSystemUser(@RequestBody SystemUserInputBean systemUserInputBean) {
        logger.info("[" + sessionBean.getSessionid() + "]  SYSTEM USER SEARCH");
        DataTablesResponse<SystemUser> responseBean = new DataTablesResponse<>();

        try {
            long count = systemUserService.getCount(systemUserInputBean);

            if (count > 0) {
                List<SystemUser> systemUserList = systemUserService.getSystemUserSearchResultList(systemUserInputBean);
                //set data set to response bean
                responseBean.data.addAll(systemUserList);
            } else {
                //set data set to response bean
                responseBean.data.addAll(new ArrayList<>());
            }
            responseBean.echo = systemUserInputBean.echo;
            responseBean.columns = systemUserInputBean.columns;
            responseBean.totalRecords = count;
            responseBean.totalDisplayRecords = count;


        } catch (Exception exception) {
            logger.error("Exception " + exception);
        }

        return responseBean;

    }

    @GetMapping(value = "/getSystemUser")
    public @ResponseBody
    SystemUser getSystemUser(@RequestParam String userName) {
        logger.info("[" + sessionBean.getSessionid() + "]  GET SYSTEM USER");
        SystemUser systemUser = new SystemUser();
        try {
            if (userName != null && !userName.isEmpty()) {
                systemUser = systemUserService.getSystemUser(userName);
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return systemUser;
    }

    @PostMapping(value = "/updateSystemUser", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseBean updateSystemUser(@ModelAttribute("systemuser") SystemUserInputBean systemUserInputBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] UPDATE SYSTEM USER");
        ResponseBean responseBean;
        try {
            BindingResult bindingResult = validateRequestBean(systemUserInputBean);
            if (bindingResult.hasErrors()) {
                responseBean = new ResponseBean(false, null, messageSource.getMessage(bindingResult.getAllErrors().get(0).getCode(), new Object[]{bindingResult.getAllErrors().get(0).getDefaultMessage()}, Locale.US));
            } else {
                String message = systemUserService.updateSystemUser(systemUserInputBean, locale);
                if (message.isEmpty()) {
                    responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.SYSTEMUSER_MGT_UPDATE_SUCCESSFULLY, null, locale), null);
                } else {
                    responseBean = new ResponseBean(false, null, messageSource.getMessage(message, null, locale));
                }
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            responseBean = new ResponseBean(false, null, messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
        }
        return responseBean;
    }

    @ModelAttribute
    public void getSystemUserBean(Model map) throws Exception {
        SystemUserInputBean systemUserInputBean = new SystemUserInputBean();
        //get status list
        List<Status> statusActList = common.getActiveStatusList();
        List<UserRole> userRoleList = commonRepository.getUserRoleList();
        List<Status> statusList = commonRepository.getStatusList(StatusVarList.STATUS_CATEGORY_USER);
        //set values to task bean
        systemUserInputBean.setStatusList(statusList);
        systemUserInputBean.setStatusActList(statusActList);
        systemUserInputBean.setUserRoleList(userRoleList);

        //add values to model map
        map.addAttribute("systemuser", systemUserInputBean);
    }

    @Override
    public BindingResult validateRequestBean(Object object) {
        DataBinder dataBinder = new DataBinder(object);
        dataBinder.setValidator(systemUserValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }


}
