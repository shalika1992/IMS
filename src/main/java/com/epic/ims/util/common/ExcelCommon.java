package com.epic.ims.util.common;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ExcelCommon {

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 04:02:14 PM
     * @Version V1.00
     * @MethodName getFontBoldedCell
     * @MethodParams [workbook]
     * @MethodDescription - Gets XSSFWorkbook and return XSSFCellStyle cell object, witch formatted to bold font.
     */
    public static CellStyle getFontBoldedCell(SXSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 04:05:41 PM
     * @Version V1.00
     * @MethodName getFontBoldedUnderlinedCell
     * @MethodParams [workbook]
     * @MethodDescription - Gets XSSFWorkbook and return XSSFCellStyle cell object, witch formated to underlined bold font.
     */
    public static CellStyle getFontBoldedUnderlinedCell(SXSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setUnderline(XSSFFont.U_SINGLE);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 04:06:33 PM
     * @Version V1.00
     * @MethodName getColumnHeadeCell
     * @MethodParams [workbook]
     * @MethodDescription - Gets XSSFWorkbook and return XSSFCellStyle cell object, witch formated to thin border(top, right, left, bottom) and bold font.
     */
    public static CellStyle getColumnHeadeCell(SXSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        return style;
    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 04:07:29 PM
     * @Version V1.00
     * @MethodName getRowColumnCell
     * @MethodParams [workbook]
     * @MethodDescription - Gets XSSFWorkbook and return XSSFCellStyle cell object, witch formated to thin border(top, right, left, bottom).
     */
    public static CellStyle getRowColumnCell(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        return style;
    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 04:08:45 PM
     * @Version V1.00
     * @MethodName getAligneCell
     * @MethodParams [workbook, cellStyle, alignment]
     * @MethodDescription - return XSSFCellSytyle witch contains the alignment according to the
     * parameter value ' short alignment ' contain and if XSSFCellStyle
     * parameter is null then it create new style with the alignment that came
     * with the alignment parameter. Parameter cellStyel is not mandatory and if
     * cellStyle not null then it clone and set the alignment. Both workbook and
     * alignment is mandatory.
     */
    public static CellStyle getAligneCell(SXSSFWorkbook workbook, XSSFCellStyle cellStyle, short alignment) {
        CellStyle style = workbook.createCellStyle();
        if (cellStyle != null) {
            style.cloneStyleFrom(cellStyle);
        }
        style.setAlignment(alignment);
        return style;
    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 04:10:25 PM
     * @Version V1.00
     * @MethodName getHeaderCell
     * @MethodParams [workbook]
     * @MethodDescription - Get header cell
     */
    public static CellStyle getHeaderCell(SXSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setUnderline(XSSFFont.U_SINGLE);
        font.setColor(IndexedColors.WHITE.getIndex());
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        return style;
    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 04:10:56 PM
     * @Version V1.00
     * @MethodName getColumnHeaderCell
     * @MethodParams [workbook]
     * @MethodDescription - Get column header cell
     */
    public static CellStyle getColumnHeaderCell(SXSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.WHITE.getIndex());
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        return style;
    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 04:11:40 PM
     * @Version V1.00
     * @MethodName getFirstColumnHeaderCell
     * @MethodParams [workbook]
     * @MethodDescription - Get first column header cell
     */
    public static CellStyle getFirstColumnHeaderCell(SXSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.WHITE.getIndex());
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        return style;
    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 04:11:58 PM
     * @Version V1.00
     * @MethodName getLastColumnHeaderCell
     * @MethodParams [workbook]
     * @MethodDescription - Get last column header cell
     */
    public static CellStyle getLastColumnHeaderCell(SXSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.WHITE.getIndex());
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        return style;
    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 04:12:17 PM
     * @Version V1.00
     * @MethodName getRowColumnCellNew
     * @MethodParams [workbook]
     * @MethodDescription - Get row column cell
     */
    public static CellStyle getRowColumnCellNew(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        return style;

    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 04:12:31 PM
     * @Version V1.00
     * @MethodName getRowFirstColumnCell
     * @MethodParams [workbook]
     * @MethodDescription - Get row first column cell
     */
    public static CellStyle getRowFirstColumnCell(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        return style;
    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 04:12:42 PM
     * @Version V1.00
     * @MethodName getRowColumnDetailsCell
     * @MethodParams [workbook]
     * @MethodDescription - Get row column detail cell
     */
    public static CellStyle getRowColumnDetailsCell(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.LIGHT_BLUE.getIndex());
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }
}

