package hospital.inventoryservice.service;

import hospital.inventoryservice.dto.equipment.EquipmentCreateDto;
import hospital.inventoryservice.dto.equipment.EquipmentResponseDto;
import hospital.inventoryservice.exception.equipment.DuplicateEquipmentSerialNumberException;
import hospital.inventoryservice.exception.equipment.EquipmentNotFoundException;
import hospital.inventoryservice.exception.equipment.EquipmentNotAvailableException;
import hospital.inventoryservice.mapper.EquipmentMapper;
import hospital.inventoryservice.model.Equipment;
import hospital.inventoryservice.model.enums.EquipmentStatus;
import hospital.inventoryservice.model.enums.EquipmentType;
import hospital.inventoryservice.repository.EquipmentRepository;
import hospital.inventoryservice.service.impl.EquipmentServiceImpl;
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
 * Unit tests for {@link EquipmentServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class EquipmentServiceImplTest {

    @Mock private EquipmentRepository equipmentRepository;
    @Mock private EquipmentMapper equipmentMapper;

    @InjectMocks
    private EquipmentServiceImpl equipmentService;

    private Equipment testEquipment;

    @BeforeEach
    void setUp() {
        testEquipment = Equipment.builder()
                .id(1L)
                .name("Ventilator #1")
                .type(EquipmentType.VENTILATOR)
                .serialNumber("SN-V001")
                .status(EquipmentStatus.AVAILABLE)
                .isActive(true)
                .warrantyExpiry(LocalDate.now().plusYears(2))
                .build();
    }

    @Nested
    @DisplayName("Create Equipment")
    class CreateEquipmentTests {

        @Test
        @DisplayName("should create equipment successfully")
        void shouldCreateEquipment() {
            EquipmentCreateDto dto = EquipmentCreateDto.builder()
                    .name("Ventilator #1")
                    .type(EquipmentType.VENTILATOR)
                    .serialNumber("SN-V001")
                    .build();

            EquipmentResponseDto expected = EquipmentResponseDto.builder()
                    .id(1L).name("Ventilator #1").status(EquipmentStatus.AVAILABLE).build();

            when(equipmentRepository.existsBySerialNumber("SN-V001")).thenReturn(false);
            when(equipmentMapper.toEntity(any(EquipmentCreateDto.class))).thenReturn(testEquipment);
            when(equipmentMapper.toResponseDto(any(Equipment.class))).thenReturn(expected);
            when(equipmentRepository.save(any(Equipment.class))).thenReturn(testEquipment);

            EquipmentResponseDto result = equipmentService.createEquipment(dto);

            assertThat(result.getStatus()).isEqualTo(EquipmentStatus.AVAILABLE);
        }

        @Test
        @DisplayName("should throw when serial number exists")
        void shouldThrowWhenSerialExists() {
            EquipmentCreateDto dto = EquipmentCreateDto.builder()
                    .name("Ventilator #2")
                    .serialNumber("SN-V001")
                    .build();

            when(equipmentRepository.existsBySerialNumber("SN-V001")).thenReturn(true);

            assertThatThrownBy(() -> equipmentService.createEquipment(dto))
                    .isInstanceOf(DuplicateEquipmentSerialNumberException.class);
        }
    }

    @Nested
    @DisplayName("Read Equipment")
    class ReadEquipmentTests {

        @Test
        @DisplayName("should get equipment by id")
        void shouldGetById() {
            EquipmentResponseDto expected = EquipmentResponseDto.builder()
                    .id(1L).name("Ventilator #1").build();

            when(equipmentRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEquipment));
            when(equipmentMapper.toResponseDto(testEquipment)).thenReturn(expected);

            EquipmentResponseDto result = equipmentService.getEquipmentById(1L);

            assertThat(result.getName()).isEqualTo("Ventilator #1");
        }

        @Test
        @DisplayName("should get equipment by serial number")
        void shouldGetBySerialNumber() {
            when(equipmentRepository.findBySerialNumber("SN-V001"))
                    .thenReturn(Optional.of(testEquipment));
            when(equipmentMapper.toResponseDto(testEquipment)).thenReturn(
                    EquipmentResponseDto.builder().serialNumber("SN-V001").build());

            EquipmentResponseDto result = equipmentService.getEquipmentBySerialNumber("SN-V001");

            assertThat(result.getSerialNumber()).isEqualTo("SN-V001");
        }

        @Test
        @DisplayName("should get available equipment")
        void shouldGetAvailable() {
            when(equipmentRepository.findByStatus(EquipmentStatus.AVAILABLE))
                    .thenReturn(List.of(testEquipment));
            when(equipmentMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    EquipmentResponseDto.builder().status(EquipmentStatus.AVAILABLE).build()));

            List<EquipmentResponseDto> result = equipmentService.getAvailableEquipment();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should get equipment with expired warranty")
        void shouldGetExpiredWarranty() {
            when(equipmentRepository.findByWarrantyExpiryBefore(any(LocalDate.class)))
                    .thenReturn(List.of(testEquipment));
            when(equipmentMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    EquipmentResponseDto.builder().build()));

            List<EquipmentResponseDto> result = equipmentService.getEquipmentWithExpiredWarranty();

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Update Equipment")
    class UpdateEquipmentTests {

        @Test
        @DisplayName("should change equipment status")
        void shouldChangeStatus() {
            EquipmentResponseDto expected = EquipmentResponseDto.builder()
                    .status(EquipmentStatus.IN_USE).build();

            when(equipmentRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEquipment));
            when(equipmentMapper.toResponseDto(any(Equipment.class))).thenReturn(expected);
            when(equipmentRepository.save(any(Equipment.class))).thenReturn(testEquipment);

            EquipmentResponseDto result = equipmentService.changeStatus(1L, EquipmentStatus.IN_USE);

            assertThat(result.getStatus()).isEqualTo(EquipmentStatus.IN_USE);
            verify(equipmentRepository).save(argThat(e -> e.getStatus() == EquipmentStatus.IN_USE));
        }

        @Test
        @DisplayName("should toggle active status")
        void shouldToggleActive() {
            when(equipmentRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEquipment));
            when(equipmentMapper.toResponseDto(any(Equipment.class))).thenReturn(
                    EquipmentResponseDto.builder().build());
            when(equipmentRepository.save(any(Equipment.class))).thenReturn(testEquipment);

            equipmentService.toggleActive(1L);

            verify(equipmentRepository).save(argThat(e -> !e.getIsActive()));
        }
    }
}
