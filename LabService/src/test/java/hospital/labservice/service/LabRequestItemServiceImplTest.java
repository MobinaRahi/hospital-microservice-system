package hospital.labservice.service;

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
import hospital.labservice.service.impl.LabRequestItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link LabRequestItemServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class LabRequestItemServiceImplTest {

    @Mock private LabRequestItemRepository labRequestItemRepository;
    @Mock private LabRequestRepository labRequestRepository;
    @Mock private LabTestRepository labTestRepository;
    @Mock private LabRequestItemMapper labRequestItemMapper;

    @InjectMocks
    private LabRequestItemServiceImpl labRequestItemService;

    private LabRequestItem testItem;
    private LabRequest testRequest;
    private LabTest testLabTest;

    @BeforeEach
    void setUp() {
        testLabTest = LabTest.builder().id(1L).code("CBC").name("CBC").build();
        testRequest = LabRequest.builder().id(1L).requestNumber("LAB-001").build();
        testItem = LabRequestItem.builder()
                .id(1L)
                .testName("CBC")
                .test(testLabTest)
                .labRequest(testRequest)
                .status(RequestItemStatus.PENDING)
                .build();
    }

    @Nested
    @DisplayName("Create Item")
    class CreateItemTests {

        @Test
        @DisplayName("should create item in a request")
        void shouldCreateItem() {
            LabRequestItemCreateDto dto = LabRequestItemCreateDto.builder()
                    .testId(1L)
                    .testName("CBC")
                    .build();

            when(labRequestRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testRequest));
            when(labTestRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testLabTest));
            when(labRequestItemMapper.toEntity(any())).thenReturn(testItem);
            when(labRequestItemRepository.save(any())).thenReturn(testItem);
            when(labRequestItemMapper.toResponseDto(any()))
                    .thenReturn(LabRequestItemResponseDto.builder().id(1L).build());

            var result = labRequestItemService.createItem(1L, dto);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when request not found")
        void shouldThrowWhenRequestNotFound() {
            when(labRequestRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labRequestItemService.createItem(999L, new LabRequestItemCreateDto()))
                    .isInstanceOf(LabRequestNotFoundException.class);
        }

        @Test
        @DisplayName("should throw when test not found")
        void shouldThrowWhenTestNotFound() {
            LabRequestItemCreateDto dto = LabRequestItemCreateDto.builder().testId(999L).testName("X").build();

            when(labRequestRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testRequest));
            when(labTestRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labRequestItemService.createItem(1L, dto))
                    .isInstanceOf(LabTestNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Status Transitions")
    class StatusTransitionTests {

        @Test
        @DisplayName("should start processing")
        void shouldStartProcessing() {
            when(labRequestItemRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testItem));
            when(labRequestItemRepository.save(any())).thenReturn(testItem);
            when(labRequestItemMapper.toResponseDto(any()))
                    .thenReturn(LabRequestItemResponseDto.builder().id(1L).build());

            labRequestItemService.startProcessing(1L);

            verify(labRequestItemRepository).save(argThat(i -> i.getStatus() == RequestItemStatus.PROCESSING));
        }

        @Test
        @DisplayName("should complete item")
        void shouldCompleteItem() {
            testItem.setStatus(RequestItemStatus.PROCESSING);
            when(labRequestItemRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testItem));
            when(labRequestItemRepository.save(any())).thenReturn(testItem);
            when(labRequestItemMapper.toResponseDto(any()))
                    .thenReturn(LabRequestItemResponseDto.builder().id(1L).build());

            labRequestItemService.completeItem(1L);

            verify(labRequestItemRepository).save(argThat(i -> i.getStatus() == RequestItemStatus.COMPLETED));
        }

        @Test
        @DisplayName("should cancel item")
        void shouldCancelItem() {
            when(labRequestItemRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testItem));
            when(labRequestItemRepository.save(any())).thenReturn(testItem);
            when(labRequestItemMapper.toResponseDto(any()))
                    .thenReturn(LabRequestItemResponseDto.builder().id(1L).build());

            labRequestItemService.cancelItem(1L);

            verify(labRequestItemRepository).save(argThat(i -> i.getStatus() == RequestItemStatus.CANCELLED));
        }

        @Test
        @DisplayName("should throw when item not found")
        void shouldThrowWhenNotFound() {
            when(labRequestItemRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labRequestItemService.startProcessing(999L))
                    .isInstanceOf(LabRequestItemNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Read Items")
    class ReadItemsTests {

        @Test
        @DisplayName("should get items by request")
        void shouldGetByRequest() {
            when(labRequestItemRepository.findByLabRequestId(1L)).thenReturn(List.of(testItem));
            when(labRequestItemMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(LabRequestItemResponseDto.builder().id(1L).build()));

            assertThat(labRequestItemService.getItemsByRequest(1L)).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Delete Item")
    class DeleteItemTests {

        @Test
        @DisplayName("should soft delete item")
        void shouldSoftDelete() {
            when(labRequestItemRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testItem));

            labRequestItemService.deleteItem(1L);

            verify(labRequestItemRepository).save(argThat(LabRequestItem::isDeleted));
        }
    }
}
