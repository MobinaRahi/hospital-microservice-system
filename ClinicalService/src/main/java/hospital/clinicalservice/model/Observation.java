package hospital.clinicalservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * مشاهده/علائم حیاتی — ثبت علائم حیاتی بیمار
 * با کد استاندارد LOINC کدگذاری می‌شود
 * مثال: 8480-6 = فشار خون سیستولیک، 8867-4 = ضربان قلب
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

    /** ویزیت مربوطه */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id")
    private Encounter encounter;

    /** شناسه بیمار */
    @Column(nullable = false)
    private Long patientId;

    /** شناسه ثبت‌کننده (پزشک/پرستار) */
    @Column(nullable = false)
    private Long recordedBy;

    /** کد LOINC (استاندارد بین‌المللی) */
    @Column(name = "loinc_code", length = 20, nullable = false)
    private String loincCode;

    /** نام مشاهده (مثلاً: فشار خون سیستولیک) */
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /** مقدار عددی */
    @Column(name = "value_numeric")
    private Double valueNumeric;

    /** مقدار متنی */
    @Column(name = "value_text", length = 200)
    private String valueText;

    /** واحد (mmHg, bpm, °C, kg, ...) */
    @Column(name = "unit", length = 30)
    private String unit;

    /** محدوده نرمال پایین */
    @Column(name = "reference_range_low")
    private Double referenceRangeLow;

    /** محدوده نرمال بالا */
    @Column(name = "reference_range_high")
    private Double referenceRangeHigh;

    /** آیا مقدار غیرنرمال است؟ */
    @Column(name = "is_abnormal")
    private boolean abnormal;

    /** زمان اندازه‌گیری */
    @Column(name = "observed_at", nullable = false)
    private LocalDateTime observedAt;

    /** یادداشت */
    @Column(name = "notes", length = 500)
    private String notes;
}
