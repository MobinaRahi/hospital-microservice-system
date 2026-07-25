package hospital.coreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleCreateDto;
import hospital.coreservice.dto.doctor_schedule.DoctorScheduleResponseDto;
import hospital.coreservice.model.enums.DayOfWeek;
import hospital.coreservice.service.DoctorScheduleService;
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

@WebMvcTest(DoctorScheduleApi.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("DoctorScheduleApi Controller Tests")
class DoctorScheduleApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorScheduleService doctorScheduleService;

    private DoctorScheduleCreateDto createDto;
    private DoctorScheduleResponseDto responseDto;

    @BeforeEach
    void setUp() {
        createDto = new DoctorScheduleCreateDto();
        createDto.setDoctorId(1L);
        createDto.setDayOfWeek(DayOfWeek.MONDAY);

        responseDto = new DoctorScheduleResponseDto();
        responseDto.setId(1L);
        responseDto.setDoctorId(1L);
        responseDto.setDayOfWeek(DayOfWeek.MONDAY);
    }

    @Test
    @DisplayName("POST /api/v1/doctor-schedules - Create")
    void createDoctorSchedule_Success() throws Exception {
        when(doctorScheduleService.createDoctorSchedule(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/doctor-schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("GET /api/v1/doctor-schedules/active")
    void getActiveSchedules_Success() throws Exception {
        when(doctorScheduleService.getActiveDoctorSchedules()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/doctor-schedules/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/doctor-schedules/doctor/{doctorId}")
    void getByDoctorId_Success() throws Exception {
        when(doctorScheduleService.getDoctorSchedulesByDoctorId(1L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/doctor-schedules/doctor/{doctorId}", 1L))
                .andExpect(status().isOk());
    }
}