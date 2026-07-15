package hospital.clinicalservice.repository;

import hospital.clinicalservice.model.Triage;
import hospital.clinicalservice.model.enums.TriageLevel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriageRepository extends BaseEntityRepository<Triage, Long> {

    // ==================== Encounter Queries ====================

    List<Triage> findByEncounterId(Long encounterId);

    // ==================== Patient Queries ====================

    List<Triage> findByPatientIdOrderByTriagedAtDesc(Long patientId);

    Triage findFirstByPatientIdOrderByTriagedAtDesc(Long patientId);

    // ==================== Level Queries ====================

    List<Triage> findByLevel(TriageLevel level);

    @Query("SELECT t FROM triageEntity t WHERE t.deleted = false AND t.level IN ('LEVEL_1', 'LEVEL_2') ORDER BY t.triagedAt DESC")
    List<Triage> findCriticalCases();

    // ==================== Count ====================

    long countByPatientId(Long patientId);

    long countByLevel(TriageLevel level);

    @Query("SELECT COUNT(t) FROM triageEntity t WHERE t.deleted = false AND t.level IN ('LEVEL_1', 'LEVEL_2')")
    long countCriticalCases();
}
