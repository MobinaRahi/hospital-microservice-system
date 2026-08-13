package hospital.adminservice.service;

import hospital.adminservice.dto.holiday.HolidayCreateDto;
import hospital.adminservice.dto.holiday.HolidayResponseDto;
import hospital.adminservice.exception.holiday.HolidayNotFoundException;
import hospital.adminservice.mapper.HolidayMapper;
import hospital.adminservice.model.Holiday;
import hospital.adminservice.repository.HolidayRepository;
import hospital.adminservice.service.impl.HolidayServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link HolidayServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class HolidayServiceImplTest {

    @Mock private HolidayRepository holidayRepository;
    @Mock private HolidayMapper holidayMapper;

    @InjectMocks
    private HolidayServiceImpl holidayService;

    private Holiday testHoliday;

    @BeforeEach
    void setUp() {
        testHoliday = Holiday.builder()
                .id(1L)
                .name("New Year's Day")
                .date(LocalDate.of(2026, 1, 1))
                .year(2026)
                .isRecurring(true)
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Create Holiday")
    class CreateHolidayTests {

        @Test
        @DisplayName("should create holiday successfully")
        void shouldCreateHoliday() {
            HolidayCreateDto dto = HolidayCreateDto.builder()
                    .name("New Year's Day")
                    .date(LocalDate.of(2026, 1, 1))
                    .year(2026)
                    .isRecurring(true)
                    .build();

            when(holidayMapper.toEntity(any(HolidayCreateDto.class))).thenReturn(testHoliday);
            when(holidayMapper.toResponseDto(any(Holiday.class)))
                    .thenReturn(HolidayResponseDto.builder().id(1L).build());
            when(holidayRepository.save(any(Holiday.class))).thenReturn(testHoliday);

            HolidayResponseDto result = holidayService.createHoliday(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(holidayRepository).save(any(Holiday.class));
        }
    }

    @Nested
    @DisplayName("Read Holiday")
    class ReadHolidayTests {

        @Test
        @DisplayName("should get holiday by id")
        void shouldGetById() {
            HolidayResponseDto expected = HolidayResponseDto.builder().id(1L).build();

            when(holidayRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testHoliday));
            when(holidayMapper.toResponseDto(testHoliday)).thenReturn(expected);

            HolidayResponseDto result = holidayService.getHolidayById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(holidayRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> holidayService.getHolidayById(999L))
                    .isInstanceOf(HolidayNotFoundException.class);
        }

        @Test
        @DisplayName("should get holidays by year")
        void shouldGetByYear() {
            when(holidayRepository.findByYear(2026)).thenReturn(List.of(testHoliday));
            when(holidayMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    HolidayResponseDto.builder().id(1L).build()));

            assertThat(holidayService.getHolidaysByYear(2026)).hasSize(1);
        }

        @Test
        @DisplayName("should get active holidays")
        void shouldGetActiveHolidays() {
            when(holidayRepository.findByIsActiveTrue()).thenReturn(List.of(testHoliday));
            when(holidayMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    HolidayResponseDto.builder().id(1L).build()));

            assertThat(holidayService.getActiveHolidays()).hasSize(1);
        }

        @Test
        @DisplayName("should get recurring holidays")
        void shouldGetRecurringHolidays() {
            when(holidayRepository.findByIsRecurringTrue()).thenReturn(List.of(testHoliday));
            when(holidayMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    HolidayResponseDto.builder().id(1L).build()));

            assertThat(holidayService.getRecurringHolidays()).hasSize(1);
        }

        @Test
        @DisplayName("should check if date is holiday")
        void shouldCheckIfDateIsHoliday() {
            when(holidayRepository.isHolidayOnDate(LocalDate.of(2026, 1, 1))).thenReturn(true);

            assertThat(holidayService.isHoliday(LocalDate.of(2026, 1, 1))).isTrue();
        }
    }

    @Nested
    @DisplayName("Delete Holiday")
    class DeleteHolidayTests {

        @Test
        @DisplayName("should soft delete holiday")
        void shouldSoftDelete() {
            when(holidayRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testHoliday));

            holidayService.deleteHoliday(1L);

            verify(holidayRepository).save(argThat(Holiday::isDeleted));
        }
    }
}
