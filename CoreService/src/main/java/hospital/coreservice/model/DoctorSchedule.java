package hospital.coreservice.model;

import hospital.coreservice.model.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(name = "doctorScheduleEntity")
@Table(name = "doctor_schedules",
        indexes = {
                @Index(name = "idx_doc_schedule_doctor", columnList = "doctor_id"),
                @Index(name = "idx_doc_schedule_day", columnList = "day_of_week"),
                @Index(name = "idx_doc_schedule_active", columnList = "is_active")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class DoctorSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "slot_duration", nullable = false)
    private Integer slotDuration;

    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

}

