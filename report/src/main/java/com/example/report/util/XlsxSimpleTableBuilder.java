package com.example.report.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import com.example.report.util.excel.Excel;

import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class XlsxSimpleTableBuilder<T> {

    public static class SheetOptions {
        public String sheetName = "Sheet1";
        public String fontName = "Calibri";
        public int fontSizePt = 11;
        public Rgb headerBackgroundColor = new Rgb("#D9D9D9");
        public Rgb headerTextColor = new Rgb("#000000");
        public Rgb dataTextColor = new Rgb("#000000");
        public BorderStyle borderStyle = BorderStyle.MEDIUM;
        public boolean autoFitColumns = true;
    }

    private final SheetOptions options;
    private final List<String> headers;
    private final List<T> rows;
    private final List<Function<T, Object>> fieldExtractors;

    public XlsxSimpleTableBuilder(SheetOptions options, List<String> headers, List<T> rows) {
        this(options, headers, rows, null);
    }

    public XlsxSimpleTableBuilder(SheetOptions options, List<String> headers, List<T> rows, List<Function<T, Object>> fieldExtractors) {
        this.options = options;
        this.headers = headers;
        this.rows = rows;
        this.fieldExtractors = fieldExtractors;
    }

    public XSSFWorkbook build() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        
        // Create sheet
        Sheet sheet = workbook.createSheet(options.sheetName);

        // Create styles
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        CellStyle dateStyle = createDateStyle(workbook);

        // Add header row
        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < headers.size(); col++) {
            Excel.makeHeaderCell(headerRow, col, headers.get(col), headerStyle);
        }

        // Add data rows
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            T rowData = rows.get(rowIndex);
            Row row = sheet.createRow(rowIndex + 1);
            for (int col = 0; col < headers.size(); col++) {
                Object cellValue = getCellValue(rowData, col);
                Cell cell;
                if (cellValue instanceof java.time.LocalDateTime ldt) {
                    cell = Excel.makeDateCell(row, col, ldt);
                    cell.setCellStyle(dateStyle);
                } else {
                    cell = Excel.makeCellAuto(row, col, cellValue);
                    cell.setCellStyle(dataStyle);
                }
            }
        }

        // Auto-fit columns
        if (options.autoFitColumns) {
            for (int col = 0; col < headers.size(); col++) {
                sheet.autoSizeColumn(col);
            }
        }

        return workbook;
    }

    private Object getCellValue(T rowData, int colIndex) {
        if (fieldExtractors != null && colIndex < fieldExtractors.size()) {
            return fieldExtractors.get(colIndex).apply(rowData);
        }
        return extractFieldValue(rowData, colIndex);
    }

    private Object extractFieldValue(T rowData, int colIndex) {
        try {
            // Try as a record first
            RecordComponent[] components = rowData.getClass().getRecordComponents();
            if (components != null && colIndex < components.length) {
                return components[colIndex].getAccessor().invoke(rowData);
            }
        } catch (Exception e) {
            // Not a record or error, try fields
        }
        
        try {
            // Try regular fields
            Field[] fields = rowData.getClass().getDeclaredFields();
            if (colIndex < fields.length) {
                fields[colIndex].setAccessible(true);
                return fields[colIndex].get(rowData);
            }
        } catch (Exception e) {
            // Ignore
        }
        
        return null;
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        
        // Font
        XSSFFont font = workbook.createFont();
        font.setFontName(options.fontName);
        font.setFontHeightInPoints((short) options.fontSizePt);
        font.setBold(true);
        font.setColor(options.headerTextColor.toXSSFColor());
        style.setFont(font);
        
        // Background
        style.setFillForegroundColor(options.headerBackgroundColor.toXSSFColor());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Borders
        style.setBorderTop(options.borderStyle);
        style.setBorderBottom(options.borderStyle);
        style.setBorderLeft(options.borderStyle);
        style.setBorderRight(options.borderStyle);
        
        return style;
    }

    private CellStyle createDataStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        
        // Font
        XSSFFont font = workbook.createFont();
        font.setFontName(options.fontName);
        font.setFontHeightInPoints((short) options.fontSizePt);
        font.setColor(options.dataTextColor.toXSSFColor());
        style.setFont(font);
        
        // Borders
        style.setBorderTop(options.borderStyle);
        style.setBorderBottom(options.borderStyle);
        style.setBorderLeft(options.borderStyle);
        style.setBorderRight(options.borderStyle);
        
        return style;
    }

    private CellStyle createDateStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        
        // Font
        XSSFFont font = workbook.createFont();
        font.setFontName(options.fontName);
        font.setFontHeightInPoints((short) options.fontSizePt);
        font.setColor(options.dataTextColor.toXSSFColor());
        style.setFont(font);
        
        // Date format
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm"));
        
        // Borders
        style.setBorderTop(options.borderStyle);
        style.setBorderBottom(options.borderStyle);
        style.setBorderLeft(options.borderStyle);
        style.setBorderRight(options.borderStyle);
        
        return style;
    }
}
