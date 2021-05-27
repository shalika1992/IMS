package com.epic.ims.service.reportmgt;

import com.epic.ims.bean.institutionmgt.InstitutionInputBean;
import com.epic.ims.bean.reportmgt.MasterDataInputBeen;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.institution.Institution;
import com.epic.ims.mapping.reportmgt.MasterData;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.institutionmgt.InstitutionRepository;
import com.epic.ims.repository.reportmgt.ReportMgtRepository;
import com.epic.ims.util.common.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("prototype")
public class MasterDataReportService {
    @Autowired
    ReportMgtRepository reportMgtRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    Common common;

    public MasterData getMasterDataRecord(String id) throws Exception {
        MasterData masterData;
        try {
            masterData = reportMgtRepository.getMasterFileRecord(id);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return masterData;
    }

    public long getCount(MasterDataInputBeen masterDataInputBeen) throws Exception {
        long count = 0;

        try {
            count = reportMgtRepository.getCount(masterDataInputBeen);
        } catch (Exception ere) {
            throw ere;
        }

        return count;
    }

    public List<MasterData> getMasterDataSearchResultList(MasterDataInputBeen masterDataInputBeen) {
        List<MasterData> masterDataList;

        try {
            masterDataList = reportMgtRepository.getMasterDataSearchList(masterDataInputBeen);
        } catch (Exception exception) {
            throw exception;
        }

        return masterDataList;
    }
}

