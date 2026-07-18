package hospital.clinicalservice.service;

import hospital.clinicalservice.dto.nursingnote.NursingNoteCreateDto;
import hospital.clinicalservice.dto.nursingnote.NursingNoteResponseDto;
import hospital.clinicalservice.dto.nursingnote.NursingNoteUpdateDto;
import hospital.clinicalservice.model.enums.NoteType;
import hospital.clinicalservice.model.enums.Shift;

import java.util.List;

/**
 * Service interface for NursingNote management.
 * Handles CRUD operations for nursing notes and shift reports.
 *
 * @author Mobina
 */
public interface NursingNoteService {

    NursingNoteResponseDto createNursingNote(NursingNoteCreateDto createDto);

    NursingNoteResponseDto updateNursingNote(Long id, NursingNoteUpdateDto updateDto);

    void deleteNursingNote(Long id);

    NursingNoteResponseDto getNursingNoteById(Long id);

    List<NursingNoteResponseDto> getNursingNotesByEncounterId(Long encounterId);

    List<NursingNoteResponseDto> getNursingNotesByPatientId(Long patientId);

    List<NursingNoteResponseDto> getNursingNotesByNurseId(Long nurseId);

    List<NursingNoteResponseDto> getNursingNotesByNoteType(NoteType noteType);

    List<NursingNoteResponseDto> getNursingNotesByPatientIdAndNoteType(Long patientId, NoteType noteType);

    List<NursingNoteResponseDto> getNursingNotesByShift(Shift shift);

    Long countNursingNotesByPatientId(Long patientId);

    Long countNursingNotesByNurseId(Long nurseId);
}
