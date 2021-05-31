package com.epic.ims.service.plateassign;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.plate.DefaultBean;
import com.epic.ims.bean.plate.PoolBean;
import com.epic.ims.bean.plate.SwapBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.mastertemp.MasterTemp;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.plateassign.PlateAssignRepository;
import com.epic.ims.service.samplefile.SampleFileService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
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
    public List<String> getFilePathList() throws Exception {
        List<String> filePathList = new ArrayList<>();
        try {
            String message = "";
            List<String> plateNumberList = plateAssignRepository.getPlateNumberList();
            //validate the plate number list
            if (plateNumberList != null && !plateNumberList.isEmpty() && plateNumberList.size() > 0) {
                for (int i = 0; i < plateNumberList.size(); i++) {
                    String plateId = plateNumberList.get(i);
                    List<MasterTemp> masterTempList = plateAssignRepository.getMasterTempList(plateId);
                    //validate the master temp list
                    if (masterTempList != null && !masterTempList.isEmpty() && masterTempList.size() > 0) {
                        //insert to plate table
                        message = plateAssignRepository.createPlate(plateId);
                        if (message.isEmpty()) {
                            //insert the master batch
                            message = plateAssignRepository.insertMasterBatch(masterTempList);
                            if (message.isEmpty()) {
                                //update the status in sample table
                                List<String> sampleIdList = masterTempList.stream().map(m -> m.getSampleId()).collect(Collectors.toList());
                                sampleIdList = sampleIdList.stream().filter(s -> (Objects.nonNull(s) && !s.isEmpty())).collect(Collectors.toList());
                                message = plateAssignRepository.updateSampleDataList(sampleIdList);
                                if (message.isEmpty()) {
                                    //create the master file in machine location
                                    String filePath = this.createMasterFile(masterTempList);
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
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return filePathList;
    }

    private String createMasterFile(List<MasterTemp> masterTempList) throws Exception {
        String filePath = "";
        try {
            //initialize file path
            String folderPath = this.getFolderPath();
        } catch (Exception e) {
            throw e;
        }
        return filePath;
    }

    private String getFolderPath() throws Exception {
        String folderPath = "";
        String baseFolderPath = "";
        String currentDate = "";
        try {
            currentDate = commonRepository.getCurrentDateAsString();
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
            /*if (SystemUtils.IS_OS_LINUX) {
                baseFolderPath = commonVarList.LINUX_FILEPATH;
            } else if (SystemUtils.IS_OS_WINDOWS) {
                baseFolderPath = commonVarList.WINDOWS_FILEPATH;
            } else {
                baseFolderPath = commonVarList.WINDOWS_FILEPATH;
            }*/
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
