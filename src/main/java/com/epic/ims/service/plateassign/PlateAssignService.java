package com.epic.ims.service.plateassign;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.plate.DefaultBean;
import com.epic.ims.bean.plate.PlateDeleteBean;
import com.epic.ims.bean.plate.PoolBean;
import com.epic.ims.bean.plate.SwapBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.mastertemp.MasterTemp;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.plateassign.PlateAssignRepository;
import com.epic.ims.service.samplefile.SampleFileService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class PlateAssignService {
    private static Logger logger = LogManager.getLogger(SampleFileService.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    Common common;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    PlateAssignRepository plateAssignRepository;

    private final String MASTERFILE_NAME = "MASTERFILE-XXXXX.pdf";
    private final String MASTERZIPFILE_NAME = "MASTERZIPFILE.zip";

    @LogService
    public Map<Integer, List<DefaultBean>> getDefaultPlate(String receivedDate) throws Exception {
        Map<Integer, List<DefaultBean>> defaultPlateMap = new HashMap<>();
        try {
            String message = plateAssignRepository.createDefaultPlateList(receivedDate);
            if (message.isEmpty()) {
                defaultPlateMap = plateAssignRepository.getDefaultPlateList();
            }
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return defaultPlateMap;
    }

    @LogService
    public Map<Integer, List<DefaultBean>> swapBlockPlate(SwapBean swapBean) {
        Map<Integer, List<DefaultBean>> plateMap = new HashMap<>();
        try {
            String message = plateAssignRepository.swapBlockPlate(swapBean);
            if (message.isEmpty()) {
                plateMap = plateAssignRepository.getDefaultPlateList();
            }
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return plateMap;
    }

    @LogService
    public Map<Integer, List<DefaultBean>> MergeBlockPlate(PoolBean poolBean) {
        Map<Integer, List<DefaultBean>> plateMap = new HashMap<>();
        try {
            String message = plateAssignRepository.MergeBlockPlate(poolBean);
            if (message.isEmpty()) {
                plateMap = plateAssignRepository.getDefaultPlateList();
            }
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return plateMap;
    }

    @LogService
    public Map<Integer, List<DefaultBean>> deletePlate(PlateDeleteBean plateDeleteBean) {
        Map<Integer, List<DefaultBean>> plateMap = new HashMap<>();
        try {
            String message = plateAssignRepository.deletePlate(plateDeleteBean);
            if (message.isEmpty()) {
                plateMap = plateAssignRepository.getDefaultPlateList();
            }
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return plateMap;
    }

    @LogService
    public String getFilePathList() throws Exception {
        String zipFilePath = "";
        List<String> filePathList = new ArrayList<>();
        try {
            String message = "";
            String currentDate = commonRepository.getCurrentDateAsString();
            List<String> plateNumberList = plateAssignRepository.getPlateNumberList();
            //validate the plate number list
            if (plateNumberList != null && !plateNumberList.isEmpty() && plateNumberList.size() > 0) {
                for (int i = 0; i < plateNumberList.size(); i++) {
                    String plateId = plateNumberList.get(i);
                    List<MasterTemp> masterTempList = plateAssignRepository.getMasterTempList(plateId);
                    //validate the master temp list
                    if (masterTempList != null && !masterTempList.isEmpty() && masterTempList.size() > 0) {
                        //insert to plate table
                        //this is the correct plate id for plate creation
                        String masterTablePlateId = plateAssignRepository.getMaxPlateId(currentDate);
                        //create the plate
                        message = plateAssignRepository.createPlate(masterTablePlateId);
                        if (message.isEmpty()) {
                            //insert the master batch
                            message = plateAssignRepository.insertMasterBatch(masterTempList, masterTablePlateId);
                            if (message.isEmpty()) {
                                //update the status in sample table
                                List<String> sampleIdList = masterTempList.stream().map(m -> m.getSampleId()).collect(Collectors.toList());
                                sampleIdList = sampleIdList.stream().filter(s -> (Objects.nonNull(s) && !s.isEmpty())).collect(Collectors.toList());
                                message = plateAssignRepository.updateSampleDataList(sampleIdList);
                                if (message.isEmpty()) {
                                    //create the master file in machine location
                                    String filePath = this.createMasterFile(masterTempList, currentDate, masterTablePlateId);
                                    filePathList.add(filePath);
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }

                if (message.isEmpty()) {
                    plateAssignRepository.deleteAllFromMasterTempTable();
                    //zip the file list
                    zipFilePath = this.createZipFile(filePathList, currentDate);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return zipFilePath;
    }

    private String createMasterFile(List<MasterTemp> masterTempList, String currentDate, String masterTablePlateId) throws Exception {
        String filePath = "";
        try {
            String masterFileJasperPath = this.getJasperPath();
            //get the copy of master temp list
            List<MasterTemp> masterTemps = masterTempList.stream().collect(Collectors.toList());
            //update the master temp list new plate id
            masterTemps.stream().forEach(m -> m.setPlateId(masterTablePlateId));
            masterTemps.stream().filter(m -> m.getIsPool().equals(commonVarList.PLATE_POOLCODE_YES)).forEach(m -> m.setIsPool(commonVarList.PLATE_POOLDESCRIPTION_YES));
            masterTemps.stream().filter(m -> m.getIsPool().equals(commonVarList.PLATE_POOLCODE_NO)).forEach(m -> m.setIsPool(commonVarList.PLATE_POOLDESCRIPTION_NO));
            //put values to parameter map
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("date", currentDate);
            parameterMap.put("plateno", masterTablePlateId);
            //initialize file path
            String folderPath = this.getFolderPath(currentDate);
            filePath = folderPath + File.separator + MASTERFILE_NAME.replace("XXXXX", currentDate + "-" + "Plate" + "-" + masterTablePlateId);
            String printFileName = JasperFillManager.fillReportToFile(masterFileJasperPath, parameterMap, new JRBeanCollectionDataSource(masterTempList));
            //create the pdf location
            if (Objects.nonNull(printFileName) && !printFileName.isEmpty()) {
                JasperExportManager.exportReportToPdfFile(printFileName, filePath);
            }
        } catch (Exception e) {
            throw e;
        }
        return filePath;
    }

    private String getJasperPath() {
        String filePath = "";
        try {
            if (SystemUtils.IS_OS_LINUX) {
                filePath = commonVarList.MASTERFILE_LINUX_JASPER_FILEPATH;
            } else if (SystemUtils.IS_OS_WINDOWS) {
                filePath = commonVarList.MASTERFILE_WINDOWS_JASPER_FILEPATH;
            } else {
                filePath = commonVarList.MASTERFILE_WINDOWS_JASPER_FILEPATH;
            }
        } catch (Exception e) {
            throw e;
        }
        return filePath;
    }

    private String createZipFile(List<String> filePathList, String currentDate) throws Exception {
        String zipFilePath = "";
        try {
            //get base folder path
            String baseFolderPath = this.getBaseFolderPath();
            String folderPath = baseFolderPath + File.separator + currentDate;
            //set value to zip file path
            zipFilePath = folderPath + File.separator + MASTERZIPFILE_NAME;
            //delete the file if already exists in path
            common.deleteFile(zipFilePath);
            //create zip file
            common.zipFiles(filePathList, zipFilePath);
        } catch (Exception e) {
            throw e;
        }
        return zipFilePath;
    }

    private String getFolderPath(String currentDate) throws Exception {
        String folderPath = "";
        String baseFolderPath = "";
        try {
            //get base folder path
            baseFolderPath = this.getBaseFolderPath();
            folderPath = baseFolderPath + File.separator + currentDate;
            //create folder to save account open pdf
            this.createPdfFolder(folderPath);
        } catch (Exception e) {
            throw e;
        }
        return folderPath;
    }

    public String getBaseFolderPath() {
        String baseFolderPath = "";
        try {
            if (SystemUtils.IS_OS_LINUX) {
                baseFolderPath = commonVarList.MASTERFILE_LINUX_FILEPATH;
            } else if (SystemUtils.IS_OS_WINDOWS) {
                baseFolderPath = commonVarList.MASTERFILE_WINDOWS_FILEPATH;
            } else {
                baseFolderPath = commonVarList.MASTERFILE_WINDOWS_FILEPATH;
            }
        } catch (Exception e) {
            throw e;
        }
        return baseFolderPath;
    }

    public void createPdfFolder(String folderPath) throws Exception {
        try {
            File pdfFile = new File(folderPath);
            if (!pdfFile.exists()) {
                if (!pdfFile.isDirectory()) {
                    pdfFile.mkdirs();
                    common.createFolderPath(folderPath);
                }
            }
        } catch (IOException e) {
            throw e;
        }
    }
}
