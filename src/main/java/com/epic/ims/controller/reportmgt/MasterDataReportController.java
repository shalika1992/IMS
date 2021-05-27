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
import com.epic.ims.util.varlist.*;
import com.epic.ims.validation.institution.InstitutionBeanValidator;
import com.epic.ims.validation.institution.InstitutionBulkValidation;
import lombok.var;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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

    @AccessControl(sectionCode = SectionVarList.SECTION_REPORT_EXPLORER, pageCode = PageVarList.REPORT_GENERATION)
    @GetMapping(value = "/downloadMasterDataRecordPdf", headers = {"content-type=application/json"})
    public @ResponseBody void getMasterDataPDF(@RequestParam String id, HttpServletResponse response) {
        logger.info("[" + sessionBean.getSessionid() + "]  MASTER DATA RECORD REPORT PDF");
        OutputStream outputStream = null;

        try {
            MasterData masterData = masterDataReportService.getMasterDataRecord(id);
            if (masterData!=null) {
                File file = ResourceUtils.getFile("classpath:reports/finalReport/individualfinalreport.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

                Map<String, Object> parameterMap = new HashMap<>();
                //set parameters to map start
                parameterMap.put("consultantName", "Dr. (Mrs) R.N.D. De Silva");
                parameterMap.put("consultantDes", "M.B.B.S., PG Dip. Med.Micro., MD\n" +
                        "Consultant Microbiologist");

                if (masterData.getReceivedDate()!=null && !masterData.getReceivedDate().toString().isEmpty()){
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    String date = simpleDateFormat.format(masterData.getReceivedDate());
                    parameterMap.put("receivedDate", date);
                }else{
                    parameterMap.put("receivedDate", "--");
                }

                if (masterData.getReportTime()!=null && !masterData.getReportTime().toString().isEmpty()){
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    String date = simpleDateFormat.format(masterData.getReportTime());
                    parameterMap.put("reportTime",date);
                }else{
                    parameterMap.put("reportTime", "--");
                }

                if (masterData.getInstitutionName()!=null && !masterData.getInstitutionName().isEmpty()){
                    String institution = masterData.getInstitutionName();
                    parameterMap.put("institution", institution);
                }else{
                    parameterMap.put("institution", "--");
                }

                if (masterData.getName()!=null && !masterData.getName().isEmpty()){
                    String name = masterData.getName();
                    parameterMap.put("name", name);
                }else{
                    parameterMap.put("name", "--");
                }

                if (masterData.getAge()!=null && !masterData.getAge().isEmpty()){
                    String age = masterData.getAge();
                    parameterMap.put("age", age);
                }else{
                    parameterMap.put("age", "--");
                }

                if (masterData.getGender()!=null && !masterData.getGender().isEmpty()){
                    String gender = masterData.getGender();
                    parameterMap.put("gender", gender);
                }else{
                    parameterMap.put("gender", "--");
                }

                if (masterData.getNic()!=null && !masterData.getNic().isEmpty()){
                    String nic = masterData.getNic();
                    parameterMap.put("nic", nic);
                }else{
                    parameterMap.put("nic", "--");
                }

                if (masterData.getContactNumber()!=null && !masterData.getContactNumber().isEmpty()){
                    String contactNumber = masterData.getContactNumber();
                    parameterMap.put("contactNumber", contactNumber);
                }else{
                    parameterMap.put("contactNumber", "--");
                }

                if (masterData.getAddress()!=null && !masterData.getAddress().isEmpty()){
                    String address = masterData.getAddress();
                    parameterMap.put("address", address);
                }else{
                    parameterMap.put("address", "--");
                }

                if (masterData.getSerialNumber()!=null && !masterData.getSerialNumber().isEmpty()){
                    String serialNumber = masterData.getSerialNumber();
                    parameterMap.put("serialNumber", serialNumber);
                }else{
                    parameterMap.put("serialNumber", "--");
                }

                if (masterData.getBarcode()!=null && !masterData.getBarcode().isEmpty()){
                    String barcode = masterData.getBarcode();
                    parameterMap.put("barcode", barcode);
                }else{
                    parameterMap.put("barcode", "--");
                }

                if (masterData.getCt_target1()!=null && !masterData.getCt_target1().isEmpty()){
                    String ct_target1 = masterData.getCt_target1();
                    parameterMap.put("ct_target1", ct_target1);
                }else{
                    parameterMap.put("ct_target1", "--");
                }

                if (masterData.getCt_target2()!=null && !masterData.getCt_target2().isEmpty()){
                    String ct_target2 = masterData.getCt_target2();
                    parameterMap.put("ct_target2", ct_target2);
                }else{
                    parameterMap.put("ct_target2", "--");
                }

                if (masterData.getResultDescription() != null && !masterData.getResultDescription().isEmpty()) {
                    String result = masterData.getResultDescription();
                    if (result.equals(ResultVarList.RESULT_DETECTED)) {
                        parameterMap.put("fontColor", "#F7231B");
                    } else {
                        parameterMap.put("fontColor", "#030303");
                    }
                }else{
                    parameterMap.put("fontColor", "#030303");
                }

                var imageFile = new ClassPathResource("reports/finalReport/finalreportheader.png");
                Image image = ImageIO.read(imageFile.getInputStream());

                parameterMap.put("testMethod", "Real Time RT-PCR");
                parameterMap.put("Logo", image);
                parameterMap.put("result", masterData.getResultDescription());
                //set parameters to map end

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap,new JREmptyDataSource(1));

                response.setContentType("application/pdf");
                response.setHeader("Content-disposition", "attachment; filename=MasterData-Report-individual.pdf");
                outputStream = response.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

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

    @AccessControl(sectionCode = SectionVarList.SECTION_REPORT_EXPLORER, pageCode = PageVarList.REPORT_GENERATION)
    @PostMapping(value = "/downloadMasterDataPdf", headers = {"content-type=application/json"})
    public @ResponseBody void getAllMasterDataPDF(@RequestBody MasterDataInputBeen masterDataInputBeen, HttpServletResponse response) {
        logger.info("[" + sessionBean.getSessionid() + "]  MASTER DATA REPORT PDF");
        OutputStream outputStream = null;

        try {

            //set search parameters
            masterDataInputBeen.displayLength = (int)masterDataReportService.getCount(masterDataInputBeen);
            masterDataInputBeen.displayStart = 0;

            List<MasterData> masterDataList = masterDataReportService.getMasterDataSearchResultList(masterDataInputBeen);
            if (!masterDataList.isEmpty()) {
                File file = ResourceUtils.getFile("classpath:reports/finalReport/finalreportPdf.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(masterDataList);

                Map<String, Object> parameterMap = new HashMap<>();
                //set parameters to map
                parameterMap.put("consultantName", "Dr. (Mrs) R.N.D. De Silva");
                parameterMap.put("consultantDes", "M.B.B.S., PG Dip. Med.Micro., MD\n" +
                        "Consultant Microbiologist");

                if (masterDataInputBeen.getReceivedDate()!=null && !masterDataInputBeen.getReceivedDate().toString().isEmpty()){
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    String date = simpleDateFormat.format(masterDataInputBeen.getReceivedDate());
                    parameterMap.put("receivedDate", date);
                }else{
                    parameterMap.put("receivedDate", "--");
                }

                if (masterDataInputBeen.getInstitutionCode()!=null && !masterDataInputBeen.getInstitutionCode().isEmpty()){
                    String institutioin = masterDataList.get(0).getInstitutionName();
                    parameterMap.put("institution", institutioin);
                }else{
                    parameterMap.put("institution", "--");
                }

                for (MasterData masterData : masterDataList){
                    if (masterData.getResultDescription()!=null && !masterData.getResultDescription().isEmpty()){
                        String result = masterData.getResultDescription();
                        if (result.equals(ResultVarList.RESULT_DETECTED)){
                            masterData.setFontColor("#F7231B");
                        }else{
                            masterData.setFontColor("#030303");
                        }
                    }else {
                        masterData.setFontColor("#030303");
                    }
                }

                var imageFile = new ClassPathResource("reports/finalReport/finalreportheader.png");
                Image image = ImageIO.read(imageFile.getInputStream());

                parameterMap.put("reportTime", "--");
                parameterMap.put("collectionDate", "--");
                parameterMap.put("testMethod", "Real Time RT-PCR");
                parameterMap.put("Logo", image);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, jrBeanCollectionDataSource);

                response.setContentType("application/pdf");
                response.setHeader("Content-disposition", "attachment; filename=MasterData-Report.pdf");
                outputStream = response.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

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

    @ModelAttribute
    public void getMasterDataBean(Model map) throws Exception {
        MasterDataInputBeen masterDataInputBeen = new MasterDataInputBeen();
        //get status list
        List<Status> statusList = commonRepository.getStatusList(StatusVarList.STATUS_CATEGORY_SAMPLE);
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
