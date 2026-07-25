package hospital.coreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hospital.coreservice.dto.doctor.DoctorCreateDto;
import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.model.enums.Speciality;
import hospital.coreservice.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorApi.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("DoctorApi Controller Tests")
class DoctorApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorService doctorService;

    private DoctorCreateDto createDto;
    private DoctorResponseDto responseDto;

    @BeforeEach
    void setUp() {
        createDto = new DoctorCreateDto();
        createDto.setFirstName("دکتر");
        createDto.setLastName("محمدی");
        createDto.setLicenseNumber("LIC-12345");
        createDto.setSpeciality(Speciality.CARDIOLOGY);

        responseDto = new DoctorResponseDto();
        responseDto.setId(1L);
        responseDto.setFirstName("دکتر");
        responseDto.setLastName("محمدی");
        responseDto.setLicenseNumber("LIC-12345");
    }

    @Test
    @DisplayName("POST /api/v1/doctor - Create doctor")
    void createDoctor_Success() throws Exception {
        when(doctorService.createDoctor(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.licenseNumber").value("LIC-12345"));
    }

    @Test
    @DisplayName("GET /api/v1/doctor/{id}")
    void getDoctorById_Success() throws Exception {
        when(doctorService.getDoctorById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/doctor/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("GET /api/v1/doctor/by-speciality")
    void getBySpeciality_Success() throws Exception {
        when(doctorService.getDoctorsBySpeciality(any())).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/doctor/by-speciality")
                        .param("speciality", "CARDIOLOGY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/doctor/active")
    void getActiveDoctors_Success() throws Exception {
        when(doctorService.getActiveDoctors()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/doctor/active"))
                .andExpect(status().isOk());
    }
}