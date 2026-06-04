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
 * Service interface for Patient management.
 *
 * @author Mobina
 */
public interface PatientService {

    // ========== Core Operations ==========

    /**
     * Create new patient
     */
    PatientResponseDto createPatient(PatientCreateDto patientCreateDto);

    /**
     * Update existing patient
     */
    PatientResponseDto updatePatient(Long id, PatientUpdateDto patientUpdateDto);

    /**
     * Partially update patient (PATCH)
     */
    PatientResponseDto patchPatient(Long id, Map<String, Object> updates);

    /**
     * Soft delete patient (deactivate)
     */
    void deactivatePatient(Long id);

    /**
     * Batch soft delete patients
     */
    void archivePatients(List<Long> patientIds);

    // ========== Basic Retrieval ==========

    /**
     * Get patient by ID
     */
    PatientResponseDto getPatientById(Long id);

    /**
     * Get patient by national ID
     */
    PatientResponseDto getPatientByNationalId(String nationalId);

    /**
     * Get patient by phone number
     */
    PatientResponseDto getPatientByPhoneNumber(String phoneNumber);

    /**
     * Get all patients
     */
    List<PatientResponseDto> getAllPatients();

    // ========== Search by Name ==========

    /**
     * Search patients by first name (partial, case-insensitive)
     */
    List<PatientResponseDto> getPatientsByFirstNameContainingIgnoreCase(String firstName);

    /**
     * Search patients by last name (partial, case-insensitive)
     */
    List<PatientResponseDto> getPatientsByLastNameContainingIgnoreCase(String lastName);

    /**
     * Search patients by first name and last name together
     */
    List<PatientResponseDto> getPatientsByFirstNameAndLastName(String firstName, String lastName);

    // ========== Advanced Search ==========

    /**
     * Advanced search with optional filters
     */
    List<PatientResponseDto> searchPatients(String nationalId, String firstName, String lastName, PatientStatus status);

    // ========== Filter by Attributes ==========

    /**
     * Get patients by gender
     */
    List<PatientResponseDto> getPatientsByGender(Gender gender);

    /**
     * Get patients by blood type
     */
    List<PatientResponseDto> getPatientsByBloodType(BloodType bloodType);

    /**
     * Get patients by status (without pagination)
     */
    List<PatientResponseDto> getPatientsByStatus(PatientStatus status);

    /**
     * Get patients by status (with pagination)
     */
    List<PatientResponseDto> getPatientsByStatus(PatientStatus status, Pageable pageable);

    // ========== Date Range ==========

    /**
     * Get patients by birthDate range
     */
    List<PatientResponseDto> getPatientsByBirthDateBetween(LocalDate start, LocalDate end);

    // ========== Room Assignment ==========

    /**
     * Get patients currently in a specific room
     */
    List<PatientResponseDto> getPatientsByCurrentRoomId(Long roomId);

    /**
     * Assign patient to a room
     */
    void assignRoom(Long patientId, Long roomId);

    /**
     * Remove patient from current room
     */
    void unassignRoom(Long patientId);

    // ========== Statistics ==========

    /**
     * Count patients by status
     */
    Long countPatientsByStatus(PatientStatus status);

    /**
     * Count patients by gender
     */
    Long countPatientsByGender(Gender gender);

    /**
     * Count patients by blood type
     */
    Long countPatientsByBloodType(BloodType bloodType);

    /**
     * Count active patients
     */
    Long countActivePatients();

    // ========== Existence Checks ==========

    /**
     * Check if national ID already exists
     */
    boolean existsPatientByNationalId(String nationalId);

    /**
     * Check if phone number already exists
     */
    boolean existsPatientByPhoneNumber(String phoneNumber);

    // ========== Status Management ==========

    /**
     * Activate patient (reverse of soft delete)
     */
    void activatePatient(Long id);
}