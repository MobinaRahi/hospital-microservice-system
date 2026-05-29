package hospital.coreservice.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a hospital department.
 * <p>
 * This class represents various departments within the hospital such as
 * Cardiology, Internal Medicine, Emergency, ICU, etc.
 * Each department has a head doctor, head nurse, and lists of doctors,
 * nurses, and rooms assigned to it.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Entity(name = "departmentEntity")
@Table(name = "departments",
        indexes = {
                @Index(name = "idx_department_code", columnList = "department_code"),
                @Index(name = "idx_department_name", columnList = "department_name"),
                @Index(name = "idx_department_is_active", columnList = "is_active")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Department {

    // ========== Primary Key ==========
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ========== Basic Information ==========
    /**
     * Full name of the department.
     * <p>Examples: "Cardiology", "Internal Medicine", "Emergency Department"</p>
     */
    @Column(name = "department_name", length = 50, nullable = false)
    private String departmentName;

    /**
     * Unique code identifier for the department.
     * <p>Examples: "CARD", "IMED", "EMER"</p>
     */
    @Column(name = "department_code", length = 50, nullable = false, unique = true)
    private String departmentCode;

    /**
     * Detailed description of the department's services and responsibilities.
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * Physical location of the department within the hospital.
     * <p>Example: "3rd Floor, East Wing"</p>
     */
    @Column(name = "location", length = 100, nullable = false)
    private String location;

    // ========== Department Leadership ==========
    /**
     * Doctor who serves as the head of this department.
     * <p>One-to-one relationship: One department has one head doctor.</p>
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_doctor_id")
    private Doctor headDoctor;

    /**
     * Nurse who serves as the head nurse of this department.
     * <p>One-to-one relationship: One department has one head nurse.</p>
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_nurse_id")
    private Nurse headNurse;

    // ========== Relationships ==========
    /**
     * List of doctors working in this department.
     * <p>One-to-many relationship: One department can have many doctors.</p>
     */
    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Doctor> doctorList = new ArrayList<>();

    /**
     * List of nurses working in this department.
     * <p>One-to-many relationship: One department can have many nurses.</p>
     */
    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Nurse> nurseList = new ArrayList<>();

    /**
     * List of rooms belonging to this department.
     * <p>One-to-many relationship: One department can have many rooms.</p>
     */
    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Room> roomList = new ArrayList<>();

    // ========== Contact Information ==========
    /**
     * Department's contact phone number (11 digits).
     */
    @Column(name = "phone_number", nullable = false, length = 11)
    private String phoneNumber;

    // ========== Status ==========
    /**
     * Active/Inactive status of the department.
     * <p>- true: Department is active and operational</p>
     * <p>- false: Department is inactive (temporarily closed or merged)</p>
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    // ========== Helper Methods ==========
    /**
     * Adds a doctor to this department.
     * <p>Maintains bidirectional relationship by setting the doctor's department.</p>
     *
     * @param doctor the doctor to add
     */
    public void addDoctor(Doctor doctor) {
        this.doctorList.add(doctor);
        doctor.setDepartment(this);
    }

    /**
     * Removes a doctor from this department.
     * <p>Maintains bidirectional relationship by clearing the doctor's department.</p>
     *
     * @param doctor the doctor to remove
     */
    public void removeDoctor(Doctor doctor) {
        this.doctorList.remove(doctor);
        doctor.setDepartment(null);
    }

    /**
     * Adds a nurse to this department.
     *
     * @param nurse the nurse to add
     */
    public void addNurse(Nurse nurse) {
        this.nurseList.add(nurse);
    }

    /**
     * Removes a nurse from this department.
     *
     * @param nurse the nurse to remove
     */
    public void removeNurse(Nurse nurse) {
        this.nurseList.remove(nurse);
    }

    /**
     * Adds a room to this department.
     * <p>Maintains bidirectional relationship by setting the room's department.</p>
     *
     * @param room the room to add
     */
    public void addRoom(Room room) {
        this.roomList.add(room);
        room.setDepartment(this);
    }

    /**
     * Removes a room from this department.
     * <p>Maintains bidirectional relationship by clearing the room's department.</p>
     *
     * @param room the room to remove
     */
    public void removeRoom(Room room) {
        this.roomList.remove(room);
        room.setDepartment(null);
    }
}

