package hospital.clinicalservice.model;

import hospital.clinicalservice.model.enums.EncounterStatus;
import hospital.clinicalservice.model.enums.EncounterType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ویزیت — ثبت هربار مراجعه بیمار به پزشک
 * هر ویزیت شامل تشخیص‌ها، نسخه‌ها، علائم حیاتی و گزارش پرستاری است
 */
@Entity(name = "encounterEntity")
@Table(name = "encounters",
        indexes = {
                @Index(name = "idx_encounter_patient", columnList = "patientId"),
                @Index(name = "idx_encounter_doctor", columnList = "doctorId"),
                @Index(name = "idx_encounter_status", columnList = "status"),
                @Index(name = "idx_encounter_date", columnList = "encounterDate")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Encounter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** شناسه بیمار (از CoreService) */
    @Column(nullable = false)
    private Long patientId;

    /** شناسه پزشک (از CoreService) */
    @Column(nullable = false)
    private Long doctorId;

    /** شناسه نوبت (از CoreService) */
    @Column
    private Long appointmentId;

    /** شناسه بخش (از CoreService) */
    @Column
    private Long departmentId;

    /** تاریخ و ساعت ویزیت */
    @Column(nullable = false)
    private LocalDateTime encounterDate;

    /** نوع ویزیت */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EncounterType type;

    /** وضعیت ویزیت */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EncounterStatus status = EncounterStatus.IN_PROGRESS;

    /** شرح حال بیمار */
    @Column(length = 2000)
    private String chiefComplaint;

    /** یادداشت پزشک */
    @Column(length = 2000)
    private String doctorNotes;

    /** تشخیص‌های این ویزیت */
    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Diagnosis> diagnoses = new ArrayList<>();

    /** نسخه‌های این ویزیت */
    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Prescription> prescriptions = new ArrayList<>();

    /** علائم حیاتی این ویزیت */
    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Observation> observations = new ArrayList<>();

    /** گزارش‌های پرستاری */
    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<NursingNote> nursingNotes = new ArrayList<>();
}
