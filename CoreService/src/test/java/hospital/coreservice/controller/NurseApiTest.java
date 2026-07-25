package hospital.coreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hospital.coreservice.dto.nurse.NurseCreateDto;
import hospital.coreservice.dto.nurse.NurseResponseDto;
import hospital.coreservice.model.enums.NursePosition;
import hospital.coreservice.service.NurseService;
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

@WebMvcTest(NurseApi.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("NurseApi Controller Tests")
class NurseApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NurseService nurseService;

    private NurseCreateDto createDto;
    private NurseResponseDto responseDto;

    @BeforeEach
    void setUp() {
        createDto = new NurseCreateDto();
        createDto.setFirstName("فاطمه");
        createDto.setLastName("حسینی");
        createDto.setNurseCode("NUR-001");
        createDto.setPosition(NursePosition.REGISTERED);

        responseDto = new NurseResponseDto();
        responseDto.setId(1L);
        responseDto.setFirstName("فاطمه");
        responseDto.setNurseCode("NUR-001");
    }

    @Test
    @DisplayName("POST /api/v1/nurse - Create nurse")
    void createNurse_Success() throws Exception {
        when(nurseService.createNurse(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/nurse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nurseCode").value("NUR-001"));
    }

    @Test
    @DisplayName("GET /api/v1/nurse/active")
    void getActiveNurses_Success() throws Exception {
        when(nurseService.getAllActiveNurses()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/nurse/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }
}