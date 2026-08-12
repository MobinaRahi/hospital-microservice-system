package hospital.billingservice.service;

import hospital.billingservice.dto.invoice.InvoiceCreateDto;
import hospital.billingservice.dto.invoice.InvoiceResponseDto;
import hospital.billingservice.dto.invoice.InvoiceUpdateDto;
import hospital.billingservice.model.enums.InvoiceStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Invoice.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Invoice number must be unique</li>
 *   <li>totalAmount = subtotal - discount + tax</li>
 *   <li>patientShare = totalAmount - insuranceCoverage</li>
 *   <li>Status workflow: PENDING → PARTIAL → PAID</li>
 *   <li>An invoice is overdue if dueDate has passed and status is PENDING or PARTIAL</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface InvoiceService {

    // ══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Creates a new invoice with items.
     * <p>Automatically calculates totalAmount and patientShare.</p>
     *
     * @param dto the invoice creation data
     * @return the created invoice
     */
    InvoiceResponseDto createInvoice(InvoiceCreateDto dto);

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ══════════════════════════════════════════════════════════════════

    /**
     * Gets an invoice by its ID.
     *
     * @param id the invoice ID
     * @return the invoice
     */
    InvoiceResponseDto getInvoiceById(Long id);

    /**
     * Gets an invoice by its unique invoice number.
     *
     * @param invoiceNumber the invoice number
     * @return the invoice
     */
    InvoiceResponseDto getInvoiceByNumber(String invoiceNumber);

    /**
     * Gets all invoices for a specific patient.
     *
     * @param patientId the patient ID
     * @return list of invoices for the patient
     */
    List<InvoiceResponseDto> getInvoicesByPatient(Long patientId);

    /**
     * Gets invoices by status.
     *
     * @param status the invoice status
     * @return list of invoices with the status
     */
    List<InvoiceResponseDto> getInvoicesByStatus(InvoiceStatus status);

    /**
     * Gets overdue invoices (due date passed, not paid or cancelled).
     *
     * @return list of overdue invoices
     */
    List<InvoiceResponseDto> getOverdueInvoices();

    /**
     * Gets overdue invoices as of a specific date.
     *
     * @param date the date to check against
     * @return list of overdue invoices
     */
    List<InvoiceResponseDto> getOverdueInvoicesAsOf(LocalDate date);

    /**
     * Gets invoices issued within a date range.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of invoices in the range
     */
    List<InvoiceResponseDto> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Gets invoices for a specific encounter.
     *
     * @param encounterId the encounter ID from ClinicalService
     * @return list of invoices for the encounter
     */
    List<InvoiceResponseDto> getInvoicesByEncounter(Long encounterId);

    // ══════════════════════════════════════════════════════════════════
    // Update
    // ══════════════════════════════════════════════════════════════════

    /**
     * Updates an existing invoice.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the invoice ID
     * @param dto the update data
     * @return the updated invoice
     */
    InvoiceResponseDto updateInvoice(Long id, InvoiceUpdateDto dto);

    // ═══════════════════════════════════════════════════════════════════
    // Status Transitions
    // ══════════════════════════════════════════════════════════════════

    /**
     * Cancels an invoice.
     * Can only cancel PENDING or PARTIAL invoices.
     *
     * @param id the invoice ID
     * @return the cancelled invoice
     */
    InvoiceResponseDto cancelInvoice(Long id);

    /**
     * Marks an invoice as paid.
     * Status transition: PARTIAL or PENDING → PAID
     *
     * @param id the invoice ID
     * @return the updated invoice
     */
    InvoiceResponseDto markAsPaid(Long id);

    /**
     * Updates the invoice status based on payment amounts.
     * If total payments >= totalAmount → PAID
     * If total payments > 0 → PARTIAL
     *
     * @param id the invoice ID
     * @return the updated invoice
     */
    InvoiceResponseDto updateInvoiceStatusFromPayments(Long id);

    // ══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes an invoice.
     *
     * @param id the invoice ID
     */
    void deleteInvoice(Long id);

    // ══════════════════════════════════════════════════════════════════
    // Validation
    // ══════════════════════════════════════════════════════════════════

    /**
     * Checks if an invoice number is already in use.
     *
     * @param invoiceNumber the invoice number to check
     * @return true if the number exists
     */
    boolean invoiceNumberExists(String invoiceNumber);
}
