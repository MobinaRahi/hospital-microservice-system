package hospital.clinicalservice.repository;

import hospital.clinicalservice.model.Observation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Observation entity.
 * Provides query methods by encounter, LOINC code, and patient.
 *
 * @author Mobina
 */
@Repository
public interface ObservationRepository extends BaseEntityRepository<Observation, Long> {

    // ==================== Encounter Queries ====================

    List<Observation> findByEncounterId(Long encounterId);

    // ==================== Patient Queries ====================

    List<Observation> findByPatientIdOrderByObservedAtDesc(Long patientId);

    @Query("SELECT o FROM observationEntity o WHERE o.deleted = false AND o.patientId = :patientId AND o.loincCode = :loincCode ORDER BY o.observedAt DESC")
    List<Observation> findByPatientIdAndLoincCode(@Param("patientId") Long patientId, @Param("loincCode") String loincCode);

    // ==================== LOINC Queries ====================

    List<Observation> findByLoincCode(String loincCode);

    // ==================== Date Queries ====================

    List<Observation> findByObservedAtBetween(LocalDateTime start, LocalDateTime end);

    // ==================== Abnormal Queries ====================

    @Query("SELECT o FROM observationEntity o WHERE o.deleted = false AND o.patientId = :patientId AND o.abnormal = true ORDER BY o.observedAt DESC")
    List<Observation> findAbnormalByPatientId(@Param("patientId") Long patientId);

    // ==================== Latest Values ====================

    @Query("SELECT o FROM observationEntity o WHERE o.deleted = false AND o.patientId = :patientId AND o.loincCode = :loincCode ORDER BY o.observedAt DESC LIMIT 1")
    Observation findLatestByPatientAndLoinc(@Param("patientId") Long patientId, @Param("loincCode") String loincCode);

    // ==================== Count ====================

    long countByPatientId(Long patientId);

    long countByEncounterId(Long encounterId);

    @Query("SELECT COUNT(o) FROM observationEntity o WHERE o.deleted = false AND o.patientId = :patientId AND o.abnormal = true")
    long countAbnormalByPatientId(@Param("patientId") Long patientId);
}
