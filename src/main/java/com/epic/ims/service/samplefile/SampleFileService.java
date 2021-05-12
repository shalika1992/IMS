package com.epic.ims.service.samplefile;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.samplefileupload.SampleData;
import com.epic.ims.bean.samplefileupload.SampleFileInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.samplefile.SampleFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.samplefileupload.SampleFileUploadRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.common.ExcelHelper;
import com.epic.ims.util.security.SHA256Algorithm;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
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

    @Autowired
    SampleFileUploadRepository sampleFileUploadRepository;

    @Autowired
    ExcelHelper excelHelper;

    @Autowired
    CommonVarList commonVarList;

    @LogService
    public long getCount(SampleFileInputBean sampleFileInputBean) {
        long count = 0;
        try {
            count = sampleFileUploadRepository.getDataCount(sampleFileInputBean);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return count;
    }

    @LogService
    public List<SampleFile> getSampleFileSearchResultList(SampleFileInputBean sampleFileInputBean) {
        List<SampleFile> sampleFileList;
        try {
            sampleFileList = sampleFileUploadRepository.getSampleFileSearchList(sampleFileInputBean);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return sampleFileList;
    }

    @LogService
    public SampleFile getSampleFileRecord(String id) {
        SampleFile sampleFile;
        try {
            sampleFile = sampleFileUploadRepository.getSampleRecord(id);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return sampleFile;
    }

    @LogService
    public String updateSampleFileRecord(SampleFileInputBean sampleFileInputBean, Locale locale) {
        String message = "";
        SampleFile existingSampleFileRecord = null;
        try {
            existingSampleFileRecord = sampleFileUploadRepository.getSampleRecord(sampleFileInputBean.getId());
            if (existingSampleFileRecord != null) {
                //check changed values
                String oldValueAsString = this.getSampleRecordAsString(existingSampleFileRecord, true);
                String newValueAsString = this.getSampleRecordAsString(sampleFileInputBean, true);
                //check the old value and new value
                if (oldValueAsString.equals(newValueAsString)) {
                    message = MessageVarList.COMMON_ERROR_NO_VALUE_CHANGE;
                } else {
                    //set the other values to input bean
                    Date currentDate = commonRepository.getCurrentDate();
                    String lastUpdatedUser = sessionBean.getUsername();

                    sampleFileInputBean.setCreatedTime(currentDate);
                    sampleFileInputBean.setLastUpdatedTime(currentDate);
                    sampleFileInputBean.setLastUpdatedUser(lastUpdatedUser);

                    //update the sample record
                    message = sampleFileUploadRepository.updateSampleFileRecord(sampleFileInputBean);
                }
            } else {
                message = MessageVarList.SAMPLE_FILE_RECORD_NORECORD_FOUND;
            }
        } catch (EmptyResultDataAccessException ere) {
            message = MessageVarList.SAMPLE_FILE_RECORD_NORECORD_FOUND;
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    private String getSampleRecordAsString(SampleFile sampleFile, boolean checkChanges) {
        StringBuilder sampleFileRecordStringBuilder = new StringBuilder();
        try {
            if (sampleFile != null) {
                if (sampleFile.getReferenceNo() != null && !sampleFile.getReferenceNo().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFile.getReferenceNo());
                } else {
                    sampleFileRecordStringBuilder.append("error");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFile.getReceivedDate() != null && !sampleFile.getReceivedDate().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFile.getReceivedDate());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFile.getInstitutionCode() != null && !sampleFile.getInstitutionCode().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFile.getInstitutionCode());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFile.getName() != null && !sampleFile.getName().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFile.getName());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFile.getAge() != null && !sampleFile.getAge().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFile.getAge());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFile.getSymptomatic() != null && !sampleFile.getSymptomatic().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFile.getSymptomatic());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFile.getContactType() != null && !sampleFile.getContactType().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFile.getContactType());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFile.getNic() != null && !sampleFile.getNic().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFile.getNic());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFile.getAddress() != null && !sampleFile.getAddress().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFile.getAddress());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFile.getResidentDistrict() != null && !sampleFile.getResidentDistrict().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFile.getResidentDistrict());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFile.getContactNumber() != null && !sampleFile.getContactNumber().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFile.getContactNumber());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFile.getSecondaryContactNumber() != null && !sampleFile.getSecondaryContactNumber().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFile.getSecondaryContactNumber());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                if (!checkChanges) {
                    sampleFileRecordStringBuilder.append("|");
                    if (sampleFile.getCreatedTime() != null) {
                        sampleFileRecordStringBuilder.append(common.formatDateToString(sampleFile.getCreatedTime()));
                    } else {
                        sampleFileRecordStringBuilder.append("--");
                    }

                    sampleFileRecordStringBuilder.append("|");
                    if (sampleFile.getCreatedUser() != null) {
                        sampleFileRecordStringBuilder.append(sampleFile.getCreatedUser());
                    } else {
                        sampleFileRecordStringBuilder.append("--");
                    }

                    sampleFileRecordStringBuilder.append("|");
                    if (sampleFile.getLastUpdatedTime() != null) {
                        sampleFileRecordStringBuilder.append(common.formatDateToString(sampleFile.getLastUpdatedTime()));
                    } else {
                        sampleFileRecordStringBuilder.append("--");
                    }

                    sampleFileRecordStringBuilder.append("|");
                    if (sampleFile.getLastUpdatedUser() != null) {
                        sampleFileRecordStringBuilder.append(sampleFile.getLastUpdatedUser());
                    } else {
                        sampleFileRecordStringBuilder.append("--");
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return sampleFileRecordStringBuilder.toString();
    }

    private String getSampleRecordAsString(SampleFileInputBean sampleFileInputBean, boolean checkChanges) {
        StringBuilder sampleFileRecordStringBuilder = new StringBuilder();
        try {
            if (sampleFileInputBean != null) {
                if (sampleFileInputBean.getReferenceNo() != null && !sampleFileInputBean.getReferenceNo().isEmpty())
                    sampleFileRecordStringBuilder.append(sampleFileInputBean.getReferenceNo());
                else {
                    sampleFileRecordStringBuilder.append("error");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFileInputBean.getReceivedDate() != null && !sampleFileInputBean.getReceivedDate().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFileInputBean.getReceivedDate());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFileInputBean.getInstitutionCode() != null && !sampleFileInputBean.getInstitutionCode().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFileInputBean.getInstitutionCode());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFileInputBean.getName() != null && !sampleFileInputBean.getName().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFileInputBean.getName());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFileInputBean.getAge() != null && !sampleFileInputBean.getAge().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFileInputBean.getAge());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFileInputBean.getSymptomatic() != null && !sampleFileInputBean.getSymptomatic().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFileInputBean.getSymptomatic());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFileInputBean.getContactType() != null && !sampleFileInputBean.getContactType().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFileInputBean.getContactType());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFileInputBean.getNic() != null && !sampleFileInputBean.getNic().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFileInputBean.getNic());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFileInputBean.getAddress() != null && !sampleFileInputBean.getAddress().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFileInputBean.getAddress());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFileInputBean.getResidentDistrict() != null && !sampleFileInputBean.getResidentDistrict().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFileInputBean.getResidentDistrict());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFileInputBean.getContactNumber() != null && !sampleFileInputBean.getContactNumber().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFileInputBean.getContactNumber());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                sampleFileRecordStringBuilder.append("|");
                if (sampleFileInputBean.getSecondaryContactNumber() != null && !sampleFileInputBean.getSecondaryContactNumber().isEmpty()) {
                    sampleFileRecordStringBuilder.append(sampleFileInputBean.getSecondaryContactNumber());
                } else {
                    sampleFileRecordStringBuilder.append("--");
                }

                if (!checkChanges) {
                    sampleFileRecordStringBuilder.append("|");
                    if (sampleFileInputBean.getCreatedTime() != null) {
                        sampleFileRecordStringBuilder.append(common.formatDateToString(sampleFileInputBean.getCreatedTime()));
                    } else {
                        sampleFileRecordStringBuilder.append("--");
                    }

                    sampleFileRecordStringBuilder.append("|");
                    if (sampleFileInputBean.getCreatedUser() != null) {
                        sampleFileRecordStringBuilder.append(sampleFileInputBean.getCreatedUser());
                    } else {
                        sampleFileRecordStringBuilder.append("--");
                    }

                    sampleFileRecordStringBuilder.append("|");
                    if (sampleFileInputBean.getLastUpdatedTime() != null) {
                        sampleFileRecordStringBuilder.append(common.formatDateToString(sampleFileInputBean.getLastUpdatedTime()));
                    } else {
                        sampleFileRecordStringBuilder.append("--");
                    }

                    sampleFileRecordStringBuilder.append("|");
                    if (sampleFileInputBean.getLastUpdatedUser() != null) {
                        sampleFileRecordStringBuilder.append(sampleFileInputBean.getLastUpdatedUser());
                    } else {
                        sampleFileRecordStringBuilder.append("--");
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return sampleFileRecordStringBuilder.toString();
    }

    public String uploadSampleFile(MultipartFile multipartFile, String receivedDate, Locale locale) {
        String message = "";
        try {
            List<SampleData> sampleDataList = excelHelper.excelToSampleData(multipartFile.getInputStream());
            //check the size of the sample file
            if (sampleDataList != null && !sampleDataList.isEmpty() && sampleDataList.size() > 0) {
                if(sampleDataList.size() <= commonVarList.BULKUPLOAD_BATCH_SIZE){
                    message = sampleFileUploadRepository.insertSampleRecordBatch(sampleDataList, receivedDate);
                }else{
                    message = MessageVarList.SAMPLE_FILE_CONTAIN_MAXRECORDS;
                }
            } else {
                message = MessageVarList.SAMPLE_FILE_INVALID_FILE;
            }
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }
}
