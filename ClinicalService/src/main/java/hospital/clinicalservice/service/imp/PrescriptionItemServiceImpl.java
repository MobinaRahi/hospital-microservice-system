package hospital.clinicalservice.service.imp;

import hospital.clinicalservice.dto.prescriptionitem.PrescriptionItemCreateDto;
import hospital.clinicalservice.dto.prescriptionitem.PrescriptionItemResponseDto;
import hospital.clinicalservice.dto.prescriptionitem.PrescriptionItemUpdateDto;
import hospital.clinicalservice.exception.prescription.PrescriptionItemNotFoundException;
import hospital.clinicalservice.exception.prescription.PrescriptionNotFoundException;
import hospital.clinicalservice.mapper.PrescriptionItemMapper;
import hospital.clinicalservice.model.Prescription;
import hospital.clinicalservice.model.PrescriptionItem;
import hospital.clinicalservice.repository.PrescriptionItemRepository;
import hospital.clinicalservice.repository.PrescriptionRepository;
import hospital.clinicalservice.service.PrescriptionItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PrescriptionItemServiceImpl implements PrescriptionItemService {

    private final PrescriptionItemRepository prescriptionItemRepository;
    private final PrescriptionItemMapper prescriptionItemMapper;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    @Transactional
    public PrescriptionItemResponseDto createPrescriptionItem(PrescriptionItemCreateDto createDto) {
        log.info("Creating prescription item for prescription: {}", createDto.getPrescriptionId());
        Prescription prescription = prescriptionRepository.findNotDeletedById(createDto.getPrescriptionId())
                .orElseThrow(() -> PrescriptionNotFoundException.byId(createDto.getPrescriptionId()));
        PrescriptionItem item = prescriptionItemMapper.toEntity(createDto);
        item.setPrescription(prescription);
        PrescriptionItem saved = prescriptionItemRepository.save(item);
        log.info("Prescription item created with id: {}", saved.getId());
        return prescriptionItemMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public PrescriptionItemResponseDto updatePrescriptionItem(Long id, PrescriptionItemUpdateDto updateDto) {
        log.info("Updating prescription item id: {}", id);
        PrescriptionItem item = prescriptionItemRepository.findNotDeletedById(id)
                .orElseThrow(() -> PrescriptionItemNotFoundException.byId(id));
        prescriptionItemMapper.updateEntity(item, updateDto);
        PrescriptionItem updated = prescriptionItemRepository.save(item);
        return prescriptionItemMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deletePrescriptionItem(Long id) {
        log.info("Deleting prescription item id: {}", id);
        prescriptionItemRepository.softDeleteById(id, LocalDateTime.now());
    }

    @Override
    public PrescriptionItemResponseDto getPrescriptionItemById(Long id) {
        PrescriptionItem item = prescriptionItemRepository.findNotDeletedById(id)
                .orElseThrow(() -> PrescriptionItemNotFoundException.byId(id));
        return prescriptionItemMapper.toResponseDto(item);
    }

    @Override
    public List<PrescriptionItemResponseDto> getPrescriptionItemsByPrescriptionId(Long prescriptionId) {
        return prescriptionItemRepository.findByPrescriptionId(prescriptionId)
                .stream().map(prescriptionItemMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionItemResponseDto> getPrescriptionItemsByDrugId(Long drugId) {
        return prescriptionItemRepository.findByDrugId(drugId)
                .stream().map(prescriptionItemMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionItemResponseDto> searchPrescriptionItemsByDrugName(String drugName) {
        return prescriptionItemRepository.findByDrugNameContaining(drugName)
                .stream().map(prescriptionItemMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionItemResponseDto> getPrescriptionItemsByPatientId(Long patientId) {
        return prescriptionItemRepository.findByPatientId(patientId)
                .stream().map(prescriptionItemMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public Long countPrescriptionItemsByPrescriptionId(Long prescriptionId) {
        return prescriptionItemRepository.countByPrescriptionId(prescriptionId);
    }
}
