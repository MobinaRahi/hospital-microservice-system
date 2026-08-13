package hospital.labservice.service;

import hospital.labservice.dto.labequipment.LabEquipmentCreateDto;
import hospital.labservice.dto.labequipment.LabEquipmentResponseDto;
import hospital.labservice.exception.labequipment.DuplicateLabEquipmentSerialNumberException;
import hospital.labservice.exception.labequipment.LabEquipmentNotFoundException;
import hospital.labservice.mapper.LabEquipmentMapper;
import hospital.labservice.model.LabEquipment;
import hospital.labservice.model.enums.EquipmentStatus;
import hospital.labservice.repository.LabEquipmentRepository;
import hospital.labservice.service.impl.LabEquipmentServiceImpl;
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
 * Unit tests for {@link LabEquipmentServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class LabEquipmentServiceImplTest {

    @Mock private LabEquipmentRepository labEquipmentRepository;
    @Mock private LabEquipmentMapper labEquipmentMapper;

    @InjectMocks
    private LabEquipmentServiceImpl labEquipmentService;

    private LabEquipment testEquipment;

    @BeforeEach
    void setUp() {
        testEquipment = LabEquipment.builder()
                .id(1L)
                .name("Coulter Counter")
                .serialNumber("SN-001")
                .status(EquipmentStatus.OPERATIONAL)
                .build();
    }

    @Nested
    @DisplayName("Create Equipment")
    class CreateEquipmentTests {

        @Test
        @DisplayName("should create equipment successfully")
        void shouldCreateEquipment() {
            LabEquipmentCreateDto dto = LabEquipmentCreateDto.builder()
                    .name("Coulter Counter")
                    .serialNumber("SN-001")
                    .build();

            when(labEquipmentRepository.existsBySerialNumber("SN-001")).thenReturn(false);
            when(labEquipmentMapper.toEntity(any())).thenReturn(testEquipment);
            when(labEquipmentRepository.save(any())).thenReturn(testEquipment);
            when(labEquipmentMapper.toResponseDto(any()))
                    .thenReturn(LabEquipmentResponseDto.builder().id(1L).build());

            var result = labEquipmentService.createEquipment(dto);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when serial number exists")
        void shouldThrowWhenSerialExists() {
            LabEquipmentCreateDto dto = LabEquipmentCreateDto.builder()
                    .name("Coulter Counter")
                    .serialNumber("SN-001")
                    .build();

            when(labEquipmentRepository.existsBySerialNumber("SN-001")).thenReturn(true);

            assertThatThrownBy(() -> labEquipmentService.createEquipment(dto))
                    .isInstanceOf(DuplicateLabEquipmentSerialNumberException.class);
        }
    }

    @Nested
    @DisplayName("Status Changes")
    class StatusChangeTests {

        @Test
        @DisplayName("should mark under maintenance")
        void shouldMarkMaintenance() {
            when(labEquipmentRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEquipment));
            when(labEquipmentRepository.save(any())).thenReturn(testEquipment);
            when(labEquipmentMapper.toResponseDto(any()))
                    .thenReturn(LabEquipmentResponseDto.builder().id(1L).build());

            labEquipmentService.markUnderMaintenance(1L);

            verify(labEquipmentRepository).save(argThat(e -> e.getStatus() == EquipmentStatus.MAINTENANCE));
        }

        @Test
        @DisplayName("should mark operational")
        void shouldMarkOperational() {
            testEquipment.setStatus(EquipmentStatus.MAINTENANCE);
            when(labEquipmentRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEquipment));
            when(labEquipmentRepository.save(any())).thenReturn(testEquipment);
            when(labEquipmentMapper.toResponseDto(any()))
                    .thenReturn(LabEquipmentResponseDto.builder().id(1L).build());

            labEquipmentService.markOperational(1L);

            verify(labEquipmentRepository).save(argThat(e -> e.getStatus() == EquipmentStatus.OPERATIONAL));
        }

        @Test
        @DisplayName("should mark broken")
        void shouldMarkBroken() {
            when(labEquipmentRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEquipment));
            when(labEquipmentRepository.save(any())).thenReturn(testEquipment);
            when(labEquipmentMapper.toResponseDto(any()))
                    .thenReturn(LabEquipmentResponseDto.builder().id(1L).build());

            labEquipmentService.markBroken(1L);

            verify(labEquipmentRepository).save(argThat(e -> e.getStatus() == EquipmentStatus.BROKEN));
        }

        @Test
        @DisplayName("should decommission")
        void shouldDecommission() {
            when(labEquipmentRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEquipment));
            when(labEquipmentRepository.save(any())).thenReturn(testEquipment);
            when(labEquipmentMapper.toResponseDto(any()))
                    .thenReturn(LabEquipmentResponseDto.builder().id(1L).build());

            labEquipmentService.decommission(1L);

            verify(labEquipmentRepository).save(argThat(e -> e.getStatus() == EquipmentStatus.DECOMMISSIONED));
        }

        @Test
        @DisplayName("should schedule calibration")
        void shouldScheduleCalibration() {
            LocalDate nextDate = LocalDate.now().plusMonths(6);
            when(labEquipmentRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEquipment));
            when(labEquipmentRepository.save(any())).thenReturn(testEquipment);
            when(labEquipmentMapper.toResponseDto(any()))
                    .thenReturn(LabEquipmentResponseDto.builder().id(1L).build());

            labEquipmentService.scheduleCalibration(1L, nextDate);

            verify(labEquipmentRepository).save(argThat(e ->
                    e.getNextCalibrationDate() != null && e.getLastCalibrationDate() != null));
        }

        @Test
        @DisplayName("should throw when equipment not found")
        void shouldThrowWhenNotFound() {
            when(labEquipmentRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labEquipmentService.markBroken(999L))
                    .isInstanceOf(LabEquipmentNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Read Equipment")
    class ReadEquipmentTests {

        @Test
        @DisplayName("should get equipment needing calibration")
        void shouldGetNeedingCalibration() {
            when(labEquipmentRepository.findNeedsCalibration(any())).thenReturn(List.of(testEquipment));
            when(labEquipmentMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(LabEquipmentResponseDto.builder().id(1L).build()));

            assertThat(labEquipmentService.getEquipmentNeedingCalibration()).hasSize(1);
        }

        @Test
        @DisplayName("should get available equipment")
        void shouldGetAvailable() {
            when(labEquipmentRepository.findByStatus(EquipmentStatus.OPERATIONAL))
                    .thenReturn(List.of(testEquipment));
            when(labEquipmentMapper.toResponseDto(any()))
                    .thenReturn(LabEquipmentResponseDto.builder().id(1L).build());

            assertThat(labEquipmentService.getAvailableEquipment()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Delete Equipment")
    class DeleteEquipmentTests {

        @Test
        @DisplayName("should soft delete equipment")
        void shouldSoftDelete() {
            when(labEquipmentRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEquipment));

            labEquipmentService.deleteEquipment(1L);

            verify(labEquipmentRepository).save(argThat(LabEquipment::isDeleted));
        }
    }
}
