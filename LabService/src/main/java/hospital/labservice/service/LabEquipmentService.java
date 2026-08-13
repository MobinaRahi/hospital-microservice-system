package hospital.labservice.service;

import hospital.labservice.dto.labequipment.LabEquipmentCreateDto;
import hospital.labservice.dto.labequipment.LabEquipmentResponseDto;
import hospital.labservice.dto.labequipment.LabEquipmentUpdateDto;
import hospital.labservice.model.enums.EquipmentStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for LabEquipment management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each equipment has a unique serialNumber</li>
 *   <li>Equipment must be calibrated on schedule</li>
 *   <li>Only OPERATIONAL equipment can be used for testing</li>
 *   <li>Status tracks availability: OPERATIONAL, MAINTENANCE, CALIBRATION, BROKEN, DECOMMISSIONED</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface LabEquipmentService {

    /**
     * Creates new lab equipment.
     *
     * @param dto the equipment creation data
     * @return the created equipment
     */
    LabEquipmentResponseDto createEquipment(LabEquipmentCreateDto dto);

    /**
     * Gets lab equipment by its ID.
     *
     * @param id the equipment ID
     * @return the equipment
     */
    LabEquipmentResponseDto getEquipmentById(Long id);

    /**
     * Gets lab equipment by its serial number.
     *
     * @param serialNumber the serial number
     * @return the equipment
     */
    LabEquipmentResponseDto getEquipmentBySerialNumber(String serialNumber);

    /**
     * Gets all lab equipment.
     *
     * @return list of all equipment
     */
    List<LabEquipmentResponseDto> getAllEquipment();

    /**
     * Gets equipment by status.
     *
     * @param status the equipment status
     * @return list of equipment with the status
     */
    List<LabEquipmentResponseDto> getEquipmentByStatus(EquipmentStatus status);

    /**
     * Gets equipment that needs calibration (overdue).
     *
     * @return list of equipment needing calibration
     */
    List<LabEquipmentResponseDto> getEquipmentNeedingCalibration();

    /**
     * Gets equipment available for use (OPERATIONAL and calibration up to date).
     *
     * @return list of available equipment
     */
    List<LabEquipmentResponseDto> getAvailableEquipment();

    /**
     * Schedules calibration for equipment.
     *
     * @param id                  the equipment ID
     * @param nextCalibrationDate the date for next calibration
     * @return the updated equipment
     */
    LabEquipmentResponseDto scheduleCalibration(Long id, LocalDate nextCalibrationDate);

    /**
     * Marks equipment as under maintenance.
     *
     * @param id the equipment ID
     * @return the updated equipment
     */
    LabEquipmentResponseDto markUnderMaintenance(Long id);

    /**
     * Marks equipment as operational (after maintenance/calibration).
     *
     * @param id the equipment ID
     * @return the updated equipment
     */
    LabEquipmentResponseDto markOperational(Long id);

    /**
     * Marks equipment as broken.
     *
     * @param id the equipment ID
     * @return the updated equipment
     */
    LabEquipmentResponseDto markBroken(Long id);

    /**
     * Decommissions equipment (retires from service).
     *
     * @param id the equipment ID
     * @return the updated equipment
     */
    LabEquipmentResponseDto decommission(Long id);

    /**
     * Updates an existing equipment.
     *
     * @param id  the equipment ID
     * @param dto the update data
     * @return the updated equipment
     */
    LabEquipmentResponseDto updateEquipment(Long id, LabEquipmentUpdateDto dto);

    /**
     * Soft-deletes lab equipment.
     *
     * @param id the equipment ID
     */
    void deleteEquipment(Long id);

    /**
     * Checks if a serial number already exists.
     *
     * @param serialNumber the serial number to check
     * @return true if exists
     */
    boolean serialNumberExists(String serialNumber);
}
