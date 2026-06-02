package hospital.coreservice.service.imp;

import hospital.coreservice.dto.patient.PatientResponseDto;
import hospital.coreservice.dto.room.RoomCreateDto;
import hospital.coreservice.dto.room.RoomResponseDto;
import hospital.coreservice.dto.room.RoomUpdateDto;
import hospital.coreservice.exception.patient.PatientNotFoundException;
import hospital.coreservice.exception.room.DuplicateRoomNumberException;
import hospital.coreservice.exception.room.RoomNotAvailableException;
import hospital.coreservice.exception.room.RoomNotFoundException;
import hospital.coreservice.mapper.PatientMapper;
import hospital.coreservice.mapper.RoomMapper;
import hospital.coreservice.model.Patient;
import hospital.coreservice.model.Room;
import hospital.coreservice.repository.PatientRepository;
import hospital.coreservice.repository.RoomRepository;
import hospital.coreservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of RoomService.
 *
 * @author Mobina
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    // ========== Core Operations ==========

    @Override
    @Transactional
    public RoomResponseDto createRoom(RoomCreateDto createDto) {
        log.info("Creating new room with number: {}", createDto.getRoomNumber());

        if (!isRoomNumberUnique(createDto.getRoomNumber())) {
            throw new DuplicateRoomNumberException(createDto.getRoomNumber());
        }

        Room room = roomMapper.toEntity(createDto);
        room.setActive(true);
        room.setOccupied(false);
        Room saved = roomRepository.save(room);
        log.info("Room created with id: {}", saved.getId());
        return roomMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public RoomResponseDto updateRoom(Long roomId, RoomUpdateDto updateDto) {
        log.info("Updating room with id: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));
        roomMapper.updateEntity(room, updateDto);
        Room updated = roomRepository.save(room);
        log.info("Room updated with id: {}", updated.getId());
        return roomMapper.toResponseDto(updated);
    }

    // ========== Basic Retrieval ==========

    @Override
    public List<RoomResponseDto> getAllRooms() {
        log.debug("Fetching all rooms");
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponseDto getRoomById(Long roomId) {
        log.debug("Fetching room by id: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));
        return roomMapper.toResponseDto(room);
    }

    @Override
    public RoomResponseDto getRoomByRoomNumber(String roomNumber) {
        log.debug("Fetching room by number: {}", roomNumber);
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> RoomNotFoundException.byRoomNumber(roomNumber));
        return roomMapper.toResponseDto(room);
    }

    @Override
    public List<RoomResponseDto> getRoomsByDepartmentId(Long departmentId) {
        log.debug("Fetching rooms by department id: {}", departmentId);
        return roomRepository.findByDepartmentId(departmentId)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDto> getActiveRoomsByDepartmentId(Long departmentId) {
        log.debug("Fetching ActiveRooms by department id: {}", departmentId);
        return roomRepository.findByDepartmentIdAndActiveTrue(departmentId)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Search & Filter ==========

    @Override
    public List<RoomResponseDto> getAvailableRooms() {
        log.debug("Fetching available rooms");
        return roomRepository.findEmptyRooms()
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDto> getOccupiedRooms() {
        log.debug("Fetching occupied rooms");
        return roomRepository.findOccupiedRooms()
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDto> getOccupiedRoomsByDepartmentId(Long departmentId) {
        log.debug("Fetching occupied rooms by department id: {}", departmentId);
        return roomRepository.findOccupiedRoomsByDepartmentId(departmentId)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDto> getRoomsByCapacityRange(int minCapacity, int maxCapacity) {
        log.debug("Fetching rooms by capacity between {} and {}", minCapacity, maxCapacity);
        return roomRepository.findByCapacityBetween(minCapacity, maxCapacity)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDto> getActiveRoomsByCapacityRange(int minCapacity, int maxCapacity) {
        log.debug("Fetching activeRooms by capacity between {} and {}", minCapacity, maxCapacity);
        return roomRepository.findByCapacityBetweenAndActiveTrue(minCapacity, maxCapacity)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDto> getRoomsByCapacity(int capacity) {
        log.debug("Fetching rooms by exact capacity: {}", capacity);
        return roomRepository.findByCapacity(capacity)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDto> getActiveRoomsByCapacity(int capacity) {
        log.debug("Fetching activeRooms by exact capacity: {}", capacity);
        return roomRepository.findByCapacityAndActiveTrue(capacity)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDto> getRoomsByCapacityGreaterThan(int capacity) {
        log.debug("Fetching rooms with capacity > {}", capacity);
        return roomRepository.findByCapacityGreaterThan(capacity)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDto> getActiveRoomsByCapacityGreaterThan(int capacity) {
        log.debug("Fetching activeRooms with capacity > {}", capacity);
        return roomRepository.findByCapacityGreaterThanAndActiveTrue(capacity)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDto> getRoomsByCapacityLessThan(int capacity) {
        log.debug("Fetching rooms with capacity < {}", capacity);
        return roomRepository.findByCapacityLessThan(capacity)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDto> getActiveRoomsByCapacityLessThan(int capacity) {
        log.debug("Fetching activeRooms with capacity < {}", capacity);
        return roomRepository.findByCapacityLessThanAndActiveTrue(capacity)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDto> searchRooms(String roomNumber, Long departmentId, Boolean isOccupied) {
        log.debug("Searching rooms with filters: number={}, dept={}, occupied={}", roomNumber, departmentId, isOccupied);
        return roomRepository.searchRooms(roomNumber, departmentId, isOccupied)
                .stream()
                .map(roomMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Patient Management ==========

    @Override
    @Transactional
    public void assignPatientToRoom(Long roomId, Long patientId) {
        log.info("Assigning patient {} to room {}", patientId, roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> PatientNotFoundException.byId(patientId));

        if (!room.hasAvailableCapacity()) {
            throw new RoomNotAvailableException(roomId);
        }

        room.addPatient(patient);
        roomRepository.save(room);
        log.info("Patient {} assigned to room {}", patientId, roomId);
    }

    @Override
    @Transactional
    public void removePatientFromRoom(Long roomId, Long patientId) {
        log.info("Removing patient {} from room {}", patientId, roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> PatientNotFoundException.byId(patientId));
        room.removePatient(patient);
        roomRepository.save(room);
        log.info("Patient {} removed from room {}", patientId, roomId);
    }

    @Override
    public List<PatientResponseDto> getCurrentPatientsOfRoom(Long roomId) {
        log.debug("Fetching current patients of room {}", roomId);
        return roomRepository.findPatientsByRoomId(roomId)
                .stream()
                .map(patientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Occupancy Management ==========

    @Override
    @Transactional
    public void occupyRoom(Long roomId) {
        log.info("Manually occupying room with id: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));
        if (room.isOccupied()) {
            log.warn("Room {} is already occupied", roomId);
            throw new IllegalStateException("Room is already occupied");
        }
        roomRepository.occupy(roomId);
    }

    @Override
    @Transactional
    public void freeRoom(Long roomId) {
        log.info("Manually freeing room with id: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));
        if (!room.isOccupied()) {
            log.warn("Room {} is already free", roomId);
            throw new IllegalStateException("Room is already free");
        }
        roomRepository.free(roomId);
    }
    // ========== Statistics ==========

    @Override
    public Long countAvailableRooms() {
        log.debug("Counting available rooms");
        return roomRepository.countAvailableRooms();
    }

    @Override
    public Long countOccupiedRooms() {
        log.debug("Counting occupied rooms");
        return roomRepository.countOccupiedRooms();
    }

    @Override
    public Long countRoomsByDepartmentId(Long departmentId) {
        log.debug("Counting rooms by department id: {}", departmentId);
        return roomRepository.countRoomsByDepartmentId(departmentId);
    }

    @Override
    public Long countAllRooms() {
        log.debug("Counting all rooms");
        return roomRepository.count();
    }

    // ========== Validation ==========

    @Override
    public boolean isRoomNumberUnique(String roomNumber) {
        log.debug("Checking uniqueness of room number: {}", roomNumber);
        return !roomRepository.existsByRoomNumber(roomNumber);
    }

    // ========== Status Management ==========

    @Override
    public void activateRoom(Long roomId) {
        log.info("Activating room id: {}", roomId);
        roomRepository.findById(roomId)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));
        roomRepository.activate(roomId);
    }

    @Override
    public void deactivateRoom(Long roomId) {
        roomRepository.findById(roomId)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));
        roomRepository.deactivate(roomId);
    }
}