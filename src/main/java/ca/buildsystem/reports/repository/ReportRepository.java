package ca.buildsystem.reports.repository;

import ca.buildsystem.reports.model.Report;
import ca.buildsystem.reports.model.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing Report entities.
 * Provides methods for CRUD operations and custom queries for reports.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    /**
     * Find reports by their type.
     *
     * @param type The report type to search for
     * @param pageable Pagination information
     * @return A page of reports matching the specified type
     */
    Page<Report> findByType(ReportType type, Pageable pageable);
    
    /**
     * Find reports by client ID.
     *
     * @param clientId The client ID to search for
     * @param pageable Pagination information
     * @return A page of reports associated with the specified client
     */
    Page<Report> findByClientId(UUID clientId, Pageable pageable);
    
    /**
     * Find reports by project ID.
     *
     * @param projectId The project ID to search for
     * @param pageable Pagination information
     * @return A page of reports associated with the specified project
     */
    Page<Report> findByProjectId(UUID projectId, Pageable pageable);
    
    /**
     * Find reports created by a specific user.
     *
     * @param createdBy The username of the creator
     * @param pageable Pagination information
     * @return A page of reports created by the specified user
     */
    Page<Report> findByCreatedBy(String createdBy, Pageable pageable);
    
    /**
     * Find reports that are scheduled.
     *
     * @param pageable Pagination information
     * @return A page of scheduled reports
     */
    Page<Report> findByScheduledTrue(Pageable pageable);
    
    /**
     * Find reports that are due for generation based on their next generation date.
     *
     * @param currentTime The current time to compare against
     * @return A list of reports due for generation
     */
    List<Report> findByScheduledTrueAndNextGenerationBefore(LocalDateTime currentTime);
    
    /**
     * Find reports by template ID.
     *
     * @param templateId The template ID to search for
     * @param pageable Pagination information
     * @return A page of reports using the specified template
     */
    Page<Report> findByTemplateId(UUID templateId, Pageable pageable);
    
    /**
     * Find reports by name containing the search term (case-insensitive).
     *
     * @param searchTerm The search term to look for in report names
     * @param pageable Pagination information
     * @return A page of reports with names containing the search term
     */
    Page<Report> findByNameContainingIgnoreCase(String searchTerm, Pageable pageable);
    
    /**
     * Find reports by date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @param pageable Pagination information
     * @return A page of reports within the specified date range
     */
    @Query("SELECT r FROM Report r WHERE r.startDate >= :startDate AND r.endDate <= :endDate")
    Page<Report> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
    
    /**
     * Find public reports by access token.
     *
     * @param accessToken The access token to search for
     * @return An optional report with the specified access token
     */
    Optional<Report> findByIsPublicTrueAndAccessToken(String accessToken);
    
    /**
     * Find reports by multiple criteria.
     *
     * @param type The report type (optional)
     * @param clientId The client ID (optional)
     * @param projectId The project ID (optional)
     * @param startDate The start date (optional)
     * @param endDate The end date (optional)
     * @param pageable Pagination information
     * @return A page of reports matching the specified criteria
     */
    @Query("SELECT r FROM Report r WHERE " +
           "(:type IS NULL OR r.type = :type) AND " +
           "(:clientId IS NULL OR r.clientId = :clientId) AND " +
           "(:projectId IS NULL OR r.projectId = :projectId) AND " +
           "(:startDate IS NULL OR r.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR r.createdAt <= :endDate)")
    Page<Report> findByMultipleCriteria(
            @Param("type") ReportType type,
            @Param("clientId") UUID clientId,
            @Param("projectId") UUID projectId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}
