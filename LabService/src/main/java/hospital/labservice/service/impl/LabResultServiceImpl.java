package hospital.labservice.service.impl;

import hospital.labservice.dto.labresult.LabResultCreateDto;
import hospital.labservice.dto.labresult.LabResultResponseDto;
import hospital.labservice.exception.labrequestitem.LabRequestItemNotFoundException;
import hospital.labservice.exception.labresult.LabResultNotFoundException;
import hospital.labservice.mapper.LabResultMapper;
import hospital.labservice.model.LabRequestItem;
import hospital.labservice.model.LabResult;
import hospital.labservice.model.enums.ResultFlag;
import hospital.labservice.repository.LabRequestItemRepository;
import hospital.labservice.repository.LabResultRepository;
import hospital.labservice.service.LabResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link LabResultService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabResultServiceImpl implements LabResultService {

    private final LabResultRepository labResultRepository;
    private final LabRequestItemRepository labRequestItemRepository;
    private final LabResultMapper labResultMapper;

    @Override
    public LabResultResponseDto createResult(LabResultCreateDto dto) {
        log.info("Creating lab result for request item: {}", dto.getRequestItemId());

        LabResult result = labResultMapper.toEntity(dto);

        // Resolve request item entity
        LabRequestItem item = labRequestItemRepository.findNotDeletedById(dto.getRequestItemId())
                .orElseThrow(() -> LabRequestItemNotFoundException.byId(dto.getRequestItemId()));
        result.setRequestItem(item);

        LabResult saved = labResultRepository.save(result);
        log.info("Lab result created with id: {}", saved.getId());

        return labResultMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LabResultResponseDto getResultById(Long id) {
        log.debug("Fetching lab result by id: {}", id);

        LabResult result = labResultRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabResultNotFoundException.byId(id));

        return labResultMapper.toResponseDto(result);
    }

    @Override
    @Transactional(readOnly = true)
    public LabResultResponseDto getResultByRequestItem(Long requestItemId) {
        log.debug("Fetching lab result for request item: {}", requestItemId);

        LabResult result = labResultRepository.findByRequestItemId(requestItemId)
                .orElseThrow(() -> LabResultNotFoundException.byRequestItemId(requestItemId));

        return labResultMapper.toResponseDto(result);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResultResponseDto> getResultsByFlag(ResultFlag flag) {
        log.debug("Fetching results by flag: {}", flag);

        List<LabResult> results = labResultRepository.findByFlag(flag);
        return labResultMapper.toResponseDtoList(results);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResultResponseDto> getUnverifiedResults() {
        log.debug("Fetching unverified results");

        List<LabResult> results = labResultRepository.findUnverifiedResults();
        return labResultMapper.toResponseDtoList(results);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResultResponseDto> getCriticalResults() {
        log.debug("Fetching critical results");

        List<LabResult> results = labResultRepository.findCriticalResults();
        return labResultMapper.toResponseDtoList(results);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResultResponseDto> getAbnormalResults() {
        log.debug("Fetching abnormal results");

        List<LabResult> results = labResultRepository.findAbnormalResults();
        return labResultMapper.toResponseDtoList(results);
    }

    @Override
    public LabResultResponseDto verifyResult(Long id, Long verifiedBy) {
        log.info("Verifying lab result id: {} by user: {}", id, verifiedBy);

        LabResult result = labResultRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabResultNotFoundException.byId(id));

        result.verify(verifiedBy);
        LabResult saved = labResultRepository.save(result);
        log.info("Lab result {} verified", id);

        return labResultMapper.toResponseDto(saved);
    }

    @Override
    public void deleteResult(Long id) {
        log.info("Soft-deleting lab result id: {}", id);

        LabResult result = labResultRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabResultNotFoundException.byId(id));

        result.softDelete(null);
        labResultRepository.save(result);
    }
}
