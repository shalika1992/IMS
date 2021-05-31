package com.epic.ims.service.resultupdate;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.resultupdate.ResultIdListBean;
import com.epic.ims.bean.resultupdate.ResultUpdateInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.plate.Plate;
import com.epic.ims.mapping.result.Result;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.resultupdate.ResultUpdateRepository;
import com.epic.ims.service.profile.ProfileService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
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
public class ResultUpdateService {
    private static Logger logger = LogManager.getLogger(ProfileService.class);

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
    ResultUpdateRepository resultUpdateRepository;

    @LogService
    public Map<Integer, List<String>> getDefaultResult(String receivedDate, String plateId) {
        Map<Integer, List<String>> defaultResultMap = new HashMap<>();
        try {
            defaultResultMap = resultUpdateRepository.getDefaultResultList(receivedDate, plateId);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return defaultResultMap;
    }

    @LogService
    public long getCount(ResultUpdateInputBean resultUpdateInputBean) {
        long count = 0;
        try {
            count = resultUpdateRepository.getDataCount(resultUpdateInputBean);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return count;
    }

    @LogService
    public List<Result> getResultUpdateSearchResultList(ResultUpdateInputBean resultUpdateInputBean) {
        List<Result> resultList;
        try {
            resultList = resultUpdateRepository.getResultUpdateSearchList(resultUpdateInputBean);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return resultList;
    }

    @LogService
    public Plate getRecord(String id) {
        Plate plate;
        try {
            plate = resultUpdateRepository.getRecord(id);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return plate;
    }

    @LogService
    public String markAsDetected(ResultIdListBean resultIdListBean) {
        String message = "";
        try {
            message = resultUpdateRepository.markAsDetected(resultIdListBean);
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    @LogService
    public String markAsNotDetected(ResultIdListBean resultIdListBean) {
        String message = "";
        try {
            message = resultUpdateRepository.markAsNotDetected(resultIdListBean);
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    @LogService
    public String markAsRepeated(ResultIdListBean resultIdListBean) {
        String message = "";
        try {
            message = resultUpdateRepository.markAsRepeated(resultIdListBean);
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }
}
