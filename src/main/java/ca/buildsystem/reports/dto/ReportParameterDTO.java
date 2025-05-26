package ca.buildsystem.reports.dto;

import ca.buildsystem.reports.model.ParameterType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * DTO for report parameters.
 */
@Data
public class ReportParameterDTO {
    private UUID id;
    
    @NotBlank(message = "Parameter name is required")
    private String name;
    
    private String displayName;
    
    private String description;
    
    @NotNull(message = "Parameter type is required")
    private ParameterType type;
    
    private String defaultValue;
    
    private String value;
    
    private boolean required;
    
    private String validationRegex;
    
    private String validationMessage;
    
    private String listValues;
    
    private String minValue;
    
    private String maxValue;
    
    private Integer displayOrder;
}
