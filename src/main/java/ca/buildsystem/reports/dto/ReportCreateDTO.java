package ca.buildsystem.reports.dto;

import ca.buildsystem.reports.model.ReportFormat;
import ca.buildsystem.reports.model.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for creating a new report.
 */
@Data
public class ReportCreateDTO {
    @NotBlank(message = "Report name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Report type is required")
    private ReportType type;
    
    private ReportFormat format;
    
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    private Boolean scheduled;
    
    private String scheduleCron;
    
    private BigDecimal totalAmount;
    
    private String currencyCode;
    
    private UUID templateId;
    
    private UUID projectId;
    
    private UUID clientId;
    
    private Boolean isPublic;
    
    private List<ReportParameterDTO> parameters;
}
