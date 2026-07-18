package hospital.clinicalservice.repository;

import hospital.clinicalservice.model.Prescription;
import hospital.clinicalservice.model.enums.PrescriptionStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Prescription entity.
 * Provides query methods by encounter, patient, status, and doctor.
 *
 * @author Mobina
 */
@Repository
public interface PrescriptionRepository extends BaseEntityRepository<Prescription, Long> {

    // ==================== Encounter Queries ====================

    List<Prescription> findByEncounterId(Long encounterId);

    // ==================== Patient Queries ====================

    List<Prescription> findByPatientIdOrderByPrescribedDateDesc(Long patientId);

    @Query("SELECT p FROM prescriptionEntity p WHERE p.deleted = false AND p.patientId = :patientId AND p.status = :status")
    List<Prescription> findByPatientIdAndStatus(@Param("patientId") Long patientId, @Param("status") PrescriptionStatus status);

    // ==================== Doctor Queries ====================

    List<Prescription> findByDoctorId(Long doctorId);

    // ==================== Status Queries ====================

    List<Prescription> findByStatus(PrescriptionStatus status);

    // ==================== Active Prescriptions ====================

    @Query("SELECT p FROM prescriptionEntity p WHERE p.deleted = false AND p.patientId = :patientId AND p.status = 'ACTIVE' AND (p.expiryDate IS NULL OR p.expiryDate >= :today)")
    List<Prescription> findActiveByPatientId(@Param("patientId") Long patientId, @Param("today") LocalDate today);

    // ==================== Expired Prescriptions ====================

    @Query("SELECT p FROM prescriptionEntity p WHERE p.deleted = false AND p.status = 'ACTIVE' AND p.expiryDate < :today")
    List<Prescription> findExpiredPrescriptions(@Param("today") LocalDate today);

    // ==================== Count ====================

    long countByPatientId(Long patientId);

    long countByDoctorId(Long doctorId);

    long countByStatus(PrescriptionStatus status);
}
