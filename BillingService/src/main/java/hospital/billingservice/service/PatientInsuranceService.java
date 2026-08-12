package hospital.billingservice.service;

import hospital.billingservice.dto.patientinsurance.PatientInsuranceCreateDto;
import hospital.billingservice.dto.patientinsurance.PatientInsuranceResponseDto;
import hospital.billingservice.dto.patientinsurance.PatientInsuranceUpdateDto;

import java.util.List;

/**
 * Service interface for PatientInsurance.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>A patient can have multiple insurance plans</li>
 *   <li>Only one insurance can be marked as primary per patient</li>
 *   <li>Policy number must be unique</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface PatientInsuranceService {

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Creates a new patient insurance record.
     *
     * @param dto the patient insurance creation data
     * @return the created patient insurance
     */
    PatientInsuranceResponseDto createPatientInsurance(PatientInsuranceCreateDto dto);

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ══════════════════════════════════════════════════════════════════

    /**
     * Gets a patient insurance record by its ID.
     *
     * @param id the patient insurance ID
     * @return the patient insurance record
     */
    PatientInsuranceResponseDto getPatientInsuranceById(Long id);

    /**
     * Gets all insurance records for a specific patient.
     *
     * @param patientId the patient ID
     * @return list of insurance records for the patient
     */
    List<PatientInsuranceResponseDto> getInsurancesByPatient(Long patientId);

    /**
     * Gets the primary insurance for a patient.
     *
     * @param patientId the patient ID
     * @return the primary insurance record
     */
    PatientInsuranceResponseDto getPrimaryInsurance(Long patientId);

    /**
     * Gets valid (non-expired) insurance records for a patient.
     *
     * @param patientId the patient ID
     * @return list of valid insurance records
     */
    List<PatientInsuranceResponseDto> getValidInsurances(Long patientId);

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Updates an existing patient insurance record.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the patient insurance ID
     * @param dto the update data
     * @return the updated patient insurance record
     */
    PatientInsuranceResponseDto updatePatientInsurance(Long id, PatientInsuranceUpdateDto dto);

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes a patient insurance record.
     *
     * @param id the patient insurance ID
     */
    void deletePatientInsurance(Long id);

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Checks if a policy number is already in use.
     *
     * @param policyNumber the policy number to check
     * @return true if the policy number exists
     */
    boolean policyNumberExists(String policyNumber);
}
