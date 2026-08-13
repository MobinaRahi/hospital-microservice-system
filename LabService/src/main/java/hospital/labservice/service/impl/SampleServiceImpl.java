package hospital.labservice.service.impl;

import hospital.labservice.dto.sample.SampleCreateDto;
import hospital.labservice.dto.sample.SampleResponseDto;
import hospital.labservice.dto.sample.SampleUpdateDto;
import hospital.labservice.exception.labrequest.LabRequestNotFoundException;
import hospital.labservice.exception.sample.DuplicateSampleNumberException;
import hospital.labservice.exception.sample.SampleNotFoundException;
import hospital.labservice.mapper.SampleMapper;
import hospital.labservice.model.LabRequest;
import hospital.labservice.model.Sample;
import hospital.labservice.model.enums.SampleQuality;
import hospital.labservice.model.enums.SampleType;
import hospital.labservice.repository.LabRequestRepository;
import hospital.labservice.repository.SampleRepository;
import hospital.labservice.service.SampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link SampleService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SampleServiceImpl implements SampleService {

    private final SampleRepository sampleRepository;
    private final LabRequestRepository labRequestRepository;
    private final SampleMapper sampleMapper;

    @Override
    public SampleResponseDto createSample(SampleCreateDto dto) {
        log.info("Creating sample: {}", dto.getSampleNumber());

        if (sampleRepository.existsBySampleNumber(dto.getSampleNumber())) {
            throw new DuplicateSampleNumberException(dto.getSampleNumber());
        }

        Sample sample = sampleMapper.toEntity(dto);

        // Resolve lab request entity
        LabRequest request = labRequestRepository.findNotDeletedById(dto.getLabRequestId())
                .orElseThrow(() -> LabRequestNotFoundException.byId(dto.getLabRequestId()));
        sample.setLabRequest(request);

        Sample saved = sampleRepository.save(sample);
        log.info("Sample created with id: {}", saved.getId());

        return sampleMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SampleResponseDto getSampleById(Long id) {
        log.debug("Fetching sample by id: {}", id);

        Sample sample = sampleRepository.findNotDeletedById(id)
                .orElseThrow(() -> SampleNotFoundException.byId(id));

        return sampleMapper.toResponseDto(sample);
    }

    @Override
    @Transactional(readOnly = true)
    public SampleResponseDto getSampleByNumber(String sampleNumber) {
        log.debug("Fetching sample by number: {}", sampleNumber);

        Sample sample = sampleRepository.findBySampleNumber(sampleNumber)
                .orElseThrow(() -> SampleNotFoundException.byNumber(sampleNumber));

        return sampleMapper.toResponseDto(sample);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SampleResponseDto> getSamplesByRequest(Long requestId) {
        log.debug("Fetching samples for request: {}", requestId);

        List<Sample> samples = sampleRepository.findByLabRequestId(requestId);
        return sampleMapper.toResponseDtoList(samples);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SampleResponseDto> getSamplesByType(SampleType sampleType) {
        log.debug("Fetching samples by type: {}", sampleType);

        List<Sample> samples = sampleRepository.findBySampleType(sampleType);
        return sampleMapper.toResponseDtoList(samples);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SampleResponseDto> getSamplesWithQualityIssues() {
        log.debug("Fetching samples with quality issues");

        List<Sample> samples = sampleRepository.findSamplesWithQualityIssues();
        return sampleMapper.toResponseDtoList(samples);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SampleResponseDto> getUnreceivedSamples() {
        log.debug("Fetching unreceived samples");

        List<Sample> samples = sampleRepository.findUnreceivedSamples();
        return sampleMapper.toResponseDtoList(samples);
    }

    @Override
    public SampleResponseDto receiveSample(Long id, Long receivedBy) {
        log.info("Receiving sample id: {} by user: {}", id, receivedBy);

        Sample sample = sampleRepository.findNotDeletedById(id)
                .orElseThrow(() -> SampleNotFoundException.byId(id));

        sample.markReceived(receivedBy);
        Sample saved = sampleRepository.save(sample);
        log.info("Sample {} received at lab", id);

        return sampleMapper.toResponseDto(saved);
    }

    @Override
    public SampleResponseDto updateSampleQuality(Long id, SampleQuality quality) {
        log.info("Updating quality for sample id: {} to: {}", id, quality);

        Sample sample = sampleRepository.findNotDeletedById(id)
                .orElseThrow(() -> SampleNotFoundException.byId(id));

        sample.setQuality(quality);
        Sample saved = sampleRepository.save(sample);

        return sampleMapper.toResponseDto(saved);
    }

    @Override
    public SampleResponseDto updateSample(Long id, SampleUpdateDto dto) {
        log.info("Updating sample id: {}", id);

        Sample sample = sampleRepository.findNotDeletedById(id)
                .orElseThrow(() -> SampleNotFoundException.byId(id));

        sampleMapper.updateEntity(dto, sample);
        Sample saved = sampleRepository.save(sample);

        return sampleMapper.toResponseDto(saved);
    }

    @Override
    public void deleteSample(Long id) {
        log.info("Soft-deleting sample id: {}", id);

        Sample sample = sampleRepository.findNotDeletedById(id)
                .orElseThrow(() -> SampleNotFoundException.byId(id));

        sample.softDelete(null);
        sampleRepository.save(sample);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean sampleNumberExists(String sampleNumber) {
        return sampleRepository.existsBySampleNumber(sampleNumber);
    }
}
