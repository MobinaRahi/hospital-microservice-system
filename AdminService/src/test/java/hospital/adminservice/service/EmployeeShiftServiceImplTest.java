package hospital.adminservice.service;

import hospital.adminservice.dto.employeeshift.EmployeeShiftCreateDto;
import hospital.adminservice.dto.employeeshift.EmployeeShiftResponseDto;
import hospital.adminservice.exception.employeeshift.AlreadyMarkedException;
import hospital.adminservice.exception.employeeshift.DuplicateEmployeeShiftException;
import hospital.adminservice.exception.employeeshift.EmployeeShiftNotFoundException;
import hospital.adminservice.exception.shift.ShiftNotFoundException;
import hospital.adminservice.mapper.EmployeeShiftMapper;
import hospital.adminservice.model.EmployeeShift;
import hospital.adminservice.model.Shift;
import hospital.adminservice.repository.EmployeeShiftRepository;
import hospital.adminservice.repository.ShiftRepository;
import hospital.adminservice.service.impl.EmployeeShiftServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EmployeeShiftServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class EmployeeShiftServiceImplTest {

    @Mock private EmployeeShiftRepository employeeShiftRepository;
    @Mock private ShiftRepository shiftRepository;
    @Mock private EmployeeShiftMapper employeeShiftMapper;

    @InjectMocks
    private EmployeeShiftServiceImpl employeeShiftService;

    private EmployeeShift testEmployeeShift;
    private Shift testShift;

    @BeforeEach
    void setUp() {
        testShift = Shift.builder().id(1L).name("Morning").code("MORNING").build();

        testEmployeeShift = EmployeeShift.builder()
                .id(1L)
                .employeeId(100L)
                .shift(testShift)
                .date(LocalDate.of(2026, 8, 13))
                .isPresent(false)
                .build();
    }

    @Nested
    @DisplayName("Create EmployeeShift")
    class CreateEmployeeShiftTests {

        @Test
        @DisplayName("should create employee shift successfully")
        void shouldCreateEmployeeShift() {
            EmployeeShiftCreateDto dto = EmployeeShiftCreateDto.builder()
                    .employeeId(100L).shiftId(1L).date(LocalDate.of(2026, 8, 13)).build();

            when(employeeShiftRepository.findByEmployeeIdAndDate(100L, LocalDate.of(2026, 8, 13)))
                    .thenReturn(Optional.empty());
            when(shiftRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testShift));
            when(employeeShiftMapper.toEntity(any(EmployeeShiftCreateDto.class))).thenReturn(testEmployeeShift);
            when(employeeShiftMapper.toResponseDto(any(EmployeeShift.class)))
                    .thenReturn(EmployeeShiftResponseDto.builder().id(1L).build());
            when(employeeShiftRepository.save(any(EmployeeShift.class))).thenReturn(testEmployeeShift);

            EmployeeShiftResponseDto result = employeeShiftService.createEmployeeShift(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(employeeShiftRepository).save(any(EmployeeShift.class));
        }

        @Test
        @DisplayName("should throw when employee already has shift on date")
        void shouldThrowWhenDuplicate() {
            EmployeeShiftCreateDto dto = EmployeeShiftCreateDto.builder()
                    .employeeId(100L).shiftId(1L).date(LocalDate.of(2026, 8, 13)).build();

            when(employeeShiftRepository.findByEmployeeIdAndDate(100L, LocalDate.of(2026, 8, 13)))
                    .thenReturn(Optional.of(testEmployeeShift));

            assertThatThrownBy(() -> employeeShiftService.createEmployeeShift(dto))
                    .isInstanceOf(DuplicateEmployeeShiftException.class);
        }

        @Test
        @DisplayName("should throw when shift not found")
        void shouldThrowWhenShiftNotFound() {
            EmployeeShiftCreateDto dto = EmployeeShiftCreateDto.builder()
                    .employeeId(100L).shiftId(999L).date(LocalDate.of(2026, 8, 13)).build();

            when(employeeShiftRepository.findByEmployeeIdAndDate(any(), any()))
                    .thenReturn(Optional.empty());
            when(shiftRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> employeeShiftService.createEmployeeShift(dto))
                    .isInstanceOf(ShiftNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Read EmployeeShift")
    class ReadEmployeeShiftTests {

        @Test
        @DisplayName("should get employee shift by id")
        void shouldGetById() {
            EmployeeShiftResponseDto expected = EmployeeShiftResponseDto.builder().id(1L).build();

            when(employeeShiftRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmployeeShift));
            when(employeeShiftMapper.toResponseDto(testEmployeeShift)).thenReturn(expected);

            EmployeeShiftResponseDto result = employeeShiftService.getEmployeeShiftById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(employeeShiftRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> employeeShiftService.getEmployeeShiftById(999L))
                    .isInstanceOf(EmployeeShiftNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Attendance")
    class AttendanceTests {

        @Test
        @DisplayName("should mark employee as present")
        void shouldMarkPresent() {
            LocalDateTime start = LocalDateTime.of(2026, 8, 13, 8, 0);
            LocalDateTime end = LocalDateTime.of(2026, 8, 13, 16, 0);

            when(employeeShiftRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmployeeShift));
            when(employeeShiftMapper.toResponseDto(any(EmployeeShift.class)))
                    .thenReturn(EmployeeShiftResponseDto.builder().build());
            when(employeeShiftRepository.save(any(EmployeeShift.class))).thenReturn(testEmployeeShift);

            employeeShiftService.markPresent(1L, start, end);

            verify(employeeShiftRepository).save(argThat(e -> Boolean.TRUE.equals(e.getIsPresent())));
        }

        @Test
        @DisplayName("should throw when already marked present")
        void shouldThrowWhenAlreadyPresent() {
            testEmployeeShift.setIsPresent(true);
            when(employeeShiftRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmployeeShift));

            assertThatThrownBy(() -> employeeShiftService.markPresent(1L,
                    LocalDateTime.now(), LocalDateTime.now()))
                    .isInstanceOf(AlreadyMarkedException.class);
        }

        @Test
        @DisplayName("should mark employee as absent")
        void shouldMarkAbsent() {
            when(employeeShiftRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmployeeShift));
            when(employeeShiftMapper.toResponseDto(any(EmployeeShift.class)))
                    .thenReturn(EmployeeShiftResponseDto.builder().build());
            when(employeeShiftRepository.save(any(EmployeeShift.class))).thenReturn(testEmployeeShift);

            employeeShiftService.markAbsent(1L);

            verify(employeeShiftRepository).save(argThat(e -> Boolean.FALSE.equals(e.getIsPresent())));
        }
    }

    @Nested
    @DisplayName("Statistics")
    class StatisticsTests {

        @Test
        @DisplayName("should count shifts by employee")
        void shouldCountByEmployee() {
            when(employeeShiftRepository.countByEmployeeId(100L)).thenReturn(20L);

            assertThat(employeeShiftService.countByEmployee(100L)).isEqualTo(20L);
        }

        @Test
        @DisplayName("should count present shifts by employee")
        void shouldCountPresentByEmployee() {
            when(employeeShiftRepository.countPresentByEmployeeId(100L)).thenReturn(18L);

            assertThat(employeeShiftService.countPresentByEmployee(100L)).isEqualTo(18L);
        }
    }

    @Nested
    @DisplayName("Delete EmployeeShift")
    class DeleteEmployeeShiftTests {

        @Test
        @DisplayName("should soft delete employee shift")
        void shouldSoftDelete() {
            when(employeeShiftRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmployeeShift));

            employeeShiftService.deleteEmployeeShift(1L);

            verify(employeeShiftRepository).save(argThat(EmployeeShift::isDeleted));
        }
    }
}
