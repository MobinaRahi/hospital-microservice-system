package hospital.coreservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a room or bed in the hospital.
 * <p>
 * This class represents physical rooms (examination rooms, wards) or individual beds
 * that can be assigned to patients. Each room belongs to a department and can
 * accommodate multiple patients (in case of shared wards) or a single patient.
 * </p>
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Entity(name = "roomEntity")
@Table(name = "rooms",
        indexes = {
                @Index(name = "idx_room_number", columnList = "room_number"),
                @Index(name = "idx_room_department", columnList = "department_id"),
                @Index(name = "idx_room_is_occupied", columnList = "is_occupied")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Room {

    // ========== Primary Key ==========
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ========== Basic Information ==========
    /**
     * Unique room or bed number.
     * <p>Example format: "A-101", "B-202", "ICU-01"</p>
     */
    @Column(name = "room_number", nullable = false, unique = true, length = 10)
    private String roomNumber;

    /**
     * Department this room belongs to (e.g., Cardiology, ICU, Emergency).
     * <p>Many rooms can belong to one department.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    /**
     * Maximum number of patients this room can accommodate.
     * <p>- For single-bed rooms: 1</p>
     * <p>- For shared wards: 2, 4, 6, etc.</p>
     */
    @Column(name = "capacity", nullable = false)
    private int capacity;

    // ========== Status ==========
    /**
     * Indicates whether the room is currently occupied.
     * <p>- true: At least one patient is assigned to this room</p>
     * <p>- false: Room is empty and available</p>
     */
    @Column(name = "is_occupied", nullable = false)
    private boolean isOccupied;

    /**
     * List of patients currently assigned to this room.
     * <p>For single-bed rooms, this list contains at most one patient.</p>
     * <p>For shared wards, this can contain multiple patients.</p>
     */
    @OneToMany(mappedBy = "currentRoom", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Patient> currentPatientList = new ArrayList<>();

    /**
     * Room features and amenities.
     * <p>Example: "Oxygen, Monitor, TV, Private Bathroom"</p>
     */
    @Column(name = "features", length = 255)
    private String features;

    // ========== Helper Methods ==========
    /**
     * Assigns a patient to this room.
     * <p>This method:
     * <ul>
     *   <li>Adds the patient to the current patient list</li>
     *   <li>Sets the bidirectional relationship (patient.currentRoom = this)</li>
     *   <li>Updates the occupied status to true</li>
     * </ul>
     * </p>
     *
     * @param patient the patient to assign to this room
     */
    public void addPatient(Patient patient) {
        this.currentPatientList.add(patient);
        patient.setCurrentRoom(this);
        this.isOccupied = true;
    }

    /**
     * Removes a patient from this room.
     * <p>This method:
     * <ul>
     *   <li>Removes the patient from the current patient list</li>
     *   <li>Clears the bidirectional relationship (patient.currentRoom = null)</li>
     *   <li>Updates the occupied status to false if no patients remain</li>
     * </ul>
     * </p>
     *
     * @param patient the patient to remove from this room
     */
    public void removePatient(Patient patient) {
        this.currentPatientList.remove(patient);
        patient.setCurrentRoom(null);
        this.isOccupied = false;
    }

    /**
     * Checks if the room has available capacity for more patients.
     *
     * @return true if current patient count is less than capacity, false otherwise
     */
    public boolean hasAvailableCapacity() {
        return currentPatientList.size() < capacity;
    }

    /**
     * Gets the number of currently occupied beds in this room.
     *
     * @return current patient count
     */
    public int getCurrentOccupancy() {
        return currentPatientList.size();
    }
}
