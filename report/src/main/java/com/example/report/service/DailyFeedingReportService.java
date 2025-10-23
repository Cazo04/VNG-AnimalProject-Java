package com.example.report.service;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;

import com.example.report.repository.interfaces.FeedingRepository;
import com.example.report.dto.DailyFeedingDto;
import com.example.report.util.XlsxSimpleTableBuilder;
import com.example.report.util.XlsxSimpleTableBuilder.SheetOptions;
import com.example.report.util.Rgb;

@Service
public class DailyFeedingReportService {
    
    private final FeedingRepository feedingRepository;

    public DailyFeedingReportService(FeedingRepository feedingRepository) {
        this.feedingRepository = feedingRepository;
    }

    public ByteBuffer export2ExcelAsBuffer(LocalDate date) throws Exception {
        List<DailyFeedingDto> data = feedingRepository.findDailyFeeding(date);

        SheetOptions options = new SheetOptions();
        options.sheetName = "Feeding Report";
        options.fontName = "Calibri";
        options.fontSizePt = 11;
        options.headerBackgroundColor = new Rgb("#5B9BD5");
        options.headerTextColor = new Rgb("#FFFFFF");
        options.borderStyle = BorderStyle.THIN;
        options.autoFitColumns = true;

        List<String> headers = List.of(
            "Animal Name",
            "Species", 
            "Food Type",
            "Quantity (kg)",
            "Keeper",
            "Feeding Time"
        );

        // Map DTO fields to columns
        List<Function<DailyFeedingDto, Object>> fieldExtractors = List.of(
            DailyFeedingDto::animalName,
            DailyFeedingDto::species,
            DailyFeedingDto::foodType,
            DailyFeedingDto::quantity,
            DailyFeedingDto::keeper,
            DailyFeedingDto::feedingTime
        );

        XlsxSimpleTableBuilder<DailyFeedingDto> tableBuilder = new XlsxSimpleTableBuilder<>(options, headers, data, fieldExtractors);
        
        // Build the Excel workbook
        XSSFWorkbook workbook = tableBuilder.build();
        
        // Write to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        
        // Convert to ByteBuffer
        byte[] bytes = baos.toByteArray();
        return ByteBuffer.wrap(bytes);
    }
}
