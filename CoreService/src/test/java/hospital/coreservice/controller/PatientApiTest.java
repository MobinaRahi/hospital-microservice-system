package hospital.coreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hospital.coreservice.dto.patient.PatientCreateDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.patient.PatientUpdateDto;
import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.model.enums.Gender;
import hospital.coreservice.model.enums.PatientStatus;
import hospital.coreservice.service.PatientService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientApi.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("PatientApi Controller Tests")
class PatientApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    private PatientCreateDto createDto;
    private PatientResponseDto responseDto;

    @BeforeEach
    void setUp() {
        createDto = new PatientCreateDto();
        createDto.setFirstName("علی");
        createDto.setLastName("رضایی");
        createDto.setNationalId("1234567890");
        createDto.setGender(Gender.MAN);

        responseDto = new PatientResponseDto();
        responseDto.setId(1L);
        responseDto.setFirstName("علی");
        responseDto.setLastName("رضایی");
        responseDto.setNationalId("1234567890");
    }

    @Test
    @DisplayName("POST /api/v1/patient - Create patient")
    void createPatient_Success() throws Exception {
        when(patientService.createPatient(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("GET /api/v1/patient/{id}")
    void getPatientById_Success() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/patient/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("GET /api/v1/patient - Get all")
    void getAllPatients_Success() throws Exception {
        when(patientService.getAllPatients()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/patient"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("PATCH /api/v1/patient/deactivate/{id}")
    void deactivatePatient_Success() throws Exception {
        doNothing().when(patientService).deactivatePatient(1L);

        mockMvc.perform(patch("/api/v1/patient/deactivate/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/patient/search")
    void searchPatients_Success() throws Exception {
        when(patientService.searchPatients(any(), any(), any(), any())).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/patient/search")
                        .param("firstName", "علی"))
                .andExpect(status().isOk());
    }
}