package com.epic.ims.controller.samplefilevalidation;

import com.epic.ims.annotation.accesscontrol.AccessControl;
import com.epic.ims.annotation.logcontroller.LogController;
import com.epic.ims.bean.common.Status;
import com.epic.ims.bean.institutionmgt.InstitutionInputBean;
import com.epic.ims.bean.samplefileverification.SampleFileVerificationInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.controller.samplefileupload.SampleFileUploadController;
import com.epic.ims.mapping.institutionmgt.Institution;
import com.epic.ims.mapping.sampleverifyfile.SampleVerifyFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.service.sampleverifyfile.SampleVerifyFileService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.common.DataTablesResponse;
import com.epic.ims.util.common.ResponseBean;
import com.epic.ims.util.varlist.*;
import com.epic.ims.validation.RequestBeanValidation;
import com.epic.ims.validation.sampleverifyfile.SampleFileVerifyValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class SampleFileValidationController implements RequestBeanValidation<Object> {
    private static Logger logger = LogManager.getLogger(SampleFileUploadController.class);

    @Autowired
    SessionBean sessionBean;

    @Autowired
    MessageSource messageSource;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    Common common;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    SampleVerifyFileService sampleVerifyFileService;

    @Autowired
    SampleFileVerifyValidator sampleFileVerifyValidator;


    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_MGT, pageCode = PageVarList.SAMPLE_DATA_VERIFICATION)
    @RequestMapping(value = "/viewSampleVerification", method = RequestMethod.GET)
    public ModelAndView getSampleFileVerification(ModelMap modelMap, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "]  SAMPLE FILE VERIFICATION PAGE VIEW");
        ModelAndView modelAndView = null;
        try {
            modelAndView = new ModelAndView("sampleverificationview", "beanmap", new ModelMap());
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            //set the error message to model map
            modelMap.put("msg", messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
            modelAndView = new ModelAndView("sampleverificationview", modelMap);
        }
        return modelAndView;
    }




    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_MGT, pageCode = PageVarList.SAMPLE_DATA_VERIFICATION)
    @PostMapping(value = "/listSampleVerification", headers = {"content-type=application/json"})
    public @ResponseBody
    DataTablesResponse<SampleVerifyFile> searchSampleFileVerification(@RequestBody SampleFileVerificationInputBean sampleFileVerificationInputBean) {
        logger.info("[" + sessionBean.getSessionid() + "]  SAMPLE FILE VERIFICATION SEARCH");
        DataTablesResponse<SampleVerifyFile> responseBean = new DataTablesResponse<>();
        try {
            long count = sampleVerifyFileService.getCount(sampleFileVerificationInputBean);
            if (count > 0) {
                List<SampleVerifyFile> sampleVerifyFileList = sampleVerifyFileService.getSampleVerifySearchResultList(sampleFileVerificationInputBean);
                //set data set to response bean
                responseBean.data.addAll(sampleVerifyFileList);
                responseBean.echo = sampleFileVerificationInputBean.echo;
                responseBean.columns = sampleFileVerificationInputBean.columns;
                responseBean.totalRecords = count;
                responseBean.totalDisplayRecords = count;
            } else {
                //set data set to response bean
                responseBean.data.addAll(new ArrayList<>());
                responseBean.echo = sampleFileVerificationInputBean.echo;
                responseBean.columns = sampleFileVerificationInputBean.columns;
                responseBean.totalRecords = count;
                responseBean.totalDisplayRecords = count;
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return responseBean;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_MGT, pageCode = PageVarList.SAMPLE_DATA_VERIFICATION)
    @GetMapping(value = "/getSampleVerifyRecord")
    public @ResponseBody
    SampleVerifyFile getSampleVerifyRecord(@RequestParam String id) {
        logger.info("[" + sessionBean.getSessionid() + "]  GET SAMPLE VERIFY RECORD");
        SampleVerifyFile sampleVerifyFile = new SampleVerifyFile();
        try {
            if (id != null && !id.isEmpty()) {
                sampleVerifyFile = sampleVerifyFileService.getSampleVerifyRecord(id);
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return sampleVerifyFile;
    }


    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_MGT, pageCode = PageVarList.SAMPLE_DATA_VERIFICATION)
    @PostMapping(value = "/verifySampleRecord", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseBean verifySampleRecord(@ModelAttribute("sampleverify") SampleFileVerificationInputBean sampleFileVerificationInputBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] VERIFY SAMPLE RECORD");
        ResponseBean responseBean;
        try {
            String message = sampleVerifyFileService.verifySampleRecord(sampleFileVerificationInputBean, locale);
            if (message.isEmpty()) {
                responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.SAMPLERECORD_VERIFY_SUCCESSFULLY, null, locale), null);
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
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_MGT, pageCode = PageVarList.SAMPLE_DATA_VERIFICATION)
    @PostMapping(value = "/rejectSampleRecord", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseBean rejectSampleRecord(@ModelAttribute("sampleverify") SampleFileVerificationInputBean sampleFileVerificationInputBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] REJECT SAMPLE RECORD");
        ResponseBean responseBean;
        try {
            BindingResult bindingResult = validateRequestBean(sampleFileVerificationInputBean);
            if (bindingResult.hasErrors()) {
                responseBean = new ResponseBean(false, null, messageSource.getMessage(bindingResult.getAllErrors().get(0).getCode(), new Object[]{bindingResult.getAllErrors().get(0).getDefaultMessage()}, locale));
            } else {
                String message = sampleVerifyFileService.rejectSampleRecord(sampleFileVerificationInputBean, locale);
                if (message.isEmpty()) {
                    responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.SAMPLERECORD_REJECT_SUCCESSFULLY, null, locale), null);
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
        SampleFileVerificationInputBean sampleFileVerificationInputBean = new SampleFileVerificationInputBean();
        //get status list
        List<Status> statusActList = common.getActiveStatusList();
        List<Status> statusList = commonRepository.getStatusList(StatusVarList.STATUS_CATEGORY_USER);
        //set values to task bean
        sampleFileVerificationInputBean.setStatusList(statusList);
        sampleFileVerificationInputBean.setStatusActList(statusActList);

        //add values to model map
        map.addAttribute("sampleverify", sampleFileVerificationInputBean);
    }


    @ModelAttribute
    public void getSampleVerifyBean(Model map) throws Exception {
        map.addAttribute("sampleverify", new SampleFileVerificationInputBean());
    }

    @Override
    public BindingResult validateRequestBean(Object o) {
        DataBinder dataBinder = new DataBinder(o);
        dataBinder.setValidator(sampleFileVerifyValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }
}
