package hospital.coreservice.repository;

import hospital.coreservice.model.Appointment;
import hospital.coreservice.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Appointment entity.
 * Provides query methods by patient, doctor, status, date, and department.
 *
 * @author Mobina
 */
@Repository
public interface AppointmentRepository extends BaseEntityRepository<Appointment, Long> {

    // ========== Update Operations ==========

    @Modifying
    @Query("UPDATE appointmentEntity a SET a.status = :status WHERE a.id = :appointmentId")
    void updateStatus(@Param("appointmentId") Long appointmentId,
                      @Param("status") AppointmentStatus status);

    @Modifying
    @Query("UPDATE appointmentEntity a SET a.status = 'CANCELLED', a.canceledReason = :reason, a.canceledBy = :canceledBy, a.canceledAt = CURRENT_TIMESTAMP WHERE a.id = :appointmentId")
    void cancelAppointment(@Param("appointmentId") Long appointmentId,
                           @Param("reason") String reason,
                           @Param("canceledBy") Long canceledBy);

    // ========== Find by Relationships ==========

    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDepartmentId(Long departmentId);
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);         // LocalDate ✓

    // ========== Find by Status ==========

    List<Appointment> findByStatus(AppointmentStatus status);

    // ========== Find by Date Range ==========

    List<Appointment> findByAppointmentDateBetween(LocalDate start, LocalDate end); // LocalDate ✓

    // ========== Find by Doctor + Date ==========

    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate date); // LocalDate ✓

    // ========== Find by Patient + Date ==========

    List<Appointment> findByPatientIdAndAppointmentDate(Long patientId, LocalDate date);                    // LocalDate ✓

    // ========== Find by Patient + Status ==========

    List<Appointment> findByPatientIdAndStatus(Long patientId, AppointmentStatus status);

    // ========== Find by Doctor + Status ==========

    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    // ========== Count Methods ==========

    long countByPatientIdAndStatus(Long patientId, AppointmentStatus status);
    long countByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    // ========== Find by Creation Date ==========

    List<Appointment> findByCreatedAtAfter(LocalDateTime createdAt);    // createdAt از BaseEntity هست، LocalDateTime ✓
    List<Appointment> findByCreatedAtBefore(LocalDateTime createdAt);   // createdAt از BaseEntity هست، LocalDateTime ✓

    // ========== Find by Cancellation Date ==========

    List<Appointment> findByCanceledAtAfter(LocalDateTime canceledAt);  // canceledAt در entity، LocalDateTime ✓
    List<Appointment> findByCanceledAtBefore(LocalDateTime canceledAt); // canceledAt در entity، LocalDateTime ✓

    // ========== Find by User ==========

    List<Appointment> findByCreatedBy(Long createdBy);
    List<Appointment> findByCanceledBy(Long canceledBy);

    @Query("SELECT a FROM appointmentEntity a " +
            "JOIN FETCH a.patient p " +
            "JOIN FETCH a.doctor d " +
            "WHERE p.id = :patientId AND a.appointmentDate > :date AND a.status != :status " +
            "ORDER BY a.appointmentDate ASC, a.startTime ASC")
    List<Appointment> findUpcomingByPatientId(@Param("patientId") Long patientId,
                                              @Param("date") LocalDate date,
                                              @Param("status") AppointmentStatus status);

    @Query("SELECT a FROM appointmentEntity a " +
            "JOIN FETCH a.patient p " +
            "JOIN FETCH a.doctor d " +
            "WHERE p.id = :patientId AND a.appointmentDate < :date AND a.status != :status " +
            "ORDER BY a.appointmentDate DESC")
    List<Appointment> findPastByPatientId(@Param("patientId") Long patientId,
                                          @Param("date") LocalDate date,
                                          @Param("status") AppointmentStatus status);

    // AppointmentRepository.java
    @Query("SELECT a FROM appointmentEntity a " +
            "JOIN FETCH a.patient p " +
            "JOIN FETCH a.doctor d " +
            "WHERE p.id = :patientId AND a.appointmentDate >= :date AND a.status != :status " +
            "ORDER BY a.appointmentDate ASC, a.startTime ASC")
    List<Appointment> findFirstUpcomingByPatientId(@Param("patientId") Long patientId,
                                                   @Param("date") LocalDate date,
                                                   @Param("status") AppointmentStatus status);

    @Query("SELECT DISTINCT a FROM appointmentEntity a " +
            "JOIN FETCH a.patient p " +
            "LEFT JOIN FETCH p.currentRoom cr " +
            "JOIN FETCH a.doctor d " +
            "LEFT JOIN FETCH d.department dd " +
            "LEFT JOIN FETCH d.subSpecialities ss " +
            "LEFT JOIN FETCH a.department dep " +
            "WHERE d.id = :doctorId " +
            "ORDER BY a.appointmentDate DESC, a.startTime DESC")
    List<Appointment> findByDoctorIdWithDetails(@Param("doctorId") Long doctorId);

    @Query("SELECT DISTINCT a FROM appointmentEntity a " +
            "JOIN FETCH a.patient p " +
            "LEFT JOIN FETCH p.currentRoom cr " +
            "JOIN FETCH a.doctor d " +
            "LEFT JOIN FETCH d.department dd " +
            "LEFT JOIN FETCH d.subSpecialities ss " +
            "LEFT JOIN FETCH a.department dep " +
            "ORDER BY a.appointmentDate DESC, a.startTime DESC")
    List<Appointment> findAllWithDetails();

}