package com.example.report.util.excel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class Excel {

    public static Font createFont(XSSFWorkbook workbook, FontOptions options) {
        XSSFFont font = workbook.createFont();
        
        if (options.name != null) {
            font.setFontName(options.name);
        }
        if (options.sizePt > 0) {
            font.setFontHeightInPoints((short) options.sizePt);
        }
        if (options.color != null) {
            font.setColor(options.color.toXSSFColor());
        }
        font.setBold(options.bold);
        font.setItalic(options.italic);
        font.setUnderline(options.underline);
        
        return font;
    }

    public static Cell makeInlineStrCell(Row row, int colIndex, String text) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(text != null ? text : "");
        return cell;
    }

    public static Cell makeNumberCell(Row row, int colIndex, double num) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(num);
        return cell;
    }

    public static Cell makeBooleanCell(Row row, int colIndex, boolean b) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(b);
        return cell;
    }

    public static Cell makeDateCell(Row row, int colIndex, LocalDateTime localDateTime) {
        Cell cell = row.createCell(colIndex);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        cell.setCellValue(date);
        return cell;
    }

    public static Cell makeCellAuto(Row row, int colIndex, Object value) {
        if (value == null) return makeInlineStrCell(row, colIndex, "");
        if (value instanceof Number n) {
            return makeNumberCell(row, colIndex, n.doubleValue());
        }
        if (value instanceof Boolean b) {
            return makeBooleanCell(row, colIndex, b);
        }
        if (value instanceof LocalDateTime ld) {
            return makeDateCell(row, colIndex, ld);
        }
        return makeInlineStrCell(row, colIndex, String.valueOf(value));
    }

    public static String getCellReference(int colIndex, int rowIndex) {
        return colName(colIndex) + rowIndex;
    }

    private static String colName(int idx0) {
        StringBuilder sb = new StringBuilder();
        int x = idx0;
        do {
            int r = x % 26;
            sb.append((char)('A' + r));
            x = x / 26 - 1;
        } while (x >= 0);
        return sb.reverse().toString();
    }

    public static Cell makeHeaderCell(Row row, int colIndex, String text, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(text);
        if (style != null) {
            cell.setCellStyle(style);
        }
        return cell;
    }

    public static Cell makeDataCell(Row row, int colIndex, Object value, CellStyle style) {
        Cell cell = makeCellAuto(row, colIndex, value);
        if (style != null) {
            cell.setCellStyle(style);
        }
        return cell;
    }

    public static Cell makeDataCell(Row row, int colIndex, Object value, CellStyle style, CellStyle dateStyle) {
        Cell cell;
        if (value instanceof LocalDateTime) {
            cell = makeCellAuto(row, colIndex, value);
            if (dateStyle != null) {
                cell.setCellStyle(dateStyle);
            }
        } else {
            cell = makeCellAuto(row, colIndex, value);
            if (style != null) {
                cell.setCellStyle(style);
            }
        }
        return cell;
    }

    public static Row createRow(Sheet sheet, int rowNum) {
        return sheet.createRow(rowNum);
    }

    public static double calculateOptimizedColumnWidth(String text) {
        if (text == null || text.isEmpty()) {
            return 8.43; // Default Excel column width
        }
        
        // Remove milliseconds from datetime strings
        String cleanText = text.replaceAll("\\.\\d{3,9}(?=[TZ]|$)", "");
        
        double width = 0.0;
        
        for (char c : cleanText.toCharArray()) {
            if (Character.isUpperCase(c)) {
                width += 1.3;
            } else if (Character.isDigit(c)) {
                width += 1.0;
            } else if (c == ' ') {
                width += 0.5;
            } else if (c == 'i' || c == 'l' || c == 'j' || c == 't' || c == 'f') {
                width += 0.6;
            } else if (c == 'm' || c == 'w') {
                width += 1.2;
            } else if (Character.isLowerCase(c)) {
                width += 1.0;
            } else {
                width += 1.0;
            }
        }
        
        return Math.min(width * 1.1 + 2.0, 255.0);
    }

    public static void autoFitColumns(Sheet sheet, List<String> headers, 
                                     List<List<Object>> dataRows,
                                     double minWidth, double maxWidth, double paddingFactor) {
        
        if (headers == null || headers.isEmpty()) {
            return;
        }
        
        int columnCount = headers.size();
        double[] columnWidths = new double[columnCount];
        
        // Initialize with header widths
        for (int col = 0; col < columnCount; col++) {
            columnWidths[col] = calculateOptimizedColumnWidth(headers.get(col));
        }
        
        // Process all data rows
        if (dataRows != null) {
            for (List<Object> row : dataRows) {
                for (int col = 0; col < Math.min(columnCount, row.size()); col++) {
                    Object cellValue = row.get(col);
                    String cellText = cellValue != null ? cellValue.toString() : "";
                    double cellWidth = calculateOptimizedColumnWidth(cellText);
                    columnWidths[col] = Math.max(columnWidths[col], cellWidth);
                }
            }
        }
        
        // Apply widths
        for (int col = 0; col < columnCount; col++) {
            double finalWidth = columnWidths[col] * paddingFactor;
            finalWidth = Math.max(minWidth, Math.min(finalWidth, maxWidth));
            // Convert to POI units (256 units per character)
            sheet.setColumnWidth(col, (int)(finalWidth * 256));
        }
    }

    public static void autoFitColumns(Sheet sheet, List<String> headers, List<List<Object>> dataRows) {
        autoFitColumns(sheet, headers, dataRows, 8.43, 50.0, 1.15);
    }
}
