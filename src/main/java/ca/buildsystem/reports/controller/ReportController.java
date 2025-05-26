package ca.buildsystem.reports.controller;

import ca.buildsystem.reports.dto.ReportCreateDTO;
import ca.buildsystem.reports.dto.ReportResponseDTO;
import ca.buildsystem.reports.dto.ReportUpdateDTO;
import ca.buildsystem.reports.model.ReportType;
import ca.buildsystem.reports.service.ReportService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * REST controller for managing financial reports.
 * Provides endpoints for creating, retrieving, updating, and deleting reports.
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Report Controller", description = "API for managing financial reports")
public class ReportController {

    private final ReportService reportService;

    /**
     * GET /api/reports : Get all reports with pagination.
     *
     * @param pageable Pagination information
     * @return A page of reports
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER', 'ACCOUNTANT')")
    @Operation(summary = "Get all reports", description = "Returns a paginated list of all reports")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reports",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Page<ReportResponseDTO>> getAllReports(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get all Reports");
        Page<ReportResponseDTO> page = reportService.getAllReports(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * GET /api/reports/{id} : Get a report by ID.
     *
     * @param id The report ID
     * @return The report
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER', 'ACCOUNTANT')")
    @Operation(summary = "Get a report by ID", description = "Returns a report based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved report"),
            @ApiResponse(responseCode = "404", description = "Report not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<ReportResponseDTO> getReportById(
            @Parameter(description = "Report ID", required = true)
            @PathVariable UUID id) {
        log.info("REST request to get Report : {}", id);
        return reportService.getReportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/reports : Create a new report.
     *
     * @param createDTO The report creation data
     * @return The created report
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER')")
    @Operation(summary = "Create a new report", description = "Creates a new report and returns it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<ReportResponseDTO> createReport(
            @Parameter(description = "Report creation data", required = true)
            @Valid @RequestBody ReportCreateDTO createDTO) {
        log.info("REST request to create Report : {}", createDTO.getName());
        ReportResponseDTO result = reportService.createReport(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * PUT /api/reports/{id} : Update an existing report.
     *
     * @param id The report ID
     * @param updateDTO The report update data
     * @return The updated report
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER')")
    @Operation(summary = "Update an existing report", description = "Updates an existing report and returns it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Report not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<ReportResponseDTO> updateReport(
            @Parameter(description = "Report ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Report update data", required = true)
            @Valid @RequestBody ReportUpdateDTO updateDTO) {
        log.info("REST request to update Report : {}", id);
        try {
            ReportResponseDTO result = reportService.updateReport(id, updateDTO);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            log.error("Error updating report", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/reports/{id} : Delete a report.
     *
     * @param id The report ID
     * @return No content if successful
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER')")
    @Operation(summary = "Delete a report", description = "Deletes a report by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Report deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Report not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Void> deleteReport(
            @Parameter(description = "Report ID", required = true)
            @PathVariable UUID id) {
        log.info("REST request to delete Report : {}", id);
        try {
            reportService.deleteReport(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting report", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/reports/type/{type} : Get reports by type.
     *
     * @param type The report type
     * @param pageable Pagination information
     * @return A page of reports
     */
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER', 'ACCOUNTANT')")
    @Operation(summary = "Get reports by type", description = "Returns a paginated list of reports by type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reports"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Page<ReportResponseDTO>> getReportsByType(
            @Parameter(description = "Report type", required = true)
            @PathVariable ReportType type,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get Reports by type : {}", type);
        Page<ReportResponseDTO> page = reportService.findReportsByType(type, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * GET /api/reports/client/{clientId} : Get reports by client ID.
     *
     * @param clientId The client ID
     * @param pageable Pagination information
     * @return A page of reports
     */
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER', 'ACCOUNTANT')")
    @Operation(summary = "Get reports by client ID", description = "Returns a paginated list of reports by client ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reports"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Page<ReportResponseDTO>> getReportsByClientId(
            @Parameter(description = "Client ID", required = true)
            @PathVariable UUID clientId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get Reports by client ID : {}", clientId);
        Page<ReportResponseDTO> page = reportService.findReportsByClientId(clientId, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * GET /api/reports/project/{projectId} : Get reports by project ID.
     *
     * @param projectId The project ID
     * @param pageable Pagination information
     * @return A page of reports
     */
    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER', 'ACCOUNTANT', 'PROJECT_MANAGER')")
    @Operation(summary = "Get reports by project ID", description = "Returns a paginated list of reports by project ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reports"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Page<ReportResponseDTO>> getReportsByProjectId(
            @Parameter(description = "Project ID", required = true)
            @PathVariable UUID projectId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get Reports by project ID : {}", projectId);
        Page<ReportResponseDTO> page = reportService.findReportsByProjectId(projectId, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * GET /api/reports/search : Search reports by multiple criteria.
     *
     * @param type The report type (optional)
     * @param clientId The client ID (optional)
     * @param projectId The project ID (optional)
     * @param startDate The start date (optional)
     * @param endDate The end date (optional)
     * @param pageable Pagination information
     * @return A page of reports
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER', 'ACCOUNTANT')")
    @Operation(summary = "Search reports by criteria", description = "Returns a paginated list of reports matching the criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reports"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Page<ReportResponseDTO>> searchReports(
            @Parameter(description = "Report type")
            @RequestParam(required = false) ReportType type,
            @Parameter(description = "Client ID")
            @RequestParam(required = false) UUID clientId,
            @Parameter(description = "Project ID")
            @RequestParam(required = false) UUID projectId,
            @Parameter(description = "Start date (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to search Reports by criteria");
        Page<ReportResponseDTO> page = reportService.findReportsByMultipleCriteria(
                type, clientId, projectId, startDate, endDate, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * PATCH /api/reports/{id}/status : Update report status.
     *
     * @param id The report ID
     * @param status The new status
     * @return The updated report
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER')")
    @Operation(summary = "Update report status", description = "Updates the status of a report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Report not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<ReportResponseDTO> updateReportStatus(
            @Parameter(description = "Report ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "New status", required = true)
            @RequestParam String status) {
        log.info("REST request to update status of Report : {} to {}", id, status);
        try {
            ReportResponseDTO result = reportService.updateReportStatus(id, status);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            log.error("Error updating report status", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/reports/public/{accessToken} : Get a public report by access token.
     *
     * @param accessToken The access token
     * @return The report
     */
    @GetMapping("/public/{accessToken}")
    @Operation(summary = "Get a public report by access token", description = "Returns a public report based on its access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved report"),
            @ApiResponse(responseCode = "404", description = "Report not found")
    })
    public ResponseEntity<ReportResponseDTO> getPublicReportByAccessToken(
            @Parameter(description = "Access token", required = true)
            @PathVariable String accessToken) {
        log.info("REST request to get public Report by access token");
        return reportService.findPublicReportByAccessToken(accessToken)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
