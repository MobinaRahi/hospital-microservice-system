package hospital.coreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hospital.coreservice.dto.appointment.AppointmentCreateDto;
import hospital.coreservice.dto.appointment.AppointmentResponseDto;
import hospital.coreservice.dto.appointment.AppointmentUpdateDto;
import hospital.coreservice.dto.appointment.TimeSlotResponseDto;
import hospital.coreservice.dto.doctor.DoctorResponseDto;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.department.DepartmentResponseDto;

import hospital.coreservice.model.enums.AppointmentStatus;
import hospital.coreservice.model.enums.AppointmentType;
import hospital.coreservice.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentApi.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AppointmentApi Controller Tests")
class AppointmentApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    private AppointmentCreateDto createDto;
    private AppointmentResponseDto responseDto;
    private AppointmentUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        createDto = new AppointmentCreateDto();
        createDto.setPatientId(1L);
        createDto.setDoctorId(1L);
        createDto.setDepartmentId(1L);
        createDto.setAppointmentDate(LocalDate.now().plusDays(1));
        createDto.setStartTime(LocalTime.of(10, 0));
        createDto.setEndTime(LocalTime.of(10, 30));
        createDto.setReason("درد قفسه سینه");
        createDto.setType(AppointmentType.IN_PERSON);

        responseDto = new AppointmentResponseDto();
        responseDto.setId(100L);
        responseDto.setStatus(AppointmentStatus.SCHEDULED);
        responseDto.setType(AppointmentType.IN_PERSON);
        responseDto.setAppointmentDate(LocalDate.now().plusDays(1));
        responseDto.setStartTime(LocalTime.of(10, 0));
        responseDto.setEndTime(LocalTime.of(10, 30));
        responseDto.setReason("درد قفسه سینه");

        PatientResponseDto patient = new PatientResponseDto();
        patient.setId(1L);
        patient.setFirstName("علی");
        responseDto.setPatient(patient);

        DoctorResponseDto doctor = new DoctorResponseDto();
        doctor.setId(1L);
        doctor.setFirstName("دکتر");
        responseDto.setDoctor(doctor);

        DepartmentResponseDto dept = new DepartmentResponseDto();
        dept.setId(1L);
        dept.setDepartmentName("داخلی");
        responseDto.setDepartment(dept);

        updateDto = new AppointmentUpdateDto();
        updateDto.setReason("تغییر دلیل");
        updateDto.setType(AppointmentType.VIDEO);
    }

    @Test
    @DisplayName("POST /api/v1/appointments - Create successfully")
    void createAppointment_Success() throws Exception {
        when(appointmentService.createAppointment(any(AppointmentCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Appointment created successfully"))
                .andExpect(jsonPath("$.data.id").value(100L));

        verify(appointmentService).createAppointment(any(AppointmentCreateDto.class));
    }

    @Test
    @DisplayName("GET /api/v1/appointments/{id} - Get by ID")
    void getAppointmentById_Success() throws Exception {
        when(appointmentService.getAppointmentById(100L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/appointments/{id}", 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(100L));
    }

    @Test
    @DisplayName("PUT /api/v1/appointments/{id} - Update")
    void updateAppointment_Success() throws Exception {
        when(appointmentService.updateAppointment(eq(100L), any(AppointmentUpdateDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/appointments/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("PATCH /api/v1/appointments/{id}/cancel - Cancel")
    void cancelAppointment_Success() throws Exception {
        doNothing().when(appointmentService).cancelAppointment(eq(100L), anyString(), anyLong());

        mockMvc.perform(patch("/api/v1/appointments/{id}/cancel", 100L)
                        .param("reason", "بیمار انصراف داد")
                        .param("canceledBy", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Appointment cancelled successfully"));
    }

    @Test
    @DisplayName("GET /api/v1/appointments/patient/{patientId} - By patient")
    void getByPatientId_Success() throws Exception {
        when(appointmentService.getAppointmentsByPatientId(1L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/appointments/patient/{patientId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/appointments/doctor/available - Available slots")
    void getAvailableSlots_Success() throws Exception {
        when(appointmentService.getAvailableSlots(eq(1L), any(LocalDate.class)))
                .thenReturn(List.of(new TimeSlotResponseDto()));

        mockMvc.perform(get("/api/v1/appointments/doctor/available")
                        .param("doctorId", "1")
                        .param("date", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/appointments/count - Total count")
    void countTotalAppointments_Success() throws Exception {
        when(appointmentService.countTotalAppointments()).thenReturn(150L);

        mockMvc.perform(get("/api/v1/appointments/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(150L));
    }
}