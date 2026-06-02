package hospital.coreservice.repository;

import hospital.coreservice.model.Appointment;
import hospital.coreservice.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Appointment entity.
 *
 * @author Mobina
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

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
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    // ========== Find by Status ==========

    List<Appointment> findByStatus(AppointmentStatus status);

    // ========== Find by Date Range ==========

    List<Appointment> findByAppointmentDateBetween(LocalDate start, LocalDate end);

    // ========== Find by Doctor + Date ==========

    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate date);

    // ========== Find by Patient + Date ==========

    List<Appointment> findByPatientIdAndAppointmentDate(Long patientId, LocalDate date);
    List<Appointment> findByPatientIdAndAppointmentDateGreaterThanEqual(Long patientId, LocalDate date);
    List<Appointment> findByPatientIdAndAppointmentDateLessThanEqual(Long patientId, LocalDate date);

    // ========== Find by Patient + Status ==========

    List<Appointment> findByPatientIdAndStatus(Long patientId, AppointmentStatus status);

    // ========== Find by Doctor + Status ==========

    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    // ========== Count Methods ==========

    long countByPatientIdAndStatus(Long patientId, AppointmentStatus status);
    long countByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    // ========== Find by Creation Date ==========

    List<Appointment> findByCreatedAtAfter(LocalDateTime createdAt);
    List<Appointment> findByCreatedAtBefore(LocalDateTime createdAt);

    // ========== Find by Cancellation Date ==========

    List<Appointment> findByCanceledAtAfter(LocalDateTime canceledAt);
    List<Appointment> findByCanceledAtBefore(LocalDateTime canceledAt);

    // ========== Find by User ==========

    List<Appointment> findByCreatedBy(Long createdBy);
    List<Appointment> findByCanceledBy(Long canceledBy);
}
