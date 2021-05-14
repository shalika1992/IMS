package com.epic.ims.controller.plateassign;

import com.epic.ims.annotation.accesscontrol.AccessControl;
import com.epic.ims.annotation.logcontroller.LogController;
import com.epic.ims.bean.plate.PlateBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.service.plateassign.PlateAssignService;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.PageVarList;
import com.epic.ims.util.varlist.SectionVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
@Scope("request")
public class PlateAssignController {
    private static Logger logger = LogManager.getLogger(PlateAssignController.class);

    @Autowired
    SessionBean sessionBean;

    @Autowired
    MessageSource messageSource;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    PlateAssignService plateAssignService;

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/viewPlateAssign", method = RequestMethod.GET)
    public ModelAndView getPlateAssignPage(ModelMap modelMap, Locale locale) {
        return new ModelAndView("plate/assign", "plate", new PlateBean());
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/generateDefaultPlate", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> getDefaultPlate(@RequestParam("receivedDate") String receivedDate, ModelMap modelMap, Locale locale) {
        Map<String, String> defaultPlateMap = new HashMap<>();
        try {
            plateAssignService.getDefaultPlate(receivedDate);
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return defaultPlateMap;
    }
}
