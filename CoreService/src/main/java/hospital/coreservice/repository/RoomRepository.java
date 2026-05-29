package hospital.coreservice.repository;

import com.hospital.coreService.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Room entity.
 * Provides database operations for room management.
 *
 * @author Mobina
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // ========== Find by Relationships ==========

    /**
     * Finds all rooms in a specific department.
     *
     * @param departmentId the department ID
     * @return list of rooms in the department
     */
    List<Room> findByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * Finds a room by its unique room number.
     *
     * @param roomNumber the room number (e.g., "A-101")
     * @return Optional containing the room if found
     */
    Optional<Room> findByRoomNumber(@Param("roomNumber") String roomNumber);

    // ========== Find by Capacity ==========

    /**
     * Finds rooms with exact capacity.
     *
     * @param capacity the number of beds
     * @return list of rooms with the given capacity
     */
    List<Room> findByCapacity(@Param("capacity") int capacity);

    /**
     * Finds rooms with capacity greater than the specified value.
     *
     * @param capacity the minimum capacity
     * @return list of rooms with larger capacity
     */
    List<Room> findByCapacityGreaterThan(@Param("capacity") int capacity);

    /**
     * Finds rooms with capacity less than the specified value.
     *
     * @param capacity the maximum capacity
     * @return list of rooms with smaller capacity
     */
    List<Room> findByCapacityLessThan(@Param("capacity") int capacity);

    // ========== Find by Occupancy Status ==========

    /**
     * Finds all currently occupied rooms.
     *
     * @return list of occupied rooms
     */
    @Query("SELECT r FROM roomEntity r WHERE r.isOccupied = true")
    List<Room> findOccupiedRooms();

    /**
     * Finds all currently empty (non-occupied) rooms.
     *
     * @return list of empty rooms
     */
    @Query("SELECT r FROM roomEntity r WHERE r.isOccupied = false")
    List<Room> findEmptyRooms();

    // ========== Update Operations ==========

    /**
     * Marks a room as occupied.
     *
     * @param roomId the ID of the room to occupy
     */
    @Modifying
    @Query("UPDATE roomEntity r SET r.isOccupied = true WHERE r.id = :roomId")
    void occupy(@Param("roomId") Long roomId);

    /**
     * Marks a room as free (unoccupied).
     *
     * @param roomId the ID of the room to free
     */
    @Modifying
    @Query("UPDATE roomEntity r SET r.isOccupied = false WHERE r.id = :roomId")
    void free(@Param("roomId") Long roomId);

    Long countByDepartmentId(Long departmentId);
}
