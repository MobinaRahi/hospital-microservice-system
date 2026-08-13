package hospital.labservice.service;

import hospital.labservice.dto.labtechnician.LabTechnicianCreateDto;
import hospital.labservice.dto.labtechnician.LabTechnicianResponseDto;
import hospital.labservice.exception.labtechnician.DuplicateLabTechnicianEmployeeCodeException;
import hospital.labservice.exception.labtechnician.DuplicateLabTechnicianUserIdException;
import hospital.labservice.exception.labtechnician.LabTechnicianNotFoundException;
import hospital.labservice.mapper.LabTechnicianMapper;
import hospital.labservice.model.LabTechnician;
import hospital.labservice.model.enums.LabShift;
import hospital.labservice.repository.LabTechnicianRepository;
import hospital.labservice.service.impl.LabTechnicianServiceImpl;
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
 * Unit tests for {@link LabTechnicianServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class LabTechnicianServiceImplTest {

    @Mock private LabTechnicianRepository labTechnicianRepository;
    @Mock private LabTechnicianMapper labTechnicianMapper;

    @InjectMocks
    private LabTechnicianServiceImpl labTechnicianService;

    private LabTechnician testTechnician;

    @BeforeEach
    void setUp() {
        testTechnician = LabTechnician.builder()
                .id(1L)
                .userId(100L)
                .firstName("Ali")
                .lastName("Rezaei")
                .employeeCode("TECH-001")
                .shift(LabShift.MORNING)
                .isActive(true)
                .hireDate(LocalDate.of(2024, 1, 15))
                .build();
    }

    @Nested
    @DisplayName("Create Technician")
    class CreateTechnicianTests {

        @Test
        @DisplayName("should create technician successfully")
        void shouldCreateTechnician() {
            LabTechnicianCreateDto dto = LabTechnicianCreateDto.builder()
                    .userId(100L)
                    .firstName("Ali")
                    .lastName("Rezaei")
                    .employeeCode("TECH-001")
                    .shift(LabShift.MORNING)
                    .hireDate(LocalDate.of(2024, 1, 15))
                    .build();

            when(labTechnicianRepository.existsByUserId(100L)).thenReturn(false);
            when(labTechnicianRepository.existsByEmployeeCode("TECH-001")).thenReturn(false);
            when(labTechnicianMapper.toEntity(any())).thenReturn(testTechnician);
            when(labTechnicianRepository.save(any())).thenReturn(testTechnician);
            when(labTechnicianMapper.toResponseDto(any()))
                    .thenReturn(LabTechnicianResponseDto.builder().id(1L).build());

            var result = labTechnicianService.createTechnician(dto);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when user ID exists")
        void shouldThrowWhenUserIdExists() {
            LabTechnicianCreateDto dto = LabTechnicianCreateDto.builder()
                    .userId(100L)
                    .build();

            when(labTechnicianRepository.existsByUserId(100L)).thenReturn(true);

            assertThatThrownBy(() -> labTechnicianService.createTechnician(dto))
                    .isInstanceOf(DuplicateLabTechnicianUserIdException.class);
        }

        @Test
        @DisplayName("should throw when employee code exists")
        void shouldThrowWhenEmployeeCodeExists() {
            LabTechnicianCreateDto dto = LabTechnicianCreateDto.builder()
                    .userId(200L)
                    .employeeCode("TECH-001")
                    .build();

            when(labTechnicianRepository.existsByUserId(200L)).thenReturn(false);
            when(labTechnicianRepository.existsByEmployeeCode("TECH-001")).thenReturn(true);

            assertThatThrownBy(() -> labTechnicianService.createTechnician(dto))
                    .isInstanceOf(DuplicateLabTechnicianEmployeeCodeException.class);
        }
    }

    @Nested
    @DisplayName("Activate/Deactivate")
    class ActivationTests {

        @Test
        @DisplayName("should activate technician")
        void shouldActivate() {
            testTechnician.setIsActive(false);
            when(labTechnicianRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTechnician));
            when(labTechnicianRepository.save(any())).thenReturn(testTechnician);
            when(labTechnicianMapper.toResponseDto(any()))
                    .thenReturn(LabTechnicianResponseDto.builder().id(1L).build());

            labTechnicianService.activateTechnician(1L);

            verify(labTechnicianRepository).save(argThat(t -> Boolean.TRUE.equals(t.getIsActive())));
        }

        @Test
        @DisplayName("should deactivate technician")
        void shouldDeactivate() {
            when(labTechnicianRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTechnician));
            when(labTechnicianRepository.save(any())).thenReturn(testTechnician);
            when(labTechnicianMapper.toResponseDto(any()))
                    .thenReturn(LabTechnicianResponseDto.builder().id(1L).build());

            labTechnicianService.deactivateTechnician(1L);

            verify(labTechnicianRepository).save(argThat(t -> Boolean.FALSE.equals(t.getIsActive())));
        }

        @Test
        @DisplayName("should throw when technician not found")
        void shouldThrowWhenNotFound() {
            when(labTechnicianRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labTechnicianService.activateTechnician(999L))
                    .isInstanceOf(LabTechnicianNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Change Shift")
    class ShiftTests {

        @Test
        @DisplayName("should change shift")
        void shouldChangeShift() {
            when(labTechnicianRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTechnician));
            when(labTechnicianRepository.save(any())).thenReturn(testTechnician);
            when(labTechnicianMapper.toResponseDto(any()))
                    .thenReturn(LabTechnicianResponseDto.builder().id(1L).build());

            labTechnicianService.changeShift(1L, LabShift.NIGHT);

            verify(labTechnicianRepository).save(argThat(t -> t.getShift() == LabShift.NIGHT));
        }
    }

    @Nested
    @DisplayName("Read Technicians")
    class ReadTechnicianTests {

        @Test
        @DisplayName("should get active technicians by shift")
        void shouldGetActiveByShift() {
            when(labTechnicianRepository.findByShiftAndIsActive(LabShift.MORNING, true))
                    .thenReturn(List.of(testTechnician));
            when(labTechnicianMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(LabTechnicianResponseDto.builder().id(1L).build()));

            assertThat(labTechnicianService.getActiveTechniciansByShift(LabShift.MORNING)).hasSize(1);
        }

        @Test
        @DisplayName("should get technician by user id")
        void shouldGetByUserId() {
            when(labTechnicianRepository.findByUserId(100L)).thenReturn(Optional.of(testTechnician));
            when(labTechnicianMapper.toResponseDto(testTechnician))
                    .thenReturn(LabTechnicianResponseDto.builder().id(1L).build());

            var result = labTechnicianService.getTechnicianByUserId(100L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found by user id")
        void shouldThrowWhenNotFoundByUserId() {
            when(labTechnicianRepository.findByUserId(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> labTechnicianService.getTechnicianByUserId(999L))
                    .isInstanceOf(LabTechnicianNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Delete Technician")
    class DeleteTechnicianTests {

        @Test
        @DisplayName("should soft delete technician")
        void shouldSoftDelete() {
            when(labTechnicianRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTechnician));

            labTechnicianService.deleteTechnician(1L);

            verify(labTechnicianRepository).save(argThat(LabTechnician::isDeleted));
        }
    }
}
