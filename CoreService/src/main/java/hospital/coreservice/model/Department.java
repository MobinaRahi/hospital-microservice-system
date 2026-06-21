package hospital.coreservice.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "department_name", length = 50, nullable = false)
    private String departmentName;

    @Column(name = "department_code", length = 50, nullable = false, unique = true)
    private String departmentCode;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "location", length = 100, nullable = false)
    private String location;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_doctor_id")
    private Doctor headDoctor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_nurse_id")
    private Nurse headNurse;

    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Doctor> doctorList = new ArrayList<>();

    @ManyToMany(mappedBy = "departmentList", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Nurse> nurseList = new ArrayList<>();

    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Room> roomList = new ArrayList<>();

    @Column(name = "phone_number", nullable = false, length = 11)
    private String phoneNumber;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    public void addDoctor(Doctor doctor) {
        this.doctorList.add(doctor);
        doctor.setDepartment(this);
    }

    public void removeDoctor(Doctor doctor) {
        this.doctorList.remove(doctor);
        doctor.setDepartment(null);
    }

    public void addNurse(Nurse nurse) {
        this.nurseList.add(nurse);
        nurse.getDepartmentList().add(this);
    }

    public void removeNurse(Nurse nurse) {
        this.nurseList.remove(nurse);
        nurse.getDepartmentList().remove(this);
    }

    public void addRoom(Room room) {
        this.roomList.add(room);
        room.setDepartment(this);
    }

    public void removeRoom(Room room) {
        this.roomList.remove(room);
        room.setDepartment(null);
    }
}

