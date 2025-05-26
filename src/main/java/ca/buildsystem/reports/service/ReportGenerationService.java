package ca.buildsystem.reports.service;

import ca.buildsystem.reports.dto.ReportGenerationDTO;
import ca.buildsystem.reports.dto.ReportResponseDTO;
import ca.buildsystem.reports.model.Report;
import ca.buildsystem.reports.model.ReportFormat;
import ca.buildsystem.reports.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Service for generating financial reports.
 * Handles the actual generation of report files in various formats.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportGenerationService {

    private final ReportRepository reportRepository;
    private final ReportService reportService;
    private final ReportMapper reportMapper;
    
    @Value("${app.report.storage-path:/tmp/reports}")
    private String reportStoragePath;
    
    @Value("${app.report.default-format:PDF}")
    private String defaultFormat;

    /**
     * Generate a report based on the provided data.
     *
     * @param generationDTO The report generation data
     * @return The generated report DTO
     */
    @Transactional
    public ReportResponseDTO generateReport(ReportGenerationDTO generationDTO) {
        log.info("Generating report: {}", generationDTO.getName());
        
        // Create a new report entity
        Report report = new Report();
        report.setName(generationDTO.getName());
        report.setDescription(generationDTO.getDescription());
        report.setType(generationDTO.getType());
        report.setFormat(generationDTO.getFormat() != null ? generationDTO.getFormat() : ReportFormat.valueOf(defaultFormat));
        report.setStartDate(generationDTO.getStartDate());
        report.setEndDate(generationDTO.getEndDate());
        report.setProjectId(generationDTO.getProjectId());
        report.setClientId(generationDTO.getClientId());
        report.setStatus("GENERATING");
        
        // Save the initial report
        Report savedReport = reportRepository.save(report);
        
        try {
            // Generate the report file
            String filePath = generateReportFile(savedReport);
            
            // Update the report with the file path and status
            savedReport.setFilePath(filePath);
            savedReport.setFileSize(new File(filePath).length());
            savedReport.setStatus("COMPLETED");
            savedReport.setLastGenerated(LocalDateTime.now());
            
            // Save the updated report
            savedReport = reportRepository.save(savedReport);
            
            return reportMapper.toResponseDTO(savedReport);
        } catch (Exception e) {
            log.error("Error generating report", e);
            
            // Update the report with error status
            savedReport.setStatus("ERROR");
            reportRepository.save(savedReport);
            
            throw new RuntimeException("Error generating report: " + e.getMessage(), e);
        }
    }
    
    /**
     * Generate the actual report file.
     * In a real implementation, this would use a template engine and data sources.
     *
     * @param report The report entity
     * @return The path to the generated file
     * @throws Exception if an error occurs during file generation
     */
    private String generateReportFile(Report report) throws Exception {
        // Create the storage directory if it doesn't exist
        Path storagePath = Paths.get(reportStoragePath);
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }
        
        // Generate a unique filename
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = String.format("%s_%s_%s.%s", 
                report.getType().toString().toLowerCase(),
                report.getName().replaceAll("\\s+", "_").toLowerCase(),
                timestamp,
                report.getFormat().toString().toLowerCase());
        
        Path filePath = storagePath.resolve(filename);
        
        // In a real implementation, this would generate the actual report content
        // For now, we'll just create an empty file
        Files.createFile(filePath);
        
        return filePath.toString();
    }
    
    /**
     * Download a report by its ID.
     *
     * @param id The report ID
     * @return The path to the report file
     * @throws RuntimeException if the report is not found or the file doesn't exist
     */
    @Transactional(readOnly = true)
    public String downloadReport(UUID id) {
        log.info("Downloading report with ID: {}", id);
        
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with ID: " + id));
        
        if (report.getFilePath() == null || !new File(report.getFilePath()).exists()) {
            throw new RuntimeException("Report file not found");
        }
        
        return report.getFilePath();
    }
}
