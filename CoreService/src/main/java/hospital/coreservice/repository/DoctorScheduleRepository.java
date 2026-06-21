package hospital.coreservice.repository;

import hospital.coreservice.model.DoctorSchedule;
import hospital.coreservice.model.enums.DayOfWeek;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorScheduleRepository extends BaseEntityRepository<DoctorSchedule, Long> {

    List<DoctorSchedule> findByDoctorId(Long doctorId);

    List<DoctorSchedule> findByDayOfWeek(DayOfWeek dayOfWeek);

    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);

    @Query("SELECT ds FROM doctorScheduleEntity ds WHERE ds.doctor.id = :doctorId AND ds.isActive = true")
    List<DoctorSchedule> findActiveByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT ds FROM doctorScheduleEntity ds WHERE ds.doctor.id = :doctorId AND ds.isActive = false")
    List<DoctorSchedule> findInactiveByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT ds FROM doctorScheduleEntity ds WHERE ds.doctor.id = :doctorId AND ds.dayOfWeek = :dayOfWeek AND ds.isActive = true")
    Optional<DoctorSchedule> findActiveByDoctorIdAndDayOfWeek(@Param("doctorId") Long doctorId, @Param("dayOfWeek") DayOfWeek dayOfWeek);

    List<DoctorSchedule> findByStartTimeAfter(LocalTime time);

    List<DoctorSchedule> findActiveByStartTimeAfter(LocalTime time);

    List<DoctorSchedule> findByEndTimeBefore(LocalTime time);

    List<DoctorSchedule> findActiveByEndTimeBefore(LocalTime time);

    boolean existsByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);

    @Query("SELECT d FROM doctorScheduleEntity d WHERE d.id = :id AND d.isActive = true")
    Optional<DoctorSchedule> findActiveById(@Param("id") Long id);

    @Query("SELECT d FROM doctorScheduleEntity d WHERE d.isActive = true")
    List<DoctorSchedule> findAllActive();

    @Query("SELECT d FROM doctorScheduleEntity d WHERE d.isActive = false")
    List<DoctorSchedule> findAllInactive();

    @Modifying
    @Query("UPDATE doctorScheduleEntity d SET d.isActive = false WHERE d.id = :id")
    void deactivate(@Param("id") Long id);

    @Modifying
    @Query("UPDATE doctorScheduleEntity d SET d.isActive = true WHERE d.id = :id")
    void activate(@Param("id") Long id);
}
