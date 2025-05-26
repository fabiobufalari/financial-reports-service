package ca.buildsystem.reports.dto;

import ca.buildsystem.reports.model.ReportFormat;
import ca.buildsystem.reports.model.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * DTO for creating a new report template.
 */
@Data
public class TemplateCreateDTO {
    
    @NotBlank(message = "Template name is required")
    private String name;
    
    @NotBlank(message = "Template description is required")
    private String description;
    
    @NotNull(message = "Report type is required")
    private ReportType type;
    
    @NotBlank(message = "Template content is required")
    private String templateContent;
    
    private String templatePath;
    
    private ReportFormat defaultFormat;
    
    private boolean systemTemplate;
    
    private boolean active;
    
    private String version;
    
    private List<ReportParameterDTO> parameters;
}
