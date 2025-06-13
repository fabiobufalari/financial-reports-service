package ca.buildsystem.reports.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entity representing a financial report in the system.
 * Reports can be generated on-demand or scheduled, and can be exported in various formats.
 */
@Entity
@Table(name = "reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportFormat format;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "is_scheduled")
    private boolean scheduled;

    @Column(name = "schedule_cron")
    private String scheduleCron;

    @Column(name = "last_generated")
    private LocalDateTime lastGenerated;

    @Column(name = "next_generation")
    private LocalDateTime nextGeneration;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private ReportTemplate template;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReportParameter> parameters = new HashSet<>();

    @Column(name = "project_id")
    private UUID projectId;

    @Column(name = "client_id")
    private UUID clientId;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "status")
    private String status;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * Adds a parameter to this report.
     *
     * @param parameter The parameter to add
     * @return This report instance for method chaining
     */
    public Report addParameter(ReportParameter parameter) {
        parameters.add(parameter);
        parameter.setReport(this);
        return this;
    }

    /**
     * Removes a parameter from this report.
     *
     * @param parameter The parameter to remove
     * @return This report instance for method chaining
     */
    public Report removeParameter(ReportParameter parameter) {
        parameters.remove(parameter);
        parameter.setReport(null);
        return this;
    }
}
