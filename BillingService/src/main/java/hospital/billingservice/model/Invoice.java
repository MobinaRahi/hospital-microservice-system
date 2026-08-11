package hospital.billingservice.model;

import hospital.billingservice.model.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a billing invoice for a patient.
 * Contains all financial details: subtotal, discount, tax, insurance coverage,
 * patient share, and total amount.
 *
 * <p><strong>Status Lifecycle:</strong></p>
 * <pre>
 * PENDING → PARTIAL → PAID
 *   ↑                   ↓
 *   └── REFUNDED ← CANCELLED
 *   OVERDUE (auto-detected based on dueDate)
 * </pre>
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>invoiceNumber must be unique</li>
 *   <li>totalAmount = subtotal - discount + tax - insuranceCoverage</li>
 *   <li>patientShare = totalAmount - insuranceCoverage</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "invoices",
        indexes = {
                @Index(name = "idx_invoice_number", columnList = "invoice_number", unique = true),
                @Index(name = "idx_invoice_patient", columnList = "patient_id"),
                @Index(name = "idx_invoice_encounter", columnList = "encounter_id"),
                @Index(name = "idx_invoice_status", columnList = "status"),
                @Index(name = "idx_invoice_issue", columnList = "issue_date"),
                @Index(name = "idx_invoice_due", columnList = "due_date")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Invoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique invoice number (e.g., "INV-2026-0001").
     */
    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;

    /**
     * Patient ID from CoreService.
     */
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    /**
     * Encounter ID from ClinicalService (nullable for non-visit invoices).
     */
    @Column(name = "encounter_id")
    private Long encounterId;

    /**
     * Date the invoice was issued.
     */
    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    /**
     * Due date for payment.
     */
    @Column(name = "due_date")
    private LocalDate dueDate;

    /**
     * Subtotal before discount and tax.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal subtotal = BigDecimal.ZERO;

    /**
     * Discount amount applied to the invoice.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO ;
    /**
     * Tax amount applied to the invoice.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal tax = BigDecimal.ZERO;

    /**
     * Amount covered by insurance.
     */
    @Column(name = "insurance_coverage", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal insuranceCoverage =BigDecimal.ZERO;

    /**
     * Amount the patient must pay (totalAmount - insuranceCoverage).
     */
    @Column(name = "patient_share", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal patientShare = BigDecimal.ZERO;

    /**
     * Total amount due from the patient after insurance.
     */
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * Current status of the invoice.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private InvoiceStatus status = InvoiceStatus.PENDING;

    /**
     * Additional notes on the invoice.
     */
    @Column(length = 1000)
    private String notes;

    /**
     * ID of the user who created this invoice.
     */
    @Column(name = "created_by")
    private Long createdByUser;

    /**
     * Line items on this invoice.
     */
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();

    /**
     * Payments made against this invoice.
     */
    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    /**
     * Checks if this invoice is overdue.
     *
     * @return true if dueDate has passed and status is not PAID or CANCELLED
     */
    public boolean isOverdue() {
        if (dueDate == null) return false;
        if (status == InvoiceStatus.PAID || status == InvoiceStatus.CANCELLED) return false;
        return dueDate.isBefore(LocalDate.now());
    }

    /**
     * Recalculates totalAmount and patientShare from components.
     * totalAmount = subtotal - discount + tax
     * patientShare = totalAmount - insuranceCoverage
     */
    public void recalculateTotals() {
        this.totalAmount = subtotal
                .subtract(discount != null ? discount : BigDecimal.ZERO)
                .add(tax != null ? tax : BigDecimal.ZERO);
        this.patientShare = totalAmount
                .subtract(insuranceCoverage != null ? insuranceCoverage : BigDecimal.ZERO);
    }

    /**
     * Checks if the invoice can be cancelled.
     *
     * @return true if status is PENDING or PARTIAL
     */
    public boolean canBeCancelled() {
        return status == InvoiceStatus.PENDING || status == InvoiceStatus.PARTIAL;
    }
}
