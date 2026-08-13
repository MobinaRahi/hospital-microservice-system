package hospital.labservice.service;

import hospital.labservice.dto.sample.SampleCreateDto;
import hospital.labservice.dto.sample.SampleResponseDto;
import hospital.labservice.dto.sample.SampleUpdateDto;
import hospital.labservice.model.enums.SampleQuality;
import hospital.labservice.model.enums.SampleType;

import java.util.List;

/**
 * Service interface for Sample management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each sample has a unique sampleNumber</li>
 *   <li>Sample quality is assessed upon receipt at the lab</li>
 *   <li>Transport time is calculated from collection to receipt</li>
 *   <li>Samples with quality issues may need re-collection</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface SampleService {

    /**
     * Creates a new sample.
     *
     * @param dto the sample creation data
     * @return the created sample
     */
    SampleResponseDto createSample(SampleCreateDto dto);

    /**
     * Gets a sample by its ID.
     *
     * @param id the sample ID
     * @return the sample
     */
    SampleResponseDto getSampleById(Long id);

    /**
     * Gets a sample by its unique number.
     *
     * @param sampleNumber the sample number
     * @return the sample
     */
    SampleResponseDto getSampleByNumber(String sampleNumber);

    /**
     * Gets all samples for a specific lab request.
     *
     * @param requestId the parent request ID
     * @return list of samples for the request
     */
    List<SampleResponseDto> getSamplesByRequest(Long requestId);

    /**
     * Gets samples by type.
     *
     * @param sampleType the sample type
     * @return list of samples of the type
     */
    List<SampleResponseDto> getSamplesByType(SampleType sampleType);

    /**
     * Gets samples with quality issues.
     *
     * @return list of samples with non-GOOD quality
     */
    List<SampleResponseDto> getSamplesWithQualityIssues();

    /**
     * Gets unreceived samples (not yet arrived at lab).
     *
     * @return list of unreceived samples
     */
    List<SampleResponseDto> getUnreceivedSamples();

    /**
     * Receives a sample at the laboratory.
     * Sets receivedAtLab timestamp and receivedBy user.
     *
     * @param id         the sample ID
     * @param receivedBy the user ID receiving the sample
     * @return the updated sample
     */
    SampleResponseDto receiveSample(Long id, Long receivedBy);

    /**
     * Updates sample quality assessment.
     *
     * @param id      the sample ID
     * @param quality the new quality assessment
     * @return the updated sample
     */
    SampleResponseDto updateSampleQuality(Long id, SampleQuality quality);

    /**
     * Updates an existing sample (only mutable fields).
     *
     * @param id  the sample ID
     * @param dto the update data
     * @return the updated sample
     */
    SampleResponseDto updateSample(Long id, SampleUpdateDto dto);

    /**
     * Soft-deletes a sample.
     *
     * @param id the sample ID
     */
    void deleteSample(Long id);

    /**
     * Checks if a sample number already exists.
     *
     * @param sampleNumber the sample number to check
     * @return true if exists
     */
    boolean sampleNumberExists(String sampleNumber);
}
