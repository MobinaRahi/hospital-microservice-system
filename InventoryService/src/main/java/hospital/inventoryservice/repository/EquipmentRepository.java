package hospital.inventoryservice.repository;

import hospital.inventoryservice.repository.BaseEntityRepository;

import hospital.inventoryservice.model.Equipment;
import hospital.inventoryservice.model.enums.EquipmentStatus;
import hospital.inventoryservice.model.enums.EquipmentType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Equipment entity.
 * 
 * <p><strong>Query Methods:</strong></p>
 * <ul>
 *   <li>{@code findByType(EquipmentType)} - Equipment by type</li>
 *   <li>{@code findByStatus(EquipmentStatus)} - Equipment by status</li>
 *   <li>{@code findBySerialNumber(String)} - Equipment by unique serial number</li>
 *   <li>{@code findByCurrentLocationContainingIgnoreCase(String)} - Search by location</li>
 *   <li>{@code findByWarrantyExpiryBefore(LocalDate)} - Expired warranty equipment</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface EquipmentRepository extends BaseEntityRepository<Equipment, Long> {
    
    /**
     * Finds equipment by type.
     *
     * @param type the equipment type (BED, VENTILATOR, etc.)
     * @return list of equipment of the type
     */
    List<Equipment> findByType(EquipmentType type);
    
    /**
     * Finds equipment by status.
     *
     * @param status the equipment status (AVAILABLE, IN_USE, etc.)
     * @return list of equipment with the status
     */
    List<Equipment> findByStatus(EquipmentStatus status);
    
    /**
     * Finds equipment by unique serial number.
     *
     * @param serialNumber the serial number
     * @return equipment if found
     */
    Optional<Equipment> findBySerialNumber(String serialNumber);
    
    /**
     * Finds equipment by current location (case-insensitive, partial match).
     *
     * @param location the location to search
     * @return list of equipment in the location
     */
    List<Equipment> findByCurrentLocationContainingIgnoreCase(String location);
    
    /**
     * Finds equipment with expired warranty.
     *
     * @param date the date to check (usually today)
     * @return list of equipment with expired warranty
     */
    List<Equipment> findByWarrantyExpiryBefore(java.time.LocalDate date);
    
    /**
     * Finds equipment by manufacturer.
     *
     * @param manufacturer the manufacturer name
     * @return list of equipment from the manufacturer
     */
    List<Equipment> findByManufacturerContainingIgnoreCase(String manufacturer);
    
    /**
     * Finds active equipment only.
     *
     * @return list of active equipment
     */
    List<Equipment> findByIsActiveTrue();
    
    /**
     * Checks if a serial number already exists.
     *
     * @param serialNumber the serial number to check
     * @return true if exists
     */
    boolean existsBySerialNumber(String serialNumber);
}
