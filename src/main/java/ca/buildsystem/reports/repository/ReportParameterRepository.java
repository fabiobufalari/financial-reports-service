package ca.buildsystem.reports.repository;

import ca.buildsystem.reports.model.ParameterType;
import ca.buildsystem.reports.model.ReportParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing ReportParameter entities.
 * Provides methods for CRUD operations and custom queries for report parameters.
 */
@Repository
public interface ReportParameterRepository extends JpaRepository<ReportParameter, UUID> {

    /**
     * Find parameters by report ID.
     *
     * @param reportId The report ID to search for
     * @return A list of parameters associated with the specified report
     */
    List<ReportParameter> findByReportId(UUID reportId);
    
    /**
     * Find parameters by template ID.
     *
     * @param templateId The template ID to search for
     * @return A list of parameters associated with the specified template
     */
    List<ReportParameter> findByTemplateId(UUID templateId);
    
    /**
     * Find parameters by report ID, ordered by display order.
     *
     * @param reportId The report ID to search for
     * @return A list of parameters associated with the specified report, ordered by display order
     */
    List<ReportParameter> findByReportIdOrderByDisplayOrderAsc(UUID reportId);
    
    /**
     * Find parameters by template ID, ordered by display order.
     *
     * @param templateId The template ID to search for
     * @return A list of parameters associated with the specified template, ordered by display order
     */
    List<ReportParameter> findByTemplateIdOrderByDisplayOrderAsc(UUID templateId);
    
    /**
     * Find parameter by report ID and name.
     *
     * @param reportId The report ID to search for
     * @param name The parameter name to search for
     * @return An optional parameter matching the specified report ID and name
     */
    Optional<ReportParameter> findByReportIdAndName(UUID reportId, String name);
    
    /**
     * Find parameter by template ID and name.
     *
     * @param templateId The template ID to search for
     * @param name The parameter name to search for
     * @return An optional parameter matching the specified template ID and name
     */
    Optional<ReportParameter> findByTemplateIdAndName(UUID templateId, String name);
    
    /**
     * Find parameters by type.
     *
     * @param type The parameter type to search for
     * @return A list of parameters with the specified type
     */
    List<ReportParameter> findByType(ParameterType type);
    
    /**
     * Find required parameters for a report.
     *
     * @param reportId The report ID to search for
     * @return A list of required parameters for the specified report
     */
    List<ReportParameter> findByReportIdAndRequiredTrue(UUID reportId);
    
    /**
     * Find required parameters for a template.
     *
     * @param templateId The template ID to search for
     * @return A list of required parameters for the specified template
     */
    List<ReportParameter> findByTemplateIdAndRequiredTrue(UUID templateId);
    
    /**
     * Check if all required parameters for a report have values.
     *
     * @param reportId The report ID to check
     * @return True if all required parameters have values, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(p) = 0 THEN true ELSE false END FROM ReportParameter p " +
           "WHERE p.report.id = :reportId AND p.required = true AND (p.value IS NULL OR p.value = '')")
    boolean areAllRequiredParametersFilledForReport(@Param("reportId") UUID reportId);
    
    /**
     * Delete all parameters for a report.
     *
     * @param reportId The report ID to delete parameters for
     */
    void deleteByReportId(UUID reportId);
    
    /**
     * Delete all parameters for a template.
     *
     * @param templateId The template ID to delete parameters for
     */
    void deleteByTemplateId(UUID templateId);
}
