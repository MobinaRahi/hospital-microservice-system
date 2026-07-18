package hospital.clinicalservice.model;

import hospital.clinicalservice.model.enums.NoteType;
import hospital.clinicalservice.model.enums.Shift;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Represents a nursing note documenting patient care observations and actions.
 * Supports multiple note types (general, shift report, medication, etc.).
 *
 * @author Mobina
 */
@Entity(name = "nursingNoteEntity")
@Table(name = "nursing_notes",
        indexes = {
                @Index(name = "idx_nursing_note_encounter", columnList = "encounter_id"),
                @Index(name = "idx_nursing_note_patient", columnList = "patientId")
        })
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class NursingNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Related encounter
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id")
    private Encounter encounter;

    /**
     * Patient ID
     */
    @Column(nullable = false)
    private Long patientId;

    @Column(name = "note_type", nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private NoteType noteType = NoteType.GENERAL;

    /**
     * Nurse ID
     */
    @Column(nullable = false)
    private Long nurseId;

    @Column(name = "shift")
    @Enumerated(EnumType.STRING)
    private Shift shift;

    /**
     * متن گزارش
     */
    @Column(name = "note", length = 2000, nullable = false)
    private String note;

    /**
     * Recorded at
     */
    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;
}
