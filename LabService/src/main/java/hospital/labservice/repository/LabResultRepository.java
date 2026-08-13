package hospital.labservice.repository;

import hospital.labservice.model.LabResult;
import hospital.labservice.model.enums.ResultFlag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for LabResult entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByRequestItemId - Result for a specific request item</li>
 *   <li>findByFlag - Results by result flag (NORMAL, HIGH, etc.)</li>
 *   <li>findUnverifiedResults - Results pending verification</li>
 *   <li>findCriticalResults - Results with critical flags</li>
 *   <li>findByPerformedAtBetween - Date range queries</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface LabResultRepository extends BaseEntityRepository<LabResult, Long> {

    /**
     * Finds the result for a specific lab request item.
     * Each request item has at most one result (One-to-One).
     *
     * @param requestItemId the lab request item ID
     * @return lab result if found
     */
    Optional<LabResult> findByRequestItemId(Long requestItemId);

    /**
     * Finds results by flag.
     * Used for reporting abnormal results.
     *
     * @param flag the result flag (NORMAL, LOW, HIGH, etc.)
     * @return list of results with the flag
     */
    List<LabResult> findByFlag(ResultFlag flag);

    /**
     * Finds results that have not been verified yet.
     * These results need verification by a qualified technician.
     *
     * @return list of unverified results
     */
    @Query("SELECT r FROM LabResult r WHERE r.verifiedAt IS NULL AND r.deleted = false ORDER BY r.performedAt ASC")
    List<LabResult> findUnverifiedResults();

    /**
     * Finds critical results (CRITICAL_LOW or CRITICAL_HIGH).
     * Critical results require immediate notification to the ordering doctor.
     *
     * @return list of critical results
     */
    @Query("SELECT r FROM LabResult r WHERE r.flag IN ('CRITICAL_LOW', 'CRITICAL_HIGH') AND r.deleted = false")
    List<LabResult> findCriticalResults();

    /**
     * Finds results performed within a date range.
     * Used for reporting and analytics.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of results in the range
     */
    List<LabResult> findByPerformedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds results verified within a date range.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of verified results in the range
     */
    List<LabResult> findByVerifiedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds results performed by a specific user.
     *
     * @param performedBy the user ID of the technician
     * @return list of results performed by the user
     */
    List<LabResult> findByPerformedBy(Long performedBy);

    /**
     * Finds abnormal results (any flag that is not NORMAL).
     * Used for quality review.
     *
     * @return list of abnormal results
     */
    @Query("SELECT r FROM LabResult r WHERE r.flag <> 'NORMAL' AND r.deleted = false ORDER BY r.performedAt DESC")
    List<LabResult> findAbnormalResults();

    /**
     * Counts results by flag.
     *
     * @param flag the result flag
     * @return number of results with the flag
     */
    long countByFlag(ResultFlag flag);
}
