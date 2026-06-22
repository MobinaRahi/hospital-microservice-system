package hospital.coreservice.model;

import hospital.coreservice.model.enums.NursePosition;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "nurseEntity")
@Table(name = "nurses",
        indexes = {
                @Index(name = "idx_nurse_is_active", columnList = "is_active")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Nurse extends  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "national_id", length = 10, nullable = false, unique = true)
    private String nationalId;

    @Column(name = "phone_number", length = 11, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "nurse_code", length = 50, nullable = false, unique = true)
    private String nurseCode;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "nurse_department",
            joinColumns = @JoinColumn(name = "nurse_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id"),
            foreignKey = @ForeignKey(name = "fk_nurse_departments_nurse"),
            inverseForeignKey = @ForeignKey(name = "fk_nurse_departments_department"),
            indexes = {
                    @Index(name = "idx_nurse_dept_nurse", columnList = "nurse_id"),
                    @Index(name = "idx_nurse_dept_dept", columnList = "department_id")
            }
    )
    @Builder.Default
    private List<Department> departmentList = new ArrayList<>();

    @Column(name = "nurse_position", nullable = false)
    @Enumerated(EnumType.STRING)
    private NursePosition position;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "nurse_shifts",
            joinColumns = @JoinColumn(name = "nurse_id"),
            inverseJoinColumns = @JoinColumn(name = "shift_id"),
            foreignKey = @ForeignKey(name = "fk_nurse_shifts_nurse"),
            inverseForeignKey = @ForeignKey(name = "fk_nurse_shifts_shift"),
            indexes = {
                    @Index(name = "idx_nurse_shift_nurse", columnList = "nurse_id"),
                    @Index(name = "idx_nurse_shift_shift", columnList = "shift_id")
            }
    )
    @Builder.Default
    private List<Shift> shiftPreferenceList = new ArrayList<>();

    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    public void addDepartment(Department department) {
        departmentList.add(department);
    }

    public void removeDepartment(Department department) {
        departmentList.remove(department);
    }

    public void addShiftPreference(Shift shift) {
        shiftPreferenceList.add(shift);
    }

    public void removeShiftPreference(Shift shift) {
        shiftPreferenceList.remove(shift);
    }
}
