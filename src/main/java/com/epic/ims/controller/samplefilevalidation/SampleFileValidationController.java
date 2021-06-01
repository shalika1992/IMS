package com.epic.ims.controller.samplefilevalidation;

import com.epic.ims.annotation.accesscontrol.AccessControl;
import com.epic.ims.annotation.logcontroller.LogController;
import com.epic.ims.bean.common.Status;
import com.epic.ims.bean.samplefileverification.DefaultLabCode;
import com.epic.ims.bean.samplefileverification.SampleFileVerificationInputBean;
import com.epic.ims.bean.samplefileverification.SampleIdListBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.controller.samplefileupload.SampleFileUploadController;
import com.epic.ims.mapping.institution.Institution;
import com.epic.ims.mapping.sampleverifyfile.SampleVerifyFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.service.sampleverifyfile.SampleVerifyFileService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.common.DataTablesResponse;
import com.epic.ims.util.common.ResponseBean;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import com.epic.ims.util.varlist.PageVarList;
import com.epic.ims.util.varlist.SectionVarList;
import com.epic.ims.validation.RequestBeanValidation;
import com.epic.ims.validation.sampleverifyfile.SampleFileVerifyValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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
    @PostMapping(value = "/validsample", consumes = "application/json")
    public @ResponseBody
    ResponseBean validSample(@RequestBody SampleIdListBean sampleIdListBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] UPDATE SAMPLE RECORD");
        ResponseBean responseBean;
        try {
            String message = sampleVerifyFileService.validateSample(sampleIdListBean);
            if (message.isEmpty()) {
                responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.SAMPLE_VERIFY_FILE_RECORD_UPDATE_SUCCESSFULLY, null, locale), null);
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
    @PostMapping(value = "/invalidsample", consumes = "application/json")
    public @ResponseBody
    ResponseBean invalidSample(@RequestBody SampleIdListBean sampleIdListBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] UPDATE SAMPLE RECORD");
        ResponseBean responseBean;
        try {
            String message = sampleVerifyFileService.invalidateSample(sampleIdListBean);
            if (message.isEmpty()) {
                responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.SAMPLE_VERIFY_FILE_RECORD_UPDATE_SUCCESSFULLY, null, locale), null);
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
    @PostMapping(value = "/notfoundsample", consumes = "application/json")
    public @ResponseBody
    ResponseBean notFoundSample(@RequestBody SampleIdListBean sampleIdListBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] UPDATE SAMPLE RECORD");
        ResponseBean responseBean;
        try {
            String message = sampleVerifyFileService.notFoundSample(sampleIdListBean);
            if (message.isEmpty()) {
                responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.SAMPLE_VERIFY_FILE_RECORD_UPDATE_SUCCESSFULLY, null, locale), null);
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
    @GetMapping(value = "/generateinitiallabcode", consumes = "application/json")
    public @ResponseBody
    DefaultLabCode generateInitialLabCode() {
        logger.info("[" + sessionBean.getSessionid() + "] GENERATE INITIAL LAB CODE");
        DefaultLabCode defaultLabCode = new DefaultLabCode();
        try {
            defaultLabCode = sampleVerifyFileService.generateDefaultLabCode();
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return defaultLabCode;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_MGT, pageCode = PageVarList.SAMPLE_DATA_VERIFICATION)
    @PostMapping(value = "/updatelabcode")
    public void updateLabCode(@ModelAttribute("sampleverify") SampleFileVerificationInputBean sampleFileVerificationInputBean, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.info("[" + sessionBean.getSessionid() + "]  UPDATE LAB CODE");
        OutputStream outputStream = null;
        try {
            Object object = sampleVerifyFileService.generateLabCodeExcelReport(httpServletRequest, sampleFileVerificationInputBean);
            if (object instanceof SXSSFWorkbook) {
                SXSSFWorkbook workbook = (SXSSFWorkbook) object;
                httpServletResponse.setContentType("application/vnd.ms-excel");
                httpServletResponse.setHeader("Content-disposition", "attachment; filename=AuditTrace_Report.xlsx");
                httpServletResponse.setBufferSize(61440);
                outputStream = httpServletResponse.getOutputStream();
                workbook.write(outputStream);
            }
        } catch (Exception ex) {
            logger.error("Exception  :  ", ex);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException ex) {
                //do nothing
            }
        }
    }

    @ModelAttribute
    public void getSampleVerifyBean(Model map) throws Exception {
        SampleFileVerificationInputBean sampleFileVerificationInputBean = new SampleFileVerificationInputBean();
        //get status list and institution list
        List<Institution> institutionList = commonRepository.getInstitutionList();
        List<Status> statusList = commonRepository.getStatusListForSampleVerify();
        //set values to input bean
        sampleFileVerificationInputBean.setInstitutionList(institutionList);
        sampleFileVerificationInputBean.setStatusList(statusList);
        map.addAttribute("sampleverify", sampleFileVerificationInputBean);
    }

    @Override
    public BindingResult validateRequestBean(Object o) {
        DataBinder dataBinder = new DataBinder(o);
        dataBinder.setValidator(sampleFileVerifyValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }
}
