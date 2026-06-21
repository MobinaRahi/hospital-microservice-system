//package hospital.coreservice.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import hospital.coreservice.dto.appointment.AppointmentCreateDto;
//import hospital.coreservice.dto.appointment.AppointmentResponseDto;
//import hospital.coreservice.dto.appointment.AppointmentUpdateDto;
//import hospital.coreservice.dto.doctor.DoctorResponseDto;
//import hospital.coreservice.dto.patient.PatientResponseDto;
//import hospital.coreservice.dto.department.DepartmentResponseDto;
//import hospital.coreservice.model.enums.AppointmentStatus;
//import hospital.coreservice.model.enums.AppointmentType;
//import hospital.coreservice.service.AppointmentService;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//@WebMvcTest(AppointmentApi.class)
//@AutoConfigureMockMvc(addFilters = false)
//class AppointmentApiTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private AppointmentService appointmentService;
//
//    private AppointmentCreateDto createDto;
//    private AppointmentResponseDto responseDto;
//    private AppointmentUpdateDto updateDto;
//
//    @BeforeEach
//    void setUp() {
//
//        createDto = new AppointmentCreateDto();
//        createDto.setPatientId(1L);
//        createDto.setDoctorId(1L);
//        createDto.setDepartmentId(1L);
//        createDto.setAppointmentDate(LocalDate.now().plusDays(1));
//        createDto.setStartTime(LocalTime.of(10, 0));
//        createDto.setEndTime(LocalTime.of(10, 30));
//        createDto.setReason("درد قفسه سینه");
//        createDto.setType(AppointmentType.IN_PERSON);
//
//        responseDto = new AppointmentResponseDto();
//        responseDto.setId(100L);
//        responseDto.setStatus(AppointmentStatus.SCHEDULED);
//        responseDto.setType(AppointmentType.IN_PERSON);
//        responseDto.setAppointmentDate(LocalDate.now().plusDays(1));
//        responseDto.setStartTime(LocalTime.of(10, 0));
//        responseDto.setEndTime(LocalTime.of(10, 30));
//        responseDto.setReason("درد قفسه سینه");
//        responseDto.setNotes("فقط ویزیت معمولی");
//        responseDto.setCreatedAt(LocalDateTime.now());
//        responseDto.setCreatedBy(10L);
//
//        PatientResponseDto patientDto = new PatientResponseDto();
//        patientDto.setId(1L);
//        patientDto.setFirstName("احمد");
//        patientDto.setLastName("رضایی");
//        responseDto.setPatient(patientDto);
//
//        DoctorResponseDto doctorDto = new DoctorResponseDto();
//        doctorDto.setId(1L);
//        doctorDto.setFirstName("دکتر");
//        doctorDto.setLastName("محمدی");
//        responseDto.setDoctor(doctorDto);
//
//        DepartmentResponseDto departmentDto = new DepartmentResponseDto();
//        departmentDto.setId(1L);
//        departmentDto.setDepartmentName("داخلی");
//        responseDto.setDepartment(departmentDto);
//
//        updateDto = new AppointmentUpdateDto();
//        updateDto.setId(1L);
//        updateDto.setReason("تغییر دلیل");
//        updateDto.setStatus(AppointmentStatus.SCHEDULED);
//        updateDto.setType(AppointmentType.VIDEO);
//    }
//
//    @Test
//    void createAppointment_Success_ReturnsCreated() throws Exception {
//        // WHEN: به mock می‌گوییم اگر متد createAppointment با هر AppointmentCreateDtoای صدا شد، responseDto را برگردان.
//        when(appointmentService.createAppointment(any(AppointmentCreateDto.class))).thenReturn(responseDto);
//
//        // THEN: درخواست POST را با بدنهٔ createDto می‌فرستیم.
//        mockMvc.perform(post("/api/v1/appointments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createDto)))
//                // بررسی می‌کنیم که وضعیت HTTP 201 (Created) باشد.
//                .andExpect(status().isCreated())
//                // فیلد success در پاسخ برابر true باشد.
//                .andExpect(jsonPath("$.success").value(true))
//                // پیام موفقیت را بررسی می‌کنیم.
//                .andExpect(jsonPath("$.message").value("Appointment created successfully"))
//                // داده برگشتی باید شامل id برابر 100 باشد.
//                .andExpect(jsonPath("$.data.id").value(100));
//
//        // اطمینان از اینکه متد createAppointment دقیقاً یک بار صدا زده شده.
//        verify(appointmentService, times(1)).createAppointment(any(AppointmentCreateDto.class));
//    }
//
//    @Test
//    void createAppointment_InvalidInput_ReturnsBadRequest() throws Exception {
//        // بدنهٔ خالی می‌فرستیم (هیچ فیلدی مقدار ندارد) تا اعتبارسنجی فعال شود.
//        AppointmentCreateDto invalidDto = new AppointmentCreateDto();
//
//        mockMvc.perform(post("/api/v1/appointments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidDto)))
//                // انتظار داریم خطای 400 (Bad Request) برگردد.
//                .andExpect(status().isBadRequest());
//
//        // مطمئن می‌شویم که سرویس هرگز صدا زده نشده است.
//        verify(appointmentService, never()).createAppointment(any());
//    }
//
//    // ==================== تست‌های ویرایش نوبت (PUT) ====================
//
//    @Test
//    void updateAppointment_Success_ReturnsOk() throws Exception {
//        when(appointmentService.updateAppointment(eq(100L), any(AppointmentUpdateDto.class)))
//                .thenReturn(responseDto);
//
//        mockMvc.perform(put("/api/v1/appointments/{id}", 100L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.id").value(100));
//
//        verify(appointmentService, times(1)).updateAppointment(eq(100L), any());
//    }
//
//    @Test
//    void updateAppointment_NotFound_ReturnsNotFound() throws Exception {
//        // شبیه‌سازی خطای "پیدا نشد" با استفاده از EntityNotFoundException که در GlobalExceptionHandler به 404 تبدیل می‌شود.
//        when(appointmentService.updateAppointment(eq(999L), any(AppointmentUpdateDto.class)))
//                .thenThrow(new EntityNotFoundException("Appointment not found with id: 999"));
//
//        mockMvc.perform(put("/api/v1/appointments/{id}", 999L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDto)))
//                .andExpect(status().isNotFound());
//    }
//
//    // ==================== تست‌های لغو نوبت (PATCH cancel) ====================
//
//    @Test
//    void cancelAppointment_Success_ReturnsOk() throws Exception {
//        // برای متدهای void از doNothing استفاده می‌کنیم.
//        doNothing().when(appointmentService).cancelAppointment(eq(100L), eq("بیمار انصراف داد"), eq(2L));
//
//        mockMvc.perform(patch("/api/v1/appointments/{id}/cancel", 100L)
//                        .param("reason", "بیمار انصراف داد")
//                        .param("canceledBy", "2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Appointment cancelled successfully"));
//
//        verify(appointmentService, times(1)).cancelAppointment(eq(100L), anyString(), eq(2L));
//    }
//
//    // ==================== تست‌های check-in و complete ====================
//
//    @Test
//    void checkInAppointment_Success_ReturnsOk() throws Exception {
//        doNothing().when(appointmentService).checkInAppointment(100L);
//        mockMvc.perform(patch("/api/v1/appointments/{id}/check-in", 100L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Appointment checked in successfully"));
//    }
//
//    @Test
//    void completeAppointment_Success_ReturnsOk() throws Exception {
//        doNothing().when(appointmentService).completeAppointment(100L);
//        mockMvc.perform(patch("/api/v1/appointments/{id}/complete", 100L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Appointment completed successfully"));
//    }
//
//    // ==================== تست تغییر زمان نوبت (reschedule) ====================
//
//    @Test
//    void rescheduleAppointment_Success_ReturnsOk() throws Exception {
//        LocalDate newDate = LocalDate.now().plusDays(2);
//        LocalTime newStart = LocalTime.of(11, 0);
//        LocalTime newEnd = LocalTime.of(11, 30);
//
//        when(appointmentService.rescheduleAppointment(eq(100L), eq(newDate), eq(newStart), eq(newEnd)))
//                .thenReturn(responseDto);
//
//        mockMvc.perform(put("/api/v1/appointments/{id}/reschedule", 100L)
//                        .param("newDate", newDate.toString())
//                        .param("newStartTime", newStart.toString())
//                        .param("newEndTime", newEnd.toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id").value(100));
//    }
//
//    // ==================== تست گرفتن نوبت با شناسه ====================
//
//    @Test
//    void getAppointmentById_Success_ReturnsOk() throws Exception {
//        when(appointmentService.getAppointmentById(100L)).thenReturn(responseDto);
//        mockMvc.perform(get("/api/v1/appointments/{id}", 100L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id").value(100));
//    }
//
//    @Test
//    void getAppointmentById_NotFound_ReturnsNotFound() throws Exception {
//        when(appointmentService.getAppointmentById(999L))
//                .thenThrow(new EntityNotFoundException("Appointment not found with id: 999"));
//        mockMvc.perform(get("/api/v1/appointments/{id}", 999L))
//                .andExpect(status().isNotFound());
//    }
//
//    // ==================== تست گرفتن نوبت‌های یک بیمار ====================
//
//    @Test
//    void getAppointmentsByPatientId_Success() throws Exception {
//        when(appointmentService.getAppointmentsByPatientId(1L)).thenReturn(List.of(responseDto));
//        mockMvc.perform(get("/api/v1/appointments/patient/{patientId}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.length()").value(1)); // لیست باید یک عنصر داشته باشد
//    }
//
//    // ==================== تست گرفتن نوبت‌های یک پزشک ====================
//
//    @Test
//    void getAppointmentsByDoctorId_Success() throws Exception {
//        when(appointmentService.getAppointmentsByDoctorId(1L)).thenReturn(List.of(responseDto));
//        mockMvc.perform(get("/api/v1/appointments/doctor/{doctorId}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.length()").value(1));
//    }
//
//    // ==================== تست فیلتر بر اساس وضعیت ====================
//
//    @Test
//    void getAppointmentsByStatus_Success() throws Exception {
//        when(appointmentService.getAppointmentsByStatus(AppointmentStatus.SCHEDULED))
//                .thenReturn(List.of(responseDto));
//        mockMvc.perform(get("/api/v1/appointments/by-status")
//                        .param("status", "SCHEDULED"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.length()").value(1));
//    }
//
//    // ==================== تست فیلتر بر اساس تاریخ ====================
//
//    @Test
//    void getAppointmentsByDate_Success() throws Exception {
//        LocalDate date = LocalDate.now().plusDays(1);
//        when(appointmentService.getAppointmentsByDate(date)).thenReturn(List.of(responseDto));
//        mockMvc.perform(get("/api/v1/appointments/by-date")
//                        .param("date", date.toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.length()").value(1));
//    }
//
//    // ==================== تست بازه تاریخ ====================
//
//    @Test
//    void getAppointmentsByDateRange_Success() throws Exception {
//        LocalDate start = LocalDate.now();
//        LocalDate end = LocalDate.now().plusDays(7);
//        when(appointmentService.getAppointmentsByDateRange(start, end)).thenReturn(List.of(responseDto));
//        mockMvc.perform(get("/api/v1/appointments/by-date-range")
//                        .param("startDate", start.toString())
//                        .param("endDate", end.toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.length()").value(1));
//    }
//
//    // ==================== تست‌های زمانی (امروز، این هفته، این ماه) ====================
//
//    @Test
//    void getTodayAppointments_Success() throws Exception {
//        when(appointmentService.getTodayAppointments()).thenReturn(List.of(responseDto));
//        mockMvc.perform(get("/api/v1/appointments/time/today"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.length()").value(1));
//    }
//
//    @Test
//    void getThisWeekAppointments_Success() throws Exception {
//        when(appointmentService.getThisWeekAppointments()).thenReturn(List.of(responseDto));
//        mockMvc.perform(get("/api/v1/appointments/time/this-week"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void getThisMonthAppointments_Success() throws Exception {
//        when(appointmentService.getThisMonthAppointments()).thenReturn(List.of(responseDto));
//        mockMvc.perform(get("/api/v1/appointments/time/this-month"))
//                .andExpect(status().isOk());
//    }
//
//    // ==================== تست فیلتر زمانی با createdAfter ====================
//    @Test
//    void getAppointmentsCreatedAfter_Success() throws Exception {
//        LocalDateTime time = LocalDateTime.now().minusDays(1);
//        String formatted = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        when(appointmentService.getAppointmentsByCreatedAtAfter(time)).thenReturn(List.of(responseDto));
//        mockMvc.perform(get("/api/v1/appointments/time/created-after")
//                        .param("createdAtAfter", formatted))
//                .andExpect(status().isOk());
//    }
//
//    // ==================== تست مخصوص بیمار: نوبت‌های آینده و گذشته ====================
//    @Test
//    void getUpcomingAppointments_Success() throws Exception {
//        when(appointmentService.getUpcomingAppointments(1L)).thenReturn(List.of(responseDto));
//        mockMvc.perform(get("/api/v1/appointments/patient/{patientId}/upcoming", 1L))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void getPastAppointments_Success() throws Exception {
//        when(appointmentService.getPastAppointments(1L)).thenReturn(List.of(responseDto));
//        mockMvc.perform(get("/api/v1/appointments/patient/{patientId}/past", 1L))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void countAppointmentsByPatient_Success() throws Exception {
//        when(appointmentService.countAppointmentsByPatientAndStatus(eq(1L), eq(AppointmentStatus.SCHEDULED)))
//                .thenReturn(5L);
//        mockMvc.perform(get("/api/v1/appointments/patient/count/{patientId}", 1L)
//                        .param("status", "SCHEDULED"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(5));
//    }
//
//    // ==================== تست مخصوص پزشک: دریافت ساعات خالی ====================
//    @Test
//    void getAvailableSlots_Success() throws Exception {
//        List<LocalTime> slots = List.of(LocalTime.of(9, 0), LocalTime.of(9, 30));
//        when(appointmentService.getAvailableSlots(eq(1L), any(LocalDate.class))).thenReturn(slots);
//        mockMvc.perform(get("/api/v1/appointments/doctor/available")
//                        .param("doctorId", "1")
//                        .param("date", LocalDate.now().plusDays(1).toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.length()").value(2));
//    }
//
//    // ==================== تست آمار ====================
//    @Test
//    void countTotalAppointments_Success() throws Exception {
//        when(appointmentService.countTotalAppointments()).thenReturn(100L);
//        mockMvc.perform(get("/api/v1/appointments/count"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(100));
//    }
//
//    // ==================== تست نوبت‌های حاضر نشده ====================
//    @Test
//    void getNoShowAppointments_Success() throws Exception {
//        when(appointmentService.getNoShowAppointments()).thenReturn(List.of(responseDto));
//        mockMvc.perform(get("/api/v1/appointments/no-show"))
//                .andExpect(status().isOk());
//    }
//}
