package hospital.labservice.service;

import hospital.labservice.dto.labrequest.LabRequestCreateDto;
import hospital.labservice.dto.labrequest.LabRequestResponseDto;
import hospital.labservice.dto.labrequestitem.LabRequestItemCreateDto;
import hospital.labservice.exception.labrequest.DuplicateLabRequestNumberException;
import hospital.labservice.exception.labrequest.LabRequestNotFoundException;
import hospital.labservice.mapper.LabRequestItemMapper;
import hospital.labservice.mapper.LabRequestMapper;
import hospital.labservice.model.LabRequest;
import hospital.labservice.model.LabRequestItem;
import hospital.labservice.model.LabTest;
import hospital.labservice.model.enums.RequestPriority;
import hospital.labservice.model.enums.RequestStatus;
import hospital.labservice.repository.LabRequestRepository;
import hospital.labservice.repository.LabTestRepository;
import hospital.labservice.service.impl.LabRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link LabRequestServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class LabRequestServiceImplTest {

    @Mock private LabRequestRepository labRequestRepository;
    @Mock private LabTestRepository labTestRepository;
    @Mock private LabRequestMapper labRequestMapper;
    @Mock private LabRequestItemMapper labRequestItemMapper;

    @InjectMocks
    private LabRequestServiceImpl labRequestService;

    private LabRequest testRequest;

    @BeforeEach
    void setUp() {
        testRequest = LabRequest.builder()
                .id(1L)
                .requestNumber("LAB-2026-001")
                .patientId(100L)
                .doctorId(200L)
                .priority(RequestPriority.ROUTINE)
                .status(RequestStatus.PENDING)
                .items(new ArrayList<>())
                .build();
    }

    @Nested
    @DisplayName("Create LabRequest")
    class CreateLabRequestTests {

        @Test
        @DisplayName("should create lab request with items")
        void shouldCreateLabRequest() {
            LabRequestItemCreateDto itemDto = LabRequestItemCreateDto.builder()
                    .testId(1L)
                    .testName("CBC")
                    .build();

            LabRequestCreateDto dto = LabRequestCreateDto.builder()
                    .requestNumber("LAB-2026-001")
                    .patientId(100L)
                    .doctorId(200L)
                    .items(List.of(itemDto))
                    .build();

            LabTest testLabTest = LabTest.builder().id(1L).code("CBC").name("CBC").build();
            LabRequestItem item = LabRequestItem.builder().id(1L).testName("CBC").test(testLabTest).build();

            when(labRequestRepository.existsByRequestNumber("LAB-2026-001")).thenReturn(false);
            when(labRequestMapper.toEntity(any(LabRequestCreateDto.class))).thenReturn(testRequest);
            when(labRequestRepository.save(any(LabRequest.class))).thenReturn(testRequest);
            when(labRequestItemMapper.toEntity(any(LabRequestItemCreateDto.class))).thenReturn(item);
            when(labTestRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testLabTest));
            when(labRequestMapper.toResponseDto(any(LabRequest.class)))
                    .thenReturn(LabRequestResponseDto.builder().id(1L).build());

            LabRequestResponseDto result = labRequestService.createLabRequest(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(labRequestRepository, atLeastOnce()).save(any(LabRequest.class));
        }

        @Test
        @DisplayName("should throw when request number already exists")
        void shouldThrowWhenNumberExists() {
            LabRequestCreateDto dto = LabRequestCreateDto.builder()
                    .requestNumber("LAB-2026-001")
                    .build();

            when(labRequestRepository.existsByRequestNumber("LAB-2026-001")).thenReturn(true);

            assertThatThrownBy(() -> labRequestService.createLabRequest(dto))
                    .isInstanceOf(DuplicateLabRequestNumberException.class);
        }
    }

    @Nested
    @DisplayName("Read LabRequest")
    class ReadLabRequestTests {

        @Test
        @DisplayName("should get request by id")
        void shouldGetById() {
            when(labRequestRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testRequest));
            when(labRequestMapper.toResponseDto(testRequest))
                    .thenReturn(LabRequestResponseDto.builder().id(1L).build());

            var result = labRequestService.getLabRequestById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(labRequestRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labRequestService.getLabRequestById(999L))
                    .isInstanceOf(LabRequestNotFoundException.class);
        }

        @Test
        @DisplayName("should get requests by patient")
        void shouldGetByPatient() {
            when(labRequestRepository.findByPatientId(100L)).thenReturn(List.of(testRequest));
            when(labRequestMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(LabRequestResponseDto.builder().id(1L).build()));

            assertThat(labRequestService.getRequestsByPatient(100L)).hasSize(1);
        }

        @Test
        @DisplayName("should get urgent pending requests")
        void shouldGetUrgentPending() {
            when(labRequestRepository.findUrgentPendingRequests(anyList()))
                    .thenReturn(List.of(testRequest));
            when(labRequestMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(LabRequestResponseDto.builder().id(1L).build()));

            assertThat(labRequestService.getUrgentPendingRequests()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Status Transitions")
    class StatusTransitionTests {

        @Test
        @DisplayName("should approve request")
        void shouldApprove() {
            when(labRequestRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testRequest));
            when(labRequestRepository.save(any(LabRequest.class))).thenReturn(testRequest);
            when(labRequestMapper.toResponseDto(any(LabRequest.class)))
                    .thenReturn(LabRequestResponseDto.builder().id(1L).build());

            labRequestService.approveRequest(1L);

            verify(labRequestRepository).save(argThat(r -> r.getStatus() == RequestStatus.APPROVED));
        }

        @Test
        @DisplayName("should reject request")
        void shouldReject() {
            when(labRequestRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testRequest));
            when(labRequestRepository.save(any(LabRequest.class))).thenReturn(testRequest);
            when(labRequestMapper.toResponseDto(any(LabRequest.class)))
                    .thenReturn(LabRequestResponseDto.builder().id(1L).build());

            labRequestService.rejectRequest(1L);

            verify(labRequestRepository).save(argThat(r -> r.getStatus() == RequestStatus.REJECTED));
        }

        @Test
        @DisplayName("should throw when approving non-existent request")
        void shouldThrowWhenApprovingNonExistent() {
            when(labRequestRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labRequestService.approveRequest(999L))
                    .isInstanceOf(LabRequestNotFoundException.class);
        }

        @Test
        @DisplayName("should cancel request")
        void shouldCancel() {
            when(labRequestRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testRequest));
            when(labRequestRepository.save(any(LabRequest.class))).thenReturn(testRequest);
            when(labRequestMapper.toResponseDto(any(LabRequest.class)))
                    .thenReturn(LabRequestResponseDto.builder().id(1L).build());

            labRequestService.cancelRequest(1L);

            verify(labRequestRepository).save(argThat(r -> r.getStatus() == RequestStatus.CANCELLED));
        }
    }

    @Nested
    @DisplayName("Delete LabRequest")
    class DeleteLabRequestTests {

        @Test
        @DisplayName("should soft delete request")
        void shouldSoftDelete() {
            when(labRequestRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testRequest));

            labRequestService.deleteLabRequest(1L);

            verify(labRequestRepository).save(argThat(LabRequest::isDeleted));
        }
    }
}
