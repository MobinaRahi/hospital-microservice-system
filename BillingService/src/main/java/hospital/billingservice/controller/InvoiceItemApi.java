package hospital.billingservice.controller;

import hospital.billingservice.dto.invoiceitem.InvoiceItemCreateDto;
import hospital.billingservice.dto.invoiceitem.InvoiceItemResponseDto;
import hospital.billingservice.dto.invoiceitem.InvoiceItemUpdateDto;
import hospital.billingservice.dto.response.ApiResponse;
import hospital.billingservice.service.InvoiceItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for InvoiceItem management.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Accountant/Admin: Full CRUD access</li>
 *   <li>Items can only be added to PENDING invoices</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/billing/invoice-items")
@RequiredArgsConstructor
@Tag(name = "Invoice Item Management", description = "Invoice line item CRUD APIs")
public class InvoiceItemApi {

    private final InvoiceItemService invoiceItemService;

    @PostMapping("/invoice/{invoiceId}")
    @Operation(summary = "Add an item to an invoice")
    public ResponseEntity<ApiResponse<InvoiceItemResponseDto>> addItemToInvoice(
            @PathVariable Long invoiceId,
            @Valid @RequestBody InvoiceItemCreateDto createDto) {
        InvoiceItemResponseDto created = invoiceItemService.addItemToInvoice(invoiceId, createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Item added to invoice successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an invoice item by ID")
    public ResponseEntity<ApiResponse<InvoiceItemResponseDto>> updateItem(@PathVariable Long id, @Valid @RequestBody InvoiceItemUpdateDto updateDto) {
        InvoiceItemResponseDto updated = invoiceItemService.updateItem(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Invoice item updated successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get invoice item by ID")
    public ResponseEntity<ApiResponse<InvoiceItemResponseDto>> getItemById(@PathVariable Long id) {
        InvoiceItemResponseDto item = invoiceItemService.getItemById(id);
        return ResponseEntity.ok(ApiResponse.success(item, "Invoice item retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/invoice/{invoiceId}")
    @Operation(summary = "Get all items for an invoice")
    public ResponseEntity<ApiResponse<List<InvoiceItemResponseDto>>> getItemsByInvoice(@PathVariable Long invoiceId) {
        List<InvoiceItemResponseDto> items = invoiceItemService.getItemsByInvoice(invoiceId);
        return ResponseEntity.ok(ApiResponse.success(items, "Invoice items retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/service/{serviceCode}")
    @Operation(summary = "Get all invoice items for a specific service code")
    public ResponseEntity<ApiResponse<List<InvoiceItemResponseDto>>> getItemsByServiceCode(@PathVariable String serviceCode) {
        List<InvoiceItemResponseDto> items = invoiceItemService.getItemsByServiceCode(serviceCode);
        return ResponseEntity.ok(ApiResponse.success(items, "Invoice items retrieved successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove an item from an invoice")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        invoiceItemService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.success("Invoice item removed successfully", HttpStatus.OK.value()));
    }
}
