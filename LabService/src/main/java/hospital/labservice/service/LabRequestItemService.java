package hospital.labservice.service;

import hospital.labservice.dto.labrequestitem.LabRequestItemCreateDto;
import hospital.labservice.dto.labrequestitem.LabRequestItemResponseDto;
import hospital.labservice.model.enums.RequestItemStatus;

import java.util.List;

/**
 * Service interface for LabRequestItem management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each item belongs to a parent LabRequest</li>
 *   <li>Each item references a specific LabTest</li>
 *   <li>Status workflow: PENDING → PROCESSING → COMPLETED (or CANCELLED)</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface LabRequestItemService {

    /**
     * Creates a new lab request item within a request.
     *
     * @param requestId the parent request ID
     * @param dto       the item creation data
     * @return the created item
     */
    LabRequestItemResponseDto createItem(Long requestId, LabRequestItemCreateDto dto);

    /**
     * Gets a lab request item by its ID.
     *
     * @param id the item ID
     * @return the item
     */
    LabRequestItemResponseDto getItemById(Long id);

    /**
     * Gets all items for a specific lab request.
     *
     * @param requestId the parent request ID
     * @return list of items in the request
     */
    List<LabRequestItemResponseDto> getItemsByRequest(Long requestId);

    /**
     * Gets items by processing status.
     *
     * @param status the item status
     * @return list of items with the status
     */
    List<LabRequestItemResponseDto> getItemsByStatus(RequestItemStatus status);

    /**
     * Starts processing a lab request item.
     *
     * @param id the item ID
     * @return the updated item
     */
    LabRequestItemResponseDto startProcessing(Long id);

    /**
     * Completes a lab request item.
     *
     * @param id the item ID
     * @return the completed item
     */
    LabRequestItemResponseDto completeItem(Long id);

    /**
     * Cancels a lab request item.
     *
     * @param id the item ID
     * @return the cancelled item
     */
    LabRequestItemResponseDto cancelItem(Long id);

    /**
     * Soft-deletes a lab request item.
     *
     * @param id the item ID
     */
    void deleteItem(Long id);
}
