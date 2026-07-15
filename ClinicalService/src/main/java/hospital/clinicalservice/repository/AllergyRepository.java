package hospital.clinicalservice.repository;

import hospital.clinicalservice.model.Allergy;
import hospital.clinicalservice.model.enums.AllergySeverity;
import hospital.clinicalservice.model.enums.AllergyType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergyRepository extends BaseEntityRepository<Allergy, Long> {

    // ==================== Patient Queries ====================

    List<Allergy> findByPatientId(Long patientId);

    List<Allergy> findByPatientIdAndActive(Long patientId, boolean active);

    @Query("SELECT a FROM allergyEntity a WHERE a.deleted = false AND a.patientId = :patientId AND a.active = true")
    List<Allergy> findActiveByPatientId(@Param("patientId") Long patientId);

    // ==================== Type Queries ====================

    List<Allergy> findByType(AllergyType type);

    List<Allergy> findByPatientIdAndType(Long patientId, AllergyType type);

    // ==================== Severity Queries ====================

    List<Allergy> findBySeverity(AllergySeverity severity);

    @Query("SELECT a FROM allergyEntity a WHERE a.deleted = false AND a.patientId = :patientId AND a.severity IN ('SEVERE', 'LIFE_THREATENING') AND a.active = true")
    List<Allergy> findSevereByPatientId(@Param("patientId") Long patientId);

    // ==================== Search ====================

    @Query("SELECT a FROM allergyEntity a WHERE a.deleted = false AND LOWER(a.allergenName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Allergy> searchByAllergenName(@Param("query") String query);

    // ==================== Count ====================

    long countByPatientId(Long patientId);

    long countByPatientIdAndActive(Long patientId, boolean active);

    @Query("SELECT COUNT(a) FROM allergyEntity a WHERE a.deleted = false AND a.patientId = :patientId AND a.severity IN ('SEVERE', 'LIFE_THREATENING') AND a.active = true")
    long countSevereByPatientId(@Param("patientId") Long patientId);
}
