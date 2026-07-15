package hospital.clinicalservice.repository;

import hospital.clinicalservice.model.Diagnosis;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends BaseEntityRepository<Diagnosis, Long> {

    // ==================== Encounter Queries ====================

    List<Diagnosis> findByEncounterId(Long encounterId);

    List<Diagnosis> findByEncounterIdAndPrimary(Long encounterId, boolean primary);

    // ==================== ICD-10 Queries ====================

    List<Diagnosis> findByIcd10Code(String icd10Code);

    @Query("SELECT d FROM diagnosisEntity d WHERE d.deleted = false AND LOWER(d.icd10Code) LIKE LOWER(CONCAT('%', :code, '%'))")
    List<Diagnosis> findByIcd10CodeContaining(@Param("code") String code);

    // ==================== Patient Queries ====================

    @Query("SELECT d FROM diagnosisEntity d WHERE d.deleted = false AND d.encounter.patientId = :patientId ORDER BY d.encounter.encounterDate DESC")
    List<Diagnosis> findByPatientId(@Param("patientId") Long patientId);

    // ==================== Search ====================

    @Query("SELECT d FROM diagnosisEntity d WHERE d.deleted = false AND " +
            "(LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(d.icd10Code) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Diagnosis> searchDiagnoses(@Param("query") String query);

    // ==================== Count ====================

    long countByEncounterId(Long encounterId);

    @Query("SELECT COUNT(d) FROM diagnosisEntity d WHERE d.deleted = false AND d.encounter.patientId = :patientId")
    long countByPatientId(@Param("patientId") Long patientId);
}
