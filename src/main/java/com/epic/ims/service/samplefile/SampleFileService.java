package com.epic.ims.service.samplefile;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.samplefileupload.SampleFileInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.samplefile.SampleFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.security.SHA256Algorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@Scope("prototype")
public class SampleFileService {
    private static Logger logger = LogManager.getLogger(SampleFileService.class);

    @Autowired
    SHA256Algorithm sha256Algorithm;

    @Autowired
    MessageSource messageSource;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    Common common;

    @LogService
    public long getCount(SampleFileInputBean sampleFileBean) {
        return 0;
    }

    @LogService
    public List<SampleFile> getSampleFileSearchResultList(SampleFileInputBean sampleFileInputBean) {
        return null;
    }

    @LogService
    public SampleFile getSampleFileRecord(String referenceNo) {
        return null;
    }

    @LogService
    public String updateSampleFileRecord(SampleFileInputBean sampleFileInputBean, Locale locale) {
        return null;
    }
}
