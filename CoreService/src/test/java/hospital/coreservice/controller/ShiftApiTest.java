package hospital.coreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hospital.coreservice.dto.shift.ShiftCreateDto;
import hospital.coreservice.dto.shift.ShiftResponseDto;
import hospital.coreservice.service.ShiftService;
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

@WebMvcTest(ShiftApi.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ShiftApi Controller Tests")
class ShiftApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShiftService shiftService;

    private ShiftCreateDto createDto;
    private ShiftResponseDto responseDto;

    @BeforeEach
    void setUp() {
        createDto = new ShiftCreateDto();
        createDto.setName("صبح");
        createDto.setNightShift(false);

        responseDto = new ShiftResponseDto();
        responseDto.setId(1L);
        responseDto.setName("صبح");
    }

    @Test
    @DisplayName("POST /api/v1/shift - Create shift")
    void createShift_Success() throws Exception {
        when(shiftService.createShift(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/shift")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("صبح"));
    }

    @Test
    @DisplayName("GET /api/v1/shift/active")
    void getActiveShifts_Success() throws Exception {
        when(shiftService.getActiveShifts()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/shift/active"))
                .andExpect(status().isOk());
    }
}