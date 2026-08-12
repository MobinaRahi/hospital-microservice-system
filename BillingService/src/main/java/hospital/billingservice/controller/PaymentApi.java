package hospital.billingservice.controller;

import hospital.billingservice.dto.payment.PaymentCreateDto;
import hospital.billingservice.dto.payment.PaymentResponseDto;
import hospital.billingservice.dto.payment.PaymentUpdateDto;
import hospital.billingservice.dto.response.ApiResponse;
import hospital.billingservice.model.enums.PaymentMethod;
import hospital.billingservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for Payment management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Payment amount cannot exceed remaining invoice balance</li>
 *   <li>Multiple payments can be made against one invoice (partial payments)</li>
 *   <li>Registering a payment auto-updates the invoice status</li>
 * </ul>
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Accountant/Admin: Full CRUD access</li>
 *   <li>Patient: Read own payments</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/billing/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Management", description = "Payment CRUD and invoice balance APIs")
public class PaymentApi {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Create a new payment record")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> createPayment(@Valid @RequestBody PaymentCreateDto createDto) {
        PaymentResponseDto created = paymentService.createPayment(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Payment recorded successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a payment record by ID")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> updatePayment(@PathVariable Long id, @Valid @RequestBody PaymentUpdateDto updateDto) {
        PaymentResponseDto updated = paymentService.updatePayment(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Payment updated successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> getPaymentById(@PathVariable Long id) {
        PaymentResponseDto payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.success(payment, "Payment retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/invoice/{invoiceId}")
    @Operation(summary = "Get all payments for an invoice")
    public ResponseEntity<ApiResponse<List<PaymentResponseDto>>> getPaymentsByInvoice(@PathVariable Long invoiceId) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByInvoice(invoiceId);
        return ResponseEntity.ok(ApiResponse.success(payments, "Payments retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/method/{method}")
    @Operation(summary = "Get payments by payment method")
    public ResponseEntity<ApiResponse<List<PaymentResponseDto>>> getPaymentsByMethod(@PathVariable PaymentMethod method) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByMethod(method);
        return ResponseEntity.ok(ApiResponse.success(payments, "Payments retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/reference/{referenceNumber}")
    @Operation(summary = "Get payment by reference number (from payment gateway)")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> getPaymentByReferenceNumber(@PathVariable String referenceNumber) {
        PaymentResponseDto payment = paymentService.getPaymentByReferenceNumber(referenceNumber);
        return ResponseEntity.ok(ApiResponse.success(payment, "Payment retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/receipt/{receiptNumber}")
    @Operation(summary = "Get payment by receipt number")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> getPaymentByReceiptNumber(@PathVariable String receiptNumber) {
        PaymentResponseDto payment = paymentService.getPaymentByReceiptNumber(receiptNumber);
        return ResponseEntity.ok(ApiResponse.success(payment, "Payment retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get payments within a date range")
    public ResponseEntity<ApiResponse<List<PaymentResponseDto>>> getPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(payments, "Payments retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/invoice/{invoiceId}/total-paid")
    @Operation(summary = "Get total amount paid for an invoice")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalPaidForInvoice(@PathVariable Long invoiceId) {
        BigDecimal totalPaid = paymentService.getTotalPaidForInvoice(invoiceId);
        return ResponseEntity.ok(ApiResponse.success(totalPaid, "Total paid retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/invoice/{invoiceId}/remaining-balance")
    @Operation(summary = "Get remaining balance for an invoice")
    public ResponseEntity<ApiResponse<BigDecimal>> getRemainingBalance(@PathVariable Long invoiceId) {
        BigDecimal remainingBalance = paymentService.getRemainingBalance(invoiceId);
        return ResponseEntity.ok(ApiResponse.success(remainingBalance, "Remaining balance retrieved successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a payment record")
    public ResponseEntity<ApiResponse<Void>> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok(ApiResponse.success("Payment deleted successfully", HttpStatus.OK.value()));
    }
}
