package com.epic.ims.service.resultupdate;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.resultupdate.ResultUpdateInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.plate.Plate;
import com.epic.ims.mapping.result.Result;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.resultupdate.ResultUpdateRepository;
import com.epic.ims.service.profile.ProfileService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
