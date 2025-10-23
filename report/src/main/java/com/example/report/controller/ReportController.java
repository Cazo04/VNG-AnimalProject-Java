package com.example.report.controller;

import com.example.report.dto.AnimalHealthCheckDto;
import com.example.report.dto.DailyFeedingDto;
import com.example.report.query.AnimalHealthCheckQuery;
import com.example.report.query.DailyFeedingQuery;
import com.example.report.service.DailyFeedingReportService;
import com.example.report.service.AnimalHealthCheckReportService;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    
    private final QueryGateway queryGateway;
    private final DailyFeedingReportService dailyFeedingReportService;
    private final AnimalHealthCheckReportService animalHealthCheckReportService;

    public ReportController(QueryGateway queryGateway, DailyFeedingReportService dailyFeedingReportService, AnimalHealthCheckReportService animalHealthCheckReportService) {
        this.queryGateway = queryGateway;
        this.dailyFeedingReportService = dailyFeedingReportService;
        this.animalHealthCheckReportService = animalHealthCheckReportService;
    }

    @GetMapping("/daily-feeding")
    public ResponseEntity<List<DailyFeedingDto>> getDailyFeeding(
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd") 
            LocalDate date) {
        
        Date queryDate = date != null 
            ? Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
            : null;
            
        DailyFeedingQuery query = new DailyFeedingQuery(queryDate);
        List<DailyFeedingDto> result = queryGateway.query(
            query,
            ResponseTypes.multipleInstancesOf(DailyFeedingDto.class)
        ).join();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/daily-feeding/export")
    public ResponseEntity<byte[]> exportDailyFeedingReport(
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd") 
            LocalDate date) throws Exception {
        
        LocalDate reportDate = date != null ? date : LocalDate.now();
        
        // Generate Excel file
        ByteBuffer buffer = dailyFeedingReportService.export2ExcelAsBuffer(reportDate);
        byte[] excelBytes = new byte[buffer.remaining()];
        buffer.get(excelBytes);
        
        // Format filename: animal_health_check_report_YYYYMMDD.xlsx
        String filename = String.format(
            "animal_health_check_report_%s.xlsx",
            reportDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        );
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(excelBytes);
    }

    @GetMapping("/animal-health-check")
    public ResponseEntity<List<AnimalHealthCheckDto>> getAnimalHealthCheck(
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd") 
            LocalDate fromDate,
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd") 
            LocalDate toDate) {
        
        Date from = fromDate != null 
            ? Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            : null;
        Date to = toDate != null 
            ? Date.from(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            : null;
            
        AnimalHealthCheckQuery query = new AnimalHealthCheckQuery(from, to);
        List<AnimalHealthCheckDto> result = queryGateway.query(
            query,
            ResponseTypes.multipleInstancesOf(AnimalHealthCheckDto.class)
        ).join();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/animal-health-check/export/pdf")
    public ResponseEntity<byte[]> exportAnimalHealthCheckReportPdf(
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd") 
            LocalDate fromDate,
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd") 
            LocalDate toDate) throws Exception {
        
        // Generate PDF file
        ByteBuffer buffer = animalHealthCheckReportService.export2PdfAsBuffer(fromDate, toDate);
        byte[] pdfBytes = new byte[buffer.remaining()];
        buffer.get(pdfBytes);
        
        // Format filename: animal_health_check_report_YYYYMMDD_YYYYMMDD.pdf
        String fromStr = fromDate != null ? fromDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) : "N/A";
        String toStr = toDate != null ? toDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) : "N/A";
        String filename = String.format(
            "animal_health_check_report_%s_%s.pdf",
            fromStr,
            toStr
        );
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);
    }
}
