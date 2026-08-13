package hospital.adminservice.service;

import hospital.adminservice.dto.hospital.HospitalCreateDto;
import hospital.adminservice.dto.hospital.HospitalResponseDto;
import hospital.adminservice.exception.hospital.DuplicateHospitalCodeException;
import hospital.adminservice.exception.hospital.HospitalNotFoundException;
import hospital.adminservice.mapper.HospitalMapper;
import hospital.adminservice.model.Hospital;
import hospital.adminservice.repository.HospitalRepository;
import hospital.adminservice.service.impl.HospitalServiceImpl;
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
 * Unit tests for {@link HospitalServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class HospitalServiceImplTest {

    @Mock private HospitalRepository hospitalRepository;
    @Mock private HospitalMapper hospitalMapper;

    @InjectMocks
    private HospitalServiceImpl hospitalService;

    private Hospital testHospital;

    @BeforeEach
    void setUp() {
        testHospital = Hospital.builder()
                .id(1L)
                .name("Tehran Heart Center")
                .code("THC-001")
                .build();
    }

    @Nested
    @DisplayName("Create Hospital")
    class CreateHospitalTests {

        @Test
        @DisplayName("should create hospital successfully")
        void shouldCreateHospital() {
            HospitalCreateDto dto = HospitalCreateDto.builder()
                    .name("Tehran Heart Center")
                    .code("THC-001")
                    .build();

            when(hospitalRepository.existsByCode("THC-001")).thenReturn(false);
            when(hospitalMapper.toEntity(any(HospitalCreateDto.class))).thenReturn(testHospital);
            when(hospitalMapper.toResponseDto(any(Hospital.class)))
                    .thenReturn(HospitalResponseDto.builder().id(1L).name("Tehran Heart Center").build());
            when(hospitalRepository.save(any(Hospital.class))).thenReturn(testHospital);

            HospitalResponseDto result = hospitalService.createHospital(dto);

            assertThat(result.getName()).isEqualTo("Tehran Heart Center");
            verify(hospitalRepository).save(any(Hospital.class));
        }

        @Test
        @DisplayName("should throw when code exists")
        void shouldThrowWhenCodeExists() {
            HospitalCreateDto dto = HospitalCreateDto.builder().code("THC-001").build();

            when(hospitalRepository.existsByCode("THC-001")).thenReturn(true);

            assertThatThrownBy(() -> hospitalService.createHospital(dto))
                    .isInstanceOf(DuplicateHospitalCodeException.class);
        }
    }

    @Nested
    @DisplayName("Read Hospital")
    class ReadHospitalTests {

        @Test
        @DisplayName("should get hospital by id")
        void shouldGetById() {
            HospitalResponseDto expected = HospitalResponseDto.builder().id(1L).build();

            when(hospitalRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testHospital));
            when(hospitalMapper.toResponseDto(testHospital)).thenReturn(expected);

            HospitalResponseDto result = hospitalService.getHospitalById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(hospitalRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> hospitalService.getHospitalById(999L))
                    .isInstanceOf(HospitalNotFoundException.class);
        }

        @Test
        @DisplayName("should get all hospitals")
        void shouldGetAllHospitals() {
            when(hospitalRepository.findAllNotDeleted()).thenReturn(List.of(testHospital));
            when(hospitalMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    HospitalResponseDto.builder().id(1L).build()));

            assertThat(hospitalService.getAllHospitals()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Delete Hospital")
    class DeleteHospitalTests {

        @Test
        @DisplayName("should soft delete hospital")
        void shouldSoftDelete() {
            when(hospitalRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testHospital));

            hospitalService.deleteHospital(1L);

            verify(hospitalRepository).save(argThat(Hospital::isDeleted));
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check code existence")
        void shouldCheckCodeExistence() {
            when(hospitalRepository.existsByCode("THC-001")).thenReturn(true);

            assertThat(hospitalService.codeExists("THC-001")).isTrue();
        }
    }
}
