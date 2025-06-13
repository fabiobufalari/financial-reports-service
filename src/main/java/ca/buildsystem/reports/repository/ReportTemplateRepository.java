package ca.buildsystem.reports.repository;

import ca.buildsystem.reports.model.ReportTemplate;
import ca.buildsystem.reports.model.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing ReportTemplate entities.
 * Provides methods for CRUD operations and custom queries for report templates.
 */
@Repository
public interface ReportTemplateRepository extends JpaRepository<ReportTemplate, UUID> {

    /**
     * Find templates by their type.
     *
     * @param type The report type to search for
     * @param pageable Pagination information
     * @return A page of templates matching the specified type
     */
    Page<ReportTemplate> findByType(ReportType type, Pageable pageable);
    
    /**
     * Find active templates.
     *
     * @param pageable Pagination information
     * @return A page of active templates
     */
    Page<ReportTemplate> findByActiveTrue(Pageable pageable);
    
    /**
     * Find system templates.
     *
     * @param pageable Pagination information
     * @return A page of system templates
     */
    Page<ReportTemplate> findBySystemTemplateTrue(Pageable pageable);
    
    /**
     * Find active templates by type.
     *
     * @param type The report type to search for
     * @param pageable Pagination information
     * @return A page of active templates matching the specified type
     */
    Page<ReportTemplate> findByTypeAndActiveTrue(ReportType type, Pageable pageable);
    
    /**
     * Find templates by name containing the search term (case-insensitive).
     *
     * @param searchTerm The search term to look for in template names
     * @param pageable Pagination information
     * @return A page of templates with names containing the search term
     */
    Page<ReportTemplate> findByNameContainingIgnoreCase(String searchTerm, Pageable pageable);
    
    /**
     * Find template by name (exact match, case-insensitive).
     *
     * @param name The template name to search for
     * @return An optional template with the specified name
     */
    Optional<ReportTemplate> findByNameIgnoreCase(String name);
    
    /**
     * Find templates by creator.
     *
     * @param createdBy The username of the creator
     * @param pageable Pagination information
     * @return A page of templates created by the specified user
     */
    Page<ReportTemplate> findByCreatedBy(String createdBy, Pageable pageable);
    
    /**
     * Find templates by version.
     *
     * @param version The version to search for
     * @param pageable Pagination information
     * @return A page of templates with the specified version
     */
    Page<ReportTemplate> findByVersion(String version, Pageable pageable);
    
    /**
     * Find all active non-system templates.
     *
     * @return A list of active non-system templates
     */
    List<ReportTemplate> findByActiveTrueAndSystemTemplateFalse();
}
