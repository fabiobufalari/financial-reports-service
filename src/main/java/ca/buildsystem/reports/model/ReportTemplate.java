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

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entity representing a report template in the system.
 * Templates define the structure and parameters for generating reports.
 */
@Entity
@Table(name = "report_templates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ReportTemplate {

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

    @Column(name = "template_path")
    private String templatePath;

    @Column(name = "template_content", columnDefinition = "TEXT")
    private String templateContent;

    @Column(name = "default_format")
    @Enumerated(EnumType.STRING)
    private ReportFormat defaultFormat;

    @Column(name = "is_system_template")
    private boolean systemTemplate;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "version")
    private String version;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReportParameter> parameters = new HashSet<>();

    @OneToMany(mappedBy = "template")
    private Set<Report> reports = new HashSet<>();

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
     * Adds a parameter to this template.
     *
     * @param parameter The parameter to add
     * @return This template instance for method chaining
     */
    public ReportTemplate addParameter(ReportParameter parameter) {
        parameters.add(parameter);
        parameter.setTemplate(this);
        return this;
    }

    /**
     * Removes a parameter from this template.
     *
     * @param parameter The parameter to remove
     * @return This template instance for method chaining
     */
    public ReportTemplate removeParameter(ReportParameter parameter) {
        parameters.remove(parameter);
        parameter.setTemplate(null);
        return this;
    }
}
