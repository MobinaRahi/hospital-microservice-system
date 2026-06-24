package hospital.coreservice.service;

import hospital.coreservice.dto.patient.PatientCreateDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.patient.PatientUpdateDto;
import hospital.coreservice.dto.request.CompleteRegistrationRequest;
import hospital.coreservice.model.Patient;
import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.model.enums.PatientStatus;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PatientService {
    PatientResponseDto createPatient(PatientCreateDto patientCreateDto);
    PatientResponseDto updatePatient(Long id, PatientUpdateDto patientUpdateDto);
    PatientResponseDto patchPatient(Long id, Map<String, Object> updates);
    void deactivatePatient(Long id);
    void archivePatients(List<Long> patientIds);
    PatientResponseDto getPatientById(Long id);
    PatientResponseDto getPatientByNationalId(String nationalId);
    PatientResponseDto getPatientByPhoneNumber(String phoneNumber);
    List<PatientResponseDto> getAllPatients();
    List<PatientResponseDto> getPatientsByFirstNameContainingIgnoreCase(String firstName);
    List<PatientResponseDto> getPatientsByLastNameContainingIgnoreCase(String lastName);
    List<PatientResponseDto> getPatientsByFirstNameAndLastName(String firstName, String lastName);
    List<PatientResponseDto> searchPatients(String nationalId, String firstName, String lastName, PatientStatus status);
    List<PatientResponseDto> getPatientsByGender(Gender gender);
    List<PatientResponseDto> getPatientsByBloodType(BloodType bloodType);
    List<PatientResponseDto> getPatientsByStatus(PatientStatus status);
    List<PatientResponseDto> getPatientsByStatus(PatientStatus status, Pageable pageable);
    List<PatientResponseDto> getPatientsByBirthDateBetween(LocalDate start, LocalDate end);
    List<PatientResponseDto> getPatientsByCurrentRoomId(Long roomId);
    void assignRoom(Long patientId, Long roomId);
    void unassignRoom(Long patientId);
    Long countPatientsByStatus(PatientStatus status);
    Long countPatientsByGender(Gender gender);
    Long countPatientsByBloodType(BloodType bloodType);
    Long countActivePatients();
    Long countInactivePatients();
    Long countAllPatients();
    boolean existsPatientByNationalId(String nationalId);
    boolean existsPatientByPhoneNumber(String phoneNumber);
    void activatePatient(Long id);
    PatientResponseDto completeRegistration(Long patientId, CompleteRegistrationRequest request);
    Optional<Patient> getPatientByUserId(Long userId);
}