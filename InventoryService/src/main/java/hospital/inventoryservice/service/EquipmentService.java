package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.equipment.EquipmentCreateDto;
import hospital.inventoryservice.dto.equipment.EquipmentResponseDto;
import hospital.inventoryservice.dto.equipment.EquipmentUpdateDto;
import hospital.inventoryservice.model.enums.EquipmentStatus;
import hospital.inventoryservice.model.enums.EquipmentType;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Equipment management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Serial number must be unique</li>
 *   <li>Equipment status transitions: AVAILABLE → IN_USE → MAINTENANCE → AVAILABLE</li>
 *   <li>Broken equipment should be flagged for repair or disposal</li>
 *   <li>Warranty expiry is tracked</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface EquipmentService {

    // ════════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Creates a new equipment record.
     *
     * @param dto the equipment creation data
     * @return the created equipment
     */
    EquipmentResponseDto createEquipment(EquipmentCreateDto dto);

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    /**
     * Gets an equipment by its ID.
     *
     * @param id the equipment ID
     * @return the equipment
     */
    EquipmentResponseDto getEquipmentById(Long id);

    /**
     * Gets an equipment by its serial number.
     *
     * @param serialNumber the serial number
     * @return the equipment
     */
    EquipmentResponseDto getEquipmentBySerialNumber(String serialNumber);

    /**
     * Gets all active equipment.
     *
     * @return list of active equipment
     */
    List<EquipmentResponseDto> getAllActiveEquipment();

    /**
     * Gets all equipment (including inactive).
     *
     * @return list of all equipment
     */
    List<EquipmentResponseDto> getAllEquipment();

    /**
     * Gets equipment by type.
     *
     * @param type the equipment type
     * @return list of equipment
     */
    List<EquipmentResponseDto> getEquipmentByType(EquipmentType type);

    /**
     * Gets equipment by status.
     *
     * @param status the equipment status
     * @return list of equipment
     */
    List<EquipmentResponseDto> getEquipmentByStatus(EquipmentStatus status);

    /**
     * Gets available equipment (ready for assignment).
     *
     * @return list of available equipment
     */
    List<EquipmentResponseDto> getAvailableEquipment();

    /**
     * Gets equipment with expired warranty.
     *
     * @return list of equipment with expired warranty
     */
    List<EquipmentResponseDto> getEquipmentWithExpiredWarranty();

    /**
     * Searches equipment by location.
     *
     * @param location the location to search
     * @return list of equipment in the location
     */
    List<EquipmentResponseDto> searchByLocation(String location);

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    /**
     * Updates an existing equipment record.
     * <p>Only provided fields in the DTO will be updated.</p>
     *
     * @param id  the equipment ID
     * @param dto the update data
     * @return the updated equipment
     */
    EquipmentResponseDto updateEquipment(Long id, EquipmentUpdateDto dto);

    /**
     * Changes the status of an equipment.
     *
     * @param id     the equipment ID
     * @param status the new status
     * @return the updated equipment
     */
    EquipmentResponseDto changeStatus(Long id, EquipmentStatus status);

    /**
     * Toggles the active status of an equipment.
     *
     * @param id the equipment ID
     * @return the updated equipment
     */
    EquipmentResponseDto toggleActive(Long id);

    // ════════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    /**
     * Soft-deletes an equipment.
     *
     * @param id the equipment ID
     */
    void deleteEquipment(Long id);

    // ════════════════════════════════════════════════════════════════════
    // Validation
    // ════════════════════════════════════════════════════════════════════

    /**
     * Checks if a serial number is already in use.
     *
     * @param serialNumber the serial number to check
     * @return true if exists
     */
    boolean serialNumberExists(String serialNumber);
}
