package hospital.coreservice.dto.doctor;


import hospital.coreservice.dto.department.DepartmentSlimResponseDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleResponseDto;
import hospital.coreservice.model.enums.Speciality;
import hospital.coreservice.model.enums.SubSpeciality;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for sending doctor data back to the client.
 * <p>
 * This is the response format for GET, POST, and PUT requests.
 * Unlike the Entity, this DTO contains only the information that should
 * be exposed to the client, with relationships represented as nested DTOs.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class DoctorResponseDto {

    // ========== Primary Key ==========

    /**
     * Unique identifier of the doctor.
     */
    private Long id;

    /**
     * User ID reference from Auth Service.
     * <p>Links this doctor to a system user account for login and permissions.</p>
     */
    private Long userId;

    // ========== Personal Information ==========

    /**
     * Doctor's first name.
     */
    private String firstName;

    /**
     * Doctor's last name.
     */
    private String lastName;

    /**
     * Doctor's full name (firstName + lastName).
     * <p>Example: "احمد احمدی"</p>
     */
    private String fullName;

    /**
     * Doctor's mobile phone number.
     * <p>Example: "09123456789"</p>
     */
    private String phoneNumber;

    // ========== Professional Information ==========

    /**
     * Doctor's primary medical specialty.
     * <p>Example: CARDIOLOGY, INTERNAL_MEDICINE, PEDIATRICS</p>
     */
    private Speciality speciality;

    /**
     * List of sub-specialties (fellowships or additional expertise).
     * <p>Example: INTERVENTIONAL_CARDIOLOGY, ECHOCARDIOGRAPHY</p>
     */
    private List<SubSpeciality> subSpecialities = new ArrayList<>();

    /**
     * Medical council license number (unique identifier for certified doctors).
     */
    private String licenseNumber;

    /**
     * Number of years of professional experience.
     */
    private Integer yearsOfExperience;

    /**
     * Consultation fee in Rials (Iranian currency).
     * <p>Example: 500000 means 500,000 Rials</p>
     */
    private BigDecimal consultationFee;

    // ========== Business Rules ==========

    /**
     * Maximum number of appointments this doctor can have per day.
     */
    private Integer maxAppointmentsPerDay;

    /**
     * Default duration of each appointment slot in minutes.
     * <p>Typically 15-20 minutes for general practice, 30-45 for specialists.</p>
     */
    private Integer defaultSlotDuration;

    // ========== Relationships ==========

    /**
     * Department this doctor belongs to.
     * <p>Contains full department information (id, name, location, etc.).</p>
     * <p>Can be null if not assigned to any department.</p>
     */
    private DepartmentSlimResponseDto department;


    /**
     * List of work schedules for this doctor.
     * <p>Each schedule defines working hours for a specific day of the week.</p>
     * <p>Empty list if no schedules are defined.</p>
     */
    private List<DoctorScheduleResponseDto> schedules = new ArrayList<>();

    // ========== Status ==========

    /**
     * Active/Inactive status of the doctor.
     * <p>- true: Currently employed and active in the system</p>
     * <p>- false: On leave, retired, or no longer employed</p>
     */
    private Boolean isActive;

    // ========== Audit Fields ==========

    /**
     * Timestamp when this doctor was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when this doctor was last updated.
     */
    private LocalDateTime updatedAt;
}
