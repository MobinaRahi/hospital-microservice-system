package hospital.billingservice.controller;

import hospital.billingservice.dto.invoice.InvoiceCreateDto;
import hospital.billingservice.dto.invoice.InvoiceResponseDto;
import hospital.billingservice.dto.invoice.InvoiceUpdateDto;
import hospital.billingservice.dto.response.ApiResponse;
import hospital.billingservice.model.enums.InvoiceStatus;
import hospital.billingservice.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for Invoice management.
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * PENDING → PARTIAL → PAID
 *   ↑                   ↓
 *   └── REFUNDED ← CANCELLED
 * </pre>
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Accountant/Admin: Full CRUD access</li>
 *   <li>Patient: Read own invoices</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/billing/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoice Management", description = "Invoice CRUD and status management APIs")
public class InvoiceApi {

    private final InvoiceService invoiceService;

    @PostMapping
    @Operation(summary = "Create a new invoice with items")
    public ResponseEntity<ApiResponse<InvoiceResponseDto>> createInvoice(@Valid @RequestBody InvoiceCreateDto createDto) {
        InvoiceResponseDto created = invoiceService.createInvoice(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Invoice created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an invoice by ID")
    public ResponseEntity<ApiResponse<InvoiceResponseDto>> updateInvoice(@PathVariable Long id, @Valid @RequestBody InvoiceUpdateDto updateDto) {
        InvoiceResponseDto updated = invoiceService.updateInvoice(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Invoice updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel an invoice")
    public ResponseEntity<ApiResponse<InvoiceResponseDto>> cancelInvoice(@PathVariable Long id) {
        InvoiceResponseDto cancelled = invoiceService.cancelInvoice(id);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Invoice cancelled successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/mark-paid")
    @Operation(summary = "Mark an invoice as paid")
    public ResponseEntity<ApiResponse<InvoiceResponseDto>> markAsPaid(@PathVariable Long id) {
        InvoiceResponseDto paid = invoiceService.markAsPaid(id);
        return ResponseEntity.ok(ApiResponse.success(paid, "Invoice marked as paid", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/update-status-from-payments")
    @Operation(summary = "Update invoice status based on payment amounts")
    public ResponseEntity<ApiResponse<InvoiceResponseDto>> updateStatusFromPayments(@PathVariable Long id) {
        InvoiceResponseDto updated = invoiceService.updateInvoiceStatusFromPayments(id);
        return ResponseEntity.ok(ApiResponse.success(updated, "Invoice status updated successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get invoice by ID")
    public ResponseEntity<ApiResponse<InvoiceResponseDto>> getInvoiceById(@PathVariable Long id) {
        InvoiceResponseDto invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(ApiResponse.success(invoice, "Invoice retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/number/{invoiceNumber}")
    @Operation(summary = "Get invoice by invoice number")
    public ResponseEntity<ApiResponse<InvoiceResponseDto>> getInvoiceByNumber(@PathVariable String invoiceNumber) {
        InvoiceResponseDto invoice = invoiceService.getInvoiceByNumber(invoiceNumber);
        return ResponseEntity.ok(ApiResponse.success(invoice, "Invoice retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get all invoices for a patient")
    public ResponseEntity<ApiResponse<List<InvoiceResponseDto>>> getInvoicesByPatient(@PathVariable Long patientId) {
        List<InvoiceResponseDto> invoices = invoiceService.getInvoicesByPatient(patientId);
        return ResponseEntity.ok(ApiResponse.success(invoices, "Invoices retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get invoices by status")
    public ResponseEntity<ApiResponse<List<InvoiceResponseDto>>> getInvoicesByStatus(@PathVariable InvoiceStatus status) {
        List<InvoiceResponseDto> invoices = invoiceService.getInvoicesByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(invoices, "Invoices retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue invoices")
    public ResponseEntity<ApiResponse<List<InvoiceResponseDto>>> getOverdueInvoices() {
        List<InvoiceResponseDto> invoices = invoiceService.getOverdueInvoices();
        return ResponseEntity.ok(ApiResponse.success(invoices, "Overdue invoices retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/overdue/as-of")
    @Operation(summary = "Get overdue invoices as of a specific date")
    public ResponseEntity<ApiResponse<List<InvoiceResponseDto>>> getOverdueInvoicesAsOf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<InvoiceResponseDto> invoices = invoiceService.getOverdueInvoicesAsOf(date);
        return ResponseEntity.ok(ApiResponse.success(invoices, "Overdue invoices retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get invoices by date range")
    public ResponseEntity<ApiResponse<List<InvoiceResponseDto>>> getInvoicesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<InvoiceResponseDto> invoices = invoiceService.getInvoicesByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(invoices, "Invoices retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/encounter/{encounterId}")
    @Operation(summary = "Get invoices for a specific encounter")
    public ResponseEntity<ApiResponse<List<InvoiceResponseDto>>> getInvoicesByEncounter(@PathVariable Long encounterId) {
        List<InvoiceResponseDto> invoices = invoiceService.getInvoicesByEncounter(encounterId);
        return ResponseEntity.ok(ApiResponse.success(invoices, "Invoices retrieved successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete an invoice")
    public ResponseEntity<ApiResponse<Void>> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Invoice deleted successfully", HttpStatus.OK.value()));
    }
}
