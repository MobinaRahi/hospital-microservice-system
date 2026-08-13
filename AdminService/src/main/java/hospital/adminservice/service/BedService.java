package hospital.adminservice.service;

import hospital.adminservice.dto.bed.BedCreateDto;
import hospital.adminservice.dto.bed.BedResponseDto;
import hospital.adminservice.dto.bed.BedUpdateDto;
import hospital.adminservice.model.enums.BedStatus;
import hospital.adminservice.model.enums.BedType;

import java.util.List;

/**
 * Service interface for Bed management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each bed must have a unique bedNumber within the hospital</li>
 *   <li>Beds can be assigned to patients or discharged</li>
 *   <li>Bed status tracks availability and current state</li>
 *   <li>Soft delete supported</li>
 * </ul>
 *
 * <p><strong>Status Workflow:</strong></p>
 * <pre>
 * AVAILABLE → OCCUPIED (when patient assigned)
 * OCCUPIED → AVAILABLE (when patient discharged)
 * AVAILABLE → RESERVED (when reserved for future admission)
 * AVAILABLE → MAINTENANCE (when under maintenance)
 * AVAILABLE → CLEANING (when being cleaned)
 * </pre>
 *
 * @author MobinaRahi
 */
public interface BedService {

    /**
     * Creates a new bed.
     *
     * @param dto the bed creation data
     * @return the created bed
     */
    BedResponseDto createBed(BedCreateDto dto);

    /**
     * Gets a bed by its ID.
     *
     * @param id the bed ID
     * @return the bed
     */
    BedResponseDto getBedById(Long id);

    /**
     * Gets all beds.
     *
     * @return list of all beds
     */
    List<BedResponseDto> getAllBeds();

    /**
     * Gets beds by department ID.
     *
     * @param departmentId the department ID
     * @return list of beds in the department
     */
    List<BedResponseDto> getBedsByDepartment(Long departmentId);

    /**
     * Gets beds by status.
     *
     * @param status the bed status
     * @return list of beds with the status
     */
    List<BedResponseDto> getBedsByStatus(BedStatus status);

    /**
     * Gets beds by type.
     *
     * @param type the bed type
     * @return list of beds with the type
     */
    List<BedResponseDto> getBedsByType(BedType type);

    /**
     * Gets available beds in a department.
     *
     * @param departmentId the department ID
     * @return list of available beds
     */
    List<BedResponseDto> getAvailableBedsByDepartment(Long departmentId);

    /**
     * Assigns a bed to a patient.
     * Changes status to OCCUPIED.
     *
     * @param id          the bed ID
     * @param patientId   the patient ID from CoreService
     * @param admissionId the admission ID from CoreService
     * @return the updated bed
     */
    BedResponseDto assignToPatient(Long id, Long patientId, Long admissionId);

    /**
     * Discharges a patient from a bed.
     * Resets all patient-related fields and sets status to AVAILABLE.
     *
     * @param id the bed ID
     * @return the updated bed
     */
    BedResponseDto dischargePatient(Long id);

    /**
     * Updates an existing bed.
     *
     * @param id  the bed ID
     * @param dto the update data
     * @return the updated bed
     */
    BedResponseDto updateBed(Long id, BedUpdateDto dto);

    /**
     * Soft-deletes a bed.
     *
     * @param id the bed ID
     */
    void deleteBed(Long id);

    /**
     * Counts available beds.
     *
     * @return number of available beds
     */
    long countAvailableBeds();

    /**
     * Counts occupied beds.
     *
     * @return number of occupied beds
     */
    long countOccupiedBeds();
}
