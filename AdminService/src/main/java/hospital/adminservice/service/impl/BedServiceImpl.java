package hospital.adminservice.service.impl;

import hospital.adminservice.dto.bed.BedCreateDto;
import hospital.adminservice.dto.bed.BedResponseDto;
import hospital.adminservice.dto.bed.BedUpdateDto;
import hospital.adminservice.exception.bed.BedAlreadyOccupiedException;
import hospital.adminservice.exception.bed.BedNotAvailableException;
import hospital.adminservice.exception.bed.BedNotFoundException;
import hospital.adminservice.mapper.BedMapper;
import hospital.adminservice.model.Bed;
import hospital.adminservice.model.enums.BedStatus;
import hospital.adminservice.model.enums.BedType;
import hospital.adminservice.repository.BedRepository;
import hospital.adminservice.service.BedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BedServiceImpl implements BedService {

    private final BedRepository bedRepository;
    private final BedMapper bedMapper;

    @Override
    public BedResponseDto createBed(BedCreateDto dto) {
        log.info("Creating bed: {}", dto.getBedNumber());

        Bed bed = bedMapper.toEntity(dto);
        Bed saved = bedRepository.save(bed);
        log.info("Bed created with id: {}", saved.getId());

        return bedMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BedResponseDto getBedById(Long id) {
        log.debug("Fetching bed by id: {}", id);

        Bed bed = bedRepository.findNotDeletedById(id)
                .orElseThrow(() -> BedNotFoundException.byId(id));

        return bedMapper.toResponseDto(bed);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BedResponseDto> getAllBeds() {
        log.debug("Fetching all beds");

        List<Bed> beds = bedRepository.findAllNotDeleted();
        return bedMapper.toResponseDtoList(beds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BedResponseDto> getBedsByDepartment(Long departmentId) {
        log.debug("Fetching beds by department: {}", departmentId);

        List<Bed> beds = bedRepository.findByDepartmentId(departmentId);
        return bedMapper.toResponseDtoList(beds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BedResponseDto> getBedsByStatus(BedStatus status) {
        log.debug("Fetching beds by status: {}", status);

        List<Bed> beds = bedRepository.findByStatus(status);
        return bedMapper.toResponseDtoList(beds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BedResponseDto> getBedsByType(BedType type) {
        log.debug("Fetching beds by type: {}", type);

        List<Bed> beds = bedRepository.findByType(type);
        return bedMapper.toResponseDtoList(beds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BedResponseDto> getAvailableBedsByDepartment(Long departmentId) {
        log.debug("Fetching available beds in department: {}", departmentId);

        List<Bed> beds = bedRepository.findByDepartmentIdAndStatus(departmentId, BedStatus.AVAILABLE);
        return bedMapper.toResponseDtoList(beds);
    }

    @Override
    public BedResponseDto assignToPatient(Long id, Long patientId, Long admissionId) {
        log.info("Assigning bed {} to patient {}", id, patientId);

        Bed bed = bedRepository.findNotDeletedById(id)
                .orElseThrow(() -> BedNotFoundException.byId(id));

        if (bed.getStatus() != BedStatus.AVAILABLE) {
            throw new BedNotAvailableException(id, bed.getStatus().name());
        }

        bed.assignToPatient(patientId, admissionId);
        Bed saved = bedRepository.save(bed);
        log.info("Bed {} assigned to patient {}", id, patientId);

        return bedMapper.toResponseDto(saved);
    }

    @Override
    public BedResponseDto dischargePatient(Long id) {
        log.info("Discharging patient from bed {}", id);

        Bed bed = bedRepository.findNotDeletedById(id)
                .orElseThrow(() -> BedNotFoundException.byId(id));

        if (bed.getStatus() != BedStatus.OCCUPIED) {
            throw new BedAlreadyOccupiedException(id);
        }

        bed.dischargePatient();
        Bed saved = bedRepository.save(bed);
        log.info("Patient discharged from bed {}", id);

        return bedMapper.toResponseDto(saved);
    }

    @Override
    public BedResponseDto updateBed(Long id, BedUpdateDto dto) {
        log.info("Updating bed id: {}", id);

        Bed bed = bedRepository.findNotDeletedById(id)
                .orElseThrow(() -> BedNotFoundException.byId(id));

        bedMapper.updateEntity(dto, bed);
        Bed saved = bedRepository.save(bed);
        log.info("Bed updated id: {}", saved.getId());

        return bedMapper.toResponseDto(saved);
    }

    @Override
    public void deleteBed(Long id) {
        log.info("Soft-deleting bed id: {}", id);

        Bed bed = bedRepository.findNotDeletedById(id)
                .orElseThrow(() -> BedNotFoundException.byId(id));

        bed.softDelete(null);
        bedRepository.save(bed);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAvailableBeds() {
        return bedRepository.countAvailableBeds();
    }

    @Override
    @Transactional(readOnly = true)
    public long countOccupiedBeds() {
        return bedRepository.countOccupiedBeds();
    }
}
