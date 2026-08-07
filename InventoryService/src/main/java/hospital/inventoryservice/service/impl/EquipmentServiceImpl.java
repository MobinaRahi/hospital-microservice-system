package hospital.inventoryservice.service.impl;

import hospital.inventoryservice.exception.equipment.DuplicateEquipmentSerialNumberException;
import hospital.inventoryservice.dto.equipment.EquipmentCreateDto;
import hospital.inventoryservice.dto.equipment.EquipmentResponseDto;
import hospital.inventoryservice.dto.equipment.EquipmentUpdateDto;


import hospital.inventoryservice.mapper.EquipmentMapper;
import hospital.inventoryservice.exception.equipment.EquipmentNotFoundException;
import hospital.inventoryservice.exception.equipment.DuplicateEquipmentSerialNumberException;
import hospital.inventoryservice.model.Equipment;
import hospital.inventoryservice.model.enums.EquipmentStatus;
import hospital.inventoryservice.model.enums.EquipmentType;
import hospital.inventoryservice.repository.EquipmentRepository;
import hospital.inventoryservice.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link EquipmentService}.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Serial number must be unique</li>
 *   <li>Equipment status transitions: AVAILABLE → IN_USE → MAINTENANCE → AVAILABLE</li>
 *   <li>Broken equipment should be flagged for repair or disposal</li>
 *   <li>Warranty expiry is tracked</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;

    // ═══════════════════════════════════════════════════════════════════
    // Create
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public EquipmentResponseDto createEquipment(EquipmentCreateDto dto) {
        log.info("Creating equipment: {}", dto.getName());

        // Validate serial number uniqueness
        if (dto.getSerialNumber() != null && serialNumberExists(dto.getSerialNumber())) {
            throw new DuplicateEquipmentSerialNumberException(dto.getSerialNumber());
        }

        // Map DTO to entity
        Equipment equipment = equipmentMapper.toEntity(dto);
        equipment.setStatus(EquipmentStatus.AVAILABLE);
        equipment.setIsActive(true);

        // Save and return
        Equipment saved = equipmentRepository.save(equipment);
        log.info("Equipment created with id: {}", saved.getId());

        return equipmentMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public EquipmentResponseDto getEquipmentById(Long id) {
        log.debug("Fetching equipment by id: {}", id);

        Equipment equipment = equipmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> EquipmentNotFoundException.byId(id));

        return equipmentMapper.toResponseDto(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public EquipmentResponseDto getEquipmentBySerialNumber(String serialNumber) {
        log.debug("Fetching equipment by serial number: {}", serialNumber);

        Equipment equipment = equipmentRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> EquipmentNotFoundException.bySerialNumber(serialNumber));

        return equipmentMapper.toResponseDto(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentResponseDto> getAllActiveEquipment() {
        log.debug("Fetching all active equipment");

        List<Equipment> equipment = equipmentRepository.findByIsActiveTrue();
        return equipmentMapper.toResponseDtoList(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentResponseDto> getAllEquipment() {
        log.debug("Fetching all equipment");

        List<Equipment> equipment = equipmentRepository.findAllNotDeleted();
        return equipmentMapper.toResponseDtoList(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentResponseDto> getEquipmentByType(EquipmentType type) {
        log.debug("Fetching equipment by type: {}", type);

        List<Equipment> equipment = equipmentRepository.findByType(type);
        return equipmentMapper.toResponseDtoList(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentResponseDto> getEquipmentByStatus(EquipmentStatus status) {
        log.debug("Fetching equipment by status: {}", status);

        List<Equipment> equipment = equipmentRepository.findByStatus(status);
        return equipmentMapper.toResponseDtoList(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentResponseDto> getAvailableEquipment() {
        log.debug("Fetching available equipment");

        List<Equipment> equipment = equipmentRepository.findByStatus(EquipmentStatus.AVAILABLE);
        return equipmentMapper.toResponseDtoList(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentResponseDto> getEquipmentWithExpiredWarranty() {
        log.debug("Fetching equipment with expired warranty");

        List<Equipment> equipment = equipmentRepository.findByWarrantyExpiryBefore(java.time.LocalDate.now());
        return equipmentMapper.toResponseDtoList(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentResponseDto> searchByLocation(String location) {
        log.debug("Searching equipment by location: {}", location);

        List<Equipment> equipment = equipmentRepository.findByCurrentLocationContainingIgnoreCase(location);
        return equipmentMapper.toResponseDtoList(equipment);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Update
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public EquipmentResponseDto updateEquipment(Long id, EquipmentUpdateDto dto) {
        log.info("Updating equipment id: {}", id);

        Equipment equipment = equipmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> EquipmentNotFoundException.byId(id));

        // Map update DTO to entity
        equipmentMapper.updateEntity(dto, equipment);

        Equipment saved = equipmentRepository.save(equipment);
        log.info("Equipment updated id: {}", saved.getId());

        return equipmentMapper.toResponseDto(saved);
    }

    @Override
    public EquipmentResponseDto changeStatus(Long id, EquipmentStatus status) {
        log.info("Changing status for equipment id: {} to {}", id, status);

        Equipment equipment = equipmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> EquipmentNotFoundException.byId(id));

        equipment.setStatus(status);
        Equipment saved = equipmentRepository.save(equipment);

        return equipmentMapper.toResponseDto(saved);
    }

    @Override
    public EquipmentResponseDto toggleActive(Long id) {
        log.info("Toggling active status for equipment id: {}", id);

        Equipment equipment = equipmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> EquipmentNotFoundException.byId(id));

        equipment.setIsActive(!equipment.getIsActive());
        Equipment saved = equipmentRepository.save(equipment);

        return equipmentMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Delete
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public void deleteEquipment(Long id) {
        log.info("Soft-deleting equipment id: {}", id);

        Equipment equipment = equipmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> EquipmentNotFoundException.byId(id));

        equipment.softDelete(null);
        equipmentRepository.save(equipment);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Validation
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public boolean serialNumberExists(String serialNumber) {
        return equipmentRepository.existsBySerialNumber(serialNumber);
    }
}
