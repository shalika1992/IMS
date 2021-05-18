package com.epic.ims.controller.samplefileupload;

import com.epic.ims.annotation.accesscontrol.AccessControl;
import com.epic.ims.annotation.logcontroller.LogController;
import com.epic.ims.bean.samplefileupload.SampleData;
import com.epic.ims.bean.samplefileupload.SampleFileInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.district.District;
import com.epic.ims.mapping.institution.Institution;
import com.epic.ims.mapping.samplefile.SampleFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.service.samplefile.SampleFileService;
import com.epic.ims.util.common.DataTablesResponse;
import com.epic.ims.util.common.ExcelHelper;
import com.epic.ims.util.common.ResponseBean;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import com.epic.ims.util.varlist.PageVarList;
import com.epic.ims.util.varlist.SectionVarList;
import com.epic.ims.validation.RequestBeanValidation;
import com.epic.ims.validation.samplefileupload.SampleFileUploadValidator;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@Scope("request")
public class SampleFileUploadController implements RequestBeanValidation<Object> {
    private static Logger logger = LogManager.getLogger(SampleFileUploadController.class);

    @Autowired
    SessionBean sessionBean;

    @Autowired
    MessageSource messageSource;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    SampleFileUploadValidator sampleFileUploadValidator;

    @Autowired
    SampleFileService sampleFileService;

    @Autowired
    ExcelHelper excelHelper;

    @Autowired
    CommonRepository commonRepository;

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_MGT, pageCode = PageVarList.SAMPLE_FILE_UPLOAD)
    @RequestMapping(value = "/viewSampleFile", method = RequestMethod.GET)
    public ModelAndView getSampleFileUpload(ModelMap modelMap, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "]  SAMPLE FILE PAGE VIEW");
        ModelAndView modelAndView = null;
        try {
            modelAndView = new ModelAndView("samplefileview", "beanmap", new ModelMap());
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            //set the error message to model map
            modelMap.put("msg", messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
            modelAndView = new ModelAndView("samplefileview", modelMap);
        }
        return modelAndView;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_MGT, pageCode = PageVarList.SAMPLE_FILE_UPLOAD)
    @PostMapping(value = "/listSampleFile", headers = {"content-type=application/json"})
    public @ResponseBody
    DataTablesResponse<SampleFile> searchSampleFileUpload(@RequestBody SampleFileInputBean sampleFileInputBean) {
        logger.info("[" + sessionBean.getSessionid() + "]  SAMPLE FILE SEARCH");
        DataTablesResponse<SampleFile> responseBean = new DataTablesResponse<>();
        try {
            long count = sampleFileService.getCount(sampleFileInputBean);
            if (count > 0) {
                List<SampleFile> list = sampleFileService.getSampleFileSearchResultList(sampleFileInputBean);
                //set data set to response bean
                responseBean.data.addAll(list);
                responseBean.echo = sampleFileInputBean.echo;
                responseBean.columns = sampleFileInputBean.columns;
                responseBean.totalRecords = count;
                responseBean.totalDisplayRecords = count;
            } else {
                //set data set to response bean
                responseBean.data.addAll(new ArrayList<>());
                responseBean.echo = sampleFileInputBean.echo;
                responseBean.columns = sampleFileInputBean.columns;
                responseBean.totalRecords = count;
                responseBean.totalDisplayRecords = count;
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return responseBean;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_MGT, pageCode = PageVarList.SAMPLE_FILE_UPLOAD)
    @RequestMapping(value = "/sampleFileUpload", method = RequestMethod.POST)
    public @ResponseBody
    ResponseBean postSampleFileUpload(@RequestParam("sampleFile") MultipartFile multipartFile, @RequestParam("receivedDate") String receivedDate, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] UPDATE SAMPLE FILE RECORD");
        ResponseBean responseBean;
        try {
            if (excelHelper.hasExcelFormat(multipartFile)) {
                List<SampleData> sampleDataList = excelHelper.excelToSampleData(multipartFile.getInputStream(), receivedDate);
                if (sampleDataList != null) {
                    //validate the sample data mandatory fields
                    String message = sampleFileService.validateMandatoryFields(sampleDataList, locale);
                    if (message.isEmpty()) {
                        //validate the duplicate records
                        message = sampleFileService.checkDuplicate(sampleDataList, receivedDate, locale);
                        if (message.isEmpty()) {
                            //upload sample file
                            message = sampleFileService.uploadSampleFile(sampleDataList, receivedDate, locale);
                            if (message.isEmpty()) {
                                responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.SAMPLE_FILE_UPLOAD_SUCCESSFULLY, null, locale), null);
                            } else {
                                responseBean = new ResponseBean(false, null, messageSource.getMessage(message, null, locale));
                            }
                        } else {
                            responseBean = new ResponseBean(false, null, message);
                        }
                    } else {
                        responseBean = new ResponseBean(false, null, message);
                    }
                } else {
                    responseBean = new ResponseBean(false, null, messageSource.getMessage(MessageVarList.SAMPLE_FILE_INVALID_FILE, null, locale));
                }
            } else {
                responseBean = new ResponseBean(false, null, messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            responseBean = new ResponseBean(false, null, messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
        }
        return responseBean;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_MGT, pageCode = PageVarList.SAMPLE_FILE_UPLOAD)
    @GetMapping(value = "/getSampleFileRecord")
    public @ResponseBody
    SampleFile getSampleFileRecord(@RequestParam String id) {
        logger.info("[" + sessionBean.getSessionid() + "]  GET SAMPLE FILE RECORD");
        SampleFile sampleFile = new SampleFile();
        try {
            if (id != null && !id.isEmpty()) {
                sampleFile = sampleFileService.getSampleFileRecord(id);
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return sampleFile;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_MGT, pageCode = PageVarList.SAMPLE_FILE_UPLOAD)
    @PostMapping(value = "/updateSampleFileRecord", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseBean updateSampleFileRecord(@ModelAttribute("samplefile") SampleFileInputBean sampleFileInputBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] UPDATE SAMPLE FILE RECORD");
        ResponseBean responseBean;
        try {
            BindingResult bindingResult = validateRequestBean(sampleFileInputBean);
            if (bindingResult.hasErrors()) {
                responseBean = new ResponseBean(false, null, messageSource.getMessage(bindingResult.getAllErrors().get(0).getCode(), new Object[]{bindingResult.getAllErrors().get(0).getDefaultMessage()}, locale));
            } else {
                String message = sampleFileService.updateSampleFileRecord(sampleFileInputBean, locale);
                if (message.isEmpty()) {
                    responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.SAMPLE_FILE_RECORD_UPDATE_SUCCESSFULLY, null, locale), null);
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
    public void getSampleFileUploadBean(Model map) throws Exception {
        SampleFileInputBean sampleFileInputBean = new SampleFileInputBean();
        //get district and institution list
        List<District> districtList = commonRepository.getDistrictList();
        List<Institution> institutionList = commonRepository.getInstitutionList();
        //set list to input bean
        sampleFileInputBean.setDistrictList(districtList);
        sampleFileInputBean.setInstitutionList(institutionList);
        //add to model
        map.addAttribute("samplefile", sampleFileInputBean);
    }

    @Override
    public BindingResult validateRequestBean(Object o) {
        DataBinder dataBinder = new DataBinder(o);
        dataBinder.setValidator(sampleFileUploadValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }
}
