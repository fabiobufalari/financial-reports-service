package ca.buildsystem.reports.controller;

import ca.buildsystem.reports.dto.ReportGenerationDTO;
import ca.buildsystem.reports.dto.ReportResponseDTO;
import ca.buildsystem.reports.service.ReportGenerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.UUID;

/**
 * REST controller for generating financial reports.
 * Provides endpoints for generating and downloading reports.
 */
@RestController
@RequestMapping("/api/reports/generate")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Report Generation Controller", description = "API for generating financial reports")
public class ReportGenerationController {

    private final ReportGenerationService reportGenerationService;

    /**
     * POST /api/reports/generate : Generate a new report.
     *
     * @param generationDTO The report generation data
     * @return The generated report
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER')")
    @Operation(summary = "Generate a new report", description = "Generates a new report based on the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<ReportResponseDTO> generateReport(
            @Parameter(description = "Report generation data", required = true)
            @Valid @RequestBody ReportGenerationDTO generationDTO) {
        log.info("REST request to generate report: {}", generationDTO.getName());
        ReportResponseDTO result = reportGenerationService.generateReport(generationDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/reports/generate/{id}/download : Download a generated report.
     *
     * @param id The report ID
     * @return The report file
     */
    @GetMapping("/{id}/download")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER', 'ACCOUNTANT')")
    @Operation(summary = "Download a report", description = "Downloads a generated report file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report downloaded successfully",
                    content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404", description = "Report not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Resource> downloadReport(
            @Parameter(description = "Report ID", required = true)
            @PathVariable UUID id) {
        log.info("REST request to download report: {}", id);
        
        try {
            String filePath = reportGenerationService.downloadReport(id);
            File file = new File(filePath);
            
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new FileSystemResource(file);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("Error downloading report", e);
            return ResponseEntity.notFound().build();
        }
    }
}
