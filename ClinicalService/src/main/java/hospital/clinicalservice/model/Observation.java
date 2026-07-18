package hospital.clinicalservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Represents a clinical observation or vital sign using LOINC codes.
 * Tracks numeric/text values with reference ranges and abnormality flags.
 *
 * @author Mobina
 */
@Entity(name = "observationEntity")
@Table(name = "observations",
        indexes = {
                @Index(name = "idx_observation_encounter", columnList = "encounter_id"),
                @Index(name = "idx_observation_patient", columnList = "patientId"),
                @Index(name = "idx_observation_loinc", columnList = "loincCode")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Observation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Related encounter */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id")
    private Encounter encounter;

    /** Patient ID */
    @Column(nullable = false)
    private Long patientId;

    /** Recorder ID (doctor/nurse) */
    @Column(nullable = false)
    private Long recordedBy;

    /** کد LOINC (استاندارد بین‌المللی) */
    @Column(name = "loinc_code", length = 20, nullable = false)
    private String loincCode;

    /** نام مشاهده (مثلاً: فشار خون سیستولیک) */
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /** Numeric value */
    @Column(name = "value_numeric")
    private Double valueNumeric;

    /** Text value */
    @Column(name = "value_text", length = 200)
    private String valueText;

    /** واحد (mmHg, bpm, °C, kg, ...) */
    @Column(name = "unit", length = 30)
    private String unit;

    /** Normal range low */
    @Column(name = "reference_range_low")
    private Double referenceRangeLow;

    /** Normal range high */
    @Column(name = "reference_range_high")
    private Double referenceRangeHigh;

    /** Is abnormal? */
    @Column(name = "is_abnormal")
    private boolean abnormal;

    /** Observed at */
    @Column(name = "observed_at", nullable = false)
    private LocalDateTime observedAt;

    /** Notes */
    @Column(name = "notes", length = 500)
    private String notes;
}
