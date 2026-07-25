package hospital.coreservice.service;

import hospital.coreservice.client.AuthClient;
import hospital.coreservice.dto.nurse.NurseCreateDto;
import hospital.coreservice.dto.nurse.NurseResponseDto;
import hospital.coreservice.exception.nurse.NurseNotFoundException;
import hospital.coreservice.mapper.NurseMapper;
import hospital.coreservice.model.Nurse;
import hospital.coreservice.model.enums.NursePosition;
import hospital.coreservice.repository.DepartmentRepository;
import hospital.coreservice.repository.NurseRepository;
import hospital.coreservice.service.imp.NurseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("NurseService Tests - CoreService")
class NurseServiceTest {

    @Mock private NurseRepository nurseRepository;
    @Mock private NurseMapper nurseMapper;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private AuthClient authClient;

    @InjectMocks
    private NurseServiceImpl nurseService;

    private Nurse nurse;
    private NurseCreateDto createDto;
    private NurseResponseDto responseDto;

    @BeforeEach
    void setUp() {
        nurse = new Nurse();
        nurse.setId(1L);
        nurse.setFirstName("فاطمه");
        nurse.setLastName("حسینی");
        nurse.setNurseCode("NUR-001");
        nurse.setPosition(NursePosition.SENIOR_NURSE);
        nurse.setActive(true);

        createDto = new NurseCreateDto();
        createDto.setUserId(10L);  // important for authClient call
        createDto.setFirstName("فاطمه");
        createDto.setLastName("حسینی");
        createDto.setNurseCode("NUR-001");
        createDto.setPosition(NursePosition.SENIOR_NURSE);

        responseDto = new NurseResponseDto();
        responseDto.setId(1L);
        responseDto.setFirstName("فاطمه");
        responseDto.setLastName("حسینی");
        responseDto.setNurseCode("NUR-001");
    }

    @Test
    @DisplayName("Should create nurse")
    void shouldCreateNurse() {
        doNothing().when(authClient).validateUserHasRole(anyLong(), anyString());
        when(nurseMapper.toEntity(any())).thenReturn(nurse);
        when(nurseRepository.save(any())).thenReturn(nurse);
        when(nurseMapper.toResponseDto(any())).thenReturn(responseDto);

        NurseResponseDto result = nurseService.createNurse(createDto);

        assertThat(result).isNotNull();
        verify(nurseRepository).save(any());
        verify(authClient).validateUserHasRole(10L, "NURSE");
    }

    @Test
    @DisplayName("Should get nurse by id")
    void shouldGetById() {
        when(nurseRepository.findById(1L)).thenReturn(Optional.of(nurse));
        when(nurseMapper.toResponseDto(any())).thenReturn(responseDto);

        NurseResponseDto result = nurseService.getNurseById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should return active nurses")
    void shouldReturnActiveNurses() {
        when(nurseRepository.findAllActive()).thenReturn(List.of(nurse));
        when(nurseMapper.toResponseDto(any())).thenReturn(responseDto);

        List<NurseResponseDto> result = nurseService.getAllActiveNurses();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Should throw when nurse not found")
    void shouldThrowWhenNotFound() {
        when(nurseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> nurseService.getNurseById(999L))
                .isInstanceOf(NurseNotFoundException.class);
    }

    @Test
    @DisplayName("Should count active nurses")
    void shouldCountActiveNurses() {
        when(nurseRepository.countActive()).thenReturn(15L);

        Long count = nurseService.countActiveNurses();

        assertThat(count).isEqualTo(15L);
    }
}
