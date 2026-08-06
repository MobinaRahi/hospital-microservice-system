package hospital.inventoryservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a supplier (vendor) of drugs and medical equipment.
 *
 * @author MobinaRahi
 */
@Entity
@Table(name = "suppliers",
        indexes = {
                @Index(name = "idx_supplier_name", columnList = "name"),
                @Index(name = "idx_supplier_email", columnList = "email")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Supplier extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Company name of the supplier.
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Primary phone number.
     */
    @Column(length = 20)
    private String phone;

    /**
     * Secondary/mobile phone number.
     */
    @Column(length = 20)
    private String mobile;

    /**
     * Email address.
     */
    @Column(length = 200)
    private String email;

    /**
     * Physical address.
     */
    @Column(length = 500)
    private String address;

    /**
     * Name of the primary contact person.
     */
    @Column(name = "contact_person", length = 200)
    private String contactPerson;

    /**
     * Payment terms (e.g., "Net 30", "Cash on delivery").
     */
    @Column(name = "payment_terms", length = 200)
    private String paymentTerms;

    /**
     * Whether this supplier is currently active.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
