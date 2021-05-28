package com.epic.ims.service.rejectedsample;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.rejectedsample.RejectedSampleDataInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.rejectedsampledata.RejectedSampleData;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.rejectedsample.RejectedSampleRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@Scope("prototype")
public class RejectedSampleService {
    private static Logger logger = LogManager.getLogger(RejectedSampleService.class);

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
    RejectedSampleRepository rejectedSampleRepository;

    @Autowired
    RejectedSampleData rejectedSampleData;

    @LogService
    public long getCount(RejectedSampleDataInputBean rejectedSampleDataInputBean) throws Exception {
        long count = 0;
        try {
            count = rejectedSampleRepository.getCount(rejectedSampleDataInputBean);
        } catch (Exception exception) {
            throw exception;
        }
        return count;
    }


    @LogService
    public List<RejectedSampleData> getRejectedSampleSearchResultList(RejectedSampleDataInputBean rejectedSampleDataInputBean) throws Exception{
        List<RejectedSampleData> rejectedSampleDataList;
        try {
            rejectedSampleDataList = rejectedSampleRepository.getRejectedSampleSearchList(rejectedSampleDataInputBean);
        } catch (Exception exception) {
            throw exception;
        }
        return rejectedSampleDataList;
    }

    @LogService
    public List<RejectedSampleData> getRejectedSampleSearchResultListForReport(RejectedSampleDataInputBean rejectedSampleDataInputBean) throws Exception{
        List<RejectedSampleData> rejectedSampleDataList;
        try {
            rejectedSampleDataList = rejectedSampleRepository.getRejectedSampleSearchResultListForReport(rejectedSampleDataInputBean);
        } catch (Exception exception) {
            throw exception;
        }
        return rejectedSampleDataList;
    }
}
