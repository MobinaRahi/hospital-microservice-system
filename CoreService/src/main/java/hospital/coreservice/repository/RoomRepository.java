package hospital.coreservice.repository;

import hospital.coreservice.model.Patient;
import hospital.coreservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Room entity.
 *
 * @author Mobina
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // ========== Find by Relationships ==========

    /**
     * Find all rooms in a specific department
     */
    List<Room> findByDepartmentId(Long departmentId);
    List<Room> findByDepartmentIdAndActiveTrue(Long departmentId);

    /**
     * Find room by unique room number
     */
    Optional<Room> findByRoomNumber(String roomNumber);

    // ========== Find by Capacity ==========

    /**
     * Find rooms with exact capacity
     */
    List<Room> findByCapacity(int capacity);
    List<Room> findByCapacityAndActiveTrue(int capacity);

    /**
     * Find rooms with capacity greater than given value
     */
    List<Room> findByCapacityGreaterThan(int capacity);
    List<Room> findByCapacityGreaterThanAndActiveTrue(int capacity);

    /**
     * Find rooms with capacity less than given value
     */
    List<Room> findByCapacityLessThan(int capacity);
    List<Room> findByCapacityLessThanAndActiveTrue(int capacity);

    /**
     * Find rooms with capacity between min and max
     */
    List<Room> findByCapacityBetween(int capacityStart, int capacityEnd);
    List<Room> findByCapacityBetweenAndActiveTrue(int capacityStart, int capacityEnd);

    // ========== Find by Occupancy ==========

    /**
     * Find all occupied rooms
     */
    @Query("SELECT r FROM roomEntity r WHERE r.isOccupied = true AND r.isActive=true")
    List<Room> findOccupiedRooms();

    /**
     * Find occupied rooms in a specific department
     */
    @Query("SELECT r FROM roomEntity r WHERE r.department.id = :departmentId AND r.isOccupied = true AND r.isActive=true")
    List<Room> findOccupiedRoomsByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * Find all empty (non-occupied) rooms
     */
    @Query("SELECT r FROM roomEntity r WHERE r.isOccupied = false AND r.isActive=true")
    List<Room> findEmptyRooms();

    // ========== Advanced Search ==========

    /**
     * Dynamic search with optional filters
     */
    @Query("SELECT r FROM roomEntity r WHERE " +
            "(:roomNumber IS NULL OR r.roomNumber = :roomNumber) AND " +
            "(:departmentId IS NULL OR r.department.id = :departmentId) AND " +
            "(:isOccupied IS NULL OR r.isOccupied = :isOccupied)")
    List<Room> searchRooms(@Param("roomNumber") String roomNumber,
                           @Param("departmentId") Long departmentId,
                           @Param("isOccupied") Boolean isOccupied);

    // ========== Patient Queries ==========

    /**
     * Find all patients currently assigned to a room
     */
    @Query("SELECT p FROM patientEntity p WHERE p.currentRoom.id = :roomId")
    List<Patient> findPatientsByRoomId(@Param("roomId") Long roomId);

    // ========== Update Operations ==========

    /**
     * Mark room as occupied
     */
    @Modifying
    @Query("UPDATE roomEntity r SET r.isOccupied = true WHERE r.id = :roomId AND r.isActive=true")
    void occupy(@Param("roomId") Long roomId);

    /**
     * Mark room as free (unoccupied)
     */
    @Modifying
    @Query("UPDATE roomEntity r SET r.isOccupied = false WHERE r.id = :roomId AND r.isActive=true ")
    void free(@Param("roomId") Long roomId);

    /**
     * Soft delete (deactivate) room
     */
    @Modifying
    @Query("UPDATE roomEntity r SET r.isActive = false WHERE r.id = :roomId")
    void deactivate(@Param("roomId") Long roomId);

    /**
     * Activate room
     */
    @Modifying
    @Query("UPDATE roomEntity r SET r.isActive = true WHERE r.id = :roomId")
    void activate(@Param("roomId") Long roomId);

    // ========== Count Methods ==========

    /**
     * Count available (empty) rooms
     */
    @Query("SELECT COUNT(r) FROM roomEntity r WHERE r.isOccupied = false")
    long countAvailableRooms();

    /**
     * Count occupied rooms
     */
    @Query("SELECT COUNT(r) FROM roomEntity r WHERE r.isOccupied = true")
    long countOccupiedRooms();

    /**
     * Count rooms by department ID
     */
    @Query("SELECT COUNT(r) FROM roomEntity r WHERE r.department.id = :departmentId")
    long countRoomsByDepartmentId(@Param("departmentId") Long departmentId);

    // ========== Existence Check ==========

    /**
     * Check if a room number already exists
     */
    boolean existsByRoomNumber(String roomNumber);
}