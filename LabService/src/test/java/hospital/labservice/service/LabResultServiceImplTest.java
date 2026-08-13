package hospital.labservice.service;

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
import hospital.labservice.service.impl.LabResultServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link LabResultServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class LabResultServiceImplTest {

    @Mock private LabResultRepository labResultRepository;
    @Mock private LabRequestItemRepository labRequestItemRepository;
    @Mock private LabResultMapper labResultMapper;

    @InjectMocks
    private LabResultServiceImpl labResultService;

    private LabResult testResult;
    private LabRequestItem testItem;

    @BeforeEach
    void setUp() {
        testItem = LabRequestItem.builder().id(1L).testName("CBC").build();
        testResult = LabResult.builder()
                .id(1L)
                .value("14.5")
                .normalRange("12.0-16.0")
                .flag(ResultFlag.NORMAL)
                .unit("g/dL")
                .performedAt(LocalDateTime.now())
                .requestItem(testItem)
                .build();
    }

    @Nested
    @DisplayName("Create Result")
    class CreateResultTests {

        @Test
        @DisplayName("should create result successfully")
        void shouldCreateResult() {
            LabResultCreateDto dto = LabResultCreateDto.builder()
                    .requestItemId(1L)
                    .value("14.5")
                    .performedAt(LocalDateTime.now())
                    .build();

            when(labResultMapper.toEntity(any())).thenReturn(testResult);
            when(labRequestItemRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testItem));
            when(labResultRepository.save(any())).thenReturn(testResult);
            when(labResultMapper.toResponseDto(any()))
                    .thenReturn(LabResultResponseDto.builder().id(1L).build());

            var result = labResultService.createResult(dto);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when request item not found")
        void shouldThrowWhenItemNotFound() {
            LabResultCreateDto dto = LabResultCreateDto.builder()
                    .requestItemId(999L)
                    .value("14.5")
                    .performedAt(LocalDateTime.now())
                    .build();

            when(labResultMapper.toEntity(any())).thenReturn(LabResult.builder().build());
            when(labRequestItemRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labResultService.createResult(dto))
                    .isInstanceOf(LabRequestItemNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Verify Result")
    class VerifyResultTests {

        @Test
        @DisplayName("should verify result")
        void shouldVerifyResult() {
            when(labResultRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testResult));
            when(labResultRepository.save(any())).thenReturn(testResult);
            when(labResultMapper.toResponseDto(any()))
                    .thenReturn(LabResultResponseDto.builder().id(1L).build());

            labResultService.verifyResult(1L, 50L);

            verify(labResultRepository).save(argThat(r -> r.getVerifiedBy() == 50L && r.getVerifiedAt() != null));
        }

        @Test
        @DisplayName("should throw when result not found for verify")
        void shouldThrowWhenNotFound() {
            when(labResultRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labResultService.verifyResult(999L, 50L))
                    .isInstanceOf(LabResultNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Read Results")
    class ReadResultTests {

        @Test
        @DisplayName("should get unverified results")
        void shouldGetUnverified() {
            when(labResultRepository.findUnverifiedResults()).thenReturn(List.of(testResult));
            when(labResultMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(LabResultResponseDto.builder().id(1L).build()));

            assertThat(labResultService.getUnverifiedResults()).hasSize(1);
        }

        @Test
        @DisplayName("should get critical results")
        void shouldGetCritical() {
            when(labResultRepository.findCriticalResults()).thenReturn(List.of(testResult));
            when(labResultMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(LabResultResponseDto.builder().id(1L).build()));

            assertThat(labResultService.getCriticalResults()).hasSize(1);
        }

        @Test
        @DisplayName("should get abnormal results")
        void shouldGetAbnormal() {
            when(labResultRepository.findAbnormalResults()).thenReturn(List.of(testResult));
            when(labResultMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(LabResultResponseDto.builder().id(1L).build()));

            assertThat(labResultService.getAbnormalResults()).hasSize(1);
        }

        @Test
        @DisplayName("should get result by request item")
        void shouldGetByRequestItem() {
            when(labResultRepository.findByRequestItemId(1L)).thenReturn(Optional.of(testResult));
            when(labResultMapper.toResponseDto(testResult))
                    .thenReturn(LabResultResponseDto.builder().id(1L).build());

            var result = labResultService.getResultByRequestItem(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when result not found by request item")
        void shouldThrowWhenNotFoundByRequestItem() {
            when(labResultRepository.findByRequestItemId(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labResultService.getResultByRequestItem(999L))
                    .isInstanceOf(LabResultNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Delete Result")
    class DeleteResultTests {

        @Test
        @DisplayName("should soft delete result")
        void shouldSoftDelete() {
            when(labResultRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testResult));

            labResultService.deleteResult(1L);

            verify(labResultRepository).save(argThat(LabResult::isDeleted));
        }
    }
}
