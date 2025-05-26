package ca.buildsystem.reports.service;

import ca.buildsystem.reports.dto.TemplateCreateDTO;
import ca.buildsystem.reports.dto.TemplateResponseDTO;
import ca.buildsystem.reports.exception.ResourceNotFoundException;
import ca.buildsystem.reports.model.ReportTemplate;
import ca.buildsystem.reports.model.ReportType;
import ca.buildsystem.reports.repository.ReportTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing report templates.
 * Handles template creation, retrieval, updating, and deletion.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateService {

    private final ReportTemplateRepository templateRepository;
    private final ReportMapper reportMapper;

    /**
     * Retrieve all templates with pagination.
     *
     * @param pageable Pagination information
     * @return A page of template DTOs
     */
    @Transactional(readOnly = true)
    public Page<TemplateResponseDTO> getAllTemplates(Pageable pageable) {
        log.info("Retrieving all templates with pagination: {}", pageable);
        return templateRepository.findAll(pageable)
                .map(reportMapper::toTemplateResponseDTO);
    }

    /**
     * Retrieve a template by its ID.
     *
     * @param id The template ID
     * @return An optional containing the template DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<TemplateResponseDTO> getTemplateById(UUID id) {
        log.info("Retrieving template with ID: {}", id);
        return templateRepository.findById(id)
                .map(reportMapper::toTemplateResponseDTO);
    }

    /**
     * Create a new template.
     *
     * @param createDTO The template creation data
     * @return The created template DTO
     */
    @Transactional
    public TemplateResponseDTO createTemplate(TemplateCreateDTO createDTO) {
        log.info("Creating new template: {}", createDTO.getName());
        
        ReportTemplate template = reportMapper.toTemplateEntity(createDTO);
        
        // Set default values if not provided
        if (template.getDefaultFormat() == null) {
            template.setDefaultFormat(createDTO.getDefaultFormat());
        }
        
        // Set active by default
        template.setActive(true);
        
        ReportTemplate savedTemplate = templateRepository.save(template);
        
        return reportMapper.toTemplateResponseDTO(savedTemplate);
    }

    /**
     * Update an existing template.
     *
     * @param id The template ID
     * @param updateDTO The template update data
     * @return The updated template DTO
     * @throws ResourceNotFoundException if the template is not found
     */
    @Transactional
    public TemplateResponseDTO updateTemplate(UUID id, TemplateCreateDTO updateDTO) {
        log.info("Updating template with ID: {}", id);
        
        ReportTemplate existingTemplate = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template", "id", id));
        
        // Update fields from DTO
        existingTemplate.setName(updateDTO.getName());
        existingTemplate.setDescription(updateDTO.getDescription());
        existingTemplate.setType(updateDTO.getType());
        existingTemplate.setTemplateContent(updateDTO.getTemplateContent());
        
        if (updateDTO.getTemplatePath() != null) {
            existingTemplate.setTemplatePath(updateDTO.getTemplatePath());
        }
        
        if (updateDTO.getDefaultFormat() != null) {
            existingTemplate.setDefaultFormat(updateDTO.getDefaultFormat());
        }
        
        existingTemplate.setSystemTemplate(updateDTO.isSystemTemplate());
        existingTemplate.setActive(updateDTO.isActive());
        
        if (updateDTO.getVersion() != null) {
            existingTemplate.setVersion(updateDTO.getVersion());
        }
        
        ReportTemplate updatedTemplate = templateRepository.save(existingTemplate);
        return reportMapper.toTemplateResponseDTO(updatedTemplate);
    }

    /**
     * Delete a template by its ID.
     *
     * @param id The template ID
     * @throws ResourceNotFoundException if the template is not found
     */
    @Transactional
    public void deleteTemplate(UUID id) {
        log.info("Deleting template with ID: {}", id);
        
        if (!templateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Template", "id", id);
        }
        
        templateRepository.deleteById(id);
    }

    /**
     * Find templates by type with pagination.
     *
     * @param type The template type
     * @param pageable Pagination information
     * @return A page of template DTOs
     */
    @Transactional(readOnly = true)
    public Page<TemplateResponseDTO> findTemplatesByType(ReportType type, Pageable pageable) {
        log.info("Finding templates by type: {}", type);
        return templateRepository.findByType(type, pageable)
                .map(reportMapper::toTemplateResponseDTO);
    }

    /**
     * Find active templates with pagination.
     *
     * @param pageable Pagination information
     * @return A page of active template DTOs
     */
    @Transactional(readOnly = true)
    public Page<TemplateResponseDTO> findActiveTemplates(Pageable pageable) {
        log.info("Finding active templates");
        return templateRepository.findByActiveTrue(pageable)
                .map(reportMapper::toTemplateResponseDTO);
    }

    /**
     * Find all active non-system templates.
     *
     * @return A list of active non-system template DTOs
     */
    @Transactional(readOnly = true)
    public List<TemplateResponseDTO> findActiveNonSystemTemplates() {
        log.info("Finding active non-system templates");
        return templateRepository.findByActiveTrueAndSystemTemplateFalse()
                .stream()
                .map(reportMapper::toTemplateResponseDTO)
                .collect(Collectors.toList());
    }
}
