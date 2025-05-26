package ca.buildsystem.reports.dto;

import ca.buildsystem.reports.model.ReportFormat;
import ca.buildsystem.reports.model.ReportType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for returning report data in API responses.
 */
@Data
public class ReportResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private ReportType type;
    private ReportFormat format;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String filePath;
    private Long fileSize;
    private boolean scheduled;
    private String scheduleCron;
    private LocalDateTime lastGenerated;
    private LocalDateTime nextGeneration;
    private BigDecimal totalAmount;
    private String currencyCode;
    private UUID templateId;
    private String templateName;
    private UUID projectId;
    private UUID clientId;
    private boolean isPublic;
    private String status;
    private List<ReportParameterDTO> parameters;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
