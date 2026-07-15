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

@Repository
public interface EncounterRepository extends BaseEntityRepository<Encounter, Long> {

    // ==================== Patient Queries ====================

    List<Encounter> findByPatientId(Long patientId);

    List<Encounter> findByPatientIdOrderByEncounterDateDesc(Long patientId);

    Page<Encounter> findByPatientId(Long patientId, Pageable pageable);

    // ==================== Doctor Queries ====================

    List<Encounter> findByDoctorId(Long doctorId);

    List<Encounter> findByDoctorIdOrderByEncounterDateDesc(Long doctorId);

    Page<Encounter> findByDoctorId(Long doctorId, Pageable pageable);

    // ==================== Status Queries ====================

    List<Encounter> findByStatus(EncounterStatus status);

    List<Encounter> findByPatientIdAndStatus(Long patientId, EncounterStatus status);

    List<Encounter> findByDoctorIdAndStatus(Long doctorId, EncounterStatus status);

    // ==================== Date Queries ====================

    List<Encounter> findByEncounterDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT e FROM encounterEntity e WHERE e.deleted = false AND e.encounterDate >= :start AND e.encounterDate < :end")
    List<Encounter> findTodayEncounters(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // ==================== Appointment Queries ====================

    List<Encounter> findByAppointmentId(Long appointmentId);

    // ==================== Department Queries ====================

    List<Encounter> findByDepartmentId(Long departmentId);

    // ==================== Count Queries ====================

    long countByPatientId(Long patientId);

    long countByDoctorId(Long doctorId);

    long countByStatus(EncounterStatus status);

    @Query("SELECT COUNT(e) FROM encounterEntity e WHERE e.deleted = false AND e.patientId = :patientId AND e.status = :status")
    long countByPatientIdAndStatus(@Param("patientId") Long patientId, @Param("status") EncounterStatus status);
}
