package com.epic.ims.controller.Institutionmgt;

import com.epic.ims.bean.common.Status;
import com.epic.ims.bean.institutionmgt.InstitutionInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.institutionmgt.Institution;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.service.institutionmgt.InstitutionService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.common.DataTablesResponse;
import com.epic.ims.util.common.ResponseBean;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import com.epic.ims.util.varlist.StatusVarList;
import com.epic.ims.validation.RequestBeanValidation;
import com.epic.ims.validation.institution.InstitutionBeanValidator;
import com.epic.ims.validation.institution.InstitutionBulkValidation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class InstitutionController implements RequestBeanValidation<Object> {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    MessageSource messageSource;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    Common common;

    @Autowired
    InstitutionService institutionService;

    @Autowired
    InstitutionBeanValidator institutionBeanValidator;

    @Autowired
    InstitutionBulkValidation institutionBulkValidation;

    @PostMapping(value = "/addInstitutionBulk", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseBean addInstitutionBulk(@ModelAttribute("institution") InstitutionInputBean institutionInputBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "]  INSTITUTION BULK ADD");
        ResponseBean responseBean = null;
        try {
            responseBean = institutionBulkValidation.validateInstitutionBulk(institutionInputBean, locale);

            if (responseBean.isFlag()) {

                String message = institutionService.insertInstitutionBulk(institutionInputBean, locale);
                if (message.isEmpty()) {
                    responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.INSTITUTION_BULK_MGT_ADDED_SUCCESSFULLY, null, locale), null);
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

    @PostMapping(value = "/deleteInstitution", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseBean deleteDepartment(@RequestParam String code, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] DELETE INSTITUTION");
        ResponseBean responseBean;
        try {
            String message = institutionService.deleteInstitution(code);
            if (message.isEmpty()) {
                responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.INSTITUTION_MGT_DELETE_SUCCESSFULLY, null, locale), null);
            } else {
                responseBean = new ResponseBean(false, null, messageSource.getMessage(message, null, locale));
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
            responseBean = new ResponseBean(false, null, messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
        }
        return responseBean;
    }

    @PostMapping(value = "/updateInstitution", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseBean updateInstitution(@ModelAttribute("institution") InstitutionInputBean institutionInputBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "] UPDATE INSTITUTION");
        ResponseBean responseBean;
        try {
            BindingResult bindingResult = validateRequestBean(institutionInputBean);
            if (bindingResult.hasErrors()) {
                responseBean = new ResponseBean(false, null, messageSource.getMessage(bindingResult.getAllErrors().get(0).getCode(), new Object[]{bindingResult.getAllErrors().get(0).getDefaultMessage()}, Locale.US));
            } else {
                String message = institutionService.updateInstitution(institutionInputBean);
                if (message.isEmpty()) {
                    responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.INSTITUTION_MGT_UPDATE_SUCCESSFULLY, null, locale), null);
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

    @GetMapping(value = "/institutionMgt.htm")
    public ModelAndView viewInstitutionPage(ModelMap modelMap, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "]  INSTITUTION MANAGEMENT PAGE VIEW");
        ModelAndView modelAndView;
        try {
            modelAndView = new ModelAndView("institutionview", "beanmap", new ModelMap());
        } catch (Exception exception) {
            logger.error(exception);
            //set the error message to model map
            modelMap.put("msg", messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
            modelAndView = new ModelAndView("institutionview", modelMap);
        }
        return modelAndView;
    }

    @PostMapping(value = "/listInstitution", headers = {"content-type=application/json"})
    public @ResponseBody
    DataTablesResponse<Institution> searchInstitution(@RequestBody InstitutionInputBean institutionInputBean) {
        logger.info("[" + sessionBean.getSessionid() + "]  INSTITUTION SEARCH");
        DataTablesResponse<Institution> responseBean = new DataTablesResponse<>();

        try {
            long count = institutionService.getCount(institutionInputBean);

            if (count > 0) {
                List<Institution> institutionList = institutionService.getInstitutionSearchResultList(institutionInputBean);
                //set data set to response bean
                responseBean.data.addAll(institutionList);
            } else {
                //set data set to response bean
                responseBean.data.addAll(new ArrayList<>());
            }
            responseBean.echo = institutionInputBean.echo;
            responseBean.columns = institutionInputBean.columns;
            responseBean.totalRecords = count;
            responseBean.totalDisplayRecords = count;


        } catch (Exception exception) {
            logger.error("Exception " + exception);
        }

        return responseBean;

    }

    @PostMapping(value = "/addInstitution", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseBean addInstitution(@ModelAttribute("institution") InstitutionInputBean institutionInputBean, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "]  INSTITUTION ADD");
        ResponseBean responseBean = null;
        try {
            BindingResult bindingResult = validateRequestBean(institutionInputBean);
            if (bindingResult.hasErrors()) {
                responseBean = new ResponseBean(false, null, messageSource.getMessage(bindingResult.getAllErrors().get(0).getCode(), new Object[]{bindingResult.getAllErrors().get(0).getDefaultMessage()}, Locale.US));
            } else {
                String message = institutionService.insertInstitution(institutionInputBean, locale);
                if (message.isEmpty()) {
                    responseBean = new ResponseBean(true, messageSource.getMessage(MessageVarList.INSTITUTION_MGT_ADDED_SUCCESSFULLY, null, locale), null);
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

    @GetMapping(value = "/getInstitution")
    public @ResponseBody
    Institution getInstitution(@RequestParam String institutionCode) {
        logger.info("[" + sessionBean.getSessionid() + "]  GET INSTITUTION");
        Institution institution = new Institution();
        try {
            if (institutionCode != null && !institutionCode.isEmpty()) {
                institution = institutionService.getInstitution(institutionCode);
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return institution;
    }

    @ModelAttribute
    public void getSystemUserBean(Model map) throws Exception {
        InstitutionInputBean institutionInputBean = new InstitutionInputBean();
        //get status list
        List<Status> statusActList = common.getActiveStatusList();
        List<Status> statusList = commonRepository.getStatusList(StatusVarList.STATUS_CATEGORY_USER);
        //set values to task bean
        institutionInputBean.setStatusList(statusList);
        institutionInputBean.setStatusActList(statusActList);

        //add values to model map
        map.addAttribute("institution", institutionInputBean);
    }

    @Override
    public BindingResult validateRequestBean(Object o) {
        DataBinder dataBinder = new DataBinder(o);
        dataBinder.setValidator(institutionBeanValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }
}
