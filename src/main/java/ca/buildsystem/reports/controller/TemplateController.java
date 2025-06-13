package ca.buildsystem.reports.controller;

import ca.buildsystem.reports.dto.TemplateCreateDTO;
import ca.buildsystem.reports.dto.TemplateResponseDTO;
import ca.buildsystem.reports.model.ReportType;
import ca.buildsystem.reports.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing report templates.
 * Provides endpoints for creating, retrieving, updating, and deleting templates.
 */
@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Template Controller", description = "API for managing report templates")
public class TemplateController {

    private final TemplateService templateService;

    /**
     * GET /api/templates : Get all templates with pagination.
     *
     * @param pageable Pagination information
     * @return A page of templates
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER', 'ACCOUNTANT')")
    @Operation(summary = "Get all templates", description = "Returns a paginated list of all templates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved templates",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Page<TemplateResponseDTO>> getAllTemplates(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get all Templates");
        Page<TemplateResponseDTO> page = templateService.getAllTemplates(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * POST /api/templates : Create a new template.
     *
     * @param createDTO The template creation data
     * @return The created template
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER')")
    @Operation(summary = "Create a new template", description = "Creates a new template and returns it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Template created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<TemplateResponseDTO> createTemplate(
            @Parameter(description = "Template creation data", required = true)
            @Valid @RequestBody TemplateCreateDTO createDTO) {
        log.info("REST request to create Template : {}", createDTO.getName());
        TemplateResponseDTO result = templateService.createTemplate(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * GET /api/templates/{id} : Get a template by ID.
     *
     * @param id The template ID
     * @return The template
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER', 'ACCOUNTANT')")
    @Operation(summary = "Get a template by ID", description = "Returns a template based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved template"),
            @ApiResponse(responseCode = "404", description = "Template not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<TemplateResponseDTO> getTemplateById(
            @Parameter(description = "Template ID", required = true)
            @PathVariable UUID id) {
        log.info("REST request to get Template : {}", id);
        return templateService.getTemplateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/templates/{id} : Update an existing template.
     *
     * @param id The template ID
     * @param updateDTO The template update data
     * @return The updated template
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER')")
    @Operation(summary = "Update an existing template", description = "Updates an existing template and returns it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Template not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<TemplateResponseDTO> updateTemplate(
            @Parameter(description = "Template ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Template update data", required = true)
            @Valid @RequestBody TemplateCreateDTO updateDTO) {
        log.info("REST request to update Template : {}", id);
        try {
            TemplateResponseDTO result = templateService.updateTemplate(id, updateDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error updating template", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/templates/{id} : Delete a template.
     *
     * @param id The template ID
     * @return No content if successful
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER')")
    @Operation(summary = "Delete a template", description = "Deletes a template by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Template deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Template not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Void> deleteTemplate(
            @Parameter(description = "Template ID", required = true)
            @PathVariable UUID id) {
        log.info("REST request to delete Template : {}", id);
        try {
            templateService.deleteTemplate(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting template", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/templates/type/{type} : Get templates by type.
     *
     * @param type The template type
     * @param pageable Pagination information
     * @return A page of templates
     */
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER', 'ACCOUNTANT')")
    @Operation(summary = "Get templates by type", description = "Returns a paginated list of templates by type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved templates"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Page<TemplateResponseDTO>> getTemplatesByType(
            @Parameter(description = "Template type", required = true)
            @PathVariable ReportType type,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get Templates by type : {}", type);
        Page<TemplateResponseDTO> page = templateService.findTemplatesByType(type, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * GET /api/templates/active : Get active templates.
     *
     * @param pageable Pagination information
     * @return A page of active templates
     */
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER', 'ACCOUNTANT')")
    @Operation(summary = "Get active templates", description = "Returns a paginated list of active templates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved templates"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Page<TemplateResponseDTO>> getActiveTemplates(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get active Templates");
        Page<TemplateResponseDTO> page = templateService.findActiveTemplates(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * GET /api/templates/active-non-system : Get all active non-system templates.
     *
     * @return A list of active non-system templates
     */
    @GetMapping("/active-non-system")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCIAL_MANAGER', 'ACCOUNTANT')")
    @Operation(summary = "Get active non-system templates", description = "Returns a list of all active non-system templates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved templates"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<TemplateResponseDTO>> getActiveNonSystemTemplates() {
        log.info("REST request to get active non-system Templates");
        List<TemplateResponseDTO> templates = templateService.findActiveNonSystemTemplates();
        return ResponseEntity.ok(templates);
    }
}
