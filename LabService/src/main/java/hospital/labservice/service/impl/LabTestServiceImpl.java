package hospital.labservice.service.impl;

import hospital.labservice.dto.labtest.LabTestCreateDto;
import hospital.labservice.dto.labtest.LabTestResponseDto;
import hospital.labservice.dto.labtest.LabTestUpdateDto;
import hospital.labservice.exception.labtest.DuplicateLabTestCodeException;
import hospital.labservice.exception.labtest.LabTestNotFoundException;
import hospital.labservice.mapper.LabTestMapper;
import hospital.labservice.model.LabTest;
import hospital.labservice.model.enums.TestCategory;
import hospital.labservice.repository.LabTestRepository;
import hospital.labservice.service.LabTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link LabTestService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabTestServiceImpl implements LabTestService {

    private final LabTestRepository labTestRepository;
    private final LabTestMapper labTestMapper;

    @Override
    public LabTestResponseDto createLabTest(LabTestCreateDto dto) {
        log.info("Creating lab test: {}", dto.getCode());

        if (labTestRepository.existsByCode(dto.getCode())) {
            throw new DuplicateLabTestCodeException(dto.getCode());
        }

        LabTest labTest = labTestMapper.toEntity(dto);
        LabTest saved = labTestRepository.save(labTest);
        log.info("Lab test created with id: {}", saved.getId());

        return labTestMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LabTestResponseDto getLabTestById(Long id) {
        log.debug("Fetching lab test by id: {}", id);

        LabTest labTest = labTestRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabTestNotFoundException.byId(id));

        return labTestMapper.toResponseDto(labTest);
    }

    @Override
    @Transactional(readOnly = true)
    public LabTestResponseDto getLabTestByCode(String code) {
        log.debug("Fetching lab test by code: {}", code);

        LabTest labTest = labTestRepository.findByCode(code)
                .orElseThrow(() -> LabTestNotFoundException.byCode(code));

        return labTestMapper.toResponseDto(labTest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTestResponseDto> getAllLabTests() {
        log.debug("Fetching all lab tests");

        List<LabTest> tests = labTestRepository.findAllNotDeleted();
        return labTestMapper.toResponseDtoList(tests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTestResponseDto> getLabTestsByCategory(TestCategory category) {
        log.debug("Fetching lab tests by category: {}", category);

        List<LabTest> tests = labTestRepository.findByCategory(category);
        return labTestMapper.toResponseDtoList(tests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTestResponseDto> getActiveLabTests() {
        log.debug("Fetching active lab tests");

        List<LabTest> tests = labTestRepository.findByIsActive(true);
        return labTestMapper.toResponseDtoList(tests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTestResponseDto> searchByName(String name) {
        log.debug("Searching lab tests by name: {}", name);

        List<LabTest> tests = labTestRepository.findByNameContainingIgnoreCase(name);
        return labTestMapper.toResponseDtoList(tests);
    }

    @Override
    public LabTestResponseDto updateLabTest(Long id, LabTestUpdateDto dto) {
        log.info("Updating lab test id: {}", id);

        LabTest labTest = labTestRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabTestNotFoundException.byId(id));

        labTestMapper.updateEntity(dto, labTest);
        LabTest saved = labTestRepository.save(labTest);
        log.info("Lab test updated id: {}", saved.getId());

        return labTestMapper.toResponseDto(saved);
    }

    @Override
    public void deleteLabTest(Long id) {
        log.info("Soft-deleting lab test id: {}", id);

        LabTest labTest = labTestRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabTestNotFoundException.byId(id));

        labTest.softDelete(null);
        labTestRepository.save(labTest);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean codeExists(String code) {
        return labTestRepository.existsByCode(code);
    }
}
