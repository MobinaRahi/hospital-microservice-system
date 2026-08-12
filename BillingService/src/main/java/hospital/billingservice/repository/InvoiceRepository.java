package hospital.billingservice.repository;

import hospital.billingservice.model.Invoice;
import hospital.billingservice.model.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Invoice entity.
 *
 * @author MobinaRahi
 */
@Repository
public interface InvoiceRepository extends BaseEntityRepository<Invoice, Long> {

    /**
     * Finds an invoice by its unique invoice number.
     *
     * @param invoiceNumber the invoice number
     * @return invoice if found
     */
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    /**
     * Finds all invoices for a specific patient.
     *
     * @param patientId the patient ID
     * @return list of invoices for the patient
     */
    List<Invoice> findByPatientId(Long patientId);

    /**
     * Finds invoices by status.
     *
     * @param status the invoice status
     * @return list of invoices with the status
     */
    List<Invoice> findByStatus(InvoiceStatus status);

    /**
     * Finds invoices for a specific encounter.
     *
     * @param encounterId the encounter ID from ClinicalService
     * @return list of invoices for the encounter
     */
    List<Invoice> findByEncounterId(Long encounterId);

    /**
     * Finds overdue invoices (due date passed, not paid or cancelled).
     * An invoice is overdue if dueDate < today AND status is PENDING or PARTIAL.
     *
     * @param today current date
     * @return list of overdue invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.dueDate < :today AND i.status IN ('PENDING', 'PARTIAL')")
    List<Invoice> findOverdueInvoices(@Param("today") LocalDate today);

    /**
     * Finds invoices issued within a date range.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of invoices in the range
     */
    List<Invoice> findByIssueDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Checks if an invoice number already exists.
     *
     * @param invoiceNumber the invoice number to check
     * @return true if exists
     */
    boolean existsByInvoiceNumber(String invoiceNumber);

    /**
     * Finds invoices created by a specific user.
     *
     * @param createdByUser the user ID who created the invoice
     * @return list of invoices created by the user
     */
    List<Invoice> findByCreatedByUser(Long createdByUser);
}
