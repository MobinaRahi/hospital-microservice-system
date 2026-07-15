package hospital.clinicalservice.service.imp;

import hospital.clinicalservice.dto.prescription.PrescriptionCreateDto;
import hospital.clinicalservice.dto.prescription.PrescriptionResponseDto;
import hospital.clinicalservice.dto.prescription.PrescriptionUpdateDto;
import hospital.clinicalservice.exception.prescription.PrescriptionNotFoundException;
import hospital.clinicalservice.exception.prescription.InvalidPrescriptionStateException;
import hospital.clinicalservice.exception.encounter.EncounterNotFoundException;
import hospital.clinicalservice.mapper.PrescriptionMapper;
import hospital.clinicalservice.model.Encounter;
import hospital.clinicalservice.model.Prescription;
import hospital.clinicalservice.model.enums.PrescriptionStatus;
import hospital.clinicalservice.repository.EncounterRepository;
import hospital.clinicalservice.repository.PrescriptionRepository;
import hospital.clinicalservice.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final EncounterRepository encounterRepository;

    @Override
    @Transactional
    public PrescriptionResponseDto createPrescription(PrescriptionCreateDto createDto) {
        log.info("Creating prescription for patient: {} by doctor: {}", createDto.getPatientId(), createDto.getDoctorId());
        Prescription prescription = prescriptionMapper.toEntity(createDto);
        if (createDto.getEncounterId() != null) {
            Encounter encounter = encounterRepository.findNotDeletedById(createDto.getEncounterId())
                    .orElseThrow(() -> EncounterNotFoundException.byId(createDto.getEncounterId()));
            prescription.setEncounter(encounter);
        }
        Prescription saved = prescriptionRepository.save(prescription);
        log.info("Prescription created with id: {}", saved.getId());
        return prescriptionMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public PrescriptionResponseDto updatePrescription(Long id, PrescriptionUpdateDto updateDto) {
        log.info("Updating prescription id: {}", id);
        Prescription prescription = prescriptionRepository.findNotDeletedById(id)
                .orElseThrow(() -> PrescriptionNotFoundException.byId(id));
        prescriptionMapper.updateEntity(prescription, updateDto);
        Prescription updated = prescriptionRepository.save(prescription);
        log.info("Prescription updated id: {}", id);
        return prescriptionMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void cancelPrescription(Long id) {
        log.info("Cancelling prescription id: {}", id);
        Prescription prescription = prescriptionRepository.findNotDeletedById(id)
                .orElseThrow(() -> PrescriptionNotFoundException.byId(id));
        if (prescription.getStatus() == PrescriptionStatus.CANCELLED) {
            throw InvalidPrescriptionStateException.alreadyCancelled(id);
        }
        if (prescription.getStatus() == PrescriptionStatus.EXPIRED) {
            throw InvalidPrescriptionStateException.alreadyExpired(id);
        }
        prescription.setStatus(PrescriptionStatus.CANCELLED);
        prescriptionRepository.save(prescription);
        log.info("Prescription cancelled id: {}", id);
    }

    @Override
    @Transactional
    public void completePrescription(Long id) {
        log.info("Completing prescription id: {}", id);
        Prescription prescription = prescriptionRepository.findNotDeletedById(id)
                .orElseThrow(() -> PrescriptionNotFoundException.byId(id));
        prescription.setStatus(PrescriptionStatus.COMPLETED);
        prescriptionRepository.save(prescription);
        log.info("Prescription completed id: {}", id);
    }

    @Override
    public PrescriptionResponseDto getPrescriptionById(Long id) {
        log.debug("Fetching prescription by id: {}", id);
        Prescription prescription = prescriptionRepository.findNotDeletedById(id)
                .orElseThrow(() -> PrescriptionNotFoundException.byId(id));
        return prescriptionMapper.toResponseDto(prescription);
    }

    @Override
    public List<PrescriptionResponseDto> getPrescriptionsByEncounterId(Long encounterId) {
        log.debug("Fetching prescriptions by encounterId: {}", encounterId);
        return prescriptionRepository.findByEncounterId(encounterId)
                .stream()
                .map(prescriptionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionResponseDto> getPrescriptionsByPatientId(Long patientId) {
        log.debug("Fetching prescriptions by patientId: {}", patientId);
        return prescriptionRepository.findByPatientIdOrderByPrescribedDateDesc(patientId)
                .stream()
                .map(prescriptionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionResponseDto> getActivePrescriptionsByPatientId(Long patientId) {
        log.debug("Fetching active prescriptions by patientId: {}", patientId);
        return prescriptionRepository.findActiveByPatientId(patientId, LocalDate.now())
                .stream()
                .map(prescriptionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionResponseDto> getPrescriptionsByDoctorId(Long doctorId) {
        log.debug("Fetching prescriptions by doctorId: {}", doctorId);
        return prescriptionRepository.findByDoctorId(doctorId)
                .stream()
                .map(prescriptionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionResponseDto> getPrescriptionsByStatus(PrescriptionStatus status) {
        log.debug("Fetching prescriptions by status: {}", status);
        return prescriptionRepository.findByStatus(status)
                .stream()
                .map(prescriptionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionResponseDto> getExpiredPrescriptions() {
        log.debug("Fetching expired prescriptions");
        return prescriptionRepository.findExpiredPrescriptions(LocalDate.now())
                .stream()
                .map(prescriptionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long countPrescriptionsByPatientId(Long patientId) {
        return prescriptionRepository.countByPatientId(patientId);
    }

    @Override
    public Long countPrescriptionsByDoctorId(Long doctorId) {
        return prescriptionRepository.countByDoctorId(doctorId);
    }

    @Override
    public Long countPrescriptionsByStatus(PrescriptionStatus status) {
        return prescriptionRepository.countByStatus(status);
    }
}
