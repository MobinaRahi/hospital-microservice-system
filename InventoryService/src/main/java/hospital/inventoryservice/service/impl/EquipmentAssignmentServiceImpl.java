package hospital.inventoryservice.service.impl;

import hospital.inventoryservice.dto.equipmentassignment.EquipmentAssignmentCreateDto;
import hospital.inventoryservice.dto.equipmentassignment.EquipmentAssignmentResponseDto;
import hospital.inventoryservice.dto.equipmentassignment.EquipmentAssignmentUpdateDto;

import hospital.inventoryservice.mapper.EquipmentAssignmentMapper;
import hospital.inventoryservice.exception.equipmentassignment.EquipmentAssignmentNotFoundException;
import hospital.inventoryservice.exception.equipmentassignment.EquipmentAlreadyAssignedException;
import hospital.inventoryservice.exception.equipmentassignment.EquipmentNotReturnedException;
import hospital.inventoryservice.exception.equipment.EquipmentNotFoundException;
import hospital.inventoryservice.exception.equipment.EquipmentNotAvailableException;
import hospital.inventoryservice.model.Equipment;
import hospital.inventoryservice.model.EquipmentAssignment;
import hospital.inventoryservice.model.enums.EquipmentStatus;
import hospital.inventoryservice.repository.EquipmentAssignmentRepository;
import hospital.inventoryservice.repository.EquipmentRepository;
import hospital.inventoryservice.service.EquipmentAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link EquipmentAssignmentService}.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>An equipment can only be assigned if its status is AVAILABLE</li>
 *   <li>An equipment cannot be assigned to both patient and department simultaneously</li>
 *   <li>Assigning an equipment changes its status to IN_USE</li>
 *   <li>Returning an equipment changes its status back to AVAILABLE</li>
 *   <li>Overdue assignments are tracked</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EquipmentAssignmentServiceImpl implements EquipmentAssignmentService {

    private final EquipmentAssignmentRepository assignmentRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentAssignmentMapper assignmentMapper;

    // ════════════════════════════════════════════════════════════════════
    // Create
    // ════════════════════════════════════════════════════════════════════

    @Override
    public EquipmentAssignmentResponseDto createAssignment(EquipmentAssignmentCreateDto dto) {
        log.info("Creating equipment assignment for equipment: {}", dto.getEquipmentId());

        // Validate equipment exists and is AVAILABLE
        Equipment equipment = equipmentRepository.findNotDeletedById(dto.getEquipmentId())
                .orElseThrow(() -> EquipmentNotFoundException.byId(dto.getEquipmentId()));

        if (equipment.getStatus() != EquipmentStatus.AVAILABLE) {
            throw new EquipmentNotAvailableException(dto.getEquipmentId());
        }

        // Validate either patient or department is provided
        if (dto.getPatientId() == null && dto.getDepartmentId() == null) {
            throw new IllegalArgumentException("Either patientId or departmentId must be provided");
        }

        // Map DTO to entity
        EquipmentAssignment assignment = assignmentMapper.toEntity(dto);
        assignment.setEquipment(equipment);
        assignment.setActualReturnDate(null);

        // Change equipment status to IN_USE
        equipment.setStatus(EquipmentStatus.IN_USE);
        equipmentRepository.save(equipment);

        // Save and return
        EquipmentAssignment saved = assignmentRepository.save(assignment);
        log.info("Assignment created with id: {}", saved.getId());

        return assignmentMapper.toResponseDto(saved);
    }

    // ═══════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public EquipmentAssignmentResponseDto getAssignmentById(Long id) {
        log.debug("Fetching assignment by id: {}", id);

        EquipmentAssignment assignment = assignmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> EquipmentAssignmentNotFoundException.byId(id));

        return assignmentMapper.toResponseDto(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentAssignmentResponseDto> getActiveAssignments() {
        log.debug("Fetching active assignments");

        List<EquipmentAssignment> assignments = assignmentRepository.findAllNotDeleted().stream()
                .filter(EquipmentAssignment::isActive)
                .toList();
        return assignmentMapper.toResponseDtoList(assignments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentAssignmentResponseDto> getAllAssignments() {
        log.debug("Fetching all assignments");

        List<EquipmentAssignment> assignments = assignmentRepository.findAllNotDeleted();
        return assignmentMapper.toResponseDtoList(assignments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentAssignmentResponseDto> getAssignmentsByEquipment(Long equipmentId) {
        log.debug("Fetching assignments for equipment: {}", equipmentId);

        List<EquipmentAssignment> assignments = assignmentRepository.findByEquipmentIdAndActualReturnDateIsNull(equipmentId);
        return assignmentMapper.toResponseDtoList(assignments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentAssignmentResponseDto> getAssignmentsByPatient(Long patientId) {
        log.debug("Fetching assignments for patient: {}", patientId);

        List<EquipmentAssignment> assignments = assignmentRepository.findByPatientId(patientId);
        return assignmentMapper.toResponseDtoList(assignments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentAssignmentResponseDto> getAssignmentsByDepartment(Long departmentId) {
        log.debug("Fetching assignments for department: {}", departmentId);

        List<EquipmentAssignment> assignments = assignmentRepository.findByDepartmentId(departmentId);
        return assignmentMapper.toResponseDtoList(assignments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentAssignmentResponseDto> getOverdueAssignments() {
        log.debug("Fetching overdue assignments");

        List<EquipmentAssignment> assignments = assignmentRepository
                .findByExpectedReturnDateBeforeAndActualReturnDateIsNull(LocalDateTime.now());
        return assignmentMapper.toResponseDtoList(assignments);
    }

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    @Override
    public EquipmentAssignmentResponseDto updateAssignment(Long id, EquipmentAssignmentUpdateDto dto) {
        log.info("Updating assignment id: {}", id);

        EquipmentAssignment assignment = assignmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> EquipmentAssignmentNotFoundException.byId(id));

        // Map update DTO to entity
        assignmentMapper.updateEntity(dto, assignment);

        EquipmentAssignment saved = assignmentRepository.save(assignment);
        log.info("Assignment updated id: {}", saved.getId());

        return assignmentMapper.toResponseDto(saved);
    }

    @Override
    public EquipmentAssignmentResponseDto returnEquipment(Long id) {
        log.info("Returning equipment for assignment id: {}", id);

        return returnEquipment(id, LocalDateTime.now());
    }

    @Override
    public EquipmentAssignmentResponseDto returnEquipment(Long id, LocalDateTime returnDate) {
        log.info("Returning equipment for assignment id: {} at {}", id, returnDate);

        EquipmentAssignment assignment = assignmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> EquipmentAssignmentNotFoundException.byId(id));

        if (!assignment.isActive()) {
            throw new EquipmentNotReturnedException(id);
        }

        // Set return date
        assignment.setActualReturnDate(returnDate);

        // Change equipment status back to AVAILABLE
        Equipment equipment = assignment.getEquipment();
        equipment.setStatus(EquipmentStatus.AVAILABLE);
        equipmentRepository.save(equipment);

        EquipmentAssignment saved = assignmentRepository.save(assignment);
        return assignmentMapper.toResponseDto(saved);
    }

    // ════════════════════════════════════════════════════════════════════
    // Delete
    // ════════════════════════════════════════════════════════════════════

    @Override
    public void deleteAssignment(Long id) {
        log.info("Soft-deleting assignment id: {}", id);

        EquipmentAssignment assignment = assignmentRepository.findNotDeletedById(id)
                .orElseThrow(() -> EquipmentAssignmentNotFoundException.byId(id));

        // Check if equipment is still assigned
        if (assignment.isActive()) {
            throw new EquipmentAlreadyAssignedException(id);
        }

        assignment.softDelete(null);
        assignmentRepository.save(assignment);
    }
}
