package hospital.billingservice.service;

import hospital.billingservice.dto.invoiceitem.InvoiceItemCreateDto;
import hospital.billingservice.dto.invoiceitem.InvoiceItemResponseDto;
import hospital.billingservice.dto.invoiceitem.InvoiceItemUpdateDto;

import java.util.List;

/**
 * Service interface for InvoiceItem.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>totalPrice = unitPrice × quantity</li>
 *   <li>serviceCode references ServiceCatalog.code</li>
 *   <li>Items can only be added to PENDING invoices</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface InvoiceItemService {

    // ═════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Adds a new item to an existing invoice.
     * <p>Automatically calculates totalPrice.</p>
     *
     * @param invoiceId the invoice ID to add the item to
     * @param dto       the item creation data
     * @return the created invoice item
     */
    InvoiceItemResponseDto addItemToInvoice(Long invoiceId, InvoiceItemCreateDto dto);

    // ══════════════════════════════════════════════════════════════════
    // Read
    // ══════════════════════════════════════════════════════════════════

    /**
     * Gets an invoice item by its ID.
     *
     * @param id the item ID
     * @return the invoice item
     */
    InvoiceItemResponseDto getItemById(Long id);

    /**
     * Gets all items for a specific invoice.
     *
     * @param invoiceId the invoice ID
     * @return list of invoice items
     */
    List<InvoiceItemResponseDto> getItemsByInvoice(Long invoiceId);

    /**
     * Gets all invoice items for a specific service code.
     * Useful for reporting: "how many times was service X billed?"
     *
     * @param serviceCode the service code
     * @return list of invoice items with the service code
     */
    List<InvoiceItemResponseDto> getItemsByServiceCode(String serviceCode);

    // ══════════════════════════════════════════════════════════════════
    // Update
    // ══════════════════════════════════════════════════════════════════

    /**
     * Updates an existing invoice item.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the item ID
     * @param dto the update data
     * @return the updated invoice item
     */
    InvoiceItemResponseDto updateItem(Long id, InvoiceItemUpdateDto dto);

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ══════════════════════════════════════════════════════════════════

    /**
     * Removes an item from an invoice.
     *
     * @param id the item ID
     */
    void deleteItem(Long id);
}
