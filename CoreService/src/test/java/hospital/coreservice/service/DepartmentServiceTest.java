package hospital.coreservice.service;

import hospital.coreservice.dto.department.DepartmentCreateDto;
import hospital.coreservice.dto.department.DepartmentResponseDto;
import hospital.coreservice.exception.department.DepartmentNotFoundException;
import hospital.coreservice.mapper.DepartmentMapper;
import hospital.coreservice.model.Department;
import hospital.coreservice.repository.DepartmentRepository;
import hospital.coreservice.repository.DoctorRepository;
import hospital.coreservice.repository.NurseRepository;
import hospital.coreservice.repository.RoomRepository;
import hospital.coreservice.service.imp.DepartmentServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DepartmentService Tests - CoreService")
class DepartmentServiceTest {

    @Mock private DepartmentRepository departmentRepository;
    @Mock private DepartmentMapper departmentMapper;
    @Mock private DoctorRepository doctorRepository;
    @Mock private NurseRepository nurseRepository;
    @Mock private RoomRepository roomRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    private DepartmentCreateDto createDto;
    private DepartmentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setDepartmentCode("CARD");
        department.setDepartmentName("قلب و عروق");
        department.setActive(true);

        createDto = new DepartmentCreateDto();
        createDto.setDepartmentCode("CARD");
        createDto.setDepartmentName("قلب و عروق");

        responseDto = new DepartmentResponseDto();
        responseDto.setId(1L);
        responseDto.setDepartmentCode("CARD");
        responseDto.setDepartmentName("قلب و عروق");
    }

    @Nested
    @DisplayName("Create & Update")
    class CreateUpdate {

        @Test
        @DisplayName("Should create department")
        void shouldCreateDepartment() {
            when(departmentMapper.toEntity(any())).thenReturn(department);
            when(departmentRepository.save(any())).thenReturn(department);
            when(departmentMapper.toResponseDto(any())).thenReturn(responseDto);

            DepartmentResponseDto result = departmentService.createDepartment(createDto);

            assertThat(result).isNotNull();
            verify(departmentRepository).save(any());
        }

        @Test
        @DisplayName("Should update department")
        void shouldUpdateDepartment() {
            when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
            when(departmentRepository.save(any())).thenReturn(department);
            when(departmentMapper.toResponseDto(any())).thenReturn(responseDto);

            DepartmentResponseDto result = departmentService.updateDepartment(1L, new hospital.coreservice.dto.department.DepartmentUpdateDto());

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("Retrieval")
    class Retrieval {

        @Test
        @DisplayName("Should get by id")
        void shouldGetById() {
            when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
            when(departmentMapper.toResponseDto(any())).thenReturn(responseDto);

            DepartmentResponseDto result = departmentService.getDepartmentById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should throw when not found")
        void shouldThrowWhenNotFound() {
            when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> departmentService.getDepartmentById(999L))
                    .isInstanceOf(DepartmentNotFoundException.class);
        }

        @Test
        @DisplayName("Should return active departments")
        void shouldReturnActive() {
            when(departmentRepository.findAllActive()).thenReturn(List.of(department));
            when(departmentMapper.toResponseDto(any())).thenReturn(responseDto);

            List<DepartmentResponseDto> result = departmentService.getActiveDepartments();

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Leadership & Members")
    class Leadership {

        @Test
        @DisplayName("Should assign head doctor")
        void shouldAssignHeadDoctor() {
            when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
            when(doctorRepository.findById(10L)).thenReturn(Optional.of(new hospital.coreservice.model.Doctor()));

            departmentService.assignHeadDoctor(1L, 10L);

            verify(departmentRepository).save(any());
        }

        @Test
        @DisplayName("Should remove head doctor")
        void shouldRemoveHeadDoctor() {
            when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

            departmentService.removeHeadDoctor(1L);

            verify(departmentRepository).save(any());
        }
    }

    @Test
    @DisplayName("Should count departments")
    void shouldCountDepartments() {
        when(departmentRepository.countActive()).thenReturn(8L);

        Long count = departmentService.countActiveDepartments();

        assertThat(count).isEqualTo(8L);
    }
}
