package com.epic.ims.repository.sampleverifyfile;

import com.epic.ims.bean.samplefileverification.SampleFileVerificationInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.sampleverifyfile.SampleVerifyFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Scope("prototype")
public class SampleVerifyFileRepository {

    @Autowired
    SessionBean sessionBean;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    Common common;

    public long getDataCount(SampleFileVerificationInputBean sampleFileVerificationInputBean) {
        return 0;
    }

    public List<SampleVerifyFile> getSampleVerifyFileSearchList(SampleFileVerificationInputBean sampleFileVerificationInputBean) {
        return null;
    }

    public SampleVerifyFile getSampleVerifyRecord(String id) {
        return null;
    }

    public String rejectSampleRecord(SampleFileVerificationInputBean sampleFileVerificationInputBean) {
        return null;
    }
}
