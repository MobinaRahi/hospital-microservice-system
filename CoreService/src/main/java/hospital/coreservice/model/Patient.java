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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "patientEntity")
@Table(name = "patients",
        indexes = {
                @Index(name = "idx_patient_status", columnList = "status")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Patient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "national_id", length = 10, nullable = false, unique = true)
    private String nationalId;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "gender", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "phone_number", length = 11, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "address", length = 500, nullable = false)
    private String address;

    @Column(name = "blood_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Column(name = "insurance_id")
    private Long insuranceId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PatientStatus status;

    @Column(name = "allergies", length = 1000)
    private String allergies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_room_id")
    private Room currentRoom;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
}
