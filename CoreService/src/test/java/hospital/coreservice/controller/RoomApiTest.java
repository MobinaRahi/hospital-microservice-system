package hospital.coreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hospital.coreservice.dto.room.RoomCreateDto;
import hospital.coreservice.dto.room.RoomResponseDto;
import hospital.coreservice.service.RoomService;
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

@WebMvcTest(RoomApi.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("RoomApi Controller Tests")
class RoomApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomService roomService;

    private RoomCreateDto createDto;
    private RoomResponseDto responseDto;

    @BeforeEach
    void setUp() {
        createDto = new RoomCreateDto();
        createDto.setRoomNumber("R-101");
        createDto.setCapacity(2);

        responseDto = new RoomResponseDto();
        responseDto.setId(1L);
        responseDto.setRoomNumber("R-101");
        responseDto.setCapacity(2);
    }

    @Test
    @DisplayName("POST /api/v1/room - Create room")
    void createRoom_Success() throws Exception {
        when(roomService.createRoom(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.roomNumber").value("R-101"));
    }

    @Test
    @DisplayName("GET /api/v1/room/available")
    void getAvailableRooms_Success() throws Exception {
        when(roomService.getAvailableRooms()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/room/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("PATCH /api/v1/room/occupy/{id}")
    void occupyRoom_Success() throws Exception {
        mockMvc.perform(patch("/api/v1/room/occupy/{id}", 1L))
                .andExpect(status().isOk());
    }
}