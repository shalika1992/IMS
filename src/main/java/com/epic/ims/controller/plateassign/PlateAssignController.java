package com.epic.ims.controller.plateassign;

import com.epic.ims.annotation.accesscontrol.AccessControl;
import com.epic.ims.annotation.logcontroller.LogController;
import com.epic.ims.bean.plate.PlateBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.util.varlist.PageVarList;
import com.epic.ims.util.varlist.SectionVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static Logger logger = LogManager.getLogger(PlateAssignController.class);

    @Autowired
    SessionBean sessionBean;

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/viewPlateAssign", method = RequestMethod.GET)
    public ModelAndView getPlateAssignPage(@RequestParam(value = "error", required = false) Integer error, ModelMap modelMap, Locale locale) {
        return new ModelAndView("plate/assign", "plate", new PlateBean());
    }
}
