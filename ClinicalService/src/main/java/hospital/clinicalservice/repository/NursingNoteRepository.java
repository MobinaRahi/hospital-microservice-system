package hospital.clinicalservice.repository;

import hospital.clinicalservice.model.NursingNote;
import hospital.clinicalservice.model.enums.NoteType;
import hospital.clinicalservice.model.enums.Shift;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NursingNoteRepository extends BaseEntityRepository<NursingNote, Long> {

    // ==================== Encounter Queries ====================

    List<NursingNote> findByEncounterId(Long encounterId);

    // ==================== Patient Queries ====================

    List<NursingNote> findByPatientIdOrderByRecordedAtDesc(Long patientId);

    // ==================== Nurse Queries ====================

    List<NursingNote> findByNurseId(Long nurseId);

    List<NursingNote> findByNurseIdOrderByRecordedAtDesc(Long nurseId);

    // ==================== Type Queries ====================

    List<NursingNote> findByNoteType(NoteType noteType);

    List<NursingNote> findByPatientIdAndNoteType(Long patientId, NoteType noteType);

    // ==================== Shift Queries ====================

    List<NursingNote> findByShift(Shift shift);

    // ==================== Count ====================

    long countByPatientId(Long patientId);

    long countByNurseId(Long nurseId);

    long countByEncounterId(Long encounterId);
}
