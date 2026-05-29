package hospital.coreservice.model;

import hospital.coreservice.model.enums.BloodType;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.model.enums.PatientStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a patient in the hospital system.
 * <p>
 * This class stores all demographic and medical information about a patient,
 * including personal details, contact information, blood type, allergies,
 * insurance information, and current room assignment.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Entity(name = "patientEntity")
@Table(name = "patients",
        indexes = {
                @Index(name = "idx_patient_national_id", columnList = "national_id"),
                @Index(name = "idx_patient_phone", columnList = "phone_number"),
                @Index(name = "idx_patient_status", columnList = "status")
        })
@SQLRestriction("deleted = false")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Patient {

    // ========== Primary Key ==========
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ========== Personal Information ==========
    /**
     * National ID (10 digits, unique).
     * <p>Stored as String to preserve leading zeros (e.g., "0123456789")</p>
     */
    @Column(name = "national_id", length = 10, nullable = false, unique = true)
    private String nationalId;

    /**
     * Patient's first name.
     */
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    /**
     * Patient's last name.
     */
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    /**
     * Patient's gender (MALE, FEMALE, OTHER, UNKNOWN).
     */
    @Column(name = "gender", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    // ========== Contact Information ==========
    /**
     * Patient's mobile phone number (11 digits).
     * <p>Example: "09123456789"</p>
     */
    @Column(name = "phone_number", length = 11, nullable = false, unique = true)
    private String phoneNumber;

    /**
     * Patient's full residential address.
     */
    @Column(name = "address", length = 500, nullable = false)
    private String address;

    // ========== Medical Information ==========
    /**
     * Patient's blood type (A+, A-, B+, B-, AB+, AB-, O+, O-, UNKNOWN).
     */
    @Column(name = "blood_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    /**
     * Insurance ID reference (links to insurance service).
     * <p>Can be null if patient has no insurance.</p>
     */
    @Column(name = "insurance_id")
    private Long insuranceId;

    /**
     * Current patient status (ACTIVE, ARCHIVED, DECEASED, TRANSFERRED).
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PatientStatus status;

    /**
     * Known allergies (medication, food, environmental, etc.).
     * <p>Example: "Penicillin, Peanuts, Pollen"</p>
     */
    @Column(name = "allergies", length = 1000)
    private String allergies;

    // ========== Relationships ==========
    /**
     * Current room/bed where this patient is assigned.
     * <p>Many patients can be in one room (for shared wards), or null if not admitted.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_room_id")
    private Room currentRoom;

    // ========== Audit Fields ==========
    /**
     * Timestamp when this patient record was created.
     * <p>Automatically populated by Hibernate.</p>
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when this patient record was last updated.
     * <p>Automatically populated by Hibernate.</p>
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * ID of the user who created this patient record.
     */
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @Column(name = "deleted")
    private boolean deleted;

    /**
     * Patient's date of birth.
     * <p>Example: 1990-05-15</p>
     */
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
}
