package com.epic.ims.controller.plateassign;

import com.epic.ims.bean.login.LoginBean;
import com.epic.ims.bean.plate.PlateBean;
import com.epic.ims.bean.session.SessionBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
@Scope("request")
public class PlateAssignController {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    SessionBean sessionBean;


    @RequestMapping(value = "/viewPlateAssign", method = RequestMethod.GET)
    public ModelAndView getPlateAssignPage(@RequestParam(value = "error", required = false) Integer error, ModelMap modelMap, Locale locale) {
        return new ModelAndView("plate/assign", "plate", new PlateBean());
    }


}
