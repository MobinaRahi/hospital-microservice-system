package hospital.clinicalservice.model;

import hospital.clinicalservice.model.enums.PrescriptionStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * نسخه — ثبت نسخه و داروهای تجویز شده
 */
@Entity(name = "prescriptionEntity")
@Table(name = "prescriptions",
        indexes = {
                @Index(name = "idx_prescription_encounter", columnList = "encounter_id"),
                @Index(name = "idx_prescription_patient", columnList = "patientId"),
                @Index(name = "idx_prescription_status", columnList = "status")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Prescription extends BaseEntity {

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

    /** شناسه پزشک */
    @Column(nullable = false)
    private Long doctorId;

    /** وضعیت نسخه */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PrescriptionStatus status = PrescriptionStatus.ACTIVE;

    /** تاریخ تجویز */
    @Column(name = "prescribed_date", nullable = false)
    private LocalDate prescribedDate;

    /** تاریخ انقضا */
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    /** یادداشت پزشک */
    @Column(name = "notes", length = 1000)
    private String notes;

    /** آیتم‌های نسخه (داروها) */
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PrescriptionItem> items = new ArrayList<>();
}
