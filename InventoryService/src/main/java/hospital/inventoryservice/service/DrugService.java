package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.drug.DrugCreateDto;
import hospital.inventoryservice.dto.drug.DrugResponseDto;
import hospital.inventoryservice.dto.drug.DrugUpdateDto;
import hospital.inventoryservice.model.enums.DrugForm;

import java.util.List;

/**
 * Service interface for Drug management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Generic name is immutable after creation</li>
 *   <li>Barcode must be unique across all drugs</li>
 *   <li>Category must exist before creating a drug</li>
 *   <li>Drugs can be soft-deleted</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface DrugService {

    // ════════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    /**
     * Creates a new drug.
     *
     * @param dto the drug creation data
     * @return the created drug
     */
    DrugResponseDto createDrug(DrugCreateDto dto);

    // ════════════════════════════════════════════════════════════════════
    // Read
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Gets a drug by its ID.
     *
     * @param id the drug ID
     * @return the drug
     */
    DrugResponseDto getDrugById(Long id);

    /**
     * Gets a drug by its barcode.
     *
     * @param barcode the barcode to search
     * @return the drug
     */
    DrugResponseDto getDrugByBarcode(String barcode);

    /**
     * Gets all active drugs.
     *
     * @return list of drugs
     */
    List<DrugResponseDto> getAllDrugs();

    /**
     * Searches drugs by generic name (case-insensitive, partial match).
     *
     * @param name the generic name to search
     * @return list of matching drugs
     */
    List<DrugResponseDto> searchByGenericName(String name);

    /**
     * Gets drugs by category.
     *
     * @param categoryId the category ID
     * @return list of drugs in the category
     */
    List<DrugResponseDto> getDrugsByCategory(Long categoryId);

    /**
     * Gets drugs by pharmaceutical form.
     *
     * @param form the drug form
     * @return list of drugs
     */
    List<DrugResponseDto> getDrugsByForm(DrugForm form);

    /**
     * Gets prescription-only drugs.
     *
     * @return list of prescription drugs
     */
    List<DrugResponseDto> getPrescriptionDrugs();

    /**
     * Gets drugs with low stock.
     *
     * @return list of drugs with low stock
     */
    List<DrugResponseDto> getDrugsWithLowStock();

    /**
     * Gets drugs with expiring stock.
     *
     * @param daysThreshold number of days before expiry
     * @return list of drugs with expiring stock
     */
    List<DrugResponseDto> getDrugsWithExpiringStock(int daysThreshold);

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    /**
     * Updates an existing drug.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the drug ID
     * @param dto the update data
     * @return the updated drug
     */
    DrugResponseDto updateDrug(Long id, DrugUpdateDto dto);

    /**
     * Toggles the active status of a drug.
     *
     * @param id the drug ID
     * @return the updated drug
     */
    DrugResponseDto toggleActive(Long id);

    // ════════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes a drug.
     *
     * @param id the drug ID
     */
    void deleteDrug(Long id);

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ════════════════════════════════════════════════════════════════════

    /**
     * Checks if a barcode is already in use.
     *
     * @param barcode the barcode to check
     * @return true if the barcode exists
     */
    boolean barcodeExists(String barcode);
}
