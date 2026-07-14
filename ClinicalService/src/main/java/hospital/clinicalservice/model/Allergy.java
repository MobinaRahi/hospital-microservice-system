package hospital.clinicalservice.model;

import hospital.clinicalservice.model.enums.AllergySeverity;
import hospital.clinicalservice.model.enums.AllergyType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * حساسیت — ثبت حساسیت‌های بیمار (دارویی، غذایی، محیطی)
 * این اطلاعات برای جلوگیری از تجویز داروهای خطرناک بسیار حیاتی است
 */
@Entity(name = "allergyEntity")
@Table(name = "allergies",
        indexes = {
                @Index(name = "idx_allergy_patient", columnList = "patientId"),
                @Index(name = "idx_allergy_type", columnList = "type"),
                @Index(name = "idx_allergy_severity", columnList = "severity")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Allergy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** شناسه بیمار */
    @Column(nullable = false)
    private Long patientId;

    /** نوع حساسیت */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AllergyType type;

    /** نام ماده حساسیت‌زا */
    @Column(name = "allergen_name", length = 200, nullable = false)
    private String allergenName;

    @Column(name = "reaction",length = 500)
    private String reaction;

    /** شدت حساسیت */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AllergySeverity severity;

    /** علائم */
    @Column(name = "symptoms", length = 500)
    private String symptoms;

    /** یادداشت */
    @Column(name = "notes", length = 500)
    private String notes;

    /** آیا فعال است؟ */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean active = true;
}
