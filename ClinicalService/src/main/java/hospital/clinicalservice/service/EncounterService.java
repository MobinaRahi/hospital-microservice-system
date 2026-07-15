package hospital.clinicalservice.service;

import hospital.clinicalservice.dto.encounter.EncounterCreateDto;
import hospital.clinicalservice.dto.encounter.EncounterResponseDto;
import hospital.clinicalservice.dto.encounter.EncounterUpdateDto;
import hospital.clinicalservice.model.enums.EncounterStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface EncounterService {

    EncounterResponseDto createEncounter(EncounterCreateDto createDto);

    EncounterResponseDto updateEncounter(Long id, EncounterUpdateDto updateDto);

    void completeEncounter(Long id);

    void cancelEncounter(Long id);

    EncounterResponseDto getEncounterById(Long id);

    List<EncounterResponseDto> getEncountersByPatientId(Long patientId);

    List<EncounterResponseDto> getEncountersByDoctorId(Long doctorId);

    List<EncounterResponseDto> getEncountersByStatus(EncounterStatus status);

    List<EncounterResponseDto> getEncountersByDateRange(LocalDateTime start, LocalDateTime end);

    List<EncounterResponseDto> getTodayEncounters();

    List<EncounterResponseDto> getEncountersByAppointmentId(Long appointmentId);

    List<EncounterResponseDto> getEncountersByDepartmentId(Long departmentId);

    Long countEncountersByPatientId(Long patientId);

    Long countEncountersByDoctorId(Long doctorId);

    Long countEncountersByStatus(EncounterStatus status);

    Long countEncountersByPatientIdAndStatus(Long patientId, EncounterStatus status);
}
