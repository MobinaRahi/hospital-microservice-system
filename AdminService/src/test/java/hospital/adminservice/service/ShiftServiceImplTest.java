package hospital.adminservice.service;

import hospital.adminservice.dto.shift.ShiftCreateDto;
import hospital.adminservice.dto.shift.ShiftResponseDto;
import hospital.adminservice.exception.shift.DuplicateShiftCodeException;
import hospital.adminservice.exception.shift.ShiftNotFoundException;
import hospital.adminservice.mapper.ShiftMapper;
import hospital.adminservice.model.Shift;
import hospital.adminservice.repository.ShiftRepository;
import hospital.adminservice.service.impl.ShiftServiceImpl;
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
 * Unit tests for {@link ShiftServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class ShiftServiceImplTest {

    @Mock private ShiftRepository shiftRepository;
    @Mock private ShiftMapper shiftMapper;

    @InjectMocks
    private ShiftServiceImpl shiftService;

    private Shift testShift;

    @BeforeEach
    void setUp() {
        testShift = Shift.builder()
                .id(1L)
                .name("Morning Shift")
                .code("MORNING")
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Create Shift")
    class CreateShiftTests {

        @Test
        @DisplayName("should create shift successfully")
        void shouldCreateShift() {
            ShiftCreateDto dto = ShiftCreateDto.builder()
                    .name("Morning Shift").code("MORNING").build();

            when(shiftRepository.existsByCode("MORNING")).thenReturn(false);
            when(shiftMapper.toEntity(any(ShiftCreateDto.class))).thenReturn(testShift);
            when(shiftMapper.toResponseDto(any(Shift.class)))
                    .thenReturn(ShiftResponseDto.builder().id(1L).build());
            when(shiftRepository.save(any(Shift.class))).thenReturn(testShift);

            ShiftResponseDto result = shiftService.createShift(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(shiftRepository).save(any(Shift.class));
        }

        @Test
        @DisplayName("should throw when code exists")
        void shouldThrowWhenCodeExists() {
            ShiftCreateDto dto = ShiftCreateDto.builder().code("MORNING").build();

            when(shiftRepository.existsByCode("MORNING")).thenReturn(true);

            assertThatThrownBy(() -> shiftService.createShift(dto))
                    .isInstanceOf(DuplicateShiftCodeException.class);
        }
    }

    @Nested
    @DisplayName("Read Shift")
    class ReadShiftTests {

        @Test
        @DisplayName("should get shift by id")
        void shouldGetById() {
            ShiftResponseDto expected = ShiftResponseDto.builder().id(1L).build();

            when(shiftRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testShift));
            when(shiftMapper.toResponseDto(testShift)).thenReturn(expected);

            ShiftResponseDto result = shiftService.getShiftById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(shiftRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> shiftService.getShiftById(999L))
                    .isInstanceOf(ShiftNotFoundException.class);
        }

        @Test
        @DisplayName("should get active shifts")
        void shouldGetActiveShifts() {
            when(shiftRepository.findByIsActiveTrue()).thenReturn(List.of(testShift));
            when(shiftMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    ShiftResponseDto.builder().id(1L).build()));

            assertThat(shiftService.getAllActiveShifts()).hasSize(1);
        }

        @Test
        @DisplayName("should get night shifts")
        void shouldGetNightShifts() {
            when(shiftRepository.findByNightShiftTrue()).thenReturn(List.of(testShift));
            when(shiftMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    ShiftResponseDto.builder().id(1L).build()));

            assertThat(shiftService.getNightShifts()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Update Shift")
    class UpdateShiftTests {

        @Test
        @DisplayName("should toggle active status")
        void shouldToggleActive() {
            when(shiftRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testShift));
            when(shiftMapper.toResponseDto(any(Shift.class)))
                    .thenReturn(ShiftResponseDto.builder().build());
            when(shiftRepository.save(any(Shift.class))).thenReturn(testShift);

            shiftService.toggleActive(1L);

            verify(shiftRepository).save(argThat(s -> !s.getIsActive()));
        }
    }

    @Nested
    @DisplayName("Delete Shift")
    class DeleteShiftTests {

        @Test
        @DisplayName("should soft delete shift")
        void shouldSoftDelete() {
            when(shiftRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testShift));

            shiftService.deleteShift(1L);

            verify(shiftRepository).save(argThat(Shift::isDeleted));
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check code existence")
        void shouldCheckCodeExistence() {
            when(shiftRepository.existsByCode("MORNING")).thenReturn(true);

            assertThat(shiftService.codeExists("MORNING")).isTrue();
        }
    }
}
