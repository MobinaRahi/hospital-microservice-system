package hospital.inventoryservice.model;

import hospital.inventoryservice.model.enums.EquipmentStatus;
import hospital.inventoryservice.model.enums.EquipmentType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Represents a piece of medical equipment in the hospital.
 *
 * <p><strong>Equipment Types:</strong></p>
 * <ul>
 *   <li>BED - Hospital bed</li>
 *   <li>VENTILATOR - Mechanical ventilator</li>
 *   <li>MONITOR - Patient monitor</li>
 *   <li>INFUSION_PUMP - IV infusion pump</li>
 *   <li>WHEELCHAIR, CRUTCHES - Mobility aids</li>
 *   <li>OXYGEN_TANK - Oxygen cylinder</li>
 *   <li>DEFIBRILLATOR - Emergency defibrillator</li>
 *   <li>ULTRASOUND, XRAY - Imaging equipment</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "equipment",
        indexes = {
                @Index(name = "idx_equipment_type", columnList = "type"),
                @Index(name = "idx_equipment_status", columnList = "status"),
                @Index(name = "idx_equipment_serial", columnList = "serial_number", unique = true)
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Equipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the equipment (e.g., "Bed #101", "Ventilator A").
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Type of equipment.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentType type;

    /**
     * Unique serial number from the manufacturer.
     */
    @Column(name = "serial_number", unique = true, length = 100)
    private String serialNumber;

    /**
     * Model number.
     */
    @Column(length = 100)
    private String model;

    /**
     * Manufacturer name.
     */
    @Column(length = 200)
    private String manufacturer;

    /**
     * Date of purchase.
     */
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    /**
     * Warranty expiry date.
     */
    @Column(name = "warranty_expiry")
    private LocalDate warrantyExpiry;

    /**
     * Current status of the equipment.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EquipmentStatus status = EquipmentStatus.AVAILABLE;

    /**
     * Current physical location (e.g., "ICU Room 3", "Warehouse B").
     */
    @Column(name = "current_location", length = 200)
    private String currentLocation;

    /**
     * Whether this equipment is currently active in use.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Checks whether warranty has expired.
     */
    public boolean isWarrantyExpired() {
        return warrantyExpiry != null && warrantyExpiry.isBefore(LocalDate.now());
    }
}
