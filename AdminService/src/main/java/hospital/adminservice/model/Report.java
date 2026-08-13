package hospital.adminservice.model;

import hospital.adminservice.model.enums.ReportStatus;
import hospital.adminservice.model.enums.ReportType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents a generated report in the system.
 * Tracks report generation status, parameters, and output file.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Reports can be generated on-demand or scheduled</li>
 *   <li>Each report has a type that determines its content</li>
 *   <li>Parameters are stored as JSON string for flexibility</li>
 *   <li>Generated files are stored with a URL reference</li>
 *   <li>Report status tracks the generation lifecycle</li>
 * </ul>
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * PENDING → PROCESSING → COMPLETED
 *                       → FAILED
 * COMPLETED → EXPIRED (after retention period)
 * </pre>
 *
 * <p><strong>Report Types:</strong></p>
 * <ul>
 *   <li>PATIENT_STATS: Patient statistics and demographics</li>
 *   <li>APPOINTMENT_STATS: Appointment analytics</li>
 *   <li>FINANCIAL_DAILY/MONTHLY/YEARLY: Financial reports</li>
 *   <li>DOCTOR_PERFORMANCE: Doctor productivity metrics</li>
 *   <li>BED_OCCUPANCY: Bed utilization reports</li>
 *   <li>REVENUE_BY_INSURANCE/SERVICE: Revenue breakdowns</li>
 *   <li>COLLECTION_REPORT: Payment collection reports</li>
 *   <li>DELAYED_PAYMENTS: Outstanding payments report</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "reports",
        indexes = {
                @Index(name = "idx_report_type", columnList = "type"),
                @Index(name = "idx_report_status", columnList = "status"),
                @Index(name = "idx_report_generated_by", columnList = "generated_by"),
                @Index(name = "idx_report_generated_at", columnList = "generated_at")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Human-readable name of the report.
     * Example: "Monthly Revenue Report - March 2026"
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Type of report to generate.
     * Determines the report content and format.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    /**
     * Report parameters stored as JSON string.
     * Example: {"startDate": "2026-03-01", "endDate": "2026-03-31", "departmentId": 5}
     */
    @Column(columnDefinition = "TEXT")
    private String parameters;

    /**
     * User ID from AuthService who requested/generated the report.
     */
    @Column(name = "generated_by")
    private Long generatedBy;

    /**
     * When the report was generated.
     */
    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    /**
     * URL to the generated report file.
     * Example: "/reports/2026/monthly-revenue-march.pdf"
     */
    @Column(name = "file_url", length = 500)
    private String fileUrl;

    /**
     * Current status of the report generation.
     * Default: PENDING
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ReportStatus status = ReportStatus.PENDING;

    /**
     * Checks if the report generation is completed.
     *
     * @return true if status is COMPLETED
     */
    public boolean isCompleted() {
        return status == ReportStatus.COMPLETED;
    }

    /**
     * Checks if the report generation failed.
     *
     * @return true if status is FAILED
     */
    public boolean isFailed() {
        return status == ReportStatus.FAILED;
    }

    /**
     * Checks if the report is currently being processed.
     *
     * @return true if status is PROCESSING
     */
    public boolean isProcessing() {
        return status == ReportStatus.PROCESSING;
    }

    /**
     * Marks the report as being processed.
     * Sets status to PROCESSING and records generation start time.
     */
    public void markAsProcessing() {
        this.status = ReportStatus.PROCESSING;
        this.generatedAt = LocalDateTime.now();
    }

    /**
     * Marks the report as successfully completed.
     * Sets status to COMPLETED and stores the file URL.
     *
     * @param fileUrl the URL to the generated report file
     */
    public void markAsCompleted(String fileUrl) {
        this.status = ReportStatus.COMPLETED;
        this.fileUrl = fileUrl;
    }

    /**
     * Marks the report generation as failed.
     * Sets status to FAILED.
     */
    public void markAsFailed() {
        this.status = ReportStatus.FAILED;
    }
}
