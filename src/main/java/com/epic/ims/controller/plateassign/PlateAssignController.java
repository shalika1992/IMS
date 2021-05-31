package com.epic.ims.controller.plateassign;

import com.epic.ims.annotation.accesscontrol.AccessControl;
import com.epic.ims.annotation.logcontroller.LogController;
import com.epic.ims.bean.plate.DefaultBean;
import com.epic.ims.bean.plate.PlateInputBean;
import com.epic.ims.bean.plate.PoolBean;
import com.epic.ims.bean.plate.SwapBean;
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
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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
            modelAndView = new ModelAndView("plate/assign", "beanmap", new ModelMap());
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
    Map<Integer, List<DefaultBean>> getDefaultPlate(@RequestParam("receivedDate") String receivedDate, ModelMap modelMap, Locale locale) {
        Map<Integer, List<DefaultBean>> defaultPlateMap = new HashMap<>();
        try {
            defaultPlateMap = plateAssignService.getDefaultPlate(receivedDate);
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return defaultPlateMap;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/swapBlockPlate", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Map<Integer, List<DefaultBean>> postSwapBlockPlate(@RequestBody SwapBean swapBean, HttpServletRequest request, HttpServletResponse response, Locale locale) {
        Map<Integer, List<DefaultBean>> defaultPlateMap = new HashMap<>();
        try {
            defaultPlateMap = plateAssignService.swapBlockPlate(swapBean);
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return defaultPlateMap;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/mergeBlockPlate", method = RequestMethod.POST)
    public @ResponseBody
    Map<Integer, List<DefaultBean>> postMergeBlockPlate(@RequestBody PoolBean poolBean, HttpServletRequest request, HttpServletResponse response, Locale locale) {
        Map<Integer, List<DefaultBean>> defaultPlateMap = new HashMap<>();
        try {
            defaultPlateMap = plateAssignService.MergeBlockPlate(poolBean);
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return defaultPlateMap;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/createPlate", method = RequestMethod.POST)
    public void createPlate(@ModelAttribute("plate") PlateInputBean plateInputBean, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.info("[" + sessionBean.getSessionid() + "]  MASTER PLATE CREATION");
        OutputStream outputStream = null;
        try {
            List<String> fileList = plateAssignService.getFilePathList();
        } catch (Exception ex) {
            logger.error("Exception  :  ", ex);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException ex) {
            }
        }

    }

    @ModelAttribute
    public void getPlateBean(Model map) throws Exception {
        PlateInputBean plateInputBean = new PlateInputBean();
        //add values to model map
        map.addAttribute("plate", plateInputBean);
    }
}
