package com.epic.ims.controller.resultupdate;

import com.epic.ims.annotation.accesscontrol.AccessControl;
import com.epic.ims.annotation.logcontroller.LogController;
import com.epic.ims.bean.resultupdate.ResultIdListBean;
import com.epic.ims.bean.resultupdate.ResultUpdateInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.controller.samplefileupload.SampleFileUploadController;
import com.epic.ims.mapping.institution.Institution;
import com.epic.ims.mapping.plate.Plate;
import com.epic.ims.mapping.result.Result;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.service.resultupdate.ResultUpdateService;
import com.epic.ims.util.common.DataTablesResponse;
import com.epic.ims.util.common.ResponseBean;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@Scope("request")
public class ResultUpdateController {
    private static Logger logger = LogManager.getLogger(SampleFileUploadController.class);

    @Autowired
    SessionBean sessionBean;

    @Autowired
    MessageSource messageSource;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    ResultUpdateService resultUpdateService;

    @Autowired
    CommonRepository commonRepository;

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.RESULT_UPDATE)
    @RequestMapping(value = "/viewResultUpdate", method = RequestMethod.GET)
    public ModelAndView getResultUpdatePage(ModelMap modelMap, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "]  RESULT UPDATE PAGE VIEW");
        ModelAndView modelAndView = null;
        try {
            modelAndView = new ModelAndView("resultupdateview", "beanmap", new ModelMap());
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            //set the error message to model map
            modelMap.put("msg", messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
            modelAndView = new ModelAndView("resultupdateview", modelMap);
        }
        return modelAndView;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.RESULT_UPDATE)
    @RequestMapping(value = "/getPlateList", method = RequestMethod.GET)
    public @ResponseBody
    List<Plate> getPlateList(@RequestParam("receivedDate") String receivedDate, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] GET PLATE LIST");
        List<Plate> plateList = new ArrayList<>();
        try {
            plateList = commonRepository.getPlateListList(receivedDate);
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return plateList;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.RESULT_UPDATE)
    @PostMapping(value = "/listResultUpdate", headers = {"content-type=application/json"})
    public @ResponseBody
    DataTablesResponse<Result> searchResultUpdate(@RequestBody ResultUpdateInputBean resultUpdateInputBean) {
        logger.info("[" + sessionBean.getSessionid() + "]  RESULT UPDATE SEARCH");
        DataTablesResponse<Result> responseBean = new DataTablesResponse<>();
        try {
            long count = resultUpdateService.getCount(resultUpdateInputBean);
            if (count > 0) {
                List<Result> list = resultUpdateService.getResultUpdateSearchResultList(resultUpdateInputBean);
                //set data set to response bean
                responseBean.data.addAll(list);
                responseBean.echo = resultUpdateInputBean.echo;
                responseBean.columns = resultUpdateInputBean.columns;
                responseBean.totalRecords = count;
                responseBean.totalDisplayRecords = count;
            } else {
                //set data set to response bean
                responseBean.data.addAll(new ArrayList<>());
                responseBean.echo = resultUpdateInputBean.echo;
                responseBean.columns = resultUpdateInputBean.columns;
                responseBean.totalRecords = count;
                responseBean.totalDisplayRecords = count;
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return responseBean;
    }

    @GetMapping(value = "/getRecordResultUpdate", headers = {"content-type=application/json"})
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.RESULT_UPDATE)
    public @ResponseBody
    Plate getRecordResultUpdate(@RequestParam String id) {
        logger.info("[" + sessionBean.getSessionid() + "]  RESULT UPDATE GET");
        Plate plate = new Plate();
        try {
            if (id != null && !id.isEmpty()) {
                plate = resultUpdateService.getRecord(id);
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return plate;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.RESULT_UPDATE)
    @PostMapping(value = "/updateDetectedList", consumes = "application/json")
    public @ResponseBody
    ResponseBean updateDetectedList(@RequestBody ResultIdListBean resultIdListBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] RESULT UPDATE DETECTED");
        ResponseBean responseBean;
        try {
            String message = resultUpdateService.markAsDetected(resultIdListBean);
            if (message.isEmpty()) {
                responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.SAMPLERECORD_UPDATE_SUCCESSFULLY, null, locale), null);
            } else {
                responseBean = new ResponseBean(false, null, messageSource.getMessage(message, null, locale));
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            responseBean = new ResponseBean(false, null, messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
        }
        return responseBean;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.RESULT_UPDATE)
    @PostMapping(value = "/updateNotDetectedList", consumes = "application/json")
    public @ResponseBody
    ResponseBean updateNotDetectedList(@RequestBody ResultIdListBean resultIdListBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] RESULT UPDATE NOT DETECTED");
        ResponseBean responseBean;
        try {
            String message = resultUpdateService.markAsNotDetected(resultIdListBean);
            if (message.isEmpty()) {
                responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.SAMPLERECORD_UPDATE_SUCCESSFULLY, null, locale), null);
            } else {
                responseBean = new ResponseBean(false, null, messageSource.getMessage(message, null, locale));
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            responseBean = new ResponseBean(false, null, messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
        }
        return responseBean;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.RESULT_UPDATE)
    @PostMapping(value = "/updateRepeatList", consumes = "application/json")
    public @ResponseBody
    ResponseBean updateResultList(@RequestBody ResultIdListBean resultIdListBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] RESULT UPDATE REPEATED");
        ResponseBean responseBean;
        try {
            String message = resultUpdateService.markAsRepeated(resultIdListBean);
            if (message.isEmpty()) {
                responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.SAMPLERECORD_UPDATE_SUCCESSFULLY, null, locale), null);
            } else {
                responseBean = new ResponseBean(false, null, messageSource.getMessage(message, null, locale));
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            responseBean = new ResponseBean(false, null, messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
        }
        return responseBean;
    }

    @ModelAttribute
    public void getResultUpdateBean(Model map) throws Exception {
        ResultUpdateInputBean sampleFileInputBean = new ResultUpdateInputBean();
        //get district and institution list
        String currentDate = commonRepository.getCurrentDateAsString();
        List<Institution> institutionList = commonRepository.getInstitutionList();
        List<Plate> plateList = commonRepository.getPlateListList(currentDate);
        //set list to input bean
        sampleFileInputBean.setInstitutionList(institutionList);
        sampleFileInputBean.setPlateList(plateList);
        //add to model
        map.addAttribute("resultupdate", sampleFileInputBean);
    }
}
