package hospital.coreservice.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Entity(name = "shiftEntity")
@Table(name = "shifts",
        indexes = {
                @Index(name = "idx_shift_is_active", columnList = "is_active")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
/**
 * Represents a work shift.
 * Defines working hours for hospital staff.
 *
 * @author Mobina
 */
public class Shift extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "duration_hours", nullable = false)
    private Integer durationHours;

    @Column(name = "night_shift")
    private boolean nightShift;

    @Column(name = "has_extra_pay")
    private boolean hasExtraPay;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

}
