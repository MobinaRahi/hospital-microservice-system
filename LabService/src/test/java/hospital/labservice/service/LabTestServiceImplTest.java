package hospital.labservice.service;

import hospital.labservice.dto.labtest.LabTestCreateDto;
import hospital.labservice.dto.labtest.LabTestResponseDto;
import hospital.labservice.dto.labtest.LabTestUpdateDto;
import hospital.labservice.exception.labtest.DuplicateLabTestCodeException;
import hospital.labservice.exception.labtest.LabTestNotFoundException;
import hospital.labservice.mapper.LabTestMapper;
import hospital.labservice.model.LabTest;
import hospital.labservice.model.enums.TestCategory;
import hospital.labservice.repository.LabTestRepository;
import hospital.labservice.service.impl.LabTestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link LabTestServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class LabTestServiceImplTest {

    @Mock private LabTestRepository labTestRepository;
    @Mock private LabTestMapper labTestMapper;

    @InjectMocks
    private LabTestServiceImpl labTestService;

    private LabTest testLabTest;

    @BeforeEach
    void setUp() {
        testLabTest = LabTest.builder()
                .id(1L)
                .code("CBC")
                .name("Complete Blood Count")
                .category(TestCategory.HEMATOLOGY)
                .price(new BigDecimal("25.00"))
                .turnaroundHours(24)
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Create LabTest")
    class CreateLabTestTests {

        @Test
        @DisplayName("should create lab test successfully")
        void shouldCreateLabTest() {
            LabTestCreateDto dto = LabTestCreateDto.builder()
                    .code("CBC")
                    .name("Complete Blood Count")
                    .category(TestCategory.HEMATOLOGY)
                    .price(new BigDecimal("25.00"))
                    .turnaroundHours(24)
                    .build();

            when(labTestRepository.existsByCode("CBC")).thenReturn(false);
            when(labTestMapper.toEntity(any(LabTestCreateDto.class))).thenReturn(testLabTest);
            when(labTestRepository.save(any(LabTest.class))).thenReturn(testLabTest);
            when(labTestMapper.toResponseDto(any(LabTest.class)))
                    .thenReturn(LabTestResponseDto.builder().id(1L).code("CBC").build());

            LabTestResponseDto result = labTestService.createLabTest(dto);

            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getCode()).isEqualTo("CBC");
            verify(labTestRepository).save(any(LabTest.class));
        }

        @Test
        @DisplayName("should throw when code already exists")
        void shouldThrowWhenCodeExists() {
            LabTestCreateDto dto = LabTestCreateDto.builder()
                    .code("CBC")
                    .build();

            when(labTestRepository.existsByCode("CBC")).thenReturn(true);

            assertThatThrownBy(() -> labTestService.createLabTest(dto))
                    .isInstanceOf(DuplicateLabTestCodeException.class);
        }
    }

    @Nested
    @DisplayName("Read LabTest")
    class ReadLabTestTests {

        @Test
        @DisplayName("should get lab test by id")
        void shouldGetById() {
            when(labTestRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testLabTest));
            when(labTestMapper.toResponseDto(testLabTest))
                    .thenReturn(LabTestResponseDto.builder().id(1L).build());

            LabTestResponseDto result = labTestService.getLabTestById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found by id")
        void shouldThrowWhenNotFoundById() {
            when(labTestRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labTestService.getLabTestById(999L))
                    .isInstanceOf(LabTestNotFoundException.class);
        }

        @Test
        @DisplayName("should get lab test by code")
        void shouldGetByCode() {
            when(labTestRepository.findByCode("CBC")).thenReturn(Optional.of(testLabTest));
            when(labTestMapper.toResponseDto(testLabTest))
                    .thenReturn(LabTestResponseDto.builder().id(1L).code("CBC").build());

            LabTestResponseDto result = labTestService.getLabTestByCode("CBC");

            assertThat(result.getCode()).isEqualTo("CBC");
        }

        @Test
        @DisplayName("should throw when not found by code")
        void shouldThrowWhenNotFoundByCode() {
            when(labTestRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labTestService.getLabTestByCode("UNKNOWN"))
                    .isInstanceOf(LabTestNotFoundException.class);
        }

        @Test
        @DisplayName("should get all lab tests")
        void shouldGetAll() {
            when(labTestRepository.findAllNotDeleted()).thenReturn(List.of(testLabTest));
            when(labTestMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(LabTestResponseDto.builder().id(1L).build()));

            assertThat(labTestService.getAllLabTests()).hasSize(1);
        }

        @Test
        @DisplayName("should get active lab tests")
        void shouldGetActive() {
            when(labTestRepository.findByIsActive(true)).thenReturn(List.of(testLabTest));
            when(labTestMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(LabTestResponseDto.builder().id(1L).build()));

            assertThat(labTestService.getActiveLabTests()).hasSize(1);
        }

        @Test
        @DisplayName("should search by name")
        void shouldSearchByName() {
            when(labTestRepository.findByNameContainingIgnoreCase("blood"))
                    .thenReturn(List.of(testLabTest));
            when(labTestMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(LabTestResponseDto.builder().id(1L).build()));

            assertThat(labTestService.searchByName("blood")).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Update LabTest")
    class UpdateLabTestTests {

        @Test
        @DisplayName("should update lab test")
        void shouldUpdateLabTest() {
            LabTestUpdateDto dto = LabTestUpdateDto.builder()
                    .name("Updated Name")
                    .build();

            when(labTestRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testLabTest));
            when(labTestRepository.save(any(LabTest.class))).thenReturn(testLabTest);
            when(labTestMapper.toResponseDto(any(LabTest.class)))
                    .thenReturn(LabTestResponseDto.builder().id(1L).build());

            LabTestResponseDto result = labTestService.updateLabTest(1L, dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(labTestMapper).updateEntity(dto, testLabTest);
        }

        @Test
        @DisplayName("should throw when updating non-existent test")
        void shouldThrowWhenUpdatingNonExistent() {
            when(labTestRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labTestService.updateLabTest(999L, new LabTestUpdateDto()))
                    .isInstanceOf(LabTestNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Delete LabTest")
    class DeleteLabTestTests {

        @Test
        @DisplayName("should soft delete lab test")
        void shouldSoftDelete() {
            when(labTestRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testLabTest));

            labTestService.deleteLabTest(1L);

            verify(labTestRepository).save(argThat(LabTest::isDeleted));
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check code existence")
        void shouldCheckCodeExistence() {
            when(labTestRepository.existsByCode("CBC")).thenReturn(true);

            assertThat(labTestService.codeExists("CBC")).isTrue();
        }
    }
}
