package com.epic.ims.controller.home;

import com.epic.ims.annotation.logcontroller.LogController;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.util.varlist.MessageVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
@Scope("request")
public class HomeController {
    private static Logger logger = LogManager.getLogger(HomeController.class);

    @Autowired
    SessionBean sessionBean;

    @Autowired
    MessageSource messageSource;

    @LogController
    @RequestMapping(value = "/home", method = {RequestMethod.GET})
    public ModelAndView getHome(ModelMap modelMap, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "]  HOME PAGE VIEW");
        ModelAndView modelAndView = null;
        try {
            modelAndView = new ModelAndView("home/home", "homemap", new ModelMap());
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            //set the error message to model map
            modelMap.put("msg", messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
            modelAndView = new ModelAndView("home/home", modelMap);
        }
        return modelAndView;
    }
}
