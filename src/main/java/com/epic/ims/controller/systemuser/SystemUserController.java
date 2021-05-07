package com.epic.ims.controller.systemuser;

import com.epic.ims.bean.common.Status;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.bean.usermgt.sysuser.SystemUserInputBean;
import com.epic.ims.mapping.user.UserRole;
import com.epic.ims.repository.CommonRepository;
import com.epic.ims.service.sysuser.SystemUserService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.StatusVarList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Scope("request")
public class SystemUserController {
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
    Common common;

    public ModelAndView viewSysUser(){
        return null;
    }

    @ModelAttribute
    public void getSystemUserBean(Model map) throws Exception {
        SystemUserInputBean systemUserInputBean = new SystemUserInputBean();
        //get status list
        List<Status> statusList = commonRepository.getStatusList(StatusVarList.STATUS_CATEGORY_USER);
        List<Status> statusActList = common.getActiveStatusList();
        List<UserRole> userRoleList = commonRepository.getUserRoleListByUserRoleTypeCode(commonVarList.USERROLE_TYPE_WEB);
        //set values to task bean
        systemUserInputBean.setStatusList(statusList);
        systemUserInputBean.setStatusActList(statusActList);
        systemUserInputBean.setUserRoleList(userRoleList);

        //add values to model map
        map.addAttribute("systemuser", systemUserInputBean);
    }



}
