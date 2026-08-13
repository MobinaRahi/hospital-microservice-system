package hospital.labservice.service.impl;

import hospital.labservice.dto.labrequest.LabRequestCreateDto;
import hospital.labservice.dto.labrequest.LabRequestResponseDto;
import hospital.labservice.dto.labrequest.LabRequestUpdateDto;
import hospital.labservice.dto.labrequestitem.LabRequestItemCreateDto;
import hospital.labservice.exception.labrequest.DuplicateLabRequestNumberException;
import hospital.labservice.exception.labrequest.LabRequestNotFoundException;
import hospital.labservice.exception.labtest.LabTestNotFoundException;
import hospital.labservice.mapper.LabRequestItemMapper;
import hospital.labservice.mapper.LabRequestMapper;
import hospital.labservice.model.LabRequest;
import hospital.labservice.model.LabRequestItem;
import hospital.labservice.model.LabTest;
import hospital.labservice.model.enums.RequestStatus;
import hospital.labservice.repository.LabRequestRepository;
import hospital.labservice.repository.LabTestRepository;
import hospital.labservice.service.LabRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link LabRequestService}.
 *
 * <p>Handles the complete lab request lifecycle including status transitions.</p>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabRequestServiceImpl implements LabRequestService {

    private final LabRequestRepository labRequestRepository;
    private final LabTestRepository labTestRepository;
    private final LabRequestMapper labRequestMapper;
    private final LabRequestItemMapper labRequestItemMapper;

    @Override
    public LabRequestResponseDto createLabRequest(LabRequestCreateDto dto) {
        log.info("Creating lab request: {}", dto.getRequestNumber());

        if (labRequestRepository.existsByRequestNumber(dto.getRequestNumber())) {
            throw new DuplicateLabRequestNumberException(dto.getRequestNumber());
        }

        // Map to entity
        LabRequest request = labRequestMapper.toEntity(dto);
        request.setStatus(RequestStatus.PENDING);
        if (request.getRequestDate() == null) {
            request.setRequestDate(LocalDateTime.now());
        }

        // Save request first
        LabRequest saved = labRequestRepository.save(request);

        // Create items and link to test entities
        for (LabRequestItemCreateDto itemDto : dto.getItems()) {
            LabRequestItem item = labRequestItemMapper.toEntity(itemDto);

            // Resolve test entity
            LabTest test = labTestRepository.findNotDeletedById(itemDto.getTestId())
                    .orElseThrow(() -> LabTestNotFoundException.byId(itemDto.getTestId()));
            item.setTest(test);
            item.setLabRequest(saved);

            saved.addItem(item);
        }

        saved = labRequestRepository.save(saved);
        log.info("Lab request created with id: {}", saved.getId());

        return labRequestMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LabRequestResponseDto getLabRequestById(Long id) {
        log.debug("Fetching lab request by id: {}", id);

        LabRequest request = labRequestRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestNotFoundException.byId(id));

        return labRequestMapper.toResponseDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public LabRequestResponseDto getLabRequestByNumber(String requestNumber) {
        log.debug("Fetching lab request by number: {}", requestNumber);

        LabRequest request = labRequestRepository.findByRequestNumber(requestNumber)
                .orElseThrow(() -> LabRequestNotFoundException.byNumber(requestNumber));

        return labRequestMapper.toResponseDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabRequestResponseDto> getAllLabRequests() {
        log.debug("Fetching all lab requests");

        List<LabRequest> requests = labRequestRepository.findAllNotDeleted();
        return labRequestMapper.toResponseDtoList(requests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabRequestResponseDto> getRequestsByPatient(Long patientId) {
        log.debug("Fetching requests for patient: {}", patientId);

        List<LabRequest> requests = labRequestRepository.findByPatientId(patientId);
        return labRequestMapper.toResponseDtoList(requests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabRequestResponseDto> getRequestsByDoctor(Long doctorId) {
        log.debug("Fetching requests for doctor: {}", doctorId);

        List<LabRequest> requests = labRequestRepository.findByDoctorId(doctorId);
        return labRequestMapper.toResponseDtoList(requests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabRequestResponseDto> getRequestsByStatus(RequestStatus status) {
        log.debug("Fetching requests by status: {}", status);

        List<LabRequest> requests = labRequestRepository.findByStatus(status);
        return labRequestMapper.toResponseDtoList(requests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabRequestResponseDto> getUrgentPendingRequests() {
        log.debug("Fetching urgent pending requests");

        List<RequestStatus> pendingStatuses = List.of(RequestStatus.PENDING, RequestStatus.APPROVED);
        List<LabRequest> requests = labRequestRepository.findUrgentPendingRequests(pendingStatuses);
        return labRequestMapper.toResponseDtoList(requests);
    }

    @Override
    public LabRequestResponseDto updateLabRequest(Long id, LabRequestUpdateDto dto) {
        log.info("Updating lab request id: {}", id);

        LabRequest request = labRequestRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestNotFoundException.byId(id));

        labRequestMapper.updateEntity(dto, request);
        LabRequest saved = labRequestRepository.save(request);
        log.info("Lab request updated id: {}", saved.getId());

        return labRequestMapper.toResponseDto(saved);
    }

    @Override
    public LabRequestResponseDto approveRequest(Long id) {
        log.info("Approving lab request id: {}", id);

        LabRequest request = labRequestRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestNotFoundException.byId(id));

        // Use current user ID from security context (simplified)
        request.approve(null);
        LabRequest saved = labRequestRepository.save(request);
        log.info("Lab request {} approved", id);

        return labRequestMapper.toResponseDto(saved);
    }

    @Override
    public LabRequestResponseDto rejectRequest(Long id) {
        log.info("Rejecting lab request id: {}", id);

        LabRequest request = labRequestRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestNotFoundException.byId(id));

        request.reject();
        LabRequest saved = labRequestRepository.save(request);
        log.info("Lab request {} rejected", id);

        return labRequestMapper.toResponseDto(saved);
    }

    @Override
    public LabRequestResponseDto markSampleCollected(Long id) {
        log.info("Marking sample collected for request id: {}", id);

        LabRequest request = labRequestRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestNotFoundException.byId(id));

        request.markSampleCollected();
        LabRequest saved = labRequestRepository.save(request);

        return labRequestMapper.toResponseDto(saved);
    }

    @Override
    public LabRequestResponseDto startProcessing(Long id) {
        log.info("Starting processing for request id: {}", id);

        LabRequest request = labRequestRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestNotFoundException.byId(id));

        request.startProcessing();
        LabRequest saved = labRequestRepository.save(request);

        return labRequestMapper.toResponseDto(saved);
    }

    @Override
    public LabRequestResponseDto completeRequest(Long id) {
        log.info("Completing lab request id: {}", id);

        LabRequest request = labRequestRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestNotFoundException.byId(id));

        request.complete();
        LabRequest saved = labRequestRepository.save(request);

        return labRequestMapper.toResponseDto(saved);
    }

    @Override
    public LabRequestResponseDto cancelRequest(Long id) {
        log.info("Cancelling lab request id: {}", id);

        LabRequest request = labRequestRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestNotFoundException.byId(id));

        request.cancel();
        LabRequest saved = labRequestRepository.save(request);
        log.info("Lab request {} cancelled", id);

        return labRequestMapper.toResponseDto(saved);
    }

    @Override
    public void deleteLabRequest(Long id) {
        log.info("Soft-deleting lab request id: {}", id);

        LabRequest request = labRequestRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestNotFoundException.byId(id));

        request.softDelete(null);
        labRequestRepository.save(request);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean requestNumberExists(String requestNumber) {
        return labRequestRepository.existsByRequestNumber(requestNumber);
    }
}
