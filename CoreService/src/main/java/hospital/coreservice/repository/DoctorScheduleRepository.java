package hospital.coreservice.repository;

import hospital.coreservice.model.DoctorSchedule;
import hospital.coreservice.model.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for DoctorSchedule entity.
 *
 * @author Mobina
 */
@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    // ========== Basic Queries ==========

    /**
     * Find all schedules for a specific doctor
     */
    List<DoctorSchedule> findByDoctorId(Long doctorId);

    /**
     * Find all schedules for a specific day of week
     */
    List<DoctorSchedule> findByDayOfWeek(DayOfWeek dayOfWeek);
    /**
     * Find schedule for a doctor on a specific day of week
     */
    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);

    // ========== Active/Inactive Queries ==========

    /**
     * Find all active schedules
     */
    @Query("SELECT ds FROM doctorScheduleEntity ds WHERE ds.isActive = true")
    List<DoctorSchedule> findAllActive();

    /**
     * Find all inactive schedules
     */
    @Query("SELECT ds FROM doctorScheduleEntity ds WHERE ds.isActive = false")
    List<DoctorSchedule> findAllInactive();

    /**
     * Find active schedules for a specific doctor
     */
    @Query("SELECT ds FROM doctorScheduleEntity ds WHERE ds.doctor.id = :doctorId AND ds.isActive = true")
    List<DoctorSchedule> findActiveByDoctorId(@Param("doctorId") Long doctorId);

    /**
     * Find inactive schedules for a specific doctor
     */
    @Query("SELECT ds FROM doctorScheduleEntity ds WHERE ds.doctor.id = :doctorId AND ds.isActive = false")
    List<DoctorSchedule> findInactiveByDoctorId(@Param("doctorId") Long doctorId);

    /**
     * Find active schedule for a doctor on a specific day of week
     */
    @Query("SELECT ds FROM doctorScheduleEntity ds WHERE ds.doctor.id = :doctorId AND ds.dayOfWeek = :dayOfWeek AND ds.isActive = true")
    Optional<DoctorSchedule> findActiveByDoctorIdAndDayOfWeek(@Param("doctorId") Long doctorId, @Param("dayOfWeek") DayOfWeek dayOfWeek);

    // ========== Update Operations ==========

    /**
     * Deactivate schedule (set isActive = false)
     */
    @Modifying
    @Query("UPDATE doctorScheduleEntity d SET d.isActive = false WHERE d.id = :scheduleId")
    void deactivate(@Param("scheduleId") Long scheduleId);

    /**
     * Activate schedule (set isActive = true)
     */
    @Modifying
    @Query("UPDATE doctorScheduleEntity d SET d.isActive = true WHERE d.id = :scheduleId")
    void activate(@Param("scheduleId") Long scheduleId);

    // ========== Time Based Queries ==========

    /**
     * Find schedules where start time is after given time
     */
    List<DoctorSchedule> findByStartTimeAfter(LocalTime time);
    List<DoctorSchedule> findActiveByStartTimeAfter(LocalTime time);

    /**
     * Find schedules where end time is before given time
     */
    List<DoctorSchedule> findByEndTimeBefore(LocalTime time);
    List<DoctorSchedule> findActiveByEndTimeBefore(LocalTime time);

    // ========== Existence Check ==========

    /**
     * Check if schedule exists for a doctor on a specific day
     */
    boolean existsByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);


}
