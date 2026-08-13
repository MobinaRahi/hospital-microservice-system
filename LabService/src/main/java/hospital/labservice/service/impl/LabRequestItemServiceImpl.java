package hospital.labservice.service.impl;

import hospital.labservice.dto.labrequestitem.LabRequestItemCreateDto;
import hospital.labservice.dto.labrequestitem.LabRequestItemResponseDto;
import hospital.labservice.exception.labrequest.LabRequestNotFoundException;
import hospital.labservice.exception.labrequestitem.LabRequestItemNotFoundException;
import hospital.labservice.exception.labtest.LabTestNotFoundException;
import hospital.labservice.mapper.LabRequestItemMapper;
import hospital.labservice.model.LabRequest;
import hospital.labservice.model.LabRequestItem;
import hospital.labservice.model.LabTest;
import hospital.labservice.model.enums.RequestItemStatus;
import hospital.labservice.repository.LabRequestItemRepository;
import hospital.labservice.repository.LabRequestRepository;
import hospital.labservice.repository.LabTestRepository;
import hospital.labservice.service.LabRequestItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link LabRequestItemService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabRequestItemServiceImpl implements LabRequestItemService {

    private final LabRequestItemRepository labRequestItemRepository;
    private final LabRequestRepository labRequestRepository;
    private final LabTestRepository labTestRepository;
    private final LabRequestItemMapper labRequestItemMapper;

    @Override
    public LabRequestItemResponseDto createItem(Long requestId, LabRequestItemCreateDto dto) {
        log.info("Creating lab request item for request: {}", requestId);

        LabRequest request = labRequestRepository.findNotDeletedById(requestId)
                .orElseThrow(() -> LabRequestNotFoundException.byId(requestId));

        LabTest test = labTestRepository.findNotDeletedById(dto.getTestId())
                .orElseThrow(() -> LabTestNotFoundException.byId(dto.getTestId()));

        LabRequestItem item = labRequestItemMapper.toEntity(dto);
        item.setLabRequest(request);
        item.setTest(test);

        LabRequestItem saved = labRequestItemRepository.save(item);
        log.info("Lab request item created with id: {}", saved.getId());

        return labRequestItemMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LabRequestItemResponseDto getItemById(Long id) {
        log.debug("Fetching lab request item by id: {}", id);

        LabRequestItem item = labRequestItemRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestItemNotFoundException.byId(id));

        return labRequestItemMapper.toResponseDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabRequestItemResponseDto> getItemsByRequest(Long requestId) {
        log.debug("Fetching items for request: {}", requestId);

        List<LabRequestItem> items = labRequestItemRepository.findByLabRequestId(requestId);
        return labRequestItemMapper.toResponseDtoList(items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabRequestItemResponseDto> getItemsByStatus(RequestItemStatus status) {
        log.debug("Fetching items by status: {}", status);

        List<LabRequestItem> items = labRequestItemRepository.findByStatus(status);
        return labRequestItemMapper.toResponseDtoList(items);
    }

    @Override
    public LabRequestItemResponseDto startProcessing(Long id) {
        log.info("Starting processing for item id: {}", id);

        LabRequestItem item = labRequestItemRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestItemNotFoundException.byId(id));

        item.startProcessing();
        LabRequestItem saved = labRequestItemRepository.save(item);

        return labRequestItemMapper.toResponseDto(saved);
    }

    @Override
    public LabRequestItemResponseDto completeItem(Long id) {
        log.info("Completing item id: {}", id);

        LabRequestItem item = labRequestItemRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestItemNotFoundException.byId(id));

        item.complete();
        LabRequestItem saved = labRequestItemRepository.save(item);

        return labRequestItemMapper.toResponseDto(saved);
    }

    @Override
    public LabRequestItemResponseDto cancelItem(Long id) {
        log.info("Cancelling item id: {}", id);

        LabRequestItem item = labRequestItemRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestItemNotFoundException.byId(id));

        item.cancel();
        LabRequestItem saved = labRequestItemRepository.save(item);

        return labRequestItemMapper.toResponseDto(saved);
    }

    @Override
    public void deleteItem(Long id) {
        log.info("Soft-deleting lab request item id: {}", id);

        LabRequestItem item = labRequestItemRepository.findNotDeletedById(id)
                .orElseThrow(() -> LabRequestItemNotFoundException.byId(id));

        item.softDelete(null);
        labRequestItemRepository.save(item);
    }
}
