package com.epic.ims.service.plateassign;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.plate.PlateBean;
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
    public Map<Integer, List<String>> getDefaultPlate(String receivedDate) {
        Map<Integer, List<String>> defaultPlateMap = new HashMap<>();
        try {
            defaultPlateMap = plateAssignRepository.getDefaultPlateList(receivedDate);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return defaultPlateMap;
    }

    @LogService
    public String swapBlockPlate(PlateBean plateBean) {
        String message = "";
        try {
            message = plateAssignRepository.swapBlockPlate(plateBean);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return message;
    }

    @LogService
    public String MergeBlockPlate(PlateBean plateBean) {
        String message = "";
        try {
            message = plateAssignRepository.MergeBlockPlate(plateBean);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return message;
    }
}
