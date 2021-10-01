package com.epic.ims.service.plateassign;

import com.epic.ims.annotation.logservice.LogService;
import com.epic.ims.bean.plate.DefaultBean;
import com.epic.ims.bean.plate.PlateDeleteBean;
import com.epic.ims.bean.plate.PoolBean;
import com.epic.ims.bean.plate.SwapBean;
import com.epic.ims.bean.samplefileverification.SampleFileVerificationInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.mastertemp.MasterTemp;
import com.epic.ims.mapping.sampleverifyfile.SampleVerifyFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.plateassign.PlateAssignRepository;
import com.epic.ims.service.samplefile.SampleFileService;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.common.ExcelCommon;
import com.epic.ims.util.varlist.CommonVarList;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class PlateAssignService {
    private static Logger logger = LogManager.getLogger(SampleFileService.class);
    private final int labReportColumnCount = 3;
    private final int labReportHeaderRowCount = 2;

    @Autowired
    MessageSource messageSource;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    Common common;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    PlateAssignRepository plateAssignRepository;

    @Autowired
    ExcelCommon excelCommon;

    private final String MASTERFILE_NAME = "MASTERFILE-XXXXX.pdf";
    private final String MASTERXLFILE_NAME = "MASTERFILE-XXXXX.xlsx";
    private final String MASTERCSVFILE_NAME = "MASTERFILE-XXXXX.csv";
    private final String MASTERZIPFILE_NAME = "MASTERZIPFILE.zip";

    @LogService
    public Map<Integer, List<DefaultBean>> getDefaultPlate(String receivedDate) throws Exception {
        Map<Integer, List<DefaultBean>> defaultPlateMap = new HashMap<>();
        try {
            String message = plateAssignRepository.createDefaultPlateList(receivedDate);
            if (message.isEmpty()) {
                defaultPlateMap = plateAssignRepository.getDefaultPlateList();
            }
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return defaultPlateMap;
    }

    @LogService
    public MasterTemp getMaxWellLabCodeOfMaxPlate(String plateid, String receivedDate) throws Exception {
        MasterTemp masterTemp = new MasterTemp();
        try {
            masterTemp = plateAssignRepository.getMaxWellLabCodeOfMaxPlate(plateid, receivedDate);

        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return masterTemp;
    }

    @LogService
    public int getMaxPlateIdForCorrespondingDate(int plateid) throws Exception {
        int max_plate_for_cur_date;
        int deletePlate;
        String currentDate = commonRepository.getCurrentDateAsString();
        try {
            max_plate_for_cur_date = plateAssignRepository.getMaxPlateIdForCorrespondingDate(currentDate);
            deletePlate = max_plate_for_cur_date + plateid;
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return deletePlate;
    }

    @LogService
    public Map<Integer, List<DefaultBean>> swapBlockPlate(SwapBean swapBean) {
        Map<Integer, List<DefaultBean>> plateMap = new HashMap<>();
        try {
            String message = plateAssignRepository.swapBlockPlate(swapBean);
            if (message.isEmpty()) {
                plateMap = plateAssignRepository.getDefaultPlateList();
            }
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return plateMap;
    }

    @LogService
    public Map<Integer, List<DefaultBean>> MergeBlockPlate(PoolBean poolBean) {
        Map<Integer, List<DefaultBean>> plateMap = new HashMap<>();
        try {
            String message = plateAssignRepository.MergeBlockPlate(poolBean);
            if (message.isEmpty()) {
                plateMap = plateAssignRepository.getDefaultPlateList();
            }
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return plateMap;
    }

    @LogService
    public Map<Integer, List<DefaultBean>> deletePlate(PlateDeleteBean plateDeleteBean) {
        Map<Integer, List<DefaultBean>> plateMap = new HashMap<>();
        try {
            String message = plateAssignRepository.deletePlate(plateDeleteBean);
            if (message.isEmpty()) {
                plateMap = plateAssignRepository.getDefaultPlateList();
            }
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return plateMap;
    }

    @LogService
    public Map<Integer, List<DefaultBean>> deleteWell(String  labCode) {
        Map<Integer, List<DefaultBean>> plateMap = new HashMap<>();
        try {
            String message = plateAssignRepository.deleteWell(labCode);
            if (message.isEmpty()) {
                plateMap = plateAssignRepository.getDefaultPlateList();
            }
        } catch (EmptyResultDataAccessException ere) {
            throw ere;
        } catch (Exception e) {
            throw e;
        }
        return plateMap;
    }

    @LogService
    public String getFilePathList(HttpServletRequest httpServletRequest, String receiveDate) throws Exception {
        String zipFilePath = "";
        List<String> filePathList = new ArrayList<>();
        try {
            String message = "";
            String currentDate = commonRepository.getCurrentDateAsString();
            List<String> plateNumberList = plateAssignRepository.getPlateNumberList();
            //validate the plate number list
            if (plateNumberList != null && !plateNumberList.isEmpty() && plateNumberList.size() > 0) {
                for (int i = 0; i < plateNumberList.size(); i++) {
                    String plateId = plateNumberList.get(i);
                    List<MasterTemp> masterTempList = plateAssignRepository.getMasterTempList(plateId);
                    //validate the master temp list
                    if (masterTempList != null && !masterTempList.isEmpty() && masterTempList.size() > 0) {

                        //1. delete pending existing from master data, here
                        //2. check plates in master_data & plates are related.
                        //  if mofficer marked a whole plate as pending, repeat the pcr process for those samples.
                        //  delete those pending existing sample records from master data.
                        //  so now there are no more sample records related to plate2 in master_data.
                        //  But plate table the record is not deleted. so mofficer can see empty plate 2.
                        //3. if not delete plate table unwanted record.

                        //insert to plate table
                        //this is the correct plate id for plate creation
                        String masterTablePlateId = plateAssignRepository.getMaxPlateId(currentDate);
                        //create the plate
                        message = plateAssignRepository.createPlate(masterTablePlateId, receiveDate);
                        if (message.isEmpty()) {
                            //before insert: delete existing records
                            plateAssignRepository.deleteExistingPendingFromMasterData(masterTempList);
                            //insert the master batch
                            message = plateAssignRepository.insertMasterBatch(masterTempList, masterTablePlateId);
                            if (message.isEmpty()) {
                                //update the status in sample table
                                List<String> sampleIdList = masterTempList.stream().map(m -> m.getSampleId()).collect(Collectors.toList());
                                sampleIdList = sampleIdList.stream().filter(s -> (Objects.nonNull(s) && !s.isEmpty())).collect(Collectors.toList());
                                message = plateAssignRepository.updateSampleDataList(sampleIdList);
                                //Temporary Pending table clean
                                //plateAssignRepository.deleteExistingTemporaryPending(masterTempList);
                                if (message.isEmpty()) {
                                    //create the master file in machine location
                                    String filePath = this.createMasterFile(masterTempList, currentDate, masterTablePlateId);
                                    filePathList.add(filePath);
                                    //String xlFilePath  = this.generateExcelReport(httpServletRequest, masterTempList, currentDate, masterTablePlateId);
                                    //filePathList.add(xlFilePath);
                                    String csvFilePath =this.writeBeanAsCSVToOutputStream(httpServletRequest, masterTempList, currentDate, masterTablePlateId);
                                    filePathList.add(csvFilePath);
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }

                if (message.isEmpty()) {
                    plateAssignRepository.deleteAllFromMasterTempTable();
                    //zip the file list
                    zipFilePath = this.createZipFile(filePathList, currentDate);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return zipFilePath;
    }

    private String createMasterFile(List<MasterTemp> masterTempList, String currentDate, String masterTablePlateId) throws Exception {
        String filePath = "";
        try {
            String masterFileJasperPath = this.getJasperPath();
            //get the copy of master temp list
            List<MasterTemp> masterTemps = masterTempList.stream().collect(Collectors.toList());
            //update the master temp list new plate id
            masterTemps.stream().forEach(m -> m.setPlateId(masterTablePlateId));
            masterTemps.stream().filter(m -> m.getIsPool().equals(commonVarList.PLATE_POOLCODE_YES)).forEach(m -> m.setIsPool(commonVarList.PLATE_POOLDESCRIPTION_YES));
            masterTemps.stream().filter(m -> m.getIsPool().equals(commonVarList.PLATE_POOLCODE_NO)).forEach(m -> m.setIsPool(commonVarList.PLATE_POOLDESCRIPTION_NO));
            //put values to parameter map
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("date", currentDate);
            parameterMap.put("plateno", masterTablePlateId);
            //initialize file path
            String folderPath = this.getFolderPath(currentDate);
            filePath = folderPath + File.separator + MASTERFILE_NAME.replace("XXXXX", currentDate + "-" + "Plate" + "-" + masterTablePlateId);
            String printFileName = JasperFillManager.fillReportToFile(masterFileJasperPath, parameterMap, new JRBeanCollectionDataSource(masterTempList));
            //create the pdf location
            if (Objects.nonNull(printFileName) && !printFileName.isEmpty()) {
                JasperExportManager.exportReportToPdfFile(printFileName, filePath);
            }
        } catch (Exception e) {
            throw e;
        }
        return filePath;
    }

    private String getJasperPath() {
        String filePath = "";
        try {
            if (SystemUtils.IS_OS_LINUX) {
                filePath = commonVarList.MASTERFILE_LINUX_JASPER_FILEPATH;
            } else if (SystemUtils.IS_OS_WINDOWS) {
                filePath = commonVarList.MASTERFILE_WINDOWS_JASPER_FILEPATH;
            } else {
                filePath = commonVarList.MASTERFILE_WINDOWS_JASPER_FILEPATH;
            }
        } catch (Exception e) {
            throw e;
        }
        return filePath;
    }

    private String createZipFile(List<String> filePathList, String currentDate) throws Exception {
        String zipFilePath = "";
        try {
            //get base folder path
            String baseFolderPath = this.getBaseFolderPath();
            String folderPath = baseFolderPath + File.separator + currentDate;
            //set value to zip file path
            zipFilePath = folderPath + File.separator + MASTERZIPFILE_NAME;
            //delete the file if already exists in path
            common.deleteFile(zipFilePath);
            //create zip file
            common.zipFiles(filePathList, zipFilePath);
        } catch (Exception e) {
            throw e;
        }
        return zipFilePath;
    }

    private String getFolderPath(String currentDate) throws Exception {
        String folderPath = "";
        String baseFolderPath = "";
        try {
            //get base folder path
            baseFolderPath = this.getBaseFolderPath();
            folderPath = baseFolderPath + File.separator + currentDate;
            //create folder to save account open pdf
            this.createPdfFolder(folderPath);
        } catch (Exception e) {
            throw e;
        }
        return folderPath;
    }

    public String getBaseFolderPath() {
        String baseFolderPath = "";
        try {
            if (SystemUtils.IS_OS_LINUX) {
                baseFolderPath = commonVarList.MASTERFILE_LINUX_FILEPATH;
            } else if (SystemUtils.IS_OS_WINDOWS) {
                baseFolderPath = commonVarList.MASTERFILE_WINDOWS_FILEPATH;
            } else {
                baseFolderPath = commonVarList.MASTERFILE_WINDOWS_FILEPATH;
            }
        } catch (Exception e) {
            throw e;
        }
        return baseFolderPath;
    }

    public void createPdfFolder(String folderPath) throws Exception {
        try {
            File pdfFile = new File(folderPath);
            if (!pdfFile.exists()) {
                if (!pdfFile.isDirectory()) {
                    pdfFile.mkdirs();
                    common.createFolderPath(folderPath);
                }
            }
        } catch (IOException e) {
            throw e;
        }
    }


    /**
     * New Dev PCR file integration
     *
     */

    @LogService
    public String generateExcelReport(HttpServletRequest httpServletRequest, List<MasterTemp> masterTempList, String currentDate, String masterTablePlateId) throws Exception {

        String filePath = "";
        long count = 0;
        try {

            String folderPath = this.getFolderPath(currentDate);
            filePath = folderPath + File.separator + MASTERXLFILE_NAME.replace("XXXXX", currentDate + "-" + "Plate" + "-" + masterTablePlateId);
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            //get count
            count = masterTempList.size();
            if (count > 0) {

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

                            for (MasterTemp masterTempFile : masterTempList) {
                                if (currRow + 1 > maxRow) {
                                    fileCount++;
                                    this.writeTemporaryFile(workbook, fileCount, filePath);
                                    workbook = this.createExcelTopSection();
                                    sheet = workbook.getSheetAt(0);
                                    currRow = labReportHeaderRowCount;
                                    this.createExcelTableHeaderSection(workbook, currRow);
                                }
                                currRow = this.createExcelTableBodySection(workbook, masterTempFile, currRow, listrownumber);
                                listrownumber++;
                                if (currRow % 100 == 0) {
                                    // retain 100 last rows and flush all others
                                    ((SXSSFSheet) sheet).flushRows(100);
                                }
                            }
                        this.createExcelTableBodyBottom(workbook, currRow, listrownumber);

                        from = from + selectRow;
                    }

                    //Date createdTime = commonRepository.getCurrentDate();
                    //this.createExcelBotomSection(workbook, currRow, count, createdTime);
                    for (int i = 0; i < labReportColumnCount; i++) {
                        //to auto size all column in the sheet
                        sheet.autoSizeColumn(i);
                    }
                    try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                        workbook.write(outputStream);
                    }

            }
        } catch (Exception e) {
            throw e;
        }
        return filePath;
    }


    private SXSSFWorkbook createExcelTopSection() throws Exception {
        SXSSFWorkbook workbook = null;
        try {
            workbook = new SXSSFWorkbook(-1);
            Sheet sheet = workbook.createSheet("BIOER Import Sample Info");

            CellStyle fontBoldedUnderlinedCell = excelCommon.getFontBoldedUnderlinedCell(workbook);

//            Row row = sheet.createRow(0);
//            Cell cell = row.createCell(0);
//            cell.setCellValue("BIOER Import Sample Info Report");
//            cell.setCellStyle(fontBoldedUnderlinedCell);
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
            cell.setCellValue("Sample Id");
            cell.setCellStyle(columnHeaderCell);

            cell = row.createCell(1);
            cell.setCellValue("Color ");
            cell.setCellStyle(columnHeaderCell);

            cell = row.createCell(2);
            cell.setCellValue("Sample Name ");
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
                file = new File(directory + File.separator + "BIOERReport_" + fileCount + ".xlsx");
            } else {
                file = new File(directory + File.separator + "BIOERReport.xlsx");
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

    private int createExcelTableBodySection(SXSSFWorkbook workbook, MasterTemp masterTempFile, int currrow, int rownumber) throws Exception {
        try {
            String colorCode = "#0000FF";
            String colorCodePBS = "#00FFFF";

            Sheet sheet = workbook.getSheetAt(0);
            CellStyle rowColumnCell = ExcelCommon.getRowColumnCell(workbook);
            Row row = sheet.createRow(currrow++);

            CellStyle style = workbook.createCellStyle();
            style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN);

            Cell cell = row.createCell(0);
            cell.setCellValue(masterTempFile.getBarcode());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            switch (masterTempFile.getBlockValue()) {
                case "C3"://PBS
                    if (isPbs(masterTempFile.getBarcode())) {
                        cell.setCellValue(commonVarList.COLOR_CODE_PBS);
                    } else {
                        cell.setCellValue(commonVarList.COLOR_CODE_NORMAL);
                    }
                    break;
                default://Normal
                    cell.setCellValue(commonVarList.COLOR_CODE_NORMAL);
                    break;
            }
            cell.setCellStyle(rowColumnCell);

            cell = row.createCell(2);
            cell.setCellValue("");
            cell.setCellStyle(rowColumnCell);


        } catch (Exception e) {
            throw e;
        }
        return currrow;
    }

    public static boolean isPbs(String word){
        boolean isPbs = false;
        String str = "";
        if(word!=null && word.trim().length() > 0){
            if (word.length() > 3) {
                str = word.substring(word.length() - 3);
            }
        }
        if(str.equalsIgnoreCase("PBS")){
            isPbs = true;
        }
        return isPbs;
    }

    private int createExcelTableBodyBottom(SXSSFWorkbook workbook, int currrow, int rownumber) throws Exception {
        try {

            for (int iterator = 0; iterator < 2; iterator++) {

                Sheet sheet = workbook.getSheetAt(0);
                CellStyle rowColumnCell = ExcelCommon.getRowColumnCell(workbook);
                Row row = sheet.createRow(currrow++);

                CellStyle style = workbook.createCellStyle();
                style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
                style.setBorderBottom(XSSFCellStyle.BORDER_THIN);

                Cell cell = row.createCell(0);
                if(iterator==0) {
                    cell.setCellValue("Blank");
                    cell.setCellStyle(style);
                    cell = row.createCell(1);
                    cell.setCellValue(commonVarList.COLOR_CODE_BLANK);
                }else if(iterator==1){
                    cell.setCellValue("Positive");
                    cell.setCellStyle(style);
                    cell = row.createCell(1);
                    cell.setCellValue(commonVarList.COLOR_CODE_POSITIVE);
                }
                cell.setCellStyle(rowColumnCell);

                cell = row.createCell(2);
                cell.setCellValue("");
                cell.setCellStyle(rowColumnCell);

            }

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


    // Write the CSV file
    private String writeBeanAsCSVToOutputStream(HttpServletRequest httpServletRequest, List<MasterTemp> masterTempList, String currentDate, String masterTablePlateId) throws IOException {
        CSVPrinter csvPrinter = null;
        String filePath = "";
        try {

            //String currentDate = commonRepository.getCurrentDateAsString();
            String folderPath = this.getFolderPath(currentDate);
            filePath = folderPath + File.separator + MASTERCSVFILE_NAME.replace("XXXXX", currentDate + "-" + "Plate" + "-" + masterTablePlateId);
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }

            FileWriter out = new FileWriter(filePath);
            //csv header
            csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader("Sample Id","Color","Sample Name"));

            //csv body
            if (masterTempList != null) {

                for(MasterTemp sample : masterTempList){
                    List<String> record = new ArrayList<>();
                    record.add(sample.getBarcode());
                    switch (sample.getBlockValue()) {
                        case "C3"://PBS
                            if (isPbs(sample.getBarcode())) {
                                record.add(commonVarList.COLOR_CODE_PBS);
                            } else {
                                record.add(commonVarList.COLOR_CODE_NORMAL);
                            }
                            break;
                        default://Normal
                            record.add(commonVarList.COLOR_CODE_NORMAL);
                            break;
                    }
                    record.add("");

                    csvPrinter.printRecord(record);
                }
                csvPrinter.printRecord("Blank", commonVarList.COLOR_CODE_BLANK,"");
                csvPrinter.printRecord("Positive",commonVarList.COLOR_CODE_POSITIVE,"");
                //csv end
            }

        } catch (Exception e) {
            System.out.println("Failed to write CSV file to output stream");
        } finally {
            try {
                if (csvPrinter != null) {
                    csvPrinter.flush(); // Flush and close CSVPrinter
                    csvPrinter.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error when closing CSV Printer");
            }
        }
        return filePath;
    }


//    // Convert an XSSFWorkbook to CSV and write to provided OutputStream
//    private String writeWorkbookAsCSVToOutputStream(String  workBookPath,String currentDate, String masterTablePlateId) throws IOException {
//        CSVPrinter csvPrinter = null;
//        XSSFWorkbook workbook = null;
//        String filePath = "";
//        try {
//            //FileInputStream fileInStream = new FileInputStream(new File(workBookPath));
//            workbook = new XSSFWorkbook(new FileInputStream(new File(workBookPath)));
//            //String currentDate = commonRepository.getCurrentDateAsString();
//            String folderPath = this.getFolderPath(currentDate);
//            filePath = folderPath + File.separator + MASTERCSVFILE_NAME.replace("XXXXX", currentDate + "-" + "Plate" + "-" + masterTablePlateId);
//            File file = new File(filePath);
//            if (file.exists()) {
//                file.delete();
//            }
//
//            FileWriter out = new FileWriter(filePath);
//            csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT);
//
//            if (workbook != null) {
//                XSSFSheet sheet = workbook.getSheetAt(0); // Sheet #0 in this example
//                Iterator<Row> rowIterator = sheet.rowIterator();
//                while (rowIterator.hasNext()) {
//                    Row row = rowIterator.next();
//                    Iterator<Cell> cellIterator = row.cellIterator();
//                    List<String> record = new ArrayList<>();
//                    while (cellIterator.hasNext()) {
//                        Cell cell = cellIterator.next();
//                        //csvPrinter.print(cell.getStringCellValue());
//                        record.add(cell.getStringCellValue());
//                    }
//                    csvPrinter.printRecord(record);
//                    //csvPrinter.println(); // Newline after each row
//                }
//            }
//
//        } catch (Exception e) {
//            System.out.println("Failed to write CSV file to output stream");
//        } finally {
//            try {
//                if (csvPrinter != null) {
//                    csvPrinter.flush(); // Flush and close CSVPrinter
//                    csvPrinter.close();
//                }
//            }
//            catch (IOException ioe) {
//                System.out.println("Error when closing CSV Printer");
//            }
//        }
//
//        return filePath;
//
//    }

}
