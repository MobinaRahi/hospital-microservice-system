package hospital.adminservice.service;

import hospital.adminservice.dto.bed.BedCreateDto;
import hospital.adminservice.dto.bed.BedResponseDto;
import hospital.adminservice.exception.bed.BedAlreadyOccupiedException;
import hospital.adminservice.exception.bed.BedNotAvailableException;
import hospital.adminservice.exception.bed.BedNotFoundException;
import hospital.adminservice.mapper.BedMapper;
import hospital.adminservice.model.Bed;
import hospital.adminservice.model.enums.BedStatus;
import hospital.adminservice.model.enums.BedType;
import hospital.adminservice.repository.BedRepository;
import hospital.adminservice.service.impl.BedServiceImpl;
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
 * Unit tests for {@link BedServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class BedServiceImplTest {

    @Mock private BedRepository bedRepository;
    @Mock private BedMapper bedMapper;

    @InjectMocks
    private BedServiceImpl bedService;

    private Bed testBed;

    @BeforeEach
    void setUp() {
        testBed = Bed.builder()
                .id(1L)
                .bedNumber("ICU-001")
                .type(BedType.ICU)
                .status(BedStatus.AVAILABLE)
                .build();
    }

    @Nested
    @DisplayName("Create Bed")
    class CreateBedTests {

        @Test
        @DisplayName("should create bed successfully")
        void shouldCreateBed() {
            BedCreateDto dto = BedCreateDto.builder()
                    .bedNumber("ICU-001")
                    .type(BedType.ICU)
                    .build();

            when(bedMapper.toEntity(any(BedCreateDto.class))).thenReturn(testBed);
            when(bedMapper.toResponseDto(any(Bed.class)))
                    .thenReturn(BedResponseDto.builder().id(1L).build());
            when(bedRepository.save(any(Bed.class))).thenReturn(testBed);

            BedResponseDto result = bedService.createBed(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(bedRepository).save(any(Bed.class));
        }
    }

    @Nested
    @DisplayName("Read Bed")
    class ReadBedTests {

        @Test
        @DisplayName("should get bed by id")
        void shouldGetById() {
            BedResponseDto expected = BedResponseDto.builder().id(1L).build();

            when(bedRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testBed));
            when(bedMapper.toResponseDto(testBed)).thenReturn(expected);

            BedResponseDto result = bedService.getBedById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(bedRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bedService.getBedById(999L))
                    .isInstanceOf(BedNotFoundException.class);
        }

        @Test
        @DisplayName("should get available beds by department")
        void shouldGetAvailableBedsByDepartment() {
            when(bedRepository.findByDepartmentIdAndStatus(1L, BedStatus.AVAILABLE))
                    .thenReturn(List.of(testBed));
            when(bedMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    BedResponseDto.builder().id(1L).build()));

            assertThat(bedService.getAvailableBedsByDepartment(1L)).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Patient Assignment")
    class PatientAssignmentTests {

        @Test
        @DisplayName("should assign bed to patient")
        void shouldAssignToPatient() {
            when(bedRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testBed));
            when(bedMapper.toResponseDto(any(Bed.class)))
                    .thenReturn(BedResponseDto.builder().build());
            when(bedRepository.save(any(Bed.class))).thenReturn(testBed);

            bedService.assignToPatient(1L, 100L, 200L);

            verify(bedRepository).save(argThat(b -> b.getStatus() == BedStatus.OCCUPIED));
        }

        @Test
        @DisplayName("should throw when bed not available")
        void shouldThrowWhenNotAvailable() {
            testBed.setStatus(BedStatus.OCCUPIED);
            when(bedRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testBed));

            assertThatThrownBy(() -> bedService.assignToPatient(1L, 100L, 200L))
                    .isInstanceOf(BedNotAvailableException.class);
        }

        @Test
        @DisplayName("should discharge patient from bed")
        void shouldDischargePatient() {
            testBed.setStatus(BedStatus.OCCUPIED);
            when(bedRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testBed));
            when(bedMapper.toResponseDto(any(Bed.class)))
                    .thenReturn(BedResponseDto.builder().build());
            when(bedRepository.save(any(Bed.class))).thenReturn(testBed);

            bedService.dischargePatient(1L);

            verify(bedRepository).save(argThat(b -> b.getStatus() == BedStatus.AVAILABLE));
        }

        @Test
        @DisplayName("should throw when discharging from available bed")
        void shouldThrowWhenDischargingFromAvailable() {
            when(bedRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testBed));

            assertThatThrownBy(() -> bedService.dischargePatient(1L))
                    .isInstanceOf(BedAlreadyOccupiedException.class);
        }
    }

    @Nested
    @DisplayName("Statistics")
    class StatisticsTests {

        @Test
        @DisplayName("should count available beds")
        void shouldCountAvailableBeds() {
            when(bedRepository.countAvailableBeds()).thenReturn(10L);

            assertThat(bedService.countAvailableBeds()).isEqualTo(10L);
        }

        @Test
        @DisplayName("should count occupied beds")
        void shouldCountOccupiedBeds() {
            when(bedRepository.countOccupiedBeds()).thenReturn(5L);

            assertThat(bedService.countOccupiedBeds()).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("Delete Bed")
    class DeleteBedTests {

        @Test
        @DisplayName("should soft delete bed")
        void shouldSoftDelete() {
            when(bedRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testBed));

            bedService.deleteBed(1L);

            verify(bedRepository).save(argThat(Bed::isDeleted));
        }
    }
}
