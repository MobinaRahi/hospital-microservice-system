package hospital.adminservice.service.impl;

import hospital.adminservice.dto.hospital.HospitalCreateDto;
import hospital.adminservice.dto.hospital.HospitalResponseDto;
import hospital.adminservice.dto.hospital.HospitalUpdateDto;
import hospital.adminservice.exception.hospital.DuplicateHospitalCodeException;
import hospital.adminservice.exception.hospital.HospitalNotFoundException;
import hospital.adminservice.mapper.HospitalMapper;
import hospital.adminservice.model.Hospital;
import hospital.adminservice.repository.HospitalRepository;
import hospital.adminservice.service.HospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;

    @Override
    public HospitalResponseDto createHospital(HospitalCreateDto dto) {
        log.info("Creating hospital: {}", dto.getName());

        if (hospitalRepository.existsByCode(dto.getCode())) {
            throw new DuplicateHospitalCodeException(dto.getCode());
        }

        Hospital hospital = hospitalMapper.toEntity(dto);
        Hospital saved = hospitalRepository.save(hospital);
        log.info("Hospital created with id: {}", saved.getId());

        return hospitalMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public HospitalResponseDto getHospitalById(Long id) {
        log.debug("Fetching hospital by id: {}", id);

        Hospital hospital = hospitalRepository.findNotDeletedById(id)
                .orElseThrow(() -> HospitalNotFoundException.byId(id));

        return hospitalMapper.toResponseDto(hospital);
    }

    @Override
    @Transactional(readOnly = true)
    public HospitalResponseDto getHospitalByCode(String code) {
        log.debug("Fetching hospital by code: {}", code);

        Hospital hospital = hospitalRepository.findByCode(code)
                .orElseThrow(() -> HospitalNotFoundException.byCode(code));

        return hospitalMapper.toResponseDto(hospital);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HospitalResponseDto> getAllHospitals() {
        log.debug("Fetching all hospitals");

        List<Hospital> hospitals = hospitalRepository.findAllNotDeleted();
        return hospitalMapper.toResponseDtoList(hospitals);
    }

    @Override
    public HospitalResponseDto updateHospital(Long id, HospitalUpdateDto dto) {
        log.info("Updating hospital id: {}", id);

        Hospital hospital = hospitalRepository.findNotDeletedById(id)
                .orElseThrow(() -> HospitalNotFoundException.byId(id));

        hospitalMapper.updateEntity(dto, hospital);
        Hospital saved = hospitalRepository.save(hospital);
        log.info("Hospital updated id: {}", saved.getId());

        return hospitalMapper.toResponseDto(saved);
    }

    @Override
    public void deleteHospital(Long id) {
        log.info("Soft-deleting hospital id: {}", id);

        Hospital hospital = hospitalRepository.findNotDeletedById(id)
                .orElseThrow(() -> HospitalNotFoundException.byId(id));

        hospital.softDelete(null);
        hospitalRepository.save(hospital);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean codeExists(String code) {
        return hospitalRepository.existsByCode(code);
    }
}
