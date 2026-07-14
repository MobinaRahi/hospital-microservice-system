package hospital.clinicalservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * تشخیص — ثبت تشخیص پزشک با کد استاندارد ICD-10
 * مثال: I10 = فشار خون بالا، E11 = دیابت نوع دو
 */
@Entity(name = "diagnosisEntity")
@Table(name = "diagnoses",
        indexes = {
                @Index(name = "idx_diagnosis_encounter", columnList = "encounter_id"),
                @Index(name = "idx_diagnosis_icd", columnList = "icd10Code")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Diagnosis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ویزیت مربوطه */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id", nullable = false)
    private Encounter encounter;

    /** کد ICD-10 (استاندارد بین‌المللی) */
    @Column(name = "icd10_code", length = 10, nullable = false)
    private String icd10Code;

    /** نام تشخیص */
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    /** توضیحات تشخیص */
    @Column(name = "description", length = 1000)
    private String description;

    /** تشخیص اصلی یا فرعی */
    @Column(name = "is_primary", nullable = false)
    private boolean primary;

    /** یادداشت پزشک */
    @Column(name = "notes", length = 1000)
    private String notes;
}
