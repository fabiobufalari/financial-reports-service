package ca.buildsystem.reports.dto;

import ca.buildsystem.reports.model.ReportFormat;
import ca.buildsystem.reports.model.ReportType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for updating an existing report.
 */
@Data
public class ReportUpdateDTO {
    private String name;
    private String description;
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
