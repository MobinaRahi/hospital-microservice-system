package hospital.clinicalservice.service.imp;

import hospital.clinicalservice.dto.encounter.EncounterCreateDto;
import hospital.clinicalservice.dto.encounter.EncounterResponseDto;
import hospital.clinicalservice.dto.encounter.EncounterUpdateDto;
import hospital.clinicalservice.exception.encounter.EncounterNotFoundException;
import hospital.clinicalservice.exception.encounter.InvalidEncounterStateException;
import hospital.clinicalservice.mapper.EncounterMapper;
import hospital.clinicalservice.model.Encounter;
import hospital.clinicalservice.model.enums.EncounterStatus;
import hospital.clinicalservice.repository.EncounterRepository;
import hospital.clinicalservice.service.EncounterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of EncounterService.
 * Manages the lifecycle of patient encounters (visits).
 *
 * <p><strong>Key responsibilities:</strong></p>
 * <ul>
 *   <li>Create new encounters when patient visits a doctor</li>
 *   <li>Update encounter details (chief complaint, doctor notes)</li>
 *   <li>Complete encounters when visit is finished</li>
 *   <li>Cancel encounters when visit is cancelled</li>
 *   <li>Query encounters by patient, doctor, status, date</li>
 * </ul>
 *
 * @author Mobina
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EncounterServiceImpl implements EncounterService {

    // ==================== Dependencies ====================

    private final EncounterRepository encounterRepository;
    private final EncounterMapper encounterMapper;

    // ==================== Create ====================

    /**
     * Creates a new encounter (visit) for a patient.
     * Sets encounter date to now and status to IN_PROGRESS.
     *
     * @param createDto DTO containing patient ID, doctor ID, encounter type, etc.
     * @return The created encounter with generated ID
     */
    @Override
    @Transactional
    public EncounterResponseDto createEncounter(EncounterCreateDto createDto) {
        log.info("Creating encounter for patient: {} by doctor: {}", createDto.getPatientId(), createDto.getDoctorId());
        Encounter encounter = encounterMapper.toEntity(createDto);
        encounter.setEncounterDate(LocalDateTime.now());
        encounter.setStatus(EncounterStatus.IN_PROGRESS);
        Encounter saved = encounterRepository.save(encounter);
        log.info("Encounter created with id: {}", saved.getId());
        return encounterMapper.toResponseDto(saved);
    }

    // ==================== Update ====================

    /**
     * Updates an existing encounter's details (type, complaint, notes).
     * Only fields that are not null in updateDto will be updated.
     *
     * @param id The encounter ID to update
     * @param updateDto DTO with fields to update
     * @return The updated encounter
     * @throws EncounterNotFoundException if encounter not found
     */
    @Override
    @Transactional
    public EncounterResponseDto updateEncounter(Long id, EncounterUpdateDto updateDto) {
        log.info("Updating encounter id: {}", id);
        Encounter encounter = encounterRepository.findNotDeletedById(id)
                .orElseThrow(() -> EncounterNotFoundException.byId(id));
        encounterMapper.updateEntity(encounter, updateDto);
        Encounter updated = encounterRepository.save(encounter);
        log.info("Encounter updated id: {}", id);
        return encounterMapper.toResponseDto(updated);
    }

    // ==================== Status Changes ====================

    /**
     * Completes an encounter — marks visit as finished.
     * Validates that encounter can be completed (must be IN_PROGRESS).
     *
     * @param id The encounter ID to complete
     * @throws EncounterNotFoundException if encounter not found
     * @throws InvalidEncounterStateException if already completed or cancelled
     */
    @Override
    @Transactional
    public void completeEncounter(Long id) {
        log.info("Completing encounter id: {}", id);
        Encounter encounter = encounterRepository.findNotDeletedById(id)
                .orElseThrow(() -> EncounterNotFoundException.byId(id));
        if (encounter.getStatus() == EncounterStatus.COMPLETED) {
            throw InvalidEncounterStateException.alreadyCompleted(id);
        }
        if (encounter.getStatus() == EncounterStatus.CANCELLED) {
            throw InvalidEncounterStateException.cannotComplete(id, "CANCELLED");
        }
        encounter.setStatus(EncounterStatus.COMPLETED);
        encounterRepository.save(encounter);
        log.info("Encounter completed id: {}", id);
    }

    /**
     * Cancels an encounter — marks visit as cancelled.
     * Cannot cancel an already completed encounter.
     *
     * @param id The encounter ID to cancel
     * @throws EncounterNotFoundException if encounter not found
     * @throws InvalidEncounterStateException if already cancelled
     */
    @Override
    @Transactional
    public void cancelEncounter(Long id) {
        log.info("Cancelling encounter id: {}", id);
        Encounter encounter = encounterRepository.findNotDeletedById(id)
                .orElseThrow(() -> EncounterNotFoundException.byId(id));
        if (encounter.getStatus() == EncounterStatus.CANCELLED) {
            throw InvalidEncounterStateException.alreadyCancelled(id);
        }
        encounter.setStatus(EncounterStatus.CANCELLED);
        encounterRepository.save(encounter);
        log.info("Encounter cancelled id: {}", id);
    }

    // ==================== Read ====================

    /**
     * Gets a single encounter by ID.
     *
     * @param id The encounter ID
     * @return The encounter details
     * @throws EncounterNotFoundException if not found
     */
    @Override
    public EncounterResponseDto getEncounterById(Long id) {
        log.debug("Fetching encounter by id: {}", id);
        Encounter encounter = encounterRepository.findNotDeletedById(id)
                .orElseThrow(() -> EncounterNotFoundException.byId(id));
        return encounterMapper.toResponseDto(encounter);
    }

    /**
     * Gets all encounters for a patient, ordered by date (newest first).
     * Used for: patient history, doctor viewing patient's past visits.
     *
     * @param patientId The patient ID
     * @return List of encounters
     */
    @Override
    public List<EncounterResponseDto> getEncountersByPatientId(Long patientId) {
        log.debug("Fetching encounters by patientId: {}", patientId);
        return encounterRepository.findByPatientIdOrderByEncounterDateDesc(patientId)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets all encounters for a doctor, ordered by date (newest first).
     * Used for: doctor dashboard, viewing visit history.
     *
     * @param doctorId The doctor ID
     * @return List of encounters
     */
    @Override
    public List<EncounterResponseDto> getEncountersByDoctorId(Long doctorId) {
        log.debug("Fetching encounters by doctorId: {}", doctorId);
        return encounterRepository.findByDoctorIdOrderByEncounterDateDesc(doctorId)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets encounters by status (IN_PROGRESS, COMPLETED, CANCELLED).
     * Used for: admin dashboard, filtering active encounters.
     *
     * @param status The encounter status to filter by
     * @return List of encounters with the given status
     */
    @Override
    public List<EncounterResponseDto> getEncountersByStatus(EncounterStatus status) {
        log.debug("Fetching encounters by status: {}", status);
        return encounterRepository.findByStatus(status)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets all encounters within a date range.
     * Used for: reports, statistics, date-based filtering.
     *
     * @param start Start date (inclusive)
     * @param end End date (exclusive)
     * @return List of encounters in the date range
     */
    @Override
    public List<EncounterResponseDto> getEncountersByDateRange(LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching encounters between {} and {}", start, end);
        return encounterRepository.findByEncounterDateBetween(start, end)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets today's encounters.
     * Used for: doctor dashboard, receptionist view.
     *
     * @return List of today's encounters
     */
    @Override
    public List<EncounterResponseDto> getTodayEncounters() {
        log.debug("Fetching today's encounters");
        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return encounterRepository.findTodayEncounters(start, end)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets encounters linked to a specific appointment.
     * Used for: viewing all visits related to an appointment.
     *
     * @param appointmentId The appointment ID
     * @return List of encounters for this appointment
     */
    @Override
    public List<EncounterResponseDto> getEncountersByAppointmentId(Long appointmentId) {
        log.debug("Fetching encounters by appointmentId: {}", appointmentId);
        return encounterRepository.findByAppointmentId(appointmentId)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets encounters in a specific department.
     * Used for: department head viewing all visits.
     *
     * @param departmentId The department ID
     * @return List of encounters in this department
     */
    @Override
    public List<EncounterResponseDto> getEncountersByDepartmentId(Long departmentId) {
        log.debug("Fetching encounters by departmentId: {}", departmentId);
        return encounterRepository.findByDepartmentId(departmentId)
                .stream()
                .map(encounterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ==================== Count ====================

    /**
     * Counts total encounters for a patient.
     * Used for: patient statistics, visit history count.
     */
    @Override
    public Long countEncountersByPatientId(Long patientId) {
        return encounterRepository.countByPatientId(patientId);
    }

    /**
     * Counts total encounters for a doctor.
     * Used for: doctor workload statistics.
     */
    @Override
    public Long countEncountersByDoctorId(Long doctorId) {
        return encounterRepository.countByDoctorId(doctorId);
    }

    /**
     * Counts encounters by status.
     * Used for: dashboard statistics (how many active/completed/cancelled).
     */
    @Override
    public Long countEncountersByStatus(EncounterStatus status) {
        return encounterRepository.countByStatus(status);
    }

    /**
     * Counts encounters for a patient by status.
     * Used for: patient statistics (active visits, completed visits).
     */
    @Override
    public Long countEncountersByPatientIdAndStatus(Long patientId, EncounterStatus status) {
        return encounterRepository.countByPatientIdAndStatus(patientId, status);
    }
}
