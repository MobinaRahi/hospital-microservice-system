package hospital.clinicalservice.model;

import hospital.clinicalservice.model.enums.TriageLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * تریاژ — تعیین سطح فوریت بیمار در اورژانس
 * هرچه سطح کمتر باشد، بیمار اورژانسی‌تر است
 */
@Entity(name = "triageEntity")
@Table(name = "triages",
        indexes = {
                @Index(name = "idx_triage_encounter", columnList = "encounter_id"),
                @Index(name = "idx_triage_patient", columnList = "patientId"),
                @Index(name = "idx_triage_level", columnList = "level")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Triage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ویزیت مربوطه */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id", nullable = false)
    private Encounter encounter;

    /** شناسه بیمار */
    @Column(nullable = false)
    private Long patientId;

    /** سطح فوریت */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TriageLevel level;

    /** شرح حال */
    @Column(name = "chief_complaint", length = 1000, nullable = false)
    private String chiefComplaint;

    /** علائم */
    @Column(name = "symptoms", length = 1000)
    private String symptoms;

    @Column(name = "blood_pressure", length = 1000)
    private String bloodPressure;

    @Column(name = "heart_rate")
    private Long heartRate;

    @Column(name = "temperature")
    private Long temperature;

    @Column(name = "consciousness")
    private String consciousness;

    /** یادداشت */
    @Column(name = "notes", length = 1000)
    private String notes;

    /** شناسه پرستار تریاژ */
    @Column(nullable = false)
    private Long triagedBy;

    /** زمان تریاژ */
    @Column(name = "triaged_at", nullable = false)
    private LocalDateTime triagedAt;
}
