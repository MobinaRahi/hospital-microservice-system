package hospital.adminservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a hospital or medical facility in the system.
 * Each tenant (organization) can have one or more hospitals.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each hospital must have a unique code</li>
 *   <li>Hospital code is used for identification across the system</li>
 *   <li>All contact information is optional except name and code</li>
 * </ul>
 *
 * <p><strong>Multi-Tenancy:</strong></p>
 * <ul>
 *   <li>Inherits tenantId from BaseEntity for data isolation</li>
 *   <li>Each hospital belongs to a specific tenant</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "hospitals",
        indexes = {
                @Index(name = "idx_hospital_code", columnList = "code", unique = true)
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Hospital extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the hospital.
     * Example: "Tehran Heart Center", "Shariati Hospital"
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Unique code identifier for the hospital.
     * Used for system-wide identification.
     * Example: "THC-001", "SHR-002"
     */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /**
     * Official registration number from health authorities.
     */
    @Column(name = "registration_number", length = 100)
    private String registrationNumber;

    /**
     * Physical address of the hospital.
     */
    @Column(length = 500)
    private String address;

    /**
     * Primary contact phone number.
     */
    @Column(length = 50)
    private String phone;

    /**
     * Fax number (optional).
     */
    @Column(length = 50)
    private String fax;

    /**
     * Official email address.
     */
    @Column(length = 200)
    private String email;

    /**
     * Website URL.
     */
    @Column(length = 200)
    private String website;

    /**
     * URL to hospital logo image.
     */
    @Column(length = 500)
    private String logo;

    /**
     * Tax identification number.
     */
    @Column(name = "tax_id", length = 50)
    private String taxId;

    /**
     * Bank account number for financial transactions.
     */
    @Column(name = "bank_account", length = 50)
    private String bankAccount;
}
