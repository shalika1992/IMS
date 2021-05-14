package com.epic.ims.service.sampleverifyfile;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.samplefileverification.SampleFileVerificationInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.sampleverifyfile.SampleVerifyFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.sampleverifyfile.SampleVerifyFileRepository;
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

import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Scope("prototype")
public class SampleVerifyFileService {
    private static Logger logger = LogManager.getLogger(SampleVerifyFileService.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    Common common;

    @Autowired
    SampleVerifyFileRepository sampleVerifyFileRepository;

    @Autowired
    CommonVarList commonVarList;

    @LogService
    public long getCount(SampleFileVerificationInputBean sampleFileVerificationInputBean) {
        long count = 0;
        try {
            count = sampleVerifyFileRepository.getDataCount(sampleFileVerificationInputBean);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return count;
    }

    @LogService
    public List<SampleVerifyFile> getSampleVerifySearchResultList(SampleFileVerificationInputBean sampleFileVerificationInputBean) {
        List<SampleVerifyFile> sampleVerifyFileList;
        try {
            sampleVerifyFileList = sampleVerifyFileRepository.getSampleVerifyFileSearchList(sampleFileVerificationInputBean);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return sampleVerifyFileList;
    }

    @LogService
    public SampleVerifyFile getSampleVerifyRecord(String id) {
        SampleVerifyFile sampleVerifyFile;
        try {
            sampleVerifyFile = sampleVerifyFileRepository.getSampleVerifyRecord(id);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return sampleVerifyFile;
    }

    @LogService
    public String verifySampleRecord(SampleFileVerificationInputBean sampleFileVerificationInputBean, Locale locale) {
        String message = "";
        try {
            List<SampleVerifyFile> sampleVerifyFileList = sampleVerifyFileRepository.getSampleVerifyFileSearchList(sampleFileVerificationInputBean);
            //TODO
        } catch (EmptyResultDataAccessException ere) {
            message = MessageVarList.SAMPLERECORD_NORECORD_FOUND;
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    @LogService
    public String rejectSampleRecord(SampleFileVerificationInputBean sampleFileVerificationInputBean, Locale locale) {
        String message = "";
        SampleVerifyFile existingSampleVerifyFile = null;
        try {
            existingSampleVerifyFile = sampleVerifyFileRepository.getSampleVerifyRecord(sampleFileVerificationInputBean.getId());
            if (existingSampleVerifyFile != null) {
                //set the other values to input bean
                Date currentDate = commonRepository.getCurrentDate();
                String lastUpdatedUser = sessionBean.getUsername();

                sampleFileVerificationInputBean.setId(existingSampleVerifyFile.getId() + "");
                sampleFileVerificationInputBean.setReferenceNo(existingSampleVerifyFile.getReferenceNo());
                sampleFileVerificationInputBean.setReceivedDate(existingSampleVerifyFile.getReceivedDate());
                sampleFileVerificationInputBean.setInstitutionCode(existingSampleVerifyFile.getInstitutionCode());
                sampleFileVerificationInputBean.setName(existingSampleVerifyFile.getName());
                sampleFileVerificationInputBean.setAge(existingSampleVerifyFile.getAge());
                sampleFileVerificationInputBean.setGender(existingSampleVerifyFile.getGender());
                sampleFileVerificationInputBean.setSymptomatic(existingSampleVerifyFile.getSymptomatic());
                sampleFileVerificationInputBean.setContactType(existingSampleVerifyFile.getContactType());
                sampleFileVerificationInputBean.setNic(existingSampleVerifyFile.getNic());
                sampleFileVerificationInputBean.setAddress(existingSampleVerifyFile.getAddress());
                sampleFileVerificationInputBean.setResidentDistrict(existingSampleVerifyFile.getResidentDistrict());
                sampleFileVerificationInputBean.setContactNumber(existingSampleVerifyFile.getContactNumber());
                sampleFileVerificationInputBean.setSecondaryContactNumber(existingSampleVerifyFile.getSecondaryContactNumber());
                sampleFileVerificationInputBean.setCreatedTime(currentDate);
                sampleFileVerificationInputBean.setCreatedUser(existingSampleVerifyFile.getCreatedUser());
                sampleFileVerificationInputBean.setLastUpdatedTime(currentDate);
                sampleFileVerificationInputBean.setLastUpdatedUser(lastUpdatedUser);

                //update the sample record
                message = sampleVerifyFileRepository.rejectSampleRecord(sampleFileVerificationInputBean);
            } else {
                message = MessageVarList.SAMPLERECORD_NORECORD_FOUND;
            }
        } catch (EmptyResultDataAccessException ere) {
            message = MessageVarList.SAMPLERECORD_NORECORD_FOUND;
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }
}
