package hospital.labservice.service.impl;

import hospital.labservice.dto.labtechnician.LabTechnicianCreateDto;
import hospital.labservice.dto.labtechnician.LabTechnicianResponseDto;
import hospital.labservice.dto.labtechnician.LabTechnicianUpdateDto;
import hospital.labservice.exception.labtechnician.DuplicateLabTechnicianEmployeeCodeException;
import hospital.labservice.exception.labtechnician.DuplicateLabTechnicianUserIdException;
import hospital.labservice.exception.labtechnician.LabTechnicianNotFoundException;
import hospital.labservice.mapper.LabTechnicianMapper;
import hospital.labservice.model.LabTechnician;
import hospital.labservice.model.enums.LabShift;
import hospital.labservice.repository.LabTechnicianRepository;
import hospital.labservice.service.LabTechnicianService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link LabTechnicianService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabTechnicianServiceImpl implements LabTechnicianService {

    private final LabTechnicianRepository labTechnicianRepository;
    private final LabTechnicianMapper labTechnicianMapper;

    @Override
    public LabTechnicianResponseDto createTechnician(LabTechnicianCreateDto dto) {
        log.info("Creating lab technician: {} {}", dto.getFirstName(), dto.getLastName());

        // Validate unique user ID
        if (labTechnicianRepository.existsByUserId(dto.getUserId())) {
            throw new DuplicateLabTechnicianUserIdException(dto.getUserId());
        }

        // Validate unique employee code
        if (labTechnicianRepository.existsByEmployeeCode(dto.getEmployeeCode())) {
            throw new DuplicateLabTechnicianEmployeeCodeException(dto.getEmployeeCode());
        }

        LabTechnician technician = labTechnicianMapper.toEntity(dto);
        LabTechnician saved = labTechnicianRepository.save(technician);
        log.info("Lab technician created with id: {}", saved.getId());

        return labTechnicianMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LabTechnicianResponseDto getTechnicianById(Long id) {
        log.debug("Fetching lab technician by id: {}", id);

        LabTechnician technician = labTechnicianRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabTechnicianNotFoundException.byId(id));

        return labTechnicianMapper.toResponseDto(technician);
    }

    @Override
    @Transactional(readOnly = true)
    public LabTechnicianResponseDto getTechnicianByUserId(Long userId) {
        log.debug("Fetching lab technician by user id: {}", userId);

        LabTechnician technician = labTechnicianRepository.findByUserId(userId)
                .orElseThrow(() -> LabTechnicianNotFoundException.byUserId(userId));

        return labTechnicianMapper.toResponseDto(technician);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTechnicianResponseDto> getAllTechnicians() {
        log.debug("Fetching all lab technicians");

        List<LabTechnician> technicians = labTechnicianRepository.findAllNotDeleted();
        return labTechnicianMapper.toResponseDtoList(technicians);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTechnicianResponseDto> getTechniciansByShift(LabShift shift) {
        log.debug("Fetching technicians by shift: {}", shift);

        List<LabTechnician> technicians = labTechnicianRepository.findByShift(shift);
        return labTechnicianMapper.toResponseDtoList(technicians);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTechnicianResponseDto> getActiveTechniciansByShift(LabShift shift) {
        log.debug("Fetching active technicians by shift: {}", shift);

        List<LabTechnician> technicians = labTechnicianRepository.findByShiftAndIsActive(shift, true);
        return labTechnicianMapper.toResponseDtoList(technicians);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTechnicianResponseDto> getActiveTechnicians() {
        log.debug("Fetching active technicians");

        List<LabTechnician> technicians = labTechnicianRepository.findByIsActive(true);
        return labTechnicianMapper.toResponseDtoList(technicians);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTechnicianResponseDto> getTechniciansBySpecialization(String specialization) {
        log.debug("Fetching technicians by specialization: {}", specialization);

        List<LabTechnician> technicians = labTechnicianRepository.findBySpecialization(specialization);
        return labTechnicianMapper.toResponseDtoList(technicians);
    }

    @Override
    public LabTechnicianResponseDto activateTechnician(Long id) {
        log.info("Activating technician id: {}", id);

        LabTechnician technician = labTechnicianRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabTechnicianNotFoundException.byId(id));

        technician.activate();
        LabTechnician saved = labTechnicianRepository.save(technician);

        return labTechnicianMapper.toResponseDto(saved);
    }

    @Override
    public LabTechnicianResponseDto deactivateTechnician(Long id) {
        log.info("Deactivating technician id: {}", id);

        LabTechnician technician = labTechnicianRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabTechnicianNotFoundException.byId(id));

        technician.deactivate();
        LabTechnician saved = labTechnicianRepository.save(technician);

        return labTechnicianMapper.toResponseDto(saved);
    }

    @Override
    public LabTechnicianResponseDto changeShift(Long id, LabShift newShift) {
        log.info("Changing shift for technician id: {} to: {}", id, newShift);

        LabTechnician technician = labTechnicianRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabTechnicianNotFoundException.byId(id));

        technician.changeShift(newShift);
        LabTechnician saved = labTechnicianRepository.save(technician);

        return labTechnicianMapper.toResponseDto(saved);
    }

    @Override
    public LabTechnicianResponseDto updateTechnician(Long id, LabTechnicianUpdateDto dto) {
        log.info("Updating lab technician id: {}", id);

        LabTechnician technician = labTechnicianRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabTechnicianNotFoundException.byId(id));

        labTechnicianMapper.updateEntity(dto, technician);
        LabTechnician saved = labTechnicianRepository.save(technician);
        log.info("Lab technician updated id: {}", saved.getId());

        return labTechnicianMapper.toResponseDto(saved);
    }

    @Override
    public void deleteTechnician(Long id) {
        log.info("Soft-deleting lab technician id: {}", id);

        LabTechnician technician = labTechnicianRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabTechnicianNotFoundException.byId(id));

        technician.softDelete(null);
        labTechnicianRepository.save(technician);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIdExists(Long userId) {
        return labTechnicianRepository.existsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean employeeCodeExists(String employeeCode) {
        return labTechnicianRepository.existsByEmployeeCode(employeeCode);
    }
}
