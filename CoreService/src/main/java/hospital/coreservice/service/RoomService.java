package hospital.coreservice.service;

import hospital.coreservice.dto.room.RoomCreateDto;
import hospital.coreservice.dto.room.RoomResponseDto;
import hospital.coreservice.dto.room.RoomUpdateDto;
import hospital.coreservice.dto.patient.PatientResponseDto;

import java.util.List;

/**
 * Service interface for Room management.
 * Handles CRUD, occupancy tracking, and patient assignment.
 *
 * @author Mobina
 */
public interface RoomService {

    RoomResponseDto createRoom(RoomCreateDto roomCreateDto);
    RoomResponseDto updateRoom(Long roomId, RoomUpdateDto roomUpdateDto);
    List<RoomResponseDto> getAllRooms();
    RoomResponseDto getRoomById(Long roomId);
    RoomResponseDto getRoomByRoomNumber(String roomNumber);
    List<RoomResponseDto> getRoomsByDepartmentId(Long departmentId);
    List<RoomResponseDto> getActiveRoomsByDepartmentId(Long departmentId);
    List<RoomResponseDto> getAvailableRooms();
    List<RoomResponseDto> getOccupiedRooms();
    List<RoomResponseDto> getOccupiedRoomsByDepartmentId(Long departmentId);
    List<RoomResponseDto> getRoomsByCapacity(int capacity);
    List<RoomResponseDto> getActiveRoomsByCapacity(int capacity);
    List<RoomResponseDto> getRoomsByCapacityGreaterThan(int capacity);
    List<RoomResponseDto> getActiveRoomsByCapacityGreaterThan(int capacity);
    List<RoomResponseDto> getRoomsByCapacityLessThan(int capacity);
    List<RoomResponseDto> getActiveRoomsByCapacityLessThan(int capacity);
    List<RoomResponseDto> getRoomsByCapacityRange(int minCapacity, int maxCapacity);
    List<RoomResponseDto> getActiveRoomsByCapacityRange(int minCapacity, int maxCapacity);
    List<RoomResponseDto> searchRooms(String roomNumber, Long departmentId, Boolean isOccupied);
    void assignPatientToRoom(Long roomId, Long patientId);
    void removePatientFromRoom(Long roomId, Long patientId);
    List<PatientResponseDto> getCurrentPatientsOfRoom(Long roomId);
    void occupyRoom(Long roomId);
    void freeRoom(Long roomId);
    Long countAvailableRooms();
    Long countOccupiedRooms();
    Long countRoomsByDepartmentId(Long departmentId);
    Long countAllRooms();
    boolean isRoomNumberUnique(String roomNumber);
    void activateRoom(Long roomId);
    void deactivateRoom(Long roomId);
}