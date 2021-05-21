package com.epic.ims.util.common;

import com.epic.ims.bean.samplefileupload.SampleData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Scope("prototype")
public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Autowired
    Common common;

    public boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public List<SampleData> excelToSampleData(InputStream inputStream,String receivedDate) throws Exception {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<SampleData> sampleDataList = new ArrayList<SampleData>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                SampleData sampleData = new SampleData();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            try {
                                sampleData.setReferenceNo(common.handleNullAndEmptyInExcelCell(currentCell.getStringCellValue()));
                            } catch (Exception e) {
                                sampleData.setReferenceNo("");
                            }
                            break;

                        case 1:
                            sampleData.setDate(receivedDate);
                            break;

                        case 2:
                            sampleData.setMohArea(common.handleNullAndEmptyInExcelCell(currentCell.getStringCellValue()));
                            break;

                        case 3:
                            sampleData.setName(common.handleNullAndEmptyInExcelCell(currentCell.getStringCellValue()));
                            break;

                        case 4:
                            String age = NumberToTextConverter.toText(currentCell.getNumericCellValue());
                            sampleData.setAge(age);
                            break;

                        case 5:
                            sampleData.setGender((currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty()) ? currentCell.getStringCellValue() : "");
                            break;

                        case 6:
                            sampleData.setSymptomatic((currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty()) ? currentCell.getStringCellValue() : "");
                            break;

                        case 7:
                            sampleData.setContactType((currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty()) ? currentCell.getStringCellValue() : "");
                            break;

                        case 8:
                            sampleData.setNic((currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty()) ? currentCell.getStringCellValue() : "");
                            break;

                        case 9:
                            sampleData.setAddress((currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty()) ? currentCell.getStringCellValue() : "");
                            break;

                        case 10:
                            sampleData.setResidentDistrict((currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty()) ? currentCell.getStringCellValue() : "");
                            break;

                        case 11:
                            String contactNumber = NumberToTextConverter.toText(currentCell.getNumericCellValue());
                            if(contactNumber != "0"){
                                sampleData.setContactNumber(contactNumber);
                            }else{
                                sampleData.setContactNumber("");
                            }
                            break;

                        case 12:
                            sampleData.setSecondaryContactNumber((currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isEmpty()) ? currentCell.getStringCellValue() : "");
                            break;

                        default:
                            break;
                    }
                    cellIdx++;
                }
                sampleDataList.add(sampleData);
            }
            return sampleDataList;
        } catch (Exception e) {
            throw e;
        } finally {
            inputStream.close();
        }
    }
}
