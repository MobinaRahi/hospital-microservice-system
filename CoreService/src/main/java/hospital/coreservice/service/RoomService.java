package hospital.coreservice.service;

import hospital.coreservice.dto.room.RoomCreateDto;
import hospital.coreservice.dto.room.RoomResponseDto;
import hospital.coreservice.dto.room.RoomUpdateDto;
import hospital.coreservice.dto.patient.PatientResponseDto;

import java.util.List;

/**
 * Service interface for Room management.
 *
 * @author Mobina
 */
public interface RoomService {

    // ========== Core Operations ==========

    /**
     * Create new room
     */
    RoomResponseDto createRoom(RoomCreateDto roomCreateDto);

    /**
     * Update existing room
     */
    RoomResponseDto updateRoom(Long roomId, RoomUpdateDto roomUpdateDto);

    /**
     * Delete room (physical or logical)
     */
    RoomResponseDto deleteRoom(Long roomId);

    // ========== Basic Retrieval ==========

    /**
     * Get all rooms
     */
    List<RoomResponseDto> getAllRooms();

    /**
     * Get room by ID
     */
    RoomResponseDto getRoomById(Long roomId);

    /**
     * Get room by room number (unique)
     */
    RoomResponseDto getRoomByRoomNumber(String roomNumber);

    /**
     * Get rooms by department ID
     */
    List<RoomResponseDto> getRoomsByDepartmentId(Long departmentId);

    // ========== Search & Filter ==========

    /**
     * Get available (empty) rooms
     */
    List<RoomResponseDto> getAvailableRooms();

    /**
     * Get occupied rooms
     */
    List<RoomResponseDto> getOccupiedRooms();

    /**
     * Get occupied rooms by department ID
     */
    List<RoomResponseDto> getOccupiedRoomsByDepartmentId(Long departmentId);

    /**
     * Get rooms by exact capacity
     */
    List<RoomResponseDto> getRoomsByCapacity(int capacity);

    /**
     * Get rooms with capacity greater than given value
     */
    List<RoomResponseDto> getRoomsByCapacityGreaterThan(int capacity);

    /**
     * Get rooms with capacity less than given value
     */
    List<RoomResponseDto> getRoomsByCapacityLessThan(int capacity);

    /**
     * Get rooms by capacity range (min - max)
     */
    List<RoomResponseDto> getRoomsByCapacityRange(int minCapacity, int maxCapacity);

    /**
     * Dynamic search with optional filters
     */
    List<RoomResponseDto> searchRooms(String roomNumber, Long departmentId, Boolean isOccupied);

    // ========== Patient Management ==========

    /**
     * Assign a patient to a room (bidirectional)
     */
    void assignPatientToRoom(Long roomId, Long patientId);

    /**
     * Remove a patient from a room
     */
    void removePatientFromRoom(Long roomId, Long patientId);

    /**
     * Get list of patients currently in a room
     */
    List<PatientResponseDto> getCurrentPatientsOfRoom(Long roomId);

    // ========== Statistics ==========

    /**
     * Count available (empty) rooms
     */
    Long countAvailableRooms();

    /**
     * Count occupied rooms
     */
    Long countOccupiedRooms();

    /**
     * Count rooms by department ID
     */
    Long countRoomsByDepartmentId(Long departmentId);

    /**
     * Count total rooms
     */
    Long countAllRooms();

    // ========== Validation ==========

    /**
     * Check if room number is unique
     */
    boolean isRoomNumberUnique(String roomNumber);
}