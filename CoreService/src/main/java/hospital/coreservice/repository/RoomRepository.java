package hospital.coreservice.repository;

import hospital.coreservice.model.Patient;
import hospital.coreservice.model.Room;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends BaseEntityRepository<Room, Long> {

    List<Room> findByDepartmentId(Long departmentId);
    @Query("SELECT r FROM roomEntity r WHERE r.department.id = :departmentId AND r.isActive = true")
    List<Room> findByDepartmentIdAndActiveTrue(@Param("departmentId") Long departmentId);

    Optional<Room> findByRoomNumber(String roomNumber);

    List<Room> findByCapacity(int capacity);
    @Query("SELECT r FROM roomEntity r WHERE r.capacity = :capacity AND r.isActive = true")
    List<Room> findByCapacityAndActiveTrue(@Param("capacity") int capacity);

    List<Room> findByCapacityGreaterThan(int capacity);
    @Query("SELECT r FROM roomEntity r WHERE r.capacity > :capacity AND r.isActive = true")
    List<Room> findByCapacityGreaterThanAndActiveTrue(@Param("capacity") int capacity);

    List<Room> findByCapacityLessThan(int capacity);
    @Query("SELECT r FROM roomEntity r WHERE r.capacity < :capacity AND r.isActive = true")
    List<Room> findByCapacityLessThanAndActiveTrue(@Param("capacity") int capacity);

    List<Room> findByCapacityBetween(int capacityStart, int capacityEnd);
    @Query("SELECT r FROM roomEntity r WHERE r.capacity BETWEEN :capacityStart AND :capacityEnd AND r.isActive = true")
    List<Room> findByCapacityBetweenAndActiveTrue(@Param("capacityStart") int capacityStart, @Param("capacityEnd") int capacityEnd);

    @Query("SELECT r FROM roomEntity r WHERE r.isOccupied = true AND r.isActive=true")
    List<Room> findOccupiedRooms();

    @Query("SELECT r FROM roomEntity r WHERE r.department.id = :departmentId AND r.isOccupied = true AND r.isActive=true")
    List<Room> findOccupiedRoomsByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT r FROM roomEntity r WHERE r.isOccupied = false AND r.isActive=true")
    List<Room> findEmptyRooms();

    @Query("SELECT r FROM roomEntity r WHERE " +
            "(:roomNumber IS NULL OR r.roomNumber = :roomNumber) AND " +
            "(:departmentId IS NULL OR r.department.id = :departmentId) AND " +
            "(:isOccupied IS NULL OR r.isOccupied = :isOccupied)")
    List<Room> searchRooms(@Param("roomNumber") String roomNumber,
                           @Param("departmentId") Long departmentId,
                           @Param("isOccupied") Boolean isOccupied);

    @Query("SELECT p FROM patientEntity p WHERE p.currentRoom.id = :roomId")
    List<Patient> findPatientsByRoomId(@Param("roomId") Long roomId);

    @Modifying
    @Query("UPDATE roomEntity r SET r.isOccupied = true WHERE r.id = :roomId AND r.isActive=true")
    void occupy(@Param("roomId") Long roomId);

    @Modifying
    @Query("UPDATE roomEntity r SET r.isOccupied = false WHERE r.id = :roomId AND r.isActive=true ")
    void free(@Param("roomId") Long roomId);

    @Query("SELECT COUNT(r) FROM roomEntity r WHERE r.isOccupied = false")
    long countAvailableRooms();

    @Query("SELECT COUNT(r) FROM roomEntity r WHERE r.isOccupied = true")
    long countOccupiedRooms();

    @Query("SELECT COUNT(r) FROM roomEntity r WHERE r.department.id = :departmentId")
    long countRoomsByDepartmentId(@Param("departmentId") Long departmentId);

    boolean existsByRoomNumber(String roomNumber);

    @Query("SELECT r FROM roomEntity r WHERE r.id = :id AND r.isActive = true")
    Optional<Room> findActiveById(@Param("id") Long id);

    @Query("SELECT r FROM roomEntity r WHERE r.isActive = true")
    List<Room> findAllActive();

    @Query("SELECT r FROM roomEntity r WHERE r.isActive = false")
    List<Room> findAllInactive();

    @Modifying
    @Query("UPDATE roomEntity r SET r.isActive = false WHERE r.id = :id")
    void deactivate(@Param("id") Long id);

    @Modifying
    @Query("UPDATE roomEntity r SET r.isActive = true WHERE r.id = :id")
    void activate(@Param("id") Long id);
}