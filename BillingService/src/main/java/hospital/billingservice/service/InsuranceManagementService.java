package hospital.billingservice.service;

import hospital.billingservice.dto.insurancemanagement.InsuranceManagementCreateDto;
import hospital.billingservice.dto.insurancemanagement.InsuranceManagementResponseDto;
import hospital.billingservice.dto.insurancemanagement.InsuranceManagementUpdateDto;

import java.util.List;

/**
 * Service interface for InsuranceManagement.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Insurance code must be unique</li>
 *   <li>Coverage percent must be between 0 and 100</li>
 *   <li>Soft delete is supported</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface InsuranceManagementService {

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Creates a new insurance plan.
     *
     * @param dto the insurance creation data
     * @return the created insurance
     */
    InsuranceManagementResponseDto createInsurance(InsuranceManagementCreateDto dto);

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Gets an insurance plan by its ID.
     *
     * @param id the insurance ID
     * @return the insurance plan
     */
    InsuranceManagementResponseDto getInsuranceById(Long id);

    /**
     * Gets an insurance plan by its unique code.
     *
     * @param code the insurance code
     * @return the insurance plan
     */
    InsuranceManagementResponseDto getInsuranceByCode(String code);

    /**
     * Gets all active insurance plans.
     *
     * @return list of active insurance plans
     */
    List<InsuranceManagementResponseDto> getAllActiveInsurances();

    /**
     * Gets all insurance plans (including inactive).
     *
     * @return list of all insurance plans
     */
    List<InsuranceManagementResponseDto> getAllInsurances();

    /**
     * Searches insurance plans by name (case-insensitive, partial match).
     *
     * @param name the name to search
     * @return list of matching insurance plans
     */
    List<InsuranceManagementResponseDto> searchByName(String name);

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Updates an existing insurance plan.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the insurance ID
     * @param dto the update data
     * @return the updated insurance plan
     */
    InsuranceManagementResponseDto updateInsurance(Long id, InsuranceManagementUpdateDto dto);

    /**
     * Toggles the active status of an insurance plan.
     *
     * @param id the insurance ID
     * @return the updated insurance plan
     */
    InsuranceManagementResponseDto toggleActive(Long id);

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ══════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes an insurance plan.
     *
     * @param id the insurance ID
     */
    void deleteInsurance(Long id);

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Checks if an insurance code is already in use.
     *
     * @param code the code to check
     * @return true if the code exists
     */
    boolean codeExists(String code);
}
