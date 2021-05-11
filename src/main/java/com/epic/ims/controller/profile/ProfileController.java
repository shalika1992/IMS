package com.epic.ims.controller.profile;

import com.epic.ims.annotation.logcontroller.LogController;
import com.epic.ims.bean.profile.PasswordChangeBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.service.profile.ProfileService;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import com.epic.ims.validation.RequestBeanValidation;
import com.epic.ims.validation.profile.ChangePasswordValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
@Scope("request")
public class ProfileController implements RequestBeanValidation<Object> {
    private static Logger logger = LogManager.getLogger(ProfileController.class);

    @Autowired
    SessionBean sessionBean;

    @Autowired
    MessageSource messageSource;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    ChangePasswordValidator changePasswordValidator;

    @Autowired
    ProfileService profileService;

    @LogController
    @RequestMapping(value = "/passwordchange", method = RequestMethod.GET)
    public ModelAndView getPasswordChange(ModelMap modelMap, Locale locale) {
        return new ModelAndView("profile/changepassword", "passwordchangeform", new PasswordChangeBean());
    }

    @LogController
    @RequestMapping(value = "/passwordchange", method = RequestMethod.POST)
    public ModelAndView postPasswordChange(@ModelAttribute("passwordchangeform") PasswordChangeBean passwordChangeBean, ModelMap modelMap, HttpServletRequest httpServletRequest, Locale locale) {
        ModelAndView modelAndView;
        try {
            BindingResult bindingResult = validateRequestBean(passwordChangeBean);
            if (bindingResult.hasErrors()) {
                String errorMsg = bindingResult.getFieldErrors().stream().findFirst().get().getDefaultMessage();
                //set the error message to model map
                modelMap.put("msg", errorMsg);
                modelAndView = new ModelAndView("profile/changepassword", modelMap);
            } else {
                String message = profileService.changePassword(passwordChangeBean);
                if (message.isEmpty()) {
                    modelAndView = new ModelAndView("redirect:/logout.htm?error=2", modelMap);
                } else {
                    modelMap.put("msg", message);
                    modelAndView = new ModelAndView("profile/changepassword", modelMap);
                }
            }
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Exception  :  ", ex);
            //set the error message to model map
            modelMap.put("msg", messageSource.getMessage(MessageVarList.COMMON_ERROR_RECORD_DOESNOT_EXISTS, null, locale));
            modelAndView = new ModelAndView("profile/changepassword", modelMap);
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            //set the error message to model map
            modelMap.put("msg", messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
            modelAndView = new ModelAndView("profile/changepassword", modelMap);
        }
        return modelAndView;
    }

    @ModelAttribute
    public void getPasswordChange(Model map) throws Exception {
        map.addAttribute("passwordchangeform", new PasswordChangeBean());
    }

    @Override
    public BindingResult validateRequestBean(Object o) {
        DataBinder dataBinder = new DataBinder(o);
        dataBinder.setValidator(changePasswordValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }
}
