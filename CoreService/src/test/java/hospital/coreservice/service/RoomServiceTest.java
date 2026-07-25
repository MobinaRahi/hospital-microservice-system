package hospital.coreservice.service;

import hospital.coreservice.dto.room.RoomCreateDto;
import hospital.coreservice.dto.room.RoomResponseDto;
import hospital.coreservice.exception.room.DuplicateRoomNumberException;
import hospital.coreservice.exception.room.RoomNotFoundException;
import hospital.coreservice.mapper.RoomMapper;
import hospital.coreservice.model.Patient;
import hospital.coreservice.model.Room;
import hospital.coreservice.repository.PatientRepository;
import hospital.coreservice.repository.RoomRepository;
import hospital.coreservice.service.imp.RoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomService Tests - CoreService")
class RoomServiceTest {

    @Mock private RoomRepository roomRepository;
    @Mock private RoomMapper roomMapper;
    @Mock private PatientRepository patientRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room room;
    private RoomCreateDto createDto;
    private RoomResponseDto responseDto;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setRoomNumber("101");
        room.setCapacity(2);
        room.setOccupied(false);
        room.setActive(true);

        createDto = new RoomCreateDto();
        createDto.setRoomNumber("101");
        createDto.setCapacity(2);

        responseDto = new RoomResponseDto();
        responseDto.setId(1L);
        responseDto.setRoomNumber("101");
    }

    @Nested
    @DisplayName("Create & Update")
    class CreateUpdate {

        @Test
        @DisplayName("Should create room successfully")
        void shouldCreateRoom() {
            when(roomRepository.existsByRoomNumber(any())).thenReturn(false);
            when(roomMapper.toEntity(any())).thenReturn(room);
            when(roomRepository.save(any())).thenReturn(room);
            when(roomMapper.toResponseDto(any())).thenReturn(responseDto);

            RoomResponseDto result = roomService.createRoom(createDto);

            assertThat(result).isNotNull();
            verify(roomRepository).save(any());
        }

        @Test
        @DisplayName("Should throw on duplicate room number")
        void shouldThrowOnDuplicateRoomNumber() {
            when(roomRepository.existsByRoomNumber("101")).thenReturn(true);

            assertThatThrownBy(() -> roomService.createRoom(createDto))
                    .isInstanceOf(DuplicateRoomNumberException.class);
        }
    }

    @Nested
    @DisplayName("Retrieval & Filtering")
    class Retrieval {

        @Test
        @DisplayName("Should get room by id")
        void shouldGetById() {
            when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
            when(roomMapper.toResponseDto(any())).thenReturn(responseDto);

            RoomResponseDto result = roomService.getRoomById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should return available rooms")
        void shouldReturnAvailableRooms() {
            when(roomRepository.findEmptyRooms()).thenReturn(List.of(room));
            when(roomMapper.toResponseDto(any())).thenReturn(responseDto);

            List<RoomResponseDto> result = roomService.getAvailableRooms();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Should return occupied rooms")
        void shouldReturnOccupiedRooms() {
            when(roomRepository.findOccupiedRooms()).thenReturn(List.of(room));
            when(roomMapper.toResponseDto(any())).thenReturn(responseDto);

            List<RoomResponseDto> result = roomService.getOccupiedRooms();

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Patient Assignment")
    class PatientAssignment {

        @Test
        @DisplayName("Should assign patient to room")
        void shouldAssignPatient() {
            Patient patient = new Patient();
            patient.setId(5L);

            when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
            when(patientRepository.findById(5L)).thenReturn(Optional.of(patient));
            when(roomRepository.save(any())).thenReturn(room);

            roomService.assignPatientToRoom(1L, 5L);

            verify(roomRepository).save(any());
        }

        @Test
        @DisplayName("Should remove patient from room")
        void shouldRemovePatient() {
            Patient patient = new Patient();
            patient.setId(5L);
            room.addPatient(patient);

            when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
            when(patientRepository.findById(5L)).thenReturn(Optional.of(patient));

            roomService.removePatientFromRoom(1L, 5L);

            verify(roomRepository).save(any());
        }
    }

    @Test
    @DisplayName("Should count available and occupied rooms")
    void shouldCountRooms() {
        when(roomRepository.countAvailableRooms()).thenReturn(12L);
        when(roomRepository.countOccupiedRooms()).thenReturn(8L);

        assertThat(roomService.countAvailableRooms()).isEqualTo(12L);
        assertThat(roomService.countOccupiedRooms()).isEqualTo(8L);
    }
}
