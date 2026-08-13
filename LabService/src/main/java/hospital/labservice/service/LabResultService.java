package hospital.labservice.service;

import hospital.labservice.dto.labresult.LabResultCreateDto;
import hospital.labservice.dto.labresult.LabResultResponseDto;
import hospital.labservice.model.enums.ResultFlag;

import java.util.List;

/**
 * Service interface for LabResult management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each result is linked to exactly one LabRequestItem (One-to-One)</li>
 *   <li>Results must be verified by a qualified technician before release</li>
 *   <li>Critical results (CRITICAL_LOW, CRITICAL_HIGH) require immediate notification</li>
 *   <li>Flag is determined by comparing value against normalRange</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface LabResultService {

    /**
     * Creates a new lab result for a request item.
     *
     * @param dto the result creation data
     * @return the created result
     */
    LabResultResponseDto createResult(LabResultCreateDto dto);

    /**
     * Gets a lab result by its ID.
     *
     * @param id the result ID
     * @return the result
     */
    LabResultResponseDto getResultById(Long id);

    /**
     * Gets the result for a specific request item.
     *
     * @param requestItemId the request item ID
     * @return the result
     */
    LabResultResponseDto getResultByRequestItem(Long requestItemId);

    /**
     * Gets results by flag.
     *
     * @param flag the result flag
     * @return list of results with the flag
     */
    List<LabResultResponseDto> getResultsByFlag(ResultFlag flag);

    /**
     * Gets unverified results (pending verification).
     *
     * @return list of unverified results
     */
    List<LabResultResponseDto> getUnverifiedResults();

    /**
     * Gets critical results requiring immediate attention.
     *
     * @return list of critical results
     */
    List<LabResultResponseDto> getCriticalResults();

    /**
     * Gets abnormal results (non-NORMAL flags).
     *
     * @return list of abnormal results
     */
    List<LabResultResponseDto> getAbnormalResults();

    /**
     * Verifies a lab result.
     * Sets verifiedAt timestamp and verifiedBy user.
     *
     * @param id         the result ID
     * @param verifiedBy the user ID verifying the result
     * @return the verified result
     */
    LabResultResponseDto verifyResult(Long id, Long verifiedBy);

    /**
     * Soft-deletes a lab result.
     *
     * @param id the result ID
     */
    void deleteResult(Long id);
}
