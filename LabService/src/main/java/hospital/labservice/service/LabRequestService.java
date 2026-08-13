package hospital.labservice.service;

import hospital.labservice.dto.labrequest.LabRequestCreateDto;
import hospital.labservice.dto.labrequest.LabRequestResponseDto;
import hospital.labservice.dto.labrequest.LabRequestUpdateDto;
import hospital.labservice.model.enums.RequestStatus;

import java.util.List;

/**
 * Service interface for LabRequest management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each request must have a unique requestNumber</li>
 *   <li>Status workflow: PENDING → APPROVED → SAMPLE_COLLECTED → IN_PROGRESS → COMPLETED</li>
 *   <li>Requests can be REJECTED from PENDING, CANCELLED from any non-COMPLETED state</li>
 *   <li>STAT/URGENT requests are processed first</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface LabRequestService {

    /**
     * Creates a new lab request with items.
     *
     * @param dto the request creation data
     * @return the created request
     */
    LabRequestResponseDto createLabRequest(LabRequestCreateDto dto);

    /**
     * Gets a lab request by its ID.
     *
     * @param id the request ID
     * @return the request
     */
    LabRequestResponseDto getLabRequestById(Long id);

    /**
     * Gets a lab request by its unique number.
     *
     * @param requestNumber the request number
     * @return the request
     */
    LabRequestResponseDto getLabRequestByNumber(String requestNumber);

    /**
     * Gets all lab requests.
     *
     * @return list of all requests
     */
    List<LabRequestResponseDto> getAllLabRequests();

    /**
     * Gets lab requests by patient ID.
     *
     * @param patientId the patient ID
     * @return list of requests for the patient
     */
    List<LabRequestResponseDto> getRequestsByPatient(Long patientId);

    /**
     * Gets lab requests by doctor ID.
     *
     * @param doctorId the doctor ID
     * @return list of requests by the doctor
     */
    List<LabRequestResponseDto> getRequestsByDoctor(Long doctorId);

    /**
     * Gets lab requests by status.
     *
     * @param status the request status
     * @return list of requests with the status
     */
    List<LabRequestResponseDto> getRequestsByStatus(RequestStatus status);

    /**
     * Gets urgent pending requests (STAT and URGENT priority).
     *
     * @return list of urgent pending requests
     */
    List<LabRequestResponseDto> getUrgentPendingRequests();

    /**
     * Updates an existing lab request (priority and clinical notes only).
     *
     * @param id  the request ID
     * @param dto the update data
     * @return the updated request
     */
    LabRequestResponseDto updateLabRequest(Long id, LabRequestUpdateDto dto);

    /**
     * Approves a pending lab request.
     *
     * @param id the request ID
     * @return the approved request
     */
    LabRequestResponseDto approveRequest(Long id);

    /**
     * Rejects a pending lab request.
     *
     * @param id the request ID
     * @return the rejected request
     */
    LabRequestResponseDto rejectRequest(Long id);

    /**
     * Marks sample as collected for a request.
     *
     * @param id the request ID
     * @return the updated request
     */
    LabRequestResponseDto markSampleCollected(Long id);

    /**
     * Starts processing a request (after sample collection).
     *
     * @param id the request ID
     * @return the updated request
     */
    LabRequestResponseDto startProcessing(Long id);

    /**
     * Completes a request (all items processed).
     *
     * @param id the request ID
     * @return the completed request
     */
    LabRequestResponseDto completeRequest(Long id);

    /**
     * Cancels a lab request.
     *
     * @param id the request ID
     * @return the cancelled request
     */
    LabRequestResponseDto cancelRequest(Long id);

    /**
     * Soft-deletes a lab request.
     *
     * @param id the request ID
     */
    void deleteLabRequest(Long id);

    /**
     * Checks if a request number already exists.
     *
     * @param requestNumber the request number to check
     * @return true if exists
     */
    boolean requestNumberExists(String requestNumber);
}
