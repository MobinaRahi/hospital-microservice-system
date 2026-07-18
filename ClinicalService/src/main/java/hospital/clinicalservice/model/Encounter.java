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
 * Represents a patient encounter (visit) with a doctor.
 * This is the core entity of the clinical service — all clinical data
 * (diagnoses, prescriptions, observations, nursing notes) is linked to an encounter.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>A patient can have multiple encounters per day (different doctors)</li>
 *   <li>Each encounter has a status workflow: IN_PROGRESS → COMPLETED / CANCELLED</li>
 *   <li>All clinical data must be linked to an encounter</li>
 * </ul>
 *
 * @author Mobina
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

    // ==================== Foreign References (from CoreService) ====================

    /**
     * Patient ID from CoreService.
     * Links this encounter to a specific patient.
     * Stored as Long (not Entity) because Patient is in CoreService.
     */
    @Column(nullable = false)
    private Long patientId;

    /**
     * Doctor ID from CoreService.
     * Links this encounter to the doctor who performed the visit.
     * Stored as Long (not Entity) because Doctor is in CoreService.
     */
    @Column(nullable = false)
    private Long doctorId;

    /**
     * Appointment ID from CoreService (optional).
     * Links this encounter to a pre-booked appointment.
     * Null if the encounter was walk-in (no appointment).
     */
    @Column
    private Long appointmentId;

    /**
     * Department ID from CoreService (optional).
     * The department where the encounter took place.
     */
    @Column
    private Long departmentId;

    // ==================== Encounter Details ====================

    /**
     * Date and time when the encounter started.
     * Set automatically when encounter is created.
     * Used for: today's encounters, date range queries, statistics.
     */
    @Column(nullable = false)
    private LocalDateTime encounterDate;

    /**
     * Type of encounter:
     * - OUTPATIENT: Patient visits clinic and goes home
     * - INPATIENT: Patient is admitted to hospital
     * - EMERGENCY: Urgent visit to ER
     * - FOLLOW_UP: Follow-up visit after previous encounter
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EncounterType type;

    /**
     * Current status of the encounter:
     * - IN_PROGRESS: Doctor is currently seeing the patient
     * - COMPLETED: Visit finished, all data recorded
     * - CANCELLED: Visit was cancelled
     *
     * Status workflow: IN_PROGRESS → COMPLETED / CANCELLED
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EncounterStatus status = EncounterStatus.IN_PROGRESS;

    /**
     * Patient's chief complaint - the main reason for the visit.
     * Example: "chest pain for 2 days", "headache and dizziness"
     * Max 2000 characters.
     */
    @Column(length = 2000)
    private String chiefComplaint;

    /**
     * Doctor's notes about the encounter.
     * General observations, treatment plan, follow-up instructions.
     * Max 2000 characters.
     */
    @Column(length = 2000)
    private String doctorNotes;

    // ==================== Relationships ====================

    /**
     * Diagnoses associated with this encounter.
     * A doctor can add multiple diagnoses (primary + secondary).
     * Example: Primary = "Hypertension", Secondary = "Type 2 Diabetes"
     * Cascade: When encounter is deleted, all diagnoses are deleted too.
     */
    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Diagnosis> diagnoses = new ArrayList<>();

    /**
     * Prescriptions associated with this encounter.
     * A doctor can create one or more prescriptions per visit.
     * Cascade: When encounter is deleted, all prescriptions are deleted too.
     */
    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Prescription> prescriptions = new ArrayList<>();

    /**
     * Observations (vital signs) associated with this encounter.
     * Includes blood pressure, heart rate, temperature, etc.
     * Cascade: When encounter is deleted, all observations are deleted too.
     */
    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Observation> observations = new ArrayList<>();

    /**
     * Nursing notes associated with this encounter.
     * Nurses document their observations and care given.
     * Cascade: When encounter is deleted, all notes are deleted too.
     */
    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<NursingNote> nursingNotes = new ArrayList<>();
}
