package hospital.coreservice.dto.room;

import hospital.coreservice.dto.department.DepartmentResponseDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for sending room data back to the client.
 * <p>
 * This is the response format for GET, POST, and PUT requests.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class RoomResponseDto {

    // ========== Primary Key ==========

    /**
     * Unique identifier of the room.
     */
    private Long id;

    // ========== Basic Information ==========

    /**
     * Unique room or bed number.
     * <p>Example: "A-101", "B-202", "ICU-01"</p>
     */
    private String roomNumber;

    /**
     * Department this room belongs to.
     * <p>Contains full department information (id, name, location, etc.).</p>
     * <p>Null if not assigned to any department.</p>
     */
    private DepartmentResponseDto department;

    /**
     * Maximum number of patients this room can accommodate.
     */
    private Integer capacity;

    // ========== Occupancy Information ==========

    /**
     * Indicates whether the room is currently occupied.
     * <p>- true: At least one patient is assigned</p>
     * <p>- false: Room is empty and available</p>
     */
    private Boolean isOccupied;

    /**
     * Current number of patients in this room.
     */
    private Integer currentOccupancy;

    /**
     * Remaining available capacity.
     */
    private Integer availableCapacity;

    /**
     * List of patients currently assigned to this room.
     * <p>Empty list if room is empty.</p>
     */
    private List<PatientResponseDto> currentPatients = new ArrayList<>();

    /**
     * Room features and amenities.
     * <p>Example: "Oxygen, Monitor, TV, Private Bathroom"</p>
     */
    private String features;
}