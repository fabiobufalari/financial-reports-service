package ca.buildsystem.reports.service;

import ca.buildsystem.reports.dto.ReportResponseDTO;
import ca.buildsystem.reports.dto.ReportCreateDTO;
import ca.buildsystem.reports.dto.ReportUpdateDTO;
import ca.buildsystem.reports.model.Report;
import ca.buildsystem.reports.model.ReportFormat;
import ca.buildsystem.reports.model.ReportParameter;
import ca.buildsystem.reports.model.ReportType;
import ca.buildsystem.reports.repository.ReportParameterRepository;
import ca.buildsystem.reports.repository.ReportRepository;
import ca.buildsystem.reports.repository.ReportTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing financial reports.
 * Handles report creation, retrieval, updating, and deletion.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportTemplateRepository templateRepository;
    private final ReportParameterRepository parameterRepository;
    private final ReportMapper reportMapper;

    /**
     * Retrieve all reports with pagination.
     *
     * @param pageable Pagination information
     * @return A page of report DTOs
     */
    @Transactional(readOnly = true)
    public Page<ReportResponseDTO> getAllReports(Pageable pageable) {
        log.info("Retrieving all reports with pagination: {}", pageable);
        return reportRepository.findAll(pageable)
                .map(reportMapper::toResponseDTO);
    }

    /**
     * Retrieve a report by its ID.
     *
     * @param id The report ID
     * @return An optional containing the report DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<ReportResponseDTO> getReportById(UUID id) {
        log.info("Retrieving report with ID: {}", id);
        return reportRepository.findById(id)
                .map(reportMapper::toResponseDTO);
    }

    /**
     * Create a new report.
     *
     * @param createDTO The report creation data
     * @return The created report DTO
     */
    @Transactional
    public ReportResponseDTO createReport(ReportCreateDTO createDTO) {
        log.info("Creating new report: {}", createDTO.getName());
        
        Report report = reportMapper.toEntity(createDTO);
        
        // Set default values if not provided
        if (report.getFormat() == null) {
            report.setFormat(ReportFormat.PDF);
        }
        
        if (report.getCurrencyCode() == null) {
            report.setCurrencyCode("CAD"); // Default to Canadian Dollar
        }
        
        // Generate access token for public reports
        if (report.isPublic() && report.getAccessToken() == null) {
            report.setAccessToken(UUID.randomUUID().toString());
        }
        
        // Set status to "PENDING" for new reports
        report.setStatus("PENDING");
        
        Report savedReport = reportRepository.save(report);
        
        // Save parameters if provided
        if (createDTO.getParameters() != null && !createDTO.getParameters().isEmpty()) {
            List<ReportParameter> parameters = createDTO.getParameters().stream()
                    .map(paramDTO -> {
                        ReportParameter param = reportMapper.toParameterEntity(paramDTO);
                        param.setReport(savedReport);
                        return param;
                    })
                    .collect(Collectors.toList());
            
            parameterRepository.saveAll(parameters);
        }
        
        return reportMapper.toResponseDTO(savedReport);
    }

    /**
     * Update an existing report.
     *
     * @param id The report ID
     * @param updateDTO The report update data
     * @return The updated report DTO
     * @throws RuntimeException if the report is not found
     */
    @Transactional
    public ReportResponseDTO updateReport(UUID id, ReportUpdateDTO updateDTO) {
        log.info("Updating report with ID: {}", id);
        
        Report existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with ID: " + id));
        
        reportMapper.updateEntityFromDTO(updateDTO, existingReport);
        
        // Update parameters if provided
        if (updateDTO.getParameters() != null) {
            // Remove existing parameters
            parameterRepository.deleteByReportId(id);
            
            // Add new parameters
            List<ReportParameter> parameters = updateDTO.getParameters().stream()
                    .map(paramDTO -> {
                        ReportParameter param = reportMapper.toParameterEntity(paramDTO);
                        param.setReport(existingReport);
                        return param;
                    })
                    .collect(Collectors.toList());
            
            parameterRepository.saveAll(parameters);
        }
        
        Report updatedReport = reportRepository.save(existingReport);
        return reportMapper.toResponseDTO(updatedReport);
    }

    /**
     * Delete a report by its ID.
     *
     * @param id The report ID
     * @throws RuntimeException if the report is not found
     */
    @Transactional
    public void deleteReport(UUID id) {
        log.info("Deleting report with ID: {}", id);
        
        if (!reportRepository.existsById(id)) {
            throw new RuntimeException("Report not found with ID: " + id);
        }
        
        // Delete parameters first to avoid foreign key constraints
        parameterRepository.deleteByReportId(id);
        
        // Delete the report
        reportRepository.deleteById(id);
    }

    /**
     * Find reports by type with pagination.
     *
     * @param type The report type
     * @param pageable Pagination information
     * @return A page of report DTOs
     */
    @Transactional(readOnly = true)
    public Page<ReportResponseDTO> findReportsByType(ReportType type, Pageable pageable) {
        log.info("Finding reports by type: {}", type);
        return reportRepository.findByType(type, pageable)
                .map(reportMapper::toResponseDTO);
    }

    /**
     * Find reports by client ID with pagination.
     *
     * @param clientId The client ID
     * @param pageable Pagination information
     * @return A page of report DTOs
     */
    @Transactional(readOnly = true)
    public Page<ReportResponseDTO> findReportsByClientId(UUID clientId, Pageable pageable) {
        log.info("Finding reports by client ID: {}", clientId);
        return reportRepository.findByClientId(clientId, pageable)
                .map(reportMapper::toResponseDTO);
    }

    /**
     * Find reports by project ID with pagination.
     *
     * @param projectId The project ID
     * @param pageable Pagination information
     * @return A page of report DTOs
     */
    @Transactional(readOnly = true)
    public Page<ReportResponseDTO> findReportsByProjectId(UUID projectId, Pageable pageable) {
        log.info("Finding reports by project ID: {}", projectId);
        return reportRepository.findByProjectId(projectId, pageable)
                .map(reportMapper::toResponseDTO);
    }

    /**
     * Find reports by date range with pagination.
     *
     * @param startDate The start date
     * @param endDate The end date
     * @param pageable Pagination information
     * @return A page of report DTOs
     */
    @Transactional(readOnly = true)
    public Page<ReportResponseDTO> findReportsByDateRange(
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.info("Finding reports by date range: {} to {}", startDate, endDate);
        return reportRepository.findByDateRange(startDate, endDate, pageable)
                .map(reportMapper::toResponseDTO);
    }

    /**
     * Find reports by multiple criteria with pagination.
     *
     * @param type The report type (optional)
     * @param clientId The client ID (optional)
     * @param projectId The project ID (optional)
     * @param startDate The start date (optional)
     * @param endDate The end date (optional)
     * @param pageable Pagination information
     * @return A page of report DTOs
     */
    @Transactional(readOnly = true)
    public Page<ReportResponseDTO> findReportsByMultipleCriteria(
            ReportType type, UUID clientId, UUID projectId,
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.info("Finding reports by multiple criteria");
        return reportRepository.findByMultipleCriteria(
                type, clientId, projectId, startDate, endDate, pageable)
                .map(reportMapper::toResponseDTO);
    }

    /**
     * Find scheduled reports that are due for generation.
     *
     * @return A list of report DTOs
     */
    @Transactional(readOnly = true)
    public List<ReportResponseDTO> findReportsDueForGeneration() {
        log.info("Finding reports due for generation");
        LocalDateTime now = LocalDateTime.now();
        return reportRepository.findByScheduledTrueAndNextGenerationBefore(now)
                .stream()
                .map(reportMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update report status.
     *
     * @param id The report ID
     * @param status The new status
     * @return The updated report DTO
     * @throws RuntimeException if the report is not found
     */
    @Transactional
    public ReportResponseDTO updateReportStatus(UUID id, String status) {
        log.info("Updating status of report with ID: {} to {}", id, status);
        
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with ID: " + id));
        
        report.setStatus(status);
        
        if ("COMPLETED".equals(status)) {
            report.setLastGenerated(LocalDateTime.now());
            
            // If scheduled, calculate next generation time
            if (report.isScheduled() && report.getScheduleCron() != null) {
                // This would use a cron expression parser in a real implementation
                // For simplicity, just add 24 hours for now
                report.setNextGeneration(LocalDateTime.now().plusHours(24));
            }
        }
        
        Report updatedReport = reportRepository.save(report);
        return reportMapper.toResponseDTO(updatedReport);
    }

    /**
     * Find a public report by its access token.
     *
     * @param accessToken The access token
     * @return An optional containing the report DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<ReportResponseDTO> findPublicReportByAccessToken(String accessToken) {
        log.info("Finding public report by access token");
        return reportRepository.findByIsPublicTrueAndAccessToken(accessToken)
                .map(reportMapper::toResponseDTO);
    }
}
