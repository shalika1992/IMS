package com.epic.ims.service.sampleverifyfile;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.institutionmgt.InstitutionInputBean;
import com.epic.ims.bean.samplefileverification.SampleFileVerificationInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.institution.Institution;
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
    public SampleVerifyFile getSampleVerifyRecord(String id) throws Exception {
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

    @LogService
    public String validateSample(SampleFileVerificationInputBean sampleFileVerificationInputBean) {
        String message = "";
        SampleVerifyFile existingSample = null;
        try {
            existingSample = sampleVerifyFileRepository.getSampleVerifyRecord(sampleFileVerificationInputBean.getId());
            System.out.println("ExistingSample: "+existingSample);
            if (existingSample != null) {
                //check changed values
                String oldValueAsString = this.getSampleAsString(existingSample, true);
                String newValueAsString = this.getSampleAsString(existingSample, true);
                //check the old value and new value
                if (oldValueAsString.equals(newValueAsString)) {
                    message = MessageVarList.COMMON_ERROR_NO_VALUE_CHANGE;
                } else {
                    //set the other values to input bean
                    Date currentDate = commonRepository.getCurrentDate();
                    String lastUpdatedUser = sessionBean.getUsername();

                    sampleFileVerificationInputBean.setCreatedTime(currentDate);
                    sampleFileVerificationInputBean.setLastUpdatedTime(currentDate);
                    sampleFileVerificationInputBean.setLastUpdatedUser(lastUpdatedUser);

                    message = sampleVerifyFileRepository.validateSample(sampleFileVerificationInputBean);
                    System.out.println("RepoFirstElse: "+message);

                }
            } else {
                message = MessageVarList.INSTITUTION_MGT_NORECORD_FOUND;
                System.out.println("RepoElse: "+message);
            }
        } catch (EmptyResultDataAccessException ere) {
            message = MessageVarList.INSTITUTION_MGT_NORECORD_FOUND;
            System.out.println("RepoCatch: "+message);

        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        System.out.println("Repo: "+message);
        return message;
    }
}
