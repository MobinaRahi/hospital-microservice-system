package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.supplier.SupplierCreateDto;
import hospital.inventoryservice.dto.supplier.SupplierResponseDto;
import hospital.inventoryservice.dto.supplier.SupplierUpdateDto;

import java.util.List;

/**
 * Service interface for Supplier management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Supplier email must be unique</li>
 *   <li>Suppliers can be deactivated (inactive) instead of deleted</li>
 *   <li>A supplier can be soft-deleted</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface SupplierService {

    // ════════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Creates a new supplier.
     *
     * @param dto the supplier creation data
     * @return the created supplier
     */
    SupplierResponseDto createSupplier(SupplierCreateDto dto);

    // ════════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    /**
     * Gets a supplier by its ID.
     *
     * @param id the supplier ID
     * @return the supplier
     */
    SupplierResponseDto getSupplierById(Long id);

    /**
     * Gets a supplier by email.
     *
     * @param email the email to search
     * @return the supplier
     */
    SupplierResponseDto getSupplierByEmail(String email);

    /**
     * Gets all active suppliers.
     *
     * @return list of active suppliers
     */
    List<SupplierResponseDto> getAllActiveSuppliers();

    /**
     * Gets all suppliers (including inactive).
     *
     * @return list of all suppliers
     */
    List<SupplierResponseDto> getAllSuppliers();

    /**
     * Searches suppliers by name (case-insensitive, partial match).
     *
     * @param name the name to search
     * @return list of matching suppliers
     */
    List<SupplierResponseDto> searchByName(String name);

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    /**
     * Updates an existing supplier.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the supplier ID
     * @param dto the update data
     * @return the updated supplier
     */
    SupplierResponseDto updateSupplier(Long id, SupplierUpdateDto dto);

    /**
     * Toggles the active status of a supplier.
     *
     * @param id the supplier ID
     * @return the updated supplier
     */
    SupplierResponseDto toggleActive(Long id);

    // ════════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes a supplier.
     *
     * @param id the supplier ID
     */
    void deleteSupplier(Long id);

    // ════════════════════════════════════════════════════════════════════
    // Validation
    // ════════════════════════════════════════════════════════════════════

    /**
     * Checks if a supplier email already exists.
     *
     * @param email the email to check
     * @return true if exists
     */
    boolean emailExists(String email);
}
