package hospital.coreservice.dto.department;

import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.dto.nurse.NurseResponseDto;
import hospital.coreservice.dto.room.RoomResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for sending department data back to the client.
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
public class DepartmentResponseDto {

    // ========== Primary Key ==========

    /**
     * Unique identifier of the department.
     */
    private Long id;

    // ========== Basic Information ==========

    /**
     * Full name of the department.
     * <p>Example: "Cardiology", "Internal Medicine"</p>
     */
    private String departmentName;

    /**
     * Unique code identifier for the department.
     * <p>Example: "CARD", "IMED", "EMER"</p>
     */
    private String departmentCode;

    /**
     * Detailed description of the department's services.
     */
    private String description;

    /**
     * Physical location of the department within the hospital.
     * <p>Example: "3rd Floor, East Wing"</p>
     */
    private String location;

    // ========== Department Leadership (Full objects for display) ==========

    /**
     * Doctor who serves as the head of this department.
     * Contains full doctor information (id, name, specialty, etc.).
     * <p>Can be null if no head doctor is assigned.</p>
     */
    private DoctorResponseDto headDoctor;

    /**
     * Nurse who serves as the head nurse of this department.
     * Contains full nurse information (id, name, position, etc.).
     * <p>Can be null if no head nurse is assigned.</p>
     */
    private NurseResponseDto headNurse;

    // ========== Relationships (Full objects for display) ==========

    /**
     * List of doctors working in this department.
     * Each doctor contains full information (id, name, specialty, etc.).
     * <p>Empty list if no doctors are assigned.</p>
     */
    private List<DoctorResponseDto> doctors = new ArrayList<>();

    /**
     * List of nurses working in this department.
     * Each nurse contains full information (id, name, position, etc.).
     * <p>Empty list if no nurses are assigned.</p>
     */
    private List<NurseResponseDto> nurses = new ArrayList<>();

    /**
     * List of rooms belonging to this department.
     * Each room contains full information (id, roomNumber, capacity, etc.).
     * <p>Empty list if no rooms belong to this department.</p>
     */
    private List<RoomResponseDto> rooms = new ArrayList<>();

    // ========== Contact Information ==========

    /**
     * Department's contact phone number (11 digits).
     * <p>Example: "02122345678"</p>
     */
    private String phoneNumber;

    // ========== Status ==========

    /**
     * Active/Inactive status of the department.
     * <p>- true: Department is active and operational</p>
     * <p>- false: Department is inactive</p>
     */
    private Boolean isActive;

    // ========== Audit Fields ==========

    /**
     * Timestamp when this department was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when this department was last updated.
     */
    private LocalDateTime updatedAt;
}