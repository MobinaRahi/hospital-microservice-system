package hospital.labservice.service.impl;

import hospital.labservice.dto.labequipment.LabEquipmentCreateDto;
import hospital.labservice.dto.labequipment.LabEquipmentResponseDto;
import hospital.labservice.dto.labequipment.LabEquipmentUpdateDto;
import hospital.labservice.exception.labequipment.DuplicateLabEquipmentSerialNumberException;
import hospital.labservice.exception.labequipment.LabEquipmentNotFoundException;
import hospital.labservice.mapper.LabEquipmentMapper;
import hospital.labservice.model.LabEquipment;
import hospital.labservice.model.enums.EquipmentStatus;
import hospital.labservice.repository.LabEquipmentRepository;
import hospital.labservice.service.LabEquipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of {@link LabEquipmentService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabEquipmentServiceImpl implements LabEquipmentService {

    private final LabEquipmentRepository labEquipmentRepository;
    private final LabEquipmentMapper labEquipmentMapper;

    @Override
    public LabEquipmentResponseDto createEquipment(LabEquipmentCreateDto dto) {
        log.info("Creating lab equipment: {}", dto.getSerialNumber());

        if (labEquipmentRepository.existsBySerialNumber(dto.getSerialNumber())) {
            throw new DuplicateLabEquipmentSerialNumberException(dto.getSerialNumber());
        }

        LabEquipment equipment = labEquipmentMapper.toEntity(dto);
        LabEquipment saved = labEquipmentRepository.save(equipment);
        log.info("Lab equipment created with id: {}", saved.getId());

        return labEquipmentMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LabEquipmentResponseDto getEquipmentById(Long id) {
        log.debug("Fetching lab equipment by id: {}", id);

        LabEquipment equipment = labEquipmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabEquipmentNotFoundException.byId(id));

        return labEquipmentMapper.toResponseDto(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public LabEquipmentResponseDto getEquipmentBySerialNumber(String serialNumber) {
        log.debug("Fetching lab equipment by serial number: {}", serialNumber);

        LabEquipment equipment = labEquipmentRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> LabEquipmentNotFoundException.bySerialNumber(serialNumber));

        return labEquipmentMapper.toResponseDto(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabEquipmentResponseDto> getAllEquipment() {
        log.debug("Fetching all lab equipment");

        List<LabEquipment> equipment = labEquipmentRepository.findAllNotDeleted();
        return labEquipmentMapper.toResponseDtoList(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabEquipmentResponseDto> getEquipmentByStatus(EquipmentStatus status) {
        log.debug("Fetching equipment by status: {}", status);

        List<LabEquipment> equipment = labEquipmentRepository.findByStatus(status);
        return labEquipmentMapper.toResponseDtoList(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabEquipmentResponseDto> getEquipmentNeedingCalibration() {
        log.debug("Fetching equipment needing calibration");

        List<LabEquipment> equipment = labEquipmentRepository.findNeedsCalibration(LocalDate.now());
        return labEquipmentMapper.toResponseDtoList(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabEquipmentResponseDto> getAvailableEquipment() {
        log.debug("Fetching available equipment");

        List<LabEquipment> equipment = labEquipmentRepository.findByStatus(EquipmentStatus.OPERATIONAL);
        // Filter to only those that don't need calibration
        return equipment.stream()
                .filter(LabEquipment::isAvailable)
                .map(labEquipmentMapper::toResponseDto)
                .toList();
    }

    @Override
    public LabEquipmentResponseDto scheduleCalibration(Long id, LocalDate nextCalibrationDate) {
        log.info("Scheduling calibration for equipment id: {} on: {}", id, nextCalibrationDate);

        LabEquipment equipment = labEquipmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabEquipmentNotFoundException.byId(id));

        equipment.scheduleCalibration(nextCalibrationDate);
        LabEquipment saved = labEquipmentRepository.save(equipment);
        log.info("Calibration scheduled for equipment id: {}", id);

        return labEquipmentMapper.toResponseDto(saved);
    }

    @Override
    public LabEquipmentResponseDto markUnderMaintenance(Long id) {
        log.info("Marking equipment id: {} as under maintenance", id);

        LabEquipment equipment = labEquipmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabEquipmentNotFoundException.byId(id));

        equipment.markUnderMaintenance();
        LabEquipment saved = labEquipmentRepository.save(equipment);

        return labEquipmentMapper.toResponseDto(saved);
    }

    @Override
    public LabEquipmentResponseDto markOperational(Long id) {
        log.info("Marking equipment id: {} as operational", id);

        LabEquipment equipment = labEquipmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabEquipmentNotFoundException.byId(id));

        equipment.markOperational();
        LabEquipment saved = labEquipmentRepository.save(equipment);

        return labEquipmentMapper.toResponseDto(saved);
    }

    @Override
    public LabEquipmentResponseDto markBroken(Long id) {
        log.info("Marking equipment id: {} as broken", id);

        LabEquipment equipment = labEquipmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabEquipmentNotFoundException.byId(id));

        equipment.markBroken();
        LabEquipment saved = labEquipmentRepository.save(equipment);

        return labEquipmentMapper.toResponseDto(saved);
    }

    @Override
    public LabEquipmentResponseDto decommission(Long id) {
        log.info("Decommissioning equipment id: {}", id);

        LabEquipment equipment = labEquipmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabEquipmentNotFoundException.byId(id));

        equipment.decommission();
        LabEquipment saved = labEquipmentRepository.save(equipment);
        log.info("Equipment {} decommissioned", id);

        return labEquipmentMapper.toResponseDto(saved);
    }

    @Override
    public LabEquipmentResponseDto updateEquipment(Long id, LabEquipmentUpdateDto dto) {
        log.info("Updating lab equipment id: {}", id);

        LabEquipment equipment = labEquipmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabEquipmentNotFoundException.byId(id));

        labEquipmentMapper.updateEntity(dto, equipment);
        LabEquipment saved = labEquipmentRepository.save(equipment);
        log.info("Lab equipment updated id: {}", saved.getId());

        return labEquipmentMapper.toResponseDto(saved);
    }

    @Override
    public void deleteEquipment(Long id) {
        log.info("Soft-deleting lab equipment id: {}", id);

        LabEquipment equipment = labEquipmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabEquipmentNotFoundException.byId(id));

        equipment.softDelete(null);
        labEquipmentRepository.save(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean serialNumberExists(String serialNumber) {
        return labEquipmentRepository.existsBySerialNumber(serialNumber);
    }
}
