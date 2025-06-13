package ca.buildsystem.reports.dto;

import ca.buildsystem.reports.model.ReportFormat;
import ca.buildsystem.reports.model.ReportType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for returning template data in API responses.
 */
@Data
public class TemplateResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private ReportType type;
    private String templatePath;
    private String templateContent;
    private ReportFormat defaultFormat;
    private boolean systemTemplate;
    private boolean active;
    private String version;
    private List<ReportParameterDTO> parameters;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
