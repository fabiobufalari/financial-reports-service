package ca.buildsystem.reports.service;

import ca.buildsystem.reports.dto.ReportCreateDTO;
import ca.buildsystem.reports.dto.ReportParameterDTO;
import ca.buildsystem.reports.dto.ReportResponseDTO;
import ca.buildsystem.reports.dto.ReportUpdateDTO;
import ca.buildsystem.reports.dto.TemplateCreateDTO;
import ca.buildsystem.reports.dto.TemplateResponseDTO;
import ca.buildsystem.reports.model.Report;
import ca.buildsystem.reports.model.ReportParameter;
import ca.buildsystem.reports.model.ReportTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between entities and DTOs.
 * Handles the transformation of data between the persistence and service layers.
 */
@Component
public class ReportMapper {

    /**
     * Convert a Report entity to a ReportResponseDTO.
     *
     * @param report The Report entity to convert
     * @return The corresponding ReportResponseDTO
     */
    public ReportResponseDTO toResponseDTO(Report report) {
        if (report == null) {
            return null;
        }

        ReportResponseDTO dto = new ReportResponseDTO();
        dto.setId(report.getId());
        dto.setName(report.getName());
        dto.setDescription(report.getDescription());
        dto.setType(report.getType());
        dto.setFormat(report.getFormat());
        dto.setStartDate(report.getStartDate());
        dto.setEndDate(report.getEndDate());
        dto.setFilePath(report.getFilePath());
        dto.setFileSize(report.getFileSize());
        dto.setScheduled(report.isScheduled());
        dto.setScheduleCron(report.getScheduleCron());
        dto.setLastGenerated(report.getLastGenerated());
        dto.setNextGeneration(report.getNextGeneration());
        dto.setTotalAmount(report.getTotalAmount());
        dto.setCurrencyCode(report.getCurrencyCode());
        dto.setProjectId(report.getProjectId());
        dto.setClientId(report.getClientId());
        dto.setPublic(report.isPublic());
        dto.setStatus(report.getStatus());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setCreatedBy(report.getCreatedBy());
        dto.setUpdatedAt(report.getUpdatedAt());
        dto.setUpdatedBy(report.getUpdatedBy());
        
        if (report.getTemplate() != null) {
            dto.setTemplateId(report.getTemplate().getId());
            dto.setTemplateName(report.getTemplate().getName());
        }
        
        if (report.getParameters() != null && !report.getParameters().isEmpty()) {
            dto.setParameters(report.getParameters().stream()
                    .map(this::toParameterDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    /**
     * Convert a ReportCreateDTO to a Report entity.
     *
     * @param dto The ReportCreateDTO to convert
     * @return The corresponding Report entity
     */
    public Report toEntity(ReportCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Report report = new Report();
        report.setName(dto.getName());
        report.setDescription(dto.getDescription());
        report.setType(dto.getType());
        report.setFormat(dto.getFormat());
        report.setStartDate(dto.getStartDate());
        report.setEndDate(dto.getEndDate());
        report.setScheduled(dto.getScheduled());
        report.setScheduleCron(dto.getScheduleCron());
        report.setTotalAmount(dto.getTotalAmount());
        report.setCurrencyCode(dto.getCurrencyCode());
        report.setProjectId(dto.getProjectId());
        report.setClientId(dto.getClientId());
        report.setPublic(dto.getIsPublic());
        
        return report;
    }

    /**
     * Update a Report entity from a ReportUpdateDTO.
     *
     * @param dto The ReportUpdateDTO containing update data
     * @param report The Report entity to update
     */
    public void updateEntityFromDTO(ReportUpdateDTO dto, Report report) {
        if (dto == null || report == null) {
            return;
        }

        if (dto.getName() != null) {
            report.setName(dto.getName());
        }
        
        if (dto.getDescription() != null) {
            report.setDescription(dto.getDescription());
        }
        
        if (dto.getType() != null) {
            report.setType(dto.getType());
        }
        
        if (dto.getFormat() != null) {
            report.setFormat(dto.getFormat());
        }
        
        if (dto.getStartDate() != null) {
            report.setStartDate(dto.getStartDate());
        }
        
        if (dto.getEndDate() != null) {
            report.setEndDate(dto.getEndDate());
        }
        
        if (dto.getScheduled() != null) {
            report.setScheduled(dto.getScheduled());
        }
        
        if (dto.getScheduleCron() != null) {
            report.setScheduleCron(dto.getScheduleCron());
        }
        
        if (dto.getTotalAmount() != null) {
            report.setTotalAmount(dto.getTotalAmount());
        }
        
        if (dto.getCurrencyCode() != null) {
            report.setCurrencyCode(dto.getCurrencyCode());
        }
        
        if (dto.getProjectId() != null) {
            report.setProjectId(dto.getProjectId());
        }
        
        if (dto.getClientId() != null) {
            report.setClientId(dto.getClientId());
        }
        
        if (dto.getIsPublic() != null) {
            report.setPublic(dto.getIsPublic());
        }
    }

    /**
     * Convert a ReportParameter entity to a ReportParameterDTO.
     *
     * @param parameter The ReportParameter entity to convert
     * @return The corresponding ReportParameterDTO
     */
    public ReportParameterDTO toParameterDTO(ReportParameter parameter) {
        if (parameter == null) {
            return null;
        }

        ReportParameterDTO dto = new ReportParameterDTO();
        dto.setId(parameter.getId());
        dto.setName(parameter.getName());
        dto.setDisplayName(parameter.getDisplayName());
        dto.setDescription(parameter.getDescription());
        dto.setType(parameter.getType());
        dto.setDefaultValue(parameter.getDefaultValue());
        dto.setValue(parameter.getValue());
        dto.setRequired(parameter.isRequired());
        dto.setValidationRegex(parameter.getValidationRegex());
        dto.setValidationMessage(parameter.getValidationMessage());
        dto.setListValues(parameter.getListValues());
        dto.setMinValue(parameter.getMinValue());
        dto.setMaxValue(parameter.getMaxValue());
        dto.setDisplayOrder(parameter.getDisplayOrder());
        
        return dto;
    }

    /**
     * Convert a ReportParameterDTO to a ReportParameter entity.
     *
     * @param dto The ReportParameterDTO to convert
     * @return The corresponding ReportParameter entity
     */
    public ReportParameter toParameterEntity(ReportParameterDTO dto) {
        if (dto == null) {
            return null;
        }

        ReportParameter parameter = new ReportParameter();
        parameter.setName(dto.getName());
        parameter.setDisplayName(dto.getDisplayName());
        parameter.setDescription(dto.getDescription());
        parameter.setType(dto.getType());
        parameter.setDefaultValue(dto.getDefaultValue());
        parameter.setValue(dto.getValue());
        parameter.setRequired(dto.isRequired());
        parameter.setValidationRegex(dto.getValidationRegex());
        parameter.setValidationMessage(dto.getValidationMessage());
        parameter.setListValues(dto.getListValues());
        parameter.setMinValue(dto.getMinValue());
        parameter.setMaxValue(dto.getMaxValue());
        parameter.setDisplayOrder(dto.getDisplayOrder());
        
        return parameter;
    }

    /**
     * Convert a ReportTemplate entity to a TemplateResponseDTO.
     *
     * @param template The ReportTemplate entity to convert
     * @return The corresponding TemplateResponseDTO
     */
    public TemplateResponseDTO toTemplateResponseDTO(ReportTemplate template) {
        if (template == null) {
            return null;
        }

        TemplateResponseDTO dto = new TemplateResponseDTO();
        dto.setId(template.getId());
        dto.setName(template.getName());
        dto.setDescription(template.getDescription());
        dto.setType(template.getType());
        dto.setTemplatePath(template.getTemplatePath());
        dto.setTemplateContent(template.getTemplateContent());
        dto.setDefaultFormat(template.getDefaultFormat());
        dto.setSystemTemplate(template.isSystemTemplate());
        dto.setActive(template.isActive());
        dto.setVersion(template.getVersion());
        dto.setCreatedAt(template.getCreatedAt());
        dto.setCreatedBy(template.getCreatedBy());
        dto.setUpdatedAt(template.getUpdatedAt());
        dto.setUpdatedBy(template.getUpdatedBy());
        
        return dto;
    }

    /**
     * Convert a TemplateCreateDTO to a ReportTemplate entity.
     *
     * @param dto The TemplateCreateDTO to convert
     * @return The corresponding ReportTemplate entity
     */
    public ReportTemplate toTemplateEntity(TemplateCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        ReportTemplate template = new ReportTemplate();
        template.setName(dto.getName());
        template.setDescription(dto.getDescription());
        template.setType(dto.getType());
        template.setTemplateContent(dto.getTemplateContent());
        template.setTemplatePath(dto.getTemplatePath());
        template.setDefaultFormat(dto.getDefaultFormat());
        template.setSystemTemplate(dto.isSystemTemplate());
        template.setActive(dto.isActive());
        template.setVersion(dto.getVersion());
        
        return template;
    }
}
