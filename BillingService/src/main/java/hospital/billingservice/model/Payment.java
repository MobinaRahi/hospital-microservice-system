package hospital.billingservice.model;

import hospital.billingservice.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a payment made against an invoice.
 * Tracks amount, method, reference number, and receipt.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Payment amount cannot exceed invoice totalAmount</li>
 *   <li>Multiple payments can be made against one invoice (partial payments)</li>
 *   <li>referenceNumber is provided by the payment gateway</li>
 *   <li>receiptNumber is generated internally</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "payments",
        indexes = {
                @Index(name = "idx_payment_invoice", columnList = "invoice_id"),
                @Index(name = "idx_payment_method", columnList = "method"),
                @Index(name = "idx_payment_ref", columnList = "reference_number"),
                @Index(name = "idx_payment_date", columnList = "payment_date"),
                @Index(name = "idx_payment_receipt", columnList = "receipt_number")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The invoice this payment is for.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    /**
     * Amount paid in this transaction.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /**
     * Method of payment (cash, card, insurance, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    /**
     * Reference number from payment gateway or bank.
     */
    @Column(name = "reference_number", length = 200)
    private String referenceNumber;

    /**
     * Date and time of the payment.
     */
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    /**
     * Internal receipt number.
     */
    @Column(name = "receipt_number", length = 100)
    private String receiptNumber;

    /**
     * ID of the user who received this payment.
     */
    @Column(name = "received_by")
    private Long receivedBy;

    /**
     * Additional notes about the payment.
     */
    @Column(length = 500)
    private String notes;

    /**
     * Validates that the payment amount is positive.
     *
     * @return true if amount is greater than zero
     */
    public boolean isValidAmount() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
