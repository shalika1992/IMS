package com.epic.ims.service.plateassign;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.plate.DefaultBean;
import com.epic.ims.bean.plate.PlateBean;
import com.epic.ims.bean.plate.SwapBean;
import com.epic.ims.bean.session.SessionBean;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String MergeBlockPlate(PlateBean plateBean) {
        String message = "";
        try {
            message = plateAssignRepository.MergeBlockPlate(plateBean);
            if (message.isEmpty()) {
                //defaultPlateMap = plateAssignRepository.getDefaultPlateList();
            }
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return message;
    }
}
