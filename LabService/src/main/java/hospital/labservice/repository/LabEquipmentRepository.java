package hospital.labservice.repository;

import hospital.labservice.model.LabEquipment;
import hospital.labservice.model.enums.EquipmentStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for LabEquipment entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findBySerialNumber - Find by unique serial number</li>
 *   <li>findByStatus - Equipment by operational status</li>
 *   <li>findNeedsCalibration - Equipment overdue for calibration</li>
 *   <li>findOperationalEquipment - Available equipment</li>
 *   <li>findByLocation - Equipment by physical location</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface LabEquipmentRepository extends BaseEntityRepository<LabEquipment, Long> {

    /**
     * Finds equipment by its unique serial number.
     *
     * @param serialNumber the serial number
     * @return equipment if found
     */
    Optional<LabEquipment> findBySerialNumber(String serialNumber);

    /**
     * Finds equipment by operational status.
     *
     * @param status the equipment status (OPERATIONAL, MAINTENANCE, etc.)
     * @return list of equipment with the status
     */
    List<LabEquipment> findByStatus(EquipmentStatus status);

    /**
     * Finds equipment that needs calibration (next calibration date has passed).
     * Used for maintenance scheduling and alerts.
     *
     * @param today current date
     * @return list of equipment overdue for calibration
     */
    @Query("SELECT e FROM LabEquipment e WHERE e.nextCalibrationDate < :today AND e.status = 'OPERATIONAL' AND e.deleted = false")
    List<LabEquipment> findNeedsCalibration(@Param("today") LocalDate today);

    /**
     * Finds equipment by location.
     *
     * @param location the physical location in the lab
     * @return list of equipment at the location
     */
    List<LabEquipment> findByLocation(String location);

    /**
     * Checks if a serial number already exists.
     *
     * @param serialNumber the serial number to check
     * @return true if exists
     */
    boolean existsBySerialNumber(String serialNumber);

    /**
     * Finds equipment by name pattern (case-insensitive).
     *
     * @param name the name pattern
     * @return list of matching equipment
     */
    List<LabEquipment> findByNameContainingIgnoreCase(String name);

    /**
     * Counts equipment by status.
     *
     * @param status the equipment status
     * @return number of equipment with the status
     */
    long countByStatus(EquipmentStatus status);

    /**
     * Finds equipment whose calibration is due within a date range.
     * Used for proactive maintenance planning.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of equipment with calibration due in the range
     */
    List<LabEquipment> findByNextCalibrationDateBetween(LocalDate startDate, LocalDate endDate);
}
