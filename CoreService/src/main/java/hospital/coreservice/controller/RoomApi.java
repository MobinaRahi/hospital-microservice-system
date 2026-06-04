package hospital.coreservice.controller;

import hospital.coreservice.dto.api.ApiResponse;
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

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
@Tag(name = "Room Management", description = "Room CRUD and management APIs")
public class RoomApi {

    private final RoomService roomService;

    // ========== Core Operations ==========

    @PostMapping
    @Operation(summary = "Create a new room")
    public ResponseEntity<ApiResponse> createRoom(@Valid @RequestBody RoomCreateDto roomCreateDto) {
        RoomResponseDto created = roomService.createRoom(roomCreateDto);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Room created successfully")
                .data(created)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a room by ID")
    public ResponseEntity<ApiResponse> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomUpdateDto updateDto) {
        RoomResponseDto updated = roomService.updateRoom(id, updateDto);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Room updated successfully")
                .data(updated)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/deactivate/{id}")
    @Operation(summary = "Deactivate a room (soft delete)")
    public ResponseEntity<ApiResponse> deactivateRoom(@PathVariable Long id) {
        roomService.deactivateRoom(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Room deactivated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/activate/{id}")
    @Operation(summary = "Activate a room")
    public ResponseEntity<ApiResponse> activateRoom(@PathVariable Long id) {
        roomService.activateRoom(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Room activated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Occupancy Management ==========

    @PatchMapping("/occupy/{id}")
    @Operation(summary = "Mark room as occupied")
    public ResponseEntity<ApiResponse> occupyRoom(@PathVariable Long id) {
        roomService.occupyRoom(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Room occupied successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/free/{id}")
    @Operation(summary = "Mark room as free")
    public ResponseEntity<ApiResponse> freeRoom(@PathVariable Long id) {
        roomService.freeRoom(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Room freed successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Patient Assignment ==========

    @PatchMapping("/assign/{roomId}")
    @Operation(summary = "Assign a patient to a room")
    public ResponseEntity<ApiResponse> assignPatientToRoom(@RequestParam Long patientId, @PathVariable Long roomId) {
        roomService.assignPatientToRoom(patientId, roomId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patient assigned to room successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/remove/{roomId}")
    @Operation(summary = "Remove a patient from a room")
    public ResponseEntity<ApiResponse> removePatientFromRoom(@RequestParam Long patientId, @PathVariable Long roomId) {
        roomService.removePatientFromRoom(patientId, roomId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Patient removed from room successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Basic Retrieval ==========

    @GetMapping("/{id}")
    @Operation(summary = "Get room by ID")
    public ResponseEntity<ApiResponse> getRoomById(@PathVariable Long id) {
        RoomResponseDto room = roomService.getRoomById(id);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Room retrieved successfully")
                .data(room)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-room-number")
    @Operation(summary = "Get room by room number")
    public ResponseEntity<ApiResponse> getRoomByRoomNumber(@RequestParam String roomNumber) {
        RoomResponseDto room = roomService.getRoomByRoomNumber(roomNumber);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Room retrieved by number successfully")
                .data(room)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-department-id")
    @Operation(summary = "Get rooms by department ID")
    public ResponseEntity<ApiResponse> getRoomsByDepartmentId(@RequestParam Long departmentId) {
        List<RoomResponseDto> rooms = roomService.getRoomsByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Rooms retrieved by department ID successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current-patient/{roomId}")
    @Operation(summary = "Get current patients of a room")
    public ResponseEntity<ApiResponse> getCurrentPatientsOfRoom(@PathVariable Long roomId) {
        List<PatientResponseDto> patients = roomService.getCurrentPatientsOfRoom(roomId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Current patients retrieved successfully")
                .data(patients)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all rooms")
    public ResponseEntity<ApiResponse> getAllRooms() {
        List<RoomResponseDto> rooms = roomService.getAllRooms();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("All rooms retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Occupancy Status Filters ==========

    @GetMapping("/available")
    @Operation(summary = "Get all available (empty) rooms")
    public ResponseEntity<ApiResponse> getAvailableRooms() {
        List<RoomResponseDto> rooms = roomService.getAvailableRooms();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Available rooms retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/occupied")
    @Operation(summary = "Get all occupied rooms")
    public ResponseEntity<ApiResponse> getOccupiedRooms() {
        List<RoomResponseDto> rooms = roomService.getOccupiedRooms();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Occupied rooms retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/occupied/by-department-id")
    @Operation(summary = "Get occupied rooms by department ID")
    public ResponseEntity<ApiResponse> getOccupiedRoomsByDepartmentId(@RequestParam Long departmentId) {
        List<RoomResponseDto> rooms = roomService.getOccupiedRoomsByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Occupied rooms by department ID retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/by-department-id")
    @Operation(summary = "Get active rooms by department ID")
    public ResponseEntity<ApiResponse> getActiveRoomsByDepartmentId(@RequestParam Long departmentId) {
        List<RoomResponseDto> rooms = roomService.getActiveRoomsByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active rooms by department ID retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Capacity Filters ==========

    @GetMapping("/capacity")
    @Operation(summary = "Get rooms by exact capacity")
    public ResponseEntity<ApiResponse> getRoomsByCapacity(@RequestParam int capacity) {
        List<RoomResponseDto> rooms = roomService.getRoomsByCapacity(capacity);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Rooms with exact capacity retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/capacity")
    @Operation(summary = "Get active rooms by exact capacity")
    public ResponseEntity<ApiResponse> getActiveRoomsByCapacity(@RequestParam int capacity) {
        List<RoomResponseDto> rooms = roomService.getActiveRoomsByCapacity(capacity);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active rooms with exact capacity retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/capacity/less-than")
    @Operation(summary = "Get rooms with capacity less than given value")
    public ResponseEntity<ApiResponse> getRoomsByCapacityLessThan(@RequestParam int capacity) {
        List<RoomResponseDto> rooms = roomService.getRoomsByCapacityLessThan(capacity);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Rooms with capacity less than " + capacity + " retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/capacity/less-than")
    @Operation(summary = "Get active rooms with capacity less than given value")
    public ResponseEntity<ApiResponse> getActiveRoomsByCapacityLessThan(@RequestParam int capacity) {
        List<RoomResponseDto> rooms = roomService.getActiveRoomsByCapacityLessThan(capacity);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active rooms with capacity less than " + capacity + " retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/capacity/greater-than")
    @Operation(summary = "Get rooms with capacity greater than given value")
    public ResponseEntity<ApiResponse> getRoomsByCapacityGreaterThan(@RequestParam int capacity) {
        List<RoomResponseDto> rooms = roomService.getRoomsByCapacityGreaterThan(capacity);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Rooms with capacity greater than " + capacity + " retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/capacity/greater-than")
    @Operation(summary = "Get active rooms with capacity greater than given value")
    public ResponseEntity<ApiResponse> getActiveRoomsByCapacityGreaterThan(@RequestParam int capacity) {
        List<RoomResponseDto> rooms = roomService.getActiveRoomsByCapacityGreaterThan(capacity);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active rooms with capacity greater than " + capacity + " retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/capacity/range")
    @Operation(summary = "Get rooms by capacity range")
    public ResponseEntity<ApiResponse> getRoomsByCapacityRange(@RequestParam int minCapacity, @RequestParam int maxCapacity) {
        List<RoomResponseDto> rooms = roomService.getRoomsByCapacityRange(minCapacity, maxCapacity);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Rooms with capacity between " + minCapacity + " and " + maxCapacity + " retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/capacity/range")
    @Operation(summary = "Get active rooms by capacity range")
    public ResponseEntity<ApiResponse> getActiveRoomsByCapacityRange(@RequestParam int minCapacity, @RequestParam int maxCapacity) {
        List<RoomResponseDto> rooms = roomService.getActiveRoomsByCapacityRange(minCapacity, maxCapacity);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Active rooms with capacity between " + minCapacity + " and " + maxCapacity + " retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Advanced Search ==========

    @GetMapping("/search")
    @Operation(summary = "Dynamic search rooms with optional filters")
    public ResponseEntity<ApiResponse> searchRooms(
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Boolean isOccupied) {
        List<RoomResponseDto> rooms = roomService.searchRooms(roomNumber, departmentId, isOccupied);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Search completed successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    // ========== Statistics ==========

    @GetMapping("/count")
    @Operation(summary = "Count total rooms")
    public ResponseEntity<ApiResponse> countAllRooms() {
        Long count = roomService.countAllRooms();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Total rooms count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/available")
    @Operation(summary = "Count available (empty) rooms")
    public ResponseEntity<ApiResponse> countAvailableRooms() {
        Long count = roomService.countAvailableRooms();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Available rooms count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/occupied")
    @Operation(summary = "Count occupied rooms")
    public ResponseEntity<ApiResponse> countOccupiedRooms() {
        Long count = roomService.countOccupiedRooms();
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Occupied rooms count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/room-by-department-id")
    @Operation(summary = "Count rooms by department ID")
    public ResponseEntity<ApiResponse> countRoomsByDepartmentId(@RequestParam Long departmentId) {
        Long count = roomService.countRoomsByDepartmentId(departmentId);
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Rooms count by department ID retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }
}
