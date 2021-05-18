package com.epic.ims.controller.plateassign;

import com.epic.ims.annotation.accesscontrol.AccessControl;
import com.epic.ims.annotation.logcontroller.LogController;
import com.epic.ims.bean.plate.PlateBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.service.plateassign.PlateAssignService;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
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
import java.util.List;
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
        logger.info("[" + sessionBean.getSessionid() + "]  PLATE ASSIGN PAGE VIEW");
        ModelAndView modelAndView = null;
        try {
            return new ModelAndView("plate/assign", "plate", new PlateBean());
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            //set the error message to model map
            modelMap.put("msg", messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
            modelAndView = new ModelAndView("plate/assign", modelMap);
        }
        return modelAndView;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/generateDefaultPlate", method = RequestMethod.POST)
    public @ResponseBody
    Map<Integer, List<String>> getDefaultPlate(@RequestParam("receivedDate") String receivedDate, ModelMap modelMap, Locale locale) {
        Map<Integer, List<String>> defaultPlateMap = new HashMap<>();
        try {
            defaultPlateMap = plateAssignService.getDefaultPlate(receivedDate);
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return defaultPlateMap;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/swapBlockPlate", method = RequestMethod.POST)
    public @ResponseBody
    String postSwapBlockPlate(@RequestParam("plateArray") Map<Integer, List<String>> plateArray, @RequestParam("swapArray") Map<Integer, String> swapArray, Locale locale) {
        String message = "";
        try {
            message = plateAssignService.swapBlockPlate(plateArray, swapArray);
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            message = messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale);
        }
        return message;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/mergeBlockPlate", method = RequestMethod.POST)
    public @ResponseBody
    String postMergeBlockPlate(@RequestParam("plateArray") Map<Integer, List<String>> plateArray, @RequestParam("mergeArray") Map<Integer, List<String>> mergeArray, Locale locale) {
        String message = "";
        try {
            message = plateAssignService.MergeBlockPlate(plateArray, mergeArray);
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            message = messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale);
        }
        return message;
    }
}
