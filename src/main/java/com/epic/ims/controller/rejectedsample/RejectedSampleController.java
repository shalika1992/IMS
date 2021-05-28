package com.epic.ims.controller.rejectedsample;


import com.epic.ims.annotation.accesscontrol.AccessControl;
import com.epic.ims.bean.rejectedsample.RejectedSampleDataInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.institution.Institution;
import com.epic.ims.mapping.rejectedsampledata.RejectedSampleData;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.institutionmgt.InstitutionRepository;
import com.epic.ims.service.rejectedsample.RejectedSampleService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.common.DataTablesResponse;
import com.epic.ims.util.varlist.MessageVarList;
import com.epic.ims.util.varlist.PageVarList;
import com.epic.ims.util.varlist.SectionVarList;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Controller
@Scope("request")
public class RejectedSampleController {
    private static Logger logger = LogManager.getLogger(RejectedSampleController.class);

    @Autowired
    InstitutionRepository institutionRepository;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    Common common;

    @Autowired
    RejectedSampleService rejectedSampleService;

    @GetMapping(value = "/viewRejectSample")
    @AccessControl(sectionCode = SectionVarList.SECTION_REPORT_EXPLORER, pageCode = PageVarList.REPEATED_SAMPLES)
    public ModelAndView viewRejectSamplePage(ModelMap modelMap, Locale locale) {
        logger.info("[" + sessionBean.getSessionid() + "]  SYSTEM REJECTED SAMPLE PAGE VIEW");
        ModelAndView modelAndView;
        try {
            modelAndView = new ModelAndView("rejectedsampleview", "rejectsamplemap", new ModelMap());
        } catch (Exception exception) {
            modelMap.put("msg", messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
            modelAndView = new ModelAndView("rejectedsampleview", modelMap);
        }
        return modelAndView;
    }

    @ResponseBody
    @AccessControl(sectionCode = SectionVarList.SECTION_REPORT_EXPLORER, pageCode = PageVarList.REPEATED_SAMPLES)
    @PostMapping(value = "/listRejectedSample", headers = {"content-type=application/json"})
    public DataTablesResponse<RejectedSampleData> searchRejectedSample(@RequestBody RejectedSampleDataInputBean rejectedSampleInputBean) {
        logger.info("[" + sessionBean.getSessionid() + "]  REJECTED SAMPLE SEARCH");
        DataTablesResponse<RejectedSampleData> responseBean = new DataTablesResponse<>();
        try {
            long count = rejectedSampleService.getCount(rejectedSampleInputBean);
            if (count > 0) {
                List<RejectedSampleData> rejectedSampleDataList = rejectedSampleService.getRejectedSampleSearchResultList(rejectedSampleInputBean);
                //set data set to response bean
                responseBean.data.addAll(rejectedSampleDataList);
                responseBean.echo = rejectedSampleInputBean.echo;
                responseBean.columns = rejectedSampleInputBean.columns;
                responseBean.totalRecords = count;
                responseBean.totalDisplayRecords = count;
            } else {
                //set data set to response bean
                responseBean.data.addAll(new ArrayList<>());
                responseBean.echo = rejectedSampleInputBean.echo;
                responseBean.columns = rejectedSampleInputBean.columns;
                responseBean.totalRecords = count;
                responseBean.totalDisplayRecords = count;
            }
        } catch (Exception exception) {
            logger.error("Exception " + exception);
        }
        return responseBean;
    }

    @PostMapping(value = "/pdfReportRejected")
    @AccessControl(sectionCode = SectionVarList.SECTION_REPORT_EXPLORER, pageCode = PageVarList.REPEATED_SAMPLES)
    public void pdfReportRejectedSample(@ModelAttribute("rejectedsample") RejectedSampleDataInputBean rejectedSampleDataInputBean, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.info("[" + sessionBean.getSessionid() + "]  REJECTED SAMPLE REPORT");
        OutputStream outputStream = null;
        try {
            List<RejectedSampleData> rejectedSampleDataList = rejectedSampleService.getRejectedSampleSearchResultListForReport(rejectedSampleDataInputBean);
            if (rejectedSampleDataList != null && !rejectedSampleDataList.isEmpty() && rejectedSampleDataList.size() > 0) {
                InputStream jasperStream = this.getClass().getResourceAsStream("/reports/rejectsamples/rejectsamples_report.jasper");
                Map<String, Object> parameterMap = new HashMap<>();
                //set parameters to map
                parameterMap.put("receiveddate", common.replaceEmptyorNullStringToALL(rejectedSampleDataInputBean.getReceivedDate()));
                parameterMap.put("referenceno", common.replaceEmptyorNullStringToALL(rejectedSampleDataInputBean.getReferenceNo()));
                parameterMap.put("institutioncode", common.replaceEmptyorNullStringToALL(rejectedSampleDataInputBean.getInstitutionCode()));
                parameterMap.put("name", common.replaceEmptyorNullStringToALL(rejectedSampleDataInputBean.getName()));
                parameterMap.put("nic", common.replaceEmptyorNullStringToALL(rejectedSampleDataInputBean.getNic()));

                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, new JRBeanCollectionDataSource(rejectedSampleDataList));

                httpServletResponse.setContentType("application/x-download");
                httpServletResponse.setHeader("Content-disposition", "inline; filename=Rejectsample-Report.pdf");

                final OutputStream outStream = httpServletResponse.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
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
    public void getRejectedSampleBean(Model map) throws Exception {
        RejectedSampleDataInputBean rejectedSampleDataInputBean = new RejectedSampleDataInputBean();
        List<Institution> institutionList = commonRepository.getInstitutionList();

        rejectedSampleDataInputBean.setInstitutionList(institutionList);
        map.addAttribute("rejectedsample", rejectedSampleDataInputBean);
    }
}
