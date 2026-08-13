package hospital.labservice.repository;

import hospital.labservice.model.LabRequestItem;
import hospital.labservice.model.enums.RequestItemStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for LabRequestItem entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByLabRequestId - Items for a specific request</li>
 *   <li>findByTestId - Items for a specific test</li>
 *   <li>findByStatus - Items by processing status</li>
 *   <li>findByLabRequestIdAndStatus - Items by request and status</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface LabRequestItemRepository extends BaseEntityRepository<LabRequestItem, Long> {

    /**
     * Finds all items for a specific lab request.
     *
     * @param labRequestId the parent lab request ID
     * @return list of lab request items
     */
    List<LabRequestItem> findByLabRequestId(Long labRequestId);

    /**
     * Finds items that reference a specific lab test.
     * Used for: "How many times has test X been requested?"
     *
     * @param testId the lab test ID
     * @return list of items with the test
     */
    List<LabRequestItem> findByTestId(Long testId);

    /**
     * Finds items by processing status.
     *
     * @param status the item status (PENDING, PROCESSING, COMPLETED, CANCELLED)
     * @return list of items with the status
     */
    List<LabRequestItem> findByStatus(RequestItemStatus status);

    /**
     * Finds items for a specific request that match a given status.
     *
     * @param labRequestId the parent lab request ID
     * @param status       the item status
     * @return list of matching items
     */
    List<LabRequestItem> findByLabRequestIdAndStatus(Long labRequestId, RequestItemStatus status);

    /**
     * Finds a specific item within a lab request.
     *
     * @param labRequestId the parent lab request ID
     * @param id           the item ID
     * @return lab request item if found
     */
    Optional<LabRequestItem> findByLabRequestIdAndId(Long labRequestId, Long id);

    /**
     * Counts items by status.
     *
     * @param status the item status
     * @return number of items with the status
     */
    long countByStatus(RequestItemStatus status);

    /**
     * Finds pending items for a specific test.
     * Used for batch processing.
     *
     * @param testId the lab test ID
     * @param status the item status
     * @return list of matching items
     */
    List<LabRequestItem> findByTestIdAndStatus(Long testId, RequestItemStatus status);

    /**
     * Counts items in a specific lab request.
     *
     * @param labRequestId the parent lab request ID
     * @return number of items in the request
     */
    long countByLabRequestId(Long labRequestId);
}
