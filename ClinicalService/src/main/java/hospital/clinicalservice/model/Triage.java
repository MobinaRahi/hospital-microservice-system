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
 * Represents an emergency triage assessment with 5 severity levels.
 * Level 1 (critical) to Level 5 (consultation).
 *
 * @author Mobina
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

    /** Related encounter */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id", nullable = false)
    private Encounter encounter;

    /** Patient ID */
    @Column(nullable = false)
    private Long patientId;

    /** Triage level */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TriageLevel level;

    /** Chief complaint */
    @Column(name = "chief_complaint", length = 1000, nullable = false)
    private String chiefComplaint;

    /** Symptoms */
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

    /** Notes */
    @Column(name = "notes", length = 1000)
    private String notes;

    /** Triage nurse ID */
    @Column(nullable = false)
    private Long triagedBy;

    /** Triage time */
    @Column(name = "triaged_at", nullable = false)
    private LocalDateTime triagedAt;
}
