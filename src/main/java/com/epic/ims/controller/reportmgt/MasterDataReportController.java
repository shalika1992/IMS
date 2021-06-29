package com.epic.ims.controller.reportmgt;

import com.epic.ims.annotation.accesscontrol.AccessControl;
import com.epic.ims.annotation.logcontroller.LogController;
import com.epic.ims.bean.common.CommonInstitution;
import com.epic.ims.bean.common.Result;
import com.epic.ims.bean.common.Status;
import com.epic.ims.bean.reportmgt.MasterDataInputBeen;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.reportmgt.MasterData;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.service.reportmgt.MasterDataReportService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.common.DataTablesResponse;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import com.epic.ims.util.varlist.PageVarList;
import com.epic.ims.util.varlist.SectionVarList;
import com.epic.ims.validation.institution.InstitutionBeanValidator;
import com.epic.ims.validation.institution.InstitutionBulkValidation;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.lang.SystemUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

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
                responseBean.echo = masterDataInputBeen.echo;
                responseBean.columns = masterDataInputBeen.columns;
                responseBean.totalRecords = count;
                responseBean.totalDisplayRecords = count;
            } else {
                //set data set to response bean
                responseBean.data.addAll(new ArrayList<>());
                responseBean.echo = masterDataInputBeen.echo;
                responseBean.columns = masterDataInputBeen.columns;
                responseBean.totalRecords = count;
                responseBean.totalDisplayRecords = count;
            }
        } catch (Exception exception) {
            logger.error("Exception " + exception);
        }
        return responseBean;
    }

    @LogController
    @PostMapping(value = "/downloadMasterDataPdf")
    @AccessControl(sectionCode = SectionVarList.SECTION_REPORT_EXPLORER, pageCode = PageVarList.REPORT_GENERATION)
    public void getMasterDataPDF(@ModelAttribute("masterData") MasterDataInputBeen masterDataInputBeen, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.info("[" + sessionBean.getSessionid() + "]  MASTER DATA REPORT PDF");
        OutputStream outputStream = null;
        try {
            List<MasterData> masterDataList = masterDataReportService.getMasterDataSearchResultListForReport(masterDataInputBeen);
            if (masterDataList != null && !masterDataList.isEmpty() && masterDataList.size() > 0) {
                String currentDateTime = commonRepository.getCurrentDateTimeAsString();
                String masterDataReportFileJasperPath = this.getMasterDataReportJasperPath();

                //report file initialization
                File file = new File(masterDataReportFileJasperPath);
                InputStream jasperStream = new FileInputStream(file);

                Map<String, Object> parameterMap = new HashMap<>();

                //set parameters to map
                parameterMap.put("reportTime", currentDateTime);
                parameterMap.put("receivedDate", common.replaceEmptyorNullStringToALL(masterDataInputBeen.getReceivedDate()));
                parameterMap.put("collectionDate", "--");
                parameterMap.put("serialNo", common.replaceEmptyorNullStringToALL(masterDataInputBeen.getSerialNumber()));
                parameterMap.put("name", common.replaceEmptyorNullStringToALL(masterDataInputBeen.getName()));
                parameterMap.put("nic", common.replaceEmptyorNullStringToALL(masterDataInputBeen.getNic()));
                parameterMap.put("institution", common.replaceEmptyorNullStringToALL(masterDataInputBeen.getInstitutionCode()));
                parameterMap.put("status", common.replaceEmptyorNullStringToALL(masterDataInputBeen.getStatus()));
                parameterMap.put("result", common.replaceEmptyorNullStringToALL(masterDataInputBeen.getResult()));
                parameterMap.put("testMethod", commonVarList.REPORT_TEST_METHOD);
                parameterMap.put("consultantName", commonVarList.REPORT_CONSULTANT_NAME);
                parameterMap.put("consultantDes", commonVarList.REPORT_CONSULTANT_DESCRIPTION);

                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, new JRBeanCollectionDataSource(masterDataList));

                httpServletResponse.setContentType("application/x-download");
                httpServletResponse.setHeader("Content-disposition", "inline; filename=Full-Test-Report.pdf");

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
            }
        }
    }

    @LogController
    @PostMapping(value = "/downloadMasterDataIndividualPdf")
    @AccessControl(sectionCode = SectionVarList.SECTION_REPORT_EXPLORER, pageCode = PageVarList.REPORT_GENERATION)
    public void getMasterDataIndividualPdf(@ModelAttribute("masterData") MasterDataInputBeen masterDataInputBeen, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.info("[" + sessionBean.getSessionid() + "]  MASTER DATA INDIVIDUAL REPORT PDF");
        OutputStream outputStream = null;
        try {
            MasterData masterData = masterDataReportService.getMasterDataSearchObjectForIndividualReport(masterDataInputBeen);
            if (masterData != null) {
                String currentDateTime = commonRepository.getCurrentDateTimeAsString();
                String indivudualReportFileJasperPath = this.getIndividualDataReportJasperPath();

                //report file initialization
                File file = new File(indivudualReportFileJasperPath);
                InputStream jasperStream = new FileInputStream(file);

                Map<String, Object> parameterMap = new HashMap<>();
                //set parameters to map
                parameterMap.put("reportTime", currentDateTime);
                parameterMap.put("receivedDate", common.replaceEmptyorNullStringToALL(masterData.getReceivedDate()));
                parameterMap.put("collectionDate", "--");
                parameterMap.put("institution", common.replaceEmptyorNullStringToALL(masterData.getInstitutionCode()));
                parameterMap.put("name", common.replaceEmptyorNullStringToALL(masterData.getName()));
                parameterMap.put("age", common.replaceEmptyorNullStringToALL(masterData.getAge()));
                parameterMap.put("gender", common.replaceEmptyorNullStringToALL(masterData.getGender()));
                parameterMap.put("nic", common.replaceEmptyorNullStringToALL(masterData.getNic()));
                parameterMap.put("contactNumber", common.replaceEmptyorNullStringToALL(masterData.getContactNumber()));
                parameterMap.put("address", common.replaceEmptyorNullStringToALL(masterData.getAddress()));
                parameterMap.put("serialNumber", common.replaceEmptyorNullStringToALL(masterData.getReferenceNumber()));
                parameterMap.put("barcode", common.replaceEmptyorNullStringToALL(masterData.getBarcode()));
                parameterMap.put("testMethod", commonVarList.REPORT_TEST_METHOD);
                parameterMap.put("result", common.replaceEmptyorNullStringToALL(masterData.getResultDescription()));
                parameterMap.put("ct_target1", common.replaceEmptyorNullStringToALL(masterData.getCt_target1()));
                parameterMap.put("ct_target2", common.replaceEmptyorNullStringToALL(masterData.getCt_target1()));

                parameterMap.put("consultantName", commonVarList.REPORT_CONSULTANT_NAME);
                parameterMap.put("consultantDes", commonVarList.REPORT_CONSULTANT_DESCRIPTION);

                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, new JREmptyDataSource());

                httpServletResponse.setContentType("application/x-download");
                httpServletResponse.setHeader("Content-disposition", "inline; filename=Test-Report.pdf");

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
            }
        }
    }

    private String getMasterDataReportJasperPath() {
        String filePath = "";
        try {
            if (SystemUtils.IS_OS_LINUX) {
                filePath = commonVarList.MASTERREPORTFILE_LINUX_JASPER_FILEPATH;
            } else if (SystemUtils.IS_OS_WINDOWS) {
                filePath = commonVarList.MASTERREPORTFILE_WINDOWS_JASPER_FILEPATH;
            } else {
                filePath = commonVarList.MASTERREPORTFILE_WINDOWS_JASPER_FILEPATH;
            }
        } catch (Exception e) {
            throw e;
        }
        return filePath;
    }

    private String getIndividualDataReportJasperPath() {
        String filePath = "";
        try {
            if (SystemUtils.IS_OS_LINUX) {
                filePath = commonVarList.INDIVIDUALREPORTFILE_LINUX_JASPER_FILEPATH;
            } else if (SystemUtils.IS_OS_WINDOWS) {
                filePath = commonVarList.INDIVIDUALREPORTFILE_WINDOWS_JASPER_FILEPATH;
            } else {
                filePath = commonVarList.INDIVIDUALREPORTFILE_WINDOWS_JASPER_FILEPATH;
            }
        } catch (Exception e) {
            throw e;
        }
        return filePath;
    }

    @ModelAttribute
    public void getMasterDataBean(Model map) throws Exception {
        MasterDataInputBeen masterDataInputBeen = new MasterDataInputBeen();
        //get status list
        List<Status> statusList = commonRepository.getReportStatusList();
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
