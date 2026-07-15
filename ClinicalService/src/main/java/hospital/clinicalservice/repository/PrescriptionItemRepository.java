package hospital.clinicalservice.repository;

import hospital.clinicalservice.model.PrescriptionItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionItemRepository extends BaseEntityRepository<PrescriptionItem, Long> {

    // ==================== Prescription Queries ====================

    List<PrescriptionItem> findByPrescriptionId(Long prescriptionId);

    // ==================== Drug Queries ====================

    List<PrescriptionItem> findByDrugId(Long drugId);

    @Query("SELECT pi FROM prescriptionItemEntity pi WHERE pi.deleted = false AND LOWER(pi.drugName) LIKE LOWER(CONCAT('%', :drugName, '%'))")
    List<PrescriptionItem> findByDrugNameContaining(@Param("drugName") String drugName);

    // ==================== Patient Queries ====================

    @Query("SELECT pi FROM prescriptionItemEntity pi WHERE pi.deleted = false AND pi.prescription.patientId = :patientId")
    List<PrescriptionItem> findByPatientId(@Param("patientId") Long patientId);

    // ==================== Count ====================

    long countByPrescriptionId(Long prescriptionId);

    long countByDrugId(Long drugId);
}
