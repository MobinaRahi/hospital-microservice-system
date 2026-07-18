package hospital.coreservice.controller;

import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.room.RoomCreateDto;
import hospital.coreservice.dto.room.RoomResponseDto;
import hospital.coreservice.dto.room.RoomUpdateDto;
import hospital.coreservice.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Room management.
 * Handles CRUD, occupancy tracking, and patient assignment.
 *
 * @author Mobina
 */
@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
@Tag(name = "Room Management", description = "Room CRUD and management APIs")
public class RoomApi {

    private final RoomService roomService;

    // ========== Core Operations ==========

    @PostMapping
    @Operation(summary = "Create a new room")
    public ResponseEntity<ApiResponse<RoomResponseDto>> createRoom(@Valid @RequestBody RoomCreateDto roomCreateDto) {
        RoomResponseDto created = roomService.createRoom(roomCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Room created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a room by ID")
    public ResponseEntity<ApiResponse<RoomResponseDto>> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomUpdateDto updateDto) {
        RoomResponseDto updated = roomService.updateRoom(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Room updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/deactivate/{id}")
    @Operation(summary = "Deactivate a room (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deactivateRoom(@PathVariable Long id) {
        roomService.deactivateRoom(id);
        return ResponseEntity.ok(ApiResponse.success("Room deactivated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/activate/{id}")
    @Operation(summary = "Activate a room")
    public ResponseEntity<ApiResponse<Void>> activateRoom(@PathVariable Long id) {
        roomService.activateRoom(id);
        return ResponseEntity.ok(ApiResponse.success("Room activated successfully", HttpStatus.OK.value()));
    }

    // ========== Occupancy Management ==========

    @PatchMapping("/occupy/{id}")
    @Operation(summary = "Mark room as occupied")
    public ResponseEntity<ApiResponse<Void>> occupyRoom(@PathVariable Long id) {
        roomService.occupyRoom(id);
        return ResponseEntity.ok(ApiResponse.success("Room occupied successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/free/{id}")
    @Operation(summary = "Mark room as free")
    public ResponseEntity<ApiResponse<Void>> freeRoom(@PathVariable Long id) {
        roomService.freeRoom(id);
        return ResponseEntity.ok(ApiResponse.success("Room freed successfully", HttpStatus.OK.value()));
    }

    // ========== Patient Assignment ==========

    @PatchMapping("/assign/{roomId}")
    @Operation(summary = "Assign a patient to a room")
    public ResponseEntity<ApiResponse<Void>> assignPatientToRoom(@RequestParam Long patientId, @PathVariable Long roomId) {
        roomService.assignPatientToRoom(patientId, roomId);
        return ResponseEntity.ok(ApiResponse.success("Patient assigned to room successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/remove/{roomId}")
    @Operation(summary = "Remove a patient from a room")
    public ResponseEntity<ApiResponse<Void>> removePatientFromRoom(@RequestParam Long patientId, @PathVariable Long roomId) {
        roomService.removePatientFromRoom(patientId, roomId);
        return ResponseEntity.ok(ApiResponse.success("Patient removed from room successfully", HttpStatus.OK.value()));
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/{id}")
    @Operation(summary = "Get room by ID")
    public ResponseEntity<ApiResponse<RoomResponseDto>> getRoomById(@PathVariable Long id) {
        RoomResponseDto room = roomService.getRoomById(id);
        return ResponseEntity.ok(ApiResponse.success(room, "Room retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-room-number")
    @Operation(summary = "Get room by room number")
    public ResponseEntity<ApiResponse<RoomResponseDto>> getRoomByRoomNumber(@RequestParam String roomNumber) {
        RoomResponseDto room = roomService.getRoomByRoomNumber(roomNumber);
        return ResponseEntity.ok(ApiResponse.success(room, "Room retrieved by number successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/by-department-id")
    @Operation(summary = "Get rooms by department ID")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getRoomsByDepartmentId(@RequestParam Long departmentId) {
        List<RoomResponseDto> rooms = roomService.getRoomsByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(rooms, "Rooms retrieved by department ID successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/current-patient/{roomId}")
    @Operation(summary = "Get current patients of a room")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getCurrentPatientsOfRoom(@PathVariable Long roomId) {
        List<PatientResponseDto> patients = roomService.getCurrentPatientsOfRoom(roomId);
        return ResponseEntity.ok(ApiResponse.success(patients, "Current patients retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all rooms")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getAllRooms() {
        List<RoomResponseDto> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(ApiResponse.success(rooms, "All rooms retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Occupancy Status Filters ==========

    @GetMapping("/available")
    @Operation(summary = "Get all available (empty) rooms")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getAvailableRooms() {
        List<RoomResponseDto> rooms = roomService.getAvailableRooms();
        return ResponseEntity.ok(ApiResponse.success(rooms, "Available rooms retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/occupied")
    @Operation(summary = "Get all occupied rooms")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getOccupiedRooms() {
        List<RoomResponseDto> rooms = roomService.getOccupiedRooms();
        return ResponseEntity.ok(ApiResponse.success(rooms, "Occupied rooms retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/occupied/by-department-id")
    @Operation(summary = "Get occupied rooms by department ID")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getOccupiedRoomsByDepartmentId(@RequestParam Long departmentId) {
        List<RoomResponseDto> rooms = roomService.getOccupiedRoomsByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(rooms, "Occupied rooms by department ID retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/by-department-id")
    @Operation(summary = "Get active rooms by department ID")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getActiveRoomsByDepartmentId(@RequestParam Long departmentId) {
        List<RoomResponseDto> rooms = roomService.getActiveRoomsByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(rooms, "Active rooms by department ID retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Capacity Filters ==========

    @GetMapping("/capacity")
    @Operation(summary = "Get rooms by exact capacity")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getRoomsByCapacity(@RequestParam int capacity) {
        List<RoomResponseDto> rooms = roomService.getRoomsByCapacity(capacity);
        return ResponseEntity.ok(ApiResponse.success(rooms, "Rooms with exact capacity retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/capacity")
    @Operation(summary = "Get active rooms by exact capacity")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getActiveRoomsByCapacity(@RequestParam int capacity) {
        List<RoomResponseDto> rooms = roomService.getActiveRoomsByCapacity(capacity);
        return ResponseEntity.ok(ApiResponse.success(rooms, "Active rooms with exact capacity retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/capacity/less-than")
    @Operation(summary = "Get rooms with capacity less than given value")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getRoomsByCapacityLessThan(@RequestParam int capacity) {
        List<RoomResponseDto> rooms = roomService.getRoomsByCapacityLessThan(capacity);
        return ResponseEntity.ok(ApiResponse.success(rooms, "Rooms with capacity less than " + capacity + " retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/capacity/less-than")
    @Operation(summary = "Get active rooms with capacity less than given value")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getActiveRoomsByCapacityLessThan(@RequestParam int capacity) {
        List<RoomResponseDto> rooms = roomService.getActiveRoomsByCapacityLessThan(capacity);
        return ResponseEntity.ok(ApiResponse.success(rooms, "Active rooms with capacity less than " + capacity + " retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/capacity/greater-than")
    @Operation(summary = "Get rooms with capacity greater than given value")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getRoomsByCapacityGreaterThan(@RequestParam int capacity) {
        List<RoomResponseDto> rooms = roomService.getRoomsByCapacityGreaterThan(capacity);
        return ResponseEntity.ok(ApiResponse.success(rooms, "Rooms with capacity greater than " + capacity + " retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/capacity/greater-than")
    @Operation(summary = "Get active rooms with capacity greater than given value")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getActiveRoomsByCapacityGreaterThan(@RequestParam int capacity) {
        List<RoomResponseDto> rooms = roomService.getActiveRoomsByCapacityGreaterThan(capacity);
        return ResponseEntity.ok(ApiResponse.success(rooms, "Active rooms with capacity greater than " + capacity + " retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/capacity/range")
    @Operation(summary = "Get rooms by capacity range")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getRoomsByCapacityRange(@RequestParam int minCapacity, @RequestParam int maxCapacity) {
        List<RoomResponseDto> rooms = roomService.getRoomsByCapacityRange(minCapacity, maxCapacity);
        return ResponseEntity.ok(ApiResponse.success(rooms, "Rooms with capacity between " + minCapacity + " and " + maxCapacity + " retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active/capacity/range")
    @Operation(summary = "Get active rooms by capacity range")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getActiveRoomsByCapacityRange(@RequestParam int minCapacity, @RequestParam int maxCapacity) {
        List<RoomResponseDto> rooms = roomService.getActiveRoomsByCapacityRange(minCapacity, maxCapacity);
        return ResponseEntity.ok(ApiResponse.success(rooms, "Active rooms with capacity between " + minCapacity + " and " + maxCapacity + " retrieved successfully", HttpStatus.OK.value()));
    }

    // ========== Advanced Search ==========

    @GetMapping("/search")
    @Operation(summary = "Dynamic search rooms with optional filters")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> searchRooms(
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Boolean isOccupied) {
        List<RoomResponseDto> rooms = roomService.searchRooms(roomNumber, departmentId, isOccupied);
        return ResponseEntity.ok(ApiResponse.success(rooms, "Search completed successfully", HttpStatus.OK.value()));
    }

    // ========== Statistics ==========

    @GetMapping("/count")
    @Operation(summary = "Count total rooms")
    public ResponseEntity<ApiResponse<Long>> countAllRooms() {
        Long count = roomService.countAllRooms();
        return ResponseEntity.ok(ApiResponse.success(count, "Total rooms count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/available")
    @Operation(summary = "Count available (empty) rooms")
    public ResponseEntity<ApiResponse<Long>> countAvailableRooms() {
        Long count = roomService.countAvailableRooms();
        return ResponseEntity.ok(ApiResponse.success(count, "Available rooms count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/occupied")
    @Operation(summary = "Count occupied rooms")
    public ResponseEntity<ApiResponse<Long>> countOccupiedRooms() {
        Long count = roomService.countOccupiedRooms();
        return ResponseEntity.ok(ApiResponse.success(count, "Occupied rooms count retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/count/room-by-department-id")
    @Operation(summary = "Count rooms by department ID")
    public ResponseEntity<ApiResponse<Long>> countRoomsByDepartmentId(@RequestParam Long departmentId) {
        Long count = roomService.countRoomsByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success(count, "Rooms count by department ID retrieved successfully", HttpStatus.OK.value()));
    }
}