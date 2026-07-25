package hospital.coreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hospital.coreservice.dto.department.DepartmentCreateDto;
import hospital.coreservice.dto.department.DepartmentResponseDto;
import hospital.coreservice.service.DepartmentService;
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

@WebMvcTest(DepartmentApi.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("DepartmentApi Controller Tests")
class DepartmentApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartmentService departmentService;

    private DepartmentCreateDto createDto;
    private DepartmentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        createDto = new DepartmentCreateDto();
        createDto.setDepartmentCode("CARD");
        createDto.setDepartmentName("قلب و عروق");

        responseDto = new DepartmentResponseDto();
        responseDto.setId(1L);
        responseDto.setDepartmentCode("CARD");
        responseDto.setDepartmentName("قلب و عروق");
    }

    @Test
    @DisplayName("POST /api/v1/departments - Create")
    void createDepartment_Success() throws Exception {
        when(departmentService.createDepartment(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.departmentCode").value("CARD"));
    }

    @Test
    @DisplayName("GET /api/v1/departments/active")
    void getActiveDepartments_Success() throws Exception {
        when(departmentService.getActiveDepartments()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/departments/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("PUT /api/v1/departments/{id}")
    void updateDepartment_Success() throws Exception {
        when(departmentService.updateDepartment(anyLong(), any())).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/departments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk());
    }
}