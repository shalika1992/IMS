package com.epic.ims.service.sampleverifyfile;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.samplefileverification.DefaultLabCode;
import com.epic.ims.bean.samplefileverification.SampleFileVerificationInputBean;
import com.epic.ims.bean.samplefileverification.SampleIdListBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.sampleverifyfile.SampleVerifyFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.sampleverifyfile.SampleVerifyFileRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.common.ExcelCommon;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
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

    @Autowired
    ExcelCommon excelCommon;

    private final int labReportColumnCount = 10;
    private final int labReportHeaderRowCount = 2;

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
    public String checkInitLabCodeExist(String initLabCode) throws Exception {
        String existInitLabCode = "";
        try {
            existInitLabCode = sampleVerifyFileRepository.checkInitLabCodeExist(initLabCode);
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return existInitLabCode;
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
            message = sampleVerifyFileRepository.validateSampleList(sampleIdListBean);
            if (message.isEmpty()) {
                message = sampleVerifyFileRepository.validateSample(sampleIdListBean);
            }
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    @LogService
    public String invalidateSample(SampleIdListBean sampleIdListBean) {
        String message = "";
        try {
            message = sampleVerifyFileRepository.invalidateSample(sampleIdListBean);
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    @LogService
    public String notFoundSample(SampleIdListBean sampleIdListBean) {
        String message = "";
        try {
            message = sampleVerifyFileRepository.notFoundSample(sampleIdListBean);
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    @LogService
    public DefaultLabCode generateDefaultLabCode() throws Exception {
        DefaultLabCode defaultLabCode = new DefaultLabCode();
        try {
            defaultLabCode = sampleVerifyFileRepository.generateDefaultLabCode();
        } catch (Exception e) {
            throw e;
        }
        return defaultLabCode;
    }

    @LogService
    public Object generateLabCodeExcelReport(HttpServletRequest httpServletRequest, SampleFileVerificationInputBean sampleFileVerificationInputBean) throws Exception {
        DecimalFormat df = new DecimalFormat("00000000");
        Object returnObject = null;
        long count = 0;
        try {
            String initialLabCode = sampleFileVerificationInputBean.getInitialLabCode();
            //check the temporary file path and create file path
            String directory = httpServletRequest.getServletContext().getInitParameter("tmpreportpath");
            File file = new File(directory);
            if (file.exists()) {
                FileUtils.deleteDirectory(file);
            }
            //get count
            count = sampleVerifyFileRepository.getDataCountEmptyLabCode(sampleFileVerificationInputBean);
            if (count > 0) {
                List<SampleVerifyFile> sampleVerifyFileList = sampleVerifyFileRepository.getSampleVerifyFileLabReportSearchList(sampleFileVerificationInputBean);
                for (SampleVerifyFile sampleVerifyFile : sampleVerifyFileList) {
                    sampleVerifyFile.setBarcode(initialLabCode);
                    String value = initialLabCode.substring(1, initialLabCode.length());
                    initialLabCode = initialLabCode.substring(0, 1) + df.format(Integer.parseInt(value) + 1);
                }

                String message = sampleVerifyFileRepository.updateSampleVerifyBatch(sampleVerifyFileList);
                if (message.isEmpty()) {
                    long maxRow = Long.parseLong(httpServletRequest.getServletContext().getInitParameter("numberofrowsperexcel"));
                    SXSSFWorkbook workbook = this.createExcelTopSection();
                    Sheet sheet = workbook.getSheetAt(0);

                    int fileCount = 0;
                    int currRow = labReportHeaderRowCount;
                    currRow = this.createExcelTableHeaderSection(workbook, currRow);

                    int selectRow = Integer.parseInt(httpServletRequest.getServletContext().getInitParameter("numberofselectrows"));
                    long numberOfTimes = count / selectRow;
                    if ((count % selectRow) > 0) {
                        numberOfTimes += 1;
                    }

                    int from = 0;
                    int listrownumber = 1;
                    for (int i = 0; i < numberOfTimes; i++) {
                        if (sampleVerifyFileList.size() > 0) {
                            for (SampleVerifyFile sampleVerifyFile : sampleVerifyFileList) {
                                if (currRow + 1 > maxRow) {
                                    fileCount++;
                                    this.writeTemporaryFile(workbook, fileCount, directory);
                                    workbook = this.createExcelTopSection();
                                    sheet = workbook.getSheetAt(0);
                                    currRow = labReportHeaderRowCount;
                                    this.createExcelTableHeaderSection(workbook, currRow);
                                }
                                currRow = this.createExcelTableBodySection(workbook, sampleVerifyFile, currRow, listrownumber);
                                listrownumber++;
                                if (currRow % 100 == 0) {
                                    // retain 100 last rows and flush all others
                                    ((SXSSFSheet) sheet).flushRows(100);
                                }
                            }
                        }
                        from = from + selectRow;
                    }

                    Date createdTime = commonRepository.getCurrentDate();
                    this.createExcelBotomSection(workbook, currRow, count, createdTime);
                    for (int i = 0; i < labReportColumnCount; i++) {
                        //to auto size all column in the sheet
                        sheet.autoSizeColumn(i);
                    }
                    returnObject = workbook;
                }
            }else{
                returnObject = new String("samples not available");
            }
        } catch (Exception e) {
            throw e;
        }
        return returnObject;
    }

    private SXSSFWorkbook createExcelTopSection() throws Exception {
        SXSSFWorkbook workbook = null;
        try {
            workbook = new SXSSFWorkbook(-1);
            Sheet sheet = workbook.createSheet("Lab_Report");

            CellStyle fontBoldedUnderlinedCell = excelCommon.getFontBoldedUnderlinedCell(workbook);

            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("Sample Data Report");
            cell.setCellStyle(fontBoldedUnderlinedCell);
        } catch (Exception e) {
            throw e;
        }
        return workbook;
    }

    private int createExcelTableHeaderSection(SXSSFWorkbook workbook, int currrow) throws Exception {
        try {
            CellStyle columnHeaderCell = ExcelCommon.getColumnHeadeCell(workbook);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.createRow(currrow++);

            Cell cell = row.createCell(0);
            cell.setCellValue("No");
            cell.setCellStyle(columnHeaderCell);

            cell = row.createCell(1);
            cell.setCellValue("Lab No");
            cell.setCellStyle(columnHeaderCell);

            cell = row.createCell(2);
            cell.setCellValue("Serial No");
            cell.setCellStyle(columnHeaderCell);

            cell = row.createCell(3);
            cell.setCellValue("Institution Code");
            cell.setCellStyle(columnHeaderCell);

            cell = row.createCell(4);
            cell.setCellValue("Name");
            cell.setCellStyle(columnHeaderCell);

            cell = row.createCell(5);
            cell.setCellValue("Nic");
            cell.setCellStyle(columnHeaderCell);

            cell = row.createCell(6);
            cell.setCellValue("District");
            cell.setCellStyle(columnHeaderCell);

            cell = row.createCell(7);
            cell.setCellValue("Contact No");
            cell.setCellStyle(columnHeaderCell);

            cell = row.createCell(8);
            cell.setCellValue("Received Date");
            cell.setCellStyle(columnHeaderCell);

            cell = row.createCell(9);
            cell.setCellValue("Ward");
            cell.setCellStyle(columnHeaderCell);
        } catch (Exception e) {
            throw e;
        }
        return currrow;
    }

    private void writeTemporaryFile(SXSSFWorkbook workbook, int fileCount, String directory) throws Exception {
        File file;
        FileOutputStream outputStream = null;
        try {
            file = new File(directory);
            if (!file.exists()) {
                file.mkdirs();
            }

            if (fileCount > 0) {
                file = new File(directory + File.separator + "LabReport_" + fileCount + ".xlsx");
            } else {
                file = new File(directory + File.separator + "LabReport.xlsx");
            }
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);

        } catch (IOException e) {
            throw e;
        } finally {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    private int createExcelTableBodySection(SXSSFWorkbook workbook, SampleVerifyFile sampleVerifyFile, int currrow, int rownumber) throws Exception {
        try {
            Sheet sheet = workbook.getSheetAt(0);
            CellStyle rowColumnCell = ExcelCommon.getRowColumnCell(workbook);
            Row row = sheet.createRow(currrow++);

            CellStyle style = workbook.createCellStyle();
            style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN);

            Cell cell = row.createCell(0);
            cell.setCellValue(rownumber);
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(sampleVerifyFile.getBarcode());
            cell.setCellStyle(rowColumnCell);

            cell = row.createCell(2);
            cell.setCellValue(sampleVerifyFile.getReferenceNo());
            cell.setCellStyle(rowColumnCell);

            cell = row.createCell(3);
            cell.setCellValue(sampleVerifyFile.getInstitutionCode());
            cell.setCellStyle(rowColumnCell);

            cell = row.createCell(4);
            cell.setCellValue(sampleVerifyFile.getName());
            cell.setCellStyle(rowColumnCell);

            cell = row.createCell(5);
            cell.setCellValue(sampleVerifyFile.getNic());
            cell.setCellStyle(rowColumnCell);

            cell = row.createCell(6);
            cell.setCellValue(sampleVerifyFile.getResidentDistrict());
            cell.setCellStyle(rowColumnCell);

            cell = row.createCell(7);
            cell.setCellValue(sampleVerifyFile.getContactNumber());
            cell.setCellStyle(rowColumnCell);

            cell = row.createCell(8);
            cell.setCellValue(sampleVerifyFile.getReceivedDate());
            cell.setCellStyle(rowColumnCell);

            cell = row.createCell(9);
            cell.setCellValue(sampleVerifyFile.getWard());
            cell.setCellStyle(rowColumnCell);
        } catch (Exception e) {
            throw e;
        }
        return currrow;
    }

    private void createExcelBotomSection(SXSSFWorkbook workbook, int currrow, long count, Date date) throws Exception {
        try {
            CellStyle fontBoldedCell = ExcelCommon.getFontBoldedCell(workbook);
            Sheet sheet = workbook.getSheetAt(0);

            currrow++;
            Row row = sheet.createRow(currrow++);
            Cell cell = row.createCell(0);
            cell.setCellValue("Summary");
            cell.setCellStyle(fontBoldedCell);

            row = sheet.createRow(currrow++);
            cell = row.createCell(0);
            cell.setCellValue("Report Created Time");
            cell = row.createCell(1);

            if (date != null && !date.toString().isEmpty()) {
                cell.setCellValue(date.toString().substring(0, 19));
            } else {
                cell.setCellValue("--");
            }
            cell.setCellStyle(ExcelCommon.getAligneCell(workbook, null, XSSFCellStyle.ALIGN_RIGHT));
        } catch (Exception e) {
            throw e;
        }
    }
}
