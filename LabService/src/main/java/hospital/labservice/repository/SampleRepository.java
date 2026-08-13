package hospital.labservice.repository;

import hospital.labservice.model.Sample;
import hospital.labservice.model.enums.SampleQuality;
import hospital.labservice.model.enums.SampleType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Sample entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findBySampleNumber - Find by unique sample number</li>
 *   <li>findByLabRequestId - Samples for a specific request</li>
 *   <li>findBySampleType - Samples by biological type</li>
 *   <li>findByQuality - Samples by quality assessment</li>
 *   <li>findUnreceivedSamples - Samples not yet received at lab</li>
 *   <li>findByCollectionDateBetween - Date range queries</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface SampleRepository extends BaseEntityRepository<Sample, Long> {

    /**
     * Finds a sample by its unique sample number.
     *
     * @param sampleNumber the sample number (e.g., "SMP-2026-000456")
     * @return sample if found
     */
    Optional<Sample> findBySampleNumber(String sampleNumber);

    /**
     * Finds all samples for a specific lab request.
     *
     * @param labRequestId the parent lab request ID
     * @return list of samples for the request
     */
    List<Sample> findByLabRequestId(Long labRequestId);

    /**
     * Finds samples by biological type.
     *
     * @param sampleType the sample type (BLOOD, URINE, etc.)
     * @return list of samples of the type
     */
    List<Sample> findBySampleType(SampleType sampleType);

    /**
     * Finds samples by quality assessment.
     * Used for quality control reporting.
     *
     * @param quality the quality status (GOOD, HEMOLYZED, etc.)
     * @return list of samples with the quality
     */
    List<Sample> findByQuality(SampleQuality quality);

    /**
     * Finds samples not yet received at the laboratory.
     * receivedAtLab is null means the sample hasn't arrived yet.
     *
     * @return list of unreceived samples
     */
    @Query("SELECT s FROM Sample s WHERE s.receivedAtLab IS NULL AND s.deleted = false ORDER BY s.collectionDate ASC")
    List<Sample> findUnreceivedSamples();

    /**
     * Finds samples with quality issues.
     * Used for quality control review.
     *
     * @return list of samples with non-GOOD quality
     */
    @Query("SELECT s FROM Sample s WHERE s.quality <> 'GOOD' AND s.deleted = false")
    List<Sample> findSamplesWithQualityIssues();

    /**
     * Finds samples collected within a date range.
     * Used for reporting and analytics.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of samples collected in the range
     */
    List<Sample> findByCollectionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Checks if a sample number already exists.
     *
     * @param sampleNumber the sample number to check
     * @return true if exists
     */
    boolean existsBySampleNumber(String sampleNumber);

    /**
     * Finds samples by sample type and quality.
     *
     * @param sampleType the sample type
     * @param quality    the quality assessment
     * @return list of matching samples
     */
    List<Sample> findBySampleTypeAndQuality(SampleType sampleType, SampleQuality quality);

    /**
     * Counts unreceived samples.
     *
     * @return number of samples not yet received at the lab
     */
    @Query("SELECT COUNT(s) FROM Sample s WHERE s.receivedAtLab IS NULL AND s.deleted = false")
    long countUnreceivedSamples();
}
