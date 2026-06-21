package hospital.coreservice.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true, length = 10)
    private String roomNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "features", length = 255)
    private String features;

    @Column(name = "is_occupied", nullable = false)
    private boolean isOccupied;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @OneToMany(mappedBy = "currentRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Patient> currentPatientList = new ArrayList<>();

    public void addPatient(Patient patient) {
        this.currentPatientList.add(patient);
        patient.setCurrentRoom(this);
        this.isOccupied = true;
    }

    public void removePatient(Patient patient) {
        this.currentPatientList.remove(patient);
        patient.setCurrentRoom(null);
        this.isOccupied = false;
    }

    public boolean hasAvailableCapacity() {
        return currentPatientList.size() < capacity;
    }

    public int getCurrentOccupancy() {
        return currentPatientList.size();
    }
}