package hospital.clinicalservice.repository;

import hospital.clinicalservice.model.Encounter;
import hospital.clinicalservice.model.enums.EncounterStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Encounter entity.
 * Provides query methods for patient encounters by patient, doctor, status, and date.
 *
 * <p><strong>Key features:</strong></p>
 * <ul>
 *   <li>Soft delete support (via BaseEntityRepository)</li>
 *   <li>Optimistic locking (via BaseEntity.version)</li>
 *   <li>Custom queries with JPQL for complex filtering</li>
 * </ul>
 *
 * @author Mobina
 */
@Repository
public interface EncounterRepository extends BaseEntityRepository<Encounter, Long> {

    // ==================== Patient Queries ====================

    /**
     * Gets all encounters for a patient.
     * Used when viewing patient history.
     */
    List<Encounter> findByPatientId(Long patientId);

    /**
     * Gets all encounters for a patient, ordered by date (newest first).
     * Used for: patient dashboard, visit history.
     */
    List<Encounter> findByPatientIdOrderByEncounterDateDesc(Long patientId);

    /**
     * Gets encounters for a patient with pagination.
     * Used for: paginated patient history view.
     */
    Page<Encounter> findByPatientId(Long patientId, Pageable pageable);

    // ==================== Doctor Queries ====================

    /**
     * Gets all encounters for a doctor.
     * Used when viewing doctor's visit history.
     */
    List<Encounter> findByDoctorId(Long doctorId);

    /**
     * Gets all encounters for a doctor, ordered by date (newest first).
     * Used for: doctor dashboard.
     */
    List<Encounter> findByDoctorIdOrderByEncounterDateDesc(Long doctorId);

    /**
     * Gets encounters for a doctor with pagination.
     * Used for: paginated doctor history view.
     */
    Page<Encounter> findByDoctorId(Long doctorId, Pageable pageable);

    // ==================== Status Queries ====================

    /**
     * Gets encounters by status (IN_PROGRESS, COMPLETED, CANCELLED).
     * Used for: admin dashboard, filtering active encounters.
     */
    List<Encounter> findByStatus(EncounterStatus status);

    /**
     * Gets encounters for a patient with a specific status.
     * Used for: viewing only active or completed encounters for a patient.
     */
    List<Encounter> findByPatientIdAndStatus(Long patientId, EncounterStatus status);

    /**
     * Gets encounters for a doctor with a specific status.
     * Used for: doctor viewing only active or completed encounters.
     */
    List<Encounter> findByDoctorIdAndStatus(Long doctorId, EncounterStatus status);

    // ==================== Date Queries ====================

    /**
     * Gets encounters within a date range.
     * Used for: reports, statistics, date-based filtering.
     */
    List<Encounter> findByEncounterDateBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Gets today's encounters using JPQL query.
     * More efficient than date range query for "today" specifically.
     */
    @Query("SELECT e FROM encounterEntity e WHERE e.deleted = false AND e.encounterDate >= :start AND e.encounterDate < :end")
    List<Encounter> findTodayEncounters(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // ==================== Appointment Queries ====================

    /**
     * Gets encounters linked to a specific appointment.
     * Used for: viewing all visits related to an appointment.
     */
    List<Encounter> findByAppointmentId(Long appointmentId);

    // ==================== Department Queries ====================

    /**
     * Gets encounters in a specific department.
     * Used for: department head viewing all visits.
     */
    List<Encounter> findByDepartmentId(Long departmentId);

    // ==================== Count Queries ====================

    /**
     * Counts total encounters for a patient.
     * Used for: patient statistics card.
     */
    long countByPatientId(Long patientId);

    /**
     * Counts total encounters for a doctor.
     * Used for: doctor dashboard statistics.
     */
    long countByDoctorId(Long doctorId);

    /**
     * Counts encounters by status.
     * Used for: dashboard statistics (how many active/completed/cancelled).
     */
    long countByStatus(EncounterStatus status);

    /**
     * Counts encounters for a patient by status.
     * Used for: patient statistics (active visits, completed visits).
     */
    @Query("SELECT COUNT(e) FROM encounterEntity e WHERE e.deleted = false AND e.patientId = :patientId AND e.status = :status")
    long countByPatientIdAndStatus(@Param("patientId") Long patientId, @Param("status") EncounterStatus status);
}
