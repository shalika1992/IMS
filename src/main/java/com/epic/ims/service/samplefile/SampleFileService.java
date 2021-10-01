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
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Scope("prototype")
public class SampleFileService {
    private static Logger logger = LogManager.getLogger(SampleFileService.class);

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
    public String insertSampleWardEntry(SampleFileInputBean sampleFileInputBean,Boolean isExist, Locale locale) {
        String message = "";
        try {
            //SampleFile existingSampleFile = sampleFileUploadRepository.getSampleWardEntry(sampleFileInputBean);
            if (!isExist) {
                //set the other values to input bean
                String currentDate = commonRepository.getCurrentDateAsString();
                String createdUser = sessionBean.getUsername();

                sampleFileInputBean.setReceivedDate(currentDate);
                sampleFileInputBean.setCreatedUser(createdUser);

                message = sampleFileUploadRepository.insertSampleWardEntry(sampleFileInputBean);
            } else {
                message = MessageVarList.SAMPLERECORD_ALREADY_EXISTS;
            }
        } catch (DuplicateKeyException ex) {
            message = MessageVarList.SAMPLERECORD_ALREADY_EXISTS;
        } catch (Exception exception) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    @LogService
    public String validateMandatoryFields(List<SampleData> sampleDataList, Locale locale) {
        String message = "";
        try {
            int MANDATORY_FIELDCOUNT = 2;
            for (int i = 0; i < sampleDataList.size(); i++) {
                SampleData s = sampleDataList.get(i);
                if (s != null) {
                    int count = 0;
                    if (s.getReferenceNo() == null || s.getReferenceNo().isEmpty()) {
                        count++;
                    }

                    if (s.getMohArea() == null || s.getMohArea().isEmpty()) {
                        count++;
                    }

                    if (s.getName() == null || s.getName().isEmpty()) {
                        count++;
                    }

                    if (s.getNic() == null || s.getNic().isEmpty()) {
                        count++;
                    }

                    if (s.getContactNumber() == null || s.getContactNumber().isEmpty()) {
                        count++;
                    }
                    if (count > MANDATORY_FIELDCOUNT) {
                        message = messageSource.getMessage(MessageVarList.SAMPLERECORD_MANDATORYFILED_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                        message = message + (s.getReferenceNo() != null && !s.getReferenceNo().isEmpty() ? "Reference No - " + s.getReferenceNo() : "");
                        break;
                    }
                } else {
                    message = messageSource.getMessage(MessageVarList.SAMPLERECORD_MANDATORYFILED_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return message;
    }

    @LogService
    public String validateSpecialCharactersFields(List<SampleData> sampleDataList, Locale locale) {
        String message = "";
        try {
            for (int i = 0; i < sampleDataList.size(); i++) {
                SampleData s = sampleDataList.get(i);
                if (s != null) {

                    String mohArea = s.getMohArea();
                    if (mohArea != null && !mohArea.isEmpty()) {
                        if (common.checkSpecialCharacter(mohArea)) {
                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                            break;
                        }
                    }


                    String name = s.getName();
                    if (name != null && !name.isEmpty()) {
                        if (common.checkSpecialCharacter(name)) {
                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                            break;
                        }
                    }

                    String age = s.getAge();
                    if (age != null && !age.isEmpty()) {
                        if (common.checkSpecialCharacter(age)) {
                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                            break;
                        }
                    }

                    String gender = s.getGender();
                    if (gender != null && !gender.isEmpty()) {
                        if (common.checkSpecialCharacter(gender)) {
                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                            break;
                        }
                    }

                    String nic = s.getNic();
                    if (nic != null && !nic.isEmpty()) {
                        if (common.checkSpecialCharacter(nic)) {
                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                            break;
                        }
                    }

                    String contactNumber = s.getContactNumber();
                    if (contactNumber != null && !contactNumber.isEmpty()) {
                        if (common.checkSpecialCharacter(contactNumber)) {
                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                            break;
                        }
                    }

                    String secondaryContactNumber = s.getSecondaryContactNumber();
                    if (secondaryContactNumber != null && !secondaryContactNumber.isEmpty()) {
                        if (common.checkSpecialCharacter(secondaryContactNumber)) {
                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                            break;
                        }
                    }

                    //Symptomatic
                    String symptomatic = s.getSymptomatic();
                    if (symptomatic != null && !symptomatic.isEmpty()) {
                        if (common.checkSpecialCharacter(symptomatic)) {
                            System.out.println("Symptomatic");
                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                            break;
                        }
                    }

                    //Contact Type
                    String contactType = s.getContactType();
                    if (contactType != null && !contactType.isEmpty()) {
                        if (common.checkSpecialCharacter(contactType)) {
                            System.out.println("Contact Type");
                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                            break;
                        }
                    }

                    //Address
                    String address = s.getAddress();
                    if (address != null && !address.isEmpty()) {
                        if (common.checkSpecialCharacterForAddress(address)) {
                            System.out.println("Address");
                            System.out.println(address);
                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                            break;
                        }
                    }

                    //Resident District
                    String residentDistrict = s.getResidentDistrict();
                    if (residentDistrict != null && !residentDistrict.isEmpty()) {
                        if (common.checkSpecialCharacter(residentDistrict)) {
                            System.out.println("Resident District");
                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                            break;
                        }
                    }

                    //Date Validation removed as requested
//                    String date = s.getDate();
//                    if (date != null && !date.isEmpty()) {
//                        if (common.checkSpecialCharacterForDate(date)) {
//                            System.out.println("Date");
//                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
//                            break;
//                        }
//                    }

                    // Reference No.
                    String referenceNo = s.getReferenceNo();
                    if (referenceNo != null && !referenceNo.isEmpty()) {
                        if (common.checkSpecialCharacterOfReferenceNo(referenceNo)) {
                            System.out.println("reference");
                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                            break;
                        }
                    }

                } else {
                    message = messageSource.getMessage(MessageVarList.SAMPLERECORD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Row number - " + (i + 1);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return message;
    }

    public String validateSpecialCharactersFieldsInputs(SampleFileInputBean s, Locale locale) {
        String message = "";
        try {

                if (s != null) {

                    String name = s.getName();
                    String age = s.getAge();
                    String nic = s.getNic();
                    String contactNumber = s.getContactNumber();
                    String secondaryContactNumber = s.getSecondaryContactNumber();
                    String address = s.getAddress();
                    String referenceNo = s.getReferenceNo();

                    if (referenceNo != null && !referenceNo.isEmpty()) {
                        if (common.checkSpecialCharacterOfReferenceNo(referenceNo)) {
                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_ADD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Reference No Field";
                        }
                        else if (name != null && !name.isEmpty()) {
                            if (common.checkSpecialCharacter(name)) {
                                message = messageSource.getMessage(MessageVarList.SAMPLERECORD_ADD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Name Field ";
                            }
                            else if (age != null && !age.isEmpty()) {
                                if (common.checkSpecialCharacter(age)) {
                                    message = messageSource.getMessage(MessageVarList.SAMPLERECORD_ADD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Age Field ";
                                }
                                else if (nic != null && !nic.isEmpty()) {
                                    if (common.checkSpecialCharacter(nic)) {
                                        message = messageSource.getMessage(MessageVarList.SAMPLERECORD_ADD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "NIC Field ";
                                    }
                                    else if (address != null && !address.isEmpty()) {
                                        if (common.checkSpecialCharacterForAddress(address)) {
                                            message = messageSource.getMessage(MessageVarList.SAMPLERECORD_ADD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Address Field ";
                                        }
                                        else if (contactNumber != null && !contactNumber.isEmpty()) {
                                            if (common.checkSpecialCharacter(contactNumber)) {
                                                message = messageSource.getMessage(MessageVarList.SAMPLERECORD_ADD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Contact No Field ";

                                            } else if (secondaryContactNumber != null && !secondaryContactNumber.isEmpty()) {
                                                if (common.checkSpecialCharacter(secondaryContactNumber)) {
                                                    message = messageSource.getMessage(MessageVarList.SAMPLERECORD_ADD_SPECIALCHARACTER_VALIDATIONFAIL, null, locale) + " - " + "Contact No Field 2 ";
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

        } catch (Exception e) {
            throw e;
        }
        return message;
    }

    @LogService
    public String checkDuplicate(List<SampleData> sampleDataFileList, String receivedDate, Locale locale) {
        String message = "";
        try {
            List<SampleData> sampleDataRepositoryList = sampleFileUploadRepository.getExistingSampleDataList(receivedDate);
            if (sampleDataRepositoryList != null && !sampleDataRepositoryList.isEmpty() && sampleDataRepositoryList.size() > 0) {
                List<SampleData> duplicateList = this.getDuplicateList(sampleDataFileList, sampleDataRepositoryList);
                //check the duplicate list
                if (duplicateList != null && !duplicateList.isEmpty() && duplicateList.size() > 0) {
                    SampleData sampleData = duplicateList.get(0);
                    //get the reference no - moh area - date
                    String referenceNo = sampleData.getReferenceNo();
                    String mohArea = sampleData.getMohArea();
                    String date = sampleData.getDate();
                    //set the message
                    message = messageSource.getMessage(MessageVarList.SAMPLERECORD_DUPLICATE_RECORDFOUND, null, locale);
                    message = message + (referenceNo != null && !referenceNo.isEmpty() ? " Reference No - " + referenceNo : "");
                    message = message + (mohArea != null && !mohArea.isEmpty() ? " MOH Area - " + mohArea : "");
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return message;
    }
    //dev
    @LogService
    public boolean checkDuplicateWardEntry(List<SampleFileInputBean> sampleDataFileList, Locale locale) throws Exception {
        String message = "";
        boolean isExist = false;
        String receivedDate = commonRepository.getCurrentDateAsString();
        try {
            List<SampleFileInputBean> sampleDataRepositoryList = sampleFileUploadRepository.getExistingWardSampleDataList(receivedDate);
            if (sampleDataRepositoryList != null && !sampleDataRepositoryList.isEmpty() && sampleDataRepositoryList.size() > 0) {
                List<SampleFileInputBean> duplicateList = this.getDuplicateWardList(sampleDataFileList, sampleDataRepositoryList);
                //check the duplicate list
                if (duplicateList != null && !duplicateList.isEmpty() && duplicateList.size() > 0) {
                    isExist = true;
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return isExist;
    }
    //end
    @LogService
    public String uploadSampleFile(List<SampleData> sampleDataList, String receivedDate, Locale locale) {
        String message = "";
        try {
            //check the size of the sample file
            if (sampleDataList != null && !sampleDataList.isEmpty() && sampleDataList.size() > 0) {
                if (sampleDataList.size() <= commonVarList.BULKUPLOAD_BATCH_SIZE) {
                    message = sampleFileUploadRepository.insertSampleRecordBatch(sampleDataList, receivedDate);
                } else {
                    message = MessageVarList.SAMPLEFILE_CONTAIN_MAXRECORDS;
                }
            } else {
                message = MessageVarList.SAMPLEFILE_INVALID_FILE;
            }
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
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
                message = MessageVarList.SAMPLERECORD_NORECORD_FOUND;
            }
        } catch (EmptyResultDataAccessException ere) {
            message = MessageVarList.SAMPLERECORD_NORECORD_FOUND;
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

    private List<SampleData> getDuplicateList(List<SampleData> sampleDataFileList, List<SampleData> sampleDataRepositoryList) {
        List<SampleData> duplicateList = new ArrayList<>();
        try {
            outerLoop:
            for (int i = 0; i < sampleDataRepositoryList.size(); i++) {
                SampleData sampleData1 = sampleDataRepositoryList.get(i);
                for (int j = 0; j < sampleDataFileList.size(); j++) {
                    SampleData sampleData2 = sampleDataFileList.get(j);
                    if (sampleData1.equals(sampleData2)) {
                        duplicateList.add(sampleData2);
                        break outerLoop;
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return duplicateList;
    }

    private List<SampleFileInputBean> getDuplicateWardList(List<SampleFileInputBean> sampleWardDataFileList, List<SampleFileInputBean> sampleDataRepositoryList) {
        List<SampleFileInputBean> duplicateList = new ArrayList<>();
        try {
            outerLoop:
            for (int i = 0; i < sampleDataRepositoryList.size(); i++) {
                SampleFileInputBean sampleData1 = sampleDataRepositoryList.get(i);
                for (int j = 0; j < sampleWardDataFileList.size(); j++) {
                    SampleFileInputBean sampleData2 = sampleWardDataFileList.get(j);
                    if(sampleData1.getInstitutionCode().equals(sampleData2.getInstitutionCode()) &&
                            sampleData1.getReceivedDate().equals(sampleData2.getReceivedDate()) &&
                            sampleData1.getReferenceNo().equals(sampleData2.getReferenceNo())
                            ) {
                        //sampleData1.getWardNumber().equals(sampleData2.getWardNumber())
                        duplicateList.add(sampleData2);
                        break outerLoop;
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return duplicateList;
    }

}
