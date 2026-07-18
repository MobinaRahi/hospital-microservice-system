package hospital.clinicalservice.service.imp;

import hospital.clinicalservice.dto.nursingnote.NursingNoteCreateDto;
import hospital.clinicalservice.dto.nursingnote.NursingNoteResponseDto;
import hospital.clinicalservice.dto.nursingnote.NursingNoteUpdateDto;
import hospital.clinicalservice.exception.nursingnote.NursingNoteNotFoundException;
import hospital.clinicalservice.exception.encounter.EncounterNotFoundException;
import hospital.clinicalservice.mapper.NursingNoteMapper;
import hospital.clinicalservice.model.Encounter;
import hospital.clinicalservice.model.NursingNote;
import hospital.clinicalservice.model.enums.NoteType;
import hospital.clinicalservice.model.enums.Shift;
import hospital.clinicalservice.repository.EncounterRepository;
import hospital.clinicalservice.repository.NursingNoteRepository;
import hospital.clinicalservice.service.NursingNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of NursingNoteService.
 * Manages nursing notes including shift reports.
 *
 * @author Mobina
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NursingNoteServiceImpl implements NursingNoteService {

    private final NursingNoteRepository nursingNoteRepository;
    private final NursingNoteMapper nursingNoteMapper;
    private final EncounterRepository encounterRepository;

    @Override
    @Transactional
    public NursingNoteResponseDto createNursingNote(NursingNoteCreateDto createDto) {
        log.info("Creating nursing note for patient: {}", createDto.getPatientId());
        NursingNote note = nursingNoteMapper.toEntity(createDto);
        if (createDto.getEncounterId() != null) {
            Encounter encounter = encounterRepository.findNotDeletedById(createDto.getEncounterId())
                    .orElseThrow(() -> EncounterNotFoundException.byId(createDto.getEncounterId()));
            note.setEncounter(encounter);
        }
        NursingNote saved = nursingNoteRepository.save(note);
        log.info("Nursing note created with id: {}", saved.getId());
        return nursingNoteMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public NursingNoteResponseDto updateNursingNote(Long id, NursingNoteUpdateDto updateDto) {
        log.info("Updating nursing note id: {}", id);
        NursingNote note = nursingNoteRepository.findNotDeletedById(id)
                .orElseThrow(() -> NursingNoteNotFoundException.byId(id));
        nursingNoteMapper.updateEntity(note, updateDto);
        NursingNote updated = nursingNoteRepository.save(note);
        return nursingNoteMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteNursingNote(Long id) {
        log.info("Deleting nursing note id: {}", id);
        nursingNoteRepository.softDeleteById(id, LocalDateTime.now());
    }

    @Override
    public NursingNoteResponseDto getNursingNoteById(Long id) {
        NursingNote note = nursingNoteRepository.findNotDeletedById(id)
                .orElseThrow(() -> NursingNoteNotFoundException.byId(id));
        return nursingNoteMapper.toResponseDto(note);
    }

    @Override
    public List<NursingNoteResponseDto> getNursingNotesByEncounterId(Long encounterId) {
        return nursingNoteRepository.findByEncounterId(encounterId)
                .stream().map(nursingNoteMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<NursingNoteResponseDto> getNursingNotesByPatientId(Long patientId) {
        return nursingNoteRepository.findByPatientIdOrderByRecordedAtDesc(patientId)
                .stream().map(nursingNoteMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<NursingNoteResponseDto> getNursingNotesByNurseId(Long nurseId) {
        return nursingNoteRepository.findByNurseIdOrderByRecordedAtDesc(nurseId)
                .stream().map(nursingNoteMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<NursingNoteResponseDto> getNursingNotesByNoteType(NoteType noteType) {
        return nursingNoteRepository.findByNoteType(noteType)
                .stream().map(nursingNoteMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<NursingNoteResponseDto> getNursingNotesByPatientIdAndNoteType(Long patientId, NoteType noteType) {
        return nursingNoteRepository.findByPatientIdAndNoteType(patientId, noteType)
                .stream().map(nursingNoteMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<NursingNoteResponseDto> getNursingNotesByShift(Shift shift) {
        return nursingNoteRepository.findByShift(shift)
                .stream().map(nursingNoteMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public Long countNursingNotesByPatientId(Long patientId) {
        return nursingNoteRepository.countByPatientId(patientId);
    }

    @Override
    public Long countNursingNotesByNurseId(Long nurseId) {
        return nursingNoteRepository.countByNurseId(nurseId);
    }
}
