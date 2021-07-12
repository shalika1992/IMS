package com.epic.ims.controller.plateassign;

import com.epic.ims.annotation.accesscontrol.AccessControl;
import com.epic.ims.annotation.logcontroller.LogController;
import com.epic.ims.bean.common.Result;
import com.epic.ims.bean.plate.*;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.mastertemp.MasterTemp;
import com.epic.ims.mapping.plate.Plate;
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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

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

    private static final int BUFFER_SIZE = 4096;

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
    @RequestMapping(value = "/isEligibleToPool", method = RequestMethod.POST)
    public @ResponseBody
    String checkEligibleToPool(@RequestBody Map<String, String[]> mergedPool, HttpServletRequest request, HttpServletResponse response, Locale locale) {
        String poolStatus = "yes";
        try {
            if(!isEligibleToPool(mergedPool)){
                poolStatus = "no";
            }
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return poolStatus;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/mergeBlockPlate", method = RequestMethod.POST)
    public @ResponseBody
    Map<Integer, List<DefaultBean>> postMergeBlockPlate(@RequestBody Map<String, String[]> mergedPool, HttpServletRequest request, HttpServletResponse response, Locale locale) {
        Map<Integer, List<DefaultBean>> defaultPlateMap = new HashMap<>();
        try {
            PoolBean poolBean = convertMergedPoolToBean(mergedPool);
            defaultPlateMap = plateAssignService.MergeBlockPlate(poolBean);
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return defaultPlateMap;
    }

    PoolBean convertMergedPoolToBean(Map<String, String[]> mergedPool) {
        PoolBean poolBean = new PoolBean();
        List<ArrayList<String>> poolList = new ArrayList<>();
        Iterator it = mergedPool.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String[] pools = (String[]) pair.getValue();
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(pools));
            poolList.add(arrayList);
        }
        poolBean.setPoolList(poolList);
        return poolBean;
    }

    private static boolean isEligibleToPool(Map<String, String[]> mergedPool) {
        boolean itr = true;
        for (Map.Entry<String,String[]> entry : mergedPool.entrySet()){
            //System.out.println("Key = " + entry.getKey());

            for (String string : entry.getValue()) {
                //System.out.println("Value = "+string);
                if(string==null || string.equals("")){
                    //System.out.println("Value = empty");
                    itr = false;
                }
            }
            if(itr==false){
                break;
            }
        }
        return itr;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/deletePlate", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Map<Integer, List<DefaultBean>> postDeletePlate(@RequestBody PlateDeleteBean plateDeleteBean, HttpServletRequest request, HttpServletResponse response, Locale locale) {
        Map<Integer, List<DefaultBean>> defaultPlateMap = new HashMap<>();
        try {
            defaultPlateMap = plateAssignService.deletePlate(plateDeleteBean);
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return defaultPlateMap;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/isEligibleToDeleteWell", method = RequestMethod.POST)
    public @ResponseBody
    String checkEligibleToDeleteWell(@RequestBody MasterTemp masterTempBean, HttpServletRequest request, HttpServletResponse response, Locale locale) {
        String deleteStatus = "yes";
        try {
            //get max plate for the date
            Plate  plate = commonRepository.getMaxPlateId(masterTempBean.getReceivedDate());
            //get max well from max plate
            MasterTemp maxTempWell = plateAssignService.getMaxWellLabCodeOfMaxPlate(plate.getId(),masterTempBean.getReceivedDate());
            //if max well == wellToDelete then allow delete
            if(!maxTempWell.getBarcode().equalsIgnoreCase(masterTempBean.getBarcode())){
                deleteStatus = "no";
            }

        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return deleteStatus;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/deleteWell", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Map<Integer, List<DefaultBean>> postDeleteWell(@RequestBody MasterTemp masterTempBean, HttpServletRequest request, HttpServletResponse response, Locale locale) {
        Map<Integer, List<DefaultBean>> defaultPlateMap = new HashMap<>();
        try {
            defaultPlateMap = plateAssignService.deleteWell(masterTempBean.getBarcode());//labcode
        } catch (Exception e) {
            logger.error("Exception  :  ", e);
        }
        return defaultPlateMap;
    }

    @LogController
    @AccessControl(sectionCode = SectionVarList.SECTION_FILE_GENERATION, pageCode = PageVarList.PLATE_ASSIGN)
    @RequestMapping(value = "/createPlate", method = RequestMethod.POST)
    public void createPlate(@ModelAttribute("plate") PlateInputBean plateInputBean, @RequestParam String receiveDate, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.info("[" + sessionBean.getSessionid() + "]  MASTER PLATE CREATION");
        ServletContext context = httpServletRequest.getServletContext();
        OutputStream outputStream = null;
        try {
            String zipFilePath = plateAssignService.getFilePathList(httpServletRequest, receiveDate);
            if (zipFilePath != null && !zipFilePath.isEmpty()) {
                File downloadFile = new File(zipFilePath);
                FileInputStream inputStream = new FileInputStream(downloadFile);

                // get MIME type of the file
                String mimeType = context.getMimeType(zipFilePath);
                if (mimeType == null) {
                    // set to binary type if MIME mapping not found
                    mimeType = "application/octet-stream";
                }
                System.out.println("MIME type: " + mimeType);

                // set content attributes for the response
                httpServletResponse.setContentType(mimeType);
                httpServletResponse.setContentLength((int) downloadFile.length());

                // set headers for the response
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
                httpServletResponse.setHeader(headerKey, headerValue);

                // get output stream of the response
                OutputStream outStream = httpServletResponse.getOutputStream();

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;

                // write bytes read from the input stream into the output stream
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outStream.close();
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
    public void getPlateBean(Model map) throws Exception {
        PlateInputBean plateInputBean = new PlateInputBean();
        //add values to model map
        map.addAttribute("plate", plateInputBean);
    }
}
