package com.epic.ims.validation.institution;

import com.epic.ims.bean.institutionmgt.InstitutionInputBean;
import com.epic.ims.util.common.ResponseBean;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import com.epic.ims.util.varlist.TaskVarList;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class InstitutionBulkValidation {

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    MessageSource messageSource;

    @Autowired
    InstitutionBeanValidator institutionBeanValidator;

    public ResponseBean validateInstitutionBulk(InstitutionInputBean institutionInputBean, Locale locale) throws IOException {
        MultipartFile file = institutionInputBean.getInstitutionBulk();
        Workbook workbook;
        ResponseBean responseBean;

        if (file != null && !file.isEmpty()) {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            InputStream excelFile = file.getInputStream();
            try {

                assert extension != null;
                if (extension.equals("xls")) {
                    workbook = new HSSFWorkbook(excelFile);
                    responseBean = validateExcelFile(workbook, locale);
                } else if (extension.equals("xlsx")) {
                    workbook = new XSSFWorkbook(excelFile);
                    responseBean = validateExcelFile(workbook, locale);
                } else {
                    responseBean = new ResponseBean(false, null, MessageVarList.INSTITUTION_MGT_INVALID_FILE);
                }
            } catch (Exception exception) {
                responseBean = new ResponseBean(false, null, MessageVarList.COMMON_ERROR_PROCESS);
            } finally {
                excelFile.close();
            }
        } else {
            responseBean = new ResponseBean(false, null, MessageVarList.INSTITUTION_MGT_EMPTY_FILE);
        }

        return responseBean;
    }

    private ResponseBean validateExcelFile(Workbook workbook, Locale locale) {
        final String columnOrder = "|Institution Code|Institution Name|Address|Contact Number|";
        Sheet sheet = workbook.getSheetAt(0);
        Row titleRow = sheet.getRow(0);
        int count = sheet.getPhysicalNumberOfRows();
        ResponseBean responseBean = new ResponseBean();
        HashSet<String> uniqueInstitutionCodeSet = new HashSet<>();

        try {

            if (count > 1) {
                if (columnOrder.equals(this.getColumnOrder(titleRow))) {

                    for (Row row : sheet) {
                        if (row.getRowNum() == 0) {
                            continue;
                        } else {
                            String institutionCode = null;
                            String institutionName = null;
                            String address = null;
                            String contactNumber = null;
                            int rowNumber = row.getRowNum();

                            for (Cell cell : row) {
                                if (cell.getColumnIndex() == 0) {
                                    institutionCode = cell.getStringCellValue();
                                } else if (cell.getColumnIndex() == 1) {
                                    institutionName = cell.getStringCellValue();
                                } else if (cell.getColumnIndex() == 2) {
                                    address = cell.getStringCellValue();
                                } else if (cell.getColumnIndex() == 3) {
                                    if (cell.getCellType() == 0) {
                                        long contactNumberInteger = (long) cell.getNumericCellValue();
                                        contactNumber = String.valueOf(contactNumberInteger);

                                        if (contactNumber.length() > 10) {
                                            contactNumber = "invalidData";
                                        }

                                    } else {
                                        contactNumber = "invalidData";
                                    }
                                }
                            }

                            InstitutionInputBean institutionInputBean = new InstitutionInputBean();

                            institutionInputBean.setInstitutionCode(institutionCode);
                            institutionInputBean.setInstitutionName(institutionName);
                            institutionInputBean.setAddress(address);
                            institutionInputBean.setContactNumber(contactNumber);
                            institutionInputBean.setUserTask(TaskVarList.ADD_BULK_TASK);
                            institutionInputBean.setRowNumber(rowNumber);

                            BindingResult bindingResult = validateInstitutionBean(institutionInputBean);

                            if (bindingResult.hasErrors()) {
                                responseBean = new ResponseBean(false, null, messageSource.getMessage(Objects.requireNonNull(bindingResult.getAllErrors().get(0).getCode()), new Object[]{bindingResult.getAllErrors().get(0).getDefaultMessage()}, Locale.US) + " Row Number: " + ++rowNumber);
                                break;
                            } else {
                                //check for duplicates
                                if (!uniqueInstitutionCodeSet.add(institutionInputBean.getInstitutionCode())){
                                    responseBean = new ResponseBean(false, null, messageSource.getMessage(MessageVarList.INSTITUTION_MGT_DUPLICATE_RECORD, null, locale)+ " Row Number: " + ++rowNumber);
                                    break;
                                }else {
                                    responseBean.setFlag(true);
                                }

                            }
                        }
                    }

                } else {
                    responseBean = new ResponseBean(false, null, messageSource.getMessage(MessageVarList.INSTITUTION_MGT_INVALID_COLUMN_ORDER, null, locale));
                }
            } else {
                responseBean = new ResponseBean(false, null, messageSource.getMessage(MessageVarList.INSTITUTION_MGT_EMPTY_SHEET, null, locale));
            }

        } catch (Exception e) {
            responseBean = new ResponseBean(false, null, messageSource.getMessage(MessageVarList.COMMON_ERROR_PROCESS, null, locale));
        }
        return responseBean;
    }

    private BindingResult validateInstitutionBean(Object o) {
        DataBinder dataBinder = new DataBinder(o);
        dataBinder.setValidator(institutionBeanValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }

    private String getColumnOrder(Row titleRow) {
        StringBuilder columnOrder = new StringBuilder("|");

        for (Cell cell : titleRow) {
            try {
                columnOrder.append(cell.getRichStringCellValue().toString().trim());
            } catch (Exception e) {
                columnOrder.append("--");
            } finally {
                columnOrder.append("|");
            }
        }
        return columnOrder.toString();
    }

}
