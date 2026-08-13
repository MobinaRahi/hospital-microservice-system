package hospital.labservice.model;

import hospital.labservice.model.enums.EquipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Represents laboratory equipment used for performing tests.
 * Tracks equipment status, calibration dates, and maintenance needs.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each equipment has a unique serialNumber</li>
 *   <li>Equipment must be calibrated regularly</li>
 *   <li>Status tracks availability (OPERATIONAL, MAINTENANCE, etc.)</li>
 *   <li>Next calibration date triggers maintenance alerts</li>
 * </ul>
 *
 * <p><strong>Equipment Status:</strong></p>
 * <ul>
 *   <li>OPERATIONAL - Equipment is working and available</li>
 *   <li>MAINTENANCE - Equipment is undergoing maintenance</li>
 *   <li>CALIBRATION - Equipment is being calibrated</li>
 *   <li>BROKEN - Equipment is broken and needs repair</li>
 *   <li>DECOMMISSIONED - Equipment is retired from service</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "lab_equipment",
        indexes = {
                @Index(name = "idx_lab_equip_serial", columnList = "serialNumber", unique = true),
                @Index(name = "idx_lab_equip_status", columnList = "status"),
                @Index(name = "idx_lab_equip_calibration", columnList = "nextCalibrationDate")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LabEquipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Display name of the equipment.
     * Example: "Coulter Counter", "AutoAnalyzer 5000"
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Equipment model number.
     * Example: "Model XYZ-100"
     */
    @Column(length = 100)
    private String model;

    /**
     * Equipment manufacturer.
     * Example: "Roche", "Siemens", "Abbott"
     */
    @Column(length = 100)
    private String manufacturer;

    /**
     * Unique serial number for the equipment.
     */
    @Column(name = "serial_number", nullable = false, unique = true, length = 100)
    private String serialNumber;

    /**
     * Current status of the equipment.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EquipmentStatus status = EquipmentStatus.OPERATIONAL;

    /**
     * Date of the last calibration.
     */
    @Column(name = "last_calibration_date")
    private LocalDate lastCalibrationDate;

    /**
     * Date when the next calibration is due.
     * Used for maintenance scheduling.
     */
    @Column(name = "next_calibration_date")
    private LocalDate nextCalibrationDate;

    /**
     * Physical location of the equipment in the lab.
     * Example: "Room 101, Station 3"
     */
    @Column(length = 100)
    private String location;

    /**
     * Additional notes about the equipment.
     * Example: "Requires special ventilation", "Sensitive to temperature"
     */
    @Column(length = 500)
    private String notes;

    // ════════════════════════════════════════════════════════════════════
    // Business Logic Methods
    // ════════════════════════════════════════════════════════════════════

    /**
     * Checks if the equipment is operational and available for use.
     *
     * @return true if status is OPERATIONAL
     */
    public boolean isOperational() {
        return this.status == EquipmentStatus.OPERATIONAL;
    }

    /**
     * Checks if the equipment needs calibration.
     *
     * @return true if nextCalibrationDate is in the past
     */
    public boolean needsCalibration() {
        if (this.nextCalibrationDate == null) {
            return false;
        }
        return this.nextCalibrationDate.isBefore(LocalDate.now());
    }

    /**
     * Checks if the equipment is available for use.
     * Available means OPERATIONAL and calibration is up to date.
     *
     * @return true if equipment is operational and calibration is current
     */
    public boolean isAvailable() {
        return this.isOperational() && !this.needsCalibration();
    }

    /**
     * Schedules the next calibration date.
     *
     * @param nextCalibrationDate the date for next calibration
     */
    public void scheduleCalibration(LocalDate nextCalibrationDate) {
        this.lastCalibrationDate = LocalDate.now();
        this.nextCalibrationDate = nextCalibrationDate;
    }

    /**
     * Marks the equipment as under maintenance.
     */
    public void markUnderMaintenance() {
        this.status = EquipmentStatus.MAINTENANCE;
    }

    /**
     * Marks the equipment as operational after maintenance.
     */
    public void markOperational() {
        this.status = EquipmentStatus.OPERATIONAL;
    }

    /**
     * Marks the equipment as broken.
     */
    public void markBroken() {
        this.status = EquipmentStatus.BROKEN;
    }

    /**
     * Decommissions the equipment (retires from service).
     */
    public void decommission() {
        this.status = EquipmentStatus.DECOMMISSIONED;
    }
}
