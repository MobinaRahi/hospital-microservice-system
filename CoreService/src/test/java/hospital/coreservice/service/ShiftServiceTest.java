package hospital.coreservice.service;

import hospital.coreservice.dto.shift.ShiftCreateDto;
import hospital.coreservice.dto.shift.ShiftResponseDto;
import hospital.coreservice.exception.shift.ShiftNotFoundException;
import hospital.coreservice.mapper.ShiftMapper;
import hospital.coreservice.model.Shift;
import hospital.coreservice.repository.ShiftRepository;
import hospital.coreservice.service.imp.ShiftServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ShiftService Tests - CoreService")
class ShiftServiceTest {

    @Mock private ShiftRepository shiftRepository;
    @Mock private ShiftMapper shiftMapper;

    @InjectMocks
    private ShiftServiceImpl shiftService;

    private Shift shift;
    private ShiftCreateDto createDto;
    private ShiftResponseDto responseDto;

    @BeforeEach
    void setUp() {
        shift = new Shift();
        shift.setId(1L);
        shift.setName("صبح");
        shift.setStartTime(java.time.LocalTime.of(8, 0));
        shift.setEndTime(java.time.LocalTime.of(16, 0));
        shift.setActive(true);

        createDto = new ShiftCreateDto();
        createDto.setName("صبح");
        createDto.setStartTime(java.time.LocalTime.of(8, 0));
        createDto.setEndTime(java.time.LocalTime.of(16, 0));

        responseDto = new ShiftResponseDto();
        responseDto.setId(1L);
        responseDto.setName("صبح");
    }

    @Test
    @DisplayName("Should create shift")
    void shouldCreateShift() {
        when(shiftRepository.existsByName(any())).thenReturn(false);
        when(shiftMapper.toEntity(any())).thenReturn(shift);
        when(shiftRepository.save(any())).thenReturn(shift);
        when(shiftMapper.toResponseDto(any())).thenReturn(responseDto);

        ShiftResponseDto result = shiftService.createShift(createDto);

        assertThat(result).isNotNull();
        verify(shiftRepository).save(any());
    }

    @Test
    @DisplayName("Should get shift by id")
    void shouldGetById() {
        when(shiftRepository.findById(1L)).thenReturn(Optional.of(shift));
        when(shiftMapper.toResponseDto(any())).thenReturn(responseDto);

        ShiftResponseDto result = shiftService.getShiftById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should return active shifts")
    void shouldReturnActiveShifts() {
        when(shiftRepository.findAllActive()).thenReturn(List.of(shift));
        when(shiftMapper.toResponseDto(any())).thenReturn(responseDto);

        List<ShiftResponseDto> result = shiftService.getActiveShifts();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Should throw when shift not found")
    void shouldThrowWhenNotFound() {
        when(shiftRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> shiftService.getShiftById(999L))
                .isInstanceOf(ShiftNotFoundException.class);
    }

    @Test
    @DisplayName("Should count shifts")
    void shouldCountShifts() {
        when(shiftRepository.countActive()).thenReturn(5L);

        Long count = shiftService.countActiveShifts();

        assertThat(count).isEqualTo(5L);
    }
}
