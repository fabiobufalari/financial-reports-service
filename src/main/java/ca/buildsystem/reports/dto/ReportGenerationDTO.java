package ca.buildsystem.reports.dto;

import ca.buildsystem.reports.model.ReportFormat;
import ca.buildsystem.reports.model.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for report generation requests.
 */
@Data
public class ReportGenerationDTO {
    @NotNull(message = "Report type is required")
    private ReportType type;
    
    @NotBlank(message = "Report name is required")
    private String name;
    
    private String description;
    
    private ReportFormat format;
    
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;
    
    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
    
    private UUID templateId;
    
    private UUID projectId;
    
    private UUID clientId;
    
    private List<ReportParameterDTO> parameters;
}
