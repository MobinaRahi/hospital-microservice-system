package hospital.coreservice.model;

import hospital.coreservice.model.enums.Speciality;
import hospital.coreservice.model.enums.SubSpeciality;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "doctorEntity")
@Table(name = "doctors",
        indexes = {
                @Index(name = "idx_doctor_user", columnList = "user_id"),
                @Index(name = "idx_doctor_speciality", columnList = "speciality"),
                @Index(name = "idx_doctor_department", columnList = "department_id"),
                @Index(name = "idx_doctor_is_active", columnList = "is_active")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
/**
 * Represents a doctor in the hospital.
 * Linked to a User account via userId.
 * Has specialty, department, and schedule.
 *
 * @author Mobina
 */
public class Doctor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "speciality", nullable = false)
    @Enumerated(EnumType.STRING)
    private Speciality speciality;

    @ElementCollection(targetClass = SubSpeciality.class)
    @CollectionTable(name = "doctor_sub_specialities", joinColumns = @JoinColumn(name = "doctor_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<SubSpeciality> subSpecialities = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DoctorSchedule> doctorSchedules = new ArrayList<>();

    @Column(name = "license_number", length = 50, nullable = false, unique = true)
    private String licenseNumber;

    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience;

    @Column(name = "consultation_fee", nullable = false)
    private Long consultationFee;

    @Column(name = "phone_number", length = 11, nullable = false)
    private String phoneNumber;

    @Column(name = "max_appointments_per_day", nullable = false)
    private Integer maxAppointmentsPerDay;

    @Column(name = "default_slot_duration", nullable = false)
    private Integer defaultSlotDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    public void addSubSpeciality(SubSpeciality subSpeciality) {
        this.subSpecialities.add(subSpeciality);
    }

    public void removeSubSpeciality(SubSpeciality subSpeciality) {
        this.subSpecialities.remove(subSpeciality);
    }

    public void addDoctorSchedule(DoctorSchedule doctorSchedule) {
        this.doctorSchedules.add(doctorSchedule);
        doctorSchedule.setDoctor(this);
    }

    public void removeDoctorSchedule(DoctorSchedule doctorSchedule) {
        this.doctorSchedules.remove(doctorSchedule);
        doctorSchedule.setDoctor(null);
    }

}

