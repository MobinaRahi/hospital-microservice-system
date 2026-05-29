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

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    // ========== Basic Queries ==========

    /**
     * Find all schedules for a specific doctor
     */
    List<DoctorSchedule> findByDoctorId(Long doctorId);

    /**
     * Find schedule for a specific day of week
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

    // ========== Update Operations (Status Management) ==========

    /**
     * Deactivate a doctorSchedule (sets isActive = false).
     *
     * @param doctorScheduleId the ID of the doctorSchedule to deactivate
     */
    @Modifying
    @Query("UPDATE doctorScheduleEntity d SET d.isActive = false WHERE d.id = :doctorScheduleId")
    void deactivate(@Param("doctorScheduleId") Long doctorScheduleId);

    /**
     * Activate a doctorSchedule (sets isActive = true).
     *
     * @param doctorScheduleId the ID of the doctor to activate
     */
    @Modifying
    @Query("UPDATE doctorScheduleEntity d SET d.isActive = true WHERE d.id = :doctorScheduleId")
    void activate(@Param("doctorScheduleId") Long doctorScheduleId);

    /**
     * Find active schedules for a specific doctor
     */
    @Query("SELECT ds FROM doctorScheduleEntity ds WHERE ds.doctor.id = :doctorId AND ds.isActive = true")
    List<DoctorSchedule> findActiveByDoctorId(@Param("doctorId") Long doctorId);

    // ========== Time Based Queries ==========

    /**
     * Find schedules where start time is after given time
     */
    List<DoctorSchedule> findByStartTimeAfter(LocalTime time);

    /**
     * Find schedules where end time is before given time
     */
    List<DoctorSchedule> findByEndTimeBefore(LocalTime time);

    @Query("SELECT ds FROM doctorScheduleEntity ds WHERE ds.doctor.id = :doctorId AND ds.dayOfWeek = :dayOfWeek")
    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeek(@Param("doctorId") Long doctorId,
                                                        @Param("dayOfWeek") String dayOfWeek);
}
