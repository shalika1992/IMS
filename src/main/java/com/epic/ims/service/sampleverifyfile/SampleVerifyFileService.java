package com.epic.ims.service.sampleverifyfile;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.samplefileverification.SampleFileVerificationInputBean;
import com.epic.ims.bean.samplefileverification.SampleIdListBean;
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

import java.util.List;

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
    public long getCount(SampleFileVerificationInputBean sampleFileVerificationInputBean) throws Exception {
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
    public String validateSample(SampleIdListBean sampleIdListBean) {
        String message = "";
        try {
            message = sampleVerifyFileRepository.validateSample(sampleIdListBean);
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    @LogService
    private String getSampleAsString(SampleVerifyFile sampleVerifyFile, boolean checkChanges) {
        StringBuilder sampleStringBuilder = new StringBuilder();
        try {
            if (sampleVerifyFile != null) {

                if (sampleVerifyFile.getId() != 0) {
                    sampleStringBuilder.append(sampleVerifyFile.getId());
                } else {
                    sampleStringBuilder.append("error");
                }

                sampleStringBuilder.append("|");
                if (sampleVerifyFile.getStatus() != null && !sampleVerifyFile.getStatus().isEmpty()) {
                    sampleStringBuilder.append(sampleVerifyFile.getStatus());
                } else {
                    sampleStringBuilder.append("--");
                }

            }
        } catch (Exception e) {
            throw e;
        }
        return sampleStringBuilder.toString();
    }


}
