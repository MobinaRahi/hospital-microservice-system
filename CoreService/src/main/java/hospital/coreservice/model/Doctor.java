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

/**
 * Entity representing a doctor in the hospital system.
 * <p>
 * This class stores professional information about doctors including their
 * specialty, sub-specialties, consultation fees, work schedule, and department assignment.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Entity(name = "doctorEntity")
@Table(name = "doctors",
        indexes = {
                @Index(name = "idx_doctor_user", columnList = "user_id"),
                @Index(name = "idx_doctor_license", columnList = "license_number"),
                @Index(name = "idx_doctor_speciality", columnList = "speciality"),
                @Index(name = "idx_doctor_department", columnList = "department_id"),
                @Index(name = "idx_doctor_is_active", columnList = "is_active")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Doctor {

    // ========== Primary Key ==========
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ========== Authentication Information ==========
    /**
     * User ID reference from Auth Service.
     * <p>Links this doctor to a system user account for login and permissions.</p>
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // ========== Personal Information ==========
    /**
     * Doctor's first name.
     */
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    /**
     * Doctor's last name.
     */
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    // ========== Professional Information ==========
    /**
     * Doctor's primary medical specialty (CARDIOLOGY, INTERNAL_MEDICINE, etc.).
     */
    @Column(name = "speciality", nullable = false)
    @Enumerated(EnumType.STRING)
    private Speciality speciality;

    /**
     * List of sub-specialties (fellowships or additional expertise).
     * <p>Example: INTERVENTIONAL_CARDIOLOGY, ECHOCARDIOGRAPHY</p>
     */
    @ElementCollection(targetClass = SubSpeciality.class)
    @CollectionTable(name = "doctor_sub_specialities", joinColumns = @JoinColumn(name = "doctor_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<SubSpeciality> subSpecialities = new ArrayList<>();

    /**
     * List of work schedules for this doctor.
     * <p>Each schedule defines working hours for a specific day of the week.</p>
     */
    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DoctorSchedule> doctorSchedules = new ArrayList<>();

    /**
     * Medical council license number (unique identifier for certified doctors).
     */
    @Column(name = "license_number", length = 50, nullable = false, unique = true)
    private String licenseNumber;

    /**
     * Number of years of professional experience.
     */
    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience;

    /**
     * Consultation fee in Rials (Iranian currency).
     * <p>Example: 500000 means 500,000 Rials</p>
     */
    @Column(name = "consultation_fee", nullable = false)
    private Long consultationFee;

    // ========== Contact Information ==========
    /**
     * Mobile phone number (11 digits).
     * <p>Example: "09123456789"</p>
     */
    @Column(name = "phone_number", length = 11, nullable = false)
    private String phoneNumber;

    // ========== Business Rules ==========
    /**
     * Maximum number of appointments this doctor can have per day.
     * <p>Prevents overbooking and ensures quality of care.</p>
     */
    @Column(name = "max_appointments_per_day", nullable = false)
    private Integer maxAppointmentsPerDay;

    /**
     * Default duration of each appointment slot in minutes.
     * <p>Typically 15-20 minutes for general practice, 30-45 for specialists.</p>
     */
    @Column(name = "default_slot_duration", nullable = false)
    private Integer defaultSlotDuration;

    // ========== Relationships ==========
    /**
     * Department this doctor is assigned to.
     * <p>Many doctors can belong to one department. Can be null if not yet assigned.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    // ========== Status ==========
    /**
     * Active/Inactive status of the doctor.
     * <p>- true: Currently employed and active in the system</p>
     * <p>- false: On leave, retired, or no longer employed</p>
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    // ========== Helper Methods ==========
    /**
     * Adds a sub-specialty to the doctor's list.
     *
     * @param subSpeciality the sub-specialty to add
     */
    public void addSubSpeciality(SubSpeciality subSpeciality) {
        this.subSpecialities.add(subSpeciality);
    }

    /**
     * Removes a sub-specialty from the doctor's list.
     *
     * @param subSpeciality the sub-specialty to remove
     */
    public void removeSubSpeciality(SubSpeciality subSpeciality) {
        this.subSpecialities.remove(subSpeciality);
    }

    /**
     * Adds a work schedule to the doctor's schedule list.
     * <p>This method maintains the bidirectional relationship.</p>
     *
     * @param doctorSchedule the schedule to add
     */
    public void addDoctorSchedule(DoctorSchedule doctorSchedule) {
        this.doctorSchedules.add(doctorSchedule);
        doctorSchedule.setDoctor(this);
    }

    /**
     * Removes a work schedule from the doctor's schedule list.
     * <p>This method maintains the bidirectional relationship.</p>
     *
     * @param doctorSchedule the schedule to remove
     */
    public void removeDoctorSchedule(DoctorSchedule doctorSchedule) {
        this.doctorSchedules.remove(doctorSchedule);
        doctorSchedule.setDoctor(null);
    }

}

