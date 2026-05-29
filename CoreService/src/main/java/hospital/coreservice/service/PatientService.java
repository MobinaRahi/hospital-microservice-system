package hospital.coreservice.service;

import hospital.coreservice.dto.patient.PatientCreateDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.patient.PatientUpdateDto;
import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.model.enums.PatientStatus;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Patient entity.
 * <p>
 * Defines all business operations for patient management in the hospital system.
 * This interface is implemented by {@link hospital.coreservice.service.imp.PatientServiceImpl}.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
public interface PatientService {

    // ========== Create Operations ==========

    /**
     * Creates a new patient in the system.
     *
     * @param patientCreateDto the DTO containing patient creation data
     * @return the created patient as ResponseDto
     */
    PatientResponseDto createPatient(PatientCreateDto patientCreateDto);

    // ========== Update Operations ==========

    /**
     * Updates an existing patient's information.
     *
     * @param id the ID of the patient to update
     * @param patientUpdateDto the DTO containing updated values
     * @return the updated patient as ResponseDto
     */
    PatientResponseDto updatePatient(Long id, PatientUpdateDto patientUpdateDto);

    // ========== Partial Update (Patch) ==========

    /**
     * Partially updates a patient's information.
     * <p>
     * Only the fields present in the updates map will be modified.
     * This is useful for PATCH endpoints where client sends only changed fields.
     * </p>
     * <p>
     * Supported keys: "firstName", "lastName", "phoneNumber", "address",
     * "bloodType", "insuranceId", "allergies"
     * </p>
     *
     * @param id the ID of the patient to update
     * @param updates a map of field names to new values
     * @return the updated patient as ResponseDto
     */
    PatientResponseDto patchPatient(Long id, Map<String, Object> updates);

    // ========== Delete Operations ==========

    /**
     * Soft deletes a single patient from the system.
     *
     * @param id the ID of the patient to delete
     */
    void deletePatient(Long id);

    /**
     * Batch soft deletes multiple patients.
     * <p>
     * Useful for bulk operations where admin wants to archive multiple patients at once.
     * </p>
     *
     * @param patientIds list of patient IDs to archive
     */
    void archivePatients(List<Long> patientIds);

    // ========== Single Record Retrieval ==========

    PatientResponseDto getPatientById(Long id);
    PatientResponseDto getPatientByNationalId(String nationalId);
    PatientResponseDto getPatientByPhoneNumber(String phoneNumber);

    // ========== Multi-Record Retrieval ==========

    List<PatientResponseDto> getAllPatients();

    // ========== Search by Name (Case-Insensitive, Partial Match) ==========

    List<PatientResponseDto> getPatientsByFirstNameContainingIgnoreCase(String firstName);
    List<PatientResponseDto> getPatientsByLastNameContainingIgnoreCase(String lastName);
    List<PatientResponseDto> getPatientsByFirstNameAndLastName(String firstName, String lastName);

    // ========== Advanced Search (Multiple Filters) ==========

    /**
     * Advanced search with multiple filters.
     * <p>
     * All parameters are optional. Only non-null parameters are used in the search.
     * This provides a flexible search experience similar to real hospital systems.
     * </p>
     *
     * @param nationalId filter by national ID (exact match, optional)
     * @param firstName filter by first name (partial match, optional)
     * @param lastName filter by last name (partial match, optional)
     * @param status filter by patient status (optional)
     * @return list of patients matching all provided filters
     */
    List<PatientResponseDto> searchPatients(String nationalId, String firstName, String lastName, PatientStatus status);

    // ========== Filtering by Attributes ==========

    List<PatientResponseDto> getPatientsByGender(Gender gender);
    List<PatientResponseDto> getPatientsByBloodType(BloodType bloodType);
    List<PatientResponseDto> getPatientsByStatus(PatientStatus status);
    List<PatientResponseDto> getPatientsByStatus(PatientStatus status, Pageable pageable);

    // ========== Date Range Queries ==========

    /**
     * Retrieves patients born within a specific date range.
     * <p>
     * Useful for demographic reports and birthday notifications.
     * </p>
     *
     * @param start the start date (inclusive)
     * @param end the end date (inclusive)
     * @return list of patients born between the given dates
     */
    List<PatientResponseDto> getPatientsByBirthDateBetween(LocalDate start, LocalDate end);

    // ========== Room Assignment Operations ==========

    List<PatientResponseDto> getPatientsByCurrentRoomId(Long roomId);
    void assignRoom(Long patientId, Long roomId);
    void unassignRoom(Long patientId);

    // ========== Counting Operations ==========

    Long countPatientsByStatus(PatientStatus status);
    Long countPatientsByGender(Gender gender);
    Long countPatientsByBloodType(BloodType bloodType);
    Long countActivePatients();

    // ========== Existence Checks ==========

    boolean existsPatientByNationalId(String nationalId);
    boolean existsPatientByPhoneNumber(String phoneNumber);
    void activatePatient(Long id);
}
