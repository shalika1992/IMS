package com.epic.ims.controller.reportmgt;

import com.epic.ims.annotation.accesscontrol.AccessControl;
import com.epic.ims.annotation.logcontroller.LogController;
import com.epic.ims.bean.common.CommonInstitution;
import com.epic.ims.bean.common.Result;
import com.epic.ims.bean.common.Status;
import com.epic.ims.bean.institutionmgt.InstitutionInputBean;
import com.epic.ims.bean.reportmgt.MasterDataInputBeen;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.institution.Institution;
import com.epic.ims.mapping.reportmgt.MasterData;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.service.institutionmgt.InstitutionService;
import com.epic.ims.service.reportmgt.MasterDataReportService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.common.DataTablesResponse;
import com.epic.ims.util.varlist.*;
import com.epic.ims.validation.institution.InstitutionBeanValidator;
import com.epic.ims.validation.institution.InstitutionBulkValidation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class MasterDataReportController {
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
    MasterDataReportService masterDataReportService;

    @Autowired
    InstitutionBeanValidator institutionBeanValidator;

    @Autowired
    InstitutionBulkValidation institutionBulkValidation;

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_REPORT_EXPLORER, pageCode = PageVarList.REPORT_GENERATION)
    @RequestMapping(value = "/viewReportGeneration", method = RequestMethod.GET)
    public ModelAndView viewReportGenerationPage(ModelMap modelMap, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "]  MASTER DATA PAGE VIEW");
        ModelAndView modelAndView;
        try {
            modelAndView = new ModelAndView("reportgenerationview", "beanmap", new ModelMap());
        } catch (Exception exception) {
            logger.error(exception);
            //set the error message to model map
            modelMap.put("msg", messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
            modelAndView = new ModelAndView("reportgenerationview", modelMap);
        }
        return modelAndView;
    }

    @LogController
    @PostMapping(value = "/listMasterData", headers = {"content-type=application/json"})
    public @ResponseBody
    DataTablesResponse<MasterData> searchMasterData(@RequestBody MasterDataInputBeen masterDataInputBeen) {
        logger.info("[" + sessionBean.getSessionid() + "]  MASTER DATA SEARCH");
        DataTablesResponse<MasterData> responseBean = new DataTablesResponse<>();
        try {
            long count = masterDataReportService.getCount(masterDataInputBeen);
            if (count > 0) {
                List<MasterData> masterDataList = masterDataReportService.getMasterDataSearchResultList(masterDataInputBeen);
                //set data set to response bean
                responseBean.data.addAll(masterDataList);
            } else {
                //set data set to response bean
                responseBean.data.addAll(new ArrayList<>());
            }
            responseBean.echo = masterDataInputBeen.echo;
            responseBean.columns = masterDataInputBeen.columns;
            responseBean.totalRecords = count;
            responseBean.totalDisplayRecords = count;
        } catch (Exception exception) {
            logger.error("Exception " + exception);
        }
        return responseBean;
    }

    @ModelAttribute
    public void getMasterDataBean(Model map) throws Exception {
        MasterDataInputBeen masterDataInputBeen = new MasterDataInputBeen();
        //get status list
        List<Status> statusList = commonRepository.getStatusList(StatusVarList.STATUS_CATEGORY_REPORT);
        List<Result> resultList = commonRepository.getResultList();
        List<CommonInstitution> commonInstitutionList = commonRepository.getCommonInstitutionList();
        //set values to task bean
        masterDataInputBeen.setStatusList(statusList);
        masterDataInputBeen.setResultList(resultList);
        masterDataInputBeen.setCommonInstitutionList(commonInstitutionList);

        //add values to model map
        map.addAttribute("masterData", masterDataInputBeen);
    }

}
