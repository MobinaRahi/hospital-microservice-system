package hospital.labservice.repository;

import hospital.labservice.model.LabRequest;
import hospital.labservice.model.enums.RequestPriority;
import hospital.labservice.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for LabRequest entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByRequestNumber - Find request by unique number</li>
 *   <li>findByPatientId - Requests for a specific patient</li>
 *   <li>findByDoctorId - Requests by a specific doctor</li>
 *   <li>findByStatus - Requests by status</li>
 *   <li>findByPriority - Requests by priority</li>
 *   <li>findUrgentPendingRequests - Urgent requests still pending</li>
 *   <li>findPendingRequests - All pending requests</li>
 *   <li>findByRequestDateBetween - Date range queries</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface LabRequestRepository extends BaseEntityRepository<LabRequest, Long> {

    /**
     * Finds a lab request by its unique request number.
     *
     * @param requestNumber the request number (e.g., "LAB-2026-000123")
     * @return lab request if found
     */
    Optional<LabRequest> findByRequestNumber(String requestNumber);

    /**
     * Finds all lab requests for a specific patient.
     *
     * @param patientId the patient ID from CoreService
     * @return list of lab requests for the patient
     */
    List<LabRequest> findByPatientId(Long patientId);

    /**
     * Finds all lab requests by a specific doctor.
     *
     * @param doctorId the doctor ID from CoreService
     * @return list of lab requests ordered by the doctor
     */
    List<LabRequest> findByDoctorId(Long doctorId);

    /**
     * Finds lab requests by status.
     *
     * @param status the request status (PENDING, APPROVED, etc.)
     * @return list of lab requests with the status
     */
    List<LabRequest> findByStatus(RequestStatus status);

    /**
     * Finds lab requests by priority.
     *
     * @param priority the request priority (ROUTINE, URGENT, STAT)
     * @return list of lab requests with the priority
     */
    List<LabRequest> findByPriority(RequestPriority priority);

    /**
     * Finds lab requests by patient and status.
     *
     * @param patientId the patient ID
     * @param status    the request status
     * @return list of matching lab requests
     */
    List<LabRequest> findByPatientIdAndStatus(Long patientId, RequestStatus status);

    /**
     * Finds urgent or STAT requests that are still pending or approved.
     * Used for priority queue display.
     *
     * @param statuses list of statuses to include
     * @return list of urgent pending requests
     */
    @Query("SELECT r FROM LabRequest r WHERE r.priority IN ('URGENT', 'STAT') AND r.status IN :statuses AND r.deleted = false ORDER BY r.requestDate ASC")
    List<LabRequest> findUrgentPendingRequests(@Param("statuses") List<RequestStatus> statuses);

    /**
     * Finds all pending lab requests ordered by date.
     *
     * @return list of pending requests
     */
    List<LabRequest> findByStatusOrderByRequestDateAsc(RequestStatus status);

    /**
     * Finds lab requests within a date range.
     * Used for reporting and analytics.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of requests in the range
     */
    List<LabRequest> findByRequestDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Checks if a request number already exists.
     *
     * @param requestNumber the request number to check
     * @return true if exists
     */
    boolean existsByRequestNumber(String requestNumber);

    /**
     * Counts requests by status.
     *
     * @param status the request status
     * @return number of requests with the status
     */
    long countByStatus(RequestStatus status);

    /**
     * Finds lab requests by encounter ID.
     *
     * @param encounterId the encounter ID from ClinicalService
     * @return list of lab requests for the encounter
     */
    List<LabRequest> findByEncounterId(Long encounterId);

    /**
     * Finds lab requests by doctor and status.
     * Used for: "Show me all pending requests from Dr. X"
     *
     * @param doctorId the doctor ID from CoreService
     * @param status   the request status
     * @return list of matching lab requests
     */
    List<LabRequest> findByDoctorIdAndStatus(Long doctorId, RequestStatus status);

    /**
     * Counts requests for a specific patient.
     *
     * @param patientId the patient ID
     * @return number of requests for the patient
     */
    long countByPatientId(Long patientId);
}
