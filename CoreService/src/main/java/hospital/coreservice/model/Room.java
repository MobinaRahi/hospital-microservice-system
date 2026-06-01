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
     * Unique room or bed number (e.g., "A-101", "B-202")
     */
    @Column(name = "room_number", nullable = false, unique = true, length = 10)
    private String roomNumber;

    /**
     * Department this room belongs to (many rooms -> one department)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    /**
     * Maximum number of patients this room can accommodate
     */
    @Column(name = "capacity", nullable = false)
    private int capacity;

    /**
     * Room features and amenities (e.g., "Oxygen, Monitor, TV")
     */
    @Column(name = "features", length = 255)
    private String features;

    // ========== Status ==========
    /**
     * Indicates whether the room is currently occupied
     */
    @Column(name = "is_occupied", nullable = false)
    private boolean isOccupied;

    /**
     * Active/Inactive status (soft delete)
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    // ========== Relationships ==========
    /**
     * List of patients currently assigned to this room (bidirectional)
     */
    @OneToMany(mappedBy = "currentRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Patient> currentPatientList = new ArrayList<>();

    // ========== Helper Methods ==========
    /**
     * Assigns a patient to this room, updates both sides
     */
    public void addPatient(Patient patient) {
        this.currentPatientList.add(patient);
        patient.setCurrentRoom(this);
        this.isOccupied = true;
    }

    /**
     * Removes a patient from this room, updates both sides
     */
    public void removePatient(Patient patient) {
        this.currentPatientList.remove(patient);
        patient.setCurrentRoom(null);
        this.isOccupied = false;
    }

    /**
     * Checks if there is available capacity
     */
    public boolean hasAvailableCapacity() {
        return currentPatientList.size() < capacity;
    }

    /**
     * Returns current number of patients in the room
     */
    public int getCurrentOccupancy() {
        return currentPatientList.size();
    }
}